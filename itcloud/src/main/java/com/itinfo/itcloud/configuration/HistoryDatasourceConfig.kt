package com.itinfo.itcloud.configuration

import com.itinfo.common.LoggerDelegate
// import com.itinfo.service.engine.WorkloadPredictionService
import com.zaxxer.hikari.HikariDataSource
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.sql.SQLException
import java.util.*
import javax.sql.DataSource


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
	basePackages=["com.itinfo.itcloud.repository.history"],
	entityManagerFactoryRef = "historyEntityManager",
	transactionManagerRef = "historyTransactionManager"
)
class HistoryDatasourceConfig {

	// castanets-core-context.xml
	// query-postgresql-context.xml
	@Bean(name=["historyEntityManager"])
	fun historyEntityManager(builder: EntityManagerFactoryBuilder): LocalContainerEntityManagerFactoryBean {
		return builder.dataSource(historyDataSource())
			.packages("com.itinfo.itcloud.repository.history.entity") // entity 클래스 패키지
			.build().apply {
				setJpaProperties(Properties().apply {
					put("hibernate.physical_naming_strategy", CamelCaseToUnderscoresNamingStrategy::class.java.canonicalName)
				})
			}
	}

	@Bean
	fun historyTransactionManager(
		@Qualifier("historyEntityManager") entityManagerFactoryBean: LocalContainerEntityManagerFactoryBean
	): PlatformTransactionManager {
		return JpaTransactionManager(entityManagerFactoryBean.getObject()!!)
	}

	@Bean
	fun historyJdbcTemplate(): JdbcTemplate {
		log.debug("... historyJdbcTemplate")
		return JdbcTemplate(historyDataSource())
	}

	@Bean
	@Primary
	@ConfigurationProperties("spring.datasource.history")
	fun historyDataSourceProperties(): DataSourceProperties {
		log.info("historyDataSourceProperties ...")
		return DataSourceProperties()
	}

	@Bean
	@Throws(SQLException::class)
	fun historyDataSource(): DataSource {
		log.debug("... historyDataSource")
		return historyDataSourceProperties()
			.initializeDataSourceBuilder()
			.type(HikariDataSource::class.java)
			.build()
	}

	companion object {
		private val log by LoggerDelegate()
	}
}