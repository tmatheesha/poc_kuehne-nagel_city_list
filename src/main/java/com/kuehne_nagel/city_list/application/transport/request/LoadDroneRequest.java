package com.kuehne_nagel.city_list.application.transport.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties( ignoreUnknown = true )
public class LoadDroneRequest extends  BaseRequest{
    private Long droneId;
    private List <Long> medicationIdList;
}
