package com.itinfo.util.ovirt

import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.error.FailureType
import com.itinfo.util.ovirt.error.toError
import com.itinfo.util.ovirt.error.toResult
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.services.AffinityGroupsService
import org.ovirt.engine.sdk4.services.AffinityGroupService
import org.ovirt.engine.sdk4.services.AffinityGroupHostLabelsService
import org.ovirt.engine.sdk4.services.AffinityGroupHostsService
import org.ovirt.engine.sdk4.services.AffinityGroupVmLabelsService
import org.ovirt.engine.sdk4.services.AffinityGroupVmsService
import org.ovirt.engine.sdk4.services.ClustersService
import org.ovirt.engine.sdk4.services.ClusterService
import org.ovirt.engine.sdk4.services.ClusterNetworksService
import org.ovirt.engine.sdk4.services.ClusterNetworkService
import org.ovirt.engine.sdk4.services.ClusterExternalProvidersService
import org.ovirt.engine.sdk4.types.AffinityGroup
import org.ovirt.engine.sdk4.types.AffinityLabel
import org.ovirt.engine.sdk4.types.Cluster
import org.ovirt.engine.sdk4.types.Host
import org.ovirt.engine.sdk4.types.Vm
import org.ovirt.engine.sdk4.types.ExternalProvider
import org.ovirt.engine.sdk4.types.Network
import org.ovirt.engine.sdk4.types.Permission
import kotlin.Error

private fun Connection.srvClusters(): ClustersService =
	this.systemService.clustersService()

fun Connection.findAllClusters(searchQuery: String = "", follow: String = ""): Result<List<Cluster>> = runCatching {
	if (searchQuery.isNotEmpty() && follow.isNotEmpty())
		this.srvClusters().list().search(searchQuery).follow(follow).caseSensitive(false).send().clusters() ?: listOf()
	else if (searchQuery.isNotEmpty())
		this.srvClusters().list().search(searchQuery).caseSensitive(false).send().clusters() ?: listOf()
	else if (follow.isNotEmpty())
		this.srvClusters().list().follow(follow).caseSensitive(false).send().clusters() ?: listOf()
	else
		this.srvClusters().list().send().clusters() ?: listOf()
}.onSuccess {
	Term.CLUSTER.logSuccess("목록조회")
}.onFailure {
	Term.CLUSTER.logFail("목록조회", it)
	throw it
}


private fun Connection.srvCluster(id: String): ClusterService =
	this.srvClusters().clusterService(id)

fun Connection.findCluster(clusterId: String): Result<Cluster?> = runCatching {
	this.srvCluster(clusterId).get().send().cluster()
}.onSuccess {
	Term.CLUSTER.logSuccess("상세조회")
}.onFailure {
	Term.CLUSTER.logFail("상세조회", it)
	throw it
}

fun Connection.findClusterName(clusterId: String): Result<String> = runCatching {
	this.findCluster(clusterId).getOrNull()?.name() ?: ""
}.onSuccess {
	Term.CLUSTER.logSuccess("이름조회")
}.onFailure {
	Term.CLUSTER.logFail("이름조회", it)
	throw it
}

fun List<Cluster>.nameDuplicateCluster(clusterName: String, clusterId: String? = null): Boolean =
	this.filter { it.id() != clusterId}.any { it.name() == clusterName }


fun Connection.addCluster(cluster: Cluster): Result<Cluster?> = runCatching {
	if (this.findAllClusters()
			.getOrDefault(listOf())
			.nameDuplicateCluster(cluster.name())) {
		return FailureType.DUPLICATE.toResult(Term.CLUSTER.desc)
	}
	val clusterAdded: Cluster =
		this.srvClusters().add().cluster(cluster).send().cluster()
	clusterAdded
}.onSuccess {
	Term.CLUSTER.logSuccess("생성")
}.onFailure {
	Term.CLUSTER.logFail("생성", it)
	throw it
}

