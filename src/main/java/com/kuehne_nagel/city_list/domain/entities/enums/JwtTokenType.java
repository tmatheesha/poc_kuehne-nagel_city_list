package com.kuehne_nagel.city_list.domain.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtTokenType {
    ACCESS_TOKEN(1, "Access Token"), REFRESH_TOKEN(2, "Refresh Token");

    private Integer tokenType;

    private String jwtTokenTypeDescription;
}
