package com.itinfo.itcloud.ovirt

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.service.ItSystemPropertyService
import com.itinfo.util.model.SystemPropertiesVo

import org.ovirt.engine.sdk4.Connection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*


@Service
class ConnectionService {
	@Autowired private lateinit var itSystemPropertyService: ItSystemPropertyService
	private val uid: String = Random().nextInt(1000).toString()

	fun getConnection(): Connection {
		val systemPropertiesVO: SystemPropertiesVo =
			itSystemPropertyService.searchSystemProperties()
		log.info(systemPropertiesVO.ip)
		return systemPropertiesVO.toConnection()
	}

	companion object {
		private val log by LoggerDelegate()
	}
}