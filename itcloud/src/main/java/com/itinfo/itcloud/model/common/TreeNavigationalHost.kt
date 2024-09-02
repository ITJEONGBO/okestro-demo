package com.itinfo.itcloud.model.common

import com.itinfo.itcloud.model.gson
import com.itinfo.util.ovirt.findAllVms
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.Host
import org.ovirt.engine.sdk4.types.Vm
import java.io.Serializable

class TreeNavigationalHost (
    id: String = "",
    name: String = "",
    val vms: List<TreeNavigational> = listOf(),
    val templates: List<TreeNavigational> = listOf()
): TreeNavigational(TreeNavigationalType.HOST, id, name), Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
        private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
        private var bVms: List<TreeNavigational> = listOf(); fun vms(block: () -> List<TreeNavigational>?) { bVms = block() ?: listOf() }
        private var bTemplates: List<TreeNavigational> = listOf(); fun templates(block: () -> List<TreeNavigational>?) { bTemplates = block() ?: listOf() }
        fun build(): TreeNavigationalHost = TreeNavigationalHost(bId, bName, bVms, bTemplates)
    }
    companion object {
        inline fun builder(block: TreeNavigationalHost.Builder.() -> Unit): TreeNavigationalHost = TreeNavigationalHost.Builder().apply(block).build()
    }
}

fun Host.toNavigationalWithStorageDomains(conn: Connection): TreeNavigationalHost {
    val vms: List<Vm> =
        conn.findAllVms()
            .getOrDefault(listOf())
            .filter {
                (it.hostPresent() && it.host().id() == this@toNavigationalWithStorageDomains.id()) ||
                        (it.placementPolicy().hostsPresent() &&
                         it.placementPolicy().hosts().any { h -> h?.id() == this@toNavigationalWithStorageDomains.id() })
            }
    return TreeNavigationalHost.builder {
        id { this@toNavigationalWithStorageDomains.id() }
        name { this@toNavigationalWithStorageDomains.name() }
        vms { vms.toTreeNavigationals() }
    }
}

fun List<Host>.toTreeNavigationals(conn: Connection): List<TreeNavigationalHost> =
    this@toTreeNavigationals.map { it.toNavigationalWithStorageDomains(conn) }