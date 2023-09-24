package com.kuehne_nagel.city_list.domain.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDto {
    private Long id;
    private String password;
    private String email;
    private LocalDateTime lastLogin;
    private String name;
    private Boolean isActive;
    private String userRole;
}