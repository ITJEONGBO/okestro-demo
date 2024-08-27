package com.itinfo.itcloud.controller.admin

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.response.Res
import com.itinfo.itcloud.service.admin.ItSystemPropertiesService
import com.itinfo.util.model.SystemPropertiesVo

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

/**
 * [SystemPropertiesController]
 * 시스템 설정 값
 */
@Controller
@Api(tags = ["system"])
@RequestMapping("/admin/sysprop")
class SystemPropertiesController {
	@Autowired private lateinit var sysProp: ItSystemPropertiesService

	@ApiOperation(
		httpMethod="GET",
		value="시스템 설정정보 조회",
		notes="시스템 설정정보를 조회한다")
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping()
	@ResponseBody
	fun findOne(): SystemPropertiesVo {
		log.debug("/admin/sysprop ... ")
		return sysProp.findOne()
	}


	@ApiOperation(
		httpMethod="GET",
		value="시스템 설정정보 저장",
		notes="시스템 설정정보를 저장한다"
	)
	@ApiResponses(
		ApiResponse(code = 200, message = "OK")
	)
	@GetMapping("/update")
	@ResponseBody
	fun update(): SystemPropertiesVo? {
		log.debug("/admin/sysprop/update ... ")
		// 관리-설정-엔진 에서 id, pw, ip를 입력하고 저장을 하면 대시보드 결과가 뜬다.
		// 이거는 json방식이긴한데.. 일단 나중에
		return sysProp.update(sysProp.findOne())
	}
	companion object {
		private val log by LoggerDelegate()
	}
}