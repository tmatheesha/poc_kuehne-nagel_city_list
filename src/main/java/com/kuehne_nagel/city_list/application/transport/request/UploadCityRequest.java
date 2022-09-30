package com.kuehne_nagel.city_list.application.transport.request;

import javax.validation.constraints.NotNull;

import com.kuehne_nagel.city_list.domain.util.ExternalFileTypes;
import lombok.Data;

@Data
public class UploadCityRequest extends BaseRequest{

    @NotNull( message = "externalFileTypes not found in ImportProductRequest for operation. This action is not allowed" )
    private ExternalFileTypes externalFileTypes;
}
