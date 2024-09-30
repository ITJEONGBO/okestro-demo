package com.itinfo.itcloud.controller.storage

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.controller.BaseController
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.itcloud.error.IdNotFoundException
import com.itinfo.itcloud.error.InvalidRequestException
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.computing.EventVo
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.storage.DiskImageVo
import com.itinfo.itcloud.model.storage.StorageDomainVo
import com.itinfo.itcloud.service.storage.ItDiskService
import com.itinfo.itcloud.service.storage.ItStorageService
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@Controller
@Api(tags = ["Storage"])
@RequestMapping("/api/v1/storages")
class StorageController: BaseController() {
	@Autowired private lateinit var iDomain: ItStorageService

	@ApiOperation(
		httpMethod="GET",
		value="스토리지 도메인 목록",
		notes="전체 스토리지 도메인 목록을 보여준다"
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/domains")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	fun domains(
	): ResponseEntity<List<StorageDomainVo>> {
		log.info("/storages/domains ... 스토리지 도메인 목록")
		return ResponseEntity.ok(iDomain.findAll())
	}

	@ApiOperation(
		httpMethod="GET",
		value="/{dataCenterId}/domains",
		notes="Domain(s) 목록"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="dataCenterId", value="데이터센터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{dataCenterId}/domains")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	fun findAllStorageDomainsFromDataCenter(
		@PathVariable dataCenterId: String? = null,
	): ResponseEntity<List<StorageDomainVo>> {
		if (dataCenterId.isNullOrEmpty())
			throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()
		log.info("/storages/{}/domains ... Domain(s) 목록", dataCenterId)
		return ResponseEntity.ok(iDomain.findAllFromDataCenter(dataCenterId))
	}

	// 생성

	@ApiOperation(
		httpMethod="GET",
		value="/{storageDomainId}/permissions",
		notes="Permission(s) 목록"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="storageDomainId", value="스토리지 도메인 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{storageDomainId}/permissions")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	fun permissions(
		@PathVariable storageDomainId: String? = null,
	): ResponseEntity<List<PermissionVo>> {
		if (storageDomainId.isNullOrEmpty())
			throw ErrorPattern.STORAGE_DOMAIN_ID_NOT_FOUND.toException()
		log.info("/storages/{}/permissions ... Permission(s) 목록", storageDomainId)
		return ResponseEntity.ok(iDomain.findAllPermissionsFromStorageDomain(storageDomainId))
	}

	@ApiOperation(
		httpMethod="GET",
		value="/{storageDomainId}/events",
		notes = "Event(s) 목록"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="storageDomainId", value="스토리지 도메인 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{storageDomainId}/events")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	fun events(
		@PathVariable("storageDomainId") storageDomainId: String? = null
	): ResponseEntity<List<EventVo>> {
		if (storageDomainId.isNullOrEmpty())
			throw ErrorPattern.STORAGE_DOMAIN_ID_NOT_FOUND.toException()
		log.info("/storages/{}/events ... Event(s) 목록", storageDomainId)
		return ResponseEntity.ok(iDomain.findAllEventsFromStorageDomain(storageDomainId))
	}



	@Autowired private lateinit var iDisk: ItDiskService

