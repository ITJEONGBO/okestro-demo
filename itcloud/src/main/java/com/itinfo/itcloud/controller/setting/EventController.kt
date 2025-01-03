package com.itinfo.itcloud.controller.setting

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.controller.BaseController
import com.itinfo.itcloud.model.computing.EventVo
import com.itinfo.itcloud.service.setting.ItEventService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@Controller
@Api(tags = ["Events"])
@RequestMapping("/api/v1/events")
class EventController: BaseController() {
    @Autowired private lateinit var iEvent: ItEventService

    @ApiOperation(
        httpMethod="GET",
        value="이벤트 목록 조회",
        notes="전체 이벤트 목록을 조회한다"
    )
    @ApiResponses(
        ApiResponse(code = 200, message = "OK")
    )
    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    fun events(): ResponseEntity<List<EventVo>> {
        log.info("/events ... 이벤트 목록")
        return ResponseEntity.ok(iEvent.findAll())
    }

    companion object {
        private val log by LoggerDelegate()
    }
}