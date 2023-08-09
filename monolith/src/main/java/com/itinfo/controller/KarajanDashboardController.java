package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.KarajanDashboardService;
import com.itinfo.model.karajan.ConsolidationVo;
import com.itinfo.model.karajan.KarajanVo;
import com.itinfo.model.karajan.WorkloadVo;
import com.itinfo.service.engine.WorkloadPredictionService;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.ovirt.engine.sdk4.types.VmStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class KarajanDashboardController {
	@Autowired private KarajanDashboardService karajanDashboardService;

	@Autowired private WorkloadPredictionService workloadPredictionService;

	@RequestMapping({"/symphony"})
	public String getDashboardView() {
		log.info("... getDashboardView");
		return "/castanets/karajan/karajan";
	}

	@RequestMapping({"/symphony/retrieveDataCenterStatus"})
	public String retrieveDataCenterStatus(Model model) {
		log.info("... retrieveDataCenterStatus");
		KarajanVo karajan = karajanDashboardService.retrieveDataCenterStatus();
		model.addAttribute(ItInfoConstant.RESULT_KEY, karajan);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/symphony/consolidateVm"})
	public String consolidateVm(String clusterId, Model model) {
		log.info("... consolidateVm('{}')", clusterId);
		List<ConsolidationVo> consolidated = karajanDashboardService.consolidateVm(clusterId);
		model.addAttribute(ItInfoConstant.RESULT_KEY, consolidated);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/symphony/migrateVm"})
	public String migrateVm(String hostId, String vmId, Model model) {
		log.info("... migrateVm('{}', '{}')", hostId, vmId);
		String result = karajanDashboardService.migrateVm(hostId, vmId);
		if (result.equalsIgnoreCase(VmStatus.MIGRATING.value()))
			karajanDashboardService.publishVmStatus(hostId, vmId);
		model.addAttribute(ItInfoConstant.RESULT_KEY, result);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/symphony/relocateVms"})
	public String relocateVms(@RequestBody List<ConsolidationVo> consolidations, Model model) {
		log.info("... relocateVms[{}]", consolidations.size());
		karajanDashboardService.relocateVms(consolidations);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/symphony/workload"})
	public String getWorkload(Model model) {
		log.info("... getWorkload");
		WorkloadVo workload = this.workloadPredictionService.getWorkload();
		model.addAttribute(ItInfoConstant.RESULT_KEY, workload);
		return ItInfoConstant.JSON_VIEW;
	}
}