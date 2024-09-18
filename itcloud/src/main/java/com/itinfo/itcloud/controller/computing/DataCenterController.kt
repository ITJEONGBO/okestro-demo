package com.itinfo.itcloud.controller.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.controller.BaseController
import com.itinfo.itcloud.error.toException
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.itcloud.model.computing.DataCenterVo
import com.itinfo.itcloud.model.computing.EventVo
import com.itinfo.itcloud.model.network.NetworkVo
import com.itinfo.itcloud.model.storage.StorageDomainVo
import com.itinfo.itcloud.service.computing.ItDataCenterService
import com.itinfo.itcloud.service.network.ItNetworkService
import com.itinfo.itcloud.service.storage.ItStorageService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@Api(tags = ["Computing", "DataCenter"])
@RequestMapping("/api/v1/computing/datacenters")
class DataCenterController: BaseController() {
	@Autowired private lateinit var iDataCenter: ItDataCenterService
	@Autowired private lateinit var iNetwork: ItNetworkService
	@Autowired private lateinit var iStorage: ItStorageService

	@ApiOperation(
		httpMethod="GET",
		value="데이터센터 목록 조회",
		notes="전체 데이터센터 목록을 조회한다"
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun findAll(): ResponseEntity<List<DataCenterVo?>> {
		log.info("/computing/datacenters ... 데이터센터 목록")
		return ResponseEntity.ok(iDataCenter.findAll())
	}

	@ApiOperation(
		httpMethod="GET",
		value="데이터센터의 정보 상세조회",
		notes="선택된 데이터센터의 정보를 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="dataCenterId", value="데이터센터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{dataCenterId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun findOne(
		@PathVariable dataCenterId: String? = null,
	): ResponseEntity<DataCenterVo?> {
		if (dataCenterId.isNullOrEmpty())
			throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()
		log.info("/computing/datacenters/{} ... 데이터센터 상세정보", dataCenterId)
		return ResponseEntity.ok(iDataCenter.findOne(dataCenterId))
	}

	@ApiOperation(
		httpMethod="POST",
		value="데이터센터 생성",
		notes="데이터센터를 생성한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="dataCenter", value="데이터센터", dataTypeClass=DataCenterVo::class, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED"),
		ApiResponse(code = 404, message = "NOT_FOUND")
	)
	@PostMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	fun add(
		@RequestBody dataCenterVo: DataCenterVo?
	): ResponseEntity<DataCenterVo?> {
		if (dataCenterVo == null)
			throw ErrorPattern.DATACENTER_VO_INVALID.toException()
		log.info("/computing/datacenters ... 데이터센터 생성\n{}", dataCenterVo)
		return ResponseEntity(iDataCenter.add(dataCenterVo), HttpStatus.CREATED)
	}

	@ApiOperation(
		httpMethod="PUT",
		value="데이터센터 편집",
		notes="데이터센터를 편집한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="dataCenterId", value="데이터센터 ID", dataTypeClass=String::class, required=true, paramType="path"),
		ApiImplicitParam(name="dataCenter", value="데이터센터", dataTypeClass=DataCenterVo::class, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@PutMapping("/{dataCenterId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun update(
		@PathVariable dataCenterId: String? = null,
		@RequestBody dataCenter: DataCenterVo? = null,
	): ResponseEntity<DataCenterVo?> {
		if (dataCenterId.isNullOrEmpty())
			throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()
		if (dataCenter == null)
			throw ErrorPattern.DATACENTER_VO_INVALID.toException()
		log.info("/computing/datacenters/{} ... 데이터센터 편집\n{}", dataCenterId, dataCenter)
		return ResponseEntity.ok(iDataCenter.update(dataCenter))
	}

	@ApiOperation(
		httpMethod="DELETE",
		value="데이터센터 삭제",
		notes="데이터센터를 삭제한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="dataCenterId", value="데이터센터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@DeleteMapping("/{dataCenterId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun remove(
		@PathVariable dataCenterId: String? = null,
	): ResponseEntity<Boolean> {
		if (dataCenterId.isNullOrEmpty())
			throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()
		log.info("/computing/datacenters/{} ... 데이터센터 삭제", dataCenterId)
		return ResponseEntity.ok(iDataCenter.remove(dataCenterId))
	}

	@ApiOperation(
		httpMethod="GET",
		value="데이터센터 이벤트 목록조회",
		notes="데이터센터의 이벤트 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="dataCenterId", value="데이터센터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{dataCenterId}/events")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun findAllEventsFromDataCenter(
		@PathVariable dataCenterId: String? = null,
	): ResponseEntity<List<EventVo>> {
		if (dataCenterId.isNullOrEmpty())
			throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()
		log.info("/computing/datacenters/{}/events ... 데이터센터 이벤트목록", dataCenterId)
		return ResponseEntity.ok(iDataCenter.findAllEventsFromDataCenter(dataCenterId))
	}


	/**
	 * 네트워크 - 데이터센터가 가지고 있는 네트워크 목록
	 */
	@ApiOperation(
		httpMethod="GET",
		value="네트워크 목록 조회",
		notes="네트워크 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="dataCenterId", value="데이터센터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/{dataCenterId}/networks")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun findAllNetworksFromDataCenter(
		@PathVariable dataCenterId: String? = null
	): ResponseEntity<List<NetworkVo>> {
		if (dataCenterId.isNullOrEmpty())
			throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()
		log.info("GET /api/v1/dataCenters/{}/networks ... 네트워크 목록", dataCenterId)
		return ResponseEntity.ok(iNetwork.findAllFromDataCenter(dataCenterId))
	}

	/**
	 * 스토리지 - 데이터센터가 가지고 있는 도메인 목록
	 */
	@ApiOperation(
		httpMethod="GET",
		value="스토리지 - 도메인 목록 조회",
		notes="스토리지 - 도메인 목록을 조회한다"
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
	fun findAllDomainsFromDataCenter(
		@PathVariable dataCenterId: String? = null,
	): ResponseEntity<List<StorageDomainVo>> {
		if (dataCenterId.isNullOrEmpty())
			throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()
		log.info("GET /api/v1/dataCenters/{}/domains ... 스토리지 목록", dataCenterId)
		return ResponseEntity.ok(iStorage.findAllDomainsFromDataCenter(dataCenterId))
	}


	// 대시보드 옆에 트리구조
	companion object {
		private val log by LoggerDelegate()
	}
}