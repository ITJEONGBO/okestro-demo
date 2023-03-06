package com.itinfo.model.karajan

import com.itinfo.SystemServiceHelper
import com.itinfo.model.SystemPropertiesVo

import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.Cluster
import org.ovirt.engine.sdk4.types.Host
import org.ovirt.engine.sdk4.types.Statistic
import org.ovirt.engine.sdk4.types.Vm

import org.springframework.jdbc.core.JdbcTemplate

import java.math.BigDecimal
import java.math.BigInteger
import java.util.function.Consumer

data class KarajanVo(
	var clusters: List<ClusterVo>
	, var cpuThreshold: Int = -1
	, var memoryThreshold: Int = -1
)

fun SystemPropertiesVo.toKarajanVo(connection: Connection): KarajanVo {
	val clusters: List<Cluster>
		= SystemServiceHelper.getInstance().findAllClusters(connection)
	val tgtClusters: List<ClusterVo>
		= clusters.toClusterVos(connection)
	return KarajanVo(
		tgtClusters,
		cpuThreshold,
		memoryThreshold
	)
}

data class ClusterVo(
	var id: String
	, var name: String
	, var cpuType: String
	, var hosts: List<HostVo>
	, var vms: List<WorkloadVmVo>
)

fun Cluster.toClusterVo(connection: Connection): ClusterVo {
	val hosts2Add: List<Host>
		= SystemServiceHelper.getInstance().findAllHosts(connection, "cluster=${name()}")
	val hostVos2Add: List<HostVo>
		= hosts2Add.toHostVos(connection)
	val vms2Add: List<Vm>
		= SystemServiceHelper.getInstance().findAllVms(connection, "cluster=${name()}")
	val workloadVos2Add: List<WorkloadVmVo>
		= vms2Add.toWorkloadVmVos(connection)

	return ClusterVo(
		if (idPresent()) id() else ""				// id
		, if (namePresent()) name() else ""			// name
		, if (cpuPresent() &&
			cpu().typePresent()) cpu().type() else ""		// cpuType
		, hostVos2Add		// hosts
		, workloadVos2Add	// vms
	)
}

fun List<Cluster>.toClusterVos(connection: Connection): List<ClusterVo>
	= this.map { it.toClusterVo(connection) }


data class ConsolidationVo(
	var hostId: String
	, var hostName: String
	, var vmId: String
	, var vmName: String
	, var fromHostId: String
	, var fromHostName: String
	, var description: String
)

fun List<HostVo>.findHostId(vmId: String): String {
	var hostId = ""
	for (host in this) {
		val vmInfo = host.vms
		val vmIdFound = vmInfo.firstOrNull { vmi -> vmi.id == vmId }
		hostId = vmIdFound?.id ?: ""
	}
	return hostId
}

fun List<HostVo>.findHostname(vmId: String): String {
	var hostname = ""
	for (host in this) {
		val vmInfo = host.vms
		val vmIdFound = vmInfo.firstOrNull { vmi -> vmi.id == vmId }
		hostname = vmIdFound?.hostName ?: ""
	}
	return hostname
}

fun VmVo.toConsolidationVo(vmDesc: String = "", host: HostVo? = null): ConsolidationVo = ConsolidationVo(
	host?.id ?: this.hostId,
	host?.name ?: hostName,
	id,
	name,
	"",
	"",
	if (placementPolicy == "pinned") "호스트에 고정된 가상머신입니다." else vmDesc
)

fun VmVo.toConsolidationVoPostMigration(hosts: List<HostVo>, srcVmId: String): ConsolidationVo = ConsolidationVo(
	hosts.findHostId(srcVmId),
	hosts.findHostname(srcVmId),
	id,
	name,
	hostId,
	hostName,
	"",
)

fun VmVo.toConsolidationVoWithSpecificHost(host: HostVo) : ConsolidationVo = ConsolidationVo(
	host.id,
	host.name,
	id,
	name,
	hostId,
	hostName,
	""
)





data class HistoryVo(
	var historyDatetime: String
	, var cpuUsagePercent: Int
	, var memoryUsagePercent: Int
	, var memoryUsage: BigDecimal
)
data class HostVo(
	var id: String
	, var name: String
	, var status: String
	, var clusterId: String
	, var cores: Int
	, var sockets: Int
	, var threads: Int
	, var cpuVmUsed: Int
	, var cpuCurrentUser: Double
	, var cpuCurrentSystem: Double
	, var cpuCurrentIdle: Double
	, var maxSchedulingMemory: BigInteger
	, var memoryTotal: BigDecimal
	, var memoryUsed: BigDecimal
	, var memoryFree: BigDecimal
	, var vms: List<VmVo>
)

