package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.ClustersService;
import com.itinfo.service.HostsService;
import com.itinfo.model.karajan.ConsolidationVo;
import com.itinfo.model.karajan.HostVo;
import com.itinfo.model.*;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.*;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@Api(value = "HostsController", tags = {"hosts"})
public class HostsController extends BaseController {
	@Autowired private HostsService hostsService;
	@Autowired private ClustersService clustersService;

	@ApiOperation(httpMethod = "GET", value = "createHostView", notes = "페이지 이동 > /compute/hosts")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/hosts"})
	public String hostsView() {
		log.info("... hostsView");
		return "/castanets/compute/hosts";
	}

	@ApiOperation(httpMethod = "GET", value = "hostView", notes = "페이지 이동 > /compute/host")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/host"})
	public String hostView(String id, Model model) {
		log.info("... hostView('{}')", id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, id);
		return "/castanets/compute/host";
	}
	
	@ApiOperation(httpMethod = "GET", value = "createHostView", notes = "페이지 이동 > /compute/createHost")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/createHost"})
	public String createHostView() {
		log.info("... createHostView");
		return "/castanets/compute/createHost";
	}

	@ApiOperation(httpMethod = "GET", value = "updateHostView", notes = "페이지 이동 > /compute/updateHost")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "갱신할 호스트 ID", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/updateHost"})
	public String updateHostView(String id,
								 Model model) {
		log.info("... createHostView('{}')", id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, id);
		return "/castanets/compute/createHost";
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveCreateClusterInfo", notes = "호스트 생성정보 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "호스트 ID", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/hosts/retrieveCreateHostInfo"})
	public String retrieveCreateClusterInfo(String id, 
											Model model) {
		log.info("... retrieveCreateClusterInfo('{}')", id);
		HostCreateVo hostCreateVo
				= hostsService.retrieveCreateHostInfo(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, hostCreateVo);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveHostsInfo", notes = "호스트 목록 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "status", value = "호스트 상태")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/hosts/retrieveHostsInfo"})
	public String retrieveHostsInfo(String status, 
									Model model) {
		log.info("... retrieveHostsInfo('{}')", status);
		List<HostDetailVo> hosts
				= hostsService.retrieveHostsInfo(status);
		model.addAttribute(ItInfoConstant.RESULT_KEY, hosts);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveLunHostsInfo", notes = "LUN 호스트 목록 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "status", value = "호스트 상태")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/hosts/retrieveLunHostsInfo"})
	public String retrieveLunHostsInfo(String status,
									   Model model) {
		log.info("... retrieveLunHostsInfo('{}')", status);
		List<HostDetailVo> hosts
				= hostsService.retrieveLunHostsInfo(status);
		model.addAttribute(ItInfoConstant.RESULT_KEY, hosts);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveHostDetail", notes = "호스트 상세 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "호스트 ID", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/hosts/retrieveHostDetail"})
	public String retrieveHostDetail(String id, 
									 Model model) {
		log.info("... retrieveHostDetail('{}')", id);
		HostDetailVo hostDetailVo
				= hostsService.retrieveHostDetail(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, hostDetailVo);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveHostEvents", notes = "호스트 이벤트 목록 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "호스트 ID", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/hosts/retrieveHostEvents"})
	public String retrieveHostEvents(String id, 
									 Model model) {
		log.info("... retrieveHostEvents('{}')", id);
		List<EventVo> events = hostsService.retrieveHostEvents(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, events);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "consolidateVms", notes = "???")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "hosts", value = "호스트", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/hosts/consolidateVms"})
	public String consolidateVms(@RequestBody List<String> hosts, 
								 Model model) {
		log.info("... consolidateVms[{}]", hosts.size());
		List<ConsolidationVo> result = hostsService.maintenanceBeforeConsolidateVms(hosts);
		model.addAttribute(ItInfoConstant.RESULT_KEY, result);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "maintenanceStart", notes = "유지보수 모드 시작")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "hosts", value = "호스트", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/hosts/maintenanceStart"})
	public String maintenanceStart(@RequestBody List<String> hosts,
								   Model model) {
		log.info("... maintenanceStart[{}]", hosts.size());
		hostsService.maintenanceStart(hosts);
		doSleep();
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "maintenanceStop", notes = "활성 모드 시작")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "hosts", value = "호스트", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/hosts/maintenanceStop"})
	public String maintenanceStop(@RequestBody List<String> hosts,
								  Model model) {
		log.info("... maintenanceStop[{}]", hosts.size());
		hostsService.maintenanceStop(hosts);
		doSleep();
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "restartHost", notes = "호스트 재시작")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "hosts", value = "재시작할 호스트", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/hosts/restartHost"})
	public String restartHost(@RequestBody List<String> hosts,
							  Model model) {
		log.info("... restartHost[{}]", hosts.size());
		hostsService.restartHost(hosts);
		doSleep();
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "startHost", notes = "호스트 시작")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "hosts", value = "시작할 호스트", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/hosts/startHost"})
	public String startHost(@RequestBody List<String> hosts,
							Model model) {
		log.info("... startHost[{}]", hosts.size());
		hostsService.startHost(hosts);
		doSleep();
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "stopHost", notes = "호스트 정지")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "hosts", value = "정지할 호스트", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/hosts/stopHost"})
	public String stopHost(@RequestBody List<String> hosts,
						   Model model) {
		log.info("... stopHost[{}]", hosts.size());
		hostsService.stopHost(hosts);
		doSleep();
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "createHost", notes = "호스트 생성")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "hostCreateVo", value = "생성할 호스트 정보", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/hosts/createHost"})
	public String createHost(@RequestBody HostCreateVo hostCreateVo,
							 Model model) {
		log.info("... createHost");
		hostsService.createHost(hostCreateVo);
		doSleep();
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "updateHost", notes = "호스트 갱신")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "hostCreateVo", value = "갱신할 호스트 정보", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/hosts/updateHost"})
	public String updateHost(@RequestBody HostCreateVo hostCreateVo,
							 Model model) {
		log.info("... updateHost");
		hostsService.updateHost(hostCreateVo);
		doSleep();
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "removeHost", notes = "호스트 제거")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "hosts", value = "제거할 호스트", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/hosts/removeHost"})
	public String removeHost(@RequestBody List<String> hosts,
							 Model model) {
		log.info("... removeHost[{}]", hosts.size());
		hostsService.removeHost(hosts);
		doSleep();
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveClusters", notes = "클러스터 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/hosts/retrieveClusters"})
	public String retrieveClusters(Model model) {
		log.info("... retrieveClusters");
		List<ClusterVo> clusters = this.clustersService.retrieveClusters();
		model.addAttribute(ItInfoConstant.RESULT_KEY, clusters);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveFenceAgentType", notes = "펜스 에이전트 유형 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/hosts/retrieveFanceAgentType"})
	public String retrieveFenceAgentType(Model model) {
		log.info("... retrieveClusters");
		List<String> fenceAgentTypes = hostsService.retrieveFanceAgentType();
		model.addAttribute(ItInfoConstant.RESULT_KEY, fenceAgentTypes);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "connectTestFenceAgent", notes = "테스트용 펜스 에이전트 연결")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "fenceAgentVo", value = "연결대상 펜스 에이전트", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/hosts/connectTestFenceAgent"})
	public String connectTestFenceAgent(@RequestBody FenceAgentVo fenceAgentVo,
										Model model) {
		log.info("... connectTestFenceAgent");
		hostsService.connectTestFenceAgent(fenceAgentVo);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "shutdownHost", notes = "호스트 종료")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "종료할 호스트 ID", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/hosts/shutdownHost"})
	public String shutdownHost(@RequestParam("id") String id,
							   Model model) {
		log.info("... shutdownHost('{}')", id);
		HostVo host = new HostVo();
		host.setId(id);
		List<HostVo> hosts = new ArrayList<>();
		hosts.add(host);
		hostsService.shutdownHost(hosts);
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "setupHostNetwork", notes = "호스트 네트워크 구성")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "nicUsageApiVoList", value = "???", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/hosts/setupHostNetwork"})
	public String setupHostNetwork(@RequestBody List<NicUsageApiVo> nicUsageApiVoList,
								   Model model) {
		log.info("... setupHostNetwork");
		hostsService.setupHostNetwork(nicUsageApiVoList);
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "modifyNicNetwork", notes = "??? 수정")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "nicUsageApiVoList", value = "수정할 ???", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/hosts/modifyNicNetwork"})
	public String modifyNicNetwork(@RequestBody NetworkAttachmentVo networkAttachmentVo, Model model) {
		log.info("... modifyNicNetwork");
		hostsService.modifyNicNetwork(networkAttachmentVo);
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}
}

