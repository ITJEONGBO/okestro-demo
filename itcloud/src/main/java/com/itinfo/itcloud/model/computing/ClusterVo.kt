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
 * @property dataCenterVo [IdentifiedVo]
 * @property networkVo [IdentifiedVo] // 관리네트워크
 * @property hostSizeVo [SizeVo]
 * @property vmSizeVo [SizeVo]
 * @property hostVos List<[IdentifiedVo]>
 * @property networkVos List<[IdentifiedVo]>
 * @property templateVos List<[IdentifiedVo]>
 *
 * @property networkPropertyVo [NetworkPropertyVo]
 **/
class ClusterVo(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val comment: String = "",
    val isConnected: Boolean = false,
    val ballooningEnabled: Boolean = false,
    val biosType: BiosType = BiosType.CLUSTER_DEFAULT,
    val cpuArc: Architecture = Architecture.UNDEFINED,
    val cpuType: String = "",
    val errorHandling: MigrateOnError = MigrateOnError.MIGRATE,
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
	val dataCenterVo: IdentifiedVo = IdentifiedVo(),
    val networkVo: IdentifiedVo = IdentifiedVo(), // 관리네트워크
    val hostSizeVo: SizeVo = SizeVo(),
    val vmSizeVo: SizeVo = SizeVo(),
    val hostVos: List<IdentifiedVo> = listOf(),
    val networkVos: List<IdentifiedVo> = listOf(), // 관리네트워크가 핵심, 다른 네트워크 존재가능
    val templateVos: List<IdentifiedVo> = listOf(),
	val networkPropertyVo: NetworkPropertyVo = NetworkPropertyVo(),

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
		private var bBiosType: BiosType = BiosType.CLUSTER_DEFAULT; fun biosType(block: () -> BiosType?) { bBiosType = block() ?: BiosType.CLUSTER_DEFAULT }
		private var bCpuArc: Architecture = Architecture.UNDEFINED; fun cpuArc(block: () -> Architecture?) { bCpuArc = block() ?: Architecture.UNDEFINED }
		private var bCpuType: String = ""; fun cpuType(block: () -> String?) { bCpuType = block() ?: "" }
		private var bErrorHandling: MigrateOnError = MigrateOnError.MIGRATE; fun errorHandling(block: () -> MigrateOnError?) { bErrorHandling = block() ?: MigrateOnError.MIGRATE }
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
		private var bDataCenterVo: IdentifiedVo = IdentifiedVo(); fun dataCenterVo(block: () -> IdentifiedVo?) { bDataCenterVo = block() ?: IdentifiedVo() }
		private var bNetworkVo: IdentifiedVo = IdentifiedVo(); fun networkVo(block: () -> IdentifiedVo?) { bNetworkVo = block() ?: IdentifiedVo() }
		private var bHostSizeVo: SizeVo = SizeVo(); fun hostSizeVo(block: () -> SizeVo?) { bHostSizeVo = block() ?: SizeVo() }
		private var bVmSizeVo: SizeVo = SizeVo(); fun vmSizeVo(block: () -> SizeVo?) { bVmSizeVo = block() ?: SizeVo() }
		private var bHostVos: List<IdentifiedVo> = listOf();fun hostVos(block: () -> List<IdentifiedVo>?) { bHostVos = block() ?: listOf() }
		private var bNetworkVos: List<IdentifiedVo> = listOf();fun networkVos(block: () -> List<IdentifiedVo>?) { bNetworkVos = block() ?: listOf() }
		private var bTemplateVos: List<IdentifiedVo> = listOf();fun templateVos(block: () -> List<IdentifiedVo>?) { bTemplateVos = block() ?: listOf() }
		private var bNetworkPropertyVo: NetworkPropertyVo = NetworkPropertyVo();fun networkPropertyVo(block: () -> NetworkPropertyVo?) { bNetworkPropertyVo = block() ?: NetworkPropertyVo() }

		fun build(): ClusterVo = ClusterVo(bId, bName, bDescription, bComment, bIsConnected, bBallooningEnabled, bBiosType, bCpuArc, bCpuType, bErrorHandling, bFipsMode, bFirewallType, bGlusterService, bHaReservation, bLogMaxMemory, bLogMaxMemoryType, bMemoryOverCommit, bMigrationPolicy, bBandwidth, bEncrypted, bSwitchType, bThreadsAsCores, bVersion, bVirtService, bNetworkProvider, bDataCenterVo, bNetworkVo, bHostSizeVo, bVmSizeVo, bHostVos, bNetworkVos, bTemplateVos, bNetworkPropertyVo)
	}

	companion object {
		inline fun builder(block: ClusterVo.Builder.() -> Unit): ClusterVo = ClusterVo.Builder().apply(block).build()
	}
}

fun Cluster.toClusterIdName(): ClusterVo = ClusterVo.builder {
	id { this@toClusterIdName.id() }
	name { this@toClusterIdName.name() }
}

fun List<Cluster>.toClusterIdNames(): List<ClusterVo> =
	this@toClusterIdNames.map { it.toClusterIdName() }


