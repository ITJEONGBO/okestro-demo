package com.itinfo.util.ovirt

import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.services.OpenstackVolumeProviderService
import org.ovirt.engine.sdk4.services.OpenstackVolumeProvidersService
import org.ovirt.engine.sdk4.types.OpenStackVolumeProvider

private fun Connection.srvOpenStackVolumeProviders(): OpenstackVolumeProvidersService =
	this.systemService.openstackVolumeProvidersService()

fun Connection.findAllOpenStackVolumeProviders(): Result<List<OpenStackVolumeProvider>> = runCatching {
	this.srvOpenStackVolumeProviders().list().send().providers()
}.onSuccess {
	Term.OPEN_STAK_VOLUME_PROVIDER.logSuccess("목록조회")
}.onFailure {
	Term.OPEN_STAK_VOLUME_PROVIDER.logFail("목록조회", it)
	throw it
}

private fun Connection.srvOpenStackVolumeProvider(openStackVolumeProviderId: String): OpenstackVolumeProviderService =
	this.srvOpenStackVolumeProviders().providerService(openStackVolumeProviderId)

fun Connection.findOpenStackVolumeProvider(openStackVolumeProviderId: String): Result<OpenStackVolumeProvider?> = runCatching {
	this.srvOpenStackVolumeProvider(openStackVolumeProviderId).get().send().provider()
}.onSuccess {
	Term.OPEN_STAK_VOLUME_PROVIDER.logSuccess("상세조회")
}.onFailure {
	Term.OPEN_STAK_VOLUME_PROVIDER.logFail("상세조회", it)
	throw it
}