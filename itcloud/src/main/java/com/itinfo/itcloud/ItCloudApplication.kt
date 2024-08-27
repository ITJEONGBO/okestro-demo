package com.itinfo.itcloud

import com.itinfo.common.LoggerDelegate
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.ComponentScan
import org.springframework.core.SpringVersion
import java.text.SimpleDateFormat

@SpringBootApplication(scanBasePackages = ["com.itinfo.itcloud.jsp"])
@ComponentScan(basePackages = [
	 "com.itinfo.itcloud.configuration",
	// "com.itinfo.security",
	"com.itinfo.itcloud.service",
	"com.itinfo.itcloud.repository"
])
@ServletComponentScan
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

const val GB = 1073741824.0 // gb 변환
const val OVIRT_API_DATE_FORMAT = "yyyy. MM. dd. HH:mm:ss"
val ovirtDf = SimpleDateFormat(OVIRT_API_DATE_FORMAT)