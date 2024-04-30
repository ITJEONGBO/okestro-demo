package com.itinfo.itcloud.model.computing;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @ToString
public class VmVnicVo {
    private String id;
    private String name;
    private String networkName;
    private boolean externalNetwork;
}
