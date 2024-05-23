package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.ConsoleVo;
import com.itinfo.itcloud.service.ItConsoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.types.GraphicsType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/computing")
public class ConsoleController {

    private final ItConsoleService consoleService;

    @GetMapping("/test")
    public String test(){
        return "/computing/in2";
    }

    @PostMapping("/vm/{id}/console")
    @ResponseStatus(HttpStatus.OK)
    public ConsoleVo console(@PathVariable String id,
                             @RequestBody ConsoleVo consoleVo) {

        log.info("--- 가상머신 콘솔");
        return consoleService.setDisplay(id, consoleVo);
    }

    @PostMapping("/vm/{id}/console2")
    @ResponseStatus(HttpStatus.OK)
    public ConsoleVo console2(@PathVariable String id,  Model model) {
        ConsoleVo vo =
                ConsoleVo.builder()
                        .protocol(String.valueOf(GraphicsType.VNC))
                        .vmId(id)
                        .build();
        vo = consoleService.setDisplay(id, vo);

        model.addAttribute("vo", vo);

        return vo;
    }

    @GetMapping("/redirect")
    public String redirect() {
        return "redirect:http://192.168.0.80:8443/dashboard";
    }



    @GetMapping("/vmConsole/vncView")
    public String vncView() {
        return "/computing/vnc";
    }


}
