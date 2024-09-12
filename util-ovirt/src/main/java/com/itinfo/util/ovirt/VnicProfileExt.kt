package com.itinfo.util.ovirt

import com.itinfo.util.ovirt.error.*

import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.services.VnicProfileService
import org.ovirt.engine.sdk4.services.VnicProfilesService
import org.ovirt.engine.sdk4.types.VnicProfile

private fun Connection.srvVnicProfiles(): VnicProfilesService =
	this.systemService.vnicProfilesService()

fun Connection.findAllVnicProfiles(): Result<List<VnicProfile>> = runCatching {
	this.srvVnicProfiles().list().follow("network").send().profiles()
}.onSuccess {
	Term.VNIC_PROFILE.logSuccess("목록조회")
}.onFailure {
	Term.VNIC_PROFILE.logFail("목록조회", it)
	throw if (it is Error) it.toItCloudException() else it
}

private fun Connection.srvVnicProfile(vnicProfileId: String): VnicProfileService =
	this.srvVnicProfiles().profileService(vnicProfileId)

fun Connection.findVnicProfile(vnicProfileId: String): Result<VnicProfile?> = runCatching {
	this.srvVnicProfile(vnicProfileId).get().follow("network").send().profile()
}.onSuccess {
	Term.VNIC_PROFILE.logSuccess("상세조회", vnicProfileId)
}.onFailure {
	Term.VNIC_PROFILE.logFail("상세조회", it, vnicProfileId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.updateVnicProfile(vnicProfileId: String, vnicProfile: VnicProfile): Result<VnicProfile?> = runCatching {
	this.srvVnicProfile(vnicProfileId).update().profile(vnicProfile).send().profile()
}.onSuccess {
	Term.VNIC_PROFILE.logSuccess("편집", vnicProfileId)
}.onFailure {
	Term.VNIC_PROFILE.logFail("편집", it, vnicProfileId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.removeVnicProfile(vnicProfileId: String): Result<Boolean> = runCatching {
	this.srvVnicProfile(vnicProfileId).remove().send()
	true
}.onSuccess {
	Term.VNIC_PROFILE.logSuccess("삭제", vnicProfileId)
}.onFailure {
	Term.VNIC_PROFILE.logFail("삭제", it, vnicProfileId)
	throw if (it is Error) it.toItCloudException() else it
}