package com.itinfo.service

import com.itinfo.model.ClusterCreateVo
import com.itinfo.model.ClusterVo
import com.itinfo.model.NetworkProviderVo
import com.itinfo.model.NetworkVo

/**
 * [ClustersService]
 * 클러스터 관리 서비스
 *
 * @since 2023.12.07
 * @author chlee
 */
interface ClustersService {
	fun createCluster(clusterCreateVo: ClusterCreateVo)
	fun updateCluster(clusterCreateVo: ClusterCreateVo)
	fun removeCluster(clusterId: String)
	fun retrieveClusters(): List<ClusterVo>
	fun retrieveCluster(clusterId: String): ClusterVo?
	fun retrieveCreateClusterInfo(clusterId: String): ClusterCreateVo
	fun retrieveNetworks(): List<NetworkVo>
	fun retrieveNetworkProviders(): List<NetworkProviderVo>
}