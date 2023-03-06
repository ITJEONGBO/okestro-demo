package com.itinfo.service;

import com.itinfo.model.ClusterCreateVo;
import com.itinfo.model.ClusterVo;
import com.itinfo.model.NetworkProviderVo;
import com.itinfo.model.NetworkVo;
import java.util.List;

public interface ClustersService {
	void createCluster(ClusterCreateVo clusterCreateVo);

	void updateCluster(ClusterCreateVo clusterCreateVo);

	void removeCluster(String clusterId);

	List<ClusterVo> retrieveClusters();

	ClusterVo retrieveCluster(String clusterId);

	ClusterCreateVo retrieveCreateClusterInfo(String clusterId);

	List<NetworkVo> retrieveNetworks();

	List<NetworkProviderVo> retrieveNetworkProviders();
}

