package com.itinfo.util.ovirt

import com.itinfo.util.ovirt.error.*

import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.services.*
import org.ovirt.engine.sdk4.types.*
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import java.net.InetAddress


fun Connection.srvHosts(): HostsService =
	this.systemService.hostsService()

fun Connection.srvHost(hostId: String): HostService =
	this.srvHosts().hostService(hostId)

fun Connection.findAllHosts(searchQuery: String = "", follow: String = ""): Result<List<Host>> = runCatching {
	// https://192.168.0.70/ovirt-engine/api/hosts?all_content=true
	if (searchQuery.isNotEmpty() && follow.isNotEmpty())
		this.srvHosts().list().search(searchQuery).follow(follow).caseSensitive(false).send().hosts()
	else if (searchQuery.isNotEmpty())
		this.srvHosts().list().search(searchQuery).caseSensitive(false).send().hosts()
	else if (follow.isNotEmpty())
		this.srvHosts().list().follow(follow).caseSensitive(false).send().hosts()
	else
		this.srvHosts().list().allContent(true).send().hosts()
}.onSuccess {
	Term.HOST.logSuccess("목록조회")
}.onFailure {
	Term.HOST.logFail("목록조회", it)
	throw if (it is Error) it.toItCloudException() else it
}


fun Connection.findHost(hostId: String): Result<Host?> = runCatching {
	srvHost(hostId).get().allContent(true).send().host()
}.onSuccess {
	Term.HOST.logSuccess("상세조회", hostId)
}.onFailure {
	Term.HOST.logFail("상세조회", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}

fun List<Host>.nameDuplicateHost(hostName: String, hostId: String? = null): Boolean =
	this.filter { it.id() != hostId }.any { it.name() == hostName }


fun Connection.findAllVmsFromHost(hostId: String, searchQuery: String = ""): Result<List<Vm>> = runCatching {
	if(this.findHost(hostId).isFailure){
		throw ErrorPattern.CLUSTER_NOT_FOUND.toError()
	}
	if (searchQuery.isNotEmpty())
		this.srvVms().list().search(searchQuery).send().vms().filter { it.host().id() == hostId }
	else
		this.srvVms().list().send().vms().filter { it.host().id() == hostId }
}.onSuccess {
	Term.HOST.logSuccess("vms 조회", hostId)
}.onFailure {
	Term.HOST.logFail("vms 조회", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}


fun Connection.addHost(host: Host, deployHostedEngine: Boolean = false): Result<Host?> = runCatching {
	if(this.findAllHosts()
			.getOrDefault(listOf())
			.nameDuplicateHost(host.name())) {
		return FailureType.DUPLICATE.toResult(Term.HOST.desc)
	}
	val hostAdded: Host? =
		srvHosts().add().deployHostedEngine(deployHostedEngine).host(host).send().host()

//	hostAdded?.let { this.expectHostStatus(it.id(), HostStatus.UP) }
	hostAdded ?: throw ErrorPattern.HOST_NOT_FOUND.toError()
}.onSuccess {
	Term.HOST.logSuccess("생성")
}.onFailure {
	Term.HOST.logFail("생성", it)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.updateHost(host: Host): Result<Host?> = runCatching {
	if(this.findAllHosts()
			.getOrDefault(listOf())
			.nameDuplicateHost(host.name(), host.id())) {
		return FailureType.DUPLICATE.toResult(Term.HOST.desc)
	}
	val hostUpdated: Host? =
		this.srvHost(host.id()).update().host(host).send().host()

	hostUpdated ?: throw ErrorPattern.HOST_NOT_FOUND.toError()
}.onSuccess {
	Term.HOST.logSuccess("편집", host.id())
}.onFailure {
	Term.HOST.logFail("편집", it, host.id())
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.removeHost(hostId: String): Result<Boolean> = runCatching {
	val host: Host =
		this.findHost(hostId)
			.getOrNull() ?: throw ErrorPattern.HOST_NOT_FOUND.toError()

	if(this.findAllVmsFromHost("", hostId)
			.getOrDefault(listOf())
			.any { it.status() == VmStatus.UP }){
		throw ErrorPattern.HOST_HAS_RUNNING_VMS.toError()
	}

	if (host.status() != HostStatus.MAINTENANCE) {
		log.warn("{} 삭제 실패... {} 가 유지관리 상태가 아님 ", Term.HOST.desc, hostId)
		throw throw ErrorPattern.HOST_NOT_MAINTENANCE.toError()
	}

	this.srvHost(hostId).remove().send()
	if(!this.expectHostDeleted(hostId)){
		throw Error("삭제 실패했습니다 ... ${hostId}.")
	}
	true
}.onSuccess {
	Term.HOST.logSuccess("삭제", hostId)
}.onFailure {
	Term.HOST.logFail("삭제", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}


@Throws(InterruptedException::class)
fun Connection.expectHostDeleted(hostId: String, timeout: Long = 60000L, interval: Long = 1000L): Boolean {
	val startTime = System.currentTimeMillis()
	while (true) {
		val hosts: List<Host> =
			this.findAllHosts().getOrDefault(listOf())
		val hostToRemove: Host? = hosts.firstOrNull() {it.id() == hostId}
		if (hostToRemove == null) {// !(매치되는것이 있다)
			Term.HOST.logSuccess("삭제")
			return true
		} else if (System.currentTimeMillis() - startTime > timeout) {
			log.error("{} {} 삭제 시간 초과", Term.HOST.desc, hostToRemove.name())
			return false
		}
		log.debug("{} 삭제 진행중 ... ", Term.HOST.desc)
		Thread.sleep(interval)
	}
}

fun Connection.deactivateHost(hostId: String): Result<Boolean> = runCatching {
	val host: Host =
		this.findHost(hostId).getOrNull() ?: throw ErrorPattern.HOST_NOT_FOUND.toError()

	if (host.status() == HostStatus.MAINTENANCE) {
		throw Error("deactivateHost 실패 ... $hostId 가 이미 유지관리 상태") // return 대신 throw
	}
	srvHost(host.id()).deactivate().send()

	if (!this.expectHostStatus(host.id(), HostStatus.MAINTENANCE)) {
		throw Error("expectHostStatus가 실패했습니다 ... $hostId 가 유지관리 상태가 아닙니다.")
	}
	true
}.onSuccess {
	Term.HOST.logSuccess("비활성화", hostId)
}.onFailure {
	Term.HOST.logFail("비활성화", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}


fun Connection.activateHost(hostId: String): Result<Boolean> = runCatching {
	val host: Host =
		this.findHost(hostId)
			.getOrNull() ?: throw ErrorPattern.HOST_NOT_FOUND.toError()

	if (host.status() == HostStatus.UP) {
		return Result.failure(Error("activateHost 실패 ... ${host.name()}가 이미 활성 상태 "))
	}
	srvHost(host.id()).activate().send()

	if (!this.expectHostStatus(host.id(), HostStatus.UP)) {
		throw Error("activate Host 실패했습니다 ...")
	}
	true
}.onSuccess {
	Term.HOST.logSuccess("활성화", hostId)
}.onFailure {
	Term.HOST.logFail("활성화", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}


fun Connection.refreshHost(hostId: String): Result<Boolean> = runCatching {
	if(this.findHost(hostId).isFailure){
		throw ErrorPattern.HOST_NOT_FOUND.toError()
	}
	this.srvHost(hostId).refresh().send()

	if (!this.expectHostStatus(hostId, HostStatus.UP)) {
		throw Error("refresh Host 실패했습니다 ...")
	}
	true
}.onSuccess {
	Term.HOST.logSuccess("새로고침", hostId)
}.onFailure {
	Term.HOST.logFail("새로고침", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.restartHost(hostId: String, hostPw: String): Result<Boolean> = runCatching {
	val host: Host =
		this.findHost(hostId)
			.getOrNull() ?: throw ErrorPattern.HOST_NOT_FOUND.toError()
	if(hostPw.isEmpty()){
		throw ErrorPattern.HOST_NOT_FOUND.toError()
	}
	val address: InetAddress = InetAddress.getByName(host.address())
	if (address.rebootHostViaSSH("root", hostPw, 22).isFailure)
		return Result.failure(Error("SSH를 통한 호스트 재부팅 실패"))

	if (!this.expectHostStatus(hostId, HostStatus.UP)) {
		throw Error("호스트 재부팅 실패했습니다 ...")
	}
	true
}.onSuccess {
	Term.HOST.logSuccess("재부팅", hostId)
}.onFailure {
	Term.HOST.logFail("재부팅", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}

/**
 * [InetAddress.rebootHostViaSSH]
 * host SSH 관리 - 재시작 부분
 */
fun InetAddress.rebootHostViaSSH(username: String, password: String, port: Int): Result<Boolean> = runCatching {
	log.debug("ssh 시작")
	// SSH 세션 생성 및 연결
	val session: com.jcraft.jsch.Session = JSch().getSession(username, hostAddress, port)
	session.setPassword(password)
	session.setConfig("StrictHostKeyChecking", "no") // 호스트 키 확인을 건너뛰기 위해 설정
	session.connect()

	val channel: ChannelExec = session.openChannel("exec") as ChannelExec // SSH 채널 열기
	channel.setCommand("sudo reboot") // 재부팅 명령 실행
	channel.connect()

	// 명령 실행 완료 대기
	while (!channel.isClosed) {
		Thread.sleep(100)
	}

	channel.disconnect()
	session.disconnect()
	val exitStatus = channel.exitStatus
	log.debug("rebootHostViaSSH")
	return Result.success(exitStatus == 0)
}.onSuccess {

}.onFailure {
	log.error(it.localizedMessage)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.enrollCertificate(hostId: String): Result<Boolean> = runCatching {
	if(this.findHost(hostId).isFailure){
		throw ErrorPattern.HOST_NOT_FOUND.toError()
	}
	this.srvHost(hostId).enrollCertificate().send()
	true
}.onSuccess {
	Term.HOST.logSuccess("인증서 등록", hostId)
}.onFailure {
	Term.HOST.logFail("인증서 등록", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}


fun Connection.findPowerManagementFromHost(hostId: String, fenceType: FenceType): Result<PowerManagement?> = runCatching {
	val res = srvHost(hostId).fence().fenceType(fenceType.name).send().powerManagement()
	log.info("powerManagementFromHost result: $res")
	res
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.POWER_MANAGEMENT,"상세조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.POWER_MANAGEMENT,"상세조회", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.migrateHostFromVm(vmId: String, host: Host): Result<Boolean> = runCatching {
	this.srvVm(vmId).migrate().host(host).send()
	true
}.onSuccess {
	Term.VM.logSuccessWithin(Term.HOST,"이동", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.HOST,"이동", it, vmId)
	throw if (it is Error) it.toItCloudException() else it
}

/**
 * [Connection.expectHostStatus]
 * 호스트 상태 체크하는 메소드
 *
 * @param hostId 호스트
 * @param expectStatus 원하는 호스트 상태
 * @param interval 상태 확인 간격(밀리초)
 * @param timeout 최대 대기 시간(밀리초)
 * @throws InterruptedException
 */
@Throws(InterruptedException::class)
fun Connection.expectHostStatus(hostId: String, expectStatus: HostStatus, interval: Long = 3000L, timeout: Long = 900000L): Boolean {
	val startTime: Long = System.currentTimeMillis()
	while (true) {
		val currentHost: Host? = this@expectHostStatus.findHost(hostId).getOrNull()
		val status = currentHost?.status()
		if (status == expectStatus) {
			log.info("호스트 완료 ... {}", expectStatus)
			return true
		} else if (System.currentTimeMillis() - startTime > timeout) {
			log.error("호스트 시간 초과: {}", currentHost?.name());
			return false
		}

		log.info("호스트 상태: {} -> expect '{}'", status, expectStatus)
		Thread.sleep(interval)
	}
}

private fun Connection.srvAllNicsFromHost(hostId: String): HostNicsService =
	this.srvHost(hostId).nicsService()

fun Connection.findAllNicsFromHost(hostId: String): Result<List<HostNic>> = runCatching {
	if(this.findHost(hostId).isFailure){
		throw ErrorPattern.HOST_NOT_FOUND.toError()
	}
	this.srvAllNicsFromHost(hostId).list().send().nics() ?: listOf()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.HOST_NIC,"목록조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.HOST_NIC,"목록조회", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}

private fun Connection.srvNicFromHost(hostId: String, hostNicId: String): HostNicService =
	this.srvAllNicsFromHost(hostId).nicService(hostNicId)

fun Connection.findNicFromHost(hostId: String, hostNicId: String): Result<HostNic?> = runCatching {
	this.srvNicFromHost(hostId, hostNicId).get().send().nic()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.HOST_NIC,"상세조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.HOST_NIC,"상세조회", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.setUpNetworks(hostId: String): Result<Boolean> = runCatching {
	this.srvHost(hostId).setupNetworks().send()

	true
	TODO("호스트 -> 네트워크 인터페이스")
}.onSuccess {
	Term.HOST_NIC.logSuccessWithin(Term.STATISTIC,"호스트 네트워크 설정", hostId)
}.onFailure {
	Term.HOST_NIC.logFailWithin(Term.STATISTIC,"호스트 네트워크 설정", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}



fun Connection.findAllStatisticsFromHostNic(hostId: String, hostNicId: String): Result<List<Statistic>> = runCatching {
	this.srvNicFromHost(hostId, hostNicId).statisticsService().list().send().statistics()
}.onSuccess {
	Term.HOST_NIC.logSuccessWithin(Term.STATISTIC,"목록조회", hostId)
}.onFailure {
	Term.HOST_NIC.logFailWithin(Term.STATISTIC,"목록조회", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}

private fun Connection.srvStatisticsFromHost(hostId: String): StatisticsService =
	this.srvHost(hostId).statisticsService()

fun Connection.findAllStatisticsFromHost(hostId: String): Result<List<Statistic>> = runCatching {
	this.srvStatisticsFromHost(hostId).list().send().statistics()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.STATISTIC,"목록조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.STATISTIC,"목록조회", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.srvStoragesFromHost(hostId: String): HostStorageService =
	this.srvHost(hostId).storageService()

fun Connection.findAllStoragesFromHost(hostId: String): Result<List<HostStorage>> = runCatching {
	this.srvStoragesFromHost(hostId).list().send().storages()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.STORAGE,"목록조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.STORAGE,"목록조회", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}







private fun Connection.srvNetworkAttachmentsFromHost(hostId: String): NetworkAttachmentsService =
	this.srvHost(hostId).networkAttachmentsService()

fun Connection.findAllNetworkAttachmentsFromHost(hostId: String): Result<List<NetworkAttachment>> = runCatching {
	this.srvNetworkAttachmentsFromHost(hostId).list().send().attachments()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.NETWORK_ATTACHMENT,"목록조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.NETWORK_ATTACHMENT,"목록조회", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}

private fun Connection.srvNetworkAttachmentFromHost(hostId: String, networkAttachmentId: String): NetworkAttachmentService =
	this.srvNetworkAttachmentsFromHost(hostId).attachmentService(networkAttachmentId)

fun Connection.modifyNetworkAttachmentsFromHost(hostId: String, networkAttachments: List<NetworkAttachment>): Result<Boolean> = runCatching {
	this.srvHost(hostId).setupNetworks().modifiedNetworkAttachments(networkAttachments).send()
	this.srvHost(hostId).commitNetConfig().send()
	true
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.NETWORK_ATTACHMENT, "일괄편집", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.NETWORK_ATTACHMENT, "일괄편집", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.modifyNetworkAttachmentFromHost(hostId: String, networkAttachment: NetworkAttachment): Result<Boolean> = runCatching {
	this.srvHost(hostId).setupNetworks().modifiedNetworkAttachments(networkAttachment).send()
	this.srvHost(hostId).commitNetConfig().send()
	true
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.NETWORK_ATTACHMENT, "편집", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.NETWORK_ATTACHMENT, "편집", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.updateNetworkAttachmentFromHost(hostId: String, networkAttachmentId: String, networkAttachment: NetworkAttachment): Result<Boolean> = runCatching {
	this.srvNetworkAttachmentFromHost(hostId, networkAttachmentId).update().attachment(networkAttachment).send()
	this.srvHost(hostId).commitNetConfig().send()
	true
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.NETWORK_ATTACHMENT, "편집", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.NETWORK_ATTACHMENT, "편집", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.removeNetworkAttachmentFromHost(hostId: String, networkAttachmentId: String): Result<Boolean> = runCatching {
	this.srvNetworkAttachmentFromHost(hostId, networkAttachmentId).remove().send()
	this.srvHost(hostId).commitNetConfig().send()
	true
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.NETWORK_ATTACHMENT, "제거", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.NETWORK_ATTACHMENT, "제거", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.removeBondsFromHost(hostId: String, hostNics: List<HostNic> = listOf()): Result<Boolean> = runCatching {
	this.srvHost(hostId).setupNetworks().removedBonds(hostNics).send()
	this.srvHost(hostId).commitNetConfig().send()
	true
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.BOND, "제거", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.BOND, "제거", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.setupNetworksFromHost(
	hostId: String,
	hostNics: HostNic,
	networkAttachments: List<NetworkAttachment> = listOf()
): Result<Boolean> = runCatching {
	if (networkAttachments.isEmpty())
		this.srvHost(hostId).setupNetworks().modifiedBonds(hostNics).send()
	else
		this.srvHost(hostId).setupNetworks().modifiedBonds(hostNics).modifiedNetworkAttachments(networkAttachments).send()
	this.srvHost(hostId).commitNetConfig().send()
	true
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.BOND, "설정", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.BOND, "설정", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}

private fun Connection.srvFenceAgentsFromHost(hostId: String): FenceAgentsService =
	this.srvHost(hostId).fenceAgentsService()

fun Connection.findAllFenceAgentsFromHost(hostId: String): Result<List<Agent>> = runCatching {
	this.srvFenceAgentsFromHost(hostId).list().send().agents()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.FENCE_AGENT, "목록조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.FENCE_AGENT, "목록조회", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.addFenceAgent(hostId: String, agent: Agent): Result<Agent?> = runCatching {
	this.srvFenceAgentsFromHost(hostId).add().agent(agent).send().agent()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.FENCE_AGENT, "생성", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.FENCE_AGENT, "생성", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}

private fun Connection.srvAllIscsiDetailsFromHost(hostId: String): HostService.IscsiDiscoverRequest =
	this.srvHost(hostId).iscsiDiscover()

fun Connection.findAllIscsiTargetsFromHost(hostId: String, iscsiDetails: IscsiDetails): Result<List<String>> = runCatching {
	this.srvAllIscsiDetailsFromHost(hostId).iscsi(iscsiDetails).send().iscsiTargets()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.ISCSI_DETAIL, "목록조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.ISCSI_DETAIL, "목록조회", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.findAllCpuUnitFromHost(hostId: String): Result<List<HostCpuUnit>> = runCatching {
	this.srvHost(hostId).cpuUnitsService().list().send().cpuUnits()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.HOST_CPU_UNIT, "목록조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.HOST_CPU_UNIT, "목록조회", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.findAllHostDeviceFromHost(hostId: String): Result<List<HostDevice>> = runCatching {
	if(this.findHost(hostId).isFailure){
		throw ErrorPattern.HOST_NOT_FOUND.toError()
	}
	this.srvHost(hostId).devicesService().list().send().devices() ?: listOf()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.HOST_DEVICES, "목록조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.HOST_DEVICES, "목록조회", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}

fun Connection.findAllPermissionFromHost(hostId: String): Result<List<Permission>> = runCatching {
	if(this.findHost(hostId).isFailure){
		throw ErrorPattern.HOST_NOT_FOUND.toError()
	}
	this.srvHost(hostId).permissionsService().list().send().permissions() ?: listOf()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.PERMISSION, "목록조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.PERMISSION, "목록조회", it, hostId)
	throw if (it is Error) it.toItCloudException() else it
}