	@ApiOperation(
		httpMethod="GET",
		value="/storages/disks",
		notes="Disk 목록"
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/disks")
	@ResponseBody
	fun findAllDisks(
	): ResponseEntity<List<DiskImageVo>> {
		log.info("/storages/disks ... 스토리지 Disk 목록")
		return ResponseEntity.ok(iDisk.findAllDisks())
	}

	@ApiOperation(
		httpMethod="GET",
		value="/storages/{storageDomainId}/disks",
		notes="스토리지 도메인 밑에 붙어있는 Disk 목록"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name = "storageDomainId", value = "스토리지 도메인 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{storageDomainId}/disks")
	@ResponseBody
	fun findAllDisksFromStorageDomain(
		@PathVariable("storageDomainId") storageDomainId: String? = null // id=dcId
	): ResponseEntity<List<DiskImageVo>> {
		if (storageDomainId == null)
			throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()
		log.info("/storages/{}/disks ... 스토리지 도메인 밑에 붙어있는 Disk 목록", storageDomainId)
		return ResponseEntity.ok(iDomain.findAllDisksFromStorageDomain(storageDomainId))
	}


	@ApiOperation(
		httpMethod="POST",
		value="/storages/{dataCenterId}/disks/image",
		notes="새가상 디스크 - 이미지 생성"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name = "dataCenterId", value = "데이터센터 ID", dataTypeClass = String::class, required=true, paramType="path"),
		ApiImplicitParam(name = "diskImage", value = "디스크이미지", dataTypeClass = DiskImageVo::class, required=true, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED")
	)
	@PostMapping("/disks/image")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun addDiskImage(
		@PathVariable dataCenterId: String? = null,
		@RequestBody diskImage: DiskImageVo? = null,
	): ResponseEntity<DiskImageVo?> {
		if (dataCenterId == null)
			throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()
		if (diskImage == null)
			throw ErrorPattern.DISK_IMAGE_VO_INVALID.toException()
		log.info("/storages/{}/disks/image ... 새가상 디스크 - 이미지 생성", dataCenterId)
		return ResponseEntity.ok(iDisk.addDisk(diskImage))
	}

	@ApiOperation(
		httpMethod="PUT",
		value="/storages/{dataCenterId}/disks/image/{diskImageId}",
		notes="새가상 디스크 - 이미지 편집"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name = "dataCenterId", value = "데이터센터 ID", dataTypeClass = String::class, required=true, paramType="path"),
		ApiImplicitParam(name = "diskImageId", value = "디스크이미지 ID", dataTypeClass = String::class, required=true, paramType="path"),
		ApiImplicitParam(name = "diskImage", value = "디스크이미지", dataTypeClass = DiskImageVo::class, required=true, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED")
	)
	@PutMapping("/disks/image/{diskImageId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun editDiskImage(
		@PathVariable dataCenterId: String? = null,
		@PathVariable diskImageId: String? = null,
		@RequestBody diskImage: DiskImageVo?
	): ResponseEntity<DiskImageVo?> {
		if (dataCenterId == null)
			throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()
		if (diskImageId == null)
			throw ErrorPattern.DISK_IMAGE_ID_NOT_FOUND.toException()
		if (diskImage == null)
			throw ErrorPattern.DISK_IMAGE_VO_INVALID.toException()
		log.info("/storages/{}/disks/image/{} ... 새가상 디스크 - 이미지 편집", dataCenterId, diskImageId)
		return ResponseEntity.ok(iDisk.updateDisk(diskImage))
	}

	@ApiOperation(
		httpMethod="DELETE",
		value="/storages/{dataCenterId}/disks/image/{diskImageId}",
		notes="새가상 디스크 - 이미지 삭제"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name = "dataCenterId", value = "데이터센터 ID", dataTypeClass = String::class, required=true, paramType="path"),
		ApiImplicitParam(name = "diskImageId", value = "디스크이미지 ID", dataTypeClass = String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@DeleteMapping("/disks/image/{diskImageId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun deleteDiskImage(
		@PathVariable dataCenterId: String? = null,
		@PathVariable diskImageId: String? = null,
	): ResponseEntity<Boolean> {
		if (dataCenterId == null)
			throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()
		if (diskImageId == null)
			throw ErrorPattern.DISK_IMAGE_ID_NOT_FOUND.toException()
		log.info("/storages/{}/disks/image/{} ... 새가상 디스크 - 이미지 삭제", dataCenterId, diskImageId)
		return ResponseEntity.ok(iDisk.moveDisk(diskImageId, storageDomainId = ""))
	}

	@ApiOperation(
		httpMethod="POST",
		value="/storages/disks/{diskId}/copy",
		notes="새가상 디스크 - 이미지 삭제"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name = "diskId", value = "디스크 ID", dataTypeClass = String::class, required=true, paramType="path"),
		ApiImplicitParam(name = "diskImage", value = "디스크이미지", dataTypeClass = DiskImageVo::class, required=true, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED")
	)
	@PostMapping("disks/{diskId}/copy")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun copyDisk(
		@PathVariable diskId: String? = null,
		@RequestBody diskImage: DiskImageVo? = null,
	): ResponseEntity<Boolean> {
		if (diskId.isNullOrEmpty()) throw ErrorPattern.DISK_ID_NOT_FOUND.toException()
		if (diskImage == null)  throw ErrorPattern.DISK_IMAGE_VO_INVALID.toException()
		log.info("/storages/disks/{}/copy ... 디스크 - 복사", diskId)
		return ResponseEntity(iDisk.copyDisk(diskImage), HttpStatus.CREATED)
	}

	@ApiOperation(
		httpMethod="POST",
		value="/storages/{dataCenterId}/disks/upload",
		notes="디스크 - 업로드"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name = "dataCenterId", value = "데이터센터 ID", dataTypeClass = String::class, required=true, paramType="path"),
		ApiImplicitParam(name = "diskImage", value = "디스크이미지", dataTypeClass = DiskImageVo::class, required=true, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@PostMapping("/disks/upload")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@Throws(IdNotFoundException::class, InvalidRequestException::class, IOException::class)
	fun uploadDisk(
		@PathVariable dataCenterId: String? = null,
		@RequestPart file: MultipartFile,
		@RequestPart diskImage: DiskImageVo?
	): ResponseEntity<Boolean> {
		if (dataCenterId == null)
			throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()
		if (diskImage == null)
			throw ErrorPattern.DISK_IMAGE_VO_INVALID.toException()
		return ResponseEntity.ok(iDisk.uploadDisk(file, diskImage))
	}

//	@ApiOperation(
//		httpMethod="GET",
//		value="/{dataCenterId}/disks/{diskId}",
//		notes="Domain(s) 목록"
//	)
//	@ApiImplicitParams(
//		ApiImplicitParam(name = "dataCenterId", value = "데이터센터 ID", dataTypeClass = String::class, required=true, paramType="path"),
//		ApiImplicitParam(name = "diskId", value = "디스크 ID", dataTypeClass = String::class, required=true, paramType="path"),
//	)
//	@ApiResponses(
//		ApiResponse(code = 200, message = "OK")
//	)
//	@GetMapping("/{dataCenterId}/disks/{diskId}")
//	@ResponseStatus(HttpStatus.OK)
//	@ResponseBody
//	fun disks(
//		@PathVariable dataCenterId: String? = null,
//		@PathVariable diskId: String? = null,
//	): ResponseEntity<List<DiskImageVo>> {
//		if (dataCenterId.isNullOrEmpty())
//			throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()
//		if (diskId.isNullOrEmpty())
//			throw ErrorPattern.DISK_ID_NOT_FOUND.toException()
//		// TODO: 파라미터 재정의 필요 id인지 dcID인지
//		// 데이터센터 밑에 붙어있는 디스크
//		log.info("--- Disk 목록")
//		return ResponseEntity.ok(iStorage.findAllDisksFromDataCenter(dataCenterId))
//	}


//	@ApiOperation(
//		httpMethod="GET",
//		value="/{dataCenterId}/networks",
//		notes="Network(s) 목록")
//	@ApiImplicitParams(
//		ApiImplicitParam(name="dataCenterId", value="데이터센터 ID", dataTypeClass=String::class, required=true, paramType="path"),
//	)
//	@ApiResponses(
//		ApiResponse(code = 200, message = "OK")
//	)
//	@GetMapping("/{dataCenterId}/networks")
//	@ResponseStatus(HttpStatus.OK)
//	@ResponseBody
//	fun networks(
//		@PathVariable dataCenterId: String? = null,
//	): ResponseEntity<List<NetworkVo>> {
//		if (dataCenterId.isNullOrEmpty())
//			throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()
//		log.info("/storages/{}/networks ... Network(s) 목록", dataCenterId)
//		return ResponseEntity.ok(iStorage.findAllNetworksFromDataCenter(dataCenterId))
//	}

//	@ApiOperation(
//		httpMethod="GET",
//		value="/{dataCenterId}/clusters",
//		notes="Cluster(s) 목록"
//	)
//	@ApiImplicitParams(
//		ApiImplicitParam(name="dataCenterId", value="데이터센터 ID", dataTypeClass=String::class, required=true, paramType="path"),
//	)
//	@ApiResponses(
//		ApiResponse(code = 200, message = "OK")
//	)
//	@GetMapping("/{dataCenterId}/clusters")
//	@ResponseStatus(HttpStatus.OK)
//	@ResponseBody
//	fun clusters(
//		@PathVariable("dataCenterId") dataCenterId: String? = null,
//	): ResponseEntity<List<ClusterVo>> {
//		if (dataCenterId.isNullOrEmpty())
//			throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()
//		log.info("/storages/{}/clusters ... Cluster(s) 목록", dataCenterId)
//		return ResponseEntity.ok(iStorage.findAllClustersFromDataCenter(dataCenterId))
//	}

	
	companion object {
		private val log by LoggerDelegate()
	}
}