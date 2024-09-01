package com.itinfo.itcloud.model.common

import com.itinfo.itcloud.model.gson
import org.ovirt.engine.sdk4.types.Vm
import java.io.Serializable

class DashVmVo (
    val id: String = "",
    val name: String = "",
): Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
        private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }

        fun build(): DashVmVo = DashVmVo(bId, bName)
    }
    companion object {
        inline fun builder(block: DashVmVo.Builder.() -> Unit): DashVmVo = DashVmVo.Builder().apply(block).build()
    }
}

fun Vm.toDashVm(): DashVmVo = DashVmVo.builder {
    id { this@toDashVm.id() }
    name { this@toDashVm.name() }
}

fun List<Vm>.toDashVms(): List<DashVmVo> =
    this@toDashVms.map { it.toDashVm() }