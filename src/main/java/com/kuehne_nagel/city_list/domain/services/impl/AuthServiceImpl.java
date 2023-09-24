package com.kuehne_nagel.city_list.domain.services.impl;

import com.kuehne_nagel.city_list.application.transport.request.AuthenticateRequest;
import com.kuehne_nagel.city_list.application.transport.request.RefreshTokenRequest;
import com.kuehne_nagel.city_list.application.transport.response.AuthResponse;
import com.kuehne_nagel.city_list.domain.entities.dto.UserDetailDto;
import com.kuehne_nagel.city_list.domain.exception.AuthorizationException;
import com.kuehne_nagel.city_list.domain.exception.DomainException;
import com.kuehne_nagel.city_list.domain.services.AuthService;
import com.kuehne_nagel.city_list.domain.services.UserMgtService;
import com.kuehne_nagel.city_list.domain.entities.enums.ErrorCodes;
import com.kuehne_nagel.city_list.domain.entities.enums.JwtTokenType;
import com.kuehne_nagel.city_list.domain.util.Constants;
import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 *Implementation of Authentication Service
 *TODO: use async encryption method, and key rotation.
 * and a password policy
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMgtService userMgtService;

    @Value( "${user.info.transfer.synchronous.key}" )
    private String secretKey;

    @Autowired
    private EncryptionFacilitator encryptionFacilitator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    /**
     * Authenticate user credentials.
     * @param authenticateRequest
     * @return
     */
    @Override
    public AuthResponse authenticate(AuthenticateRequest authenticateRequest) throws  AuthorizationException {
        String userName = null;
        UserDetailDto userDetailDto;
        String password;
        try {
            userName = encryptionFacilitator.decrypt(authenticateRequest.getUserName(), secretKey);


            password = encryptionFacilitator.decrypt(authenticateRequest.getPassword(), secretKey);
        } catch (DomainException e) {
            logger.info(ErrorCodes.CITY_ERROR_DECRYPTING.getDescription(), userName);
            throw new AuthorizationException(String.format(ErrorCodes.CITY_ERROR_DECRYPTING.getMessage(),userName),ErrorCodes.CITY_ERROR_DECRYPTING.getCode());
        }
        try {
            userDetailDto = userMgtService.getUserByEmail(userName);
        } catch (DomainException e) {
            logger.info(ErrorCodes.CITY_ERROR_NO_USER_FOR_EMAIL.getDescription(), userName);
            throw new AuthorizationException(ErrorCodes.CITY_ERROR_NO_USER_FOR_EMAIL.getMessage(),ErrorCodes.CITY_ERROR_NO_USER_FOR_EMAIL.getCode());
        }

        if ( Boolean.FALSE.equals(passwordEncoder.matches(password, userDetailDto.getPassword())) ) {
            logger.info(ErrorCodes.CITY_ERROR_USERNAME_PWD_NOT_MATCHED.getDescription(), userName);
            throw new AuthorizationException(ErrorCodes.CITY_ERROR_USERNAME_PWD_NOT_MATCHED.getMessage(),ErrorCodes.CITY_ERROR_USERNAME_PWD_NOT_MATCHED.getCode());
        }
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAuthorizationToken(jwtService.generateJwtToken(userDetailDto, JwtTokenType.ACCESS_TOKEN));
        authResponse.setRefreshToken(jwtService.generateJwtToken(userDetailDto, JwtTokenType.REFRESH_TOKEN));
        authResponse.setUserId(userDetailDto.getId());
        authResponse.setUserName(userDetailDto.getEmail());
        authResponse.setResponseHeader(createResponseHeader(authenticateRequest.getRequestHeader(), "Successfully authenticated"));
        return authResponse;
    }

    /**
     * Get {@link AuthResponse} from refresh token.
     *
     * @param refreshTokenRequest
     * @return
     */
    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws DomainException {
        Map< String, Object > claims;
        String refreshToken = refreshTokenRequest.getRefreshToken();
        claims = jwtService.getClaimsFromTokenAsMap(refreshToken);
        String email = (String) claims.get(Constants.EMAIL);
        UserDetailDto userDetailDto = userMgtService.getUserByEmail(email);
        if (Boolean.FALSE.equals(jwtService.validateToken(refreshToken, userDetailDto))) {
            logger.error(ErrorCodes.CITY_ERROR_INVALID_TOKEN.getDescription(), "Not a valid refresh token");
            throw new DomainException(String.format(ErrorCodes.CITY_ERROR_INVALID_TOKEN.getDescription(),"Not a valid refresh token"),ErrorCodes.CITY_ERROR_INVALID_TOKEN.getCode());
        }
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAuthorizationToken(jwtService.generateJwtToken(userDetailDto, JwtTokenType.ACCESS_TOKEN));
        authResponse.setRefreshToken(jwtService.generateJwtToken(userDetailDto, JwtTokenType.REFRESH_TOKEN));
        authResponse.setResponseHeader(createResponseHeader(refreshTokenRequest.getRequestHeader(), "Successfully authenticated"));
        return authResponse;
    }
}
