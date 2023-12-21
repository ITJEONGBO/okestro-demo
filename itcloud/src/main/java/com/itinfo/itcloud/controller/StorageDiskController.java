package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.storage.DiskVO;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItStorageDiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class StorageDiskController {

    @Autowired
    private ItStorageDiskService storageDiskSerivce;


    public StorageDiskController(){}



}
