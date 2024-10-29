package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.gson
import com.itinfo.itcloud.model.*
import com.itinfo.itcloud.model.network.*
import com.itinfo.util.ovirt.*
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.builders.ClusterBuilder
import org.ovirt.engine.sdk4.builders.CpuBuilder
import org.ovirt.engine.sdk4.builders.DataCenterBuilder
import org.ovirt.engine.sdk4.builders.ErrorHandlingBuilder
import org.ovirt.engine.sdk4.builders.FencingPolicyBuilder
import org.ovirt.engine.sdk4.builders.MigrationBandwidthBuilder
import org.ovirt.engine.sdk4.builders.MigrationOptionsBuilder
import org.ovirt.engine.sdk4.builders.NetworkBuilder
import org.ovirt.engine.sdk4.builders.SkipIfConnectivityBrokenBuilder
import org.ovirt.engine.sdk4.builders.SkipIfSdActiveBuilder
import org.ovirt.engine.sdk4.builders.VersionBuilder
import org.ovirt.engine.sdk4.types.*
import org.slf4j.LoggerFactory
import java.io.Serializable

private val log = LoggerFactory.getLogger(ClusterVo::class.java)

/**
 * [ClusterVo]
 * 클러스터
 *
 * @property id [String]
 * @property name [String]
 * @property description [String]
 * @property comment [String]
 * @property ballooningEnabled [Boolean]
 * @property biosType [BiosType] 칩셋/펌웨어 유형
 * @property cpuArc [Architecture] cpu 아키텍쳐 cpu().architecture()
 * @property cpuType [String] cpu 유형 cpu().type()
 * @property errorHandling [MigrateOnError] 복구정책 <error_handling> <on_error>migrate_highly_available</on_error> </error_handling>
 * @property fipsMode [FipsMode] FIPS 모드
 * @property firewallType [FirewallType] 방화벽 유형
 * @property glusterService [Boolean] Gluster 서비스 활성화
 * @property haReservation [Boolean]
 * @property logMaxMemory [Long] 로그의 최대 메모리 한계
 * @property logMaxMemoryType [LogMaxMemoryUsedThresholdType] 로그의 최대 메모리 타입 (absolute_value_in_mb, percentage)
 * @property memoryOverCommit [Int]
 * @property migrationPolicy [InheritableBoolean] 마이그레이션 정책 migration-auto_coverage
 * @property bandwidth [MigrationBandwidthAssignmentMethod]  마이그레이션 대역폭
 * @property encrypted [InheritableBoolean]  마이그레이션 추가속성- 암호화 사용
 * @property switchType [SwitchType] 스위치 유형
 * @property threadsAsCores [Boolean]
 * @property version [String]
 * @property virtService [Boolean] virt 서비스 활성화
 * @property networkProvider [Boolean] 네트워크 공급자 여부 (clusters/id/externalnetworkproviders 에 포함되는지)
 * @property datacenterVo [IdentifiedVo]
 * @property networkVo [IdentifiedVo] // 관리네트워크
 * @property hostSize [SizeVo]
 * @property vmSize [SizeVo]
 *
 * @property hostVos List<[IdentifiedVo]>
 * @property networkVos List<[IdentifiedVo]>
 * @property templateVos List<[IdentifiedVo]>

// * @property networkProperty [NetworkPropertyVo]
 *
// * @property attached [Boolean]
 * @property required [Boolean]
 **/
