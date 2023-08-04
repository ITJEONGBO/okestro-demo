package com.itinfo.controller;

import com.itinfo.service.HostsService;
import com.itinfo.service.VirtualMachinesService;
import com.itinfo.service.DashboardService;
import com.itinfo.model.*;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
public class DashboardController {
    @Autowired private DashboardService dashboardService;
    @Autowired private VirtualMachinesService virtualMachinesService;
    @Autowired private HostsService hostsService;

    @RequestMapping({"/dashboard"})
    public String getDashboardView() {
        log.info("... getDashboardView");
        return "/castanets/dashboard/dashboard";
    }

    @RequestMapping({"/dashboard/retrieveDataCenterStatus"})
    public String retrieveDataCenterStatus(Model model) {
        log.info("... retrieveDataCenterStatus");
        DataCenterVo dcv = this.dashboardService.retrieveDataCenterStatus();
        model.addAttribute("resultKey", dcv);
        return "jsonView";
    }

    @RequestMapping({"/dashboard/retrieveEvents"})
    public String retrieveEvents(Model model) {
        List<EventVo> events = this.dashboardService.retrieveEvents();
        model.addAttribute("resultKey", events);
        return "jsonView";
    }

    @RequestMapping(value = {"/dashboard/retrieveVms"}, method = {RequestMethod.GET})
    public String retrieveVms(String status, Model model) {
        List<VmVo> vms
                = this.virtualMachinesService.retrieveVmsAll();
        List<DashboardTopVo> vmsTop = new ArrayList<>();
        if (!vms.isEmpty())
            vmsTop = this.virtualMachinesService.retrieveVmsTop(vms);
        model.addAttribute("resultKey", vmsTop);
        return "jsonView";
    }

    @RequestMapping(value = {"/dashboard/retrieveHosts"}, method = {RequestMethod.GET})
    public String retrieveHosts(String status, Model model) {
        List<HostDetailVo> hosts
                = this.hostsService.retrieveHostsInfo(status);
        List<DashboardTopVo> hostsTop = new ArrayList<>();
        if (!hosts.isEmpty())
            hostsTop = this.hostsService.retrieveHostsTop(hosts);
        model.addAttribute("resultKey", hostsTop);
        return "jsonView";
    }
}

