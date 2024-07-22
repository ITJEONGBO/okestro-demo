package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.computing.DashBoardVo;
import com.itinfo.itcloud.repository.dto.HostUsageDto;
import com.itinfo.itcloud.repository.dto.StorageUsageDto;
import com.itinfo.itcloud.repository.dto.UsageDto;
import com.itinfo.itcloud.service.computing.ItGraphService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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


    @GetMapping("/cpumemory")
    public HostUsageDto totalCpuMemory() {
        log.info("----- cpu, memory");
        return graphService.totalCpuMemory();
    }

    @GetMapping("/storage")
    public StorageUsageDto totalStorage() {
        log.info("----- storage");
        return graphService.totalStorage();
    }



    @GetMapping("/vmCpu")
    public List<UsageDto> vmCpuChart() {
        log.info("----- vmCpuChart");
        return graphService.vmCpuChart();
    }

    @GetMapping("/vmMemory")
    public List<UsageDto> vmMemoryChart() {
        log.info("----- vmMemoryChart");
        return graphService.vmMemoryChart();
    }


    @GetMapping("/storageMemory")
    public List<UsageDto> storageChart() {
        log.info("----- storageChart");
        return graphService.storageChart();
    }



}
