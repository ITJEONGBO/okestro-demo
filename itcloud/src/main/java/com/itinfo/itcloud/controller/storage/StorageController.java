package com.itinfo.itcloud.controller.storage;

import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.storage.DiskVo;
import com.itinfo.itcloud.model.storage.DomainVo;
import com.itinfo.itcloud.model.storage.VolumeVo;
import com.itinfo.itcloud.service.ItStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/storage")
public class StorageController {
	private final ItStorageService itStorageService;

	@GetMapping("/disks")
	@ResponseBody
	public List<DiskVo> disks(String id){
		return itStorageService.getDiskVoList(id);
	}

	@GetMapping("/domains")
	@ResponseBody
	public List<DomainVo> domains(){
		return itStorageService.getDomainList();
	}

	@GetMapping("/volumes")
	@ResponseBody
	public List<VolumeVo> volumes(String id){
		return itStorageService.getVolumeVoList(id);
	}

	@GetMapping("/storages")
	@ResponseBody
	public List<DomainVo> storages(String id){
		return itStorageService.getStorageList(id);
	}

	@GetMapping("/networks")
	@ResponseBody
	public List<NetworkVo> networks(String id){
		return itStorageService.getNetworkVoList(id);
	}

	@GetMapping("/clusters")
	@ResponseBody
	public List<ClusterVo> clusters(String id){
		return itStorageService.getClusterVoList(id);
	}

	@GetMapping("/events")
	@ResponseBody
	public List<EventVo> events(String id){
		return itStorageService.getEvent(id);
	}

	@GetMapping("/permissions")
	@ResponseBody
	public List<PermissionVo> permissions(String id){
		return itStorageService.getPermission(id);
	}


}
