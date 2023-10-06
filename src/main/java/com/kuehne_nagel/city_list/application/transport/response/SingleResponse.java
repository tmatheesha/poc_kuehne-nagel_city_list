package com.kuehne_nagel.city_list.application.transport.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleResponse<T> extends BaseResponse {

    private T dto;

}
