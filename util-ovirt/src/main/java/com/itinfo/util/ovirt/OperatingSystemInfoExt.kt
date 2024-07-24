package com.itinfo.util.ovirt

import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.services.OperatingSystemsService
import org.ovirt.engine.sdk4.types.OperatingSystemInfo

private fun Connection.srvOperatingSystems(): OperatingSystemsService =
	systemService.operatingSystemsService()

fun Connection.findAllOperatingSystems(): Result<List<OperatingSystemInfo>> = runCatching {
	this.srvOperatingSystems().list().send().operatingSystem()
}.onSuccess {
	Term.OPERATING_SYSTEM.logSuccess("목록조회")
}.onFailure {
	Term.OPERATING_SYSTEM.logFail("목록조회", it)
	throw it
}
