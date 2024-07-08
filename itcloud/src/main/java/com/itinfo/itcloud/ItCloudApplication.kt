package com.itinfo.itcloud

import com.itinfo.common.LoggerDelegate
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.ComponentScan
import org.springframework.core.SpringVersion

@SpringBootApplication(scanBasePackages = ["com.itinfo.itcloud.jsp"])
@ComponentScan(basePackages = [
	 "com.itinfo.itcloud.configuration",
	// "com.itinfo.security",
	"com.itinfo.itcloud.service",
	"com.itinfo.itcloud.repository"
])
class ItCloudApplication: SpringBootServletInitializer() {
	override fun configure(builder: SpringApplicationBuilder?): SpringApplicationBuilder {
		return builder!!.sources(ItCloudApplication::class.java)
	}

	companion object {
		val log by LoggerDelegate()
	}
}

fun main(args: Array<String>) {
	ItCloudApplication.log.info("SPRING VERSION: ${SpringVersion.getVersion()}")
	runApplication<ItCloudApplication>(*args)
}
