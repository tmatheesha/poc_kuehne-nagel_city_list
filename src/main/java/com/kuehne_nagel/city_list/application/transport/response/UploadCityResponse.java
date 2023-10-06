package com.kuehne_nagel.city_list.application.transport.response;

import com.kuehne_nagel.city_list.domain.entities.dto.CityDto;
import lombok.Data;

import java.util.List;

@Data
public class UploadCityResponse extends BaseResponse {

    private List<CityDto> cityDtoList;

}
