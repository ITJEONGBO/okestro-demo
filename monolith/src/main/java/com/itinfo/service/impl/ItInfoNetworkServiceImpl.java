package com.itinfo.service.impl;

import com.itinfo.SystemServiceHelper;
import com.itinfo.model.*;
import com.itinfo.service.ItInfoNetworkService;
import com.itinfo.service.engine.AdminConnectionService;
import com.itinfo.service.engine.WebsocketService;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.builders.Builders;
import org.ovirt.engine.sdk4.builders.NetworkBuilder;
import org.ovirt.engine.sdk4.types.Cluster;
import org.ovirt.engine.sdk4.types.DataCenter;
import org.ovirt.engine.sdk4.types.Host;
import org.ovirt.engine.sdk4.types.HostNic;
import org.ovirt.engine.sdk4.types.Network;
import org.ovirt.engine.sdk4.types.NetworkLabel;
import org.ovirt.engine.sdk4.types.NetworkUsage;
import org.ovirt.engine.sdk4.types.Nic;
import org.ovirt.engine.sdk4.types.OpenStackNetworkProvider;
import org.ovirt.engine.sdk4.types.Qos;
import org.ovirt.engine.sdk4.types.Vm;
import org.ovirt.engine.sdk4.types.VnicProfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@NoArgsConstructor
public class ItInfoNetworkServiceImpl implements ItInfoNetworkService {
	@Autowired private AdminConnectionService adminConnectionService;
	@Autowired	private WebsocketService websocketService;

	@Override
	public List<ItInfoNetworkVo> getNetworkList() {
		Connection connection = adminConnectionService.getConnection();
		List<Network> networkList
				= SystemServiceHelper.getInstance().findAllNetworks(connection);
		List<ItInfoNetworkVo> list
				= ModelsKt.toItInfoNetworkVos(networkList, connection);
		return list;
	}


	@Override
	public List<ItInfoNetworkVo> getHostNetworkList(String id) {
		Connection connection = adminConnectionService.getConnection();

		List<HostNic> hostNics
				= SystemServiceHelper.getInstance().findNicsFromHost(connection, id);
		List<Vm> vms
				= SystemServiceHelper.getInstance().findAllVms(connection, "");
		DataCenter dataCenter
				= SystemServiceHelper.getInstance().findAllDataCenters(connection).get(0);
		List<Cluster> clusters
				= SystemServiceHelper.getInstance().findAllClusters(connection, "");
		List<Network> networkList
				= SystemServiceHelper.getInstance().findAllNetworks(connection);
		List<OpenStackNetworkProvider> openStackNetworkProviders
				= SystemServiceHelper.getInstance().findAllOpenStackNetworkProviders(connection);
		List<Qos> qoss
				= SystemServiceHelper.getInstance().findAllQossFromDataCenter(connection, dataCenter.id());

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
						= SystemServiceHelper.getInstance().findAllNetworksFromCluster(connection, cluster.id());;
				for (Network clusterNetwork : networkss) {
					ItInfoNetworkClusterVo castanetsNetworkClusterVo = new ItInfoNetworkClusterVo();
					List<ItInfoNetworkUsagesVo> usages = new ArrayList<>();
					if (clusterNetwork.id().equalsIgnoreCase(network.id()) && clusterNetwork.required())
						networkVo.setRequired(clusterNetwork.required());
					clusterNetwork.usages().forEach(cn -> {

					});
					// ItInfoNetworkUsagesVo usageVo = new ItInfoNetworkUsagesVo();
					castanetsNetworkClusterVo.setUsages(usages);
					castanetsNetworkClusterVo.setStatus(clusterNetwork.status().value());
					itInfoNetworkClusterVos.add(castanetsNetworkClusterVo);
				}
			}
			List<VnicProfile> vnicProfiles
					= SystemServiceHelper.getInstance().findAllVnicProfilesFromNetwork(connection, network.id());
			List<String> vnicIds = new ArrayList<>();
			for (VnicProfile vnic : vnicProfiles)
				vnicIds.add(vnic.id());
			for (Vm vm : vms) {
				List<Nic> nics
						= SystemServiceHelper.getInstance().findNicsFromVm(connection, vm.id());
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
		if (networkList != null && networkList.size() != 0)
			itInfoNetworkClusterVoList = itInfoNetworkClusterVos.subList(0, networkList.size());
		for (int j = 0; j < list.size(); j++) {
			list.get(j).setUsages((itInfoNetworkClusterVoList.get(j)).getUsages());
			list.get(j).setStatus((itInfoNetworkClusterVoList.get(j)).getStatus());
		}
		return list;
	}

