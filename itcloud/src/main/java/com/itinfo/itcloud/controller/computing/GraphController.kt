package com.itinfo.itcloud.controller.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.computing.DashBoardVo
import com.itinfo.itcloud.repository.dto.HostUsageDto
import com.itinfo.itcloud.repository.dto.StorageUsageDto
import com.itinfo.itcloud.repository.dto.UsageDto
import com.itinfo.itcloud.service.computing.ItGraphService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@Api(tags = ["Graph"])
@RequestMapping("/api/v1/dashboard")
class GraphController {
	@Autowired private lateinit var graph: ItGraphService

	@ApiOperation(
		httpMethod="GET",
		value="대시보드 정보",
		notes="대시보드 정보를 조회한다"
	)
	@GetMapping
	@ResponseBody
	fun dashboard(): ResponseEntity<DashBoardVo> {
		log.info("----- 대시보드")
		return ResponseEntity.ok(graph.getDashboard())
	}

	@ApiOperation(
		httpMethod="GET",
		value="호스트 사용정보",
		notes="호스트 사용정보를 조회한다"
	)
	@GetMapping("/cpumemory")
	@ResponseBody
	fun totalCpuMemory(): ResponseEntity<HostUsageDto> {
		log.info("----- cpu, memory")
		return ResponseEntity.ok(graph.totalCpuMemory())
	}

	@ApiOperation(
		httpMethod="GET",
		value="스토리지 총 사용량 정보",
		notes="스토리지 총 사용량 정보를 조회한다"
	)
	@GetMapping("/storage")
	@ResponseBody
	fun totalStorage(): ResponseEntity<StorageUsageDto> {
		log.info("----- storage")
		return ResponseEntity.ok(graph.totalStorage())
	}

	@ApiOperation(
		httpMethod="GET",
		value="VM의 CPU 총 사용량 정보",
		notes="VM의 CPU 총 사용량 정보를 조회한다"
	)
	@GetMapping("/vmCpu")
	@ResponseBody
	fun vmCpuChart(): ResponseEntity<List<UsageDto>> {
		log.info("----- vmCpuChart")
		return ResponseEntity.ok(graph.vmCpuChart())
	}

	@ApiOperation(
		httpMethod="GET",
		value="VM의 메모리 총 사용량 정보",
		notes="VM의 메모리 총 사용량 정보를 조회한다"
	)
	@GetMapping("/vmMemory")
	@ResponseBody
	fun vmMemoryChart(): ResponseEntity<List<UsageDto>> {
		log.info("----- vmMemoryChart")
		return ResponseEntity.ok(graph.vmMemoryChart())
	}

		@GetMapping("/storageMemory")
	fun storageChart(): ResponseEntity<List<UsageDto>> {
		log.info("----- storageChart")
		return ResponseEntity.ok(graph.storageChart())
	}
	
	companion object {
		private val log by LoggerDelegate()
	}
}
