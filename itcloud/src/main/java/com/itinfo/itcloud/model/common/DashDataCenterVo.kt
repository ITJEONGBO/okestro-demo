package com.itinfo.itcloud.model.common

import com.itinfo.itcloud.model.gson
import com.itinfo.itcloud.model.storage.toStorageDomainIdNames
import com.itinfo.util.ovirt.findAllAttachedStorageDomainsFromDataCenter
import com.itinfo.util.ovirt.findAllClustersFromDataCenter
import com.itinfo.util.ovirt.findAllNetworksFromFromDataCenter
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.Cluster
import org.ovirt.engine.sdk4.types.DataCenter
import org.ovirt.engine.sdk4.types.Network
import org.ovirt.engine.sdk4.types.StorageDomain
import java.io.Serializable

class DashDataCenterVo (
    val id: String = "",
    val name: String = "",

    val clusters: List<DashClusterVo> = listOf(),
    val networks: List<DashNetworkVo> = listOf(),
    val storageDomains: List<DashStorageDomainVo> = listOf(),
): Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
        private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
        private var bClusters: List<DashClusterVo> = listOf(); fun clusters(block: () -> List<DashClusterVo>?) { bClusters = block() ?: listOf() }
        private var bNetworks: List<DashNetworkVo> = listOf(); fun networks(block: () -> List<DashNetworkVo>?) { bNetworks = block() ?: listOf() }
        private var bStorageDomains: List<DashStorageDomainVo> = listOf(); fun storageDomains(block: () -> List<DashStorageDomainVo>?) { bStorageDomains = block() ?: listOf() }

        fun build(): DashDataCenterVo = DashDataCenterVo(bId, bName, bClusters, bNetworks, bStorageDomains)
    }
    companion object {
        inline fun builder(block: DashDataCenterVo.Builder.() -> Unit): DashDataCenterVo = DashDataCenterVo.Builder().apply(block).build()
    }
}

fun DataCenter.toComputing(conn: Connection): DashDataCenterVo {
    val clusters: List<Cluster> =
        conn.findAllClustersFromDataCenter(this@toComputing.id())
            .getOrDefault(listOf())

    return DashDataCenterVo.builder {
        id { this@toComputing.id() }
        name { this@toComputing.name() }
        clusters { clusters.toDashClusters(conn) }
    }
}
fun List<DataCenter>.toComputings(conn: Connection): List<DashDataCenterVo> =
    this@toComputings.map { it.toComputing(conn) }


fun DataCenter.toNetwork(conn: Connection): DashDataCenterVo {
    val networks: List<Network> =
        conn.findAllNetworksFromFromDataCenter(this@toNetwork.id())
            .getOrDefault(listOf())

    return DashDataCenterVo.builder {
        id { this@toNetwork.id() }
        name { this@toNetwork.name() }
        networks { networks.toNetworks() }
    }
}
fun List<DataCenter>.toNetworks(conn: Connection): List<DashDataCenterVo> =
    this@toNetworks.map { it.toNetwork(conn) }


fun DataCenter.toStorageDomain(conn: Connection): DashDataCenterVo {
    val storageDomains: List<StorageDomain> =
        conn.findAllAttachedStorageDomainsFromDataCenter(this@toStorageDomain.id())
            .getOrDefault(listOf())

    return DashDataCenterVo.builder {
        id { this@toStorageDomain.id() }
        name { this@toStorageDomain.name() }
        storageDomains { storageDomains.toStorageDomains(conn) }
    }
}
fun List<DataCenter>.toStorageDomains(conn: Connection): List<DashDataCenterVo> =
    this@toStorageDomains.map { it.toStorageDomain(conn) }

