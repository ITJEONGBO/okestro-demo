package com.itinfo.itcloud.controller.network;

import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.create.NetworkCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.*;
import com.itinfo.itcloud.service.network.ItNetworkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@Api(tags = "Network")
@RequestMapping("/networks")
public class NetworkController {
	private final ItNetworkService networkService;


	@GetMapping
	@ApiOperation(value = "네트워크 목록", notes = "전체 네트워크 목록을 조회한다")
	@ResponseBody
	public List<NetworkVo> networks(){
		log.info("--- Network 목록");
		return networkService.getList();
	}

	@GetMapping("/settings")
	@ApiOperation(value = "네트워크 생성 창", notes = "네트워크 생성시 필요한 내용을 조회한다")
	@ResponseBody
	public List<NetworkDcClusterVo> setDcCluster(){
		log.info("--- Network 생성 창");
		return networkService.setDcCluster();
	}

	@PostMapping
	@ApiOperation(value = "네트워크 생성", notes = "네트워크를 생성한다")
	@ApiImplicitParam(name = "nVo", value = "네트워크", dataTypeClass = String.class)
	@ResponseBody
	public CommonVo<Boolean> addNetwork(@RequestBody NetworkCreateVo nVo){
		log.info("--- Network 생성");
        return networkService.addNetwork(nVo);
	}
	
	@GetMapping("/{id}/edit")
	@ApiOperation(value = "네트워크 수정창", notes = "선택된 네트워크의 정보를 조회한다")
	@ApiImplicitParam(name = "nVo", value = "네트워크", dataTypeClass = String.class)
	@ResponseBody
	public NetworkCreateVo setEditNetwork(@PathVariable String id){
		log.info("--- Network 편집 창");
		return networkService.setEditNetwork(id);
	}

	@PutMapping("/{id}")
	@ApiOperation(value = "네트워크 수정", notes = "네트워크를 수정한다")
	@ApiImplicitParam(name = "nVo", value = "네트워크", dataTypeClass = String.class)
	@ResponseBody
	public CommonVo<Boolean> editNetwork(@PathVariable String id,
										 @RequestBody NetworkCreateVo nVo){
		log.info("--- Network 편집");
        return networkService.editNetwork(nVo);
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "네트워크 삭제", notes = "네트워크를 삭제한다")
	@ApiImplicitParam(name = "id", value = "네트워크 아이디", dataTypeClass = String.class)
	@ResponseBody
	public CommonVo<Boolean> deleteNetwork(@PathVariable String id){
		log.info("--- Network 삭제");
        return networkService.deleteNetwork(id);
	}



	// TODO:HELP
	// 네트워크 가져오기 창
	@GetMapping("/import/settings")
	@ResponseBody
	public NetworkImportVo setImportNetwork(){
		log.info("--- Network 가져오기 창");
		return networkService.setImportNetwork();
	}

	// 네트워크 가져오기
	@PostMapping("/import")
	public CommonVo<Boolean> importNw(){
		log.info("--- Network 가져오기");
		return networkService.importNetwork();
	}



	@GetMapping("/{id}")
	@ApiOperation(value = "네트워크 상세정보", notes = "네트워크의 상세정보를 조회한다")
	@ApiImplicitParam(name = "nVo", value = "네트워크", dataTypeClass = String.class)
	@ResponseBody
	public NetworkVo network(@PathVariable String id){
		log.info("--- Network 일반");
		return networkService.getNetwork(id);
	}


	@GetMapping("/{id}/vnics")
	@ApiOperation(value = "네트워크 vnic 프로파일 목록", notes = "선택된 네트워크의 vnic 프로파일 목록을 조회한다")
	@ApiImplicitParam(name = "nVo", value = "네트워크", dataTypeClass = String.class)
	@ResponseBody
	public List<VnicProfileVo> vnic(@PathVariable String id){
		log.info("--- Network vnic 프로파일");
		return networkService.getVnic(id);
	}

	// vnic 생성 창
	// TODO:HELP
	@GetMapping("/{id}/vnics/settings")
	@ResponseBody
	public VnicCreateVo setVnic(@PathVariable String id){
		log.info("--- Network vnic 프로파일 생성창");
		return networkService.setVnic(id);
	}

	// vnic 생성
	@PostMapping("/{id}/vnic")
	@ResponseBody
	public CommonVo<Boolean> addVnic(@PathVariable String id,
									 @RequestBody VnicCreateVo vcVo){
		log.info("--- Network vnic 프로파일 생성");
		return networkService.addVnic(id, vcVo);
	}

