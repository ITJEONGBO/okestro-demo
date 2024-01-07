package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.network.NetworkClusterVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import com.itinfo.itcloud.service.ItNetworkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class NetworkController {

	private final ItNetworkService itNetworkService;


	@GetMapping("/network/networks")
	public String networks(Model model){
		List<NetworkVo> networks = itNetworkService.getList();
		model.addAttribute("networks", networks);
		return "/network/networks";
	}

	@GetMapping("/networksStatus")
	@ResponseBody
	public List<NetworkVo> networks(){
		return itNetworkService.getList();
	}


	@GetMapping("/network/network")
	public String network(String id, Model model){
		NetworkVo network = itNetworkService.getNetwork(id);
		model.addAttribute("network", network);
		model.addAttribute("id", id);
		return "/network/network";
	}

	@GetMapping("/networkStatus")
	@ResponseBody
	public NetworkVo network(String id){
		return itNetworkService.getNetwork(id);
	}

	@GetMapping("/network/network-vnicProfile")
	public String vnicProfile(String id, Model model){
		List<VnicProfileVo> vnic = itNetworkService.getVnic(id);
		model.addAttribute("vnic", vnic);
		model.addAttribute("id", id);

		return "/network/network-vnicProfile";
	}

	@GetMapping("/network/vnicProfileStatus")
	@ResponseBody
	public List<VnicProfileVo> vnic(String id){
		return itNetworkService.getVnic(id);
	}


	@GetMapping("/network/network-cluster")
	public String cluster(String id, Model model){
		List<NetworkClusterVo> cluster = itNetworkService.getCluster(id);
		model.addAttribute("cluster", cluster);
		model.addAttribute("id", id);

		return "/network/network-cluster";
	}

	@GetMapping("/network/clusterStatus")
	@ResponseBody
	public List<NetworkClusterVo> cluster(String id){
		return itNetworkService.getCluster(id);
	}


	@GetMapping("/network/network-host")
	public String host(String id, Model model){
		List<HostVo> host = itNetworkService.getHost(id);
		model.addAttribute("host", host);
		model.addAttribute("id", id);

		return "/network/network-host";
	}

	@GetMapping("/network/hostStatus")
	@ResponseBody
	public List<HostVo> host(String id){
		return itNetworkService.getHost(id);
	}


	@GetMapping("/network/network-vm")
	public String vm(String id, Model model){
		List<VmVo> vm = itNetworkService.getVm(id);
		model.addAttribute("vm", vm);
		model.addAttribute("id", id);

		return "/network/network-vm";
	}

	@GetMapping("/network/vmStatus")
	@ResponseBody
	public List<VmVo> vm(String id){
		return itNetworkService.getVm(id);
	}


	@GetMapping("/network/network-template")
	public String template(String id, Model model){
		List<TemplateVo> template = itNetworkService.getTemplate(id);
		model.addAttribute("template", template);
		model.addAttribute("id", id);

		return "/network/network-template";
	}

	@GetMapping("/network/templateStatus")
	@ResponseBody
	public List<TemplateVo> template(String id){
		return itNetworkService.getTemplate(id);
	}


	@GetMapping("/network/network-permission")
	public String permission(String id, Model model){
		List<PermissionVo> permission = itNetworkService.getPermission(id);
		model.addAttribute("permission", permission);
		model.addAttribute("id", id);

		return "/network/network-permission";
	}

	@GetMapping("/network/permissionStatus")
	@ResponseBody
	public List<PermissionVo> permission(String id){
		return itNetworkService.getPermission(id);
	}

}
