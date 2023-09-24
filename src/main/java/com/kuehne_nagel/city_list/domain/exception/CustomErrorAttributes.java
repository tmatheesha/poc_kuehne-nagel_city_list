package com.kuehne_nagel.city_list.domain.exception;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;

import com.kuehne_nagel.city_list.application.config.YAMLConfig;
import com.kuehne_nagel.city_list.application.exception.BaseException;
import com.kuehne_nagel.city_list.application.transport.response.ResponseHeader;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    private static final String ERROR_ATTRIBUTES_LOG_MSG_MESSAGE = "message";

    private static final String RESPONSE_HEADER_TEXT = "responseHeader";

    private static final String NO_VALIDATION_MESSAGE_FOR_ERROR = "Validation error with an empty message";



    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    DataSource dataSource;


    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    private DateFormat dateFormatter = new SimpleDateFormat(DATE_TIME_FORMAT);

    @Override
    public Map < String, Object > getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Throwable error = getError(webRequest);
        if ( error == null ) {
            return null;
        }
        switch ( error.getClass().getSimpleName() ) {
            case "DomainException":
            case "AuthorizationException":
                return handleDomainException(( BaseException ) error);
            case "HttpMessageNotReadableException":
                return handleMessageNotReadableError(( HttpMessageNotReadableException ) error);
            case "ProductCatalogDomainException":
                return handleProductCatalogDomainException(( BaseException ) error);
            case "WebClientException":
                return handleRecoverableException(( BaseException ) error, options);
            case "MethodArgumentNotValidException":
                return handleMethodArgumentNotValidException(( MethodArgumentNotValidException ) error);
            default:
                return handleGenericException(error, options);
        }
    }

    private Map < String, Object > handleDomainException(BaseException error) {
        Map < String, Object > errorDetails = new LinkedHashMap <>();
        errorDetails.put(RESPONSE_HEADER_TEXT, this.generateResponseHeaderDto(MDC.get(yamlConfig.getRequestIdKey()), error.getCode(), error.getMessage(), LocalDateTime.now(), error.getMessage()));
        return errorDetails;
    }


    /**
     * Handle Message not readable exceptions.
     *
     * @param error
     * @return
     */
    private Map < String, Object > handleMessageNotReadableError(HttpMessageNotReadableException error) {
        Map < String, Object > errorDetails = new LinkedHashMap <>();
        errorDetails.put(RESPONSE_HEADER_TEXT, this.generateResponseHeaderDto(MDC.get(yamlConfig.getRequestIdKey()), "400", error.getMessage(), LocalDateTime.now(), "Error in Input"));
        return errorDetails;
    }

    /**
     * Handling {@link MethodArgumentNotValidException}
     *
     * @param error
     * @return
     */
    private Map < String, Object > handleMethodArgumentNotValidException(MethodArgumentNotValidException error) {
        Map < String, Object > errorDetails = new LinkedHashMap <>();
        errorDetails.put(RESPONSE_HEADER_TEXT, this.generateResponseHeaderDto(MDC.get(yamlConfig.getRequestIdKey()), "400", getTheValidationErrorMessage(error), LocalDateTime.now(), "validationError"));
        return errorDetails;
    }

    /**
     * Retrieve a the default message from the Message in the exception.
     *
     * @param exception
     * @return
     */
    private String getTheValidationErrorMessage(MethodArgumentNotValidException exception) {
        if ( exception == null ) {
            return NO_VALIDATION_MESSAGE_FOR_ERROR;
        } else {
            Optional < String > message = exception.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).findFirst();
            if ( Boolean.TRUE.equals(message.isPresent()) ) {
                return message.get();
            } else {
                return NO_VALIDATION_MESSAGE_FOR_ERROR;
            }
        }
    }

    /**
     * Handle ProductCatalogDomainException.
     *
     * @param error
     * @return
     */
    private Map < String, Object > handleProductCatalogDomainException(BaseException error) {
        Map < String, Object > errorDetails = new LinkedHashMap <>();
        errorDetails.put(RESPONSE_HEADER_TEXT, this.generateResponseHeaderDto(MDC.get(yamlConfig.getRequestIdKey()), "400", error.getMessage(), LocalDateTime.now(), error.getCode()));
        return errorDetails;
    }

    /**
     * Generate ResponseHeader for non happy paths.
     *
     * @param requestId
     * @param responseCode
     * @param responseDesc
     * @param timeStamp
     * @param responseDescDisplay
     * @return
     */
    private ResponseHeader generateResponseHeaderDto(String requestId, String responseCode, String responseDesc, LocalDateTime timeStamp, String responseDescDisplay) {
        ResponseHeader responseHeader = new ResponseHeader();
        responseHeader.setRequestId(requestId);
        responseHeader.setResponseCode(responseCode);
        responseHeader.setResponseDesc(responseDesc);
        responseHeader.setTimestamp(dateTimeFormatter.format(timeStamp));
        responseHeader.setResponseDescDisplay(responseDescDisplay);
        return responseHeader;
    }

    /**
     * Handle unrecoverable and more generic exceptions
     *
     * @param error exception
     * @return error description
     */
    private Map < String, Object > handleGenericException(Throwable error, ErrorAttributeOptions options) {

        Map < String, Object > errorDetails = new LinkedHashMap <>();

        errorDetails.put("code", "500");
        errorDetails.put("type", error.getClass().getSimpleName());
        errorDetails.put(ERROR_ATTRIBUTES_LOG_MSG_MESSAGE, error.getMessage());

        return errorDetails;
    }

    /**
     * Handle recoverable exceptions
     *
     * @param error exception
     * @param includeStackTrace
     * @return error description
     */
    private Map < String, Object > handleRecoverableException(BaseException error,
                                                              ErrorAttributeOptions includeStackTrace) {

        Map < String, Object > errorDetails = new LinkedHashMap <>();

        errorDetails.put("code", error.getCode() != null ? error.getCode() : "400");
        errorDetails.put("type", error.getClass().getSimpleName());
        errorDetails.put(ERROR_ATTRIBUTES_LOG_MSG_MESSAGE, error.getMessage());


        return errorDetails;
    }

}

