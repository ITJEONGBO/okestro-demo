package com.itinfo.util.ovirt

import com.itinfo.util.ovirt.error.*

import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.services.CpuProfileService
import org.ovirt.engine.sdk4.services.CpuProfilesService
import org.ovirt.engine.sdk4.types.CpuProfile

private fun Connection.srvCpuProfiles(): CpuProfilesService =
	systemService.cpuProfilesService()

fun Connection.findAllCpuProfiles(): Result<List<CpuProfile>> = runCatching {
	this.srvCpuProfiles().list().send().profile()
}.onSuccess {
	Term.CPU_PROFILE.logSuccess("목록조회")
}.onFailure {
	Term.CPU_PROFILE.logFail("목록조회", it)
	throw if (it is Error) it.toItCloudException() else it
}

private fun Connection.srvCpuProfile(cpuProfileId: String): CpuProfileService =
	this.systemService.cpuProfilesService().profileService(cpuProfileId)

fun Connection.findCpuProfile(cpuProfileId: String): Result<CpuProfile?> = runCatching {
	this.srvCpuProfile(cpuProfileId).get().send().profile()
}.onSuccess {
	Term.CPU_PROFILE.logSuccess("상세조회")
}.onFailure {
	Term.CPU_PROFILE.logFail("상세조회", it)
	throw if (it is Error) it.toItCloudException() else it
}