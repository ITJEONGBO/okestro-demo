package com.itinfo.itcloud.model.computing;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class HostDeviceVo {
    private String name;
    private String capability;
    private String vendorId;
    private String vendorName;
    private String productId;
    private String productName;
    private String driver;
}
