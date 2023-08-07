package com.itinfo.model

import com.itinfo.SystemServiceHelper
import com.itinfo.dao.ClustersDao
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.builders.*
import org.ovirt.engine.sdk4.internal.containers.*
import org.ovirt.engine.sdk4.services.HostService
import org.ovirt.engine.sdk4.services.NetworkService
import org.ovirt.engine.sdk4.services.SystemService
import org.ovirt.engine.sdk4.types.*

import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

import kotlin.math.pow
import kotlin.math.roundToInt

data class ItInfoNetworkClusterVo(
	var clusterName: String = "",
	var clusterVersion: String = "",
	var required: Boolean = false,
	var status: String = "",
	var usages: List<ItInfoNetworkUsagesVo> = arrayListOf(),
	var clusterDescription: String = "",
	var connect: Boolean = false,
	var clusterId: String = "",
)

fun Cluster.toItInfoNetworkClusterVo(connection: Connection, networkId: String): ItInfoNetworkClusterVo {
	val networks: List<Network>
		= SystemServiceHelper.getInstance().findAllNetworksFromCluster(connection, id())
	val networkFound: Network?
		= networks.firstOrNull { it.id().equals(networkId, ignoreCase = true) }
	val networkUsages: List<NetworkUsage>
		= networkFound?.usages() ?: listOf()
	val networkUsagesVo: List<ItInfoNetworkUsagesVo>
		= listOf()
	val required: Boolean
		= networkFound != null && networkFound.requiredPresent() && networkFound.required()
	val status: String
		= if (networkFound != null && networkFound.statusPresent()) networkFound.status().name else ""

	return ItInfoNetworkClusterVo(
		if (namePresent()) name() else "",
		if (versionPresent() &&
			version().majorPresent()) "${version().major()}.${version().minor()}" else "1.0",
		required,
		status,
		networkUsagesVo,
		if (descriptionPresent()) description() else "",
		networkFound != null,
		id()
	)
}

fun List<Cluster>.toItInfoNetworkClusterVos(connection: Connection, networkId: String): List<ItInfoNetworkClusterVo>
	= this.map { it.toItInfoNetworkClusterVo(connection, networkId) }
data class ItInfoNetworkCreateVo(
	var qoss: List<ItInfoNetworkQosVo> = arrayListOf(),
	var networkName: List<String> = arrayListOf(),
	var clusters: List<ItInfoNetworkClusterVo> = arrayListOf(),
)

data class ItInfoNetworkDnsVo(
	val dnsIp: String
)

data class ItInfoNetworkGroupVo(
	var network: ItInfoNetworkVo,
	var clusters: List<ItInfoNetworkClusterVo>,
	var hosts: List<ItInfoNetworkHostVo>,
	var vms: List<ItInfoNetworkVmVo>,
)

data class ItInfoNetworkHostVo(
	var hostStatus: String = "",
	var hostName: String = "",
	var nicStatus: String = "",
	var nicName: String = "",
	var hostClusterName: String = "",
	var nicSpeed: BigInteger = BigInteger.ZERO,
	var dataCurrentRxBps: BigDecimal = BigDecimal.ZERO,
	var dataCurrentTxBps: BigDecimal = BigDecimal.ZERO,
	var dataTotalRx: BigDecimal = BigDecimal.ZERO,
	var dataTotalTx: BigDecimal = BigDecimal.ZERO,
)

fun Host.toItInfoNetworkHostVo(connection: Connection): ItInfoNetworkHostVo {
	val cluster: List<Cluster>
		= SystemServiceHelper.getInstance().findAllClusters(connection)
	val hostNics: List<HostNic>
		= SystemServiceHelper.getInstance().findNicsFromHost(connection, id())
	return ItInfoNetworkHostVo(
		if (statusPresent()) status().value() else "",
		if (namePresent()) name() else "",
		if (nicsPresent()) "ok" else "none",
		if (nicsPresent() &&
			nics().isNotEmpty()) nics().firstOrNull()?.name() ?: "" else "",
		if (clusterPresent() &&
			cluster().namePresent()) cluster().name() else "",
	)
}

fun List<Host>.toItInfoNetworkHostVos(connection: Connection): List<ItInfoNetworkHostVo>
	= this.map { it.toItInfoNetworkHostVo(connection) }

data class ItInfoNetworkQosVo(
	var name: String = "",
	var id: String = "",
)

data class ItInfoNetworkUsagesVo(
	var usage: String = "",
)

data class ItInfoNetworkVmVo(
	var vmStatus: String,
	var vmName: String,
	var vmCluster: String,
	var ip: String,
	var fqdn: String,
	var linked: String,
	var nicName: String,
	var dataCurrentRxBps: BigDecimal,
	var dataCurrentTxBps: BigDecimal,
	var dataTotalRx: BigDecimal,
	var dataTotalTx: BigDecimal,
)

data class ItInfoNetworkVo(
	var id: String = "",
	var name: String = "",
	var description: String = "",
	var comment: String = "",
	var mtu: String = "",
	var provider: String = "",
	var vlan: String? = "",
	var qos: String = "",
	var qosId: String = "",
	var usage: String = "",
	var label: String = "",
	var cluster: String = "",
	var status: String = "",
	var baseInterface: String = "",
	var usingVmNetwork: Boolean = false,
	var required: Boolean = false,
	var clusters: List<ItInfoNetworkClusterVo> = arrayListOf(),
	var dnss: List<ItInfoNetworkDnsVo> = arrayListOf(),
	var usages: List<ItInfoNetworkUsagesVo> = arrayListOf(),
)

fun Network.toItInfoNetworkVo(connection: Connection): ItInfoNetworkVo {
	val usage: String
		= if (usagesPresent()) usages().firstOrNull { it.name.equals("VM", true) }?.name ?: ""
		else ""
	val vms: List<Vm>
		= SystemServiceHelper.getInstance().findAllVms(connection)
	val clusters: List<Cluster>
		= SystemServiceHelper.getInstance().findAllClusters(connection)
	val dataCenter: DataCenter
		= SystemServiceHelper.getInstance().findAllDataCenters(connection).first()
	val openStackNetworkProviders: List<OpenStackNetworkProvider>
		= SystemServiceHelper.getInstance().findAllOpenStackNetworkProviders(connection);
	val labels: List<NetworkLabel>
		= SystemServiceHelper.getInstance().findAllNetworkLabelsFromNetwork(connection, id())
	val qoss: List<Qos>
		= SystemServiceHelper.getInstance().findAllQossFromDataCenter(connection, dataCenter.id())

	return ItInfoNetworkVo(
		id(),
		if (namePresent()) name() else "",
		if (descriptionPresent()) description() else "",
		if (commentPresent()) comment() else "",
		if (mtuPresent()) mtuAsInteger().toString() else "",
		"",
		if (vlanPresent() &&
			vlan().idPresent()) "${vlan().id()}" else "",
		if (qosPresent())
			qos().id() else "",
		if (qosPresent())
			qos().id() else "",
		usage,
		labels.joinToString { it.name() },
		"",
		"",
		"",
		false,
		if (requiredPresent()) required() else requiredPresent(),
		clusters.toItInfoNetworkClusterVos(connection, id()),
		arrayListOf(),
		arrayListOf(),
	)
}

fun List<Network>.toItInfoNetworkVos(connection: Connection): List<ItInfoNetworkVo>
	= this.map { it.toItInfoNetworkVo(connection) }

data class ClusterCreateVo(
	var id: String = "",
	var name: String = "",
	var description: String = "",
	var comment: String = "",
	var cpuArchitecture: String = "",
	var cpuType: String = "",
	var switchType: String = "",
	var firewallType: String = "",
	var networkId: String = "",
	var networkProviderId: String = "",
)

fun ClusterCreateVo.toCluster(connection: Connection): Cluster {
	val dataCenter: DataCenter
		= SystemServiceHelper.getInstance().findAllDataCenters(connection).first()
	val networkProvider: OpenStackNetworkProvider
		= SystemServiceHelper.getInstance().findOpenStackNetworkProvider(connection, networkProviderId)
	val network: Network
		= SystemServiceHelper.getInstance().findNetwork(connection, networkId)

	return ClusterBuilder()
		.name(name)
		.description(description)
		.comment(comment)
		.cpu(CpuBuilder()
			.architecture(Architecture.fromValue(cpuArchitecture))
			.type(cpuType)
		)
		.dataCenter(dataCenter)
		.managementNetwork(network)
		.firewallType(FirewallType.fromValue(firewallType))
		.switchType(SwitchType.fromValue(switchType))
		.externalNetworkProviders(networkProvider)
		.build()
}

fun Cluster.toClusterCreateVo(connetion: Connection): ClusterCreateVo {
	val externalProvider: ExternalProvider? =
		SystemServiceHelper.getInstance().findExternalNetworkProviders(connetion, id()).firstOrNull()
	val network: Network?
		= SystemServiceHelper.getInstance().findAllNetworksFromCluster(connetion, id()).firstOrNull()

	return ClusterCreateVo(
		if (idPresent()) id() else "",
		if (namePresent()) name() else "",
		if (descriptionPresent()) description() else "",
		if (commentPresent()) comment() else "",
		if (cpuPresent() &&
			cpu().architecturePresent()) cpu().architecture().name.toLowerCase() else "",
		if (cpuPresent() &&
			cpu().typePresent()) cpu().type() else "",
		if (switchTypePresent()) switchType().name.toLowerCase() else "",
		if (firewallTypePresent()) firewallType().name.toLowerCase() else "",
		if (network?.idPresent() == true) network.id() else "",
		if (externalProvider?.idPresent() == true) externalProvider.id() else ""
	)
}

data class ClusterVo(
	var id: String = "",
	var name: String = "",
	var description: String = "",
	var cpuType: String = "",
	var cpuImage: String = "",
	var hostCnt: Int = 0,
	var vmCnt: Int = 0,
	var clusterNetworkList: List<NetworkVo> = arrayListOf(),
	var hostsUp: Int = 0,
	var hostsDown: Int = 0,
	var vmsUp: Int = 0,
	var vmsDown: Int = 0,
	var memoryTotal: BigDecimal = BigDecimal.ZERO,
	var memoryUsed: BigDecimal = BigDecimal.ZERO,
	var memoryFree: BigDecimal = BigDecimal.ZERO,
	var memoryUsagePercent: BigDecimal = BigDecimal.ZERO,
	var ksmCpuUsagePercent: BigDecimal = BigDecimal.ZERO,
	var userCpuUsagePercent: BigDecimal = BigDecimal.ZERO,
	var systemCpuUsagePercent: BigDecimal = BigDecimal.ZERO,
	var idleCpuUsagePercent: BigDecimal = BigDecimal.ZERO,
	var cpuUsage: List<List<String>> = arrayListOf(),
	var memoryUsage: List<List<String>> = arrayListOf(),
	var network: NetworkVo? = null,
	var hostsDetail: List<HostDetailVo> = arrayListOf(),
	var vmSummaries: List<VmSummaryVo> = arrayListOf(),
	var usageVos: List<UsageVo> = arrayListOf(),
)

fun Cluster.toClusterVo(c: Connection, clustersDao: ClustersDao?): ClusterVo {
	val network: List<Network>
		= SystemServiceHelper.getInstance().findAllNetworksFromCluster(c, id())
	val networkVos: List<NetworkVo>
		= network.toNetworkVos()

	val hosts: List<Host>
		= SystemServiceHelper.getInstance().findAllHosts(c).filter { it.cluster().id() == id() }
	val hostCnt: Int = hosts.size
	val hostsUp: Int = hosts.filter { "up".equals(it.status().name, true) }.size
	val hostsDown: Int = hostCnt - hostsUp

	val vms: List<Vm>
		= SystemServiceHelper.getInstance().findAllVms(c).filter { it.cluster().id() == id() }
	val vmCnt: Int = vms.size
	val vmsUp: Int = vms.filter { "up".equals(it.status().name, true) }.size
	val vmsDown: Int = vmCnt - vmsUp

	val hostDetails: List<HostDetailVo>
		= hosts.filter { it.cluster().id() == id() }.map { it.toHostDetailVo(c, clustersDao) }
	val vmSummaries: List<VmSummaryVo>
		= vms.toVmSummaryVos(c, clustersDao)

	val ids: List<String>
		= hostDetails.map { it.id }
	val hostUsageVos: List<HostUsageVo>
		= if (ids.isEmpty()) listOf() else clustersDao?.retrieveClusterUsage(ids) ?: listOf()

	return ClusterVo(
		if (idPresent()) id() else "",
		if (namePresent()) name() else "",
		if (descriptionPresent()) description() else "",
		if (cpuPresent() &&
			cpu().typePresent()) cpu().type() else "",
		if (cpuPresent() &&
			cpu().architecturePresent()) cpu().architecture().name else "",
		hostCnt,
		vmCnt,
		networkVos,
		hostsUp,
		hostsDown,
		vmsUp,
		vmsDown,
		BigDecimal.ZERO,
		BigDecimal.ZERO,
		BigDecimal.ZERO,
		BigDecimal.ZERO,
		BigDecimal.ZERO,
		BigDecimal.ZERO,
		BigDecimal.ZERO,
		BigDecimal.ZERO,
		arrayListOf(),
		arrayListOf(),
		networkVos.firstOrNull(),
		hostDetails,
		vmSummaries,
		hostUsageVos.toUsageVosWithHostUsage()
	)
}

fun List<Cluster>.toClusterVos(connection: Connection, clustersDao: ClustersDao?): List<ClusterVo>
	= this.map { it.toClusterVo(connection, clustersDao) }

fun Cluster.toClusterVo4VmCreate(connection: Connection): ClusterVo {
	val clusterNetworks:List<NetworkVo>
		= SystemServiceHelper.getInstance().findAllNetworksFromCluster(connection, id()).toNetworkVos()
	return this.toClusterVo(connection, null).apply {
		clusterNetworkList = clusterNetworks
	}
}

fun List<Cluster>.toClusterVos4VmCreate(connection: Connection): List<ClusterVo> =
	this.map { it.toClusterVo4VmCreate(connection) }

data class CpuProfileVo(
	var id: String = "",
	var name: String = "",
	var shares: Int = 0,
)

