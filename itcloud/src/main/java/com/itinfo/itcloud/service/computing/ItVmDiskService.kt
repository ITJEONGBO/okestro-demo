package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.response.Res
import com.itinfo.itcloud.model.storage.DiskAttachmentVo
import com.itinfo.itcloud.model.storage.DiskImageVo
import com.itinfo.itcloud.model.storage.toDiskAttachmentVos
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.findAllDiskAttachmentsFromVm
import org.ovirt.engine.sdk4.types.DiskAttachment
import org.springframework.stereotype.Service

interface ItVmDiskService {
	fun findAllDisksFromVm(vmId: String): List<DiskAttachmentVo>
	fun deleteDisk(diskAttachmentId: String, type: Boolean): Res<Boolean> // 디스크 삭제
	fun activeDisk(diskAttachmentId: String): Res<Boolean> // 디스크 활성화
	fun deactivateDisk(diskAttachmentId: String): Res<Boolean> // 디스크 비활성화
	fun setDiskMove(diskAttachmentId: String): DiskImageVo // 디스크 이동창
	fun moveDisk(diskAttachmentId: String, diskVo: DiskImageVo): Res<Boolean> // 디스크 스토리지 이동
}

@Service
class VmDiskService(

): BaseService(), ItVmDiskService {
	override fun findAllDisksFromVm(vmId: String): List<DiskAttachmentVo> {
		log.debug("findAllDisksFromVm ... vmId: {}", vmId)
		val diskAtts: List<DiskAttachment> =
			conn.findAllDiskAttachmentsFromVm(vmId)
				.getOrDefault(listOf())
		return diskAtts.toDiskAttachmentVos(conn)
	}

	override fun deleteDisk(diskAttachmentId: String, type: Boolean): Res<Boolean> {
		TODO("Not yet implemented")
	}

	override fun activeDisk(diskAttachmentId: String): Res<Boolean> {
		TODO("Not yet implemented")
	}

	override fun deactivateDisk(diskAttachmentId: String): Res<Boolean> {
		TODO("Not yet implemented")
	}

	override fun setDiskMove(diskAttachmentId: String): DiskImageVo {
		TODO("Not yet implemented")
	}

	override fun moveDisk(diskAttachmentId: String, diskVo: DiskImageVo): Res<Boolean> {
		TODO("Not yet implemented")
	}

	companion object {
		private val log by LoggerDelegate()
	}
}