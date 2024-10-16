package com.itinfo.itcloud.service.storage

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.*
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.setting.toPermissionVos
import com.itinfo.itcloud.model.storage.*
import com.itinfo.itcloud.repository.engine.DiskVmElementRepository
import com.itinfo.itcloud.repository.engine.entity.DiskVmElementEntity
import com.itinfo.itcloud.repository.engine.entity.toVmId
import com.itinfo.itcloud.service.BaseService
import com.itinfo.itcloud.service.storage.DiskServiceImpl.Companion
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import org.ovirt.engine.sdk4.builders.*
import org.ovirt.engine.sdk4.services.*
import org.ovirt.engine.sdk4.types.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

import javax.net.ssl.*
import kotlin.Error

interface ItStorageService {
	/**
	 * [ItStorageService.findAll]
	 * 전체 스토리지 도메인 목록
	 *
	 * @return List<[StorageDomainVo]> 스토리지 도메인 목록
	 */
	@Throws(Error::class)
	fun findAll(): List<StorageDomainVo>
	/**
	 * [ItStorageService.findAllFromDataCenter]
	 * 데이터센터가 가지고 있는 스토리지 도메인 목록
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return List<[StorageDomainVo]> 스토리지 도메인 목록
	 */
	@Throws(Error::class)
	fun findAllFromDataCenter(dataCenterId: String): List<StorageDomainVo>
	/**
	 * [ItStorageService.findOne]
	 * 데이터센터 - 스토리지 도메인 정보
	 *
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @return [StorageDomainVo]?
	 */
	@Throws(Error::class)
	fun findOne(storageDomainId: String): StorageDomainVo?

	// 도메인 생성 - 호스트 목록 [ItDataCenterService.findAllHostsFromDataCenter]
	/**
	 * [ItStorageService.findAllIscsiFromHost]
	 * 도메인 생성(가져오기?) - iSCSI 유형 대상 LUN 목록
	 *
	 * @param hostId [String] 호스트 Id
	 * @return List<[HostStorageVo]>
	 */
	@Throws(Error::class)
	fun findAllIscsiFromHost(hostId: String): List<HostStorageVo>
	/**
	 * [ItStorageService.findAllFibreFromHost]
	 * 도메인 생성(가져오기?) - Fibre Channel 유형 대상 LUN 목록
	 * 타입이 tcp로 뜸
	 *
	 * @param hostId [String] 호스트 Id
	 * @return List<[HostStorageVo]>
	 */
	@Throws(Error::class)
	fun findAllFibreFromHost(hostId: String): List<HostStorageVo>

	/**
	 * [ItStorageService.add]
	 * 도메인 생성
	 *
	 * @param storageDomainVo [StorageDomainVo]
	 * @return [StorageDomainVo]?
	 */
	@Throws(Error::class)
	fun add(storageDomainVo: StorageDomainVo): StorageDomainVo?
	/**
	 * [ItStorageService.import]
	 * 도메인 가져오기
	 *
	 * @param storageDomainVo [StorageDomainVo]
	 * @return [StorageDomainVo]?
	 */
	@Throws(Error::class)
	fun import(storageDomainVo: StorageDomainVo): StorageDomainVo?
	/**
	 * [ItStorageService.update]
	 * 도메인 편집(관리)
	 *
	 * @param storageDomainVo [StorageDomainVo]
	 * @return [StorageDomainVo]?
	 */
	@Throws(Error::class)
	fun update(storageDomainVo: StorageDomainVo): StorageDomainVo?
	/**
	 * [ItStorageService.remove]
	 * 도메인 삭제
	 *
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun remove(storageDomainId: String): Boolean
	/**
	 * [ItStorageService.destroy]
	 * 도메인 파괴
	 *
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun destroy(storageDomainId: String): Boolean

	/**
	 * [ItStorageService.findAllDataCentersFromStorageDomain]
	 * 스토리지도메인 - 데이터센터 목록
	 *
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @return List<[DataCenterVo]> 데이터센터 목록
	 */
	@Throws(Error::class)
	fun findAllDataCentersFromStorageDomain(storageDomainId: String): StorageDomainVo

