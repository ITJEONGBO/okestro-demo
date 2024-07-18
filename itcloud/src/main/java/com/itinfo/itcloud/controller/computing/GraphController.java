package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.computing.DashBoardVo;
import com.itinfo.itcloud.repository.dto.HostUsageDto;
import com.itinfo.itcloud.service.computing.ItGraphService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequiredArgsConstructor
@Api(tags = "Graph")
@ResponseBody
@RequestMapping("/dashboard")
public class GraphController {

    @Autowired ItGraphService graphService;

    // Dashboard
    @GetMapping
    public DashBoardVo dashboard() {
        log.info("----- 대시보드");
        return graphService.getDashboard();
    }

    @GetMapping("/cpu")
    public int totalCpu() {
        log.info("----- cpu");
        return graphService.totalCpu();
    }

    @GetMapping("/cpumemory")
    public HostUsageDto totalCpuMemory() {
        log.info("----- cpu, memory");
        return graphService.totalCpuMemory();
    }

}
