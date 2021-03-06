package com.example.profileservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Configuration for Swagger implementation.
 * */
@EnableSwagger2WebMvc
@Configuration
public class SwaggerConfig {

    /**
     * The api method to set up Swagger.
     * @return a Docket that defines the apis used, paths to display and information on API endpoints.
     * */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.profileservice.controller"))
                .paths(PathSelectors.any())
                //.paths(PathSelectors.ant("/signup/**"))
                .build()
                .apiInfo(apiEndPointsInfo());
    }

    /**
     * Method that describes the APIs used in Swagger for the application.
     * @return an ApiInfoBuilder object that includes a title, description and version.
     * */
    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Scandit Profile Service REST API")
                .description("REST API for the Scandit Profile service")
                .version("1.0.0")
                .build();
    }
}
