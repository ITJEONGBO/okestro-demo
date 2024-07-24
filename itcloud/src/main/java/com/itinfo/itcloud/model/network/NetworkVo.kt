package com.itinfo.itcloud.model.network

import com.itinfo.itcloud.model.computing.*
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
 * @property stp [Boolean]
 * @property usageVo [UsageVo]  management 기본이 체크된 상태 (true, 가상머신 네트워크 (기본) (usage))
 * @property vdsmName [String]
 * @property dataCenterVo [DataCenterVo]
// * @property networkClusterVo [NetworkClusterVo]
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
 * @property clusterVo [ClusterVo]
 *
 * <link>
 * @property vnicProfileVos List<[VnicProfileVo]> vnicProfile
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
	val dataCenterVo: DataCenterVo = DataCenterVo(),
	val openStackNetworkVo: OpenStackNetworkVo = OpenStackNetworkVo(),
	val vlan: Int = 0,
	val status: NetworkStatus = NetworkStatus.NON_OPERATIONAL,
	val display: Boolean = false,
	val allotment: Boolean = false,
	val required: Boolean = false,
//	val clusterVo: ClusterVo = ClusterVo(),
	val vnicProfileVos: List<VnicProfileVo> = listOf(),
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
		private var bDataCenterVo: DataCenterVo = DataCenterVo(); fun dataCenterVo(block: () -> DataCenterVo?) { bDataCenterVo = block() ?: DataCenterVo() }
		private var bOpenStackNetworkVo: OpenStackNetworkVo = OpenStackNetworkVo(); fun openStackNetworkVo(block: () -> OpenStackNetworkVo?) { bOpenStackNetworkVo = block() ?: OpenStackNetworkVo() }
		private var bVlan: Int = 0; fun vlan(block: () -> Int?) { bVlan = block() ?: 0 }
		private var bStatus: NetworkStatus = NetworkStatus.NON_OPERATIONAL; fun status(block: () -> NetworkStatus?) { bStatus = block() ?: NetworkStatus.NON_OPERATIONAL }
		private var bDisplay: Boolean = false; fun display(block: () -> Boolean?) { bDisplay = block() ?: false }
		private var bAllotment: Boolean = false; fun allotment(block: () -> Boolean?) { bAllotment = block() ?: false }
		private var bRequired: Boolean = false; fun required(block: () -> Boolean?) { bRequired = block() ?: false }
//		private var bClusterVo: ClusterVo = ClusterVo(); fun clusterVo(block: () -> ClusterVo?) { bClusterVo = block() ?: ClusterVo() }
		private var bVnicProfileVos: List<VnicProfileVo> = listOf(); fun vnicProfileVos(block: () -> List<VnicProfileVo>?) { bVnicProfileVos = block() ?: listOf() }
		private var bClusterVos: List<ClusterVo> = listOf(); fun clusterVos(block: () -> List<ClusterVo>?) { bClusterVos = block() ?: listOf() }

		fun build(): NetworkVo = NetworkVo(bId, bName, bDescription, bComment, bMtu, bPortIsolation, bStp, bUsageVo, bVdsmName, bDataCenterVo, bOpenStackNetworkVo, bVlan, bStatus, bDisplay, bAllotment, bRequired, /*bClusterVo,*/ bVnicProfileVos, bClusterVos)
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
	val vnicProfileVos: List<VnicProfileVo> =
		conn.findAllVnicProfiles().getOrDefault(listOf()).filter { it.network().id() == this@toNetworkVo.id() }
			.toVnicProfileVos(conn)

	return NetworkVo.builder {
		id { this@toNetworkVo.id() }
		name { this@toNetworkVo.name() }
		description { this@toNetworkVo.description() }
		comment { this@toNetworkVo.comment() }
		mtu { this@toNetworkVo.mtu().toInt() }
		portIsolation { this@toNetworkVo.portIsolation() }
		stp { this@toNetworkVo.stp() }
//		usageVo { this@toNetworkVo.usages() }
		vdsmName { this@toNetworkVo.vdsmName() }
		dataCenterVo { conn.findDataCenter(this@toNetworkVo.dataCenter().id()).getOrNull()?.toDataCenterIdName() }
		openStackNetworkVo {
			if(this@toNetworkVo.externalProviderPresent())
				conn.findOpenStackNetworkProvider(this@toNetworkVo.externalProvider().id())
					.getOrNull()
					?.toOpenStackNetworkVo(conn)
			else
				null
		}
		vlan { this@toNetworkVo.vlan().idAsInteger() }
		vnicProfileVos { vnicProfileVos }
	}
}

fun List<Network>.toNetworkVos(conn: Connection): List<NetworkVo> =
	this@toNetworkVos.map { it.toNetworkVo(conn) }



