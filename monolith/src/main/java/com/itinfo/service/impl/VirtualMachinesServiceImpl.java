package com.itinfo.service.impl;


import com.itinfo.SystemServiceHelper;
import com.itinfo.dao.ComputingDao;
import com.itinfo.model.*;
import com.itinfo.service.VirtualMachinesService;
import com.itinfo.service.engine.AdminConnectionService;
import com.itinfo.service.engine.ConnectionService;
import com.itinfo.service.engine.KarajanService;
import com.itinfo.service.engine.WebsocketService;
import com.itinfo.service.consolidation.GreedyHost;

import com.google.gson.Gson;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.builders.BootBuilder;
import org.ovirt.engine.sdk4.builders.Builders;
import org.ovirt.engine.sdk4.builders.CdromBuilder;
import org.ovirt.engine.sdk4.builders.CpuBuilder;
import org.ovirt.engine.sdk4.builders.CpuProfileBuilder;
import org.ovirt.engine.sdk4.builders.CpuTopologyBuilder;
import org.ovirt.engine.sdk4.builders.DiskBuilder;
import org.ovirt.engine.sdk4.builders.DisplayBuilder;
import org.ovirt.engine.sdk4.builders.FileBuilder;
import org.ovirt.engine.sdk4.builders.HighAvailabilityBuilder;
import org.ovirt.engine.sdk4.builders.HostBuilder;
import org.ovirt.engine.sdk4.builders.HostStorageBuilder;
import org.ovirt.engine.sdk4.builders.InitializationBuilder;
import org.ovirt.engine.sdk4.builders.InstanceTypeBuilder;
import org.ovirt.engine.sdk4.builders.IoBuilder;
import org.ovirt.engine.sdk4.builders.LogicalUnitBuilder;
import org.ovirt.engine.sdk4.builders.MacBuilder;
import org.ovirt.engine.sdk4.builders.MemoryPolicyBuilder;
import org.ovirt.engine.sdk4.builders.MethodBuilder;
import org.ovirt.engine.sdk4.builders.MigrationOptionsBuilder;
import org.ovirt.engine.sdk4.builders.NicBuilder;
import org.ovirt.engine.sdk4.builders.OperatingSystemBuilder;
import org.ovirt.engine.sdk4.builders.RateBuilder;
import org.ovirt.engine.sdk4.builders.ReportedDeviceBuilder;
import org.ovirt.engine.sdk4.builders.RngDeviceBuilder;
import org.ovirt.engine.sdk4.builders.SsoBuilder;
import org.ovirt.engine.sdk4.builders.StorageDomainLeaseBuilder;
import org.ovirt.engine.sdk4.builders.VirtioScsiBuilder;
import org.ovirt.engine.sdk4.builders.VmBuilder;
import org.ovirt.engine.sdk4.builders.VmPlacementPolicyBuilder;
import org.ovirt.engine.sdk4.builders.VnicProfileBuilder;
import org.ovirt.engine.sdk4.services.ClusterService;
import org.ovirt.engine.sdk4.services.DiskAttachmentService;
import org.ovirt.engine.sdk4.services.DiskAttachmentsService;
import org.ovirt.engine.sdk4.services.DiskService;
import org.ovirt.engine.sdk4.services.HostService;
import org.ovirt.engine.sdk4.services.HostStorageService;
import org.ovirt.engine.sdk4.services.InstanceTypesService;
import org.ovirt.engine.sdk4.services.SnapshotService;
import org.ovirt.engine.sdk4.services.StorageDomainService;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.services.TemplateService;
import org.ovirt.engine.sdk4.services.VmCdromService;
import org.ovirt.engine.sdk4.services.VmCdromsService;
import org.ovirt.engine.sdk4.services.VmGraphicsConsolesService;
import org.ovirt.engine.sdk4.services.VmNicService;
import org.ovirt.engine.sdk4.services.VmNicsService;
import org.ovirt.engine.sdk4.services.VmService;
import org.ovirt.engine.sdk4.services.VmsService;
import org.ovirt.engine.sdk4.services.VnicProfileService;
import org.ovirt.engine.sdk4.types.BootDevice;
import org.ovirt.engine.sdk4.types.Cdrom;
import org.ovirt.engine.sdk4.types.Cluster;
import org.ovirt.engine.sdk4.types.CpuProfile;
import org.ovirt.engine.sdk4.types.Disk;
import org.ovirt.engine.sdk4.types.DiskAttachment;
import org.ovirt.engine.sdk4.types.DiskFormat;
import org.ovirt.engine.sdk4.types.DiskInterface;
import org.ovirt.engine.sdk4.types.DiskProfile;
import org.ovirt.engine.sdk4.types.DisplayType;
import org.ovirt.engine.sdk4.types.Event;
import org.ovirt.engine.sdk4.types.File;
import org.ovirt.engine.sdk4.types.GraphicsConsole;
import org.ovirt.engine.sdk4.types.Host;
import org.ovirt.engine.sdk4.types.HostStorage;
import org.ovirt.engine.sdk4.types.InheritableBoolean;
import org.ovirt.engine.sdk4.types.InstanceType;
import org.ovirt.engine.sdk4.types.Ip;
import org.ovirt.engine.sdk4.types.Network;
import org.ovirt.engine.sdk4.types.NetworkAttachment;
import org.ovirt.engine.sdk4.types.Nic;
import org.ovirt.engine.sdk4.types.OperatingSystemInfo;
import org.ovirt.engine.sdk4.types.ReportedDevice;
import org.ovirt.engine.sdk4.types.RngSource;
import org.ovirt.engine.sdk4.types.Snapshot;
import org.ovirt.engine.sdk4.types.SsoMethod;
import org.ovirt.engine.sdk4.types.StorageDomain;
import org.ovirt.engine.sdk4.types.StorageType;
import org.ovirt.engine.sdk4.types.Template;
import org.ovirt.engine.sdk4.types.Vm;
import org.ovirt.engine.sdk4.types.VmAffinity;
import org.ovirt.engine.sdk4.types.VmStatus;
import org.ovirt.engine.sdk4.types.VmStorageErrorResumeBehaviour;
import org.ovirt.engine.sdk4.types.VmType;
import org.ovirt.engine.sdk4.types.VnicProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@NoArgsConstructor
public class VirtualMachinesServiceImpl implements VirtualMachinesService {
	@Autowired private ConnectionService connectionService;
	@Autowired private AdminConnectionService adminConnectionService;
	@Autowired private KarajanService karajanService;
	@Autowired private GreedyHost greedyHost;
	@Autowired private ComputingDao computingDao;
	@Autowired private WebsocketService websocketService;

