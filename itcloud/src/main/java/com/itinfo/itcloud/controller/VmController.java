package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.computing.HostVO;
import com.itinfo.itcloud.service.ItVmService;
import com.itinfo.itcloud.model.computing.VmVO;
import com.itinfo.itcloud.service.ItSystemPropertyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
public class VmController {

    @Autowired
    private ItVmService itVmService;

    @Autowired
    private ItSystemPropertyService itSystemPropertyService;

    public VmController(){}


    @GetMapping("/computing/vms")
    public String vmList(Model model){
        List<VmVO> vmVOList = itVmService.getList();
        model.addAttribute("vmList", vmVOList);
        return "computing/vms";
    }

    @GetMapping("/vmsList")
    @ResponseBody
    public List<VmVO> vms(){
        long start = System.currentTimeMillis();

        List<VmVO> vmsList = itVmService.getList();

        long end = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        System.out.println("수행시간(ms): " + (end-start));

        return vmsList;
    }

    @GetMapping("/computing/vm")
    public String vm(String id, Model model){
        VmVO vm = itVmService.getInfo(id);
        model.addAttribute("vm", vm);
        model.addAttribute("id", id);

        return "computing/vm";
    }

    @GetMapping("/computing/vm/status")
    @ResponseBody
    public VmVO vm(String id){
        log.info("----- vm id 일반 불러오기: " + id);
        return itVmService.getInfo(id);
    }


    @GetMapping("/computing/vm-nic")
    public String nic(String id, Model model){
        VmVO nic = itVmService.getNic(id);
        model.addAttribute("nic", nic);
        model.addAttribute("id", id);

        return "computing/vm-nic";
    }

    @GetMapping("/computing/vm/nicstatus")
    @ResponseBody
    public VmVO nic(String id){
        log.info("----- vm nic 일반 불러오기: " + id);
        return itVmService.getNic(id);
    }


    @GetMapping("/computing/vm-disk")
    public String disk(String id, Model model){
        VmVO disk = itVmService.getDisk(id);
        model.addAttribute("disk", disk);
        model.addAttribute("id", id);

        return "computing/vm-disk";
    }

    @GetMapping("/computing/vm/diskstatus")
    @ResponseBody
    public VmVO disk(String id){
        log.info("----- vm disk 일반 불러오기: " + id);
        return itVmService.getDisk(id);
    }


}
