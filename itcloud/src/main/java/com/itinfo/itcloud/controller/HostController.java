package com.itinfo.itcloud.controller;

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
		log.info("--- Host 목록");
		return hostService.getList();
	}

	// 호스트 생성 창
	@GetMapping("/host/settings")
	@ResponseBody
	public List<ClusterVo> setHostDefaultInfo(){
		log.info("--- Host 생성창");
		return hostService.setHostDefaultInfo();
	}

	// 호스트 생성
	@PostMapping("/host")
	@ResponseBody
	public CommonVo<Boolean> addHost(@RequestBody HostCreateVo hVo){
		log.info("--- Host 생성");
		return hostService.addHost(hVo);
	}

	// 호스트 수정 창
	@GetMapping("/host/{id}/settings")
	@ResponseBody
	public HostCreateVo getHostCreate(@PathVariable String id){
		log.info("--- Host 수정창");
		return hostService.setHost(id);
	}

	// 호스트 수정
	@PutMapping("/host/{id}")
	@ResponseBody
	public CommonVo<Boolean> editHost(@PathVariable String id,
									  @RequestBody HostCreateVo hVo){
		log.info("--- Host 수정");
		return hostService.editHost(id, hVo);
	}

	// 호스트 삭제
	@DeleteMapping("/host/{id}")
	@ResponseBody
	public CommonVo<Boolean> deleteHost(@PathVariable String id){
		log.info("--- Host 삭제");
		return hostService.deleteHost(id);
	}

	
	// 호스트 유지보수
	@PostMapping("/host/{id}/deactivate")
	@ResponseBody
	public void deActive(@PathVariable String id) {
		log.info("--- Host 유지보수");
		hostService.deActive(id);
	}

	// 호스트 활성
	@PostMapping("/host/{id}/activate")
	@ResponseBody
	public void active(@PathVariable String id) {
		log.info("--- Host 활성");
		hostService.active(id);
	}

	// 호스트 새로고침
	@PostMapping("/host/{id}/refresh")
	@ResponseBody
	public void refresh(@PathVariable String id) {
		log.info("--- Host 새로고침");
		hostService.refresh(id);
	}

	
	
	// 호스트 ssh 재시작
	@PostMapping("/host/{id}/restart")
	@ResponseBody
	public void reStart(@PathVariable String id) {
		log.info("--- Host 재시작");
		hostService.reStart(id);
	}


	// 호스트 ssh 중지
	@PostMapping("/host/{id}/stop")
	@ResponseBody
	public void stop(@PathVariable String id) {
		log.info("--- Host 정지");
		hostService.stop(id);
	}



	
	



	


	// 일반
	@GetMapping("/host/{id}")
	@ResponseBody
	public HostVo host(@PathVariable String id) {
		log.info("--- Host 일반");
		return hostService.getInfo(id);
	}

	// 가상머신
	@GetMapping("/host/{id}/vms")
	@ResponseBody
	public List<VmVo> vm(@PathVariable String id) {
		log.info("--- Host vm");
		return hostService.getVm(id);
	}

	// 네트워크 인터페이스
	@GetMapping("/host/{id}/nics")
	@ResponseBody
	public List<NicVo> nic(@PathVariable String id) {
		log.info("--- Host nic");
		return hostService.getNic(id);
	}

	// 호스트 장치
	@GetMapping("/host/{id}/devices")
	@ResponseBody
	public List<HostDeviceVo> device(@PathVariable String id) {
		log.info("--- Host 장치");
		return hostService.getHostDevice(id);
	}

	// 권한
	@GetMapping("/host/{id}/permissions")
	@ResponseBody
	public List<PermissionVo> permission(@PathVariable String id) {
		log.info("--- Host 권한");
		return hostService.getPermission(id);
	}

	// 선호도 레이블
	@GetMapping("/host/{id}/affinitylabels")
	@ResponseBody
	public List<AffinityLabelVo> getAffinitylabels(@PathVariable String id) {
		log.info("--- Host 선호도 레이블");
		return hostService.getAffinitylabels(id);
	}


	// 호스트 선호도 그룹 생성위한 목록
	// 해당 host가 가지고 있는 cluster가 가지고 있는 host, vm, 레이블 생성시 필요
	@GetMapping("/host/{id}/affinitylabel/settings")
	@ResponseBody
	public AffinityHostVm setAffinitygroup(@PathVariable String id){
		log.info("--- Host 선호도 레이블 생성 창");
		return hostService.setAffinityDefaultInfo(id, "label");
	}


	// 선호도 레이블 생성
	@PostMapping("/host/{id}/affinitylabel")
	@ResponseBody
	public CommonVo<Boolean> addAff(@PathVariable String id,
									@RequestBody AffinityLabelCreateVo alVo) {
		log.info("--- Host 선호도 레이블 생성");
		return hostService.addAffinitylabel(id, alVo);
	}

	// 선호도 레이블 편집 창
	@GetMapping("/host/{id}/affinitylabel/{alId}/settings")
	@ResponseBody
	public AffinityHostVm setAffinityDefaultInfo(@PathVariable String id,
												 @PathVariable String type){
		log.info("--- Host 선호도 레이블 편집 창");
		return hostService.setAffinityDefaultInfo(id, "label");
	}


	// 선호도 레이블 편집
	@PutMapping("/host/{id}/affinitylabel/{alId}")
	@ResponseBody
	public CommonVo<Boolean> editAff(@PathVariable String id,
									 @PathVariable String alId,
									 @RequestBody AffinityLabelCreateVo alVo) {
		log.info("--- Host 선호도 레이블 편집");
		return hostService.editAffinitylabel(id, alId, alVo);
	}

	// 선호도 레이블 삭제
	@DeleteMapping("/host/{id}/affinitylabel/{alId}")
	@ResponseBody
	public CommonVo<Boolean> deleteAff(@PathVariable String id,
									   @PathVariable String alId) {
		log.info("--- Host 선호도 레이블 삭제");
		return hostService.deleteAffinitylabel(id, alId);
	}


	// 이벤트
	@GetMapping("/host/{id}/events")
	@ResponseBody
	public List<EventVo> event(@PathVariable String id) {
		log.info("--- Host 이벤트");
		return hostService.getEvent(id);
	}


}