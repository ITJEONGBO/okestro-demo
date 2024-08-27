package com.itinfo.itcloud.controller.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.controller.BaseController
import com.itinfo.itcloud.error.toException
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.itcloud.model.computing.DataCenterVo
import com.itinfo.itcloud.model.computing.EventVo
import com.itinfo.itcloud.model.response.Res
import com.itinfo.itcloud.service.computing.ItDataCenterService
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
@Api(tags = ["Computing-DataCenter"])
@RequestMapping("/computing/datacenters")
class DataCenterController: BaseController() {
	@Autowired private lateinit var iDataCenter: ItDataCenterService
/*
	@ApiOperation(
		httpMethod="GET",
		value="/computing/datacenters",
		notes="데이터센터 목록 > 전체 데이터센터 목록을 조회한다"
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun datacenters(): List<DataCenterVo> {
		log.info("/computing/datacenters ... 데이터센터 목록")
		return dcService.get
	}
*/

	@ApiOperation(
		httpMethod="POST",
		value="데이터센터 생성",
		notes="데이터센터를 생성한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="dataCenter", value="데이터센터", dataTypeClass=DataCenterVo::class, paramType="body"),
	)
	@ApiResponses(
		ApiResponse(code = 201, message = "CREATED")
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
		httpMethod="GET",
		value="데이터센터의 정보 상세조회",
		notes="선택된 데이터센터의 정보를 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="dataCenterId", value="데이터센터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{dataCenterId}/edit")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun findOne(
		@PathVariable dataCenterId: String? = null,
	): ResponseEntity<DataCenterVo?> {
		if (dataCenterId.isNullOrEmpty())
			throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()
		log.info("/computing/datacenters/{}/edit ... 데이터센터 편집 창", dataCenterId)
		return ResponseEntity(iDataCenter.findOne(dataCenterId), HttpStatus.OK)
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
	@PutMapping("/{dataCenterId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun editDataCenter(
		@PathVariable dataCenterId: String? = null,
		@RequestBody dataCenterVo: DataCenterVo? = null,
	): ResponseEntity<DataCenterVo?> {
		if (dataCenterId.isNullOrEmpty())
			throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()
		if (dataCenterVo == null)
			throw ErrorPattern.DATACENTER_VO_INVALID.toException()
		log.info("/computing/datacenters/{} ... 데이터센터 편집\n{}", dataCenterId, dataCenterVo)
		return ResponseEntity(iDataCenter.update(dataCenterVo), HttpStatus.OK)
	}


	@ApiOperation(
		httpMethod="DELETE",
		value="데이터센터 삭제",
		notes="데이터센터를 삭제한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="dataCenterId", value="데이터센터 ID", dataTypeClass=String::class, required=true, paramType="path"),
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
		return ResponseEntity(iDataCenter.remove(dataCenterId), HttpStatus.OK)
	}


	@ApiOperation(
		httpMethod="GET",
		value="데이터센터 이벤트 목록조회",
		notes="데이터센터의 이벤트 목록을 조회한다"
	)
	@ApiImplicitParams(
		ApiImplicitParam(name="dataCenterId", value="데이터센터 ID", dataTypeClass=String::class, required=true, paramType="path"),
	)
	@GetMapping("/{dataCenterId}/events")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun getEventsByDatacenter(
		@PathVariable dataCenterId: String? = null,
	): ResponseEntity<List<EventVo>> {
		if (dataCenterId.isNullOrEmpty())
			throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()
		log.info("/computing/datacenters/{}/events ... 데이터센터 이벤트목록", dataCenterId)
		return ResponseEntity(iDataCenter.findAllEventsBy(dataCenterId), HttpStatus.OK)
	}

	// 대시보드 옆에 트리구조
	companion object {
		private val log by LoggerDelegate()
	}
}