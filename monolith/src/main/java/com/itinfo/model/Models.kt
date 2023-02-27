package com.itinfo.model

import java.math.BigDecimal
import java.math.BigInteger
import java.util.Date

import org.ovirt.engine.sdk4.types.ImageTransferDirection
import org.ovirt.engine.sdk4.types.ImageTransferPhase

import org.ovirt.engine.sdk4.internal.containers.VmContainer
import org.ovirt.engine.sdk4.internal.containers.VnicProfileContainer
import org.ovirt.engine.sdk4.internal.containers.NetworkContainer
import org.ovirt.engine.sdk4.internal.containers.NetworkFilterContainer
import org.ovirt.engine.sdk4.internal.containers.VnicPassThroughContainer

data class ItInfoNetworkClusterVo(
	var clusterName: String,
	var clusterVersion: String,
	var required: Boolean,
	var status: String,
	val usages: List<ItInfoNetworkUsagesVo>,
	var clusterDescription: String,
	var connect: Boolean,
	var clusterId: String,
)

data class ItInfoNetworkCreateVo(
	val qoss: List<ItInfoNetworkQosVo>,
	val networkName: List<String>,
	val clusters: List<ItInfoNetworkClusterVo>,
)

data class ItInfoNetworkDnsVo(
	val dnsIp: String
)

data class ItInfoNetworkGroupVo(
	val network: ItInfoNetworkVo,
	val clusters: List<ItInfoNetworkClusterVo>,
	val hosts: List<ItInfoNetworkHostVo>,
	val vms: List<ItInfoNetworkVmVo>,
)

data class ItInfoNetworkHostVo(
	var hostStatus: String,
	var hostName: String,
	var nicStatus: String,
	var nicName: String,
	var hostClusterName: String,
	var nicSpeed: BigInteger,
	var dataCurrentRxBps: BigDecimal,
	var dataCurrentTxBps: BigDecimal,
	var dataTotalRx: BigDecimal,
	var dataTotalTx: BigDecimal,
)

data class ItInfoNetworkQosVo(
	var name: String,
	var id: String,
)

data class ItInfoNetworkUsagesVo(
	var usage: String
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
	var id: String,
	var name: String,
	var description: String,
	var comment: String,
	var mtu: String,
	var provider: String,
	var vlan: String,
	var qos: String,
	var qosId: String,
	var usage: String,
	var label: String,
	var cluster: String,
	var status: String,
	var baseInterface: String,
	var usingVmNetwork: Boolean,
	var required: Boolean,
	val clusters: List<ItInfoNetworkClusterVo>,
	val dnss: List<ItInfoNetworkDnsVo>,
	val usages: List<ItInfoNetworkUsagesVo>,
)

data class ClusterCreateVo(
	var id: String,
	var name: String,
	var description: String,
	var comment: String,
	var cpuArchitecture: String,
	var cpuType: String,
	var switchType: String,
	var firewallType: String,
	var networkId: String,
	var networkProviderId: String,
)

data class ClusterVo(
	var id: String,
	var name: String,
	var description: String,
	var cpuType: String,
	var cpuImage: String,
	var hostCnt: Int,
	var vmCnt: Int,
	val clusterNetworkList: List<NetworkVo>,
	val hostsUp: Int,
	val hostsDown: Int,
	val vmsUp: Int,
	val vmsDown: Int,
	var memoryTotal: BigDecimal,
	var memoryUsed: BigDecimal,
	var memoryFree: BigDecimal,
	var memoryUsagePercent: BigDecimal,
	var ksmCpuUsagePercent: BigDecimal,
	var userCpuUsagePercent: BigDecimal,
	var systemCpuUsagePercent: BigDecimal,
	var idleCpuUsagePercent: BigDecimal,
	val cpuUsage: List<List<String>>,
	val memoryUsage: List<List<String>>,
	val network: NetworkVo,
	val hostsDetail: List<HostDetailVo>,
	val vmSummaries: List<VmSummaryVo>,
	val usageVos: List<UsageVo>,
)

data class CpuProfileVo(
	var id: String,
	var name: String,
	var shares: Int,
)

