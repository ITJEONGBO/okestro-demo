package com.itinfo.util.ovirt

import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.error.FailureType
import com.itinfo.util.ovirt.error.toError
import com.itinfo.util.ovirt.error.toResult
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.services.*
import org.ovirt.engine.sdk4.types.*
import kotlin.Error

private fun Connection.srvNetworks(): NetworksService =
	this.systemService.networksService()

fun Connection.findAllNetworks(follow: String = ""): Result<List<Network>> = runCatching {
	if (follow.isNotEmpty())
		this.srvNetworks().list().follow(follow).send().networks()
	else
		this.srvNetworks().list().send().networks()
}.onSuccess {
	Term.NETWORK.logSuccess("목록조회")
}.onFailure {
	Term.NETWORK.logFail("목록조회", it)
	throw it
}

fun Connection.srvNetwork(networkId: String): NetworkService =
	this.srvNetworks().networkService(networkId)

fun Connection.findNetwork(networkId: String, follow: String = ""): Result<Network?> = runCatching {
	if (follow.isNotEmpty())
		this.srvNetwork(networkId).get().send().network()
	else
		this.srvNetwork(networkId).get().follow(follow).send().network()
}.onSuccess {
	Term.NETWORK.logSuccess("상세조회", networkId)
}.onFailure {
	Term.NETWORK.logFail("상세조회", it, networkId)
	throw it
}


fun Connection.addNetwork(network: Network): Result<Network?> = runCatching {

	val networkAdded: Network =
		this.srvNetworks().add().network(network).send().network()

	networkAdded
}.onSuccess {
	Term.NETWORK.logSuccess("생성")
}.onFailure {
	Term.NETWORK.logFail("생성", it)
	throw it
}

fun Connection.addVnicProfileFromNetwork(networkId: String, vnicProfile: VnicProfile): Result<VnicProfile> = runCatching {
	val vnicProfiles: List<VnicProfile> =
		this.findAllVnicProfilesFromNetwork(networkId)
			.getOrDefault(listOf())

	val hasDuplicateName: Boolean = vnicProfiles.any { it.name() == vnicProfile.name() }
	if (hasDuplicateName)
		return FailureType.DUPLICATE.toResult(vnicProfile.name())
	this@addVnicProfileFromNetwork.srvVnicProfilesFromNetwork(networkId).add().profile(vnicProfile).send().profile()
}.onSuccess {
	log.info("VnicProfile 생성 완료 ... {}", vnicProfile.name())
} .onFailure {
	log.error("VnicProfile 생성 실패... {}", it.localizedMessage)
	throw it
}

fun Connection.removeVnicProfileFromNetwork(networkId: String, vnicProfileId: String): Result<Boolean> = runCatching {
    this.srvVnicProfileFromNetwork(networkId, vnicProfileId).remove().send()
	true
}.onSuccess {
	log.info("VnicProfile 삭제 완료 ... {}", vnicProfileId)
}.onFailure {
	log.info("VnicProfile 삭제 실패 ... 이유: {}", it.localizedMessage)
	throw it
}

fun Connection.updateNetwork(network: Network): Result<Network?> = runCatching {
	val networkUpdated: Network =
		this.srvNetwork(network.id()).update().network(network).send().network()
	networkUpdated
}.onSuccess {
	Term.NETWORK.logSuccess("편집", it.id())
}.onFailure {
	Term.NETWORK.logFail("편집", it)
	throw it
}

fun Connection.removeNetwork(networkId: String): Result<Boolean> = runCatching {
	val network: Network =
		this.findNetwork(networkId).getOrNull() ?: throw ErrorPattern.NETWORK_NOT_FOUND.toError()
	if(network.status() == NetworkStatus.NON_OPERATIONAL){
		this.srvNetwork(networkId).remove().send()
		true
	}else {
		log.warn("{} 삭제 실패... {} 가 OPERATIONAL 상태 ", Term.NETWORK.desc, networkId)
		false
	}
}.onSuccess {
	Term.NETWORK.logSuccess("삭제", networkId)
}.onFailure {
	Term.NETWORK.logFail("삭제", it, networkId)
	throw it
}
private fun Connection.srvNetworkLabelsFromNetwork(networkId: String): NetworkLabelsService =
	this.srvNetwork(networkId).networkLabelsService()

