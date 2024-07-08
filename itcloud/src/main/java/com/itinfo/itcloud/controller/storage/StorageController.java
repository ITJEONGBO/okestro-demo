package com.itinfo.itcloud.controller.storage;

import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.storage.*;
import com.itinfo.itcloud.service.storage.ItStorageService;
import io.swagger.annotations.*;
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
@Api(tags = "Storage")
@RequestMapping("/storages")
public class StorageController {
	private final ItStorageService storageService;

	@ApiOperation(httpMethod="GET", value="/{dcId}/disks", notes="Disk 목록")
	@GetMapping("/{dcId}/disks")
	@ApiImplicitParams(
		@ApiImplicitParam(name="dcId", value="스토리지ID", dataTypeClass=String.class, required=true, paramType="path")
	)
	@ApiResponses(
		@ApiResponse(code=200, message="OK")
	)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<DiskVo> disks(
		@PathVariable("dcId") String dcId // id=dcId
	) {
		// 데이터센터 밑에 붙어있는 디스크
		log.info("--- Disk 목록");
		return storageService.getDiskList(dcId);
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

//	@PostMapping("disks/{id}/move")
//	@ResponseBody
//	@ResponseStatus(HttpStatus.OK)
//	public CommonVo<Boolean> moveDisk(@PathVariable("id") String id,
//									  @RequestBody DiskMoveCopyVo diskMoveCopyVo){
//		log.info("--- 디스크 - 이동");
//		return storageService.moveDisk(id, diskMoveCopyVo);
//	}

	@PostMapping("disks/{id}/copy")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonVo<Boolean> copyDisk(@PathVariable String id,
									  @RequestBody DiskVo disk){
		log.info("--- 디스크 - 복사");
		return storageService.copyDisk(disk);
	}

	@PostMapping("/{dcId}/disk/upload")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonVo<Boolean> uploadDisk(
			@RequestPart MultipartFile file,
			@RequestPart ImageCreateVo image
	) throws IOException {
		return storageService.uploadDisk(file, image);
	}

	@ApiOperation(httpMethod="GET", value="/{dcId}/disks/{id}", notes="Domain(s) 목록")
	@GetMapping("/{dcId}/disks/{id}")
	@ApiImplicitParams(
			@ApiImplicitParam(name="dcId", value="스토리지ID", dataTypeClass=String.class, required=true, paramType="path")
	)
	@ApiResponses(
			@ApiResponse(code=200, message="OK")
	)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<DiskVo> disks(
		@PathVariable("dcId") String dcId,
		@PathVariable("id") String id
	) {
		// TODO: 파라미터 재정의 필요 id인지 dcID인지
		// 데이터센터 밑에 붙어있는 디스크
		log.info("--- Disk 목록");
		return storageService.getDiskList(id);
	}





	@ApiOperation(httpMethod="GET", value="/{dcId}/domains", notes="Domain(s) 목록")
	@GetMapping("/{dcId}/domains")
	@ApiImplicitParams(
			@ApiImplicitParam(name="dcId", value="스토리지ID", dataTypeClass=String.class, required=true, paramType="path")
	)
	@ApiResponses(
			@ApiResponse(code=200, message="OK")
	)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<DomainVo> domains(
		@PathVariable("dcId") String dcId
	) {
		log.info("--- Domain 목록");
		return storageService.getDomainList(dcId);
	}




//	@GetMapping("/{dcId}/storages")
//	@ResponseBody
//	public List<DomainVo> storages(@PathVariable("dcId") String dcId){
//		log.info("--- networks");
//		return storageService.getStorageList(dcId);
//	}

	@ApiOperation(httpMethod="GET", value="/{dcId}/networks", notes="Network(s) 목록")
	@GetMapping("/{dcId}/networks")
	@ApiImplicitParams(
			@ApiImplicitParam(name="dcId", value="스토리지ID", dataTypeClass=String.class, required=true, paramType="path")
	)
	@ApiResponses(
			@ApiResponse(code=200, message="OK")
	)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<NetworkVo> networks(
		@PathVariable("dcId") String dcId
	) {
		log.info("--- networks");
		return storageService.getNetworkVoList(dcId);
	}

	@ApiOperation(httpMethod="GET", value="/{dcId}/clusters", notes="Cluster(s) 목록")
	@GetMapping("/{dcId}/clusters")
	@ApiImplicitParams(
			@ApiImplicitParam(name="dcId", value="스토리지ID", dataTypeClass=String.class, required=true, paramType="path")
	)
	@ApiResponses(
			@ApiResponse(code=200, message="OK")
	)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ClusterVo> clusters(
		@PathVariable("dcId") String dcId
	) {
		log.info("--- clusters");
		return storageService.getClusterVoList(dcId);
	}


	@ApiOperation(httpMethod="GET", value="/{dcId}/permissions", notes="Permission(s) 목록")
	@GetMapping("/{dcId}/permissions")
	@ApiImplicitParams(
			@ApiImplicitParam(name="dcId", value="스토리지ID", dataTypeClass=String.class, required=true, paramType="path")
	)
	@ApiResponses(
			@ApiResponse(code=200, message="OK")
	)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<PermissionVo> permissions(
		@PathVariable("dcId") String dcId
	) {
		log.info("--- permissions");
		return storageService.getPermission(dcId);
	}

	@ApiOperation(httpMethod="GET", value="/{dcId}/events", notes="Event(s) 목록")
	@GetMapping("/{dcId}/events")
	@ApiImplicitParams(
			@ApiImplicitParam(name="dcId", value="스토리지ID", dataTypeClass=String.class, required=true, paramType="path")
	)
	@ApiResponses(
			@ApiResponse(code=200, message="OK")
	)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<EventVo> events(
		@PathVariable("dcId") String dcId
	) {
		log.info("--- events");
		return storageService.getEvent(dcId);
	}
}
