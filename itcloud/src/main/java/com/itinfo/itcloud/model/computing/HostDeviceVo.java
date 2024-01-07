package com.itinfo.itcloud.model.computing;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HostDeviceVo {
    private String name;
    private String capability;
    private String vendorId;
    private String vendorName;
    private String productId;
    private String productName;
    private String driver;
}