data class DashboardTopVo(
	val vmCpuKey: List<String>,
	val vmCpuVal: List<String>,
	val vmMemoryKey: List<String>,
	val vmMemoryVal: List<String>,
	val hostCpuKey: List<String>,
	val hostCpuVal: List<String>,
	val hostMemoryKey: List<String>,
	val hostMemoryVal: List<String>,
)


data class DataCenterVo(
	var name: String,
	var id: String,
	var description: String,
	var clusters: Int,
	var hostsUp: Int,
	var hostsDown: Int,
	var storagesActive: Int,
	var storagesUnattached: Int,
	var vmsUp: Int,
	var vmsDown: Int,
	var cpuCurrentUser: Double,
	var cpuCurrentSystem: Double,
	var cpuCurrentIdle: Double,
	var memoryTotal: BigDecimal,
	var memoryUsed: BigDecimal,
	var memoryFree: BigDecimal,
	var storageAvaliable: BigInteger,
	var storageUsed: BigInteger,
	var cpuUsage: List<List<String>>,
	var memoryUsage: List<List<String>>,
	var receiveRate: List<List<String>>,
	var transmitRate: List<List<String>>,
	var disks: List<List<String>>,
	var usageVos: List<UsageVo>,
	var usageDate: String,
	var storageUsageDate: String,
	var cpuUsages: List<Int>,
	var memoryUsages: List<Int>,
	var transitUsages: List<Int>,
	var receiveUsages: List<Int>,
	var storageUsages: List<Int>,
	var totalcpu: Int,
	var usingcpu: Int,
)

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

data class DiskMigrationVo(
	var migrationType: String,
	var sourceStorageDomainId: String,
	var targetStorageDomainId: String,
	var targetDiskName: String,
	var disk: DiskVo,
)

data class DiskProfileVo(
	var storageDomainId: String,
	var id: String,
	var name: String,
)
data class DiskVo(
	var id: String,
	var name: String,
	var virtualSize: String,
	var actualSize: String,
	var format: String,
	var wipeAfterDelete: Boolean,
	var bootable: Boolean,
	var sharable: Boolean,
	var readOnly: Boolean,
	var passDiscard: Boolean,
	var attachedTo: String,
	var storageDomainId: String,
	var storageDomainName: String,
	var diskProfileId: String,
	var diskInterface: String,
	var alignment: String,
	var status: String,
	var type: String,
	var description: String,
	var snapshotId: String,
	var lunId: String,
	var hostId: String,
	var diskId: String,
	var storageType: String,
	var cdate: String,
)


data class EncryptionVo(
	var inputStr: String,
)

data class EventVo(
	var id: String,
	var correlationId: String,
	var code: BigInteger,
	var origin: String,
	var description: String,
	var time: Date,
	var severity: String,
)

data class FenceAgentVo(
	var id: String,
	var address: String,
	var username: String,
	var password: String,
	var type: String,
	var option: String,
)

data class HostCreateVo(
	var id: String,
	var clusterId: String,
	var name: String,
	var comment: String,
	var description: String,
	var status: String,
	var networkProviderId: String,
	var powerManagementEnabled: Boolean,
	var hostEngineEnabled: Boolean,
	var ssh: SshVo,
	var fenceAgent: FenceAgentVo,
)

data class HostDetailVo(
	var id: String,
	var name: String,
	var description: String,
	var comment: String,
	var address: String,
	var status: String,
	var clusterName: String,
	var clusterId: String,
	var powerManagementEnabled: Boolean,
	var vmsCnt: Int,
	var vmsUpCnt: Int,
	var vmsDownCnt: Int,
	var memoryTotal: BigDecimal,
	var memoryUsed: BigDecimal,
	var memoryFree: BigDecimal,
	var swapTotal: BigDecimal,
	var swapUsed: BigDecimal,
	var swapFree: BigDecimal,
	var ksmCpuUsagePercent: BigDecimal,
	var userCpuUsagePercent: BigDecimal,
	var systemCpuUsagePercent: BigDecimal,
	var idleCpuUsagePercent: BigDecimal,
	var bootTime: BigDecimal,
	var hwManufacturer: String,
	var hwProductName: String,
	var cpuType: String,
	var cpuName: String,
	var cpuCores: BigInteger,
	var cpuSockets: BigInteger,
	var cpuThreads: BigInteger,
	var cpuTotal: Int,
	var osVersion: String,
	var osInfo: String,
	var kernelVersion: String,
	var haConfigured: Boolean,
	var haScore: String,
	var hostSw: HostSwVo,
	var cpuUsage: List<List<String>>,
	var memoryUsage: List<List<String>>,
	var hostLastUsage: HostUsageVo,
	var hostUsageList: List<HostUsageVo>,
	var vmSummaries: List<VmSummaryVo>,
	var hostNicsLastUsage: List<NicUsageVo>,
	var hostNicsUsageApi: List<NicUsageApiVo>,
	var netAttachment: List<NetworkAttachmentVo>,
	var sshVo: SshVo,
	var lunVos: List<LunVo>,
	var usageVos: List<UsageVo>,
)

