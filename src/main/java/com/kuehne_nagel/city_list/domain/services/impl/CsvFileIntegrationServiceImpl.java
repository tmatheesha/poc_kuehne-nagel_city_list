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
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
            logError("Error occurred in converting multipart file to a file, {}", e);
            throw new DomainException("Please check the file and try again", HttpStatus.BAD_REQUEST.toString());
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
            logError("Error occurred in reading the file {}", e);
            throw new DomainException(String.format("Please check the file and try again, error occurred: %s", e.getMessage()), HttpStatus.BAD_REQUEST.toString());
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
            logError("file is null");
            throw new DomainException("file is null", HttpStatus.BAD_REQUEST.toString());
        }
        if ( Boolean.FALSE.equals(CSV_FILE_INTEGRATE_SERVICE_EXPORT_FILE_NAME_PURE_EXTENSION.equals(FilenameUtils.getExtension(file.getOriginalFilename()))) ) {
            logError("file type {} is not supported, use {} file type", FilenameUtils.getExtension(file.getOriginalFilename()), CSV_FILE_INTEGRATE_SERVICE_EXPORT_FILE_NAME_EXTENSION);
            throw new DomainException(String.format("file type %s is not supported, use %s file type", FilenameUtils.getExtension(file.getOriginalFilename()), CSV_FILE_INTEGRATE_SERVICE_EXPORT_FILE_NAME_EXTENSION), HttpStatus.BAD_REQUEST.toString());
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
