package com.itinfo.service.impl;

import com.itinfo.model.*;
import com.itinfo.service.ItInfoNetworkService;
import com.itinfo.service.engine.AdminConnectionService;
import com.itinfo.service.engine.WebsocketService;

import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.builders.Builders;
import org.ovirt.engine.sdk4.builders.NetworkBuilder;
import org.ovirt.engine.sdk4.types.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ItInfoNetworkServiceImpl extends BaseService implements ItInfoNetworkService {
	@Autowired private AdminConnectionService adminConnectionService;
	@Autowired	private WebsocketService websocketService;

	@Override
	public List<ItInfoNetworkVo> getNetworkList() {
		log.info("... getNetworkList");
		Connection connection = adminConnectionService.getConnection();
		List<Network> networkList
				= getSysSrvHelper().findAllNetworks(connection);
		List<ItInfoNetworkVo> list
				= ModelsKt.toItInfoNetworkVos(networkList, connection);
		return list;
	}


	@Override
	public List<ItInfoNetworkVo> getHostNetworkList(String id) {
		log.info("... getHostNetworkList('{}')", id);
		Connection connection = adminConnectionService.getConnection();

		List<HostNic> hostNics
				= getSysSrvHelper().findAllNicsFromHost(connection, id);
		List<Vm> vms
				= getSysSrvHelper().findAllVms(connection, "");
		DataCenter dataCenter
				= getSysSrvHelper().findAllDataCenters(connection).get(0);
		List<Cluster> clusters
				= getSysSrvHelper().findAllClusters(connection, "");
		List<Network> networkList
				= getSysSrvHelper().findAllNetworks(connection);
		List<OpenStackNetworkProvider> openStackNetworkProviders
				= getSysSrvHelper().findAllOpenStackNetworkProviders(connection);
		List<Qos> qoss
				= getSysSrvHelper().findAllQossFromDataCenter(connection, dataCenter.id());

		List<ItInfoNetworkClusterVo> itInfoNetworkClusterVos = new ArrayList<>();
		List<ItInfoNetworkClusterVo> itInfoNetworkClusterVoList = new ArrayList<>();
		List<ItInfoNetworkVo> list = new ArrayList<>();

		networkList.forEach(network -> {
			ItInfoNetworkVo networkVo = new ItInfoNetworkVo();
			for (HostNic hostNic : hostNics) {
				if (hostNic.network() != null && network.id().equals(hostNic.network().id())) {
					if (hostNic.baseInterface() == null) {
						networkVo.setBaseInterface(hostNic.name());
						break;
					}
					if (hostNic.baseInterface() != null) {
						networkVo.setBaseInterface(hostNic.baseInterface());
						break;
					}
				}
			}
			for (Cluster cluster : clusters) {
				List<Network> networkss
						= getSysSrvHelper().findAllNetworksFromCluster(connection, cluster.id());;
				for (Network clusterNetwork : networkss) {
					ItInfoNetworkClusterVo networkClusterVo = new ItInfoNetworkClusterVo();
					List<ItInfoNetworkUsagesVo> usages = new ArrayList<>();
					if (clusterNetwork.id().equalsIgnoreCase(network.id()) && clusterNetwork.required())
						networkVo.setRequired(clusterNetwork.required());
					clusterNetwork.usages().forEach(cn -> {
						ItInfoNetworkUsagesVo usageVo = new ItInfoNetworkUsagesVo();
						usageVo.setUsage(cn.name());
						usages.add(usageVo);
					});
					networkClusterVo.setUsages(usages);
					networkClusterVo.setStatus(clusterNetwork.status().value());
					itInfoNetworkClusterVos.add(networkClusterVo);
				}
			}
			List<VnicProfile> vnicProfiles
					= getSysSrvHelper().findAllVnicProfilesFromNetwork(connection, network.id());
			List<String> vnicIds = new ArrayList<>();
			for (VnicProfile vnic : vnicProfiles)
				vnicIds.add(vnic.id());
			for (Vm vm : vms) {
				List<Nic> nics
						= getSysSrvHelper().findNicsFromVm(connection, vm.id());
				for (Nic nic : nics) {
					if (nic.idPresent())
						for (String vnicId : vnicIds) {
							if (nic.vnicProfile() != null && vnicId.equalsIgnoreCase(nic.vnicProfile().id()) && "up".equals(vm.status().value()))
								networkVo.setUsingVmNetwork(true);
						}
				}
			}
			networkVo.setComment(network.comment());
			networkVo.setId(network.id());
			networkVo.setName(network.name());
			networkVo.setDescription(network.description());
			if (network.mtuPresent())
				networkVo.setMtu(String.valueOf(network.mtuAsInteger()));
			if (network.vlanPresent())
				networkVo.setVlan(String.valueOf(network.vlan().id().intValue()));
			list.add(networkVo);
		});
		if (!networkList.isEmpty())
			itInfoNetworkClusterVoList = itInfoNetworkClusterVos.subList(0, networkList.size());
		for (int j = 0; j < list.size(); j++) {
			list.get(j).setUsages((itInfoNetworkClusterVoList.get(j)).getUsages());
			list.get(j).setStatus((itInfoNetworkClusterVoList.get(j)).getStatus());
		}
		return list;
	}

	@Override
	public ItInfoNetworkGroupVo getNetworkDetail(ItInfoNetworkVo castanetsNetworkVo) {
		String networkId = castanetsNetworkVo.getId();
		log.info("... getNetworkDetail({})", networkId);
		Connection c = adminConnectionService.getConnection();
		List<Cluster> clusters
				= getSysSrvHelper().findAllClusters(c, "");
		List<ItInfoNetworkClusterVo> networkClusterVos
				= ModelsKt.toItInfoNetworkClusterVos(clusters, c, networkId);

		List<ItInfoNetworkHostVo> networkHostVos
				= getNetworkHost(networkId);
		castanetsNetworkVo
				= getNetwork(networkId);

		List<ItInfoNetworkVmVo> castanetsNetworkVmVos
				= getNetworkVm(networkId);


		return new ItInfoNetworkGroupVo(
				castanetsNetworkVo,
				networkClusterVos,
				networkHostVos,
				castanetsNetworkVmVos
		);
	}

	@Override
	public List<ItInfoNetworkClusterVo> getNetworkCluster(String clusterId, String networkId) {
		log.info("... getNetworkCluster('{}', '{}')", clusterId, networkId);
		Connection connection = adminConnectionService.getConnection();
		List<Cluster> clusters
				= getSysSrvHelper().findAllClusters(connection, "");
		List<ItInfoNetworkClusterVo> list
				= ModelsKt.toItInfoNetworkClusterVos(clusters, connection, networkId);
		return list;
	}

	public ItInfoNetworkVo getNetwork(String networkId) {
		log.info("... getNetwork('{}')", networkId);
		Connection connection = adminConnectionService.getConnection();
		List<Network> networkList
				= getSysSrvHelper().findAllNetworks(connection);
		/*
		DataCenter dataCenter
				= getSysSrvHelper().findAllDataCenters(connection).get(0);
		List<OpenStackNetworkProvider> openStackNetworkProviders
				= getSysSrvHelper().findAllOpenStackNetworkProviders(connection);
		List<Qos> qoss
				= getSysSrvHelper().findAllQossFromDataCenter(connection, dataCenter.id());
		*/

		Network n = networkList.stream()
					.filter((Network network) -> network.id().equalsIgnoreCase(networkId))
					.collect(Collectors.toList()).get(0);

		return ModelsKt.toItInfoNetworkVo(n, connection);
	}


	@Override
	public List<ItInfoNetworkHostVo> getNetworkHost(String networkId) {
		log.info("... getNetworkHost('{}')", networkId);
		Connection connection = adminConnectionService.getConnection();
		List<Host> hosts
				= getSysSrvHelper().findAllHosts(connection, "");
		return ModelsKt.toItInfoNetworkHostVos(hosts, connection, networkId);
	}

	@Override
	public List<ItInfoNetworkVmVo> getNetworkVm(String networkId) {
		log.info("... getNetworkVm('{}')", networkId);
		Connection connection = adminConnectionService.getConnection();
		List<ItInfoNetworkVmVo> networkVmVos = new ArrayList<>();
		List<Vm> vms
				= getSysSrvHelper().findAllVms(connection, "");
		List<Cluster> clusters
				= getSysSrvHelper().findAllClusters(connection, "");
		List<VnicProfile> vnicProfiles
				= getSysSrvHelper().findAllVnicProfilesFromNetwork(connection, networkId);
		List<String> vnicIds = new ArrayList<>();
		for (VnicProfile vnic : vnicProfiles)
			vnicIds.add(vnic.id());
		BigDecimal number = new BigDecimal("1000000");
		vms.forEach(vm -> {
			List<Nic> nics
					= getSysSrvHelper().findNicsFromVm(connection, vm.id());
			nics.forEach(nic -> {
				if (nic.idPresent()) {
					Boolean vnicIdCheck = false;
					for (String vnicId : vnicIds)
						if (nic.vnicProfile() != null && vnicId.equalsIgnoreCase(nic.vnicProfile().id()))
							vnicIdCheck = true;

					if (vnicIdCheck) {
						ItInfoNetworkVmVo castanetsNetworkVmVo = new ItInfoNetworkVmVo();
						castanetsNetworkVmVo.setVmName(vm.name());
						castanetsNetworkVmVo.setVmStatus(vm.status().value());
						for (Cluster cluster : clusters)
							if (cluster.id().equalsIgnoreCase(vm.cluster().id()))
								castanetsNetworkVmVo.setVmCluster(cluster.name());

						castanetsNetworkVmVo.setNicName(nic.name());
						if (nic.reportedDevicesPresent()) {
							String ips = "";
							for (int i = 0; i < nic.reportedDevices().size(); i++) {
								for (int j = 0; j < (nic.reportedDevices().get(i)).ips().size(); j++) {
									ips = ips + " " + ((nic.reportedDevices().get(i)).ips().get(j)).address();
								}
							}
							castanetsNetworkVmVo.setIp(ips);
						}
						if (vm.fqdnPresent()) castanetsNetworkVmVo.setFqdn(vm.fqdn());
						if (nic.linkedPresent()) castanetsNetworkVmVo.setLinked(nic.linked() ? "true" : "false");

						List<Statistic> statistics
								= getSysSrvHelper().findAllStatisticsFromVmNic(connection, vm.id(), nic.id());
						statistics.forEach(statistic -> {
							if (statistic.namePresent()) {
								List<Value> values = statistic.values();
								if (statistic.name().equalsIgnoreCase("data.current.rx.bps")) {
									values.forEach(value -> {
										castanetsNetworkVmVo.setDataCurrentRxBps(value.datum().divide(number, 1, 0));
									});
								}
								if (statistic.name().equalsIgnoreCase("data.current.tx.bps")) {
									values.forEach(value2 -> {
										castanetsNetworkVmVo.setDataCurrentTxBps(value2.datum().divide(number, 1, 0));
									});
								}
								if (statistic.name().equalsIgnoreCase("data.total.rx")) {
									values.forEach(value3 -> {
										castanetsNetworkVmVo.setDataTotalRx(value3.datum());
									});
								}
								if (statistic.name().equalsIgnoreCase("data.total.tx")) {
									values.forEach(value4 -> {
										castanetsNetworkVmVo.setDataTotalTx(value4.datum());
									});
								}
							}
						});
						networkVmVos.add(castanetsNetworkVmVo);
					}
				}
			});
		});
		return networkVmVos;
	}

	public ItInfoNetworkCreateVo getNetworkCreateResource() {
		log.info("... getNetworkCreateResource");
		Connection connection = adminConnectionService.getConnection();
		DataCenter dataCenter
				= getSysSrvHelper().findAllDataCenters(connection).get(0);
		List<Cluster> clusters
				= getSysSrvHelper().findAllClusters(connection, "");
		List<Network> networks
				= getSysSrvHelper().findAllNetworks(connection);
		List<Qos> qoss
				= getSysSrvHelper().findAllQossFromDataCenter(connection, dataCenter.id());

		ItInfoNetworkCreateVo castanetsNetworkCreateVo = new ItInfoNetworkCreateVo();
		List<ItInfoNetworkClusterVo> clusterList = new ArrayList<>();
		clusters.forEach(cluster -> {
			ItInfoNetworkClusterVo castanetsNetworkClusterVo = new ItInfoNetworkClusterVo();
			if (cluster.idPresent())		castanetsNetworkClusterVo.setClusterId(cluster.id());
			if (cluster.namePresent())		castanetsNetworkClusterVo.setClusterName(cluster.name());

			castanetsNetworkClusterVo.setConnect(true);
			castanetsNetworkClusterVo.setRequired(true);
			clusterList.add(castanetsNetworkClusterVo);
		});
		castanetsNetworkCreateVo.setClusters(clusterList);
		List<ItInfoNetworkQosVo> qosList = new ArrayList<>();
		qoss.forEach(qos -> {
			ItInfoNetworkQosVo castanetsNetworkQosVo = new ItInfoNetworkQosVo();
			if (qos.idPresent())	castanetsNetworkQosVo.setId(qos.id());
			if (qos.namePresent())	castanetsNetworkQosVo.setName(qos.name());
			qosList.add(castanetsNetworkQosVo);
		});

		if (!qosList.isEmpty())	castanetsNetworkCreateVo.setQoss(qosList);
		List<String> networkList = new ArrayList<>();
		networks.forEach(network -> {
			if (network.namePresent()) networkList.add(network.name());
		});
		if (!networkList.isEmpty()) castanetsNetworkCreateVo.setNetworkName(networkList);
		return castanetsNetworkCreateVo;
	}

	@Async("karajanTaskExecutor")
	@Override
	public void addLogicalNetwork(ItInfoNetworkVo castanetsNetworkVo) {
		log.info("... addLogicalNetwork");
		Connection connection = adminConnectionService.getConnection();
		Gson gson = new Gson();
		DataCenter dataCenter
				= getSysSrvHelper().findAllDataCenters(connection).get(0);
		List<String> nameServers = new ArrayList<>();
		if (!castanetsNetworkVo.getDnss().isEmpty()) {
			castanetsNetworkVo.getDnss().forEach(dnsIp -> {
				if (!"".equals(dnsIp.getDnsIp())) nameServers.add(dnsIp.getDnsIp());
			});
		}

		try {
			NetworkBuilder networkBuilder = new NetworkBuilder()
					.name(castanetsNetworkVo.getName())
					.description(castanetsNetworkVo.getDescription())
					.dataCenter(Builders.dataCenter().id(dataCenter.id()))
					.vlan(
							!castanetsNetworkVo.getVlan().isEmpty()
									? Builders.vlan().id(Integer.parseInt(castanetsNetworkVo.getVlan()))
									: null
					)
					.usages(
							castanetsNetworkVo.getUsage().equalsIgnoreCase("true")
									? NetworkUsage.VM
									: NetworkUsage.DEFAULT_ROUTE
					)
					.mtu(!castanetsNetworkVo.getMtu().isEmpty()
							? Integer.parseInt(castanetsNetworkVo.getMtu())
							: 1500
					)
					.dnsResolverConfiguration(Builders.dnsResolverConfiguration().nameServers(nameServers))
					.qos(!"".equals(castanetsNetworkVo.getQos())
							? Builders.qos().id(castanetsNetworkVo.getQos()).build()
							: Builders.qos().build()
					);

			Network network
					= getSysSrvHelper().addNetwork(connection, networkBuilder.build());

			castanetsNetworkVo.getClusters().forEach(clusterVo -> {
				if (clusterVo.getConnect()) {
					Network n2add
							= Builders.network().id(network.id()).required(clusterVo.getRequired()).build();
					String clusterId
							= getSysSrvHelper().addNetworkFromCluster(connection, clusterVo.getClusterId(), n2add).id();
				}
			});

			if (!castanetsNetworkVo.getLabel().isEmpty()) {
				NetworkLabel nl2add
						= Builders.networkLabel().id(castanetsNetworkVo.getLabel()).build();
				getSysSrvHelper().addNetworkLabelFromNetwork(connection, network.id(), nl2add);
			}
			Thread.sleep(2000L);
			MessageVo message
					= MessageVo.createMessage(MessageType.NETWORK_ADD, true, castanetsNetworkVo.getName(), "");
			this.websocketService.sendMessage("/topic/notify", gson.toJson(message));
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			try { Thread.sleep(2000L); } catch (InterruptedException interruptedException) {}
			MessageVo message
					= MessageVo.createMessage(MessageType.NETWORK_ADD, false, castanetsNetworkVo.getName(), "");
			websocketService.sendMessage("/topic/notify", gson.toJson(message));
		}
	}

	@Override
	public void deleteNetworks(List<ItInfoNetworkVo> itinfoNetworkVos) {
		log.info("... deleteNetworks[{}]", itinfoNetworkVos.size());
		Connection connection = adminConnectionService.getConnection();
		Gson gson = new Gson();
		try {
			getSysSrvHelper().findAllNetworks(connection).forEach(network -> {
				itinfoNetworkVos.forEach(itinfoNetworkVo -> {
					if (network.id().equalsIgnoreCase(itinfoNetworkVo.getId())) {
						if (network.status() != NetworkStatus.OPERATIONAL)
							getSysSrvHelper().removeNetwork(connection, itinfoNetworkVo.getId());
					}
				});
			});
			MessageVo message
					= MessageVo.createMessage(MessageType.NETWORK_REMOVE, true, itinfoNetworkVos.get(0).getName(), "");
			websocketService.sendMessage("/topic/notify", gson.toJson(message));
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			MessageVo message
					= MessageVo.createMessage(MessageType.NETWORK_REMOVE, false, itinfoNetworkVos.get(0).getName(), "");
			websocketService.sendMessage("/topic/notify", gson.toJson(message));
		}
	}

	@Async("karajanTaskExecutor")
	@Override
	public void updateNetwork(ItInfoNetworkVo castanetsNetworkVo) {
		log.info("... updateNetwork");
		Connection connection = adminConnectionService.getConnection();
		Gson gson = new Gson();
		DataCenter dataCenter
				= getSysSrvHelper().findAllDataCenters(connection).get(0);
		List<String> nameServers = new ArrayList<>();
		if (!castanetsNetworkVo.getDnss().isEmpty()) {
			castanetsNetworkVo.getDnss().forEach(dnsIp -> {
				if (!"".equals(dnsIp.getDnsIp())) nameServers.add(dnsIp.getDnsIp());
			});
		}
		try {
			Network n2Update = new NetworkBuilder()
					.name(castanetsNetworkVo.getName())
					.description(castanetsNetworkVo.getDescription())
					.dataCenter(Builders.dataCenter().id(dataCenter.id()))
					.vlan(
							(!castanetsNetworkVo.getVlan().isEmpty())
									? Builders.vlan().id(Integer.parseInt(castanetsNetworkVo.getVlan()))
									: null
					)
					.usages(
							"true".equalsIgnoreCase(castanetsNetworkVo.getUsage())
									? NetworkUsage.VM
									: NetworkUsage.DEFAULT_ROUTE
					)
					.dnsResolverConfiguration(Builders.dnsResolverConfiguration().nameServers(nameServers))
					.qos(!castanetsNetworkVo.getQos().isEmpty()
							? Builders.qos().id(castanetsNetworkVo.getQos()).build()
							: Builders.qos().build()
					).build();
			Network nUpdated
					= getSysSrvHelper().updateNetwork(connection, castanetsNetworkVo.getId(), n2Update);
			List<NetworkLabel> networkLabels
					= getSysSrvHelper().findAllNetworkLabelsFromNetwork(connection, castanetsNetworkVo.getId());
			networkLabels.forEach(networkLabel ->
					getSysSrvHelper().removeNetworkLabelFromNetwork(connection, castanetsNetworkVo.getId(), networkLabel.id())
			);

			if (!"".equals(castanetsNetworkVo.getLabel())) {
				NetworkLabel lbl2add
						= Builders.networkLabel().id(castanetsNetworkVo.getLabel()).build();
				getSysSrvHelper().addNetworkLabelFromNetwork(connection, castanetsNetworkVo.getId(), lbl2add);
			}

			List<ItInfoNetworkClusterVo> clusterVos
					= getNetworkCluster(null, castanetsNetworkVo.getId());

			clusterVos.forEach(clusterVo -> {
				if (clusterVo.getConnect())
					castanetsNetworkVo.getClusters().forEach(cluster -> {
						if (cluster.getClusterId().equalsIgnoreCase(clusterVo.getClusterId())) {
							if (!cluster.getConnect()) {
								getSysSrvHelper().removeNetworkFromCluster(connection, clusterVo.getClusterId(), castanetsNetworkVo.getId());
								return;
							}

							if (cluster.getRequired() && !clusterVo.getRequired()) {
								getSysSrvHelper().updateNetworkFromCluster(connection, clusterVo.getClusterId(), Builders.network().id(castanetsNetworkVo.getId()).required(true).build());
							} else if (clusterVo.getRequired()) {
								getSysSrvHelper().updateNetworkFromCluster(connection, clusterVo.getClusterId(), Builders.network().id(castanetsNetworkVo.getId()).required(false).build());
							}
						}
					});
				else
					castanetsNetworkVo.getClusters().forEach(cluster2 -> {
						if (cluster2.getClusterId().equalsIgnoreCase(clusterVo.getClusterId())) {
							if (cluster2.getConnect()) {
								String clusterId
										= getSysSrvHelper().addNetworkFromCluster(connection, clusterVo.getClusterId(), Builders.network().id(castanetsNetworkVo.getId()).required(true).build())
										.id();
								if (!cluster2.getRequired()) {
									getSysSrvHelper().updateNetworkFromCluster(connection, clusterId, Builders.network().id(castanetsNetworkVo.getId()).required(false).build());
								}
							}
						}
					});
			});
			try { Thread.sleep(2000L); } catch (InterruptedException e2) { log.error(e2.getLocalizedMessage());}
			MessageVo message
					= MessageVo.createMessage(MessageType.NETWORK_UPDATE, true, castanetsNetworkVo.getName(), "");
			websocketService.sendMessage("/topic/notify", gson.toJson(message));
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			try { Thread.sleep(2000L); } catch (InterruptedException e2) { log.error(e2.getLocalizedMessage());}
			MessageVo message
					= MessageVo.createMessage(MessageType.NETWORK_UPDATE, false, castanetsNetworkVo.getName(), "");
			websocketService.sendMessage("/topic/notify", gson.toJson(message));
		}
	}
}
