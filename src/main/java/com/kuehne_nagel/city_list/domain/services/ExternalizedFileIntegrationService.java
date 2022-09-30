package com.kuehne_nagel.city_list.domain.services;

import java.util.List;

import com.kuehne_nagel.city_list.domain.exception.DomainException;
import com.kuehne_nagel.city_list.domain.util.ExternalFileTypes;
import org.springframework.web.multipart.MultipartFile;

public interface ExternalizedFileIntegrationService extends CommonService {

    List importFile(ExternalFileTypes externalFileTypes, MultipartFile multipartFile, Class classType) throws DomainException;

}
