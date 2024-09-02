package com.itinfo.itcloud.controller.common

import com.itinfo.itcloud.model.common.TreeNavigationalDataCenter
import com.itinfo.itcloud.service.common.ItTreeNavigationService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus


@Controller
@Api(tags = ["Navigation"])
@RequestMapping("/api/v1/navigation/")
class NavigationController {
	@Autowired private lateinit var treeNavigation: ItTreeNavigationService

	@ApiOperation(
		httpMethod="GET",
		value="컴퓨팅 목록이 담긴 네비게이션 정보조회",
		notes="컴퓨팅 목록이 담긴 네비게이션 목록을 조회한다")
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("{typeId}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	fun findDcNavigationals(
		@PathVariable typeId: String = "none"
	): ResponseEntity<List<TreeNavigationalDataCenter>> {
		val res: List<TreeNavigationalDataCenter> = when(typeId) {
			"cluster", "clusters" -> treeNavigation.findAllNavigationalsWithClusters() // 클러스터
			"network", "networks" -> treeNavigation.findAllNavigationalsWithNetworks() // 네트워크
			"storagedomain", "storagedomains" -> treeNavigation.findAllNavigationalsWithStorageDomains() // 스토리지 도메인
			else -> listOf()
		}
		return ResponseEntity(res, HttpStatus.OK)
	}
}