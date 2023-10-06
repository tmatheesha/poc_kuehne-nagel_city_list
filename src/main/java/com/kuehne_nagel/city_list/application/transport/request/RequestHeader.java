package com.kuehne_nagel.city_list.application.transport.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestHeader {

    @NotNull(message = "requestId should not be null")
    private String requestId;

    @NotNull(message = "timestamp should not be null")
    private LocalDateTime timestamp;

    private String userId;

    private String userName;

}
