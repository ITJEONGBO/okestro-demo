package com.itinfo.itcloud.model.common

import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.gson
import com.itinfo.util.ovirt.findAllVms
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.Host
import org.ovirt.engine.sdk4.types.Vm
import java.io.Serializable

class DashHostVo (
    val id: String = "",
    val name: String = "",
    val vms: List<DashVmVo> = listOf(),
    val templates: List<DashTemplateVo> = listOf()
): Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
        private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
        private var bVms: List<DashVmVo> = listOf(); fun vms(block: () -> List<DashVmVo>?) { bVms = block() ?: listOf() }
        private var bTemplates: List<DashTemplateVo> = listOf(); fun templates(block: () -> List<DashTemplateVo>?) { bTemplates = block() ?: listOf() }

        fun build(): DashHostVo = DashHostVo(bId, bName, bVms, bTemplates)
    }
    companion object {
        inline fun builder(block: DashHostVo.Builder.() -> Unit): DashHostVo = DashHostVo.Builder().apply(block).build()
    }
}

fun Host.toDashHost(conn: Connection): DashHostVo {
    val vms: List<Vm> =
        conn.findAllVms()
            .getOrDefault(listOf())
            .filter {
                (it.hostPresent() && it.host().id() == this@toDashHost.id()) ||
                        (it.placementPolicy().hostsPresent()
                                && it.placementPolicy().hosts()
                            .any { h -> h?.id() == this@toDashHost.id() })
            }
    return DashHostVo.builder {
        id { this@toDashHost.id() }
        name { this@toDashHost.name() }
        vms { vms.toDashVms() }
    }
}

fun List<Host>.toDashHosts(conn: Connection): List<DashHostVo> =
    this@toDashHosts.map { it.toDashHost(conn) }