fun CpuProfile.toCpuProfileVo(shares: Int = 0): CpuProfileVo = CpuProfileVo(
	id(),
	name(),
	shares
)

fun List<CpuProfile>.toCpuProfileVos(): List<CpuProfileVo> =  this.map { it.toCpuProfileVo() }

data class DashboardTopVo(
	var vmCpuKey: List<String>,
	var vmCpuVal: List<String>,
	var vmMemoryKey: List<String>,
	var vmMemoryVal: List<String>,
	var hostCpuKey: List<String>,
	var hostCpuVal: List<String>,
	var hostMemoryKey: List<String>,
	var hostMemoryVal: List<String>,
)

fun List<VmVo>.toDashboardTopVo(): DashboardTopVo = DashboardTopVo(
	toVmCpuKeys(),
	toVmCpuVals(),
	toVmMemoryKeys(),
	toVmMemoryVals(),
	arrayListOf(),
	arrayListOf(),
	arrayListOf(),
	arrayListOf()

)

fun List<VmVo>.toVmCpuParamMap(): Map<String, Int> {
	val vmCpuParamMap: MutableMap<String, Int> = mutableMapOf()
	this.forEach { vmCpuParamMap[it.name] = it.cpuUsage[0][1].toInt() }
	return vmCpuParamMap
}
fun List<VmVo>.toVmCpuKeys(): List<String> = this.toVmCpuParamMap().keys.toList().sortedBy { it }
fun List<VmVo>.toVmCpuVals(): List<String> {
	var i = 0;
	val vals = arrayListOf<String>()
	val vmCpuParamMap = this.toVmCpuParamMap()
	for (key in this.toVmCpuKeys()) {
		vals.add(i, vmCpuParamMap[key].toString())
		i++
	}
	return vals
}
fun List<VmVo>.toVmMemoryParamMap(): Map<String, Int> {
	val vmMemoryParamMap: MutableMap<String, Int> = mutableMapOf()
	this.forEach { vmMemoryParamMap[it.name] = it.memoryUsage[0][1].toInt() }
	return vmMemoryParamMap
}
fun List<VmVo>.toVmMemoryVals(): List<String> {
	var i = 0;
	val vals = arrayListOf<String>()
	val vmMemoryParamMap = this.toVmMemoryParamMap()
	for (key in this.toVmMemoryKeys()) {
		vals.add(i, vmMemoryParamMap[key].toString())
		i++
	}
	return vals
}

fun List<VmVo>.toVmMemoryKeys(): List<String> = this.toVmMemoryParamMap().keys.toList().sortedBy { it }


data class DataCenterVo(
	var name: String = "",
	var id: String = "",
	var description: String = "",
	var clusters: Int = 0,
	var hostsUp: Int = 0,
	var hostsDown: Int = 0,
	var storagesActive: Int = 0,
	var storagesUnattached: Int = 0,
	var vmsUp: Int = 0,
	var vmsDown: Int = 0,
	var cpuCurrentUser: Double = 0.0,
	var cpuCurrentSystem: Double = 0.0,
	var cpuCurrentIdle: Double = 0.0,
	var memoryTotal: BigDecimal = BigDecimal.ZERO,
	var memoryUsed: BigDecimal = BigDecimal.ZERO,
	var memoryFree: BigDecimal = BigDecimal.ZERO,
	var storageAvaliable: BigInteger = BigInteger.ZERO,
	var storageUsed: BigInteger = BigInteger.ZERO,
	var cpuUsage: List<List<String>> = arrayListOf(),
	var memoryUsage: List<List<String>> = arrayListOf(),
	var receiveRate: List<List<String>> = arrayListOf(),
	var transmitRate: List<List<String>> = arrayListOf(),
	var disks: List<List<String>> = arrayListOf(),
	var usageVos: List<UsageVo> = arrayListOf(),
	var usageDate: String = "",
	var storageUsageDate: String = "",
	var cpuUsages: List<Int> = arrayListOf(),
	var memoryUsages: List<Int> = arrayListOf(),
	var transitUsages: List<Int> = arrayListOf(),
	var receiveUsages: List<Int> = arrayListOf(),
	var storageUsages: List<Int> = arrayListOf(),
	var totalcpu: Int = 0,
	var usingcpu: Int = 0,
) {
	companion object {
		fun simpleSetup(c: Connection): DataCenterVo {
			val clusters: List<Cluster>
				= SystemServiceHelper.getInstance().findAllClusters(c)

			val hostsDown: List<Host>
				= SystemServiceHelper.getInstance().findAllHosts(c, "status!=up")
			val hostsUp: List<Host>
				= SystemServiceHelper.getInstance().findAllHosts(c, "status=up")

			val vmsDown: List<Vm>
				= SystemServiceHelper.getInstance().findAllVms(c, "status!=up")
			val vmsUp: List<Vm>
				= SystemServiceHelper.getInstance().findAllVms(c, "status=up")

			val storageDomainsUnattached: List<StorageDomain>
				= SystemServiceHelper.getInstance().findAllStorageDomains(c, "status=unattached")
			val storageDomainsUnattachedCnt: Int
				= storageDomainsUnattached.filter { it.type().name == "DATA" }.size
			val storageDomainsActive: List<StorageDomain>
				= SystemServiceHelper.getInstance().findAllStorageDomains(c, "status=active")
			val storageDomainsActiveCnt: Int
				= storageDomainsActive.filter { it.type().name == "DATA" }.size

			val events: List<Event>
				= SystemServiceHelper.getInstance().findAllEvents(c, "time>today")

			val statsWithinHost: List<List<Statistic>>
				= hostsUp.map { SystemServiceHelper.getInstance().findAllStatisticsFromHost(c, it.id()) }
			val memoryTotal: Int
				= statsWithinHost.flatten().filter { it.name() == "memory.total" }.sumBy { it.values().firstOrNull()?.datum()?.toInt() ?: 0 }
			val memoryUsed: Int
				= statsWithinHost.flatten().filter { it.name() == "memory.used" }.sumBy { it.values().firstOrNull()?.datum()?.toInt() ?: 0 }
			val memoryFree: Int
				= statsWithinHost.flatten().filter { it.name() == "memory.free" }.sumBy { it.values().firstOrNull()?.datum()?.toInt() ?: 0 }
			val cpuCurrentUser: Int
				= statsWithinHost.flatten().filter { it.name() == "cpu.current.user" }.sumBy { it.values().firstOrNull()?.datum()?.toInt() ?: 0 }
			val cpuCurrentSystem: Int
				= statsWithinHost.flatten().filter { it.name() == "cpu.current.system" }.sumBy { it.values().firstOrNull()?.datum()?.toInt() ?: 0 }
			val cpuCurrentIdle: Int
				= statsWithinHost.flatten().filter { it.name() == "cpu.current.idle" }.sumBy { it.values().firstOrNull()?.datum()?.toInt() ?: 0 }
			// if (stat.name() == "ksm.cpu.current")

			val sumTotalCpu: Int = hostsUp.sumOf { host ->
				host.cpu().topology().cores().toInt() *
				host.cpu().topology().sockets().toInt() *
				host.cpu().topology().threads().toInt()
			}

			val vmTopologyWithinHost: List<List<Int>>
				= hostsUp.map {
					SystemServiceHelper.getInstance().findAllVms(c, "host=${it.name()}").map { vm ->
						vm.cpu().topology().cores().toInt() *
						vm.cpu().topology().sockets().toInt() *
						vm.cpu().topology().threads().toInt()
					}
				}
			val usingCpu: Int = vmTopologyWithinHost.flatten().sum()

			val interfaceIds: List<List<String>>
				= hostsUp.map {
					SystemServiceHelper.getInstance().findNicsFromHost(c, it.id()).map { nic ->
						nic.id()
					}
				}

			return DataCenterVo(
				"",
				"",
				"",
				clusters.size,
				hostsUp.size,
				hostsDown.size,
				storageDomainsActiveCnt,
				storageDomainsUnattachedCnt,
				vmsUp.size,
				vmsDown.size,

			)
		}
	}
}

fun DataCenter.toDataCenterVo(c: Connection): DataCenterVo {
	val clusters: List<Cluster>
		= SystemServiceHelper.getInstance().findAllClusters(c)
	val clusterSize: Int
		= if (clustersPresent()) clusters().size else clusters.size

	return DataCenterVo(
		if (namePresent()) name() else "",
		id(),
		if (descriptionPresent()) description() else "",
		clusterSize,
	)
}

fun List<DataCenter>.toDataCenterVos(c: Connection): List<DataCenterVo>
	= this.map { it.toDataCenterVo(c) }

data class DiskCreateVo(
	var name: String,
	var size: String,
	var format: String,
	var virtualSize: BigInteger,
	var description: String,
	var bootable: Boolean,
	var wipeAfterDelete: Boolean,
	var shareable: Boolean,
	var storageDomainId: String,
	var diskProfileId: String,
	var dataCenterName: String,
	var dataCenterId: String,
	var usingHostName: String,
	var usingHostId: String,
	var diskId: String,
	var storageDomainName: String,
	var diskProfileName: String,
	var lunId: String,
	var hostId: String,
)

fun DiskCreateVo.toDisk(c: Connection): Disk {
	val storageDomain: StorageDomain
		= SystemServiceHelper.getInstance().findStorageDomain(c, storageDomainId)

	return DiskBuilder()
			.name(name)
			.description(description)
			.format(if (shareable) DiskFormat.RAW else DiskFormat.COW)
			.shareable(shareable)
			.wipeAfterDelete(wipeAfterDelete)
			.provisionedSize(
				BigInteger.valueOf(size.toLong()).multiply(BigInteger.valueOf(2L).pow(30))
			)
			.storageDomain(storageDomain)
			.build()
}

fun DiskCreateVo.toDisk4Lun(c: Connection): Disk {
	val luns: List<HostStorage>
		= SystemServiceHelper.getInstance().findAllStoragesFromHost(c, hostId)

	val logicalUnits: List<LogicalUnit>
		= luns.filter { it.id() == lunId }.map {
			LogicalUnitBuilder()
				.id(lunId)
				.lunMapping(it.logicalUnits().first().lunMapping())
				.productId(it.logicalUnits().first().productId())
				.serial(it.logicalUnits().first().serial())
				.size(it.logicalUnits().first().size())
				.vendorId(it.logicalUnits().first().vendorId())
				.build()
		}

	val lunStorage: HostStorage
		= HostStorageBuilder()
			.host(HostBuilder().id(hostId).build())
			.type(StorageType.FCP)
			.logicalUnits(logicalUnits)
			.build()

	return DiskBuilder()
		.alias(name)
		.description(description)
		.shareable(shareable)
		.lunStorage(lunStorage)
		.build()
}


data class DiskMigrationVo(
	var migrationType: String,
	var sourceStorageDomainId: String,
	var targetStorageDomainId: String,
	var targetDiskName: String,
	var disk: DiskVo,
)

data class DiskProfileVo(
	var storageDomainId: String = "",
	var id: String,
	var name: String,
)

fun DiskProfile.toDiskProfileVo(storageDomainId: String = ""): DiskProfileVo = DiskProfileVo(
	storageDomainId,
	id(),
	name(),
)

fun List<DiskProfile>.toDiskProfileVos(connection: Connection? = null): List<DiskProfileVo> {
	val systemService: SystemService? = connection?.systemService()
	return this.map {
		it.toDiskProfileVo(
			if (systemService != null) systemService.diskProfilesService().diskProfileService(it.id()).get().send().profile().storageDomain().id()
			else						""
		)
	}
}

data class DiskVo(
	var id: String = "",
	var name: String = "",
	var virtualSize: String = "",
	var actualSize: String = "",
	var format: String = "",
	var wipeAfterDelete: Boolean = false,
	var bootable: Boolean = false,
	var sharable: Boolean = false,
	var readOnly: Boolean = false,
	var passDiscard: Boolean = false,
	var attachedTo: String = "",
	var storageDomainId: String = "",
	var storageDomainName: String = "",
	var diskProfileId: String = "",
	var diskInterface: String = "",
	var alignment: String = "",
	var status: String = "",
	var type: String = "",
	var description: String = "",
	var snapshotId: String = "",
	var lunId: String = "",
	var hostId: String = "",
	var diskId: String = "",
	var storageType: String = "",
	var cdate: String = "",
)

