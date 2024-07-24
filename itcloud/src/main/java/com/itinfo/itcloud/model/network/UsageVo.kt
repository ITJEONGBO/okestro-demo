package com.itinfo.itcloud.model.network

import com.itinfo.itcloud.model.gson
import org.ovirt.engine.sdk4.types.Network
import org.ovirt.engine.sdk4.types.NetworkUsage
import org.slf4j.LoggerFactory
import java.io.Serializable

private val log = LoggerFactory.getLogger(UsageVo::class.java)
/**
 * [UsageVo]
 *
 * @property vm
 * @property management 관리
 * @property display 출력
 * @property migration
 * @property gluster
 * @property defaultRoute
 */
class UsageVo (
    val vm: Boolean = false,
    val management: Boolean = false,
    val display: Boolean = false,
    val migration: Boolean = false,
    val gluster: Boolean = false,
    val defaultRoute: Boolean = false
): Serializable{
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bVm: Boolean = false; fun vm(block: () -> Boolean) { bVm = block() ?: false }
        private var bManagement: Boolean = false; fun management(block: () -> Boolean) { bManagement = block() ?: false }
        private var bDisplay: Boolean = false; fun display (block: () -> Boolean) { bDisplay = block() ?: false }
        private var bMigration: Boolean = false; fun migration(block: () -> Boolean) { bMigration = block() ?: false }
        private var bGluster: Boolean = false; fun gluster (block: () -> Boolean) { bGluster = block() ?: false }
        private var bDefaultRoute: Boolean = false; fun defaultRoute (block: () -> Boolean) { bDefaultRoute = block() ?: false }
        fun build(): UsageVo = UsageVo(bVm, bManagement, bDisplay, bMigration, bGluster, bDefaultRoute)
    }

    companion object{
        inline fun builder(blcok: UsageVo.Builder.() -> Unit): UsageVo = UsageVo.Builder().apply(blcok).build()
    }
}

fun Network.toUsageVo(): UsageVo {
    log.debug("Network.toUsageVo ... ")
    return UsageVo.builder {
        vm { this@toUsageVo.usages().contains(NetworkUsage.VM) }
        display { this@toUsageVo.usages().contains(NetworkUsage.DISPLAY) }
        migration { this@toUsageVo.usages().contains(NetworkUsage.MIGRATION) }
        management { this@toUsageVo.usages().contains(NetworkUsage.MANAGEMENT) }
        defaultRoute { this@toUsageVo.usages().contains(NetworkUsage.DEFAULT_ROUTE) }
        gluster { this@toUsageVo.usages().contains(NetworkUsage.GLUSTER) }
    }
}

