package com.itinfo.itcloud.controller.storage;

import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.storage.*;
import com.itinfo.itcloud.service.ItStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/storage")
public class StorageController {
	private final ItStorageService storageService;

	@GetMapping("/disks")
	@ResponseBody
	public List<DiskVo> disks(String id){
		return storageService.getDiskVoList(id);
	}


	@GetMapping("/diskShow")
	@ResponseBody
	public DiskDcVo setAddDisk(String id){
		return storageService.setAddDisk(id);
	}

	@PostMapping("/disk-image/add")
	public CommonVo<Boolean> addDiskImage(@RequestBody ImageCreateVo image){
		log.info("새가상 디스크 - 이미지 생성");
		return storageService.addDiskImage(image);
	}


	@GetMapping("/domains")
	@ResponseBody
	public List<DomainVo> domains(){
		return storageService.getDomainList();
	}

	@GetMapping("/volumes")
	@ResponseBody
	public List<VolumeVo> volumes(String id){
		return storageService.getVolumeVoList(id);
	}

	@GetMapping("/storages")
	@ResponseBody
	public List<DomainVo> storages(String id){
		return storageService.getStorageList(id);
	}

	@GetMapping("/networks")
	@ResponseBody
	public List<NetworkVo> networks(String id){
		return storageService.getNetworkVoList(id);
	}

	@GetMapping("/clusters")
	@ResponseBody
	public List<ClusterVo> clusters(String id){
		return storageService.getClusterVoList(id);
	}

	@GetMapping("/events")
	@ResponseBody
	public List<EventVo> events(String id){
		return storageService.getEvent(id);
	}

	@GetMapping("/permissions")
	@ResponseBody
	public List<PermissionVo> permissions(String id){
		return storageService.getPermission(id);
	}


}
