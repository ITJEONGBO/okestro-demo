package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.storage.DiskImageVo
import com.itinfo.itcloud.model.storage.toDiskAttachmentVos
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.addSnapshotFromVm
import com.itinfo.util.ovirt.findAllDiskAttachmentsFromVm
import com.itinfo.util.ovirt.findAllSnapshotsFromVm
import com.itinfo.util.ovirt.removeSnapshotFromVm
import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.builders.SnapshotBuilder
import org.ovirt.engine.sdk4.services.SystemService
import org.ovirt.engine.sdk4.services.VmService
import org.ovirt.engine.sdk4.types.*
import org.springframework.stereotype.Service

interface ItVmSnapshotService {
	/**
	 * [ItVmSnapshotService.findAllSnapshotsFromVm]
	 * 스냅샷 목록
	 *
	 * @param vmId [String] 가상머신 id
	 * @return
	 */
	@Throws(Error::class)
	fun findAllSnapshotsFromVm(vmId: String): List<SnapshotVo>
	/**
	 * [ItVmSnapshotService.findSnapshotFromVm]
	 * 스냅샷 목록
	 *
	 * @param vmId [String] 가상머신 id
	 * @param snapshotId [String]
	 * @return SnapshotVo
	 */
	@Throws(Error::class)
	fun findSnapshotFromVm(vmId: String, snapshotId: String): SnapshotVo

	/**
	 * [ItVmSnapshotService.findAllDisksFromVm]
	 * 스냅샷 생성창
	 * [ItVmDiskService.findAllDisksFromVm] 대체가능
	 * 가상머신 스냅샷 생성 창
	 * 
	 * @param vmId [String] 가상머신 id
	 * @return 스냅샷 목록
	 */
//	@Throws(Error::class)
//	fun findAllDisksFromVm(vmId: String): List<DiskImageVo>
	/**
	 * [ItVmSnapshotService.addSnapshot]
	 * 스냅샷 생성
	 * 스냅샷 생성 중에는 다른기능(삭제, 커밋)같은 기능 구현 x
	 * 
	 * @param snapshotVo
	 * @return SnapshotVo
	 */
	fun addSnapshot(vmId: String, snapshotVo: SnapshotVo): SnapshotVo?
	/**
	 * [ItVmSnapshotService.removeSnapshot]
	 * 스냅샷 삭제
	 * 
	 * @param vmId [String] 가상머신 id
	 * @param snapshotId 스냅샷 id
	 * 
	 * @return
	 */
	@Throws(Error::class)
	fun removeSnapshot(vmId: String, snapshotId: String): Boolean

	interface IOperation {
		fun activeSnapshotDisk(system: SystemService, vmService: VmService, snapshot: Snapshot): List<SnapshotDiskVo>
	}
}

@Service
class VmSnapshotServiceImpl(

): BaseService(), ItVmSnapshotService {

	@Throws(Error::class)
	override fun findAllSnapshotsFromVm(vmId: String): List<SnapshotVo> {
		log.info("findAllSnapshotsFromVm ... ")
		val res: List<Snapshot> =
			conn.findAllSnapshotsFromVm(vmId)
				.getOrDefault(listOf())
		return res.toSnapshotVos(conn, vmId)
	}

	override fun findSnapshotFromVm(vmId: String, snapshotId: String): SnapshotVo {
		TODO("Not yet implemented")
	}

//	@Throws(Error::class)
//	override fun findAllDisksFromVm(vmId: String): List<DiskImageVo> {
//		log.info("findAllSnapshotDisksFromVm ... vmId: {}", vmId)
//		val res: List<DiskAttachment> =
//			conn.findAllDiskAttachmentsFromVm(vmId)
//				.getOrDefault(listOf())
//		return res.toDiskAttachmentVos(conn, vmId)
//	}

	@Throws(Error::class)
	override fun addSnapshot(vmId: String, snapshotVo: SnapshotVo): SnapshotVo? {
		log.info("addSnapshot ... ")
		// TODO
//		val disk: List<Disk> =
//			snapshotVo.snapshotDiskVos


		val snapshot2Add: Snapshot = SnapshotBuilder()
			.description(snapshotVo.description)
			.persistMemorystate(snapshotVo.persistMemory)
//			.diskAttachments(diskAttachments)
			.build()
		val res: Snapshot? =
			conn.addSnapshotFromVm(vmId, snapshot2Add)
				.getOrNull()
		return res?.toSnapshotVo(conn, vmId)
	}

	@Throws(Error::class)
	override fun removeSnapshot(vmId: String, snapshotId: String): Boolean {
		log.info("removeSnapshot ... id: {}, snapId: {}", vmId, snapshotId)
		val res: Result<Boolean> =
			conn.removeSnapshotFromVm(vmId, snapshotId)
		return res.isSuccess
	}


	companion object {
		private val log by LoggerDelegate()
	}
}