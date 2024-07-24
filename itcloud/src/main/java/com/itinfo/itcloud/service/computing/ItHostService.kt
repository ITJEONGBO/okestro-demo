package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.HostNicVo
import com.itinfo.itcloud.model.network.NicVo
import com.itinfo.itcloud.model.network.toHostNicVos
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.setting.toPermissionVos
import com.itinfo.itcloud.repository.*
import com.itinfo.itcloud.service.BaseService
import com.itinfo.itcloud.service.computing.HostOperationServiceImpl.Companion
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.error.toError
import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.types.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface ItHostService {
	/**
	 * [ItHostService.findAll]
	 * 호스트 목록
	 * 
	 * @return List<[HostVo]> 호스트 목록
	 */
	@Throws(Error::class)
	fun findAll(): List<HostVo>
	/**
	 * [ItHostService.findOne]
	 * 호스트 상세정보
	 *
	 * @param hostId [String] 호스트 아이디
	 * @return [HostVo] 호스트 정보
	 */
	@Throws(Error::class)
	fun findOne(hostId: String): HostVo?
	/**
	 * [ItHostService.findAllClustersFromHost]
	 * 호스트 생성 - 클러스터 목록 출력
	 *
	 * @return 클러스터 목록
	 */
	@Deprecated("[ItClusterService.findAll] 내용 같음")
	@Throws(Error::class)
	fun findAllClustersFromHost(): List<ClusterVo>
	/**
	 * [ItHostService.add]
	 * 호스트 생성 (전원관리 제외)
	 *
	 * @param hostVo [HostVo] 호스트 객체
	 * @return [HostVo]
	 */
	@Throws(Error::class)
	fun add(hostVo: HostVo): HostVo?
	/**
	 * [ItHostService.update]
	 * 호스트 편집 (전원관리 제외)
	 *
	 * @param hostVo [HostVo] 호스트 객체
	 * @return [HostVo]
	 */
	@Throws(Error::class)
	fun update(hostVo: HostVo): HostVo?
	/**
	 * [ItHostService.remove]
	 * 호스트 삭제
	 * 삭제 여부 = 가상머신 돌아가는게 있는지 -> 유지보수 상태인지 -> 삭제
	 *
	 * @param hostId [String] 호스트 id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun remove(hostId: String): Boolean
	/**
	 * [ItHostService.findAllVmsFromHost]
	 * 호스트 가상머신 목록
	 *
	 * @param hostId [String] 호스트 아이디
	 * @return List<[VmVo]>? 가상머신 목록
	 */
	@Throws(Error::class)
	fun findAllVmsFromHost(hostId: String): List<VmVo>
	/**
	 * [ItHostService.findAllHostNicsFromHost]
	 * 호스트 네트워크 인터페이스 목록
	 *
	 * @param hostId [String] 호스트 아이디
	 * @return List<[HostNicVo]>? 네트워크 인터페이스 목록
	 */
	@Throws(Error::class)
	fun findAllHostNicsFromHost(hostId: String): List<HostNicVo>
	// 호스트 네트워크 설정
	/**
	 * [ItHostService.findAllHostDevicesFromHost]
	 * 호스트 호스트 장치 목록
	 *
	 *  @param hostId [String] 호스트 아이디
	 *  @return List<[HostDeviceVo]> 호스트 장치 목록
	 */
	@Throws(Error::class)
	fun findAllHostDevicesFromHost(hostId: String): List<HostDeviceVo>
	/**
	 * [ItHostService.findAllPermissionsFromHost]
	 * 호스트 권한 목록
	 *
	 *  @param hostId [String] 호스트 아이디
	 *  @return List<[PermissionVo]> 권한 목록
	 */
	@Throws(Error::class)
	fun findAllPermissionsFromHost(hostId: String): List<PermissionVo>
	/**
	 * [ItHostService.findAllAffinityLabelsFromHost]
	 * 호스트 선호도 레이블 목록
	 *
	 *  @param hostId [String] 호스트 아이디
	 *  @return List<[AffinityLabelVo]>? 선호도 레이블 목록
	 */
	@Throws(Error::class)
	fun findAllAffinityLabelsFromHost(hostId: String): List<AffinityLabelVo>
	/**
	 * [ItHostService.findAllEventsFromHost]
	 * 호스트 이벤트 목록
	 *
	 * @param hostId [String] 호스트 아이디
	 * @return List<[EventVo]>? 이벤트 목록
	 */
	@Throws(Error::class)
	fun findAllEventsFromHost(hostId: String): List<EventVo>
}

