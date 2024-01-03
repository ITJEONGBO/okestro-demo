package com.itinfo.itcloud.model.network;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NetworkClusterVo {
    // cluster
    private String id;
    private String name;
    private String description;
    private String version;

    // network
    private boolean connected;
    private String status;
    private boolean required;

    private NetworkUsageVo networkUsageVo;
}
