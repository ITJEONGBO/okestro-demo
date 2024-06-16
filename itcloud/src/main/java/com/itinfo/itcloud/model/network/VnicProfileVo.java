package com.itinfo.itcloud.model.network;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString @Builder
public class VnicProfileVo {
    private String id;
    private String name;
    private String passThrough;
    private boolean portMirroring;
    private String description;
    private String version;

    private String dcId;
    private String dcName;
    private String networkId;
    private String networkName;
    private String networkFilterName;
    
    private String nicName; // 가상머신 편집

    private boolean provider;
}
