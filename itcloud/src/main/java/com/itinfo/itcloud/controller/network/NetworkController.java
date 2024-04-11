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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class NetworkController {
	private final ItNetworkService networkService;


	// 네트워크 목록
	@GetMapping("/networks")
	@ResponseBody
	public List<NetworkVo> networks(){
		log.info("--- Network 목록");
		return networkService.getList();
	}

	// 네트워크 생성 창
	@GetMapping("/network/settings")
	@ResponseBody
	public List<NetworkDcClusterVo> setDcCluster(){
		log.info("--- Network 생성 창");
		return networkService.setDcCluster();
	}

	// 네트워크 생성
	@PostMapping("/network")
	@ResponseBody
	public CommonVo<Boolean> addNetwork(@RequestBody NetworkCreateVo nVo){
		log.info("--- Network 생성");
        return networkService.addNetwork(nVo);
	}
	
	// 네트워크 편집 창
	@GetMapping("/network/{id}/settings")
	@ResponseBody
	public NetworkCreateVo setEditNetwork(@PathVariable String id){
		log.info("--- Network 편집 창");
		return networkService.setEditNetwork(id);
	}

	// 네트워크 편집
	@PutMapping("/network/{id}")
	@ResponseBody
	public CommonVo<Boolean> editNetwork(@PathVariable String id,
										 @RequestBody NetworkCreateVo nVo){
		log.info("--- Network 편집");
        return networkService.editNetwork(nVo);
	}

	// 네트워크 삭제
	@DeleteMapping("/network/{id}")
	@ResponseBody
	public CommonVo<Boolean> deleteNetwork(@PathVariable String id){
		log.info("--- Network 삭제");
        return networkService.deleteNetwork(id);
	}



	// 네트워크 가져오기 창
	@GetMapping("/network/import/settings")
	@ResponseBody
	public NetworkImportVo setImportNetwork(){
		log.info("--- Network 가져오기 창");
		return networkService.setImportNetwork();
	}

	// 네트워크 가져오기
	@PostMapping("/network/import")
	public CommonVo<Boolean> importNw(){
		log.info("--- Network 가져오기");
		return networkService.importNetwork();
	}



	// 일반
	@GetMapping("/network/{id}")
	@ResponseBody
	public NetworkVo network(@PathVariable String id){
		log.info("--- Network 일반");
		return networkService.getNetwork(id);
	}

	// vnic 목록
	@GetMapping("/network/{id}/vnic")
	@ResponseBody
	public List<VnicProfileVo> vnic(@PathVariable String id){
		log.info("--- Network vnic프로파일");
		return networkService.getVnic(id);
	}

	// 클러스터 목록
	@GetMapping("/network/{id}/cluster")
	@ResponseBody
	public List<NetworkClusterVo> cluster(@PathVariable String id){
		log.info("--- Network 클러스터");
		return networkService.getCluster(id);
	}

	// 호스트 목록
	@GetMapping("/network/{id}/host")
	@ResponseBody
	public List<NetworkHostVo> host(@PathVariable String id){
		log.info("--- Network 호스트");
		return networkService.getHost(id);
	}

	// 가상머신 목록
	@GetMapping("/network/{id}/vm")
	@ResponseBody
	public List<NetworkVmVo> vm(@PathVariable String id){
		log.info("--- Network 가상머신");
		return networkService.getVm(id);
	}

	// 템플릿 목록
	@GetMapping("/network/{id}/template")
	@ResponseBody
	public List<TemplateVo> template(@PathVariable String id){
		log.info("--- Network 템플릿");
		return networkService.getTemplate(id);
	}

	// 권한 목록
	@GetMapping("/network/{id}/permission")
	@ResponseBody
	public List<PermissionVo> permission(@PathVariable String id){
		log.info("--- Network 권한");
		return networkService.getPermission(id);
	}


}