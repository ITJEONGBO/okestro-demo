package com.itinfo.service.impl;

import com.google.gson.Gson;
import com.itinfo.dao.ClustersDao;

import com.itinfo.model.*;
import com.itinfo.model.karajan.ConsolidationVo;
import com.itinfo.model.karajan.HostVo;
import com.itinfo.model.karajan.KarajanVo;
import com.itinfo.service.HostsService;
import com.itinfo.service.consolidation.Ffd;
import com.itinfo.service.engine.AdminConnectionService;
import com.itinfo.service.engine.KarajanService;
import com.itinfo.service.engine.WebsocketService;

import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.builders.*;
import org.ovirt.engine.sdk4.services.HostNicsService;
import org.ovirt.engine.sdk4.services.HostService;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Service
@Slf4j
public class HostsServiceImpl extends BaseService implements HostsService {
	@Autowired private AdminConnectionService adminConnectionService;
	@Autowired private KarajanService karajanService;
	@Autowired private Ffd ffd;
	@Autowired private ClustersDao clustersDao;
	@Autowired private WebsocketService websocketService;


	@Override
	public List<ConsolidationVo> maintenanceBeforeConsolidateVms(List<String> hosts) {
		Connection connection = this.adminConnectionService.getConnection();
		List<ConsolidationVo> consolidationLIst = new ArrayList<>();
		try {
			for (String id : hosts) {
				Host host = getSysSrvHelper().findHost(connection, id);
				KarajanVo karajan
						= this.karajanService.getDataCenter();
				consolidationLIst = this.ffd.reassignVirtualMachine(karajan, host.cluster().id(), host.id());
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return consolidationLIst;
	}

	@Async("karajanTaskExecutor")
	@Override
	public void maintenanceStart(List<String> hosts) {
		Connection connection = this.adminConnectionService.getConnection();
		MessageVo message = new MessageVo();
		message.setTitle("호스트 유지보수 모드 시작");
		for (String id : hosts) {
			HostService hostService =
					getSysSrvHelper().srvHost(connection, id);
			Host host = (hostService.get().send()).host();
			try {
				Host item;
				if (host.status() != HostStatus.MAINTENANCE)
					hostService.deactivate().send();
				do {
					Thread.sleep(5000L);
					item = (hostService.get().send()).host();
				} while (item.status() != HostStatus.MAINTENANCE);
				message.setText("호스트 유지보수 모드 완료("+ host.name() + ")");
				message.setStyle("success");
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getLocalizedMessage());
				message.setText("호스트 유지보수 모드 실패("+ host.name() + ")");
				message.setStyle("error");
			}
			this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
			this.websocketService.sendMessage("/topic/hosts/reload", "");
		}
	}

	@Async("karajanTaskExecutor")
	@Override
	public void maintenanceStop(List<String> hosts) {
		Connection connection = this.adminConnectionService.getConnection();
		MessageVo message = new MessageVo();
		message.setTitle("호스트 활성 모드 시작");
		for (String id : hosts) {
			HostService hostService =
					getSysSrvHelper().srvHost(connection, id);
			Host host = (hostService.get().send()).host();
			try {
				Host item;
				hostService.activate().send();
				do {
					Thread.sleep(5000L);
					item = (hostService.get().send()).host();
				} while (item.status() != HostStatus.UP);
				message.setText("호스트 활성 모드 완료(" + host.name() + ")");
				message.setStyle("success");
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getLocalizedMessage());
				message.setText("호스트 활성 모드 실패(" + host.name() + ")");
				message.setText("+ host.name() + ");
				message.setStyle("error");
			}
			this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
			this.websocketService.sendMessage("/topic/hosts/reload", "");
		}
	}

