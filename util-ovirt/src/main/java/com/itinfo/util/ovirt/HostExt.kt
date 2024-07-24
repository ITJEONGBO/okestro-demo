package com.itinfo.util.ovirt

import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.error.FailureType
import com.itinfo.util.ovirt.error.toError
import com.itinfo.util.ovirt.error.toResult
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.services.*
import org.ovirt.engine.sdk4.types.*
import java.net.InetAddress


private fun Connection.srvHosts(): HostsService =
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
	throw it
}


fun Connection.findHost(hostId: String): Result<Host?> = runCatching {
	srvHost(hostId).get().allContent(true).send().host()
}.onSuccess {
	Term.HOST.logSuccess("상세조회", hostId)
}.onFailure {
	Term.HOST.logFail("상세조회", it, hostId)
	throw it
}

fun Connection.findHostName(hostId: String): String =
	this.srvHost(hostId).get().send().host().name()

fun List<Host>.nameDuplicateHost(hostName: String, hostId: String? = null): Boolean =
	this.filter { it.id() != hostId }.any { it.name() == hostName }

fun Connection.addHost(host: Host, deployHostedEngine: Boolean): Result<Host?> = runCatching {
	if(this.findAllHosts()
			.getOrDefault(listOf())
			.nameDuplicateHost(host.name())) {
		return FailureType.DUPLICATE.toResult(Term.HOST.desc)
	}
	val hostAdded: Host =
		srvHosts().add().deployHostedEngine(deployHostedEngine).host(host).send().host()

	this.expectHostStatus(hostAdded.id(), HostStatus.UP)
	hostAdded
}.onSuccess {
	Term.HOST.logSuccess("생성")
}.onFailure {
	Term.HOST.logFail("생성", it)
	throw it
}

// TODO deploy HostEngine 찾아보기
fun Connection.updateHost(host: Host): Result<Host?> = runCatching {
	if(this.findAllHosts()
			.getOrDefault(listOf())
			.nameDuplicateHost(host.name(), host.id())) {
		return FailureType.DUPLICATE.toResult(Term.HOST.desc)
	}
	val hostUpdated: Host =
		this.srvHost(host.id()).update().host(host).send().host()
	hostUpdated
}.onSuccess {
	Term.HOST.logSuccess("편집", host.id())
}.onFailure {
	Term.HOST.logFail("편집", it, host.id())
	throw it
}

fun Connection.removeHost(hostId: String): Result<Boolean> = runCatching {
	val host: Host =
		this.findHost(hostId).getOrNull() ?: throw ErrorPattern.HOST_NOT_FOUND.toError()
	val hostStatus = host.status()
	if (hostStatus == HostStatus.MAINTENANCE) {
		srvHost(hostId).remove().send()
		this.expectHostDeleted(hostId) /*(true)*/
	} else {
		log.warn("{} 삭제 실패... {} 가 유지관리 상태가 아님 ", Term.HOST.desc, hostId)
		false
	}
}.onSuccess {
	Term.HOST.logSuccess("삭제", hostId)
}.onFailure {
	Term.HOST.logFail("삭제", it, hostId)
	throw it
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
	val host: Host = this.findHost(hostId).getOrNull() ?: throw ErrorPattern.HOST_NOT_FOUND.toError()
	if (host.status() != HostStatus.MAINTENANCE) {
		srvHost(host.id()).deactivate().send()
	} else {
		return Result.failure(Error("deactivateHost 실패 ... {} 가 이미 유지관리 상태"))
	}
	val result = expectHostStatus(host.id(), HostStatus.MAINTENANCE)
	result
}.onSuccess {
	Term.HOST.logSuccess("비활성화", hostId)
}.onFailure {
	Term.HOST.logFail("비활성화", it, hostId)
	throw it
}

fun Connection.activateHost(hostId: String): Result<Boolean> = runCatching {
	val host: Host = this.findHost(hostId).getOrNull() ?: throw ErrorPattern.HOST_NOT_FOUND.toError()
	if (host.status() != HostStatus.UP) {
		srvHost(host.id()).activate().send()
	} else {
		return Result.failure(Error("activateHost 실패 ... ${host.name()}가 이미 활성 상태 "))
	}
	val result = expectHostStatus(host.id(), HostStatus.UP)
	result
}.onSuccess {
	Term.HOST.logSuccess("활성화", hostId)
}.onFailure {
	Term.HOST.logFail("활성화", it, hostId)
	throw it
}