class ClusterVo(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val comment: String = "",
    val isConnected: Boolean = false,
    val ballooningEnabled: Boolean = false,
    val biosType: String /*BiosType*/ = "",
    val cpuArc: String/*Architecture*/ = "",
    val cpuType: String = "",
    val errorHandling: String/*MigrateOnError*/ = "",
    val fipsMode: FipsMode = FipsMode.UNDEFINED,
    val firewallType: FirewallType = FirewallType.FIREWALLD,
    val glusterService: Boolean = false,
    val haReservation: Boolean = false,
    val logMaxMemory: Long = 0L,
    val logMaxMemoryType: LogMaxMemoryUsedThresholdType = LogMaxMemoryUsedThresholdType.PERCENTAGE,
    val memoryOverCommit: Int = 0,
    val migrationPolicy: InheritableBoolean = InheritableBoolean.INHERIT,
    val bandwidth: MigrationBandwidthAssignmentMethod = MigrationBandwidthAssignmentMethod.AUTO,
    val encrypted: InheritableBoolean = InheritableBoolean.INHERIT,
    val switchType: SwitchType = SwitchType.LEGACY,
    val threadsAsCores: Boolean = false,
    val version: String = "",
    val virtService: Boolean = false,
    val networkProvider: Boolean = false,
	val datacenterVo: IdentifiedVo = IdentifiedVo(),
    val networkVo: NetworkVo = NetworkVo(), // 관리네트워크
    val hostSize: SizeVo = SizeVo(),
    val vmSize: SizeVo = SizeVo(),

    val hostVos: List<IdentifiedVo> = listOf(),
    val networkVos: List<IdentifiedVo> = listOf(), // 관리네트워크가 핵심, 다른 네트워크 존재가능
    val templateVos: List<IdentifiedVo> = listOf(),

	val required: Boolean = false, // 네트워크 생성시 필수 지정
): Serializable {
	override fun toString(): String =
		gson.toJson(this)

	class Builder {
		private var bId: String = "";fun id(block: () -> String?) { bId = block() ?: "" }
		private var bName: String = "";fun name(block: () -> String?) { bName = block() ?: "" }
		private var bDescription: String = "";fun description(block: () -> String?) { bDescription = block() ?: "" }
		private var bComment: String = "";fun comment(block: () -> String?) { bComment = block() ?: "" }
		private var bIsConnected: Boolean = false; fun isConnected(block: () -> Boolean?) { bIsConnected = block() ?: false }
		private var bBallooningEnabled: Boolean = false; fun ballooningEnabled(block: () -> Boolean?) { bBallooningEnabled = block() ?: false }
		private var bBiosType: String = ""; fun biosType(block: () -> String?) { bBiosType = block() ?: "" }
		private var bCpuArc: String = ""; fun cpuArc(block: () -> String?) { bCpuArc = block() ?: "" }
		private var bCpuType: String = ""; fun cpuType(block: () -> String?) { bCpuType = block() ?: "" }
		private var bErrorHandling: String = ""; fun errorHandling(block: () -> String?) { bErrorHandling = block() ?: "" }
		private var bFipsMode: FipsMode = FipsMode.UNDEFINED; fun fipsMode(block: () -> FipsMode?) { bFipsMode = block() ?: FipsMode.UNDEFINED}
		private var bFirewallType: FirewallType = FirewallType.FIREWALLD; fun firewallType(block: () -> FirewallType?) { bFirewallType = block() ?: FirewallType.FIREWALLD}
		private var bGlusterService: Boolean = false; fun glusterService(block: () -> Boolean?) { bGlusterService = block() ?: false }
		private var bHaReservation: Boolean = false; fun haReservation(block: () -> Boolean?) { bHaReservation = block() ?: false }
		private var bLogMaxMemory: Long = 0L; fun logMaxMemory(block: () -> Long?) { bLogMaxMemory = block() ?: 0L }
		private var bLogMaxMemoryType: LogMaxMemoryUsedThresholdType = LogMaxMemoryUsedThresholdType.PERCENTAGE; fun logMaxMemoryType(block: () -> LogMaxMemoryUsedThresholdType?) { bLogMaxMemoryType = block() ?: LogMaxMemoryUsedThresholdType.PERCENTAGE }
		private var bMemoryOverCommit: Int = 0; fun memoryOverCommit(block: () -> Int?) { bMemoryOverCommit = block() ?: 0 }
		private var bMigrationPolicy: InheritableBoolean = InheritableBoolean.INHERIT; fun migrationPolicy(block: () -> InheritableBoolean?) { bMigrationPolicy = block() ?: InheritableBoolean.INHERIT }
		private var bBandwidth: MigrationBandwidthAssignmentMethod = MigrationBandwidthAssignmentMethod.AUTO; fun bandwidth(block: () -> MigrationBandwidthAssignmentMethod?) { bBandwidth = block() ?: MigrationBandwidthAssignmentMethod.AUTO }
		private var bEncrypted: InheritableBoolean = InheritableBoolean.INHERIT; fun encrypted(block: () -> InheritableBoolean?) { bEncrypted = block() ?: InheritableBoolean.INHERIT }
		private var bSwitchType: SwitchType = SwitchType.LEGACY; fun switchType(block: () -> SwitchType?) { bSwitchType = block() ?: SwitchType.LEGACY }
		private var bThreadsAsCores: Boolean = false; fun threadsAsCores(block: () -> Boolean?) { bThreadsAsCores = block() ?: false }
		private var bVersion: String = ""; fun version(block: () -> String?) { bVersion = block() ?: "" }
		private var bVirtService: Boolean = false; fun virtService(block: () -> Boolean?) { bVirtService = block() ?: false }
		private var bNetworkProvider: Boolean = false; fun networkProvider(block: () -> Boolean?) { bNetworkProvider = block() ?: false }
		private var bDatacenterVo: IdentifiedVo = IdentifiedVo(); fun datacenterVo(block: () -> IdentifiedVo?) { bDatacenterVo = block() ?: IdentifiedVo() }
		private var bNetworkVo: NetworkVo = NetworkVo(); fun networkVo(block: () -> NetworkVo?) { bNetworkVo = block() ?: NetworkVo() }
		private var bHostSize: SizeVo = SizeVo(); fun hostSize(block: () -> SizeVo?) { bHostSize = block() ?: SizeVo() }
		private var bVmSize: SizeVo = SizeVo(); fun vmSize(block: () -> SizeVo?) { bVmSize = block() ?: SizeVo() }
		private var bHostVos: List<IdentifiedVo> = listOf();fun hostVos(block: () -> List<IdentifiedVo>?) { bHostVos = block() ?: listOf() }
		private var bNetworkVos: List<IdentifiedVo> = listOf();fun networkVos(block: () -> List<IdentifiedVo>?) { bNetworkVos = block() ?: listOf() }
		private var bTemplateVos: List<IdentifiedVo> = listOf();fun templateVos(block: () -> List<IdentifiedVo>?) { bTemplateVos = block() ?: listOf() }
		private var bRequired: Boolean = false; fun required(block: () -> Boolean?) { bRequired = block() ?: false }

		fun build(): ClusterVo = ClusterVo(bId, bName, bDescription, bComment, bIsConnected, bBallooningEnabled, bBiosType, bCpuArc, bCpuType, bErrorHandling, bFipsMode, bFirewallType, bGlusterService, bHaReservation, bLogMaxMemory, bLogMaxMemoryType, bMemoryOverCommit, bMigrationPolicy, bBandwidth, bEncrypted, bSwitchType, bThreadsAsCores, bVersion, bVirtService, bNetworkProvider, bDatacenterVo, bNetworkVo, bHostSize, bVmSize, bHostVos, bNetworkVos, bTemplateVos, /*bNetworkProperty, bAttached, */bRequired)
	}

	companion object {
		inline fun builder(block: ClusterVo.Builder.() -> Unit): ClusterVo = ClusterVo.Builder().apply(block).build()
	}
}

