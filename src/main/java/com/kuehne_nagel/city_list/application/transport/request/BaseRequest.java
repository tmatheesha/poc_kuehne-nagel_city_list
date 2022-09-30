package com.kuehne_nagel.city_list.application.transport.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class BaseRequest {

    @NotNull( message = "requestHeader not found for operation. This action is not allowed" )
    @Valid
    private RequestHeader requestHeader;
}
