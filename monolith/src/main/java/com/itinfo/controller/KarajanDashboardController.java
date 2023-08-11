package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.KarajanDashboardService;
import com.itinfo.model.karajan.ConsolidationVo;
import com.itinfo.model.karajan.KarajanVo;
import com.itinfo.model.karajan.WorkloadVo;
import com.itinfo.service.engine.WorkloadPredictionService;

import java.util.List;

import io.swagger.annotations.*;

import lombok.extern.slf4j.Slf4j;

import org.ovirt.engine.sdk4.types.VmStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
@Api(value = "KarajanDashboardController", tags = {"karajan-dashboard"})
public class KarajanDashboardController extends BaseController {
	@Autowired private KarajanDashboardService karajanDashboardService;

	@Autowired private WorkloadPredictionService workloadPredictionService;

	@ApiOperation(httpMethod = "GET", value = "getDashboardView", notes = " 페이지 이동 > /symphony")
	@ApiImplicitParams({
			@ApiImplicitParam(name="user", value = "갱신할 사용자 정보")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/symphony"})
	public String getDashboardView() {
		log.info("... getDashboardView");
		return "/castanets/karajan/karajan";
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveDataCenterStatus", notes = "데이터 센터 상태 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/symphony/retrieveDataCenterStatus"})
	public String retrieveDataCenterStatus(Model model) {
		log.info("... retrieveDataCenterStatus");
		KarajanVo karajan = karajanDashboardService.retrieveDataCenterStatus();
		model.addAttribute(ItInfoConstant.RESULT_KEY, karajan);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "consolidateVm", notes = "???") // TODO: 확인 필요
	@ApiImplicitParams({
			@ApiImplicitParam(name="clusterId", value = "클러스터 ID")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/symphony/consolidateVm"})
	public String consolidateVm(String clusterId, 
								Model model) {
		log.info("... consolidateVm('{}')", clusterId);
		List<ConsolidationVo> consolidated = karajanDashboardService.consolidateVm(clusterId);
		model.addAttribute(ItInfoConstant.RESULT_KEY, consolidated);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "relocateVms", notes = "VM 재배치") // TODO: 확인 필요
	@ApiImplicitParams({
			@ApiImplicitParam(name="consolidations", value = "???", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/symphony/relocateVms"})
	public String relocateVms(@RequestBody List<ConsolidationVo> consolidations,
							  Model model) {
		log.info("... relocateVms[{}]", consolidations.size());
		karajanDashboardService.relocateVms(consolidations);
		model.addAttribute(ItInfoConstant.RESULT_KEY, "OK");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "migrateVm", notes = "VM 이관") // TODO: 확인 필요
	@ApiImplicitParams({
			@ApiImplicitParam(name="hostId", value = "호스트 ID", required = true),
			@ApiImplicitParam(name="vmId", value = "VM ID", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/symphony/migrateVm"})
	public String migrateVm(String hostId, String vmId, Model model) {
		log.info("... migrateVm('{}', '{}')", hostId, vmId);
		String result = karajanDashboardService.migrateVm(hostId, vmId);
		if (result.equalsIgnoreCase(VmStatus.MIGRATING.value()))
			karajanDashboardService.publishVmStatus(hostId, vmId);
		model.addAttribute(ItInfoConstant.RESULT_KEY, result);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "getWorkload", notes = "???") // TODO: 확인 필요
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/symphony/workload"})
	public String getWorkload(Model model) {
		log.info("... getWorkload");
		WorkloadVo workload = this.workloadPredictionService.getWorkload();
		model.addAttribute(ItInfoConstant.RESULT_KEY, workload);
		return ItInfoConstant.JSON_VIEW;
	}
}