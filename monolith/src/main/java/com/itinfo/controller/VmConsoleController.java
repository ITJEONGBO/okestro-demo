package com.itinfo.controller;

import com.itinfo.service.VmConsoleService;
import com.itinfo.model.VmConsoleVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VmConsoleController {
	@Autowired private VmConsoleService vmConsoleService;

	@RequestMapping({"/vmConsole/ticket"})
	public String ConsoleConnectionInformation(@RequestBody VmConsoleVo vmConsoleVo, Model model) {
		try {
			vmConsoleVo = this.vmConsoleService.getDisplayTicket(vmConsoleVo);
		} catch (Exception e) {
			model.addAttribute("result", "error");
		}
		model.addAttribute("vmConsoleVo", vmConsoleVo);
		return "jsonView";
	}

	@RequestMapping({"/vmconsole/ticket2"})
	public String ConsoleConnectionInformation2(Model model) {
		VmConsoleVo vo = new VmConsoleVo();
		vo.setType("vnc");
		vo.setVmName("nested-host");
		vo = this.vmConsoleService.getDisplayTicket(vo);
		model.addAttribute("vo", vo);
		return "jsonView";
	}

	@RequestMapping({"/vmConsole/vncView"})
	public String vncView() {
		return "/vmconsole/vnc";
	}
}
