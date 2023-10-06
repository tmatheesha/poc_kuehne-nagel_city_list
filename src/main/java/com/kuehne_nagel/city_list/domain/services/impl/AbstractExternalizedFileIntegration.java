package com.kuehne_nagel.city_list.domain.services.impl;

import com.kuehne_nagel.city_list.domain.entities.enums.ErrorCodes;
import com.kuehne_nagel.city_list.domain.exception.DomainException;
import com.kuehne_nagel.city_list.domain.services.ExternalizedFileIntegrationService;
import com.kuehne_nagel.city_list.domain.util.ExternalFileTypes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

/**
 * Abstract service layer for resolving the file type.
 */
public abstract class AbstractExternalizedFileIntegration implements ExternalizedFileIntegrationService {

    /**
     * Import a file.
     *
     * @param externalFileTypes
     * @param multipartFile
     * @param classType
     * @return
     * @throws DomainException
     */
    @Override
    public List importFile(ExternalFileTypes externalFileTypes, MultipartFile multipartFile, Class classType) throws DomainException {
        if (Objects.isNull(externalFileTypes)) {
            logger.error(ErrorCodes.CITY_ERROR_EXT_FILE_TYPE_NULL.getMessage());
            throw new DomainException(ErrorCodes.CITY_ERROR_EXT_FILE_TYPE_NULL.getMessage(), ErrorCodes.CITY_ERROR_EXT_FILE_TYPE_NULL.getCode());
        }
        if (ExternalFileTypes.CSV.equals(externalFileTypes)) {
            this.validateFile(multipartFile);
            return this.importFile(multipartFile, classType);
        } else {
            logger.error(ErrorCodes.CITY_ERROR_EXT_FILE_TYPE_NOT_IMPL.getDescription(), externalFileTypes);
            throw new DomainException(ErrorCodes.CITY_ERROR_EXT_FILE_TYPE_NOT_IMPL.getMessage(), ErrorCodes.CITY_ERROR_EXT_FILE_TYPE_NOT_IMPL.getCode());
        }
    }

    public abstract List importFile(MultipartFile file, Class classType) throws DomainException;

    public abstract Boolean validateFile(MultipartFile file) throws DomainException;

}