fun Network.toClusterNetworkVo(): NetworkVo {
	return NetworkVo.builder {
		id { this@toClusterNetworkVo.id() }
		name { this@toClusterNetworkVo.name() }
		description { this@toClusterNetworkVo.description() }
		portIsolation { this@toClusterNetworkVo.portIsolation() }
		status { this@toClusterNetworkVo.status() }
		required { this@toClusterNetworkVo.required() }
//		usageVo { this@toNetworkVo.usages() } // TODO
	}
}
fun List<Network>.toClusterNetworkVos(): List<NetworkVo> =
	this@toClusterNetworkVos.map { it.toClusterNetworkVo() }

/**
 * 네트워크 빌더
 */
fun NetworkVo.toNetworkBuilder(conn: Connection): NetworkBuilder {
	return NetworkBuilder()
		.dataCenter(DataCenterBuilder().id(this@toNetworkBuilder.dataCenterVo.id).build())
		.name(this@toNetworkBuilder.name)
		.description(this@toNetworkBuilder.description)
		.comment(this@toNetworkBuilder.comment)
		.vlan(VlanBuilder().id(this@toNetworkBuilder.vlan))
		.usages(if (this@toNetworkBuilder.usageVo.vm) NetworkUsage.VM else NetworkUsage.DEFAULT_ROUTE) // TODO
		.portIsolation(this@toNetworkBuilder.portIsolation)
		.mtu(this@toNetworkBuilder.mtu)
		.stp(this@toNetworkBuilder.stp) // ?
		.externalProvider(
			if (this@toNetworkBuilder.openStackNetworkVo.id.isNotEmpty())
				OpenStackNetworkProviderBuilder().id(this@toNetworkBuilder.openStackNetworkVo.id).build()
			else null
		)
	// .externalProviderPhysicalNetwork(new NetworkBuilder().externalProviderPhysicalNetwork()) // 물리적네트워크
}

fun NetworkVo.toAddNetworkBuilder(conn: Connection): Network =
	this@toAddNetworkBuilder.toNetworkBuilder(conn).build()

fun NetworkVo.toEditNetworkBuilder(conn: Connection): Network =
	this@toEditNetworkBuilder.toNetworkBuilder(conn).id(this@toEditNetworkBuilder.id).build()






@Deprecated("unknown")
fun Network.toNetworkVod(conn: Connection? = null): NetworkVo {
	log.debug("Network.toNetworkVo ... ")
	if (conn?.validate() == true) {
		val network: Network? =
			conn.findNetwork(this@toNetworkVod.id(), "datacenter.clusters")
				.getOrNull()

		val dataCenter: DataCenter? =
			conn.findDataCenter(this@toNetworkVod.dataCenter().id())
				.getOrNull()
		val clusterList: List<Cluster>? = network?.dataCenter()?.clusters()
		val hostList: List<Host> =
			conn.findAllHosts(follow = "networkattachments")
				.getOrDefault(listOf())
				.filter {
					it.networkAttachments().any { na -> na.network().id().equals(this@toNetworkVod.id()) }
				}
		val vmList: List<Vm> =
			conn.findAllVms().getOrDefault(listOf()).filter {
				hostList.any { host -> host.id() == it.host().id() }
			}
//		val templateList: List<Template> =
		val vnicProfileList: List<VnicProfile> =
			conn.findAllVnicProfilesFromNetwork(this@toNetworkVod.id()).getOrDefault(listOf())
		val nlList: List<NetworkLabel> =
			conn.findAllNetworkLabelsFromNetwork(this@toNetworkVod.id()).getOrDefault(listOf())

		val externalProvider: ExternalProvider? =
			if (this@toNetworkVod.externalProviderPresent())
				conn.findOpenStackNetworkProvider(this@toNetworkVod.externalProvider().id())
					.getOrNull()
				else null

		return NetworkVo.builder {
			id { this@toNetworkVod.id() }
			name { this@toNetworkVod.name() }
			description { this@toNetworkVod.description() }
			comment { this@toNetworkVod.comment() }
			mtu { this@toNetworkVod.mtuAsInteger() }
			portIsolation { this@toNetworkVod.portIsolation() }
			stp { this@toNetworkVod.stp() }
			usageVo { this@toNetworkVod.toUsageVo() }
			vdsmName { this@toNetworkVod.vdsmName() }
			dataCenterVo { dataCenter?.toDataCenterIdName() }
//			externalProviderVo { externalProvider?.toExternalProviderVo(conn) }
			vnicProfileVos { vnicProfileList.toVnicProfileVos(conn) }
//			clusterVos { clusterList?.toIdNames() }
//			hostVos { hostList.toHostVos(conn) }
//			vmVos { vmList.toIdNames() }
//			templateVos {  }
		}
	} else{
		return NetworkVo()
	}
}

//fun List<Network>.toNetworkVods(conn: Connection): List<NetworkVo> =
//	this@toNetworkVods.map { it.toNetworkVod(conn) }