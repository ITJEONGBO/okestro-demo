package com.itinfo.itcloud.service.storage

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.*
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.setting.toPermissionVos
import com.itinfo.itcloud.model.storage.*
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import org.ovirt.engine.sdk4.builders.*
import org.ovirt.engine.sdk4.services.*
import org.ovirt.engine.sdk4.types.DataCenter
import org.ovirt.engine.sdk4.types.StorageDomain
import org.ovirt.engine.sdk4.types.Disk
import org.ovirt.engine.sdk4.types.DiskProfile
import org.ovirt.engine.sdk4.types.Permission
import org.ovirt.engine.sdk4.types.Event
import org.ovirt.engine.sdk4.types.StorageConnection
import org.ovirt.engine.sdk4.types.StorageConnectionExtension
import org.ovirt.engine.sdk4.types.Template
import org.ovirt.engine.sdk4.types.Vm
import org.springframework.stereotype.Service

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

	// 도메인 생성 - iSCSI 유형에서 대상 LUN 목록?

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
	fun findAllDataCentersFromStorageDomain(storageDomainId: String): List<DataCenterVo>

	/**
	 * [ItStorageService.connectFromDataCenter]
	 * 스토리지 도메인 - 연결
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun connectFromDataCenter(dataCenterId: String, storageDomainId: String): Boolean
	/**
	 * [ItStorageService.separateFromDataCenter]
	 * 스토리지 도메인 - 분리
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun separateFromDataCenter(dataCenterId: String, storageDomainId: String): Boolean
	/**
	 * [ItStorageService.activateFromDataCenter]
	 * 스토리지 도메인 - 활성
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun activateFromDataCenter(dataCenterId: String, storageDomainId: String): Boolean
	/**
	 * [ItStorageService.deactivateFromDataCenter]
	 * 스토리지 도메인 - 유지보수
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun deactivateFromDataCenter(dataCenterId: String, storageDomainId: String): Boolean

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
	 * [ItStorageService.findAllTemplatesFromStorageDomain]
	 * 스토리지도메인 - 템플릿 목록
	 *
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @return List<[TemplateVo]> 템플릿 목록
	 */
	@Throws(Error::class)
	fun findAllTemplatesFromStorageDomain(storageDomainId: String): List<TemplateVo>
	/**
	 * [ItStorageService.findAllDisksFromStorageDomain]
	 * 스토리지 도메인 - 디스크 목록
	 *
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @return List<[DiskImageVo]> 디스크 목록
	 */
	@Throws(Error::class)
	fun findAllDisksFromStorageDomain(storageDomainId: String): List<DiskImageVo>

//	/**
//	 * [ItStorageService.findAllDiskProfilesFromStorageDomain]
//	 * 스토리지 도메인 - 디스크 프로파일 목록
//	 *
//	 * @param storageDomainId [String] 스토리지 도메인 Id
//	 * @return List<[DiskProfileVo]> 디스크 프로파일 목록
//	 */
//	@Throws(Error::class)
//	fun findAllDiskProfilesFromStorageDomain(storageDomainId: String): List<DiskProfileVo>
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
	 * [ItStorageService.findAllPermissionsFromStorageDomain]
	 * 스토리지도메인 - 권한
	 *
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @return List<[PermissionVo]> 권한 목록
	 */
	@Throws(Error::class)
	@Deprecated("나중구현")
	fun findAllPermissionsFromStorageDomain(storageDomainId: String): List<PermissionVo>
	/**
	 * [ItStorageService.findAllEventsFromStorageDomain]
	 * 스토리지도메인 - 이벤트
	 *
	 * @param storageDomainId [String] 스토리지 도메인 Id
	 * @return List<[EventVo]>
	 */
	@Throws(Error::class)
	fun findAllEventsFromStorageDomain(storageDomainId: String): List<EventVo>
}

