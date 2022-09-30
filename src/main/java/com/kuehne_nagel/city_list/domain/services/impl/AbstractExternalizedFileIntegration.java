package com.kuehne_nagel.city_list.domain.services.impl;

import java.util.List;
import java.util.Objects;

import com.kuehne_nagel.city_list.domain.exception.DomainException;
import com.kuehne_nagel.city_list.domain.services.ExternalizedFileIntegrationService;
import com.kuehne_nagel.city_list.domain.util.ExternalFileTypes;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

/**
 * Abstract service layer for resolving the file type.
 *
 */
public abstract class AbstractExternalizedFileIntegration implements ExternalizedFileIntegrationService {

    private static final String ABSTRACT_EXTERNALIZED_FILE_INTEGRATION_EXTERNAL_FILE_TYPE_IS_NULL = "externalFileTypes is null";

    private static final String ABSTRACT_EXTERNALIZED_FILE_INTEGRATION_EXTERNAL_FILE_TYPE_NOT_IMPLEMENTED = "externalFileTypes is not implemented: {}";

    private static final String ABSTRACT_EXTERNALIZED_FILE_INTEGRATION_EXTERNAL_FILE_TYPE_NOT_IMPLEMENTED_EXP = "Please check the file and try again";

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
        if( Objects.isNull(externalFileTypes) ) {
            logger.error(ABSTRACT_EXTERNALIZED_FILE_INTEGRATION_EXTERNAL_FILE_TYPE_IS_NULL);
            throw new DomainException(ABSTRACT_EXTERNALIZED_FILE_INTEGRATION_EXTERNAL_FILE_TYPE_IS_NULL, HttpStatus.BAD_REQUEST.toString());
        }
        if(ExternalFileTypes.CSV.equals(externalFileTypes)) {
            this.validateFile(multipartFile);
            return this.importFile(multipartFile, classType );
        } else {
            logger.error(ABSTRACT_EXTERNALIZED_FILE_INTEGRATION_EXTERNAL_FILE_TYPE_NOT_IMPLEMENTED,externalFileTypes);
            throw new DomainException(ABSTRACT_EXTERNALIZED_FILE_INTEGRATION_EXTERNAL_FILE_TYPE_NOT_IMPLEMENTED_EXP, HttpStatus.BAD_REQUEST.toString());
        }
    }

    public abstract List importFile(MultipartFile file, Class classType) throws DomainException;

    public abstract Boolean validateFile(MultipartFile file) throws DomainException;

}
