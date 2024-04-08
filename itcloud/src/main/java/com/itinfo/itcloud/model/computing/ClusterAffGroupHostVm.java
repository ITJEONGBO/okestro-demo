package com.itinfo.itcloud.model.computing;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter @Builder
public class ClusterAffGroupHostVm {
    private String clusterId;
    private List<HostVo> hostList;
    private List<VmVo> vmList;
}
