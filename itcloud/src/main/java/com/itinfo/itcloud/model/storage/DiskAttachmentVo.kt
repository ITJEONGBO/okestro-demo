package com.itinfo.itcloud.model.storage

import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.VmVo
import com.itinfo.itcloud.model.computing.toVmVo
import com.itinfo.itcloud.model.fromVmToIdentifiedVo
import com.itinfo.itcloud.gson
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.error.toError
import org.slf4j.LoggerFactory
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.builders.DiskAttachmentBuilder
import org.ovirt.engine.sdk4.builders.DiskBuilder
import org.ovirt.engine.sdk4.types.Disk
import org.ovirt.engine.sdk4.types.DiskAttachment
import org.ovirt.engine.sdk4.types.DiskInterface
import org.ovirt.engine.sdk4.types.Vm
import java.io.Serializable

private val log = LoggerFactory.getLogger(DiskAttachmentVo::class.java)

/**
 * [DiskAttachmentVo]
 * Vm, StorageDomainVmDiskAttachment, TemplateDiskAttachment
 * 가상머신에서만 사용
 * 
 * @property active [Boolean] 활성 여부
 * @property bootable [Boolean] 부팅가능 (가상머신에서 부팅가능한 디스크는 한개만 지정가능)
 * @property readOnly [Boolean] 읽기전용
 * @property passDiscard [Boolean]
 * @property interface_ [DiskInterface]  인터페이스
 * @property logicalName [String]  논리적 이름 (보통 없음)
 * @property detachOnly [Boolean] 완전삭제 여부 (기본 false=분리, true=완전삭제)
 *
 * @property diskImageVo [DiskImageVo] 디스크 이미지 생성
 * @property IdentifiedVo [IdentifiedVo] 가상머신
 */
class DiskAttachmentVo(
	val id: String = "",
	val active: Boolean = false,
	val bootable: Boolean = false,
	val readOnly: Boolean = false,
	val passDiscard: Boolean = false,
	val interface_: DiskInterface = DiskInterface.VIRTIO,
	val logicalName: String = "",
	val detachOnly: Boolean = false,
	val diskImageVo: DiskImageVo = DiskImageVo(),
	val vmVo: IdentifiedVo = IdentifiedVo()
): Serializable {
	override fun toString(): String =
		gson.toJson(this)

	class Builder {
		private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
		private var bActive: Boolean = false;fun active(block: () -> Boolean?) { bActive = block() ?: false }
		private var bBootable: Boolean = false;fun bootable(block: () -> Boolean?) { bBootable = block() ?: false }
		private var bReadOnly: Boolean = false;fun readOnly(block: () -> Boolean?) { bReadOnly = block() ?: false }
		private var bPassDiscard: Boolean = false;fun passDiscard(block: () -> Boolean?) { bPassDiscard = block() ?: false }
		private var bInterface_: DiskInterface = DiskInterface.VIRTIO;fun interface_(block: () -> DiskInterface?) { bInterface_ = (block() ?: DiskInterface.VIRTIO) }
		private var bLogicalName: String = "";fun logicalName(block: () -> String?) { bLogicalName = block() ?: "" }
		private var bDetachOnly: Boolean = false;fun detachOnly(block: () -> Boolean?) { bDetachOnly = block() ?: false }
		private var bDiskImageVo: DiskImageVo = DiskImageVo();fun diskImageVo(block: () -> DiskImageVo?) { bDiskImageVo = block() ?: DiskImageVo() }
		private var bVmVo: IdentifiedVo = IdentifiedVo();fun vmVo(block: () -> IdentifiedVo?) { bVmVo = block() ?: IdentifiedVo() }
		fun build(): DiskAttachmentVo = DiskAttachmentVo(bId, bActive, bBootable, bReadOnly, bPassDiscard, bInterface_, bLogicalName, bDetachOnly, bDiskImageVo, bVmVo)
	}
	
	companion object {
		inline fun builder(block: DiskAttachmentVo.Builder.() -> Unit): DiskAttachmentVo = DiskAttachmentVo.Builder().apply(block).build()
	}
}

fun DiskAttachment.toDiskAttachMenu(conn: Connection): DiskAttachmentVo {
	val disk: Disk? =
		conn.findDisk(this@toDiskAttachMenu.disk().id())
			.getOrNull()
	return DiskAttachmentVo.builder { 
		id { this@toDiskAttachMenu.id() }
		diskImageVo { disk?.toDiskIdName() }
	}
}


