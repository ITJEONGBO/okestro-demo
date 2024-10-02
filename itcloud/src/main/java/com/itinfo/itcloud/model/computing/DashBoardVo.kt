package com.itinfo.itcloud.model.computing

import com.itinfo.itcloud.gson
import com.itinfo.util.ovirt.*
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.StorageDomainStatus
import org.ovirt.engine.sdk4.types.StorageType
import org.slf4j.LoggerFactory
import java.io.Serializable

private val log = LoggerFactory.getLogger(DashBoardVo::class.java)

/**
 * [DashBoardVo]
 * 대시보드 그래프
 *
 * @property datacenters
 * @property datacentersUp
 * @property datacentersDown
 * @property clusters
 * @property hosts
 * @property hostsUp
 * @property hostsDown
 * @property vms
 * @property vmsUp
 * @property vmsDown
 * @property events
 * @property eventsAlert
 * @property eventsError
 * @property eventsWarning
 */
class DashBoardVo (
    val datacenters: Int = 0,
    val datacentersUp: Int = 0,
    val datacentersDown: Int = 0,
    val clusters: Int = 0,
    val hosts: Int = 0,
    val hostsUp: Int = 0,
    val hostsDown: Int = 0,
    val vms: Int = 0,
    val vmsUp: Int = 0,
    val vmsDown: Int = 0,
    val storageDomains: Int = 0,
    val events: Int = 0,
    val eventsAlert: Int = 0,
    val eventsError: Int = 0,
    val eventsWarning: Int = 0,
): Serializable {
    override fun toString(): String =
        gson.toJson(this)

    class Builder {
        private var bDatacenters: Int = 0; fun datacenters(block: () -> Int?) { bDatacenters = block() ?: 0}
        private var bDatacentersUp: Int = 0; fun datacentersUp(block: () -> Int?) { bDatacentersUp = block() ?: 0}
        private var bDatacentersDown: Int = 0; fun datacentersDown(block: () -> Int?) { bDatacentersDown = block() ?: 0}
        private var bClusters: Int = 0; fun clusters(block: () -> Int?) { bClusters = block() ?: 0}
        private var bHosts: Int = 0; fun hosts(block: () -> Int?) { bHosts = block() ?: 0}
        private var bHostsUp: Int = 0; fun hostsUp(block: () -> Int?) { bHostsUp = block() ?: 0}
        private var bHostsDown: Int = 0; fun hostsDown(block: () -> Int?) { bHostsDown = block() ?: 0}
        private var bVms: Int = 0; fun vms(block: () -> Int?) { bVms = block() ?: 0}
        private var bVmsUp: Int = 0; fun vmsUp(block: () -> Int?) { bVmsUp = block() ?: 0}
        private var bVmsDown: Int = 0; fun vmsDown(block: () -> Int?) { bVmsDown = block() ?: 0}
        private var bStorageDomains: Int = 0; fun storageDomains(block: () -> Int?) { bStorageDomains = block() ?: 0}
        private var bEvents: Int = 0; fun events(block: () -> Int?) { bEvents = block() ?: 0}
        private var bEventAlert: Int = 0; fun eventsAlert(block: () -> Int?) { bEventAlert = block() ?: 0}
        private var bEventError: Int = 0; fun eventsError(block: () -> Int?) { bEventError = block() ?: 0}
        private var bEventsWarning: Int = 0; fun eventsWarning(block: () -> Int?) { bEventsWarning = block() ?: 0}
        fun build(): DashBoardVo = DashBoardVo(bDatacenters, bDatacentersUp, bDatacentersDown, bClusters, bHosts, bHostsUp, bHostsDown, bVms, bVmsUp, bVmsDown, bStorageDomains, bEvents, bEventAlert, bEventError, bEventsWarning)
    }

    companion object {
        inline fun builder(block: DashBoardVo.Builder.() -> Unit): DashBoardVo = DashBoardVo.Builder().apply(block).build()
    }
}

fun Connection.toDashboardVo(): DashBoardVo {
    val statusUp = "status=up"
    val dataCenters: Int =
        this@toDashboardVo.findAllDataCenters()
            .getOrDefault(listOf())
            .size
    val dataCentersUp: Int =
        this@toDashboardVo.findAllDataCenters(statusUp)
            .getOrDefault(listOf())
            .size
    val hosts: Int =
        this@toDashboardVo.findAllHosts()
            .getOrDefault(listOf())
            .size
    val hostsUp: Int =
        this@toDashboardVo.findAllHosts(statusUp)
            .getOrDefault(listOf())
            .size
    val vms: Int =
        this@toDashboardVo.findAllVms()
            .getOrDefault(listOf())
            .size
    val vmsUp: Int =
        this@toDashboardVo.findAllVms(statusUp)
            .getOrDefault(listOf())
            .size
    val clusters: Int =
        this@toDashboardVo.findAllClusters()
            .getOrDefault(listOf())
            .size
    val storageDomains: Int =
        this@toDashboardVo.findAllStorageDomains()
            .getOrDefault(listOf())
            .filter { it.storage().type() != StorageType.GLANCE }
            .size
    val eventsAlert: Int =
        this@toDashboardVo.findAllEvents("severity=alert")
            .getOrDefault(listOf())
            .size
    val eventsError: Int =
        this@toDashboardVo.findAllEvents(searchQuery = "severity=error and time > Today")
            .getOrDefault(listOf())
            .size
    val eventsWarning: Int =
        this@toDashboardVo.findAllEvents(searchQuery = "severity=warning and time > Today")
            .getOrDefault(listOf())
            .size

    return DashBoardVo.builder {
        datacenters { dataCenters }
        datacentersUp { dataCentersUp }
        datacentersDown { dataCenters - dataCentersUp }
        clusters { clusters }
        hosts { hosts }
        hostsUp { hostsUp }
        hostsDown { hosts - hostsUp }
        vms { vms }
        vmsUp { vmsUp }
        vmsDown { vms - vmsUp }
        storageDomains { storageDomains }
        events { eventsAlert + eventsError + eventsWarning }
        eventsAlert { eventsAlert }
        eventsError { eventsError }
        eventsWarning { eventsWarning }
    }
}