package com.kuehne_nagel.city_list.application.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.AbstractPathProvider;
import springfox.documentation.spring.web.paths.Paths;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value( "${base-url.context}" )
    private String context;

    @Value( "${app.host}" )
    private String host;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host(host)
                .select().apis(RequestHandlerSelectors.basePackage("com.kuehne_nagel.city_list.application.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .pathProvider(new BasePathAwareRelativePathProvider(context));
    }

    private ApiInfo apiInfo() {
        String title = "Test";
        return new ApiInfo(
                title,
                "Test",
                "v1.0",
                "Test",
                new Contact("Admin", "http://www.google.com", "test@google.com"),
                "License of API", "API license URL", Collections.emptyList());
    }

    class BasePathAwareRelativePathProvider extends AbstractPathProvider {

        private String basePath;

        public BasePathAwareRelativePathProvider(String basePath) {
            this.basePath = basePath;
        }

        protected String applicationPath() {
            return basePath;
        }

        @Override
        protected String getDocumentationPath() {
            return "/";
        }

        @Override
        public String getOperationPath(String operationPath) {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/");
            return Paths.removeAdjacentForwardSlashes(
                    uriComponentsBuilder.path(operationPath.replaceFirst(basePath, "")).build().toString());
        }

    }

}
