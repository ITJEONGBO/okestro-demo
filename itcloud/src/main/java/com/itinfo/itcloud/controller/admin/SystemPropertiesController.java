package com.itinfo.itcloud.controller.admin;

import com.itinfo.util.model.SystemPropertiesVo;
import com.itinfo.itcloud.service.admin.ItSystemPropertyService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequiredArgsConstructor
@Api(tags = "system")
public class SystemPropertiesController {
	private final ItSystemPropertyService itSystemPropertyService;

	@GetMapping("/admin/setting")
	public String setting(Model model) {
		SystemPropertiesVo systemProperties = this.itSystemPropertyService.searchSystemProperties();
		model.addAttribute("setting", systemProperties);
		return "admin/setting";
	}

	@GetMapping("/admin/searchSystemProperties")
	@ResponseBody
	public SystemPropertiesVo searchSystemProperties() {
		SystemPropertiesVo systemPropertiesVO = null;
		try {
			systemPropertiesVO = this.itSystemPropertyService.searchSystemProperties();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return systemPropertiesVO;
	}

	// 관리-설정-엔진 에서 id, pw, ip를 입력하고 저장을 하면 대시보드 결과가 뜬다.
	// 이거는 json방식이긴한데.. 일단 나중에
	@GetMapping("/admin/saveSystemProperties")
	@ResponseBody
	public int systemPropertiesVO() {
		SystemPropertiesVo systemPropertiesVO = null;
		int result = 0;
		try {
			result = itSystemPropertyService.saveSystemProperties(systemPropertiesVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


}