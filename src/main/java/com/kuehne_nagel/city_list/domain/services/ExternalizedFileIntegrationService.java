package com.kuehne_nagel.city_list.domain.services;

import com.kuehne_nagel.city_list.domain.exception.DomainException;
import com.kuehne_nagel.city_list.domain.util.ExternalFileTypes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExternalizedFileIntegrationService extends CommonService {

    List importFile(ExternalFileTypes externalFileTypes, MultipartFile multipartFile, Class classType) throws DomainException;

}
