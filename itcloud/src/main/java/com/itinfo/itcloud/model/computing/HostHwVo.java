package com.itinfo.itcloud.model.computing;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class HostHwVo {
    private String manufacturer;
    private String family;      // 제품군
    private String productName;
    private String serialNum;
    private String uuid;
    private String hwVersion;

    private String cpuName;
    private String cpuType;
    private int cpuSocket;
    private int coreThread;
    private int coreSocket;
    // cpu소켓
    // TSC 주파수

}
