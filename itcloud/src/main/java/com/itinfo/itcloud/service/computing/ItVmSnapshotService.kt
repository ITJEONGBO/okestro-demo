package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.addSnapshotFromVm
import com.itinfo.util.ovirt.findAllSnapshotsFromVm
import com.itinfo.util.ovirt.findSnapshotFromVm
import com.itinfo.util.ovirt.removeSnapshotFromVm
import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.builders.SnapshotBuilder
import org.ovirt.engine.sdk4.services.SystemService
import org.ovirt.engine.sdk4.services.VmService
import org.ovirt.engine.sdk4.types.*
import org.springframework.stereotype.Service

interface ItVmSnapshotService {
	/**
	 * [ItVmSnapshotService.findAllFromVm]
	 * 스냅샷 목록
	 *
	 * @param vmId [String] 가상머신 Id
	 * @return List<[SnapshotVo]>
	 */
	@Throws(Error::class)
	fun findAllFromVm(vmId: String): List<SnapshotVo>
	/**
	 * [ItVmSnapshotService.findOneFromVm]
	 * 스냅샷 상세정보
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param snapshotId [String] 스냅샷 Id
	 * @return [SnapshotVo] ?
	 */
	@Throws(Error::class)
	fun findOneFromVm(vmId: String, snapshotId: String): SnapshotVo?

	// 가상머신 스냅샷 생성 창 - [ItVmDiskService.findAllDisksFromVm]

	/**
	 * [ItVmSnapshotService.addFromVm]
	 * 스냅샷 생성 (생성 중에는 다른기능(삭제, 커밋)같은 기능 구현 x)
	 * 
	 * @param vmId
	 * @param snapshotVo
	 * @return [SnapshotVo]
	 */
	@Throws(Error::class)
	fun addFromVm(vmId: String, snapshotVo: SnapshotVo): SnapshotVo?
	/**
	 * [ItVmSnapshotService.removeFromVm]
	 * 스냅샷 삭제
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param snapshotIds List<[String]> 스냅샷 Id 목록
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun removeFromVm(vmId: String, snapshotIds: List<String>): Boolean
	/**
	 * [ItVmSnapshotService.copyFromVm]
	 * 스냅샷 복제
	 * 복제하면 nic, disks는 스냅샷에 있는 값을 가져와서 복제
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param snapshotId [String]
	 * @return [SnapshotVo]
	 */
	@Throws(Error::class)
	fun copyFromVm(vmId: String, snapshotId: String): SnapshotVo?
}

@Service
class VmSnapshotServiceImpl(

): BaseService(), ItVmSnapshotService {

	@Throws(Error::class)
	override fun findAllFromVm(vmId: String): List<SnapshotVo> {
		log.info("findAllFromVm ... ")
		val res: List<Snapshot> =
			conn.findAllSnapshotsFromVm(vmId).getOrDefault(listOf())
		return res.toSnapshotVos(conn, vmId)
	}

	@Throws(Error::class)
	override fun findOneFromVm(vmId: String, snapshotId: String): SnapshotVo? {
		log.info("findOneFromVm ... vmId: {}, snapshotId: {}", vmId, snapshotId)
		val res: Snapshot? =
			conn.findSnapshotFromVm(vmId, snapshotId).getOrNull()
		return res?.toSnapshotVo(conn, vmId)
	}

	@Throws(Error::class)
	override fun addFromVm(vmId: String, snapshotVo: SnapshotVo): SnapshotVo? {
		log.info("addFromVm ... ")
		// TODO
//		val disk: List<Disk> =
//			snapshotVo.snapshotDiskVos


		val snapshot2Add: Snapshot = SnapshotBuilder()
			.description(snapshotVo.description)
			.persistMemorystate(snapshotVo.persistMemory)
//			.diskAttachments(diskAttachments)
			.build()
		val res: Snapshot? =
			conn.addSnapshotFromVm(
				vmId, snapshot2Add
			).getOrNull()
		return res?.toSnapshotVo(conn, vmId)
	}

	@Throws(Error::class)
	override fun removeFromVm(vmId: String, snapshotIds: List<String>): Boolean {
		log.info("removeFromVm ... id: {}", vmId)
//		val res: Result<Boolean> =
//			conn.removeSnapshotFromVm(vmId, snapshotIds)
//		return res.isSuccess
		TODO()
	}

	@Throws(Error::class)
	override fun copyFromVm(vmId: String, snapshotId: String): SnapshotVo? {
		TODO("Not yet implemented")
	}


	companion object {
		private val log by LoggerDelegate()
	}
}