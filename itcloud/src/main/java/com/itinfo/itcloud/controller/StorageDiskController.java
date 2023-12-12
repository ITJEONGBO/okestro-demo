package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.storage.DiskVO;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.StorageDiskSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class StorageDiskController {

    @Autowired
    private StorageDiskSerivce storageDiskSerivce;

    @Autowired
    private AdminConnectionService adminConnectionService;

    public StorageDiskController(){}

    @GetMapping ("/storage/disks")
    public String disks(Model model){
        List<DiskVO> diskVOList = this.storageDiskSerivce.showDiskList();
        model.addAttribute("diskVOList", diskVOList);
        return "storage/disks";
    }

    @GetMapping("/diskStatus")
    @ResponseBody
    public List<DiskVO> disk(){
        List<DiskVO> diskList = null;
        try{
            diskList = storageDiskSerivce.showDiskList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return diskList;
    }


}
