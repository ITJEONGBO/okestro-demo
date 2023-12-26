package com.itinfo.itcloud.ovirt

import com.itinfo.util.model.SystemPropertiesVo
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.ConnectionBuilder

fun SystemPropertiesVo.toConnection(): Connection = ConnectionBuilder.connection()
	.url(ovirtEngineApiUrl)
	.user(ovirtUserId)
	.password(password)
	.insecure(true)
	.timeout(20000)
	.build()