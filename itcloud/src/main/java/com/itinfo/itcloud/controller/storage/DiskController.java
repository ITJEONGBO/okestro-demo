package com.itinfo.itcloud.controller.storage;

import com.itinfo.itcloud.model.MenuVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.computing.VmVo;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.model.storage.DomainVo;
import com.itinfo.itcloud.service.ItDiskService;

import com.itinfo.itcloud.service.ItMenuService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@Slf4j
@RequiredArgsConstructor
public class DiskController {
	private final ItDiskService itDiskService;
	private final ItMenuService menu;

	@GetMapping("/storage/disks")
	public String disks(Model model){
		List<DiskVo> disks = itDiskService.getList();
		model.addAttribute("disks", disks);

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/disks";
	}
	@GetMapping("/storage/disk")
	public String disk(String id, Model model){
		DiskVo disk = itDiskService.getInfo(id);
		model.addAttribute("disk", disk);
		model.addAttribute("id", id);
		model.addAttribute("name", itDiskService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/disk";
	}

	@GetMapping("/storage/disk-vm")
	public String vm(String id, Model model){
		List<VmVo> vm = itDiskService.getVm(id);
		model.addAttribute("vm", vm);
		model.addAttribute("id", id);
		model.addAttribute("name", itDiskService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/disk-vm";
	}

	@GetMapping("/storage/disk-storage")
	public String storage(String id, Model model){
		List<DomainVo> storage = itDiskService.getStorage(id);
		model.addAttribute("storage", storage);
		model.addAttribute("id", id);
		model.addAttribute("name", itDiskService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/disk-storage";
	}

	@GetMapping("/storage/disk-permission")
	public String permission(String id, Model model){
		List<PermissionVo> permission = itDiskService.getPermission(id);
		model.addAttribute("permission", permission);
		model.addAttribute("id", id);
		model.addAttribute("name", itDiskService.getName(id));

		MenuVo m = menu.getMenu();
		model.addAttribute("m", m);
		return "storage/disk-permission";
	}


	//region: ResponseBody
	@GetMapping("/disksStatus")
	@ResponseBody
	public List<DiskVo> disks(){
		return itDiskService.getList();
	}

	@GetMapping("/storage/diskStatus")
	@ResponseBody
	public DiskVo disk(String id){
		return itDiskService.getInfo(id);
	}

	@GetMapping("/storage/disk/vmStatus")
	@ResponseBody
	public List<VmVo> vm(String id){
		return itDiskService.getVm(id);
	}

	@GetMapping("/storage/disk/storageStatus")
	@ResponseBody
	public List<DomainVo> storage(String id){
		return itDiskService.getStorage(id);
	}

	@GetMapping("/storage/permissionStatus")
	@ResponseBody
	public List<PermissionVo> permission(String id){
		return itDiskService.getPermission(id);
	}

	//endregion


}