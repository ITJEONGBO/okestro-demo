package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.AffinityLabelCreateVo;
import com.itinfo.itcloud.model.create.HostCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.service.ItHostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/computing")
public class HostController {
	private final ItHostService hostService;


	// 호스트 목록
	@GetMapping("/hosts")
	@ResponseBody
	public List<HostVo> hosts() {
		log.info("----- Host 목록 불러오기");
		return hostService.getList();
	}

	// 호스트 생성 기본 창
	@GetMapping("/host/settings")
	@ResponseBody
	public List<ClusterVo> setHostDefaultInfo(){
		log.info("----- Host 생성시 필요한 cluster List 불러오기");
		return hostService.setHostDefaultInfo();
	}

	// 호스트 생성
	@PostMapping("/host")
	@ResponseBody
	public CommonVo<Boolean> addHost(@RequestBody HostCreateVo hVo){
		log.info("호스트 생성");
		return hostService.addHost(hVo);
	}

	// 호스트 수정 창
	@GetMapping("/host/{id}/settings")
	@ResponseBody
	public HostCreateVo getHostCreate(@PathVariable String id){
		log.info("호스트 수정창");
		return hostService.getHostCreate(id);
	}

	// 호스트 수정
	@PutMapping("/host/{id}")
	@ResponseBody
	public CommonVo<Boolean> editHost(@PathVariable String id,
									  @RequestBody HostCreateVo hVo){
		log.info("호스트 수정");
		return hostService.editHost(id, hVo);
	}

	// 호스트 삭제
	@DeleteMapping("/host/{id}")
	@ResponseBody
	public CommonVo<Boolean> deleteHost(@PathVariable String id){
		log.info("호스트 삭제");
		return hostService.deleteHost(id);
	}

	


	// 호스트 일반 출력
	@GetMapping("/host/{id}")
	@ResponseBody
	public HostVo host(@PathVariable String id) {
		log.info("----- host id 일반 불러오기: " + id);
		return hostService.getInfo(id);
	}

	@GetMapping("/host/{id}/vms")
	@ResponseBody
	public List<VmVo> vm(@PathVariable String id) {
		log.info("----- host vm 일반 불러오기: " + id);
		return hostService.getVm(id);
	}

	@GetMapping("/host/{id}/nics")
	@ResponseBody
	public List<NicVo> nic(@PathVariable String id) {
		log.info("----- host nic 일반 불러오기: " + id);
		return hostService.getNic(id);
	}

	@GetMapping("/host/{id}/devices")
	@ResponseBody
	public List<HostDeviceVo> device(@PathVariable String id) {
		log.info("----- host device 일반 불러오기: " + id);
		return hostService.getHostDevice(id);
	}

	@GetMapping("/host/{id}/permissions")
	@ResponseBody
	public List<PermissionVo> permission(@PathVariable String id) {
		log.info("----- host permission 불러오기: " + id);
		return hostService.getPermission(id);
	}

	@GetMapping("/host/{id}/affinitylabels")
	@ResponseBody
	public List<AffinityLabelVo> getAffinitylabels(@PathVariable String id) {
		log.info("----- host aff 일반 불러오기: " + id);
		return hostService.getAffinitylabels(id);
	}

	// TODO cluster
	// 해당 cluster가 가지고 있는 host, 레이블 생성시 필요
	// 해당 cluster가 가지고 있는 vm, 레이블 생성시 필요
	// 클러스터 선호도 그룹 생성위한 목록
//	@GetMapping("/cluster/{id}/affinitygroup/settings")
//	@ResponseBody
//	public AffinityHostVm setAffinitygroup(@PathVariable String id){
//		String type = "group";
//		return clusterService.setAffinityDefaultInfo(id, type);
//	}


	// 클러스터 선호도 레이블 생성
	@PostMapping("/host/{id}/affinitylabel")
	public CommonVo<Boolean> addAff(@PathVariable String id,
									@RequestBody AffinityLabelCreateVo alVo) {
		log.info("--- 선호도 레이블 생성");
		return hostService.addAffinitylabel(alVo);
	}

	// 클러스터 선호도 레이블 편집
	@PutMapping("/host/{id}/affinitylabel/{alId}")
	public CommonVo<Boolean> editAff(@PathVariable String id,
									 @PathVariable String alId,
									 @RequestBody AffinityLabelCreateVo alVo) {
		log.info("--- 선호도 레이블 편집");
		return hostService.editAffinitylabel(alVo);
	}

	// 클러스터 선호도 레이블 삭제
	@DeleteMapping("/host/{id}/affinitylabel/{alId}")
	public CommonVo<Boolean> deleteAff(@PathVariable String id,
									   @PathVariable String alId) {
		log.info("--- 선호도 레이블 삭제");
		return hostService.deleteAffinitylabel(id);
	}

	@GetMapping("/host/{id}/events")
	@ResponseBody
	public List<EventVo> event(@PathVariable String id) {
		log.info("----- event 목록 불러오기: " + id);
		return hostService.getEvent(id);
	}





}