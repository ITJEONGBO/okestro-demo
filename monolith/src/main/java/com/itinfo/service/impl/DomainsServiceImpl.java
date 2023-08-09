package com.itinfo.service.impl;

import com.itinfo.dao.DomainsDao;
import com.itinfo.model.*;
import com.itinfo.service.DisksService;
import com.itinfo.service.DomainsService;
import com.itinfo.service.engine.AdminConnectionService;
import com.itinfo.service.engine.WebsocketService;

import lombok.extern.slf4j.Slf4j;

import com.google.gson.Gson;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.builders.Builders;
import org.ovirt.engine.sdk4.builders.DataCenterBuilder;
import org.ovirt.engine.sdk4.builders.HostStorageBuilder;
import org.ovirt.engine.sdk4.builders.StorageDomainBuilder;
import org.ovirt.engine.sdk4.internal.containers.LogicalUnitContainer;
import org.ovirt.engine.sdk4.services.AttachedStorageDomainService;
import org.ovirt.engine.sdk4.services.AttachedStorageDomainsService;
import org.ovirt.engine.sdk4.services.DataCenterService;

import org.ovirt.engine.sdk4.services.HostService;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.types.DataCenter;
import org.ovirt.engine.sdk4.types.DiskSnapshot;
import org.ovirt.engine.sdk4.types.Event;
import org.ovirt.engine.sdk4.types.File;
import org.ovirt.engine.sdk4.types.Host;
import org.ovirt.engine.sdk4.types.HostStorage;
import org.ovirt.engine.sdk4.types.IscsiDetails;
import org.ovirt.engine.sdk4.types.LogicalUnit;
import org.ovirt.engine.sdk4.types.NfsVersion;
import org.ovirt.engine.sdk4.types.StorageDomain;
import org.ovirt.engine.sdk4.types.StorageDomainStatus;
import org.ovirt.engine.sdk4.types.StorageDomainType;
import org.ovirt.engine.sdk4.types.StorageType;
import org.ovirt.engine.sdk4.types.Vm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DomainsServiceImpl extends BaseService implements DomainsService {
	@Autowired private AdminConnectionService adminConnectionService;
	@Autowired private DomainsDao domainsDao;
	@Autowired private DisksService disksService;
	@Autowired private WebsocketService websocketService;

	@Override
	public List<StorageDomainVo> retrieveStorageDomains(String status, String domainType) {
		log.info("... retrieveStorageDomains('{}', '{}')", status, domainType);
		Connection connection = adminConnectionService.getConnection();
		/*
		String dataCenterId
				= getSysSrvHelper().findAllDataCenters(connection).get(0).id();
		*/
		List<StorageDomain> storageDomains
				= getSysSrvHelper().findAllStorageDomains(connection, "");

		if (StorageDomainStatus.ACTIVE.value().equalsIgnoreCase(status))
			storageDomains = getSysSrvHelper().findAllStorageDomains(connection, "status=active");
		else if (StorageDomainStatus.INACTIVE.value().equalsIgnoreCase(status))
			storageDomains = getSysSrvHelper().findAllStorageDomains(connection, "status!=active");
		else
			storageDomains = getSysSrvHelper().findAllStorageDomains(connection, "");

		List<StorageDomainVo> StorageDomainVoList
				= ModelsKt.toStorageDomainVos(storageDomains, connection);
		/*
		List<DiskProfile> diskProfiles
				= getSysSrvHelper().findAllDiskProfiles(connection);
		storageDomains.forEach(storageDomain -> {
			if ("all".equals(domainType) || storageDomain.type().name().equalsIgnoreCase(domainType)) {
				StorageDomainVo storageDomainVo = new StorageDomainVo();
				storageDomainVo.setId(storageDomain.id());
				storageDomainVo.setName(storageDomain.name());
				storageDomainVo.setType(storageDomain.type().name());
				storageDomainVo.setComment(storageDomain.comment());
				storageDomainVo.setDescription(storageDomain.description());
				storageDomainVo.setDiskFree(storageDomain.available());
				storageDomainVo.setDiskUsed(storageDomain.used());
				storageDomainVo.setStorageFormat(storageDomain.storageFormat().name());
				storageDomainVo.setStorageAddress(storageDomain.storage().address());
				storageDomainVo.setStoragePath(storageDomain.storage().path());
				storageDomainVo.setStorageType(storageDomain.storage().type().name());
				if (storageDomain.status() == null) {
					try {
						StorageDomain sd
								= getSysSrvHelper().findAttachedStorageDomainFromDataCenter(connection, dataCenterId, storageDomain.id());
						storageDomainVo.setStatus(sd.status().value());
					} catch (Exception e) {
						log.error(e.getLocalizedMessage());
						storageDomainVo.setStatus(null);
					}
				} else {
					storageDomainVo.setStatus(storageDomain.status().value());
				}
				if (storageDomain.type().name().equals(StorageDomainType.ISO.name())) {
					List<ImageFileVo> imageFiles = new ArrayList<>();
					List<File> files = getSysSrvHelper().findAllFilesFromStorageDomain(connection, storageDomain.id());
					// files.forEach();
					storageDomainVo.setImageFileList(imageFiles);
				}

				for (DiskProfile dp : diskProfiles) {
					if (dp.storageDomain().id().equals(storageDomain.id())) {
						storageDomainVo.setDiskProfileId(dp.id());
						storageDomainVo.setDiskProfileName(dp.name());
						break;
					}
				}
				StorageDomainVoList.add(storageDomainVo);
			}
		});
		*/
		return StorageDomainVoList;
	}

	@Async("karajanTaskExecutor")
	@Override
	public void maintenanceStart(List<String> domains) {
		log.info("... maintenanceStart[{}]", domains.size());
		Connection connection = adminConnectionService.getConnection();
		MessageVo message = new MessageVo();
		message.setTitle("스토리지 도메인 유지보수 모드");
		for (String id : domains) {
			String dataCenterId
					= getSysSrvHelper().findAllStorageDomains(connection, id).get(0).id();
			StorageDomain domain
					= getSysSrvHelper().findAttachedStorageDomainFromDataCenter(connection, dataCenterId, id);

			try {
				if (domain.status() != StorageDomainStatus.MAINTENANCE)
					getSysSrvHelper().deactiveAttachedStorageDomainFromDataCenter(connection, dataCenterId, id);
				do {
					Thread.sleep(5000L);
					domain
						= getSysSrvHelper().findAttachedStorageDomainFromDataCenter(connection, dataCenterId, id);
				} while (domain.status() != StorageDomainStatus.MAINTENANCE);
				message.setText("스토리지 도메인 유지보수 모드 완료("+ domain.name() + ")");
				message.setStyle("success");
			} catch (Exception e) {
				e.printStackTrace();
				message.setText("스토리지 도메인 유지보수 모드 실패("+ domain.name() + ")");
				message.setStyle("error");
			}
			this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
			this.websocketService.sendMessage("/topic/domains/reload", "");
		}
	}

	@Async("karajanTaskExecutor")
	@Override
	public void maintenanceStop(List<String> domains) {
		log.info("... maintenanceStop[{}]", domains.size());
		Connection connection = adminConnectionService.getConnection();
		MessageVo message = new MessageVo();
		message.setTitle("스토리지 도메인 활성 모드");
		for (String id : domains) {
			String dataCenterId
					= getSysSrvHelper().findAllStorageDomains(connection, id).get(0).id();
			StorageDomain domain
					= getSysSrvHelper().findAttachedStorageDomainFromDataCenter(connection, dataCenterId, id);
			try {
				if (domain.status() == StorageDomainStatus.MAINTENANCE ||
					domain.status() == StorageDomainStatus.PREPARING_FOR_MAINTENANCE)
					getSysSrvHelper().activeAttachedStorageDomainFromDataCenter(connection, dataCenterId, id);
				do {
					Thread.sleep(5000L);
					domain
							= getSysSrvHelper().findAttachedStorageDomainFromDataCenter(connection, dataCenterId, id);
				} while (domain.status() != StorageDomainStatus.ACTIVE);
				message.setText("스토리지 도메인 활성 모드 완료("+ domain.name() + ")");
				message.setStyle("success");
			} catch (Exception e) {
				e.printStackTrace();
				message.setText("스토리지 도메인 활성 모드 실패("+ domain.name() + ")");
				message.setStyle("error");
			}
			this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
			this.websocketService.sendMessage("/topic/domains/reload", "");
		}
	}

	@Override
	public StorageDomainVo retrieveStorageDomain(String id) {
		log.info("... retrieveStorageDomain('{}')", id);
		Connection connection = adminConnectionService.getConnection();
		StorageDomain storageDomain
				= getSysSrvHelper().findStorageDomain(connection, id);
		StorageDomainVo storageDomainVo = new StorageDomainVo();
		storageDomainVo.setId(storageDomain.id());
		storageDomainVo.setName(storageDomain.name());
		storageDomainVo.setType(storageDomain.type().name());
		if (storageDomain.statusPresent())
			storageDomainVo.setStatus(storageDomain.status().value());
		storageDomainVo.setComment(storageDomain.comment());
		storageDomainVo.setDescription(storageDomain.description());
		storageDomainVo.setDiskFree(storageDomain.available());
		storageDomainVo.setDiskUsed(storageDomain.used());
		storageDomainVo.setStorageFormat(storageDomain.storageFormat().name());
		if (storageDomain.storagePresent() && storageDomain.storage().addressPresent()) {
			storageDomainVo.setStorageAddress(storageDomain.storage().address());
		} else if (storageDomain.storagePresent() && !storageDomain.storage().addressPresent()) {
			storageDomainVo.setStorageAddress("Domain ID");
		}
		if (storageDomain.storagePresent() && storageDomain.storage().pathPresent()) {
			storageDomainVo.setStoragePath(storageDomain.storage().path());
		} else if (storageDomain.storagePresent() && !storageDomain.storage().addressPresent()) {
			storageDomainVo.setStoragePath(storageDomain.id());
		}
		storageDomainVo.setStorageType(storageDomain.storage().type().name());
		storageDomainVo.setStorageDomainUsages(retrieveStorageDomainUsage(id));
		storageDomainVo.setDiskVoList(this.disksService.retrieveDisks(storageDomain.name()));
		if (storageDomain.type().name().equals(StorageDomainType.ISO.name())) {
			List<File> files
					= getSysSrvHelper().findAllFilesFromStorageDomain(connection, id);
			storageDomainVo.setImageFileList(files.stream().map(e -> {
				ImageFileVo imageFile = new ImageFileVo();
				imageFile.setId(e.id());
				imageFile.setName(e.name());
				return imageFile;
			}).collect(Collectors.toList()));
		}
		List<Vm> vmList
				= getSysSrvHelper().findAllVms(connection, "");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		List<VmVo> attaachedVmList = vmList.stream().filter(vm ->
			!getSysSrvHelper().findAllDiskAttachmentsFromVm(connection, vm.id()).isEmpty()
		).map(vm -> {
			VmVo attachedVm = new VmVo();
			attachedVm.setId(getSysSrvHelper().findAllDiskAttachmentsFromVm(connection, vm.id()).get(0).id());
			attachedVm.setName(vm.name());
			Long cdateLong = getSysSrvHelper().findAllSnapshotsFromVm(connection, vm.id()).get(0).date().getTime();
			attachedVm.setCdate(format.format(cdateLong));
			return attachedVm;
		}).collect(Collectors.toList());

		List<DiskSnapshot> snapshots
				= getSysSrvHelper().findAllDiskSnapshotsFromStorageDomain(connection, id);
		List<DiskVo> diskSnapshotVoList = new ArrayList<>();
		snapshots.forEach(snapshot -> {
			DiskVo diskSnapshotVo = new DiskVo();
			diskSnapshotVo.setId(snapshot.id());
			diskSnapshotVo.setStatus(snapshot.status().name());
			diskSnapshotVo.setActualSize(snapshot.actualSize().toString());
			diskSnapshotVo.setDescription(snapshot.description());
			diskSnapshotVo.setName(snapshot.name());
			diskSnapshotVo.setType(snapshot.contentType().toString());
			for (VmVo vmVo : attaachedVmList) {
				if (vmVo.getId().equals(snapshot.disk().id())) {
					diskSnapshotVo.setAttachedTo(((VmVo) vmVo).getName());
					diskSnapshotVo.setCdate(((VmVo) vmVo).getCdate());
				}
			}
			diskSnapshotVoList.add(diskSnapshotVo);
		});
		storageDomainVo.setDiskSnapshotVoList(diskSnapshotVoList);
		return storageDomainVo;
	}

	@Override
	public StorageDomainCreateVo retrieveCreateDomainInfo(String storageDomainId) {
		log.info("... retrieveCreateDomainInfo('{}')", storageDomainId);
		Connection connection = adminConnectionService.getConnection();
		StorageDomain storageDomain
				= getSysSrvHelper().findStorageDomain(connection, storageDomainId);
		StorageDomainCreateVo storageDomainCreateVo = new StorageDomainCreateVo();
		storageDomainCreateVo.setId(storageDomainId);
		storageDomainCreateVo.setName(storageDomain.name());
		storageDomainCreateVo.setDescription(storageDomain.description());
		storageDomainCreateVo.setDomainType(storageDomain.type().name().toLowerCase());
		storageDomainCreateVo.setPath(storageDomain.storage().address() + ":" + storageDomain.storage().path());
		storageDomainCreateVo.setStorageType(storageDomain.storage().type().name());
		if (StorageType.ISCSI.value().equalsIgnoreCase(storageDomain.storage().type().name())) {
			IscsiVo iscsiVo = new IscsiVo();
			iscsiVo.setAddress((storageDomain.storage().volumeGroup().logicalUnits().get(0)).address());
			iscsiVo.setPort(String.valueOf((storageDomain.storage().volumeGroup().logicalUnits().get(0)).port()));
			iscsiVo.setTarget((storageDomain.storage().volumeGroup().logicalUnits().get(0)).target());
			storageDomainCreateVo.setIscsi(iscsiVo);
		}
		return storageDomainCreateVo;
	}

	@Override
	public List<List<String>> retrieveStorageDomainUsage(String storageDomainId) {
		log.info("... retrieveStorageDomainUsage('{}')", storageDomainId);
		List<StorageDomainUsageVo> storageDomainUsageVoList = this.domainsDao.retrieveStorageDomainUsage(storageDomainId);
		return storageDomainUsageVoList.stream().map(e -> {
			List<String> storageDomain = new ArrayList<>();
			storageDomain.add(e.getHistoryDatetime());
			storageDomain.add(String.valueOf(e.getUsedDiskSizeGb() / (e.getAvailableDiskSizeGb() + e.getUsedDiskSizeGb()) * 100.0D));
			return storageDomain;
		}).collect(Collectors.toList());
	}

	@Override
	public List<EventVo> retrieveDomainEvents(String id) {
		log.info("... retrieveDomainEvents('{}')", id);
		Connection connection = adminConnectionService.getConnection();
		List<Event> items
				= getSysSrvHelper().findAllEvents(connection, "");
		return items.stream().filter(e -> e.storageDomainPresent() && id.equals(e.storageDomain().id())).map(ModelsKt::toEventVo).collect(Collectors.toList());
	}

	@Override
	public List<HostDetailVo> retrieveHosts() {
		log.info("... retrieveHosts");
		Connection connection = adminConnectionService.getConnection();
		List<Host> hosts =
				getSysSrvHelper().findAllHosts(connection, "");
		return hosts.stream().map(e -> {
			HostDetailVo hostDetailVo = new HostDetailVo();
			hostDetailVo.setId(e.id());
			hostDetailVo.setName(e.name());
			return hostDetailVo;
		}).collect(Collectors.toList());
	}


	@Async("karajanTaskExecutor")
	@Override
	public void createDomain(StorageDomainCreateVo storageDomainCreateVo) {
		log.info("... createDomain");
		Connection connection = adminConnectionService.getConnection();
		SystemService systemService = connection.systemService();
		DataCenter dataCenter =
				getSysSrvHelper().findAllDataCenters(connection).get(0);
		DataCenterService dcService = systemService.dataCentersService().dataCenterService(dataCenter.id());
		Host host
				= getSysSrvHelper().findHost(connection, storageDomainCreateVo.getHostId());
		MessageVo message = new MessageVo();
		message.setTitle("스토리지 도메인 생성");
		try {
			StorageDomain s, sd = null;
			if (StorageType.NFS.value().equalsIgnoreCase(storageDomainCreateVo.getStorageType())) {
				String address;
				String path;
				String[] pathArr = storageDomainCreateVo.getPath().split(":");
				if (pathArr.length == 2) {
					address = pathArr[0];
					path = pathArr[1];
				} else {
					return;
				}
				HostStorageBuilder hostStorageBuilder = new HostStorageBuilder();
				hostStorageBuilder.type(StorageType.NFS);
				hostStorageBuilder.nfsVersion(NfsVersion.AUTO);
				hostStorageBuilder.address(address);
				hostStorageBuilder.path(path);
				StorageDomainBuilder storageDomainBuilder = new StorageDomainBuilder();
				storageDomainBuilder.name(storageDomainCreateVo.getName());
				if (!storageDomainCreateVo.getDescription().isEmpty())
					storageDomainBuilder.description(storageDomainCreateVo.getDescription());
				storageDomainBuilder.type(StorageDomainType.fromValue(storageDomainCreateVo.getDomainType()));
				storageDomainBuilder.host(Builders.host().name(host.name()));
				storageDomainBuilder.storage(hostStorageBuilder);
				sd
						= getSysSrvHelper().addStorageDomain(connection, storageDomainBuilder.build());
			} else if (StorageType.ISCSI.value().equalsIgnoreCase(storageDomainCreateVo.getStorageType())) {
				List<HostStorage> list
						= getSysSrvHelper().findAllStoragesFromHost(connection, storageDomainCreateVo.getHostId());
				HostStorageBuilder hostStorageBuilder = new HostStorageBuilder()
						.type(StorageType.ISCSI)
						.target(storageDomainCreateVo.getIscsi().getTarget())
						.address(storageDomainCreateVo.getIscsi().getAddress())
						.port(Integer.parseInt(storageDomainCreateVo.getIscsi().getPort()))
						.logicalUnits((list.get(list.size()-1)).logicalUnits())
						.overrideLuns(true);
				if (!storageDomainCreateVo.getIscsi().getId().isEmpty())
					hostStorageBuilder.username(storageDomainCreateVo.getIscsi().getId());
				if (!storageDomainCreateVo.getIscsi().getPassword().isEmpty())
					hostStorageBuilder.password(storageDomainCreateVo.getIscsi().getPassword());
				StorageDomainBuilder storageDomainBuilder = new StorageDomainBuilder();
				storageDomainBuilder.name(storageDomainCreateVo.getName());
				if (!storageDomainCreateVo.getDescription().isEmpty())
					storageDomainBuilder.description(storageDomainCreateVo.getDescription());
				storageDomainBuilder.type(StorageDomainType.fromValue(storageDomainCreateVo.getDomainType()));
				storageDomainBuilder.host(Builders.host().name(host.name()));
				storageDomainBuilder.discardAfterDelete(false);
				storageDomainBuilder.supportsDiscard(false);
				storageDomainBuilder.backup(false);
				storageDomainBuilder.wipeAfterDelete(false);
				storageDomainBuilder.storage(hostStorageBuilder);
				sd =
					getSysSrvHelper().addStorageDomain(connection, storageDomainBuilder.build());
			} else if (StorageType.FCP.value().equalsIgnoreCase(storageDomainCreateVo.getStorageType())) {
				HostStorageBuilder hostStorageBuilder = new HostStorageBuilder();
				StorageDomainBuilder storageDomainBuilder = new StorageDomainBuilder();
				hostStorageBuilder.type(StorageType.FCP);
				List<LunVo> lunVos = storageDomainCreateVo.getLunVos();
				List<LogicalUnit> logicalUnitList = new ArrayList<>();
				lunVos.forEach(lunVo -> {
					LogicalUnitContainer logicalUnitContainer = new LogicalUnitContainer();
					logicalUnitContainer.id(lunVo.getId());
					logicalUnitContainer.paths(new BigInteger(lunVo.getPath()));
					logicalUnitContainer.productId(lunVo.getProductId());
					logicalUnitContainer.serial(lunVo.getProductId());
					logicalUnitContainer.size(new BigInteger(lunVo.getSize()));
					logicalUnitList.add(logicalUnitContainer);
				});
				hostStorageBuilder.logicalUnits(logicalUnitList);
				storageDomainBuilder.storage(hostStorageBuilder);
				storageDomainBuilder.name(storageDomainCreateVo.getName());
				storageDomainBuilder.type(StorageDomainType.fromValue(storageDomainCreateVo.getDomainType()));
				storageDomainBuilder.host(Builders.host().name(host.name()));
				sd =
						getSysSrvHelper().addStorageDomain(connection, storageDomainBuilder.build());
			}
			do {
				try { Thread.sleep(2000L); } catch (InterruptedException interruptedException) { log.error(interruptedException.getLocalizedMessage()); }
				s =
					getSysSrvHelper().findStorageDomain(connection, sd.id());
			} while (s.status() != StorageDomainStatus.UNATTACHED);
			AttachedStorageDomainsService attachedSdsService = dcService.storageDomainsService();
			try {
				attachedSdsService.add()
						.storageDomain(Builders.storageDomain().id(sd.id()).dataCenter((new DataCenterBuilder()).id(dataCenter.id())))
						.send();
			} catch (Exception e) {
				e.printStackTrace();
			}
			AttachedStorageDomainService attachedSdService = attachedSdsService.storageDomainService(sd.id());
			do {
				try { Thread.sleep(2000L); } catch (InterruptedException interruptedException) { log.error(interruptedException.getLocalizedMessage()); }
				sd = getSysSrvHelper().findAttachedStorageDomainFromDataCenter(connection, dataCenter.id(), sd.id());
			} while (sd.status() != StorageDomainStatus.ACTIVE);
			message.setText("스토리지 도메인 생성 성공("+ storageDomainCreateVo.getName() + ")");
			message.setStyle("success");
		} catch (Exception e) {
			e.printStackTrace();
			message.setText("스토리지 도메인 생성 실패("+ storageDomainCreateVo.getName() + ")");
			message.setStyle("error");
		}
		this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
		this.websocketService.sendMessage("/topic/domains/reload", "");
	}


	@Async("karajanTaskExecutor")
	@Override
	public void updateDomain(StorageDomainCreateVo storageDomainCreateVo) {
		log.info("... updateDomain");
		Connection connection = adminConnectionService.getConnection();
		MessageVo message = new MessageVo();
		message.setTitle("스토리지 도메인 수정");
		try {
			StorageDomainBuilder sdb = new StorageDomainBuilder();
			sdb.name(storageDomainCreateVo.getName());
			sdb.description(storageDomainCreateVo.getDescription());
			getSysSrvHelper().updateStorageDomain(connection, storageDomainCreateVo.getId(), sdb.build());
			message.setText("스토리지 도메인 수정 완료("+ storageDomainCreateVo.getName() + ")");
			message.setStyle("success");
		} catch (Exception e) {
			e.printStackTrace();
			message.setText("스토리지 도메인 수정 실패("+ storageDomainCreateVo.getName() + ")");
			message.setStyle("error");
		}
		try { Thread.sleep(1000L); } catch (InterruptedException interruptedException) { log.error(interruptedException.getLocalizedMessage()); }
		this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
		this.websocketService.sendMessage("/topic/domains/reload", "");
	}


	@Async("karajanTaskExecutor")
	@Override
	public void removeDomain(StorageDomainVo storageDomainVo) {
		log.info("... removeDomain");
		Connection connection = adminConnectionService.getConnection();
		MessageVo message = new MessageVo();
		message.setTitle("스토리지 도메인 삭제");

		StorageDomain sd
				= getSysSrvHelper().findStorageDomain(connection, storageDomainVo.getId());
		String dataCenterId
				= getSysSrvHelper().findAllDataCenters(connection).get(0).id();
		try {
			if (sd.dataCenters() != null && sd.dataCenters().size() != 0) {
				sd
						= getSysSrvHelper().findAttachedStorageDomainFromDataCenter(connection,dataCenterId, storageDomainVo.getId());
				if (!StorageDomainStatus.MAINTENANCE.value().equalsIgnoreCase(sd.status().value()))
					return;
				try {
					getSysSrvHelper().removeAttachedStorageDomainFromDataCenter(connection, dataCenterId, storageDomainVo.getId());
				} catch (Exception e) {
					log.error(e.getLocalizedMessage());
					e.printStackTrace();
				}
			}
			do {
				try { Thread.sleep(2000L); } catch (InterruptedException interruptedException) { log.error(interruptedException.getLocalizedMessage()); }
				sd
						= getSysSrvHelper().findAttachedStorageDomainFromDataCenter(connection, dataCenterId, storageDomainVo.getId());

			} while (sd.status() == null || sd.status() != StorageDomainStatus.UNATTACHED);
			try {
				try { Thread.sleep(3000L); } catch (InterruptedException interruptedException) { log.error(interruptedException.getLocalizedMessage()); }
				getSysSrvHelper().removeStorageDomain(connection, storageDomainVo.getId(), storageDomainVo.getFormat());
			} catch (Exception e) {
				log.error(e.getLocalizedMessage());
				e.printStackTrace();
			}
			try { Thread.sleep(2000L); } catch (InterruptedException interruptedException) { log.error(interruptedException.getLocalizedMessage()); }
			message.setText("스토리지 도메인 삭제 완료("+ sd.name()+ ")");
			message.setStyle("success");
		} catch (Exception e) {
			e.printStackTrace();
			message.setText("스토리지 도메인 삭제 실패("+ sd.name() + ")");
			message.setStyle("error");
		}
		this.websocketService.sendMessage("/topic/notify", (new Gson()).toJson(message));
		this.websocketService.sendMessage("/topic/domains/reload", "");
	}

	@Override
	public List<IscsiVo> iscsiDiscover(StorageDomainCreateVo storageDomainCreateVo) {
		log.info("... iscsiDiscover");
		Connection connection = adminConnectionService.getConnection();
		List<IscsiDetails> iscsisDiscovered = getSysSrvHelper().findAllIscsiDetailsFromHost(connection, storageDomainCreateVo.getHostId(),
				Builders.iscsiDetails()
						.address(storageDomainCreateVo.getIscsi().getAddress())
						.port(Integer.parseInt(storageDomainCreateVo.getIscsi().getPort()))
						.build()
		);

		return iscsisDiscovered.stream().map(e -> {
			log.info(e.address() + ":" + e.target());
			IscsiVo iscsi = new IscsiVo();
			iscsi.setAddress(e.address());
			iscsi.setPort(String.valueOf(e.port()));
			iscsi.setTarget(e.target());
			return iscsi;
		}).collect(Collectors.toList());
	}

	@Override
	public boolean iscsiLogin(StorageDomainCreateVo storageDomainCreateVo) {
		log.info("... iscsiLogin");
		Connection connection = adminConnectionService.getConnection();
		SystemService systemService = connection.systemService();
		HostService hostService = systemService.hostsService().hostService(storageDomainCreateVo.getHostId());
		try {
			hostService.iscsiLogin()
					.iscsi(Builders.iscsiDetails().address(storageDomainCreateVo.getIscsi().getAddress())
							.port(Integer.parseInt(storageDomainCreateVo.getIscsi().getPort()))
							.target(storageDomainCreateVo.getIscsi().getTarget()))
					.send();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
