package com.kuehne_nagel.city_list.domain.services;

import com.kuehne_nagel.city_list.application.transport.request.PageableRequest;
import com.kuehne_nagel.city_list.application.transport.request.UpdateRequest;
import com.kuehne_nagel.city_list.application.transport.request.UploadCityRequest;
import com.kuehne_nagel.city_list.application.transport.response.ListResponse;
import com.kuehne_nagel.city_list.application.transport.response.SingleResponse;
import com.kuehne_nagel.city_list.application.transport.response.UploadCityResponse;
import com.kuehne_nagel.city_list.domain.entities.dto.CityDto;
import com.kuehne_nagel.city_list.domain.entities.dto.CityPaginationDto;
import com.kuehne_nagel.city_list.domain.exception.DomainException;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service for management activities of {@link com.kuehne_nagel.city_list.domain.entities.City}
 *
 *
 */
public interface CityMgtService extends CommonService{

    UploadCityResponse importProduct(UploadCityRequest uploadCityRequest, MultipartFile file) throws DomainException;

    ListResponse< CityDto> getPageableRecordList(PageableRequest< CityPaginationDto > pageableRequest) throws DomainException;

    SingleResponse< CityDto> updateRecord(UpdateRequest< CityDto> dtoUpdateRequest) throws DomainException;

}
