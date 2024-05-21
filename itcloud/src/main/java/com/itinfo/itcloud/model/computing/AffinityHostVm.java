package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.IdentifiedVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @Builder @ToString
public class AffinityHostVm {
    private String clusterId;
    private List<IdentifiedVo> hostList;
    private List<IdentifiedVo> vmList;

    private List<IdentifiedVo> hostLabel;
    private List<IdentifiedVo> vmLabel;
}
