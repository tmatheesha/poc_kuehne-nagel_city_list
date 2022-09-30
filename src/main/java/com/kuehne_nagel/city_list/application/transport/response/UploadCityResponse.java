package com.kuehne_nagel.city_list.application.transport.response;

import java.util.List;

import com.kuehne_nagel.city_list.domain.entities.dto.CityDto;
import lombok.Data;

@Data
public class UploadCityResponse extends BaseResponse {

    private List < CityDto > cityDtoList;

}
