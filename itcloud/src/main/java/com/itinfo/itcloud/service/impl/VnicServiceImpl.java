package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.computing.TemplateVo;
import com.itinfo.itcloud.model.computing.VmVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItVnicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class VnicServiceImpl implements ItVnicService {

    @Autowired
    private AdminConnectionService adminConnectionService;

    @Override
    public List<VnicProfileVo> getVnics() {
        return null;
    }

    @Override
    public List<VmVo> getVms(String id) {
        return null;
    }

    @Override
    public List<TemplateVo> getTemplates(String id) {
        return null;
    }
}
