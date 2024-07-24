package com.itinfo.util.ovirt

import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.services.OpenstackImageProviderService
import org.ovirt.engine.sdk4.services.OpenstackImageProvidersService
import org.ovirt.engine.sdk4.types.OpenStackImageProvider

private fun Connection.srvOpenStackImageProviders(): OpenstackImageProvidersService =
	systemService.openstackImageProvidersService()

fun Connection.findAllOpenStackImageProviders(): Result<List<OpenStackImageProvider>> = runCatching {
	this.srvOpenStackImageProviders().list().send().providers()
}.onSuccess {
	Term.OPEN_STAK_IMAGE_PROVIDER.logSuccess("목록조회")
}.onFailure {
	Term.OPEN_STAK_IMAGE_PROVIDER.logFail("목록조회", it)
	throw it
}

private fun Connection.srvOpenStackImageProvider(openStackImageProviderId: String): OpenstackImageProviderService =
	this.srvOpenStackImageProviders().providerService(openStackImageProviderId)

fun Connection.findOpenStackImageProvider(openStackImageProviderId: String): Result<OpenStackImageProvider?> = runCatching {
	this.srvOpenStackImageProvider(openStackImageProviderId).get().send().provider()
}.onSuccess {
	Term.OPEN_STAK_IMAGE_PROVIDER.logSuccess("상세조회", openStackImageProviderId)
}.onFailure {
	Term.OPEN_STAK_IMAGE_PROVIDER.logFail("상세조회", it, openStackImageProviderId)
	throw it
}