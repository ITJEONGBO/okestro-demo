package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.service.ItVmService;
import com.itinfo.itcloud.service.ItSystemPropertyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class VmController {
	private final ItVmService itVmService;
	private final ItSystemPropertyService itSystemPropertyService;


	@GetMapping("/computing/vms")
	public String vmList(Model model) {
		List<VmVo> vms = itVmService.getList();
		model.addAttribute("vms", vms);
		return "computing/vms";
	}

	@GetMapping("/vmsList")
	@ResponseBody
	public List<VmVo> vms() {
		long start = System.currentTimeMillis();

		List<VmVo> vmsList = itVmService.getList();

		long end = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		log.debug("수행시간(ms): {}", end - start);

		return vmsList;
	}

	@GetMapping("/computing/vm")
	public String vm(String id, Model model) {
		VmVo vm = itVmService.getInfo(id);
		model.addAttribute("vm", vm);
		model.addAttribute("id", id);

		return "computing/vm";
	}

	@GetMapping("/computing/vm/status")
	@ResponseBody
	public VmVo vm(String id) {
		log.info("----- vm id 일반 불러오기: {}", id);
		return itVmService.getInfo(id);
	}


	@GetMapping("/computing/vm-nic")
	public String nic(String id, Model model) {
		List<NicVo> nic = itVmService.getNic(id);
		model.addAttribute("nic", nic);
		model.addAttribute("id", id);

		return "computing/vm-nic";
	}

	@GetMapping("/computing/vm/nicstatus")
	@ResponseBody
	public List<NicVo> nic(String id) {
		log.info("----- vm nic 일반 불러오기: " + id);
		return itVmService.getNic(id);
	}


	@GetMapping("/computing/vm-disk")
	public String disk(String id, Model model) {
		List<VmDiskVo> disk = itVmService.getDisk(id);
		model.addAttribute("disk", disk);
		model.addAttribute("id", id);

		return "computing/vm-disk";
	}

	@GetMapping("/computing/vm/diskstatus")
	@ResponseBody
	public List<VmDiskVo> disk(String id) {
		log.info("----- vm disk 일반 불러오기: " + id);
		return itVmService.getDisk(id);
	}


	@GetMapping("/computing/vm-snapshot")
	public String snapshot(String id, Model model) {
		log.info("---snapshot");
		List<SnapshotVo> snapshot = itVmService.getSnapshot(id);
		model.addAttribute("snapshot", snapshot);
		model.addAttribute("id", id);

		return "computing/vm-snapshot";
	}

	@GetMapping("/computing/vm/snapshotstatus")
	@ResponseBody
	public List<SnapshotVo> snapshot(String id) {
		log.info("----- vm snapshot 불러오기: " + id);
		return itVmService.getSnapshot(id);
	}


	@GetMapping("/computing/vm-application")
	public String application(String id, Model model) {
		List<ApplicationVo> app = itVmService.getApplication(id);
		model.addAttribute("app", app);
		model.addAttribute("id", id);

		return "computing/vm-application";
	}

	@GetMapping("/computing/vm/applicationstatus")
	@ResponseBody
	public List<ApplicationVo> app(String id) {
		log.info("----- vm app 불러오기: " + id);
		return itVmService.getApplication(id);
	}


	@GetMapping("/computing/vm-affGroup")
	public String affGroup(String id, Model model) {
		List<AffinityGroupVo> aff = itVmService.getAffinitygroup(id);
		model.addAttribute("aff", aff);
		model.addAttribute("id", id);

		return "computing/vm-affGroup";
	}

	@GetMapping("/computing/vm/affGroupstatus")
	@ResponseBody
	public List<AffinityGroupVo> affGroup(String id) {
		log.info("----- vm affGroup 불러오기: " + id);
		return itVmService.getAffinitygroup(id);
	}


	@GetMapping("/computing/vm-affLabel")
	public String affLabel(String id, Model model) {
		List<AffinityLabelVo> aff = itVmService.getAffinitylabel(id);
		model.addAttribute("aff", aff);
		model.addAttribute("id", id);

		return "computing/vm-affLabel";
	}

	@GetMapping("/computing/vm/affLabelstatus")
	@ResponseBody
	public List<AffinityLabelVo> affLabel(String id) {
		log.info("----- vm affLabel 불러오기: " + id);
		return itVmService.getAffinitylabel(id);
	}


	@GetMapping("/computing/vm-guest")
	public String guest(String id, Model model) {
		GuestInfoVo guest = itVmService.getGuestInfo(id);
		model.addAttribute("guest", guest);
		model.addAttribute("id", id);

		return "computing/vm-guest";
	}

	@GetMapping("/computing/vm/gueststatus")
	@ResponseBody
	public GuestInfoVo guest(String id) {
		log.info("----- vm disk 일반 불러오기: " + id);
		return itVmService.getGuestInfo(id);
	}


	@GetMapping("/computing/vm-permission")
	public String permission(String id, Model model) {
		List<PermissionVo> permission = itVmService.getPermission(id);
		model.addAttribute("permission", permission);
		model.addAttribute("id", id);

		return "computing/vm-permission";
	}

	@GetMapping("/computing/vm/permissionstatus")
	@ResponseBody
	public List<PermissionVo> permission(String id) {
		log.info("----- vm event 일반 불러오기: " + id);
		return itVmService.getPermission(id);
	}


	@GetMapping("/computing/vm-event")
	public String event(String id, Model model) {
		List<EventVo> event = itVmService.getEvent(id);
		model.addAttribute("event", event);
		model.addAttribute("id", id);

		return "computing/vm-event";
	}

	@GetMapping("/computing/vm/eventstatus")
	@ResponseBody
	public List<EventVo> event(String id) {
		log.info("----- vm event 일반 불러오기: " + id);
		return itVmService.getEvent(id);
	}


}
