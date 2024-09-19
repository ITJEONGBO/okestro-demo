package com.itinfo.itcloud.model.computing

import com.itinfo.itcloud.gson
import com.itinfo.util.ovirt.*
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.*
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
    val allHost: List<Host> = conn.findAllHostsFromCluster(this@findHostCntFromCluster.id()).getOrDefault(listOf())
    val allCnt: Int = allHost.size
    val upCnt: Int = allHost.count { it.status() == HostStatus.UP }
    return SizeVo.builder {
        allCnt { allCnt }
        upCnt { upCnt }
        downCnt { allCnt - upCnt }
    }
}

fun Cluster.findVmCntFromCluster(conn: Connection): SizeVo {
    val allVms: List<Vm> = conn.findAllVmsFromCluster(this@findVmCntFromCluster.id()).getOrDefault(listOf())
    val allCnt: Int = allVms.size
    val upCnt: Int = allVms.count { it.status() == VmStatus.UP }
    return SizeVo.builder {
        allCnt { allCnt }
        upCnt { upCnt }
        downCnt { allCnt - upCnt }
    }
}

fun Host.findVmCntFromHost(conn: Connection): SizeVo {
    val allVms: List<Vm> = conn.findAllVmsFromHost(this@findVmCntFromHost.id()).getOrDefault(listOf())
    val allCnt: Int = allVms.size
    val upCnt: Int = allVms.count { it.status() == VmStatus.UP }
    return SizeVo.builder {
        allCnt { allCnt }
        upCnt { upCnt }
        downCnt { allCnt - upCnt }
    }
}