@Service
class StorageServiceImpl(

): BaseService(), ItStorageService {

	@Throws(Error::class)
	override fun findAll(): List<StorageDomainVo> {
		log.info("findAll ...")
		val res: List<StorageDomain> =
			conn.findAllStorageDomains()
				.getOrDefault(listOf())
		return res.toStorageDomainsMenu(conn)
	}

	@Throws(Error::class)
	override fun findAllFromDataCenter(dataCenterId: String): List<StorageDomainVo> {
		log.info("findAllFromDataCenter ... dcId: $dataCenterId")
		val res: List<StorageDomain> =
			conn.findAllAttachedStorageDomainsFromDataCenter(dataCenterId)
				.getOrDefault(listOf())
		return res.toStorageDomainsMenu(conn)
	}

	@Throws(Error::class)
	override fun findOne(storageDomainId: String): StorageDomainVo? {
		log.info("findOne... ")
		val res: StorageDomain? =
			conn.findStorageDomain(storageDomainId)
				.getOrNull()
		return res?.toStorageDomainVo(conn)
	}


	// requires: name, type, host, and storage attributes. Identify the host attribute with the id or name attributes.
	// To add a new storage domain with specified name, type, storage.type, storage.address, and storage.path,
	// and using a host with an id 123, send a request like this
	@Throws(Error::class)
	override fun add(storageDomainVo: StorageDomainVo): StorageDomainVo? {
		log.info("add ... ")
//		val res: StorageDomain? =
//			conn.addStorageDomain(storageDomainVo.toAddStorageDomainBuilder(conn))
//				.getOrNull()
//		return res?.toStorageDomainVo(conn)
		TODO("Not yet implemented")
	}

	@Throws(Error::class)
	override fun import(storageDomainVo: StorageDomainVo): StorageDomainVo? {
		TODO("Not yet implemented")
	}

	@Throws(Error::class)
	override fun update(storageDomainVo: StorageDomainVo): StorageDomainVo? {
		TODO("Not yet implemented")
	}
//		val storageDomainsService = system.storageDomainsService()
//		val dataCenterService = system.dataCentersService().dataCenterService(storageDomainVo.dataCenterVo.id)
//		// TODO: storageDomain서비스에서 한번 넣고, DataCenter의 attachedStorageDoamin서비스에서 한번 더 생성하는지 모르겠음
//		try {
//			var storageDomain = storageDomainsService.add().storageDomain(storageDomainVo.toStorageDomainBuilder(conn)).send().storageDomain()
//			val storageDomainService = storageDomainsService.storageDomainService(storageDomain.id())
//
//			do {
//				Thread.sleep(2000L)
//				storageDomain = storageDomainService.get().send().storageDomain()
//			} while (storageDomain.status() != StorageDomainStatus.UNATTACHED)
//
//			val asdsService = dataCenterService.storageDomainsService()
//			val asdService = asdsService.storageDomainService(storageDomain.id())
//			try {
//				asdsService.add().storageDomain(StorageDomainBuilder().id(storageDomain.id())).send()
////					.dataCenter(new DataCenterBuilder().id(dcVo.getDatacenterId()).build())
//			} catch (var18: java.lang.Exception) {
//				var18.printStackTrace()
//			}
//			do {
//				Thread.sleep(2000L)
//				storageDomain = asdService.get().send().storageDomain()
//			} while (storageDomain.status() != StorageDomainStatus.ACTIVE)
//
//			return true
//		}

	@Throws(Error::class)
	override fun remove(storageDomainId: String): Boolean {
		log.info("removeDomain ... storageDomainId: {}", storageDomainId)
		val res: Result<Boolean> =
			conn.removeStorageDomain(storageDomainId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun destroy(storageDomainId: String): Boolean {
		TODO("Not yet implemented")
	}


	@Throws(Error::class)
	// TODO 이상함
	override fun findAllDataCentersFromStorageDomain(storageDomainId: String): List<DataCenterVo> {
		log.info("findAllDataCentersFromStorageDomain ... storageDomainId: {}", storageDomainId)
		val storageDomain: StorageDomain =
			conn.findStorageDomain(storageDomainId)
				.getOrNull() ?: throw ErrorPattern.STORAGE_DOMAIN_ID_NOT_FOUND.toException()

		val res: List<DataCenterVo> =
			storageDomain.dataCenters()
				.map { dataCenter ->
					val dc: DataCenter =
						conn.findDataCenter(dataCenter.id())
							.getOrNull() ?: throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()
					dc.toDataCenterIdName()
				}
		return res
	}

	@Throws(Error::class)
	override fun connectFromDataCenter(dataCenterId: String, storageDomainId: String): Boolean {
		TODO("Not yet implemented")
	}

	@Throws(Error::class)
	override fun separateFromDataCenter(dataCenterId: String, storageDomainId: String): Boolean {
		TODO("Not yet implemented")
	}

	@Throws(Error::class)
	override fun activateFromDataCenter(dataCenterId: String, storageDomainId: String): Boolean {
		TODO("Not yet implemented")
	}

	@Throws(Error::class)
	override fun deactivateFromDataCenter(dataCenterId: String, storageDomainId: String): Boolean {
		TODO("Not yet implemented")
	}


	@Throws(Error::class)
	override fun findAllVmsFromStorageDomain(storageDomainId: String): List<VmVo> {
		log.info("findAllVmsFromStorageDomain ... storageDomainId: {}", storageDomainId)
		val res: List<Vm> =
			conn.findAllVmsFromStorageDomain(storageDomainId)
				.getOrDefault(listOf())
		return res.toVmsIdName()
	}

	@Throws(Error::class)
	override fun findAllTemplatesFromStorageDomain(storageDomainId: String): List<TemplateVo> {
		log.info("findAllTemplatesFromStorageDomain ... storageDomainId: {}", storageDomainId)
		val res: List<Template> =
			conn.findAllTemplatesFromStorageDomain(storageDomainId)
				.getOrDefault(listOf())
		return res.toTemplateVos(conn)
	}

	@Throws(Error::class)
	override fun findAllDisksFromStorageDomain(storageDomainId: String): List<DiskImageVo> {
		log.info("findAllDisksFromStorageDomain ... storageDomainId: {}", storageDomainId)
		val res: List<Disk> =
			conn.findAllDisksFromStorageDomain(storageDomainId)
				.getOrDefault(listOf())
		return res.toDiskImageVos(conn)
	}

//	@Throws(Error::class)
//	override fun findAllDiskProfilesFromStorageDomain(storageDomainId: String): List<DiskProfileVo> {
//		log.info("findAllDiskProfilesFromStorageDomain ... storageDomainId: {}", storageDomainId)
//		val res: List<DiskProfile> =
//			conn.findAllDiskProfilesFromStorageDomain(storageDomainId)
//				.getOrDefault(listOf())
//		return res.toDiskProfileVos()
//	}
//
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


	@Deprecated("나중구현")
	@Throws(Error::class)
	override fun findAllPermissionsFromStorageDomain(storageDomainId: String): List<PermissionVo> {
		log.info("findAllPermissionsFromStorageDomain ... storageDomainId: {}", storageDomainId)
		val res: List<Permission> =
			conn.findAllPermissionsFromStorageDomain(storageDomainId)
				.getOrDefault(listOf())
		return res.toPermissionVos(conn)
	}

	@Throws(Error::class)
	override fun findAllEventsFromStorageDomain(storageDomainId: String): List<EventVo> {
		log.info("findAllEventsFromStorageDomain ... storageDomainId: {}", storageDomainId)
		val storageDomain: StorageDomain =
			conn.findStorageDomain(storageDomainId).getOrNull() ?: throw ErrorPattern.STORAGE_DOMAIN_ID_NOT_FOUND.toException()
		val res: List<Event> =
			conn.findAllEvents()
				.getOrDefault(listOf())
				.filter {event ->
					event.storageDomainPresent() &&
						(event.storageDomain().idPresent() && event.storageDomain().id().equals(storageDomainId) || (event.storageDomain().namePresent() && event.storageDomain().name().equals(storageDomain.name())) )
				}
		return res.toEventVos()
	}


	companion object {
		private val log by LoggerDelegate()
	}
}