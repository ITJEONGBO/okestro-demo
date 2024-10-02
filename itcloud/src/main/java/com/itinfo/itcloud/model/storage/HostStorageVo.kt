package com.itinfo.itcloud.model.storage

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.gson
import com.itinfo.itcloud.model.IdentifiedVo
import org.ovirt.engine.sdk4.types.HostStorage
import org.ovirt.engine.sdk4.types.LogicalUnit
import org.ovirt.engine.sdk4.types.StorageType
import java.io.Serializable

/**
 * [HostStorageVo]
 *
 * @property id [String]
 * @property logicalUnits List<[LogicalUnitVo]>
 * @property type [StorageType]
 * @property host [IdentifiedVo]
 */
class HostStorageVo(
    val id: String = "",
    val logicalUnits: List<LogicalUnitVo> = listOf(),
    val type: StorageType = StorageType.NFS,
    val host: IdentifiedVo = IdentifiedVo()
): Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
        private var bLogicalUnits: List<LogicalUnitVo> = listOf();fun logicalUnits(block: () -> List<LogicalUnitVo>?) { bLogicalUnits = block() ?: listOf() }
        private var bType: StorageType = StorageType.NFS;fun type(block: () -> StorageType?) { bType = block() ?: StorageType.NFS }
        private var bHost: IdentifiedVo = IdentifiedVo();fun host(block: () -> IdentifiedVo?) { bHost = block() ?: IdentifiedVo() }
        fun build(): HostStorageVo = HostStorageVo(bId, bLogicalUnits, bType, bHost)
    }

    companion object {
        private val log by LoggerDelegate()
        inline fun builder(block: Builder.() -> Unit): HostStorageVo = Builder().apply(block).build()
    }
}

fun HostStorage.toIscsiHostStorageVo() : HostStorageVo {
    val units: List<LogicalUnit> =
        this@toIscsiHostStorageVo.logicalUnits()
    return HostStorageVo.builder {
        id { this@toIscsiHostStorageVo.id() }
        logicalUnits { units.toIscsiLogicalUnitVos() }
        type { this@toIscsiHostStorageVo.type() }
        host { IdentifiedVo.builder { id { this@toIscsiHostStorageVo.host().id() } } }
    }
}
fun List<HostStorage>.toIscsiHostStorageVos(): List<HostStorageVo> =
    this@toIscsiHostStorageVos.map { it.toIscsiHostStorageVo() }


fun HostStorage.toFibreHostStorageVo() : HostStorageVo {
    val units: List<LogicalUnit> =
        this@toFibreHostStorageVo.logicalUnits()
    return HostStorageVo.builder {
        id { this@toFibreHostStorageVo.id() }
        logicalUnits { units.toFibreLogicalUnitVos() }
        type { this@toFibreHostStorageVo.type() }
        host { IdentifiedVo.builder { id { this@toFibreHostStorageVo.host().id() } } }
    }
}
fun List<HostStorage>.toFibreHostStorageVos(): List<HostStorageVo> =
    this@toFibreHostStorageVos.map { it.toFibreHostStorageVo() }

