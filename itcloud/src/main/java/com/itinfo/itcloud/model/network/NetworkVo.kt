package com.itinfo.itcloud.model.network

import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.fromDataCenterToIdentifiedVo
import com.itinfo.itcloud.model.fromVnicProfilesToIdentifiedVos
import com.itinfo.itcloud.gson
import com.itinfo.util.ovirt.*
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.builders.DataCenterBuilder
import org.ovirt.engine.sdk4.builders.NetworkBuilder
import org.ovirt.engine.sdk4.builders.NetworkLabelBuilder
import org.ovirt.engine.sdk4.builders.OpenStackNetworkProviderBuilder
import org.ovirt.engine.sdk4.builders.VlanBuilder
import org.ovirt.engine.sdk4.types.*
import org.slf4j.LoggerFactory
import java.io.Serializable

private val log = LoggerFactory.getLogger(NetworkVo::class.java)

/**
 * [NetworkVo]
 * 네트워크, 클러스터 네트워크
 *
 * @property id [String]
 * @property name [String]
 * @property description [String]
 * @property comment [String]
 * @property mtu [Int] true면 기본값
 * @property portIsolation [Boolean]
 * @property stp [Boolean] Spanning Tree Protocol 없어도 될듯
 * @property usage [UsageVo]  management 기본이 체크된 상태 (true, 가상머신 네트워크 (기본) (usage))
 * @property vdsmName [String]
 * @property dataCenter [IdentifiedVo]
// * @property networkClusterVo []
 * @property openStackNetwork [OpenStackNetworkVo] 네트워크 공급자(한개만 있음) (생성시 여부(boolean)으로 처리,추가)
 *
 * 네트워크 생성시 필요
 * @property vlan [Int] vlan 태그 (태그 자체는 활성화를 해야 입력란이 생김)
// * @proprety dnsList List<[String]> DNS 서버는 애매함
 *
 * 클러스터에서 출력될 내용
 * @property status [NetworkStatus] 네트워크 상태
 * @property display [Boolean] 클러스터-네트워크 관리
 * @property networkLabel [String] 네트워크 레이블
 * @property cluster [IdentifiedVo]
 * @property attached [Boolean] 할당 -> cluster-network-cluster<> 생성되고 아니면 cluster-network 에서 제외
 * @property required [Boolean] 클러스터-네트워크 관리 -> 필수(t,f)
 *
 * @property vnicProfiles List<[IdentifiedVo]> vnicProfile
 * @property clusters List<[ClusterVo]> clusters  // networks clusters
 *
 */
