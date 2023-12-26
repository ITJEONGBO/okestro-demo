package com.itinfo.itcloud.model.computing;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HostDeviceVo {
    private String name;
    private String capability;
    private String vendor;
    private String product;
    private String driver;
}
