package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.*
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.*
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.setting.toPermissionVos
import com.itinfo.itcloud.model.storage.*
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import org.ovirt.engine.sdk4.builders.*
import org.ovirt.engine.sdk4.types.*
import org.springframework.stereotype.Service
import kotlin.Error

interface ItVmService {
	/**
	 * [ItVmService.findAll]
	 * 가상머신 목록
	 *
	 * @return List<[VmVo]> 가상머신 목록
	 */
	@Throws(Error::class)
	fun findAll(): List<VmVo>
	/**
	 * [ItVmService.findOne]
	 * 가상머신 정보, 편집
	 *
	 * @param vmId [String] 가상머신 id
	 * @return [VmVo]
	 */
	@Throws(Error::class)
	fun findOne(vmId: String): VmVo?

	// 가상머신 생성창 - 클러스터 목록 [ItClusterService.findAll]
	// 가상머신 생성창 - 템플릿 목록 [ItTemplateService.findAll]
	// 가상머신 생성창 - 호스트 목록 [ItClusterService.findAllHostsFromCluster] (vo 다름)
	// 가상머신 생성창 - 스토리지 도메인 목록 [ItStorageService.findAllDomainsFromDataCenter] (vo 다름)
	// 인스턴스 이미지 -> 생성 시 필요한 스토리지 도메인 / 고가용성 - 임대 대상 스토리지 도메인 목록

	// 가상머신 생성창 - 디스크 프로파일 목록 [ItStorageService.findAllDiskProfilesFromStorageDomain]
	// 인스턴스 이미지 생성 -> 스토리지 도메인과 연동되어 생기는

	/**
	 * [ItVmService.findAllDiskImage]
	 * 가상머신 생성 - 인스턴스 이미지 - 연결 -> 디스크 목록
	 * 기준: 아무것도 연결되어 있지 않은 디스크
	 *
	 * @return List<[DiskImageVo]> 디스크  목록
	 */
	@Throws(Error::class)
	fun findAllDiskImage(): List<DiskImageVo>

	/**
	 * [ItVmService.findAllVnicProfilesFromCluster]
	 * 가상머신 생성 -  vNic-vnicprofile 목록 출력 (가상머신 생성, 네트워크 인터페이스 생성)
	 *
	 * @param clusterId [String] 클러스터 id
	 * @return List<[VnicProfileVo]> VnicProfile 목록
	 */
	@Throws(Error::class)
	fun findAllVnicProfilesFromCluster(clusterId: String): List<VnicProfileVo>

	/**
	 * [ItVmService.findAllCpuProfilesFromCluster]
	 * 가상머신 생성 - 리소스할당 - cpuProfile 목록 출력
	 *
	 * @param clusterId [String] 클러스터 id
	 * @return List<[CpuProfileVo]> cpuProfile 목록
	 */
    @Throws(Error::class)
	fun findAllCpuProfilesFromCluster(clusterId: String): List<CpuProfileVo>
	/**
	 * [ItVmService.findAllISO]
	 * 가상머신 생성 - 부트 옵션 - 생성 시 필요한 CD/DVD 연결할 ISO 목록 (디스크이미지)
	 *
	 * @return List<[IdentifiedVo]> ISO 목록
	 */
    @Throws(Error::class)
	fun findAllISO(): List<IdentifiedVo>


	/**
	 * [ItVmService.add]
	 * 가상머신 생성
	 *
	 * @param vmVo [VmVo] 가상머신 객체
	 * @return [VmVo] 가상머신 정보
	 */
	@Throws(Error::class)
	fun add(vmVo: VmVo): VmVo?
	/**
	 * [ItVmService.update]
	 * 가상머신 편집
	 *
	 * @param vmVo [VmVo]
	 * @return [VmVo] 가상머신 정보
	 */
	@Throws(Error::class)
	fun update(vmVo: VmVo): VmVo?
	/**
	 * [ItVmService.remove]
	 * 가상머신 삭제
	 *
	 * @param vmId [String] 가상머신 id
	 * @param disk [Boolean] disk 삭제여부, disk가 true면 디스크 삭제하라는 말
	 * // detachOnly => true==가상머신만 삭제/ false==가상머신+디스크 삭제
	 * @return CommonVo<[Boolean]> 200(success) 404(fail)
	 */
	@Throws(Error::class)
	fun remove(vmId: String, disk: Boolean): Boolean

	// 스냅샷 생성은 스냅샷에서 api로 연결

	// 네트워크 인터페이스, 디스크, 스냅샷은 따른 서비스로

	/**
	 * [ItVmService.findAllApplicationsFromVm]
	 *
	 * @param vmId [String] 가상머신 id
	 */
	@Throws(Error::class)
	fun findAllApplicationsFromVm(vmId: String): List<IdentifiedVo>
	/**
	 * [ItVmService.findGuestFromVm]
	 *
	 * @param vmId [String] 가상머신 id
	 */
	@Throws(Error::class)
	fun findGuestFromVm(vmId: String): GuestInfoVo?
	/**
	 * [ItVmService.findAllPermissionsFromVm]
	 *
	 * @param vmId [String] 가상머신 id
	 */
	@Throws(Error::class)
	fun findAllPermissionsFromVm(vmId: String): List<PermissionVo>
	/**
	 * [ItVmService.findAllEventsFromVm]
	 * 이벤트
	 *
	 * @param vmId [String] 가상머신 id
	 */
	fun findAllEventsFromVm(vmId: String): List<EventVo>
	/**
	 * [ItVmService.findConsole]
	 *
	 * @param vmId [String] 가상머신 id
	 */
	fun findConsole(vmId: String): ConsoleVo?
}

