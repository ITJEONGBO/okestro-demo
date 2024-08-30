package com.itinfo.itcloud.model.network

import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.fromDataCenterToIdentifiedVo
import com.itinfo.itcloud.model.fromVnicProfilesToIdentifiedVos
import com.itinfo.itcloud.model.gson
import com.itinfo.util.ovirt.*
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.builders.DataCenterBuilder
import org.ovirt.engine.sdk4.builders.NetworkBuilder
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
 * @property usageVo [UsageVo]  management 기본이 체크된 상태 (true, 가상머신 네트워크 (기본) (usage))
 * @property vdsmName [String]
 * @property dataCenterVo [IdentifiedVo]
// * @property networkClusterVo []
 * @property openStackNetworkVo [OpenStackNetworkVo] 네트워크 공급자(한개만 있음) (생성시 여부(boolean)으로 처리,추가)
 *
 * 네트워크 생성시 필요
 * @property vlan [Int] vlan 태그 (태그 자체는 활성화를 해야 입력란이 생김)
// * @proprety dnsList List<[String]> DNS 서버는 애매함
 *
 * 클러스터에서 출력될 내용
 * @property status [NetworkStatus] 네트워크 상태
 * @property display [Boolean] 클러스터-네트워크 관리
 * @property allotment [Boolean] 할당 -> cluster-network-cluster<> 생성되고 아니면 cluster-network 에서 제외
 * @property required [Boolean] 클러스터-네트워크 관리 -> 필수(t,f)
 * @property clusterVo [IdentifiedVo]
 *
 * <link>
 * @property vnicProfileVos List<[IdentifiedVo]> vnicProfile
 * @property clusterVos List<[ClusterVo]> clusters
 *
// * @property hostVos List<[HostVo]>
// * @property vmVos List<[VmVo]>
// * @property templateVos List<[TemplateVo]>
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
	val usageVo: UsageVo = UsageVo(),
	val vdsmName: String = "",
	val dataCenterVo: IdentifiedVo = IdentifiedVo(),
	val openStackNetworkVo: OpenStackNetworkVo = OpenStackNetworkVo(),
	val vlan: Int = 0,
	val status: NetworkStatus = NetworkStatus.NON_OPERATIONAL,
	val display: Boolean = false,
	val allotment: Boolean = false,
	val required: Boolean = false,
	val clusterVo: IdentifiedVo = IdentifiedVo(),
	val vnicProfileVos: List<IdentifiedVo> = listOf(),
	val clusterVos: List<ClusterVo> = listOf()
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
		private var bUsageVo: UsageVo = UsageVo(); fun usageVo(block: () -> UsageVo?) { bUsageVo = block() ?: UsageVo() }
		private var bVdsmName: String = ""; fun vdsmName(block: () -> String?) { bVdsmName = block() ?: "" }
		private var bDataCenterVo: IdentifiedVo = IdentifiedVo(); fun dataCenterVo(block: () -> IdentifiedVo?) { bDataCenterVo = block() ?: IdentifiedVo() }
		private var bOpenStackNetworkVo: OpenStackNetworkVo = OpenStackNetworkVo(); fun openStackNetworkVo(block: () -> OpenStackNetworkVo?) { bOpenStackNetworkVo = block() ?: OpenStackNetworkVo() }
		private var bVlan: Int = 0; fun vlan(block: () -> Int?) { bVlan = block() ?: 0 }
		private var bStatus: NetworkStatus = NetworkStatus.NON_OPERATIONAL; fun status(block: () -> NetworkStatus?) { bStatus = block() ?: NetworkStatus.NON_OPERATIONAL }
		private var bDisplay: Boolean = false; fun display(block: () -> Boolean?) { bDisplay = block() ?: false }
		private var bAllotment: Boolean = false; fun allotment(block: () -> Boolean?) { bAllotment = block() ?: false }
		private var bRequired: Boolean = false; fun required(block: () -> Boolean?) { bRequired = block() ?: false }
		private var bClusterVo: IdentifiedVo = IdentifiedVo(); fun clusterVo(block: () -> IdentifiedVo?) { bClusterVo = block() ?: IdentifiedVo() }
		private var bVnicProfileVos: List<IdentifiedVo> = listOf(); fun vnicProfileVos(block: () -> List<IdentifiedVo>?) { bVnicProfileVos = block() ?: listOf() }
		private var bClusterVos: List<ClusterVo> = listOf(); fun clusterVos(block: () -> List<ClusterVo>?) { bClusterVos = block() ?: listOf() }

		fun build(): NetworkVo = NetworkVo(bId, bName, bDescription, bComment, bMtu, bPortIsolation, bStp, bUsageVo, bVdsmName, bDataCenterVo, bOpenStackNetworkVo, bVlan, bStatus, bDisplay, bAllotment, bRequired, bClusterVo, bVnicProfileVos, bClusterVos)
	}

	companion object{
		inline fun builder(block: NetworkVo.Builder.() -> Unit): NetworkVo =  NetworkVo.Builder().apply(block).build()
	}
}

