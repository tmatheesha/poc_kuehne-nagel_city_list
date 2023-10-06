package com.kuehne_nagel.city_list.application.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${base-url.context}")
    private String context;

    @Value("${app.host}")
    private String host;

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("city-list")
                .pathsToMatch(String.format("%s/**", context))
                .build();
    }

}
