package com.itinfo.itcloud.model.computing;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @ToString
public class CpuProfileVo {
    private String id;
    private String name;
    private String description;
    private String qosName;
}
