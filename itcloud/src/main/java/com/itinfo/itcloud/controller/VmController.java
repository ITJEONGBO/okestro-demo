package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.storage.VmDiskVo;
import com.itinfo.itcloud.service.ItVmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/computing")
public class VmController {
	private final ItVmService vmService;

	@GetMapping("/vms")
	@ResponseBody
	public List<VmVo> vms() {
		log.info("--- 가상머신 리스트");
		return vmService.getList();
	}

	@GetMapping("/vm/{id}")
	@ResponseBody
	public VmVo vm(@PathVariable String id) {
		return vmService.getInfo(id);
	}

	@GetMapping("/vm/{id}/nics")
	@ResponseBody
	public List<NicVo> nic(@PathVariable String id) {
		log.info("----- vm nic 일반 불러오기: " + id);
		return vmService.getNic(id);
	}

	@GetMapping("/vm/{id}/disks")
	@ResponseBody
	public List<VmDiskVo> disk(@PathVariable String id) {
		log.info("----- vm disk 일반 불러오기: " + id);
		return vmService.getDisk(id);
	}

	@GetMapping("/vm/{id}/snapshots")
	@ResponseBody
	public List<SnapshotVo> snapshot(@PathVariable String id) {
		log.info("----- vm snapshot 불러오기: " + id);
		return vmService.getSnapshot(id);
	}

	@GetMapping("/vm/{id}/applications")
	@ResponseBody
	public List<ApplicationVo> app(@PathVariable String id) {
		log.info("----- vm app 불러오기: " + id);
		return vmService.getApplication(id);
	}

	@GetMapping("/vm/{id}/affinitygroups")
	@ResponseBody
	public List<AffinityGroupVo> affGroup(@PathVariable String id) {
		log.info("----- vm affGroup 불러오기: " + id);
		return vmService.getAffinitygroup(id);
	}

	@GetMapping("/vm/{id}/affinitylabels")
	@ResponseBody
	public List<AffinityLabelVo> affLabel(@PathVariable String id) {
		log.info("----- vm affLabel 불러오기: " + id);
		return vmService.getAffinitylabel(id);
	}

	@GetMapping("/vm/{id}/guests")
	@ResponseBody
	public GuestInfoVo guest(@PathVariable String id) {
		log.info("----- vm disk 일반 불러오기: " + id);
		return vmService.getGuestInfo(id);
	}

	@GetMapping("/vm/{id}/permissions")
	@ResponseBody
	public List<PermissionVo> permission(@PathVariable String id) {
		log.info("----- vm event 일반 불러오기: " + id);
		return vmService.getPermission(id);
	}

	@GetMapping("/vm/{id}/events")
	@ResponseBody
	public List<EventVo> event(@PathVariable String id) {
		log.info("----- vm event 일반 불러오기: " + id);
		return vmService.getEvent(id);
	}

}