data class HostHaVo(
	var hostId: String,
	var haScore: String,
	var haConfigured: Boolean,
	var haActive: Boolean,
	var haGlobalMaintenance: Boolean,
	var haLocalMaintenance: Boolean,
)

data class HostInterfaceVo(
	var historyDatetime: String,
	var receiveRatePercent: Int,
	var transmitRatePercent: Int,
	var receivedTotalByte: Int,
	var transmittedTotalByte: Int,
)

data class HostSwVo(
	var hostId: String,
	var hostOs: String,
	var kernelVersion: String,
	var kvmVersion: String,
	var vdsmVersion: String,
)

data class HostUsageVo(
	var hostId: String,
	var hostStatus: String,
	var cpuUsagePercent: String,
	var memoryUsagePercent: String,
	var historyDatetime: String,
)

data class HostVo(
	var clusterId: String,
	var hostId: String,
	var hostName: String,
	var historyDatetime: String,
	var hostStatus: String,
	var memoryUsagePercent: Int,
	var cpuUsagePercent: Int,
	var ksmCpuPercent: Int,
	var activeVms: Int,
	var totalVms: Int,
	var totalVmsVcpus: Int,
	var cpuLoad: Int,
	var systemCpuUsagePercent: Int,
	var userCpuUsagePercent: Int,
	var swapUsedMb: Int,
	var ksmSharedMemoryMb: Int,
	var lunVos: List<LunVo>,
	var netAttachment: List<NetworkAttachmentVo>,
)

data class ImageFileVo(
	var id: String,
	var name: String,
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
	var nics: List<VmNicVo>,
	var id: String,
	var name: String,
	var description: String,
	var selectNics: List<VmNicVo>,
	var memory: BigInteger,
	var maximumMemory: BigInteger,
	var virtualSockets: Int,
	var coresPerVirtualSocket: Int,
	var threadsPerCore: Int,
	var affinity: String,
	var customMigrationUsed: Boolean,
	var customMigrationDowntimeUsed: Boolean,
	var customMigration: String,
	var customMigrationDowntime: BigInteger,
	var autoConverge: String,
	var compressed: String,
	var highAvailability: Boolean,
	var priority: BigInteger,
	var watchdogModel: String,
	var watchdogAction: String,
	var firstDevice: String,
	var secondDevice: String,
	var physicalMemory: BigInteger,
	var memoryBalloon: Boolean,
	var ioThreadsEnabled: BigInteger,
	var virtioScsiEnabled: Boolean,
)

data class IscsiVo(
	var address: String,
	var port: String,
	var authAt: String,
	var id: String,
	var password: String,
	var target: String,
	var loginAt: Boolean,
)

data class LunVo(
	var id: String,
	var size: String,
	var path: String,
	var vendor: String,
	var productId: String,
	var serial: String,
	var type: String,
	var name: String,
	var description: String,
	var diskId: String,
	var hostId: String,
)

data class MacAddressPoolsVo(
	var id: String,
	var name: String,
	var userName: String,
	var authorizationProvider: String,
	var nameSpace: String,
	var role: String,
	var allowDuplicates: Boolean,
	var macAdressFrom: String,
	var macAddressTo: String,
	var description: String,
	var ranges: List<MacAddressPoolsVo>,
	var from: String,
	var to: String,
)

