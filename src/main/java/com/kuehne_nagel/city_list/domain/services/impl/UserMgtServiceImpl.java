package com.kuehne_nagel.city_list.domain.services.impl;

import com.kuehne_nagel.city_list.application.transport.request.CreateRequest;
import com.kuehne_nagel.city_list.application.transport.request.IdRequest;
import com.kuehne_nagel.city_list.application.transport.request.UpdateRequest;
import com.kuehne_nagel.city_list.application.transport.response.BaseResponse;
import com.kuehne_nagel.city_list.application.transport.response.SingleResponse;
import com.kuehne_nagel.city_list.domain.assemblers.Assembler;
import com.kuehne_nagel.city_list.domain.entities.User;
import com.kuehne_nagel.city_list.domain.entities.dto.UserDetailDto;
import com.kuehne_nagel.city_list.domain.exception.DomainException;
import com.kuehne_nagel.city_list.domain.services.UserMgtService;
import com.kuehne_nagel.city_list.domain.entities.enums.ErrorCodes;
import com.kuehne_nagel.city_list.external.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserMgtServiceImpl implements UserMgtService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Assembler userAssembler;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EncryptionFacilitator encryptionFacilitator;

    @Value( "${user.info.transfer.synchronous.key}" )
    private String secretKey;

    /**
     * Create User.
     *
     * @param createRequest
     * @return
     */
    @Override
    public SingleResponse<UserDetailDto> create(CreateRequest<UserDetailDto> createRequest) throws DomainException {
        User user = (User) userAssembler.fromDto(createRequest.getCreateDto());
        user.setId(null);
        String password = encryptionFacilitator.decrypt(user.getPassword(), secretKey);
        user.setPassword(passwordEncoder.encode(password));
        User savedUser = userRepository.save(user);
        SingleResponse singleResponse = new SingleResponse(userAssembler.toDto(savedUser));
        singleResponse.setResponseHeader(createResponseHeader(createRequest.getRequestHeader(), String.format("Successfully Created the User with id %s",savedUser.getId())));
        return singleResponse;
    }

    /**
     * Update User
     *
     * @param updateRequest
     * @return
     */
    @Override
    public SingleResponse<UserDetailDto> update(UpdateRequest<UserDetailDto> updateRequest) throws DomainException {
        User user = (User) userAssembler.fromDto(updateRequest.getUpdateDto());
        user.setId(updateRequest.getId());
        String password = encryptionFacilitator.decrypt(user.getPassword(), secretKey);
        user.setPassword(passwordEncoder.encode(password));
        User savedUser = userRepository.save(user);
        SingleResponse singleResponse = new SingleResponse(userAssembler.toDto(savedUser));
        singleResponse.setResponseHeader(createResponseHeader(updateRequest.getRequestHeader(), String.format("Successfully Updated the User with id %s",savedUser.getId())));
        return singleResponse;
    }

    /**
     * Delete User.
     *
     * @param idRequest
     * @return
     */
    @Override
    public BaseResponse delete(IdRequest idRequest) throws DomainException {
        Optional<User> savedUserOptional = userRepository.findById(idRequest.getId());
        if (Boolean.FALSE.equals(savedUserOptional.isPresent())) {
            logError(ErrorCodes.CITY_ERROR_NO_USER_ID.getDescription(), idRequest.getId());
            throw new DomainException(String.format(ErrorCodes.CITY_ERROR_NO_USER_ID.getMessage(), idRequest.getId()),ErrorCodes.CITY_ERROR_NO_USER_ID.getCode());
        }
        User savedUser = savedUserOptional.get();
        savedUser.setDeleted(Boolean.TRUE);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResponseHeader(createResponseHeader(idRequest.getRequestHeader(), String.format("There is no User for given Id %l", idRequest.getId())));
        return baseResponse;
    }

    /**
     * Get {@link UserDetailDto} by email
     * @param email
     * @return
     */
    @Override
    public UserDetailDto getUserByEmail(String email) throws DomainException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (Boolean.FALSE.equals(userOptional.isPresent())) {
            logError(ErrorCodes.CITY_ERROR_NO_USER_FOR_EMAIL.getDescription(), email);
            throw new DomainException(String.format(ErrorCodes.CITY_ERROR_NO_USER_FOR_EMAIL.getMessage(), email),ErrorCodes.CITY_ERROR_NO_USER_FOR_EMAIL.getCode());
        }
        return (UserDetailDto) userAssembler.toDto(userOptional.get());
    }
}
