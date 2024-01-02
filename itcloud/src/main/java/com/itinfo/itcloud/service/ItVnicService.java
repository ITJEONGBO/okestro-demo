package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.computing.NicVo;
import com.itinfo.itcloud.model.computing.TemplateVo;
import com.itinfo.itcloud.model.computing.VmVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItVnicService {
    List<VnicProfileVo> getVnics();
    List<VmVo> getVmNics(String id);
    List<TemplateVo> getTemplates(String id);
//    권한
}
