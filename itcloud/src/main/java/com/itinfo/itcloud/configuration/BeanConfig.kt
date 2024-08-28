package com.itinfo.itcloud.configuration

import com.itinfo.common.LoggerDelegate

import com.zaxxer.hikari.HikariConfig

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter


@Configuration
class BeanConfig: HikariConfig() {

	// @Autowired private lateinit var applicationContext: ApplicationContext
	@Bean
	fun entityManagerFactoryBuilder(): EntityManagerFactoryBuilder {
		log.info("... entityManagerFactoryBuilder")
		return EntityManagerFactoryBuilder(HibernateJpaVendorAdapter(), hashMapOf<String,Any>(), null)
	}

	companion object {
		private val log by LoggerDelegate()
	}
}