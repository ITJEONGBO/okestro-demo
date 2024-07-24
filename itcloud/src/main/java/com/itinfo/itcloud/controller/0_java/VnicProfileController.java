/*
package com.itinfo.itcloud.controller.network;

import com.itinfo.itcloud.model.MenuVo;
import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import com.itinfo.itcloud.service.ItMenuService;
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
    private final ItMenuService menu;

    @GetMapping("/network/vnicProfiles")
    public String vnicProfiles(Model model){
        List<VnicProfileVo> vnics = itVnicService.getVnics();
        model.addAttribute("vnics", vnics);

        MenuVo m = menu.getMenu();
        model.addAttribute("m", m);
        log.info("---vnics");
        return "/network/vnicProfiles";
    }

    @GetMapping("/network/vnicProfile-vm")
    public String vnicVm(String id, Model model){
        List<VmVo> nicVm = itVnicService.getVmNics(id);
        model.addAttribute("nicVm", nicVm);
        model.addAttribute("id", id);
        model.addAttribute("name", itVnicService.getName(id));

        MenuVo m = menu.getMenu();
        model.addAttribute("m", m);
        log.info("---nicVm");
        return "/network/vnicProfile-vm";
    }

    @GetMapping("/network/vnicProfile-template")
    public String template(String id, Model model){
        List<TemplateVo> template = itVnicService.getTemplates(id);
        model.addAttribute("template", template);
        model.addAttribute("id", id);
        model.addAttribute("name", itVnicService.getName(id));

        MenuVo m = menu.getMenu();
        model.addAttribute("m", m);
        log.info("---nicVm");
        return "/network/vnicProfile-template";
    }

    @GetMapping("/network/vnicProfile-permission")
    public String permission(String id, Model model){
        List<PermissionVo> permission = itVnicService.getPermission(id);
        model.addAttribute("permission", permission);
        model.addAttribute("id", id);
        model.addAttribute("name", itVnicService.getName(id));

        MenuVo m = menu.getMenu();
        model.addAttribute("m", m);
        log.info("---permission");
        return "/network/vnicProfile-permission";
    }




    //region: ResponseBody
    @GetMapping("/network/vnicProfilesStatus")
    @ResponseBody
    public List<VnicProfileVo> vnics(){
        log.info("----- VnicProfile 목록 불러오기");
        return itVnicService.getVnics();
    }

    @GetMapping("/network/vnicProfiles/vmStatus")
    @ResponseBody
    public List<VmVo> vm(String id){
        log.info("----- vm 목록 불러오기");
        return itVnicService.getVmNics(id);
    }

    @GetMapping("/network/vnicProfiles/templateStatus")
    @ResponseBody
    public List<TemplateVo> template(String id){
        log.info("----- template 목록 불러오기");
        return itVnicService.getTemplates(id);
    }

    @GetMapping("/network/vnicProfiles/permissionStatus")
    @ResponseBody
    public List<PermissionVo> permission(String id) {
        log.info("----- permission불러오기");
        return itVnicService.getPermission(id);
    }
    //endregion


}
*/