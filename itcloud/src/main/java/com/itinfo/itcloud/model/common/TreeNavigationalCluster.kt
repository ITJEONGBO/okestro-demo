package com.itinfo.itcloud.model.common

import com.itinfo.itcloud.model.gson
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

fun Cluster.toNavigationalWithStorageDomains(conn: Connection): TreeNavigationalCluster {
    val hosts: List<Host> =
        conn.findAllHosts()
            .getOrDefault(listOf())
            .filter { it.cluster().id() == this@toNavigationalWithStorageDomains.id() }

    return TreeNavigationalCluster.builder {
        id { this@toNavigationalWithStorageDomains.id() }
        name { this@toNavigationalWithStorageDomains.name() }
        hosts { hosts.toDashHosts(conn) }
    }
}

fun List<Cluster>.toDashClusters(conn: Connection): List<TreeNavigationalCluster> =
    this@toDashClusters.map { it.toDashCluster(conn) }