class NetworkVo (
	val id: String = "",
	val name: String = "",
	val description: String = "",
	val comment: String = "",
	val mtu: Int = 0,
	val portIsolation: Boolean = false,
	val stp: Boolean = false,
	val usage: UsageVo = UsageVo(),
	val vdsmName: String = "",
	val dataCenter: IdentifiedVo = IdentifiedVo(),
	val openStackNetwork: OpenStackNetworkVo = OpenStackNetworkVo(),
	val vlan: Int = 0,
	val status: NetworkStatus = NetworkStatus.NON_OPERATIONAL,
	val display: Boolean = false,
	val networkLabel: String = "",
	val cluster: IdentifiedVo = IdentifiedVo(),
	val vnicProfiles: List<IdentifiedVo> = listOf(),
	val clusters: List<ClusterVo> = listOf(),

):Serializable {
	override fun toString(): String = gson.toJson(this)

	class Builder{
		private var bId: String = ""; fun id(block: () -> String?) { bId = block() ?: "" }
		private var bName: String = ""; fun name(block: () -> String?) { bName = block() ?: "" }
		private var bDescription: String = ""; fun description(block: () -> String?) { bDescription = block() ?: "" }
		private var bComment: String = ""; fun comment(block: () -> String?) { bComment = block() ?: "" }
		private var bMtu: Int = 0; fun mtu( block: () -> Int?) { bMtu = block() ?: 0 }
		private var bPortIsolation: Boolean = false; fun portIsolation( block: () -> Boolean?) { bPortIsolation = block() ?: false }
		private var bStp: Boolean = false; fun stp( block: () -> Boolean?) { bStp = block() ?: false }
		private var bUsage: UsageVo = UsageVo(); fun usage(block: () -> UsageVo?) { bUsage = block() ?: UsageVo() }
		private var bVdsmName: String = ""; fun vdsmName(block: () -> String?) { bVdsmName = block() ?: "" }
		private var bDataCenter: IdentifiedVo = IdentifiedVo(); fun dataCenter(block: () -> IdentifiedVo?) { bDataCenter = block() ?: IdentifiedVo() }
		private var bOpenStackNetwork: OpenStackNetworkVo = OpenStackNetworkVo(); fun openStackNetwork(block: () -> OpenStackNetworkVo?) { bOpenStackNetwork = block() ?: OpenStackNetworkVo() }
		private var bVlan: Int = 0; fun vlan(block: () -> Int?) { bVlan = block() ?: 0 }
		private var bStatus: NetworkStatus = NetworkStatus.NON_OPERATIONAL; fun status(block: () -> NetworkStatus?) { bStatus = block() ?: NetworkStatus.NON_OPERATIONAL }
		private var bDisplay: Boolean = false; fun display(block: () -> Boolean?) { bDisplay = block() ?: false }
		private var bNetworkLabel: String = ""; fun networkLabel(block: () -> String?) { bNetworkLabel = block() ?: "" }
		private var bCluster: IdentifiedVo = IdentifiedVo(); fun cluster(block: () -> IdentifiedVo?) { bCluster = block() ?: IdentifiedVo() }
		private var bVnicProfiles: List<IdentifiedVo> = listOf(); fun vnicProfiles(block: () -> List<IdentifiedVo>?) { bVnicProfiles = block() ?: listOf() }
		private var bClusters: List<ClusterVo> = listOf(); fun clusters(block: () -> List<ClusterVo>?) { bClusters = block() ?: listOf() }

		fun build(): NetworkVo = NetworkVo(bId, bName, bDescription, bComment, bMtu, bPortIsolation, bStp, bUsage, bVdsmName, bDataCenter, bOpenStackNetwork, bVlan, bStatus, bDisplay, bNetworkLabel, bCluster, bVnicProfiles, bClusters,)
	}

	companion object{
		inline fun builder(block: NetworkVo.Builder.() -> Unit): NetworkVo =  NetworkVo.Builder().apply(block).build()
	}
}

fun Network.toNetworkVo(conn: Connection): NetworkVo {
	val vnicProfileVos: List<IdentifiedVo> =
		conn.findAllVnicProfiles().getOrDefault(listOf())
			.filter { it.network().id() == this@toNetworkVo.id() }
			.fromVnicProfilesToIdentifiedVos()

	val usages: MutableList<NetworkUsage>? =
		conn.findNetwork(this@toNetworkVo.id())
			.getOrNull()
			?.usages()

	return NetworkVo.builder {
		id { this@toNetworkVo.id() }
		name { this@toNetworkVo.name() }
		description { this@toNetworkVo.description() }
		comment { this@toNetworkVo.comment() }
		mtu { this@toNetworkVo.mtu().toInt() }
		portIsolation { this@toNetworkVo.portIsolation() }
		stp { this@toNetworkVo.stp() }
		usage { usages?.toUsagesVo() }
		vdsmName { this@toNetworkVo.vdsmName() }
		dataCenter { conn.findDataCenter(this@toNetworkVo.dataCenter().id()).getOrNull()?.fromDataCenterToIdentifiedVo() }
		openStackNetwork {
			if(this@toNetworkVo.externalProviderPresent())
				conn.findOpenStackNetworkProvider(this@toNetworkVo.externalProvider().id())
					.getOrNull()
					?.toOpenStackNetworkVo(conn)
			else
				null
		}
		vlan { if (this@toNetworkVo.vlanPresent()) this@toNetworkVo.vlan().idAsInteger() else 0}
		vnicProfiles { vnicProfileVos }
	}
}

