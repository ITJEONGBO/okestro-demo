package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.storage.VmDiskVo;
import com.itinfo.itcloud.service.ItSystemPropertyService;
import com.itinfo.itcloud.service.ItVmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/computing")
public class VmController {
	private final ItSystemPropertyService itSystemPropertyService;
	private final ItVmService itVmService;

	//region: ResponseBody

	@GetMapping("/vmsList")
	@ResponseBody
	public List<VmVo> vms() {
		long start = System.currentTimeMillis();

		List<VmVo> vmsList = itVmService.getList();

		long end = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		log.debug("수행시간(ms): {}", end - start);

		return vmsList;
	}

	@GetMapping("/vm/status")
	@ResponseBody
	public VmVo vm(String id) {
		return itVmService.getInfo(id);
	}

	@GetMapping("/vm/nicstatus")
	@ResponseBody
	public List<NicVo> nic(String id) {
		log.info("----- vm nic 일반 불러오기: " + id);
		return itVmService.getNic(id);
	}

	@GetMapping("/vm/diskstatus")
	@ResponseBody
	public List<VmDiskVo> disk(String id) {
		log.info("----- vm disk 일반 불러오기: " + id);
		return itVmService.getDisk(id);
	}

	@GetMapping("/vm/snapshotstatus")
	@ResponseBody
	public List<SnapshotVo> snapshot(String id) {
		log.info("----- vm snapshot 불러오기: " + id);
		return itVmService.getSnapshot(id);
	}

	@GetMapping("/vm/applicationstatus")
	@ResponseBody
	public List<ApplicationVo> app(String id) {
		log.info("----- vm app 불러오기: " + id);
		return itVmService.getApplication(id);
	}

	@GetMapping("/vm/affGroupstatus")
	@ResponseBody
	public List<AffinityGroupVo> affGroup(String id) {
		log.info("----- vm affGroup 불러오기: " + id);
		return itVmService.getAffinitygroup(id);
	}

	@GetMapping("/vm/affLabelstatus")
	@ResponseBody
	public List<AffinityLabelVo> affLabel(String id) {
		log.info("----- vm affLabel 불러오기: " + id);
		return itVmService.getAffinitylabel(id);
	}

	@GetMapping("/vm/gueststatus")
	@ResponseBody
	public GuestInfoVo guest(String id) {
		log.info("----- vm disk 일반 불러오기: " + id);
		return itVmService.getGuestInfo(id);
	}

	@GetMapping("/vm/permissionstatus")
	@ResponseBody
	public List<PermissionVo> permission(String id) {
		log.info("----- vm event 일반 불러오기: " + id);
		return itVmService.getPermission(id);
	}

	@GetMapping("/vm/eventstatus")
	@ResponseBody
	public List<EventVo> event(String id) {
		log.info("----- vm event 일반 불러오기: " + id);
		return itVmService.getEvent(id);
	}
	//endregion

}
