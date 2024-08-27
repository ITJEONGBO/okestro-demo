package com.itinfo.itcloud.model.computing

import com.itinfo.itcloud.model.gson
import com.itinfo.util.ovirt.findAllHosts
import com.itinfo.util.ovirt.findAllVms
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.Cluster
import org.ovirt.engine.sdk4.types.Host
import java.io.Serializable

class SizeVo (
    val allCnt: Int = 0,
    val upCnt: Int = 0,
    val downCnt: Int = 0
): Serializable {
    override fun toString(): String = gson.toJson(this)

    class Builder {
        private var bAllCnt: Int = 0; fun allCnt(block: () -> Int) { bAllCnt = block() ?: 0 }
        private var bUpCnt: Int = 0; fun upCnt(block: () -> Int) { bUpCnt = block() ?: 0 }
        private var bDownCnt: Int = 0; fun downCnt(block: () -> Int) { bDownCnt = block() ?: 0 }
        fun build(): SizeVo = SizeVo(bAllCnt, bUpCnt, bDownCnt)
    }

    companion object{
        inline fun builder(block: Builder.() -> Unit): SizeVo = Builder().apply(block).build()
    }
}

fun Cluster.findHostCntFromCluster(conn: Connection): SizeVo {
    val allCnt: Int = conn.findAllHosts()
        .getOrDefault(listOf())
        .count { it.cluster().id().equals(this@findHostCntFromCluster.id()) }
    val upCnt: Int = conn.findAllHosts("status=up")
        .getOrDefault(listOf())
        .count { it.cluster().id().equals(this@findHostCntFromCluster.id()) }

    return SizeVo.builder {
        allCnt { allCnt }
        upCnt { upCnt }
        downCnt { allCnt - upCnt }
    }
}

fun Cluster.findVmCntFromCluster(conn: Connection): SizeVo {
    val allCnt: Int = conn.findAllVms()
        .getOrDefault(listOf())
        .count { it.cluster().id().equals(this@findVmCntFromCluster.id()) }
    val upCnt: Int = conn.findAllVms("status=up")
        .getOrDefault(listOf())
        .count { it.cluster().id().equals(this@findVmCntFromCluster.id()) }

    return SizeVo.builder {
        allCnt { allCnt }
        upCnt { upCnt }
        downCnt { allCnt - upCnt }
    }
}

fun Host.findVmCntFromHost(conn: Connection): SizeVo {
    val allCnt: Int = conn.findAllVms()
        .getOrDefault(listOf())
        .count { it.host().id().equals(this@findVmCntFromHost.id()) }
    val upCnt: Int = conn.findAllVms("status=up")
        .getOrDefault(listOf())
        .count { it.host().id().equals(this@findVmCntFromHost.id()) }

    return SizeVo.builder {
        allCnt { allCnt }
        upCnt { upCnt }
        downCnt { allCnt - upCnt }
    }
}