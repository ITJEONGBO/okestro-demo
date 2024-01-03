package com.itinfo.itcloud.model.network;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NetworkUsageVo {
    private boolean vm;
    private boolean management;
    private boolean display; //출력
    private boolean migration;
    private boolean gluster;
    private boolean defaultRoute;
}
