package com.itinfo.itcloud.model.computing;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VmNicVO {
    // https://192.168.0.70/ovirt-engine/api/vms/aa8a3c93-0ce3-4d6e-9ff7-aa4267cc74e8/nics
    // https://192.168.0.70/ovirt-engine/api/vnicprofiles

    private String nicId;       // nic id
    private String nicName;     // nic 이름
    
    private String vmsId;
    private String vmsName;

    private String networkName;
    private String profileName;

    private String ipv4;
    private String ipv6;
    private String macAddress;

    private String type;            // 유형
    private boolean linkStatus;     // 연결상태 up&down
    private boolean plugged;        // 카드상태  connect&unconnect

}
