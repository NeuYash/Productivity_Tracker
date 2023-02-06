package com.cg.tasktracker;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.context.annotation.Bean;

import springfox.documentation.service.Contact;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableAutoConfiguration(exclude = JmxAutoConfiguration.class)
@EnableSwagger2
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2).select().paths(PathSelectors.any())
				.apis(RequestHandlerSelectors.basePackage("com.cg")).build().apiInfo(myApiInfo());
	}

	private ApiInfo myApiInfo() {
		// for version 2.9.1
		return new ApiInfo("SPRING WITH SWAGGER API", "API CREATION", "1.0", "Free to Use",
				new Contact("Yash Pawar", "/", "yash.pawar642@gmail.com"), "API licence", "/",
				Collections.emptyList());
		
	}

}
