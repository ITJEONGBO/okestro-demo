package com.itinfo.itcloud.ovirt

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.service.ItSystemPropertyService
import org.ovirt.engine.sdk4.Connection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AdminConnectionService {
	@Autowired
	private lateinit var systemPropertiesService: ItSystemPropertyService
	fun getConnection(): Connection {
		// TODO null 체크 할 수 있는 예외처리 필요
		log.debug("getConnection ... ")
		val systemProperties = systemPropertiesService.searchSystemProperties()
		var connection: Connection? = null
		try {
			connection = systemProperties.toConnection()
		} catch (e: Exception) {
			log.error(e.localizedMessage)
		}
		return connection!!
	}

	companion object {
		private val log by LoggerDelegate()
	}
}