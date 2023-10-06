package com.kuehne_nagel.city_list.domain.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuehne_nagel.city_list.application.transport.request.RequestHeader;
import com.kuehne_nagel.city_list.application.transport.response.ResponseHeader;
import com.kuehne_nagel.city_list.domain.entities.enums.ErrorCodes;
import com.kuehne_nagel.city_list.domain.exception.DomainException;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Objects;

public interface CommonService {

    Logger logger = LoggerFactory.getLogger(CommonService.class);
    ObjectMapper mapper = new ObjectMapper();

    ModelMapper modelMapper = new ModelMapper();

    default <D> D map(Object sourceEntity, Class<D> destinationType) throws DomainException {
        try {
            return modelMapper.map(sourceEntity, destinationType);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            logger.error("Source : {}, to destination by class type : {}, mapping exception : ", sourceEntity, destinationType, e);
            throw new DomainException(String.format(ErrorCodes.CITY_ERROR_MAPPING_ERROR.getMessage(), e.getMessage()), ErrorCodes.CITY_ERROR_MAPPING_ERROR.getCode());
        }
    }

    /**
     * Log messages
     *
     * @param logMessage
     * @param logParams
     */
    default void log(String logMessage, Object... logParams) {
        if (logger.isDebugEnabled()) {
            logger.debug(logMessage, logParams);
        }
    }

    /**
     * Log messages
     *
     * @param logMessage
     * @param logParams
     */
    default void logError(String logMessage, Object... logParams) {
        if (logger.isErrorEnabled()) {
            logger.debug(logMessage, logParams);
        }
    }

    /**
     * Create Response Header.
     *
     * @param requestHeader
     * @param responseDescription
     * @return
     */
    default ResponseHeader createResponseHeader(RequestHeader requestHeader, String responseDescription) {
        ResponseHeader responseHeader = new ResponseHeader();
        if (Objects.nonNull(requestHeader)) {
            responseHeader.setRequestId(requestHeader.getRequestId());
        }
        responseHeader.setResponseCode("200");
        responseHeader.setResponseDesc(responseDescription);
        responseHeader.setResponseDescDisplay(responseDescription);
        responseHeader.setTimestamp(LocalDateTime.now().toString());
        return responseHeader;
    }
}
