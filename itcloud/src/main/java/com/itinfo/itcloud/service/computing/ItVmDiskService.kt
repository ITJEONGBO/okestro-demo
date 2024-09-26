package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.storage.*
import com.itinfo.itcloud.service.BaseService
import com.itinfo.itcloud.service.storage.ItStorageService
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.types.Disk
import org.ovirt.engine.sdk4.types.DiskAttachment
import org.ovirt.engine.sdk4.types.StorageDomain
import org.ovirt.engine.sdk4.types.StorageDomainStatus
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

interface ItVmDiskService {
	/**
	 * [ItVmDiskService.findAllDisksFromVm]
	 * 가상머신 디스크 목록
	 *
	 * @param vmId [String] 가상머신 Id
	 * @return List<[DiskAttachmentVo]>
	 */
	@Throws(Error::class)
	fun findAllDisksFromVm(vmId: String): List<DiskAttachmentVo>
	/**
	 * [ItVmDiskService.findDiskFromVm]
	 * 가상머신 디스크
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param diskAttachmentId [String] 디스크 Id
	 * @return [DiskAttachmentVo]?
	 */
	@Throws(Error::class)
	fun findDiskFromVm(vmId: String, diskAttachmentId: String): DiskAttachmentVo?

	/**
	 * [ItVmDiskService.addDiskFromVm]
	 * 가상머신 디스크 생성
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param diskAttachmentVo [DiskAttachmentVo]
	 * @return [DiskAttachmentVo]?
	 */
	@Throws(Error::class)
	fun addDiskFromVm(vmId: String, diskAttachmentVo: DiskAttachmentVo): DiskAttachmentVo?
	//
	/**
	 * [ItVmDiskService.attachMultiDiskFromVm]
	 * 가상머신 디스크 연결
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param diskAttachmentVos List<[DiskAttachmentVo]>
	 * @return List<[DiskAttachmentVo]>?
	 */
	@Throws(Error::class)
	fun attachMultiDiskFromVm(vmId: String, diskAttachmentVos: List<DiskAttachmentVo>): Boolean
	/**
	 * [ItVmDiskService.updateDiskFromVm]
	 * 가상머신 디스크 편집
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param diskAttachmentVo [DiskAttachmentVo]
	 * @return [DiskAttachmentVo]?
	 */
	@Throws(Error::class)
	fun updateDiskFromVm(vmId: String, diskAttachmentVo: DiskAttachmentVo): DiskAttachmentVo?
	/**
	 * [ItVmDiskService.removeDisksFromVm]
	 * 가상머신 디스크 삭제
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param diskAttachmentVos List<[DiskAttachmentVo]>
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun removeDisksFromVm(vmId: String, diskAttachmentVos: List<DiskAttachmentVo>): Boolean
	/**
	 * [ItVmDiskService.activeDisksFromVm]
	 * 가상머신 디스크 활성화
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param diskAttachmentIds List<[String]> 디스크 목록
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun activeDisksFromVm(vmId: String, diskAttachmentIds: List<String>): Boolean
	/**
	 * [ItVmDiskService.deactivateDisksFromVm]
	 * 가상머신 디스크 비활성화
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param diskAttachmentIds List<[String]> 디스크 목록
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun deactivateDisksFromVm(vmId: String, diskAttachmentIds: List<String>): Boolean
	/**
	 * [ItVmDiskService.findAllStorageDomains]
	 * 보류:[ItStorageService.findAllDomainsFromDataCenter] 와 다른점은 내가 가지고 있는 도메인은 제외하고 출력
	 * 가상머신 디스크 이동창- 스토리지 도메인
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param diskAttachmentId [String]
	 * @return [DiskAttachmentVo]
	 */
	@Throws(Error::class)
	fun findAllStorageDomains(vmId: String, diskAttachmentId: String): List<StorageDomainVo>

	// 가상머신 디스크 이동창- 디스크 프로파일 [ItStorageService.findAllDiskProfilesFromStorageDomain] 사용