fun List<Network>.toNetworkVos(conn: Connection): List<NetworkVo> =
	this@toNetworkVos.map { it.toNetworkVo(conn) }


fun Network.toClusterNetworkVo(conn: Connection): NetworkVo {
	val usages: List<NetworkUsage> = this@toClusterNetworkVo.usages()

	return NetworkVo.builder {
		id { this@toClusterNetworkVo.id() }
		name { this@toClusterNetworkVo.name() }
		description { this@toClusterNetworkVo.description() }
		portIsolation { this@toClusterNetworkVo.portIsolation() }
		status { this@toClusterNetworkVo.status() }
		usage { usages.toUsagesVo() } // TODO
	}
}
fun List<Network>.toClusterNetworkVos(conn: Connection): List<NetworkVo> =
	this@toClusterNetworkVos.map { it.toClusterNetworkVo(conn) }


/**
 * 네트워크 빌더
 */
fun NetworkVo.toNetworkBuilder(conn: Connection): NetworkBuilder {
	// external provider 선택시 vlan, portisolation=false 선택되면 안됨
	val networkBuilder = NetworkBuilder()
	networkBuilder
		.dataCenter(DataCenterBuilder().id(this@toNetworkBuilder.dataCenter.id).build())
		.name(this@toNetworkBuilder.name)
		.description(this@toNetworkBuilder.description)
		.comment(this@toNetworkBuilder.comment)
		.portIsolation(this@toNetworkBuilder.portIsolation)
		.mtu(this@toNetworkBuilder.mtu)  // 제한수가 있음

	if (this@toNetworkBuilder.usage.vm) {
		networkBuilder.usages(NetworkUsage.VM)
	}
	if(this@toNetworkBuilder.vlan != 0){
		networkBuilder
			.vlan(VlanBuilder().id(this@toNetworkBuilder.vlan))
	}
	if(this@toNetworkBuilder.openStackNetwork.id.isNotEmpty()) {
		networkBuilder
			.externalProvider(OpenStackNetworkProviderBuilder().id(this@toNetworkBuilder.openStackNetwork.id))
	}
	if (this@toNetworkBuilder.openStackNetwork.id.isEmpty() && this@toNetworkBuilder.networkLabel.isNotEmpty()) {
		conn.addNetworkLabelFromNetwork(
			this@toNetworkBuilder.id,
			NetworkLabelBuilder().id(this@toNetworkBuilder.networkLabel).build()
		)
//		networkBuilder.networkLabels(NetworkLabelBuilder().id(this@toNetworkBuilder.networkLabel))
	}
	// VnicProfile은 기본생성만 /qos는 제외항목, 네트워크필터도 vdsm으로 고정(?)

	// 클러스터 연결.할당
	// 클러스터 모두연결이 선택되어야지만 모두 필요가 선택됨
	// front 에서 연결이 선택되면 clusterNetwork 에 add 하고 필요가 선택되면 required 에 true

//	val clusters: List<Cluster> =
//		conn.findAllClustersFromDataCenter(this@toNetworkBuilder.dataCenter.id)
//			.getOrDefault(listOf())
//
//	for (clusterVo in clusters) {
//		val n: Network = NetworkBuilder()
//			.id(network.id())
//			.required(clusterVo.required) // TODO: 어디서 찾는 값?
//			.build()
//		val resNetwork: Result<Network?> =
//			conn.addNetworkFromCluster(clusterVo.id, n)
//		log.info("신규 network(s) 추가 결과: {}", resNetwork.isSuccess)
//	}
	return networkBuilder
}

// 필요 name, datacenter_id
fun NetworkVo.toAddNetworkBuilder(conn: Connection): Network =
	this@toAddNetworkBuilder.toNetworkBuilder(conn).build()

fun NetworkVo.toEditNetworkBuilder(conn: Connection): Network =
	this@toEditNetworkBuilder.toNetworkBuilder(conn).id(this@toEditNetworkBuilder.id).build()

