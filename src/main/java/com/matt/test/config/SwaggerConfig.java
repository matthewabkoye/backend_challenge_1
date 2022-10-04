package com.matt.test.config;

//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.*;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.service.contexts.SecurityContext;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//
//@Configuration
//@EnableSwagger2
public class SwaggerConfig {
//    private static final String BASIC_AUTH = "basicAuth";
//
//    private ApiInfo apiInfo() {
//        return new ApiInfo("MVP Assignment",
//                "Backend API Challenge",
//                "1.0",
//                "Terms of service",
//                new Contact("Matthew Abikoye", "", "matthewabikoye@gmail.com"),
//                "License of API",
//                "API license URL",
//                Collections.emptyList());
//    }
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.matt.test"))
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo())
//                .securitySchemes(securitySchemes())
//                .securityContexts(Arrays.asList(securityContext()));
//    }
//
//
//
//    private List<SecurityScheme> securitySchemes() {
//        return Arrays.asList(new BasicAuth(BASIC_AUTH));
//    }
//    private ApiKey apiKey() {
//        return new ApiKey("apiKey", "Authorization", "header");
//    }
//
//    private SecurityContext securityContext() {
//        return SecurityContext.builder().
//                securityReferences(Arrays.asList(basicAuthReference())).
//                forPaths(PathSelectors.any()).build();
//    }
//
//    private SecurityReference basicAuthReference() {
//        return new SecurityReference(BASIC_AUTH, new AuthorizationScope[0]);
//    }
}
