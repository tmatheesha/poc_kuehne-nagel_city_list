package com.kuehne_nagel.city_list.application.transport.response;

import com.kuehne_nagel.city_list.domain.entities.dto.UserDetailDto;
import lombok.Data;

@Data
public class SingleUserResponse extends BaseResponse {

    private UserDetailDto userDetailDto;
}
