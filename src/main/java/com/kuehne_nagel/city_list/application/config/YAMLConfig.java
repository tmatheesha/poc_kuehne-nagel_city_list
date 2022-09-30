package com.kuehne_nagel.city_list.application.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "msce")
@Getter
@Setter
public class YAMLConfig {

    @Value("${log.identifierKey}")
    private String logIdentifierKey;

    @Value("${request.id-key}")
    private String requestIdKey;

    @Value("${logging.request.shouldLogInfo}")
    private String loggingRequestShouldLogInfo;

    @Value("${logging.response.shouldLogInfo}")
    private String loggingResponseShouldLogInfo;
}
