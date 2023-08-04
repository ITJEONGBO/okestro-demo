package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.DataCenterService;
import com.itinfo.model.DataCenterVo;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class DataCenterController {
	@Autowired private DataCenterService dataCenterService;

	@RequestMapping({"/compute/dataCenters/retrieveDataCenters"})
	public String retrieveDataCentersInfo(Model model) {
		log.info("... retrieveDataCentersInfo");
		List<DataCenterVo> dataCenters = this.dataCenterService.retrieveDataCenters();
		model.addAttribute(ItInfoConstant.RESULT_KEY, dataCenters);
		return ItInfoConstant.JSON_VIEW;
	}
}
