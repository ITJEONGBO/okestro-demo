package com.itinfo.service.impl;

import com.itinfo.model.*;
import com.itinfo.service.ClustersService;
import com.itinfo.service.HostsService;
import com.itinfo.service.engine.AdminConnectionService;
import com.itinfo.service.engine.WebsocketService;
import com.itinfo.dao.ClustersDao;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.types.Cluster;
import org.ovirt.engine.sdk4.types.Host;
import org.ovirt.engine.sdk4.types.Network;
import org.ovirt.engine.sdk4.types.OpenStackNetworkProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ClustersServiceImpl extends BaseService implements ClustersService {
	@Autowired private AdminConnectionService adminConnectionService;
	@Autowired private ClustersDao clustersDao;
	@Autowired private HostsService hostsService;
	@Autowired private WebsocketService websocketService;

	@Async("karajanTaskExecutor")
	@Override
	public void createCluster(ClusterCreateVo clusterCreateVo) {
		log.info("... createCluster");
		Connection connection = adminConnectionService.getConnection();
		Cluster c =
				ModelsKt.toCluster(clusterCreateVo, connection);
		Cluster res
				= getSysSrvHelper().addCluster(connection, c);

		MessageVo message
				= MessageVo.createMessage(MessageType.CLUSTER_ADD, res != null, clusterCreateVo.getName(), "");
		try { Thread.sleep(1000L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }

		websocketService.sendMessage("/topic/notify", new Gson().toJson(message));
		websocketService.sendMessage("/topic/clusters/reload", "");
	}

	@Async("karajanTaskExecutor")
	@Override
	public void updateCluster(ClusterCreateVo clusterCreateVo) {
		log.info("... updateCluster");
		Connection connection = adminConnectionService.getConnection();
		Cluster c
				= ModelsKt.toCluster(clusterCreateVo, connection);
		Cluster res
				= getSysSrvHelper().updateCluster(connection, clusterCreateVo.getId(), c);

		MessageVo message
				= MessageVo.createMessage(MessageType.CLUSTER_UPDATE, res != null, clusterCreateVo.getName(), "");
		try { Thread.sleep(1000L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }

		websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
		websocketService.sendMessage("/topic/clusters/reload", "");
	}

	@Async("karajanTaskExecutor")
	@Override
	public void removeCluster(String clusterId) {
		log.info("... removeCluster('{}')", clusterId);
		Connection connection = adminConnectionService.getConnection();

		Cluster cluster
				= getSysSrvHelper().findCluster(connection, clusterId);
		boolean removeRes
				= getSysSrvHelper().removeCluster(connection, clusterId);
		MessageVo message
				= MessageVo.createMessage(MessageType.CLUSTER_REMOVE, removeRes, cluster.name(), "");
		try { Thread.sleep(1000L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }

		websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
		websocketService.sendMessage("/topic/clusters/reload", "");
	}

	@Override
	public List<ClusterVo> retrieveClusters() {
		log.info("... retrieveClusters");
		Connection connection = adminConnectionService.getConnection();

		List<Cluster> clustersFound
				= getSysSrvHelper().findAllClusters(connection, "");
		log.info("... retrieveClusters clustersFound[{}]", clustersFound.size());

		List<ClusterVo> clusterVos
				= ModelsKt.toClusterVos(clustersFound, connection, clustersDao);

		return clusterVos;
	}

	@Override
	public ClusterVo retrieveCluster(String clusterId) {
		log.info("... retrieveCluster('{}')", clusterId);
		Connection connection = adminConnectionService.getConnection();
		Cluster cluster
				= getSysSrvHelper().findCluster(connection, clusterId);
		ClusterVo clusterVo
				= ModelsKt.toClusterVo(cluster, connection, clustersDao);
		setClusterDetailHosts(connection, clusterId, clusterVo);
		return clusterVo;
	}

	private void setClusterDetailHosts(Connection connection, String clusterId, ClusterVo clusterVo) {
		log.info("... setClusterDetailHosts");
		List<Host> hosts
				= getSysSrvHelper().findAllHosts(connection, "");
		List<HostDetailVo> hostsDetail = new ArrayList<>();
		List<String> ids = new ArrayList<>();
		for (Host host : hosts) {
			if (clusterId.equals(host.cluster().id())) {
				HostDetailVo hostDetailVo = hostsService.getHostInfo(connection, host);
				hostsDetail.add(hostDetailVo);
				ids.add(hostDetailVo.getId());
			}
		}
		clusterVo.setHostsDetail(hostsDetail);
	}


	@Override
	public ClusterCreateVo retrieveCreateClusterInfo(String clusterId) {
		log.info("... retrieveCreateClusterInfo('{}')", clusterId);
		Connection connection = adminConnectionService.getConnection();
		Cluster cluster
				= getSysSrvHelper().findCluster(connection, clusterId);
		if (cluster == null) {
			log.warn("no cluster FOUND! id: '{}'", clusterId);
			return new ClusterCreateVo();
		}
		return ModelsKt.toClusterCreateVo(cluster, connection);
	}

	@Override
	public List<NetworkVo> retrieveNetworks() {
		log.info("... retrieveNetworks");
		Connection connection = adminConnectionService.getConnection();
		List<Network> nws
				= getSysSrvHelper().findAllNetworks(connection);
		return ModelsKt.toNetworkVos(nws);
	}

	@Override
	public List<NetworkProviderVo> retrieveNetworkProviders() {
		log.info("... retrieveNetworkProviders");
		Connection connection = adminConnectionService.getConnection();
		List<OpenStackNetworkProvider> nwps
				= getSysSrvHelper().findAllOpenStackNetworkProviders(connection);

		return ModelsKt.toNetworkProviderVos(nwps);
	}
}
