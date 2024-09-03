package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
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
	 * [ItVmDiskService.findOneDiskFromVm]
	 * 가상머신 디스크
	 *
	 * @param vmId [String] 가상머신 id
	 * @param diskAttachmentId [String] 디스크 id
	 * @return [DiskAttachmentVo]
	 */
	fun findOneDiskFromVm(vmId: String, diskAttachmentId: String): DiskAttachmentVo
	/**
	 * [ItVmDiskService.addDiskFromVm]
	 * 가상머신 디스크 생성
	 *
	 * @param vmId [String] 가상머신 id
	 * @param diskAttachmentVo [DiskAttachmentVo] 가상머신 id
	 * @return [Boolean]
	 */
	fun addDiskFromVm(vmId: String, diskAttachmentVo: DiskAttachmentVo): DiskAttachmentVo?
	/**
	 * [ItVmDiskService.updateDiskFromVm]
	 * 가상머신 디스크 생성
	 *
	 * @param vmId [String] 가상머신 id
	 * @param diskAttachmentVo [DiskAttachmentVo] 가상머신 id
	 * @return [Boolean]
	 */
	fun updateDiskFromVm(vmId: String, diskAttachmentVo: DiskAttachmentVo): DiskAttachmentVo?
	/**
	 * [ItVmDiskService.removeDiskFromVm]
	 * 가상머신 디스크 삭제
	 *
	 * @param vmId [String] 가상머신 id
	 * @param diskAttachmentId [String] 가상머신 디스크
	 * @param type [Boolean] 가상머신 디스크
	 * @return [Boolean]
	 */
	fun removeDiskFromVm(vmId: String, diskAttachmentId: String, type: Boolean): Res<Boolean>
	/**
	 * [ItVmDiskService.activeDiskFromVm]
	 * 가상머신 디스크 활성화
	 *
	 * @param diskAttachmentId [String] 가상머신 id
	 * @return [Boolean]
	 */
	fun activeDiskFromVm(diskAttachmentId: String): Res<Boolean>
	/**
	 * [ItVmDiskService.deactivateDiskFromVm]
	 * 가상머신 디스크 비활성화
	 *
	 * @param diskAttachmentId [String] 가상머신 id
	 * @return [Boolean]
	 */
	fun deactivateDiskFromVm(diskAttachmentId: String): Res<Boolean>
	fun setDiskMove(diskAttachmentId: String): DiskImageVo // 디스크 이동창
	/**
	 * [ItVmDiskService.moveDiskFromVm]
	 * 가상머신 디스크 스토리지 이동
	 *
	 * @param diskAttachmentId [String] 가상머신 id
	 * @param diskVo [DiskImageVo] 디스크 이미지
	 * @return [Boolean]
	 */
	fun moveDiskFromVm(diskAttachmentId: String, diskVo: DiskImageVo): Res<Boolean>
}

@Service
class VmDiskService(
	private val itStorageService: ItStorageService
): BaseService(), ItVmDiskService {

	override fun findAllDisksFromVm(vmId: String): List<DiskAttachmentVo> {
		log.debug("findAllDisksFromVm ... vmId: {}", vmId)
		conn.findVm(vmId).getOrNull()?: throw ErrorPattern.VM_NOT_FOUND.toError()
		val res: List<DiskAttachment> =
			conn.findAllDiskAttachmentsFromVm(vmId)
				.getOrDefault(listOf())
		return res.toDiskAttachmentVos(conn)
	}

	override fun findOneDiskFromVm(vmId: String, diskAttachmentId: String): DiskAttachmentVo {
		log.debug("findOneDiskFromVm ... vmId: {}", vmId)
		conn.findVm(vmId).getOrNull()?: throw ErrorPattern.VM_NOT_FOUND.toError()
		val res: DiskAttachment =
			conn.findDiskAttachmentFromVm(vmId, diskAttachmentId)
				.getOrNull() ?: throw ErrorPattern.DISK_ATTACHMENT_NOT_FOUND.toError()
		return res.toDiskAttachmentVo(conn)
	}

	override fun addDiskFromVm(vmId: String, diskAttachmentVo: DiskAttachmentVo): DiskAttachmentVo? {
		log.debug("addDiskFromVm ... vmId: {}", vmId)
		conn.findVm(vmId).getOrNull()?: throw ErrorPattern.VM_NOT_FOUND.toError()

		// 디스크 생성
		val diskImageVo: DiskImageVo? =
			itStorageService.addDisk(diskAttachmentVo.diskImageVo)

		diskAttachmentVo.toDiskAttachmentBuilder(conn, diskAttachmentVo)

		/**
		 * 가상머신에서 디스크 생성과정은
		 * 디스크를 만들고, 해당 디스크를 diskattachment를 이용해서 붙이는거
		 * 그러니까
		 * DIsk Add 하고 나온 disk id를 가지고 diskAttchment 하기
		 */
		TODO("Not yet implemented")
	}

	override fun updateDiskFromVm(vmId: String, diskAttachmentVo: DiskAttachmentVo): DiskAttachmentVo? {
		TODO("Not yet implemented")
	}

	override fun removeDiskFromVm(vmId: String, diskAttachmentId: String, type: Boolean): Res<Boolean> {
		TODO("Not yet implemented")
	}

	override fun activeDiskFromVm(diskAttachmentId: String): Res<Boolean> {
		TODO("Not yet implemented")
	}

	override fun deactivateDiskFromVm(diskAttachmentId: String): Res<Boolean> {
		TODO("Not yet implemented")
	}

	override fun setDiskMove(diskAttachmentId: String): DiskImageVo {
		log.debug("setDiskMove ... diskAttachmentId: {}", diskAttachmentId)
		TODO("Not yet implemented")
	}

	override fun moveDiskFromVm(diskAttachmentId: String, diskVo: DiskImageVo): Res<Boolean> {
		TODO("Not yet implemented")
	}

	companion object {
		private val log by LoggerDelegate()
	}
}