package com.kuehne_nagel.city_list.domain.services.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.kuehne_nagel.city_list.application.transport.request.PageableRequest;
import com.kuehne_nagel.city_list.application.transport.request.UpdateRequest;
import com.kuehne_nagel.city_list.application.transport.request.UploadCityRequest;
import com.kuehne_nagel.city_list.application.transport.response.ListResponse;
import com.kuehne_nagel.city_list.application.transport.response.SingleResponse;
import com.kuehne_nagel.city_list.application.transport.response.UploadCityResponse;
import com.kuehne_nagel.city_list.domain.assemblers.Assembler;
import com.kuehne_nagel.city_list.domain.assemblers.impl.CityUploadProjectionAssembler;
import com.kuehne_nagel.city_list.domain.entities.dto.CityDto;
import com.kuehne_nagel.city_list.domain.entities.City;
import com.kuehne_nagel.city_list.domain.entities.dto.CityPaginationDto;
import com.kuehne_nagel.city_list.domain.entities.projections.CityUploadProjection;
import com.kuehne_nagel.city_list.domain.exception.DomainException;
import com.kuehne_nagel.city_list.domain.services.CityMgtService;
import com.kuehne_nagel.city_list.domain.services.ExternalizedFileIntegrationService;
import com.kuehne_nagel.city_list.domain.util.Constants;
import com.kuehne_nagel.city_list.domain.entities.enums.ErrorCodes;
import com.kuehne_nagel.city_list.external.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service implementation of {@link CityMgtService}
 */
@Service
public class CityMgtServiceImpl extends BaseCrudService implements CityMgtService {

    private static final String CITY = "City";

    @Autowired
    private ExternalizedFileIntegrationService csvFileIntegrationService;

    @Autowired
    private CityUploadProjectionAssembler cityUploadProjectionAssembler;

    @Autowired
    private Assembler cityUpdateAssembler;

    @Autowired
    private CityRepository cityRepository;

    /**
     * Upload set of {@link com.kuehne_nagel.city_list.domain.entities.City}'s.
     *
     * @param uploadCityRequest
     * @param file
     * @return
     */
    @Override
    public UploadCityResponse importProduct(UploadCityRequest uploadCityRequest, MultipartFile file) throws DomainException {
        UploadCityResponse uploadCityResponse = new UploadCityResponse();
        List < CityUploadProjection > cityUploadProjectionList = csvFileIntegrationService.importFile(uploadCityRequest.getExternalFileTypes(), file, CityUploadProjection.class);
        List < CityDto > cityDtoList = new LinkedList <>();
        List < City > convertedCityList = convertFromExportProjection(cityUploadProjectionList);
        for ( City city : convertedCityList ) {
            cityDtoList.add(( CityDto ) cityUpdateAssembler.toDto(super.create(city, cityRepository, CITY, super.getContextMapFromRequestHeader(uploadCityRequest.getRequestHeader()))));
        }
        uploadCityResponse.setCityDtoList(cityDtoList);
        uploadCityResponse.setResponseHeader(createResponseHeader(uploadCityRequest.getRequestHeader(), "Successfully uploaded the Cities"));
        return uploadCityResponse;
    }

    /**
     * Get Pageable {@link City} List
     * @param pageableRequest
     * @return
     */
    @Override
    public ListResponse < CityDto > getPageableRecordList(PageableRequest < CityPaginationDto > pageableRequest) throws DomainException {
        ListResponse < CityDto > cityDtoListResponse = new ListResponse <>();
        Pageable pageable = PageRequest.of(pageableRequest.getPageNumber() > 0 ? pageableRequest.getPageNumber() -1: pageableRequest.getPageNumber(), pageableRequest.getPageSize());
        List < CityDto > cityDtoList = new LinkedList <>();
        if( StringUtils.hasLength(pageableRequest.getPaginationDto().getSearchBy())) {
            for(Object city: cityRepository.findDistinctByNameContaining(pageableRequest.getPaginationDto().getSearchBy(),pageable)){
                cityDtoList.add(( CityDto ) cityUpdateAssembler.toDto(city));
            }
            cityDtoListResponse.setNoOfItems(cityRepository.countByNameContaining(pageableRequest.getPaginationDto().getSearchBy()));
        } else {
            for(Object city: cityRepository.findAll(pageable)){
                cityDtoList.add(( CityDto ) cityUpdateAssembler.toDto(city));
            }
            cityDtoListResponse.setNoOfItems(cityRepository.count());
        }
        cityDtoListResponse.setDtoList(cityDtoList);
        cityDtoListResponse.setResponseHeader(createResponseHeader(pageableRequest.getRequestHeader(), "Successfully Retrieved the City Page"));
        return cityDtoListResponse;
    }

