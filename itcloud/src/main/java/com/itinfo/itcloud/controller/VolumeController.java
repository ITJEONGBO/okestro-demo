package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.service.ItVolumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class VolumeController {
    private final ItVolumeService itVolumeService;

    @GetMapping("/storage/volumes")
    public String volumes(Model model){
        return "storage/volumes";
    }

}
