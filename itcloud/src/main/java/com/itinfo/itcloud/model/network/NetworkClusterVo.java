package com.itinfo.itcloud.model.network;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Builder
public class NetworkClusterVo {
    // cluster
    private String id;
    private String name;
    private String description;
    private String version;

    // network
    private boolean connected;
    private boolean required;
    private String status;

    private NetworkUsageVo networkUsageVo;
}