fun Disk.toDiskVo(
	connection: Connection,
	diskAttachment: DiskAttachment? = null,
): DiskVo {
	val storageDomains: List<StorageDomain>
		= SystemServiceHelper.getInstance().findAllStorageDomains(connection)

	val virtualSize: String
		= if (storageTypePresent() &&
			storageType() == DiskStorageType.IMAGE &&
			contentTypePresent() &&
			contentType() == DiskContentType.DATA &&
			provisionedSizePresent())
			"${(provisionedSize().toDouble() / 1024.0.pow(3.0) * 100.0).roundToInt() / 100.0} GiB"
		else if (storageTypePresent() &&
			storageType() == DiskStorageType.LUN &&
			contentTypePresent() &&
			contentType() == DiskContentType.DATA &&
			lunStoragePresent() &&
			lunStorage().logicalUnitsPresent() &&
			lunStorage().logicalUnits().isNotEmpty())
			"${lunStorage().logicalUnits().first().size().toDouble() / 1024.0.pow(3.0)}"
		else
			"-1 GiB"
	val actualSize: String
		= if (storageTypePresent() && storageType() == DiskStorageType.IMAGE &&
			contentTypePresent() && contentType() == DiskContentType.DATA &&
			actualSizePresent()) "${(actualSize().toDouble() / 1024.0.pow(3.0) * 100.0).roundToInt() / 100.0} GiB"
		else if (storageTypePresent() && storageType() == DiskStorageType.LUN &&
			contentTypePresent() && contentType() == DiskContentType.DATA &&
			lunStoragePresent() &&
			lunStorage().logicalUnitsPresent() &&
			lunStorage().logicalUnits().isNotEmpty())
			"${lunStorage().logicalUnits().first().size().toDouble() / 1024.0.pow(3.0)}"
		else
			"-1 GiB"

	val storageDomain: StorageDomain? = storageDomains.firstOrNull {
			it.type() == StorageDomainType.DATA &&
				SystemServiceHelper.getInstance().findAllDisksFromStorageDomain(connection, it.id()).any { disk ->
			it.id() == disk.id()
		}
	}
	val storageDomainId: String
		= if (storageDomain?.idPresent() == true) storageDomain.id() ?: "" else ""
	val storageDomainName: String
		= if (storageDomain?.namePresent() == true) storageDomain.name() ?: "" else ""
	val lunId: String
		= if (storageTypePresent() && storageType() == DiskStorageType.LUN &&
		contentTypePresent() && contentType() == DiskContentType.DATA &&
		lunStoragePresent() &&
		lunStorage().logicalUnitsPresent() &&
		lunStorage().logicalUnits().isNotEmpty()) lunStorage().logicalUnits().first().id() else ""

	return DiskVo(
		id(),
		if (namePresent()) name() else "",
		virtualSize,
		actualSize,
		if (formatPresent()) format().value() else "",
		if (wipeAfterDeletePresent()) wipeAfterDelete() else false,
		if (diskAttachment == null || !diskAttachment.bootablePresent()) bootable() else diskAttachment.bootable(),
		shareable(),
		if (diskAttachment == null || !diskAttachment.readOnlyPresent()) readOnly() else diskAttachment.readOnly(),
		if (diskAttachment == null || !diskAttachment.passDiscardPresent()) false else diskAttachment.passDiscard(),
		"",
		storageDomainId,
		storageDomainName,
		"",
		if (diskAttachment == null || !diskAttachment.interface_Present()) "" else diskAttachment.interface_().value(),
		"",
		if (statusPresent()) status().value() else "ok",
		if (storageTypePresent()) storageType().value().toUpperCase() else "",
		if (descriptionPresent()) description() else "",
		if (snapshotPresent() &&
			snapshot().idPresent()) snapshot().id() else "",
		lunId,
		imageId(),
	)
}

fun List<Disk>.toDiskVos(
	connection: Connection,
	diskAttachmentIds: List<String> = arrayListOf(),
): List<DiskVo> = if (diskAttachmentIds.isEmpty()) this.map {
	it.toDiskVo(connection)
}.sortedBy {
	it.name
} else this.filter {
	diskAttachmentIds.contains(it.id())
}.map {
	it.toDiskVo(connection)
}.sortedBy {
	it.name
}

data class EncryptionVo(
	var inputStr: String,
)

data class EventVo(
	var id: String = "",
	var correlationId: String = "",
	var code: BigInteger = BigInteger.ZERO,
	var origin: String = "",
	var description: String = "",
	var time: Date? = null,
	var severity: String = "",
)

fun Event.toEventVo(): EventVo = EventVo(
	id(),
	if (correlationIdPresent()) correlationId() else "",
	if (codePresent()) code() else BigInteger.ZERO,
	if (originPresent()) origin() else "",
	if (descriptionPresent()) description() else "",
	if (timePresent()) time() else null,
	if (severityPresent()) severity().value() else "",
)
fun List<Event>.toEventVos(): List<EventVo> = this.map { it.toEventVo() }

fun List<Event>.toEventVos4Vm(vmId: String) = this.filter { it.vmPresent() && it.vm().id() == vmId }.toEventVos()

data class FenceAgentVo(
	var id: String = "",
	var address: String = "",
	var username: String = "",
	var password: String = "",
	var type: String = "",
	var option: String = "",
)

fun Agent.toFenceAgentVo(): FenceAgentVo = FenceAgentVo(
	id(),
	if (addressPresent()) address() else "",
	if (usernamePresent()) username() else "",
	if (passwordPresent()) password() else "",
	if (typePresent()) type() else "",
	if (optionsPresent()) options().joinToString { "," } else ""
)

fun FenceAgentVo.toAgent(): Agent = AgentBuilder()
	.address(address)
	.username(username)
	.password(password)
	.type(type)
	.encryptOptions(false)
	.order(1)
	.build()

data class HostCreateVo(
	var id: String = "",
	var clusterId: String = "",
	var name: String = "",
	var comment: String = "",
	var description: String = "",
	var status: String = "",
	var networkProviderId: String = "",
	var powerManagementEnabled: Boolean = false,
	var hostEngineEnabled: Boolean = false,
	var ssh: SshVo? = null,
	var fenceAgent: FenceAgentVo? = null,
)

fun Host.toHostCreateVo(c: Connection, hostId: String, hostHas: List<HostHaVo>): HostCreateVo {
	val agents: List<Agent>
		= SystemServiceHelper.getInstance().findAllFenceAgentsFromHost(c, hostId)
	val powerManagementEnabled: Boolean
		= if (powerManagementPresent() &&
		powerManagement().enabledPresent()) powerManagement().enabled() else false
	val sshVo: SshVo = toSshVo()
	val fenceAgentVo: FenceAgentVo?
		= if (agents.isNotEmpty() && powerManagementEnabled) agents.firstOrNull()?.toFenceAgentVo() else null
	return HostCreateVo(
		hostId,
		if (clusterPresent()) cluster().id() else "",
		if (namePresent()) name() else "",
		if (commentPresent()) comment() else "",
		if (descriptionPresent()) description() else "",
		if (statusPresent()) status().value() else "",
		"",
		powerManagementEnabled,
hostHas.isEmpty() && hostHas.any { id() == it.hostId },
		sshVo,
		fenceAgentVo
	)
}

data class HostDetailVo(
	var id: String = "",
	var name: String = "",
	var description: String = "",
	var comment: String = "",
	var address: String = "",
	var status: String = "",
	var clusterName: String = "",
	var clusterId: String = "",
	var powerManagementEnabled: Boolean = false,
	var vmsCnt: Int = 0,
	var vmsUpCnt: Int = 0,
	var vmsDownCnt: Int = 0,
	var memoryTotal: BigDecimal = BigDecimal.ZERO,
	var memoryUsed: BigDecimal = BigDecimal.ZERO,
	var memoryFree: BigDecimal = BigDecimal.ZERO,
	var swapTotal: BigDecimal = BigDecimal.ZERO,
	var swapUsed: BigDecimal = BigDecimal.ZERO,
	var swapFree: BigDecimal = BigDecimal.ZERO,
	var ksmCpuUsagePercent: BigDecimal = BigDecimal.ZERO,
	var userCpuUsagePercent: BigDecimal = BigDecimal.ZERO,
	var systemCpuUsagePercent: BigDecimal = BigDecimal.ZERO,
	var idleCpuUsagePercent: BigDecimal = BigDecimal.ZERO,
	var bootTime: BigDecimal = BigDecimal.ZERO,
	var hwManufacturer: String = "",
	var hwProductName: String = "",
	var cpuType: String = "",
	var cpuName: String = "",
	var cpuCores: BigInteger = BigInteger.ZERO,
	var cpuSockets: BigInteger = BigInteger.ZERO,
	var cpuThreads: BigInteger = BigInteger.ZERO,
	var cpuTotal: Int = 0,
	var osVersion: String = "",
	var osInfo: String = "",
	var kernelVersion: String = "",
	var haConfigured: Boolean = false,
	var haScore: String = "",
	var hostSw: HostSwVo? = null,
	var cpuUsage: List<List<String>> = arrayListOf(),
	var memoryUsage: List<List<String>> = arrayListOf(),
	var hostLastUsage: HostUsageVo? = null,
	var hostUsageList: List<HostUsageVo> = arrayListOf(),
	var vmSummaries: List<VmSummaryVo> = arrayListOf(),
	var hostNicsLastUsage: List<NicUsageVo> = arrayListOf(),
	var hostNicsUsageApi: List<NicUsageApiVo> = arrayListOf(),
	var netAttachment: List<NetworkAttachmentVo> = arrayListOf(),
	var sshVo: SshVo? = null,
	var lunVos: List<LunVo> = arrayListOf(),
	var usageVos: List<UsageVo> = arrayListOf(),
)

fun Host.toHostDetailVo(c: Connection, clustersDao: ClustersDao?): HostDetailVo {

	val vms: List<Vm>
		= SystemServiceHelper.getInstance().findAllVms(c, "host=${name()}")
	val cntUp: Int = vms.filter { it.statusPresent() && "up".equals(it.status().value(), true) }.size
	val cntDown: Int = vms.size - cntUp

	val stats: List<Statistic>
		= SystemServiceHelper.getInstance().findAllStatisticsFromHost(c, id())
	val memTot: BigDecimal = stats.first { it.namePresent() && it.name() == "memory.total" }.values().first().datum()
	val memUsed: BigDecimal = stats.first { it.namePresent() && it.name() == "memory.used" }.values().first().datum()
	val memFree: BigDecimal = stats.first { it.namePresent() && it.name() == "memory.free" }.values().first().datum()

	val swapTot: BigDecimal = stats.first { it.namePresent() && it.name() == "swap.total" }.values().first().datum()
	val swapUsed: BigDecimal = stats.first { it.namePresent() && it.name() == "swap.used" }.values().first().datum()
	val swapFree: BigDecimal = stats.first { it.namePresent() && it.name() == "swap.free" }.values().first().datum()

	val ksmCpuUsagePercent: BigDecimal = stats.first { it.namePresent() && it.name() == "ksm.cpu.current" }.values().first().datum()
	val userCpuUsagePercent: BigDecimal = stats.first { it.namePresent() && it.name() == "cpu.current.user" }.values().first().datum()
	val systemCpuUsagePercent: BigDecimal = stats.first { it.namePresent() && it.name() == "cpu.current.system" }.values().first().datum()
	val idleCpuUsagePercent: BigDecimal = stats.first { it.namePresent() && it.name() == "cpu.current.idle" }.values().first().datum()

	val bootTime: BigDecimal = stats.first { it.namePresent() && it.name() == "boot.time" }.values().first().datum()

	val cluster: Cluster?
		 = if (clusterPresent()) SystemServiceHelper.getInstance().findCluster(c, cluster().id()) else null

	val cpuCores: BigInteger
		= if (cpuPresent() &&
			cpu().topologyPresent() &&
			cpu().topology().coresPresent()) cpu().topology().cores() else BigInteger.ZERO
	val cpuSockets: BigInteger
			= if (cpuPresent() &&
		cpu().topologyPresent() &&
		cpu().topology().socketsPresent()) cpu().topology().sockets() else BigInteger.ZERO
	val cpuThreads: BigInteger
			= if (cpuPresent() &&
		cpu().topologyPresent() &&
		cpu().topology().threadsPresent()) cpu().topology().threads() else BigInteger.ZERO
	val cpuTotal: Int
		= (cpuCores * cpuSockets * cpuThreads).toInt()

	val hostHas: List<HostHaVo>
		= clustersDao?.retrieveHostHaInfo() ?: listOf()
	val haConfigured: Boolean
		= hostHas.any { it.hostId == id() }
	val haScore: String
		= hostHas.firstOrNull { it.hostId == id() }?.haScore ?: ""

	val hostSw: HostSwVo?
		= clustersDao?.retrieveHostSw(id())
	val hostUsageVos: List<HostUsageVo>
		= clustersDao?.retrieveHostUsage(id()) ?: listOf()

	val hostNics: List<HostNic>
		= SystemServiceHelper.getInstance().findNicsFromHost(c, id())

//	val nicUsage: NicUsageVo?
//		= clustersDao?.retrieveHostNicUsage()

	val usageVos: List<UsageVo>
		= hostUsageVos.toUsageVosWithHostUsage()

	val hostLastUsage: HostUsageVo?
		= clustersDao?.retrieveHostLastUsage(id())

	val hostNicsLastUsage: List<HostNic>
		= SystemServiceHelper.getInstance().findNicsFromHost(c, id())

	return HostDetailVo(
		id(),
		if (namePresent()) name() else "",
		if (descriptionPresent()) description() else "",
		if (commentPresent()) comment() else "",
		if (addressPresent()) address() else "",
		if (statusPresent()) status().value() else "",
		if (clusterPresent() &&
			cluster().namePresent()) cluster().name() else "",
		if (clusterPresent() &&
			cluster().idPresent()) cluster().id() else "",
		if (powerManagementPresent() &&
			powerManagement().enabledPresent()) powerManagement().enabled() else false,
		vms.size,
		cntUp,
		cntDown,
		memTot,
		memUsed,
		memFree,
		swapTot,
		swapUsed,
		swapFree,
		ksmCpuUsagePercent,
		userCpuUsagePercent,
		systemCpuUsagePercent,
		idleCpuUsagePercent,
		bootTime,
		if (hardwareInformationPresent() &&
			hardwareInformation().manufacturerPresent()) hardwareInformation().manufacturer() else "",
		if (hardwareInformationPresent() &&
			hardwareInformation().productNamePresent()) hardwareInformation().productName() else "",
		if (cluster?.cpuPresent() == true) cluster.cpu().type() else "",
		if (cpuPresent() &&
			cpu().namePresent()) cpu().name() else "",
		cpuCores,
		cpuSockets,
		cpuThreads,
		cpuTotal,
		"",
		"",
		"",
		haConfigured,
		haScore,
		hostSw,
		// TODO 상세화 진행 필요 ([HostsServiceImpl.retrieve]
	)
}

data class HostHaVo(
	var hostId: String = "",
	var haScore: String = "",
	var haConfigured: Boolean = false,
	var haActive: Boolean = false,
	var haGlobalMaintenance: Boolean = false,
	var haLocalMaintenance: Boolean = false,
)

data class HostInterfaceVo(
	var historyDatetime: String = "",
	var receiveRatePercent: Int = 0,
	var transmitRatePercent: Int = 0,
	var receivedTotalByte: Int = 0,
	var transmittedTotalByte: Int = 0,
)

data class HostSwVo(
	var hostId: String = "",
	var hostOs: String = "",
	var kernelVersion: String = "",
	var kvmVersion: String = "",
	var vdsmVersion: String = "",
)

data class HostUsageVo(
	var hostId: String = "",
	var hostStatus: String = "",
	var cpuUsagePercent: String = "",
	var memoryUsagePercent: String = "",
	var historyDatetime: String = "",
)