fun Connection.refreshHost(hostId: String): Result<Boolean> = runCatching {
	this.findHost(hostId).getOrNull() ?: throw ErrorPattern.HOST_NOT_FOUND.toError()
	this.srvHost(hostId).refresh().send()
	this@refreshHost.expectHostStatus(hostId, HostStatus.UP)
}.onSuccess {
	Term.HOST.logSuccess("새로고침", hostId)
}.onFailure {
	Term.HOST.logFail("새로고침", it, hostId)
	throw it
}

fun Connection.restartHost(hostId: String): Result<Boolean> = runCatching {
	val host: Host = this.findHost(hostId).getOrNull() ?: throw ErrorPattern.HOST_NOT_FOUND.toError()
	val address: InetAddress = InetAddress.getByName(host.address())
	if (address.rebootHostViaSSH("root", "adminRoot!@#", 22).isFailure)
		return Result.failure(Error("SSH를 통한 호스트 재부팅 실패"))

	Thread.sleep(60000) // 60초 대기, 재부팅 시간 고려
	if (this@restartHost.expectHostStatus(host.id(), HostStatus.UP, 900000, 3000)) {
		true
	} else {
		log.error("재부팅 전환 시간 초과")
		false
	}
}.onSuccess {
	Term.HOST.logSuccess("재부팅", hostId)
}.onFailure {
	Term.HOST.logFail("재부팅", it, hostId)
	throw it
}

/**
 * [InetAddress.rebootHostViaSSH]
 * host SSH 관리 - 재시작 부분
 */
fun InetAddress.rebootHostViaSSH(username: String, password: String, port: Int): Result<Boolean> = runCatching {
	var channel: ChannelExec? = null
	log.debug("ssh 시작")

	// SSH 세션 생성 및 연결
	val session: com.jcraft.jsch.Session = JSch().getSession(username, hostAddress, port)
	session.setPassword(password)
	session.setConfig("StrictHostKeyChecking", "no") // 호스트 키 확인을 건너뛰기 위해 설정
	session.connect()

	channel = session.openChannel("exec") as ChannelExec // SSH 채널 열기
	channel.setCommand("sudo reboot") // 재부팅 명령 실행
	channel.connect()

	// 명령 실행 완료 대기
	while (!channel.isClosed) {
		Thread.sleep(100)
	}

	channel.disconnect()
	session.disconnect()
	val exitStatus = channel.exitStatus
	return Result.success(exitStatus == 0)
}.onSuccess {

}.onFailure {
	log.error(it.localizedMessage)
	throw it
}



fun Connection.findPowerManagementFromHost(hostId: String, fenceType: FenceType): Result<PowerManagement?> = runCatching {
	val res = srvHost(hostId).fence().fenceType(fenceType.name).send().powerManagement()
	log.info("powerManagementFromHost result: $res")
	res
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.POWER_MANAGEMENT,"상세조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.POWER_MANAGEMENT,"상세조회", it, hostId)
	throw it
}

fun Connection.migrateHostFromVm(vmId: String, host: Host): Result<Boolean> = runCatching {
	this.srvVm(vmId).migrate().host(host).send()
	true
}.onSuccess {
	Term.VM.logSuccessWithin(Term.HOST,"이동", vmId)
}.onFailure {
	Term.VM.logFailWithin(Term.HOST,"이동", it, vmId)
	throw it
}

/**
 * [Connection.expectHostStatus]
 * 호스트 상태 체크하는 메소드
 *
 * @param hostService 호스트 서비스
 * @param expectStatus 원하는 호스트 상태
 * @param check 상태 확인 간격(밀리초)
 * @param timeout 최대 대기 시간(밀리초)
 * @return true =pass / false=fail
 * @throws InterruptedException
 */