@Service
class VmServiceImpl(

) : BaseService(), ItVmService {

	@Throws(Error::class)
	override fun findAll(): List<VmVo> {
		log.info("findAll ... ")
		val res: List<Vm> =
			conn.findAllVms()
				.getOrDefault(listOf())
		return res.toVmVos(conn)
	}

	override fun findOne(vmId: String): VmVo? {
		log.info("findOne ... vmId : {}", vmId)
		val res: Vm? =
			conn.findVm(vmId).getOrNull()
		return res?.toVmVo(conn)
	}

	override fun findAllDiskImage(): List<DiskImageVo> {
		log.info("findAllDiskImage ... ")
		val attDiskIds =
			conn.findAllVms()
				.getOrDefault(listOf())
				.flatMap {
					conn.findAllDiskAttachmentsFromVm(it.id())
						.getOrDefault(listOf())
				}.map { it.id() }

		val res: List<Disk> =
			conn.findAllDisks()
				.getOrDefault(listOf())
				.filter {
					(it.storageType() == DiskStorageType.IMAGE || it.storageType() == DiskStorageType.LUN) &&
					(it.contentType() == DiskContentType.DATA) && !attDiskIds.contains(it.id())
				}
		return res.toDiskImageVos(conn)
	}

	override fun findAllVnicProfilesFromCluster(clusterId: String): List<VnicProfileVo> {
		log.info("findAllVnicProfilesFromCluster ... clusterId: {}", clusterId)
		val cluster: Cluster =
			conn.findCluster(clusterId)
				.getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toException()

		// 데이터 센터가 같아야함
		val res: List<VnicProfile> =
			conn.findAllVnicProfiles()
				.getOrDefault(listOf())
				.filter {
					it.network()?.dataCenter()?.id() == cluster.dataCenter().id()
				}
		return res.toVnicProfileVos(conn)
	}

	override fun findAllCpuProfilesFromCluster(clusterId: String): List<CpuProfileVo> {
		log.info("findAllCpuProfilesFromCluster ... clusterId: {}", clusterId)
		conn.findCluster(clusterId)
			.getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toException()

		val res: List<CpuProfile> =
			conn.findAllCpuProfiles()
				.getOrDefault(listOf())
				.filter { it.cluster().id() == clusterId }
		return res.toCpuProfileVos()
	}

	override fun findAllISO(): List<IdentifiedVo> {
		log.info("findAllISO ... ")
		val res: List<Disk> =
			conn.findAllDisks()
				.getOrDefault(listOf())
				.filter { it.contentType() == DiskContentType.ISO }
		return res.fromDisksToIdentifiedVos()
	}


	override fun add(vmVo: VmVo): VmVo? {
		log.info("add ... ")
		// TODO: 파라미터에 대한 처리 내용 검토 필요
//		val res: Vm? =
//			conn.addVm(
//				vmVo.toAddVmBuilder(conn),
//				vmVo.vnicProfileVos.map { it.id },
//				vmVo.diskAttachmentVos.map { it.id },
//			)
//			.getOrNull()
//		return res?.toVmVo(conn)
		TODO("Not yet implemented")
	}

	override fun update(vmVo: VmVo): VmVo? {
		log.info("update ... ")
		val res: Vm? =
			conn.updateVm(vmVo.toEditVmBuilder(conn))
				.getOrNull()
		return res?.toVmVo(conn)
	}

	override fun remove(vmId: String, disk: Boolean): Boolean {
		log.info("remove ...  vmName: {}", conn.findVmName(vmId))
		val res: Result<Boolean> =
			conn.removeVm(vmId, disk)
		return res.isSuccess
	}


	override fun findAllApplicationsFromVm(vmId: String): List<IdentifiedVo> {
		log.info("findAllApplicationsFromVm ... vmId: {}", vmId)
		val res: List<Application> =
			conn.findAllApplicationsFromVm(vmId)
				.getOrDefault(listOf())
		return res.fromApplicationsToIdentifiedVos()
	}

	override fun findGuestFromVm(vmId: String): GuestInfoVo? {
		log.info("findGuestFromVm ... vmId: {}", vmId)
		val res: Vm =
			conn.findVm(vmId)
				.getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toException()
		if (!res.guestOperatingSystemPresent()) {
			log.warn("게스트 운영 체제 정보가 없습니다.")
			return null
		}
		return res.toGuestInfoVo()
	}

	override fun findAllPermissionsFromVm(vmId: String): List<PermissionVo> {
		log.info("findAllPermissionsFromVm ... vmId: {}", vmId)
		val res: List<Permission> =
			conn.findAllAssignedPermissionsFromVm(vmId)
				.getOrDefault(listOf())
		return res.toPermissionVos(conn)
	}

	override fun findAllEventsFromVm(vmId: String): List<EventVo> {
		log.info("findAllEventsFromVm ... vmId: {}", vmId)
		val vm: Vm =
			conn.findVm(vmId)
				.getOrNull()?:throw ErrorPattern.VM_NOT_FOUND.toException()
		val res: List<Event> =
			conn.findAllEvents()
				.getOrDefault(listOf())
				.filter { it.vmPresent() && it.vm().name() == vm.name() }
		return res.toEventVos()
	}

	override fun findConsole(vmId: String): ConsoleVo? {
		log.info("findConsole ... vmId: {}", vmId)
		val res: Vm =
			conn.findVm(vmId)
				.getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toException()
		return res.toConsoleVo(conn, systemPropertiesVo)
	}

	companion object {
		private val log by LoggerDelegate()
	}
}