fun HostUsageVo.toUsageVo(): UsageVo {
	val cpuUsages = this.cpuUsagePercent.toInt()
	val memoryUsages = this.memoryUsagePercent.toInt()
	val usageDate = this.historyDatetime
	return UsageVo().apply {
		this.cpuUsages = cpuUsages
		this.memoryUsages = memoryUsages
		this.usageDate = usageDate
	}
}

fun List<HostUsageVo>.toUsageVosWithHostUsage(): List<UsageVo>
	= this.filter { it.cpuUsagePercent.isNotEmpty() || it.memoryUsagePercent.isNotEmpty() } .map { it.toUsageVo() }

data class HostVo(
	var clusterId: String = "",
	var hostId: String = "",
	var hostName: String = "",
	var historyDatetime: String = "",
	var hostStatus: String = "",
	var memoryUsagePercent: Int = 0,
	var cpuUsagePercent: Int = 0,
	var ksmCpuPercent: Int = 0,
	var activeVms: Int = 0,
	var totalVms: Int = 0,
	var totalVmsVcpus: Int = 0,
	var cpuLoad: Int = 0,
	var systemCpuUsagePercent: Int = 0,
	var userCpuUsagePercent: Int = 0,
	var swapUsedMb: Int = 0,
	var ksmSharedMemoryMb: Int = 0,
	var lunVos: List<LunVo> = arrayListOf(),
	var netAttachment: List<NetworkAttachmentVo> = arrayListOf(),
)

fun Host.toHostVo(connection: Connection): HostVo {
	val hostStorages: List<HostStorage>
		= SystemServiceHelper.getInstance().findAllStoragesFromHost(connection, id())
	val networkAttachments: List<NetworkAttachment>
		= SystemServiceHelper.getInstance().findAllNetworkAttachmentsFromHost(connection, id())
	val luns: List<LunVo>
		= hostStorages.toLunVos()
	val networks: List<NetworkAttachmentVo>
		= networkAttachments.toNetworkAttachmentVos()
	return HostVo(
		if (clusterPresent() &&
			cluster().idPresent()) cluster().id() else "",
		if (idPresent()) id() else "",
		if (namePresent()) name() else "",
		"",
		if (statusPresent()) status().value() else "",
		0,
//	if (cpuPresent()) cpu().levelAsInteger() else "",
		0,
		if (ksmPresent() && ksm().enabled()) 0 else 0,
	).apply {
		lunVos = luns
		netAttachment = networks
	}
}

fun List<Host>.toHostVos(connection: Connection): List<HostVo> =
	this.filter {
		"up" == it.status().value()
	}.map { it.toHostVo(connection) }

data class ImageFileVo(
	var id: String = "",
	var name: String = "",
)

data class ImageTransferVo(
	var active: Boolean,
	var comment: String,
	var description: String,
	var direction: ImageTransferDirection,
	var id: String,
	var inactivityTimeout: Int,
	var name: String,
	var phase: ImageTransferPhase,
	var proxyUrl: String,
	var signedTicket: String,
	var transferUrl: String,
)

data class InstanceTypeVo(
	var nics: List<VmNicVo> = arrayListOf(),
	var id: String = "",
	var name: String = "",
	var description: String = "",
	var selectNics: List<VmNicVo> = arrayListOf(),
	var memory: BigInteger = BigInteger.ZERO,
	var maximumMemory: BigInteger = BigInteger.ZERO,
	var virtualSockets: Int = 0,
	var coresPerVirtualSocket: Int = 0,
	var threadsPerCore: Int = 0,
	var affinity: String = "",
	var customMigrationUsed: Boolean = false,
	var customMigrationDowntimeUsed: Boolean = false,
	var customMigration: String = "",
	var customMigrationDowntime: BigInteger = BigInteger.ZERO,
	var autoConverge: String = "",
	var compressed: String = "",
	var highAvailability: Boolean = false,
	var priority: BigInteger = BigInteger.ZERO,
	var watchdogModel: String = "",
	var watchdogAction: String = "",
	var firstDevice: String = "",
	var secondDevice: String = "",
	var physicalMemory: BigInteger = BigInteger.ZERO,
	var memoryBalloon: Boolean = false,
	var ioThreadsEnabled: BigInteger = BigInteger.ZERO,
	var virtioScsiEnabled: Boolean = false,
)

fun InstanceTypeVo.toInstanceType(): InstanceType {
	// TODO: watchdogModel, watchdogAction 적용 방법 찾기
	val bootDevices: MutableList<BootDevice> = arrayListOf()
	bootDevices.add(BootDevice.fromValue(firstDevice))
	if (secondDevice != "none") bootDevices.add(BootDevice.fromValue(secondDevice))

	return InstanceTypeBuilder()
		.display(DisplayBuilder()
			.type(DisplayType.VNC)
			.singleQxlPci(false)
		)
		.name(name)
		.description(description)
		.memory(memory)
		.memoryPolicy(MemoryPolicyBuilder()
			.max(maximumMemory)
		)
		.cpu(CpuBuilder().topology(
			CpuTopologyBuilder()
				.cores(coresPerVirtualSocket)
				.sockets(virtualSockets)
				.threads(threadsPerCore)
			)
		)
		.placementPolicy(
			VmPlacementPolicyBuilder()
				.affinity(VmAffinity.fromValue(affinity))
		)
		.migration(
			if (customMigrationUsed) MigrationOptionsBuilder()
				.autoConverge(InheritableBoolean.fromValue(autoConverge))
				.compressed(InheritableBoolean.fromValue(compressed))
			else null
		)
		.migrationDowntime(
			if (customMigrationUsed && customMigrationDowntimeUsed) customMigrationDowntime
			else BigInteger.ZERO
		)
		.highAvailability(HighAvailabilityBuilder()
			.enabled(highAvailability)
			.priority(priority)
		)
		.os(OperatingSystemBuilder()
			.boot(BootBuilder()
				.devices(bootDevices)
			)
		)
		.virtioScsi(VirtioScsiBuilder()
			.enabled(virtioScsiEnabled)
		)
		.build()
}

fun InstanceType.toInstanceTypeVo(connection: Connection): InstanceTypeVo {
	val nicItemList: List<VnicProfile>
		= SystemServiceHelper.getInstance().findAllVnicProfiles(connection)
	val vnics: MutableList<VmNicVo>
		= nicItemList.toVmNicVos().toMutableList()
	if (vnics.isEmpty()) vnics.addAll(nics().toVmNicVos(connection))

	return InstanceTypeVo(
		vnics,
		id(),
		if (namePresent()) name() else "",
		if (descriptionPresent()) description() else "",
		arrayListOf(),
		if (memoryPolicyPresent() &&
			memoryPolicy().guaranteedPresent()) memoryPolicy().guaranteed() else BigInteger.ZERO,
		if (memoryPolicyPresent() &&
			memoryPolicy().maxPresent()) memoryPolicy().max() else BigInteger.ZERO,
		cpu().topology().socketsAsInteger(),
		cpu().topology().coresAsInteger(),
		cpu().topology().threadsAsInteger(),
		if (placementPolicyPresent() &&
			placementPolicy().affinityPresent()) placementPolicy().affinity().value() else "migratable",
		migration().policyPresent(),
		migrationPresent() &&
				migration().policyPresent() && migrationDowntimePresent(),
		if (migrationPresent() &&
			migration().policyPresent()) migration().policy().id() else "",
		if (migrationPresent() &&
			migration().policyPresent() && migrationDowntimePresent()) migrationDowntime() else BigInteger.ZERO,
		if (migrationPresent() &&
			migration().autoConvergePresent()) migration().autoConverge().value() else "",
		if (migrationPresent() &&
			migration().compressedPresent()) migration().compressed().value() else "",
		highAvailability().enabled(),
		highAvailability().priority(),
		"",
		"",
		if (osPresent() &&
			os().bootPresent() &&
			os().boot().devicesPresent() &&
			os().boot().devices().isNotEmpty()) os().boot().devices()[0].value() else "none",
		if (osPresent() &&
			os().bootPresent() &&
			os().boot().devicesPresent() &&
			os().boot().devices().isNotEmpty() &&
			os().boot().devices().size > 1) os().boot().devices()[1].value() else "none",
		if (memoryPolicyPresent() &&
			memoryPolicy().guaranteedPresent()) memoryPolicy().guaranteed() else BigInteger.ZERO,
		if (memoryPolicyPresent() &&
			memoryPolicy().ballooningPresent()) memoryPolicy().ballooning() else false,
		if (ioPresent() &&
			io().threadsPresent()) io().threads() else BigInteger.ZERO,
		false
	)
}

fun List<InstanceType>.toInstanceTypeVos(connection: Connection): List<InstanceTypeVo>
	= this.map { it.toInstanceTypeVo(connection) }

data class IscsiVo(
	var address: String = "",
	var port: String = "",
	var authAt: String = "",
	var id: String = "",
	var password: String = "",
	var target: String = "",
	var loginAt: Boolean = false
)

data class LunVo(
	var id: String = "",
	var size: String = "",
	var path: String = "",
	var vendor: String = "",
	var productId: String = "",
	var serial: String = "",
	var type: String = "",
	var name: String = "",
	var description: String = "",
	var diskId: String = "",
	var hostId: String = "",
)

fun HostStorage.toLunVo(): LunVo = LunVo(
	logicalUnits()[0].id(),
	logicalUnits()[0].size().toString(),
	logicalUnits()[0].paths().toString(),
	logicalUnits()[0].vendorId(),
	logicalUnits()[0].productId(),
	logicalUnits()[0].serial(),
	type().value(),
	if (namePresent()) name() else "",
	if (descriptionPresent()) description() else "",
	if (logicalUnits()[0].diskIdPresent()) logicalUnits()[0].diskId() else "",
	id(),
)

fun List<HostStorage>.toLunVos(): List<LunVo> = this.map { it.toLunVo() }

data class MacAddressPoolsVo(
	var id: String = "",
	var name: String = "",
	var userName: String = "",
	var authorizationProvider: String = "",
	var nameSpace: String = "",
	var role: String = "",
	var allowDuplicates: Boolean = false,
	var macAdressFrom: String = "",
	var macAddressTo: String = "",
	var description: String = "",
	var ranges: List<MacAddressPoolsVo> = arrayListOf(),
	var from: String = "",
	var to: String = "",
)

fun MacPool.toMacAddressPoolVo(): MacAddressPoolsVo = MacAddressPoolsVo(
	id(),
	name(),
	"",
	"",
	"",
	"",
	allowDuplicates(),
	"",
	"",
	description(),
	arrayListOf(),
	ranges()[0].from(),
	ranges()[0].to()
)

fun List<MacPool>.toMacAddressPoolsVos(): List<MacAddressPoolsVo> = this.map { it.toMacAddressPoolVo() }

