package com.itinfo.itcloud.service

import com.itinfo.itcloud.ovirt.AdminConnectionService
import com.itinfo.itcloud.service.admin.ItSystemPropertiesService
import com.itinfo.util.model.SystemPropertiesVo
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.services.SystemService
import org.springframework.beans.factory.annotation.Autowired

open class BaseService {
	@Autowired protected lateinit var admin: AdminConnectionService
	@Autowired private lateinit var systemProperties: ItSystemPropertiesService

	open val conn: Connection
		get() = admin.getConnection()

	open val system: SystemService
		get() = conn.systemService()

	open val systemPropertiesVo: SystemPropertiesVo
		get() = systemProperties.findOne()

	companion object {
	}
}