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
	/**
	 * [ItVmService.findAllCluster]
	 * 가상머신 생성 - 클러스터 목록
	 *
	 * @return List<[ClusterVo]> 클러스터 목록
	 */
	@Deprecated("[ItClusterService.findAll] 내용")
	fun findAllCluster(): List<ClusterVo>
	/**
	 * [ItVmService.findAllTemplate]
	 *  가상머신 생성 - 템플릿 목록
	 *
	 * @return List<[TemplateVo]> 템플릿 목록
	 */
	@Deprecated("[ItTemplateService.findAll] 내용")
	fun findAllTemplate(): List<TemplateVo>
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
	 * [ItVmService.findAllStorageDomainFromDataCenter]
	 * 가상머신 생성 - 인스턴스 이미지 - 생성 -> 생성 시 필요한 스토리지 도메인
	 * 가상머신 생성 - 고가용성 - 임대 대상 스토리지 도메인 목록
	 * HELP 데이터센터 id가 있어야되는거 같긴함, 근데 애매한부분
	 *
	 * @param dataCenterId [String] 데이터센터 id (클러스터가 가지고 있는 데이터센터 id)
	 * @return List<[StorageDomainVo]> 스토리지 도메인 목록
	 */
	@Deprecated("[ItStorageService.findAllStorageDomainsFromDataCenter] 내용")
	@Throws(Error::class)
	fun findAllStorageDomainFromDataCenter(dataCenterId: String): List<StorageDomainVo>
	/**
	 * [ItVmService.findAllDiskProfileFromStorageDomain]
	 * 가상머신 생성 - 인스턴스 이미지 - 생성 -> 스토리지 도메인과 연동되어 생기는 디스크 프로파일
	 *
	 * @param storageDomainId [String] 스토리지 도메인 id
	 * @return List<[DiskProfileVo]> 디스크 프로파일 목록
	 */
	@Deprecated("[ItStorageService.findOneDiskProfile] 내용")
	@Throws(Error::class)
	fun findAllDiskProfileFromStorageDomain(storageDomainId: String) : List<DiskProfileVo>
	/**
	 * [ItVmService.findAllVnicProfileFromCluster]
	 * 가상머신 생성 -  vNic-vnicprofile 목록 출력 (가상머신 생성, 네트워크 인터페이스 생성)
	 *
	 * @param clusterId [String] 클러스터 id
	 * @return List<[VnicProfileVo]> VnicProfile 목록
	 */
	@Throws(Error::class)
	fun findAllVnicProfileFromCluster(clusterId: String): List<VnicProfileVo>
	/**
	 * [ItVmService.findAllHostFromCluster]
	 * 가상머신 생성 - 호스트 - 호스트 목록
	 *
	 * @param clusterId [String] 클러스터 id
	 * @return List<[HostVo]> 호스트 목록
	 */
	@Deprecated("[ItClusterService.findAllHostFromCluster] 내용과 비슷함")
	@Throws(Error::class)
	fun findAllHostFromCluster(clusterId: String): List<HostVo>
	/**
	 * [ItVmService.findAllCpuProfileFromCluster]
	 * 가상머신 생성 - 리소스할당 - cpuProfile 목록 출력
	 *
	 * @param clusterId [String] 클러스터 id
	 * @return List<[CpuProfileVo]> cpuProfile 목록
	 */
    @Throws(Error::class)
	fun findAllCpuProfileFromCluster(clusterId: String): List<CpuProfileVo>
	/**
	 * [ItVmService.findAllISO]
	 * 가상머신 생성 - 부트 옵션 - 생성 시 필요한 CD/DVD 연결할 ISO 목록 (디스크이미지)
	 *
	 * @return List<[IdentifiedVo]> ISO 목록
	 */
    @Throws(Error::class)
	fun findAllISO(): List<IdentifiedVo>

	// 선호도 그룹/레이블은 나중구현 가능
	/**
	 * [ItVmService.findAllAffinityGroupFromCluster]
	 * 가상머신 생성 - 선호도 - 선호도 그룹 목록
	 *
	 * @param clusterId [String] 클러스터 id
	 * @return List<[IdentifiedVo]> 선호도 그룹 목록
	 */
	@Deprecated("선호도 나중 구현")
	@Throws(Error::class)
	fun findAllAffinityGroupFromCluster(clusterId: String): List<IdentifiedVo>
	/**
	 * [ItVmService.findAllAffinityLabel]
	 * 가상머신 생성 - 선호도 - 선호도 레이블 목록
	 * 기준을 모르겠음
	 *
	 * @return List<[IdentifiedVo]> 선호도 레이블 목록
	 */
	@Deprecated("선호도 나중 구현")
	@Throws(Error::class)
	fun findAllAffinityLabel(): List<IdentifiedVo> // 선호도 레이블 리스트
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
	 * [ItVmService.findAllApplicationFromVm]
	 *
	 * @param vmId [String] 가상머신 id
	 */
	@Throws(Error::class)
	fun findAllApplicationFromVm(vmId: String): List<IdentifiedVo>
	/**
	 * [ItVmService.findAllGuestFromVm]
	 *
	 * @param vmId [String] 가상머신 id
	 */
	@Throws(Error::class)
	fun findAllGuestFromVm(vmId: String): GuestInfoVo?
	/**
	 * [ItVmService.findAllPermissionFromVm]
	 *
	 * @param vmId [String] 가상머신 id
	 */
	@Throws(Error::class)
	fun findAllPermissionFromVm(vmId: String): List<PermissionVo>
	/**
	 * [ItVmService.findAllEventFromVm]
	 * 이벤트
	 *
	 * @param vmId [String] 가상머신 id
	 */
	fun findAllEventFromVm(vmId: String): List<EventVo>
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

	@Deprecated("[ItClusterService.findAll] 내용")
	@Throws(Error::class)
	override fun findAllCluster(): List<ClusterVo> {
		log.info("findAllClusters ... ")
		val res: List<Cluster> =
			conn.findAllClusters()
				.getOrDefault(listOf())
				.filter { it.dataCenterPresent() && it.cpuPresent() }
		return res.toClusterVos(conn)
	}

	@Deprecated("ItTemplateService.findAll 내용 같음")
	@Throws(Error::class)
	override fun findAllTemplate(): List<TemplateVo> {
		log.info("findAllTemplate ... ")
		val res: List<Template> =
			conn.findAllTemplates()
				.getOrDefault(listOf())
		return res.toTemplateVos(conn)
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

	@Deprecated("[ItStorageService.findAllStorageDomainsFromDataCenter] 내용")
	override fun findAllStorageDomainFromDataCenter(dataCenterId: String): List<StorageDomainVo> {
		log.info("findAllStorageDomainFromDataCenter ... ")
		val res: List<StorageDomain> =
			conn.findAllAttachedStorageDomainsFromDataCenter(dataCenterId)
				.getOrDefault(listOf())
//		val storageDomains: List<StorageDomain> =
//			conn.findAllStorageDomains()
//				.getOrDefault(listOf())
//				.filter { !it.dataCentersPresent() }
		return res.toStorageDomainIdNames()
	}

	@Deprecated("[ItStorageService.findOneDiskProfile] 내용")
	@Throws(Error::class)
	override fun findAllDiskProfileFromStorageDomain(storageDomainId: String) : List<DiskProfileVo> {
		log.info("findAllDiskProfileFromStorageDomain ... domainId: $storageDomainId")
		val res: List<DiskProfile> =
			conn.findAllDiskProfilesFromStorageDomain(storageDomainId)
				.getOrDefault(listOf())
		return res.toDiskProfileVos()
	}


	override fun findAllVnicProfileFromCluster(clusterId: String): List<VnicProfileVo> {
		log.info("findAllVnicProfileFromCluster ... clusterId: {}", clusterId)
		val cluster: Cluster =
			conn.findCluster(clusterId)
				.getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toException()

		// 데이터 센터가 같아야함
		val res: List<VnicProfile> =
			conn.findAllVnicProfiles()
				.getOrDefault(listOf())
				.filter { vNic: VnicProfile ->
					val network: Network? = conn.findNetwork(vNic.network().id()).getOrNull()
					network?.dataCenter()?.id() == cluster.dataCenter().id()
				}
		return res.toVnicProfileVos(conn)
	}

	@Deprecated("[ItClusterService.findAllHostFromCluster] 내용과 비슷함")
	override fun findAllHostFromCluster(clusterId: String): List<HostVo> {
		log.info("findAllHostFromCluster ... clusterId: {}", clusterId)
		conn.findCluster(clusterId)
			.getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toException()
		val res: List<Host> =
			conn.findAllHosts()
				.getOrDefault(listOf())
				.filter { it.cluster().id() == clusterId }
		return res.toHostIdNames()
	}

	override fun findAllCpuProfileFromCluster(clusterId: String): List<CpuProfileVo> {
		log.info("findAllCpuProfileFromCluster ... clusterId: {}", clusterId)
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

	@Deprecated("선호도 나중 구현")
	override fun findAllAffinityGroupFromCluster(clusterId: String): List<IdentifiedVo> {
		log.info("findAllAffinityGroupFromCluster ... ")
		conn.findCluster(clusterId)
			.getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toException()
		val res: List<AffinityGroup> =
			conn.findAllAffinityGroupsFromCluster(clusterId)
				.getOrDefault(listOf())
		return res.fromAffinityGroupsToIdentifiedVos()
	}

	@Deprecated("선호도 나중 구현")
	override fun findAllAffinityLabel(): List<IdentifiedVo> {
		log.info("findAllAffinityLabel ... ")
		val res: List<AffinityLabel> =
			conn.findAllAffinityLabels()
				.getOrDefault(listOf())
		return res.fromAffinityLabelsToIdentifiedVos()
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


	override fun findAllApplicationFromVm(vmId: String): List<IdentifiedVo> {
		log.info("findAllApplicationFromVm ... vmId: {}", vmId)
		val res: List<Application> =
			conn.findAllApplicationsFromVm(vmId)
				.getOrDefault(listOf())
		return res.fromApplicationsToIdentifiedVos()
	}

	override fun findAllGuestFromVm(vmId: String): GuestInfoVo? {
		log.info("findAllGuestByVm ... vmId: {}", vmId)
		val res: Vm =
			conn.findVm(vmId)
				.getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toException()
		if (!res.guestOperatingSystemPresent()) {
			log.warn("게스트 운영 체제 정보가 없습니다.")
			return null
		}
		return res.toGuestInfoVo()
	}

	override fun findAllPermissionFromVm(vmId: String): List<PermissionVo> {
		log.info("findAllPermissionFromVm ... vmId: {}", vmId)
		val res: List<Permission> =
			conn.findAllAssignedPermissionsFromVm(vmId)
				.getOrDefault(listOf())
		return res.toPermissionVos(conn)
	}

	override fun findAllEventFromVm(vmId: String): List<EventVo> {
		log.info("findAllEventFromVm ... vmId: {}", vmId)
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