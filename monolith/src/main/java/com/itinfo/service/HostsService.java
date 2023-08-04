package com.itinfo.service;

import com.itinfo.model.karajan.ConsolidationVo;
import com.itinfo.model.karajan.HostVo;
import com.itinfo.model.DashboardTopVo;
import com.itinfo.model.EventVo;
import com.itinfo.model.FenceAgentVo;
import com.itinfo.model.HostCreateVo;
import com.itinfo.model.HostDetailVo;
import com.itinfo.model.NetworkAttachmentVo;
import com.itinfo.model.NicUsageApiVo;
import com.itinfo.model.VmSummaryVo;

import java.util.List;

import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.types.Host;
import org.ovirt.engine.sdk4.types.Vm;

public interface HostsService {
	List<ConsolidationVo> maintenanceBeforeConsolidateVms(List<String> hosts);
	void maintenanceStart(List<String> hosts);
	void maintenanceStop(List<String> hosts);
	void restartHost(List<String> hosts);
	void startHost(List<String> hosts);
	void stopHost(List<String> hosts);
	void createHost(HostCreateVo hostCreateVo);
	void updateHost(HostCreateVo hostCreateVo);
	void removeHost(List<String> hosts);
	void setupHostNetwork(List<NicUsageApiVo> paramList);
	void modifyNicNetwork(NetworkAttachmentVo paramNetworkAttachmentVo);

	HostCreateVo retrieveCreateHostInfo(String hostId);
	List<HostDetailVo> retrieveHostsInfo(String status);
	List<HostDetailVo> retrieveLunHostsInfo(String status);
	HostDetailVo retrieveHostDetail(String hostId);
	List<EventVo> retrieveHostEvents(String hostId);
	HostDetailVo getHostInfo(Connection connection, Host host);
	VmSummaryVo getVmInfo(Connection connection, Vm vm);
	List<String> retrieveFanceAgentType();
	Boolean connectTestFenceAgent(FenceAgentVo fenceAgentVo);
	void shutdownHost(List<HostVo> hosts);
	List<DashboardTopVo> retrieveHostsTop(List<HostDetailVo> totalHosts);
}

