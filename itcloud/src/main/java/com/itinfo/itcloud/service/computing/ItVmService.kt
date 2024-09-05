package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.*
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.*
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.setting.toPermissionVos
import com.itinfo.itcloud.model.storage.*
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.error.toError
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
	 * [ItVmService.findAllClustersBy]
	 * 가상머신 생성 - 클러스터 목록
	 *
	 * @return List<[ClusterVo]> 클러스터 목록
	 */
	@Deprecated("ClusterService 에 있는 내용과 비슷함")
	fun findAllClustersBy(): List<ClusterVo>
	/**
	 * [ItVmService.findAllTemplatesBy]
	 *  가상머신 생성 - 템플릿 목록
	 *
	 * @return List<[TemplateVo]> 템플릿 목록
	 */
	@Deprecated("ItTemplateService.findAll 에 들어갈 내용과 비슷함")
	fun findAllTemplatesBy(): List<TemplateVo>
	/**
	 * [ItVmService.findAllDiskImagesBy]
	 * 가상머신 생성 - 인스턴스 이미지 - 연결 -> 디스크 목록
	 * 기준: 아무것도 연결되어 있지 않은 디스크
	 *
	 * @return List<[DiskImageVo]> 디스크  목록
	 */
	@Throws(Error::class)
	fun findAllDiskImagesBy(): List<DiskImageVo>
	/**
	 * [ItVmService.findAllStorageDomainsBy]
	 * 가상머신 생성 - 인스턴스 이미지 - 생성 -> 생성 시 필요한 스토리지 도메인
	 * 가상머신 생성 - 고가용성 - 임대 대상 스토리지 도메인 목록
	 * HELP 데이터센터 id가 있어야되는거 같긴함, 근데 애매한부분
	 *
	 * @param dataCenterId [String] 데이터센터 id (클러스터가 가지고 있는 데이터센터 id)
	 * @return List<[StorageDomainVo]> 스토리지 도메인 목록
	 */
	@Deprecated("ItStorageService.findAllStorageDomainsFromDataCenter 내용과 비슷함")
	@Throws(Error::class)
	fun findAllStorageDomainsBy(dataCenterId: String): List<StorageDomainVo>
	/**
	 * [ItVmService.findAllDiskProfilesBy]
	 * 가상머신 생성 - 인스턴스 이미지 - 생성 -> 스토리지 도메인과 연동되어 생기는 디스크 프로파일
	 *
	 * @param storageDomainId [String] 스토리지 도메인 id
	 * @return List<[DiskProfileVo]> 디스크 프로파일 목록
	 */
	@Deprecated("ItStorageService.findOneDiskProfile 내용과 비슷함")
	@Throws(Error::class)
	fun findAllDiskProfilesBy(storageDomainId: String) : List<DiskProfileVo>
	/**
	 * [ItVmService.findAllVnicProfilesBy]
	 * 가상머신 생성 -  vNic-vnicprofile 목록 출력 (가상머신 생성, 네트워크 인터페이스 생성)
	 *
	 * @param clusterId [String] 클러스터 id
	 * @return List<[VnicProfileVo]> VnicProfile 목록
	 */
	@Throws(Error::class)
	fun findAllVnicProfilesBy(clusterId: String): List<VnicProfileVo>
	/**
	 * [ItVmService.findAllHostsBy]
	 * 가상머신 생성 - 호스트 - 호스트 목록
	 *
	 * @param clusterId [String] 클러스터 id
	 * @return List<[HostVo]> 호스트 목록
	 */
	@Deprecated("ItClusterService.findAllHostsBy 내용과 비슷함")
	@Throws(Error::class)
	fun findAllHostsBy(clusterId: String): List<HostVo>
	/**
	 * [ItVmService.findAllCpuProfilesBy]
	 * 가상머신 생성 - 리소스할당 - cpuProfile 목록 출력
	 *
	 * @param clusterId [String] 클러스터 id
	 * @return List<[CpuProfileVo]> cpuProfile 목록
	 */
    @Throws(Error::class)
	fun findAllCpuProfilesBy(clusterId: String): List<CpuProfileVo>
	/**
	 * [ItVmService.findAllISOsBy]
	 * 가상머신 생성 - 부트 옵션 - 생성 시 필요한 CD/DVD 연결할 ISO 목록 (디스크이미지)
	 *
	 * @return List<[IdentifiedVo]> ISO 목록
	 */
    @Throws(Error::class)
	fun findAllISOsBy(): List<IdentifiedVo>

	// 선호도 그룹/레이블은 나중구현 가능
	/**
	 * [ItVmService.findAllAffinityGroupsBy]
	 * 가상머신 생성 - 선호도 - 선호도 그룹 목록
	 *
	 * @param clusterId [String] 클러스터 id
	 * @return List<[IdentifiedVo]> 선호도 그룹 목록
	 */
	@Throws(Error::class)
	fun findAllAffinityGroupsBy(clusterId: String): List<IdentifiedVo>
	/**
	 * [ItVmService.findAllAffinityLabelsBy]
	 * 가상머신 생성 - 선호도 - 선호도 레이블 목록
	 * 기준을 모르겠음
	 *
	 * @return List<[IdentifiedVo]> 선호도 레이블 목록
	 */
	@Throws(Error::class)
	fun findAllAffinityLabelsBy(): List<IdentifiedVo> // 선호도 레이블 리스트
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
	 * [ItVmService.findAllApplicationsByVm]
	 *
	 * @param vmId [String] 가상머신 id
	 */
	@Throws(Error::class)
	fun findAllApplicationsByVm(vmId: String): List<IdentifiedVo>
	/**
	 * [ItVmService.findAllGuestFromVm]
	 *
	 * @param vmId [String] 가상머신 id
	 */
	@Throws(Error::class)
	fun findAllGuestFromVm(vmId: String): GuestInfoVo?
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
		log.info("getVmDetail ... ")
		val res: Vm =
			conn.findVm(vmId).getOrNull()
				?: throw ErrorPattern.VM_NOT_FOUND.toError()
		return res.toVmVo(conn)
	}

	@Deprecated("ClusterService에 있는 내용과 비슷함")
	@Throws(Error::class)
	override fun findAllClustersBy(): List<ClusterVo> {
		log.info("findAllClusters ... ")
		val res: List<Cluster> =
			conn.findAllClusters()
				.getOrDefault(listOf())
				.filter { it.dataCenterPresent() && it.cpuPresent() }
		return res.toClusterVos(conn)
	}

	@Deprecated("ItTemplateService.findAll 내용 같음")
	@Throws(Error::class)
	override fun findAllTemplatesBy(): List<TemplateVo> {
		log.info("getTemplates ... ")
		val res: List<Template> =
			conn.findAllTemplates()
				.getOrDefault(listOf())
		return res.toTemplateVos(conn)
	}

	override fun findAllDiskImagesBy(): List<DiskImageVo> {
		log.info("findAllDiskImages ... ")
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

	@Deprecated("ItStorageService.findAllStorageDomainsFromDataCenter 내용과 비슷함")
	override fun findAllStorageDomainsBy(dataCenterId: String): List<StorageDomainVo> {
		log.info("findAllStorageDomainsBy ... ")
		val res: List<StorageDomain> =
			conn.findAllAttachedStorageDomainsFromDataCenter(dataCenterId)
				.getOrDefault(listOf())

//		val storageDomains: List<StorageDomain> =
//			conn.findAllStorageDomains()
//				.getOrDefault(listOf())
//				.filter { !it.dataCentersPresent() }
		return res.toStorageDomainIdNames()
	}

	@Deprecated("ItStorageService.findOneDiskProfile 내용과 비슷함")
	@Throws(Error::class)
	override fun findAllDiskProfilesBy(storageDomainId: String) : List<DiskProfileVo> {
		log.info("findAllDiskProfilesBy ... domainId: $storageDomainId")
		val res: List<DiskProfile> =
			conn.findAllDiskProfilesFromStorageDomain(storageDomainId)
				.getOrDefault(listOf())
		return res.toDiskProfileVos()
	}


	override fun findAllVnicProfilesBy(clusterId: String): List<VnicProfileVo> {
		log.info("findAllVnicProfiles ... clusterId: {}", clusterId)
		val cluster: Cluster =
			conn.findCluster(clusterId)
				.getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toError()

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

	@Deprecated("사용안할듯")
	override fun findAllHostsBy(clusterId: String): List<HostVo> {
		log.info("findAllHostsBy ... clusterId: {}", clusterId)
		conn.findCluster(clusterId).getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toError()
		val res: List<Host> =
			conn.findAllHosts()
				.getOrDefault(listOf())
				.filter { it.cluster().id() == clusterId }
		return res.toHostIdNames()
	}

	override fun findAllCpuProfilesBy(clusterId: String): List<CpuProfileVo> {
		log.info("findAllCpuProfilesBy ... clusterId: {}", clusterId)
		conn.findCluster(clusterId).getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toError()
		val res: List<CpuProfile> =
			conn.findAllCpuProfiles()
				.getOrDefault(listOf())
				.filter { it.cluster().id() == clusterId }
		return res.toCpuProfileVos()
	}

	override fun findAllISOsBy(): List<IdentifiedVo> {
		log.info("findAllISOsBy ... ")
		val res: List<Disk> =
			conn.findAllDisks().getOrDefault(listOf())
				.filter { it.contentType() == DiskContentType.ISO }
		return res.fromDisksToIdentifiedVos()
	}

	override fun findAllAffinityGroupsBy(clusterId: String): List<IdentifiedVo> {
		log.info("findAllAffinityGroupsBy ... ")
		conn.findCluster(clusterId).getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toError()
		val res: List<AffinityGroup> =
			conn.findAllAffinityGroupsFromCluster(clusterId)
				.getOrDefault(listOf())
		return res.fromAffinityGroupsToIdentifiedVos()
	}

	override fun findAllAffinityLabelsBy(): List<IdentifiedVo> {
		log.info("findAllAffinityLabelsBy ... ")
		val res: List<AffinityLabel> =
			conn.findAllAffinityLabels()
				.getOrDefault(listOf())
		return res.fromAffinityLabelsToIdentifiedVos()
	}

	override fun add(vmVo: VmVo): VmVo? {
		log.info("addVm ... ")

		// TODO: 파라미터에 대한 처리 내용 검토 필요
		val res: Vm =
			conn.addVm(
				vmVo.toAddVmBuilder(conn),
				vmVo.vnicProfileVos.map { vnicProfileVo ->  vnicProfileVo.id },
				vmVo.diskAttachmentVos.map { diskAttachmentVo ->  diskAttachmentVo.id },
			)
			.getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()
		return res.toVmVo(conn)

		// 가상머신 만들고 nic 붙이고 disk 붙이는 식
/*
		try {
			if (vmCreateVo.vnics.isNotEmpty()) {

				val result: Result<Boolean> = conn.addVnicsFromVm(vm, vmCreateVo.vnics)
				if (result.head.code == 404)
					return CommonVo.failResponse("vnic 연결 실패")
			}

			// disk가 있다면
			if (vmCreateVo.vDisks.isNotEmpty()) {
				val result: CommonVo<Boolean> = addVmDisk(vm, vmCreateVo.vDisks)
				if (result.head.code == 404)
					return CommonVo.failResponse("disk 연결 실패")
			}

			// 이것도 vm id가 있어야 생성가능
			if (vmCreateVo.vmBootVo.connId.isNotEmpty()) {
				vmService.cdromsService().add()
					.cdrom(CdromBuilder().file(FileBuilder().id(vmCreateVo.vmBootVo.connId)).build())
					.send()
			}

			if (conn.expectVmStatus(vm.id(), VmStatus.DOWN, 3000, 900000)) {
				log.info("가상머신 생성 완료: {}", vm.name())
				return CommonVo.createResponse()
			} else {
				log.error("가상머신 생성 시간 초과: {}", vm.name())
				return CommonVo.failResponse("가상머신 생성 시간 초과")
			}
		} catch (e: Exception) {
			e.printStackTrace()
			log.error("가상머신 생성 실패")
			return CommonVo.failResponse("가상머신 생성 실패")
		}
*/
	}

	override fun update(vmVo: VmVo): VmVo? {
		log.info("update ... ")
		val res: Vm =
			conn.updateVm(vmVo.toEditVmBuilder(conn))
				.getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()
		return res.toVmVo(conn)
		// 가상머신 만들고 nic 붙이고 disk 붙이는 식
		/*
		//		try{
		//			if (vmVo.getVnicList() != null) {
		//				addVmNic(system, vm, vmVo);
		//			}
		//
		//			// disk가 있다면
		//			if (vmVo.getVDiskList() != null) {
		//				addVmDisk(system, vm, vmVo.getVDiskList());
		//			}
		//
		//			// 이것도 vm id가 있어야 생성가능
		//			if (vmVo.getVmBootVo().getConnId() != null) {
		//				vmService.cdromsService().add().cdrom(new CdromBuilder().file(new FileBuilder().id(vmVo.getVmBootVo().getConnId())).build()).send();
		//			}
		//
		//			if(expectStatus(vmService, VmStatus.DOWN, 3000, 900000)){
		//				log.info("가상머신 수정 완료: " + vm.name());
		//				return CommonVo.createResponse();
		//			} else {
		//				log.error("가상머신 수정 시간 초과: {}", vm.name());
		//				return CommonVo.failResponse("가상머신 수정 시간 초과");
		//			}
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//			log.error("가상머신 수정 실패");
		//			return CommonVo.failResponse("가상머신 수정 실패");
		//		}
		*/

	}

	override fun remove(vmId: String, disk: Boolean): Boolean {
		log.info("deleteVm ...  vmName: {}", conn.findVmName(vmId))
		conn.findVm(vmId).getOrNull()?: throw ErrorPattern.VM_NOT_FOUND.toError()
		val res: Result<Boolean> =
			conn.removeVm(vmId, disk)
		return res.isSuccess
	}


	override fun findAllApplicationsByVm(vmId: String): List<IdentifiedVo> {
		log.info("getApplicationsByVm ... vmId: {}", vmId)
		conn.findVm(vmId).getOrNull()?: throw ErrorPattern.VM_NOT_FOUND.toError()
		val res: List<Application> =
			conn.findAllApplicationsFromVm(vmId)
				.getOrDefault(listOf())
		return res.fromApplicationsToIdentifiedVos()
	}

	override fun findAllGuestFromVm(vmId: String): GuestInfoVo? {
		log.info("findAllGuestByVm ... vmId: {}", vmId)
		val res: Vm =
			conn.findVm(vmId).getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()
		if (!res.guestOperatingSystemPresent()) {
			log.warn("게스트 운영 체제 정보가 없습니다.")
			return null
		}
		return res.toGuestInfoVo()
	}

	override fun findAllPermissionsFromVm(vmId: String): List<PermissionVo> {
		log.info("getPermissionsByVm ... vmId: {}", vmId)
		conn.findVm(vmId).getOrNull()?: throw ErrorPattern.VM_NOT_FOUND.toError()
		val res: List<Permission> =
			conn.findAllAssignedPermissionsFromVm(vmId)
				.getOrDefault(listOf())
		return res.toPermissionVos(conn)
	}

	override fun findAllEventsFromVm(vmId: String): List<EventVo> {
		log.info("getEventsByVm ... vmId: {}", vmId)
		val vm: Vm =
			conn.findVm(vmId).getOrNull()?:throw ErrorPattern.VM_NOT_FOUND.toError()
		val res: List<Event> =
			conn.findAllEvents()
				.getOrDefault(listOf())
				.filter { it.vmPresent() && it.vm().name() == vm.name() }
		return res.toEventVos()
	}

	override fun findConsole(vmId: String): ConsoleVo? {
		log.info("getConsole ... vmId: {}", vmId)
		val res: Vm =
			conn.findVm(vmId).getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()
		return res.toConsoleVo(conn, systemPropertiesVo)
	}

	// region: 생성
	/**
	 * 가상머신 생성 - 인스턴스 이미지 [ 연결 / 생성 ]
	 * 디스크 여러개 가능 (연결+생성, 연결+연결, 생성+생성)
	 * @param vm
	 * @param vDiskVoList
	 * @return
	 */
/*	@Throws(Exception::class)
	private fun addVmDisk(vm: Vm, vDiskVoList: List<VDiskVo>): CommonVo<Boolean> {
		log.debug("addVmDisk ... ")
		try {
			val dasService = system.vmsService().vmService(vm.id()).diskAttachmentsService()
			var daBuilder: DiskAttachmentBuilder
			var bootableDiskExists =
				dasService.list().send().attachments().stream().anyMatch { obj: DiskAttachment -> obj.bootable() }

			for (vDiskVo in vDiskVoList) {
				log.debug("null: {}", vDiskVo.vDiskImageVo.diskId.isNotEmpty())

				if (vDiskVo.vDiskImageVo.diskId.isNotEmpty()) {
					// 디스크 이미지 연결
					val disk = system.disksService().diskService(vDiskVo.vDiskImageVo.diskId).get().send().disk()
					daBuilder = attachDisk(disk, vDiskVo, vDiskVo.vDiskImageVo.bootable, true)
					log.info("디스크 연결")
				} else {
					// 디스크 이미지 생성
					val diskBuilder = createDisk(vDiskVo)
					val disk = system.disksService().add().disk(diskBuilder).send().disk()
					val diskService = system.disksService().diskService(disk.id())

					// 디스크 상태 확인 (LOCK -> OK)
					if (conn.expectDiskStatus(disk.id())) {
						log.info("디스크 생성 완료: {}", disk.name())
					} else {
						log.error("디스크 생성 시간 초과: {}", disk.name())
						return CommonVo.failResponse("생성 시간 초과")
					}

					val isBootable = bootableFlag(bootableDiskExists, vDiskVo)
					bootableDiskExists = bootableDiskExists or isBootable

					daBuilder = attachDisk(disk, vDiskVo, isBootable, false)
				}
				// 추가된 디스크를 VM에 붙임
				dasService.add().attachment(daBuilder).send().attachment()
			}
			log.info("디스크 붙이기 완료")
			return CommonVo.createResponse()
		} catch (e: Exception) {
			e.printStackTrace()
			return CommonVo.failResponse("${vm.name()} 디스크 생성 실패")
		}
	}*/


	/**
	 * [VmServiceImpl.createDisk]
	 * Disk 생성 정보
	 * @param vDiskVo
	 * @return
	 * [VDiskVo] 구현
	 */
	/*private fun createDisk(vDiskVo: VDiskVo): DiskBuilder {
		return DiskBuilder()
			.provisionedSize(BigInteger.valueOf(vDiskVo.vDiskImageVo.size).multiply(BigInteger.valueOf(1024).pow(3)) ) // 값 받은 것을 byte로 변환하여 준다
			.alias(vDiskVo.vDiskImageVo.alias)
			.description(vDiskVo.vDiskImageVo.description)
			.storageDomains(*arrayOf(StorageDomainBuilder().id(vDiskVo.vDiskImageVo.storageDomainId).build()))
			.sparse(vDiskVo.vDiskImageVo.allocationPolicy) // 할당정책: 씬 true
			.diskProfile(DiskProfileBuilder().id(vDiskVo.vDiskImageVo.diskProfile).build()) // 없어도 상관없음
			.wipeAfterDelete(vDiskVo.vDiskImageVo.wipeAfterDelete) // 삭제후 초기화
			.shareable(vDiskVo.vDiskImageVo.shareable) // 공유 가능 (공유가능 o 이라면 증분백업 안됨 FRONT에서 막기?)
			.backup(if (vDiskVo.vDiskImageVo.backup) DiskBackup.INCREMENTAL else DiskBackup.NONE) // 증분 백업 사용(기본이 true)
			.format(if (vDiskVo.vDiskImageVo.backup) DiskFormat.COW else DiskFormat.RAW) // 백업 안하면 RAW
	}

	private fun attachDisk(/*disk: Disk,*/ diskAttachmentVo: DiskAttachmentVo, isBootable: Boolean, conn: Boolean): DiskAttachmentBuilder {
		return DiskAttachmentBuilder()
			.disk(if (conn) DiskBuilder().id(diskAttachmentVo.diskImageVo.id).build() else null)
			.active(true)
			.interface_(diskAttachmentVo.interface_)
			.bootable(if (conn) diskAttachmentVo.bootable else isBootable)
			.readOnly(diskAttachmentVo.readOnly)
	}

	private fun addVmNic(vm: Vm, vmVo: VmCreateVo): CommonVo<Boolean> {
		// 연결할때 문제생길거같음
		try {
			val vmNicsService = system.vmsService().vmService(vm.id()).nicsService()
			val nicBuilders =
				vmVo.vnics.map { identifiedVo: VnicProfileVo ->
					NicBuilder()
						.name("nic" + (vmVo.vnics.indexOf(identifiedVo) + 1))
						.vnicProfile(VnicProfileBuilder().id(identifiedVo.id).build())
				}

			for (nicBuilder in nicBuilders) {
				vmNicsService.add().nic(nicBuilder).send()
			}

			log.info("{} vnic 생성 성공", vm.name())
			return CommonVo.createResponse()
		} catch (e: Exception) {
			log.error("vnic 생성 실패 ... 이유: {}", e.localizedMessage)
			return CommonVo.failResponse("${vm.name()} vnic 생성 실패")
		}
	}

	private fun getVmSystemBuilder(system: SystemService, vmBuilder: VmBuilder, vmVo: VmCreateVo): VmBuilder {
		val convertMb = BigInteger.valueOf(1024).pow(2)

		// 인스턴스 타입이 지정되어 있다면
		if (vmVo.vmSystemVo.instanceType.isNotEmpty()) {
			val it = system.instanceTypesService().list().search("name=" + vmVo.vmSystemVo.instanceType).send()
				.instanceType()[0]
			vmBuilder.instanceType(it)
		} else {	// 사용자 정의 값
			vmBuilder
				.memory(BigInteger.valueOf(vmVo.vmSystemVo.memorySize).multiply(convertMb))
				.memoryPolicy(
					MemoryPolicyBuilder()
						.max(BigInteger.valueOf(vmVo.vmSystemVo.memoryMax).multiply(convertMb))
						.ballooning(vmVo.vmResourceVo.isMemoryBalloon) // 리소스할당- 메모리 balloon 활성화
						.guaranteed(BigInteger.valueOf(vmVo.vmSystemVo.memoryActual).multiply(convertMb))
				)
				.cpu(
					CpuBuilder().topology(
						CpuTopologyBuilder()
							.cores(vmVo.vmSystemVo.vCpuSocketCore)
							.sockets(vmVo.vmSystemVo.vCpuSocket)
							.threads(vmVo.vmSystemVo.vCpuCoreThread)
					)
				)
		}
		return vmBuilder.timeZone(TimeZoneBuilder().name(vmVo.vmSystemVo.timeOffset)) // 시스템-일반 하드웨어 클럭의 시간 오프셋
	}
*/
	/**
	 * [VmServiceImpl.getVmInitBuilder]
	 * 가상머신 - 초기 실행
	 *
	 * @param vmBuilder 가상머신 빌더
	 * @param vmVo 가상머신 객체
	 */
   /* private fun getVmInitBuilder(vmBuilder: VmBuilder, vmVo: VmCreateVo) {
		// 초기실행이 true면 뜨는경우
		if (vmVo.vmInitVo.isCloudInit) {
			vmBuilder.initialization(
				InitializationBuilder()
					.hostName(vmVo.vmInitVo.hostName)
					.timezone(vmVo.vmInitVo.timeStandard) // Asia/Seoul
					.customScript(vmVo.vmInitVo.script)
			)
		}
	}*/

	/**
	 * [VmServiceImpl.getVmHostBuilder]
	 * 가상머신 - 호스트
	 *
	 * @param vmBuilder 가상머신 빌더
	 * @param vmVo 가상머신 객체
	 */
  /*  private fun getVmHostBuilder(vmBuilder: VmBuilder, vmVo: VmCreateVo): VmBuilder {
		val placementBuilder = VmPlacementPolicyBuilder()

		// 실행 호스트 - 특정 호스트(무조건 한개는 존재)
		// 기본이 클러스터 내 호스트라 지정 필요없음
		if (!vmVo.vmHostVo.isHostInCluster) {
			// 선택된 호스트 전부 넣기
			placementBuilder.hosts(
				vmVo.vmHostVo.hostIds.map { identifiedVo: IdentifiedVo ->
					HostBuilder().id(
						identifiedVo.id
					).build()
				}
			)
		}

		// 마이그레이션: 사용자 정의 일때만 마이그레이션 모드 설정가능
		return vmBuilder
			.placementPolicy(
				placementBuilder.affinity(
					if ("none" == vmVo.vmSystemVo.instanceType) VmAffinity.valueOf(vmVo.vmHostVo.migrationMode) else VmAffinity.MIGRATABLE
				)
			)
			.migration( // 정책은 찾을 수가 없음, parallel Migrations 안보임, 암호화
				MigrationOptionsBuilder().encrypted(vmVo.vmHostVo.migrationEncrypt).build()
			)
	}
*/
	/**
	 * [VmServiceImpl.getVmHaBuilder]
	 * 가상머신 - 고가용성
	 * @param vmBuilder 가상머신 빌더
	 * @param vmVo 가상머신 객체
	 */
	/*private fun getVmHaBuilder(vmBuilder: VmBuilder, vmVo: VmCreateVo) {
		// 고가용성 설정되면 스토리지 도메인 지정
		if (vmVo.vmHaVo.isHa)
			vmBuilder.lease(StorageDomainLeaseBuilder().storageDomain(StorageDomainBuilder().id(vmVo.vmHaVo.vmStorageDomainId)))

		vmBuilder.highAvailability(HighAvailabilityBuilder().enabled(vmVo.vmHaVo.isHa).priority(vmVo.vmHaVo.priority))
	}*/


	/**
	 * [VmServiceImpl.getVmResourceBuilder]
	 * 가상머신 - 리소스 할당
	 *
	 * @param vmBuilder 가상머신 빌더
	 * @param vmVo 가상머신 객체
	 */
	/*private fun getVmResourceBuilder(vmBuilder: VmBuilder, vmVo: VmCreateVo): VmBuilder {
		// CPU 할당
		return vmBuilder
			.cpuProfile(CpuProfileBuilder().id(vmVo.vmResourceVo.cpuProfileId))
			.cpuShares(vmVo.vmResourceVo.cpuShare)
			.autoPinningPolicy(if ("RESIZE_AND_PIN_NUMA" == vmVo.vmResourceVo.cpuPinningPolicy) AutoPinningPolicy.ADJUST else AutoPinningPolicy.DISABLED)
			.cpuPinningPolicy(CpuPinningPolicy.valueOf(vmVo.vmResourceVo.cpuPinningPolicy))
			.virtioScsiMultiQueuesEnabled(vmVo.vmResourceVo.multiQue) // VirtIO-SCSI 활성화
	}*/


	/**
	 * [VmServiceImpl.getVmBootBuilder]
	 * 가상머신 - 부트 옵션
	 *
	 * @param vmBuilder 가상머신 빌더
	 * @param vmVo 가상머신 객체
	 */
	/*private fun getVmBootBuilder(vmBuilder: VmBuilder, vmVo: VmCreateVo): VmBuilder {
		val bootDeviceList: MutableList<BootDevice> = mutableListOf(
			BootDevice.valueOf(vmVo.vmBootVo.firstDevice), // 첫번째 장치
		)

		if (vmVo.vmBootVo.secDevice.isNotEmpty())
			bootDeviceList.add(BootDevice.valueOf(vmVo.vmBootVo.secDevice)) // 두번째 장치

		return vmBuilder.os(
			OperatingSystemBuilder()
				.type(vmVo.os) // 일반 - 운영시스템
				.boot(BootBuilder().devices(bootDeviceList))
		)
//		.bios(new BiosBuilder ().type(BiosType.valueOf(vmVo.getChipsetType())))  // 칩셋/펌웨어
//		.bootMenu(new BootMenuBuilder ().enabled(vmVo.getVmBootVo().isBootingMenu()))
	}
*/

	companion object {
		private val log by LoggerDelegate()
	}
}