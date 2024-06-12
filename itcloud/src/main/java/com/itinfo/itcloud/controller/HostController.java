package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.HostCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.service.ItHostService;
import com.itinfo.itcloud.service.ItVmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/computing")
public class HostController {
	private final ItHostService hostService;
	private final ItVmService vmService;


	@GetMapping("/hosts")
	@ResponseBody
	public List<HostVo> hosts() {
		log.info("--- 호스트 목록");
		return hostService.getList();
	}

	@GetMapping("/host/settings")
	@ResponseBody
	public List<ClusterVo> setClusterList(){
		log.info("--- 호스트 생성 창");
		return hostService.setClusterList();
	}

	@PostMapping("/host")
	@ResponseBody
	public CommonVo<Boolean> addHost(@RequestBody HostCreateVo hVo){
		log.info("--- 호스트 생성");
		return hostService.addHost(hVo);
	}

	@GetMapping("/host/{id}/settings")
	@ResponseBody
	public HostCreateVo getHostCreate(@PathVariable String id){
		log.info("--- 호스트 수정창");
		return hostService.setHost(id);
	}

	@PutMapping("/host/{id}")
	@ResponseBody
	public CommonVo<Boolean> editHost(@PathVariable String id,
									  @RequestBody HostCreateVo hVo){
		log.info("--- 호스트 수정");
		return hostService.editHost(hVo);
	}

	@DeleteMapping("/host/{id}")
	@ResponseBody
	public CommonVo<Boolean> deleteHost(@PathVariable String id){
		log.info("--- 호스트 삭제");
		return hostService.deleteHost(id);
	}

	
	@PostMapping("/host/{id}/deactivate")
	@ResponseBody
	public void deactiveHost(@PathVariable String id) {
		log.info("--- 호스트 유지보수");
		hostService.deactiveHost(id);
	}

	@PostMapping("/host/{id}/activate")
	@ResponseBody
	public void activeHost(@PathVariable String id) {
		log.info("--- 호스트 활성");
		hostService.activeHost(id);
	}

	@PostMapping("/host/{id}/refresh")
	@ResponseBody
	public void refreshHost(@PathVariable String id) {
		log.info("--- 호스트 새로고침");
		hostService.refreshHost(id);
	}

	
	
	@PostMapping("/host/{id}/restart")
	@ResponseBody
	public void reStartHost(@PathVariable String id) throws UnknownHostException {
		log.info("--- 호스트 ssh 재시작");
		hostService.reStartHost(id);
	}





	@GetMapping("/host/{id}")
	@ResponseBody
	public HostVo getHost(@PathVariable String id) {
		log.info("--- 호스트 일반");
		return hostService.getHost(id);
	}



	@GetMapping("/host/{id}/vms")
	@ResponseBody
	public List<VmVo> getvm(@PathVariable String id) {
		log.info("--- 호스트 vm");
		return hostService.getVm(id);
	}

//	@PostMapping("/host/{id}/vms/{vmId}/start")
//	@ResponseBody
//	public CommonVo<Boolean> startHostVm(@PathVariable String id, @PathVariable String vmId){
//		log.info("--- 호스트 가상머신 실행");
//		return vmService.startVm(vmId);
//	}
//
//	@PostMapping("/host/{id}/vms/{vmId}/pause")
//	@ResponseBody
//	public CommonVo<Boolean> pauseHostVm(@PathVariable String id, @PathVariable String vmId){
//		log.info("--- 호스트 가상머신 일시정지");
//		return vmService.pauseVm(vmId);
//	}







	@GetMapping("/host/{id}/nics")
	@ResponseBody
	public List<NicVo> nic(@PathVariable String id) {
		log.info("--- 호스트 nic");
		return hostService.getNic(id);
	}


	@GetMapping("/host/{id}/devices")
	@ResponseBody
	public List<HostDeviceVo> device(@PathVariable String id) {
		log.info("--- 호스트 장치");
		return hostService.getHostDevice(id);
	}


	@GetMapping("/host/{id}/permissions")
	@ResponseBody
	public List<PermissionVo> permission(@PathVariable String id) {
		log.info("--- 호스트 권한");
		return hostService.getPermission(id);
	}


//	@GetMapping("/host/{id}/affinitylabels")
//	@ResponseBody
//	public List<AffinityLabelVo> getAffinitylabels(@PathVariable String id) {
//		log.info("--- 호스트 선호도 레이블");
//		return hostService.getAffinitylabels(id);
//	}
//
//
//
//	@GetMapping("/host/{id}/affinitylabel/settings")
//	@ResponseBody
//	public AffinityHostVm setAffinitygroup(@PathVariable String id){
//		log.info("--- 호스트 선호도 레이블 생성 창");
//		return hostService.setAffinityDefaultInfo(id, "label");
//	}
//
//
//
//	@PostMapping("/host/{id}/affinitylabel")
//	@ResponseBody
//	public CommonVo<Boolean> addAff(@PathVariable String id,
//									@RequestBody AffinityLabelCreateVo alVo) {
//		log.info("--- 호스트 선호도 레이블 생성");
//		return hostService.addAffinitylabel(id, alVo);
//	}
//
//	@GetMapping("/host/{id}/affinitylabel/{alId}/settings")
//	@ResponseBody
//	public AffinityHostVm setAffinityDefaultInfo(@PathVariable String id,
//												 @PathVariable String type){
//		log.info("--- 호스트 선호도 레이블 편집 창");
//		return hostService.setAffinityDefaultInfo(id, "label");
//	}
//
//
//	@PutMapping("/host/{id}/affinitylabel/{alId}")
//	@ResponseBody
//	public CommonVo<Boolean> editAff(@PathVariable String id,
//									 @PathVariable String alId,
//									 @RequestBody AffinityLabelCreateVo alVo) {
//		log.info("--- 호스트 선호도 레이블 편집");
//		return hostService.editAffinitylabel(id, alId, alVo);
//	}
//
//	@DeleteMapping("/host/{id}/affinitylabel/{alId}")
//	@ResponseBody
//	public CommonVo<Boolean> deleteAff(@PathVariable String id,
//									   @PathVariable String alId) {
//		log.info("--- 호스트 선호도 레이블 삭제");
//		return hostService.deleteAffinitylabel(id, alId);
//	}


	@GetMapping("/host/{id}/events")
	@ResponseBody
	public List<EventVo> event(@PathVariable String id) {
		log.info("--- 호스트 이벤트");
		return hostService.getEvent(id);
	}


}