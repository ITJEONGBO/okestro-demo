package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.ovirt.engine.sdk4.types.NetworkUsage;

@Getter @Builder @ToString
public class NetworkCreateVo {
    // name, data_center
    private String datacenterId;
    private String datacenterName;

    private String id;
    private String name;
    private String description;
    private String comment;
    private String label;   // 네트워크 레이블

    private Integer vlan;   // vlan 태그 (태그 자체는 활성화를 해야 입력란이 생김)
    private int mtu;
    private boolean usageVm;  // 기본이 체크된 상태
    private boolean portIsolation;
    private boolean stp;
//    private String vdsm;
}
