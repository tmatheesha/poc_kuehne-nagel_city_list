package com.kuehne_nagel.city_list.application.controller;

import jakarta.servlet.http.HttpServletRequest;

import com.kuehne_nagel.city_list.application.transport.request.PageableRequest;
import com.kuehne_nagel.city_list.application.transport.request.UpdateRequest;
import com.kuehne_nagel.city_list.application.transport.request.UploadCityRequest;
import com.kuehne_nagel.city_list.application.transport.response.ListResponse;
import com.kuehne_nagel.city_list.application.transport.response.SingleResponse;
import com.kuehne_nagel.city_list.application.transport.response.UploadCityResponse;
import com.kuehne_nagel.city_list.domain.entities.dto.CityDto;
import com.kuehne_nagel.city_list.domain.entities.dto.CityPaginationDto;
import com.kuehne_nagel.city_list.domain.exception.DomainException;
import com.kuehne_nagel.city_list.domain.services.CityMgtService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller implementation of City management.
 * 1. Retrieve City List
 * 2. Upload City List
 * 3. Edit City
 */
@RestController
@RequestMapping( "${base-url.context}/city" )
@CrossOrigin( origins = "*",
        allowedHeaders = "*" )
public class CityController extends BaseController {

    @Autowired
    private CityMgtService cityMgtService;

    @ApiOperation( value = "upload a set of Cities" )
    @PostMapping( value = "/upload",
            produces = MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8" )
    public ResponseEntity < UploadCityResponse > uploadCities(
            @Validated
            @RequestBody( required = true )
            @RequestParam( "file" )
                    MultipartFile file,
            UploadCityRequest uploadCityRequest, HttpServletRequest request) throws DomainException {
        setLogIdentifier(request);
        setRequestId(uploadCityRequest.getRequestHeader());
        UploadCityResponse importProductResponse = cityMgtService.importProduct(uploadCityRequest, file);
        return new ResponseEntity <>(importProductResponse, HttpStatus.OK);
    }

    @PostMapping( value = "/pages",
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity < ListResponse > getPageableCityList(
            @Validated
            @RequestBody( required = true )
                    PageableRequest < CityPaginationDto > pageableRequest, HttpServletRequest request) throws DomainException {
        setLogIdentifier(request);
        setRequestId(pageableRequest.getRequestHeader());
        ListResponse < CityDto > cityDtoListResponse = cityMgtService.getPageableRecordList(pageableRequest);
        return new ResponseEntity <>(cityDtoListResponse, HttpStatus.OK);
    }

    @PostMapping( value = "/update",
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity < SingleResponse > updateCity(
            @Validated
            @RequestBody( required = true )
                    UpdateRequest < CityDto > dtoUpdateRequest, HttpServletRequest request) throws DomainException {
        setLogIdentifier(request);
        setRequestId(dtoUpdateRequest.getRequestHeader());
        SingleResponse < CityDto > categoryV2DtoSingleResponse = cityMgtService.updateRecord(dtoUpdateRequest);
        return new ResponseEntity <>(categoryV2DtoSingleResponse, HttpStatus.OK);
    }

}
