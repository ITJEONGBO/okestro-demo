package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.VmCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import com.itinfo.itcloud.model.storage.VmDiskVo;
import com.itinfo.itcloud.service.computing.ItVmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "Computing-Vm")
@RequestMapping("/computing/vms")
public class VmController {
	private final ItVmService vmService;

	@GetMapping
	@ApiOperation(value = "가상머신 목록", notes = "전체 가상머신 목록을 조회한다")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<VmVo> vms() {
		log.info("--- 가상머신 리스트");
		return vmService.getVms();
	}



	@GetMapping("/settings/vnic")
	@ApiOperation(value = "가상머신 생성창", notes = "가상머신 생성시 필요한 내용을 조회한다")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<VnicProfileVo> setVnic() {
		log.info("--- 가상머신 생성 창 - vnic");
//		return vmService.setVnic();
		return null;
	}



	@PostMapping
	@ApiOperation(value = "가상머신 생성", notes = "가상머신을 생성한다")
	@ApiImplicitParam(name = "vm", value = "가상머신", dataTypeClass = VmCreateVo.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public CommonVo<Boolean> addVm(@RequestBody VmCreateVo vm) {
		log.info("--- 가상머신 생성");
		return vmService.addVm(vm);
	}

	@PostMapping("/{id}/edit")
	@ApiOperation(value = "가상머신 수정창", notes = "선택된 가상머신의 정보를 조회한다")
	@ApiImplicitParam(name = "id", value = "가상머신 아이디", dataTypeClass = String.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public VmCreateVo getVmCreate(@PathVariable String id) {
		log.info("--- 가상머신 편집 창");
		return vmService.setVm(id);
	}

	@PutMapping("/{id}")
	@ApiOperation(value = "가상머신 수정", notes = "가상머신을 수정한다")
	@ApiImplicitParam(name = "id", value = "가상머신 아이디", dataTypeClass = String.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public CommonVo<Boolean> editVm(@PathVariable String id,
									@RequestBody VmCreateVo vm) {
		log.info("--- 가상머신 편집");
		return vmService.editVm(vm);
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "가상머신 삭제", notes = "가상머신을 삭제한다")
	@ApiImplicitParam(name = "id", value = "가상머신 아이디", dataTypeClass = String.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonVo<Boolean> deleteVm(@PathVariable String id) {
		boolean type = false;
		log.info("--- 가상머신 삭제");

		return vmService.deleteVm(id, type);
	}







	@GetMapping("/{id}")
	@ApiOperation(value = "가상머신 상세정보", notes = "가상머신의 상세정보를 조회한다")
	@ApiImplicitParam(name = "id", value = "가상머신 아이디", dataTypeClass = String.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public VmVo vm(@PathVariable String id) {
		log.info("--- 가상머신 일반");
		return vmService.getVm(id);
	}

	@GetMapping("/{id}/nics")
	@ApiOperation(value = "가상머신 네트워크 인터페이스 목록", notes = "선택된 가상머신의 네트워크 인터페이스 목록을 조회한다")
	@ApiImplicitParam(name = "id", value = "가상머신 아이디", dataTypeClass = String.class)
	@ResponseBody
	public List<NicVo> nic(@PathVariable String id) {
		log.info("----- vm nic 일반 불러오기: " + id);
		return vmService.getNicsByVm(id);
	}

	@GetMapping("/{id}/disks")
	@ApiOperation(value = "가상머신 디스크 목록", notes = "선택된 가상머신의 디스크 목록을 조회한다")
	@ApiImplicitParam(name = "id", value = "가상머신 아이디", dataTypeClass = String.class)
	@ResponseBody
	public List<VmDiskVo> disk(@PathVariable String id) {
		log.info("----- vm disk 일반 불러오기: " + id);
		return vmService.getDisksByVm(id);
	}

	@GetMapping("/{id}/snapshots")
	@ApiOperation(value = "가상머신 스냅샷 목록", notes = "선택된 가상머신의 스냅샷 목록을 조회한다")
	@ApiImplicitParam(name = "id", value = "가상머신 아이디", dataTypeClass = String.class)
	@ResponseBody
	public List<SnapshotVo> snapshot(@PathVariable String id) {
		log.info("----- vm snapshot 불러오기: " + id);
		return vmService.getSnapshotsByVm(id);
	}

	@GetMapping("/{id}/applications")
	@ApiOperation(value = "가상머신 어플리케이션 목록", notes = "선택된 가상머신의 어플리케이션 목록을 조회한다")
	@ApiImplicitParam(name = "id", value = "가상머신 아이디", dataTypeClass = String.class)
	@ResponseBody
	public List<IdentifiedVo> app(@PathVariable String id) {
		log.info("----- vm app 불러오기: " + id);
		return vmService.getApplicationsByVm(id);
	}

//	@GetMapping("/{id}/affinitygroups")
//	@ResponseBody
//	public List<AffinityGroupVo> affGroup(@PathVariable String id) {
//		log.info("----- vm affGroup 불러오기: " + id);
//		return vmService.getAffinitygroup(id);
//	}
//
//	@GetMapping("/{id}/affinitylabels")
//	@ResponseBody
//	public List<AffinityLabelVo> affLabel(@PathVariable String id) {
//		log.info("----- vm affLabel 불러오기: " + id);
//		return vmService.getAffinitylabel(id);
//	}

	@GetMapping("/{id}/guests")
	@ApiOperation(value = "가상머신 게스트 목록", notes = "선택된 가상머신의 게스트 목록을 조회한다")
	@ApiImplicitParam(name = "id", value = "가상머신 아이디", dataTypeClass = String.class)
	@ResponseBody
	public GuestInfoVo guest(@PathVariable String id) {
		log.info("----- vm disk 일반 불러오기: " + id);
		return vmService.getGuestByVm(id);
	}

	@GetMapping("/{id}/permissions")
	@ApiOperation(value = "가상머신 권한 목록", notes = "선택된 가상머신의 권한 목록을 조회한다")
	@ApiImplicitParam(name = "id", value = "가상머신 아이디", dataTypeClass = String.class)
	@ResponseBody
	public List<PermissionVo> permission(@PathVariable String id) {
		log.info("----- vm event 일반 불러오기: " + id);
		return vmService.getPermissionsByVm(id);
	}

	@GetMapping("/{id}/events")
	@ApiOperation(value = "가상머신 이벤트 목록", notes = "선택된 가상머신의 이벤트 목록을 조회한다")
	@ApiImplicitParam(name = "id", value = "가상머신 아이디", dataTypeClass = String.class)
	@ResponseBody
	public List<EventVo> event(@PathVariable String id) {
		log.info("----- vm event 일반 불러오기: " + id);
		return vmService.getEventsByVm(id);
	}




//	@PostMapping("/{id}/console")
//	@ResponseStatus(HttpStatus.OK)
//	public com.itinfo.itcloud.model.ConsoleVo console(@PathVariable String id,
//													  @RequestBody ConsoleVo consoleVo) {
//
//		log.info("--- 가상머신 콘솔");
//		return vmService.getConsole(id, consoleVo);
//	}


	@GetMapping("/console/{id}")
	@ApiOperation(value = "가상머신 콘솔", notes = "선택된 가상머신의 콘솔을 출력한다")
	@ApiImplicitParam(name = "id", value = "가상머신 아이디", dataTypeClass = String.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ConsoleVo setConsole(@PathVariable String id) {
		return vmService.getConsole(id);
	}

	
	@GetMapping("/console/vncView")
	public String vncView() {
		return "vnc";
	}


}