fun Connection.updateCluster(cluster: Cluster): Result<Cluster?> = runCatching {
	if (this.findAllClusters()
			.getOrDefault(listOf())
			.nameDuplicateCluster(cluster.name(), cluster.id())) {
		return FailureType.DUPLICATE.toResult(Term.CLUSTER.desc)
	}
	val clusterUpdated: Cluster =
		this.srvCluster(cluster.id()).update().cluster(cluster).send().cluster()
	clusterUpdated
}.onSuccess {
	Term.CLUSTER.logSuccess("편집")
}.onFailure {
	Term.CLUSTER.logFail("편집", it)
	throw it
}

fun Connection.removeCluster(clusterId: String): Result<Boolean> = runCatching {
	val cluster: Cluster =
		this.findCluster(clusterId).getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toError()

	this.srvCluster(cluster.id()).remove().send()
	this.expectClusterDeleted(clusterId)
}.onSuccess {
	Term.CLUSTER.logSuccess("삭제")
}.onFailure {
	Term.CLUSTER.logFail("삭제", it)
	throw it
}


@Throws(InterruptedException::class)
fun Connection.expectClusterDeleted(clusterId: String, timeout: Long = 60000L, interval: Long = 1000L): Boolean {
	val startTime = System.currentTimeMillis()
	while (true) {
		val clusters: List<Cluster> =
			this.findAllClusters().getOrDefault(listOf())
		val clusterToRemove: Cluster? = clusters.firstOrNull() { it.id() == clusterId } // cluster 어느것이라도 매치되는것이 있다면
		if (clusterToRemove == null) { // !(매치되는것이 있다)
			Term.CLUSTER.logSuccess("삭제")
			return true
		} else if (System.currentTimeMillis() - startTime > timeout) {
			log.error("{} {} 삭제 실패 ... 시간 초과", Term.CLUSTER.desc, clusterToRemove.name())
			return false
		}
		log.debug("{} 삭제 진행중 ... ", Term.CLUSTER.desc)
		Thread.sleep(interval)
	}
}


private fun Connection.srvClusterNetworks(clusterId: String): ClusterNetworksService =
	this.srvCluster(clusterId).networksService()


fun Connection.findAllNetworksFromCluster(clusterId: String): Result<List<Network>> = runCatching {
	if (this@findAllNetworksFromCluster.findCluster(clusterId).isFailure) {
		return FailureType.NOT_FOUND.toResult(Term.CLUSTER.desc)
	}
	this.srvClusterNetworks(clusterId).list().send().networks()
}.onSuccess {
	Term.CLUSTER.logSuccessWithin(Term.NETWORK, "목록조회")
}.onFailure {
	Term.CLUSTER.logFailWithin(Term.NETWORK, "목록조회", it)
	throw it
}

private fun Connection.srvNetworkFromCluster(clusterId: String, networkId: String): ClusterNetworkService =
	this.srvClusterNetworks(clusterId).networkService(networkId)

fun Connection.findNetworkFromCluster(clusterId: String, networkId: String): Result<Network?> = runCatching {
	this.srvNetworkFromCluster(clusterId, networkId).get().send().network()
}.onSuccess {
	Term.CLUSTER.logSuccessWithin(Term.NETWORK, "상세조회")
}.onFailure {
	Term.CLUSTER.logFailWithin(Term.NETWORK, "상세조회", it)
	throw it
}

fun Connection.addNetworkFromCluster(clusterId: String, network: Network): Result<Network?> = runCatching {
	this.srvCluster(clusterId).networksService().add().network(network).send().network()
}.onSuccess {
	Term.CLUSTER.logSuccessWithin(Term.NETWORK, "생성")
}.onFailure {
	Term.CLUSTER.logFailWithin(Term.NETWORK, "생성", it)
	throw it
}

fun Connection.updateNetworkFromCluster(clusterId: String, network: Network): Result<Network?> = runCatching {
	this.srvNetworkFromCluster(clusterId, network.id()).update().network(network).send().network()
}.onSuccess {
	Term.CLUSTER.logSuccessWithin(Term.NETWORK, "편집")
}.onFailure {
	Term.CLUSTER.logFailWithin(Term.NETWORK, "편집", it)
	throw it
}

