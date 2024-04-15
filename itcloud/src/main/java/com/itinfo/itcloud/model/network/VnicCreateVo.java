package com.itinfo.itcloud.model.network;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ovirt.engine.sdk4.types.VnicPassThroughMode;

@Getter @Setter @Builder
public class VnicCreateVo {
    private String id;
    private String name;
    private String description;
//    private NetworkFilterVo nfVo;
    private String nfName;
    private VnicPassThroughMode passThrough;
    private boolean migration;
    private boolean portMirror;
//    사용자 정의 속성

    private String dcId;
    private String dcName;
    private String networkId;
    private String networkName;
}
