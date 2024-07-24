package com.itinfo.util.ovirt

import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.services.DiskProfileService
import org.ovirt.engine.sdk4.services.DiskProfilesService
import org.ovirt.engine.sdk4.types.DiskProfile

private fun Connection.srvDiskProfiles(): DiskProfilesService =
	systemService.diskProfilesService()

fun Connection.findAllDiskProfiles(): Result<List<DiskProfile>> = runCatching {
	this.srvDiskProfiles().list().send().profile()
}.onSuccess {
	Term.DISK_PROFILE.logSuccess("목록조회")
}.onFailure {
	Term.DISK_PROFILE.logFail("목록조회")
	throw it
}

private fun Connection.srvDiskProfile(diskProfileId: String): DiskProfileService =
	this.srvDiskProfiles().diskProfileService(diskProfileId)

fun Connection.findDiskProfile(diskProfileId: String): Result<DiskProfile?> = runCatching {
	this.srvDiskProfile(diskProfileId).get().send().profile()
}.onSuccess {
	Term.DISK_PROFILE.logSuccess("상세조회")
}.onFailure {
	Term.DISK_PROFILE.logFail("상세조회")
	throw it
}