fun Connection.removeNetworkFromCluster(clusterId: String, networkId: String): Result<Boolean> = runCatching {
	this.srvNetworkFromCluster(clusterId, networkId).remove().send()
	false
}.onSuccess {
	Term.CLUSTER.logSuccessWithin(Term.NETWORK, "삭제")
}.onFailure {
	Term.CLUSTER.logFailWithin(Term.NETWORK, "삭제", it)
	throw it
}

/*
private fun Connection.srvDataCenterFromCluster(clusterId: String): DataCenterService =
	this.srvCluster(clusterId)

fun Connection.findDataCenterFromCluster(clusterId: String): DataCenter =
	this.srvDataCenterFromCluster(clusterId)
*/

private fun Connection.srvExternalNetworkProviders(clusterId: String): ClusterExternalProvidersService =
	this.srvCluster(clusterId).externalNetworkProvidersService()

fun Connection.findAllExternalNetworkProviders(clusterId: String): Result<List<ExternalProvider>> = runCatching {
	srvExternalNetworkProviders(clusterId).list().send().providers()
}.onSuccess {
	Term.EXTERNAL_NETWORK_PROVIDER.logSuccess("목록조회")
}.onFailure {
	Term.EXTERNAL_NETWORK_PROVIDER.logFail("목록조회", it)
	throw it
}

private fun Connection.srvAffinityGroupsFromCluster(clusterId: String): AffinityGroupsService =
	this.srvCluster(clusterId).affinityGroupsService()

fun Connection.findAllAffinityGroupsFromCluster(clusterId: String): Result<List<AffinityGroup>> = runCatching {
	this@findAllAffinityGroupsFromCluster.findCluster(clusterId).getOrNull() ?: throw ErrorPattern.DATACENTER_NOT_FOUND.toError()
	this.srvAffinityGroupsFromCluster(clusterId).list().send().groups()

}.onSuccess {
	Term.CLUSTER.logSuccessWithin(Term.AFFINITY_GROUP, "목록조회", clusterId)
}.onFailure {
	Term.CLUSTER.logFailWithin(Term.AFFINITY_GROUP, "목록조회", it)
	throw it
}

private fun Connection.srvAffinityGroupFromCluster(clusterId: String, agId: String): AffinityGroupService =
	this.srvAffinityGroupsFromCluster(clusterId).groupService(agId)

fun Connection.findAffinityGroupFromCluster(clusterId: String, agId: String, follow: String = ""): Result<AffinityGroup?> = runCatching {
	if (follow.isNotEmpty())
		this.srvAffinityGroupFromCluster(clusterId, agId).get().follow(follow).send().group()
	else
		this.srvAffinityGroupFromCluster(clusterId, agId).get().send().group()
}.onSuccess {
	Term.CLUSTER.logSuccessWithin(Term.AFFINITY_GROUP, "상세조회", clusterId)
}.onFailure {
	Term.CLUSTER.logFailWithin(Term.AFFINITY_GROUP, "상세조회", it)
	throw it
}

fun Connection.addAffinityGroupFromCluster(
	clusterId: String,
	affinityGroup: AffinityGroup,
	affinityGroupName: String? = "",
	affinityGroupId: String? = null
): Result<AffinityGroup> = runCatching {
	val affinityGroups: List<AffinityGroup> =
		this@addAffinityGroupFromCluster.findAllAffinityGroupsFromCluster(clusterId)
			.getOrDefault(listOf())
			.filter { it.id() != affinityGroupId }
	// 선호도 그룹 이름 중복검사
	if (affinityGroups.any { it.name() == affinityGroupName }) {
		log.error("addAffinityGroupFromCluster ... 선호도그룹 이름 중복")
		return Result.failure(Error("실패: 선호도그룹 이름 중복"))
	}
	this@addAffinityGroupFromCluster.srvAffinityGroupsFromCluster(clusterId).add().group(affinityGroup).send().group()
}.onSuccess {
	Term.CLUSTER.logSuccessWithin(Term.AFFINITY_GROUP, "생성", clusterId)
}.onFailure {
	Term.CLUSTER.logFailWithin(Term.AFFINITY_GROUP, "생성", it)
	throw it
}

