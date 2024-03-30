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
	private final ItNetworkService networkService;


	//region: set Network

	@GetMapping("/networkdc")
	@ResponseBody
	public List<NetworkDcClusterVo> getDcCluster(){
		return networkService.getDcCluster();
	}

	@PostMapping("/network-add")
	public CommonVo<Boolean> addNw(@RequestBody NetworkCreateVo nVo){
        return networkService.addNetwork(nVo);
	}
	@PostMapping("/network-edit")
	public CommonVo<Boolean> editNw(@RequestBody NetworkCreateVo nVo){
        return networkService.editNetwork(nVo);
	}
	@PostMapping("/network-delete")
	public CommonVo<Boolean> deleteNw(@RequestParam String id){
        return networkService.deleteNetwork(id);
	}



	@GetMapping("/network-importView")
	@ResponseBody
	public NetworkImportVo viewImportNetwork(){
		return networkService.viewImportNetwork();
	}

	@PostMapping("/network-import")
	public CommonVo<Boolean> importNw(){
		return networkService.importNetwork();
	}


	// end region










	//region: ResponseBody
	@GetMapping("/networksStatus")
	@ResponseBody
	public List<NetworkVo> networks(){
		return networkService.getList();
	}

	@GetMapping("/networkStatus")
	@ResponseBody
	public NetworkVo network(String id){
		return networkService.getNetwork(id);
	}

	@GetMapping("/network/vnicProfileStatus")
	@ResponseBody
	public List<VnicProfileVo> vnic(String id){
		return networkService.getVnic(id);
	}

	@GetMapping("/network/clusterStatus")
	@ResponseBody
	public List<NetworkClusterVo> cluster(String id){
		return networkService.getCluster(id);
	}

	@GetMapping("/network/hostStatus")
	@ResponseBody
	public List<NetworkHostVo> host(String id){
		return networkService.getHost(id);
	}

	@GetMapping("/network/vmStatus")
	@ResponseBody
	public List<NetworkVmVo> vm(String id){
		return networkService.getVm(id);
	}

	@GetMapping("/network/templateStatus")
	@ResponseBody
	public List<TemplateVo> template(String id){
		return networkService.getTemplate(id);
	}

	@GetMapping("/network/permissionStatus")
	@ResponseBody
	public List<PermissionVo> permission(String id){
		return networkService.getPermission(id);
	}

	//endregion

}
