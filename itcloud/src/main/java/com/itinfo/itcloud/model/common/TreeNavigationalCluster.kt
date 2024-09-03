package com.itinfo.itcloud.model.common

import com.itinfo.itcloud.gson
import com.itinfo.util.ovirt.findAllHosts
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.Cluster
import org.ovirt.engine.sdk4.types.Host
import java.io.Serializable

class TreeNavigationalCluster (
    id: String = "",
    name: String = "",
    val hosts: List<TreeNavigational> = listOf(),
): TreeNavigational(TreeNavigationalType.CLUSTER, id, name), Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
        private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
        private var bHosts: List<TreeNavigational> = listOf(); fun hosts(block: () -> List<TreeNavigational>?) { bHosts = block() ?: listOf() }
        fun build(): TreeNavigationalCluster = TreeNavigationalCluster(bId, bName, bHosts)
    }
    companion object {
        inline fun builder(block: TreeNavigationalCluster.Builder.() -> Unit): TreeNavigationalCluster = TreeNavigationalCluster.Builder().apply(block).build()
    }
}

fun Cluster.toNavigational(conn: Connection): TreeNavigationalCluster {
    val hosts: List<Host> =
        conn.findAllHosts()
            .getOrDefault(listOf())
            .filter { it.cluster().id() == this@toNavigational.id() }

    return TreeNavigationalCluster.builder {
        id { this@toNavigational.id() }
        name { this@toNavigational.name() }
        hosts { hosts.fromDisksToTreeNavigationals(conn) }
    }
}

fun List<Cluster>.toNavigationals(conn: Connection): List<TreeNavigationalCluster> =
    this@toNavigationals.map { it.toNavigational(conn) }
