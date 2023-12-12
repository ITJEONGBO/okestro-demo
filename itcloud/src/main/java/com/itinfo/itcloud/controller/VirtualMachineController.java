package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.service.VirtualMachineSerivce;
import com.itinfo.itcloud.model.computing.VmVO;
import com.itinfo.itcloud.service.SystemPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class VirtualMachineController {

    @Autowired
    private VirtualMachineSerivce virtualMachineSerivce;

    @Autowired
    private SystemPropertiesService systemPropertiesService;

    public VirtualMachineController(){}


    @GetMapping("/computing/vms")
    public String vmList(Model model){
        List<VmVO> vmVOList = virtualMachineSerivce.showVmList();
        model.addAttribute("vmList", vmVOList);
        return "compute/vms";
    }

    @GetMapping("/vmsList")
    @ResponseBody
    public List<VmVO> vms(){
        List<VmVO> vmsList = null;
        try{
            long start = System.currentTimeMillis();

            vmsList = virtualMachineSerivce.showVmList();

            long end = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
            System.out.println("수행시간(ms): " + (end-start));
        }catch (Exception e){
            e.printStackTrace();
        }
        return vmsList;
    }

    /*@GetMapping("/vmList")
    @ResponseBody
    public List<VmVO> vmli(){
        List<VmVO> vmList = null;
        try{
            long start = System.currentTimeMillis();

            vmList = virtualMachineSerivce.showList();

            long end = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
            System.out.println("수행시간(ms): " + (end-start));
        }catch (Exception e){
            e.printStackTrace();
        }
        return vmList;
    }*/


    /*@GetMapping("/nic")
    @ResponseBody
    public VmNicVO vmnic(){
        VmNicVO vmsList = null;
        try{
            vmsList = virtualMachineSerivce.retrieveVmNic("aa8a3c93-0ce3-4d6e-9ff7-aa4267cc74e8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return vmsList;
    }*/



}