fun Host.toHostVo(connection: Connection): HostVo {
	val vms: List<Vm>
		= SystemServiceHelper.getInstance().findAllVms(connection, "Hosts.name=${name()}").filter {
			it.cpuPresent() && it.cpu().topologyPresent()
		}
	val vmVos: List<VmVo>
		= vms.toVmVos(connection)
	val cpuVmUsed: Int
		= vmVos.map { it.cores + it.sockets + it.threads }.reduce { acc, i -> acc + i }
	val stats
			= SystemServiceHelper.getInstance().findAllStatisticsFromHost(connection, id())
	stats.forEach(Consumer { _: Statistic? ->

	})

	return HostVo(
		id(),
		name(),
		status().value(),
		cluster().id(),
		if (cpu().topologyPresent() &&
			cpu().topology().coresAsInteger() != null) cpu().topology().coresAsInteger() else 0,
		if (cpu().topologyPresent() &&
			cpu().topology().socketsAsInteger() != null) cpu().topology().socketsAsInteger() else 0,
		if (cpu().topologyPresent() &&
			cpu().topology().threadsAsInteger() != null) cpu().topology().threadsAsInteger() else 0,
		cpuVmUsed,
		0.0,
		0.0,
		0.0,
		maxSchedulingMemory(),
		BigDecimal.ZERO,
		BigDecimal.ZERO,
		BigDecimal.ZERO,
		vmVos,
	)
}

fun List<Host>.toHostVos(connection: Connection): List<HostVo>
	= this.map { it.toHostVo(connection) }


data class VmVo(
	var id: String = ""
	, var name: String = ""
	, var status: String = ""
	, var hostId: String = ""
	, var hostName: String = ""
	, var cores: Int = -1
	, var sockets: Int = -1
	, var threads: Int = -1
	, var cpuCurrentGuest: Double = -1.0
	, var cpuCurrentHypervisor: Double = -1.0
	, var cpuCurrentTotal: Double = -1.0
	, var memoryInstalled: BigDecimal = BigDecimal.ZERO
	, var memoryUsed: BigDecimal = BigDecimal.ZERO
	, var memoryFree: BigDecimal = BigDecimal.ZERO
	, var memoryBuffered: BigDecimal = BigDecimal.ZERO
	, var memoryCached: BigDecimal = BigDecimal.ZERO
	, var placementPolicy: String = ""
)

fun Vm.toVmVo(connection: Connection) : VmVo {
	val stats
			= SystemServiceHelper.getInstance().findAllStatisticsFromVm(connection, id())
	stats.forEach(Consumer { _: Statistic? ->

	})

	val vo = VmVo(
		id(),
		name(),
		status().value(),
		host().id(),
		host().name(),
		if (cpuPresent() &&
			cpu().topologyPresent() &&
			cpu().topology().coresAsInteger() != null) cpu().topology().coresAsInteger() else 0,
		if (cpuPresent() &&
			cpu().topologyPresent() &&
			cpu().topology().socketsAsInteger() != null) cpu().topology().socketsAsInteger() else 0,
		if (cpuPresent() &&
			cpu().topologyPresent() &&
			cpu().topology().threadsAsInteger() != null) cpu().topology().threadsAsInteger() else 0,
		0.0,
		0.0,
		0.0,
		BigDecimal.ZERO,
		BigDecimal.ZERO,
		BigDecimal.ZERO,
		BigDecimal.ZERO,
		BigDecimal.ZERO,
		placementPolicy().affinity().value()
	)
	return vo
}

fun List<Vm>.toVmVos(connection: Connection): List<VmVo> =
	this.map { it.toVmVo(connection) }

data class WorkloadVmVo(
	var id: String = ""
	, var name: String = ""
	, var status: String = ""
	, var hostId: String? = ""
	, var hostName: String? = ""
	, var cores: Int = 0
	, var sockets: Int = 0
	, var threads: Int = 0
	, var memoryInstalled: BigDecimal = BigDecimal.ZERO
	, var histories: List<HistoryVo> = arrayListOf()
)

fun Vm.toWorkloadVmVo(connection: Connection, jdbcTemplate: JdbcTemplate? = null): WorkloadVmVo {
	val stats: List<Statistic>
		= SystemServiceHelper.getInstance().findAllStatisticsFromVm(connection, id())
	val histories: List<HistoryVo>
		= arrayListOf()

	return WorkloadVmVo(
		if (idPresent()) id() else "",
		if (namePresent()) name() else "",
		if (statusPresent()) status().value() else "",
		if (hostPresent() &&
			host().idPresent()) host().id() else "",
		if (hostPresent() &&
			host().namePresent()) host().name() else "",
		if (cpuPresent() &&
			cpu().topologyPresent() &&
			cpu().topology().coresPresent()) cpu().topology().coresAsInteger() else 0,
		if (cpuPresent() &&
			cpu().topologyPresent() &&
			cpu().topology().socketsPresent()) cpu().topology().socketsAsInteger() else 0,
		if (cpuPresent() &&
			cpu().topologyPresent() &&
			cpu().topology().threadsPresent()) cpu().topology().threadsAsInteger() else 0,
		BigDecimal.ZERO,
		histories
	)
}

fun List<Vm>.toWorkloadVmVos(connection: Connection, jdbcTemplate: JdbcTemplate? = null)
	= this.map { it.toWorkloadVmVo(connection, jdbcTemplate) }

data class WorkloadVo(
	var clusters: List<ClusterVo> = arrayListOf(),
	var cpuThreshold: Int = 0,
	var memoryThreshold: Int = 0,
)

fun SystemPropertiesVo.toWorkloadVo(connection: Connection): WorkloadVo {
	val clusters: List<Cluster>
		= SystemServiceHelper.getInstance().findAllClusters(connection)
	val tgtClusters: List<ClusterVo>
		= clusters.toClusterVos(connection)
	return WorkloadVo(
		tgtClusters,
		cpuThreshold,
		memoryThreshold
	)
}