	public ItInfoNetworkGroupVo getNetworkDetail(ItInfoNetworkVo castanetsNetworkVo) {
		Connection connection = adminConnectionService.getConnection();
		List<Cluster> clusters
				= SystemServiceHelper.getInstance().findAllClusters(connection, "");
		List<ItInfoNetworkClusterVo> castanetsNetworkClusterVos
				= ModelsKt.toItInfoNetworkClusterVos(clusters, connection, castanetsNetworkVo.getId());

		List<ItInfoNetworkHostVo> CastanetsNetworkHostVos
				= getNetworkHost(castanetsNetworkVo.getId());
		castanetsNetworkVo
				= getNetwork(castanetsNetworkVo.getId());

		List<ItInfoNetworkVmVo> castanetsNetworkVmVos
				= getNetworkVm(castanetsNetworkVo.getId());
		return new ItInfoNetworkGroupVo(
				castanetsNetworkVo,
				castanetsNetworkClusterVos,
				CastanetsNetworkHostVos,
				castanetsNetworkVmVos
		);
	}

	public List<ItInfoNetworkClusterVo> getNetworkCluster(String clusterId, String networkId) {
		Connection connection = adminConnectionService.getConnection();
		List<Cluster> clusters
				= SystemServiceHelper.getInstance().findAllClusters(connection, "");
		List<ItInfoNetworkClusterVo> list
				= ModelsKt.toItInfoNetworkClusterVos(clusters, connection, networkId);
		return list;
	}

	public ItInfoNetworkVo getNetwork(String networkId) {
		Connection connection = adminConnectionService.getConnection();
		List<Network> networkList
				= SystemServiceHelper.getInstance().findAllNetworks(connection);
		DataCenter dataCenter
				= SystemServiceHelper.getInstance().findAllDataCenters(connection).get(0);
		List<OpenStackNetworkProvider> openStackNetworkProviders
				= SystemServiceHelper.getInstance().findAllOpenStackNetworkProviders(connection);
		List<Qos> qoss
				= SystemServiceHelper.getInstance().findAllQossFromDataCenter(connection, dataCenter.id());

		Network n = networkList.stream()
					.filter((Network network) -> network.id().equalsIgnoreCase(networkId))
					.collect(Collectors.toList()).get(0);

		return ModelsKt.toItInfoNetworkVo(n, connection);
	}

	public List<ItInfoNetworkHostVo> getNetworkHost(String networkId) {
		Connection connection = adminConnectionService.getConnection();
		List<Host> hosts
				= SystemServiceHelper.getInstance().findAllHosts(connection, "");
		return ModelsKt.toItInfoNetworkHostVos(hosts, connection);
	}

	public List<ItInfoNetworkVmVo> getNetworkVm(String networkId) {
		Connection connection = adminConnectionService.getConnection();
		List<ItInfoNetworkVmVo> networkVmVos = new ArrayList<>();
		List<Vm> vms
				= SystemServiceHelper.getInstance().findAllVms(connection, "");
		List<Cluster> clusters
				= SystemServiceHelper.getInstance().findAllClusters(connection, "");
		List<VnicProfile> vnicProfiles
				= SystemServiceHelper.getInstance().findAllVnicProfilesFromNetwork(connection, networkId);

		List<String> vnicIds = new ArrayList<>();
		for (VnicProfile vnic : vnicProfiles)
			vnicIds.add(vnic.id());
		vms.forEach(vm -> {
			List<Nic> nics
					= SystemServiceHelper.getInstance().findNicsFromVm(connection, vm.id());
			nics.forEach(nic -> {

			});
		});
		return networkVmVos;
	}

	public ItInfoNetworkCreateVo getNetworkCreateResource() {
		Connection connection = adminConnectionService.getConnection();
		DataCenter dataCenter
				= SystemServiceHelper.getInstance().findAllDataCenters(connection).get(0);
		List<Cluster> clusters
				= SystemServiceHelper.getInstance().findAllClusters(connection, "");
		List<Network> networks
				= SystemServiceHelper.getInstance().findAllNetworks(connection);
		List<Qos> qoss
				= SystemServiceHelper.getInstance().findAllQossFromDataCenter(connection, dataCenter.id());

		ItInfoNetworkCreateVo castanetsNetworkCreateVo = new ItInfoNetworkCreateVo();
		List<ItInfoNetworkClusterVo> clusterList = new ArrayList<>();
		clusters.forEach(cluster -> {
			ItInfoNetworkClusterVo castanetsNetworkClusterVo = new ItInfoNetworkClusterVo();
			if (cluster.idPresent())		castanetsNetworkClusterVo.setClusterId(cluster.id());
			if (cluster.namePresent())		castanetsNetworkClusterVo.setClusterName(cluster.name());
			if (castanetsNetworkClusterVo != null) {
				castanetsNetworkClusterVo.setConnect(true);
				castanetsNetworkClusterVo.setRequired(true);
				clusterList.add(castanetsNetworkClusterVo);
			}
		});
		castanetsNetworkCreateVo.setClusters(clusterList);
		List<ItInfoNetworkQosVo> qosList = new ArrayList<>();
		qoss.forEach(qos -> {
			ItInfoNetworkQosVo castanetsNetworkQosVo = new ItInfoNetworkQosVo();
			if (qos.idPresent())	castanetsNetworkQosVo.setId(qos.id());
			if (qos.namePresent())	castanetsNetworkQosVo.setName(qos.name());
			qosList.add(castanetsNetworkQosVo);
		});

		if (qosList != null)	castanetsNetworkCreateVo.setQoss(qosList);
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
		Connection connection = adminConnectionService.getConnection();
		Gson gson = new Gson();
		DataCenter dataCenter
				= SystemServiceHelper.getInstance().findAllDataCenters(connection).get(0);
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
							(castanetsNetworkVo.getVlan() != null && !"".equals(castanetsNetworkVo.getVlan()))
									? Builders.vlan().id(Integer.parseInt(castanetsNetworkVo.getVlan()))
									: null
					)
					.usages(
							castanetsNetworkVo.getUsage().equalsIgnoreCase("true")
									? NetworkUsage.VM
									: NetworkUsage.DEFAULT_ROUTE
					)
					.mtu(!"".equals(castanetsNetworkVo.getMtu())
							? Integer.parseInt(castanetsNetworkVo.getMtu())
							: 1500
					)
					.dnsResolverConfiguration(Builders.dnsResolverConfiguration().nameServers(nameServers))
					.qos(!"".equals(castanetsNetworkVo.getQos())
							? Builders.qos().id(castanetsNetworkVo.getQos()).build()
							: Builders.qos().build()
					);