@Service
class HostServiceImpl(

): BaseService(), ItHostService {
	@Autowired private lateinit var hostConfigurationRepository: HostConfigurationRepository
	@Autowired private lateinit var hostSamplesHistoryRepository: HostSamplesHistoryRepository
	@Autowired private lateinit var hostInterfaceSampleHistoryRepository: HostInterfaceSampleHistoryRepository
	@Autowired private lateinit var vmSamplesHistoryRepository: VmSamplesHistoryRepository
	@Autowired private lateinit var vmInterfaceSamplesHistoryRepository: VmInterfaceSamplesHistoryRepository

	@Throws(Error::class)
	override fun findAll(): List<HostVo> {
		log.info("findAll ... ")
		val hosts: List<Host> =
			conn.findAllHosts()
				.getOrDefault(listOf()) // hosted Engine의 정보가 나온다
		return hosts.toHostVos(conn)
	}

	@Throws(Error::class)
	override fun findOne(hostId: String): HostVo? {
		log.info("findOne ... hostId: {}", hostId)
		val res: Host =
			conn.findHost(hostId)
				.getOrNull()?: throw ErrorPattern.HOST_NOT_FOUND.toError()
		return res.toHostVo(conn)
	}

	@Deprecated("[ItClusterService.findAll] 내용 같음")
	@Throws(Error::class)
	override fun findAllClustersFromHost(): List<ClusterVo> {
		log.info("findAllClustersFromHost ... ")
		val res: List<Cluster> =
			conn.findAllClusters()
				.getOrDefault(listOf())
				.filter { it.cpuPresent() }
		return res.toClusterVos(conn)
	}

	@Throws(Error::class)
	override fun add(hostVo: HostVo): HostVo? {
		log.info("add ... ")
		// ssh port가 22면 .ssh() 설정하지 않아도 알아서 지정됨.
		// ssh port 변경을 ovirt에서 해보신적은 없어서 우선 보류 (cmd로 하셨음)
		// 비밀번호 잘못되면 보여줄 코드?
		val res: Host =
			conn.addHost(hostVo.toAddHostBuilder(), hostVo.hostedEngine)
				.getOrNull()?: throw ErrorPattern.HOST_NOT_FOUND.toError()
		return res.toHostVo(conn)
	}

	@Throws(Error::class)
	override fun update(hostVo: HostVo): HostVo? {
		log.info("update ... hostName: {}", hostVo.name)
		val res: Host =
			conn.updateHost(hostVo.toEditHostBuilder())
				.getOrNull()?: throw ErrorPattern.HOST_NOT_FOUND.toError()
		return res.toHostVo(conn)
	}

	@Throws(Error::class)
	override fun remove(hostId: String): Boolean {
		log.info("remove ... hostName: {}", conn.findHostName(hostId))
		conn.findHost(hostId).getOrNull()?: throw ErrorPattern.HOST_NOT_FOUND.toError()
		val res: Result<Boolean> =
			conn.removeHost(hostId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun findAllVmsFromHost(hostId: String): List<VmVo> {
		log.info("findAllVmsFromHost ... hostId: {}", hostId)
		conn.findHost(hostId).getOrNull()?: throw ErrorPattern.HOST_NOT_FOUND.toError()
		val res: List<Vm> =
			conn.findAllVms()
				.getOrDefault(listOf())
				.filter {
					(it.hostPresent() && it.host().id() == hostId) ||
					(it.placementPolicy().hostsPresent()
							&& it.placementPolicy().hosts()
								.any { h -> h?.id() == hostId })
				}
		return res.toVmVosFromHost(conn)
	}

	@Throws(Error::class)
	override fun findAllHostNicsFromHost(hostId: String): List<HostNicVo> {
		log.info("findAllHostNicsFromHost ... hostId: {}", hostId)
		conn.findHost(hostId).getOrNull()?: throw ErrorPattern.HOST_NOT_FOUND.toError()
		val res: List<HostNic> =
			conn.findAllNicsFromHost(hostId)
				.getOrDefault(listOf())
		return res.toHostNicVos(conn)
	}

	@Throws(Error::class)
	override fun findAllHostDevicesFromHost(hostId: String): List<HostDeviceVo> {
		log.info("findAllHostDevicesFromHost ... hostId: {}", hostId)
		conn.findHost(hostId).getOrNull()?: throw ErrorPattern.HOST_NOT_FOUND.toError()
		val res: List<HostDevice> =
			conn.findAllHostDeviceFromHost(hostId)
				.getOrDefault(listOf())
		return res.toHostDeviceVos(conn)
	}

	@Throws(Error::class)
	override fun findAllPermissionsFromHost(hostId: String): List<PermissionVo> {
		log.info("findAllPermissionsFromHost ... hostId: {}", hostId)
		conn.findHost(hostId).getOrNull()?: throw ErrorPattern.HOST_NOT_FOUND.toError()
		val res: List<Permission> =
			conn.findAllPermissionFromHost(hostId)
				.getOrDefault(listOf())
		return res.toPermissionVos(conn)
	}

	@Throws(Error::class)
	override fun findAllAffinityLabelsFromHost(hostId: String): List<AffinityLabelVo> {
		log.info("findAllAffinityLabelsFromHost ... hostId: {}", hostId)
		conn.findHost(hostId).getOrNull()?: throw ErrorPattern.HOST_NOT_FOUND.toError()

		TODO("Not yet implemented")
	}

	@Throws(Error::class)
	override fun findAllEventsFromHost(hostId: String): List<EventVo> {
		log.info("findAllEventsFromHost ... ")
		val host: Host =
			conn.findHost(hostId)
				.getOrNull()?: throw ErrorPattern.HOST_NOT_FOUND.toError()

		val res: List<Event> =
			conn.findAllEvents("host.name= ${host.name()}")
				.getOrDefault(listOf())
				.filter {
					//TODO
					!(
					  it.severity().value() == "alert" &&
					  it.description().contains("Failed to verify Power Management configuration for Host")
					)
				}
		return res.toEventVos()
	}

	companion object {
		private val log by LoggerDelegate()
	}
}