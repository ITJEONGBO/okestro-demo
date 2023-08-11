package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.SystemPropertiesService;
import com.itinfo.service.VirtualMachinesService;
import com.itinfo.model.ClusterVo;
import com.itinfo.model.DiskProfileVo;
import com.itinfo.model.DiskVo;
import com.itinfo.model.EventVo;
import com.itinfo.model.HostVo;
import com.itinfo.model.SnapshotVo;
import com.itinfo.model.StorageDomainVo;
import com.itinfo.model.VmCreateVo;
import com.itinfo.model.VmDeviceVo;
import com.itinfo.model.VmNicVo;
import com.itinfo.model.VmVo;

import java.util.List;

import io.swagger.annotations.*;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
@Api(value = "VirtualMachinesController", tags = {"virtual-machines"})
public class VirtualMachinesController {
	@Autowired private VirtualMachinesService virtualMachinesService;
	@Autowired private SystemPropertiesService systemPropertiesService;

	@ApiOperation(httpMethod = "GET", value = "vmsView", notes = "페이지 이동 > /compute/vms")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/vms"})
	public String vmsView() {
		log.info("... vmsView");
		return "/castanets/compute/vms";
	}

	@ApiOperation(httpMethod = "GET", value = "vmView", notes = "페이지 이동 > /compute/vm")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/vm"})
	public String vmView() {
		log.info("... vmView");
		return "/castanets/compute/vmDetail";
	}

	@ApiOperation(httpMethod = "GET", value = "createVmView", notes = "페이지 이동 > /compute/createVmView")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/createVmView"})
	public String createVmView() {
		log.info("... createVmView");
		return "/castanets/compute/createVm";
	}

	@ApiOperation(httpMethod = "GET", value = "updateVmView", notes = "페이지 이동 > /compute/updateVmInfo")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/updateVmInfo"})
	public String updateVmView() {
		log.info("... updateVmView");
		return "/castanets/compute/updateVm";
	}

