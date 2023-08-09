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

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
public class VirtualMachinesController {
	@Autowired private VirtualMachinesService virtualMachinesService;
	@Autowired private SystemPropertiesService systemPropertiesService;

	@RequestMapping({"/compute/vms"})
	public String vms() {
		log.info("... vms");
		return "/castanets/compute/vms";
	}

	@RequestMapping({"/compute/vm"})
	public String vm() {
		log.info("... vm");
		return "/castanets/compute/vmDetail";
	}

	@RequestMapping({"/compute/createVmView"})
	public String createVm() {
		log.info("... createVm");
		return "/castanets/compute/createVm";
	}

	@RequestMapping({"/compute/updateVmInfo"})
	public String updateVm() {
		log.info("... updateVm");
		return "/castanets/compute/updateVm";
	}

	@RequestMapping({"/compute/cloneVmInfo"})
	public String cloneVm() {
		log.info("... cloneVm");
		return "/castanets/compute/cloneVm";
	}

	@RequestMapping(value = {"/compute/vmList"}, method = {RequestMethod.GET})
	public String retrieveVms(String status, Model model) {
		log.info("... retrieveVms('{}')", status);
		List<VmVo> vms = (status.equals("all"))
				? virtualMachinesService.retrieveVmsAll()
				: virtualMachinesService.retrieveVms(status);
		model.addAttribute(ItInfoConstant.RESULT_KEY, vms);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/vmList/hosts"}, method = {RequestMethod.GET})
	public String retrieveVmsHosts(Model model) {
		log.info("... retrieveVmsHosts");
		List<HostVo> hosts = virtualMachinesService.retrieveVmsHosts();
		model.addAttribute(ItInfoConstant.RESULT_KEY, hosts);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/vmList/clusters"}, method = {RequestMethod.GET})
	public String retrieveVmsClusters(Model model) {
		log.info("... retrieveVmsClusters");
		List<ClusterVo> clusters = virtualMachinesService.retrieveVmsClusters();
		model.addAttribute(ItInfoConstant.RESULT_KEY, clusters);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/vmDetail"}, method = {RequestMethod.GET})
	public String retrieveVm(String id, Model model) {
		log.info("... retrieveVm('{}')", id);
		VmVo vm = virtualMachinesService.retrieveVm(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, vm);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/vm/nic"}, method = {RequestMethod.GET})
	public String retrieveVmNetworkInterface(String id, Model model) {
		log.info("... retrieveVmNetworkInterface('{}')", id);
		List<VmNicVo> vmNics = virtualMachinesService.retrieveVmNics(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, vmNics);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/vm/createVmNic"}, method = {RequestMethod.POST})
	public String createVmNic(@RequestBody VmNicVo vmNicVo, Model model) {
		log.info("... createVmNic");
		virtualMachinesService.createVmNic(vmNicVo);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/vm/updateVmNic"}, method = {RequestMethod.POST})
	public String updateVmNic(@RequestBody VmNicVo vmNicVo, Model model) {
		log.info("... updateVmNic");
		virtualMachinesService.updateVmNic(vmNicVo);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/vm/removeVmNic"}, method = {RequestMethod.POST})
	public String reMoveVmNic(@RequestBody VmNicVo vmNicVo, Model model) {
		log.info("... reMoveVmNic");
		virtualMachinesService.removeVmNic(vmNicVo);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/vm/disks"}, method = {RequestMethod.GET})
	public String retrieveVmDisks(String id, Model model) {
		log.info("... retrieveVmDisks('{}')", id);
		List<DiskVo> list = virtualMachinesService.retrieveDisks(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, list);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/vm/snapshots"}, method = {RequestMethod.GET})
	public String retrieveVmSnapshots(String id, Model model) {
		log.info("... retrieveVmSnapshots('{}')", id);
		List<SnapshotVo> list = virtualMachinesService.retrieveVmSnapshots(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, list);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/vm/devices"}, method = {RequestMethod.GET})
	public String retrieveVmDevices(String id, Model model) {
		log.info("... retrieveVmDevices('{}')", id);
		List<VmDeviceVo> list = virtualMachinesService.retrieveVmDevices(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, list);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/vm/events"}, method = {RequestMethod.GET})
	public String retrieveVmEvents(String id, Model model) {
		log.info("... retrieveVmEvents('{}')", id);
		List<EventVo> list = virtualMachinesService.retrieveVmEvents(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, list);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/startVm"}, method = {RequestMethod.POST})
	public String startVm(@RequestBody List<VmVo> vms, Model model) {
		log.info("... startVm[{}]", vms.size());
		virtualMachinesService.startVm(vms);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/stopVm"}, method = {RequestMethod.POST})
	public String stopVm(@RequestBody List<VmVo> vms, Model model) {
		log.info("... stopVm[{}]", vms.size());
		virtualMachinesService.stopVm(vms);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/rebootVm"}, method = {RequestMethod.POST})
	public String rebootVm(@RequestBody List<VmVo> vms, Model model) {
		log.info("... rebootVm[{}]", vms.size());
		virtualMachinesService.rebootVm(vms);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/suspendVm"}, method = {RequestMethod.POST})
	public String suspendVm(@RequestBody List<VmVo> vms, Model model) {
		log.info("... suspendVm[{}]", vms.size());
		virtualMachinesService.suspendVm(vms);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/removeVm"})
	public String removeVm(@RequestBody List<VmVo> vms, Model model) {
		log.info("... removeVm[{}]", vms.size());
		virtualMachinesService.removeVm(vms);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/vm/checkDuplicateName"})
	public String checkDuplicateName(String name, Model model) {
		log.info("... checkDuplicateName('{}')", name);
		boolean result = virtualMachinesService.checkDuplicateName(name);
		model.addAttribute(ItInfoConstant.RESULT_KEY, result);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/createVm/checkDuplicateDiskName"})
	public String checkDuplicateDiskName(DiskVo disk, Model model) {
		log.info("... checkDuplicateDiskName");
		boolean result = virtualMachinesService.checkDuplicateDiskName(disk);
		model.addAttribute(ItInfoConstant.RESULT_KEY, result);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/createVm/info"}, method = {RequestMethod.GET})
	public String createVmInfo(Model model) throws Exception {
		log.info("... createVmInfo");
		VmCreateVo vmCreate = virtualMachinesService.retrieveVmCreateInfo();
		model.addAttribute(ItInfoConstant.RESULT_KEY, vmCreate);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/createVm"})
	public String createVm(@RequestBody VmCreateVo vmCreate, Model model) {
		log.info("... createVm");
		virtualMachinesService.createVm(vmCreate);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/updateVm/info"})
	public String updateVmInfo(String id, Model model) {
		log.info("... updateVmInfo('{}')", id);
		VmCreateVo vmCreate = virtualMachinesService.retrieveVmUpdateInfo(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, vmCreate);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/updateVm"})
	public String updateVm(@RequestBody VmCreateVo vmUpdate, Model model) {
		log.info("... updateVm");
		virtualMachinesService.updateVm(vmUpdate);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/cloneVm/info"})
	public String cloneVmInfo(String vmId, String snapshotId, Model model) {
		log.info("... cloneVmInfo('{}')", vmId);
		VmCreateVo vmCreate = virtualMachinesService.retrieveVmCloneInfo(vmId, snapshotId);
		model.addAttribute(ItInfoConstant.RESULT_KEY, vmCreate);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/cloneVm"})
	public String cloneVm(@RequestBody VmCreateVo vmCreate, Model model) {
		log.info("... cloneVm");
		virtualMachinesService.cloneVm(vmCreate);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/createVm/recommendHosts"})
	public String recommendHosts(@RequestBody VmCreateVo vmCreate, Model model) {
		log.info("... recommendHosts");
		List<String[]> recommendHosts = virtualMachinesService.recommendHosts(vmCreate);
		model.addAttribute(ItInfoConstant.RESULT_KEY, recommendHosts);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/createVm/disks"})
	public String retrieveDisks(Model model) {
		log.info("... retrieveDisks");
		List<DiskVo> disks = virtualMachinesService.retrieveDisks();
		model.addAttribute(ItInfoConstant.RESULT_KEY, disks);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/retrieveDiskProfiles"})
	public String retrieveDiskProfiles(Model model) {
		log.info("... retrieveDiskProfiles");
		List<DiskProfileVo> diskProfiles = virtualMachinesService.retrieveDiskProfiles();
		model.addAttribute(ItInfoConstant.RESULT_KEY, diskProfiles);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/vm/createSnapshot"})
	public String createSnapshot(@RequestBody SnapshotVo snapshot, Model model) {
		log.info("... createSnapshot");
		virtualMachinesService.createSnapshot(snapshot);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/vm/previewSnapshot"})
	public String previewSnapshot(@RequestBody SnapshotVo snapshot, Model model) {
		log.info("... previewSnapshot");
		virtualMachinesService.previewSnapshot(snapshot);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/vm/commitSnapshot"}, method = {RequestMethod.GET})
	public String commitSnapshot(String vmId, Model model) {
		log.info("... commitSnapshot('{}')", vmId);
		virtualMachinesService.commitSnapshot(vmId);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/vm/undoSnapshot"}, method = {RequestMethod.GET})
	public String undoSnapshot(String vmId, Model model) {
		log.info("... undoSnapshot('{}')", vmId);
		virtualMachinesService.undoSnapshot(vmId);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/vm/removeSnapshot"})
	public String removeSnapshot(@RequestBody SnapshotVo snapshot, Model model) {
		log.info("... removeSnapshot");
		virtualMachinesService.removeSnapshot(snapshot);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/discs"}, method = {RequestMethod.GET})
	public String retrieveDiscs(Model model) {
		log.info("... retrieveDiscs");
		List<StorageDomainVo> discs = virtualMachinesService.retrieveDiscs();
		model.addAttribute(ItInfoConstant.RESULT_KEY, discs);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/changeDisc"}, method = {RequestMethod.POST})
	public String changeDisc(@RequestBody VmVo vm, Model model) {
		log.info("... changeDisc");
		virtualMachinesService.changeDisc(vm);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping(value = {"/compute/retrieveEngineIp"}, method = {RequestMethod.GET})
	public String retrieveEngineIp(Model model) {
		log.info("... retrieveEngineIp");
		String engineIp = systemPropertiesService.retrieveSystemProperties().getIp();
		model.addAttribute(ItInfoConstant.RESULT_KEY, engineIp);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/compute/vm/metrics"})
	public String getVmOverview() {
		log.info("... getVmOverview");
		return "/castanets/compute/vmMetrics";
	}

	@RequestMapping({"/compute/vm/metrics/uri"})
	public String getGrafanaUri(Model model) {
		log.info("... getGrafanaUri");
		model.addAttribute(ItInfoConstant.RESULT_KEY, systemPropertiesService.retrieveSystemProperties().getGrafanaUri());
		return ItInfoConstant.JSON_VIEW;
	}
}