	// vnic 편집 창
	@GetMapping("/{id}/vnics/{vcId}/settings")
	@ResponseBody
	public VnicCreateVo setEditVnic(@PathVariable String id,
								 @PathVariable String vcId){
		log.info("--- Network vnic프로파일 편집창");
		return networkService.setEditVnic(id, vcId);
	}

	// vnic 편집
	@PutMapping("/{id}/vnics/{vcId}")
	@ResponseBody
	public CommonVo<Boolean> editVnic(@PathVariable String id,
									  @PathVariable String vcId,
									  @RequestBody VnicCreateVo vcVo){
		log.info("--- Network vnic프로파일 편집");
		return networkService.editVnic(id, vcId, vcVo);
	}

	// vnic 삭제
	@DeleteMapping("/{id}/vnics/{vcId}")
	@ResponseBody
	public CommonVo<Boolean> deleteVnic(@PathVariable String id,
										@PathVariable String vcId){
		log.info("--- Network vnic프로파일 삭제");
		return networkService.deleteVnic(id, vcId);
	}



	// 클러스터 목록
	@GetMapping("/{id}/clusters")
	@ApiOperation(value = "네트워크 클러스터 목록", notes = "선택된 네트워크의 클러스터 목록을 조회한다")
	@ApiImplicitParam(name = "nVo", value = "네트워크", dataTypeClass = String.class)
	@ResponseBody
	public List<NetworkClusterVo> cluster(@PathVariable String id){
		log.info("--- Network 클러스터");
		return networkService.getCluster(id);
	}
	
	// 클러스터 네트워크 관리창
	@GetMapping("/{id}/clusters/{cId}/settings")
	@ResponseBody
	public NetworkUsageVo getUsage(@PathVariable String id, 
								   @PathVariable String cId){
		log.info("--- Network 클러스터 네트워크 관리 창");
		return networkService.getUsage(id, cId);
	}
	
	
	

	@GetMapping("/{id}/hosts")
	@ApiOperation(value = "네트워크 호스트 목록", notes = "선택된 네트워크의 호스트 목록을 조회한다")
	@ApiImplicitParam(name = "nVo", value = "네트워크", dataTypeClass = String.class)
	@ResponseBody
	public List<NetworkHostVo> host(@PathVariable String id){
		log.info("--- Network 호스트");
		return networkService.getHost(id);
	}

	@GetMapping("/{id}/vms")
	@ApiOperation(value = "네트워크 가상머신 목록", notes = "선택된 네트워크의 가상머신 목록을 조회한다")
	@ApiImplicitParam(name = "nVo", value = "네트워크", dataTypeClass = String.class)
	@ResponseBody
	public List<NetworkVmVo> vm(@PathVariable String id){
		log.info("--- Network 가상머신");
		return networkService.getVm(id);
	}

	// 가상머신 nic 제거
	@DeleteMapping("/{id}/vms/{vmId}/{nicId}")
	@ResponseBody
	public CommonVo<Boolean> deleteVmNic(@PathVariable String id,
										 @PathVariable String vmId,
										 @PathVariable String nicId){
		log.info("--- Network 가상머신 nic 제거");
		return networkService.deleteVmNic(id, vmId, nicId);
	}

	@GetMapping("/{id}/templates")
	@ApiOperation(value = "네트워크 템플릿 목록", notes = "선택된 네트워크의 템플릿 목록을 조회한다")
	@ApiImplicitParam(name = "nVo", value = "네트워크", dataTypeClass = String.class)
	@ResponseBody
	public List<NetworkTemplateVo> template(@PathVariable String id){
		log.info("--- Network 템플릿");
		return networkService.getTemplate(id);
	}

	// 템플릿 제거
	@DeleteMapping("/{id}/templates/{tempId}/{nicId}")
	@ResponseBody
	public CommonVo<Boolean> deleteTempNic(@PathVariable String id,
										   @PathVariable String tempId,
										   @PathVariable String nicId){
		log.info("--- Network 템플릿 nic 제거");
		return networkService.deleteTempNic(id, tempId, nicId);
	}


	// 권한 목록
	@GetMapping("/{id}/permissions")
	@ApiOperation(value = "네트워크 권한 목록", notes = "선택된 네트워크의 권한 목록을 조회한다")
	@ApiImplicitParam(name = "id", value = "네트워크 아이디", dataTypeClass = String.class)
	@ResponseBody
	public List<PermissionVo> permission(@PathVariable String id){
		log.info("--- Network 권한");
		return networkService.getPermission(id);
	}


}