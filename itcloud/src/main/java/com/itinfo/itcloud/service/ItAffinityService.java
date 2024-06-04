package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.computing.AffinityGroupVo;
import com.itinfo.itcloud.model.computing.AffinityLabelVo;
import com.itinfo.itcloud.model.computing.HostVo;
import com.itinfo.itcloud.model.computing.VmVo;
import com.itinfo.itcloud.model.create.AffinityGroupCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import org.ovirt.engine.sdk4.services.SystemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItAffinityService {
    List<IdentifiedVo> getHostList(String clusterId);
    List<IdentifiedVo> getVmList(String clusterId);

    List<IdentifiedVo> getLabel();

    List<AffinityGroupVo> getAffinitygroup(String id, String type);
    CommonVo<Boolean> addAffinitygroup(String id,  String type, AffinityGroupCreateVo agVo);

    AffinityGroupCreateVo setEditAffinitygroup(String id, String type, String agId);


    List<AffinityLabelVo> getAffinitylabel(String id);

    List<AffinityLabelVo> getLabelName(String alId);

    List<HostVo> getAgHostList(SystemService system, String clusterId, String agId);
    List<VmVo> getAgVmList(SystemService system, String clusterId, String agId);

    List<HostVo> getHostLabelMember(SystemService system, String alid);
    List<VmVo> getVmLabelMember(SystemService system, String alid);

    // cluster label, group
    // host label
    // vm label, group
}
