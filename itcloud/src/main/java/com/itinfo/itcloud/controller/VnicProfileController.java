package com.itinfo.itcloud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VnicProfileController {

    @GetMapping("/network/vnicProfiles")
    public String vnicProfiles(){

        return "/network/vnicProfiles";
    }

}