/**
 * 클러스터 id&name
 */
fun Cluster.toClusterIdName(): ClusterVo = ClusterVo.builder {
	id { this@toClusterIdName.id() }
	name { this@toClusterIdName.name() }
}
fun List<Cluster>.toClustersIdName(): List<ClusterVo> =
	this@toClustersIdName.map { it.toClusterIdName() }

/**
 * 클러스터 목록
 */
fun Cluster.toClusterMenu(conn: Connection): ClusterVo = ClusterVo.builder {
	id { this@toClusterMenu.id() }
	name { this@toClusterMenu.name() }
	comment { this@toClusterMenu.comment() }
	version { this@toClusterMenu.version().major().toString() + "." + this@toClusterMenu.version().minor() }
	description { this@toClusterMenu.description() }
	cpuType { if(this@toClusterMenu.cpuPresent()) this@toClusterMenu.cpu().type().toString() else null }
	hostSize { this@toClusterMenu.findHostCntFromCluster(conn) }
	vmSize { this@toClusterMenu.findVmCntFromCluster(conn) }
}
fun List<Cluster>.toClustersMenu(conn: Connection): List<ClusterVo> =
	this@toClustersMenu.map { it.toClusterMenu(conn) }


fun Cluster.toClusterInfo(conn: Connection): ClusterVo {
	return ClusterVo.builder {
		id { this@toClusterInfo.id() }
		name { this@toClusterInfo.name() }
		description {this@toClusterInfo.description() }
		comment { this@toClusterInfo.comment() }
		biosType { this@toClusterInfo.biosType().toString() }
		cpuArc { this@toClusterInfo.cpu().architecture().toString() }
		cpuType { if (this@toClusterInfo.cpuPresent()) this@toClusterInfo.cpu().type() else null }
		firewallType { this@toClusterInfo.firewallType() }
		haReservation { this@toClusterInfo.haReservation() }
		logMaxMemory { this@toClusterInfo.logMaxMemoryUsedThresholdAsLong() }
		logMaxMemoryType { this@toClusterInfo.logMaxMemoryUsedThresholdType() }
		memoryOverCommit { this@toClusterInfo.memoryPolicy().overCommit().percentAsInteger() }
		migrationPolicy { this@toClusterInfo.migration().autoConverge() }
		bandwidth { this@toClusterInfo.migration().bandwidth().assignmentMethod() }
		version { this@toClusterInfo.version().major().toString() + "." + this@toClusterInfo.version().minor() }
		datacenterVo { if(this@toClusterInfo.dataCenterPresent()) conn.findDataCenter(this@toClusterInfo.dataCenter().id()).getOrNull()?.fromDataCenterToIdentifiedVo() else null }
		vmSize { this@toClusterInfo.findVmCntFromCluster(conn) }
	}
}
fun List<Cluster>.toClustersInfo(conn: Connection): List<ClusterVo> =
	this@toClustersInfo.map { it.toClusterInfo(conn) }