@Throws(InterruptedException::class)
fun Connection.expectHostStatus(hostId: String, expectStatus: HostStatus, interval: Long = 3000L, timeout: Long = 900000L): Boolean {
	val startTime: Long = System.currentTimeMillis()
	while (true) {
		val currentHost: Host? = this@expectHostStatus.findHost(hostId).getOrNull()
		val status = currentHost?.status()

		if (status == expectStatus) {
			log.info("호스트 생성 완료 ... {}", expectStatus)
			return true
		} else if (System.currentTimeMillis() - startTime > timeout) {
			log.error("호스트 생성 시간 초과: {}", currentHost?.name());
			return false
		}

		log.info("호스트 상태: {} -> expect '{}'", status, expectStatus)
		Thread.sleep(interval)
	}
}

private fun Connection.srvAllNicsFromHost(hostId: String): HostNicsService =
	this.srvHost(hostId).nicsService()

fun Connection.findAllNicsFromHost(hostId: String): Result<List<HostNic>> = runCatching {
	this@findAllNicsFromHost.findHost(hostId).getOrNull() ?: throw ErrorPattern.HOST_NOT_FOUND.toError()
	this.srvAllNicsFromHost(hostId).list().send().nics()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.HOST_NIC,"목록조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.HOST_NIC,"목록조회", it, hostId)
	throw it
}

private fun Connection.srvNicFromHost(hostId: String, hostNicId: String): HostNicService =
	this.srvAllNicsFromHost(hostId).nicService(hostNicId)

fun Connection.findNicFromHost(hostId: String, hostNicId: String): Result<HostNic?> = runCatching {
	this.srvNicFromHost(hostId, hostNicId).get().send().nic()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.HOST_NIC,"상세조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.HOST_NIC,"상세조회", it, hostId)
	throw it
}

fun Connection.findAllStatisticsFromHostNic(hostId: String, hostNicId: String): Result<List<Statistic>> = runCatching {
	this.srvNicFromHost(hostId, hostNicId).statisticsService().list().send().statistics()
}.onSuccess {
	Term.HOST_NIC.logSuccessWithin(Term.STATISTIC,"목록조회", hostId)
}.onFailure {
	Term.HOST_NIC.logFailWithin(Term.STATISTIC,"목록조회", it, hostId)
	throw it
}

private fun Connection.srvStatisticsFromHost(hostId: String): StatisticsService =
	this.srvHost(hostId).statisticsService()

fun Connection.findAllStatisticsFromHost(hostId: String): Result<List<Statistic>> = runCatching {
	this.srvStatisticsFromHost(hostId).list().send().statistics()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.STATISTIC,"목록조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.STATISTIC,"목록조회", it, hostId)
	throw it
}

private fun Connection.srvStoragesFromHost(hostId: String): HostStorageService =
	this.srvHost(hostId).storageService()

fun Connection.findAllStoragesFromHost(hostId: String): Result<List<HostStorage>> = runCatching {
	this.srvStoragesFromHost(hostId).list().send().storages()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.STORAGE,"목록조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.STORAGE,"목록조회", it, hostId)
	throw it
}

private fun Connection.srvNetworkAttachmentsFromHost(hostId: String): NetworkAttachmentsService =
	this.srvHost(hostId).networkAttachmentsService()

fun Connection.findAllNetworkAttachmentsFromHost(hostId: String): Result<List<NetworkAttachment>> = runCatching {
	this.srvNetworkAttachmentsFromHost(hostId).list().send().attachments()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.NETWORK_ATTACHMENT,"목록조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.NETWORK_ATTACHMENT,"목록조회", it, hostId)
	throw it
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
	throw it
}

fun Connection.modifyNetworkAttachmentFromHost(hostId: String, networkAttachment: NetworkAttachment): Result<Boolean> = runCatching {
	this.srvHost(hostId).setupNetworks().modifiedNetworkAttachments(networkAttachment).send()
	this.srvHost(hostId).commitNetConfig().send()
	true
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.NETWORK_ATTACHMENT, "편집", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.NETWORK_ATTACHMENT, "편집", it, hostId)
	throw it
}

