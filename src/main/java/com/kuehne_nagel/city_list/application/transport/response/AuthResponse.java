package com.kuehne_nagel.city_list.application.transport.response;

import lombok.Data;

@Data
public class AuthResponse extends BaseResponse {

    private String authorizationToken;
    private String refreshToken;
    private String userName;
    private Long userId;

}
