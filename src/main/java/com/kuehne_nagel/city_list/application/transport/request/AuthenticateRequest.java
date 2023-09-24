package com.kuehne_nagel.city_list.application.transport.request;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class AuthenticateRequest extends BaseRequest {

    @NotNull(message = "userName is null")
    private String userName;

    @NotNull(message = "password is null")
    private String password;
}
