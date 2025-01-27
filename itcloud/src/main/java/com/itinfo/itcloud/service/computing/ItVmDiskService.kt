package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.storage.*
import com.itinfo.itcloud.service.BaseService
import com.itinfo.itcloud.service.storage.DiskServiceImpl
import com.itinfo.itcloud.service.storage.DiskServiceImpl.Companion
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
	 * [ItVmDiskService.findAllFromVm]
	 * 가상머신 디스크 목록
	 *
	 * @param vmId [String] 가상머신 Id
	 * @return List<[DiskAttachmentVo]>
	 */
	@Throws(Error::class)
	fun findAllFromVm(vmId: String): List<DiskAttachmentVo>
	/**
	 * [ItVmDiskService.findOneFromVm]
	 * 가상머신 디스크
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param diskAttachmentId [String] 디스크 Id
	 * @return [DiskAttachmentVo]?
	 */
	@Throws(Error::class)
	fun findOneFromVm(vmId: String, diskAttachmentId: String): DiskAttachmentVo?

	/**
	 * [ItVmDiskService.addFromVm]
	 * 가상머신 디스크 생성
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param diskAttachmentVo [DiskAttachmentVo]
	 * @return [DiskAttachmentVo]?
	 */
	@Throws(Error::class)
	fun addFromVm(vmId: String, diskAttachmentVo: DiskAttachmentVo): DiskAttachmentVo?
	//
	/**
	 * [ItVmDiskService.attachMultiFromVm]
	 * 가상머신 디스크 연결
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param diskAttachmentVos List<[DiskAttachmentVo]>
	 * @return List<[DiskAttachmentVo]>?
	 */
	@Throws(Error::class)
	fun attachMultiFromVm(vmId: String, diskAttachmentVos: List<DiskAttachmentVo>): Boolean
	/**
	 * [ItVmDiskService.updateFromVm]
	 * 가상머신 디스크 편집
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param diskAttachmentVo [DiskAttachmentVo]
	 * @return [DiskAttachmentVo]?
	 */
	@Throws(Error::class)
	fun updateFromVm(vmId: String, diskAttachmentVo: DiskAttachmentVo): DiskAttachmentVo?
	/**
	 * [ItVmDiskService.removeFromVm]
	 * 가상머신 디스크 삭제
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param diskAttachmentId [String] 디스크 id
	 * @param detachOnly [Boolean] 디스크 완전삭제 여부
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun removeFromVm(vmId: String, diskAttachmentId: String, detachOnly: Boolean): Boolean
	/**
	 * [ItVmDiskService.removeMultiFromVm]
	 * 가상머신 디스크 삭제 (다중)
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param diskAttachmentVos List<[DiskAttachmentVo]>
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun removeMultiFromVm(vmId: String, diskAttachmentVos: List<DiskAttachmentVo>): Boolean
	/**
	 * [ItVmDiskService.activeMultiFromVm]
	 * 가상머신 디스크 활성화
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param diskAttachmentIds List<[String]> 디스크 목록
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun activeMultiFromVm(vmId: String, diskAttachmentIds: List<String>): Boolean
	/**
	 * [ItVmDiskService.deactivateMultiFromVm]
	 * 가상머신 디스크 비활성화
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param diskAttachmentIds List<[String]> 디스크 목록
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun deactivateMultiFromVm(vmId: String, diskAttachmentIds: List<String>): Boolean
	/**
	 * [ItVmDiskService.findAllStorageDomains]
	 * 보류:[ItStorageService.findAllFromDataCenter] 와 다른점은 내가 가지고 있는 도메인은 제외하고 출력
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
	 * [ItVmDiskService.moveFromVm]
	 * 가상머신 디스크 스토리지 이동
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param diskAttachmentVo [DiskAttachmentVo]
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun moveFromVm(vmId: String, diskAttachmentVo: DiskAttachmentVo): Boolean
}

@Service
class VmDiskService(

): BaseService(), ItVmDiskService {

	@Throws(Error::class)
	override fun findAllFromVm(vmId: String): List<DiskAttachmentVo> {
		log.info("findAllFromVm ... vmId: {}", vmId)
		val res: List<DiskAttachment> =
			conn.findAllDiskAttachmentsFromVm(vmId)
				.getOrDefault(listOf())
		return res.toDiskAttachmentVos(conn)
	}

	@Throws(Error::class)
	override fun findOneFromVm(vmId: String, diskAttachmentId: String): DiskAttachmentVo? {
		log.info("findOneFromVm ... vmId: {}", vmId)
		val res: DiskAttachment? = conn.findDiskAttachmentFromVm(vmId, diskAttachmentId)
			.getOrNull()
		return res?.toDiskAttachmentVo(conn)
	}

	@Throws(Error::class)
	override fun addFromVm(vmId: String, diskAttachmentVo: DiskAttachmentVo): DiskAttachmentVo? {
		log.info("addFromVm ... vmId: {}", vmId)
		val res: DiskAttachment? =
			conn.addDiskAttachmentToVm(
				vmId,
				diskAttachmentVo.toAddDiskAttachment()
			).getOrNull()
		return res?.toDiskAttachmentVo(conn)
	}

	@Throws(Error::class)
	override fun attachMultiFromVm(vmId: String, diskAttachmentVos: List<DiskAttachmentVo>): Boolean {
		log.info("attachMultiFromVm ... vmId: {}", vmId)
		val res: Result<Boolean> =
			conn.addMultipleDiskAttachmentsToVm(
				vmId,
				diskAttachmentVos.toAttachDiskList()
			)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun updateFromVm(vmId: String, diskAttachmentVo: DiskAttachmentVo): DiskAttachmentVo? {
		log.info("updateFromVm ... vmId: {}", vmId)
		val res: DiskAttachment? =
			conn.updateDiskAttachmentToVm(
				vmId,
				diskAttachmentVo.toEditDiskAttachment()
			).getOrNull()
		return res?.toDiskAttachmentVo(conn)
	}

	@Throws(Error::class)
	override fun removeFromVm(vmId: String, diskAttachmentId: String, detachOnly: Boolean): Boolean {
		log.info("removeFromVm ... vmId: $vmId, diskAttachmentId: $diskAttachmentId, detachOnly: $detachOnly")
		val res: Result<Boolean> =
			conn.removeDiskAttachmentToVm(vmId, diskAttachmentId, detachOnly)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun removeMultiFromVm(vmId: String, diskAttachmentVos: List<DiskAttachmentVo>): Boolean {
		log.info("removeMultiFromVm ...")
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
	override fun activeMultiFromVm(vmId: String, diskAttachmentIds: List<String>): Boolean {
		log.info("activeMultiFromVm ... vmId: {}", vmId)
		val res: Result<Boolean> =
			conn.activeDiskAttachmentsToVm(
				vmId,
				diskAttachmentIds
			)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun deactivateMultiFromVm(vmId: String, diskAttachmentIds: List<String>): Boolean {
		log.info("deactivateMultiFromVm ... vmId: {}", vmId)
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
	override fun moveFromVm(vmId: String, diskAttachmentVo: DiskAttachmentVo): Boolean {
		log.info("moveFromVm ... diskAttachmentVo: {}", diskAttachmentVo.id)
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