package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.DataCenterService;
import com.itinfo.model.DataCenterVo;

import java.util.List;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
@Api(value = "DataCenterController", tags = {"data-center"})
public class DataCenterController extends BaseController {
	@Autowired private DataCenterService dataCenterService;

	@ApiOperation(httpMethod = "GET", value = "retrieveDataCentersInfo", notes = "데이터 센터 목록 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/compute/dataCenters/retrieveDataCenters"})
	public String retrieveDataCentersInfo(Model model) {
		log.info("... retrieveDataCentersInfo");
		List<DataCenterVo> dataCenters = dataCenterService.retrieveDataCenters();
		model.addAttribute(ItInfoConstant.RESULT_KEY, dataCenters);
		return ItInfoConstant.JSON_VIEW;
	}
}
