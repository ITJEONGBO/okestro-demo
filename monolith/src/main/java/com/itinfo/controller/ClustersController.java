package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.ClustersService;
import com.itinfo.service.engine.WebsocketService;

import com.itinfo.model.ClusterCreateVo;
import com.itinfo.model.ClusterVo;
import com.itinfo.model.NetworkProviderVo;
import com.itinfo.model.NetworkVo;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ClustersController {
	@Autowired
	private ClustersService clustersService;
	@Autowired
	private WebsocketService websocketService;

	@RequestMapping(value = {"/test/websocket"}, method = {RequestMethod.GET})
	public String testWebsocket(String id, Model model) {
		this.websocketService.sendMessage("/topic/test", "hello world");
		model.addAttribute(ItInfoConstant.RESULT_KEY, "");
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/createCluster"})
	public String createClusterView() {
		return "/castanets/compute/createCluster";
	}

	@RequestMapping(value = {"/compute/updateCluster"}, method = {RequestMethod.GET})
	public String updateCluster(String id, Model model) {
		model.addAttribute(ItInfoConstant.RESULT_KEY, id);
		return "/castanets/compute/createCluster";
	}

	@RequestMapping({"/compute/clusters"})
	public String clustersView() {
		return "/castanets/compute/clusters";
	}

	@RequestMapping(value = {"/compute/cluster"}, method = {RequestMethod.GET})
	public String clusterView(String id, Model model) {
		model.addAttribute(ItInfoConstant.RESULT_KEY, id);
		return "/castanets/compute/cluster";
	}

	@RequestMapping({"/compute/clusters/retrieveClusters"})
	public String retrieveClustersInfo(Model model) {
		List<ClusterVo> clusters = this.clustersService.retrieveClusters();
		model.addAttribute(ItInfoConstant.RESULT_KEY, clusters);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/clusters/retrieveCluster"}, method = {RequestMethod.GET})
	public String retrieveClusterDetail(String id, Model model) {
		ClusterVo cluster = this.clustersService.retrieveCluster(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, cluster);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/clusters/createCluster"}, method = {RequestMethod.POST})
	public String createCluster(@RequestBody ClusterCreateVo clusterCreateVo, Model model) {
		this.clustersService.createCluster(clusterCreateVo);
		try { Thread.sleep(500L); } catch (Exception exception) { log.error(exception.getLocalizedMessage());}
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/clusters/updateCluster"}, method = {RequestMethod.POST})
	public String updateCluster(@RequestBody ClusterCreateVo clusterCreateVo, Model model) {
		this.clustersService.updateCluster(clusterCreateVo);
		try { Thread.sleep(500L); } catch (Exception exception) { log.error(exception.getLocalizedMessage());}
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/clusters/removeCluster"}, method = {RequestMethod.GET})
	public String deleteCluster(String id, Model model) {
		this.clustersService.removeCluster(id);
		try {
			Thread.sleep(500L);
		} catch (Exception exception) {}
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/clusters/retrieveCreateClusterInfo"}, method = {RequestMethod.GET})
	public String retrieveCreateClusterInfo(String id, Model model) {
		ClusterCreateVo clusterCreateVo = this.clustersService.retrieveCreateClusterInfo(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, clusterCreateVo);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/clusters/retrieveNetworks"}, method = {RequestMethod.GET})
	public String retrieveNetworks(Model model) {
		List<NetworkVo> networks = this.clustersService.retrieveNetworks();
		model.addAttribute(ItInfoConstant.RESULT_KEY, networks);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/clusters/retrieveNetworkProviders"}, method = {RequestMethod.GET})
	public String retrieveNetworkProviders(Model model) {
		List<NetworkProviderVo> networkProviders = this.clustersService.retrieveNetworkProviders();
		model.addAttribute(ItInfoConstant.RESULT_KEY, networkProviders);
		return ItInfoConstant.JSON_VIEW;
	}
}