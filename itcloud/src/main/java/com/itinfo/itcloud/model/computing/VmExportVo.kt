package com.itinfo.itcloud.model.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.gson
import com.itinfo.itcloud.model.storage.StorageDomainVo
import org.slf4j.LoggerFactory
import java.io.Serializable

private val log = LoggerFactory.getLogger(VmExportVo::class.java)

/**
 * [VmExportVo]
 * OVA로 내보내기
 *
 * @property vmVo [VmVo]
 * @property storageDomainVo [StorageDomainVo]
 * @property hostVo [HostVo]
 * @property exclusive [Boolean] Use the `exclusive` parameter when the virtual machine should be exported even if another copy of it already exists in the export domain (override).
 * @property discardSnapshot [Boolean] Use the `discard_snapshots` parameter when the virtual machine should be exported with all of its snapshots collapsed.
 * @property directory [String]
 * @property fileName [String]
 */
class VmExportVo (
    val vmVo: VmVo = VmVo(),
    val storageDomainVo: StorageDomainVo = StorageDomainVo(),
    val hostVo: HostVo = HostVo(),
    val exclusive: Boolean = false,
    val discardSnapshot: Boolean = false,
    val directory: String = "",
    val fileName: String = "",
): Serializable {
    override fun toString(): String = gson.toJson(this)

    class Builder {
        private var bVmVo: VmVo = VmVo(); fun vmVo(block: () -> VmVo?) { bVmVo = block() ?: VmVo() }
        private var bStorageDomainVo: StorageDomainVo = StorageDomainVo(); fun storageDomainVo(block: () -> StorageDomainVo?) { bStorageDomainVo = block() ?: StorageDomainVo() }
        private var bHostVo: HostVo = HostVo(); fun hostVo(block: () -> HostVo?) { bHostVo = block() ?: HostVo() }
        private var bExclusive: Boolean =  false; fun exclusive(block: () -> Boolean?) { bExclusive = block() ?:  false }
        private var bDiscardSnapshot: Boolean =  false; fun discardSnapshot(block: () -> Boolean?) { bDiscardSnapshot = block() ?:  false }
        private var bDirectory: String = ""; fun directory(block: () -> String?) { bDirectory = block() ?: "" }
        private var bFileName: String = ""; fun fileName(block: () -> String?) { bFileName = block() ?: "" }
        fun build(): VmExportVo = VmExportVo( bVmVo, bStorageDomainVo, bHostVo, bExclusive, bDiscardSnapshot, bDirectory, bFileName)
    }

    companion object {
        private val log by LoggerDelegate()
        inline fun builder(block: VmExportVo.Builder.() -> Unit): VmExportVo = VmExportVo.Builder().apply(block).build()
    }
}