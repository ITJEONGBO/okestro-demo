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
import java.util.stream.Collectors;

import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.types.Cluster;
import org.ovirt.engine.sdk4.types.Host;
import org.ovirt.engine.sdk4.types.Network;
import org.ovirt.engine.sdk4.types.OpenStackNetworkProvider;
import org.ovirt.engine.sdk4.types.Vm;

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
		Connection connection = adminConnectionService.getConnection();
		Gson gson = new Gson();
		Cluster c = ModelsKt.toCluster(clusterCreateVo, connection);
		Cluster res
				= getSysSrvHelper().addCluster(connection, c);
		MessageVo message
				= MessageVo.createMessage(MessageType.CLUSTER_ADD, res != null, clusterCreateVo.getName(), "");

		try { Thread.sleep(1000L); } catch (Exception exception) {}
		websocketService.sendMessage("/topic/notify", gson.toJson(message));
		websocketService.sendMessage("/topic/clusters/reload", "");
	}

	@Async("karajanTaskExecutor")
	@Override
	public void updateCluster(ClusterCreateVo clusterCreateVo) {
		Connection connection = adminConnectionService.getConnection();
		Gson gson = new Gson();
		Cluster c = ModelsKt.toCluster(clusterCreateVo, connection);
		Cluster res
				= getSysSrvHelper().updateCluster(connection, clusterCreateVo.getId(), c);
		MessageVo message
				= MessageVo.createMessage(MessageType.CLUSTER_UPDATE, res != null, clusterCreateVo.getName(), "");

		try { Thread.sleep(1000L); } catch (Exception exception) {}
		websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
		websocketService.sendMessage("/topic/clusters/reload", "");
	}

	@Async("karajanTaskExecutor")
	@Override
	public void removeCluster(String clusterId) {
		Connection connection = adminConnectionService.getConnection();
		Cluster cluster
				= getSysSrvHelper().findCluster(connection, clusterId);
		boolean removeRes
				= getSysSrvHelper().removeCluster(connection, clusterId);
		MessageVo message
				= MessageVo.createMessage(MessageType.CLUSTER_REMOVE, removeRes, cluster.name(), "");

		try { Thread.sleep(1000L); } catch (Exception exception) {}
		websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
		websocketService.sendMessage("/topic/clusters/reload", "");
	}

	@Override
	public List<ClusterVo> retrieveClusters() {
		Connection connection = adminConnectionService.getConnection();
		List<Cluster> clusters
				= getSysSrvHelper().findAllClusters(connection, "");
		log.info("clusters size : " + clusters.size());
		List<ClusterVo> clustersInfo
				= ModelsKt.toClusterVos(clusters, connection);
		return clustersInfo;
	}

	@Override
	public ClusterVo retrieveCluster(String clusterId) {
		Connection connection = adminConnectionService.getConnection();
		Cluster cluster
				= getSysSrvHelper().findCluster(connection, clusterId);
		ClusterVo clusterVo = new ClusterVo();
		clusterVo.setId(clusterId);
		clusterVo.setName(cluster.name());
		clusterVo.setDescription(cluster.description());
		clusterVo.setCpuType(cluster.cpu().type());
		clusterVo.setCpuImage(cluster.cpu().architecture().name());
		List<Network> networks
				= getSysSrvHelper().findAllNetworksFromCluster(connection, clusterId);
		NetworkVo networkVo;
		if (networks.size() != 0) {
			Network network = networks.get(0);
			networkVo = ModelsKt.toNetworkVo(network);
			clusterVo.setNetwork(networkVo);
		}
		setClusterDetailHosts(connection, clusterId, clusterVo);
		List<VmSummaryVo> vmSummaries
				= getClusterDetailVms(connection, clusterId);
		clusterVo.setVmSummaries(vmSummaries);
		return clusterVo;
	}

	private void setClusterDetailHosts(Connection connection, String clusterId, ClusterVo clusterVo) {
		List<Host> hosts
				= getSysSrvHelper().findAllHosts(connection, "");
		List<String> ids = new ArrayList<>();
		List<HostDetailVo> hostsDetail = new ArrayList<>();
		for (Host host : hosts) {
			if (clusterId.equals(host.cluster().id())) {
				HostDetailVo hostDetailVo = hostsService.getHostInfo(connection, host);
				hostsDetail.add(hostDetailVo);
				ids.add(hostDetailVo.getId());
			}
		}
		if (ids.size() > 0) {
			List<HostUsageVo> usageVoList
					= clustersDao.retrieveClusterUsage(ids);
			List<UsageVo> usageVos
					= ModelsKt.toUsageVosWithHostUsage(usageVoList);
			clusterVo.setUsageVos(usageVos);
		}
		clusterVo.setHostsDetail(hostsDetail);
	}

	private List<VmSummaryVo> getClusterDetailVms(Connection connection, String clusterId) {
		List<Vm> vms = getSysSrvHelper().findAllVms(connection, "")
					.stream().filter(vm -> clusterId.equals(vm.cluster().id()))
					.collect(Collectors.toList());
		List<VmSummaryVo> vmSummaries = new ArrayList<>();
		for (Vm vm : vms) {
			if (clusterId.equals(vm.cluster().id())) {
				VmSummaryVo vmSummary = this.hostsService.getVmInfo(connection, vm);
				vmSummaries.add(vmSummary);
			}
		}
		return vmSummaries;
	}

	@Override
	public ClusterCreateVo retrieveCreateClusterInfo(String clusterId) {
		Connection connection = adminConnectionService.getConnection();
		Cluster cluster
				= getSysSrvHelper().findCluster(connection, clusterId);

		return ModelsKt.toClusterCreateVo(cluster, connection);
	}

	@Override
	public List<NetworkVo> retrieveNetworks() {
		Connection connection = adminConnectionService.getConnection();
		List<Network> nws
				= getSysSrvHelper().findAllNetworks(connection);

		return ModelsKt.toNetworkVos(nws);
	}

	@Override
	public List<NetworkProviderVo> retrieveNetworkProviders() {
		Connection connection = adminConnectionService.getConnection();
		List<OpenStackNetworkProvider> nwps
				= getSysSrvHelper().findAllOpenStackNetworkProviders(connection);

		return ModelsKt.toNetworkProviderVos(nwps);
	}
}
