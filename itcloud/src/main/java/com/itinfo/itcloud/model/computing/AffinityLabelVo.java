package com.itinfo.itcloud.model.computing;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class AffinityLabelVo {
    private String id;
    private String name;

    private String clusterName;

    private List<HostVo> hostsLabel;    // 호스트 멤버
    private List<VmVo> vmsLabel;      // 가상머신 멤버

}