data class MessageVo(
	var title: String,
	var text: String,
	var style: String,	
) {
	companion object {
		const val INFO: String = "info";
		const val ERROR: String = "error";
		const val WARNING: String = "warning";
		const val SUCCESS: String = "success";
	}
}

data class NetworkAttachmentVo(
	var dnsServer: List<String>,
	var bootProtocol: String,
	var nicAddress: String,
	var nicGateway: String,
	var nicNetmask: String,
	var nicNetworkName: String,
	var nicNetworkId: String,
	var hostNicName: String,
	var hostNicId: String,
	var netHostId: String,
	var netHostName: String,
	var netAttachmentId: String,
)

data class NetworkProviderVo(
	var id: String,
	var name: String,
	var description: String,
	var authenticationUrl: String,
	var requiresAuthentication: Boolean,
	var url: String,
	var username: String,
	var autoSync: Boolean,
	var externalPluginType: String,
	var type: String,
)

data class NetworkVo(
	var id: String,
	var name: String,
	var description: String,
	var comment: String,
)

data class NicUsageApiVo(
	var id: String,
	var name: String,
	var macAddress: String,
	var ipAddress: String,
	var dataCurrentRx: BigDecimal,
	var dataCurrentTx: BigDecimal,
	var dataCurrentRxBps: BigDecimal,
	var dataCurrentTxBps: BigDecimal,
	var dataTotalRx: BigDecimal,
	var dataTotalTx: BigDecimal,
	var bondingMode: String,
	var bondingModeName: String,
	var bonding: List<NicUsageApiVo>,
	var networkId: String,
	var networkName: String,
	var status: String,
	var hostId: String,
	var hostName: String,
	var networkAttachmentId: String,
	var checkBonding: Boolean,
	var vlan: BigInteger,
	var baseInterface: String,
	var base: String,
	var vlanNetworkList: List<String>,
	var nicExNetExist: Boolean,
	var insertSlave: Boolean,
	var unBondName: String,
)

data class NicUsageVo(
	var hostInterfaceId: String,
	var hostInterfaceName: String,
	var vmInterfaceId: String,
	var vmInterfaceName: String,
	var receiveRatePercent: String,
	var transmitRatePercent: String,
	var receivedTotalByte: String,
	var transmittedTotalByte: String,
	var historyDatetime: String,
	var macAddress: String,
)

data class OsInfoVo(
	var name: String,
	var id: String,  
)

data class PermissionVo(
	var id: String,
	var administrative: Boolean,
	var user: String,
	var authProvider: String,
	var namespace: String,
	var role: String,
)

data class ProviderVo(
	var id: String,
	var name: String,
	var description: String,
	var providerType: String,
	var providerUrl: String,  
)

data class QuotaClusterLimitVo(
	var clusterId: String,
	var clusterName: String,
	var memoryUsage: BigDecimal,
	var memoryLimit: BigDecimal,
	var vCpuUsage: BigInteger,
	var vCpuLimit: BigInteger,
)

data class QuotaCreateVo(
	var id: String,
	var name: String,
	var description: String,
	var clusterHardLimitPct: BigInteger,
	var clusterSoftLimitPct: BigInteger,
	var storageHardLimitPct: BigInteger,
	var storageSoftLimitPct: BigInteger,
	var quotaClusterType: String,
	var quotaStorageType: String,
	var quotaClusterList: List<QuotaClusterLimitVo>,
	var quotaStorageDomainList: List<QuotaStorageLimitVo>,
)

data class QuotaStorageLimitVo(
	var storageDomainId: String,
	var storageDomainName: String,
	var storageUsage: BigDecimal,
	var storageLimit: BigInteger,
)

data class QuotaVo(
	var id: String,
	var name: String,
	var comment: String,
	var description: String,
	var clusterHardLimitPct: BigInteger,
	var clusterSoftLimitPct: BigInteger,
	var storageHardLimitPct: BigInteger,
	var storageSoftLimitPct: BigInteger,
	var memoryUsageTotal: BigDecimal,
	var memoryLimitTotal: BigDecimal,
	var vCpuUsageTotal: BigInteger,
	var vCpuLimitTotal: BigInteger,
	var storageUsageTotal: BigDecimal,
	var storageLimitTotal: BigInteger,
	var quotaClusterLimitList: List<QuotaClusterLimitVo>,
	var quotaStorageLimitList: List<QuotaStorageLimitVo>,
)