			Network network
					= SystemServiceHelper.getInstance().addNetwork(connection, networkBuilder.build());

			castanetsNetworkVo.getClusters().forEach(clusterVo -> {
				if (clusterVo.getConnect()) {
					Network n2add
							= Builders.network().id(network.id()).required(clusterVo.getRequired()).build();
					String clusterId
							= SystemServiceHelper.getInstance().addNetworkFromCluster(connection, clusterVo.getClusterId(), n2add).id();
				}
			});

			if (!"".equals(castanetsNetworkVo.getLabel())) {
				NetworkLabel nl2add
						= Builders.networkLabel().id(castanetsNetworkVo.getLabel()).build();
				SystemServiceHelper.getInstance().addNetworkLabelFromNetwork(connection, network.id(), nl2add);
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
		Connection connection = adminConnectionService.getConnection();
		Gson gson = new Gson();
		try {
			SystemServiceHelper.getInstance().findAllNetworks(connection).forEach(network ->
				itinfoNetworkVos.forEach(itinfoNetworkVo -> {

				})
			);
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
	public void updateNetwork(ItInfoNetworkVo castanetsNetworkVo) {
		Connection connection = adminConnectionService.getConnection();
		Gson gson = new Gson();
		DataCenter dataCenter
				= SystemServiceHelper.getInstance().findAllDataCenters(connection).get(0);
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
							(castanetsNetworkVo.getVlan() != null && !"".equals(castanetsNetworkVo.getVlan()))
									? Builders.vlan().id(Integer.parseInt(castanetsNetworkVo.getVlan()))
									: null
					)
					.usages(
							castanetsNetworkVo.getUsage().equalsIgnoreCase("true")
									? NetworkUsage.VM
									: NetworkUsage.DEFAULT_ROUTE
					)
					.dnsResolverConfiguration(Builders.dnsResolverConfiguration().nameServers(nameServers))
					.qos(!"".equals(castanetsNetworkVo.getQos())
							? Builders.qos().id(castanetsNetworkVo.getQos()).build()
							: Builders.qos().build()
					).build();
			Network nUpdated
					= SystemServiceHelper.getInstance().updateNetwork(connection, castanetsNetworkVo.getId(), n2Update);
			List<NetworkLabel> networkLabels
					= SystemServiceHelper.getInstance().findAllNetworkLabelsFromNetwork(connection, castanetsNetworkVo.getId());
			networkLabels.forEach(networkLabel ->
					SystemServiceHelper.getInstance().removeNetworkLabelFromNetwork(connection, castanetsNetworkVo.getId(), networkLabel.id())
			);

			if (!"".equals(castanetsNetworkVo.getLabel())) {
				NetworkLabel lbl2add
						= Builders.networkLabel().id(castanetsNetworkVo.getLabel()).build();
				SystemServiceHelper.getInstance().addNetworkLabelFromNetwork(connection, castanetsNetworkVo.getId(), lbl2add);
			}

			List<ItInfoNetworkClusterVo> clusterVos
					= getNetworkCluster(null, castanetsNetworkVo.getId());

			clusterVos.forEach(clusterVo -> {
				if (clusterVo.getConnect())
					castanetsNetworkVo.getClusters().forEach(cluster -> {});
				else
					castanetsNetworkVo.getClusters().forEach(cluster -> {});
			});
			Thread.sleep(2000L);
			MessageVo message
					= MessageVo.createMessage(MessageType.NETWORK_UPDATE, true, castanetsNetworkVo.getName(), "");
			websocketService.sendMessage("/topic/notify", gson.toJson(message));
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException interruptedException) {}
			MessageVo message
					= MessageVo.createMessage(MessageType.NETWORK_UPDATE, false, castanetsNetworkVo.getName(), "");
			websocketService.sendMessage("/topic/notify", gson.toJson(message));
		}
	}
}