	// 데이터센터 연결할 목록 - [ItDataCenterService.findAll] 사용하면 될듯
	/**
	 * [ItStorageService.attachFromDataCenter]
	 * 스토리지 도메인 - 데이터센터 연결 attach
	 *
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun attachFromDataCenter(dataCenterId: String, storageDomainId: String): Boolean
	/**
	 * [ItStorageService.detachFromDataCenter]
	 * 스토리지 도메인 - 데이터센터 분리 detach
	 *
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun detachFromDataCenter(dataCenterId: String, storageDomainId: String): Boolean
	/**
	 * [ItStorageService.activateFromDataCenter]
	 * 스토리지 도메인 - 데이터센터 활성 activate
	 *
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun activateFromDataCenter(dataCenterId: String, storageDomainId: String): Boolean
	/**
	 * [ItStorageService.maintenanceFromDataCenter]
	 * 스토리지 도메인 - 데이터센터 유지보수 maintenance
	 *
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun maintenanceFromDataCenter(dataCenterId: String, storageDomainId: String): Boolean

	/**
	 * [ItStorageService.findAllVmsFromStorageDomain]
	 * 스토리지도메인 - 가상머신 목록
	 *
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @return List<[VmVo]> 가상머신 목록
	 */
	@Throws(Error::class)
	fun findAllVmsFromStorageDomain(storageDomainId: String): List<VmVo>
	/**
	 * [ItStorageService.findAllDisksFromStorageDomain]
	 * 스토리지 도메인 - 디스크 목록
	 *
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @return List<[DiskImageVo]> 디스크 목록
	 */
	@Throws(Error::class)
	fun findAllDisksFromStorageDomain(storageDomainId: String): List<DiskImageVo>
	/**
	 * [ItStorageService.findAllDiskSnapshotsFromStorageDomain]
	 * 스토리지 도메인 - 디스크 스냅샷 목록
	 *
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @return List<[SnapshotDiskVo]> 디스크 스냅샷 목록
	 */
	@Throws(Error::class)
	fun findAllDiskSnapshotsFromStorageDomain(storageDomainId: String): List<SnapshotDiskVo>
	/**
	 * [ItStorageService.findAllTemplatesFromStorageDomain]
	 * 스토리지도메인 - 템플릿 목록
	 *
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @return List<[TemplateVo]> 템플릿 목록
	 */
	@Throws(Error::class)
	fun findAllTemplatesFromStorageDomain(storageDomainId: String): List<TemplateVo>

	// 스토리지 도메인 - 디스크 목록

	/**
	 * [ItStorageService.findAllDiskProfilesFromStorageDomain]
	 * 스토리지 도메인 - 디스크 프로파일 목록
	 *
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @return List<[DiskProfileVo]> 디스크 프로파일 목록
	 */
	@Throws(Error::class)
	fun findAllDiskProfilesFromStorageDomain(storageDomainId: String): List<DiskProfileVo>
//	/**
//	 * [ItStorageService.addDiskProfileFromStorageDomain]
//	 * 스토리지 도메인 - 디스크 프로파일 생성
//	 *
//	 * @param diskProfileVo [DiskProfileVo]
//	 * @return [DiskProfileVo]
//	 */
//	@Throws(Error::class)
//	fun addDiskProfileFromStorageDomain(diskProfileVo: DiskProfileVo): DiskProfileVo?
//	/**
//	 * [ItStorageService.updateDiskProfileFromStorageDomain]
//	 * 스토리지 도메인 - 디스크 프로파일 편집
//	 *
//	 * @param diskProfileVo [DiskProfileVo]
//	 * @return [DiskProfileVo]
//	 */
//	@Throws(Error::class)
//	fun updateDiskProfileFromStorageDomain(diskProfileVo: DiskProfileVo): DiskProfileVo?
//	/**
//	 * [ItStorageService.removeDiskProfileFromStorageDomain]
//	 * 스토리지 도메인 - 디스크 프로파일 삭제
//	 *
//	 * @param diskProfileId [String]
//	 * @return [Boolean]
//	 */
//	@Throws(Error::class)
//	fun removeDiskProfileFromStorageDomain(diskProfileId: String): Boolean
	/**
	 * [ItStorageService.findAllEventsFromStorageDomain]
	 * 스토리지도메인 - 이벤트
	 *
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @return List<[EventVo]>
	 */
	@Throws(Error::class)
	fun findAllEventsFromStorageDomain(storageDomainId: String): List<EventVo>

