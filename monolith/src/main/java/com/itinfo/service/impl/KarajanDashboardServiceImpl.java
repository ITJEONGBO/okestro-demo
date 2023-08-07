package com.itinfo.service.impl;

import com.google.gson.Gson;
import com.itinfo.model.*;
import com.itinfo.model.karajan.ClusterVo;
import com.itinfo.model.karajan.HostVo;
import com.itinfo.model.karajan.ConsolidationVo;
import com.itinfo.model.karajan.KarajanVo;
import com.itinfo.service.HostsService;
import com.itinfo.service.KarajanDashboardService;
import com.itinfo.service.SystemPropertiesService;
import com.itinfo.service.consolidation.Ffd;
import com.itinfo.service.engine.AdminConnectionService;
import com.itinfo.service.engine.ConnectionService;
import com.itinfo.service.engine.KarajanService;
import com.itinfo.service.engine.WebsocketService;

import lombok.extern.slf4j.Slf4j;

import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.types.Host;
import org.ovirt.engine.sdk4.types.Vm;
import org.ovirt.engine.sdk4.types.VmStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KarajanDashboardServiceImpl extends BaseService implements KarajanDashboardService {

	@Autowired private ConnectionService connectionService;
	@Autowired private AdminConnectionService adminConnectionService;
 	@Autowired private KarajanService karajanService;
	@Autowired private Ffd ffd;

	@Autowired private WebsocketService websocketService;
	@Autowired private SystemPropertiesService systemPropertiesService;
	@Autowired private HostsService hostsService;

	@Override
	public KarajanVo retrieveDataCenterStatus() {
		log.info("... retrieveDataCenterStatus");
		return this.karajanService.getDataCenter();
	}

	@Override
	public List<ConsolidationVo> consolidateVm(String clusterId) {
		log.info("... consolidateVm('{}')", clusterId);
		KarajanVo karajan = this.karajanService.getDataCenter();
		return this.ffd.optimizeDataCenter(karajan, clusterId);
	}

	@Override
	public String migrateVm(String hostId, String vmId) {
		log.info("... migrateVm('{}', '{}')", hostId, vmId);
		Connection connection = this.connectionService.getConnection();
		Host host
				= getSysSrvHelper().findHost(connection, hostId);
		String result;
		try {
			getSysSrvHelper().migrateHostFromVm(connection, vmId, host);
			try { Thread.sleep(3000L); } catch (InterruptedException e) { log.error(e.getLocalizedMessage()); }
			result = VmStatus.MIGRATING.value();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getLocalizedMessage());
			result = e.getMessage();
		}
		return result;
	}

	@Async("karajanTaskExecutor")
	@Override
	public void publishVmStatus(String hostId, String vmId) {
		log.info("... publishVmStatus('{}', '{}')", hostId, vmId);
		Connection connection = this.adminConnectionService.getConnection();

		Vm vm;
		do {
			try { Thread.sleep(3000L); } catch (InterruptedException e) { log.error(e.getLocalizedMessage()); }
			vm = getSysSrvHelper().findVm(connection, vmId);
		} while (vm.status() != VmStatus.UP);

		notify(vm.name(), hostId.equals(vm.host().id()) ? "success" : "error");
	}

	@Async("karajanTaskExecutor")
	@Override
	public void relocateVms(List<ConsolidationVo> consolidations) {
		log.info("... relocateVms");
		Connection connection = this.adminConnectionService.getConnection();
		Vm vm = null;
		for (ConsolidationVo consolidation : consolidations) {
			Host host =
					getSysSrvHelper().findHost(connection, consolidation.getHostId());
			try {
				getSysSrvHelper().migrateHostFromVm(connection, consolidation.getVmId(), host);
			} catch (Exception e) {
				notify(consolidation.getVmName(), "error");
				break;
			}

			do {
				try { Thread.sleep(3000L); } catch (InterruptedException e) { log.error(e.getLocalizedMessage());  }
				getSysSrvHelper().findVm(connection, consolidation.getVmId());
			} while (vm.status() != VmStatus.UP);
			if (consolidation.getHostId().equals(vm.host().id())) {
				notify(vm.name(), "success");
				continue;
			}
			notify(vm.name(), "error");
		}

		SystemPropertiesVo properties = this.systemPropertiesService.retrieveSystemProperties();
		if (properties.getSymphonyPowerControll())
			turnOffHosts(consolidations);
	}

	private void turnOffHosts(List<ConsolidationVo> consolidations) {
		log.info("... turnOffHosts");
		KarajanVo karajan = this.karajanService.getDataCenter();
		for (ClusterVo cluster : karajan.getClusters()) {
			for (HostVo host : cluster.getHosts()) {
				if ((consolidations.get(0)).getHostId().equals(host.getId())) {
					List<HostVo> turnOffHosts = cluster.getHosts().stream().filter(target ->
							target.getVms().size() == 0
					).collect(Collectors.toList());
					this.hostsService.shutdownHost(turnOffHosts);
				}
			}
		}
	}

	private void notify(String vmName, String status) {
		MessageVo message = "success".equals(status)
				? MessageVo.createMessage(MessageType.VIRTUAL_MACHINE_RELOCATE, true, vmName, "")
				: MessageVo.createMessage(MessageType.VIRTUAL_MACHINE_RELOCATE, false, vmName, "");
		this.websocketService.sendMessage(
				"success".equals(status)
						? "/topic/migrateVm"
						: "/topic/notify", new Gson().toJson(message)
		);
	}
}