fun Cluster.toClusterVo(conn: Connection): ClusterVo {
	val dataCenter: DataCenter? =
		conn.findDataCenter(this@toClusterVo.dataCenter().id())
			.getOrNull()

	val hosts: List<Host> =
		conn.findAllHosts()
			.getOrDefault(listOf())
			.filter { it.clusterPresent() && it.cluster().id() == this@toClusterVo.id() }
	val hostVos: List<IdentifiedVo> =
		hosts.fromHostsToIdentifiedVos()

	val networks: List<Network> =
		conn.findAllNetworksFromCluster(this@toClusterVo.id())
			.getOrDefault(listOf())
	val networkVos: List<IdentifiedVo> =
		networks.fromNetworksToIdentifiedVos()

	val manageNetworkVo: IdentifiedVo =
		networks.first { it.usages().contains(NetworkUsage.MANAGEMENT) }
			.fromNetworkToIdentifiedVo()

//	val networkPropertyVo: NetworkPropertyVo = NetworkPropertyVo.builder {
//		attached {  }
//		required {  }
//	}

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
		biosType { this@toClusterVo.biosType() }
		cpuArc { this@toClusterVo.cpu().architecture() }
		cpuType { if (this@toClusterVo.cpuPresent()) this@toClusterVo.cpu().type() else null }
		errorHandling { this@toClusterVo.errorHandling().onError() }
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
		dataCenterVo { dataCenter?.fromDataCenterToIdentifiedVo() }
		networkVo { manageNetworkVo }
		hostSizeVo { this@toClusterVo.findHostCntFromCluster(conn) }
		vmSizeVo { this@toClusterVo.findVmCntFromCluster(conn) }
		hostVos { hostVos }
		networkVos { networkVos }
		templateVos { templateVos }
	}
}

fun List<Cluster>.toClusterVos(conn: Connection): List<ClusterVo> =
	this@toClusterVos.map { it.toClusterVo(conn) }

/**
 * 클러스터 목록
 */
fun Cluster.toClusterVoInfo(): ClusterVo {
	return ClusterVo.builder {
		id { this@toClusterVoInfo.id() }
		name { this@toClusterVoInfo.name() }
		comment { this@toClusterVoInfo.comment() }
//		version { this@toClusterVoInfo.version().major() + "." + this@toClusterVoInfo.version().minor() }
		description { this@toClusterVoInfo.description() }
		cpuType { if(this@toClusterVoInfo.cpuPresent()) this@toClusterVoInfo.cpu().type() else  null }
//		hostCnt { getClusterHostCnt(system, cluster.id(), "") }
//		vmCnt { getClusterVmCnt(system, cluster.id(), "") }
	}
}

fun List<Cluster>.toClusterVoInfos(): List<ClusterVo> =
	this@toClusterVoInfos.map { it.toClusterVoInfo() }


/**
 * 클러스터 빌더
 */
fun ClusterVo.toClusterBuilder(conn: Connection): ClusterBuilder {
	val ver = this@toClusterBuilder.version.split(".")
	if (ver.size < 2) throw IllegalArgumentException("잘못된 버전정보 입력")

	val clusterBuilder = ClusterBuilder()
	clusterBuilder
		.dataCenter(DataCenterBuilder().id(this@toClusterBuilder.dataCenterVo.id).build()) // 필수
		.name(this@toClusterBuilder.name) // 필수
		.cpu(CpuBuilder().architecture(this@toClusterBuilder.cpuArc).type(this@toClusterBuilder.cpuType)) // 필수
		.description(this@toClusterBuilder.description)
		.comment(this@toClusterBuilder.comment)
		.managementNetwork(NetworkBuilder().id(this@toClusterBuilder.networkVo.id).build())
		.biosType(this@toClusterBuilder.biosType)
		.fipsMode(this@toClusterBuilder.fipsMode)
		.version(VersionBuilder().major(Integer.parseInt(ver[0])).minor(Integer.parseInt(ver[1])).build())
		.switchType(this@toClusterBuilder.switchType)  // 편집에선 선택불가
		.firewallType(this@toClusterBuilder.firewallType)
		.logMaxMemoryUsedThreshold(this@toClusterBuilder.logMaxMemory)
		.logMaxMemoryUsedThresholdType(this@toClusterBuilder.logMaxMemoryType)
		.virtService(this@toClusterBuilder.virtService)
		.glusterService(this@toClusterBuilder.glusterService)
		.errorHandling(ErrorHandlingBuilder().onError(this@toClusterBuilder.errorHandling))
		// TODO: 마이그레이션 정책 관련 설정 값 조회 기능 존재여부 확인필요
		.migration(
			MigrationOptionsBuilder()
				.bandwidth(MigrationBandwidthBuilder().assignmentMethod(this@toClusterBuilder.bandwidth))
				.encrypted(this@toClusterBuilder.encrypted)
		)
		.fencingPolicy(
			FencingPolicyBuilder()
				.skipIfConnectivityBroken(SkipIfConnectivityBrokenBuilder().enabled(true))
				.skipIfSdActive(SkipIfSdActiveBuilder().enabled(true))
		)
//		.externalNetworkProviders( conn.findAllOpenStackNetworkProviders().getOrDefault(listOf()))
		if (this.networkProvider) {
			clusterBuilder.externalNetworkProviders(conn.findAllOpenStackNetworkProviders().getOrDefault(listOf()).first())
		}
	return clusterBuilder
}

/**
 * [ClusterVo.toAddClusterBuilder]
 * 클러스터 생성 빌더
 */
fun ClusterVo.toAddClusterBuilder(conn: Connection): Cluster =
	this@toAddClusterBuilder.toClusterBuilder(conn).build()

/**
 * [ClusterVo.toEditClusterBuilder]
 * 클러스터 편집 빌더
 */
fun ClusterVo.toEditClusterBuilder(conn: Connection): Cluster =
	this@toEditClusterBuilder.toClusterBuilder(conn).id(this@toEditClusterBuilder.id).build()


