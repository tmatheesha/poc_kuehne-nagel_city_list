package com.kuehne_nagel.city_list.application.transport.response;

import java.util.List;

import lombok.Data;

@Data
public class ListResponse <T> extends BaseResponse {

   private Long noOfItems;

   private List <T> dtoList;
}
