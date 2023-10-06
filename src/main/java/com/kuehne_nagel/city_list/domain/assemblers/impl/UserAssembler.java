package com.kuehne_nagel.city_list.domain.assemblers.impl;

import com.kuehne_nagel.city_list.domain.assemblers.Assembler;
import com.kuehne_nagel.city_list.domain.entities.User;
import com.kuehne_nagel.city_list.domain.entities.dto.UserDetailDto;
import com.kuehne_nagel.city_list.domain.exception.DomainException;
import org.springframework.stereotype.Service;

@Service
public class UserAssembler implements Assembler<User, UserDetailDto> {
    @Override
    public User fromDto(UserDetailDto dto) throws DomainException {
        User user = new User();
        if (dto != null) {
            user = map(dto, User.class);
        } else {
            log("UserDetailDto is null");
        }
        return user;
    }

    @Override
    public UserDetailDto toDto(User model) throws DomainException {
        UserDetailDto dto = new UserDetailDto();
        if (model != null) {
            dto = map(model, UserDetailDto.class);
        } else {
            log("User is null");
        }
        return dto;
    }
}