data class RoleVo(
	var id: String,
	var name: String,
	var administrative: Boolean,
	var mutable: Boolean,  
)

data class SnapshotVo(
	var vmId: String,
	var memoryRestore: Boolean,
	var id: String,
	var date: Long,
	var status: String,
	var memory: Boolean,
	var description: String,
	var disks: List<DiskVo>,
	var nics: List<VmNicVo>,
)

data class SshVo(
	var id: String,
	var password: String,
	var address: String,
	var port: Int,
	var publicKey: String,
)

data class StorageDomainCreateVo(
	var id: String,
	var name: String,
	var description: String,
	var domainType: String,
	var storageType: String,
	var wipeAfterDelete: Boolean,
	var discardAfterDelete: Boolean,
	var hostId: String,
	var path: String,
	var iscsi: IscsiVo,
	var importAt: Boolean,
	var lunVos: List<LunVo>,  
)


data class StorageDomainUsageVo(
	var storageDomainId: String,
	var availableDiskSizeGb: Int,
	var usedDiskSizeGb: Int,
	var storageDomainStatus: Int,
	var historyDatetime: String,
)

data class StorageDomainVo(
	var id: String,
	var name: String,
	var description: String,
	var comment: String,
	var type: String,
	var format: Boolean,
	var status: String,
	var storageAddress: String,
	var storagePath: String,
	var storageType: String,
	var storageFormat: String,
	var storageDomainInfo: String,
	var diskFree: BigInteger,
	var diskUsed: BigInteger,
	var diskProfileId: String,
	var diskProfileName: String,
    var storageDomainUsages: List<List<String>>,
    var diskVoList: List<DiskVo>,
    var diskSnapshotVoList: List<DiskVo>,
    var imageFileList: List<ImageFileVo>,
)

data class StorageVo(
	var storageDomainId: String,
	var historyDatetime: String,
	var availableDiskSizeGb: Int,
	var usedDiskSizeGb: Int,
	var storageDomainStatus: Int,  
)

data class SystemPropertiesVo(
   var id: String,
   var password: String,
   var ip: String,
   var vncIp: String,
   var vncPort: String,
   var cpuThreshold: Int,
   var memoryThreshold: Int,
   var grafanaUri: String,
   var deepLearningUri: String,
   var symphonyPowerControll: Boolean,
   var loginLimit: Int,
)

data class TemplateDiskVo(
  var id: String, 
  var name: String, 
  var description: String, 
  var virtualSize: String, 
  var actualSize: String, 
  var status: String, 
  var format: String, 
  var type: String, 
  var storageDomainId: String, 
  var diskProfileId: String, 
  var quotaId: String, 
  var storageDomains: List<StorageDomainVo>, 
  var diskProfiles: List<DiskProfileVo>, 
  var quotas: List<QuotaVo>, 
)

data class TemplateEditVo(
	var clusters: List<ClusterVo>,
	var operatingSystems: List<OsInfoVo>,
	var hosts: List<HostVo>,
	var leaseStorageDomains: List<StorageDomainVo>,
	var bootImages: List<StorageDomainVo>,
	var cpuProfiles: List<CpuProfileVo>,
	var id: String,
	var cluster: String,
	var operatingSystem: String,
	var type: String,
	var name: String,
	var subName: String,
	var description: String,
	var stateless: Boolean,
	var startInPause: Boolean,
	var deleteProtection: Boolean,
	var videoType: String,
	var graphicsProtocol: String,
	var usbSupport: Boolean,
	var disconnectAction: String,
	var monitors: Int,
	var singlePci: Boolean,
	var singleSignOn: Boolean,
	var smartcard: Boolean,
	var virtIO: Boolean,
	var memory: BigInteger,
	var maximumMemory: BigInteger,
	var virtualSockets: Int,
	var coresPerVirtualSocket: Int,
	var threadsPerCore: Int,
	var recommendHost: String,
	var targetHost: String,
	var affinity: String,
	var customMigrationUsed: Boolean,
	var customMigrationDowntimeUsed: Boolean,
	var customMigration: String,
	var customMigrationDowntime: BigInteger,
	var autoConverge: String,
	var compressed: String,
	var useCloudInit: Boolean,
	var hostName: String,
	var timezone: String,
	var customScript: String,
	var highAvailability: Boolean,
	var leaseStorageDomain: String,
	var resumeBehaviour: String,
	var priority: BigInteger,
	var watchdogModel: String,
	var watchdogAction: String,
	var firstDevice: String,
	var secondDevice: String,
	var bootImageUse: Boolean,
	var bootImage: String,
	var imageStorage: String,
	var cpuProfile: String,
	var cpuShare: BigInteger,
	var physicalMemory: BigInteger,
	var memoryBalloon: Boolean,
	var ioThreadsEnabled: BigInteger,
	var virtioScsiEnabled: Boolean,
)

