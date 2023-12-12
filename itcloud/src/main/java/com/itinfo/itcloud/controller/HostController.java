package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.service.HostdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class HostController {

    @Autowired
    private HostdService hostdService;

    public HostController(){}

    /*@GetMapping("/computing/hosts")
    public String hosts(Model model){
        List<HostDetailVO> hostVOList = this.hostdService.retrieveHosts();
        model.addAttribute("hostVOList", hostVOList);
        return "compute/hosts";
    }

    @GetMapping("/hostStatus")
    @ResponseBody
    public List<HostDetailVO> ho(){
        List<HostDetailVO> list = null;
        try{
            list = hostdService.retrieveHosts();
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }*/



}
