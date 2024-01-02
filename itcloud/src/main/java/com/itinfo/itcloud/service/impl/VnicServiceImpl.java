package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.computing.NicVo;
import com.itinfo.itcloud.model.computing.TemplateVo;
import com.itinfo.itcloud.model.computing.VmVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItVnicService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class VnicServiceImpl implements ItVnicService {

    @Autowired
    private AdminConnectionService adminConnectionService;

    @Override
    public List<VnicProfileVo> getVnics() {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<VnicProfileVo> vpVoList = new ArrayList<>();
        VnicProfileVo vpVo = null;

        List<VnicProfile> vnicProfileList =
                ((VnicProfilesService.ListResponse)systemService.vnicProfilesService().list().send()).profiles();

        for(VnicProfile vnicProfile : vnicProfileList){
            vpVo = new VnicProfileVo();

            vpVo.setId(vnicProfile.id());
            vpVo.setName(vnicProfile.name());
            vpVo.setPassThrough(String.valueOf(vnicProfile.passThrough().mode()));  // type 여러개
            vpVo.setPortMirroring(vnicProfile.portMirroring());

            vpVo.setNetworkId(vnicProfile.network().id());
            vpVo.setNetworkName( ((NetworkService.GetResponse)systemService.networksService().networkService(vnicProfile.network().id()).get().send()).network().name() );

            vpVo.setNetworkFilterId(vnicProfile.networkFilter().id());
            vpVo.setNetworkFilterName( ((NetworkFilterService.GetResponse)systemService.networkFiltersService().networkFilterService(vnicProfile.networkFilter().id()).get().send()).networkFilter().name() );

            vpVoList.add(vpVo);
        }
        return vpVoList;
    }

    @Override
    public List<VmVo> getVmNics(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<VmVo> vmVoList = new ArrayList<>();
        VmVo vmVo = null;

        List<Vm> vmList =
                ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();

        for(Vm vm : vmList){
            List<Nic> nicList =
                    ((VmNicsService.ListResponse)systemService.vmsService().vmService(vm.id()).nicsService().list().send()).nics();

            for(Nic nic : nicList){
                if(id.equals(nic.vnicProfile().id())){
                    vmVo = new VmVo();
                    vmVo.setId(vm.id());
                    vmVo.setName(vm.name());

                    vmVoList.add(vmVo);
                }
            }
        }
        return vmVoList;
    }

    @Override
    public List<TemplateVo> getTemplates(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<TemplateVo> tVoList = new ArrayList<>();
        TemplateVo tVo = null;

        List<Template> templateList =
                ((TemplatesService.ListResponse)systemService.templatesService().list().send()).templates();

        return null;
    }
}
