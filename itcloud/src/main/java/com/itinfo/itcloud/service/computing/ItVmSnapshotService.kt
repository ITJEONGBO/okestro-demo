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
	 * 목록
	 *
	 * 스냅샷 목록
	 * @param vmId [String] 가상머신 id
	 * @return
	 */
	@Throws(Error::class)
	fun findAllSnapshotsFromVm(vmId: String): List<SnapshotVo>
	/**
	 * [ItVmSnapshotService.findAllSnapshotDisksFromVm]
	 * 스냅샷 생성창
	 *
	 * 가상머신 스냅샷 생성 창
	 * 
	 * @param vmId [String] 가상머신 id
	 * 
	 * @return 스냅샷 목록
	 */
	@Throws(Error::class)
	fun findAllSnapshotDisksFromVm(vmId: String): List<DiskImageVo>
	/**
	 * [ItVmSnapshotService.add]
	 * 스냅샷 생성
	 * 스냅샷 생성 중에는 다른기능(삭제, 커밋)같은 기능 구현 x
	 * 
	 * @param snapshotVo
	 * 
	 * @return
	 */
	fun add(vmId: String, snapshotVo: SnapshotVo): SnapshotVo?
	/**
	 * [ItVmSnapshotService.remove]
	 * 스냅샷 삭제
	 * 
	 * @param vmId [String] 가상머신 id
	 * @param snapshotId 스냅샷 id
	 * 
	 * @return
	 */
	@Throws(Error::class)
	fun remove(vmId: String, snapshotId: String): Boolean

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

	@Throws(Error::class)
	override fun findAllSnapshotDisksFromVm(vmId: String): List<DiskImageVo> {
		log.info("findAllSnapshotDisksFromVm ... vmId: {}", vmId)
		val res: List<DiskAttachment> =
			conn.findAllDiskAttachmentsFromVm(vmId)
				.getOrDefault(listOf())
//		return res.toDiskAttachmentVos(conn, vmId)
		TODO("///")
	}

	@Throws(Error::class)
	override fun add(vmId: String, snapshotVo: SnapshotVo): SnapshotVo? {
		log.info("add ... ")
		val diskAttachments: List<DiskAttachment> =
			snapshotVo.snapshotDiskVos.toDiskAttachments(conn, vmId)
		val snapshot2Add: Snapshot = SnapshotBuilder()
			.description(snapshotVo.description)
			.persistMemorystate(snapshotVo.persistMemory)
			.diskAttachments(diskAttachments)
			.build()
		val res: Snapshot? =
			conn.addSnapshotFromVm(vmId, snapshot2Add)
				.getOrNull()
		return res?.toSnapshotVo(conn, vmId)
	}

	@Throws(Error::class)
	override fun remove(vmId: String, snapshotId: String): Boolean {
		log.info("remove ... id: {}, snapId: {}", vmId, snapshotId)
		val res: Result<Boolean> =
			conn.removeSnapshotFromVm(vmId, snapshotId)
		return res.isSuccess
	}

// region: 안쓸듯
/*
	override fun editDiskImage(id: String, image: VDiskImageVo): CommonVo<Boolean> { return null; }
	override fun addDiskLun(id: String, lun: VDiskLunVo): CommonVo<Boolean> { return null; }
	override fun editDiskLun(String id, lun: VDiskLunVo): CommonVo<Boolean> { return null; }
*/
// endregion

	companion object {
		private val log by LoggerDelegate()
	}
}