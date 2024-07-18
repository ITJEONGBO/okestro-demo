package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.IdentifiedVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @Builder @ToString
public class AffinityLabelVo {
    private String id;
    private String name;
    private String clusterName;

    private AffinityGroupMember members;
}
