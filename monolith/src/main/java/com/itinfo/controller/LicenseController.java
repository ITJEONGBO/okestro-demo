package com.itinfo.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class LicenseController {

	@RequestMapping({"/admin/license"})
	public String systemProperties() {
		log.info("... systemProperties");
		return "/castanets/admin/license";
	}
}
