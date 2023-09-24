package com.kuehne_nagel.city_list.application.controller;

import com.kuehne_nagel.city_list.application.transport.request.CreateRequest;
import com.kuehne_nagel.city_list.application.transport.request.IdRequest;
import com.kuehne_nagel.city_list.application.transport.request.UpdateRequest;
import com.kuehne_nagel.city_list.application.transport.response.BaseResponse;
import com.kuehne_nagel.city_list.application.transport.response.SingleResponse;
import com.kuehne_nagel.city_list.domain.entities.dto.UserDetailDto;
import com.kuehne_nagel.city_list.domain.exception.DomainException;
import com.kuehne_nagel.city_list.domain.services.UserMgtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * Controller implementation of User management.
 * 1. Create User
 * 2. check whether user exists
 */
@RestController
@RequestMapping("${base-url.context}/user")
@CrossOrigin(origins = "*",
        allowedHeaders = "*")
public class UserController extends BaseController {


    @Autowired
    private UserMgtService userMgtService;


    @PostMapping(value = "/create",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleResponse> createUser(
            @Validated
            @RequestBody(required = true)
            CreateRequest<UserDetailDto> dtoCreateRequest, HttpServletRequest request) throws DomainException {
        setLogIdentifier(request);
        setRequestId(dtoCreateRequest.getRequestHeader());
        SingleResponse<UserDetailDto> singleResponse = userMgtService.create(dtoCreateRequest);
        return new ResponseEntity<>(singleResponse, HttpStatus.OK);
    }


    @PostMapping(value = "/update",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleResponse> updateUser(
            @Validated
            @RequestBody(required = true)
            UpdateRequest<UserDetailDto> dtoUpdateRequest, HttpServletRequest request) throws DomainException {
        setLogIdentifier(request);
        setRequestId(dtoUpdateRequest.getRequestHeader());
        SingleResponse<UserDetailDto> singleResponse = userMgtService.update(dtoUpdateRequest);
        return new ResponseEntity<>(singleResponse, HttpStatus.OK);
    }


    @PostMapping(value = "/delete",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> deleteUser(
            @Validated
            @RequestBody(required = true)
            IdRequest deleteRequest, HttpServletRequest request) throws DomainException {
        setLogIdentifier(request);
        setRequestId(deleteRequest.getRequestHeader());
        BaseResponse baseResponse = userMgtService.delete(deleteRequest);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}
