package com.itinfo.itcloud.ovirt

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.service.admin.ItSystemPropertiesService
import com.itinfo.util.model.SystemPropertiesVo

import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.ConnectionBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*


@Service
class ConnectionService {
	@Autowired private lateinit var itSystemPropertyService: ItSystemPropertiesService
	private val uid: String
		get() = Random().nextInt(1000).toString()

	fun getConnection(): Connection {
		val systemPropertiesVO: SystemPropertiesVo =
			itSystemPropertyService.findOne()
		log.info(systemPropertiesVO.ip)
		return systemPropertiesVO.toConnection()
	}
	companion object {
		private val log by LoggerDelegate()
	}
}

fun SystemPropertiesVo.toConnection(): Connection = ConnectionBuilder.connection()
	.url(ovirtEngineApiUrl)
	.user(ovirtUserId)
	.password(password)
	.insecure(true)
	.timeout(20000)
	.build()