//package com.itinfo.itcloud.model
//
//import com.itinfo.itcloud.model.computing.ApplicationVo
//import com.itinfo.itcloud.model.network.NetworkUsageVo
//import com.itinfo.itcloud.model.setting.GroupVo
//import com.itinfo.itcloud.model.storage.DiskVo
//import java.math.BigDecimal
//import java.math.BigInteger
//import java.util.*
//
//
//data class DashboardVo (
//	val name: String = "",
//	val id: String = "",
//
//	val datacenterCnt: Int = 0,
//	val datacenterActive: Int = 0,
//	val datacenterInactive: Int = 0,
//
//	val clusterCnt: Int = 0,
//
//	val hostCnt: Int = 0,
//	val hostActive: Int = 0,
//	val hostInactive: Int = 0,
//
//	val storageDomainCnt: Int = 0,
//	val storageDomainActive: Int = 0,
//	val storageDomainInactive: Int = 0,
//
//	val vmCnt: Int = 0,
//	val vmActive: Int = 0,
//	val vmInactive: Int = 0,
//
//	val cpuTotal: Int = 0,
//	val cpuAssigned: Int = 0 ,
//	val cpuCommit: Int = 0 ,
//
//	val memoryTotal: BigDecimal = BigDecimal.ZERO,
//	val memoryUsed: BigDecimal = BigDecimal.ZERO,
//	val memoryFree: BigDecimal = BigDecimal.ZERO,
//	val storageTotal: BigDecimal = BigDecimal.ZERO,
//	val storageUsed: BigDecimal = BigDecimal.ZERO,
//	val storageFree: BigDecimal = BigDecimal.ZERO,
//)
//
//
//
//
//
//data class DataCenterVo(
//	var id: String = "",
//	var name: String = "",
//	var comment: String =  "",
//	var description: String =  "",
//	var quotaMode: String =  "", 		// QuotaMode: 비활성화됨, 감사, 강제적용
//	var storageType: Boolean = false,  	// local
//	var version: String =  "",
//	var status: String =  "",
//	var networkList: List<NetworkVo>? = arrayListOf()
//)
//
//data class ClusterVo(
//	var id: String = "",
//	var name: String = "",
//	var description: String = "",
//	var comment: String = "",
//	var ballooningEnabled: Boolean = false,
//	var cpuType: String = "",
//	var threadsAsCore: Boolean = false,
//	var memoryOverCommit: Int = 0,
//	var restoration: String = "",
//	var chipsetFirmwareType: String = "",
//	var version: String = "",
//	var guster: Boolean = false,
//	var virt: Boolean = false,
//	var datacenterId: String = "",
//	var	datacenterName: String = "",
//
//	var hostCnt: Int = 0,
//	var hostUpCnt: Int = 0,
//	var hostDownCnt: Int = 0,
//	var vmCnt: Int = 0,
//	var vmUpCnt: Int  = 0,
//	var vmDownCnt: Int  = 0
//)
//
//data class HostVo(
//	var id: String = "",
//	var name: String = "",
//	var comment: String = "",
//	var address: String = "",
//	var status: String = "",
//
//	var cpuTopologyCore: Int = 0,
//	var cpuTopologySocket: Int = 0,
//	var cpuTopologyThread: Int = 0,
//	var cpuCnt: Int = 0,
//	var cpuOnline: List<Int> = arrayListOf(),
//
//	var devicePassThrough: Boolean = false,
//	var iscsi: String = "",
//	var kdump: String = "",
//	var bootingTime: String = "",
//
//	var memory: BigInteger  = BigInteger.ZERO,
//	var memoryUsed: BigInteger  = BigInteger.ZERO,
//	var memoryFree: BigInteger  = BigInteger.ZERO,
//	var memoryMax: BigInteger  = BigInteger.ZERO,
//	var memoryShared: BigInteger  = BigInteger.ZERO,
//	var swapTotal: BigInteger  = BigInteger.ZERO,
//	var swapUsed: BigInteger  = BigInteger.ZERO,
//	var swapFree: BigInteger  = BigInteger.ZERO,
//
//	var seLinux: String = "",
//	var spmStatus: String = "",
//	var spmPriority: Int = 0,
//
//	var hostedEngine: Boolean = false,
//	var hostedActive: Boolean = false,
//	var hostedScore: Int = 0,
//
//	var ksm: Boolean = false,
//	var pageSize: Boolean = false,
//
//	var hugePage2048Free: Int = 0,
//	var hugePage2048Total: Int = 0,
//	var hugePage1048576Free: Int = 0,
//	var hugePage1048576Total: Int = 0,
//
//	var clusterVer: String = "",
//	var clusterId: String = "",
//	var clusterName: String = "",
//	var datacenterId: String = "",
//	var datacenterName: String = "",
//
//	var vmCnt: Int = 0,
//	var vmUpCnt: Int = 0,
//
//	var hostHwVo: HostHwVo? = null,
//	var hostSwVo: HostSwVo? = null,
//	var nicVo: NicVo? = null,
//)
//
//data class HostHwVo (
//	var manufacturer: String = "",
//	var family: String = "",
//	var productName: String = "",
//	var serialNum: String = "",
//	var uuid: String = "",
//	var hwVersion: String = "",
//
//	var cpuName: String = "",
//	var cpuType: String = "",
//	var cpuSocket: Int = 0,
//	var coreThread: Int = 0,
//	var coreSocket: Int = 0
//)
//
//data class HostSwVo (
//	var osVersion: String = "",
//	var osInfo: String = "",
//	var kernalVersion: String = "",
//	var kvmVersion: String = "",
//	var libvirtVersion: String = "",
//	var vdsmVersion: String = "",
//	var spiceVersion: String = "",
//	var glustersfsVersion: String = "",
//	var cephVersion: String = "",
//	var openVswitchVersion: String = "",
//	var nmstateVersion: String = ""
//)
//
//data class HostDeviceVo (
//	var name: String = "",
//	var capability: String = "",
//	var vendorId: String = "",
//	var vendorName: String = "",
//	var productId: String = "",
//	var productName: String = "",
//	var driver: String = "",
//)
//
//
//
//data class VmVo(
//	var id: String = "",
//	var name: String = "",
//	var status: String = "",
//	var description: String = "",
//	var fqdn: String = "",
//	var upTime: String = "",
//
//	var osSystem: String = "",
//	var chipsetFirmwareType: String = "",
//	var priority: Int = 0,
//	var optimizeOption: String = "",
//
//	var memory: BigInteger = BigInteger.ZERO,
//	var memoryActual: BigInteger = BigInteger.ZERO,
//	var cpuCoreCnt: Int = 0,
//	var cpuTopologyCore: Int = 0,
//	var cpuTopologySocket: Int = 0,
//	var cpuTopologyThread: Int = 0,
//	var guestCpuCnt: Int = 0,
//	var guestCpu: String = "",
//	var ha: Boolean = false,
//	var usb: Boolean = false,
//	var monitor: Int = 0,
//	var hwTimeOffset: String = "",
//
//	var guestMemory: BigInteger = BigInteger.ZERO,
//	var guestBufferedMemory: BigInteger = BigInteger.ZERO,
//
//	var datacenterId: String  = "",
//	var datacenterName: String  = "",
//	var clusterId: String  = "",
//	var clusterName: String  = "",
//	var hostId: String  = "",
//	var hostName: String  = "",
//	var templateID: String  = "",
//	var templateName: String  = "",
//	var ipv4: String  = "",
//	var ipv6: String  = "",
//)
//
//data class VmDiskVo (
//	 var id: String = "",
//	 var name: String = "",
//
//	 var active: Boolean = false,
//	 var bootAble: Boolean = false,
//	 var passDiscard: Boolean = false,
//	 var readOnly: Boolean = false,
//	 var useScsi: Boolean = false,
//
//	 var interfaceName: String = "",
//	 var logicalName: String = "",
//
//	 var virtualSize: BigInteger = BigInteger.ZERO,
//	 var connection: String = "",
//	 var status: String = "",
//	 var type: String = "",
//	 var description: String = "",
//)
//
//data class TemplateVo (
//	var id: String = "",
//	var name: String = "",
//	var description: String = "",
//	var comment: String = "",
//	var status: String = "",
//	var version: String = "",
//	var createDate: String = "",
//	var osType: String = "",
//	var chipsetFirmwareType: String = "",
//	var optimizeOption: String = "",
//
//	var memory: BigInteger? = null,
//	var cpuCnt: Int = 0,
//	var cpuCoreCnt: Int = 0,
//	var cpuSocketCnt: Int = 0,
//	var cpuThreadCnt: Int = 0,
//	var monitor: Int = 0,
//	var priority: Int = 0,
//
//	var ha: Boolean = false,
//	var usb: Boolean = false,
//
//	var origin: String = "",
//	var hostCluser: String = "",
//	var clusterID: String = "",
//	var clusterName: String = "",
//	var datacenterId: String = "",
//	var datacenterName: String = "",
//)
//
//
//data class NicVo(
//	var id: String = "",
//	var name: String = "",
//	var macAddress: String = "",
//	var status: String = "",
//
//	var rxSpeed: String = "",
//	var txSpeed: String = "",
//	var rxTotalSpeed: String = "",
//	var txTotalSpeed: String = "",
//	var speed: String = "",
//	var stop: String = "",
//
//	var networkName: String = "",
//	var ipv4: String = "",
//	var ipv6: String = "",
//	var vLan: String = "",
//
//	var plugged: Boolean = false,
//	var linkStatus: Boolean = false,
//	var type: String = "",
//	var speed2: BigInteger = BigInteger.ZERO,
//	var guestInterface: String = "",
//	var vnicProfileVo: VnicProfileVo? = null
//)
//
//
//data class NetworkVo (
//	var id: String = "",
//	var name: String = "",
//	var description: String = "",
//	var comment: String = "",
//
//	var mtu: Int = 0,
//	var portIsolation: Boolean = false,
//	var stp: Boolean = false,
//	var required: Boolean = false,
//	var vdsmName: String = "",
//	var vlan: BigInteger = BigInteger.ZERO,
//	var label: String = "",
//	var providerId: String = "",
//	var providerName: String = "",
//
//	var datacenterId: String = "",
//	var datacenterName: String = "",
//	var clusterId: String = "",
//	var clusterName: String = "",
//	var status: String = "",
//	var networkUsageVo: NetworkUsageVo? = null,
//)
//
//data class NetworkClusterVo (
//	var id: String = "",
//	var name: String = "",
//	var description: String = "",
//	var version: String = "",
//	var connected: Boolean = false,
//	var status: String = "",
//	var required: Boolean = false,
//	var networkUsageVo: NetworkUsageVo? = null
//)
//
//data class NetworkHostVo (
//	var hostId: String = "",
//	var hostName: String = "",
//	var hostStatus: String = "",
//	var clusterName: String = "",
//	var datacenterName: String = "",
//	var networkStatus: String = "",
//	var asynchronism: String = "",
//	var networkDevice: String = "",
//	var rxSpeed: String = "",
//	var rxTotalSpeed: String = "",
//	var txSpeed: String = "",
//	var txTotalSpeed: String = "",
//	var speed: BigInteger = BigInteger.ZERO
//)
//
//data class NetworkVmVo (
//	var vmId: String = "",
//	var vmName: String = "",
//	var clusterId: String = "",
//	var clusterName: String = "",
//	var ipv4: String = "",
//	var ipv6: String = "",
//	var fqdn: String = "",
//	var status: String = "",
//
//	var vnicStatus: Boolean = false,
//	var vnicName: String = "",
//	var vnicRx: String = "",
//	var vnicTx: String = "",
//	var rxTotalSpeed: String = "",
//	var txTotalSpeed: String = "",
//	var description: String = "",
//)
//
//data class NetworkUsageVo (
//	var vm: Boolean = false,
//	var management: Boolean = false,
//	var display: Boolean = false,
//	var migration: Boolean = false,
//	var gluster: Boolean = false,
//	var defaultRoute: Boolean = false,
//)
//
//data class VnicProfileVo (
//	var id: String = "",
//	var name: String = "",
//	var passThrough: String = "",
//	var portMirroring: Boolean = false,
//	var description: String = "",
//	var version: String = "",
//	var networkId: String = "",
//	var networkName: String = "",
//	var networkFilterId: String = "",
//	var networkFilterName: String = "",
//	var datacenterId: String = "",
//	var datacenterName: String = "",
//)
//
//data class AffinityGroupVo (
//	var id: String  = "",
//	var name: String  = "",
//	var description: String  = "",
//	var status: Boolean = false,
//	var priority: Int = 0,
//
//	var positive: Boolean = false,
//	var enforcing: Boolean = false,
//
//	var vmEnabled: Boolean = false,
//	var vmPositive: Boolean = false,
//	var vmEnforcing: Boolean = false,
//
//	var hostEnabled: Boolean = false,
//	var hostPositive: Boolean = false,
//	var hostEnforcing: Boolean = false,
//
//	var vmLabels: List<String>? = null,
//	var hostLabels: List<String>? = null,
//
//	var vmList: List<String>? = null,
//	var hostList: List<String>? = null,
//)
//
//
//data class AffinityLabelVo (
//	 var id: String = "",
//	 var name: String = "",
//	 var clusterName: String = "",
//
//	 var hostsLabel: List<HostVo>? = null,
//	 var vmsLabel: List<VmVo>? = null,
//	 var hosts: List<String>? = null,
//	 var vms: List<String>? = null
//)
//
//data class ApplicationVo(
//	var id: String = "",
//	var name: String = ""
//)
//
//data class SnapshotVo (
//	var name: String = "",
//	var description: String = "",
//	var date: String = "",
//	var persistMemory: Boolean = false,
//	var status: String = "",
//	var type: String = "",
//	var diskVo: DiskVo? = null,
//	var nicVo: NicVo? = null,
//	var applicationVo: ApplicationVo? = null
//)
//
//
//data class CpuProfileVo(
//	var name: String = "",
//	var description: String = "",
//	var qosName: String = ""
//)
//
//data class EventVo (
//	var id: String = "",
//	var severity: String = "",
//	var time: String = "",
//	var message: String = "",
//	var relationId: String = "",
//	var source: String = "",
//	var eventId: String = "",
//
//	var datacenterName: String = "",
//	var clusterName: String = "",
//	var hostName: String = "",
//	var vmName: String = "",
//	var templateName: String = "",
//)
//
//class GuestInfoVo (
//	var type: String = "",
//	var architecture: String = "",
//	var os: String = "",
//	var kernalVersion: String = "",
//	var guestTime: String = "",
//)
//
//data class PermissionVo (
//	var permissionId: String = "",
//	var datacenterName: String = "",
//	var clusterName: String = "",
//
//	var user: String = "",
//	var nameSpace: String = "",
//	var role: String = "",
//
//	var createDate: Date? = null,
//	var provider: String = "",
//	var inheritedFrom: String = "",
//
//	var groupVoList: List<GroupVo>? = null,
////	var userVoList: List<UserVo>? = null,
//)
//
//
//
//
//
//
//
//
//