fun DiskAttachment.toDiskAttachmentVo(conn: Connection): DiskAttachmentVo {
	val disk: Disk? =
		conn.findDisk(this@toDiskAttachmentVo.disk().id())
			.getOrNull()
	val vm: Vm? =
		conn.findVm(this@toDiskAttachmentVo.vm().id())
			.getOrNull()
	return DiskAttachmentVo.builder {
		id { this@toDiskAttachmentVo.id() }
		active { this@toDiskAttachmentVo.active() }
		bootable { this@toDiskAttachmentVo.bootable() }
		readOnly { this@toDiskAttachmentVo.readOnly() }
		passDiscard { this@toDiskAttachmentVo.passDiscard() }
		interface_ { this@toDiskAttachmentVo.interface_() }
		logicalName { this@toDiskAttachmentVo.logicalName() }
		diskImageVo { disk?.toDiskImageVo(conn) }
		vmVo { vm?.fromVmToIdentifiedVo() }
	}
}
fun List<DiskAttachment>.toDiskAttachmentVos(conn: Connection): List<DiskAttachmentVo> =
	this@toDiskAttachmentVos.map { it.toDiskAttachmentVo(conn) }



/**
 * DiskAttachmentBuilder
 */
fun DiskAttachmentVo.toDiskAttachment(): DiskAttachmentBuilder =
	DiskAttachmentBuilder()
		.active(this@toDiskAttachment.active)
		.bootable(this@toDiskAttachment.bootable)
		.passDiscard(this@toDiskAttachment.passDiscard)
		.readOnly(this@toDiskAttachment.readOnly)
		.interface_(this@toDiskAttachment.interface_)
		.logicalName(this@toDiskAttachment.logicalName)

/**
 * DiskAttachmentBuilder 에서 디스크를 생성해서 붙이는 방식
 */
fun DiskAttachmentVo.toAddDiskAttachment(): DiskAttachment =
	this@toAddDiskAttachment.toDiskAttachment()
		.disk(this@toAddDiskAttachment.diskImageVo.toAddDiskBuilder())
		.build()

/**
 * DiskAttachmentBuilder 에서 생성된 디스크를 연결해서 붙이는 방식
 */
fun DiskAttachmentVo.toAttachDisk(): DiskAttachment =
	this@toAttachDisk.toDiskAttachment()
		.disk(DiskBuilder().id(this@toAttachDisk.diskImageVo.id))
		.build()


/**
 * DiskAttachmentBuilder 에서 디스크 편집
 */
fun DiskAttachmentVo.toEditDiskAttachment(): DiskAttachment =
	this@toEditDiskAttachment.toDiskAttachment()
		.disk(this@toEditDiskAttachment.diskImageVo.toEditDiskBuilder())
		.build()

/**
 * 생성과 연결될 DiskAttachment 를 목록으로 내보낸다
 */
fun List<DiskAttachmentVo>.toAddDiskAttachmentList(): List<DiskAttachment> {
	val diskAttachmentList = mutableListOf<DiskAttachment>()
	this@toAddDiskAttachmentList.forEach { diskAttachmentVo ->
		if (diskAttachmentVo.diskImageVo.id.isEmpty()) {
			diskAttachmentList.add(diskAttachmentVo.toAddDiskAttachment())
		} else {
			diskAttachmentList.add(diskAttachmentVo.toAttachDisk())
		}
	}
	return diskAttachmentList
}

/**
 * 편집과 연결된 DiskAttachment 를 목록으로 내보낸다
 */
fun List<DiskAttachmentVo>.toEditDiskAttachmentList(conn: Connection, vmId: String): List<DiskAttachment> {
	val diskAttachmentList = mutableListOf<DiskAttachment>()
	val existDiskAttachments: List<DiskAttachment> =
		conn.findAllDiskAttachmentsFromVm(vmId).getOrDefault(listOf())

	val diskAttachmentListToAdd = mutableListOf<DiskAttachment>()
	val diskAttachmentListToDelete = mutableListOf<DiskAttachment>()

	// 1. 추가할 디스크 찾기 (새로운 목록에 있지만 기존 목록에 없는 디스크)
	this@toEditDiskAttachmentList.forEach { newDisk ->
		if (existDiskAttachments.none { it.id() == newDisk.id }) {
			diskAttachmentListToAdd.add(newDisk.toAddDiskAttachment())
		}
	}

	// 2. 삭제할 디스크 찾기 (기존 목록에 있지만 새로운 목록에 없는 디스크)
	existDiskAttachments.forEach { existingDisk ->
		if (this@toEditDiskAttachmentList.none { it.id == existingDisk.id() }) {
			// 기존 디스크가 새 목록에 없으면 삭제할 목록에 넣음
			diskAttachmentListToDelete.add(existingDisk)
		}
	}

	// 기존 로직 - DiskAttachmentVo 추가
	this@toEditDiskAttachmentList.forEach { diskAttachmentVo ->
		if (diskAttachmentVo.diskImageVo.id.isEmpty()) {
			diskAttachmentList.add(diskAttachmentVo.toAddDiskAttachment())
		} else {
			diskAttachmentList.add(diskAttachmentVo.toAttachDisk())
		}
	}

	return diskAttachmentList
}

