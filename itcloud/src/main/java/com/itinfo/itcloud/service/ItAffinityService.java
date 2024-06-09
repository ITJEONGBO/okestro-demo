package com.itinfo.itcloud.service;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.computing.AffinityGroupVo;
import com.itinfo.itcloud.model.computing.AffinityLabelVo;
import com.itinfo.itcloud.model.create.AffinityGroupCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import org.ovirt.engine.sdk4.services.SystemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItAffinityService {
    // 선호도 생성을 위한 호스트 목록 (클러스트ID에 의존)
    List<IdentifiedVo> getHostList(String clusterId);
    // 선호도 생성을 위한 가상머신 목록 (클러스트ID에 의존)
    List<IdentifiedVo> getVmList(String clusterId);
    // 선호도 레이블 출력은 데이터센터 상관없이 나옴
    List<IdentifiedVo> getLabelList();


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
