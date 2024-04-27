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
import org.springframework.http.HttpStatus;
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

	@GetMapping("/{dcId}/disks")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<DiskVo> disks(@PathVariable("dcId") String dcId){	// id=dcId
		// 데이터센터 밑에 붙어있는 디스크
		log.info("--- Disk 목록");
		return storageService.getDiskList(dcId);
	}

	@GetMapping("/{dcId}/disks/settings")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public DiskDcVo setAddDisk(@PathVariable("dcId") String dcId){
		log.info("--- 디스크 이미지 생성창");
		return storageService.setDiskImage(dcId);
	}

	@PostMapping("/{dcId}/disks/image")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public CommonVo<Boolean> addDiskImage(@PathVariable("dcId") String dcId,
										  @RequestBody ImageCreateVo image){
		log.info("--- 새가상 디스크 - 이미지 생성");
		return storageService.addDiskImage(image);
	}

	@PutMapping("/{dcId}/disks/image/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public CommonVo<Boolean> editDiskImage(@PathVariable("dcId") String dcId,
										   @PathVariable("id") String id,
										   @RequestBody ImageCreateVo image){
		log.info("--- 새가상 디스크 - 이미지 수정");
		return storageService.editDiskImage(image);
	}

	@DeleteMapping("/{dcId}/disks/image/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonVo<Boolean> deleteDiskImage(@PathVariable("dcId") String dcId,
											 @PathVariable("id") String id){
		log.info("--- 새가상 디스크 - 이미지 삭제");
		return storageService.deleteDisk(id);
	}


	@GetMapping("/{dcId}/disks/{id}/move")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public DiskVo setDiskMove(@PathVariable("dcId") String dcId,
							  @PathVariable("id") String id){
		log.info("--- 디스크 - 이동창");
		return storageService.setDiskMove(dcId, id);
	}

	@PostMapping("disks/{id}/move")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonVo<Boolean> moveDisk(@PathVariable("id") String id,
									  @RequestBody DiskMoveCopyVo diskMoveCopyVo){
		log.info("--- 디스크 - 이동");
		return storageService.moveDisk(id, diskMoveCopyVo);
	}

	@GetMapping("/{dcId}/disks/{id}/copy")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public DiskVo setDiskCopy(@PathVariable("dcId") String dcId,
							  @PathVariable("id") String id){
		log.info("--- 디스크 - 복사창");
		return storageService.setDiskCopy(dcId, id);
	}


	@PostMapping("disks/{id}/copy")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonVo<Boolean> copyDisk(@PathVariable String id,
									  @RequestBody DiskMoveCopyVo disk){
		log.info("--- 디스크 - 복사");
		return storageService.copyDisk(id, disk);
	}

	@PostMapping("/{dcId}/disk/upload")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonVo<Boolean> uploadDisk(@RequestPart MultipartFile file,
										@RequestPart ImageCreateVo image) throws IOException {
		return storageService.uploadDisk(file, image);
	}

	@GetMapping("/{dcId}/disks/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<DiskVo> disks(@PathVariable("dcId") String dcId,
							  @PathVariable("id") String id){
		// 데이터센터 밑에 붙어있는 디스크
		log.info("--- Disk 목록");
		return storageService.getDiskList(id);
	}





	// 데이터센터 {id}/domains
	@GetMapping("/{dcId}/domains")
	@ResponseBody
	public List<DomainVo> domains(@PathVariable("dcId") String dcId){
		log.info("--- Domain 목록");
		return storageService.getDomainList(dcId);
	}




	// 데이터센터 {id}/volumes
	@GetMapping("/{dcId}/volumes")
	@ResponseBody
	public List<VolumeVo> volumes(@PathVariable("dcId") String dcId){
		log.info("--- Domain 목록");
		return storageService.getVolumeVoList(dcId);
	}

	@GetMapping("/{dcId}/storages")
	@ResponseBody
	public List<DomainVo> storages(@PathVariable("dcId") String dcId){
		log.info("--- networks");
		return storageService.getStorageList(dcId);
	}

	@GetMapping("/{dcId}/networks")
	@ResponseBody
	public List<NetworkVo> networks(@PathVariable("dcId") String dcId){
		log.info("--- networks");
		return storageService.getNetworkVoList(dcId);
	}

	@GetMapping("/{dcId}/clusters")
	@ResponseBody
	public List<ClusterVo> clusters(@PathVariable("dcId") String dcId){
		log.info("--- clusters");
		return storageService.getClusterVoList(dcId);
	}


	@GetMapping("/{dcId}/permissions")
	@ResponseBody
	public List<PermissionVo> permissions(@PathVariable("dcId") String dcId){
		log.info("--- permissions");
		return storageService.getPermission(dcId);
	}

	@GetMapping("/{dcId}/events")
	@ResponseBody
	public List<EventVo> events(@PathVariable("dcId") String dcId){
		log.info("--- events");
		return storageService.getEvent(dcId);
	}



}
