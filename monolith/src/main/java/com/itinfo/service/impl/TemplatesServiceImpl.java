package com.itinfo.service.impl;

import com.itinfo.model.*;
import com.itinfo.service.TemplatesService;
import com.itinfo.service.engine.AdminConnectionService;
import com.itinfo.service.engine.ConnectionService;
import com.itinfo.service.engine.WebsocketService;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.builders.BootBuilder;
import org.ovirt.engine.sdk4.builders.Builders;
import org.ovirt.engine.sdk4.builders.CpuBuilder;
import org.ovirt.engine.sdk4.builders.CpuProfileBuilder;
import org.ovirt.engine.sdk4.builders.CpuTopologyBuilder;
import org.ovirt.engine.sdk4.builders.DisplayBuilder;
import org.ovirt.engine.sdk4.builders.HighAvailabilityBuilder;
import org.ovirt.engine.sdk4.builders.IoBuilder;
import org.ovirt.engine.sdk4.builders.MemoryPolicyBuilder;
import org.ovirt.engine.sdk4.builders.MigrationOptionsBuilder;
import org.ovirt.engine.sdk4.builders.OperatingSystemBuilder;
import org.ovirt.engine.sdk4.builders.StorageDomainLeaseBuilder;
import org.ovirt.engine.sdk4.builders.TemplateBuilder;
import org.ovirt.engine.sdk4.builders.TemplateVersionBuilder;
import org.ovirt.engine.sdk4.builders.VirtioScsiBuilder;
import org.ovirt.engine.sdk4.builders.VmPlacementPolicyBuilder;
import org.ovirt.engine.sdk4.builders.WatchdogBuilder;
import org.ovirt.engine.sdk4.services.HostService;
import org.ovirt.engine.sdk4.services.QuotaService;
import org.ovirt.engine.sdk4.services.StorageDomainService;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.services.TemplateService;

