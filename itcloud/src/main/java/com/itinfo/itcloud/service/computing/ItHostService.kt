package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.HostNicVo
import com.itinfo.itcloud.model.network.NetworkVo
import com.itinfo.itcloud.model.network.toHostNicVos
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.setting.toPermissionVos
import com.itinfo.itcloud.repository.*
import com.itinfo.itcloud.repository.history.*
import com.itinfo.itcloud.repository.history.dto.UsageDto
import com.itinfo.itcloud.repository.history.entity.HostInterfaceSamplesHistoryEntity
import com.itinfo.itcloud.repository.history.entity.HostSamplesHistoryEntity
import com.itinfo.itcloud.repository.history.entity.getUsage
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.types.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

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
	 * @param hostId [String] 호스트 Id
	 * @return [HostVo]?
	 */
	@Throws(Error::class)
	fun findOne(hostId: String): HostVo?

	// 호스트 생성창 - 클러스터 목록 [ItClusterService.findAll]

	/**
	 * [ItHostService.add]
	 * 호스트 생성 (전원관리 제외)
	 *
	 * @param hostVo [HostVo]
	 * @return [HostVo]?
	 */
	@Throws(Error::class)
	fun add(hostVo: HostVo): HostVo?
	/**
	 * [ItHostService.update]
	 * 호스트 편집 (전원관리 제외)
	 *
	 * @param hostVo [HostVo]
	 * @return [HostVo]?
	 */
	@Throws(Error::class)
	fun update(hostVo: HostVo): HostVo?
	/**
	 * [ItHostService.remove]
	 * 호스트 삭제
	 * 가상머신 돌아가는게 있는지 -> 유지보수 상태인지 -> 삭제
	 *
	 * @param hostId [String] 호스트 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun remove(hostId: String): Boolean
	/**
	 * [ItHostService.findAllVmsFromHost]
	 * 호스트 가상머신 목록
	 *
	 * @param hostId [String] 호스트 Id
	 * @return List<[VmVo]> 가상머신 목록
	 */
	@Throws(Error::class)
	fun findAllVmsFromHost(hostId: String): List<VmVo>
	// 생성
	// 편집
	// 삭제
	// 실행
	// 일시중지
	// 종료
	// 재부팅
	// 마이그레이션

	/**
	 * [ItHostService.findAllNicsFromHost]
	 * 호스트 네트워크 인터페이스 목록
	 *
	 * @param hostId [String] 호스트 Id
	 * @return List<[HostNicVo]> 네트워크 인터페이스 목록
	 */
	@Throws(Error::class)
	fun findAllNicsFromHost(hostId: String): List<HostNicVo>
	/**
	 * [ItHostService.setUpNetworksFromHost]
	 * 호스트 네트워크 설정
	 *
	 * @param hostId [String] 호스트 Id
	 * @param network [NetworkVo] 네트워크 (미정)
	 * @return [Boolean] 아직미정
	 */
	@Throws(Error::class)
	fun setUpNetworksFromHost(hostId: String, network: NetworkVo): Boolean
	/**
	 * [ItHostService.findAllHostDevicesFromHost]
	 * 호스트 호스트장치 목록
	 *
	 *  @param hostId [String] 호스트 Id
	 *  @return List<[HostDeviceVo]> 호스트장치 목록
	 */
	@Throws(Error::class)
	fun findAllHostDevicesFromHost(hostId: String): List<HostDeviceVo>
	/**
	 * [ItHostService.findAllEventsFromHost]
	 * 호스트 이벤트 목록
	 *
	 * @param hostId [String] 호스트 Id
	 * @return List<[EventVo]> 이벤트 목록
	 */
	@Throws(Error::class)
	fun findAllEventsFromHost(hostId: String): List<EventVo>

	/**
	 * [ItHostService.findAllPermissionsFromHost]
	 * 호스트 권한 목록
	 *
	 *  @param hostId [String] 호스트 Id
	 *  @return List<[PermissionVo]> 권한 목록
	 */
	@Deprecated("필요없음")
	@Throws(Error::class)
	fun findAllPermissionsFromHost(hostId: String): List<PermissionVo>
}

