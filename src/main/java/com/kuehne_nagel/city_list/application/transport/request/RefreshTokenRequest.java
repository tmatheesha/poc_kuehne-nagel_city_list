package com.kuehne_nagel.city_list.application.transport.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest extends BaseRequest {
    @Valid
    @NotNull(message = "refreshToken must not be null")
    private String refreshToken;
}
