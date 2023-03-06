package com.itinfo.controller;

import com.itinfo.service.KarajanDashboardService;
import com.itinfo.model.karajan.ConsolidationVo;
import com.itinfo.model.karajan.KarajanVo;
import com.itinfo.model.karajan.WorkloadVo;

import java.util.List;

import com.itinfo.service.engine.WorkloadPredictionService;
import org.ovirt.engine.sdk4.types.VmStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class KarajanDashboardController {
	@Autowired private KarajanDashboardService karajanDashboardService;

	@Autowired private WorkloadPredictionService workloadPredictionService;

	@RequestMapping({"/symphony"})
	public String getDashboardView() {
		return "/castanets/karajan/karajan";
	}

	@RequestMapping({"/symphony/retrieveDataCenterStatus"})
	public String retrieveDataCenterStatus(Model model) {
		KarajanVo karajan = this.karajanDashboardService.retrieveDataCenterStatus();
		model.addAttribute("resultKey", karajan);
		return "jsonView";
	}

	@RequestMapping({"/symphony/consolidateVm"})
	public String consolidateVm(String clusterId, Model model) {
		List<ConsolidationVo> consolidated = this.karajanDashboardService.consolidateVm(clusterId);
		model.addAttribute("resultKey", consolidated);
		return "jsonView";
	}

	@RequestMapping({"/symphony/migrateVm"})
	public String migrateVm(String hostId, String vmId, Model model) {
		String result = this.karajanDashboardService.migrateVm(hostId, vmId);
		if (result.equalsIgnoreCase(VmStatus.MIGRATING.value()))
			this.karajanDashboardService.publishVmStatus(hostId, vmId);
		model.addAttribute("resultKey", result);
		return "jsonView";
	}

	@RequestMapping({"/symphony/relocateVms"})
	public String relocateVms(@RequestBody List<ConsolidationVo> consolidations, Model model) {
		this.karajanDashboardService.relocateVms(consolidations);
		return "jsonView";
	}

	@RequestMapping({"/symphony/workload"})
	public String getWorkload(Model model) {
		WorkloadVo workload = this.workloadPredictionService.getWorkload();
		model.addAttribute("resultKey", workload);
		return "jsonView";
	}
}