fun Connection.updateAffinityGroupFromCluster(
	clusterId: String,
	affinityGroup: AffinityGroup,
	affinityGroupName: String? = "",
): Result<AffinityGroup> = runCatching {
	val affinityGroups: List<AffinityGroup> =
		this@updateAffinityGroupFromCluster.findAllAffinityGroupsFromCluster(clusterId)
			.getOrDefault(listOf())
			.filter { it.id() != affinityGroup.id() }
	// 선호도 그룹 이름 중복검사
	if (affinityGroups.any { it.name() == affinityGroupName }) {
		log.error("updateAffinityGroupFromCluster ... 선호도그룹 이름 중복")
		return Result.failure(Error("실패: 선호도그룹 이름 중복"))
	}
	this@updateAffinityGroupFromCluster.srvAffinityGroupFromCluster(clusterId, affinityGroup.id()).update().group(affinityGroup).send().group()
}.onSuccess {
	Term.CLUSTER.logSuccessWithin(Term.AFFINITY_GROUP, "편집", clusterId)
}.onFailure {
	Term.CLUSTER.logFailWithin(Term.AFFINITY_GROUP, "편집", it)
	throw it
}

fun Connection.removeAffinityGroupFromCluster(
	clusterId: String,
	affinityGroupId: String
): Result<Boolean> = runCatching {
	this@removeAffinityGroupFromCluster.srvAffinityGroupFromCluster(clusterId, affinityGroupId).remove().send()
	true
}.onSuccess {
	Term.CLUSTER.logSuccessWithin(Term.AFFINITY_GROUP, "삭제")
}.onFailure {
	Term.CLUSTER.logFailWithin(Term.AFFINITY_GROUP, "삭제", it)
	throw it
}

fun Connection.srvAffinityGroupHostsFromCluster(clusterId: String, agId: String): AffinityGroupHostsService =
	this.srvAffinityGroupFromCluster(clusterId, agId).hostsService()

fun Connection.findAllAffinityGroupHostsFromCluster(clusterId: String, agId: String): List<Host> =
	this.srvAffinityGroupHostsFromCluster(clusterId, agId).list().send().hosts()

fun Connection.srvAffinityGroupHostLabelsFromCluster(clusterId: String, agId: String): AffinityGroupHostLabelsService =
	this.srvAffinityGroupFromCluster(clusterId, agId).hostLabelsService()

fun Connection.findAllAffinityGroupHostLabelsFromCluster(clusterId: String, agId: String): List<AffinityLabel> =
	this.srvAffinityGroupHostLabelsFromCluster(clusterId, agId).list().send().labels()

fun Connection.srvAffinityGroupVmsFromCluster(clusterId: String, agId: String): AffinityGroupVmsService =
	this.srvAffinityGroupFromCluster(clusterId, agId).vmsService()

fun Connection.findAllAffinityGroupVmsFromCluster(clusterId: String, agId: String): List<Vm> =
	this.srvAffinityGroupVmsFromCluster(clusterId, agId).list().send().vms()

fun Connection.srvAffinityGroupVmLabelsFromCluster(clusterId: String, agId: String): AffinityGroupVmLabelsService =
	this.srvAffinityGroupFromCluster(clusterId, agId).vmLabelsService()

fun Connection.findAllAffinityGroupVmLabelsFromCluster(clusterId: String, agId: String): List<AffinityLabel> =
	this.srvAffinityGroupVmLabelsFromCluster(clusterId, agId).list().send().labels()

fun Connection.findAllPermissionsFromCluster(clusterId: String): Result<List<Permission>> = runCatching {
	if (this@findAllPermissionsFromCluster.findCluster(clusterId).getOrNull() == null) {
		log.warn("findAllPermissionsFromCluster ... 클러스터 없음")
		listOf()
	} else {
		this@findAllPermissionsFromCluster.srvCluster(clusterId).permissionsService().list().send().permissions()
	}
}.onSuccess {
	Term.CLUSTER.logSuccessWithin(Term.PERMISSION, "목록조회")
}.onFailure {
	Term.CLUSTER.logFailWithin(Term.PERMISSION, "목록조회", it)
	throw it
}
