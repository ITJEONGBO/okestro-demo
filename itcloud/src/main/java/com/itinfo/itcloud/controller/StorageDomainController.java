package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.service.ItStorageDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class StorageDomainController {

    @Autowired
    private ItStorageDomainService itStorageDomainService;

    public StorageDomainController(){}

    /*@GetMapping("/storage/domains")
    public String domain(Model model){
        List<SDomainVO> SDomainVOList = this.domainService.retrieveStorageDomainList();
        model.addAttribute("domainVOList", SDomainVOList);
        return "storage/domains";
    }

    @GetMapping("/domainStatus")
    @ResponseBody
    public List<SDomainVO> dom(*//*String status, String domainType*//*){
        List<SDomainVO> list = null;
        try{
            list = domainService.retrieveStorageDomainList(*//*status, domainType*//*);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }*/



}
