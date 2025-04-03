package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
                .apis(RequestHandlerSelectors.basePackage("com.controller")) // Change to your package
                .paths(PathSelectors.any()) // Document all endpoints
                .build()
                .apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Online Judge API")
				.description("Online Judge API reference for developers")
				.termsOfServiceUrl("http://springboot.com")
				.contact(new Contact("Sanjay Pidishetty","", "pidishettysanjay@gmail.com"))
				.license("springboot License")
				.licenseUrl("pdshettysanjay@gmail.com")
				.version("1.0")
				.build();
	}
}
