package com.itinfo.itcloud.model.common

import com.itinfo.itcloud.model.gson
import org.ovirt.engine.sdk4.types.Disk
import java.io.Serializable

class DashDiskVo (
    val id: String = "",
    val name: String = "",
): Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
        private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }

        fun build(): DashDiskVo = DashDiskVo(bId, bName)
    }
    companion object {
        inline fun builder(block: DashDiskVo.Builder.() -> Unit): DashDiskVo = DashDiskVo.Builder().apply(block).build()
    }
}

fun Disk.toDisk(): DashDiskVo = DashDiskVo.builder {
    id { this@toDisk.id() }
    name { this@toDisk.name() }
}
fun List<Disk>.toDisks(): List<DashDiskVo> =
    this@toDisks.map { it.toDisk() }