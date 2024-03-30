package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.AffinityLabelCreateVo;
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


	@GetMapping("/host/affinityLabels")
	@ResponseBody
	public List<AffinityLabelVo> getAffinitylabels(String id){
		return hostService.getAffinitylabels(id);
	}

	// 해당 cluster가 가지고 있는 host, 레이블 생성시 필요
	@GetMapping("/host/hostme")
	@ResponseBody
	public List<HostVo> getHostMember(String clusterId) {
		log.info("-----클러스터 호스트목록");
		return hostService.getHostMember(clusterId);
	}

	// 해당 cluster가 가지고 있는 vm, 레이블 생성시 필요
	@GetMapping("/host/vmme")
	@ResponseBody
	public List<VmVo> getVmMember(String clusterId) {
		log.info("-----클러스터 가상머신목록");
		return hostService.getVmMember(clusterId);
	}


	// 클러스터 선호도 레이블 생성
	@PostMapping("/host/affinityLabel/add")
	public CommonVo<Boolean> addAff(@RequestBody AffinityLabelCreateVo alVo) {
		log.info("--- 선호도 레이블 생성");
		return hostService.addAffinitylabel(alVo);
	}

	// 클러스터 선호도 레이블 편집
	@PostMapping("/host/affinityLabel/edit")
	public CommonVo<Boolean> editAff(@RequestBody AffinityLabelCreateVo alVo) {
		log.info("--- 선호도 레이블 편집");
		return hostService.editAffinitylabel(alVo);
	}

	// 클러스터 선호도 레이블 삭제
	@PostMapping("/host/affinityLabel/delete")
	public CommonVo<Boolean> deleteAff(String id) {
		log.info("--- 선호도 레이블 삭제");
		return hostService.deleteAffinitylabel(id);
	}


	//region: ResponseBody
	@GetMapping("/hosts/status")
	@ResponseBody
	public List<HostVo> hosts() {
		log.info("----- Host 목록 불러오기");
		return hostService.getList();
	}

	@GetMapping("/host/status")
	@ResponseBody
	public HostVo host(String id) {
		log.info("----- host id 일반 불러오기: " + id);
		return hostService.getInfo(id);
	}

	@GetMapping("/host/vmstatus")
	@ResponseBody
	public List<VmVo> vm(String id) {
		log.info("----- host vm 일반 불러오기: " + id);
		return hostService.getVm(id);
	}

	@GetMapping("/host/nicstatus")
	@ResponseBody
	public List<NicVo> nic(String id) {
		log.info("----- host nic 일반 불러오기: " + id);
		return hostService.getNic(id);
	}

	@GetMapping("/host/devicestatus")
	@ResponseBody
	public List<HostDeviceVo> device(String id) {
		log.info("----- host device 일반 불러오기: " + id);
		return hostService.getHostDevice(id);
	}

	@GetMapping("/host/permissionStatus")
	@ResponseBody
	public List<PermissionVo> permission(String id) {
		log.info("----- host permission 불러오기: " + id);
		return hostService.getPermission(id);
	}

	@GetMapping("/host/affstatus")
	@ResponseBody
	public List<AffinityLabelVo> aff(String id) {
		log.info("----- host aff 일반 불러오기: " + id);
		return hostService.getAffinitylabels(id);
	}

	@GetMapping("/host/eventStatus")
	@ResponseBody
	public List<EventVo> event(String id) {
		log.info("----- event 목록 불러오기: " + id);
		return hostService.getEvent(id);
	}

	//endregion

}
