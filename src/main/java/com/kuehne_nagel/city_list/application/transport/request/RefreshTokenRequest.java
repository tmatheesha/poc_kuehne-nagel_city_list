package com.kuehne_nagel.city_list.application.transport.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest extends BaseRequest{
    @Valid
    @NotNull(message = "refreshToken must not be null")
    private String refreshToken;
}
