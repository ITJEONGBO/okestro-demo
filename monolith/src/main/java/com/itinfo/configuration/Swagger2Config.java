package com.itinfo.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EnableSwagger2
@Slf4j
public class Swagger2Config {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.POST, getArrayList()) // getArrayList()함수에서 정의한 응답메시지 사용
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.itinfo.controller"))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfo(
				"OKESTRO API",
				"OKESTRO API",
				"0.0.5",
				"Terms of service",
				new Contact("chlee", "github.com/chanhi2000", "chanhi2000@gmail.com"),
				"Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList());
	}

	private List<ResponseMessage> getArrayList() {
		List<ResponseMessage> lists = new ArrayList<>();
		lists.add(new ResponseMessageBuilder().code(500).message("이상한요청").build());
		lists.add(new ResponseMessageBuilder().code(403).message("황당한요청").build());
		lists.add(new ResponseMessageBuilder().code(401).message("비인증된접근").build());
		return lists;
	}
}