	@ApiOperation(httpMethod = "GET", value = "cloneVmView", notes = "페이지 이동 > /compute/cloneVmInfo")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/cloneVmInfo"})
	public String cloneVmView() {
		log.info("... cloneVmView");
		return "/castanets/compute/cloneVm";
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveVms", notes = "가상머신 목록 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name="status", value = "가상머신 상태")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/vmList"})
	public String retrieveVms(String status,
							  Model model) {
		log.info("... retrieveVms('{}')", status);
		List<VmVo> vms = (status.equals("all"))
				? virtualMachinesService.retrieveVmsAll()
				: virtualMachinesService.retrieveVms(status);
		model.addAttribute(ItInfoConstant.RESULT_KEY, vms);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveVmsHosts", notes = "가상머신의 호스트 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/vmList/hosts"})
	public String retrieveVmsHosts(Model model) {
		log.info("... retrieveVmsHosts");
		List<HostVo> hosts = virtualMachinesService.retrieveVmsHosts();
		model.addAttribute(ItInfoConstant.RESULT_KEY, hosts);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveVmsClusters", notes = "가상머신의 클러스터 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/vmList/clusters"})
	public String retrieveVmsClusters(Model model) {
		log.info("... retrieveVmsClusters");
		List<ClusterVo> clusters
				= virtualMachinesService.retrieveVmsClusters();
		model.addAttribute(ItInfoConstant.RESULT_KEY, clusters);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveVm", notes = "가상머신 상세 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name="id", value = "가상머신 ID", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/vmDetail"})
	public String retrieveVm(String id,
							 Model model) {
		log.info("... retrieveVm('{}')", id);
		VmVo vm = virtualMachinesService.retrieveVm(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, vm);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveVmNetworkInterface", notes = "가상머신 네트워크 인터페이스 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name="id", value = "가상머신 ID", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/vm/nic"})
	public String retrieveVmNetworkInterface(String id,
											 Model model) {
		log.info("... retrieveVmNetworkInterface('{}')", id);
		List<VmNicVo> vmNics = virtualMachinesService.retrieveVmNics(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, vmNics);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "createVmNic", notes = "가상머신 네트워크 생성")
	@ApiImplicitParams({
			@ApiImplicitParam(name="vmNicVo", value = "생성할 VM 네트워크 정보", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/vm/createVmNic"})
	public String createVmNic(@RequestBody VmNicVo vmNicVo,
							  Model model) {
		log.info("... createVmNic");
		virtualMachinesService.createVmNic(vmNicVo);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "updateVmNic", notes = "가상머신 네트워크 수정")
	@ApiImplicitParams({
			@ApiImplicitParam(name="vmNicVo", value = "수정할 VM 네트워크 정보", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/vm/updateVmNic"})
	public String updateVmNic(@RequestBody VmNicVo vmNicVo,
							  Model model) {
		log.info("... updateVmNic");
		virtualMachinesService.updateVmNic(vmNicVo);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "reMoveVmNic", notes = "가상머신 네트워크 삭제")
	@ApiImplicitParams({
			@ApiImplicitParam(name="vmNicVo", value = "삭제할 VM 네트워크 정보", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/vm/removeVmNic"})
	public String reMoveVmNic(@RequestBody VmNicVo vmNicVo,
							  Model model) {
		log.info("... reMoveVmNic");
		virtualMachinesService.removeVmNic(vmNicVo);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveVmDisks", notes = "가상머신 디스크 목록 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name="id", value = "가상머신 ID", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/vm/disks"})
	public String retrieveVmDisks(String id,
								  Model model) {
		log.info("... retrieveVmDisks('{}')", id);
		List<DiskVo> list
				= virtualMachinesService.retrieveDisks(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, list);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "retrieveVmSnapshots",
			notes = "가상머신 스냅샷 목록 조회",
			httpMethod = "GET"
	)
	@ApiImplicitParam(name="id", value = "가상머신 ID", required = true)
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/vm/snapshots"})
	public String retrieveVmSnapshots(String id,
									  Model model) {
		log.info("... retrieveVmSnapshots('{}')", id);
		List<SnapshotVo> list = virtualMachinesService.retrieveVmSnapshots(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, list);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "retrieveVmDevices",
			notes = "가상머신 장치정보 목록 조회",
			httpMethod = "GET"
	)
	@ApiImplicitParam(name="id", value = "가상머신 ID", required = true)
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.GET},
			value = {"/compute/vm/devices"}
	)
	public String retrieveVmDevices(String id,
									Model model) {
		log.info("... retrieveVmDevices('{}')", id);
		List<VmDeviceVo> list = virtualMachinesService.retrieveVmDevices(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, list);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "retrieveVmEvents",
			notes = "가상머신 이벤트 목록 조회",
			httpMethod = "GET"
	)
	@ApiImplicitParam(name="id", value = "가상머신 ID")
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.GET},
			value = {"/compute/vm/events"}
	)
	public String retrieveVmEvents(String id,
								   Model model) {
		log.info("... retrieveVmEvents('{}')", id);
		List<EventVo> list = virtualMachinesService.retrieveVmEvents(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, list);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "startVm",
			notes = "가상머신 기동",
			httpMethod = "POST"
	)
	@ApiImplicitParam(name="vms", value = "기동 할 가상머신", required = true)
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.POST},
			value = {"/compute/startVm"}
	)
	public String startVm(@RequestBody List<VmVo> vms,
						  Model model) {
		log.info("... startVm[{}]", vms.size());
		virtualMachinesService.startVm(vms);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "stopVm",
			notes = "가상머신 정지",
			httpMethod = "POST"
	)
	@ApiImplicitParam(name="vms", value = "정지할 가상머신", required = true)
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.POST},
			value = {"/compute/stopVm"}
	)
	public String stopVm(@RequestBody List<VmVo> vms,
						 Model model) {
		log.info("... stopVm[{}]", vms.size());
		virtualMachinesService.stopVm(vms);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "rebootVm",
			notes = "가상머신 재기동",
			httpMethod = "POST"
	)
	@ApiImplicitParam(name="vms", value = "재기동 할 가상머신", required = true)
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.POST},
			value = {"/compute/rebootVm"}
	)
	public String rebootVm(@RequestBody List<VmVo> vms,
						   Model model) {
		log.info("... rebootVm[{}]", vms.size());
		virtualMachinesService.rebootVm(vms);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "suspendVm",
			notes = "VM 일시정지",
			httpMethod = "POST"
	)
	@ApiImplicitParam(name="vms", value = "일시정지 할 가상머신", required = true)
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.POST},
			value = {"/compute/suspendVm"}
	)
	public String suspendVm(@RequestBody List<VmVo> vms,
							Model model) {
		log.info("... suspendVm[{}]", vms.size());
		virtualMachinesService.suspendVm(vms);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "removeVm",
			notes = "가상머신 삭제",
			httpMethod = "POST"
	)
	@ApiImplicitParam(name="vms", value = "삭제 할 VM 목록", required = true)
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.POST},
			value = {"/compute/removeVm"}
	)
	public String removeVm(@RequestBody List<VmVo> vms,
						   Model model) {
		log.info("... removeVm[{}]", vms.size());
		virtualMachinesService.removeVm(vms);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "checkDuplicateName",
			notes = "가상머신 이름 중복 여부 확인",
			httpMethod = "GET"
	)
	@ApiImplicitParam(name="name", value = "가상머신 이름")
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.GET},
			value = {"/compute/vm/checkDuplicateName"}
	)
	public String checkDuplicateName(String name,
									 Model model) {
		log.info("... checkDuplicateName('{}')", name);
		boolean result = virtualMachinesService.checkDuplicateName(name);
		model.addAttribute(ItInfoConstant.RESULT_KEY, result);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "checkDuplicateDiskName",
			notes = "가상머신 디스크 이름 중복 여부 확인",
			httpMethod = "GET"
	)
	@ApiImplicitParam(name="name", value = "가상머신 디스크 이름")
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.GET},
			value = {"/compute/createVm/checkDuplicateDiskName"}
	)
	public String checkDuplicateDiskName(DiskVo disk,
										 Model model) {
		log.info("... checkDuplicateDiskName");
		boolean result = virtualMachinesService.checkDuplicateDiskName(disk);
		model.addAttribute(ItInfoConstant.RESULT_KEY, result);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "createVmInfo",
			notes = "가상머신 생성 정보 조회",
			httpMethod = "GET"
	)
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.GET},
			value = {"/compute/createVm/info"}
	)
	public String createVmInfo(Model model) {
		log.info("... createVmInfo");
		VmCreateVo vmCreate = virtualMachinesService.retrieveVmCreateInfo();
		model.addAttribute(ItInfoConstant.RESULT_KEY, vmCreate);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "createVm",
			notes = "가상머신 생성",
			httpMethod = "POST"
	)
	@ApiImplicitParam(name="vmCreate", value = "생성 할 VM 정보")
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.POST},
			value = {"/compute/createVm"}
	)
	public String createVm(@RequestBody VmCreateVo vmCreate,
						   Model model) {
		log.info("... createVm");
		virtualMachinesService.createVm(vmCreate);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "updateVmInfo",
			notes = "가상머신 수정 정보 조회",
			httpMethod = "GET"
	)
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.GET},
			value = {"/compute/updateVm/info"}
	)
	public String updateVmInfo(String id,
							   Model model) {
		log.info("... updateVmInfo('{}')", id);
		VmCreateVo vmCreate = virtualMachinesService.retrieveVmUpdateInfo(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, vmCreate);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "updateVm",
			notes = "가상머신 수정",
			httpMethod = "POST"
	)
	@ApiImplicitParam(name="vmUpdate", value = "수정 할 VM 정보")
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.POST},
			value = {"/compute/updateVm"}
	)
	public String updateVm(@RequestBody VmCreateVo vmUpdate,
						   Model model) {
		log.info("... updateVm");
		virtualMachinesService.updateVm(vmUpdate);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "cloneVmInfo",
			notes = "복제할 VM 정보 조회",
			httpMethod = "GET"
	)
	@ApiImplicitParams({
			@ApiImplicitParam(name="vmId", value = "가상머신 ID"),
			@ApiImplicitParam(name="snapshotId", value = "가상머신 스냅샷 아이디")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.GET},
			value = {"/compute/cloneVm/info"}
	)
	public String cloneVmInfo(String vmId,
							  String snapshotId,
							  Model model) {
		log.info("... cloneVmInfo('{}')", vmId);
		VmCreateVo vmCreate = virtualMachinesService.retrieveVmCloneInfo(vmId, snapshotId);
		model.addAttribute(ItInfoConstant.RESULT_KEY, vmCreate);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "cloneVm", notes = "가상머신 복제")
	@ApiImplicitParams({
			@ApiImplicitParam(name="vmCreate", value = "복제 할 VM"),
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/compute/cloneVm"})
	public String cloneVm(@RequestBody VmCreateVo vmCreate,
						  Model model) {
		log.info("... cloneVm");
		virtualMachinesService.cloneVm(vmCreate);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "recommendHosts",
			notes = "추천 할 호스트?",
			httpMethod = "POST"
	)
	@ApiImplicitParams({
			@ApiImplicitParam(name="vmCreate", value = "복제 할 VM"),
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.POST},
			value = {"/compute/createVm/recommendHosts"}
	)
	public String recommendHosts(@RequestBody VmCreateVo vmCreate,
								 Model model) {
		log.info("... recommendHosts");
		List<String[]> recommendHosts = virtualMachinesService.recommendHosts(vmCreate);
		model.addAttribute(ItInfoConstant.RESULT_KEY, recommendHosts);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "retrieveDisks",
			notes = "디스크 목록 조회",
			httpMethod = "GET"
	)
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.GET},
			value = {"/compute/createVm/disks"}
	)
	public String retrieveDisks(Model model) {
		log.info("... retrieveDisks");
		List<DiskVo> disks = virtualMachinesService.retrieveDisks();
		model.addAttribute(ItInfoConstant.RESULT_KEY, disks);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "retrieveDiskProfiles",
			notes = "디스크 프로필 목록 조회",
			httpMethod = "GET"
	)
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.GET},
			value = {"/compute/retrieveDiskProfiles"}
	)
	public String retrieveDiskProfiles(Model model) {
		log.info("... retrieveDiskProfiles");
		List<DiskProfileVo> diskProfiles = virtualMachinesService.retrieveDiskProfiles();
		model.addAttribute(ItInfoConstant.RESULT_KEY, diskProfiles);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "createSnapshot",
			notes = "스냅샷 생성",
			httpMethod = "GET"
	)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "snapshot", value = "생성할 스냅샷 정보")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.POST},
			value = {"/compute/vm/createSnapshot"}
	)
	public String createSnapshot(@RequestBody SnapshotVo snapshot,
								 Model model) {
		log.info("... createSnapshot");
		virtualMachinesService.createSnapshot(snapshot);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "previewSnapshot",
			notes = "스냅샷 미리보기",
			httpMethod = "POST"
	)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "snapshot", value = "미리보기할 스냅샷 정보")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.POST},
			value = {"/compute/vm/previewSnapshot"}
	)
	public String previewSnapshot(@RequestBody SnapshotVo snapshot,
								  Model model) {
		log.info("... previewSnapshot");
		virtualMachinesService.previewSnapshot(snapshot);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "commitSnapshot",
			notes = "스냅샷 커밋",
			httpMethod = "GET"
	)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "vmId", value = "가상머신 ID")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.GET},
			value = {"/compute/vm/commitSnapshot"}
	)
	public String commitSnapshot(String vmId,
								 Model model) {
		log.info("... commitSnapshot('{}')", vmId);
		virtualMachinesService.commitSnapshot(vmId);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "undoSnapshot",
			notes = "스냅샷 복구",
			httpMethod = "GET"
	)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "vmId", value = "가상머신 ID")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.GET},
			value = {"/compute/vm/undoSnapshot"}
	)
	public String undoSnapshot(String vmId,
							   Model model) {
		log.info("... undoSnapshot('{}')", vmId);
		virtualMachinesService.undoSnapshot(vmId);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "removeSnapshot",
			notes = "스냅샷 제거",
			httpMethod = "POST"
	)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "snapshot", value = "스냅샷 정보")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.POST},
			value = {"/compute/vm/removeSnapshot"}
	)
	public String removeSnapshot(@RequestBody SnapshotVo snapshot,
								 Model model) {
		log.info("... removeSnapshot");
		virtualMachinesService.removeSnapshot(snapshot);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "retrieveDiscs",
			notes = "디스크 목록 조회",
			httpMethod = "GET"
	)
	@ApiImplicitParams({

	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.GET},
			value = {"/compute/discs"}
	)
	public String retrieveDiscs(Model model) {
		log.info("... retrieveDiscs");
		List<StorageDomainVo> discs = virtualMachinesService.retrieveDiscs();
		model.addAttribute(ItInfoConstant.RESULT_KEY, discs);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "changeDisc",
			notes = "디스크 교체",
			httpMethod = "POST"
	)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "vm", value = "가상머신 정보")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.POST},
			value = {"/compute/changeDisc"}
	)
	public String changeDisc(@RequestBody VmVo vm,
							 Model model) {
		log.info("... changeDisc");
		virtualMachinesService.changeDisc(vm);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "retrieveEngineIp",
			notes = "엔진 IP 조회",
			httpMethod = "GET"
	)
	@ApiImplicitParams({

	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.GET},
			value = {"/compute/retrieveEngineIp"}
	)
	public String retrieveEngineIp(Model model) {
		log.info("... retrieveEngineIp");
		String engineIp = systemPropertiesService.retrieveSystemProperties().getIp();
		model.addAttribute(ItInfoConstant.RESULT_KEY, engineIp);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(
			value = "getVmOverview",
			notes = "페이지 이동 > /compute/vm/metrics",
			httpMethod = "GET"
	)
	@ApiImplicitParams({

	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.GET},
			value = {"/compute/vm/metrics"}
	)
	public String getVmOverview() {
		log.info("... getVmOverview");
		return "/castanets/compute/vmMetrics";
	}

	@ApiOperation(
			value = "getGrafanaUri",
			notes = "???", // TODO: 그라파나와 관련있음.
			httpMethod = "POST"
	)
	@ApiImplicitParams({

	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(
			method = {RequestMethod.POST},
			value = {"/compute/vm/metrics/uri"}
	)
	public String getGrafanaUri(Model model) {
		log.info("... getGrafanaUri");
		model.addAttribute(ItInfoConstant.RESULT_KEY, systemPropertiesService.retrieveSystemProperties().getGrafanaUri());
		return ItInfoConstant.JSON_VIEW;
	}
}