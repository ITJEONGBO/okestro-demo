package com.itinfo.itcloud.configuration

import com.itinfo.common.LoggerDelegate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.ResponseBuilder
import springfox.documentation.service.Response
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket


// @EnableSwagger2
@Configuration
class SwaggerConfig {
    @Bean
    fun api(): Docket {
        log.debug("... api")
        return Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .useDefaultResponseMessages(false)
            .globalResponses(HttpMethod.POST, arrayList) // getArrayList()함수에서 정의한 응답메시지 사용
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.itinfo.itcloud.controller"))
            .paths(PathSelectors.any())
            .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfo(
            "아이티클라우드 API",
            "아이티클라우드 API",
            "0.0.2",
            "Terms of service",
            Contact("chlee", "github.com/ITJEONGBO", "chanhi2000@gmail.com"),
            "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", emptyList()
        )
    }

    private val arrayList: List<Response>
        get() = arrayListOf(
            ResponseBuilder().code("500").description("이상한요청").build(),
            ResponseBuilder().code("403").description("황당한요청").build(),
            ResponseBuilder().code("401").description("비인증된접근").build()
        )

    companion object {
        private val log by LoggerDelegate()
    }
}