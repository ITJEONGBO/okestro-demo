//package com.itinfo.itcloud.model
//
//import org.ovirt.engine.sdk4.types.*
//import java.math.BigInteger
//
//data class DataCenterCreateVo(
//	var id : String = "",
//	var name : String = "",
//	var comment : String = "",
//	var description : String = "",
//	var version : String = "",
//	var quotaMode : QuotaModeType = QuotaModeType.AUDIT,
//	var storageType : Boolean = false,
//)
//
//data class ClusterCreateVo(
//	var datacenterId : String = "",
//	var datacenterName : String = "",
//	var id : String = "",
//	var name : String = "",
//	var description : String = "",
//	var comment : String = "",
//	var networkId : String = "",
//	var networkName : String = "",
//	var cpuArc : Architecture = Architecture.UNDEFINED,
//	var cpuType : String = "",
//	var biosType : BiosType = BiosType.CLUSTER_DEFAULT,
//	var fipsMode : FipsMode = FipsMode.UNDEFINED,
//	var version : String = "",
//	var switchType : SwitchType = SwitchType.LEGACY,
//	var firewallType : FirewallType = FirewallType.FIREWALLD,
//	var networkProvider : Boolean = false,
//	var logMaxMemory : Int = 0,
//	var logMaxType : LogMaxMemoryUsedThresholdType = LogMaxMemoryUsedThresholdType.PERCENTAGE,
//	var virt : Boolean = false,
//	var gluster : Boolean = false,
////	var migrationPolicy : MigrationPolicy? = null,
//	var bandwidth : MigrationBandwidthAssignmentMethod = MigrationBandwidthAssignmentMethod.AUTO,
//	var recoveryPolicy : MigrateOnError = MigrateOnError.MIGRATE,
//	var encrypted : InheritableBoolean = InheritableBoolean.FALSE,
////	var parallel : String = "",
//)
//
//data class HostCreateVo(
//	var id : String = "",
//	var name : String = "",
//	var comment : String = "",
//	var hostIp : String = "",
//	var sshName : String = "",
//	var sshPw : String = "",
//	var sshPort : Int = 0,
//	var sshPublicKey : Boolean = false,
//	var active : Boolean = false,
//	var restart : Boolean = false,
//	var powerManagementActive : Boolean = false,
//	var kdump : Boolean = false,
//	var powerPolicy : Boolean = false,
////	var fenceAgentVo : FenceAgentVo? = null,
//	var spm : Int = 0,
//	var hostEngine : Boolean = false,
//
//	var datacenterName : String = "",
//	var clusterId : String = "",
//	var clusterName : String = "",
//
//	var affinityGroupVoList : List<AffinityGroupVo> ? = null,
//	var affinityLabelVoList : List<AffinityLabelVo> ? = null,
//)
//
//data class VmCreateVo(
//	var clusterId : String = "",
//	var clusterName : String = "",
//	var templateId : String = "",
//	var templateName : String = "",
//	var datacenterName : String = "",
//	var os : String = "",
//	var chipsetType : String = "",
//	var option : VmType = VmType.SERVER,
//
//	var id : String = "",
//	var name : String = "",
//	var description : String = "",
//	var comment : String = "",
//
//	var statusSave : Boolean= false,
//	var startPaused : Boolean = false,
//	var deleteProtected : Boolean = false,
//
//	var vDiskVo : VDiskVo? = null,
//	var vnicList : List<NicVo>? = null,
//	var vmSystemVo : VmSystemVo? = null,
//	var vmHostVo : VDiskVo? = null,
//	var vmHaVo : VmHaVo? = null,
//	var vmResourceVo : VmResourceVo? = null,
//	var vmBootVo : VmSystemVo? = null,
//
//	var affinityGroupVoList : List<AffinityGroupVo> ? = null,
//	var affinityLabelVoList : List<AffinityLabelVo> ? = null
//)
//
//data class VmBootVo(
//	// 부트 옵션
//	var firstDevice: String = "",
//	var secondDevice: String = "",
//	var cdDvdConn : Boolean = false,
//	var connection: String = "",
//	var bootingMenu : Boolean = false
//)
//
//class VmHaVo (
//	// 고가용성
//	var ha : Boolean = false,
//	var vmStorageDomain : String = "",
//	var resumeOperation : String = "",
//	var priority : String = "",
//	var watchDogModel : String = "",
//	var watchDogWork : String = "",
//)
//
//data class VmHostVo (
//	var clusterHost : String = "",
//	var selectHost : String = "",
//	var migrationMode : String = "",
//	var migrationPolicy : String = "",
//	var migrationEncoding : String = "",
//	var parallelMigration : String = "",
//	var numOfVmMigration : String = ""
//)
//
//data class VmResourceVo (
//	// 리소스 할당
//	var cpuProfile : String = "",
//	var cpuShare : String = "",
//	var cpuShareCnt : Int = 0,
//	var cpuPinningPolicy : String = "",
//	var cpuPinningTopology : String = "",
//	var ioThread : Boolean = false,
//	var ioThreadCnt : Int = 0
//)
//
//class VmSystemVo (
//	// 시스템
//	var memorySize : BigInteger = BigInteger.ZERO,
//	var memoryMax : BigInteger = BigInteger.ZERO,
//	var memoryActual : BigInteger = BigInteger.ZERO,
//
//	var vCpuCnt :Int = 0,
//	var vCpuSocket : Int = 0,
//	var vCpuSocketCore : Int = 0,
//	var vCpuCoreThread : Int = 0,
//
//	var userEmulation : String = "",
//	var userCpu : String = "",
//	var userVersion : String = "",
//	var instanceType : String = "",
//	var timeOffset : String = "",
//)
//
//
//
//
//
//
//
//data class VDiskVo (
//	// 새 가상 디스크
//	val vDiskImageVo: VDiskImageVo? = null,
//	val vDiskLunVo: VDiskLunVo? = null,
//	val vDiskBlockVo: VDiskBlockVo? = null
//)
//
//data class VDiskImageVo (
//	var size : String = "",
//	var alias : String = "",
//	var description : String = "",
//	var interfaces : String = "",
//	var storageDomain : String = "",
//	var allocationPolicy : String = "",
//	var diskProfile : String = "",
//
//	var deleteInitialization : Boolean = false,
//	var bootable : Boolean = false,
//	var shareable : Boolean = false,
//	var readOnly : Boolean = false,
//	var cancel : Boolean = false,
//	var backupUse : Boolean = false
//)
//
//class VDiskLunVo (
//	var alias: String = "",
//	var description: String = "",
//	var interfaces: String = "",
//	var host: String = "",
//	var storageType: String = "",
//
//	var bootable : Boolean = false,
//	var shareable : Boolean = false,
//	var readonly : Boolean = false,
//	var cancel : Boolean = false,
//	var scsiPass : Boolean = false,
//	var scsiPermission : Boolean = false,
//	var scsiReservation : Boolean = false,
//)
//
//data class VDiskBlockVo (
//	// 관리되는 블록
//	var size: String = "",
//	var alias: String = "",
//	var description: String = "",
//	var interfaces: String = "",
//	var storageType: String = "",
//
//	var bootable : Boolean = false,
//	var shareable : Boolean = false,
//	var readonly : Boolean = false
//)
//
//
//
//
//
//data class NetworkCreateVo (
//	var id: String = "",
//	var name: String = "",
//
//	var datacenterId: String = "",
//	var datacenterName: String = "",
//	var comment: String = "",
//	var description: String = ""
//)
//
//
//
