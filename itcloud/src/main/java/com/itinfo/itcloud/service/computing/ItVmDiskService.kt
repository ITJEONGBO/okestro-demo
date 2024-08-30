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
		log.debug("deleteDisk ... diskAttachmentId: {}", diskAttachmentId)
		// TODO:
/*

		DiskAttachmentService daService = getSystem().vmsService().vmService(id).diskAttachmentsService().attachmentService(daId)
		daService.remove().send();

		DiskAttachment da = getSystem().vmsService().vmService(id).diskAttachmentsService().attachmentService(daId).get().send().attachment();
		Vm vm = getSystem().vmsService().vmService(id).get().send().vm();

		try{
			// 가상머신이 연결되어잇는지, down 상태인지
			if(vm.status() == VmStatus.DOWN) {
				if(type) {   // 완전삭제
					DiskService diskService = getSystem().disksService().diskService(da.disk().id());
					Disk disk = getSystem().disksService().diskService(da.disk().id()).get().send().disk();
					diskService.remove().send();

					do {
						log.info("디스크 완전 삭제");
					} while (!disk.idPresent());

					log.info("성공: 디스크 삭제");
					return Res.successResponse();
				}else {
					DiskAttachmentService daService = getSystem().vmsService().vmService(id).diskAttachmentsService().attachmentService(daId);
					daService.remove().send();

					do {
						log.info("디스크 삭제");
					} while (da.disk().id().isEmpty());

					log.info("디스크 삭제");
					return Res.successResponse();
				}
			}else{
				log.error("실패: 가상머신이 Down이 아님");
				return Res.failResponse("가상머신이 Down이 아님");
			}
		}catch (Exception e){
			log.error("실패: 새 가상 디스크 (이미지) 수정");
			e.printStackTrace();
			return Res.failResponse(e.getMessage());
		}
*/
		TODO("Not yet implemented")
	}

	override fun activeDisk(diskAttachmentId: String): Res<Boolean> {
		log.debug("activeDisk ... diskAttachmentId: {}", diskAttachmentId)
		TODO("Not yet implemented")
	}

	override fun deactivateDisk(diskAttachmentId: String): Res<Boolean> {
		log.debug("deactivateDisk ... diskAttachmentId: {}", diskAttachmentId)
		TODO("Not yet implemented")
	}

	override fun setDiskMove(diskAttachmentId: String): DiskImageVo {
		log.debug("setDiskMove ... diskAttachmentId: {}", diskAttachmentId)
		TODO("Not yet implemented")
	}

	override fun moveDisk(diskAttachmentId: String, diskVo: DiskImageVo): Res<Boolean> {
		log.debug("moveDisk ... diskAttachmentId: {}", diskAttachmentId)
		TODO("Not yet implemented")
	}

	companion object {
		private val log by LoggerDelegate()
	}
}