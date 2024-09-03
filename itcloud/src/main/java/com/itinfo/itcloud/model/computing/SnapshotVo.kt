package com.itinfo.itcloud.model.computing

import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.fromApplicationsToIdentifiedVos
import com.itinfo.itcloud.gson
import com.itinfo.itcloud.model.network.NicVo
import com.itinfo.itcloud.model.network.toNicVosFromVm
import com.itinfo.itcloud.ovirtDf
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.error.toError
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.*
import org.slf4j.LoggerFactory
import java.io.Serializable
import java.math.BigInteger

private val log = LoggerFactory.getLogger(SnapshotVo::class.java)

/**
 * [SnapshotVo]
 *
 * @property id[String]
 * @property vmVo [VmVo]
 * @property description[String]
 * @property date[String]
 * @property persistMemory[Boolean] 메모리 저장 여부 f/t
 * @property status[String]
 * @property setMemory[BigInteger] // 설정된 메모리
 * @property guaranteedMemory[BigInteger] 할당할 실제 메모리
 * @property cpuCore[Int]
 * @property snapshotDiskVos List<[SnapshotDiskVo]>
 * @property nicVos List<[NicVo]>
 * @property applicationVos List<[IdentifiedVo]>
// * @property saveMemory [Boolean] 스냅샷 생성창에서 필요
 */
class SnapshotVo (
    val id: String = "",
    val vmVo: VmVo = VmVo(),
    val description: String = "",
    val date: String = "",
    val persistMemory: Boolean = false,
    val status: String = "",
    val setMemory: BigInteger = BigInteger.ZERO,
    val guaranteedMemory: BigInteger = BigInteger.ZERO,
    val cpuCore: Int = 0,
    val snapshotDiskVos: List<SnapshotDiskVo> = listOf(),
    val nicVos: List<NicVo> = listOf(),
    val applicationVos: List<IdentifiedVo> = listOf(),

//    val saveMemory: boolean = false
): Serializable {
    override fun toString(): String = gson.toJson(this)

    class Builder {
        private var bId: String = ""; fun id(block: () -> String?) { bId = block() ?: "" }
        private var bVmVo: VmVo = VmVo(); fun vmVo(block: () -> VmVo?) { bVmVo = block() ?: VmVo()  }
        private var bDescription: String = ""; fun description(block: () -> String?) { bDescription= block() ?: "" }
        private var bDate: String = ""; fun date(block: () -> String?) { bDate= block() ?: "" }
        private var bPersistMemory: Boolean = false; fun persistMemory(block: () -> Boolean?) { bPersistMemory= block() ?: false }
        private var bStatus: String = ""; fun status(block: () -> String?) { bStatus= block() ?: "" }
        private var bSetMemory: BigInteger = BigInteger.ZERO; fun setMemory(block: () -> BigInteger?) { bSetMemory= block() ?: BigInteger.ZERO }
        private var bGuaranteedMemory: BigInteger = BigInteger.ZERO; fun guaranteedMemory(block: () -> BigInteger?) { bGuaranteedMemory= block() ?: BigInteger.ZERO }
        private var bCpuCore: Int = 0; fun cpuCore(block: () -> Int?) { bCpuCore= block() ?: 0 }
        private var bSnapshotDiskVos: List<SnapshotDiskVo> = listOf(); fun snapshotDiskVos(block: () -> List<SnapshotDiskVo>?) { bSnapshotDiskVos = block() ?: listOf() }
        private var bNicVos: List<NicVo> = listOf(); fun nicVos(block: () -> List<NicVo>?) { bNicVos = block() ?: listOf() }
        private var bApplicationVos: List<IdentifiedVo> = listOf(); fun applicationVos(block: () -> List<IdentifiedVo>?) { bApplicationVos = block() ?: listOf() }

        fun build(): SnapshotVo = SnapshotVo( bId, bVmVo, bDescription, bDate, bPersistMemory, bStatus, bSetMemory, bGuaranteedMemory, bCpuCore, bSnapshotDiskVos, bNicVos, bApplicationVos)
    }

    companion object {
        inline fun builder(block: SnapshotVo.Builder.() -> Unit): SnapshotVo = SnapshotVo.Builder().apply(block).build()
    }
}


@Throws(Error::class)
fun Snapshot.toSnapshotVo(conn: Connection, vmId: String): SnapshotVo {
    val vm: Vm = conn.findVm(vmId).getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()

    val disks: List<Disk?> = if (this@toSnapshotVo.vmPresent())
        conn.findAllSnapshotDisksFromVm(vmId, this@toSnapshotVo.id())
            .getOrDefault(listOf())
    else
        conn.findAllDiskAttachmentsFromVm(vmId)
            .getOrDefault(listOf())
            .map { conn.findDisk(it.disk().id()).getOrNull() }
    val snapshotDiskVos: List<SnapshotDiskVo> =
        disks.filterNotNull().toSnapshotDiskVosd(conn, vmId)
    val applications: List<Application> =
        conn.findAllApplicationsFromVm(vmId)
        .getOrDefault(listOf())

    return SnapshotVo.builder {
        id { this@toSnapshotVo.id() }
        description { this@toSnapshotVo.description() }
        date { if (this@toSnapshotVo.vmPresent()) ovirtDf.format(this@toSnapshotVo.date().time) else "현재" }
        status { this@toSnapshotVo.snapshotStatus().value() }
        persistMemory { this@toSnapshotVo.persistMemorystate() }
        setMemory { vm.memory() }
        guaranteedMemory { vm.memoryPolicy().guaranteed() }
        cpuCore {
            vm.cpu().topology().coresAsInteger() *
            vm.cpu().topology().socketsAsInteger() *
            vm.cpu().topology().threadsAsInteger()
        }
        snapshotDiskVos { snapshotDiskVos }
        nicVos { this@toSnapshotVo.nics().toNicVosFromVm(conn, vm.id()) }
        applicationVos { applications.fromApplicationsToIdentifiedVos() }
    }
}

@Throws(Error::class)
fun List<Snapshot>.toSnapshotVos(conn: Connection, vmId: String): List<SnapshotVo> =
    this@toSnapshotVos.map { it.toSnapshotVo(conn, vmId) }