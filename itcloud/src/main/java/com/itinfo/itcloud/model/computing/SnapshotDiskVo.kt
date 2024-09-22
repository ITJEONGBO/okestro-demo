package com.itinfo.itcloud.model.computing

import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.gson
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.storage.StorageDomainVo
import com.itinfo.itcloud.model.storage.toDiskIdName
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.error.toError
import com.itinfo.util.ovirt.findDisk
import com.itinfo.util.ovirt.findDiskAttachmentFromVm
import com.itinfo.util.ovirt.findSnapshotFromVm
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.builders.DiskAttachmentBuilder
import org.ovirt.engine.sdk4.builders.DiskBuilder
import org.ovirt.engine.sdk4.types.*
import org.slf4j.LoggerFactory
import java.io.Serializable
import java.math.BigInteger

private val log = LoggerFactory.getLogger(SnapshotDiskVo::class.java)

/**
 * [SnapshotDiskVo]
 *
 * @property id [String]  disk id
 * @property name [String] disk 별칭
 * @property description [String]
 * @property alias [String] disk 별칭
 * @property backup [String]
 * @property contentType [DiskContentType]
 * @property format [DiskFormat]
 * @property imageId [String] disk snapshot id
 * @property propagateErrors [Boolean]
 * @property actualSize [BigInteger] 실제크기
 * @property provisionedSize [BigInteger] 가상크기
 * @property shareable [Boolean]
 * @property sparse [Boolean] 할당정책  씬true, 사전할당false
 * @property status [DiskStatus]
 * @property storageType [DiskStorageType]
 * @property wipeAfterDelete [Boolean]
 * @property snapshotVo [SnapshotVo]
 * @property storageDomainVo [IdentifiedVo]
 * DiskInterface
 */
class SnapshotDiskVo (
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val alias: String = "",
    val backup: DiskBackup = DiskBackup.NONE,
    val contentType: DiskContentType = DiskContentType.DATA,
    val format: DiskFormat = DiskFormat.RAW,
    val imageId: String = "",
    val propagateErrors: Boolean = false,
    val actualSize: BigInteger = BigInteger.ZERO,
    val provisionedSize: BigInteger = BigInteger.ZERO,
    val shareable: Boolean = false,
    val sparse: Boolean = false,
    val status: DiskStatus = DiskStatus.LOCKED,
    val storageType: DiskStorageType = DiskStorageType.IMAGE,
    val wipeAfterDelete: Boolean = false,
    val snapshotVo: SnapshotVo = SnapshotVo(),
    val storageDomainVo: IdentifiedVo = IdentifiedVo(),

    ): Serializable {
    override fun toString(): String = gson.toJson(this)

    class Builder {
        private var bId: String = ""; fun id(block: () -> String?) { bId = block() ?: "" }
        private var bName: String = ""; fun name(block: () -> String?) { bName = block() ?: "" }
        private var bDescription: String = ""; fun description(block: () -> String?) { bDescription = block() ?: "" }
        private var bAlias: String = ""; fun alias(block: () -> String?) { bAlias = block() ?: "" }
        private var bBackup: DiskBackup = DiskBackup.NONE; fun backup(block: () -> DiskBackup?) { bBackup = block() ?: DiskBackup.NONE }
        private var bContentType: DiskContentType = DiskContentType.DATA; fun contentType(block: () -> DiskContentType?) { bContentType = block() ?: DiskContentType.DATA }
        private var bFormat: DiskFormat = DiskFormat.RAW; fun format(block: () -> DiskFormat?) { bFormat = block() ?: DiskFormat.RAW }
        private var bImageId: String = ""; fun imageId(block: () -> String?) { bImageId = block() ?: "" }
        private var bPropagateErrors: Boolean = false; fun propagateErrors(block: () -> Boolean?) { bPropagateErrors = block() ?: false }
        private var bActualSize: BigInteger = BigInteger.ZERO; fun actualSize(block: () -> BigInteger?) { bActualSize = block() ?: BigInteger.ZERO }
        private var bProvisionedSize: BigInteger = BigInteger.ZERO; fun provisionedSize(block: () -> BigInteger?) { bProvisionedSize = block() ?: BigInteger.ZERO }
        private var bShareable: Boolean = false; fun shareable(block: () -> Boolean?) { bShareable = block() ?: false }
        private var bSparse: Boolean = false; fun sparse(block: () -> Boolean?) { bSparse = block() ?: false }
        private var bStatus: DiskStatus = DiskStatus.LOCKED; fun status(block: () -> DiskStatus?) { bStatus = block() ?: DiskStatus.LOCKED }
        private var bStorageType: DiskStorageType = DiskStorageType.IMAGE; fun storageType(block: () -> DiskStorageType?) { bStorageType = block() ?: DiskStorageType.IMAGE }
        private var bWipeAfterDelete: Boolean = false; fun wipeAfterDelete(block: () -> Boolean?) { bWipeAfterDelete = block() ?: false }
        private var bSnapshotVo: SnapshotVo =  SnapshotVo(); fun snapshotVo(block: () -> SnapshotVo?) { bSnapshotVo = block() ?: SnapshotVo() }
        private var bStorageDomainVo: IdentifiedVo = IdentifiedVo(); fun storageDomainVo(block: () -> IdentifiedVo?) { bStorageDomainVo = block() ?: IdentifiedVo() }

        fun build(): SnapshotDiskVo = SnapshotDiskVo(bId, bName, bDescription, bAlias, bBackup, bContentType, bFormat, bImageId, bPropagateErrors, bActualSize, bProvisionedSize, bShareable, bSparse, bStatus, bStorageType, bWipeAfterDelete, bSnapshotVo, bStorageDomainVo,)
    }

    companion object {
        inline fun builder(block: SnapshotDiskVo.Builder.() -> Unit): SnapshotDiskVo = SnapshotDiskVo.Builder().apply(block).build()
    }
}

fun Disk.toSnapshotDiskIdName(): SnapshotDiskVo = SnapshotDiskVo.builder {
    id { this@toSnapshotDiskIdName.id() }
    name { this@toSnapshotDiskIdName.name() }
}
fun List<Disk>.toSnapshotDisksIdName(): List<SnapshotDiskVo> =
    this@toSnapshotDisksIdName.map { it.toSnapshotDiskIdName() }


fun Disk.toSnapshotDiskVo(conn: Connection): SnapshotDiskVo {
    return SnapshotDiskVo.builder {
        name { this@toSnapshotDiskVo.name() }
        description { this@toSnapshotDiskVo.description() }
        alias { this@toSnapshotDiskVo.alias() }
        backup { this@toSnapshotDiskVo.backup() }
        contentType { this@toSnapshotDiskVo.contentType() }
        format { this@toSnapshotDiskVo.format() }
        imageId { this@toSnapshotDiskVo.imageId()}
        propagateErrors { this@toSnapshotDiskVo.propagateErrors() }
        actualSize { this@toSnapshotDiskVo.actualSize() }
        provisionedSize { this@toSnapshotDiskVo.provisionedSize() }
        shareable { this@toSnapshotDiskVo.shareable() }
        sparse { this@toSnapshotDiskVo.sparse() }
        status { this@toSnapshotDiskVo.status() }
        storageType { this@toSnapshotDiskVo.storageType() }
        wipeAfterDelete { this@toSnapshotDiskVo.wipeAfterDelete() }
    }
}
fun List<Disk>.toSnapshotDiskVos(conn: Connection): List<SnapshotDiskVo> =
    this@toSnapshotDiskVos.map { it.toSnapshotDiskVo(conn) }