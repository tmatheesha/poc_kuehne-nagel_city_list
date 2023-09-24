package com.kuehne_nagel.city_list.application.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableSwagger2
public class SwaggerConfig {

    @Value("${base-url.context}")
    private String context;

    @Value("${app.host}")
    private String host;

    /*@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host(host)
                .select().apis(RequestHandlerSelectors.basePackage("com.kuehne_nagel.city_list.application.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securityContexts(Lists.newArrayList(securityContext()))
                .pathProvider(new BasePathAwareRelativePathProvider(context));
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.kuehne_nagel.city_list.application.controller"))
                .paths(PathSelectors.regex("/com/v1.0/citymgt.*"))
                .build()
                .groupName("city-list")
                .apiInfo(apiInfo());
    }*/
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("city-list")
                .pathsToMatch(String.format("%s/**", context))
                .build();
    }
   /* private SecurityContext securityContext() {
        return SecurityContext
                .builder()
                .securityReferences(defaultAuth())
                .build();
    }*/

   /* private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = {new AuthorizationScope("global", "accessEverything")};
        return Lists.newArrayList(new SecurityReference("JWT", authorizationScopes));
    }*/

  /*  private ApiInfo apiInfo() {
        String title = "Test";
        return new ApiInfo(
                title,
                "Test",
                "v1.0",
                "Test",
                new Contact("Admin", "http://www.google.com", "test@google.com"),
                "License of API", "API license URL", Collections.emptyList());
    }*/

  /*  class BasePathAwareRelativePathProvider extends AbstractPathProvider {

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

    }*/

}
