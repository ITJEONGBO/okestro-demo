package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.model.ItInfoNetworkClusterVo;
import com.itinfo.model.ItInfoNetworkCreateVo;
import com.itinfo.model.ItInfoNetworkGroupVo;
import com.itinfo.model.ItInfoNetworkVo;
import com.itinfo.service.ItInfoNetworkService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("networkController")
public class NetworkController {
	@Autowired private ItInfoNetworkService networkService;

	@RequestMapping({"/network/networks"})
	public String networksView() {
		return "/castanets/network/networks";
	}

	@RequestMapping({"/network/getNetworkList"})
	public String getNetworkList(Model model) {
		List<ItInfoNetworkVo> list = this.networkService.getNetworkList();
		model.addAttribute("list", list);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/network/getHostNetworkList"})
	public String getNetworkList(String id, Model model) {
		List<ItInfoNetworkVo> networkList = this.networkService.getHostNetworkList(id);
		model.addAttribute("list", networkList);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/network/clusters"})
	public String networkClusters(@RequestBody ItInfoNetworkVo itinfoNetworkVo, Model model) {
		List<ItInfoNetworkClusterVo> clusters = this.networkService.getNetworkCluster(null, itinfoNetworkVo.getId());
		model.addAttribute("clusters", clusters);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/network/network"})
	public String networkDetailView() {
		return "/castanets/network/network";
	}

	@RequestMapping({"/network/getNetworkDetail"})
	public String networkDeatil(@RequestBody ItInfoNetworkVo itInfoNetworkVo, Model model) {
		ItInfoNetworkGroupVo resultVo = this.networkService.getNetworkDetail(itInfoNetworkVo);
		model.addAttribute("resultData", resultVo);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/network/createNetwork"})
	public String createNetworkView() {
		return "/castanets/network/createNetwork";
	}

	@RequestMapping({"/network/createNetworkDeatil"})
	public String networkCreateResource(Model model) {
		ItInfoNetworkCreateVo ItInfoNetworkCreateVo = this.networkService.getNetworkCreateResource();
		model.addAttribute("resultData", ItInfoNetworkCreateVo);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/network/addNetwork"})
	public String addNetwork(@RequestBody ItInfoNetworkVo ItInfoNetworkVo) {
		this.networkService.addLogicalNetwork(ItInfoNetworkVo);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/network/deleteNetwork"})
	public String deleteNetwork(@RequestBody List<ItInfoNetworkVo> ItInfoNetworkVos) throws Exception {
		this.networkService.deleteNetworks(ItInfoNetworkVos);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/network/updateNetwork"})
	public String updateNetworkView() {
		return "/castanets/network/updateNetwork";
	}

	@RequestMapping({"/network/modifiedNetwork"})
	public String updateNetwok(@RequestBody ItInfoNetworkVo ItInfoNetworkVo, Model model) {
		this.networkService.updateNetwork(ItInfoNetworkVo);
		model.addAttribute("result", "SUC");
		return ItInfoConstant.JSON_VIEW;
	}
}