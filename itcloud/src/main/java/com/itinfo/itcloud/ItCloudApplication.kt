package com.itinfo.itcloud

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.itinfo.common.GsonLocalDateTimeAdapter
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.ComponentScan
import org.springframework.core.SpringVersion
import java.text.SimpleDateFormat
import java.time.LocalDateTime

private val log = LoggerFactory.getLogger(ItCloudApplication::class.java)

@SpringBootApplication(scanBasePackages = ["com.itinfo.itcloud.jsp"])
@ComponentScan(basePackages = [
	"com.itinfo.itcloud.configuration",
	// "com.itinfo.security",
	"com.itinfo.itcloud.service",
	"com.itinfo.itcloud.repository",
	"com.itinfo.itcloud.aaarepository"
])
@ServletComponentScan
class ItCloudApplication: SpringBootServletInitializer() {
	override fun configure(builder: SpringApplicationBuilder?): SpringApplicationBuilder {
		return builder!!.sources(ItCloudApplication::class.java)
	}
}

fun main(args: Array<String>) {
	log.info("SPRING VERSION: ${SpringVersion.getVersion()}")
	runApplication<ItCloudApplication>(*args)
}

val gson: Gson = GsonBuilder()
	.registerTypeAdapter(LocalDateTime::class.java, GsonLocalDateTimeAdapter())
	.setPrettyPrinting()
	.disableHtmlEscaping()
	.create()

const val GB = 1073741824.0 // gb 변환
const val OVIRT_API_DATE_FORMAT = "yyyy. MM. dd. HH:mm:ss"
val ovirtDf = SimpleDateFormat(OVIRT_API_DATE_FORMAT)

private const val DEFAULT_TIME_SLEEP_IN_MILLI = 500L
private const val DEFAULT_TIME_LONG_SLEEP_IN_MILLI = 5000L

fun doSleep(timeInMilli: Long = DEFAULT_TIME_SLEEP_IN_MILLI) {
	log.info("... doSleep($timeInMilli)")
	try { Thread.sleep(timeInMilli) } catch (e: InterruptedException) { log.error(e.localizedMessage) }
}

fun doLongSleep() =
	doSleep(DEFAULT_TIME_LONG_SLEEP_IN_MILLI)