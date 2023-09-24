package com.kuehne_nagel.city_list.application.transport.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties( ignoreUnknown = true )
public class UpdateRequest <T> extends IdRequest {

    @NotNull(message = "updateDto is null")
    @Valid
    private T updateDto;

}
