package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
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
        DataCenterVo dcv = dashboardService.retrieveDataCenterStatus();
        model.addAttribute(ItInfoConstant.RESULT_KEY, dcv);
        return "jsonView";
    }

    @RequestMapping({"/dashboard/retrieveEvents"})
    public String retrieveEvents(Model model) {
        log.info("... retrieveEvents");
        List<EventVo> events = dashboardService.retrieveEvents();
        model.addAttribute(ItInfoConstant.RESULT_KEY, events);
        return "jsonView";
    }

    @RequestMapping(value = {"/dashboard/retrieveVms"}, method = {RequestMethod.GET})
    public String retrieveVms(String status, Model model) {
        log.info("... retrieveVms('{}')", status);
        List<VmVo> vms
                = virtualMachinesService.retrieveVmsAll();
        List<DashboardTopVo> vmsTop = !vms.isEmpty()
                ? virtualMachinesService.retrieveVmsTop(vms)
                : new ArrayList<>();
        model.addAttribute(ItInfoConstant.RESULT_KEY, vmsTop);
        return "jsonView";
    }

    @RequestMapping(value = {"/dashboard/retrieveHosts"}, method = {RequestMethod.GET})
    public String retrieveHosts(String status, Model model) {
        log.info("... retrieveHosts('{}')", status);
        List<HostDetailVo> hosts
                = hostsService.retrieveHostsInfo(status);
        List<DashboardTopVo> hostsTop = !hosts.isEmpty()
            ? hostsService.retrieveHostsTop(hosts)
            : new ArrayList<>();
        model.addAttribute(ItInfoConstant.RESULT_KEY, hostsTop);
        return "jsonView";
    }
}