	@Async("karajanTaskExecutor")
	@Override
	public void restartHost(List<String> hosts) {
		Connection connection = this.adminConnectionService.getConnection();
		MessageVo message = new MessageVo();
		message.setTitle("호스트 재시작");
		for (String id : hosts) {
			HostService hostService =
					getSysSrvHelper().srvHost(connection, id);
			Host host = (hostService.get().send()).host();
			try {
				Host item;
				(hostService.fence().fenceType(FenceType.RESTART.name()).send()).powerManagement();
				do {
					Thread.sleep(5000L);
					item = (hostService.get().send()).host();
				} while (item.status() != HostStatus.UP);
				message.setText("호스트 재시작 완료(" + host.name() + ")");
				message.setStyle("success");
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getLocalizedMessage());
				message.setText("호스트 재시작 실패("+ host.name() + ")");
				message.setStyle("error");
			}
			this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
			this.websocketService.sendMessage("/topic/hosts/reload", "");
		}
	}

	@Async("karajanTaskExecutor")
	@Override
	public void startHost(List<String> hosts) {
		Connection connection = this.adminConnectionService.getConnection();
		MessageVo message = new MessageVo();
		message.setTitle("호스트 시작");
		for (String id : hosts) {
			HostService hostService =
					getSysSrvHelper().srvHost(connection, id);
			Host host = (hostService.get().send()).host();
			try {
				Host item;
				(hostService.fence().fenceType(FenceType.START.name()).send()).powerManagement();
				do {
					Thread.sleep(5000L);
					item = (hostService.get().send()).host();
				} while (item.status() != HostStatus.UP);
				message.setText("호스트 시작 완료(" + host.name() + ")");
				message.setStyle("success");
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getLocalizedMessage());
				message.setText("호스트 시작 실패("+ host.name() + ")");
				message.setStyle("error");
			}
			this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
			this.websocketService.sendMessage("/topic/hosts/reload", "");
		}
	}

	@Async("karajanTaskExecutor")
	@Override
	public void stopHost(List<String> hosts) {
		Connection connection = this.adminConnectionService.getConnection();
		MessageVo message = new MessageVo();
		message.setTitle("호스트 정지");
		for (String id : hosts) {
			HostService hostService =
					getSysSrvHelper().srvHost(connection, id);
			Host host = (hostService.get().send()).host();
			try {
				Host item;
				(hostService.fence().fenceType(FenceType.STOP.name()).send()).powerManagement();
				do {
					Thread.sleep(5000L);
					item = (hostService.get().send()).host();
				} while (item.status() != HostStatus.DOWN);
				message.setText("호스트 정지 완료(" + host.name() + ")");
				message.setStyle("success");
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getLocalizedMessage());
				message.setText("호스트 정지 실패(" + host.name() + ")");
				message.setStyle("error");
			}
			this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
			this.websocketService.sendMessage("/topic/hosts/reload", "");
		}
	}

	@Async("karajanTaskExecutor")
	@Override
	public void createHost(HostCreateVo hostCreateVo) {
		Connection connection = this.adminConnectionService.getConnection();
		Cluster cluster =
				getSysSrvHelper().findCluster(connection, hostCreateVo.getClusterId());
		HostBuilder hostBuilder = new HostBuilder();
		if (hostCreateVo.getSsh() != null && hostCreateVo.getSsh().getPort() == 22) {
			hostBuilder
					.name(hostCreateVo.getName())
					.comment(hostCreateVo.getComment())
					.description(hostCreateVo.getDescription())
					.address(hostCreateVo.getSsh().getAddress())
					.rootPassword(hostCreateVo.getSsh().getPassword())
					.cluster((new ClusterBuilder()).name(cluster.name()));
		} else {
			hostBuilder
					.name(hostCreateVo.getName())
					.comment(hostCreateVo.getComment())
					.description(hostCreateVo.getDescription())
					.address(hostCreateVo.getSsh().getAddress())
					.port(54321)
					.ssh((new SshBuilder()).port(hostCreateVo.getSsh().getPort()))
					.rootPassword(hostCreateVo.getSsh().getPassword())
					.cluster((new ClusterBuilder()).name(cluster.name()));
		}
		MessageVo message = new MessageVo();
		message.setTitle("호스트 추가");
		try {
			Host host = (hostCreateVo.getHostEngineEnabled())
					? (getSysSrvHelper().srvHosts(connection).add().deployHostedEngine(true).host(hostBuilder).send()).host()
					: (getSysSrvHelper().srvHosts(connection).add().host(hostBuilder).send()).host();

			HostService hostService = getSysSrvHelper().srvHost(connection, host.id());
			do {
				Thread.sleep(2000L);
				host = (hostService.get().send()).host();
			} while (host.status() != HostStatus.UP && host.status() != HostStatus.INSTALLING);

			if (hostCreateVo.getPowerManagementEnabled() && (
					hostCreateVo.getFenceAgent() != null &&	!"".equals(hostCreateVo.getFenceAgent().getAddress()))) {
				AgentBuilder agentBuilder = new AgentBuilder();
				agentBuilder.address(hostCreateVo.getFenceAgent().getAddress())
						.username(hostCreateVo.getFenceAgent().getUsername())
						.password(hostCreateVo.getFenceAgent().getPassword())
						.type(hostCreateVo.getFenceAgent().getType())
						.order(1)
						.encryptOptions(false);
				Agent agent = getSysSrvHelper().addFenceAgent(connection, host.id(), agentBuilder.build());
				hostBuilder.powerManagement((new PowerManagementBuilder())
						.enabled(true)
						.kdumpDetection(true)
						.agents(agent));
			}
			if (host.status() == HostStatus.UP)
				try {
					(hostService.update().host(hostBuilder).send()).host();
					message.setText("호스트 추가 완료("+ hostCreateVo.getName() + ")");
					message.setStyle("success");
				} catch (Exception e) {
					log.error(e.getLocalizedMessage());
					message.setText("호스트 추가 완료, 전원관리 추가 실패 ("+ hostCreateVo.getName() + ")");
					message.setStyle("warning");
				}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			message.setText("호스트 추가 실패("+ hostCreateVo.getName() + ")");
			message.setStyle("error");
		}
		this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
		this.websocketService.sendMessage("/topic/hosts/reload", "");
	}

	@Async("karajanTaskExecutor")
	@Override
	public void updateHost(HostCreateVo hostCreateVo) {
		Connection connection = this.adminConnectionService.getConnection();
		HostService hostService
				= getSysSrvHelper().srvHost(connection, hostCreateVo.getId());
		HostCreateVo srcHostCreateVo = retrieveCreateHostInfo(hostCreateVo.getId());
		HostBuilder hostBuilder = new HostBuilder();
		if (!"".equals(hostCreateVo.getName()) && !srcHostCreateVo.getName().equals(hostCreateVo.getName()))
			hostBuilder.name(hostCreateVo.getName());
		hostBuilder.comment(hostCreateVo.getComment());
		if (hostCreateVo.getSsh() != null && srcHostCreateVo.getSsh().getPort() != hostCreateVo.getSsh().getPort())
			hostBuilder.port(54321)
					.ssh((new SshBuilder()).port(hostCreateVo.getSsh().getPort()));
		if (!srcHostCreateVo.getClusterId().equals(hostCreateVo.getClusterId())) {
			Cluster cluster = getSysSrvHelper().findCluster(connection, hostCreateVo.getClusterId());
			hostBuilder.cluster((new ClusterBuilder()).name(cluster.name()));
		}
		if (hostCreateVo.getFenceAgent() != null)
			if ((!srcHostCreateVo.getPowerManagementEnabled() && hostCreateVo.getPowerManagementEnabled() && hostCreateVo.getFenceAgent().getAddress() != null) || "".equals(hostCreateVo.getFenceAgent().getAddress())) {
				AgentBuilder agentBuilder = new AgentBuilder();
				agentBuilder.address(hostCreateVo.getFenceAgent().getAddress())
						.username(hostCreateVo.getFenceAgent().getUsername())
						.password(hostCreateVo.getFenceAgent().getPassword())
						.type(hostCreateVo.getFenceAgent().getType())
						.order(1)
						.encryptOptions(false);
				Agent agent = getSysSrvHelper().addFenceAgent(connection, hostCreateVo.getId(), agentBuilder.build());
				hostBuilder.powerManagement((new PowerManagementBuilder())
						.enabled(true)
						.kdumpDetection(true)
						.agents(agent));
			} else if (srcHostCreateVo.getPowerManagementEnabled() && !hostCreateVo.getPowerManagementEnabled()) {
				hostBuilder.powerManagement((new PowerManagementBuilder())
						.enabled(false)
						.kdumpDetection(true));
			}
		MessageVo message = new MessageVo();
		message.setTitle("호스트 수정");
		try {
			(hostService.update().host(hostBuilder).send()).host();
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException e) {
				log.error(e.getLocalizedMessage());
				e.printStackTrace();
			}
			message.setText("호스트 수정 완료("+ hostCreateVo.getName() + ")");
			message.setStyle("success");
		} catch (Exception e) {
			e.printStackTrace();
			message.setText("호스트 수정 실패("+ hostCreateVo.getName() + ")");
			message.setStyle("error");
		}
		this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
		this.websocketService.sendMessage("/topic/hosts/reload", "");
	}


	@Async("karajanTaskExecutor")
	@Override
	public void removeHost(List<String> hosts) {
		Connection connection = this.adminConnectionService.getConnection();

		HostService hostService
				= getSysSrvHelper().srvHost(connection, hosts.get(0));
		Host host
				= getSysSrvHelper().findHost(connection, hosts.get(0));
		MessageVo message = new MessageVo();
		message.setTitle("호스트 삭제");
		try {
			hostService.remove().send();
			message.setText("호스트 삭제 완료("+ host.name() + ")");
			message.setStyle("success");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
			message.setText("호스트 삭제 실패("+ host.name() + ")");
			message.setStyle("error");
		}
		try { Thread.sleep(2000L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
		this.websocketService.sendMessage("/topic/hosts/reload", "");
	}

	@Override
	public void setupHostNetwork(List<NicUsageApiVo> nicUsageApiVoList) {
		Connection connection = this.adminConnectionService.getConnection();
		String hostId = "";
		Iterator<NicUsageApiVo> it = nicUsageApiVoList.iterator();
		if (it.hasNext()) {
			NicUsageApiVo findHostId = it.next();
			hostId = findHostId.getHostId();
		}
		if ("".equals(hostId)) {
			MessageVo message = new MessageVo();
			message.setTitle("호스트 네트워크 수정");
			message.setText("호스트 네트워크 수정 실패(no such hostId Found)");
			message.setStyle("error");
			log.error("호스트 네트워크 수정 실패(no such hostId Found)");
			return;
		}

		Host host
				= getSysSrvHelper().findHost(connection, hostId);
		HostService hostService
				= getSysSrvHelper().srvHost(connection, host.id());
		HostNicsService hostNicsService = hostService.nicsService();
		List<HostNic> hostNics = hostNicsService.list().send().nics();
		String networkAttachmentId = null;
		MessageVo message = new MessageVo();
		message.setTitle("호스트 네트워크 수정");
		for (NicUsageApiVo nicUsageApiVo : nicUsageApiVoList) {
			if ("".equals(nicUsageApiVo.getBonding()) && "".equals(nicUsageApiVo.getId())) {
				if (!"".equals(nicUsageApiVo.getNetworkId())) {
					Iterator<HostNic> it2 = hostNics.iterator();
					while (true) {
						if (!it2.hasNext()) {
							break;
						} else if (hostNics.indexOf(it2.next()) == hostNics.size() - 1) {
							if (nicUsageApiVo.getNicExNetExist()) {
								if (nicUsageApiVo.getVlanNetworkList().size() > 0 && !nicUsageApiVo.getNetworkId().equals("")) {
									try {
										List<HostNicBuilder> slaves = new ArrayList<>();
										List<NetworkBuilder> networkArray = new ArrayList<>();
										List<NetworkAttachment> modifiedNetworkAttachments = new ArrayList<>();
										for (NicUsageApiVo bonding : nicUsageApiVo.getBonding()) {
											HostNicBuilder hostNic = Builders.hostNic();
											hostNic.name(bonding.getName());
											slaves.add(hostNic);
										}
										Iterator<NetworkAttachment> it3
												= hostService.networkAttachmentsService().list().send().attachments().iterator();
										while (true) {
											if (!it3.hasNext()) {
												break;
											}
											NetworkAttachment networkAttachment = it3.next();
											if (networkAttachment.network().id().equals(nicUsageApiVo.getNetworkId())) {
												networkAttachmentId = networkAttachment.id();
												break;
											}
										}
										for (String vlan : nicUsageApiVo.getVlanNetworkList()) {
											NetworkBuilder networkBuilder = new NetworkBuilder();
											networkBuilder.id(vlan);
											networkArray.add(networkBuilder);
										}
										for (NetworkAttachment networkAttachment2 : hostService.networkAttachmentsService().list().send().attachments()) {
											Iterator<String> it4
													= nicUsageApiVo.getVlanNetworkList().iterator();
											while (true) {
												if (it4.hasNext()) {
													String vlan2 = (String) it4.next();
													if (networkAttachment2.network().id().equals(vlan2)) {
														modifiedNetworkAttachments.add(networkAttachment2);
														break;
													}
												}
											}
										}
										for (NetworkAttachment netattach : modifiedNetworkAttachments) {
											hostService.networkAttachmentsService().attachmentService(netattach.id()).remove().send();
										}
										hostService.setupNetworks().modifiedBonds(Builders.hostNic().name(nicUsageApiVo.getName()).bonding(Builders.bonding().options(new OptionBuilder[]{Builders.option().name("mode").value(nicUsageApiVo.getBondingMode()), Builders.option().name("miimon").value("100")}).slaves((HostNicBuilder[]) slaves.toArray(new HostNicBuilder[0])))).modifiedNetworkAttachments(new NetworkAttachmentBuilder[]{Builders.networkAttachment().id(networkAttachmentId).hostNic(Builders.hostNic().name(nicUsageApiVo.getName()))}).send();
										for (NetworkBuilder network : networkArray) {
											hostService.setupNetworks().modifiedBonds(Builders.hostNic().name(nicUsageApiVo.getName()).bonding(Builders.bonding().options(new OptionBuilder[]{Builders.option().name("mode").value(nicUsageApiVo.getBondingMode()), Builders.option().name("miimon").value("100")}).slaves((HostNicBuilder[]) slaves.toArray(new HostNicBuilder[0])))).modifiedNetworkAttachments(new NetworkAttachmentBuilder[]{Builders.networkAttachment().network(network).hostNic(Builders.hostNic().name(nicUsageApiVo.getName()))}).send();
										}
										hostService.commitNetConfig().send();
										message.setText("호스트 네트워크 수정 완료(" + nicUsageApiVo.getName() + ")");
										message.setStyle("success");
									} catch (Exception e) {
										log.error(e.getLocalizedMessage());
										e.printStackTrace();
										message.setText("호스트 네트워크 수정 실패(" + nicUsageApiVo.getName() + ")");
										message.setStyle("error");
									}
								} else if (nicUsageApiVo.getVlanNetworkList().size() == 0) {
									try {
										List<HostNicBuilder> slaves2 = new ArrayList<>();
										for (NicUsageApiVo bonding2 : nicUsageApiVo.getBonding()) {
											HostNicBuilder hostNic2 = Builders.hostNic();
											hostNic2.name(bonding2.getName());
											slaves2.add(hostNic2);
										}
										Iterator<NetworkAttachment> it5
												= hostService.networkAttachmentsService().list().send().attachments().iterator();
										while (true) {
											if (!it5.hasNext()) {
												break;
											}
											NetworkAttachment networkAttachment3 = (NetworkAttachment) it5.next();
											if (networkAttachment3.network().id().equals(nicUsageApiVo.getNetworkId())) {
												networkAttachmentId = networkAttachment3.id();
												break;
											}
										}
										hostService.setupNetworks().modifiedBonds(Builders.hostNic().name(nicUsageApiVo.getName()).bonding(Builders.bonding().options(new OptionBuilder[]{Builders.option().name("mode").value(nicUsageApiVo.getBondingMode()), Builders.option().name("miimon").value("100")}).slaves((HostNicBuilder[]) slaves2.toArray(new HostNicBuilder[0])))).modifiedNetworkAttachments(new NetworkAttachmentBuilder[]{Builders.networkAttachment().id(networkAttachmentId).hostNic(Builders.hostNic().name(nicUsageApiVo.getName()))}).send();
										hostService.commitNetConfig().send();
										message.setText("호스트 네트워크 수정 완료(" + nicUsageApiVo.getName() + ")");
										message.setStyle("success");
									} catch (Exception e2) {
										log.error(e2.getLocalizedMessage());
										e2.printStackTrace();
										message.setText("호스트 네트워크 수정 실패(" + nicUsageApiVo.getName() + ")");
										message.setStyle("error");
									}
								} else if (!nicUsageApiVo.getVlanNetworkList().isEmpty() && "".equals(nicUsageApiVo.getNetworkId())) {
									try {
										List<HostNicBuilder> slaves3 = new ArrayList<>();
										for (NicUsageApiVo bonding3 : nicUsageApiVo.getBonding()) {
											HostNicBuilder hostNic3 = Builders.hostNic();
											hostNic3.name(bonding3.getName());
											slaves3.add(hostNic3);
										}
										List<NetworkBuilder> networkArray2 = new ArrayList<>();
										for (String vlan3 : nicUsageApiVo.getVlanNetworkList()) {
											NetworkBuilder networkBuilder2 = new NetworkBuilder();
											networkBuilder2.id(vlan3);
											networkArray2.add(networkBuilder2);
										}
										List<NetworkAttachment> modifiedNetworkAttachments2 = new ArrayList<>();
										for (NetworkAttachment networkAttachment4 : hostService.networkAttachmentsService().list().send().attachments()) {
											Iterator it6 = nicUsageApiVo.getVlanNetworkList().iterator();
											while (true) {
												if (it6.hasNext()) {
													String vlan4 = (String) it6.next();
													if (networkAttachment4.network().id().equals(vlan4)) {
														modifiedNetworkAttachments2.add(networkAttachment4);
														break;
													}
												}
											}
										}
										for (NetworkAttachment netattach2 : modifiedNetworkAttachments2) {
											hostService.networkAttachmentsService().attachmentService(netattach2.id()).remove().send();
										}
										for (NetworkBuilder network2 : networkArray2) {
											hostService.setupNetworks().modifiedBonds(Builders.hostNic().name(nicUsageApiVo.getName()).bonding(Builders.bonding().options(new OptionBuilder[]{Builders.option().name("mode").value(nicUsageApiVo.getBondingMode()), Builders.option().name("miimon").value("100")}).slaves((HostNicBuilder[]) slaves3.toArray(new HostNicBuilder[0])))).modifiedNetworkAttachments(new NetworkAttachmentBuilder[]{Builders.networkAttachment().network(network2).hostNic(Builders.hostNic().name(nicUsageApiVo.getName()))}).send();
										}
										hostService.commitNetConfig().send();
										message.setText("호스트 네트워크 수정 완료(" + nicUsageApiVo.getName() + ")");
										message.setStyle("success");
									} catch (Exception e3) {
										e3.printStackTrace();
										message.setText("호스트 네트워크 수정 실패(" + nicUsageApiVo.getName() + ")");
										message.setStyle("error");
									}
								}
							} else if (!nicUsageApiVo.getNicExNetExist()) {
								try {
									List<NetworkBuilder> vlanList = new ArrayList<>();
									List<HostNicBuilder> slaves4 = new ArrayList<>();
									for (NicUsageApiVo bonding4 : nicUsageApiVo.getBonding()) {
										HostNicBuilder hostNic4 = Builders.hostNic();
										hostNic4.name(bonding4.getName());
										slaves4.add(hostNic4);
									}
									if (nicUsageApiVo.getNetworkId() != null && !nicUsageApiVo.getNetworkId().equals("")) {
										NetworkBuilder network3 = new NetworkBuilder();
										network3.id(nicUsageApiVo.getNetworkId());
										hostService.setupNetworks().modifiedBonds(new HostNicBuilder[]{Builders.hostNic().name(nicUsageApiVo.getName()).bonding(Builders.bonding().options(new OptionBuilder[]{Builders.option().name("mode").value(nicUsageApiVo.getBondingMode()), Builders.option().name("miimon").value("100")}).slaves((HostNicBuilder[]) slaves4.toArray(new HostNicBuilder[0])))}).modifiedNetworkAttachments(new NetworkAttachmentBuilder[]{Builders.networkAttachment().network(network3).hostNic(Builders.hostNic().name(nicUsageApiVo.getName()))}).send();
									}
									if (nicUsageApiVo.getVlanNetworkList() != null && nicUsageApiVo.getVlanNetworkList().size() > 0) {
										for (String vlan5 : nicUsageApiVo.getVlanNetworkList()) {
											NetworkBuilder networkBuilder3 = new NetworkBuilder();
											networkBuilder3.id(vlan5);
											vlanList.add(networkBuilder3);
										}
									}
									for (NetworkBuilder network4 : vlanList) {
										hostService.setupNetworks().modifiedBonds(new HostNicBuilder[]{Builders.hostNic().name(nicUsageApiVo.getName()).bonding(Builders.bonding().options(new OptionBuilder[]{Builders.option().name("mode").value(nicUsageApiVo.getBondingMode()), Builders.option().name("miimon").value("100")}).slaves((HostNicBuilder[]) slaves4.toArray(new HostNicBuilder[0])))}).modifiedNetworkAttachments(new NetworkAttachmentBuilder[]{Builders.networkAttachment().network(network4).hostNic(Builders.hostNic().name(nicUsageApiVo.getName()))}).send();
									}
									hostService.commitNetConfig().send();
									message.setText("호스트 네트워크 수정 완료(" + nicUsageApiVo.getName() + ")");
									message.setStyle("success");
								} catch (Exception e4) {
									e4.printStackTrace();
									message.setText("호스트 네트워크 수정 실패(" + nicUsageApiVo.getName() + ")");
									message.setStyle("error");
								}
							}
						}
					}
				}
				if (nicUsageApiVo.getBonding() != null && "".equals(nicUsageApiVo.getNetworkId())) {
					try {
						List<HostNicBuilder> slaves5 = new ArrayList<>();
						for (NicUsageApiVo bonding5 : nicUsageApiVo.getBonding()) {
							HostNicBuilder hostNic5 = Builders.hostNic();
							hostNic5.name(bonding5.getName());
							slaves5.add(hostNic5);
						}
						hostService.setupNetworks().modifiedBonds(new HostNicBuilder[]{Builders.hostNic().name(nicUsageApiVo.getName()).bonding(Builders.bonding().options(new OptionBuilder[]{Builders.option().name("mode").value(nicUsageApiVo.getBondingMode()), Builders.option().name("miimon").value("100")}).slaves((HostNicBuilder[]) slaves5.toArray(new HostNicBuilder[0])))}).send();
						hostService.commitNetConfig().send();
						message.setText("호스트 네트워크 수정 완료(" + nicUsageApiVo.getName() + ")");
						message.setStyle("success");
						return;
					} catch (Exception e5) {
						e5.printStackTrace();
						log.error(e5.getLocalizedMessage());
						message.setText("호스트 네트워크 수정 실패(" + nicUsageApiVo.getName() + ")");
						message.setStyle("error");
						return;
					}
				}
			} else if (!"".equals(nicUsageApiVo.getId())) {
				int vlanCount = 0;
				List<String> exBondingVlanList = new ArrayList<>();
				String logicalNetId = "";
				for (HostNic checkHostNic : hostNics) {
					if (nicUsageApiVo.getName().equals(checkHostNic.baseInterface())) {
						vlanCount++;
						exBondingVlanList.add(checkHostNic.network().id());
					} else if (nicUsageApiVo.getName().equals(checkHostNic.name()) && checkHostNic.network() != null) {
						logicalNetId = checkHostNic.network().id();
					}
				}
				if (nicUsageApiVo.getInsertSlave()) {
					try {
						List<HostNicBuilder> slaves6 = new ArrayList<>();
						for (NicUsageApiVo bonding6 : nicUsageApiVo.getBonding()) {
							HostNicBuilder hostNic6 = Builders.hostNic();
							hostNic6.name(bonding6.getName());
							slaves6.add(hostNic6);
						}
						NetworkBuilder network5 = new NetworkBuilder();
						network5.id(nicUsageApiVo.getNetworkId());
						network5.name(
								getSysSrvHelper().findNetwork(connection, nicUsageApiVo.getNetworkId()).name()
						);
						hostService.setupNetworks().modifiedBonds(new HostNicBuilder[]{Builders.hostNic().name(nicUsageApiVo.getName()).bonding(Builders.bonding().options(new OptionBuilder[]{Builders.option().name("mode").value(nicUsageApiVo.getBondingMode()), Builders.option().name("miimon").value("100")}).slaves((HostNicBuilder[]) slaves6.toArray(new HostNicBuilder[0])))}).send();
						hostService.commitNetConfig().send();
						message.setText("호스트 네트워크 수정 완료(" + nicUsageApiVo.getName() + ")");
						message.setStyle("success");
					} catch (Exception e6) {
						e6.printStackTrace();
						log.error(e6.getLocalizedMessage());
						message.setText("호스트 네트워크 수정 실패(" + nicUsageApiVo.getName() + ")");
						message.setStyle("error");
					}
				} else if (logicalNetId == null && "".equals(nicUsageApiVo.getNetworkId()) && !"".equals(nicUsageApiVo.getNetworkId())) {
					try {
						NetworkBuilder networkBuilder4 = new NetworkBuilder();
						networkBuilder4.id(nicUsageApiVo.getNetworkId());
						hostService.setupNetworks().modifiedNetworkAttachments(new NetworkAttachmentBuilder[]{Builders.networkAttachment().network(networkBuilder4).hostNic(Builders.hostNic().name(nicUsageApiVo.getName()))}).send();
						hostService.commitNetConfig().send();
						if (!nicUsageApiVo.getVlanNetworkList().isEmpty()) {
							List<NetworkBuilder> vlanNetworkList = new ArrayList<>();
							if (vlanCount == 0) {
								for (String nowVlan : nicUsageApiVo.getVlanNetworkList()) {
									NetworkBuilder networkBuilder22 = new NetworkBuilder();
									networkBuilder22.id(nowVlan);
									vlanNetworkList.add(networkBuilder22);
								}
							} else if (vlanCount > 0) {
								for (String exVlan : exBondingVlanList) {
									for (String nowVlan2 : nicUsageApiVo.getVlanNetworkList()) {
										if (!exVlan.equals(nowVlan2)) {
											NetworkBuilder networkBuilder23 = new NetworkBuilder();
											networkBuilder23.id(nowVlan2);
											vlanNetworkList.add(networkBuilder23);
										}
									}
								}
							}
							for (NetworkBuilder vlanNet : vlanNetworkList) {
								hostService.setupNetworks().modifiedNetworkAttachments(new NetworkAttachmentBuilder[]{Builders.networkAttachment().network(vlanNet).hostNic(Builders.hostNic().name(nicUsageApiVo.getName()))}).send();
								hostService.commitNetConfig().send();
							}
							hostService.commitNetConfig().send();
						}
						message.setText("호스트 네트워크 수정 완료(" + nicUsageApiVo.getName() + ")");
						message.setStyle("success");
					} catch (Exception e7) {
						e7.printStackTrace();
						log.error(e7.getLocalizedMessage());
						message.setText("호스트 네트워크 수정 실패(" + nicUsageApiVo.getName() + ")");
						message.setStyle("error");
					}
				} else if (!nicUsageApiVo.getVlanNetworkList().isEmpty()) {
					try {
						List<NetworkBuilder> vlanNetworkList2 = new ArrayList<>();
						if (vlanCount == 0) {
							for (String nowVlan3 : nicUsageApiVo.getVlanNetworkList()) {
								NetworkBuilder networkBuilder5 = new NetworkBuilder();
								networkBuilder5.id(nowVlan3);
								vlanNetworkList2.add(networkBuilder5);
							}
						} else if (vlanCount > 0) {
							for (String exVlan2 : exBondingVlanList) {
								for (String nowVlan4 : nicUsageApiVo.getVlanNetworkList()) {
									if (!exVlan2.equals(nowVlan4)) {
										NetworkBuilder networkBuilder6 = new NetworkBuilder();
										networkBuilder6.id(nowVlan4);
										vlanNetworkList2.add(networkBuilder6);
									}
								}
							}
						}
						for (NetworkBuilder vlanNet2 : vlanNetworkList2) {
							hostService.setupNetworks().modifiedNetworkAttachments(new NetworkAttachmentBuilder[]{Builders.networkAttachment().network(vlanNet2).hostNic(Builders.hostNic().name(nicUsageApiVo.getName()))}).send();
							hostService.commitNetConfig().send();
						}
						hostService.commitNetConfig().send();
						message.setText("호스트 네트워크 수정 완료(" + nicUsageApiVo.getName() + ")");
						message.setStyle("success");
					} catch (Exception e8) {
						e8.printStackTrace();
						message.setText("호스트 네트워크 수정 실패(" + nicUsageApiVo.getName() + ")");
						message.setStyle("error");
					}
				} else if (logicalNetId != null && "".equals(nicUsageApiVo.getNetworkId())) {
					try {
						Iterator<NetworkAttachment> it7
								= hostService.networkAttachmentsService().list().send().attachments().iterator();
						while (true) {
							if (!it7.hasNext()) {
								break;
							}
							NetworkAttachment networkAttachment5 = it7.next();
							if (networkAttachment5.network().id().equals(logicalNetId)) {
								networkAttachmentId = networkAttachment5.id();
								break;
							}
						}
						hostService.networkAttachmentsService().attachmentService(networkAttachmentId).remove().send();
						hostService.commitNetConfig().send();
						message.setText("호스트 네트워크 수정 완료(" + nicUsageApiVo.getName() + ")");
						message.setStyle("success");
					} catch (Exception e9) {
						e9.printStackTrace();
						log.error(e9.getLocalizedMessage());
						message.setText("호스트 네트워크 수정 실패(" + nicUsageApiVo.getName() + ")");
						message.setStyle("error");
					}
				} else if (vlanCount > 0 && !nicUsageApiVo.getVlanNetworkList().isEmpty() && vlanCount > nicUsageApiVo.getVlanNetworkList().size()) {
					List<NetworkBuilder> vlanNetworkList3 = new ArrayList<>();
					List<String> vlanNetAttachment = new ArrayList<>();
					try {
						if (!nicUsageApiVo.getVlanNetworkList().isEmpty()) {
							for (String exVlan3 : exBondingVlanList) {
								for (String nowVlan5 : nicUsageApiVo.getVlanNetworkList()) {
									if (!exVlan3.equals(nowVlan5)) {
										NetworkBuilder networkBuilder7 = new NetworkBuilder();
										networkBuilder7.id(exVlan3);
										vlanNetworkList3.add(networkBuilder7);
									}
								}
							}
						} else if (!nicUsageApiVo.getVlanNetworkList().isEmpty()) {
							for (String exVlan4 : exBondingVlanList) {
								NetworkBuilder networkBuilder8 = new NetworkBuilder();
								networkBuilder8.id(exVlan4);
								vlanNetworkList3.add(networkBuilder8);
							}
						}
						for (NetworkAttachment networkAttachment6 : hostService.networkAttachmentsService().list().send().attachments()) {
							for (NetworkBuilder vlan6 : vlanNetworkList3) {
								if (networkAttachment6.network().id().equals(vlan6)) {
									vlanNetAttachment.add(networkAttachment6.id());
								}
							}
						}
						for (String attachmentId : vlanNetAttachment) {
							hostService.networkAttachmentsService().attachmentService(attachmentId).remove().send();
							hostService.commitNetConfig().send();
						}
						message.setText("호스트 네트워크 수정 완료(" + nicUsageApiVo.getName() + ")");
						message.setStyle("success");
					} catch (Exception e10) {
						e10.printStackTrace();
						log.error(e10.getLocalizedMessage());
						message.setText("호스트 네트워크 수정 실패(" + nicUsageApiVo.getName() + ")");
						message.setStyle("error");
					}
				}
			} else if (!nicUsageApiVo.getBonding().isEmpty()) {
				List<NicUsageApiVo> deleteBondingList = new ArrayList<>();
				List<NicUsageApiVo> modifyNicList = new ArrayList<>();
				try {
					List<String> vlanNetList = new ArrayList<>();
					List<String> vlanNetAttachList = new ArrayList<>();
					for (HostNic hostNic7 : hostNics) {
						if (hostNic7.baseInterface() != null && hostNic7.baseInterface().equals(nicUsageApiVo.getUnBondName()) && hostNic7.network() != null && !hostNic7.network().id().equals("")) {
							vlanNetList.add(hostNic7.network().id());
						}
					}
					for (String vlan7 : vlanNetList) {
						for (NetworkAttachment networkAttachment7 : hostService.networkAttachmentsService().list().send().attachments()) {
							if (networkAttachment7.network().id().equals(vlan7)) {
								vlanNetAttachList.add(networkAttachment7.id());
							}
						}
					}
					for (HostNic hostNic8 : hostNics) {
						if (hostNic8.bonding() != null && hostNic8.name().equals(nicUsageApiVo.getUnBondName()) && !nicUsageApiVo.getCheckBonding()) {
							if (hostNic8.network() != null) {
								NicUsageApiVo tempDeleteBonding = new NicUsageApiVo();
								tempDeleteBonding.setNetworkName(hostNic8.network().name());
								tempDeleteBonding.setNetworkId(hostNic8.network().id());
								Iterator<HostNic> it8 = hostNic8.bonding().slaves().iterator();
								while (true) {
									if (!it8.hasNext())
										break;
									HostNic bonding7 = (HostNic) it8.next();
									if (bonding7.id().equals(nicUsageApiVo.getId())) {
										tempDeleteBonding.setHostName(hostNic8.name());
										tempDeleteBonding.setHostId(hostNic8.id());
										deleteBondingList.add(tempDeleteBonding);
										break;
									}
								}
							} else if (hostNic8.network() == null) {
								NicUsageApiVo tempDeleteBonding2 = new NicUsageApiVo();
								Iterator<HostNic> it9 = hostNic8.bonding().slaves().iterator();
								while (true) {
									if (!it9.hasNext())
										break;
									HostNic bonding8 = it9.next();
									if (bonding8.id().equals(nicUsageApiVo.getId())) {
										tempDeleteBonding2.setHostName(hostNic8.name());
										deleteBondingList.add(tempDeleteBonding2);
										break;
									}
								}
							}
						} else if (hostNic8.bonding() == null && !nicUsageApiVo.getCheckBonding() && hostNic8.id().equals(nicUsageApiVo.getId())) {
							if (hostNic8.network() == null) {
								modifyNicList.add(nicUsageApiVo);
							} else if (hostNic8.network() != null) {
								Iterator<NetworkAttachment> it10
										= hostService.networkAttachmentsService().list().send().attachments().iterator();
								while (true) {
									if (it10.hasNext()) {
										NetworkAttachment networkAttachment8 = (NetworkAttachment) it10.next();
										if (networkAttachment8.network().id().equals(hostNic8.network().id())) {
											nicUsageApiVo.setNetworkAttachmentId(networkAttachment8.id());
											modifyNicList.add(nicUsageApiVo);
											break;
										}
									}
								}
							}
						}
					}
					int idx = 0;
					while (true) {
						if (idx >= deleteBondingList.size()) {
							break;
						} else if (idx >= deleteBondingList.size() - 1 || !deleteBondingList.get(idx).getHostName().equals(deleteBondingList.get(idx + 1).getHostName()) || !deleteBondingList.get(idx).getNetworkId().equals(deleteBondingList.get(idx + 1).getNetworkId())) {
							idx++;
						} else {
							deleteBondingList.remove(idx);
							break;
						}
					}
					if (deleteBondingList.size() != 0) {
						Iterator<NicUsageApiVo> it11 = deleteBondingList.iterator();
						while (true) {
							if (!it11.hasNext()) {
								break;
							}
							NicUsageApiVo deleteBonding = it11.next();
							if (deleteBonding.getNetworkId() != null) {
								Iterator it12 = hostService.networkAttachmentsService().list().send().attachments().iterator();
								while (true) {
									if (!it12.hasNext()) {
										break;
									}
									NetworkAttachment networkAttachment9 = (NetworkAttachment) it12.next();
									if (networkAttachment9.network().id().equals(deleteBonding.getNetworkId())) {
										networkAttachmentId = networkAttachment9.id();
										break;
									}
								}
								hostService.networkAttachmentsService().attachmentService(networkAttachmentId).remove().send();
								if (vlanNetAttachList.size() > 0) {
									for (String delVlan : vlanNetAttachList) {
										hostService.networkAttachmentsService().attachmentService(delVlan).remove().send();
									}
								}
								hostService.setupNetworks().removedBonds(new HostNicBuilder[]{Builders.hostNic().name(deleteBonding.getHostName())}).send();
								hostService.commitNetConfig().send();
							} else if (deleteBonding.getNetworkId() == null) {
								hostService.setupNetworks().removedBonds(new HostNicBuilder[]{Builders.hostNic().name(deleteBonding.getHostName())}).send();
								hostService.commitNetConfig().send();
								break;
							}
						}
					} else if (!modifyNicList.isEmpty()) {
						List<NetworkBuilder> vlanNetworkList4 = new ArrayList<>();
						List<NetworkBuilder> logicalNetwork = new ArrayList<>();
						Iterator<NicUsageApiVo> it13 = modifyNicList.iterator();
						while (true) {
							if (!it13.hasNext()) {
								break;
							}
							NicUsageApiVo modifyNic = it13.next();
							Iterator<HostNic> it14 = hostNics.iterator();
							while (true) {
								if (!it14.hasNext()) {
									break;
								}
								HostNic hostNic9 = it14.next();
								if (hostNic9.id().equals(modifyNic.getId()) && hostNic9.network() == null && modifyNic.getNetworkId() != null) {
									NetworkBuilder networkBuilder9 = new NetworkBuilder();
									networkBuilder9.id(modifyNic.getNetworkId());
									logicalNetwork.add(networkBuilder9);
									break;
								}
							}
							if (!modifyNic.getVlanNetworkList().isEmpty()) {
								for (String vlanList2 : modifyNic.getVlanNetworkList()) {
									if (!vlanList2.equals("")) {
										NetworkBuilder networkBuilder10 = new NetworkBuilder();
										networkBuilder10.id(vlanList2);
										vlanNetworkList4.add(networkBuilder10);
									}
								}
							}
							for (NetworkBuilder logicalNet : logicalNetwork) {
								hostService.setupNetworks().modifiedNetworkAttachments(new NetworkAttachmentBuilder[]{Builders.networkAttachment().network(logicalNet).hostNic(Builders.hostNic().name(nicUsageApiVo.getName()))}).send();
								hostService.commitNetConfig().send();
							}
							for (NetworkBuilder vlanNet3 : vlanNetworkList4) {
								hostService.setupNetworks().modifiedNetworkAttachments(new NetworkAttachmentBuilder[]{Builders.networkAttachment().network(vlanNet3).hostNic(Builders.hostNic().name(nicUsageApiVo.getName()))}).send();
								hostService.commitNetConfig().send();
							}
							if ("".equals(modifyNic.getNetworkId())) {
								hostService.networkAttachmentsService().attachmentService(nicUsageApiVo.getNetworkAttachmentId()).remove().send();
								hostService.commitNetConfig().send();
								break;
							}
						}
					}
					message.setText("호스트 네트워크 수정 완료(" + nicUsageApiVo.getName() + ")");
					message.setStyle("success");
				} catch (Exception e11) {
					e11.printStackTrace();
					log.error(e11.getLocalizedMessage());
					message.setText("호스트 네트워크 수정 실패(" + nicUsageApiVo.getName() + ")");
					message.setStyle("error");
				}
			}
			this.websocketService.sendMessage("/topic/notify", new Gson().toJson(message));
		}

	}

	@Override
	public void modifyNicNetwork(NetworkAttachmentVo networkAttachmentVo) {
		Connection connection = this.adminConnectionService.getConnection();
		Host host
				= getSysSrvHelper().findHost(connection, networkAttachmentVo.getNetHostId());
		HostService hostService
				= getSysSrvHelper().srvHost(connection, host.id());
		MessageVo message = new MessageVo();
		message.setTitle("호스트 네트워크 수정");
		try {
			if ("none".equals(networkAttachmentVo.getBootProtocol()) && ""
					.equals(networkAttachmentVo.getDnsServer().get(0))) {
				NetworkBuilder network = new NetworkBuilder();
				network.name(networkAttachmentVo.getNicNetworkName());
				network.id(networkAttachmentVo.getNicNetworkId());

				getSysSrvHelper().removeNetworkAttachmentFromHost(connection, host.id(), networkAttachmentVo.getNetAttachmentId());
				hostService.setupNetworks()
						.modifiedNetworkAttachments(new NetworkAttachmentBuilder[] { Builders.networkAttachment()
								.hostNic(Builders.hostNic().name(networkAttachmentVo.getHostNicName()))
								.network(network)
								.ipAddressAssignments(Builders.ipAddressAssignment().assignmentMethod(BootProtocol.NONE)) }).send();
				hostService.commitNetConfig().send();
			} else if ("none".equals(networkAttachmentVo.getBootProtocol()) &&
					!networkAttachmentVo.getDnsServer().isEmpty()) {
				NetworkBuilder network = new NetworkBuilder();
				network.name(networkAttachmentVo.getNicNetworkName());
				network.id(networkAttachmentVo.getNicNetworkId());
				DnsResolverConfigurationBuilder dnsContainer = new DnsResolverConfigurationBuilder();
				for (String dnsServer : networkAttachmentVo.getDnsServer()) {
					dnsContainer.nameServers(dnsServer);
				}
				hostService.networkAttachmentsService().attachmentService(networkAttachmentVo.getNetAttachmentId()).remove().send();
				hostService.setupNetworks()
						.modifiedNetworkAttachments(new NetworkAttachmentBuilder[] { Builders.networkAttachment()
								.hostNic(Builders.hostNic().name(networkAttachmentVo.getHostNicName()))
								.network(network)

								.ipAddressAssignments(Builders.ipAddressAssignment()
										.assignmentMethod(BootProtocol.NONE)).dnsResolverConfiguration(dnsContainer) }).send();
				hostService.commitNetConfig().send();
			} else if ("dhcp".equals(networkAttachmentVo.getBootProtocol()) && ""
					.equals(networkAttachmentVo.getDnsServer().get(0))) {
				NetworkBuilder network = new NetworkBuilder();
				network.name(networkAttachmentVo.getNicNetworkName());
				network.id(networkAttachmentVo.getNicNetworkId());
				hostService.networkAttachmentsService().attachmentService(networkAttachmentVo.getNetAttachmentId()).remove().send();
				hostService.setupNetworks()
						.modifiedNetworkAttachments(new NetworkAttachmentBuilder[] { Builders.networkAttachment()
								.hostNic(Builders.hostNic().name(networkAttachmentVo.getHostNicName()))
								.network(network)

								.ipAddressAssignments(Builders.ipAddressAssignment()
								.assignmentMethod(BootProtocol.DHCP)) }).send();
				hostService.commitNetConfig().send();
			} else if ("dhcp".equals(networkAttachmentVo.getBootProtocol()) &&
					!networkAttachmentVo.getDnsServer().isEmpty()) {
				NetworkBuilder network = new NetworkBuilder();
				network.name(networkAttachmentVo.getNicNetworkName());
				network.id(networkAttachmentVo.getNicNetworkId());
				DnsResolverConfigurationBuilder dnsContainer = new DnsResolverConfigurationBuilder();
				for (String dnsServer : networkAttachmentVo.getDnsServer()) {
					dnsContainer.nameServers(dnsServer);
				}
				hostService.networkAttachmentsService().attachmentService(networkAttachmentVo.getNetAttachmentId()).remove().send();
				hostService.setupNetworks()
						.modifiedNetworkAttachments(new NetworkAttachmentBuilder[] { Builders.networkAttachment()
								.hostNic(Builders.hostNic().name(networkAttachmentVo.getHostNicName()))
								.network(network)

								.ipAddressAssignments(Builders.ipAddressAssignment()
										.assignmentMethod(BootProtocol.DHCP)).dnsResolverConfiguration(dnsContainer) }).send();
				hostService.commitNetConfig().send();
			} else if ("static".equals(networkAttachmentVo.getBootProtocol()) && ""
					.equals(networkAttachmentVo.getDnsServer().get(0))) {
				NetworkBuilder network = new NetworkBuilder();
				network.name(networkAttachmentVo.getNicNetworkName());
				network.id(networkAttachmentVo.getNicNetworkId());
				hostService.networkAttachmentsService()
						.attachmentService(networkAttachmentVo.getNetAttachmentId())
						.update().attachment(
								Builders.networkAttachment()
										.network(network)

										.hostNic(Builders.hostNic().name(networkAttachmentVo.getHostNicName()))
										.ipAddressAssignments(Builders.ipAddressAssignment()
												.assignmentMethod(BootProtocol.STATIC)
												.ip(
												Builders.ip()
														.address(networkAttachmentVo.getNicAddress())
														.netmask(networkAttachmentVo.getNicNetmask())
														.gateway(networkAttachmentVo.getNicGateway())))).send();
				hostService.commitNetConfig().send();
			} else if ("static".equals(networkAttachmentVo.getBootProtocol()) &&
					!networkAttachmentVo.getDnsServer().isEmpty()) {
				DnsResolverConfigurationBuilder dnsContainer = new DnsResolverConfigurationBuilder();
				for (String dnsServer : networkAttachmentVo.getDnsServer()) {
					dnsContainer.nameServers(dnsServer);
				}
				NetworkBuilder network = new NetworkBuilder();
				network.name(networkAttachmentVo.getNicNetworkName());
				network.id(networkAttachmentVo.getNicNetworkId());
				hostService.networkAttachmentsService()
						.attachmentService(networkAttachmentVo.getNetAttachmentId())
						.update().attachment(
								Builders.networkAttachment()
										.network(network)

										.hostNic(Builders.hostNic().name(networkAttachmentVo.getHostNicName()))
										.ipAddressAssignments(Builders.ipAddressAssignment()
												.assignmentMethod(BootProtocol.STATIC)
												.ip(Builders.ip()
													.address(networkAttachmentVo.getNicAddress())
													.netmask(networkAttachmentVo.getNicNetmask())
													.gateway(networkAttachmentVo.getNicGateway()))).dnsResolverConfiguration(dnsContainer))

						.send();
				hostService.commitNetConfig().send();
			}
			try {
				Thread.sleep(2000L);
				message.setText("할당된 네트워크 수정 완료("+ networkAttachmentVo.getNicNetworkName() + ")");
				message.setStyle("success");
			} catch (InterruptedException e) {
				e.printStackTrace();
				log.error(e.getLocalizedMessage());
				message.setText("할당된 네트워크 수정 실패("+ networkAttachmentVo.getNicNetworkName() + ")");
				message.setStyle("error");
			}
			this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
			message.setText("할당된 네트워크 수정 실패("+ networkAttachmentVo.getNicNetworkName() + ")");
			message.setStyle("error");
		}
	}

	@Override
	public HostCreateVo retrieveCreateHostInfo(String hostId) {
		Connection connection = this.adminConnectionService.getConnection();
		HostService hostService
				= getSysSrvHelper().srvHost(connection, hostId);
		Host host
				= (hostService.get().send()).host();
		HostCreateVo hostCreateVo = new HostCreateVo();
		hostCreateVo.setId(hostId);
		hostCreateVo.setClusterId(host.cluster().id());
		hostCreateVo.setName(host.name());
		hostCreateVo.setComment(host.comment());
		hostCreateVo.setDescription(host.description());
		hostCreateVo.setStatus(host.status().value());
		hostCreateVo.setPowerManagementEnabled(host.powerManagement().enabled());
		if (host.powerManagement().enabled()) {
			List<Agent> agents = (hostService.fenceAgentsService().list().send()).agents();
			FenceAgentVo fenceAgentVo = new FenceAgentVo();
			fenceAgentVo.setId((agents.get(0)).id());
			fenceAgentVo.setAddress((agents.get(0)).address());
			fenceAgentVo.setUsername((agents.get(0)).username());
			fenceAgentVo.setType((agents.get(0)).type());
			hostCreateVo.setFenceAgent(fenceAgentVo);
		}
		List<HostHaVo> hostHaList = this.clustersDao.retrieveHostHaInfo();
		for (HostHaVo hostHa : hostHaList) {
			if (hostHa.getHostId().equals(host.id()))
				hostCreateVo.setHostEngineEnabled(true);
		}
		SshVo sshVo = new SshVo();
		sshVo.setAddress(host.address());
		sshVo.setPort(host.ssh().port().intValue());
		sshVo.setId("root");
		sshVo.setPublicKey("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCbNWOIsBF4qySgLk+6Z194tAatTsGKQELvjfZv9HjciTOkA8+X3p4Ognz74Oi+RWJiHA69BUzTehDw6NMuOEu2cbY+7IrX629N/ohh7ke4+em1BHEbAzJvaDPgzCL85KqRyZURJBerOalc3LruP0jDf4QYPk3+aT/k3D79hMKPPw9NWVeb8d0vfiAUcid0TTeBcWTbHdnk4idS/FtMC5rixIzm9Yy5Z+NDI4s1fadXJ2uWYT53W5dhj4tGVXub2Qm4OTPjevqXMvEkKvW5ZOuRjs2GUdyC3xIuXP6jSInPfxjkcmj2DQlF2fJqTkJ1JvGGnR5iLpagFhrJ9lTFOEyX ovirt-engine");
		hostCreateVo.setSsh(sshVo);
		return hostCreateVo;
	}

	@Override
	public List<HostDetailVo> retrieveHostsInfo(String status) {
		Connection connection = this.adminConnectionService.getConnection();
		List<Host> hosts = null;
		if ("all".equalsIgnoreCase(status)) {
			hosts = getSysSrvHelper().findAllHosts(connection, "");
		} else if (HostStatus.UP.value().equalsIgnoreCase(status)) {
			hosts = getSysSrvHelper().findAllHosts(connection, "status=up");
		} else {
			hosts = getSysSrvHelper().findAllHosts(connection, "status!=up");
		}
		List<HostHaVo> hostHaList = this.clustersDao.retrieveHostHaInfo();
		List<HostDetailVo> hostDetailList = new ArrayList<>();
		for (Host host : hosts) {
			HostDetailVo hostDetailVo = getHostInfo(connection, host);
			setHosCpuMemory(connection, host, hostDetailVo);
			setClusterInfo(connection, hostDetailVo.getClusterId(), hostDetailVo);
			hostDetailList.add(hostDetailVo);
			for (HostHaVo hostHa : hostHaList) {
				if (hostHa.getHostId().equals(host.id())) {
					hostDetailVo.setHaConfigured(true);
					hostDetailVo.setHaScore(hostHa.getHaScore());
				}
			}
		}
		return hostDetailList;
	}

	private void setClusterInfo(Connection connection, String clusterId, HostDetailVo hostDetailVo) {
		Cluster cluster
				= getSysSrvHelper().findCluster(connection, clusterId);
		hostDetailVo.setClusterId(cluster.id());
		hostDetailVo.setClusterName(cluster.name());
		hostDetailVo.setCpuType(cluster.cpu().type());
	}

	private void setHosCpuMemory(Connection connection, Host host, HostDetailVo hostDetailVo) {
		try {
			List<Statistic> stats =
					getSysSrvHelper().findAllStatisticsFromHost(connection, host.id());
			for (Statistic stat : stats) {
				if (stat.name().equals("memory.total"))
					hostDetailVo.setMemoryTotal((stat.values().get(0)).datum());
				if (stat.name().equals("memory.used"))
					hostDetailVo.setMemoryUsed((stat.values().get(0)).datum());
				if (stat.name().equals("memory.free"))
					hostDetailVo.setMemoryFree((stat.values().get(0)).datum());
				if (stat.name().equals("swap.total"))
					hostDetailVo.setSwapTotal((stat.values().get(0)).datum());
				if (stat.name().equals("swap.used"))
					hostDetailVo.setSwapUsed((stat.values().get(0)).datum());
				if (stat.name().equals("swap.free"))
					hostDetailVo.setSwapFree((stat.values().get(0)).datum());
				if (stat.name().equals("ksm.cpu.current"))
					hostDetailVo.setKsmCpuUsagePercent((stat.values().get(0)).datum());
				if (stat.name().equals("cpu.current.user"))
					hostDetailVo.setUserCpuUsagePercent((stat.values().get(0)).datum());
				if (stat.name().equals("cpu.current.system"))
					hostDetailVo.setSystemCpuUsagePercent((stat.values().get(0)).datum());
				if (stat.name().equals("cpu.current.idle"))
					hostDetailVo.setIdleCpuUsagePercent((stat.values().get(0)).datum());
				if (stat.name().equals("boot.time"))
					hostDetailVo.setBootTime((stat.values().get(0)).datum());
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	@Override
	public List<HostDetailVo> retrieveLunHostsInfo(String status) {
		Connection connection = this.adminConnectionService.getConnection();
		List<Host> hosts
				= getSysSrvHelper().findAllHosts(connection, "");
		List<HostDetailVo> hostDetailList = new ArrayList<>();
		for (Host host : hosts) {
			if ("up".equals(host.status().value())) {
				HostDetailVo hostDetailVo = getHostInfo(connection, host);
				List<HostStorage> luns
						= getSysSrvHelper().findAllStoragesFromHost(connection, host.id());
				List<LunVo> lunVos = new ArrayList<>();
				for (HostStorage lun : luns) {
					LunVo lunVo = new LunVo();
					lunVo.setId((lun.logicalUnits().get(0)).id());
					lunVo.setPath((lun.logicalUnits().get(0)).paths().toString());
					lunVo.setProductId((lun.logicalUnits().get(0)).productId());
					lunVo.setSerial((lun.logicalUnits().get(0)).serial());
					lunVo.setSize((lun.logicalUnits().get(0)).size().toString());
					lunVo.setType(lun.type().value());
					lunVo.setVendor((lun.logicalUnits().get(0)).vendorId());
					lunVo.setHostId(host.id());
					if ((lun.logicalUnits().get(0)).diskId() != null)
						lunVo.setDiskId((lun.logicalUnits().get(0)).diskId());
					lunVos.add(lunVo);
				}
				hostDetailVo.setLunVos(lunVos);
				hostDetailList.add(hostDetailVo);
			}
		}
		return hostDetailList;
	}

	@Override
	public HostDetailVo retrieveHostDetail(String hostId) {
		Connection connection = this.adminConnectionService.getConnection();
		Host host
				= getSysSrvHelper().findHost(connection, hostId);
		HostDetailVo hostDetailVo = getHostInfo(connection, host);
		setHostDetail(connection, host, hostDetailVo);
		setNetworkAttachment(connection, host, hostDetailVo);
		setVmsInfo(connection, host, hostDetailVo);
		return hostDetailVo;
	}


	// TODO: 함수로 뺴내기
	private void setHostDetail(Connection connection, Host host, HostDetailVo hostDetailVo) {
		hostDetailVo.setHwManufacturer(host.hardwareInformation().manufacturer());
		hostDetailVo.setHwProductName(host.hardwareInformation().productName());
		List<NicUsageApiVo> nicsUsageApiVo3 = new ArrayList<>();
		List<NicUsageApiVo> nicsUsageApiVo = new ArrayList<>();
		try {
			List<HostNic> hostNics
					= getSysSrvHelper().findNicsFromHost(connection, host.id());
			boolean[] slaveFlagList = new boolean[hostNics.size()];
			for (HostNic hostNic : hostNics) {
				NicUsageApiVo nicUsageApiVo = new NicUsageApiVo();
				nicUsageApiVo.setCheckBonding(false);
				nicUsageApiVo.setHostId(host.id());
				nicUsageApiVo.setId(hostNic.id());
				nicUsageApiVo.setName(hostNic.name());
				nicUsageApiVo.setIpAddress(hostNic.ip().address());
				if (hostNic.baseInterface() == null) {
					nicUsageApiVo.setBase(hostNic.name());
				} else if (hostNic.baseInterface() != null) {
					nicUsageApiVo.setBase(hostNic.baseInterface());
				}
				if (hostNic.baseInterface() != null)
					nicUsageApiVo.setBaseInterface(hostNic.baseInterface());
				if (hostNic.vlan() != null)
					nicUsageApiVo.setVlan(hostNic.vlan().id());
				if (hostNic.status() != null)
					nicUsageApiVo.setStatus(hostNic.status().name());
				if (hostNic.network() != null)
					if (hostNic.vlan() == null) {
						nicUsageApiVo.setNetworkName(hostNic.network().name());
						nicUsageApiVo.setNetworkId(hostNic.network().id());
					} else if (hostNic.vlan() != null) {
						List<String> tempVlanList = new ArrayList<>();
						tempVlanList.add(hostNic.network().id());
						nicUsageApiVo.setVlanNetworkList(tempVlanList);
					}
				if (hostNic.bonding() != null)
					for (Option bondingOption : hostNic.bonding().options()) {
						if (bondingOption.name().equals("mode")) {
							nicUsageApiVo.setBondingMode(bondingOption.value());
							nicUsageApiVo.setBondingModeName(bondingOption.type());
						}
					}
				if (hostNic.mac() != null) {
					nicUsageApiVo.setMacAddress(hostNic.mac().address());
				} else {
					nicUsageApiVo.setMacAddress("");
				}
				List<Statistic> nicStats
						= getSysSrvHelper().findAllStatisticsFromHostNic(connection, host.id(), hostNic.id());
				for (Statistic nicStat : nicStats) {
					if (nicStat.name().equals("data.current.rx"))
						nicUsageApiVo.setDataCurrentRx(((Value)nicStat.values().get(0)).datum());
					if (nicStat.name().equals("data.current.tx"))
						nicUsageApiVo.setDataCurrentTx(((Value)nicStat.values().get(0)).datum());
					if (nicStat.name().equals("data.current.rx.bps"))
						nicUsageApiVo.setDataCurrentRxBps(((Value)nicStat.values().get(0)).datum());
					if (nicStat.name().equals("data.current.tx.bps"))
						nicUsageApiVo.setDataCurrentTxBps(((Value)nicStat.values().get(0)).datum());
					if (nicStat.name().equals("data.total.rx"))
						nicUsageApiVo.setDataTotalRx(((Value)nicStat.values().get(0)).datum());
					if (nicStat.name().equals("data.total.tx"))
						nicUsageApiVo.setDataTotalTx(((Value)nicStat.values().get(0)).datum());
					if (nicStat.name().equals("errors.total.rx"));
					if (nicStat.name().equals("errors.total.tx"));
				}
				if (hostNic.bonding() != null)
					for (HostNic bondingSlave : hostNic.bonding().slaves()) {
						String id = bondingSlave.id();
						for (int j = 0; j < hostNics.size(); j++) {
							HostNic slaveHost = hostNics.get(j);
							if (slaveHost.id().equals(id)) {
								NicUsageApiVo nicUsageApiVo2 = new NicUsageApiVo();
								nicUsageApiVo2.setCheckBonding(false);
								nicUsageApiVo2.setHostId(host.id());
								nicUsageApiVo2.setId((hostNics.get(j)).id());
								nicUsageApiVo2.setName((hostNics.get(j)).name());
								nicUsageApiVo2.setIpAddress((hostNics.get(j)).ip().address());
								nicUsageApiVo2.setStatus((hostNics.get(j)).status().name());
								if ((hostNics.get(j)).baseInterface() == null) {
									nicUsageApiVo2.setBase((hostNics.get(j)).name());
								} else if ((hostNics.get(j)).baseInterface() != null) {
									nicUsageApiVo2.setBase((hostNics.get(j)).baseInterface());
								}
								if ((hostNics.get(j)).baseInterface() != null)
									nicUsageApiVo2.setBaseInterface((hostNics.get(j)).baseInterface());
								if ((hostNics.get(j)).vlan() != null)
									nicUsageApiVo2.setVlan((hostNics.get(j)).vlan().id());
								if ((hostNics.get(j)).mac() != null) {
									nicUsageApiVo2.setMacAddress((hostNics.get(j)).mac().address());
								} else {
									nicUsageApiVo2.setMacAddress("");
								}
								if ((hostNics.get(j)).network() != null)
									if ((hostNics.get(j)).vlan() == null) {
										nicUsageApiVo2.setNetworkName(hostNic.network().name());
										nicUsageApiVo2.setNetworkId((hostNics.get(j)).network().id());
									} else if ((hostNics.get(j)).vlan() != null) {
										List<String> tempVlanList = new ArrayList<>();
										tempVlanList.add((hostNics.get(j)).network().id());
										nicUsageApiVo2.setVlanNetworkList(tempVlanList);
									}
								List<Statistic> nicStats2
										= getSysSrvHelper().findAllStatisticsFromHostNic(connection, host.id(), hostNics.get(j).id());
								for (Statistic nicStat : nicStats2) {
									if (nicStat.name().equals("data.current.rx"))
										nicUsageApiVo2.setDataCurrentRx(((Value)nicStat.values().get(0)).datum());
									if (nicStat.name().equals("data.current.tx"))
										nicUsageApiVo2.setDataCurrentTx(((Value)nicStat.values().get(0)).datum());
									if (nicStat.name().equals("data.current.rx.bps"))
										nicUsageApiVo2.setDataCurrentRxBps(((Value)nicStat.values().get(0)).datum());
									if (nicStat.name().equals("data.current.tx.bps"))
										nicUsageApiVo2.setDataCurrentTxBps(((Value)nicStat.values().get(0)).datum());
									if (nicStat.name().equals("data.total.rx"))
										nicUsageApiVo2.setDataTotalRx(((Value)nicStat.values().get(0)).datum());
									if (nicStat.name().equals("data.total.tx"))
										nicUsageApiVo2.setDataTotalTx(((Value)nicStat.values().get(0)).datum());
									if (nicStat.name().equals("errors.total.rx"));
									if (nicStat.name().equals("errors.total.tx"));
								}
								nicUsageApiVo.getBonding().add(nicUsageApiVo2);
								slaveFlagList[j] = true;
							}
						}
					}
				nicsUsageApiVo.add(nicUsageApiVo);
			}
			for (int i = 0; i < hostNics.size(); i++) {
				if (!slaveFlagList[i])
					nicsUsageApiVo3.add(nicsUsageApiVo.get(i));
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		hostDetailVo.setHostNicsUsageApi(nicsUsageApiVo3);
		HostSwVo hostSw = this.clustersDao.retrieveHostSw(host.id());
		hostDetailVo.setHostSw(hostSw);
		Cluster cluster
				= getSysSrvHelper().findCluster(connection, host.cluster().id());
		hostDetailVo.setClusterName(cluster.name());
		hostDetailVo.setCpuType(cluster.cpu().type());
	}


	// TODO: ModelKt 로 이동
	private void setNetworkAttachment(Connection connection, Host host, HostDetailVo hostDetailVo) {
		List<NetworkAttachmentVo> networkAttachmentListVo = new ArrayList<>();
		try {
			List<NetworkAttachment> networkAttachmentList
					= getSysSrvHelper().findAllNetworkAttachmentsFromHost(connection, host.id());
			for (NetworkAttachment networkAttachment : networkAttachmentList) {
				NetworkAttachmentVo networkAttachmentVo = new NetworkAttachmentVo();
				if (networkAttachment.dnsResolverConfiguration() != null)
					networkAttachmentVo.setDnsServer(networkAttachment.dnsResolverConfiguration().nameServers());
				if (networkAttachment.ipAddressAssignments() != null) {
					networkAttachmentVo.setBootProtocol((networkAttachment.ipAddressAssignments().get(0)).assignmentMethod().value());
					networkAttachmentVo.setNicAddress((networkAttachment.ipAddressAssignments().get(0)).ip().address());
					networkAttachmentVo.setNicGateway((networkAttachment.ipAddressAssignments().get(0)).ip().gateway());
					networkAttachmentVo.setNicNetmask((networkAttachment.ipAddressAssignments().get(0)).ip().netmask());
				}
				networkAttachmentVo.setNetAttachmentId(networkAttachment.id());
				networkAttachmentVo.setNetHostId(networkAttachment.host().id());
				networkAttachmentVo.setNetHostName(host.name());
				networkAttachmentVo.setHostNicId(networkAttachment.hostNic().id());
				networkAttachmentVo.setHostNicName(
						getSysSrvHelper().findNicFromHost(connection, host.id(), networkAttachmentVo.getHostNicId()).name()
				);
				networkAttachmentVo.setNicNetworkId(networkAttachment.network().id());
				networkAttachmentVo.setNicNetworkName(
						getSysSrvHelper().findNetwork(connection, networkAttachmentVo.getNicNetworkId()).name()
				);
				networkAttachmentListVo.add(networkAttachmentVo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		hostDetailVo.setNetAttachment(networkAttachmentListVo);
	}


	// TODO: ModelKt 로 이동
	@Override
	public List<EventVo> retrieveHostEvents(String hostId) {
		Connection connection = this.adminConnectionService.getConnection();
		Host host
				= getSysSrvHelper().findHost(connection, hostId);
		List<Event> events
				= getSysSrvHelper().findAllEvents(connection, "host.name=" + host.name());
		List<EventVo> eventVoList = new ArrayList<>();
		for (Event event : events) {
			EventVo eventVo = new EventVo();
			eventVo.setId(event.id());
			eventVo.setCorrelationId(event.correlationId());
			eventVo.setSeverity(event.severity().value());
			eventVo.setCode(event.code());
			eventVo.setDescription(event.description());
			eventVo.setOrigin(event.origin());
			eventVo.setTime(event.time());
			eventVoList.add(eventVo);
		}
		return eventVoList;
	}

	private void setVmsInfo(Connection connection, Host host, HostDetailVo hostDetailVo) {
		List<VmSummaryVo> vmSummaries = new ArrayList<>();
		List<Vm> vms
				= getSysSrvHelper().findAllVms(connection, "host = " + host.name()) ;
		for (Vm vm : vms) {
			VmSummaryVo vmSummary = getVmInfo(connection, vm);
			vmSummaries.add(vmSummary);
		}
		hostDetailVo.setVmSummaries(vmSummaries);
	}

	@Override
	public HostDetailVo getHostInfo(Connection connection, Host host) {
		HostDetailVo hostDetailVo = new HostDetailVo();
		hostDetailVo.setId(host.id());
		hostDetailVo.setName(host.name());
		hostDetailVo.setDescription(host.description());
		hostDetailVo.setComment(host.comment());
		hostDetailVo.setAddress(host.address());
		hostDetailVo.setStatus(host.status().value());
		hostDetailVo.setPowerManagementEnabled(host.powerManagement().enabled());
		hostDetailVo.setCpuName(host.cpu().name());
		hostDetailVo.setCpuCores(host.cpu().topology().cores());
		hostDetailVo.setCpuSockets(host.cpu().topology().sockets());
		hostDetailVo.setCpuThreads(host.cpu().topology().threads());
		if (host.cpu().topology().cores() == null || host.cpu().topology().sockets() == null || host.cpu().topology().threads() == null) {
			hostDetailVo.setCpuTotal(0);
		} else {
			hostDetailVo.setCpuTotal(host.cpu().topology().cores().intValue() * host.cpu().topology().sockets().intValue() * host.cpu().topology().threads().intValue());
		}
		HostUsageVo hostLastUsage = this.clustersDao.retrieveHostLastUsage(host.id());
		hostDetailVo.setHostLastUsage(hostLastUsage);
		List<HostUsageVo> hostUsageList = this.clustersDao.retrieveHostUsage(host.id());
		List<UsageVo> usageVos = new ArrayList<>();
		for (HostUsageVo hostUsageVo : hostUsageList) {
			UsageVo usageVo = new UsageVo();
			if (hostUsageVo.getCpuUsagePercent() == "" || hostUsageVo.getMemoryUsagePercent() == "") {
				usageVo.setCpuUsages(Integer.parseInt(hostUsageVo.getCpuUsagePercent()));
				usageVo.setMemoryUsages(Integer.parseInt(hostUsageVo.getMemoryUsagePercent()));
				usageVo.setUsageDate(hostUsageVo.getHistoryDatetime());
				usageVos.add(usageVo);
			}
		}
		hostDetailVo.setUsageVos(usageVos);
		List<HostNic> hostNics
				= getSysSrvHelper().findNicsFromHost(connection, host.id());
		List<NicUsageVo> hostNicsLastUsage = new ArrayList<>();
		for (HostNic hostNic : hostNics) {
			NicUsageVo nicUsageVo = this.clustersDao.retrieveHostNicUsage(hostNic.id());
			if (nicUsageVo == null)
				continue;
			nicUsageVo.setHostInterfaceName(hostNic.name());
			if (hostNic.mac() != null) {
				nicUsageVo.setMacAddress(hostNic.mac().address());
			} else {
				nicUsageVo.setMacAddress("");
			}
			hostNicsLastUsage.add(nicUsageVo);
		}
		hostDetailVo.setHostNicsLastUsage(hostNicsLastUsage);
		setHosCpuMemory(connection, host, hostDetailVo);
		hostDetailVo.setClusterId(host.cluster().id());
		setHostVmCnt(connection, host, hostDetailVo);
		setHostDetail(connection, host, hostDetailVo);
		return hostDetailVo;
	}


	// TODO: ModelKt 로 이동
	private void setHostVmCnt(Connection connection, Host host, HostDetailVo hostDetailVo) {
		int cnt = 0;
		int upCnt = 0;
		int downCnt = 0;
		List<Vm> vms
				= getSysSrvHelper().findAllVms(connection, "host = " + host.name());
		for (Vm vm : vms) {
			if ("up".equalsIgnoreCase(vm.status().value())) {
				upCnt++;
			} else if ("down".equalsIgnoreCase(vm.status().value())) {
				downCnt++;
			}
			cnt++;
		}
		hostDetailVo.setVmsCnt(cnt);
		hostDetailVo.setVmsUpCnt(upCnt);
		hostDetailVo.setVmsDownCnt(downCnt);
	}


	@Override
	public VmSummaryVo getVmInfo(Connection connection, Vm vm) {
		VmSummaryVo vmSummary = new VmSummaryVo();
		vmSummary.setId(vm.id());
		vmSummary.setName(vm.name());
		vmSummary.setComment(vm.comment());
		vmSummary.setDescription(vm.description());
		vmSummary.setStatus(vm.status().value());
		if (vm.host() != null && !"".equals(vm.host().id())) {
			Host host =
					getSysSrvHelper().findHost(connection, vm.host().id());
			vmSummary.setHostId(host.id());
			vmSummary.setHostName(host.name());
		}
		VmUsageVo vmLastUsage = this.clustersDao.retrieveVmUsage(vm.id());
		vmSummary.setVmLastUsage(vmLastUsage);
		List<Nic> vmNics =
				getSysSrvHelper().findNicsFromVm(connection, vm.id());
		List<NicUsageVo> vmNicsLastUsage = new ArrayList<>();
		for (Nic vmNic : vmNics) {
			NicUsageVo nicUsageVo = this.clustersDao.retrieveVmNicUsage(vmNic.id());
			if (nicUsageVo == null) {
				nicUsageVo = new NicUsageVo();
				nicUsageVo.setVmInterfaceName(vmNic.name());
				nicUsageVo.setReceiveRatePercent("0");
				nicUsageVo.setReceivedTotalByte("0");
				nicUsageVo.setTransmitRatePercent("0");
				nicUsageVo.setTransmittedTotalByte("0");
			} else {
				nicUsageVo.setVmInterfaceName(vmNic.name());
			}
			if (vmNic.mac() != null) {
				nicUsageVo.setMacAddress(vmNic.mac().address());
			} else {
				nicUsageVo.setMacAddress("");
			}
			if (vmNic.reportedDevicesPresent() && (vmNic.reportedDevices().get(0)).ips() != null && (vmNic.reportedDevices().get(0)).ips().size() != 0) {
				List<Ip> ips = (vmNic.reportedDevices().get(0)).ips();
				vmSummary.setAddress((ips.get(0)).address());
			} else {
				vmSummary.setAddress("");
			}
			vmNicsLastUsage.add(nicUsageVo);
		}
		vmSummary.setVmNicsLastUsage(vmNicsLastUsage);
		return vmSummary;
	}

	@Override
	public List<String> retrieveFanceAgentType() {
		Connection connection = this.adminConnectionService.getConnection();
		List<String> result = new ArrayList<>();
		return result;
	}

	@Async("karajanTaskExecutor")
	@Override
	public Boolean connectTestFenceAgent(FenceAgentVo fenceAgentVo) {
		Connection connection = this.adminConnectionService.getConnection();
		List<Host> hosts =
				getSysSrvHelper().findAllHosts(connection, "");
		if (hosts.size() == 0) return false;
		HostService hostService
				= getSysSrvHelper().srvHost(connection, hosts.get(0).id());
		AgentBuilder agentBuilder = new AgentBuilder();
		agentBuilder.address(fenceAgentVo.getAddress());
		agentBuilder.username(fenceAgentVo.getUsername());
		agentBuilder.password(fenceAgentVo.getPassword());
		agentBuilder.type(fenceAgentVo.getType());
		agentBuilder.encryptOptions(false);
		agentBuilder.order(1);
		Agent agent
				= getSysSrvHelper().addFenceAgent(connection, hosts.get(0).id(), agentBuilder.build());
		String status
				= (hostService.fence().fenceType(FenceType.START.name()).send()).powerManagement().status().value();
		return agent != null;
	}


	@Async("karajanTaskExecutor")
	@Override
	public void shutdownHost(List<HostVo> hosts) {
		Connection connection = this.adminConnectionService.getConnection();
		MessageVo message = new MessageVo();
		for (HostVo hostVo : hosts) {
			HostService hostService =
					getSysSrvHelper().srvHost(connection, hostVo.getId());
			Host host =
					getSysSrvHelper().findHost(connection, hostVo.getId());
			try {
				if (!host.powerManagement().enabled()) {
					Thread.sleep(500L);
					message.setText("전원관리가 활성화 되어 있지 않아 호스트를 정지할 수 없습니다.("+ host.name() + ")");
					message.setStyle("warning");
					this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
					continue;
				}
				if (host.status() != HostStatus.MAINTENANCE)
					hostService.deactivate().send();
				do {
					Thread.sleep(3000L);
					host = (hostService.get().send()).host();
				} while (host.status() != HostStatus.MAINTENANCE);
				message.setText("호스트 유지보수 모드 완료("+ host.name() + ")");
				message.setStyle("success");
				this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
				this.websocketService.sendMessage("/topic/migrateVm", (new Gson()).toJson(message));
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getLocalizedMessage());
				message.setText("호스트 유지보수 모드 실패("+ host.name() + ")");
				message.setStyle("error");
				this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
				try {
					Thread.sleep(300L);
				} catch (InterruptedException interruptedException) {}
				message.setText("호스트 유지보수 모드 전환 실패하여 호스트를 정지할 수 없습니다.("+ host.name() + ")");
				message.setStyle("warning");
				this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
				continue;
			}
			try {
				Host item;
				(hostService.fence().fenceType(FenceType.STOP.name()).send()).powerManagement();
				do {
					Thread.sleep(3000L);
					item = (hostService.get().send()).host();
				} while (item.status() != HostStatus.DOWN);
				message.setText("호스트 정지 완료("+ host.name() + ")");
				message.setStyle("success");
				this.websocketService.sendMessage("/topic/migrateVm", (new Gson()).toJson(message));
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getLocalizedMessage());
				message.setText("호스트 정지 실패("+ host.name() + ")");
				message.setStyle("error");
			}
			this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
		}
	}


	@Override
	public List<DashboardTopVo> retrieveHostsTop(List<HostDetailVo> totalHosts) {
		List<DashboardTopVo> data = new ArrayList<>();
		Map<String, Integer> hostCpuPraramMap = new HashMap<>();
		Map<String, Integer> hostMemoryPraramMap = new HashMap<>();
		int index = 0;
		int i;
		for (i = 0; i < totalHosts.size(); i++) {
			hostCpuPraramMap.put((totalHosts.get(i)).getName(), Integer.parseInt((totalHosts.get(i)).getHostLastUsage().getCpuUsagePercent()));
			hostMemoryPraramMap.put((totalHosts.get(i)).getName(), Integer.parseInt((totalHosts.get(i)).getHostLastUsage().getMemoryUsagePercent()));
		}
		List<String> hostCpuKeyList = new ArrayList<>(hostCpuPraramMap.keySet());
		List<String> hostCpuValList = new ArrayList<>();
		List<String> hostMemoryKeyList = new ArrayList<>(hostMemoryPraramMap.keySet());
		List<String> hostMemoryValList = new ArrayList<>();
		Collections.sort(hostCpuKeyList, (o1, o2) -> (hostCpuPraramMap.get(o2)).compareTo(hostCpuPraramMap.get(o1)));
		Collections.sort(hostMemoryKeyList, (o1, o2) -> (hostMemoryPraramMap.get(o2)).compareTo(hostMemoryPraramMap.get(o1)));
		for (String key : hostCpuKeyList) {
			hostCpuValList.add(index, (hostCpuPraramMap.get(key)).toString());
			index++;
		}
		index = 0;
		for (String key : hostMemoryKeyList) {
			hostMemoryValList.add(index, (hostMemoryPraramMap.get(key)).toString());
			index++;
		}
		for (i = hostCpuKeyList.size(); i < 3; i++) {
			hostCpuKeyList.add(index, "N/A");
			hostCpuValList.add(index, "0");
			hostMemoryKeyList.add(index, "N/A");
			hostMemoryValList.add(index, "0");
		}
		DashboardTopVo dashboardTopVo = new DashboardTopVo(
			new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
			hostCpuKeyList, hostCpuValList, hostMemoryKeyList, hostMemoryValList
		);
		data.add(dashboardTopVo);
		return data;
	}
}
