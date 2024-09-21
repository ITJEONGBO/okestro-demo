package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.computing.VmVo
import com.itinfo.itcloud.model.response.Res
import com.itinfo.itcloud.model.storage.*
import com.itinfo.itcloud.service.BaseService
import com.itinfo.itcloud.service.storage.ItStorageService
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.error.toError
import org.ovirt.engine.sdk4.types.DiskAttachment
import org.springframework.stereotype.Service

interface ItVmDiskService {
	/**
	 * [ItVmDiskService.findAllDisksFromVm]
	 * 가상머신 디스크 목록
	 *
	 * @param vmId [String] 가상머신 id
	 * @return List<[DiskAttachmentVo]>
	 */
	fun findAllDisksFromVm(vmId: String): List<DiskAttachmentVo>
	/**
	 * [ItVmDiskService.findDiskFromVm]
	 * 가상머신 디스크
	 *
	 * @param vmId [String] 가상머신 id
	 * @param diskAttachmentId [String] 디스크 id
	 * @return [DiskAttachmentVo]?
	 */
	fun findDiskFromVm(vmId: String, diskAttachmentId: String): DiskAttachmentVo?
//	/**
//	 * [ItVmDiskService.addDisksFromVm]
//	 * 가상머신 디스크 생성/연결
//	 * 이후 없애야함
//	 *
//	 * @param vmVo [VmVo]
//	 * @return List<[DiskAttachmentVo]>
//	 */
//	fun addDisksFromVm(vmVo: VmVo): List<DiskAttachmentVo>?
	/**
	 * [ItVmDiskService.addDiskFromVm]
	 * 가상머신 디스크 생성/연결
	 *
	 * @param vmVo [VmVo]
	 * @return [DiskAttachmentVo]?
	 */
	fun addDiskFromVm(vmVo: VmVo): DiskAttachmentVo?
	/**
	 * [ItVmDiskService.updateDiskFromVm]
	 * 가상머신 디스크 편집
	 *
	 * @param vmVo [VmVo] 가상머신 id
	 * @return [DiskAttachmentVo]?
	 */
	fun updateDiskFromVm(vmVo: VmVo): DiskAttachmentVo?
	/**
	 * [ItVmDiskService.removeDiskFromVm]
	 * 가상머신 디스크 삭제
	 *
	 * @param vmId [String] 가상머신 id
	 * @param diskAttachmentId [String] 가상머신 디스크
	 * @param type [Boolean] 가상머신 디스크
	 * @return [Boolean]
	 */
	fun removeDiskFromVm(vmId: String, diskAttachmentId: String, type: Boolean): Boolean
	/**
	 * [ItVmDiskService.activeDiskFromVm]
	 * 가상머신 디스크 활성화
	 *
	 * @param diskAttachmentId [String] 가상머신 id
	 * @return [Boolean]
	 */
	fun activeDiskFromVm(diskAttachmentId: String): Boolean
	/**
	 * [ItVmDiskService.deactivateDiskFromVm]
	 * 가상머신 디스크 비활성화
	 *
	 * @param diskAttachmentId [String] 가상머신 id
	 * @return [Boolean]
	 */
	fun deactivateDiskFromVm(diskAttachmentId: String): Boolean
	/**
	 *
	 */
	fun setDiskMove(diskAttachmentId: String): DiskImageVo // 디스크 이동창
	/**
	 * [ItVmDiskService.moveDiskFromVm]
	 * 가상머신 디스크 스토리지 이동
	 *
	 * @param diskAttachmentId [String] 가상머신 id
	 * @param diskVo [DiskImageVo] 디스크 이미지
	 * @return [Boolean]
	 */
	fun moveDiskFromVm(diskAttachmentId: String, diskVo: DiskImageVo): Boolean
}

@Service
class VmDiskService(
	private val itStorageService: ItStorageService
): BaseService(), ItVmDiskService {

	override fun findAllDisksFromVm(vmId: String): List<DiskAttachmentVo> {
		log.debug("findAllDiskFromVm ... vmId: {}", vmId)
		val res: List<DiskAttachment> =
			conn.findAllDiskAttachmentsFromVm(vmId)
				.getOrDefault(listOf())
		return res.toDiskAttachmentVos(conn)
	}

	override fun findDiskFromVm(vmId: String, diskAttachmentId: String): DiskAttachmentVo? {
		log.debug("findDiskFromVm ... vmId: {}", vmId)
		val res: DiskAttachment? =
			conn.findDiskAttachmentFromVm(vmId, diskAttachmentId)
				.getOrNull()
		return res?.toDiskAttachmentVo(conn)
	}

//	// 가상머신 생성/편집 할때에만 디스크 여러개 만들 수 있음
//	override fun addDisksFromVm(vmVo: VmVo): List<DiskAttachmentVo> {
//		log.info("addDisksFromVm ... ")
//		val res: List<DiskAttachment> =
//			conn.addMultipleDiskAttachmentsToVm(
//				vmVo.id,
//				vmVo.diskAttachmentVos.toAddDiskAttachmentList()
//			)
//			.getOrDefault(listOf())
//		return res.toDiskAttachmentVos(conn)
//	}

	override fun addDiskFromVm(vmVo: VmVo): DiskAttachmentVo? {
		val res: DiskAttachment? =
			conn.addDiskAttachmentToVm(
				vmVo.id,
				vmVo.diskAttachmentVo.toAddDisk()
			)
			.getOrNull()
		return res?.toDiskAttachmentVo(conn)
	}

	override fun updateDiskFromVm(vmVo: VmVo): DiskAttachmentVo? {
		// add 와 비슷한 방법을 쓸듯요
		TODO("Not yet implemented")
	}

	override fun removeDiskFromVm(vmId: String, diskAttachmentId: String, type: Boolean): Boolean {
		TODO("Not yet implemented")
	}

	override fun activeDiskFromVm(diskAttachmentId: String): Boolean {
		TODO("Not yet implemented")
	}

	override fun deactivateDiskFromVm(diskAttachmentId: String): Boolean {
		TODO("Not yet implemented")
	}

	override fun setDiskMove(diskAttachmentId: String): DiskImageVo {
		log.debug("setDiskMove ... diskAttachmentId: {}", diskAttachmentId)
		TODO("Not yet implemented")
	}

	override fun moveDiskFromVm(diskAttachmentId: String, diskVo: DiskImageVo): Boolean {
		TODO("Not yet implemented")
	}

	companion object {
		private val log by LoggerDelegate()
	}
}