data class TemplateVo(
	var orgVmId: String,
	var id: String,
	var name: String,
	var versionName: String,
	var version: String,
	var description: String,
	var creationTime: Long,
	var status: String,
	var forceOverride: Boolean,
	var os: String,
	var systemInfo: VmSystemVo,
	var vms: List<VmVo>,
	var nics: List<VmNicVo>,
	var events: List<EventVo>,
	var cluster: ClusterVo,
	var cpuProfileId: String,
	var quotaId: String,
	var templateDisks: List<TemplateDiskVo>,
	var rootTemplateId: String,
	var rootTemplates: List<TemplateVo>,
	var subVersionName: String,
	var allUserAccess: Boolean,
	var clonePermissions: Boolean,
	var seal: Boolean,
	var diskAttachmentSize: Int,
)

data class UsageVo(
	var cpuUsages: Int,
	var memoryUsages: Int,
	var networkUsages: Int,
	var storageUsages: Int,
	var transitUsages: Int,
	var receiveUsages: Int,
	var usageDate: String,
	var storageUsageDate: String,
)

data class UserVo(
	var no: Int,
	var id: String,
	var password: String,
	var newPassword: String,
	var administrative: Boolean,
	var name: String,
	var lastName: String,
	var principal: String,
	var namespace: String,
	var email: String,
	var authProvider: String,
	var roleId: String,
	var loginCount: Int,
	var blockTime: String,
)

data class VmConsoleVo(
	var type: String,
	var address: String,
	var port: String,
	var tlsPort: String,
	var passwd: String,
	var vmName: String,
	var hostAddress: String,
	var hostPort: String,
)

data class VmCreateVo(
	var clusters: List<ClusterVo>,
	var templates: List<TemplateVo>,
	var operatingSystems: List<OsInfoVo>,
	var instanceTypes: List<InstanceTypeVo>,
	var nics: List<VmNicVo>,
	var hosts: List<HostVo>,
	var leaseStorageDomains: List<StorageDomainVo>,
	var bootImages: List<StorageDomainVo>,
	var cpuProfiles: List<CpuProfileVo>,
	var id: String,
	var status: String,
	var cluster: String,
	var template: String,
	var operatingSystem: String,
	var instanceType: String,
	var type: String,
	var name: String,
	var description: String,
	var use: String,
	var disks: List<DiskVo>,
	var newDisk: DiskVo,
	var selectNics: List<VmNicVo>,
	var exSelectNics: List<VmNicVo>,
	var snapshotId: String,
	var videoType: String,
	var graphicsProtocol: String,
	var usbSupport: Boolean,
	var disconnectAction: String,
	var monitors: Int,
	var singlePci: Boolean,
	var singleSignOn: Boolean,
	var smartcard: Boolean,
	var virtIO: Boolean,
	var memory: BigInteger,
	var maximumMemory: BigInteger,
	var virtualSockets: Int,
	var coresPerVirtualSocket: Int,
	var threadsPerCore: Int,
	var pickHost: String,
	var recommendHost: String,
	var targetHost: String,
	var affinity: String,
	var customMigrationUsed: Boolean,
	var customMigrationDowntimeUsed: Boolean,
	var customMigration: String,
	var customMigrationDowntime: BigInteger,
	var autoConverge: String,
	var compressed: String,
	var useCloudInit: Boolean,
	var hostName: String,
	var timezone: String,
	var customScript: String,
	var highAvailability: Boolean,
	var leaseStorageDomain: String,
	var resumeBehaviour: String,
	var priority: BigInteger,
	var watchdogModel: String,
	var watchdogAction: String,
	var firstDevice: String,
	var secondDevice: String,
	var bootImageUse: Boolean,
	var headlessMode: Boolean,
	var bootImage: String,
	var imageStorage: String,
	var cpuProfile: String,
	var cpuShare: BigInteger,
	var physicalMemory: BigInteger,
	var memoryBalloon: Boolean,
	var ioThreadsEnabled: BigInteger,
	var virtioScsiEnabled: Boolean,
	var periodDuration: Long,
	var bytesPerPeriod: Long,
	var deviceSource: String,
)

