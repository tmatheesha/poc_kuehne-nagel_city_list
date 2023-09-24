package com.kuehne_nagel.city_list.domain.services.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import com.kuehne_nagel.city_list.domain.exception.DomainException;
import com.kuehne_nagel.city_list.domain.entities.enums.ErrorCodes;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CsvFileIntegrationServiceImpl extends AbstractExternalizedFileIntegration {

    private static final String CSV_FILE_INTEGRATE_SERVICE_EXPORT_FILE_NAME_EXTENSION = ".csv";

    private static final String CSV_FILE_INTEGRATE_SERVICE_EXPORT_FILE_NAME_PURE_EXTENSION = "csv";

    private static final String CSV_FILE_INTEGRATE_SERVICE_DEL = "/";

    @Value( "${importing-path}" )
    private String importingPath;

    /**
     * Import CSV file.
     *
     * @param multipartFile
     * @param classType
     * @return
     * @throws DomainException
     */
    @Override
    public List importFile(MultipartFile multipartFile, Class classType) throws DomainException {
        List outputList = new LinkedList();
        File file;
        Path filePath;
        try {
            String pathStr = getImportFilePath(multipartFile);
            filePath = Paths.get(pathStr);
            Files.createDirectories(filePath.getParent());
            file = new File(pathStr);
            multipartFile.transferTo(file);
        }
        catch ( IOException e ) {
            logError(ErrorCodes.CITY_ERROR_MULTIPART_FILE_CONVERT.getDescription(), e);
            throw new DomainException(ErrorCodes.CITY_ERROR_MULTIPART_FILE_CONVERT.getMessage(), ErrorCodes.CITY_ERROR_MULTIPART_FILE_CONVERT.getCode());
        }
        try (
                BufferedReader reader2 = Files.newBufferedReader(filePath, StandardCharsets.UTF_8) ;
        ) {
            CsvToBean cb = new CsvToBeanBuilder(reader2)
                    .withType(classType)
                    .build();
            outputList.addAll(cb.parse());
        }
        catch ( Exception e ) {
            logError(ErrorCodes.CITY_ERROR_READING_FILE.getDescription(), e);
            throw new DomainException(String.format(ErrorCodes.CITY_ERROR_READING_FILE.getMessage(), e.getMessage()), ErrorCodes.CITY_ERROR_READING_FILE.getCode());
        }
        return outputList;
    }

    /**
     * Validate file.
     *
     * @param file
     * @return
     * @throws DomainException
     */
    @Override
    public Boolean validateFile(MultipartFile file) throws DomainException {
        if ( file == null ) {
            logError(ErrorCodes.CITY_ERROR_FILE_IS_NULL.getMessage());
            throw new DomainException(ErrorCodes.CITY_ERROR_FILE_IS_NULL.getMessage(), ErrorCodes.CITY_ERROR_FILE_IS_NULL.getCode());
        }
        if ( Boolean.FALSE.equals(CSV_FILE_INTEGRATE_SERVICE_EXPORT_FILE_NAME_PURE_EXTENSION.equals(FilenameUtils.getExtension(file.getOriginalFilename()))) ) {
            logError(ErrorCodes.CITY_ERROR_FILE_TYPE_NOT_SUPPORTED.getDescription(), FilenameUtils.getExtension(file.getOriginalFilename()), CSV_FILE_INTEGRATE_SERVICE_EXPORT_FILE_NAME_EXTENSION);
            throw new DomainException(String.format(ErrorCodes.CITY_ERROR_FILE_TYPE_NOT_SUPPORTED.getMessage(), FilenameUtils.getExtension(file.getOriginalFilename()), CSV_FILE_INTEGRATE_SERVICE_EXPORT_FILE_NAME_EXTENSION), ErrorCodes.CITY_ERROR_FILE_TYPE_NOT_SUPPORTED.getCode());
        }
        return Boolean.TRUE;
    }

    /**
     * Get the file path for importing files.
     *
     * @param multipartFile
     * @return
     */
    private String getImportFilePath(MultipartFile multipartFile) {
        StringBuilder stringBuilder = new StringBuilder(importingPath);
        stringBuilder.append(CSV_FILE_INTEGRATE_SERVICE_DEL)
                .append(multipartFile.getOriginalFilename())
                .append(CSV_FILE_INTEGRATE_SERVICE_DEL);
        return stringBuilder.toString();
    }

}
