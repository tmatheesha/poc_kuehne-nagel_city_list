package com.kuehne_nagel.city_list.domain.services;

import com.kuehne_nagel.city_list.application.transport.request.CreateRequest;
import com.kuehne_nagel.city_list.application.transport.request.IdRequest;
import com.kuehne_nagel.city_list.application.transport.request.UpdateRequest;
import com.kuehne_nagel.city_list.application.transport.response.BaseResponse;
import com.kuehne_nagel.city_list.application.transport.response.SingleResponse;
import com.kuehne_nagel.city_list.domain.exception.DomainException;

/**
 * Interface for Create, Update, Delete, List Services
 */
public interface CrudService<T> extends CommonService {

    SingleResponse<T> create(CreateRequest<T> createRequest) throws DomainException;

    SingleResponse<T> update(UpdateRequest<T> updateRequest) throws DomainException;

    BaseResponse delete(IdRequest idRequest) throws DomainException;
}
