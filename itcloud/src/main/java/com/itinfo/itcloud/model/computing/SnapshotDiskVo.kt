package com.itinfo.itcloud.model.computing

import com.itinfo.itcloud.model.gson
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.error.toError
import com.itinfo.util.ovirt.findDisk
import com.itinfo.util.ovirt.findDiskAttachmentFromVm
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.builders.DiskAttachmentBuilder
import org.ovirt.engine.sdk4.builders.DiskBuilder
import org.ovirt.engine.sdk4.types.Disk
import org.ovirt.engine.sdk4.types.DiskAttachment
import org.ovirt.engine.sdk4.types.DiskStatus
import org.ovirt.engine.sdk4.types.DiskStorageType
import org.slf4j.LoggerFactory
import java.io.Serializable
import java.math.BigInteger

private val log = LoggerFactory.getLogger(SnapshotDiskVo::class.java)

/**
 * [SnapshotDiskVo]
 *
 * @property id [String]
 * @property daId [String]   disk_attachments Id
 * @property alias [String]  disk 별칭
 * @property status [DiskStatus] 상태
 * @property virtualSize [BigInteger] 가상크기
 * @property actualSize [BigInteger]  실제크기
 * @property sparse [String]  할당정책  씬true, 사전할당false
 * @property diskInterface [String] 인터페이스
 * @property date [String] 생성일자
 * @property diskSnapId [String]  디스크 스냅샷 ID
 * @property storageType [DiskStorageType] 유형
 * @property description [String]
 */
// TODO 삭제해야함
class SnapshotDiskVo (
    val id: String = "",
    val daId: String = "",
    val alias: String = "",
    val status: DiskStatus = DiskStatus.OK, // 애매함
    val virtualSize: BigInteger = BigInteger.ZERO,
    val actualSize: BigInteger = BigInteger.ZERO,
    val sparse: String = "",
    val diskInterface: String = "",
    val date: String = "",
    val diskSnapId: String = "",
    val storageType: DiskStorageType = DiskStorageType.IMAGE,
    val description: String = "",

): Serializable {
    override fun toString(): String = gson.toJson(this)

    class Builder {
        private var bId: String = ""; fun id(block: () -> String?) { bId = block() ?: ""}
        private var bDaId: String = ""; fun daId(block: () -> String?) { bDaId = block() ?: ""}
        private var bAlias: String = ""; fun alias(block: () -> String?) { bAlias = block() ?: ""}
        private var bStatus: DiskStatus = DiskStatus.OK; fun status(block: () -> DiskStatus?) { bStatus = block() ?: DiskStatus.OK }
        private var bVirtualSize: BigInteger = BigInteger.ZERO; fun virtualSize(block: () -> BigInteger?) { bVirtualSize = block() ?: BigInteger.ZERO}
        private var bActualSize: BigInteger = BigInteger.ZERO; fun actualSize(block: () -> BigInteger?) { bActualSize = block() ?: BigInteger.ZERO}
        private var bSparse: String = ""; fun sparse(block: () -> String?) { bSparse = block() ?: ""}
        private var bDiskInterface: String = ""; fun diskInterface(block: () -> String?) { bDiskInterface = block() ?: ""}
        private var bDate: String = ""; fun date(block: () -> String?) { bDate = block() ?: ""}
        private var bDiskSnapId: String = ""; fun diskSnapId(block: () -> String?) { bDiskSnapId = block() ?: ""}
        private var bStorageType: DiskStorageType = DiskStorageType.IMAGE; fun storageType(block: () -> DiskStorageType?) { bStorageType = block() ?: DiskStorageType.IMAGE}
        private var bDescription: String = ""; fun description(block: () -> String?) { bDescription = block() ?: ""}

        fun build(): SnapshotDiskVo = SnapshotDiskVo( bId, bDaId, bAlias, bStatus, bVirtualSize, bActualSize, bSparse, bDiskInterface, bDate, bDiskSnapId, bStorageType, bDescription)
    }

    companion object {
        inline fun builder(block: SnapshotDiskVo.Builder.() -> Unit): SnapshotDiskVo = SnapshotDiskVo.Builder().apply(block).build()
    }

    @Throws(Error::class)
    fun toDiskAttachment(conn: Connection, vmId: String): DiskAttachment {
        val diskAttachment: DiskAttachment =
            conn.findDiskAttachmentFromVm(vmId, daId).getOrNull() ?: throw ErrorPattern.DISK_ATTACHMENT_NOT_FOUND.toError()
        return DiskAttachmentBuilder()
            .disk(DiskBuilder().id(diskAttachment.disk().id()).build())
            .build()
    }
}

fun List<SnapshotDiskVo>.toDiskAttachments(conn: Connection, vmId: String): List<DiskAttachment> =
    this@toDiskAttachments.map { it.toDiskAttachment(conn, vmId) }

fun DiskAttachment.toSnapshotDiskVo(conn: Connection, vmId: String): SnapshotDiskVo {
    val diskId: String = this@toSnapshotDiskVo.disk().id()
    val disk: Disk = conn.findDisk(diskId).getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()
    return disk.toSnapshotDiskVo(conn, vmId)
}


fun List<DiskAttachment>.toSnapshotDiskVos(conn: Connection, vmId: String): List<SnapshotDiskVo> =
    this@toSnapshotDiskVos.map { it.toSnapshotDiskVo(conn, vmId) }

fun Disk.toSnapshotDiskVo(conn: Connection, vmId: String, isActive: Boolean = false): SnapshotDiskVo {
    val diskAttachment: DiskAttachment =
        conn.findDiskAttachmentFromVm(vmId, this@toSnapshotDiskVo.id()).getOrNull() ?: throw ErrorPattern.DISK_ATTACHMENT_NOT_FOUND.toError()
    return SnapshotDiskVo.builder {
        status { this@toSnapshotDiskVo.status() }
        alias { this@toSnapshotDiskVo.alias() }
        virtualSize { this@toSnapshotDiskVo.provisionedSize() }
        actualSize { this@toSnapshotDiskVo.actualSize() }
        sparse { if (this@toSnapshotDiskVo.sparse()) "sparse" else "?" }
        diskInterface { if (this@toSnapshotDiskVo.vmPresent()) diskAttachment.interface_()?.value() else "해당없음" }
		// date {  /* 생성일자 */ }
        diskSnapId { this@toSnapshotDiskVo.imageId() } // 디스크 스냅샷 아이디
        description { this@toSnapshotDiskVo.description() }
        storageType { this@toSnapshotDiskVo.storageType() }
    }
}
fun List<Disk>.toSnapshotDiskVosd(conn: Connection, vmId: String): List<SnapshotDiskVo> =
    this@toSnapshotDiskVosd.map { it.toSnapshotDiskVo(conn, vmId) }