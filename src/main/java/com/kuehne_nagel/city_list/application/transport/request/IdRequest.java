package com.kuehne_nagel.city_list.application.transport.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties( ignoreUnknown = true )
public class IdRequest extends BaseRequest{
    @NotNull(message = "id is null")
    @Valid
    private Long id;
}
