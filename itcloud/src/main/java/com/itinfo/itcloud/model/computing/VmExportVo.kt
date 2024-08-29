package com.itinfo.itcloud.model.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
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
 * @property hostVo [IdentifiedVo]
 * @property directory [String]
 * @property fileName [String]
 */
class VmExportVo (
    val vmVo: IdentifiedVo = IdentifiedVo(),
    val hostVo: IdentifiedVo = IdentifiedVo(),
    val directory: String = "",
    val fileName: String = "",
): Serializable {
    override fun toString(): String = gson.toJson(this)

    class Builder {
        private var bVmVo: IdentifiedVo = IdentifiedVo(); fun vmVo(block: () -> IdentifiedVo?) { bVmVo = block() ?: IdentifiedVo() }
        private var bHostVo: IdentifiedVo = IdentifiedVo(); fun hostVo(block: () -> IdentifiedVo?) { bHostVo = block() ?: IdentifiedVo() }
        private var bDirectory: String = ""; fun directory(block: () -> String?) { bDirectory = block() ?: "" }
        private var bFileName: String = ""; fun fileName(block: () -> String?) { bFileName = block() ?: "" }
        fun build(): VmExportVo = VmExportVo( bVmVo, bHostVo, bDirectory, bFileName)
    }

    companion object {
        private val log by LoggerDelegate()
        inline fun builder(block: VmExportVo.Builder.() -> Unit): VmExportVo = VmExportVo.Builder().apply(block).build()
    }
}
