package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.ovirt.engine.sdk4.types.DataCenter;

@Getter @Builder @ToString
public class NetworkCreateVo {
    // name, data_center
    private String id;
    private String name;
//    private DataCenter datacenter;
    private String datacenterId;
    private String datacenterName;
    private String comment;
    private String description;

}
