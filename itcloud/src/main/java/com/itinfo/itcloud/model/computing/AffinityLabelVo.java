package com.itinfo.itcloud.model.computing;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Builder
public class AffinityLabelVo {
    private String id;
    private String name;
    private String clusterName;

    private List<HostVo> hosts;     // 호스트 멤버
    private List<VmVo> vms;     // 가상머신 멤버(화면표시)
}
