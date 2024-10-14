package com.itinfo.itcloud.model.computing

import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.fromApplicationsToIdentifiedVos
import com.itinfo.itcloud.gson
import com.itinfo.itcloud.model.network.NicVo
import com.itinfo.itcloud.model.network.toNicVosFromSnapshot
import com.itinfo.itcloud.model.network.toNicVosFromVm
import com.itinfo.itcloud.model.storage.DiskImageVo
import com.itinfo.itcloud.ovirtDf
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.error.toError
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.builders.SnapshotBuilder
import org.ovirt.engine.sdk4.types.*
import org.slf4j.LoggerFactory
import java.io.Serializable
import java.math.BigInteger

private val log = LoggerFactory.getLogger(SnapshotVo::class.java)

/**
 * [SnapshotVo]
 *
 * @property id[String]
 * @property description [String]
 * @property date [String]
 * @property persistMemory [Boolean] 메모리 저장 여부 f/t
 * @property status [String]
 * @property vmVo [VmVo]
 * @property snapshotDiskVos List<[SnapshotDiskVo]>
 * @property nicVos List<[NicVo]>
 * @property applicationVos List<[IdentifiedVo]>
 *
 */
class SnapshotVo (
    val id: String = "",
    val vmVo: VmVo = VmVo(),
    val description: String = "",
    val date: String = "",
    val persistMemory: Boolean = false,
    val status: String = "",
    val snapshotDiskVos: List<SnapshotDiskVo> = listOf(),
    val nicVos: List<NicVo> = listOf(),
    val applicationVos: List<IdentifiedVo> = listOf(),
): Serializable {
    override fun toString(): String = gson.toJson(this)

    class Builder {
        private var bId: String = ""; fun id(block: () -> String?) { bId = block() ?: "" }
        private var bVmVo: VmVo = VmVo(); fun vmVo(block: () -> VmVo?) { bVmVo = block() ?: VmVo()  }
        private var bDescription: String = ""; fun description(block: () -> String?) { bDescription= block() ?: "" }
        private var bDate: String = ""; fun date(block: () -> String?) { bDate= block() ?: "" }
        private var bPersistMemory: Boolean = false; fun persistMemory(block: () -> Boolean?) { bPersistMemory= block() ?: false }
        private var bStatus: String = ""; fun status(block: () -> String?) { bStatus= block() ?: "" }
        private var bSnapshotDiskVos: List<SnapshotDiskVo> = listOf(); fun snapshotDiskVos(block: () -> List<SnapshotDiskVo>?) { bSnapshotDiskVos = block() ?: listOf() }
        private var bNicVos: List<NicVo> = listOf(); fun nicVos(block: () -> List<NicVo>?) { bNicVos = block() ?: listOf() }
        private var bApplicationVos: List<IdentifiedVo> = listOf(); fun applicationVos(block: () -> List<IdentifiedVo>?) { bApplicationVos = block() ?: listOf() }

        fun build(): SnapshotVo = SnapshotVo( bId, bVmVo, bDescription, bDate, bPersistMemory, bStatus, bSnapshotDiskVos, bNicVos, bApplicationVos)
    }

    companion object {
        inline fun builder(block: SnapshotVo.Builder.() -> Unit): SnapshotVo = SnapshotVo.Builder().apply(block).build()
    }
}

fun Snapshot.toSnapshotIdName(): SnapshotVo = SnapshotVo.builder {
    id { this@toSnapshotIdName.id() }
    description { this@toSnapshotIdName.description() }
}
fun List<Snapshot>.toSnapshotsIdName(): List<SnapshotVo> =
    this@toSnapshotsIdName.map { it.toSnapshotIdName() }


fun Snapshot.toSnapshotVo(conn: Connection, vmId: String): SnapshotVo {
    val vm: Vm =
        conn.findVm(vmId).getOrNull()
            ?: throw ErrorPattern.VM_NOT_FOUND.toError()

    val disks: List<Disk> =
        conn.findAllSnapshotDisksFromVm(vmId, this@toSnapshotVo.id()).getOrDefault(listOf())
    val nics: List<Nic> =
        conn.findAllSnapshotNicsFromVm(vmId, this@toSnapshotVo.id()).getOrDefault(listOf())
    val applications: List<Application> =
        conn.findAllApplicationsFromVm(vmId).getOrDefault(listOf())

    return SnapshotVo.builder {
        id { this@toSnapshotVo.id() }
        description { this@toSnapshotVo.description() }
        date { if (this@toSnapshotVo.vmPresent()) ovirtDf.format(this@toSnapshotVo.date().time) else "현재" }
        status { this@toSnapshotVo.snapshotStatus().value() }
        persistMemory { this@toSnapshotVo.persistMemorystate() }
        vmVo { vm.toVmSystem(conn) }
        snapshotDiskVos { disks.toSnapshotDiskVos(conn) }
        nicVos { nics.toNicVosFromSnapshot(conn, vmId) }
        applicationVos { applications.fromApplicationsToIdentifiedVos() }
    }
}
fun List<Snapshot>.toSnapshotVos(conn: Connection, vmId: String): List<SnapshotVo> =
    this@toSnapshotVos.map { it.toSnapshotVo(conn, vmId) }


fun SnapshotVo.toSnapshotBuilder(): SnapshotBuilder {
    return SnapshotBuilder()
        .description(this@toSnapshotBuilder.description)
        .persistMemorystate(this@toSnapshotBuilder.persistMemory)
}

fun SnapshotVo.toAddSnapshot(): Snapshot =
    this@toAddSnapshot.toSnapshotBuilder().build()

