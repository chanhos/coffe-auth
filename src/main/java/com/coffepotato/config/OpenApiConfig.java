package com.coffepotato.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${spring.profiles.include}")
    private String include;

    @Autowired
    BuildProperties buildProperties;

    @Bean
    public OpenAPI openapiConfig(){
        //OPENAPI 설정을 반환한다.
        OpenAPI openAPI = new OpenAPI();
        openAPI.setInfo(
                new Info().title("Coffe JWT Auth API")
                        .version(buildProperties.getVersion())
                        .description("JWT 인증서버 API ")
                        .contact(
                                new Contact().name("Chans")
                                        .email("lovgone@naver.com")
                        )
        );


        return openAPI;
    }

    @Bean
    public GroupedOpenApi TotalAPI(){
        String path = "com.coffepotato.controller" ;
        return  GroupedOpenApi.builder().group("A.전체").packagesToScan(path).build();
    }

    @Bean
    public GroupedOpenApi LoginAPI(){
        String[] paths = {"/manage/**"};
        return  GroupedOpenApi.builder().group("B.인증서버 매니징").pathsToMatch(paths).build();
    }

    @Bean
    public GroupedOpenApi AuthAPI(){
        String[] paths = {"/auth/**"};
        return  GroupedOpenApi.builder().group("A.인증").pathsToMatch(paths).build();
    }
}
