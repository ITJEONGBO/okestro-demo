package com.itinfo.itcloud.controller.network;

import com.itinfo.itcloud.model.MenuVo;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.ClusterCreateVo;
import com.itinfo.itcloud.model.create.NetworkCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.*;
import com.itinfo.itcloud.service.ItMenuService;
import com.itinfo.itcloud.service.ItNetworkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/network")
public class NetworkController {
	private final ItNetworkService itNetworkService;
	private final ItMenuService menu;


	@GetMapping("/networks")
	public String networks(Model model){
		List<NetworkVo> networks = itNetworkService.getList();
		model.addAttribute("networks", networks);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "/network/networks";
	}

	@GetMapping("/network")
	public String network(String id, Model model){
		NetworkVo network = itNetworkService.getNetwork(id);
		model.addAttribute("network", network);
		model.addAttribute("id", id);
		model.addAttribute("name", itNetworkService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "/network/network";
	}
	@GetMapping("/network-vnicProfile")
	public String vnicProfile(String id, Model model){
		List<VnicProfileVo> vnic = itNetworkService.getVnic(id);
		model.addAttribute("vnic", vnic);
		model.addAttribute("id", id);
		model.addAttribute("name", itNetworkService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "/network/network-vnicProfile";
	}

	@GetMapping("/network-cluster")
	public String cluster(String id, Model model){
		List<NetworkClusterVo> cluster = itNetworkService.getCluster(id);
		model.addAttribute("cluster", cluster);
		model.addAttribute("id", id);
		model.addAttribute("name", itNetworkService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "/network/network-cluster";
	}


	@GetMapping("/network-host")
	public String host(String id, Model model){
		List<NetworkHostVo> host = itNetworkService.getHost(id);
		model.addAttribute("host", host);
		model.addAttribute("id", id);
		model.addAttribute("name", itNetworkService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "/network/network-host";
	}


	@GetMapping("/network-vm")
	public String vm(String id, Model model){
		List<NetworkVmVo> vm = itNetworkService.getVm(id);
		model.addAttribute("vm", vm);
		model.addAttribute("id", id);
		model.addAttribute("name", itNetworkService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "/network/network-vm";
	}

	@GetMapping("/network-template")
	public String template(String id, Model model){
		List<TemplateVo> template = itNetworkService.getTemplate(id);
		model.addAttribute("template", template);
		model.addAttribute("id", id);
		model.addAttribute("name", itNetworkService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "/network/network-template";
	}

	@GetMapping("/network-permission")
	public String permission(String id, Model model){
		List<PermissionVo> permission = itNetworkService.getPermission(id);
		model.addAttribute("permission", permission);
		model.addAttribute("id", id);
		model.addAttribute("name", itNetworkService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);

		return "/network/network-permission";
	}


	//region: set Network

	@GetMapping("/networkdc")
	@ResponseBody
	public List<NetworkDcClusterVo> getDcCluster(){
		return itNetworkService.getDcCluster();
	}

	@GetMapping("/network-add")
	public String add(Model model){
		List<NetworkDcClusterVo> ndcList = itNetworkService.getDcCluster();

		model.addAttribute("dc", ndcList);
		return "/network/network-add";
	}
//
//	@PostMapping("/network-add2")
//	public String add2(Model model, @ModelAttribute NetworkCreateVo nVo){
//		CommonVo<Boolean> addNw = itNetworkService.addNetwork(nVo);
//
//		if(addNw.getBody().getContent().equals(true)){
//			model.addAttribute("result", "네트워크 생성 완료");
//		}else{
//			model.addAttribute("result", "네트워크 생성 실패");
//		}
//		model.addAttribute("message", addNw.getHead().getMessage());
//		model.addAttribute("body", addNw.getBody().getContent());
//
//		return "/network/network-add2";
//	}

	@PostMapping("/network-add")
	public CommonVo<Boolean> addNw(@RequestBody NetworkCreateVo nVo){
        return itNetworkService.addNetwork(nVo);
	}
	@PostMapping("/network-edit")
	public CommonVo<Boolean> editNw(@RequestBody NetworkCreateVo nVo){
        return itNetworkService.editNetwork(nVo);
	}
	@PostMapping("/network-delete")
	public CommonVo<Boolean> deleteNw(@RequestParam String id){
        return itNetworkService.deleteNetwork(id);
	}




	// end region










	//region: ResponseBody
	@GetMapping("/networksStatus")
	@ResponseBody
	public List<NetworkVo> networks(){
		return itNetworkService.getList();
	}

	@GetMapping("/networkStatus")
	@ResponseBody
	public NetworkVo network(String id){
		return itNetworkService.getNetwork(id);
	}

	@GetMapping("/network/vnicProfileStatus")
	@ResponseBody
	public List<VnicProfileVo> vnic(String id){
		return itNetworkService.getVnic(id);
	}

	@GetMapping("/network/clusterStatus")
	@ResponseBody
	public List<NetworkClusterVo> cluster(String id){
		return itNetworkService.getCluster(id);
	}

	@GetMapping("/network/hostStatus")
	@ResponseBody
	public List<NetworkHostVo> host(String id){
		return itNetworkService.getHost(id);
	}

	@GetMapping("/network/vmStatus")
	@ResponseBody
	public List<NetworkVmVo> vm(String id){
		return itNetworkService.getVm(id);
	}

	@GetMapping("/network/templateStatus")
	@ResponseBody
	public List<TemplateVo> template(String id){
		return itNetworkService.getTemplate(id);
	}

	@GetMapping("/network/permissionStatus")
	@ResponseBody
	public List<PermissionVo> permission(String id){
		return itNetworkService.getPermission(id);
	}

	//endregion

}
