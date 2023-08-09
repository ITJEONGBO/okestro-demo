package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.model.ItInfoNetworkClusterVo;
import com.itinfo.model.ItInfoNetworkCreateVo;
import com.itinfo.model.ItInfoNetworkGroupVo;
import com.itinfo.model.ItInfoNetworkVo;
import com.itinfo.service.ItInfoNetworkService;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class NetworkController {
	@Autowired private ItInfoNetworkService networkService;

	@RequestMapping({"/network/networks"})
	public String networksView() {
		log.info("... networksView");
		return "/castanets/network/networks";
	}

	@RequestMapping({"/network/getNetworkList"})
	public String getNetworkList(Model model) {
		log.info("... getNetworkList");
		List<ItInfoNetworkVo> list = networkService.getNetworkList();
		model.addAttribute("list", list);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/network/getHostNetworkList"})
	public String getNetworkList(String id, Model model) {
		log.info("... getNetworkList('{}')", id);
		List<ItInfoNetworkVo> networkList = networkService.getHostNetworkList(id);
		model.addAttribute("list", networkList);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/network/clusters"})
	public String networkClusters(@RequestBody ItInfoNetworkVo itinfoNetworkVo, Model model) {
		log.info("... networkClusters ");
		List<ItInfoNetworkClusterVo> clusters
				= networkService.getNetworkCluster("", itinfoNetworkVo.getId());
		model.addAttribute("clusters", clusters);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/network/network"})
	public String networkDetailView() {
		log.info("... networkDetailView");
		return "/castanets/network/network";
	}

	@RequestMapping({"/network/getNetworkDetail"})
	public String networkDeatil(@RequestBody ItInfoNetworkVo itInfoNetworkVo, Model model) {
		log.info("... networkDeatil");
		ItInfoNetworkGroupVo resultVo = networkService.getNetworkDetail(itInfoNetworkVo);
		model.addAttribute("resultData", resultVo);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/network/createNetwork"})
	public String createNetworkView() {
		log.info("... createNetworkView");
		return "/castanets/network/createNetwork";
	}

	@RequestMapping({"/network/createNetworkDeatil"})
	public String networkCreateResource(Model model) {
		log.info("... networkCreateResource");
		ItInfoNetworkCreateVo ItInfoNetworkCreateVo = networkService.getNetworkCreateResource();
		model.addAttribute("resultData", ItInfoNetworkCreateVo);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/network/addNetwork"})
	public String addNetwork(@RequestBody ItInfoNetworkVo ItInfoNetworkVo) {
		log.info("... addNetwork");
		networkService.addLogicalNetwork(ItInfoNetworkVo);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/network/deleteNetwork"})
	public String deleteNetwork(@RequestBody List<ItInfoNetworkVo> ItInfoNetworkVos) throws Exception {
		log.info("... deleteNetwork");
		networkService.deleteNetworks(ItInfoNetworkVos);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/network/updateNetwork"})
	public String updateNetworkView() {
		log.info("... updateNetworkView");
		return "/castanets/network/updateNetwork";
	}

	@RequestMapping({"/network/modifiedNetwork"})
	public String updateNetwork(@RequestBody ItInfoNetworkVo ItInfoNetworkVo, Model model) {
		log.info("... updateNetwork");
		networkService.updateNetwork(ItInfoNetworkVo);
		model.addAttribute("result", "SUC");
		return ItInfoConstant.JSON_VIEW;
	}
}