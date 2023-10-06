package com.kuehne_nagel.city_list.domain.assemblers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuehne_nagel.city_list.domain.entities.enums.ErrorCodes;
import com.kuehne_nagel.city_list.domain.exception.DomainException;
import com.kuehne_nagel.city_list.domain.services.CommonService;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;

public interface Assembler<A, B> extends CommonService {

    A fromDto(B dto) throws DomainException;

    B toDto(A model) throws DomainException;

    ObjectMapper mapper = new ObjectMapper();

    ModelMapper modelMapper = new ModelMapper();


    @Override
    default <D> D map(Object sourceEntity, Class<D> destinationType) throws DomainException {
        try {
            return modelMapper.map(sourceEntity, destinationType);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            logger.error("Mapping exception : {} ", e);
            throw new DomainException(String.format(ErrorCodes.CITY_ERROR_MAPPING_ERROR.getMessage(), e.getMessage()), ErrorCodes.CITY_ERROR_MAPPING_ERROR.getCode());
        }
    }


}
