package com.kuehne_nagel.city_list.domain.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.kuehne_nagel.city_list.application.transport.request.RequestHeader;
import com.kuehne_nagel.city_list.domain.entities.AbstractEntity;
import com.kuehne_nagel.city_list.domain.exception.DomainException;
import com.kuehne_nagel.city_list.domain.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

/**
 * Crud Service
 *
 */
public abstract class BaseCrudService {

    Logger logger = LoggerFactory.getLogger(BaseCrudService.class);

    public abstract void validateForCreate(Object model, String modelName) throws DomainException;

    public abstract void validateForUpdate(Object model, String modelName,Long id) throws DomainException;

    /**
     * @param modeObject
     * @param repository
     * @return
     * @throws DomainException
     */
    public Object create(AbstractEntity modeObject, JpaRepository repository, String modelName, Map contextMap) throws DomainException {
        logBaseCrud(Constants.CRUD_METHOD_INIT_LOG_MSG_CREATE);
        this.validateForCreate(modeObject, modelName);
        MDC.setContextMap(contextMap);
        setUserDetails(modeObject);
        return repository.save(modeObject);
    }

    /**
     * Update Entity.
     *
     * @param id
     * @param modelObject
     * @param contextMap
     * @return
     * @throws DomainException
     */
    public Object update(Long id, AbstractEntity modelObject, JpaRepository repository, String modelName, Map contextMap) throws DomainException {
        logBaseCrud(Constants.CRUD_METHOD_INIT_LOG_MSG_UPDATE, id);
        validateObjectNull(modelObject, String.format("%s param" , modelObject.getClass()));
        validateWhetherIdExists(id, modelObject.getClass(), repository);
        validateForUpdate(modelObject,modelName,id);
        modelObject.setId(id);
        MDC.setContextMap(contextMap);
        setUserDetails(modelObject);
        return repository.save(modelObject);
    }
    /**
     * Check if the object is null and throw {@link DomainException}
     *
     * @param object
     * @param name
     * @throws DomainException
     */
    public void validateObjectNull(Object object, String name) throws DomainException {
        if ( Boolean.TRUE.equals(object == null) ) {
            logger.error("{} is null", name);
            throw new DomainException(String.format("%s is null", name), HttpStatus.BAD_REQUEST.toString());
        }
    }

    /**
     * Check whether the Id exists and throw {@link DomainException} if not.
     * @param id
     * @param modelClass
     * @throws DomainException
     */
    public void validateWhetherIdExists(Long id, Class modelClass, JpaRepository repository) throws  DomainException {
        if ( Boolean.FALSE.equals(repository.existsById(id)) ) {
            logger.error("id: {} doesn't exist for {}", id, modelClass.getSimpleName());
            throw new DomainException(String.format("id: %s doesn't exist for %s", id, modelClass.getSimpleName()), HttpStatus.BAD_REQUEST.toString());
        }
    }

    private void logBaseCrud(String logMessage, Object... logParams) {
        if ( logger.isDebugEnabled()) {
            logger.debug(logMessage, logParams);
        }
    }

    /**
     * Setting Last Modified user details
     * @param modeObject
     */
    private void setUserDetails(AbstractEntity modeObject) {
        logBaseCrud("setUserDetails method started");
        modeObject.setLastUpdatedUserId(MDC.get(Constants.USER_ID));
        modeObject.setLastUpdatedUserName(MDC.get(Constants.USER_NAME));
    }

    /**
     * Get the ContextMap from {@link RequestHeader}
     *
     * @param requestHeader
     * @return
     */
    public Map getContextMapFromRequestHeader(RequestHeader requestHeader) {
        Map < String, String > contextMapForTransaction = new HashMap <>();
        contextMapForTransaction.put(Constants.USER_ID, requestHeader.getUserId());
        contextMapForTransaction.put(Constants.USER_NAME, requestHeader.getUserName());
        return contextMapForTransaction;
    }
}
