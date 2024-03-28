package com.itinfo.itcloud.controller.network;

import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.computing.TemplateVo;
import com.itinfo.itcloud.model.create.NetworkCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.*;
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



	@GetMapping("/network-importView")
	@ResponseBody
	public NetworkImportVo viewImportNetwork(){
		return itNetworkService.viewImportNetwork();
	}

	@PostMapping("/network-import")
	public CommonVo<Boolean> importNw(){
		return itNetworkService.importNetwork();
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
