package com.itinfo.itcloud.model.storage

import com.itinfo.itcloud.model.computing.VmVo
import com.itinfo.itcloud.model.computing.toVmVo
import com.itinfo.itcloud.model.gson
import com.itinfo.util.ovirt.findDisk
import com.itinfo.util.ovirt.findVm
import org.slf4j.LoggerFactory
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.builders.DiskAttachmentBuilder
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
 *
 * @property diskImageVo [DiskImageVo] 디스크 이미지 생성
 * @property vmVo [VmVo] 가상머신
 */
class DiskAttachmentVo(
	val id: String = "",
	val active: Boolean = false,
	val bootable: Boolean = false,
	val readOnly: Boolean = false,
	val passDiscard: Boolean = false,
	val interface_: DiskInterface = DiskInterface.VIRTIO,
	val logicalName: String = "",
	val diskImageVo: DiskImageVo = DiskImageVo(),
	val vmVo: VmVo = VmVo()
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
		private var bDiskImageVo: DiskImageVo = DiskImageVo();fun diskImageVo(block: () -> DiskImageVo?) { bDiskImageVo = block() ?: DiskImageVo() }
		private var bVmVo: VmVo = VmVo();fun vmVo(block: () -> VmVo?) { bVmVo = block() ?: VmVo() }
		fun build(): DiskAttachmentVo = DiskAttachmentVo(bId, bActive, bBootable, bReadOnly, bPassDiscard, bInterface_, bLogicalName, bDiskImageVo, bVmVo)
	}
	
	companion object {
		inline fun builder(block: DiskAttachmentVo.Builder.() -> Unit): DiskAttachmentVo = DiskAttachmentVo.Builder().apply(block).build()
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
//		passDiscard { this@toDiskAttachmentVo.passDiscard() }
		interface_ { this@toDiskAttachmentVo.interface_() }
		logicalName { this@toDiskAttachmentVo.logicalName() }
		diskImageVo { disk?.toDiskImageVo(conn) }
		vmVo { vm?.toVmVo(conn) }
//		vmVo { this@toDiskAttachmentVo.vm().toVmVo(conn) }
	}
}

fun List<DiskAttachment>.toDiskAttachmentVos(conn: Connection): List<DiskAttachmentVo> =
	this@toDiskAttachmentVos.map { it.toDiskAttachmentVo(conn) }



fun DiskAttachmentVo.toDiskAttachmentBuilder(conn: Connection, disk: Disk): DiskAttachment {
	val diskAdd: Disk? =
		conn.findDisk(disk.id()).getOrNull()

	return DiskAttachmentBuilder()
		.active(this@toDiskAttachmentBuilder.active)
		.bootable(this@toDiskAttachmentBuilder.bootable)
//		.passDiscard(this@toDiskAttachmentBuilder.passDiscard)
		.readOnly(this@toDiskAttachmentBuilder.readOnly)
		.interface_(this@toDiskAttachmentBuilder.interface_)
		.logicalName(this@toDiskAttachmentBuilder.logicalName)
		.disk(diskAdd)
		.build()
}