fun Connection.updateNetworkAttachmentFromHost(hostId: String, networkAttachmentId: String, networkAttachment: NetworkAttachment): Result<Boolean> = runCatching {
	this.srvNetworkAttachmentFromHost(hostId, networkAttachmentId).update().attachment(networkAttachment).send()
	this.srvHost(hostId).commitNetConfig().send()
	true
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.NETWORK_ATTACHMENT, "편집", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.NETWORK_ATTACHMENT, "편집", it, hostId)
	throw it
}

fun Connection.removeNetworkAttachmentFromHost(hostId: String, networkAttachmentId: String): Boolean = try {
	this.srvNetworkAttachmentFromHost(hostId, networkAttachmentId).remove().send()
	this.srvHost(hostId).commitNetConfig().send()
	true
} catch (e: Error) {
	log.error(e.localizedMessage)
	false
}

fun Connection.removeBondsFromHost(hostId: String, hostNics: List<HostNic> = listOf()): Boolean = try {
	this.srvHost(hostId).setupNetworks().removedBonds(hostNics).send()
	this.srvHost(hostId).commitNetConfig().send()
	true
} catch (e: Error) {
	log.error(e.localizedMessage)
	false
}

fun Connection.setupNetworksFromHost(
	hostId: String,
	hostNics: HostNic,
	networkAttachments: List<NetworkAttachment> = listOf()
): Boolean = try {
	if (networkAttachments.isEmpty())
		this.srvHost(hostId).setupNetworks().modifiedBonds(hostNics).send()
	else
		this.srvHost(hostId).setupNetworks().modifiedBonds(hostNics).modifiedNetworkAttachments(networkAttachments).send()
	this.srvHost(hostId).commitNetConfig().send()
	true
} catch (e: Error) {
	log.error(e.localizedMessage)
	false
}

private fun Connection.srvFenceAgentsFromHost(hostId: String): FenceAgentsService =
	this.srvHost(hostId).fenceAgentsService()

fun Connection.findAllFenceAgentsFromHost(hostId: String): Result<List<Agent>> = runCatching {
	this.srvFenceAgentsFromHost(hostId).list().send().agents()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.FENCE_AGENT, "목록조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.FENCE_AGENT, "목록조회", it, hostId)
	throw it
}

fun Connection.addFenceAgent(hostId: String, agent: Agent): Result<Agent?> = runCatching {
	this.srvFenceAgentsFromHost(hostId).add().agent(agent).send().agent()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.FENCE_AGENT, "생성", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.FENCE_AGENT, "생성", it, hostId)
	throw it
}

private fun Connection.srvAllIscsiDetailsFromHost(hostId: String): HostService.IscsiDiscoverRequest =
	this.srvHost(hostId).iscsiDiscover()

fun Connection.findAllIscsiTargetsFromHost(hostId: String, iscsiDetails: IscsiDetails): Result<List<String>> = runCatching {
	this.srvAllIscsiDetailsFromHost(hostId).iscsi(iscsiDetails).send().iscsiTargets()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.ISCSI_DETAIL, "목록조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.ISCSI_DETAIL, "목록조회", it, hostId)
	throw it
}

fun Connection.findAllCpuUnitFromHost(hostId: String): Result<List<HostCpuUnit>> = runCatching {
	this.srvHost(hostId).cpuUnitsService().list().send().cpuUnits()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.HOST_CPU_UNIT, "목록조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.HOST_CPU_UNIT, "목록조회", it, hostId)
	throw it
}

fun Connection.findAllHostDeviceFromHost(hostId: String): Result<List<HostDevice>> = runCatching {
	this.srvHost(hostId).devicesService().list().send().devices()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.HOST_DEVICES, "목록조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.HOST_DEVICES, "목록조회", it, hostId)
	throw it
}

fun Connection.findAllPermissionFromHost(hostId: String): Result<List<Permission>> = runCatching {
	this.srvHost(hostId).permissionsService().list().send().permissions()
}.onSuccess {
	Term.HOST.logSuccessWithin(Term.PERMISSION, "목록조회", hostId)
}.onFailure {
	Term.HOST.logFailWithin(Term.PERMISSION, "목록조회", it, hostId)
	throw it
}