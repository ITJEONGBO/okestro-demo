package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.computing.HostVo;
import com.itinfo.itcloud.model.computing.NicVo;
import com.itinfo.itcloud.model.computing.VmVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import com.itinfo.itcloud.service.ItVnicService;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class VnicProfileController {

    private final ItVnicService itVnicService;

    @GetMapping("/network/vnicProfiles")
    public String vnicProfiles(Model model){
        List<VnicProfileVo> vnics = itVnicService.getVnics();
        model.addAttribute("vnics", vnics);
        log.info("---vnics");
        return "/network/vnicProfiles";
    }


    @GetMapping("/network/vnicProfilesStatus")
    @ResponseBody
    public List<VnicProfileVo> vnics(){
        log.info("----- VnicProfile 목록 불러오기");
        return itVnicService.getVnics();
    }

    @GetMapping("/network/vnicProfile-vm")
    public String vnicVm(String id, Model model){
        List<VmVo> nicVm = itVnicService.getVmNics(id);
        model.addAttribute("nicVm", nicVm);
        log.info("---nicVm");
        return "/network/vnicProfile-vm";
    }


    @GetMapping("/network/vnicProfilesvmStatus")
    @ResponseBody
    public List<VmVo> vm(String id){
        log.info("----- vm 목록 불러오기");
        return itVnicService.getVmNics(id);
    }


}
