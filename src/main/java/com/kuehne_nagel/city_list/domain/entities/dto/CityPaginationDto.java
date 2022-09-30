package com.kuehne_nagel.city_list.domain.entities.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties( ignoreUnknown = true )
public class CityPaginationDto {

    private String searchBy;
}
