package com.kuehne_nagel.city_list.application.transport.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthenticateRequest extends BaseRequest {

    @NotNull(message = "userName is null")
    private String userName;

    @NotNull(message = "password is null")
    private String password;
}
