package com.itinfo.itcloud.controller;

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
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/storages")
public class StorageController {
	private final ItStorageService storageService;


	@GetMapping("/{id}/disks")
	@ResponseBody
	public List<DiskVo> disks(@PathVariable String id){	// id=dcId
		// 데이터센터 밑에 붙어있는 디스크
		log.info("--- Disk 목록");
		return storageService.getDiskList(id);
	}

	@GetMapping("/{id}/disks/settings")
	@ResponseBody
	public DiskDcVo setAddDisk(@PathVariable String id){
		log.info("--- 디스크 이미지 생성창");
		return storageService.setDiskImage(id);
	}

	@PostMapping("/{id}/disks/image")
	@ResponseBody
	public CommonVo<Boolean> addDiskImage(@PathVariable String id,
										  @RequestBody ImageCreateVo image){
		log.info("--- 새가상 디스크 - 이미지 생성");
		return storageService.addDiskImage(image);
	}

	@PutMapping("/{id}/disks/image")
	@ResponseBody
	public CommonVo<Boolean> editDiskImage(@PathVariable String id,
										   @RequestBody ImageCreateVo image){
		log.info("--- 새가상 디스크 - 이미지 수정");
		return storageService.editDiskImage(image);
	}

	@DeleteMapping("/{id}/disks/image")
	@ResponseBody
	public CommonVo<Boolean> deleteDiskImage(@PathVariable String id){
		log.info("--- 새가상 디스크 - 이미지 삭제");
		return storageService.deleteDisk(id);
	}

	@PostMapping("/{id}/disks/move")
	@ResponseBody
	public CommonVo<Boolean> moveDisk(@PathVariable String id,
									  @RequestBody DiskVo disk){
		log.info("--- 디스크 - 이동");
		return storageService.moveDisk(disk);
	}


	@PostMapping("/{id}/disk/copy")
	@ResponseBody
	public CommonVo<Boolean> copyDisk(@PathVariable String id,
									  @RequestBody DiskVo disk){
		log.info("--- 디스크 - 복사");
		return storageService.copyDisk(disk);
	}

	@PostMapping("/{id}/disk/upload")
	@ResponseBody
	public CommonVo<Boolean> uploadDisk(@RequestPart MultipartFile file,
										@RequestPart ImageCreateVo image) throws IOException {
		return storageService.uploadDisk(file, image);
	}







	// 데이터센터 {id}/domains
	@GetMapping("/{id}/domains")
	@ResponseBody
	public List<DomainVo> domains(@PathVariable String id){
		return storageService.getDomainList(id);
	}


	// 데이터센터 {id}/volumes
	@GetMapping("/{id}/volumes")
	@ResponseBody
	public List<VolumeVo> volumes(@PathVariable String id){
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