fun Network.toNetworkIdName(): NetworkVo = NetworkVo.builder {
	id { this@toNetworkIdName.id() }
	name { this@toNetworkIdName.name() }
}

fun List<Network>.toNetworkIdNames(): List<NetworkVo> =
	this@toNetworkIdNames.map { it.toNetworkIdName() }


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
		usageVo { usages?.toUsagesVo() }
		vdsmName { this@toNetworkVo.vdsmName() }
		dataCenterVo { conn.findDataCenter(this@toNetworkVo.dataCenter().id()).getOrNull()?.fromDataCenterToIdentifiedVo() }
		openStackNetworkVo {
			if(this@toNetworkVo.externalProviderPresent())
				conn.findOpenStackNetworkProvider(this@toNetworkVo.externalProvider().id())
					.getOrNull()
					?.toOpenStackNetworkVo(conn)
			else
				null
		}
		vlan { if (this@toNetworkVo.vlanPresent()) this@toNetworkVo.vlan().idAsInteger() else 0}
		vnicProfileVos { vnicProfileVos }
	}
}

fun List<Network>.toNetworkVos(conn: Connection): List<NetworkVo> =
	this@toNetworkVos.map { it.toNetworkVo(conn) }


fun Network.toClusterNetworkVo(conn: Connection): NetworkVo {
	val usages: List<NetworkUsage> =
		this@toClusterNetworkVo.usages()

	return NetworkVo.builder {
		id { this@toClusterNetworkVo.id() }
		name { this@toClusterNetworkVo.name() }
		description { this@toClusterNetworkVo.description() }
		portIsolation { this@toClusterNetworkVo.portIsolation() }
		status { this@toClusterNetworkVo.status() }
		required { this@toClusterNetworkVo.required() }
		usageVo { usages.toUsagesVo() } // TODO
	}
}
fun List<Network>.toClusterNetworkVos(conn: Connection): List<NetworkVo> =
	this@toClusterNetworkVos.map { it.toClusterNetworkVo(conn) }


/**
 * 네트워크 빌더
 */
fun NetworkVo.toNetworkBuilder(conn: Connection): NetworkBuilder {
	// external provider 선택시 vlan, portisolation=false 선택되면 안됨
	val networkBuilder: NetworkBuilder = NetworkBuilder()
	networkBuilder
		.dataCenter(DataCenterBuilder().id(this@toNetworkBuilder.dataCenterVo.id).build())
		.name(this@toNetworkBuilder.name)
		.description(this@toNetworkBuilder.description)
		.comment(this@toNetworkBuilder.comment)
		.portIsolation(this@toNetworkBuilder.portIsolation)
		.mtu(this@toNetworkBuilder.mtu)  // 제한수가 있음

	if (this@toNetworkBuilder.usageVo.vm) {
		networkBuilder.usages(NetworkUsage.VM)
	}
	if(this@toNetworkBuilder.vlan != 0){
		networkBuilder
			.vlan(VlanBuilder().id(this@toNetworkBuilder.vlan))
	}
	if(this@toNetworkBuilder.openStackNetworkVo.id.isNotEmpty()) {
		networkBuilder
			.externalProvider(OpenStackNetworkProviderBuilder().id(this@toNetworkBuilder.openStackNetworkVo.id))
	}
	// VnicProfile은 기본생성 정도만
	// cluster 연결(attach), 필수(require)

	return networkBuilder
}

fun NetworkVo.toAddNetworkBuilder(conn: Connection): Network =
	this@toAddNetworkBuilder.toNetworkBuilder(conn).build()

fun NetworkVo.toEditNetworkBuilder(conn: Connection): Network =
	this@toEditNetworkBuilder.toNetworkBuilder(conn).id(this@toEditNetworkBuilder.id).build()