data class MessageVo(
	var title: String = "",
	var text: String = "",
	var style: String = "",
) {
	companion object {
		const val INFO: String = "info"
		const val ERROR: String = "error"
		const val WARNING: String = "warning"
		const val SUCCESS: String = "success"

		@JvmStatic
		fun createMessage(type: MessageType, isSuccess: Boolean = false, message: String, reason: String = ""): MessageVo {
			return when(type) {
				MessageType.VIRTUAL_MACHINE_START -> MessageVo(
					title = type.title,
					text = "${type.title} ${if (isSuccess) " 완료($message" else " 실패(가상머신 $message $reason"})",
					style = if (isSuccess) SUCCESS else ERROR
				)
				MessageType.INSTANCE_TYPE_ADD -> MessageVo(
					title = type.title,
					text = "${type.title} ${if (isSuccess) " 완료($message 이 생성되었습니다." else " 실패($message"})",
					style = if (isSuccess) SUCCESS else ERROR
				)
				MessageType.INSTANCE_TYPE_UPDATE -> MessageVo(
					title = type.title,
					text = "${type.title} ${if (isSuccess) " 완료($message 이 수정되었습니다." else " 실패($message"})",
					style = if (isSuccess) SUCCESS else ERROR
				)
				MessageType.TEMPLATE_REMOVE -> MessageVo(
					title = type.title,
					text = "${type.title} ${if (isSuccess) " 완료($message)" else " 실패($reason $message"})",
					style = if (isSuccess) SUCCESS else ERROR
				)
				else -> MessageVo(
					title = type.title,
					text = "${type.title} ${if (isSuccess) " 완료($message" else " 실패(${message}"})",
					style = if (isSuccess) SUCCESS else ERROR
				)
			}
		}
	}
}

enum class MessageType(
	val title: String,
) {
	CLUSTER_ADD("클러스터 생성"),
	CLUSTER_UPDATE("클러스터 수정"),
	CLUSTER_REMOVE("클러스터 삭제"),
	VIRTUAL_MACHINE_START("가상머신 실행"),
	VIRTUAL_MACHINE_STOP("가상머신 정지"),
	VIRTUAL_MACHINE_REBOOT("가상머신 재기동"),
	VIRTUAL_MACHINE_SUSPEND("가상머신 일시정지"),
	VIRTUAL_MACHINE_CREATE("가상머신 생성"),
	VIRTUAL_MACHINE_MODIFY("가상머신 수정"),
	VIRTUAL_MACHINE_COPY("가상머신 복제"),
	VIRTUAL_MACHINE_RELOCATE("가상머신 이동"),
	INSTANCE_TYPE_ADD("인스턴스 유형 생성"),
	INSTANCE_TYPE_UPDATE("인스턴스 유형 수정"),
	TEMPLATE_ADD("탬플릿 생성"),
	TEMPLATE_REMOVE("탬플릿 삭제"),
	MAINTENANCE_START("호스트 유지보수 모드 시작"),
	MAINTENANCE_STOP("호스트 활성 모드 시작"),
	HOST_START("호스트 시작"),
	HOST_RESTART("호스트 재시작"),
	HOST_STOP("호스트 정지"),
	HOST_ADD("호스트 추가"),
	HOST_MODIFY("호스트 수정"),
	HOST_REMOVE("호스트 삭제"),
	HOST_NETWORK_MODIFY("호스트 네트워크 수정"),
	DISK_ADD("디스크 생성"),
	DISK_REMOVE("디스크 삭제"),
	DISK_MOVE("디스크 이동"),
	DISK_COPY("디스크 복사"),
	NETWORK_ADD("네트워크 생성"),
	NETWORK_UPDATE("네트워크 수정"),
	NETWORK_REMOVE("네트워크 삭제"),
	CHANGE_CD_ROM("CD 변경")
}

data class NetworkAttachmentVo(
	var dnsServer: List<String> = arrayListOf(),
	var bootProtocol: String = "",
	var nicAddress: String = "",
	var nicGateway: String = "",
	var nicNetmask: String = "",
	var nicNetworkName: String = "",
	var nicNetworkId: String = "",
	var hostNicName: String = "",
	var hostNicId: String = "",
	var netHostId: String = "",
	var netHostName: String = "",
	var netAttachmentId: String = "",
)

fun NetworkAttachment.toNetworkAttachmentVo(systemService: SystemService? = null): NetworkAttachmentVo {
	val networkService: NetworkService? =
		systemService?.networksService()?.networkService(hostNic().id())
	val hostService: HostService? =
		systemService?.hostsService()?.hostService(host().id())
	val hostNicId: String = if (hostNicPresent() && hostNic().idPresent()) hostNic().id() else ""

	return NetworkAttachmentVo(
		if (dnsResolverConfigurationPresent()) dnsResolverConfiguration().nameServers() else arrayListOf(),
		if (ipAddressAssignmentsPresent() &&
			ipAddressAssignments()[0].assignmentMethodPresent()) ipAddressAssignments()[0].assignmentMethod().value() else "",
		if (ipAddressAssignmentsPresent() &&
			ipAddressAssignments()[0].ipPresent() &&
			ipAddressAssignments()[0].ip().addressPresent()) ipAddressAssignments()[0].ip().address() else "",
		if (ipAddressAssignmentsPresent() &&
			ipAddressAssignments()[0].ipPresent() &&
			ipAddressAssignments()[0].ip().gatewayPresent()) ipAddressAssignments()[0].ip().gateway() else "",
		if (ipAddressAssignmentsPresent() &&
			ipAddressAssignments()[0].ipPresent() &&
			ipAddressAssignments()[0].ip().netmaskPresent()) ipAddressAssignments()[0].ip().netmask() else "",
		networkService?.get()?.send()?.network()?.name() ?: "",
		if (networkPresent() &&
			network().idPresent()) network().id() else "",
		hostService?.nicsService()?.nicService(hostNicId)?.get()?.send()?.nic()?.name() ?: "",
		hostNicId,
		if (hostPresent() && host().idPresent()) host().id() else "",
		if (namePresent()) name() else "",
		id(),
	)
}

fun List<NetworkAttachment>.toNetworkAttachmentVos(systemService: SystemService? = null): List<NetworkAttachmentVo>
	= this.map { it.toNetworkAttachmentVo(systemService) }

data class NetworkProviderVo(
	var id: String = "",
	var name: String = "",
	var description: String = "",
	var authenticationUrl: String = "",
	var requiresAuthentication: Boolean = false,
	var url: String = "",
	var username: String = "",
	var autoSync: Boolean = false,
	var externalPluginType: String = "",
	var type: String = "",
)

fun OpenStackNetworkProvider.toNetworkProviderVo(): NetworkProviderVo = NetworkProviderVo(
	id(),
	if (namePresent()) name() else "",
	if (descriptionPresent()) description() else "",
	if (authenticationUrlPresent()) authenticationUrl() else "",
	if (requiresAuthenticationPresent()) requiresAuthentication() else requiresAuthenticationPresent(),
	if (urlPresent()) url() else "",
	if (usernamePresent()) username() else "",
	if (autoSyncPresent()) autoSync() else autoSyncPresent(),
	if (externalPluginTypePresent()) externalPluginType() else "",
	if (typePresent()) type().name else "",
)


fun List<OpenStackNetworkProvider>.toNetworkProviderVos(): List<NetworkProviderVo>
	= this.map { it.toNetworkProviderVo() }

data class NetworkVo(
	var id: String = "",
	var name: String = "",
	var description: String = "",
	var comment: String = "",
)

fun Network.toNetworkVo(): NetworkVo = NetworkVo(
	id(),
	name(),
	description() ?: "",
	comment() ?: "",
)

fun List<Network>.toNetworkVos(): List<NetworkVo> = this.map { it.toNetworkVo() }

data class NicUsageApiVo(
	var id: String = "",
	var name: String = "",
	var macAddress: String = "",
	var ipAddress: String = "",
	var dataCurrentRx: BigDecimal = BigDecimal.ZERO,
	var dataCurrentTx: BigDecimal = BigDecimal.ZERO,
	var dataCurrentRxBps: BigDecimal = BigDecimal.ZERO,
	var dataCurrentTxBps: BigDecimal = BigDecimal.ZERO,
	var dataTotalRx: BigDecimal = BigDecimal.ZERO,
	var dataTotalTx: BigDecimal = BigDecimal.ZERO,
	var bondingMode: String = "",
	var bondingModeName: String = "",
	var bonding: List<NicUsageApiVo> = arrayListOf(),
	var networkId: String = "",
	var networkName: String = "",
	var status: String = "",
	var hostId: String = "",
	var hostName: String = "",
	var networkAttachmentId: String = "",
	var checkBonding: Boolean = false,
	var vlan: BigInteger = BigInteger.ZERO,
	var baseInterface: String = "",
	var base: String = "",
	var vlanNetworkList: List<String> = arrayListOf(),
	var nicExNetExist: Boolean = false,
	var insertSlave: Boolean = false,
	var unBondName: String = ""
)

data class NicUsageVo(
	var hostInterfaceId: String = "",
	var hostInterfaceName: String = "",
	var vmInterfaceId: String = "",
	var vmInterfaceName: String = "",
	var receiveRatePercent: String = "",
	var transmitRatePercent: String = "",
	var receivedTotalByte: String = "",
	var transmittedTotalByte: String = "",
	var historyDatetime: String = "",
	var macAddress: String = "",
)

fun Nic.toNicUsageVo(): NicUsageVo {
	return NicUsageVo(

	)
}

fun Nic.toNicLastUsage(): NicUsageVo {
	return NicUsageVo(
		"",
		"",
		"",
		if (namePresent()) name() else "",
	)
}

data class OsInfoVo(
	var name: String = "",
	var id: String = "",
)

fun OperatingSystemInfo.toOsInfoVo(): OsInfoVo = OsInfoVo(
	description(),
	name(),
)

fun List<OperatingSystemInfo>.toOsInfoVos(): List<OsInfoVo> {
	return this.filter { osInfo ->
		!osInfo.name().contains("s390x") && !osInfo.name().contains("ppc64")
	}.map { osInfo ->
		osInfo.toOsInfoVo()
	}.sortedBy { it.name }
}

data class PermissionVo(
	var id: String = "",
	var administrative: Boolean = false,
	var user: String = "",
	var authProvider: String = "",
	var namespace: String = "",
	var role: String = "",
)

fun Permission.toPermissionVo(connection: Connection): PermissionVo {

	val user: User?
		= if (userPresent() &&
			user().idPresent()) SystemServiceHelper.getInstance().findUser(connection, user().id()) else null
	val group: Group?
		= if (groupPresent() &&
			group().idPresent()) SystemServiceHelper.getInstance().findGroup(connection, group().id()) else null
	val role: Role?
		= if (rolePresent() &&
			role().idPresent()) SystemServiceHelper.getInstance().findRole(connection, role().id()) else null

	return PermissionVo(
		if (idPresent()) id() else "",
		if (role?.administrativePresent() == true) role.administrative() else false,
		if (user?.namePresent() == true) "${user.lastName()} ${user.name()} ( ${user.principal()} )"
		else if (group?.namePresent() == true) group().name() else "",
		if (user?.domainPresent() == true &&
			user.domain().idPresent()) user.domain().name() else "",
		if (user?.namespacePresent() == true) user.namespace() else "",
		if (role?.idPresent() == true) role.id() else "",
	)
}



fun List<Permission>.toPermissionVos(connection: Connection): List<PermissionVo>
	= this.map { it.toPermissionVo(connection) }

data class ProviderVo(
	var id: String = "",
	var name: String = "",
	var description: String = "",
	var providerType: String = "",
	var providerUrl: String = "",
)

fun ExternalHostProvider.toProviderVo(): ProviderVo = ProviderVo(
	id(),
	name(),
	description(),
	"외부 공급자",
	url(),
)

fun OpenStackImageProvider.toProviderVo(): ProviderVo = ProviderVo(
	id(),
	name(),
	description(),
	"Openstack Image 공급자",
	url(),
)
fun OpenStackNetworkProvider.toProviderVo(): ProviderVo = ProviderVo(
	id(),
	name(),
	description(),
	"Openstack Network 공급자",
	url(),
)

fun OpenStackVolumeProvider.toProviderVo(): ProviderVo = ProviderVo(
	id(),
	name(),
	description(),
	"Openstack Volume 공급자",
	url(),
)

fun List<ExternalHostProvider>.toProviderVosWithExternalHost(): List<ProviderVo> = this.map { it.toProviderVo() }

fun List<OpenStackImageProvider>.toProviderVosWithOpenStackImage(): List<ProviderVo> = this.map { it.toProviderVo() }
fun List<OpenStackNetworkProvider>.toProviderVosWithOpenStackNetwork(): List<ProviderVo> = this.map { it.toProviderVo() }
fun List<OpenStackVolumeProvider>.toProviderVosWithOpenStackVolume(): List<ProviderVo> = this.map { it.toProviderVo() }


data class QuotaClusterLimitVo(
	var clusterId: String = "",
	var clusterName: String = "",
	var memoryUsage: BigDecimal = BigDecimal.ZERO,
	var memoryLimit: BigDecimal = BigDecimal.ZERO,
	var vCpuUsage: BigInteger = BigInteger.ZERO,
	var vCpuLimit: BigInteger = BigInteger.ZERO,
)

fun QuotaClusterLimit.toQuotaClusterLimitVo(): QuotaClusterLimitVo {
	return QuotaClusterLimitVo(
		if (clusterPresent() &&
			cluster().idPresent()) cluster().id() else "",
		if (clusterPresent() &&
			cluster().namePresent()) cluster().name() else "",
		if (memoryUsagePresent()) memoryUsage() else BigDecimal.ZERO,
		if (memoryLimitPresent()) memoryLimit() else BigDecimal.ZERO,
		if (vcpuUsagePresent()) vcpuUsage() else BigInteger.ZERO,
		if (vcpuLimitPresent()) vcpuLimit()  else BigInteger.ZERO,
	)
}

fun List<QuotaClusterLimit>.toQuotaClusterLimitVos(): List<QuotaClusterLimitVo>
	= this.map { it.toQuotaClusterLimitVo() }

data class QuotaCreateVo(
	var id: String = "",
	var name: String = "",
	var description: String = "",
	var clusterHardLimitPct: BigInteger = BigInteger.ZERO,
	var clusterSoftLimitPct: BigInteger = BigInteger.ZERO,
	var storageHardLimitPct: BigInteger = BigInteger.ZERO,
	var storageSoftLimitPct: BigInteger = BigInteger.ZERO,
	var quotaClusterType: String = "",
	var quotaStorageType: String = "",
	var quotaClusterList: List<QuotaClusterLimitVo> = arrayListOf(),
	var quotaStorageDomainList: List<QuotaStorageLimitVo> = arrayListOf(),
)

fun Quota.toQuotaCreateVo(): QuotaCreateVo = QuotaCreateVo(
	if (idPresent()) id() else "",
	if (namePresent()) name() else "",
	if (descriptionPresent()) description() else "",
	if (clusterHardLimitPctPresent()) clusterHardLimitPct() else BigInteger.valueOf(20),
	if (clusterSoftLimitPctPresent()) clusterSoftLimitPct() else BigInteger.valueOf(80),
	if (storageHardLimitPctPresent()) storageHardLimitPct() else BigInteger.valueOf(50),
	if (storageSoftLimitPctPresent()) storageSoftLimitPct() else BigInteger.valueOf(70),
)

fun QuotaCreateVo.toQuota(): Quota =
	QuotaBuilder().name(name)
		.description(description)
		.clusterHardLimitPct(if (clusterHardLimitPct == BigInteger.ZERO) BigInteger.valueOf(20) else clusterHardLimitPct)
		.clusterSoftLimitPct(if (clusterSoftLimitPct == BigInteger.ZERO) BigInteger.valueOf(80) else clusterSoftLimitPct)
		.storageHardLimitPct(if (storageHardLimitPct == BigInteger.ZERO) BigInteger.valueOf(50) else storageHardLimitPct)
		.storageSoftLimitPct(if (storageSoftLimitPct == BigInteger.ZERO) BigInteger.valueOf(70) else storageSoftLimitPct)
		.build()


data class QuotaStorageLimitVo(
	var storageDomainId: String = "",
	var storageDomainName: String = "",
	var storageUsage: BigDecimal = BigDecimal.ZERO,
	var storageLimit: BigInteger = BigInteger.ZERO,
)

fun QuotaStorageLimit.toQuotaStorageLimitVo(): QuotaStorageLimitVo = QuotaStorageLimitVo(
	if (idPresent()) id() else "",
	if (namePresent()) name() else "",
	if (usagePresent()) usage() else BigDecimal.ZERO,
	if (limitPresent()) limit() else BigInteger.ZERO,
)

fun List<QuotaStorageLimit>.toQuotaStorageLimitVos(): List<QuotaStorageLimitVo>
	= this.map { it.toQuotaStorageLimitVo() }


data class QuotaVo(
	var id: String = "",
	var name: String = "",
	var comment: String = "",
	var description: String = "",
	var clusterHardLimitPct: BigInteger = BigInteger.ZERO,
	var clusterSoftLimitPct: BigInteger = BigInteger.ZERO,
	var storageHardLimitPct: BigInteger = BigInteger.ZERO,
	var storageSoftLimitPct: BigInteger = BigInteger.ZERO,
	var memoryUsageTotal: BigDecimal = BigDecimal.ZERO,
	var memoryLimitTotal: BigDecimal = BigDecimal.ZERO,
	var vCpuUsageTotal: BigInteger? = BigInteger.ZERO,
	var vCpuLimitTotal: BigInteger? = BigInteger.ZERO,
	var storageUsageTotal: BigDecimal? = BigDecimal.ZERO,
	var storageLimitTotal: BigInteger? = BigInteger.ZERO,
	var quotaClusterLimitList: List<QuotaClusterLimitVo> = arrayListOf(),
	var quotaStorageLimitList: List<QuotaStorageLimitVo> = arrayListOf(),
)

fun Quota.toQuotaVo(connection: Connection): QuotaVo {
	val dataCenterId: String
		= SystemServiceHelper.getInstance().findAllDataCenters(connection).first().id()

	val quotaClusterLimits: List<QuotaClusterLimit>
		= SystemServiceHelper.getInstance().findAllQuotaClusterLimitsFromDataCenter(connection, dataCenterId, id())
	val quotaClusterLimitVos: List<QuotaClusterLimitVo>
		= quotaClusterLimits.toQuotaClusterLimitVos()
	val quotaStorageLimits: List<QuotaStorageLimit>
		= SystemServiceHelper.getInstance().findAllQuotaStorageLimitsFromDataCenter(connection, dataCenterId, id())
	val quotaStorageLimitVos: List<QuotaStorageLimitVo>
		= quotaStorageLimits.toQuotaStorageLimitVos()

	val memoryUsageTotal: BigDecimal
		= if (quotaClusterLimitsPresent()) quotaClusterLimits().map {
			if (it.memoryUsagePresent()) it.memoryUsage() else BigDecimal.ZERO
		}.reduce { acc, bigDecimal -> acc.add(bigDecimal) } else BigDecimal.ZERO
	val memoryLimitTotal: BigDecimal
		= if (quotaClusterLimitsPresent())  quotaClusterLimits().map {
			if (it.memoryLimitPresent()) it.memoryLimit() else BigDecimal.ZERO
		}.reduce { acc, bigDecimal -> acc.add(bigDecimal) } else BigDecimal.ZERO

	val vCpuUsageTotal: BigInteger
		= if (quotaClusterLimitsPresent()) quotaClusterLimits().map {
			if (it.vcpuUsagePresent()) it.vcpuUsage() else BigInteger.ZERO
		}.reduce { acc, bigDecimal -> acc.add(bigDecimal) } else BigInteger.ZERO
	val vCpuLimitTotal: BigInteger
		= if (quotaClusterLimitsPresent())  quotaClusterLimits().map {
		if (it.vcpuLimitPresent()) it.vcpuLimit() else BigInteger.ZERO
		}.reduce { acc, bigDecimal -> acc.add(bigDecimal) } else BigInteger.ZERO

	val storageUsageTotal: BigDecimal
		= if (quotaStorageLimitsPresent()) quotaStorageLimits().map {
			if (it.usagePresent()) it.usage() else BigDecimal.ZERO
		}.reduce { acc, bigDecimal -> acc.add(bigDecimal) } else BigDecimal.ZERO
	val storageLimitTotal: BigInteger
		= if (quotaStorageLimitsPresent()) quotaStorageLimits().map {
			if (it.limitPresent()) it.limit() else BigInteger.ZERO
		}.reduce { acc, bigDecimal -> acc.add(bigDecimal) } else BigInteger.ZERO

	return QuotaVo(
		if (idPresent()) id() else "",
		if (namePresent()) name() else "",
		if (commentPresent()) comment() else "",
		if (descriptionPresent()) description() else "",
		if (clusterHardLimitPctPresent()) clusterHardLimitPct() else BigInteger.ZERO,
		if (clusterSoftLimitPctPresent()) clusterSoftLimitPct() else BigInteger.ZERO,
		if (storageHardLimitPctPresent()) storageHardLimitPct() else BigInteger.ZERO,
		if (storageSoftLimitPctPresent()) storageSoftLimitPct() else BigInteger.ZERO,
		memoryUsageTotal,
		memoryLimitTotal,
		vCpuUsageTotal,
		vCpuLimitTotal,
		storageUsageTotal,
		storageLimitTotal,
		quotaClusterLimitVos,
		quotaStorageLimitVos
	)
}

fun List<Quota>.toQuotaVos(connection: Connection): List<QuotaVo>
	= this.map { it.toQuotaVo(connection) }

data class RoleVo(
	var id: String = "",
	var name: String = "",
	var administrative: Boolean = false,
	var mutable: Boolean = false,
)

fun Role.toRoleVo(): RoleVo = RoleVo(
	id(),
	name(),
	administrative(),
	mutable()
)

fun List<Role>.toRoleVos(): List<RoleVo> {
	return this.map { it.toRoleVo() }
}

data class SnapshotVo(
	var vmId: String = "",
	var memoryRestore: Boolean = false,
	var id: String = "",
	var date: Long = 0L,
	var status: String = "",
	var memory: Boolean = false,
	var description: String = "",
	var disks: List<DiskVo> = arrayListOf(),
	var nics: List<VmNicVo> = arrayListOf()
)

fun Snapshot.toSnapshotVo(connection: Connection, vmId: String? = null): SnapshotVo {
	val diskList: List<Disk>
		= SystemServiceHelper.getInstance().findAllSnapshotDisksFromVm(connection, vmId ?: vm().id(), id())
	val disks: List<DiskVo>
		= diskList.toDiskVos(connection)
	val nicList: List<Nic>
		= SystemServiceHelper.getInstance().findAllSnapshotNicsFromVm(connection, vmId ?: vm().id(), id())
	val nics: List<VmNicVo>
		= nicList.toSnapshotVmNicVos()
	return SnapshotVo(
		if (vmPresent() && vm().idPresent()) vm().id() else vmId ?: "",
		false,
		id(),
		date().time,
		snapshotStatus().value(),
		persistMemorystate(),
		description(),
		disks,
		nics,
	)
}

fun List<Snapshot>.toSnapshotVos(connection: Connection): List<SnapshotVo> = this.map { it.toSnapshotVo(connection) }

data class SshVo(
	var id: String = "",
	var password: String = "",
	var address: String = "",
	var port: Int? = null,
	var publicKey: String = "",
)

fun Host.toSshVo(): SshVo = SshVo(
	"root",
	"",
	if (addressPresent()) address() else "",
	if (sshPresent() &&
		ssh().portPresent()) ssh().port().intValueExact() else null,
	"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCbNWOIsBF4qySgLk+6Z194tAatTsGKQELvjfZv9HjciTOkA8+X3p4Ognz74Oi+RWJiHA69BUzTehDw6NMuOEu2cbY+7IrX629N/ohh7ke4+em1BHEbAzJvaDPgzCL85KqRyZURJBerOalc3LruP0jDf4QYPk3+aT/k3D79hMKPPw9NWVeb8d0vfiAUcid0TTeBcWTbHdnk4idS/FtMC5rixIzm9Yy5Z+NDI4s1fadXJ2uWYT53W5dhj4tGVXub2Qm4OTPjevqXMvEkKvW5ZOuRjs2GUdyC3xIuXP6jSInPfxjkcmj2DQlF2fJqTkJ1JvGGnR5iLpagFhrJ9lTFOEyX ovirt-engine"
)

data class StorageDomainCreateVo(
	var id: String = "",
	var name: String = "",
	var description: String = "",
	var domainType: String = "",
	var storageType: String = "",
	var wipeAfterDelete: Boolean = false,
	var discardAfterDelete: Boolean = false,
	var hostId: String = "",
	var path: String = "",
	var iscsi: IscsiVo? = null,
	var importAt: Boolean = false,
	var lunVos: List<LunVo> = listOf()
)


data class StorageDomainUsageVo(
	var storageDomainId: String = "",
	var availableDiskSizeGb: Int = -1,
	var usedDiskSizeGb: Int = -1,
	var storageDomainStatus: Int = -1,
	var historyDatetime: String = ""
)

data class StorageDomainVo(
	var id: String = "",
	var name: String = "",
	var description: String = "",
	var comment: String = "",
	var type: String = "",
	var format: Boolean = false,
	var status: String = "",
	var storageAddress: String = "",
	var storagePath: String = "",
	var storageType: String = "",
	var storageFormat: String = "",
	var storageDomainInfo: String = "",
	var diskFree: BigInteger = BigInteger.ZERO,
	var diskUsed: BigInteger = BigInteger.ZERO,
	var diskProfileId: String = "",
	var diskProfileName: String = "",
    var storageDomainUsages: List<List<String>> = arrayListOf(),
    var diskVoList: List<DiskVo> = arrayListOf(),
    var diskSnapshotVoList: List<DiskVo> = arrayListOf(),
    var imageFileList: List<ImageFileVo> = arrayListOf()
)

fun StorageDomain.toStorageDomainVo(c: Connection): StorageDomainVo {
	val dataCenterId: String
		= SystemServiceHelper.getInstance().findAllDataCenters(c).first().id()
	val sdAttached: StorageDomain?
		= SystemServiceHelper.getInstance().findAttachedStorageDomainFromDataCenter(c, dataCenterId, id())
	val files: List<File>
		= SystemServiceHelper.getInstance().findAllFilesFromStorageDomain(c, id())

	val diskProfiles: List<DiskProfile>
		= SystemServiceHelper.getInstance().findAllDiskProfiles(c)
	val diskProfileFound: DiskProfile?
		= diskProfiles.firstOrNull { it.storageDomainPresent() && it.storageDomain().id() == id() }
	val diskProfileId: String = diskProfileFound?.id() ?: ""
	val diskProfileName: String = if (diskProfileFound?.namePresent() == true) diskProfileFound.name() ?: "" else ""

	return StorageDomainVo(
		id(),
		if (namePresent()) name() else "",
		if (descriptionPresent()) description() else "",
		if (commentPresent()) comment() else "",
		if (typePresent()) "${type().value().capitalize()} ${if (master()) "(Master)" else ""}" else "",
		false,
		if (statusPresent()) status().value().capitalize()
		else if (sdAttached?.statusPresent() == true) sdAttached.status().value().capitalize()
		else "-",
		if (storageConnectionsPresent() &&
			storageConnections().first() != null) storageConnections().first().address() else "",
		if (storagePresent() &&
			storage().pathPresent()) storage().path() else "",
		if (storagePresent() &&
			storage().typePresent()) storage().type().value() else "",
		if (storageFormatPresent()) storageFormat().value() else "",
		"",
		if (availablePresent()) available() else BigInteger.ZERO,
		if (usedPresent()) used() else BigInteger.ZERO,
		diskProfileId,
		diskProfileName,
	)
}

fun List<StorageDomain>.toStorageDomainVos(c: Connection): List<StorageDomainVo>
	= this.map { it.toStorageDomainVo(c) }

fun Disk.toStorageDomainVoUsingDisk(): StorageDomainVo = StorageDomainVo(
	if (idPresent()) id() else "",
	if (namePresent()) name() else "",
	if (descriptionPresent()) description() else "",
	if (commentPresent()) comment() else "",
	when(contentType()) {
		DiskContentType.ISO -> "디스크"
		else -> contentType().value()
	},
	formatPresent(), // 모르겠음
	if (storageDomainPresent() &&
		storageDomain().storageFormatPresent()) storageDomain().storageFormat().value() else "",
	if (storageDomainPresent() &&
		storageDomain().storagePresent() &&
		storageDomain().storage().addressPresent()) storageDomain().storage().address() else "",
)

fun List<Disk>.toStorageDomainVosUsingDisks(): List<StorageDomainVo>
	= this.map { it.toStorageDomainVoUsingDisk() }


fun File.toStorageDomainVoUsingFile(): StorageDomainVo = StorageDomainVo(
	if (idPresent()) id() else "",
	if (namePresent()) name() else "",
	if (descriptionPresent()) description() else "",
	if (commentPresent()) comment() else "",
	if (typePresent()) type() else if("iso" == type()) "도메인" else "",
	false,
	if (storageDomainPresent() &&
		storageDomain().statusPresent()) storageDomain().status().value() else "",
	if (storageDomainPresent() &&
		storageDomain().storageFormatPresent()) storageDomain().storageFormat().value() else "",
	if (storageDomainPresent() &&
		storageDomain().storagePresent() &&
		storageDomain().storage().addressPresent()) storageDomain().storage().address() else "",
	if (storageDomainPresent() &&
		storageDomain().storagePresent() &&
		storageDomain().storage().typePresent()) storageDomain().storage().type().value() else "",
	if (storageDomainPresent() &&
		storageDomain().storageFormatPresent()) storageDomain().storageFormat().value() else "",
	if (storageDomainPresent()) storageDomain().name() else "",
	if (storageDomainPresent() &&
		storageDomain().storagePresent() &&
		storageDomain().disksPresent() &&
		storageDomain().disks().isNotEmpty()) storageDomain().disks().first().totalSize() else BigInteger.ZERO,
	if (storageDomainPresent() &&
		storageDomain().storagePresent() &&
		storageDomain().disksPresent() &&
		storageDomain().disks().isNotEmpty()) storageDomain().disks().first().actualSize() else BigInteger.ZERO,
	if (storageDomainPresent() &&
		storageDomain().storagePresent() &&
		storageDomain().disksPresent() &&
		storageDomain().disks().isNotEmpty() &&
		storageDomain().disks().first().diskProfilePresent() &&
		storageDomain().disks().first().diskProfile().idPresent()) storageDomain().disks().first().diskProfile().id() else "",
	if (storageDomainPresent() &&
		storageDomain().storagePresent() &&
		storageDomain().disksPresent() &&
		storageDomain().disks().isNotEmpty() &&
		storageDomain().disks().first().diskProfilePresent() &&
		storageDomain().disks().first().diskProfile().namePresent()) storageDomain().disks().first().diskProfile().name() else "",
)

fun List<File>.toStorageDomainVosUsingFiles(): List<StorageDomainVo> = this.map { it.toStorageDomainVoUsingFile() }

data class StorageVo(
	var storageDomainId: String = "",
	var historyDatetime: String = "",
	var availableDiskSizeGb: Int = 0,
	var usedDiskSizeGb: Int = 0,
	var storageDomainStatus: Int = 0,
)


data class SystemPropertiesVo(
   var id: String = "admin",
   var password: String = "admin!123",
   var ip: String = "localhost",
   var vncIp: String = "localhost",
   var vncPort: String = "9999",
   var cpuThreshold: Int = 80,
   var memoryThreshold: Int = 78,
   var grafanaUri: String = "",
   var deeplearningUri: String = "",
   var symphonyPowerControll: Boolean = false,
   var loginLimit: Int = 5,
)

data class TemplateDiskVo(
  var id: String = "",
  var name: String = "",
  var description: String = "",
  var virtualSize: String = "",
  var actualSize: String = "",
  var status: String = "",
  var format: String = "",
  var type: String = "",
  var storageDomainId: String = "",
  var diskProfileId: String = "",
  var quotaId: String = "",
  var storageDomains: List<StorageDomainVo> = arrayListOf(),
  var diskProfiles: List<DiskProfileVo> = arrayListOf(),
  var quotas: List<QuotaVo> = arrayListOf(),
)

fun TemplateDiskVo.toDiskAttachment(connection: Connection, dataCenterId: String): DiskAttachment {
	val storageDomain: StorageDomain
		= SystemServiceHelper.getInstance().findStorageDomain(connection, storageDomainId)
	val diskProfile: DiskProfile
		= SystemServiceHelper.getInstance().findDiskProfile(connection, diskProfileId)
	val quota: Quota
		= SystemServiceHelper.getInstance().findQuotaFromDataCenter(connection, dataCenterId, quotaId)

	val d2Add: Disk = Builders.disk()
		.id(id)
		.name(name)
		.description(description)
		.format(DiskFormat.fromValue(format))
		.storageDomain(storageDomain)
		.diskProfile(diskProfile)
		.quota(quota)
		.build()
	return Builders.diskAttachment()
		.disk(d2Add)
		.build()
}

fun List<TemplateDiskVo>.toDiskAttachments(connection: Connection, dataCenterId: String): List<DiskAttachment>
	= this.map { it.toDiskAttachment(connection, dataCenterId) }

fun Disk.toTemplateDiskVo(storageDomains: List<StorageDomainVo> = arrayListOf(),
						  diskProfiles: List<DiskProfileVo> = arrayListOf(),
						  quotas: List<QuotaVo> = arrayListOf()): TemplateDiskVo = TemplateDiskVo(
	if (idPresent()) id() else "",
	if (namePresent()) name() else "",
	if (descriptionPresent()) description() else "",
	"${(provisionedSize().toDouble() / 1024.0.pow(3.0) * 100.0).roundToInt() / 100.0} GiB",
	"${(actualSize().toDouble() / 1024.0.pow(3.0) * 100.0).roundToInt() / 100.0} GiB",
	status().value(),
	format().value(),
	storageType().value(),
	"", "", "",
							  storageDomains, diskProfiles, quotas
)



data class TemplateEditVo(
	var clusters: List<ClusterVo> = arrayListOf(),
	var operatingSystems: List<OsInfoVo> = arrayListOf(),
	var hosts: List<HostVo> = arrayListOf(),
	var leaseStorageDomains: List<StorageDomainVo> = arrayListOf(),
	var bootImages: List<StorageDomainVo> = arrayListOf(),
	var cpuProfiles: List<CpuProfileVo> = arrayListOf(),
	var id: String = "",
	var cluster: String = "",
	var operatingSystem: String = "",
	var type: String = "",
	var name: String = "",
	var subName: String = "",
	var description: String = "",
	var stateless: Boolean = false,
	var startInPause: Boolean = false,
	var deleteProtection: Boolean = false,
	var videoType: String = "",
	var graphicsProtocol: String = "",
	var usbSupport: Boolean = false,
	var disconnectAction: String = "",
	var monitors: Int = 0,
	var singlePci: Boolean = false,
	var singleSignOn: Boolean = false,
	var smartcard: Boolean = false,
	var virtIO: Boolean = false,
	var memory: BigInteger = BigInteger.ZERO,
	var maximumMemory: BigInteger = BigInteger.ZERO,
	var virtualSockets: Int = 0,
	var coresPerVirtualSocket: Int = 0,
	var threadsPerCore: Int = 0,
	var recommendHost: String = "",
	var targetHost: String = "",
	var affinity: String = "",
	var customMigrationUsed: Boolean = false,
	var customMigrationDowntimeUsed: Boolean = false,
	var customMigration: String = "",
	var customMigrationDowntime: BigInteger = BigInteger.ZERO,
	var autoConverge: String = "",
	var compressed: String = "",
	var useCloudInit: Boolean = false,
	var hostName: String = "",
	var timezone: String = "",
	var customScript: String = "",
	var highAvailability: Boolean = false,
	var leaseStorageDomain: String = "",
	var resumeBehaviour: String = "",
	var priority: BigInteger = BigInteger.ZERO,
	var watchdogModel: String = "",
	var watchdogAction: String = "",
	var firstDevice: String = "",
	var secondDevice: String = "",
	var bootImageUse: Boolean = false,
	var bootImage: String = "",
	var imageStorage: String = "",
	var cpuProfile: String = "",
	var cpuShare: BigInteger = BigInteger.ZERO,
	var physicalMemory: BigInteger = BigInteger.ZERO,
	var memoryBalloon: Boolean = false,
	var ioThreadsEnabled: BigInteger = BigInteger.ZERO,
	var virtioScsiEnabled: Boolean = false,
)

data class TemplateVo(
	var orgVmId: String = "",
	var id: String = "",
	var name: String = "",
	var versionName: String = "",
	var version: String = "",
	var description: String = "",
	var creationTime: Long = 0L,
	var status: String = "",
	var forceOverride: Boolean = false,
	var os: String = "",
	var systemInfo: VmSystemVo? = null,
	var vms: List<VmVo> = arrayListOf(),
	var nics: List<VmNicVo> = arrayListOf(),
	var events: List<EventVo> = arrayListOf(),
	var cluster: ClusterVo? = null,
	var cpuProfileId: String = "",
	var quotaId: String = "",
	var templateDisks: List<TemplateDiskVo> = arrayListOf(),
	var rootTemplateId: String? = "",
	var rootTemplates: List<TemplateVo> = arrayListOf(),
	var subVersionName: String = "",
	var allUserAccess: Boolean = false,
	var clonePermissions: Boolean = false,
	var seal: Boolean = false,
	var diskAttachmentSize: Int = 0,
)

fun Template.toTemplateVo(connection: Connection): TemplateVo {
	val osFound: OperatingSystemInfo?
		= SystemServiceHelper.getInstance().findAllOperatingSystems(connection).firstOrNull { it.name() == os().type() }
	val os: String
		 = osFound?.name() ?: ""
	val clusterFound: Cluster?
		= if (clusterPresent()) SystemServiceHelper.getInstance().findCluster(connection, cluster().id())
		else null
	val clusterVo: ClusterVo?
		= clusterFound?.toClusterVo(connection, null)

	val nics: List<Nic>
		= SystemServiceHelper.getInstance().findAllNicsFromTemplate(connection, id())
	val nicVos: List<VmNicVo>
		= nics.toVmNicVos(connection)

	val vms: List<Vm>
		= SystemServiceHelper.getInstance().findAllVms(connection)

	val vmVos: List<VmVo>
		= vms.toVmVos(connection)

	val name: String
		= if (versionPresent() &&
			version().versionNumberAsInteger() > 1)  "${name()} / ${version().versionName()}"
		else if (namePresent())	name()
		else ""

	val version: String
		= if (versionPresent() &&
			version().versionNumberAsInteger() > 0) "${version().versionName()} (${version().versionNumberAsInteger()})"
		else 										""

	return TemplateVo(
		"",
		if (idPresent()) id() else "",
		name,
		version().versionName(),
		version,
		if (descriptionPresent()) description() else "",
		if (creationTimePresent()) creationTime().time else 0L,
	).apply {
		this.cluster = clusterVo
		this.nics = nicVos
		this.vms = vmVos
	}
}

fun List<Template>.toTemplateVos(connection: Connection): List<TemplateVo>
	= this.map { it.toTemplateVo(connection) }

data class UsageVo(
	var cpuUsages: Int = 0,
	var memoryUsages: Int = 0,
	var networkUsages: Int = 0,
	var storageUsages: Int = 0,
	var transitUsages: Int = 0,
	var receiveUsages: Int = 0,
	var usageDate: String = "",
	var storageUsageDate: String = "",
)



data class UserVo(
	var no: Int = -1,
	var id: String = "",
	var password: String = "",
	var newPassword: String = "",
	var administrative: Boolean = false,
	var name: String = "",
	var lastName: String = "",
	var principal: String = "",
	var namespace: String = "",
	var email: String = "",
	var authProvider: String = "",
	var roleId: String = "",
	var loginCount: Int = 0,
	var blockTime: String = "",
)

fun UserVo.user2Add(): User = Builders.user()
	.principal(principal)
	.userName("$name@internal@internal-authz")
	.domain(Builders.domain().name("internal-authz"))
	.build()

fun UserVo.permission2Add(): Permission = Builders.permission()
	.role(Builders.role().id(roleId))
	.user(Builders.user().id(id))
	.build()



data class VmConsoleVo(
	var type: String = "",
	var address: String = "",
	var port: String = "",
	var tlsPort: String = "",
	var passwd: String = "",
	var vmName: String = "",
	var hostAddress: String = "",
	var hostPort: String = "",
)

data class VmCreateVo(
	var clusters: List<ClusterVo> = arrayListOf(),
	var templates: List<TemplateVo> = arrayListOf(),
	var operatingSystems: List<OsInfoVo> = arrayListOf(),
	var instanceTypes: List<InstanceTypeVo> = arrayListOf(),
	var nics: List<VmNicVo> = arrayListOf(),
	var hosts: List<HostVo> = arrayListOf(),
	var leaseStorageDomains: List<StorageDomainVo> = arrayListOf(),
	var bootImages: List<StorageDomainVo> = arrayListOf(),
	var cpuProfiles: List<CpuProfileVo> = arrayListOf(),
	var id: String = "",
	var status: String = "",
	var cluster: String = "",
	var template: String = "",
	var operatingSystem: String = "",
	var instanceType: String = "",
	var type: String = "",
	var name: String = "",
	var description: String = "",
	var use: String = "",
	var disks: List<DiskVo> = arrayListOf(),
	var newDisk: DiskVo? = null,
	var selectNics: List<VmNicVo> = arrayListOf(),
	var exSelectNics: List<VmNicVo> = arrayListOf(),
	var snapshotId: String = "",
	var videoType: String = "",
	var graphicsProtocol: String = "",
	var usbSupport: Boolean = false,
	var disconnectAction: String = "",
	var monitors: Int = 0,
	var singlePci: Boolean = false,
	var singleSignOn: Boolean = false,
	var smartcard: Boolean = false,
	var virtIO: Boolean = false,
	var memory: BigInteger = BigInteger.ZERO,
	var maximumMemory: BigInteger = BigInteger.ZERO,
	var virtualSockets: Int = 0,
	var coresPerVirtualSocket: Int = 0,
	var threadsPerCore: Int = 0,
	var pickHost: String = "",
	var recommendHost: String = "",
	var targetHost: String = "",
	var affinity: String = "",
	var customMigrationUsed: Boolean = false,
	var customMigrationDowntimeUsed: Boolean = false,
	var customMigration: String = "",
	var customMigrationDowntime: BigInteger = BigInteger.ZERO,
	var autoConverge: String = "",
	var compressed: String = "",
	var useCloudInit: Boolean = false,
	var hostName: String = "",
	var timezone: String = "",
	var customScript: String = "",
	var highAvailability: Boolean = false,
	var leaseStorageDomain: String = "",
	var resumeBehaviour: String = "",
	var priority: BigInteger = BigInteger.ZERO,
	var watchdogModel: String = "",
	var watchdogAction: String = "",
	var firstDevice: String = "",
	var secondDevice: String = "",
	var bootImageUse: Boolean = false,
	var headlessMode: Boolean = false,
	var bootImage: String = "",
	var imageStorage: String = "",
	var cpuProfile: String = "",
	var cpuShare: BigInteger = BigInteger.ZERO,
	var physicalMemory: BigInteger = BigInteger.ZERO,
	var memoryBalloon: Boolean? = false,
	var ioThreadsEnabled: BigInteger = BigInteger.ZERO,
	var virtioScsiEnabled: Boolean = false,
	var periodDuration: Long = -1L,
	var bytesPerPeriod: Long = -1L,
	var deviceSource: String = "",
)

fun VmCreateVo.toVmVoKarajan(): com.itinfo.model.karajan.VmVo {
	return com.itinfo.model.karajan.VmVo(

	).apply {
		cores = coresPerVirtualSocket
		sockets = virtualSockets
		threads = threadsPerCore
		memoryInstalled = BigDecimal(memory)
	}
}

data class VmDeviceVo(
	var historyId: String = "",
	var type: String = "",
	var address: String = "",
	var readonly: Boolean = false,
	var plugged: Boolean = false,
	var managed: Boolean = false,
	var deviceId: String = "",
)

data class VmNetworkUsageVo(
	var historyDatetime: String = "",
	var receiveRatePercent: Int = 0,
	var transmitRatePercent: Int = 0,
)

data class VmNicVo(
	var networkId: String = "",
	var id: String = "",
	var name: String = "",
	var nicId: String = "",
	var nicName: String = "",
	var networkName: String = "",
	var profileName: String = "",
	var ipv4: String = "",
	var ipv6: String = "",
	var macAddress: String = "",
	var status: Boolean = false,
	var plugged: Boolean = false,
	var linked: Boolean = false,
	var vm: VmContainer? = null,
	var vnicProfile: VnicProfileContainer? = null,
	var interfaceType: String = "",
	var profileId: String = "",
	var profileList: List<VmNicVo> = arrayListOf(),
)

fun VnicProfile.toVmNicVo(): VmNicVo {
	return VmNicVo(
		if (networkPresent()) network().id() else "",
		if (idPresent()) id() else "",
		if (namePresent()) name() else "",
		id(),
		name(),
		if (networkPresent() &&
			network().namePresent()) network().name() else "",
	)
}

fun List<VnicProfile>.toVmNicVos(): List<VmNicVo> = this.map { it.toVmNicVo() }

fun Nic.toSnapshotVmNicVo(): VmNicVo = VmNicVo(
	if (networkPresent()) network().id() else "",
	id(),
	name(),
	id(),
	name(),
	if (networkPresent()) network().name() else "",
	if (vnicProfilePresent()) vnicProfile().name() else "",
	if (reportedDevicesPresent()) reportedDevices().first().ips()[0].address() else "해당 없음",
	if (reportedDevicesPresent()) reportedDevices().first().ips()[1].address() else "해당 없음",
	if (macPresent()) mac().address() else "",
	if (linkedPresent()) linked() else false,
	if (pluggedPresent()) plugged() else false,
	if (linkedPresent()) linked() else false,
	null,
	null,
	if (interface_Present()) interface_().value().toUpperCase() else "",
	if (vnicProfilePresent()) vnicProfile().id() else "",
	arrayListOf(),
)

fun Nic.toVmNicVo(connection: Connection): VmNicVo {
	val profiles: List<VmNicVo>
		= SystemServiceHelper.getInstance().findAllVnicProfiles(connection).toVmNicVos()
	val vnicProfile: VnicProfile
		= SystemServiceHelper.getInstance().findVnicProfile(connection, vnicProfile().id())
	val network: Network
		= SystemServiceHelper.getInstance().findNetwork(connection, vnicProfile.network().id())

	return VmNicVo(
		if (networkPresent()) network().id() else (network.id() ?: ""),
		if (idPresent()) id() else "",
		if (namePresent()) name() else "",
		id(),
		name(),
		if (networkPresent()) network().name() else (network.name() ?: ""),
		if (vnicProfilePresent()) vnicProfile().name() else (vnicProfile.name() ?: ""),
		if (reportedDevicesPresent()) reportedDevices().first().ips()[0].address() else "해당 없음",
		if (reportedDevicesPresent()) reportedDevices().first().ips()[1].address() else "해당 없음",
		if (macPresent()) mac().address() else "",
		if (linkedPresent()) linked() else false,
		if (pluggedPresent()) plugged() else false,
		if (linkedPresent()) linked() else false,
		null,
		null,
		if (interface_Present()) interface_().value().toUpperCase() else "",
		if (vnicProfilePresent()) vnicProfile().id() else (vnicProfile.id() ?: ""),
		profiles,
	)
}

fun List<Nic>.toVmNicVos(connection: Connection): List<VmNicVo>
	= this.map { it.toVmNicVo(connection) }

fun List<Nic>.toSnapshotVmNicVos(): List<VmNicVo>
 	= this.map { it.toSnapshotVmNicVo() }

fun Nic.toVmNicVo(vmId: String, nicId: String, profileId: String): VmNicVo {
	val vmContainer = VmContainer().apply {
		id(vmId)
	}
	val vnicProfileContainer = VnicProfileContainer().apply {
		id(profileId)
		name(profileId)
	}
	val vo = VmNicVo(
		"",
		nicId,
		"",
		"",
		name(),
		"",
		"",
		"",
		"",
		mac().address(),
		false,
		plugged(),
		linked(),
		vmContainer,
		vnicProfileContainer,
		"",
		"",
	)
	return vo
}


data class VmSummaryVo(
	var id: String = "",
	var name: String = "",
	var description: String = "",
	var comment: String = "",
	var address: String = "",
	var status: String = "",
	var osHostName: String = "",
	var hostId: String = "",
	var hostName: String = "",
	var memoryInstalled: BigDecimal = BigDecimal.ZERO,
	var memoryUsed: BigDecimal = BigDecimal.ZERO,
	var memoryFree: BigDecimal = BigDecimal.ZERO,
	var memoryBuffered: BigDecimal = BigDecimal.ZERO,
	var memoryCached: BigDecimal = BigDecimal.ZERO,
	var cpuCurrentGuest: BigDecimal = BigDecimal.ZERO,
	var cpuCurrentHypervisor: BigDecimal = BigDecimal.ZERO,
	var cpuCurrentTotal: BigDecimal = BigDecimal.ZERO,
	var vmLastUsage: VmUsageVo? = null,
	var vmNicsLastUsage: List<NicUsageVo> = arrayListOf(),
)

fun Vm.toVmSummaryVo(c: Connection, clustersDao: ClustersDao?): VmSummaryVo {
	val vmNics: List<Nic>
		= SystemServiceHelper.getInstance().findNicsFromVm(c, id())
	val hostFound: Host?
		= if (hostPresent() &&
			host().idPresent()) SystemServiceHelper.getInstance().findHost(c, host().id()) else null

	return VmSummaryVo(
		id(),
		if (namePresent()) name() else "",
		if (descriptionPresent()) description() else "",
		if (commentPresent()) comment() else "",
		"",
		if (statusPresent()) status().value() else "",
		"",
		hostFound?.id() ?: "",
		hostFound?.name() ?: "",
		BigDecimal.ZERO,
		BigDecimal.ZERO,
		BigDecimal.ZERO,
		BigDecimal.ZERO,
		BigDecimal.ZERO,
		BigDecimal.ZERO,
		BigDecimal.ZERO,
		BigDecimal.ZERO,
	)
}

fun List<Vm>.toVmSummaryVos(c: Connection, clustersDao: ClustersDao?): List<VmSummaryVo>
	= this.map { it.toVmSummaryVo(c, clustersDao) }

data class VmSystemVo(
	var definedMemory: String = "",
	var guaranteedMemory: String = "",
	var maxMemoryPolicy: String = "",
	var totalVirtualCpus: Int = 0,
	var virtualSockets: Int = 0,
	var coresPerVirtualSocket: Int = 0,
	var threadsPerCore: Int = 0,
)

fun Vm.toVmSystemVo(): VmSystemVo {
	val virtualSockets: Int
			= if (cpuPresent() &&
		cpu().topologyPresent() &&
		cpu().topology().socketsPresent()) cpu().topology().socketsAsInteger() else 0
	val coresPerVirtualSocket: Int
			= if (cpuPresent() &&
		cpu().topologyPresent() &&
		cpu().topology().coresPresent()) cpu().topology().coresAsInteger() else 0
	val threadsPerCore: Int
			= if (cpuPresent() &&
		cpu().topologyPresent() &&
		cpu().topology().coresPresent()) cpu().topology().coresAsInteger() else 0
	val totalVirtualCpus = virtualSockets * coresPerVirtualSocket * threadsPerCore

	return VmSystemVo(
		if (memoryPresent()) "${memoryAsLong() / 1024 / 1024} MB" else "0 MB",
		if (memoryPolicyPresent() &&
			memoryPolicy().guaranteedPresent()) "${memoryPolicy().guaranteedAsLong() / 1024 / 1024} MB" else "0 MB",
		if (memoryPolicyPresent() &&
			memoryPolicy().maxPresent()) "${memoryPolicy().maxAsLong() / 1024 / 1024} MB" else "0 MB",
		totalVirtualCpus,
		virtualSockets, coresPerVirtualSocket, threadsPerCore
	)
}

fun Template.toVmSystemVoFromTemplate(): VmSystemVo {
	val virtualSockets: Int
		= if (cpuPresent() &&
		cpu().topologyPresent() &&
		cpu().topology().socketsPresent()) cpu().topology().socketsAsInteger() else 0
	val coresPerVirtualSocket: Int
		= if (cpuPresent() &&
		cpu().topologyPresent() &&
		cpu().topology().coresPresent()) cpu().topology().coresAsInteger() else 0
	val threadsPerCore: Int
		= if (cpuPresent() &&
		cpu().topologyPresent() &&
		cpu().topology().coresPresent()) cpu().topology().coresAsInteger() else 0
	val totalVirtualCpus = virtualSockets * coresPerVirtualSocket * threadsPerCore

	return VmSystemVo(
		if (memoryPresent()) "${memoryAsLong() / 1024 / 1024} MB" else "0 MB",
		if (memoryPolicyPresent() &&
			memoryPolicy().guaranteedPresent()) "${memoryPolicy().guaranteedAsLong() / 1024 / 1024} MB" else "0 MB",
		if (memoryPolicyPresent() &&
			memoryPolicy().maxPresent()) "${memoryPolicy().maxAsLong() / 1024 / 1024} MB" else "0 MB",
		totalVirtualCpus,
		virtualSockets, coresPerVirtualSocket, threadsPerCore
	)
}

data class VmUsageVo(
	var historyDatetime: String = "",
	var cpuUsagePercent: Int = 0,
	var memoryUsagePercent: Int = 0,
)

fun VmUsageVo.toUsageVo(): UsageVo {
	val vo = UsageVo(
		cpuUsagePercent,
		memoryUsagePercent
	).apply {
		usageDate = historyDatetime
	}
	return vo
}

fun List<VmUsageVo>.toUsageVos(): List<UsageVo> = this.map { it.toUsageVo() }

data class VmVo(
	var id: String = "",
	var name: String = "",
	var comment: String = "",
	var description: String = "",
	var use: String = "",
	var runHost: String = "",
	var ipAddress: String = "",
	var fqdn: String = "",
	var template: String = "",
	var os: String = "",
	var orgin: String = "",
	var timeOffset: String = "",
	var host: String = "",
	var hostId: String = "",
	var hostName: String = "",
	var type: String = "",
	var cluster: String = "",
	var clusterId: String = "",
	var dataCenter: String = "",
	var status: String = "",
	var graphicProtocol: String = "",
	var startTime: String = "",
	var nextRunConfigurationExists: Boolean = false,
	var diskDetach: Boolean = false,
	var headlessMode: Boolean = false,
	var diskSize: Int = 0,
	var disc: String = "",
	var cdate: String = "",
	var vmSystem: VmSystemVo? = null,
	var vmNics: List<VmNicVo> = arrayListOf(),
	var disks: List<DiskVo> = arrayListOf(),
	var snapshots: List<SnapshotVo> = arrayListOf(),
	var role: List<Map<String, Object>> = arrayListOf(),
	var cpuUsage: List<List<String>> = arrayListOf(),
	var memoryUsage: List<List<String>> = arrayListOf(),
	var networkUsage: List<List<String>> = arrayListOf(),
	var devices: List<VmDeviceVo> = arrayListOf(),
	var events: List<EventVo> = arrayListOf(),
	var profileList: List<VmNicVo> = arrayListOf(),
	var usageVos: List<UsageVo> = arrayListOf(),
	var cpuUsages: List<Int> = arrayListOf(),
	var memoryUsages: List<Int> = arrayListOf(),
	var networkUsages: List<Int> = arrayListOf(),
)

fun Vm.toVmVo(connection: Connection): VmVo {
	val nicsFromVm: List<Nic>
		= SystemServiceHelper.getInstance().findNicsFromVm(connection, id())
	val ipAddress: String
		= if (nicsFromVm.isNotEmpty() &&
			nicsFromVm.first().reportedDevicesPresent() &&
			nicsFromVm.first().reportedDevices().first().ipsPresent())
				nicsFromVm.first().reportedDevices().first().ips().firstOrNull {
					it.versionPresent() && it.version() == IpVersion.V4
				}?.address() ?: ""
		else
				""
	val date = Date(System.currentTimeMillis())
	val vo: VmVo = VmVo(
		if (idPresent()) id() else "",
		if (namePresent()) name() else "",
		if (commentPresent()) comment() else "",
		if (descriptionPresent()) description() else "",
	).apply {
		this.ipAddress = ipAddress
		this.fqdn = if (fqdnPresent()) fqdn() else ""
		this.host =
			if (hostPresent() &&
				host().namePresent()) host().name() else ""
		this.hostId =
			if (hostPresent() &&
				host().idPresent()) host().id() else ""
		this.type = if (typePresent()) type().value() else ""
		this.clusterId =
			if (clusterPresent()) cluster().id() else ""
		this.cluster =
			if (clusterPresent() &&
				cluster().namePresent()) cluster().name() else ""
		this.status = if (statusPresent()) status().value() else ""
		this.nextRunConfigurationExists = nextRunConfigurationExists()
		this.headlessMode = !displayPresent()
		this.graphicProtocol =
			if (displayPresent() &&
				display().typePresent()) display().type().toString() else "없음"
		this.startTime =
			if ("up" == status().value() && startTimePresent())
				"${date.time - startTime().time / 60000L}"
			else
				"${date.time - creationTime().time / 60000L}"
	}
	return vo
}

fun List<Vm>.toVmVos(connection: Connection): List<VmVo>
	= this.map { it.toVmVo(connection) }

fun VmVo.stop(connection: Connection): Boolean =
	SystemServiceHelper.getInstance().stopVm(connection, id)
fun List<VmVo>.stopAllVms(connection: Connection): Boolean 
	= this.map { it.stop(connection) }.reduce { acc, b -> acc && b }
fun VmVo.reboot(connection: Connection): Boolean =
	SystemServiceHelper.getInstance().rebootVm(connection, id)

fun List<VmVo>.rebootAllVms(connection: Connection): Boolean = this.map { it.reboot(connection) }.reduce { acc, b -> acc && b }
		
data class VnicProfileVo(
	var name: String,
	var id: String,
	var networkFilter: NetworkFilterContainer,
	var network: NetworkContainer,
	var passThrough: VnicPassThroughContainer,
	var portMirroring: Boolean,
)

fun VnicProfile.toVnicProfileVo(): VnicProfileVo = VnicProfileVo(
	id(),
	name(),
	toNetworkFilterContainer(),
	toNetworkContainer(),
	toVnicPassThroughContainer(),
	portMirroring()
)


fun VnicProfile.toNetworkFilterContainer(): NetworkFilterContainer {
	val vo = NetworkFilterContainer().apply {
		id(networkFilter().id())
	}
	return vo
}

fun VnicProfile.toNetworkContainer(): NetworkContainer {
	val vo = NetworkContainer().apply {
		id(network().id())
	}
	return vo
}

fun VnicProfile.toVnicPassThroughContainer(): VnicPassThroughContainer {
	val vo = VnicPassThroughContainer().apply {
		mode(passThrough().mode())
	}
	return vo
}

fun List<VnicProfile>.toVnicProfileVos(): List<VnicProfileVo> = this.map { it.toVnicProfileVo() }