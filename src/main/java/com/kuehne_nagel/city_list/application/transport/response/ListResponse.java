package com.kuehne_nagel.city_list.application.transport.response;

import lombok.Data;

import java.util.List;

@Data
public class ListResponse<T> extends BaseResponse {

    private Long noOfItems;

    private List<T> dtoList;
}
