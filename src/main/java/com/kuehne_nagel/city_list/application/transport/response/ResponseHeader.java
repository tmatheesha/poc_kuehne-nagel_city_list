package com.kuehne_nagel.city_list.application.transport.response;

import lombok.Data;

@Data
public class ResponseHeader {

    private String requestId;

    private String responseCode;

    private String timestamp;

    private String responseDesc;

    private String responseDescDisplay;
}
