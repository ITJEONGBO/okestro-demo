package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.create.DataCenterCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.service.ItDataCenterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/computing")
public class DataCenterController {
	private final ItDataCenterService dcService;


	@GetMapping("/hello")
	@ResponseBody
	public String hello(){
		return "hello";
	}


	@GetMapping("/datacenters")
	@ResponseBody
	public List<DataCenterVo> datacenters() {
		log.info("----- 데이터센터 목록");
		return dcService.getList();
	}

	@GetMapping("/datacenter/{id}/events")
	@ResponseBody
	public List<EventVo> event(@PathVariable String id) {
		log.info("----- 데이터센터 이벤트 : " + id);
		return dcService.getEvent(id);
	}

	@GetMapping("/datacenter/{id}")
	@ResponseBody
	public DataCenterCreateVo getDatacenter(@PathVariable String id){
		return dcService.getDatacenter(id);
	}


	@PostMapping("/datacenter/add")
	@ResponseBody
	public CommonVo<Boolean> addDatacenter(@RequestBody DataCenterCreateVo dcVo){
		log.info("----- 데이터센터 추가 : " + dcVo.getName());
		return dcService.addDatacenter(dcVo);
	}

	@PostMapping("/datacenter/edit")
	@ResponseBody
	public CommonVo<Boolean> editDatacenter(@RequestBody DataCenterCreateVo dcVo){
		log.info("----- 데이터센터 편집 : " + dcVo.getName());
		return dcService.editDatacenter(dcVo);
	}

	@PostMapping("/datacenter/delete")
	@ResponseBody
	public CommonVo<Boolean> deleteDatacenter(@RequestParam String id){
		log.info("----- 데이터센터 삭제");
		return dcService.deleteDatacenter(id);
	}



	// region: 안쓸것 같음


//	@GetMapping("/datacenter/storageStatus")
//	@ResponseBody
//	public List<DomainVo> storage(String id) {
//		log.info("-----datacenter/storageStatus: " + id);
//		return itDataCenterService.getStorage(id);
//	}
//
//
//	@GetMapping("/datacenter/networkStatus")
//	@ResponseBody
//	public List<NetworkVo> network(String id) {
//		log.info("----- 데이터센터 network 목록 불러오기: " + id);
//		return itDataCenterService.getNetwork(id);
//	}
//
//
//	@GetMapping("/datacenter/clusterStatus")
//	@ResponseBody
//	public List<ClusterVo> cluster(String id) {
//		log.info("----- 데이터센터 cluster 목록 불러오기: " + id);
//		return itDataCenterService.getCluster(id);
//	}
//
//
//	@GetMapping("/datacenter/permissionStatus")
//	@ResponseBody
//	public List<PermissionVo> permission(String id) {
//		log.info("----- permission 목록 불러오기: " + id);
//		return itDataCenterService.getPermission(id);
//	}


	// endregion

}
