package com.itinfo.itcloud.model.computing;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HostHwVO {

    private String manufacturer;
    private String family;      // 제품군
    private String productName;
    private String serialNum;
    private String uuid;
    private String hwVersion;

    private String cpuName;
    private String cpuType;
    private int socketCore;
    private int coreThread;
    private int cpuSocket;

}
