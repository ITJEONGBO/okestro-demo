package com.itinfo.service.impl;

import com.itinfo.model.karajan.ConsolidationVo;
import com.itinfo.model.karajan.KarajanVo;
import com.itinfo.service.KarajanDashboardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KarajanDashboardServiceImpl implements KarajanDashboardService {
	@Override
	public KarajanVo retrieveDataCenterStatus() {
		return null;
	}

	@Override
	public List<ConsolidationVo> consolidateVm(String clusterId) {
		return null;
	}

	@Override
	public String migrateVm(String hostId, String vmId) {
		return null;
	}

	@Override
	public void publishVmStatus(String hostId, String vmId) {

	}

	@Override
	public void relocateVms(List<ConsolidationVo> consolidations) {

	}
}
