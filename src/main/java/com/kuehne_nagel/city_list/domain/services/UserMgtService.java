package com.kuehne_nagel.city_list.domain.services;

import com.kuehne_nagel.city_list.domain.entities.dto.UserDetailDto;
import com.kuehne_nagel.city_list.domain.exception.DomainException;

/**
 * Interface for user management functionality
 */
public interface UserMgtService extends CrudService<UserDetailDto> {

    UserDetailDto getUserByEmail(String email) throws DomainException;

}