fun Cluster.toNetworkClusterVo(conn: Connection, networkId: String): ClusterVo{
	val network: Network? =
		conn.findNetworkFromCluster(this@toNetworkClusterVo.id(), networkId)
			.getOrNull()

	return ClusterVo.builder {
		id { this@toNetworkClusterVo.id() }
		name { this@toNetworkClusterVo.name() }
		description {this@toNetworkClusterVo.description() }
		networkVo { network?.toClusterNetworkVo(conn) }
//		networks { networks }
	}
}
fun List<Cluster>.toNetworkClusterVos(conn: Connection, networkId: String): List<ClusterVo> =
	this@toNetworkClusterVos.map { it.toNetworkClusterVo(conn, networkId) }



/**
 * 클러스터 빌더
 */
fun ClusterVo.toClusterBuilder(conn: Connection): ClusterBuilder {
	return ClusterBuilder()
		.dataCenter(DataCenterBuilder().id(this@toClusterBuilder.datacenterVo.id).build()) // 필수
		.name(this@toClusterBuilder.name) // 필수
		.cpu(CpuBuilder().architecture(Architecture.fromValue(this@toClusterBuilder.cpuArc)).type(this@toClusterBuilder.cpuType)) // 필수
		.description(this@toClusterBuilder.description)
		.comment(this@toClusterBuilder.comment)
		.managementNetwork(NetworkBuilder().id(this@toClusterBuilder.networkVo.id).build())
		.biosType(BiosType.fromValue(this@toClusterBuilder.biosType))
		.fipsMode(FipsMode.DISABLED)
		.version(VersionBuilder().major(4).minor(7).build())
		.switchType(SwitchType.LEGACY)  // 편집에선 선택불가
		.firewallType(FirewallType.FIREWALLD)
		.virtService(true)
		.glusterService(false)
		.errorHandling(ErrorHandlingBuilder().onError(MigrateOnError.fromValue(this@toClusterBuilder.errorHandling)))
		.externalNetworkProviders(conn.findAllOpenStackNetworkProviders().getOrDefault(listOf()).first())
//		.logMaxMemoryUsedThreshold(this@toClusterBuilder.logMaxMemory)
//		.logMaxMemoryUsedThresholdType(this@toClusterBuilder.logMaxMemoryType)
		// HELP: 마이그레이션 정책 관련 설정 값 조회 기능 존재여부 확인필요
//		.migration(
//			MigrationOptionsBuilder()
//				.bandwidth(MigrationBandwidthBuilder().assignmentMethod(this@toClusterBuilder.bandwidth))
//				.encrypted(this@toClusterBuilder.encrypted)
//		)
//		.fencingPolicy(
//			FencingPolicyBuilder()
//				.skipIfConnectivityBroken(SkipIfConnectivityBrokenBuilder().enabled(true))
//				.skipIfSdActive(SkipIfSdActiveBuilder().enabled(true))
//		)
}

/**
 * 클러스터 생성 빌더
 */
