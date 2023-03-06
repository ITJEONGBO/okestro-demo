package com.itinfo.service;

import com.itinfo.model.karajan.ConsolidationVo;
import com.itinfo.model.karajan.KarajanVo;
import java.util.List;

public interface KarajanDashboardService {
	KarajanVo retrieveDataCenterStatus();
	List<ConsolidationVo> consolidateVm(String clusterId);
	String migrateVm(String hostId, String vmId);
	void publishVmStatus(String hostId, String vmId);
	void relocateVms(List<ConsolidationVo> consolidations);
}

