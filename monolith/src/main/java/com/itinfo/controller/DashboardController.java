package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.HostsService;
import com.itinfo.service.VirtualMachinesService;
import com.itinfo.service.DashboardService;
import com.itinfo.model.*;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.*;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@Slf4j
@Api(value = "DashboardController", tags = {"dashboard"})
public class DashboardController extends BaseController {
	@Autowired private DashboardService dashboardService;
	@Autowired private VirtualMachinesService virtualMachinesService;
	@Autowired private HostsService hostsService;

	// @GetMapping("/")
	@ApiOperation(httpMethod = "GET", value = "getDashboardView", notes = "페이지 이동 > /dashboard")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/dashboard"})
	public String getDashboardView() {
		log.info("... getDashboardView");
		return "/castanets/dashboard/dashboard";
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveDataCenterStatus", notes = "데이터센터 상태 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/dashboard/retrieveDataCenterStatus"})
	public String retrieveDataCenterStatus(Model model) {
		log.info("... retrieveDataCenterStatus");
		DataCenterVo dcv
				= dashboardService.retrieveDataCenterStatus();
		model.addAttribute(ItInfoConstant.RESULT_KEY, dcv);
		return ItInfoConstant.JSON_VIEW;
	}


	@ApiOperation(httpMethod = "GET", value = "retrieveEvents", notes = "이벤트 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/dashboard/retrieveEvents"})
	public String retrieveEvents(Model model) {
		log.info("... retrieveEvents");
		List<EventVo> events
				= dashboardService.retrieveEvents();
		model.addAttribute(ItInfoConstant.RESULT_KEY, events);
		return ItInfoConstant.JSON_VIEW;
	}

	// @GetMapping("/retrieveVms")
	@ApiOperation(httpMethod = "GET", value = "retrieveVms", notes = "VM 상태 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name="status", value = "VM 상태")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/dashboard/retrieveVms"})
	public String retrieveVms(String status,
							  Model model) {
		log.info("... retrieveVms('{}')", status);
		List<VmVo> vms
				= virtualMachinesService.retrieveVmsAll();
		List<DashboardTopVo> vmsTop = !vms.isEmpty()
				? virtualMachinesService.retrieveVmsTop(vms)
				: new ArrayList<>();
		model.addAttribute(ItInfoConstant.RESULT_KEY, vmsTop);
		return ItInfoConstant.JSON_VIEW;
	}

	// @GetMapping("/retrieveHosts")
	@ApiOperation(httpMethod = "GET", value = "retrieveHosts", notes = "호스트 목록 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name="status", value = "호스트 상태", defaultValue = "all")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/dashboard/retrieveHosts"})
	public String retrieveHosts(String status,
								Model model) {
		log.info("... retrieveHosts('{}')", status);
		List<HostDetailVo> hosts
				= hostsService.retrieveHostsInfo(status);
		List<DashboardTopVo> hostsTop = !hosts.isEmpty()
				? hostsService.retrieveHostsTop(hosts)
				: new ArrayList<>();
		model.addAttribute(ItInfoConstant.RESULT_KEY, hostsTop);
		return ItInfoConstant.JSON_VIEW;
	}
}