	@Async("karajanTaskExecutor")
	public void startVm(List<VmVo> vms) {
		Connection connection = this.adminConnectionService.getConnection();

		for (int i = 0; i < vms.size(); i++) {
			VmVo vm = vms.get(i);
			VmService vmService
					= SystemServiceHelper.getInstance().srvVm(connection, vm.getId());
			try {
				if (vmService.get().send().vm().initializationPresent())
					vmService.start().useCloudInit(true).send();
				else
					vmService.start().send();

			} catch (Exception e) {
				Gson gson = new Gson();
				if (e.getMessage().indexOf("There are no hosts to use. Check that the cluster contains at least one host in Up state.") > 0) {
					VmVo result = new VmVo();
					result.setId(vm.getId());
					result.setStatus("down");
					MessageVo message
							= MessageVo.createMessage(MessageType.VIRTUAL_MACHINE_START, false, vm.getName(), "이 사용할 호스트가 없습니다. 클러스터에 최소 하나의 Up 상태의 호스트가 있는지 확인하십시오.");
					websocketService.sendMessage("/topic/vms", gson.toJson(result));
					websocketService.sendMessage("/topic/notify", gson.toJson(message));
					vms.remove(i);
				} else if (e.getMessage().indexOf("did not satisfy internal filter Memory because its available memory is too low") > 0) {
					log.error(e.getLocalizedMessage());
					VmVo result = new VmVo();
					result.setId(vm.getId());
					result.setStatus("down");
					MessageVo message
							= MessageVo.createMessage(MessageType.VIRTUAL_MACHINE_START, false, vm.getName(), "을 실행 할 수 없습니다. 가상 머신을 실행하기 위한 여유 메모리가 충분하지 않습니다.");
					websocketService.sendMessage("/topic/vms", gson.toJson(result));
					websocketService.sendMessage("/topic/notify", gson.toJson(message));
					vms.remove(i);
				}
			}
		}

		try {
			for (VmVo vm : vms) {
				Vm item;
				do {
					Thread.sleep(5000L);
					item = SystemServiceHelper.getInstance().findVm(connection, vm.getId());
				} while (item.status() != VmStatus.UP);
				Gson gson = new Gson();
				VmVo result = new VmVo();
				result.setId(item.id());
				result.setName(item.name());
				result.setStatus(item.status().value());
				MessageVo message
						= MessageVo.createMessage(MessageType.VIRTUAL_MACHINE_START, true, item.name(), "");
				websocketService.sendMessage("/topic/vms", gson.toJson(result));
				websocketService.sendMessage("/topic/notify", gson.toJson(message));
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	@Async("karajanTaskExecutor")
	public void stopVm(List<VmVo> vms) {
		Connection connection = this.adminConnectionService.getConnection();
		SystemService systemService = connection.systemService();
		try {
			Boolean res
					= ModelsKt.stopAllVms(vms, connection);
			for (VmVo vm : vms) {
				Vm item;
				do {
					Thread.sleep(2000L);
					item = SystemServiceHelper.getInstance().findVm(connection, vm.getId());
				} while (item.status() != VmStatus.DOWN);

				Gson gson = new Gson();
				VmVo result = new VmVo();
				result.setId(item.id());
				result.setName(item.name());
				result.setStatus(item.status().value());

				MessageVo message
						= MessageVo.createMessage(MessageType.VIRTUAL_MACHINE_STOP, true, item.name(), "");
				websocketService.sendMessage("/topic/vms", gson.toJson(result));
				websocketService.sendMessage("/topic/notify", gson.toJson(message));
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	@Override
	@Async("karajanTaskExecutor")
	public void rebootVm(List<VmVo> vms) {
		Connection connection = this.adminConnectionService.getConnection();

		try {
			Boolean res
					= ModelsKt.rebootAllVms(vms, connection);
			for (VmVo vm : vms) {
				Vm item;
				do {
					Thread.sleep(5000L);
					item = SystemServiceHelper.getInstance().findVm(connection, vm.getId());
				} while (item.status() != VmStatus.UP);

				Gson gson = new Gson();
				VmVo result
						= ModelsKt.toVmVoAfterReboot(item);
				websocketService.sendMessage("/topic/vms", gson.toJson(result));
				MessageVo message
					= MessageVo.createMessage(MessageType.VIRTUAL_MACHINE_REBOOT, true, item.name(), "");
				websocketService.sendMessage("/topic/notify", gson.toJson(message));
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	@Override
	@Async("karajanTaskExecutor")
	public void suspendVm(List<VmVo> vms) {
		Connection connection = this.adminConnectionService.getConnection();
		SystemService systemService = connection.systemService();
		try {
			for (VmVo vm : vms)
				systemService.vmsService().vmService(vm.getId()).suspend().send();
			for (VmVo vm : vms) {
				Vm item;
				do {
					Thread.sleep(5000L);
					item = systemService.vmsService().vmService(vm.getId()).get().send().vm();
				} while (item.status() != VmStatus.SUSPENDED);
				Gson gson = new Gson();
				VmVo result = new VmVo();
				result.setId(item.id());
				result.setName(item.name());
				result.setStatus(item.status().value());
				websocketService.sendMessage("/topic/vms", gson.toJson(result));
				MessageVo message = new MessageVo(
						"가상머신 일시정지",
						"가상머신 일시정지 완료(" + item.name() + ")",
						"success"
				);
				websocketService.sendMessage("/topic/notify", gson.toJson(message));
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	@Async("karajanTaskExecutor")
	public void removeVm(List<VmVo> vms) {
		Connection connection = this.adminConnectionService.getConnection();
		SystemService systemService = connection.systemService();
		try {
			for (VmVo vm : vms)
				systemService.vmsService().vmService(vm.getId()).remove().detachOnly(!vm.getDiskDetach()).send();
			for (VmVo vm : vms) {
				do {
					Thread.sleep(5000L);
				} while (((VmsService.ListResponse)systemService.vmsService().list().search(" id=" + vm.getId()).send()).vms().size() != 0);
				Gson gson = new Gson();
				vm.setStatus("removed");
				websocketService.sendMessage("/topic/vms", gson.toJson(vm));
				MessageVo message = new MessageVo(
						"가상머신 삭제",
						"가상머신 삭제 완료(" + vm.getName() + ")",
						"success"
				);
				websocketService.sendMessage("/topic/notify", gson.toJson(message));
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public List<VmVo> retrieveVmsAll() {
		Connection connection = this.connectionService.getConnection();
		SystemService systemService = connection.systemService();
		VmsService vmsService = systemService.vmsService();
		List<Vm> vmList
				= vmsService.list().send().vms();
		Date date = new Date(System.currentTimeMillis());
		List<VmVo> vms = new ArrayList<>();
		try {
			vmList.forEach(item -> {
				VmService vmService
						= systemService.vmsService().vmService(item.id());
				VmVo vm
						= ModelsKt.toVmVoAfterReboot(item);
				List<Nic> nics
						= vmService.nicsService().list().send().nics();
				String ips = "";
				List<String> nicIds = new ArrayList<>();
				for (Nic nic : nics) {
					nicIds.add(nic.id());
					if (nic.reportedDevicesPresent())
						for (ReportedDevice device : nic.reportedDevices()) {
							if (device.ipsPresent())
								for (Ip ip : device.ips())
									ips += ip.address() + " /";
						}
				}
				vm.setIpAddress(ips);
				vm.setStatus(item.status().value());
				vm.setNextRunConfigurationExists(item.nextRunConfigurationExists());
				if (item.display() == null) {
					vm.setHeadlessMode(true);
					vm.setGraphicProtocol("없음");
				} else {
					vm.setHeadlessMode(false);
					vm.setGraphicProtocol(item.display().type().toString());
				}
				if (item.status().value().equals("up") && item.startTimePresent()) {
					vm.setStartTime(""+(date.getTime() - item.startTime().getTime()) / 60000L);
				} else if (item.status().value().equals("up") && item.creationTimePresent()) {
					vm.setStartTime(""+(date.getTime() - item.creationTime().getTime()) / 60000L);
				}
				if (item.host() != null) {
					vm.setHostId(item.host().id());
					vm.setHostName(systemService.hostsService().hostService(item.host().id()).get().send().host().name());
				}
				vm.setClusterId(item.cluster().id());
				VmUsageVo usage = this.computingDao.retrieveVmUsageOne(item.id());
				List<List<String>> cpuUsage = new ArrayList<>();
				List<List<String>> memoryUsage = new ArrayList<>();
				List<String> cpu = new ArrayList<>();
				List<String> memory = new ArrayList<>();
				if (usage != null) {
					cpu.add(usage.getHistoryDatetime());
					cpu.add(String.valueOf(usage.getCpuUsagePercent()));
					memory.add(usage.getHistoryDatetime());
					memory.add(String.valueOf(usage.getMemoryUsagePercent()));
				} else {
					cpu.add("0");
					cpu.add("0");
					memory.add("0");
					memory.add("0");
				}
				cpuUsage.add(cpu);
				memoryUsage.add(memory);
				vm.setCpuUsage(cpuUsage);
				vm.setMemoryUsage(memoryUsage);
				List<List<String>> networkUsages = new ArrayList<>();
				List<String> network = new ArrayList<>();
				if (nicIds.size() > 0) {
					VmNetworkUsageVo networkUsage = this.computingDao.retrieveVmNetworkUsageOne(nicIds);
					if (networkUsage != null) {
						if (networkUsage.getTransmitRatePercent() < networkUsage.getReceiveRatePercent()) {
							network.add(networkUsage.getHistoryDatetime());
							network.add(String.valueOf(networkUsage.getReceiveRatePercent()));
						} else {
							network.add(networkUsage.getHistoryDatetime());
							network.add(String.valueOf(networkUsage.getTransmitRatePercent()));
						}
					} else {
						network.add("0");
						network.add("0");
					}
					networkUsages.add(network);
					vm.setNetworkUsage(networkUsages);
				} else {
					network.add("0");
					network.add("0");
					networkUsages.add(network);
					vm.setNetworkUsage(networkUsages);
				}
				vm.setDiskSize(((DiskAttachmentsService.ListResponse)vmService.diskAttachmentsService().list().send()).attachments().size());
				VmCdromService vmCdromService = vmService.cdromsService().cdromService(((Cdrom)((VmCdromsService.ListResponse)vmService.cdromsService().list().send()).cdroms().get(0)).id());
				if (((VmCdromService.GetResponse)vmCdromService.get().current(Boolean.valueOf(true)).send()).cdrom().filePresent())
					vm.setDisc(((VmCdromService.GetResponse)vmCdromService.get().current(Boolean.valueOf(true)).send()).cdrom().file().id());
				VmSystemVo vmSystem = new VmSystemVo();
				vmSystem.setDefinedMemory((item.memoryAsLong().longValue() / 1024L / 1024L) + " MB");
				vmSystem.setGuaranteedMemory((item.memoryPolicy().guaranteedAsLong().longValue() / 1024L / 1024L) + " MB");
				vmSystem.setMaxMemoryPolicy((item.memoryPolicy().maxAsLong().longValue() / 1024L / 1024L) + " MB");
				vmSystem.setVirtualSockets(item.cpu().topology().socketsAsInteger().intValue());
				vmSystem.setCoresPerVirtualSocket(item.cpu().topology().coresAsInteger().intValue());
				vmSystem.setThreadsPerCore(item.cpu().topology().threadsAsInteger().intValue());
				vmSystem.setTotalVirtualCpus(vmSystem.getVirtualSockets() * vmSystem.getCoresPerVirtualSocket() * vmSystem.getThreadsPerCore());
				vm.setVmSystem(vmSystem);
				vms.add(vm);
			});
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return vms;
	}

	public List<HostVo> retrieveVmsHosts() {
		Connection connection = this.connectionService.getConnection();
		SystemService systemService = connection.systemService();
		List<Host> hostItems
				= systemService.hostsService().list().send().hosts();
		List<HostVo> hosts
				= ModelsKt.toHostVos(hostItems, systemService);
		return hosts;
	}

	public List<ClusterVo> retrieveVmsClusters() {
		Connection connection = this.connectionService.getConnection();
		SystemService systemService = connection.systemService();
		List<Cluster> clusterItems
				= systemService.clustersService().list().send().clusters();
		List<ClusterVo> clusters
				= ModelsKt.toClusterVos(clusterItems);
		return clusters;
	}

	public List<VmVo> retrieveVms(String status) {
		Connection connection = this.connectionService.getConnection();
		SystemService systemService = connection.systemService();
		VmsService vmsService = systemService.vmsService();
		List<Vm> vmList
				= vmsService.list().send().vms();
		Date date = new Date();
		List<VmVo> vms = new ArrayList<>();
		try {
			vmList.forEach(item -> {
				if (status.equalsIgnoreCase(VmStatus.UP.value())) {
					if (item.status() == VmStatus.UP) {
						VmService vmService = systemService.vmsService().vmService(item.id());
						VmVo vm = new VmVo();
						vm.setId(item.id());
						vm.setName(item.name());
						vm.setComment(item.comment());
						vm.setUse(item.description());
						List<Nic> nics
								= vmService.nicsService().list().send().nics();
						String ips = "";
						List<String> nicIds = new ArrayList<>();
						for (Nic nic : nics) {
							nicIds.add(nic.id());
							if (nic.reportedDevicesPresent())
								for (ReportedDevice device : nic.reportedDevices()) {
									if (device.ipsPresent())
										for (Ip ip : device.ips())
											ips = ips + ip.address() + " ";
								}
						}
						vm.setIpAddress(ips);
						vm.setStatus(item.status().value());
						vm.setNextRunConfigurationExists(item.nextRunConfigurationExists());
						if (item.display() == null) {
							vm.setHeadlessMode(true);
							vm.setGraphicProtocol("없음");
						} else {
							vm.setHeadlessMode(false);
							vm.setGraphicProtocol(item.display().type().toString());
						}
						if (item.status().value().equals("up") && item.startTimePresent()) {
							vm.setStartTime(String.valueOf((date.getTime() - item.startTime().getTime()) / 60000L));
						} else if (item.status().value().equals("up") && item.creationTimePresent()) {
							vm.setStartTime(String.valueOf((date.getTime() - item.creationTime().getTime()) / 60000L));
						}
						if (item.host() != null)
							vm.setHostId(item.host().id());
						vm.setClusterId(item.cluster().id());
						VmUsageVo usage = this.computingDao.retrieveVmUsageOne(item.id());
						List<List<String>> cpuUsage = new ArrayList<>();
						List<List<String>> memoryUsage = new ArrayList<>();
						List<String> cpu = new ArrayList<>();
						List<String> memory = new ArrayList<>();
						if (usage != null) {
							cpu.add(usage.getHistoryDatetime());
							cpu.add(String.valueOf(usage.getCpuUsagePercent()));
							memory.add(usage.getHistoryDatetime());
							memory.add(String.valueOf(usage.getMemoryUsagePercent()));
						} else {
							cpu.add("0");
							cpu.add("0");
							memory.add("0");
							memory.add("0");
						}
						cpuUsage.add(cpu);
						memoryUsage.add(memory);
						vm.setCpuUsage(cpuUsage);
						vm.setMemoryUsage(memoryUsage);
						List<List<String>> networkUsages = new ArrayList<>();
						List<String> network = new ArrayList<>();
						if (nicIds.size() > 0) {
							VmNetworkUsageVo networkUsage = this.computingDao.retrieveVmNetworkUsageOne(nicIds);
							if (networkUsage != null) {
								network.add(networkUsage.getHistoryDatetime());
								network.add(String.valueOf(networkUsage.getTransmitRatePercent()));
							} else {
								network.add("0");
								network.add("0");
							}
							networkUsages.add(network);
							vm.setNetworkUsage(networkUsages);
						} else {
							network.add("0");
							network.add("0");
							networkUsages.add(network);
							vm.setNetworkUsage(networkUsages);
						}
						vm.setDiskSize(vmsService.vmService(item.id()).diskAttachmentsService().list().send().attachments().size());
						VmCdromService vmCdromService
								= vmService.cdromsService().cdromService(vmService.cdromsService().list().send().cdroms().get(0).id());
						if (vmCdromService.get().current(true).send().cdrom().filePresent())
							vm.setDisc(vmCdromService.get().current(true).send().cdrom().file().id());
						vms.add(vm);
					}
				} else if (item.status() != VmStatus.UP) {
					VmVo vm = new VmVo();
					vm.setId(item.id());
					vm.setName(item.name());
					vm.setComment(item.comment());
					vm.setUse(item.description());
					List<Nic> nics
							= systemService.vmsService().vmService(item.id()).nicsService().list().send().nics();
					String ips = "";
					List<String> nicIds = new ArrayList<>();
					for (Nic nic : nics) {
						nicIds.add(nic.id());
						if (nic.reportedDevicesPresent())
							for (ReportedDevice device : nic.reportedDevices()) {
								if (device.ipsPresent())
									for (Ip ip : device.ips())
										ips = ips + ip.address() + " ";
							}
					}
					vm.setIpAddress(ips);
					vm.setStatus(item.status().value());
					vm.setNextRunConfigurationExists(item.nextRunConfigurationExists());
					if (item.display() == null) {
						vm.setGraphicProtocol("없음");
					} else {
						vm.setGraphicProtocol(item.display().type().toString());
					}
					if (item.status().value().equals("up"))
						vm.setStartTime(String.valueOf((date.getTime() - item.startTime().getTime()) / 60000L));
					if (item.host() != null)
						vm.setHostId(item.host().id());
					vm.setClusterId(item.cluster().id());
					VmUsageVo usage = this.computingDao.retrieveVmUsageOne(item.id());
					List<List<String>> cpuUsage = new ArrayList<>();
					List<List<String>> memoryUsage = new ArrayList<>();
					List<String> cpu = new ArrayList<>();
					List<String> memory = new ArrayList<>();
					if (usage != null) {
						cpu.add(usage.getHistoryDatetime());
						cpu.add(String.valueOf(usage.getCpuUsagePercent()));
						memory.add(usage.getHistoryDatetime());
						memory.add(String.valueOf(usage.getMemoryUsagePercent()));
					} else {
						cpu.add("0");
						cpu.add("0");
						memory.add("0");
						memory.add("0");
					}
					cpuUsage.add(cpu);
					memoryUsage.add(memory);
					vm.setCpuUsage(cpuUsage);
					vm.setMemoryUsage(memoryUsage);
					List<List<String>> networkUsages = new ArrayList<>();
					List<String> network = new ArrayList<>();
					if (nicIds.size() > 0) {
						VmNetworkUsageVo networkUsage
								= this.computingDao.retrieveVmNetworkUsageOne(nicIds);
						if (networkUsage != null) {
							network.add(networkUsage.getHistoryDatetime());
							network.add(String.valueOf(networkUsage.getTransmitRatePercent()));
						} else {
							network.add("0");
							network.add("0");
						}
						networkUsages.add(network);
						vm.setNetworkUsage(networkUsages);
					} else {
						network.add("0");
						network.add("0");
						networkUsages.add(network);
						vm.setNetworkUsage(networkUsages);
					}
					vm.setDiskSize(vmsService.vmService(item.id()).diskAttachmentsService().list().send().attachments().size());
					vms.add(vm);
				}
			});
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return vms;
	}

	public VmVo retrieveVm(String id) {
		Connection connection = this.connectionService.getConnection();
		SystemService systemService = connection.systemService();
		Vm item
				= systemService.vmsService().vmService(id).get().send().vm();
		VmVo vm = new VmVo();
		try {
			vm.setId(id);
			vm.setName(item.name());
			vm.setDescription(item.description());
			vm.setComment(item.comment());
			vm.setStatus(item.status().value());
			if (item.placementPolicyPresent() && item.placementPolicy().hostsPresent())
				vm.setRunHost(systemService.hostsService().hostService(item.placementPolicy().hosts().get(0).id()).get().send().host().name());
			List<Nic> nics
					= systemService.vmsService().vmService(item.id()).nicsService().list().send().nics();
			String ips = "";

			List<String> nicIds = new ArrayList<>();
			for (Nic nic : nics) {
				nicIds.add(nic.id());
				if (nic.reportedDevicesPresent())
					for (ReportedDevice device : nic.reportedDevices()) {
						if (device.ipsPresent())
							for (Ip ip : device.ips())
								ips = ips + ip.address() + " ";
					}
			}
			vm.setIpAddress(ips);

			Date date = new Date();
			if (item.status().value().equals("up") && item.startTimePresent()) {
				vm.setStartTime(""+((date.getTime() - item.startTime().getTime()) / 60000L));
			} else if (item.status().value().equals("up") && item.creationTimePresent()) {
				vm.setStartTime(""+((date.getTime() - item.creationTime().getTime()) / 60000L));
			}
			List<VmUsageVo> usageList
					= this.computingDao.retrieveVmUsage(item.id());
			List<UsageVo> usageVos
					= ModelsKt.toUsageVos(usageList);

			List<Integer> networkUsage = new ArrayList<>();
			String networkType = null;
			if (nicIds.size() > 0) {
				List<VmNetworkUsageVo> networkUsageList = this.computingDao.retrieveVmNetworkUsage(nicIds);
				if (networkUsageList.size() > 0) {
					if (networkUsageList.get(0).getReceiveRatePercent() > networkUsageList.get(0).getTransmitRatePercent())
						networkType = (networkUsageList.get(0).getReceiveRatePercent() > networkUsageList.get(0).getTransmitRatePercent())
								? "Receive"
								: "Transmit";
				} else {
					networkType = "undefined";
				}

				if ("Receive".equals(networkType)) {
					for (int i = 0; i < networkUsageList.size(); i++)
						usageVos.get(i).setNetworkUsages(networkUsageList.get(i).getReceiveRatePercent());
				} else {
					for (int i = 0; i < networkUsageList.size(); i++)
						usageVos.get(i).setNetworkUsages(networkUsageList.get(i).getTransmitRatePercent());
				}
			}
			vm.setUsageVos(usageVos);
			vm.setVmSystem(retrieveVmSystem(id));
			List<OperatingSystemInfo> osInfoList
					= systemService.operatingSystemsService().list().send().operatingSystem();
			for (OperatingSystemInfo osInfo : osInfoList) {
				if (osInfo.name().equals(item.os().type()))
					vm.setOs(osInfo.description());
			}
			vm.setVmNics(retrieveVmNics(id, vm));
			vm.setEvents(retrieveVmEvents(id));
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return vm;
	}

	public VmSystemVo retrieveVmSystem(String id) {
		Connection connection = connectionService.getConnection();
		SystemService systemService = connection.systemService();
		Vm vm
				= systemService.vmsService().vmService(id).get().send().vm();
		VmSystemVo vmSystem = ModelsKt.toVmSystemVo(vm);
		return vmSystem;
	}

	public List<VmNicVo> retrieveVmNics(String id) {
		Connection connection = this.connectionService.getConnection();
		SystemService systemService = connection.systemService();
		List<Nic> nicItems
				= systemService.vmsService().vmService(id).nicsService().list().send().nics();
		List<VmNicVo> nics
				= ModelsKt.toVmNicVos(nicItems, systemService);
		return nics;
	}

	public List<VmNicVo> retrieveVmNics(String id, VmVo vm) {
		Connection connection = this.connectionService.getConnection();
		SystemService systemService = connection.systemService();
		List<Nic> nicItems
				= systemService.vmsService().vmService(id).nicsService().list().send().nics();
		List<VmNicVo> nics = new ArrayList<>();
		if (nicItems.size() > 0) {
			for (Nic nicItem : nicItems) {
				VmNicVo nic = new VmNicVo();
				nic.setId(id);
				nic.setNicName(nicItem.name());
				nic.setNicId(nicItem.id());
				List<VmNicVo> profiles
						= ModelsKt.toVmNicVos(systemService.vnicProfilesService().list().send().profiles());
				nic.setProfileList(profiles);
				vm.setProfileList(profiles);
				nic.setInterfaceType(nicItem.interface_().value());

				if (nicItem.reportedDevicesPresent()) {
					List<Ip> ips = nicItem.reportedDevices().get(0).ips();
					if (ips.size() > 0)	nic.setIpv4((ips.get(0)).address());
					if (ips.size() > 1)	nic.setIpv6((ips.get(1)).address());
				} else {
					nic.setIpv4("해당 없음");
					nic.setIpv6("해당 없음");
				}
				if (nicItem.vnicProfile() != null) {
					VnicProfile vnicProfile
							= systemService.vnicProfilesService().profileService(nicItem.vnicProfile().id()).get().send().profile();
					Network network
							= systemService.networksService().networkService(vnicProfile.network().id()).get().send().network();
					nic.setNetworkName(network.name());
					nic.setProfileName(vnicProfile.name());
					nic.setProfileId(vnicProfile.id());
				}
				nic.setMacAddress(nicItem.mac().address());
				nic.setStatus(nicItem.linked());
				nic.setLinked(nicItem.linked());
				nic.setPlugged(nicItem.plugged());
				nics.add(nic);
			}
		} else {
			List<VmNicVo> profiles
					= ModelsKt.toVmNicVos(systemService.vnicProfilesService().list().send().profiles());
			vm.setProfileList(profiles);
		}
		return nics;
	}

	public void createVmNic(VmNicVo vmNicVo) {
		MessageVo message;
		try {
			Connection connection = this.connectionService.getConnection();
			SystemService systemService = connection.systemService();
			if (vmNicVo.getProfileId() != null && !vmNicVo.getProfileId().equals("none")) {
				systemService.vmsService().vmService(vmNicVo.getId()).nicsService().add()
						.nic(
							Builders.nic().name(vmNicVo.getNicName())
								.vnicProfile(Builders.vnicProfile().id(vmNicVo.getProfileId()))
						)
						.send();
			} else {
				systemService.vmsService().vmService(vmNicVo.getId()).nicsService().add()
						.nic(
							Builders.nic().name(vmNicVo.getNicName())
						)
						.send();
			}
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException e) {
				message = new MessageVo(
						"네트워크 인터페이스 생성",
						"네트워크 인터페이스 생성 실패("+ vmNicVo.getNicName() + ")",
						"error"
				);
			}
			message = new MessageVo(
					"네트워크 인터페이스 생성",
					"네트워크 인터페이스 생성 성공("+ vmNicVo.getNicName() + ")",
					"success"
			);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			message = new MessageVo(
					"네트워크 인터페이스 생성",
					"네트워크 인터페이스 생성 실패("+ vmNicVo.getNicName() + ")",
					"error"
			);
		}
		websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
	}

	public void updateVmNic(VmNicVo vmNicVo) {
		Connection connection = this.connectionService.getConnection();
		SystemService systemService = connection.systemService();
		VmNicService vmNicService
				= systemService.vmsService().vmService(vmNicVo.getId()).nicsService().nicService(vmNicVo.getNicId());
		VnicProfileService vnicProfileService
				= systemService.vnicProfilesService().profileService(vmNicVo.getProfileId());
		VmNicVo srcVmNicVo
				= setUpdateNicInfo(systemService, vmNicVo.getId(), vmNicVo.getNicId(), vmNicVo.getProfileId());
		Nic nic
				= systemService.vmsService().vmService(vmNicVo.getId()).nicsService().nicService(vmNicVo.getNicId()).get().send().nic();
		VnicProfileVo srcVnicProfileVo = (nic.vnicProfile() != null)
				? setUpdateNicProfileInfo(systemService, vmNicVo.getId(), vmNicVo.getNicId())
				: null;
		List<Nic> vmNics
				= systemService.vmsService().vmService(vmNicVo.getId()).nicsService().list().send().nics();
		MessageVo message;
		for (Nic vmNic : vmNics) {
			if (vmNic.id().equals(vmNicVo.getNicId())) {
				NicBuilder nicBuilder = new NicBuilder();
				if (!"".equals(vmNicVo.getNicName()) &&
						!srcVmNicVo.getNicName().equals(vmNicVo.getNicName()))
					nicBuilder.name(vmNicVo.getNicName());
				if (!"".equals(vmNicVo.getMacAddress()) &&
						!srcVmNicVo.getMacAddress().equals(vmNicVo.getMacAddress())) {
					MacBuilder macBuilder = new MacBuilder();
					macBuilder.address(vmNicVo.getMacAddress());
					nicBuilder.mac(macBuilder);
				}
				if (srcVmNicVo.getLinked() != vmNicVo.getLinked())			nicBuilder.linked(vmNicVo.getLinked());
				if (srcVmNicVo.getPlugged() != vmNicVo.getPlugged())		nicBuilder.plugged(vmNicVo.getPlugged());
				if (nic.vnicProfile() != null) {
					if (!"".equals(vmNicVo.getProfileId()) &&
							srcVnicProfileVo != null && !srcVnicProfileVo.getId().equals(vmNicVo.getProfileId())) {
						VnicProfileBuilder vnicProfileBuilder = new VnicProfileBuilder();
						vnicProfileBuilder.id(vmNicVo.getProfileId());
						nicBuilder.vnicProfile(vnicProfileBuilder);
					}
				} else {
					VnicProfileBuilder vnicProfileBuilder = new VnicProfileBuilder();
					vnicProfileBuilder.id(vmNicVo.getProfileId());
					nicBuilder.vnicProfile(vnicProfileBuilder);
				}
				if (!"".equals(vmNicVo.getIpv4()) && !"".equals(vmNicVo.getIpv6()) &&
						!"해당 없음".equals(vmNicVo.getIpv4()) &&
						!"해당 없음".equals(vmNicVo.getIpv6()) &&
						vmNic.reportedDevices().size() >= 1)
				if (!"".equals(vmNicVo.getMacAddress()) &&
						!srcVmNicVo.getMacAddress().equals(vmNicVo.getMacAddress())) {
					ReportedDeviceBuilder reportedDeviceBuilder = new ReportedDeviceBuilder();
					reportedDeviceBuilder.ips(vmNic.reportedDevices().get(0).ips());
					reportedDeviceBuilder.id(vmNic.reportedDevices().get(0).id());
					MacBuilder macBuilder = new MacBuilder();
					macBuilder.address(vmNicVo.getMacAddress());
					reportedDeviceBuilder.mac(macBuilder);
					reportedDeviceBuilder.type((vmNic.reportedDevices().get(0)).type());
					reportedDeviceBuilder.description((vmNic.reportedDevices().get(0)).description());
					reportedDeviceBuilder.name((vmNic.reportedDevices().get(0)).name());
					nicBuilder.reportedDevices(reportedDeviceBuilder);
				} else {
					nicBuilder.reportedDevices(vmNic.reportedDevices());
				}
				VmBuilder vmBuilder = new VmBuilder();
				vmBuilder.id(vmNicVo.getId());
				nicBuilder.vm(vmBuilder);
				nicBuilder.interface_(vmNic.interface_());
				try {
					vmNicService.update().nic(nicBuilder).send().nic();
					try {
						Thread.sleep(2000L);
					} catch (InterruptedException interruptedException) {}
					message = new MessageVo(
							"네트워크 인터페이스 수정",
							"네트워크 인터페이스 수정 완료"+ vmNicVo.getNicName() + ")",
							"success"
					);
				} catch (Exception e) {
					log.error(e.getLocalizedMessage());
					e.printStackTrace();
					message = new MessageVo(
							"네트워크 인터페이스 수정",
							"네트워크 인터페이스 수정 실패"+ vmNicVo.getNicName() + ")",
							"error"
					);
				}
				websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
			}
		}
	}

	public void removeVmNic(VmNicVo vmNicVo) {
		MessageVo message;

		try {
			Connection connection = this.connectionService.getConnection();
			SystemService systemService = connection.systemService();
			VmService vmService = systemService.vmsService().vmService(vmNicVo.getId());
			vmService.nicsService().nicService(vmNicVo.getNicId()).remove().send();
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException e) {
				message = new MessageVo(
						"네트워크 인터페이스 삭제",
						"네트워크 인터페이스 삭제 실패("+ vmNicVo.getNicName() + ")",
						"error"
				);
			}
			message = new MessageVo(
					"네트워크 인터페이스 삭제",
					"네트워크 인터페이스 삭제 완료("+ vmNicVo.getNicName() + ")",
					"success"
			);
		} catch (Exception e) {
			message = new MessageVo(
					"네트워크 인터페이스 삭제",
					"네트워크 인터페이스 삭제 실패("+ vmNicVo.getNicName() + ")",
					"error"
			);
		}
		websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
	}

	public VnicProfileVo setUpdateNicProfileInfo(SystemService systemService, String vmId, String nicId) {
		Nic nic
				= systemService.vmsService().vmService(vmId).nicsService().nicService(nicId).get().send().nic();
		VnicProfile vnicProfile
				= systemService.vnicProfilesService().profileService(nic.vnicProfile().id()).get().send().profile();
		VnicProfileVo vnicProfileVo
				= ModelsKt.toVnicProfileVo(vnicProfile);
		return vnicProfileVo;
	}

	public VmNicVo setUpdateNicInfo(SystemService systemService, String vmId, String nicId, String profileId) {
		Nic nic
				= systemService.vmsService().vmService(vmId).nicsService().nicService(nicId).get().send().nic();
		VmNicVo vmNicVo
				= ModelsKt.toVmNicVo(nic, vmId, nicId, profileId);
		return vmNicVo;
	}

	public List<DiskVo> retrieveDisks(String id) {
		Connection connection = this.connectionService.getConnection();
		SystemService systemService = connection.systemService();
		VmService vmService = systemService.vmsService().vmService(id);
		List<DiskAttachment> diskAttachments
				= vmService.diskAttachmentsService().list().send().attachments();
		List<DiskVo> disks = new ArrayList<>();
		diskAttachments.forEach(diskAttachment -> {
			Disk item
					= systemService.disksService().diskService(diskAttachment.disk().id()).get().send().disk();
			DiskVo disk = new DiskVo();
			disk.setId(item.id());
			disk.setName(item.name());
			disk.setAttachedTo(vmService.get().send().vm().name());
			disk.setDiskInterface(diskAttachment.interface_().name());
			disk.setAlignment("Unknown");
			disk.setType(item.storageType().name());
			disk.setDescription(item.description());
			if (item.provisionedSize() != null)
				disk.setVirtualSize(item.provisionedSize().doubleValue() / Math.pow(1024.0D, 3.0D) + " GiB");
			else
				disk.setVirtualSize(item.lunStorage().logicalUnits().get(0).size().doubleValue() / Math.pow(1024.0D, 3.0D) + " GiB");

			if (item.format() != null)	disk.setFormat(item.format().value());
			if (item.storageDomains().size() > 0)
				disk.setStorageDomainId(item.storageDomains().get(0).id());
			disk.setStatus(item.statusPresent() ? item.status().value() : "ok");
			disks.add(disk);
		});
		return disks;
	}

	public List<Map<String, Object>> retrieveVmRole(String id) {
		Connection connection = this.connectionService.getConnection();
		SystemService systemService = connection.systemService();
		VmService vmService = systemService.vmsService().vmService(id);
		List<Map<String, Object>> list = new ArrayList<>();
		(vmService.permissionsService().list().send()).permissions().forEach(permission -> {
			Map<String, Object> map = new HashMap<>();
			log.info("role name:" + systemService.rolesService().roleService(permission.role().id()).get().send().role().name());
			map.put("역할", systemService.rolesService().roleService(permission.role().id()).get().send().role().name());
			log.info(""+systemService.rolesService().roleService(permission.role().id()).get().send().role().href());
			if (permission.userPresent()) {
				log.info("user name:" + systemService.usersService().userService(permission.user().id()).get().send().user().name());
				map.put("사용자", systemService.usersService().userService(permission.user().id()).get().send().user().name());
			}
			list.add(map);
		});
		return list;
	}

	public List<VmDeviceVo> retrieveVmDevices(String id) {
		return this.computingDao.retrieveVmDevices(id);
	}

	public List<EventVo> retrieveVmEvents(String id) {
		Connection connection = this.connectionService.getConnection();
		SystemService systemService = connection.systemService();
		List<Event> items
				= systemService.eventsService().list().search("severity!=normal").send().events();
		List<EventVo> events
				= ModelsKt.toEventVos4Vm(items, id);
		return events;
	}

	public List<String[]> recommendHosts(VmCreateVo vmCreate) {
		com.itinfo.model.karajan.VmVo vm
				= ModelsKt.toVmVoKarajan(vmCreate);

		GreedyHost greedyHost = new GreedyHost();
		List<String[]> recommendHosts
				= greedyHost.recommendHosts(vmCreate.getCluster(), karajanService.getDataCenter(), vm);
		return recommendHosts;
	}

	public List<DiskVo> retrieveDisks() {
		Connection connection = this.connectionService.getConnection();
		SystemService systemService = connection.systemService();
		List<DiskVo> disks = new ArrayList<>();
		List<String> ids = new ArrayList<>();
		for (Vm vm : systemService.vmsService().list().send().vms()) {
			for (DiskAttachment diskAttachment : systemService.vmsService().vmService(vm.id()).diskAttachmentsService().list().send().attachments())
				ids.add(diskAttachment.id());
		}

		List<Disk> diskList
				= systemService.disksService().list().send().disks();
		disks.addAll(ModelsKt.toDiskVos(diskList, systemService, ids));
		return disks;
	}

	public VmCreateVo retrieveVmCreateInfo() {
		Connection connection = this.connectionService.getConnection();
		SystemService systemService = connection.systemService();
		List<Cluster> clusterItemList
				= systemService.clustersService().list().send().clusters();
		List<ClusterVo> clusters
				= ModelsKt.toClusterVos4VmCreate(clusterItemList, systemService);

		VmCreateVo vmCreate = new VmCreateVo();
		vmCreate.setClusters(clusters);

		List<TemplateVo> templates = new ArrayList<>();
		for (Template item : systemService.templatesService().list().send().templates()) {
			TemplateVo template = new TemplateVo();
			template.setId(item.id());
			template.setName(item.name());
			template.setDiskAttachmentSize(systemService.templatesService().templateService(item.id()).diskAttachmentsService().list().send().attachments().size());
			String version = (item.version().versionName() != null)
					? (item.version().versionName() + "(" + item.version().versionNumber() + ")")
					: ("(" + item.version().versionNumber() + ")");
			template.setVersion(version);
			List<Nic> nics
					= systemService.templatesService().templateService(item.id()).nicsService().list().send().nics();
			List<VmNicVo> vmNics = new ArrayList<>();
			if (nics.size() > 0)
				for (Nic nic : nics) {
					VmNicVo vmNic = new VmNicVo();
					if (nic.vnicProfile() != null)
						vmNic.setId(nic.vnicProfile().id());
					vmNic.setNicName(nic.name());
					vmNics.add(vmNic);
				}
			template.setNics(vmNics);
			templates.add(template);
		}
		vmCreate.setTemplates(templates);
		List<OperatingSystemInfo> osItemList
				= systemService.operatingSystemsService().list().send().operatingSystem();
		List<OsInfoVo> osInfoList
				= ModelsKt.toOsInfoVos(osItemList);
		vmCreate.setOperatingSystems(osInfoList);

		List<InstanceType> instanceTypeList
				= systemService.instanceTypesService().list().send().instanceType();
		List<InstanceTypeVo> instanceTypes
				= ModelsKt.toInstanceTypeVos(instanceTypeList, systemService);
		vmCreate.setInstanceTypes(instanceTypes);

		List<VnicProfile> nicItemList
				= systemService.vnicProfilesService().list().send().profiles();
		List<VmNicVo> vnicList
				= ModelsKt.toVmNicVos(nicItemList);
		vmCreate.setNics(vnicList);

		List<Host> hostList
				= systemService.hostsService().list().send().hosts();
		List<HostVo> hosts
				= ModelsKt.toHostVos(hostList, systemService);
		vmCreate.setHosts(hosts);
		vmCreate.setAffinity("migratable");

		List<StorageDomain> storageDomainList
				= systemService.storageDomainsService().list().send().storageDomains();

		List<StorageDomainVo> storageDomains = new ArrayList<>();
		List<StorageDomainVo> bootImages = new ArrayList<>();
		for (StorageDomain item : storageDomainList) {
			if ("data".equals(item.type().value()) && item.storage().type() == StorageType.NFS) {
				StorageDomainVo storageDomain = new StorageDomainVo();
				storageDomain.setId(item.id());
				storageDomain.setName(item.name());
				storageDomain.setType(item.type().value());
				storageDomains.add(storageDomain);
				continue;
			}

			if ("iso".equals(item.type().value())) {
				StorageDomainService storageDomainService
						= systemService.storageDomainsService().storageDomainService(item.id());
				vmCreate.setImageStorage(item.id());
				for (File file : storageDomainService.filesService().list().send().file()) {
					StorageDomainVo storageDomain = new StorageDomainVo();
					storageDomain.setId(file.id());
					storageDomain.setName(file.name());
					storageDomain.setType(file.type());
					bootImages.add(storageDomain);
				}
			}
		}
		vmCreate.setLeaseStorageDomains(storageDomains);
		vmCreate.setBootImages(bootImages);
		List<Disk> isoDisks
				= systemService.disksService().list().search(" disk_content_type = iso").send().disks();
		bootImages.addAll(ModelsKt.toStorageDomainVosUsingDisks(isoDisks));

		List<CpuProfile> cpuProfileList
				= systemService.cpuProfilesService().list().send().profile();
		List<CpuProfileVo> cpuProfiles
				= ModelsKt.toCpuProfileVos(cpuProfileList);
		vmCreate.setCpuProfiles(cpuProfiles);
		return vmCreate;
	}

	public VmCreateVo retrieveVmUpdateInfo(String id) {
		Connection connection = this.connectionService.getConnection();
		SystemService systemService = connection.systemService();
		VmService vmService = systemService.vmsService().vmService(id);
		Vm vm
				= vmService.get().send().vm();

		VmCreateVo vmInfo = retrieveVmCreateInfo();
		vmInfo.setId(id);
		vmInfo.setStatus(vm.status().value());
		vmInfo.setHeadlessMode(vm.display() == null);
		vmInfo.setCluster(vm.cluster().id());
		vmInfo.setTemplate(vm.template().id());
		List<OperatingSystemInfo> osItemList
				= systemService.operatingSystemsService().list().send().operatingSystem();
		for (OperatingSystemInfo item : osItemList) {
			if (vm.os().type().equals(item.name()))
				vmInfo.setOperatingSystem(item.name());
		}
		if (vm.instanceType() != null)
			vmInfo.setInstanceType(vm.instanceType().id());
		vmInfo.setType(vm.type().value());
		vmInfo.setName(vm.name());
		vmInfo.setDescription(vm.comment());
		vmInfo.setUse(vm.description());
		List<DiskVo> disks = new ArrayList<>();
		for (DiskAttachment diskAttachment : vmService.diskAttachmentsService().list().send().attachments()) {
			DiskService diskService
					= systemService.disksService().diskService(diskAttachment.disk().id());
			Disk item
					= diskService.get().send().disk();
			if (item.storageDomains().size() > 0) {
				DiskVo diskVo = ModelsKt.toDiskVo(item, systemService, diskAttachment);
				disks.add(diskVo);
				continue;
			}
			DiskVo disk = ModelsKt.toDiskVo(item, systemService, diskAttachment);
			disks.add(disk);
		}
		vmInfo.setDisks(disks);
		List<Cluster> clusterItemList
				= systemService.clustersService().list().send().clusters();
		List<ClusterVo> clusters = new ArrayList<>();

		for (int i = 0; i < clusterItemList.size(); i++) {
			ClusterVo cluster = new ClusterVo();
			cluster.setId(((Cluster)clusterItemList.get(i)).id());
			cluster.setName(((Cluster)clusterItemList.get(i)).name());
			List<NetworkVo> networkVos = new ArrayList<>();
			List<Network> networkList 
					= systemService.clustersService().clusterService(((Cluster)clusterItemList.get(i)).id()).networksService().list().send().networks();
			for (int j = 0; j < networkList.size(); j++) {
				NetworkVo networkVo1 = new NetworkVo();
				networkVo1.setId(networkList.get(j).id());
				networkVo1.setName(networkList.get(j).name());
				networkVos.add(networkVo1);
			}
			cluster.setClusterNetworkList(networkVos);
			clusters.add(cluster);
		}
		vmInfo.setClusters(clusters);
		List<Nic> nics 
				= vmService.nicsService().list().send().nics();
		if (vmService.nicsService().list().send().nics().size() > 0) {
			List<VmNicVo> vmNics = new ArrayList<>();
			for (Nic nic : nics) {
				VmNicVo vmNic = new VmNicVo();
				if (nic.vnicProfile() == null && nic.id() != null) {
					vmNic.setId(nic.id());
					vmNic.setNicName(nic.name());
					vmNic.setNetworkId(null);
					vmNic.setNetworkName(null);
				} else {
					VnicProfile vnicProfile = ((VnicProfileService.GetResponse)systemService.vnicProfilesService().profileService(nic.vnicProfile().id()).get().send()).profile();
					vmNic.setId(vnicProfile.id());
					vmNic.setNetworkId(vnicProfile.network().id());
					vmNic.setNetworkName(vnicProfile.name());
					vmNic.setNicName(nic.name());
				}
				vmNics.add(vmNic);
			}
			vmInfo.setSelectNics(vmNics);
		} else {
			List<VmNicVo> vmNics = new ArrayList<>();
			VmNicVo vmNic = new VmNicVo();
			vmNic.setId(null);
			vmNic.setNetworkName(null);
			vmNics.add(vmNic);
			vmInfo.setSelectNics(vmNics);
		}
		if (vm.display() != null) {
			vmInfo.setDisconnectAction(vm.display().disconnectAction());
			vmInfo.setSmartcard(vm.display().smartcardEnabled());
		}
		vmInfo.setMemory(vm.memoryPolicy().guaranteed());
		vmInfo.setPhysicalMemory(vm.memoryPolicy().guaranteed());
		vmInfo.setMaximumMemory(vm.memoryPolicy().max());
		vmInfo.setVirtualSockets(vm.cpu().topology().socketsAsInteger().intValue());
		vmInfo.setCoresPerVirtualSocket(vm.cpu().topology().coresAsInteger().intValue());
		vmInfo.setThreadsPerCore(vm.cpu().topology().threadsAsInteger().intValue());
		if (vm.placementPolicy().hostsPresent()) {
			vmInfo.setPickHost("targetHost");
			vmInfo.setTargetHost(((Host)vm.placementPolicy().hosts().get(0)).id());
		}
		List<HostVo> hosts = new ArrayList<>();
		for (Host item : systemService.hostsService().list().send().hosts()) {
			HostVo host = new HostVo();
			if ("up".equals(item.status().value())) {
				List<HostStorage> luns
						= systemService.hostsService().hostService(item.id()).storageService().list().send().storages();
				host.setHostId(item.id());
				host.setHostName(item.name());
				host.setClusterId(item.cluster().id());
				host.setLunVos(ModelsKt.toLunVos(luns));
				HostService hostService
						= systemService.hostsService().hostService(item.id());
				List<NetworkAttachment> networkAttachmentList
						= hostService.networkAttachmentsService().list().send().attachments();
				host.setNetAttachment(ModelsKt.toNetworkAttachmentVos(networkAttachmentList, systemService));
				hosts.add(host);
			}
		}
		vmInfo.setHosts(hosts);
		vmInfo.setAffinity(vm.placementPolicy().affinity().value());
		vmInfo.setAutoConverge(vm.migration().autoConverge().value());
		vmInfo.setCompressed(vm.migration().compressed().value());
		if (vm.migration().policyPresent()) {
			vmInfo.setCustomMigrationUsed(true);
			vmInfo.setCustomMigration(vm.migration().policy().id());
			vmInfo.setCustomMigrationDowntime(vm.migrationDowntime());
		}
		if (vm.initializationPresent()) {
			vmInfo.setUseCloudInit(vm.initializationPresent());
			vmInfo.setHostName(vm.initialization().hostName());
			vmInfo.setTimezone(vm.initialization().timezone());
			vmInfo.setCustomScript(vm.initialization().customScript());
		}
		vmInfo.setHighAvailability(vm.highAvailability().enabled());
		if (vm.leasePresent())
			vmInfo.setLeaseStorageDomain(vm.lease().storageDomain().id());
		vmInfo.setResumeBehaviour(vm.storageErrorResumeBehaviour().value());
		vmInfo.setPriority(vm.highAvailability().priority());
		vmInfo.setFirstDevice(vm.os().boot().devices().get(0).value());
		if (vm.os().boot().devices().size() > 1) {
			vmInfo.setSecondDevice(vm.os().boot().devices().get(1).value());
		} else {
			vmInfo.setSecondDevice("none");
		}
		List<Cdrom> cdroms
				= vmService.cdromsService().list().send().cdroms();
		if (cdroms.size() > 0 && cdroms.get(0).filePresent()) {
			vmInfo.setBootImageUse(true);
			vmInfo.setBootImage(cdroms.get(0).file().id());
		}
		vmInfo.setCpuShare(vm.cpuShares());
		vmInfo.setMemoryBalloon(vm.memoryPolicy().ballooning());
		vmInfo.setIoThreadsEnabled(vm.io().threads());
		log.info("setVirtioScsiEnabled:" + vm.virtioScsiPresent());
		return vmInfo;
	}

	public VmCreateVo retrieveVmCloneInfo(String vmId, String snapshotId) {
		Connection connection = this.connectionService.getConnection();
		SystemService systemService = connection.systemService();
		VmService vmService = systemService.vmsService().vmService(vmId);
		SnapshotService snapshotService
				= systemService.vmsService().vmService(vmId).snapshotsService().snapshotService(snapshotId);
		Vm vm
				= vmService.get().send().vm();
		VmCreateVo vmInfo = retrieveVmCreateInfo();
		vmInfo.setHeadlessMode(vm.display() == null);
		vmInfo.setCluster(vm.cluster().id());
		vmInfo.setTemplate(vm.template().id());
		List<OperatingSystemInfo> osItemList
				= systemService.operatingSystemsService().list().send().operatingSystem();
		for (OperatingSystemInfo item : osItemList) {
			if (vm.os().type().equals(item.name()))
				vmInfo.setOperatingSystem(item.name());
		}
		if (vm.instanceType() != null) vmInfo.setInstanceType(vm.instanceType().id());
		vmInfo.setType(vm.type().value());
		vmInfo.setUse(vm.description());
		if (vm.display() != null) {
			vmInfo.setDisconnectAction(vm.display().disconnectAction());
			vmInfo.setSmartcard(vm.display().smartcardEnabled());
		}
		vmInfo.setMemory(vm.memoryPolicy().guaranteed());
		vmInfo.setPhysicalMemory(vm.memoryPolicy().guaranteed());
		vmInfo.setMaximumMemory(vm.memoryPolicy().max());
		vmInfo.setVirtualSockets(vm.cpu().topology().socketsAsInteger().intValue());
		vmInfo.setCoresPerVirtualSocket(vm.cpu().topology().coresAsInteger().intValue());
		vmInfo.setThreadsPerCore(vm.cpu().topology().threadsAsInteger().intValue());
		if (vm.placementPolicy().hostsPresent())
			vmInfo.setTargetHost(((Host)vm.placementPolicy().hosts().get(0)).id());
		vmInfo.setAffinity(vm.placementPolicy().affinity().value());
		vmInfo.setAutoConverge(vm.migration().autoConverge().value());
		vmInfo.setCompressed(vm.migration().compressed().value());
		if (vm.migration().policyPresent()) {
			vmInfo.setCustomMigrationUsed(true);
			vmInfo.setCustomMigration(vm.migration().policy().id());
			vmInfo.setCustomMigrationDowntime(vm.migrationDowntime());
		}
		if (vm.initializationPresent()) {
			vmInfo.setUseCloudInit(vm.initializationPresent());
			vmInfo.setHostName(vm.initialization().hostName());
			vmInfo.setTimezone(vm.initialization().timezone());
			vmInfo.setCustomScript(vm.initialization().customScript());
		}
		vmInfo.setHighAvailability(vm.highAvailability().enabled());
		if (vm.leasePresent())
			vmInfo.setLeaseStorageDomain(vm.lease().storageDomain().id());
		vmInfo.setResumeBehaviour(vm.storageErrorResumeBehaviour().value());
		vmInfo.setPriority(vm.highAvailability().priority());
		vmInfo.setFirstDevice(((BootDevice)vm.os().boot().devices().get(0)).value());
		if (vm.os().boot().devices().size() > 1)
			vmInfo.setSecondDevice(vm.os().boot().devices().get(1).value());
		else
			vmInfo.setSecondDevice("none");

		List<Cdrom> cdroms
				= vmService.cdromsService().list().send().cdroms();
		if (cdroms.size() > 0 && cdroms.get(0).filePresent()) {
			vmInfo.setBootImageUse(true);
			vmInfo.setBootImage(cdroms.get(0).file().id());
		}
		vmInfo.setCpuShare(vm.cpuShares());
		vmInfo.setMemoryBalloon(vm.memoryPolicy().ballooning());
		vmInfo.setIoThreadsEnabled(vm.io().threads());
		return vmInfo;
	}

	public boolean checkDuplicateName(String name) {
		Connection connection = this.connectionService.getConnection();
		SystemService systemService = connection.systemService();
		boolean result = systemService.vmsService().list().search(" name=" + name).send().vms().size() > 0;
		return result;
	}

	public boolean checkDuplicateDiskName(DiskVo disk) {
		boolean result = false;
		return result;
	}

	@Async("karajanTaskExecutor")
	@Override
	public void createVm(VmCreateVo vmCreate) {
		Connection connection = this.adminConnectionService.getConnection();
		SystemService systemService = connection.systemService();
		VmsService vmsService = systemService.vmsService();
		Vm response = null;
		try {
			VmBuilder vmBuilder = new VmBuilder();
			try {
				vmBuilder.cluster(systemService.clustersService().clusterService(vmCreate.getCluster()).get().send().cluster());
			} catch (Exception e) {
				throw new Exception("클러스터를 찾을 수 없습니다.");
			}
			OperatingSystemBuilder operatingSystemBuilder = new OperatingSystemBuilder();
			operatingSystemBuilder.type(vmCreate.getOperatingSystem());
			vmBuilder.template(systemService.templatesService().templateService(vmCreate.getTemplate()).get().send().template());
			for (InstanceType instanceType : systemService.instanceTypesService().list().send().instanceType()) {
				if (vmCreate.getInstanceType() != null && vmCreate.getInstanceType().equalsIgnoreCase(instanceType.id()))
					vmBuilder.instanceType(instanceType);
			}
			if (vmCreate.getType() != null) {
				vmBuilder.type(VmType.fromValue(vmCreate.getType()));
			} else {
				vmBuilder.type(VmType.SERVER);
			}
			vmBuilder.name(vmCreate.getName());
			vmBuilder.comment(vmCreate.getDescription());
			if (vmCreate.getUse() == null) {
				vmBuilder.description("systemManagement");
			} else {
				vmBuilder.description(vmCreate.getUse());
			}
			DisplayBuilder displayBuilder = new DisplayBuilder();
			displayBuilder.type(DisplayType.VNC);
			displayBuilder.disconnectAction(vmCreate.getDisconnectAction());
			displayBuilder.smartcardEnabled(vmCreate.getSmartcard());
			if (vmCreate.getSingleSignOn()) {
				SsoBuilder ssoBuilder = new SsoBuilder();
				ssoBuilder.methods(new MethodBuilder[] { (new MethodBuilder()).id(SsoMethod.GUEST_AGENT) });
				vmBuilder.sso(ssoBuilder);
			}
			vmBuilder.display(displayBuilder);
			CpuBuilder cpuBuilder = new CpuBuilder();
			CpuTopologyBuilder cpuTopologyBuilder = new CpuTopologyBuilder();
			cpuTopologyBuilder.cores(vmCreate.getCoresPerVirtualSocket());
			cpuTopologyBuilder.sockets(vmCreate.getVirtualSockets());
			cpuTopologyBuilder.threads(vmCreate.getThreadsPerCore());
			cpuBuilder.topology(cpuTopologyBuilder);
			vmBuilder.cpu(cpuBuilder);
			MemoryPolicyBuilder memoryPolicy = new MemoryPolicyBuilder();
			vmBuilder.memory(vmCreate.getMemory());
			memoryPolicy.max(vmCreate.getMaximumMemory());
			vmBuilder.memoryPolicy(memoryPolicy);
			memoryPolicy.guaranteed(vmCreate.getPhysicalMemory());
			memoryPolicy.ballooning(vmCreate.getMemoryBalloon());
			vmBuilder.memoryPolicy(memoryPolicy);

			VmPlacementPolicyBuilder vmPlacementPolicyBuilder = new VmPlacementPolicyBuilder();
			if (vmCreate.getAffinity() != null)
				vmPlacementPolicyBuilder.affinity(VmAffinity.fromValue(vmCreate.getAffinity()));
			if (!"".equals(vmCreate.getRecommendHost()) || !"".equals(vmCreate.getTargetHost())) {
				List<Host> runHosts = new ArrayList<>();
				if (!"".equals(vmCreate.getRecommendHost()))
					runHosts.add(systemService.hostsService().hostService(vmCreate.getRecommendHost()).get().send().host());
				else
					runHosts.add(systemService.hostsService().hostService(vmCreate.getTargetHost()).get().send().host());
				vmPlacementPolicyBuilder.hosts(runHosts);
			}
			vmBuilder.placementPolicy(vmPlacementPolicyBuilder);
			if (vmCreate.getCustomMigrationUsed()) {
				MigrationOptionsBuilder migrationOptionBuilder = new MigrationOptionsBuilder();
				migrationOptionBuilder.autoConverge(InheritableBoolean.fromValue(vmCreate.getAutoConverge()));
				migrationOptionBuilder.compressed(InheritableBoolean.fromValue(vmCreate.getCompressed()));
				if (vmCreate.getCustomMigrationDowntimeUsed())
					vmBuilder.migrationDowntime(vmCreate.getCustomMigrationDowntime());
			}
			if (vmCreate.getUseCloudInit()) {
				InitializationBuilder initializationBuilder = new InitializationBuilder();
				initializationBuilder.hostName(vmCreate.getHostName());
				initializationBuilder.timezone(vmCreate.getTimezone());
				initializationBuilder.customScript(vmCreate.getCustomScript());
				vmBuilder.initialization(initializationBuilder);
			}
			HighAvailabilityBuilder highAvailabilityBuilder = new HighAvailabilityBuilder();
			highAvailabilityBuilder.enabled(vmCreate.getHighAvailability());
			highAvailabilityBuilder.priority(vmCreate.getPriority());
			vmBuilder.highAvailability(highAvailabilityBuilder);
			if (vmCreate.getLeaseStorageDomain() != null && !vmCreate.getLeaseStorageDomain().equals("")) {
				StorageDomainLeaseBuilder storageDomainLeaseBuilder = new StorageDomainLeaseBuilder();
				storageDomainLeaseBuilder.storageDomain(systemService.storageDomainsService().storageDomainService(vmCreate.getLeaseStorageDomain()).get().send().storageDomain());
				vmBuilder.lease(storageDomainLeaseBuilder);
			}
			vmBuilder.storageErrorResumeBehaviour(VmStorageErrorResumeBehaviour.fromValue(vmCreate.getResumeBehaviour()));
			if (!"".equals(vmCreate.getFirstDevice())) {
				List<BootDevice> bootDevices = new ArrayList<>();
				bootDevices.add(BootDevice.fromValue(vmCreate.getFirstDevice()));
				if (!vmCreate.getSecondDevice().equals("none"))
					bootDevices.add(BootDevice.fromValue(vmCreate.getSecondDevice()));
				BootBuilder bootBuilder = new BootBuilder();
				bootBuilder.devices(bootDevices);
				operatingSystemBuilder.boot(bootBuilder);
				vmBuilder.os(operatingSystemBuilder);
			}
			CpuProfileBuilder cpuProfileBuilder = new CpuProfileBuilder();
			try {
				cpuProfileBuilder.cluster(systemService.clustersService().clusterService(vmCreate.getCluster()).get().send().cluster());
			} catch (Exception e) {
				throw new Exception("클러스터를 찾을 수 없습니다.");
			}
			vmBuilder.cpuProfile(cpuProfileBuilder);
			vmBuilder.cpuShares(vmCreate.getCpuShare());
			IoBuilder ioBuilder = new IoBuilder();
			ioBuilder.threads(vmCreate.getIoThreadsEnabled());
			vmBuilder.io(ioBuilder);
			VirtioScsiBuilder virtioScsiBuilder = new VirtioScsiBuilder();
			virtioScsiBuilder.enabled(vmCreate.getVirtioScsiEnabled());
			vmBuilder.virtioScsi(virtioScsiBuilder);
			if (!"".equals(vmCreate.getDeviceSource())) {
				RngDeviceBuilder rngDeviceBuilder = new RngDeviceBuilder();
				RateBuilder rateBuilder = new RateBuilder();
				rateBuilder.period(vmCreate.getPeriodDuration());
				rateBuilder.bytes(vmCreate.getBytesPerPeriod());
				rngDeviceBuilder.source(RngSource.fromValue(vmCreate.getDeviceSource()));
				vmBuilder.rngDevice(rngDeviceBuilder);
			}
			response
					= vmsService.add().vm(vmBuilder).send().vm();
			if (systemService.vmsService().vmService(response.id()).nicsService().list().send().nics().size() == 0 &&
					vmCreate.getSelectNics() != null && vmCreate.getSelectNics().size() > 0)
				for (int i = 0; i < vmCreate.getSelectNics().size(); i++) {
					if (!"".equals(vmCreate.getSelectNics().get(i).getId()) &&
							"empty".equals(vmCreate.getSelectNics().get(i).getId())) {
						systemService.vmsService().vmService(response.id()).nicsService().add()
								.nic(Builders.nic()
										.name("nic" + (i + 1))
								).send();
					} else {
						systemService.vmsService().vmService(response.id()).nicsService().add()
								.nic(Builders.nic()
										.name("nic" + (i + 1))
										.vnicProfile(Builders.vnicProfile().id(vmCreate.getSelectNics().get(i).getId()))
								).send();
					}
				}
			TemplateService templateService = systemService.templatesService().templateService(vmCreate.getTemplate());
			Template template
					= templateService.get().send().template();
			if (template.version().versionName().length() > 0) {
				List<Nic> nics
						= templateService.nicsService().list().send().nics();
				for (int i = 0; i < nics.size(); i++)
					systemService.vmsService().vmService(response.id()).nicsService().add()
							.nic(Builders.nic()
									.name("nic" + (i + 1))
									.vnicProfile(Builders.vnicProfile().id(nics.get(i).vnicProfile().id()))
							).send();
			}
			DiskAttachmentsService diskAttachmentsService
					= vmsService.vmService(response.id()).diskAttachmentsService();
			if (vmCreate.getDisks() != null && vmCreate.getDisks().size() > 0)
				for (DiskVo diskCreate : vmCreate.getDisks()) {
					if (systemService.disksService().list().search(" id=" + diskCreate.getId()).send().disks().size() < 1) {
						if ("".equals(diskCreate.getLunId()) && "".equals(diskCreate.getStorageType())) {
							StorageDomain storageDomain
									= (systemService.storageDomainsService().storageDomainService(diskCreate.getStorageDomainId()).get().send()).storageDomain();
							DiskBuilder diskBuilder = new DiskBuilder();
							diskBuilder.name(diskCreate.getName());
							diskBuilder.description(diskCreate.getDescription());
							if (diskCreate.getSharable()) {
								diskBuilder.format(DiskFormat.RAW);
							} else {
								diskBuilder.format(DiskFormat.COW);
							}
							diskBuilder.shareable(diskCreate.getSharable());
							diskBuilder.provisionedSize(BigInteger.valueOf(Integer.parseInt(diskCreate.getVirtualSize())).multiply(BigInteger.valueOf(2L).pow(30)));
							diskBuilder.storageDomains(new StorageDomain[] { storageDomain });
							((DiskAttachmentsService.AddResponse)diskAttachmentsService.add()
									.attachment(
											Builders.diskAttachment()
													.disk(diskBuilder)
													.interface_(DiskInterface.fromValue(diskCreate.getDiskInterface()))
													.bootable(diskCreate.getBootable())
													.readOnly(diskCreate.getReadOnly()))

									.send())
									.attachment();
							continue;
						}
						if (!"".equals(diskCreate.getLunId()) && "FCP"
								.equals(diskCreate.getStorageType())) {
							List<HostStorage> luns = ((HostStorageService.ListResponse)systemService.hostsService().hostService(diskCreate.getHostId()).storageService().list().send()).storages();
							DiskBuilder diskBuilder = new DiskBuilder();
							diskBuilder.alias(diskCreate.getName());
							diskBuilder.description(diskCreate.getDescription());
							diskBuilder.shareable(diskCreate.getSharable());
							HostStorageBuilder lunStorage = new HostStorageBuilder();
							for (HostStorage lun : luns) {
								if (lun.id().equals(diskCreate.getLunId())) {
									LogicalUnitBuilder logicalUnitBuilder = new LogicalUnitBuilder();
									logicalUnitBuilder.id(diskCreate.getLunId());
									logicalUnitBuilder.lunMapping(lun.logicalUnits().get(0).lunMapping());
									logicalUnitBuilder.productId(lun.logicalUnits().get(0).productId());
									logicalUnitBuilder.serial(lun.logicalUnits().get(0).serial());
									logicalUnitBuilder.size(lun.logicalUnits().get(0).size());
									logicalUnitBuilder.vendorId(lun.logicalUnits().get(0).vendorId());
									HostBuilder hostBuilder = new HostBuilder();
									hostBuilder.id(diskCreate.getHostId());
									lunStorage.host(hostBuilder);
									lunStorage.type(StorageType.FCP);
									lunStorage.logicalUnits(new LogicalUnitBuilder[] { logicalUnitBuilder });
									diskBuilder.lunStorage(lunStorage);
									break;
								}
							}
							((DiskAttachmentsService.AddResponse)diskAttachmentsService.add()
									.attachment(
											Builders.diskAttachment()
													.disk(diskBuilder)
													.interface_(DiskInterface.fromValue(diskCreate.getDiskInterface()))
													.bootable(diskCreate.getBootable()))

									.send())
									.attachment();
						}
						continue;
					}
					Disk disk = ((DiskService.GetResponse)systemService.disksService().diskService(diskCreate.getId()).get().send()).disk();
					DiskAttachment diskAttachResponse = ((DiskAttachmentsService.AddResponse)diskAttachmentsService.add().attachment(Builders.diskAttachment().disk(disk).interface_(DiskInterface.fromValue(diskCreate.getDiskInterface()))).send()).attachment();
					if (diskAttachResponse != null && (diskCreate.getBootable() || diskCreate.getReadOnly())) {
						Thread.sleep(1000L);
						((DiskAttachmentService.UpdateResponse)diskAttachmentsService.attachmentService(diskAttachResponse.id()).update().diskAttachment(Builders.diskAttachment()
										.bootable(diskCreate.getBootable())
										.readOnly(diskCreate.getReadOnly()))
								.send())
								.diskAttachment();
					}
				}
			if (vmCreate.getBootImageUse()) {
				vmCreate.setBootImage(vmCreate.getBootImage().replaceAll(" ", "%20"));
				CdromBuilder cdromBuilder = new CdromBuilder();
				FileBuilder fileBuilder = new FileBuilder();
				fileBuilder.id(vmCreate.getBootImage());
				cdromBuilder.file(fileBuilder);
				vmsService.vmService(response.id()).cdromsService().add().cdrom(cdromBuilder).send();
			}
			do {
				Thread.sleep(5000L);
			} while (((VmsService.ListResponse)systemService.vmsService().list().search(" id=" + response.id()).send()).vms().size() <= 0 &&
					!((VmService.GetResponse)systemService.vmsService().vmService(response.id()).get().send()).vm().status().equals(VmStatus.DOWN));
			Gson gson = new Gson();
			VmVo vm = new VmVo();
			vm.setId(response.id());
			vm.setName(response.name());
			vm.setComment(response.comment());
			vm.setCluster(((ClusterService.GetResponse)systemService.clustersService().clusterService(response.cluster().id()).get().send()).cluster().name());
			vm.setClusterId(response.cluster().id());
			if (vmCreate.getHeadlessMode()) {
				String consoleId = ((GraphicsConsole)((VmGraphicsConsolesService.ListResponse)systemService.vmsService().vmService(response.id()).graphicsConsolesService().list().send()).consoles().get(0)).id();
				systemService.vmsService().vmService(response.id()).graphicsConsolesService().consoleService(consoleId).remove().send();
				vm.setGraphicProtocol("null");
			} else {
				vm.setGraphicProtocol("VNC");
			}
			vm.setStatus("created");
			websocketService.sendMessage("/topic/vms", gson.toJson(vm));
			MessageVo message
					= MessageVo.Companion.createMessage(MessageType.VIRTUAL_MACHINE_CREATE, true, vm.getName());
			websocketService.sendMessage("/topic/notify", gson.toJson(message));
		} catch (Exception e) {
			try {
				Thread.sleep(5000L);
			} catch (InterruptedException interruptedException) {
				log.error(e.getLocalizedMessage());
				interruptedException.printStackTrace();
			}
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			Gson gson = new Gson();
			MessageVo message
					= MessageVo.Companion.createMessage(MessageType.VIRTUAL_MACHINE_CREATE, false, e.getMessage());
			websocketService.sendMessage("/topic/notify", gson.toJson(message));
		}
	}

	@Async("karajanTaskExecutor")
	public void updateVm(VmCreateVo vmUpdate) {
		Connection connection = this.adminConnectionService.getConnection();
		SystemService systemService = connection.systemService();
		VmService vmService = systemService.vmsService().vmService(vmUpdate.getId());
		Vm response = null;
		try {
			VmBuilder vmBuilder = new VmBuilder();
			try {
				vmBuilder.cluster(systemService.clustersService().clusterService(vmUpdate.getCluster()).get().send().cluster());
			} catch (Exception e) {
				throw new Exception("클러스터를 찾을 수 없습니다.");
			}
			OperatingSystemBuilder operatingSystemBuilder = new OperatingSystemBuilder();
			operatingSystemBuilder.type(vmUpdate.getOperatingSystem());
			if (vmUpdate.getInstanceType() == null || vmUpdate.getInstanceType().equals("null")) {
				InstanceTypeBuilder instanceTypeBuilder = new InstanceTypeBuilder();
				vmBuilder.instanceType(instanceTypeBuilder);
			} else {
				for (InstanceType instanceType : systemService.instanceTypesService().list().send().instanceType()) {
					if (vmUpdate.getInstanceType().equalsIgnoreCase(instanceType.id()))
						vmBuilder.instanceType(instanceType);
				}
			}
			vmBuilder.type(VmType.fromValue(vmUpdate.getType()));
			vmBuilder.name(vmUpdate.getName());
			vmBuilder.comment(vmUpdate.getDescription());
			vmBuilder.description(vmUpdate.getUse());
			if (!vmUpdate.getHeadlessMode()) {
				DisplayBuilder displayBuilder = new DisplayBuilder();
				displayBuilder.type(DisplayType.VNC);
				displayBuilder.disconnectAction(vmUpdate.getDisconnectAction());
				displayBuilder.smartcardEnabled(vmUpdate.getSmartcard());
				if (vmUpdate.getSingleSignOn()) {
					SsoBuilder ssoBuilder = new SsoBuilder();
					ssoBuilder.methods(new MethodBuilder[] { (new MethodBuilder()).id(SsoMethod.GUEST_AGENT) });
					vmBuilder.sso(ssoBuilder);
				}
				vmBuilder.display(displayBuilder);
			} else {
				List<GraphicsConsole> graphicsConsoleList
						= systemService.vmsService().vmService(vmUpdate.getId()).graphicsConsolesService().list().send().consoles();
				if (graphicsConsoleList.size() > 0)
					for (GraphicsConsole graphicsConsole : graphicsConsoleList) {
						String consoleId = graphicsConsole.id();
						systemService.vmsService().vmService(vmUpdate.getId()).graphicsConsolesService().consoleService(consoleId).remove().send();
					}
			}
			CpuBuilder cpuBuilder = new CpuBuilder();
			CpuTopologyBuilder cpuTopologyBuilder = new CpuTopologyBuilder();
			cpuTopologyBuilder.sockets(vmUpdate.getVirtualSockets());
			cpuTopologyBuilder.cores(vmUpdate.getCoresPerVirtualSocket());
			cpuTopologyBuilder.threads(vmUpdate.getThreadsPerCore());
			cpuBuilder.topology(cpuTopologyBuilder);
			vmBuilder.cpu(cpuBuilder);
			MemoryPolicyBuilder memoryPolicy = new MemoryPolicyBuilder();
			memoryPolicy.max(vmUpdate.getMaximumMemory());
			memoryPolicy.guaranteed(vmUpdate.getPhysicalMemory());
			memoryPolicy.ballooning(vmUpdate.getMemoryBalloon());
			vmBuilder.memory(vmUpdate.getMemory());
			vmBuilder.memoryPolicy(memoryPolicy);
			VmPlacementPolicyBuilder vmPlacementPolicyBuilder = new VmPlacementPolicyBuilder();
			vmPlacementPolicyBuilder.affinity(VmAffinity.fromValue(vmUpdate.getAffinity()));
			if (vmUpdate.getRecommendHost() != null || vmUpdate.getTargetHost() != null) {
				List<Host> runHosts = new ArrayList<>();
				if (vmUpdate.getPickHost().equals("targetHost")) {
					if (vmUpdate.getRecommendHost() != null) {
						runHosts.add(((HostService.GetResponse)systemService.hostsService().hostService(vmUpdate.getRecommendHost()).get().send()).host());
					} else {
						runHosts.add(((HostService.GetResponse)systemService.hostsService().hostService(vmUpdate.getTargetHost()).get().send()).host());
					}
					vmPlacementPolicyBuilder.hosts(runHosts);
				}
			}
			vmBuilder.placementPolicy(vmPlacementPolicyBuilder);
			if (vmUpdate.getCustomMigrationUsed()) {
				MigrationOptionsBuilder migrationOptionBuilder = new MigrationOptionsBuilder();
				migrationOptionBuilder.autoConverge(InheritableBoolean.fromValue(vmUpdate.getAutoConverge()));
				migrationOptionBuilder.compressed(InheritableBoolean.fromValue(vmUpdate.getCompressed()));
				if (vmUpdate.getCustomMigrationDowntimeUsed())
					vmBuilder.migrationDowntime(vmUpdate.getCustomMigrationDowntime());
			}
			if (vmUpdate.getUseCloudInit()) {
				InitializationBuilder initializationBuilder = new InitializationBuilder();
				initializationBuilder.hostName(vmUpdate.getHostName());
				initializationBuilder.timezone(vmUpdate.getTimezone());
				initializationBuilder.customScript(vmUpdate.getCustomScript());
				vmBuilder.initialization(initializationBuilder);
			}
			HighAvailabilityBuilder highAvailabilityBuilder = new HighAvailabilityBuilder();
			highAvailabilityBuilder.enabled(Boolean.valueOf(vmUpdate.getHighAvailability()));
			highAvailabilityBuilder.priority(vmUpdate.getPriority());
			vmBuilder.highAvailability(highAvailabilityBuilder);
			StorageDomainLeaseBuilder storageDomainLeaseBuilder = new StorageDomainLeaseBuilder();
			storageDomainLeaseBuilder.storageDomain(((StorageDomainService.GetResponse)systemService.storageDomainsService().storageDomainService(vmUpdate.getLeaseStorageDomain()).get().send()).storageDomain());
			vmBuilder.lease(storageDomainLeaseBuilder);
			vmBuilder.storageErrorResumeBehaviour(VmStorageErrorResumeBehaviour.fromValue(vmUpdate.getResumeBehaviour()));
			List<BootDevice> bootDevices = new ArrayList<>();
			bootDevices.add(BootDevice.fromValue(vmUpdate.getFirstDevice()));
			if (!vmUpdate.getSecondDevice().equals("none"))
				bootDevices.add(BootDevice.fromValue(vmUpdate.getSecondDevice()));
			BootBuilder bootBuilder = new BootBuilder();
			bootBuilder.devices(bootDevices);
			operatingSystemBuilder.boot(bootBuilder);
			vmBuilder.os(operatingSystemBuilder);
			CpuProfileBuilder cpuProfileBuilder = new CpuProfileBuilder();
			try {
				cpuProfileBuilder.cluster(systemService.clustersService().clusterService(vmUpdate.getCluster()).get().send().cluster());
			} catch (Exception e) {
				throw new Exception("클러스터를 찾을 수 없습니다.");
			}
			vmBuilder.cpuProfile(cpuProfileBuilder);
			vmBuilder.cpuShares(vmUpdate.getCpuShare());
			IoBuilder ioBuilder = new IoBuilder();
			ioBuilder.threads(vmUpdate.getIoThreadsEnabled());
			vmBuilder.io(ioBuilder);

			VirtioScsiBuilder virtioScsiBuilder = new VirtioScsiBuilder();
			virtioScsiBuilder.enabled(vmUpdate.getVirtioScsiEnabled());
			vmBuilder.virtioScsi(virtioScsiBuilder);

			RngDeviceBuilder rngDeviceBuilder = new RngDeviceBuilder();
			RateBuilder rateBuilder = new RateBuilder();
			rateBuilder.period(vmUpdate.getPeriodDuration());
			rateBuilder.bytes(vmUpdate.getBytesPerPeriod());
			rngDeviceBuilder.source(RngSource.fromValue(vmUpdate.getDeviceSource()));
			vmBuilder.rngDevice(rngDeviceBuilder);

			response
					= vmService.update().vm(vmBuilder).send().vm();
			if (vmUpdate.getDisks().size() > 0) {
				DiskAttachmentsService diskAttachmentsService = vmService.diskAttachmentsService();
				for (DiskVo diskCreate : vmUpdate.getDisks()) {
					if (!"".equals(diskCreate.getStatus())) {
						if (diskCreate.getStatus().equals("create")) {
							StorageDomain storageDomain
									= (systemService.storageDomainsService().storageDomainService(diskCreate.getStorageDomainId()).get().send()).storageDomain();
							diskAttachmentsService.add()
									.attachment(
											Builders.diskAttachment()
													.disk((new DiskBuilder())

															.name(diskCreate.getName())
															.description(diskCreate.getDescription())
															.format(DiskFormat.COW)

															.provisionedSize(BigInteger.valueOf(Integer.parseInt(diskCreate.getVirtualSize())).multiply(BigInteger.valueOf(2L).pow(30)))
															.storageDomains(new StorageDomain[] { storageDomain })).interface_(DiskInterface.fromValue(diskCreate.getDiskInterface()))
													.bootable(diskCreate.getBootable())
													.readOnly(diskCreate.getReadOnly())
													.active(true))

									.send()
									.attachment();
							continue;
						}
						if (diskCreate.getStatus().equals("update")) {
							diskAttachmentsService.attachmentService(diskCreate.getId()).update()
									.diskAttachment(Builders.diskAttachment()
												.disk((new DiskBuilder())
															.name(diskCreate.getName())
															.description(diskCreate.getDescription())
															.provisionedSize(BigDecimal.valueOf(Double.parseDouble(diskCreate.getVirtualSize())).toBigInteger().multiply(BigInteger.valueOf(2L).pow(30)))
												)
												.bootable(diskCreate.getBootable())
												.readOnly(diskCreate.getReadOnly())
									).send();
							continue;
						}

						if (diskCreate.getStatus().equals("remove")) {
							systemService.disksService().diskService(diskCreate.getId()).remove().send();
							continue;
						}

						if (diskCreate.getStatus().equals("disconnect")) {
							diskAttachmentsService.attachmentService(diskCreate.getId()).remove().send();
							continue;
						}

						if (diskCreate.getStatus().equals("linked")) {
							Disk disk
									= systemService.disksService().diskService(diskCreate.getId()).get().send().disk();
							DiskAttachment diskAttachResponse
									= (diskAttachmentsService.add().attachment(Builders.diskAttachment().disk(disk).interface_(DiskInterface.fromValue(diskCreate.getDiskInterface()))).send()).attachment();
							if (diskAttachResponse != null && (diskCreate.getBootable() || diskCreate.getReadOnly())) {
								Thread.sleep(1000L);
								((DiskAttachmentService.UpdateResponse)diskAttachmentsService.attachmentService(diskAttachResponse.id()).update().diskAttachment(Builders.diskAttachment()
												.bootable(diskCreate.getBootable())
												.readOnly(diskCreate.getReadOnly()))
										.send())
										.diskAttachment();
							}
						}
					}
				}
			}
			VmNicsService nicsService
					= vmService.nicsService();
			List<Nic> nics
					= nicsService.list().send().nics();

			if (vmUpdate.getExSelectNics().size() == vmUpdate.getSelectNics().size()) {
				for (int i = 0; i < vmUpdate.getExSelectNics().size(); i++) {
					if (((VmNicVo)vmUpdate.getExSelectNics().get(i)).getId().equals("none") && ((VmNicVo)vmUpdate.getSelectNics().get(i)).getId().equals("empty")) {
						systemService.vmsService().vmService(vmUpdate.getId()).nicsService().add()
								.nic(
										Builders.nic()
												.name("nic" + (i + 1)))

								.send();
					} else if (((Nic)nics.get(i)).vnicProfile() == null) {
						if (!((VmNicVo)vmUpdate.getSelectNics().get(i)).getId().equals("empty") && !((VmNicVo)vmUpdate.getExSelectNics().get(i)).getId().equals(((VmNicVo)vmUpdate.getSelectNics().get(i)).getId())) {
							NicBuilder nic = new NicBuilder();
							VnicProfileBuilder vnicProfileBuilder = new VnicProfileBuilder();
							nic.id(((Nic)nics.get(i)).id()).vnicProfile(vnicProfileBuilder.id(((VmNicVo)vmUpdate.getSelectNics().get(i)).getId()));
							vmService.nicsService().nicService(((Nic)nics.get(i)).id()).update().nic(nic).send();
						}
					} else if (!((VmNicVo)vmUpdate.getSelectNics().get(i)).getId().equals("empty")) {
						NicBuilder nic = new NicBuilder();
						VnicProfileBuilder vnicProfileBuilder = new VnicProfileBuilder();
						nic.id(((Nic)nics.get(i)).id()).vnicProfile(vnicProfileBuilder.id(((VmNicVo)vmUpdate.getSelectNics().get(i)).getId()));
						vmService.nicsService().nicService(((Nic)nics.get(i)).id()).update().nic(nic).send();
					} else if (((VmNicVo)vmUpdate.getSelectNics().get(i)).getId().equals("empty")) {
						NicBuilder nic = new NicBuilder();
						VnicProfileBuilder vnicProfileBuilder = new VnicProfileBuilder();
						nic.id(((Nic)nics.get(i)).id()).vnicProfile(vnicProfileBuilder);
						vmService.nicsService().nicService(((Nic)nics.get(i)).id()).update().nic(nic).send();
					} else if (((VmNicVo)vmUpdate.getSelectNics().get(i)).getId().equals("none")) {
						vmService.nicsService().nicService(((Nic)nics.get(i)).id()).remove().send();
					}
				}
			} else if (vmUpdate.getExSelectNics().size() > vmUpdate.getSelectNics().size()) {
				if (!((VmService.GetResponse)vmService.get().send()).vm().status().value().equals("up"))
					for (int i = vmUpdate.getSelectNics().size(); i < vmUpdate.getExSelectNics().size(); i++) {
						if (!((VmNicVo)vmUpdate.getExSelectNics().get(i)).getId().equals("none"))
							nicsService.nicService(((Nic)nics.get(i)).id()).remove().send();
					}
				if (((VmNicsService.ListResponse)vmService.nicsService().list().send()).nics().size() == vmUpdate.getSelectNics().size()) {
					List<Nic> nicList = ((VmNicsService.ListResponse)vmService.nicsService().list().send()).nics();
					for (int i = 0; i < vmUpdate.getSelectNics().size(); i++) {
						if (((Nic)nicList.get(i)).vnicProfile() == null) {
							if (!((VmNicVo)vmUpdate.getSelectNics().get(i)).getId().equals("none"))
								if (!((VmNicVo)vmUpdate.getExSelectNics().get(i)).getId().equals(((VmNicVo)vmUpdate.getSelectNics().get(i)).getId())) {
									NicBuilder nic = new NicBuilder();
									VnicProfileBuilder vnicProfileBuilder = new VnicProfileBuilder();
									nic.id(((Nic)nics.get(i)).id()).vnicProfile(vnicProfileBuilder.id(((VmNicVo)vmUpdate.getSelectNics().get(i)).getId()));
									nic.name("nic" + (i + 1));
									vmService.nicsService().nicService(((Nic)nicList.get(i)).id()).update().nic(nic).send();
								}
						} else if (!((VmNicVo)vmUpdate.getSelectNics().get(i)).getId().equals("none") || !((VmNicVo)vmUpdate.getSelectNics().get(i)).getId().equals("empty")) {
							NicBuilder nic = new NicBuilder();
							VnicProfileBuilder vnicProfileBuilder = new VnicProfileBuilder();
							nic.id(((Nic)nicList.get(i)).id()).vnicProfile(vnicProfileBuilder.id(((VmNicVo)vmUpdate.getSelectNics().get(i)).getId()));
							nic.name("nic" + (i + 1));
							vmService.nicsService().nicService(((Nic)nicList.get(i)).id()).update().nic(nic).send();
						} else if (((VmNicVo)vmUpdate.getSelectNics().get(i)).getId().equals("empty")) {
							NicBuilder nic = new NicBuilder();
							VnicProfileBuilder vnicProfileBuilder = new VnicProfileBuilder();
							nic.id(((Nic)nicList.get(i)).id()).vnicProfile(vnicProfileBuilder);
							nic.name("nic" + (i + 1));
							vmService.nicsService().nicService(((Nic)nicList.get(i)).id()).update().nic(nic).send();
						}
					}
				}
			} else {
				List<Nic> nicList = ((VmNicsService.ListResponse)vmService.nicsService().list().send()).nics();
				int i;
				for (i = 0; i < vmUpdate.getExSelectNics().size(); i++) {
					if (((VmNicVo)vmUpdate.getExSelectNics().get(i)).getId().equals("none") && ((VmNicVo)vmUpdate.getSelectNics().get(i)).getId().equals("empty")) {
						systemService.vmsService().vmService(vmUpdate.getId()).nicsService().add()
								.nic(
										Builders.nic()
												.name("nic" + (i + 1)))

								.send();
					} else if (((Nic)nicList.get(i)).vnicProfile() == null) {
						if (!((VmNicVo)vmUpdate.getSelectNics().get(i)).getId().equals("empty") && !((VmNicVo)vmUpdate.getExSelectNics().get(i)).getId().equals(((VmNicVo)vmUpdate.getSelectNics().get(i)).getId())) {
							NicBuilder nic = new NicBuilder();
							VnicProfileBuilder vnicProfileBuilder = new VnicProfileBuilder();
							nic.id(((Nic)nics.get(i)).id()).vnicProfile(vnicProfileBuilder.id(((VmNicVo)vmUpdate.getSelectNics().get(i)).getId()));
							nic.name("nic" + (i + 1));
							vmService.nicsService().nicService(((Nic)nicList.get(i)).id()).update().nic(nic).send();
						}
					} else if (!((VmNicVo)vmUpdate.getSelectNics().get(i)).getId().equals("empty")) {
						NicBuilder nic = new NicBuilder();
						VnicProfileBuilder vnicProfileBuilder = new VnicProfileBuilder();
						nic.id(((Nic)nicList.get(i)).id()).vnicProfile(vnicProfileBuilder.id(((VmNicVo)vmUpdate.getSelectNics().get(i)).getId()));
						nic.name("nic" + (i + 1));
						vmService.nicsService().nicService(((Nic)nicList.get(i)).id()).update().nic(nic).send();
					} else if (((VmNicVo)vmUpdate.getSelectNics().get(i)).getId().equals("empty")) {
						NicBuilder nic = new NicBuilder();
						VnicProfileBuilder vnicProfileBuilder = new VnicProfileBuilder();
						nic.id(((Nic)nicList.get(i)).id()).vnicProfile(vnicProfileBuilder);
						nic.name("nic" + (i + 1));
						vmService.nicsService().nicService(((Nic)nicList.get(i)).id()).update().nic(nic).send();
					}
				}
				for (i = vmUpdate.getExSelectNics().size(); i < vmUpdate.getSelectNics().size(); i++) {
					if (!((VmNicVo)vmUpdate.getSelectNics().get(i)).getId().equals("empty")) {
						vmService.nicsService().add().nic(Builders.nic().name("nic" + (i + 1))
										.vnicProfile(Builders.vnicProfile().id(((VmNicVo)vmUpdate.getSelectNics().get(i)).getId())))
								.send();
					} else {
						VnicProfileBuilder vnicProfileBuilder = new VnicProfileBuilder();
						vmService.nicsService().add().nic(Builders.nic().name("nic" + (i + 1))
										.vnicProfile(vnicProfileBuilder))
								.send();
					}
				}
			}
			if (vmUpdate.getBootImageUse()) {
				vmUpdate.setBootImage(vmUpdate.getBootImage().replaceAll(" ", "%20"));
				CdromBuilder cdromBuilder = new CdromBuilder();
				FileBuilder fileBuilder = new FileBuilder();
				fileBuilder.id(vmUpdate.getBootImage());
				cdromBuilder.file(fileBuilder);
				vmService.cdromsService().add().cdrom(cdromBuilder).send();
			} else {
				VmCdromService vmCdromService = vmService.cdromsService().cdromService(vmService.cdromsService().list().send().cdroms().get(0).id());
				if (vmCdromService.get().send().cdrom().filePresent())
					vmCdromService.update().cdrom(Builders.cdrom().file(Builders.file().id(""))).send();
			}
		} catch (Exception e) {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException interruptedException) {
				log.error(interruptedException.getLocalizedMessage());
				interruptedException.printStackTrace();
			}
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			Gson gson1 = new Gson();
			MessageVo messageVo
					= MessageVo.Companion.createMessage(MessageType.VIRTUAL_MACHINE_MODIFY, false, e.getMessage());
			websocketService.sendMessage("/topic/notify", gson1.toJson(messageVo));
			return;
		}
		Gson gson = new Gson();
		MessageVo message
				= MessageVo.Companion.createMessage(MessageType.VIRTUAL_MACHINE_MODIFY, true, response.name());
		websocketService.sendMessage("/topic/notify", gson.toJson(message));
	}

	@Async("karajanTaskExecutor")
	public void cloneVm(VmCreateVo vmClone) {
		Connection connection = this.adminConnectionService.getConnection();
		SystemService systemService = connection.systemService();
		Vm response = null;
		try {
			VmBuilder vmBuilder = new VmBuilder();
			for (Snapshot snapshot : (systemService.vmsService().vmService(vmClone.getId()).snapshotsService().list().send()).snapshots()) {
				if (snapshot.id().equals(vmClone.getSnapshotId()))
					vmBuilder.snapshots(new Snapshot[] { snapshot });
			}
			try {
				vmBuilder.cluster(((ClusterService.GetResponse)systemService.clustersService().clusterService(vmClone.getCluster()).get().send()).cluster());
			} catch (Exception e) {
				throw new Exception(e);
			}
			OperatingSystemBuilder operatingSystemBuilder = new OperatingSystemBuilder();
			operatingSystemBuilder.type(vmClone.getOperatingSystem());
			for (InstanceType instanceType : ((InstanceTypesService.ListResponse)systemService.instanceTypesService().list().send()).instanceType()) {
				if (vmClone.getInstanceType() != null && vmClone.getInstanceType().equalsIgnoreCase(instanceType.id()))
					vmBuilder.instanceType(instanceType);
			}
			vmBuilder.type(!"".equals(vmClone.getType()) ? VmType.fromValue(vmClone.getType()) : VmType.SERVER);
			vmBuilder.name(vmClone.getName());
			vmBuilder.comment(vmClone.getDescription());
			vmBuilder.description("".equals(vmClone.getUse()) ? "systemManagement" : vmClone.getUse());

			if (!vmClone.getHeadlessMode()) {
				DisplayBuilder displayBuilder = new DisplayBuilder();
				displayBuilder.type(DisplayType.VNC);
				displayBuilder.disconnectAction(vmClone.getDisconnectAction());
				displayBuilder.smartcardEnabled(vmClone.getSmartcard());
				if (vmClone.getSingleSignOn()) {
					SsoBuilder ssoBuilder = new SsoBuilder();
					ssoBuilder.methods(new MethodBuilder[] { (new MethodBuilder()).id(SsoMethod.GUEST_AGENT) });
					vmBuilder.sso(ssoBuilder);
				}
				vmBuilder.display(displayBuilder);
			} else {
				List<GraphicsConsole> graphicsConsoleList
						= systemService.vmsService().vmService(vmClone.getId()).graphicsConsolesService().list().send().consoles();
				if (graphicsConsoleList.size() > 0)
					for (int i = 0; i < graphicsConsoleList.size(); i++) {
						String consoleId = null;
						consoleId = graphicsConsoleList.get(i).id();
						systemService.vmsService().vmService(vmClone.getId()).graphicsConsolesService().consoleService(consoleId).remove().send();
					}
			}
			CpuBuilder cpuBuilder = new CpuBuilder();

			CpuTopologyBuilder cpuTopologyBuilder = new CpuTopologyBuilder();
			cpuTopologyBuilder.cores(vmClone.getCoresPerVirtualSocket());
			cpuTopologyBuilder.sockets(vmClone.getVirtualSockets());
			cpuTopologyBuilder.threads(vmClone.getThreadsPerCore());
			cpuBuilder.topology(cpuTopologyBuilder);
			vmBuilder.cpu(cpuBuilder);

			MemoryPolicyBuilder memoryPolicy = new MemoryPolicyBuilder();
			vmBuilder.memory(vmClone.getMemory());
			memoryPolicy.max(vmClone.getMaximumMemory());
			vmBuilder.memoryPolicy(memoryPolicy);
			memoryPolicy.guaranteed(vmClone.getPhysicalMemory());
			memoryPolicy.ballooning(vmClone.getMemoryBalloon());
			vmBuilder.memoryPolicy(memoryPolicy);
			VmPlacementPolicyBuilder vmPlacementPolicyBuilder = new VmPlacementPolicyBuilder();
			if (vmClone.getAffinity() != null)
				vmPlacementPolicyBuilder.affinity(VmAffinity.fromValue(vmClone.getAffinity()));
			if (vmClone.getRecommendHost() != null || vmClone.getTargetHost() != null) {
				List<Host> runHosts = new ArrayList<>();
				if (vmClone.getRecommendHost() != null) {
					runHosts.add(((HostService.GetResponse)systemService.hostsService().hostService(vmClone.getRecommendHost()).get().send()).host());
				} else {
					runHosts.add(((HostService.GetResponse)systemService.hostsService().hostService(vmClone.getTargetHost()).get().send()).host());
				}
				vmPlacementPolicyBuilder.hosts(runHosts);
			}
			vmBuilder.placementPolicy(vmPlacementPolicyBuilder);
			if (vmClone.getCustomMigrationUsed()) {
				MigrationOptionsBuilder migrationOptionBuilder = new MigrationOptionsBuilder();
				migrationOptionBuilder.autoConverge(InheritableBoolean.fromValue(vmClone.getAutoConverge()));
				migrationOptionBuilder.compressed(InheritableBoolean.fromValue(vmClone.getCompressed()));
				if (vmClone.getCustomMigrationDowntimeUsed())
					vmBuilder.migrationDowntime(vmClone.getCustomMigrationDowntime());
			}
			if (vmClone.getUseCloudInit()) {
				InitializationBuilder initializationBuilder = new InitializationBuilder();
				initializationBuilder.hostName(vmClone.getHostName());
				initializationBuilder.timezone(vmClone.getTimezone());
				initializationBuilder.customScript(vmClone.getCustomScript());
				vmBuilder.initialization(initializationBuilder);
			}
			HighAvailabilityBuilder highAvailabilityBuilder = new HighAvailabilityBuilder();
			highAvailabilityBuilder.enabled(vmClone.getHighAvailability());
			highAvailabilityBuilder.priority(vmClone.getPriority());
			vmBuilder.highAvailability(highAvailabilityBuilder);
			StorageDomainLeaseBuilder storageDomainLeaseBuilder = new StorageDomainLeaseBuilder();
			storageDomainLeaseBuilder.storageDomain(((StorageDomainService.GetResponse)systemService.storageDomainsService().storageDomainService(vmClone.getLeaseStorageDomain()).get().send()).storageDomain());
			vmBuilder.lease(storageDomainLeaseBuilder);
			vmBuilder.storageErrorResumeBehaviour(VmStorageErrorResumeBehaviour.fromValue(vmClone.getResumeBehaviour()));
			List<BootDevice> bootDevices = new ArrayList<>();
			bootDevices.add(BootDevice.fromValue(vmClone.getFirstDevice()));
			if (!vmClone.getSecondDevice().equals("none"))
				bootDevices.add(BootDevice.fromValue(vmClone.getSecondDevice()));
			BootBuilder bootBuilder = new BootBuilder();
			bootBuilder.devices(bootDevices);
			operatingSystemBuilder.boot(bootBuilder);
			vmBuilder.os(operatingSystemBuilder);
			CpuProfileBuilder cpuProfileBuilder = new CpuProfileBuilder();
			try {
				cpuProfileBuilder.cluster(systemService.clustersService().clusterService(vmClone.getCluster()).get().send().cluster());
			} catch (Exception e) {
				throw new Exception("클러스터를 찾을 수 없습니다.");
			}
			vmBuilder.cpuProfile(cpuProfileBuilder);
			vmBuilder.cpuShares(vmClone.getCpuShare());
			IoBuilder ioBuilder = new IoBuilder();
			ioBuilder.threads(vmClone.getIoThreadsEnabled());
			vmBuilder.io(ioBuilder);
			VirtioScsiBuilder virtioScsiBuilder = new VirtioScsiBuilder();
			virtioScsiBuilder.enabled(vmClone.getVirtioScsiEnabled());
			vmBuilder.virtioScsi(virtioScsiBuilder);
			if (!"".equals(vmClone.getDeviceSource())) {
				RngDeviceBuilder rngDeviceBuilder = new RngDeviceBuilder();
				RateBuilder rateBuilder = new RateBuilder();
				rateBuilder.period(vmClone.getPeriodDuration());
				rateBuilder.bytes(vmClone.getBytesPerPeriod());
				rngDeviceBuilder.source(RngSource.fromValue(vmClone.getDeviceSource()));
				vmBuilder.rngDevice(rngDeviceBuilder);
			}
			response
					= systemService.vmsService().addFromSnapshot().vm(vmBuilder).send().vm();
			if (vmClone.getBootImageUse()) {
				vmClone.setBootImage(vmClone.getBootImage().replaceAll(" ", "%20"));
				CdromBuilder cdromBuilder = new CdromBuilder();
				FileBuilder fileBuilder = new FileBuilder();
				fileBuilder.id(vmClone.getBootImage());
				cdromBuilder.file(fileBuilder);
				systemService.vmsService().vmService(response.id()).cdromsService().add().cdrom(cdromBuilder).send();
			}
			do {
				Thread.sleep(5000L);
			} while (
				systemService.vmsService().list().search(" id=" + response.id()).send().vms().size() == 0 &&
				!systemService.vmsService().vmService(response.id()).get().send().vm().status().equals(VmStatus.DOWN)
			);
			Gson gson = new Gson();
			VmVo vm = new VmVo();
			vm.setId(response.id());
			vm.setName(response.name());
			vm.setComment(response.comment());
			vm.setCluster(systemService.clustersService().clusterService(response.cluster().id()).get().send().cluster().name());
			vm.setClusterId(response.cluster().id());
			vm.setGraphicProtocol("VNC");
			vm.setStatus("created");
			websocketService.sendMessage("/topic/vms", gson.toJson(vm));
			MessageVo message
					= MessageVo.Companion.createMessage(MessageType.VIRTUAL_MACHINE_COPY, true, vm.getName());
			websocketService.sendMessage("/topic/notify", gson.toJson(message));
		} catch (Exception e) {
			try {
				Thread.sleep(5000L);
			} catch (InterruptedException interruptedException) {
				log.error(interruptedException.getLocalizedMessage());
				interruptedException.printStackTrace();
			}
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			Gson gson = new Gson();
			MessageVo message
					= MessageVo.Companion.createMessage(MessageType.VIRTUAL_MACHINE_COPY, false, e.getMessage());
			websocketService.sendMessage("/topic/notify", gson.toJson(message));
		}
	}

	public List<DiskProfileVo> retrieveDiskProfiles() {
		Connection connection = connectionService.getConnection();
		List<DiskProfile> items
				= SystemServiceHelper.getInstance().findAllDiskProfiles(connection);
		return ModelsKt.toDiskProfileVos(items, connection);
	}

	public List<SnapshotVo> retrieveVmSnapshots(String id) {
		Connection connection = connectionService.getConnection();
		List<Snapshot> snapshots
				= SystemServiceHelper.getInstance().findAllSnapshotsFromVm(connection, id);
		return ModelsKt.toSnapshotVos(snapshots, connection);
	}

	public void createSnapshot(SnapshotVo snapshot) {
		Connection connection = connectionService.getConnection();
		List<DiskAttachment> diskAttachments
				= SystemServiceHelper.getInstance().findDiskAttachmentsFromVm(connection, snapshot.getVmId());

		List<DiskAttachment> attachments = new ArrayList<>();
		for (DiskAttachment diskAttachment : diskAttachments) {
			for (DiskVo disk : snapshot.getDisks()) {
				if (disk.getId().equals(diskAttachment.id()))
					attachments.add(diskAttachment);
			}
		}
		Snapshot s
				= Builders.snapshot().description(snapshot.getDescription())
					.diskAttachments(attachments).build();
		Boolean res
				= SystemServiceHelper.getInstance().addSnapshotFromVm(connection, snapshot.getVmId(), s);
		log.info("createSnapshot ... res: "+res);
	}

	public void previewSnapshot(SnapshotVo snapshot) {
		log.info("previewSnapshot ... vmId:" + snapshot.getVmId() + ", snapshot id:" + snapshot.getId() + ", memoryRestored:" + snapshot.getMemoryRestore());
		Connection connection = connectionService.getConnection();
		Snapshot previewSnapshot
				= SystemServiceHelper.getInstance().findSnapshotFromVm(connection, snapshot.getVmId(), snapshot.getId());
		Boolean res
				= SystemServiceHelper.getInstance().previewSnapshotFromVm(connection, snapshot.getVmId(), previewSnapshot, snapshot.getMemoryRestore());
		log.info("previewSnapshot ... res: "+res);
	}

	public void commitSnapshot(String vmId) {
		log.info("commitSnapshot ... vmId: " + vmId);
		Connection connection = connectionService.getConnection();
		Boolean res
				= SystemServiceHelper.getInstance().commitSnapshotFromVm(connection, vmId);
		log.info("commitSnapshot ... res: "+res);
	}

	public void undoSnapshot(String vmId) {
		log.info("undoSnapshot ... vmId: " + vmId);
		Connection connection = connectionService.getConnection();
		Boolean res
				= SystemServiceHelper.getInstance().undoSnapshotFromVm(connection, vmId);
		log.info("undoSnapshot ... res: "+res);
	}

	public void removeSnapshot(SnapshotVo snapshot) {
		Connection connection = connectionService.getConnection();
		Boolean res
				= SystemServiceHelper.getInstance().removeSnapshotFromVm(connection, snapshot.getVmId(), snapshot.getId());
		log.info("removeSnapshot ... res: "+res);
	}

	public List<StorageDomainVo> retrieveDiscs() {
		Connection connection = connectionService.getConnection();

		List<StorageDomain> storageDomainList
				= SystemServiceHelper.getInstance().findAllStorageDomains(connection);

		List<StorageDomainVo> discs = new ArrayList<>();
		for (StorageDomain item : storageDomainList) {
			if (item.type().value().equals("iso")) {
				List<File> fileList
						= SystemServiceHelper.getInstance().findAllFilesFromStorageDomain(connection, item.id());
				discs.addAll(ModelsKt.toStorageDomainVosUsingFiles(fileList));
			}
		}

		List<Disk> disks
				= SystemServiceHelper.getInstance().findAllDisks(connection, "");
		discs.addAll(ModelsKt.toStorageDomainVosUsingDisks(disks));
		return discs;
	}

	@Async("karajanTaskExecutor")
	@Override
	public void changeDisc(VmVo vm) {
		Connection connection = this.adminConnectionService.getConnection();
		String vmId = vm.getId();
		List<Cdrom> vmCdroms
				= SystemServiceHelper.getInstance().findAllVmCdromsFromVm(connection, vmId);
		FileBuilder fb
				= Builders.file().id("eject".equals(vm.getDisc()) ? "" : vm.getDisc());
		Cdrom cdrom
				= Builders.cdrom().file(fb).build();
		Boolean res
				= SystemServiceHelper.getInstance().updateVmCdromFromVm(connection, vm.getId(), vmCdroms.get(0).id(), cdrom);

		String vmName
				= SystemServiceHelper.getInstance().findVm(connection, vmId).name();
		MessageVo message
				= MessageVo.createMessage(MessageType.CHANGE_CD_ROM, res, vmName);
		websocketService.sendMessage("/topic/notify", new Gson().toJson(message));
	}

	public List<DashboardTopVo> retrieveVmsTop(List<VmVo> totalVms) {
		List<DashboardTopVo> data = new ArrayList<>();
		Map<String, Integer> vmCpuParamMap = new HashMap<>();
		Map<String, Integer> vmMemoryParamMap = new HashMap<>();
		int i;
		for (i = 0; i < totalVms.size(); i++) {
			vmCpuParamMap.put((totalVms.get(i)).getName(), Integer.parseInt(totalVms.get(i).getCpuUsage().get(0).get(1)));
			vmMemoryParamMap.put((totalVms.get(i)).getName(), Integer.parseInt(totalVms.get(i).getMemoryUsage().get(0).get(1)));
		}
		List<String> vmCpuKeyList
				= ModelsKt.toVmCpuKeys(totalVms);
		List<String> vmCpuValList
				= ModelsKt.toVmCpuVals(totalVms);
		List<String> vmMemoryKeyList
				= ModelsKt.toVmMemoryKeys(totalVms);
		List<String> vmMemoryValList
				= ModelsKt.toVmMemoryVals(totalVms);

		int index = 0;
		for (String key : vmMemoryKeyList) {
			vmMemoryValList.add(index, vmMemoryParamMap.get(key).toString());
			index++;
		}
		for (i = vmCpuKeyList.size(); i < 3; i++) {
			vmCpuKeyList.add(index, "N/A");
			vmCpuValList.add(index, "0");
			vmMemoryKeyList.add(index, "N/A");
			vmMemoryValList.add(index, "0");
		}
		DashboardTopVo dashboardTopVo
				= ModelsKt.toDashboardTopVo(totalVms);
		data.add(dashboardTopVo);
		return data;
	}
}