package com.kuehne_nagel.city_list.application.transport.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageableRequest<T> extends BaseRequest {

    @NotNull
    private Integer pageNumber;

    @NotNull
    private Integer pageSize;

    private Boolean isDescending;

    @NotNull
    private Boolean isPaginated;

    private T paginationDto;

}
