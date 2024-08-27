package com.itinfo.util.ovirt

import com.itinfo.util.ovirt.error.*

import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.services.OpenstackNetworkProviderService
import org.ovirt.engine.sdk4.services.OpenstackNetworkProvidersService
import org.ovirt.engine.sdk4.types.OpenStackNetworkProvider

/**
 * 외부 공급자는 한개만 있어서 이것만 쓸수 있음
 */
private fun Connection.srvOpenStackNetworkProviders(): OpenstackNetworkProvidersService =
	systemService.openstackNetworkProvidersService()

fun Connection.findAllOpenStackNetworkProviders(follow: String = ""): Result<List<OpenStackNetworkProvider>> = runCatching {
	if (follow.isNotEmpty())
		this.srvOpenStackNetworkProviders().list().follow(follow).send().providers()
	else
		this.srvOpenStackNetworkProviders().list().send().providers()
}.onSuccess {
	Term.OPEN_STAK_NETWORK_PROVIDER.logSuccess("목록조회")
}.onFailure {
	Term.OPEN_STAK_NETWORK_PROVIDER.logFail("목록조회", it)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.findExternalProvider(): OpenStackNetworkProvider? =
	this@findExternalProvider.findAllOpenStackNetworkProviders().getOrDefault(listOf()).firstOrNull()

private fun Connection.srvOpenStackNetworkProvider(networkProviderId: String): OpenstackNetworkProviderService =
	this.srvOpenStackNetworkProviders().providerService(networkProviderId)

fun Connection.findOpenStackNetworkProvider(networkProviderId: String): Result<OpenStackNetworkProvider?> = runCatching {
	this.srvOpenStackNetworkProvider(networkProviderId).get().send().provider()
}.onSuccess {
	Term.OPEN_STAK_NETWORK_PROVIDER.logSuccess("상세조회")
}.onFailure {
	Term.OPEN_STAK_NETWORK_PROVIDER.logFail("상세조회", it)
	throw if (it is Error) it.toItCloudException() else it
}
