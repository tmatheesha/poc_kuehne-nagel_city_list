package com.kuehne_nagel.city_list.domain.assemblers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuehne_nagel.city_list.domain.exception.DomainException;
import com.kuehne_nagel.city_list.domain.services.CommonService;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

public interface Assembler < A, B > extends CommonService {

    A fromDto(B dto) throws DomainException;

    B toDto(A model) throws DomainException;

    ObjectMapper mapper = new ObjectMapper();

    ModelMapper modelMapper = new ModelMapper();

    static final String LOG_MESSAGE_FROM_METHOD = "fromDto method started in {}";
    static final String LOG_MESSAGE_TO_METHOD = "toDto method started in {}";

    @Override
    default < D > D map(Object sourceEntity, Class < D > destinationType) throws DomainException {
        try {
            return modelMapper.map(sourceEntity, destinationType);
        }
        catch ( IllegalArgumentException | ConfigurationException | MappingException e ) {
            logger.error("Mapping exception : {} ",e);
            throw new DomainException(String.format("Mapping exception : %s ", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }
    }

    /**
     * check if the object is null.
     *
     * @param object
     * @param name
     * @throws DomainException
     */
    default void checkIfNull(Object object, String name) throws DomainException {
        if ( object == null ) {
            throw new DomainException(String.format("%s is null", name), HttpStatus.BAD_REQUEST.toString());
        }
    }

    /**
     * Log Assemblers
     *
     * @param isFromMethod
     * @param logMessages
     */
    default void assemblerLog(Boolean isFromMethod, String... logMessages) {
        if ( Boolean.TRUE.equals(logger.isDebugEnabled()) ) {
            if ( Boolean.TRUE.equals(isFromMethod) ) {
                log(LOG_MESSAGE_FROM_METHOD, logMessages);
            } else {
                log(LOG_MESSAGE_TO_METHOD, logMessages);
            }
        }
    }


}