@Service
class HostServiceImpl(

): BaseService(), ItHostService {
	@Autowired private lateinit var hostConfigurationRepository: HostConfigurationRepository
	@Autowired private lateinit var hostSamplesHistoryRepository: HostSamplesHistoryRepository
	@Autowired private lateinit var hostInterfaceSampleHistoryRepository: HostInterfaceSampleHistoryRepository
	@Autowired private lateinit var vmSamplesHistoryRepository: VmSamplesHistoryRepository
	@Autowired private lateinit var vmInterfaceSamplesHistoryRepository: VmInterfaceSamplesHistoryRepository
	@Autowired private lateinit var itGraphService: ItGraphService

	@Throws(Error::class)
	override fun findAll(): List<HostVo> {
		log.info("findAll ... ")
		val hosts: List<Host> =
			conn.findAllHosts()
				.getOrDefault(listOf()) // hosted Engine의 정보가 나온다
		return hosts.map { host ->
			val hostNic: HostNic? =
				conn.findAllNicsFromHost(host.id()).getOrDefault(listOf()).firstOrNull()
			val usageDto: UsageDto? = hostNic?.id()?.let { itGraphService.hostPercent(host.id(), it) }
			host.toHostMenu(conn, usageDto)
		}
	}

	@Throws(Error::class)
	override fun findOne(hostId: String): HostVo? {
		log.info("findOne ... hostId: {}", hostId)
		val res: Host? =
			conn.findHost(hostId)
				.getOrNull()
		return res?.toHostVo(conn)
	}

	@Throws(Error::class)
	override fun add(hostVo: HostVo): HostVo? {
		log.info("add ... ")
		val res: Host? =
			conn.addHost(
				hostVo.toAddHostBuilder(),
				hostVo.hostedEngine
			)
			.getOrNull()
		return res?.toHostVo(conn)
	}

	@Throws(Error::class)
	override fun update(hostVo: HostVo): HostVo? {
		log.info("update ... hostName: {}", hostVo.name)
		// TODO
		//  com.itinfo.util.ovirt.error.ItCloudException: Fault reason is 'Operation Failed'. Fault detail is '[Cannot edit Host. Host parameters cannot be modified while Host is operational.
		//  Please switch Host to Maintenance mode first.]'. HTTP response code is '409'. HTTP response message is 'Conflict'.
		val res: Host? =
			conn.updateHost(hostVo.toEditHostBuilder())
				.getOrNull()
		return res?.toHostVo(conn)
	}

	@Throws(Error::class)
	override fun remove(hostId: String): Boolean {
		log.info("remove ... hostId: {}", hostId)
		val res: Result<Boolean> =
			conn.removeHost(hostId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun findAllVmsFromHost(hostId: String): List<VmVo> {
		log.info("findAllVmsFromHost ... hostId: {}", hostId)
		val res: List<Vm> =
			conn.findAllVmsFromHost(hostId)
				.getOrDefault(listOf())
		return res.toVmsMenu(conn)
	}

	@Throws(Error::class)
	override fun findAllNicsFromHost(hostId: String): List<HostNicVo> {
		log.info("findAllNicsFromHost ... hostId: {}", hostId)
		val res: List<HostNic> =
			conn.findAllNicsFromHost(hostId)
				.getOrDefault(listOf())
		// TODO
		return res.toHostNicVos(conn)
	}

	@Throws(Error::class)
	override fun setUpNetworksFromHost(hostId: String, network: NetworkVo): Boolean {
		TODO("Not yet implemented")
	}

	@Throws(Error::class)
	override fun findAllHostDevicesFromHost(hostId: String): List<HostDeviceVo> {
		log.info("findAllHostDevicesFromHost ... hostId: {}", hostId)
		val res: List<HostDevice> =
			conn.findAllHostDeviceFromHost(hostId)
				.getOrDefault(listOf())
		return res.toHostDeviceVos(conn)
	}

	@Throws(Error::class)
	override fun findAllEventsFromHost(hostId: String): List<EventVo> {
		log.info("findAllEventsFromHost ... ")
		val host: Host =
			conn.findHost(hostId)
				.getOrNull()?: throw ErrorPattern.HOST_NOT_FOUND.toException()

		// TODO 호스트 이벤트 불러오기 애매 + power management 처리
		val res: List<Event> =
			conn.findAllEvents("host.name= ${host.name()}")
				.getOrDefault(listOf())
				.filter {
					!(
					  it.severity().value() == "alert" &&
					  it.description().contains("Failed to verify Power Management configuration for Host")
					)
				}
		return res.toEventVos()
	}

	@Deprecated("필요없음")
	@Throws(Error::class)
	override fun findAllPermissionsFromHost(hostId: String): List<PermissionVo> {
		log.info("findAllPermissionsFromHost ... hostId: {}", hostId)
		val res: List<Permission> =
			conn.findAllPermissionFromHost(hostId)
				.getOrDefault(listOf())
		return res.toPermissionVos(conn)
	}


	companion object {
		private val log by LoggerDelegate()
	}
}