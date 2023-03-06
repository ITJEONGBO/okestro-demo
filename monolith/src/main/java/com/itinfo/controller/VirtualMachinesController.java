package com.itinfo.controller;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("virtualMachinesController")
public class VirtualMachinesController {
	@Autowired private VirtualMachinesService virtualMachinesService;
	@Autowired private SystemPropertiesService systemPropertiesService;

	@RequestMapping({"/compute/vms"})
	public String vms() {
		return "/castanets/compute/vms";
	}

	@RequestMapping({"/compute/vm"})
	public String vm() {
		return "/castanets/compute/vmDetail";
	}

	@RequestMapping({"/compute/createVmView"})
	public String createVm() {
		return "/castanets/compute/createVm";
	}

	@RequestMapping({"/compute/updateVmInfo"})
	public String updateVm() {
		return "/castanets/compute/updateVm";
	}

	@RequestMapping({"/compute/cloneVmInfo"})
	public String cloneVm() {
		return "/castanets/compute/cloneVm";
	}

	@RequestMapping(value = {"/compute/vmList"}, method = {RequestMethod.GET})
	public String retrieveVms(String status, Model model) {
		List<VmVo> vms = null;
		if (status.equals("all")) {
			vms = this.virtualMachinesService.retrieveVmsAll();
		} else {
			vms = this.virtualMachinesService.retrieveVms(status);
		}
		model.addAttribute("resultKey", vms);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/vmList/hosts"}, method = {RequestMethod.GET})
	public String retrieveVmsHosts(Model model) {
		List<HostVo> hosts = this.virtualMachinesService.retrieveVmsHosts();
		model.addAttribute("resultKey", hosts);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/vmList/clusters"}, method = {RequestMethod.GET})
	public String retrieveVmsClusters(Model model) {
		List<ClusterVo> clusters = this.virtualMachinesService.retrieveVmsClusters();
		model.addAttribute("resultKey", clusters);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/vmDetail"}, method = {RequestMethod.GET})
	public String retrieveVm(String id, Model model) {
		VmVo vm = this.virtualMachinesService.retrieveVm(id);
		model.addAttribute("resultKey", vm);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/vm/nic"}, method = {RequestMethod.GET})
	public String retrieveVmNetworkInterface(String id, Model model) {
		List<VmNicVo> vmNics = this.virtualMachinesService.retrieveVmNics(id);
		model.addAttribute("resultKey", vmNics);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/vm/createVmNic"}, method = {RequestMethod.POST})
	public String createVmNic(@RequestBody VmNicVo vmNicVo, Model model) {
		this.virtualMachinesService.createVmNic(vmNicVo);
		model.addAttribute("resultKey");
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/vm/updateVmNic"}, method = {RequestMethod.POST})
	public String updateVmNic(@RequestBody VmNicVo vmNicVo, Model model) {
		this.virtualMachinesService.updateVmNic(vmNicVo);
		model.addAttribute("resultKey");
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/vm/removeVmNic"}, method = {RequestMethod.POST})
	public String reMoveVmNic(@RequestBody VmNicVo vmNicVo, Model model) {
		this.virtualMachinesService.removeVmNic(vmNicVo);
		model.addAttribute("resultKey");
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/vm/disks"}, method = {RequestMethod.GET})
	public String retrieveVmDisks(String id, Model model) {
		List<DiskVo> list = this.virtualMachinesService.retrieveDisks(id);
		model.addAttribute("resultKey", list);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/vm/snapshots"}, method = {RequestMethod.GET})
	public String retrieveVmSnapshots(String id, Model model) {
		List<SnapshotVo> list = this.virtualMachinesService.retrieveVmSnapshots(id);
		model.addAttribute("resultKey", list);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/vm/devices"}, method = {RequestMethod.GET})
	public String retrieveVmDevices(String id, Model model) {
		List<VmDeviceVo> list = this.virtualMachinesService.retrieveVmDevices(id);
		model.addAttribute("resultKey", list);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/vm/events"}, method = {RequestMethod.GET})
	public String retrieveVmEvents(String id, Model model) {
		List<EventVo> list = this.virtualMachinesService.retrieveVmEvents(id);
		model.addAttribute("resultKey", list);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/startVm"}, method = {RequestMethod.POST})
	public String startVm(@RequestBody List<VmVo> vms, Model model) {
		this.virtualMachinesService.startVm(vms);
		model.addAttribute("resultKey");
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/stopVm"}, method = {RequestMethod.POST})
	public String stopVm(@RequestBody List<VmVo> vms, Model model) {
		this.virtualMachinesService.stopVm(vms);
		model.addAttribute("resultKey");
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/rebootVm"}, method = {RequestMethod.POST})
	public String rebootVm(@RequestBody List<VmVo> vms, Model model) {
		this.virtualMachinesService.rebootVm(vms);
		model.addAttribute("resultKey");
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/suspendVm"}, method = {RequestMethod.POST})
	public String suspendVm(@RequestBody List<VmVo> vms, Model model) {
		this.virtualMachinesService.suspendVm(vms);
		model.addAttribute("resultKey");
		return "jsonView";
	}

	@RequestMapping({"/compute/removeVm"})
	public String removeVm(@RequestBody List<VmVo> vms, Model model) {
		this.virtualMachinesService.removeVm(vms);
		model.addAttribute("resultKey");
		return "jsonView";
	}

	@RequestMapping({"/compute/vm/checkDuplicateName"})
	public String checkDuplicateName(String name, Model model) {
		boolean result = this.virtualMachinesService.checkDuplicateName(name);
		model.addAttribute("resultKey", Boolean.valueOf(result));
		return "jsonView";
	}

	@RequestMapping({"/compute/createVm/checkDuplicateDiskName"})
	public String checkDuplicateDiskName(DiskVo disk, Model model) {
		boolean result = this.virtualMachinesService.checkDuplicateDiskName(disk);
		model.addAttribute("resultKey", Boolean.valueOf(result));
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/createVm/info"}, method = {RequestMethod.GET})
	public String createVmInfo(Model model) throws Exception {
		VmCreateVo vmCreate = this.virtualMachinesService.retrieveVmCreateInfo();
		System.out.println("return createVmGeneral");
		model.addAttribute("resultKey", vmCreate);
		return "jsonView";
	}

	@RequestMapping({"/compute/createVm"})
	public String createVm(@RequestBody VmCreateVo vmCreate, Model model) {
		this.virtualMachinesService.createVm(vmCreate);
		model.addAttribute("resultKey");
		return "jsonView";
	}

	@RequestMapping({"/compute/updateVm/info"})
	public String updateVmInfo(String id, Model model) {
		System.out.println("updateVmGeneral called, " + id);
		VmCreateVo vmCreate = this.virtualMachinesService.retrieveVmUpdateInfo(id);
		model.addAttribute("resultKey", vmCreate);
		return "jsonView";
	}

	@RequestMapping({"/compute/updateVm"})
	public String updateVm(@RequestBody VmCreateVo vmUpdate, Model model) {
		this.virtualMachinesService.updateVm(vmUpdate);
		model.addAttribute("resultKey");
		return "jsonView";
	}

	@RequestMapping({"/compute/cloneVm/info"})
	public String cloneVmInfo(String vmId, String snapshotId, Model model) {
		VmCreateVo vmCreate = this.virtualMachinesService.retrieveVmCloneInfo(vmId, snapshotId);
		model.addAttribute("resultKey", vmCreate);
		return "jsonView";
	}

	@RequestMapping({"/compute/cloneVm"})
	public String cloneVm(@RequestBody VmCreateVo vmCreate, Model model) {
		this.virtualMachinesService.cloneVm(vmCreate);
		model.addAttribute("resultKey");
		return "jsonView";
	}

	@RequestMapping({"/compute/createVm/recommendHosts"})
	public String recommendHosts(@RequestBody VmCreateVo vmCreate, Model model) {
		List<String[]> recommendHosts = this.virtualMachinesService.recommendHosts(vmCreate);
		model.addAttribute("resultKey", recommendHosts);
		return "jsonView";
	}

	@RequestMapping({"/compute/createVm/disks"})
	public String retrieveDisks(Model model) {
		List<DiskVo> disks = this.virtualMachinesService.retrieveDisks();
		model.addAttribute("resultKey", disks);
		return "jsonView";
	}

	@RequestMapping({"/compute/retrieveDiskProfiles"})
	public String retrieveDiskProfiles(Model model) {
		List<DiskProfileVo> diskProfiles = this.virtualMachinesService.retrieveDiskProfiles();
		model.addAttribute("resultKey", diskProfiles);
		return "jsonView";
	}

	@RequestMapping({"/compute/vm/createSnapshot"})
	public String createSnapshot(@RequestBody SnapshotVo snapshot, Model model) {
		this.virtualMachinesService.createSnapshot(snapshot);
		model.addAttribute("resultKey");
		return "jsonView";
	}

	@RequestMapping({"/compute/vm/previewSnapshot"})
	public String previewSnapshot(@RequestBody SnapshotVo snapshot, Model model) {
		System.out.println("called previewSnapshot");
		this.virtualMachinesService.previewSnapshot(snapshot);
		model.addAttribute("resultKey");
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/vm/commitSnapshot"}, method = {RequestMethod.GET})
	public String commitSnapshot(String vmId, Model model) {
		System.out.println("called commitSnapshot");
		this.virtualMachinesService.commitSnapshot(vmId);
		model.addAttribute("resultKey");
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/vm/undoSnapshot"}, method = {RequestMethod.GET})
	public String undoSnapshot(String vmId, Model model) {
		System.out.println("called undoSnapshot");
		this.virtualMachinesService.undoSnapshot(vmId);
		model.addAttribute("resultKey");
		return "jsonView";
	}

	@RequestMapping({"/compute/vm/removeSnapshot"})
	public String removeSnapshot(@RequestBody SnapshotVo snapshot, Model model) {
		System.out.println("called removeSnapshot");
		this.virtualMachinesService.removeSnapshot(snapshot);
		model.addAttribute("resultKey");
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/discs"}, method = {RequestMethod.GET})
	public String retrieveDiscs(Model model) {
		List<StorageDomainVo> discs = this.virtualMachinesService.retrieveDiscs();
		model.addAttribute("resultKey", discs);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/changeDisc"}, method = {RequestMethod.POST})
	public String changeDisc(@RequestBody VmVo vm, Model model) {
		this.virtualMachinesService.changeDisc(vm);
		model.addAttribute("resultKey");
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/retrieveEngineIp"}, method = {RequestMethod.GET})
	public String retrieveEngineIp(Model model) {
		String engineIp = this.systemPropertiesService.retrieveSystemProperties().getIp();
		model.addAttribute("resultKey", engineIp);
		return "jsonView";
	}

	@RequestMapping({"/compute/vm/metrics"})
	public String getVmOverview() {
		return "/castanets/compute/vmMetrics";
	}

	@RequestMapping({"/compute/vm/metrics/uri"})
	public String getGrafanaUri(Model model) {
		model.addAttribute("resultKey", this.systemPropertiesService.retrieveSystemProperties().getGrafanaUri());
		return "jsonView";
	}
}