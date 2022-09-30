package com.kuehne_nagel.city_list.application.transport.response;

import lombok.Data;

@Data
public class SingleResponse <T> extends BaseResponse {

    private T dto;

}