fun ClusterVo.toAddClusterBuilder(conn: Connection): Cluster =
	// 생성시 fips 모드, 호환버전, 스위치 유형, 방화벽유형, 기본네트워크 공급자, virt 서비스 활성화, gluster 서비스 활성화 기본설정
	this@toAddClusterBuilder.toClusterBuilder(conn).build()

/**
 * 클러스터 편집 빌더
 */
fun ClusterVo.toEditClusterBuilder(conn: Connection): Cluster =
	this@toEditClusterBuilder.toClusterBuilder(conn).id(this@toEditClusterBuilder.id).build()


/**
 * 전체 Cluster 정보 출력
 */
fun Cluster.toClusterVo(conn: Connection): ClusterVo {
	val dataCenter: DataCenter? =
		conn.findDataCenter(this@toClusterVo.dataCenter().id())
			.getOrNull()

	val hostVos: List<IdentifiedVo> =
		conn.findAllHostsFromCluster(this@toClusterVo.id())
			.getOrDefault(listOf())
			.fromHostsToIdentifiedVos()

	val networks: List<Network> =
		conn.findAllNetworksFromCluster(this@toClusterVo.id())
			.getOrDefault(listOf())
	val networkVos: List<NetworkVo> =
		networks.toNetworkVos(conn)

	val networkIds: List<IdentifiedVo> =
		conn.findAllNetworksFromCluster(this@toClusterVo.id())
			.getOrDefault(listOf())
			.fromNetworksToIdentifiedVos()

//	val manageNetworkVo: IdentifiedVo =
//		networks.first { it.usages().contains(NetworkUsage.MANAGEMENT) }
//			.fromNetworkToIdentifiedVo()
	val manageNetworkVo: NetworkVo =
		networks.first { it.usages().contains(NetworkUsage.MANAGEMENT) }
			.toNetworkMenu(conn)

	val templates: List<Template> =
		conn.findAllTemplates()
			.getOrDefault(listOf())
			.filter { !it.clusterPresent() || it.cluster().id() == this@toClusterVo.id() }
	val templateVos: List<IdentifiedVo> =
		templates.fromTemplatesToIdentifiedVos()

	return ClusterVo.builder {
		id { this@toClusterVo.id() }
		name { this@toClusterVo.name() }
		description {this@toClusterVo.description() }
		comment { this@toClusterVo.comment() }
//		isConnected { this@toClusterVo. }
		ballooningEnabled { this@toClusterVo.ballooningEnabled() }
		biosType { this@toClusterVo.biosType().toString() }
		cpuArc { this@toClusterVo.cpu().architecture().toString() }
		cpuType { if (this@toClusterVo.cpuPresent()) this@toClusterVo.cpu().type() else null }
		errorHandling { this@toClusterVo.errorHandling().onError().toString() }
		fipsMode { this@toClusterVo.fipsMode() }
		firewallType { this@toClusterVo.firewallType() }
		glusterService { this@toClusterVo.glusterService() }
		haReservation { this@toClusterVo.haReservation() }
		logMaxMemory { this@toClusterVo.logMaxMemoryUsedThresholdAsLong() }
		logMaxMemoryType { this@toClusterVo.logMaxMemoryUsedThresholdType() }
		memoryOverCommit { this@toClusterVo.memoryPolicy().overCommit().percentAsInteger() }
		migrationPolicy { this@toClusterVo.migration().autoConverge() }
		bandwidth { this@toClusterVo.migration().bandwidth().assignmentMethod() }
		encrypted { this@toClusterVo.migration().encrypted() }
		switchType { this@toClusterVo.switchType() }
		threadsAsCores { this@toClusterVo.threadsAsCores() }
		version { this@toClusterVo.version().major().toString() + "." + this@toClusterVo.version().minor() }
		virtService { this@toClusterVo.virtService() }
		networkProvider { this@toClusterVo.externalNetworkProviders().size != 0 } // 0이 아니라면 네트워크 공급자 존재
		datacenterVo { dataCenter?.fromDataCenterToIdentifiedVo() }
		networkVo { manageNetworkVo }
		hostSize { this@toClusterVo.findHostCntFromCluster(conn) }
		vmSize { this@toClusterVo.findVmCntFromCluster(conn) }
		hostVos { hostVos }
		networkVos { networkIds }
		templateVos { templateVos }
	}
}
fun List<Cluster>.toClusterVos(conn: Connection): List<ClusterVo> =
	this@toClusterVos.map { it.toClusterVo(conn) }