data class VmDeviceVo(
	var historyId: String,
	var type: String,
	var address: String,
	var readonly: Boolean,
	var plugged: Boolean,
	var managed: Boolean,
	var deviceId: String,
)

data class VmNetworkUsageVo(
	var historyDatetime: String,
	var receiveRatePercent: Int,
)

data class VmNicVo(
	var networkId: String,
	var id: String,
	var name: String,
	var nicId: String,
	var nicName: String,
	var networkName: String,
	var profileName: String,
	var ipv4: String,
	var ipv6: String,
	var macAddress: String,
	var status: Boolean,
	var plugged: Boolean,
	var linked: Boolean,
	var vm: VmContainer,
	var vnicProfile: VnicProfileContainer,
	var interfaceType: String,
	var profileId: String,
	var profileList: List<VmNicVo>,
)

data class VmSummaryVo(
	var id: String,
	var name: String,
	var description: String,
	var comment: String,
	var address: String,
	var status: String,
	var osHostName: String,
	var hostId: String,
	var hostName: String,
	var memoryInstalled: BigDecimal,
	var memoryUsed: BigDecimal,
	var memoryFree: BigDecimal,
	var memoryBuffered: BigDecimal,
	var memoryCached: BigDecimal,
	var cpuCurrentGuest: BigDecimal,
	var cpuCurrentHypervisor: BigDecimal,
	var cpuCurrentTotal: BigDecimal,
	var vmLastUsage: VmUsageVo,
	var vmNicsLastUsage: List<NicUsageVo>,
)

data class VmSystemVo(
	var definedMemory: String,
	var guaranteedMemory: String,
	var maxMemoryPolicy: String,
	var totalVirtualCpus: Int,
	var virtualSockets: Int,
	var coresPerVirtualSocket: Int,
	var threadsPerCore: Int,
)

data class VmUsageVo(
	var historyDatetime: String,
	var cpuUsagePercent: Int,
	var memoryUsagePercent: Int,
)

data class VmVo(
	var id: String,
	var name: String,
	var comment: String,
	var description: String,
	var use: String,
	var runHost: String,
	var ipAddress: String,
	var fqdn: String,
	var template: String,
	var os: String,
	var orgin: String,
	var timeOffset: String,
	var host: String,
	var hostId: String,
	var hostName: String,
	var type: String,
	var cluster: String,
	var clusterId: String,
	var dataCenter: String,
	var status: String,
	var graphicProtocol: String,
	var startTime: String,
	var nextRunConfigurationExists: Boolean,
	var diskDetach: Boolean,
	var headlessMode: Boolean,
	var diskSize: Int,
	var disc: String,
	var cdate: String,
	var vmSystem: VmSystemVo,
	var vmNics: List<VmNicVo>,
	var disks: List<DiskVo>,
	var snapshots: List<SnapshotVo>,
	var role: List<Map<String, Object>>,
	var cpuUsage: List<List<String>>,
	var memoryUsage: List<List<String>>,
	var networkUsage: List<List<String>>,
	var devices: List<VmDeviceVo>,
	var events: List<EventVo>,
	var profileList: List<VmNicVo>,
	var usageVos: List<UsageVo>,
	var cpuUsages: List<Int>,
	var memoryUsages: List<Int>,
	var networkUsages: List<Int>,
)

data class VnicProfileVo(
	var name: String,
	var id: String,
	var networkFilter: NetworkFilterContainer,
	var network: NetworkContainer,
	var passThrough: VnicPassThroughContainer,
	var portMirroring: Boolean,
)