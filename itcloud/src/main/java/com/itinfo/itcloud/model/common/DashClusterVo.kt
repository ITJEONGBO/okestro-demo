package com.itinfo.itcloud.model.common

import com.itinfo.itcloud.model.gson
import com.itinfo.util.ovirt.findAllHosts
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.Cluster
import org.ovirt.engine.sdk4.types.Host
import java.io.Serializable

class DashClusterVo (
    val id: String = "",
    val name: String = "",
    val hosts: List<DashHostVo> = listOf(),
): Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
        private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
        private var bHosts: List<DashHostVo> = listOf(); fun hosts(block: () -> List<DashHostVo>?) { bHosts = block() ?: listOf() }

        fun build(): DashClusterVo = DashClusterVo(bId, bName, bHosts)
    }
    companion object {
        inline fun builder(block: DashClusterVo.Builder.() -> Unit): DashClusterVo = DashClusterVo.Builder().apply(block).build()
    }
}

fun Cluster.toDashCluster(conn: Connection): DashClusterVo {
    val hosts: List<Host> =
        conn.findAllHosts()
            .getOrDefault(listOf())
            .filter { it.cluster().id() == this@toDashCluster.id() }

    return DashClusterVo.builder {
        id { this@toDashCluster.id() }
        name { this@toDashCluster.name() }
        hosts { hosts.toDashHosts(conn) }
    }
}

fun List<Cluster>.toDashClusters(conn: Connection): List<DashClusterVo> =
    this@toDashClusters.map { it.toDashCluster(conn) }
