package com.kuehne_nagel.city_list.domain.assemblers.impl;

import java.util.Base64;
import java.util.Objects;

import com.kuehne_nagel.city_list.domain.assemblers.Assembler;
import com.kuehne_nagel.city_list.domain.entities.City;
import com.kuehne_nagel.city_list.domain.entities.projections.CityUploadProjection;
import com.kuehne_nagel.city_list.domain.exception.DomainException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * {@link CityUploadProjection} and {@link City} Assembler
 */
@Service
public class CityUploadProjectionAssembler implements Assembler< City, CityUploadProjection > {

    @Override
    public City fromDto(CityUploadProjection dto) throws DomainException {
        City city = new City();
        if ( dto != null ) {
            city = map(dto, City.class);
            if ( Objects.nonNull(city.getId()) && city.getId() == 0L ) {
                city.setId(null);
            }
            if( StringUtils.hasLength(dto.getPicture()) ) {
                city.setPicture(Base64.getDecoder().decode(dto.getPicture().getBytes()));
            }
        } else {
            log("CityUploadProjection is null");
        }
        return city;
    }

    @Override
    public CityUploadProjection toDto(City model) throws DomainException {
        CityUploadProjection cityUploadProjection = new CityUploadProjection();
        if ( model != null ) {
            cityUploadProjection = map(model, CityUploadProjection.class);
            if(model.getPicture() != null)
                cityUploadProjection.setPicture(Base64.getEncoder().encodeToString(model.getPicture()));
        } else {
            log("City is null");
        }
        return cityUploadProjection;
    }

}
