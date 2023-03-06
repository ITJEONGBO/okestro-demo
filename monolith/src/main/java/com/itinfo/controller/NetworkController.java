package com.itinfo.controller;

import com.itinfo.model.ItInfoNetworkClusterVo;
import com.itinfo.model.ItInfoNetworkCreateVo;
import com.itinfo.model.ItInfoNetworkGroupVo;
import com.itinfo.model.ItInfoNetworkVo;
import com.itinfo.service.ItInfoNetworkService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("networkController")
public class NetworkController {
	@Autowired
	private ItInfoNetworkService networkServcie;

	@RequestMapping({"/network/networks"})
	public String networksView() {
		return "/castanets/network/networks";
	}

	@RequestMapping({"/network/getNetworkList"})
	public String getNetworkList(Model model) {
		List<ItInfoNetworkVo> list = this.networkServcie.getNetworkList();
		model.addAttribute("list", list);
		return "jsonView";
	}

	@RequestMapping({"/network/getHostNetworkList"})
	public String getNetworkList(String id, Model model) {
		List<ItInfoNetworkVo> networkList = this.networkServcie.getHostNetworkList(id);
		model.addAttribute("list", networkList);
		return "jsonView";
	}

	@RequestMapping({"/network/clusters"})
	public String networkClusters(@RequestBody ItInfoNetworkVo itinfoNetworkVo, Model model) {
		List<ItInfoNetworkClusterVo> clusters = this.networkServcie.getNetworkCluster(null, itinfoNetworkVo.getId());
		model.addAttribute("clusters", clusters);
		return "jsonView";
	}

	@RequestMapping({"/network/network"})
	public String networkDetailView() {
		return "/castanets/network/network";
	}

	@RequestMapping({"/network/getNetworkDetail"})
	public String networkDeatil(@RequestBody ItInfoNetworkVo itInfoNetworkVo, Model model) {
		ItInfoNetworkGroupVo resultVo = this.networkServcie.getNetworkDetail(itInfoNetworkVo);
		model.addAttribute("resultData", resultVo);
		return "jsonView";
	}

	@RequestMapping({"/network/createNetwork"})
	public String createNetworkView() {
		return "/castanets/network/createNetwork";
	}

	@RequestMapping({"/network/createNetworkDeatil"})
	public String networkCreateResource(Model model) {
		ItInfoNetworkCreateVo ItInfoNetworkCreateVo = this.networkServcie.getNetworkCreateResource();
		model.addAttribute("resultData", ItInfoNetworkCreateVo);
		return "jsonView";
	}

	@RequestMapping({"/network/addNetwork"})
	public String addNetwork(@RequestBody ItInfoNetworkVo ItInfoNetworkVo) {
		this.networkServcie.addLogicalNetwork(ItInfoNetworkVo);
		return "jsonView";
	}

	@RequestMapping({"/network/deleteNetwork"})
	public String deleteNetwork(@RequestBody List<ItInfoNetworkVo> ItInfoNetworkVos) throws Exception {
		this.networkServcie.deleteNetworks(ItInfoNetworkVos);
		return "jsonView";
	}

	@RequestMapping({"/network/updateNetwork"})
	public String updateNetworkView() {
		return "/castanets/network/updateNetwork";
	}

	@RequestMapping({"/network/modifiedNetwork"})
	public String updateNetwok(@RequestBody ItInfoNetworkVo ItInfoNetworkVo, Model model) {
		this.networkServcie.updateNetwork(ItInfoNetworkVo);
		model.addAttribute("result", "SUC");
		return "jsonView";
	}
}