fun Connection.findAllNetworkLabelsFromNetwork(networkId: String): Result<List<NetworkLabel>> = runCatching {
	this.srvNetworkLabelsFromNetwork(networkId).list().send().labels()
}.onSuccess {
	log.info("{} 내 {} 목록조회 완료 ... {}", Term.NETWORK, Term.NETWORK_LABEL, networkId)
}.onFailure {
	log.info("{} 내 {} 목록조회 실패 ... 이유: {}", Term.NETWORK, Term.NETWORK_LABEL, it.localizedMessage)
	throw it
}

fun Connection.addNetworkLabelFromNetwork(networkId: String, networkLabel: NetworkLabel): Result<NetworkLabel> = runCatching {
	this.srvNetworkLabelsFromNetwork(networkId).add().label(networkLabel).send().label()
}.onSuccess {
	log.info("{} 내 {} 생성 완료 ... {}", Term.NETWORK, Term.NETWORK_LABEL, networkLabel.name())
} .onFailure {
	log.error("{} 내 {} 생성 실패... {}", Term.NETWORK, Term.NETWORK_LABEL, it.localizedMessage)
	throw it
}

private fun Connection.srvNetworkLabelFromNetwork(networkId: String, networkLabelId: String): NetworkLabelService =
	this.srvNetworkLabelsFromNetwork(networkId).labelService(networkLabelId)

fun Connection.removeNetworkLabelFromNetwork(networkId: String, networkLabelId: String): Boolean = try {
	this.srvNetworkLabelFromNetwork(networkId, networkLabelId).remove().send()
	true
} catch (e: Error) {
	log.error(e.localizedMessage)
	false
}

private fun Connection.srvVnicProfilesFromNetwork(networkId: String): AssignedVnicProfilesService =
	this.srvNetwork(networkId).vnicProfilesService()

fun Connection.findAllVnicProfilesFromNetwork(networkId: String, follow: String = ""): Result<List<VnicProfile>> = runCatching {
	if (follow.isNotEmpty())
		this.srvVnicProfilesFromNetwork(networkId).list().follow(follow).send().profiles()
	else
		this.srvVnicProfilesFromNetwork(networkId).list().send().profiles()
}.onSuccess {
	Term.NETWORK.logSuccessWithin(Term.VNIC_PROFILE, "목록조회", networkId)
}.onFailure {
	Term.NETWORK.logFailWithin(Term.VNIC_PROFILE, "목록조회", it, networkId)
	throw it
}

private fun Connection.srvVnicProfileFromNetwork(networkId: String, vnicProfileId: String): AssignedVnicProfileService =
	this.srvVnicProfilesFromNetwork(networkId).profileService(vnicProfileId)

fun Connection.findVnicProfileFromNetwork(networkId: String, vnicProfileId: String): Result<VnicProfile?> = runCatching {
	this.srvVnicProfileFromNetwork(networkId, vnicProfileId).get().send().profile()
}.onSuccess {
	Term.NETWORK.logSuccessWithin(Term.VNIC_PROFILE, "상세조회", networkId)
}.onFailure {
	Term.NETWORK.logFailWithin(Term.VNIC_PROFILE, "상세조회", it, networkId)
	throw it
}

private fun Connection.srvPermissionsFromNetwork(networkId: String): AssignedPermissionsService =
	this.srvNetwork(networkId).permissionsService()

fun Connection.findAllPermissionsFromNetwork(networkId: String, follow: String = ""): Result<List<Permission>> = runCatching {
	if (follow.isNotEmpty())
		this.srvPermissionsFromNetwork(networkId).list().follow(follow).send().permissions()
	else
		this.srvPermissionsFromNetwork(networkId).list().send().permissions()
}.onSuccess {
	Term.NETWORK.logSuccessWithin(Term.PERMISSION, "목록조회", networkId)
}.onFailure {
	Term.NETWORK.logFailWithin(Term.PERMISSION, "목록조회", it, networkId)
	throw it
}
