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
	 * [ItVmDiskService.findOneDiskFromVm]
	 * 가상머신 디스크
	 *
	 * @param vmId [String] 가상머신 id
	 * @param diskAttachmentId [String] 디스크 id
	 * @return [DiskAttachmentVo]
	 */
	fun findOneDiskFromVm(vmId: String, diskAttachmentId: String): DiskAttachmentVo
	/**
	 * [ItVmDiskService.addDisksFromVm]
	 * 가상머신 디스크 생성/연결
	 * 이후 없애야함
	 *
	 * @param vmVo [VmVo]
	 * @return List<[DiskAttachmentVo]>
	 */
	fun addDisksFromVm(vmVo: VmVo): List<DiskAttachmentVo>
	/**
	 * [ItVmDiskService.addDiskFromVm]
	 * 가상머신 디스크 생성/연결
	 *
	 * @param vmVo [VmVo]
	 * @return[DiskAttachmentVo]
	 */
	fun addDiskFromVm(vmVo: VmVo): DiskAttachmentVo
	/**
	 * [ItVmDiskService.updateDiskFromVm]
	 * 가상머신 디스크 수정
	 *
	 * @param vmVo [VmVo] 가상머신 id
	 * @return [DiskAttachmentVo]
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
	fun moveDiskFromVm(diskAttachmentId: String, diskVo: DiskImageVo): Res<Boolean>
}

@Service
class VmDiskService(
	private val itStorageService: ItStorageService
): BaseService(), ItVmDiskService {

	override fun findAllDiskFromVm(vmId: String): List<DiskAttachmentVo> {
		log.debug("findAllDiskFromVm ... vmId: {}", vmId)
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

	override fun addDisksFromVm(vmVo: VmVo): List<DiskAttachmentVo> {
		conn.findVm(vmVo.id).getOrNull()?: throw ErrorPattern.VM_NOT_FOUND.toError()
		val res: List<DiskAttachment> =
			conn.addMultipleDiskAttachmentsToVm(vmVo.id, vmVo.diskAttachmentVos.toDiskAttachmentList())
				.getOrDefault(listOf())
		return res.toDiskAttachmentVos(conn)
	}

	override fun addDiskFromVm(vmVo: VmVo): DiskAttachmentVo {
		// disk 자체가 들어갈 수 도 있고, id가 있으면 아이디가 들어가야하는거지
		val res: DiskAttachment =
			conn.addDiskAttachmentToVm(vmVo.id, vmVo.diskAttachmentVo.toAddDisk())
				.getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()
		return res.toDiskAttachmentVo(conn)
	}

	override fun updateDiskFromVm(vmVo: VmVo): DiskAttachmentVo? {
		conn.findVm(vmVo.id).getOrNull()?: throw ErrorPattern.VM_NOT_FOUND.toError()

		// add 와 비슷한 방법을 쓸듯요
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