	/**
	 * [ItStorageService.findAllPermissionsFromStorageDomain]
	 * 스토리지도메인 - 권한
	 *
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @return List<[PermissionVo]> 권한 목록
	 */
	@Throws(Error::class)
	@Deprecated("나중구현")
	fun findAllPermissionsFromStorageDomain(storageDomainId: String): List<PermissionVo>

}

@Service
class StorageServiceImpl(

): BaseService(), ItStorageService {

	@Autowired private lateinit var diskVmElementRepository: DiskVmElementRepository

	@Throws(Error::class)
	override fun findAll(): List<StorageDomainVo> {
		log.info("findAll ...")
		val res: List<StorageDomain> =
			conn.findAllStorageDomains().getOrDefault(listOf())
				.filter { it.storage().type() != StorageType.GLANCE }
		return res.toStorageDomainsMenu(conn)
	}

	@Throws(Error::class)
	override fun findAllFromDataCenter(dataCenterId: String): List<StorageDomainVo> {
		log.info("findAllFromDataCenter ... dcId: $dataCenterId")
		val res: List<StorageDomain> =
			conn.findAllAttachedStorageDomainsFromDataCenter(dataCenterId).getOrDefault(listOf())
		return res.toStorageDomainsMenu(conn)
	}

	@Throws(Error::class)
	override fun findOne(storageDomainId: String): StorageDomainVo? {
		log.info("findOne... ")
		val res: StorageDomain? =
			conn.findStorageDomain(storageDomainId).getOrNull()
		return res?.toStorageDomainVo(conn)
	}

	@Throws(Error::class)
	override fun findAllIscsiFromHost(hostId: String): List<HostStorageVo> {
		log.info("findAllIscsiFromHost... hostId: {}", hostId)
		val res: List<HostStorage> =
			conn.findAllStoragesFromHost(hostId).getOrDefault(listOf())
				.filter { it.type() == StorageType.ISCSI }
		return res.toIscsiHostStorageVos()
	}

	@Throws(Error::class)
	override fun findAllFibreFromHost(hostId: String): List<HostStorageVo> {
		log.info("findAllFibreFromHost... hostId: {}", hostId)
		val res: List<HostStorage> =
			conn.findAllStoragesFromHost(hostId).getOrDefault(listOf())
				.filter { it.type() == StorageType.FCP }
		return res.toFibreHostStorageVos()
	}


	@Throws(Error::class)
	override fun add(storageDomainVo: StorageDomainVo): StorageDomainVo? {
		log.info("add ... storageDomain name: {}", storageDomainVo.name)
		val res: StorageDomain? =
			conn.addStorageDomain(
				storageDomainVo.toAddStorageDomainBuilder(),
				storageDomainVo.dataCenterVo.id
			).getOrNull()
		return res?.toStorageDomainVo(conn)
	}

	@Throws(Error::class)
	override fun import(storageDomainVo: StorageDomainVo): StorageDomainVo? {
		// TODO add와 다른점을 모르겟음(api측면에서)
		log.info("import ... storageDomain name: {}", storageDomainVo.name)
		val res: StorageDomain? =
			conn.addStorageDomain(
				storageDomainVo.toAddStorageDomainBuilder(),
				storageDomainVo.dataCenterVo.id
			).getOrNull()
		return res?.toStorageDomainVo(conn)
	}

	@Throws(Error::class)
	override fun update(storageDomainVo: StorageDomainVo): StorageDomainVo? {
		log.info("update ... storageDomain name: {}", storageDomainVo.name)
		val res: StorageDomain? =
			conn.updateStorageDomain(
				storageDomainVo.id,
				storageDomainVo.toEditStorageDomainBuilder(),
			).getOrNull()
		return res?.toStorageDomainVo(conn)
	}

	@Throws(Error::class)
	override fun remove(storageDomainId: String): Boolean {
		log.info("remove ... storageDomainId: {}", storageDomainId)
		val res: Result<Boolean> =
			conn.removeStorageDomain(storageDomainId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun destroy(storageDomainId: String): Boolean {
		// TODO: 여쭤보고 바꾸기(만약 삭제창에서 같이 보여주는 방식이라면 함수 필요없음(format으로 처리))
		log.info("destroy ... storageDomainId: {}", storageDomainId)
		val res: Result<Boolean> =
			conn.removeStorageDomain(storageDomainId, true)
		return res.isSuccess
	}


	@Throws(Error::class)
	override fun findAllDataCentersFromStorageDomain(storageDomainId: String): StorageDomainVo {
		log.info("findAllDataCentersFromStorageDomain ... storageDomainId: {}", storageDomainId)
		val storageDomain: StorageDomain =
			conn.findStorageDomain(storageDomainId).getOrNull()
				?: throw ErrorPattern.STORAGE_DOMAIN_ID_NOT_FOUND.toException()

//		val res: List<DataCenter> =
//			storageDomain.dataCenters()
//				.map { dataCenter ->
//					val dc: DataCenter =
//						conn.findDataCenter(dataCenter.id())
//							.getOrNull() ?: throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()
//					dc
//				}
//		return res.toStorageDomainDataCenterVos(conn)
		return storageDomain.toStorageDomainDataCenter(conn)
	}

	@Throws(Error::class)
	override fun attachFromDataCenter(dataCenterId: String, storageDomainId: String): Boolean {
		log.info("attachFromDataCenter ... storageDomainId: {}, dataCenterId: {}", storageDomainId, dataCenterId)
		val res: Result<Boolean> =
			conn.attachStorageDomainsToDataCenter(storageDomainId, dataCenterId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun detachFromDataCenter(dataCenterId: String, storageDomainId: String): Boolean {
		log.info("detachFromDataCenter ... storageDomainId: {}, dataCenterId: {}", storageDomainId, dataCenterId)
		val res: Result<Boolean> =
			conn.detachStorageDomainsToDataCenter(storageDomainId, dataCenterId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun activateFromDataCenter(dataCenterId: String, storageDomainId: String): Boolean {
		log.info("activateFromDataCenter ... storageDomainId: {}, dataCenterId: {}", storageDomainId, dataCenterId)
		val res: Result<Boolean> =
			conn.activateAttachedStorageDomainFromDataCenter(dataCenterId, storageDomainId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun maintenanceFromDataCenter(dataCenterId: String, storageDomainId: String): Boolean {
		log.info("maintenanceFromDataCenter ... storageDomainId: {}, dataCenterId: {}", storageDomainId, dataCenterId)
		val res: Result<Boolean> =
			conn.deactivateAttachedStorageDomainFromDataCenter(dataCenterId, storageDomainId)
		return res.isSuccess
	}


	@Throws(Error::class)
	override fun findAllVmsFromStorageDomain(storageDomainId: String): List<VmVo> {
		log.info("findAllVmsFromStorageDomain ... storageDomainId: {}", storageDomainId)
		val res: List<Vm> =
			conn.findAllVmsFromStorageDomain(storageDomainId).getOrDefault(listOf())
		return res.toStorageDomainVms(conn, storageDomainId)
	}


	@Throws(Error::class)
	override fun findAllDisksFromStorageDomain(storageDomainId: String): List<DiskImageVo> {
		log.info("findAllDisksFromStorageDomain ... storageDomainId: {}", storageDomainId)
		val res: List<Disk> =
			conn.findAllDisksFromStorageDomain(storageDomainId)
				.getOrDefault(listOf())

		return res.map { disk ->
			val diskVmElementEntityOpt: Optional<DiskVmElementEntity> =
				diskVmElementRepository.findByDiskId(UUID.fromString(disk.id()))
			val vmId: String =
				diskVmElementEntityOpt.map { it.toVmId() }.orElse("")

			disk.toDiskMenu(conn, vmId)
		}
	}

	@Throws(Error::class)
	override fun findAllDiskSnapshotsFromStorageDomain(storageDomainId: String): List<SnapshotDiskVo> {
		log.info("findAllDiskSnapshotsFromStorageDomain ... storageDomainId: {}", storageDomainId)
		val res: List<DiskSnapshot> =
			conn.findAllDiskSnapshotsFromStorageDomain(storageDomainId).getOrDefault(listOf())
		// TODO 연결대상
		return res.toSnapshotDiskVos(conn)
	}

	@Throws(Error::class)
	override fun findAllTemplatesFromStorageDomain(storageDomainId: String): List<TemplateVo> {
		log.info("findAllTemplatesFromStorageDomain ... storageDomainId: {}", storageDomainId)
		val res: List<Template> =
			conn.findAllTemplatesFromStorageDomain(storageDomainId).getOrDefault(listOf())
		return res.toTemplatesMenu(conn)
	}

	@Throws(Error::class)
	override fun findAllDiskProfilesFromStorageDomain(storageDomainId: String): List<DiskProfileVo> {
		log.info("findAllDiskProfilesFromStorageDomain ... storageDomainId: {}", storageDomainId)
		val res: List<DiskProfile> =
			conn.findAllDiskProfilesFromStorageDomain(storageDomainId).getOrDefault(listOf())
		return res.toDiskProfileVos()
	}

//	@Throws(Error::class)
//	override fun addDiskProfileFromStorageDomain(diskProfileVo: DiskProfileVo): DiskProfileVo? {
//		log.info("addDiskProfileFromStorageDomain ...")
//		val res: DiskProfile? =
//			conn.addDiskProfile(diskProfileVo.toAddDiskProfileBuilder())
//				.getOrNull()
//		return res?.toDiskProfileVo()
//	}
//
//	@Throws(Error::class)
//	override fun updateDiskProfileFromStorageDomain(diskProfileVo: DiskProfileVo): DiskProfileVo? {
//		log.info("updateDiskProfileFromStorageDomain ...")
//		val res: DiskProfile? =
//			conn.updateDiskProfile(diskProfileVo.toEditDiskProfileBuilder())
//				.getOrNull()
//		return res?.toDiskProfileVo()
//	}
//
//	@Throws(Error::class)
//	override fun removeDiskProfileFromStorageDomain(diskProfileId: String): Boolean {
//		log.info("removeDiskProfileFromStorageDomain ...")
//		val res: Result<Boolean> =
//			conn.removeDiskProfile(diskProfileId)
//		return res.isSuccess
//	}

	@Throws(Error::class)
	override fun findAllEventsFromStorageDomain(storageDomainId: String): List<EventVo> {
		log.info("findAllEventsFromStorageDomain ... storageDomainId: {}", storageDomainId)
		val storageDomain: StorageDomain =
			conn.findStorageDomain(storageDomainId).getOrNull() ?: throw ErrorPattern.STORAGE_DOMAIN_ID_NOT_FOUND.toException()
		val res: List<Event> =
			conn.findAllEvents().getOrDefault(listOf())
				.filter {event ->
					event.storageDomainPresent() &&
						(event.storageDomain().idPresent() && event.storageDomain().id().equals(storageDomainId) || (event.storageDomain().namePresent() && event.storageDomain().name().equals(storageDomain.name())) )
				}
		return res.toEventVos()
	}


	@Deprecated("나중구현")
	@Throws(Error::class)
	override fun findAllPermissionsFromStorageDomain(storageDomainId: String): List<PermissionVo> {
		log.info("findAllPermissionsFromStorageDomain ... storageDomainId: {}", storageDomainId)
		val res: List<Permission> =
			conn.findAllPermissionsFromStorageDomain(storageDomainId).getOrDefault(listOf())
		return res.toPermissionVos(conn)
	}


	companion object {
		private val log by LoggerDelegate()
	}
}