import org.ovirt.engine.sdk4.services.VmService;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TemplatesServiceImpl extends BaseService implements TemplatesService {
	@Autowired private ConnectionService connectionService;
	@Autowired private AdminConnectionService adminConnectionService;
	@Autowired private WebsocketService websocketService;

	public List<TemplateVo> retrieveTemplates() {
		Connection connection = this.connectionService.getConnection();
		List<Template> items
				= getSysSrvHelper().findAllTemplates(connection, "");
		return ModelsKt.toTemplateVos(items, connection);
	}

	public TemplateVo retrieveTemplate(String id) {
		Connection connection = connectionService.getConnection();
		Template item
				= getSysSrvHelper().findTemplate(connection, id);
		TemplateVo template
				= ModelsKt.toTemplateVo(item, connection);
		List<Vm> vmList
				= getSysSrvHelper().findAllVms(connection, "");
		List<VmVo> vms = new ArrayList<>();
		for (Vm vmItem : vmList) {
			String targetId = item.id();
			if (item.version().versionNumberAsInteger().intValue() > 1)
				targetId = item.version().baseTemplate().id();
			if (vmItem.template().id().equals(targetId)) {
				VmVo vm
						= ModelsKt.toVmVo(vmItem, connection);
				vms.add(vm);
			}
		}
		template.setVms(vms);
		template.setNics(retrieveNicInfo(item.id()));
		template.setTemplateDisks(retrieveDisks(item.id()));
		template.setEvents(retrieveEvents(item.id()));
		return template;
	}

	@Override
	public VmSystemVo retrieveSystemInfo(String id) {
		Connection connection = this.connectionService.getConnection();
		Template template
				= getSysSrvHelper().findTemplate(connection, id);
		return ModelsKt.toVmSystemVoFromTemplate(template);
	}

	public List<VmNicVo> retrieveNicInfo(String id) {
		Connection connection = connectionService.getConnection();
		List<Nic> nics
				= getSysSrvHelper().findAllNicsFromTemplate(connection, id);
		return ModelsKt.toVmNicVos(nics, connection);
	}

	public List<StorageDomainVo> retrieveStorageInfo(String id) {
		Connection connection = connectionService.getConnection();
		List<StorageDomain> storageDomainList
				= getSysSrvHelper().findAllStorageDomains(connection, "");
		return ModelsKt.toStorageDomainVos(storageDomainList);
	}

	public List<EventVo> retrieveEvents(String id) {
		Connection connection = connectionService.getConnection();
		List<Event> items
				= getSysSrvHelper().findAllEvents(connection, "");
		List<EventVo> events = items.stream()
					.filter(event -> event.templatePresent() && id.equals(event.template().id()))
					.map(ModelsKt::toEventVo)
					.collect(Collectors.toList());
		return events;
	}

	public List<CpuProfileVo> retrieveCpuProfiles() {
		log.info("retrieveCpuProfiles ...");
		Connection connection = connectionService.getConnection();
		List<CpuProfile> items
				= getSysSrvHelper().findAllCpuProfiles(connection);
		return ModelsKt.toCpuProfileVos(items);
	}

	public List<TemplateVo> retrieveRootTemplates() {
		Connection connection = connectionService.getConnection();
		List<Template> items = getSysSrvHelper().findAllTemplates(connection, "")
				.stream().filter(item -> item.version().versionNumber().intValue() == 1)
				.collect(Collectors.toList());
		return ModelsKt.toTemplateVos(items, connection);
	}

	public List<TemplateDiskVo> retrieveDisks(String id) {
		Connection connection = connectionService.getConnection();
		SystemService systemService = connection.systemService();
		VmService vmService = systemService.vmsService().vmService(id);
		List<DiskAttachment> diskAttachments
				= vmService.diskAttachmentsService().list().send().attachments();
		List<DiskProfile> diskProfileList
				= systemService.diskProfilesService().list().send().profile();
		String dataCenterId
				= systemService.dataCentersService().list().send().dataCenters().get(0).id();
		List<Quota> quotaList
				= systemService.dataCentersService().dataCenterService(dataCenterId).quotasService().list().send().quotas();

		List<TemplateDiskVo> templateDisks = new ArrayList<>();
		for (DiskAttachment diskAttachment : diskAttachments) {
			Disk item
					= systemService.disksService().diskService(diskAttachment.disk().id()).get().send().disk();
			TemplateDiskVo templateDisk = new TemplateDiskVo();
			templateDisk.setId(item.id());
			templateDisk.setName(item.name());
			templateDisk.setVirtualSize(Math.round(item.provisionedSize().doubleValue() / Math.pow(1024.0D, 3.0D) * 100.0D) / 100.0D + " GiB");
			templateDisk.setActualSize(Math.round(item.actualSize().doubleValue() / Math.pow(1024.0D, 3.0D) * 100.0D) / 100.0D + " GiB");
			templateDisk.setStatus(item.status().value());
			templateDisk.setFormat(item.format().value());
			templateDisk.setType(item.storageType().value());
			List<StorageDomainVo> storageDomains = new ArrayList<>();
			List<DiskProfileVo> diskProfiles = new ArrayList<>();
			List<QuotaVo> quotas = new ArrayList<>();
			if (item.storageDomainsPresent()) {
				StorageDomain storageDomainItem
						= (systemService.storageDomainsService().storageDomainService((item.storageDomains().get(0)).id()).get().send()).storageDomain();
				if (storageDomainItem.type().equals(StorageDomainType.DATA)) {
					StorageDomainVo storageDomain = new StorageDomainVo();
					storageDomain.setId(storageDomainItem.id());
					storageDomain.setName(storageDomainItem.name());
					if (storageDomainItem.master()) {
						storageDomain.setType(StringUtils.capitalize(storageDomainItem.type().value()) + "(Master)");
					} else {
						storageDomain.setType(StringUtils.capitalize(storageDomainItem.type().value()));
					}
					if (storageDomainItem.statusPresent()) {
						storageDomain.setStatus(StringUtils.capitalize(storageDomainItem.status().value()));
					} else {
						storageDomain.setStatus("-");
					}
					storageDomain.setDiskFree(storageDomainItem.available());
					storageDomain.setDiskUsed(storageDomainItem.used());
					storageDomains.add(storageDomain);
					for (DiskProfile diskProfileItem : diskProfileList) {
						if (storageDomainItem.id().equals(diskProfileItem.storageDomain().id())) {
							DiskProfileVo diskProfile =
									ModelsKt.toDiskProfileVo(diskProfileItem, "");
							diskProfiles.add(diskProfile);
						}
					}
					for (Quota quotaItem : quotaList) {
						QuotaService quotaService = systemService.dataCentersService().dataCenterService(dataCenterId).quotasService().quotaService(quotaItem.id());
						for (QuotaStorageLimit quotaStorageLimit : quotaService.quotaStorageLimitsService().list().send().limits()) {
							if (!quotaStorageLimit.storageDomainPresent()) {
								QuotaVo quota = ModelsKt.toQuotaVo(quotaItem, connection);
								quotas.add(quota);
							}
							if (quotaStorageLimit.storageDomainPresent() && storageDomainItem.id().equals(quotaStorageLimit.storageDomain().id())) {
								QuotaVo quota = ModelsKt.toQuotaVo(quotaItem, connection);
								quotas.add(quota);
							}
						}
					}
				}
			}
			templateDisk.setStorageDomains(storageDomains);
			templateDisk.setDiskProfiles(diskProfiles);
			templateDisk.setQuotas(quotas);
			templateDisks.add(templateDisk);
		}
		return templateDisks;
	}

	public Boolean checkDuplicateName(String name) {
		Connection connection = connectionService.getConnection();
		Boolean result
				= getSysSrvHelper().findAllTemplates(connection, " name=" + name).size() > 0;
		log.debug("result: "+result);
		return result;
	}

	@Async("karajanTaskExecutor")
	public void createTemplate(TemplateVo template) {
		Connection connection = adminConnectionService.getConnection();
		List<DataCenter> dataCenter
				= getSysSrvHelper().findAllDataCenters(connection);
		String dataCenterId
				= (dataCenter.size() > 0) ? dataCenter.get(0).id() : "";
		List<DiskAttachment> attachments
				= ModelsKt.toDiskAttachments(template.getTemplateDisks(), connection, dataCenterId);

		Gson gson = new Gson();
		Template t2Add;
		if (template.getRootTemplateId() != null && !"".equals(template.getRootTemplateId())) {
			Template baseTemplate =
					getSysSrvHelper().findTemplate(connection, template.getRootTemplateId());
			TemplateVersion templateVersion = new TemplateVersionBuilder()
				.baseTemplate(baseTemplate)
				.versionName(template.getSubVersionName())
				.build();
			t2Add = Builders.template()
						.name(template.getName())
						.description(template.getDescription())
						.vm(Builders.vm().id(template.getOrgVmId()).diskAttachments(attachments))
						.version(templateVersion)
						.build();
		} else {
			t2Add = Builders.template()
					.name(template.getName())
					.description(template.getDescription())
					.vm(Builders.vm().id(template.getOrgVmId()).diskAttachments(attachments))
					.build();
		}
		Template response
				= getSysSrvHelper().addTemplate(connection, t2Add, template.getClonePermissions(), template.getSeal());
		try {
			do {
				Thread.sleep(5000L);
			} while (getSysSrvHelper().findAllTemplates(connection, " name=" + response.name()).size() <= 0);
			MessageVo message
					= MessageVo.createMessage(MessageType.TEMPLATE_ADD, true, response.name(), "");
			websocketService.sendMessage("/topic/notify", gson.toJson(message));
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			MessageVo message
					= MessageVo.createMessage(MessageType.TEMPLATE_ADD, false, e.getMessage(), "");
			websocketService.sendMessage("/topic/notify", gson.toJson(message));
		}
	}

	@Async("karajanTaskExecutor")
	public void removeTemplate(String id) {
		Connection connection = adminConnectionService.getConnection();
		Gson gson = new Gson();
		Template template
				= getSysSrvHelper().findTemplate(connection, id);

		try {
			String name = template.name();
			if (template.version().versionNumberAsInteger() > 1)
				name = template.version().versionName();
			List<Vm> vms
					= getSysSrvHelper().findAllVms(connection," template.name=" + template.name());
			if (vms.size() > 0) {
				String names = "";
				for (Vm vm : vms)
					names += vm.name() + " ";

				websocketService.sendMessage("/topic/templates", gson.toJson(Arrays.asList(id, "failed")));
				MessageVo message
						= MessageVo.createMessage(MessageType.TEMPLATE_REMOVE, false, names, "이 템플릿은 현재 다음의 가상머신이 사용 중 입니다.");
				websocketService.sendMessage("/topic/notify", gson.toJson(message));
			} else {
				getSysSrvHelper().removeTemplate(connection, id);
				do {
					Thread.sleep(5000L);
				} while (getSysSrvHelper().findAllTemplates(connection, " id=" + id).size() != 0);

				websocketService.sendMessage("/topic/templates", gson.toJson(Arrays.asList(id, "removed")));
				MessageVo message
						= MessageVo.createMessage(MessageType.TEMPLATE_REMOVE, true, name, "");
				websocketService.sendMessage("/topic/notify", gson.toJson(message));
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			MessageVo message
					= MessageVo.createMessage(MessageType.TEMPLATE_REMOVE, false, e.getMessage(), "");
			websocketService.sendMessage("/topic/notify", gson.toJson(message));
		}
	}

	public TemplateEditVo retrieveTemplateEditInfo(String id) {
		Connection connection = connectionService.getConnection();
		Template template
				= getSysSrvHelper().findTemplate(connection, id);
		List<Cluster> clusterItemList
				= getSysSrvHelper().findAllClusters(connection);
		List<ClusterVo> clusters
				= ModelsKt.toClusterVos(clusterItemList, connection);

		TemplateEditVo templateEditInfo = new TemplateEditVo();
		templateEditInfo.setClusters(clusters);

		List<OperatingSystemInfo> osItemList
				= getSysSrvHelper().findAllOperatingSystems(connection);
		List<OsInfoVo> osInfoList
				= ModelsKt.toOsInfoVos(osItemList);
		templateEditInfo.setOperatingSystems(osInfoList);
		List<Host> hostList
				= getSysSrvHelper().findAllHosts(connection, "");

		List<HostVo> hosts
				= ModelsKt.toHostVos(hostList, connection);

		templateEditInfo.setHosts(hosts);
		if (template.placementPolicy().hostsPresent())
			templateEditInfo.setTargetHost(template.placementPolicy().hosts().get(0).id());
		templateEditInfo.setAffinity("migratable");
		List<StorageDomain> storageDomainList
				= getSysSrvHelper().findAllStorageDomains(connection, "");
		List<StorageDomainVo> storageDomains = new ArrayList<>();
		List<StorageDomainVo> bootImages = new ArrayList<>();
		for (StorageDomain item : storageDomainList) {
			if (item.type().value().equals("data")) {
				StorageDomainVo storageDomain = new StorageDomainVo();
				storageDomain.setId(item.id());
				storageDomain.setName(item.name());
				storageDomain.setType(item.type().value());
				storageDomains.add(storageDomain);
				continue;
			}
			if (item.type().value().equals("iso")) {
				List<File> filesFromStorageDomain
						= getSysSrvHelper().findAllFilesFromStorageDomain(connection, item.id()) ;
				templateEditInfo.setImageStorage(item.id());
				for (File file : filesFromStorageDomain) {
					StorageDomainVo storageDomain = new StorageDomainVo();
					storageDomain.setId(file.id());
					storageDomain.setName(file.name());
					storageDomain.setType(file.type());
					bootImages.add(storageDomain);
				}
			}
		}
		templateEditInfo.setLeaseStorageDomains(storageDomains);
		templateEditInfo.setBootImages(bootImages);
		List<CpuProfile> cpuProfileList
				= getSysSrvHelper().findAllCpuProfiles(connection);
		List<CpuProfileVo> cpuProfiles
				= ModelsKt.toCpuProfileVos(cpuProfileList);
		/*
		for (CpuProfile profile : systemService.cpuProfilesService().list().send().profile()) {
			CpuProfileVo cpuProfile = new CpuProfileVo();
			cpuProfile.setId(profile.id());
			cpuProfile.setName(profile.name());
			cpuProfiles.add(cpuProfile);
		}
		*/
		templateEditInfo.setCpuProfiles(cpuProfiles);
		templateEditInfo.setId(template.id());
		templateEditInfo.setCluster(template.cluster().id());
		for (OperatingSystemInfo item : osItemList) {
			if (template.os().type().equals(item.name()))
				templateEditInfo.setOperatingSystem(item.name());
		}
		templateEditInfo.setType(template.type().value());
		templateEditInfo.setName(template.name());
		templateEditInfo.setDescription(template.description());
		templateEditInfo.setStateless(template.stateless());
		templateEditInfo.setStartInPause(template.startPaused());
		templateEditInfo.setDeleteProtection(template.deleteProtected());
		if (template.version().versionNumberAsInteger().intValue() > 1) {
			templateEditInfo.setSubName(template.version().versionName());
		} else {
			templateEditInfo.setSubName("base version");
		}
		templateEditInfo.setDisconnectAction(template.display().disconnectAction());
		templateEditInfo.setSmartcard(template.display().smartcardEnabled());
		templateEditInfo.setMemory(template.memoryPolicy().guaranteed());
		templateEditInfo.setPhysicalMemory(template.memoryPolicy().guaranteed());
		templateEditInfo.setMaximumMemory(template.memoryPolicy().max());
		templateEditInfo.setVirtualSockets(template.cpu().topology().socketsAsInteger().intValue());
		templateEditInfo.setCoresPerVirtualSocket(template.cpu().topology().coresAsInteger().intValue());
		templateEditInfo.setThreadsPerCore(template.cpu().topology().threadsAsInteger().intValue());
		templateEditInfo.setAffinity(template.placementPolicy().affinity().value());
		templateEditInfo.setAutoConverge(template.migration().autoConverge().value());
		templateEditInfo.setCompressed(template.migration().compressed().value());
		if (template.migration().policyPresent()) {
			templateEditInfo.setCustomMigrationUsed(true);
			templateEditInfo.setCustomMigration(template.migration().policy().id());
			templateEditInfo.setCustomMigrationDowntime(template.migrationDowntime());
		}
		if (template.initializationPresent()) {
			templateEditInfo.setUseCloudInit(template.initializationPresent());
			templateEditInfo.setHostName(template.initialization().hostName());
			templateEditInfo.setTimezone(template.initialization().timezone());
			templateEditInfo.setCustomScript(template.initialization().customScript());
		}
		templateEditInfo.setHighAvailability(template.highAvailability().enabled());
		if (template.leasePresent())
			templateEditInfo.setLeaseStorageDomain(template.lease().storageDomain().id());
		templateEditInfo.setResumeBehaviour(template.storageErrorResumeBehaviour().value());
		templateEditInfo.setPriority(template.highAvailability().priority());
		List<Watchdog> watchdogList
				= getSysSrvHelper().findAllWatchdogsFromTemplate(connection, template.id());
		if (watchdogList.size() > 0) {
			templateEditInfo.setWatchdogModel(watchdogList.get(0).model().value());
			templateEditInfo.setWatchdogAction(watchdogList.get(0).action().value());
		}
		templateEditInfo.setFirstDevice(((BootDevice)template.os().boot().devices().get(0)).value());
		if (template.os().boot().devices().size() > 1)
			templateEditInfo.setSecondDevice(((BootDevice)template.os().boot().devices().get(1)).value());
		List<Cdrom> cdroms
				= getSysSrvHelper().findAllCdromsFromTemplate(connection, template.id());
		if (cdroms.size() > 0 && cdroms.get(0).filePresent()) {
			templateEditInfo.setBootImageUse(true);
			templateEditInfo.setBootImage(cdroms.get(0).file().id());
		}
		templateEditInfo.setCpuProfile(template.cpuProfile().id());
		templateEditInfo.setCpuShare(template.cpuShares());
		templateEditInfo.setMemoryBalloon(template.memoryPolicy().ballooning());
		templateEditInfo.setIoThreadsEnabled(template.io().threads());
		return templateEditInfo;
	}

	@Async("karajanTaskExecutor")
	public String updateTemplate(TemplateEditVo templateEditInfo) {
		Connection connection = adminConnectionService.getConnection();
		SystemService systemService = connection.systemService();
		TemplateService templateService = systemService.templatesService().templateService(templateEditInfo.getId());
		Template response = null;
		try {
			TemplateBuilder templateBuilder = new TemplateBuilder();
			try {
				templateBuilder.cluster(systemService.clustersService().clusterService(templateEditInfo.getCluster()).get().send().cluster());
			} catch (Exception e) {
				throw new Exception("클러스터를 찾을 수 없습니다.");
			}
			OperatingSystemBuilder operatingSystemBuilder = new OperatingSystemBuilder();
			operatingSystemBuilder.type(templateEditInfo.getOperatingSystem());
			templateBuilder.type(VmType.fromValue(templateEditInfo.getType()));
			templateBuilder.name(templateEditInfo.getName());
			if (templateEditInfo.getSubName() != null);
			templateBuilder.description(templateEditInfo.getDescription());
			templateBuilder.stateless(templateEditInfo.getStateless());
			templateBuilder.startPaused(templateEditInfo.getStartInPause());
			templateBuilder.deleteProtected(templateEditInfo.getDeleteProtection());
			DisplayBuilder displayBuilder = new DisplayBuilder();
			displayBuilder.type(DisplayType.VNC);
			displayBuilder.disconnectAction(templateEditInfo.getDisconnectAction());
			displayBuilder.smartcardEnabled(templateEditInfo.getSmartcard());
			templateBuilder.display(displayBuilder);
			CpuBuilder cpuBuilder = new CpuBuilder();
			CpuTopologyBuilder cpuTopologyBuilder = new CpuTopologyBuilder();
			cpuTopologyBuilder.cores(templateEditInfo.getCoresPerVirtualSocket());
			cpuTopologyBuilder.sockets(templateEditInfo.getVirtualSockets());
			cpuTopologyBuilder.threads(templateEditInfo.getThreadsPerCore());
			cpuBuilder.topology(cpuTopologyBuilder);
			templateBuilder.cpu(cpuBuilder);
			MemoryPolicyBuilder memoryPolicy = new MemoryPolicyBuilder();
			memoryPolicy.max(templateEditInfo.getMaximumMemory());
			memoryPolicy.guaranteed(templateEditInfo.getPhysicalMemory());
			memoryPolicy.ballooning(templateEditInfo.getMemoryBalloon());
			templateBuilder.memory(templateEditInfo.getMemory());
			templateBuilder.memoryPolicy(memoryPolicy);
			List<Host> runHosts = new ArrayList<>();
			if (templateEditInfo.getRecommendHost() != null) {
				runHosts.add(((HostService.GetResponse)systemService.hostsService().hostService(templateEditInfo.getRecommendHost()).get().send()).host());
			} else {
				runHosts.add(((HostService.GetResponse)systemService.hostsService().hostService(templateEditInfo.getTargetHost()).get().send()).host());
			}
			VmPlacementPolicyBuilder vmPlacementPolicyBuilder = new VmPlacementPolicyBuilder();
			vmPlacementPolicyBuilder.affinity(VmAffinity.fromValue(templateEditInfo.getAffinity()));
			vmPlacementPolicyBuilder.hosts(runHosts);
			templateBuilder.placementPolicy(vmPlacementPolicyBuilder);
			if (templateEditInfo.getCustomMigrationUsed()) {
				MigrationOptionsBuilder migrationOptionBuilder = new MigrationOptionsBuilder();
				migrationOptionBuilder.autoConverge(InheritableBoolean.fromValue(templateEditInfo.getAutoConverge()));
				migrationOptionBuilder.compressed(InheritableBoolean.fromValue(templateEditInfo.getCompressed()));
				if (templateEditInfo.getCustomMigrationDowntimeUsed())
					templateBuilder.migrationDowntime(templateEditInfo.getCustomMigrationDowntime());
			}
			if (templateEditInfo.getHighAvailability()) {
				StorageDomainLeaseBuilder storageDomainLeaseBuilder = new StorageDomainLeaseBuilder();
				storageDomainLeaseBuilder.storageDomain(((StorageDomainService.GetResponse)systemService.storageDomainsService().storageDomainService(templateEditInfo.getLeaseStorageDomain()).get().send()).storageDomain());
				templateBuilder.lease(storageDomainLeaseBuilder);
			}
			templateBuilder.storageErrorResumeBehaviour(VmStorageErrorResumeBehaviour.fromValue(templateEditInfo.getResumeBehaviour()));
			HighAvailabilityBuilder highAvailabilityBuilder = new HighAvailabilityBuilder();
			highAvailabilityBuilder.priority(templateEditInfo.getPriority());
			if (templateEditInfo.getWatchdogModel() != null) {
				WatchdogBuilder watchdogBuilder = new WatchdogBuilder();
				watchdogBuilder.model(WatchdogModel.fromValue(templateEditInfo.getWatchdogModel()));
				watchdogBuilder.action(WatchdogAction.fromValue(templateEditInfo.getWatchdogAction()));
				templateBuilder.watchdogs(new WatchdogBuilder[] { watchdogBuilder });
			}
			List<BootDevice> bootDevices = new ArrayList<>();
			bootDevices.add(BootDevice.fromValue(templateEditInfo.getFirstDevice()));
			if (templateEditInfo.getSecondDevice() != null && !templateEditInfo.getSecondDevice().equals("") && !templateEditInfo.getSecondDevice().equals("none"))
				bootDevices.add(BootDevice.fromValue(templateEditInfo.getSecondDevice()));
			BootBuilder bootBuilder = new BootBuilder();
			bootBuilder.devices(bootDevices);
			operatingSystemBuilder.boot(bootBuilder);
			templateBuilder.os(operatingSystemBuilder);
			CpuProfileBuilder cpuProfileBuilder = new CpuProfileBuilder();
			try {
				cpuProfileBuilder.cluster(systemService.clustersService().clusterService(templateEditInfo.getCluster()).get().send().cluster());
			} catch (Exception e) {
				throw new Exception("클러스터를 찾을 수 없습니다.");
			}
			templateBuilder.cpuProfile(cpuProfileBuilder);
			templateBuilder.cpuShares(templateEditInfo.getCpuShare());
			IoBuilder ioBuilder = new IoBuilder();
			ioBuilder.threads(templateEditInfo.getIoThreadsEnabled());
			templateBuilder.io(ioBuilder);
			VirtioScsiBuilder virtioScsiBuilder = new VirtioScsiBuilder();
			virtioScsiBuilder.enabled(templateEditInfo.getVirtioScsiEnabled());
			templateBuilder.virtioScsi(virtioScsiBuilder);
			templateService.update().template(templateBuilder).send();
			response = templateService.get().send().template();
			Gson gson = new Gson();
			MessageVo message = new MessageVo(
					"템플릿 수정",
					"템플릿 수정 완료("+ response.name() + ")",
					"success"
			);
			websocketService.sendMessage("/topic/notify", gson.toJson(message));
		} catch (Exception e) {
			try {
				Thread.sleep(5000L);
			} catch (InterruptedException interruptedException) {
				interruptedException.printStackTrace();
			}
			log.error(e.getLocalizedMessage());

			Gson gson = new Gson();
			MessageVo message = new MessageVo(
					"템플릿 수정",
					"템플릿 수정 실패("+ e.getMessage() + ")",
					"error"
			);
			websocketService.sendMessage("/topic/notify", gson.toJson(message));
		}
		return response.id();
	}

	@Async("karajanTaskExecutor")
	@Override
	public void exportTemplate(TemplateVo template) {
		Connection connection = adminConnectionService.getConnection();
		StorageDomain exportDomain
				= getSysSrvHelper().findAllStorageDomains(connection, "export").get(0);
		Gson gson = new Gson();
		MessageVo msg;
		try {
			getSysSrvHelper().exportTemplate(connection, template.getId(), template.getForceOverride(), exportDomain);
			int breaker = 0;
			do {
				List<Template> templates
						= getSysSrvHelper().findAllTemplatesFromStorageDomain(connection, exportDomain.id());
				Thread.sleep(5000L);
				if (templates.size() == 0) { continue; }
				for (Template exportTemplate : templates) {
					if (template.getId().equals(exportTemplate.id())) {
						msg = MessageVo.createMessage(MessageType.TEMPLATE_ADD, true, template.getName(), "");
						websocketService.sendMessage("/topic/notify", gson.toJson(msg));
						breaker = 1;
					}
				}
			} while (breaker <= 0);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			msg = MessageVo.createMessage(MessageType.TEMPLATE_ADD, false, template.getName(), "");
			websocketService.sendMessage("/topic/notify", gson.toJson(msg));
		}
	}

	public boolean checkExportTemplate(String id) {
		Connection connection = connectionService.getConnection();
		List<StorageDomain> storageDomains
				= getSysSrvHelper().findAllStorageDomains(connection, "export");
		boolean exist = false;
		if (storageDomains.size() > 0)
			for (StorageDomain storageDomain : storageDomains) {
				List<Template> templates
						= getSysSrvHelper().findAllTemplatesFromStorageDomain(connection, storageDomain.id());
				if (templates.size() > 0)
					for (Template template : templates)
						if (template.id().equals(id)) {
							exist = true;
							break;
						}
			}
		return exist;
	}
}
