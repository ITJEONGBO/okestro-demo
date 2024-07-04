package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.VmCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import com.itinfo.itcloud.model.storage.VmDiskVo;
import com.itinfo.itcloud.service.computing.ItVmService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@Api(tags = "vm")
@RequestMapping("/computing")
public class VmController {
	private final ItVmService vmService;

	@GetMapping("/vms")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<VmVo> vms() {
		log.info("--- 가상머신 리스트");
		return vmService.getList();
	}



	@GetMapping("/vm/settings/vnic")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<VnicProfileVo> setVnic() {
		log.info("--- 가상머신 생성 창 - vnic");
//		return vmService.setVnic();
		return null;
	}



	@PostMapping("/vm")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public CommonVo<Boolean> addVm(@RequestBody VmCreateVo vm) {
		log.info("--- 가상머신 생성");
		return vmService.addVm(vm);
	}

	@PostMapping("/vm/{id}/setting")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public VmCreateVo getVmCreate(@PathVariable String id) {
		log.info("--- 가상머신 편집 창");
		return vmService.setVm(id);
	}

	@PutMapping("/vm/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public CommonVo<Boolean> editVm(@PathVariable String id,
									@RequestBody VmCreateVo vm) {
		log.info("--- 가상머신 편집");
		return vmService.editVm(vm);
	}

	@DeleteMapping("/vm/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonVo<Boolean> deleteVm(@PathVariable String id) {
		boolean type = false;
		log.info("--- 가상머신 삭제");

		return vmService.deleteVm(id, type);
	}







	@GetMapping("/vm/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public VmVo vm(@PathVariable String id) {
		log.info("--- 가상머신 일반");
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
	public List<IdentifiedVo> app(@PathVariable String id) {
		log.info("----- vm app 불러오기: " + id);
		return vmService.getApplication(id);
	}

//	@GetMapping("/vm/{id}/affinitygroups")
//	@ResponseBody
//	public List<AffinityGroupVo> affGroup(@PathVariable String id) {
//		log.info("----- vm affGroup 불러오기: " + id);
//		return vmService.getAffinitygroup(id);
//	}
//
//	@GetMapping("/vm/{id}/affinitylabels")
//	@ResponseBody
//	public List<AffinityLabelVo> affLabel(@PathVariable String id) {
//		log.info("----- vm affLabel 불러오기: " + id);
//		return vmService.getAffinitylabel(id);
//	}

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




//	@PostMapping("/vm/{id}/console")
//	@ResponseStatus(HttpStatus.OK)
//	public com.itinfo.itcloud.model.ConsoleVo console(@PathVariable String id,
//													  @RequestBody ConsoleVo consoleVo) {
//
//		log.info("--- 가상머신 콘솔");
//		return vmService.getConsole(id, consoleVo);
//	}


	@GetMapping("/vmConsole/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ConsoleVo setConsole(@PathVariable String id) {
		return vmService.getConsole(id);
	}

	@GetMapping("/vmConsole/vncView")
	public String vncView() {
		return "vnc";
	}


}
