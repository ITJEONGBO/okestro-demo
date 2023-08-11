package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.model.ItInfoNetworkClusterVo;
import com.itinfo.model.ItInfoNetworkCreateVo;
import com.itinfo.model.ItInfoNetworkGroupVo;
import com.itinfo.model.ItInfoNetworkVo;
import com.itinfo.service.ItInfoNetworkService;

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
@Api(value = "NetworkController", tags = {"network"})
public class NetworkController extends BaseController {
	@Autowired private ItInfoNetworkService networkService;

	@ApiOperation(httpMethod = "GET", value = "networksView", notes = "페이지 이동 > /network/networks")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/network/networks"})
	public String networksView() {
		log.info("... networksView");
		return "/castanets/network/networks";
	}

	@ApiOperation(httpMethod = "GET", value = "networkDetailView", notes = "페이지 이동 > /network/network")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/network/network"})
	public String networkDetailView() {
		log.info("... networkDetailView");
		return "/castanets/network/network";
	}

	@ApiOperation(httpMethod = "GET", value = "createNetworkView", notes = "페이지 이동 > /network/createNetwork")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/network/createNetwork"})
	public String createNetworkView() {
		log.info("... createNetworkView");
		return "/castanets/network/createNetwork";
	}

	@ApiOperation(httpMethod = "GET", value = "updateNetworkView", notes = "페이지 이동 > /network/updateNetwork")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/network/updateNetwork"})
	public String updateNetworkView() {
		log.info("... updateNetworkView");
		return "/castanets/network/updateNetwork";
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveNetworkList", notes = "네트워크 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = {"/network/getNetworkList"}) // TODO: POST 제거
	public String retrieveNetworkList(Model model) {
		log.info("... getNetworkList");
		List<ItInfoNetworkVo> list = networkService.getNetworkList();
		model.addAttribute("list", list);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveHostNetworkList", notes = "호스트 네트워크 목록 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "네트워크 ID")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/network/getHostNetworkList"})
	public String retrieveHostNetworkList(String id,
										  Model model) {
		log.info("... retrieveHostNetworkList('{}')", id);
		List<ItInfoNetworkVo> networkList = networkService.getHostNetworkList(id);
		model.addAttribute("list", networkList);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "retrieveNetworkClusters", notes = "네트워크 클러스터 목록 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "itinfoNetworkVo", value = "네트워크 정보")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.GET, RequestMethod.POST}, // TODO: GET으로 변경
			value = {"/network/clusters"}
	)
	public String retrieveNetworkClusters(@RequestBody ItInfoNetworkVo itinfoNetworkVo,
										  Model model) {
		log.info("... networkClusters ");
		List<ItInfoNetworkClusterVo> clusters
				= networkService.getNetworkCluster("", itinfoNetworkVo.getId());
		model.addAttribute("clusters", clusters);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "retrieveNetworkClusters", notes = "네트워크 클러스터 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.GET, RequestMethod.POST}, // TODO: GET으로 변경
			value = {"/network/getNetworkDetail"}
	)
	public String retrieveNetworkDetail(@RequestBody ItInfoNetworkVo itInfoNetworkVo,
										Model model) {
		log.info("... networkDeatil");
		ItInfoNetworkGroupVo resultVo = networkService.getNetworkDetail(itInfoNetworkVo);
		model.addAttribute("resultData", resultVo);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "createNetworkResource", notes = "네트워크 생성")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/network/createNetworkDeatil"})
	public String createNetworkResource(Model model) {
		log.info("... networkCreateResource");
		ItInfoNetworkCreateVo ItInfoNetworkCreateVo = networkService.getNetworkCreateResource();
		model.addAttribute("resultData", ItInfoNetworkCreateVo);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "addNetwork", notes = "네트워크 추가")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ItInfoNetworkVo", value = "추가할 네트워크 정보")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/network/addNetwork"})
	public String addNetwork(@RequestBody ItInfoNetworkVo ItInfoNetworkVo,
							 Model model) {
		log.info("... addNetwork");
		networkService.addLogicalNetwork(ItInfoNetworkVo);
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "deleteNetwork", notes = "네트워크 제거")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ItInfoNetworkVo", value = "제거할 네트워크 정보")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/network/deleteNetwork"})
	public String deleteNetwork(@RequestBody List<ItInfoNetworkVo> ItInfoNetworkVos,
								Model model) throws Exception {
		log.info("... deleteNetwork");
		networkService.deleteNetworks(ItInfoNetworkVos);
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "updateNetwork", notes = "네트워크 수정")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ItInfoNetworkVo", value = "수정할 네트워크 정보")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/network/modifiedNetwork"})
	public String updateNetwork(@RequestBody ItInfoNetworkVo ItInfoNetworkVo,
								Model model) {
		log.info("... updateNetwork");
		networkService.updateNetwork(ItInfoNetworkVo);
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}
}