package com.itinfo.itcloud.model.common

import com.itinfo.itcloud.model.gson
import com.itinfo.util.ovirt.findAllDisksFromStorageDomain
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.Disk
import org.ovirt.engine.sdk4.types.StorageDomain
import java.io.Serializable

class DashStorageDomainVo (
    val id: String = "",
    val name: String = "",

    val disks: List<DashDiskVo> = listOf()
): Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
        private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
        private var bDisks: List<DashDiskVo> = listOf(); fun disks(block: () -> List<DashDiskVo>?) { bDisks = block() ?: listOf() }

        fun build(): DashStorageDomainVo = DashStorageDomainVo(bId, bName, bDisks)
    }
    companion object {
        inline fun builder(block: DashStorageDomainVo.Builder.() -> Unit): DashStorageDomainVo = DashStorageDomainVo.Builder().apply(block).build()
    }
}

fun StorageDomain.toStorageDomain(conn: Connection): DashStorageDomainVo {
    val disks: List<Disk> =
        conn.findAllDisksFromStorageDomain(this@toStorageDomain.id())
            .getOrDefault(listOf())

    return DashStorageDomainVo.builder {
        id { this@toStorageDomain.id() }
        name { this@toStorageDomain.name() }
        disks { disks.toDisks() }
    }
}

fun List<StorageDomain>.toStorageDomains(conn: Connection): List<DashStorageDomainVo> =
    this@toStorageDomains.map { it.toStorageDomain(conn) }