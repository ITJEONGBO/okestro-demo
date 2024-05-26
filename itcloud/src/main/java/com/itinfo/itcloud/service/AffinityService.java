package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.computing.AffinityLabelVo;
import com.itinfo.itcloud.model.computing.HostVo;
import com.itinfo.itcloud.model.computing.VmVo;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.types.Host;
import org.ovirt.engine.sdk4.types.Vm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AffinityService {
    List<IdentifiedVo> setHostList(List<Host> hostList, String clusterId);
    List<IdentifiedVo> setVmList(List<Vm> vmList, String clusterId);
    List<IdentifiedVo> setLabel(SystemService system);
    List<AffinityLabelVo> getLabelName(SystemService system, String alId);
    List<HostVo> getAgHostList(SystemService system, String clusterId, String agId);
    List<VmVo> getAgVmList(SystemService system, String clusterId, String agId);
    List<HostVo> getHostLabelMember(SystemService system, String alid);
    List<VmVo> getVmLabelMember(SystemService system, String alid);

    // cluster label, group
    // host label
    // vm label, group
}
