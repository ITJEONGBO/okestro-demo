package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.computing.AffinityGroupVo;
import com.itinfo.itcloud.model.computing.AffinityLabelVo;
import com.itinfo.itcloud.model.create.AffinityGroupCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItAffinityService {
    List<IdentifiedVo> getHostList(String clusterId);
    List<IdentifiedVo> getVmList(String clusterId);
    List<IdentifiedVo> getLabel();
    List<IdentifiedVo> getLabelName(String alId);

    List<AffinityGroupVo> getAffinitygroup(String id, String type);
    CommonVo<Boolean> addAffinitygroup(String id,  String type, AffinityGroupCreateVo agVo);
    AffinityGroupCreateVo setEditAffinitygroup(String id, String type, String agId);
    CommonVo<Boolean> editAffinitygroup(String id, AffinityGroupCreateVo agVo);
    CommonVo<Boolean> deleteAffinitygroup(String id, String type, String agId);


    List<AffinityLabelVo> getAffinitylabel(/*String id, String type*/);




    // cluster  : label, group      api-group
    // host     : label             api-affinitylabels
    // vm       : label, group      api-affinitylabels
}
