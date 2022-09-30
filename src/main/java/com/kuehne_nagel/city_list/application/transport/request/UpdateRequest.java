package com.kuehne_nagel.city_list.application.transport.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties( ignoreUnknown = true )
public class UpdateRequest <T> extends BaseRequest {

    private Long id;

    @NotNull(message = "updateDto is null")
    @Valid
    private T updateDto;

}
