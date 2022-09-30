package com.kuehne_nagel.city_list.domain.assemblers.impl;

import java.util.Base64;
import java.util.Objects;

import com.kuehne_nagel.city_list.domain.assemblers.Assembler;
import com.kuehne_nagel.city_list.domain.entities.dto.CityDto;
import com.kuehne_nagel.city_list.domain.entities.City;
import com.kuehne_nagel.city_list.domain.exception.DomainException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CityUpdateAssembler implements Assembler < City, CityDto > {

    @Override
    public City fromDto(CityDto dto) throws DomainException {
        City city = new City();
        if ( dto != null ) {
            city = map(dto, City.class);
            if ( Objects.nonNull(city.getId()) && city.getId() == 0L ) {
                city.setId(null);
            }
            if ( StringUtils.hasLength(dto.getPicture()) ) {
                city.setPicture(Base64.getDecoder().decode(dto.getPicture().getBytes()));
            }
        } else {
            log("CityUploadProjection is null");
        }
        return city;
    }

    @Override
    public CityDto toDto(City model) throws DomainException {
        CityDto cityDto = new CityDto();
        if ( model != null ) {
            cityDto = map(model, CityDto.class);
            if ( model.getPicture() != null ) {
                cityDto.setPicture(Base64.getEncoder().encodeToString(model.getPicture()));
            }
            cityDto.setId(model.getId());
        } else {
            log("City is null");
        }
        return cityDto;
    }

}