	/**
	 * [ItVmDiskService.moveDiskFromVm]
	 * 가상머신 디스크 스토리지 이동
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param diskAttachmentVo [DiskAttachmentVo]
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun moveDiskFromVm(vmId: String, diskAttachmentVo: DiskAttachmentVo): Boolean
}

@Service
class VmDiskService(

): BaseService(), ItVmDiskService {

	@Throws(Error::class)
	override fun findAllDisksFromVm(vmId: String): List<DiskAttachmentVo> {
		log.info("findAllDisksFromVm ... vmId: {}", vmId)
		val res: List<DiskAttachment> =
			conn.findAllDiskAttachmentsFromVm(vmId)
				.getOrDefault(listOf())
		return res.toDiskAttachmentVos(conn)
	}

	@Throws(Error::class)
	override fun findDiskFromVm(vmId: String, diskAttachmentId: String): DiskAttachmentVo? {
		log.info("findDiskFromVm ... vmId: {}", vmId)
		val res: DiskAttachment? =
			conn.findDiskAttachmentFromVm(vmId, diskAttachmentId)
				.getOrNull()
		return res?.toDiskAttachmentVo(conn)
	}

	@Throws(Error::class)
	override fun addDiskFromVm(vmId: String, diskAttachmentVo: DiskAttachmentVo): DiskAttachmentVo? {
		log.info("addDiskFromVm ... vmId: {}", vmId)
		val res: DiskAttachment? =
			conn.addDiskAttachmentToVm(
				vmId,
				diskAttachmentVo.toAddDiskAttachment()
			).getOrNull()
		return res?.toDiskAttachmentVo(conn)
	}

	@Throws(Error::class)
	override fun attachMultiDiskFromVm(vmId: String, diskAttachmentVos: List<DiskAttachmentVo>): Boolean {
		log.info("attachMultiDiskFromVm ... vmId: {}", vmId)
		val res: Result<Boolean> =
			conn.addMultipleDiskAttachmentsToVm(
				vmId,
				diskAttachmentVos.toAttachDiskList()
			)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun updateDiskFromVm(vmId: String, diskAttachmentVo: DiskAttachmentVo): DiskAttachmentVo? {
		log.info("updateDiskFromVm ... vmId: {}", vmId)
		val res: DiskAttachment? =
			conn.updateDiskAttachmentToVm(
				vmId,
				diskAttachmentVo.toEditDiskAttachment()
			).getOrNull()
		return res?.toDiskAttachmentVo(conn)
	}

	@Throws(Error::class)
	override fun removeDisksFromVm(vmId: String, diskAttachmentVos: List<DiskAttachmentVo>): Boolean {
		log.info("removeDiskFromVm ...")
		// 상태가 deactivate 상태인지
//		val res: Result<Boolean> =
//			diskAttachmentVos.forEach { diskAttachmentVo ->
//				conn.removeDiskAttachmentToVm(
//					diskAttachmentVo.vmVo.id,
//					diskAttachmentVo.id,
//					diskAttachmentVo.detachOnly
//				)
//			}
//		return res.isSuccess
		TODO()
	}

	@Throws(Error::class)
	override fun activeDisksFromVm(vmId: String, diskAttachmentIds: List<String>): Boolean {
		log.info("activeDiskFromVm ... vmId: {}", vmId)
		val res: Result<Boolean> =
			conn.activeDiskAttachmentsToVm(
				vmId,
				diskAttachmentIds
			)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun deactivateDisksFromVm(vmId: String, diskAttachmentIds: List<String>): Boolean {
		log.info("deactivateDiskFromVm ... vmId: {}", vmId)
		val res: Result<Boolean> =
			conn.deactivateDiskAttachmentsToVm(
				vmId,
				diskAttachmentIds
			)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun findAllStorageDomains(vmId: String, diskAttachmentId: String): List<StorageDomainVo> {
		log.info("findAllStorageDomains ... diskAttachmentId: {}", diskAttachmentId)
		val diskAttachment: DiskAttachment =
			conn.findDiskAttachmentFromVm(vmId, diskAttachmentId)
				.getOrNull() ?: throw ErrorPattern.DISK_ATTACHMENT_ID_NOT_FOUND.toException()
		val disk: Disk =
			conn.findDisk(diskAttachment.disk().id())
				.getOrNull() ?:throw ErrorPattern.DISK_NOT_FOUND.toException()

		val res: List<StorageDomain> =
			conn.findAllStorageDomains()
				.getOrDefault(listOf())
				.filter { it.id() != disk.storageDomains().first().id() && it.status() != StorageDomainStatus.UNATTACHED }
		return res.toStorageDomainsMenu(conn)
	}

	@Throws(Error::class)
	override fun moveDiskFromVm(vmId: String, diskAttachmentVo: DiskAttachmentVo): Boolean {
		log.info("moveDiskFromVm ... diskAttachmentVo: {}", diskAttachmentVo.id)
		val res: Result<Boolean> =
			conn.moveDisk(
				diskAttachmentVo.diskImageVo.id,
				diskAttachmentVo.diskImageVo.storageDomainVo.id
			)
		return res.isSuccess
	}


	companion object {
		private val log by LoggerDelegate()
	}
}