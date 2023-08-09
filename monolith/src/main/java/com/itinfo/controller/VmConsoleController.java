package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.VmConsoleService;
import com.itinfo.model.VmConsoleVo;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class VmConsoleController {
	@Autowired private VmConsoleService vmConsoleService;

	@RequestMapping({"/vmConsole/ticket"})
	public String ConsoleConnectionInformation(@RequestBody VmConsoleVo vmConsoleVo, Model model) {
		log.info("ConsoleConnectionInformation ...");
		try {
			vmConsoleVo = this.vmConsoleService.getDisplayTicket(vmConsoleVo);
		} catch (Exception e) {
			model.addAttribute("result", "error");
		}
		model.addAttribute("vmConsoleVo", vmConsoleVo);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/vmconsole/ticket2"})
	public String ConsoleConnectionInformation2(Model model) {
		log.info("ConsoleConnectionInformation2 ...");
		VmConsoleVo vo = new VmConsoleVo();
		vo.setType("vnc");
		vo.setVmName("nested-host");
		vo = this.vmConsoleService.getDisplayTicket(vo);
		model.addAttribute("vo", vo);
		return ItInfoConstant.JSON_VIEW;
	}

	@RequestMapping({"/vmConsole/vncView"})
	public String vncView() {
		log.info("vncView ...");
		return "/vmconsole/vnc";
	}
}