    /**
     * update {@link City}
     *
     * @param dtoUpdateRequest
     * @return
     */
    @Override
    public SingleResponse < CityDto > updateRecord(UpdateRequest < CityDto > dtoUpdateRequest) throws DomainException {
        City city = ( City ) cityUpdateAssembler.fromDto(dtoUpdateRequest.getUpdateDto());
        SingleResponse < CityDto > cityDtoSingleResponse = new SingleResponse <>();
        City createdScreensV2 = ( City ) super.update(dtoUpdateRequest.getId(), city, cityRepository,CITY, super.getContextMapFromRequestHeader(dtoUpdateRequest.getRequestHeader()));
        CityDto cityDto = ( CityDto ) cityUpdateAssembler.toDto(createdScreensV2);
        cityDtoSingleResponse.setDto(cityDto);
        cityDtoSingleResponse.setResponseHeader(createResponseHeader(dtoUpdateRequest.getRequestHeader(), "Successfully updated Category"));
        return cityDtoSingleResponse;
    }

    /**
     * Convert from {@link CityUploadProjection} to {@link City}
     *
     * @param cityUploadProjectionList
     * @return
     * @throws DomainException
     */
    private List < City > convertFromExportProjection(List < CityUploadProjection > cityUploadProjectionList) throws DomainException {
        List < City > cityList = new LinkedList <>();
        for ( CityUploadProjection cityUploadProjection : cityUploadProjectionList ) {
            cityList.add(( City ) cityUploadProjectionAssembler.fromDto(cityUploadProjection));
        }
        return cityList;
    }

    /**
     * Validate {@link City}
     * @param model
     * @param modelName
     * @return
     * @throws DomainException
     */
    @Override
    public void validateForCreate(Object model, String modelName) throws DomainException {
        if ( Objects.isNull(model) ) {
            logError(Constants.CRUD_METHOD_INIT_LOG_MSG_IS_NULL, CITY);
            throw new DomainException(String.format(Constants.CRUD_METHOD_INIT_MSG_IS_NULL, CITY), HttpStatus.BAD_REQUEST.toString());
        }
        City city = ( City ) model;
        if ( Boolean.TRUE.equals(cityRepository.existsByName(city.getName())) ) {
            logError(ErrorCodes.CITY_ERROR_CITY_NAME_ALREADY_EXISTS.getMessage(), city.getName());
            throw new DomainException(ErrorCodes.CITY_ERROR_CITY_NAME_ALREADY_EXISTS.getDescription(), ErrorCodes.CITY_ERROR_CITY_NAME_ALREADY_EXISTS.getCode());
        }
    }

    @Override
    public void validateForUpdate(Object model, String modelName, Long id) throws DomainException {
        if ( Objects.isNull(model) ) {
            logError(Constants.CRUD_METHOD_INIT_LOG_MSG_IS_NULL, CITY);
            throw new DomainException(String.format(Constants.CRUD_METHOD_INIT_MSG_IS_NULL, CITY), HttpStatus.BAD_REQUEST.toString());
        }
        City city = ( City ) model;
        if ( Boolean.TRUE.equals(cityRepository.existsByNameAndIdNot(city.getName(),id)) ) {
            logError(ErrorCodes.CITY_ERROR_CITY_NAME_ALREADY_EXISTS.getMessage(), city.getName());
            throw new DomainException(ErrorCodes.CITY_ERROR_CITY_NAME_ALREADY_EXISTS.getDescription(), ErrorCodes.CITY_ERROR_CITY_NAME_ALREADY_EXISTS.getCode());
        }
    }

}
