package com.itinfo.service.impl

import com.itinfo.OvirtStatsName
import com.itinfo.common.LoggerDelegate
import com.itinfo.dao.DashboardDao
import com.itinfo.model.*
import com.itinfo.model.DataCenterVo.Companion.simpleSetup
import com.itinfo.service.DashboardService
import com.itinfo.service.engine.ConnectionService

import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.*

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.math.BigDecimal

@Service
class DashboardServiceImpl : BaseService(), DashboardService {
	@Autowired private lateinit var connectionService: ConnectionService

	@Autowired private lateinit var dashboardDao: DashboardDao
	
	private var dcv: DataCenterVo? = null
	private val usageVos: MutableList<UsageVo> = arrayListOf()
	
	override fun retrieveDataCenterStatus(): DataCenterVo {
		log.info("... retrieveDataCenterStatus")
		val connection = connectionService.connection
		dcv = simpleSetup(connection)
		// getClusters(connection);
		getHosts(connection)
		// getVms(connection);
		return dcv!!
	}

	@Deprecated("")
	private fun getClusters(connection: Connection) {
		log.info("... getClusters")
		val clusters = sysSrvHelper.findAllClusters(connection, "")
		dcv?.clusters = clusters.size
	}

	private fun getHosts(connection: Connection) {
		log.info("... getHosts")
		// VmsService vmsService = systemService.vmsService();
		// HostsService hostsService = systemService.hostsService();
		var hosts = sysSrvHelper.findAllHosts(connection, "status!=up")
		dcv?.hostsDown = hosts.size
		hosts = sysSrvHelper.findAllHosts(connection, "status=up")
		dcv?.hostsUp = hosts.size
		val ids: MutableList<String> = ArrayList()
		val interfaceIds: MutableList<String> = ArrayList()
		var sumTotalCpu = 0
		for (host in hosts) {
			val stats = sysSrvHelper.findAllStatisticsFromHost(connection, host.id())
			stats.forEach { stat: Statistic ->
				if (stat.name() == OvirtStatsName.MEMORY_TOTAL) dcv?.memoryTotal =
					dcv?.memoryTotal?.add(stat.values().first().datum()) ?: BigDecimal.ZERO
				if (stat.name() == OvirtStatsName.MEMORY_USED) dcv?.memoryUsed =
					dcv?.memoryUsed?.add(stat.values().first().datum()) ?: BigDecimal.ZERO
				if (stat.name() == OvirtStatsName.MEMORY_FREE) dcv?.memoryFree =
					dcv?.memoryFree?.add(stat.values().first().datum()) ?: BigDecimal.ZERO
				if (stat.name() == OvirtStatsName.KCM_CPU_CURRENT)

				if (stat.name() == OvirtStatsName.CPU_CURRENT_USER) dcv?.cpuCurrentUser =
					dcv?.cpuCurrentUser?.plus(stat.values().first().datum().toDouble()) ?: 0.0
				if (stat.name() == OvirtStatsName.CPU_CURRENT_SYSTEM) dcv?.cpuCurrentSystem =
					dcv?.cpuCurrentSystem?.plus(stat.values().first().datum().toDouble()) ?: 0.0
				if (stat.name() == "cpu.current.idle") dcv?.cpuCurrentIdle =
					dcv?.cpuCurrentIdle?.plus(stat.values()[0].datum().toDouble()) ?: 0.0
			}
			sumTotalCpu +=
				host.cpu().topology().cores().toInt() *
				host.cpu().topology().sockets().toInt() *
				host.cpu().topology().threads().toInt()
			val vms = sysSrvHelper.findAllVms(connection, "host = " + host.name())
			var sumCpu = 0
			for (vm in vms) sumCpu += vm.cpu().topology().cores().toInt() *
					vm.cpu().topology().sockets().toInt() * vm.cpu().topology().threads().toInt()
			dcv?.usingcpu = sumCpu
			val nics = sysSrvHelper.findAllNicsFromHost(connection, host.id())
			interfaceIds.addAll(nics.map { it.id() })
			ids.add(host.id())
		}
		dcv?.totalcpu = sumTotalCpu
		if (ids.isNotEmpty()) {
			val hostStat = dashboardDao.retrieveHosts(ids)
			if (hostStat.size > 0) hostStat.forEach { (_, _, _, historyDatetime, _, memoryUsagePercent, cpuUsagePercent): HostVo ->
				val usageVo = UsageVo()
				usageVo.cpuUsages = cpuUsagePercent
				usageVo.memoryUsages = memoryUsagePercent
				usageVo.usageDate = historyDatetime
				usageVos.add(usageVo)
			}
		}
		if (interfaceIds.size > 0) {
			val hostInterfaces = dashboardDao.retrieveHostsInterface(interfaceIds)
			if (hostInterfaces.size > 0) for (i in hostInterfaces.indices) {
				usageVos[i].receiveUsages = hostInterfaces[i].receiveRatePercent
				usageVos[i].transitUsages = hostInterfaces[i].transmitRatePercent
			}
		}
		getStorageDomains(connection)
	}

	private fun getStorageDomains(connection: Connection) {
		log.info("... getStorageDomains")
		val dcvT = dcv ?: run {
			log.error("dcv NOT defined!")
		}
		var storageDomains = sysSrvHelper.findAllStorageDomains(connection, "status=unattached")
		storageDomains.forEach { storageDomain: StorageDomain ->
			if (storageDomain.type().name == "DATA") dcv?.storagesUnattached = dcv?.storagesUnattached?.plus(1) ?: 0
		}
		storageDomains = sysSrvHelper.findAllStorageDomains(connection, "status=active")
		val storageIds: MutableList<String> = ArrayList()
		storageDomains.forEach { storageDomain: StorageDomain ->
			if (storageDomain.type().name == "DATA") {
				dcv!!.storageAvaliable =
					if (dcv!!.storageAvaliable != null) dcv!!.storageAvaliable.add(storageDomain.available()) else storageDomain.available()
				dcv!!.storageUsed =
					if (dcv!!.storageUsed != null) dcv!!.storageUsed.add(storageDomain.used()) else storageDomain.used()
				storageIds.add(storageDomain.id())
			}
		}
		dcv!!.storagesActive = storageIds.size
		if (storageIds.size > 0) {
			val storages = dashboardDao.retireveStorages(storageIds)
			if (storages.size > 0) for (j in storages.indices) {
				usageVos[j].storageUsages =
					storages[j].usedDiskSizeGb * 100 / (storages[j].availableDiskSizeGb + storages[j].usedDiskSizeGb)
				usageVos[j].storageUsageDate = storages[j].historyDatetime
			}
		}
		dcv!!.usageVos = usageVos
	}

	@Deprecated("")
	private fun getVms(connection: Connection) {
		var vms: List<Vm?> = sysSrvHelper.findAllVms(connection, "status!=up")
		dcv!!.vmsDown = vms.size
		vms = sysSrvHelper.findAllVms(connection, "status=up")
		dcv!!.vmsUp = vms.size
	}

	override fun retrieveEvents(): List<EventVo> {
		log.info("... retrieveEvents")
		val connection = connectionService.connection
		val items = sysSrvHelper.findAllEvents(connection, "time>today")
		return items.toEventVos()
	}
	
	companion object {
		private val log by LoggerDelegate()
	}
}