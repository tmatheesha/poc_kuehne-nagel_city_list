package com.kuehne_nagel.city_list.domain.entities.projections;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityUploadProjection {

    @CsvBindByName(column = "name")
    private String name;

    @CsvBindByName(column = "picture")
    private String picture;
}
