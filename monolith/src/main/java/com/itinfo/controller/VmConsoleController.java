package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.VmConsoleService;
import com.itinfo.model.VmConsoleVo;

import io.swagger.annotations.*;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
@Api(
		value = "VmConsoleController",
		tags = {"vm-console"}
)
public class VmConsoleController {
	@Autowired private VmConsoleService vmConsoleService;

	@ApiOperation(httpMethod = "GET", value = "vncView", notes = "페이지 이동 > /vmConsole/vncView")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/vmConsole/vncView"})
	public String vncView() {
		log.info("vncView ...");
		return "/vmconsole/vnc";
	}

	@ApiOperation(httpMethod = "POST", value = "ConsoleConnectionInformation", notes = "VM Console 연결 시도")
	@ApiImplicitParams({
			@ApiImplicitParam(name="vmConsoleVo", value = "VM Console 정보")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/vmConsole/ticket"})
	public String ConsoleConnectionInformation(@RequestBody VmConsoleVo vmConsoleVo, Model model) {
		log.info("ConsoleConnectionInformation ...");
		try {
			vmConsoleVo = this.vmConsoleService.getDisplayTicket(vmConsoleVo);
		} catch (Exception e) {
			model.addAttribute(ItInfoConstant.RESULT_KEY, "error");
		}
		model.addAttribute("vmConsoleVo", vmConsoleVo);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "ConsoleConnectionInformation2", notes = "VM Console 연결 시도2")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/vmConsole/ticket2"})
	public String ConsoleConnectionInformation2(Model model) {
		log.info("ConsoleConnectionInformation2 ...");
		VmConsoleVo vo = new VmConsoleVo();
		vo.setType("vnc");
		vo.setVmName("nested-host");
		vo = this.vmConsoleService.getDisplayTicket(vo);
		model.addAttribute("vo", vo);
		return ItInfoConstant.JSON_VIEW;
	}
}
