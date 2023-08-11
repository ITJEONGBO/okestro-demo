package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.ClustersService;
import com.itinfo.service.engine.WebsocketService;

import com.itinfo.model.ClusterCreateVo;
import com.itinfo.model.ClusterVo;
import com.itinfo.model.NetworkProviderVo;
import com.itinfo.model.NetworkVo;

import java.util.List;

import io.swagger.annotations.*;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
@Api(value = "ClustersController", tags = {"clusters"})
public class ClustersController extends BaseController {
	@Autowired private ClustersService clustersService;
	@Autowired private WebsocketService websocketService;

	@ApiOperation(httpMethod = "GET", value = "createClusterView", notes = "페이지 이동 > /compute/createCluster")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/createCluster"})
	public String createClusterView() {
		log.info("... createClusterView");
		return "/castanets/compute/createCluster";
	}

	@ApiOperation(httpMethod = "GET", value = "updateCluster", notes = "페이지 이동 > /compute/updateCluster")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "클러스터 ID")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/updateCluster"})
	public String updateCluster(String id,
								Model model) {
		log.info("... updateCluster('{}')", id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, id);
		return "/castanets/compute/createCluster";
	}

	@ApiOperation(httpMethod = "GET", value = "clustersView", notes = "페이지 이동 > /compute/clusters")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/clusters"})
	public String clustersView() {
		log.info("... clustersView");
		return "/castanets/compute/clusters";
	}

	@ApiOperation(httpMethod = "GET", value = "clusterView", notes = "페이지 이동 > /compute/cluster")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "클러스터 ID")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/cluster"})
	public String clusterView(String id,
							  Model model) {
		log.info("... clusterView('{}')", id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, id);
		return "/castanets/compute/cluster";
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveClustersInfo", notes = "클러스터 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/clusters/retrieveClusters"})
	public String retrieveClustersInfo(Model model) {
		log.info("... retrieveClustersInfo");
		List<ClusterVo> clusters 
				= clustersService.retrieveClusters();
		model.addAttribute(ItInfoConstant.RESULT_KEY, clusters);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveClusterDetail", notes = "클러스터 상세 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "클러스터 ID", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/clusters/retrieveCluster"})
	public String retrieveClusterDetail(String id,
										Model model) {
		log.info("... retrieveClusterDetail('{}')", id);
		ClusterVo cluster = clustersService.retrieveCluster(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, cluster);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "createCluster", notes = "클러스터 생성")
	@ApiImplicitParams({
			@ApiImplicitParam(name="clusterCreateVo", value="생성 할 클러스터 정보")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/clusters/createCluster"})
	public String createCluster(@RequestBody ClusterCreateVo clusterCreateVo,
								Model model) {
		log.info("... retrieveClustersInfo");
		clustersService.createCluster(clusterCreateVo);
		doSleep();
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "updateCluster", notes = "클러스터 갱신")
	@ApiImplicitParams({
			@ApiImplicitParam(name="clusterCreateVo", value="갱신 할 클러스터 정보")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/clusters/updateCluster"})
	public String updateCluster(@RequestBody ClusterCreateVo clusterCreateVo,
								Model model) {
		log.info("... updateCluster");
		clustersService.updateCluster(clusterCreateVo);
		doSleep();
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "deleteCluster", notes = "클러스터 제거")
	@ApiImplicitParams({
			@ApiImplicitParam(name="id", value="제거 할 클러스터 ID")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/clusters/removeCluster"}) // TODO: POST로 변경 필요
	public String deleteCluster(String id,
								Model model) {
		log.info("... deleteCluster('{}')", id);
		clustersService.removeCluster(id);
		doSleep();
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveCreateClusterInfo", notes = "생성할 클러스터 상세정보 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name="id", value="생성할 클러스터 id")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping( method = {RequestMethod.GET}, value = {"/compute/clusters/retrieveCreateClusterInfo"})
	public String retrieveCreateClusterInfo(String id,
											Model model) {
		log.info("... retrieveCreateClusterInfo('{}')", id);
		ClusterCreateVo clusterCreateVo = clustersService.retrieveCreateClusterInfo(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, clusterCreateVo);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveNetworks", notes = "네트워크 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/clusters/retrieveNetworks"})
	public String retrieveNetworks(Model model) {
		log.info("... retrieveNetworks");
		List<NetworkVo> networks 
				= clustersService.retrieveNetworks();
		model.addAttribute(ItInfoConstant.RESULT_KEY, networks);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveNetworkProviders", notes = "네트워크 제공자 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/clusters/retrieveNetworkProviders"})
	public String retrieveNetworkProviders(Model model) {
		log.info("... retrieveNetworkProviders");
		List<NetworkProviderVo> networkProviders 
				= clustersService.retrieveNetworkProviders();
		model.addAttribute(ItInfoConstant.RESULT_KEY, networkProviders);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "testWebsocket", notes = "웹소켓 테스트")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "?")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/test/websocket"})
	public String testWebsocket(String id,
								Model model) {
		log.info("... testWebsocket('{}')", id);
		this.websocketService.sendMessage("/topic/test", "hello world");
		model.addAttribute(ItInfoConstant.RESULT_KEY, "");
		return ItInfoConstant.JSON_VIEW;
	}
}