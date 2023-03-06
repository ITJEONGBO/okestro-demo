package com.itinfo.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LicenseController {

	@RequestMapping({"/admin/license"})
	public String systemProperties() {
		return "/castanets/admin/license";
	}
}
