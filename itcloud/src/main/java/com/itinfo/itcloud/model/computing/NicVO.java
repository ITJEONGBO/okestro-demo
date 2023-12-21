package com.itinfo.itcloud.model.computing;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter @Setter
public class NicVO {
    // https://192.168.0.70/ovirt-engine/api/vnicprofiles

    private String id;       // nic id
    private String name;     // nic 이름
    
    private String networkId;
    private String networkName;

    private String ipv4;
    private String ipv6;
    private String macAddress;
    private BigInteger speed;

    // vm
    private String vmId;
    private String vmName;

    private String profileName;
    private String qosName;

    private String type;            // 유형
    private boolean linkStatus;     // 연결상태 up&down
    private boolean plugged;        // 카드상태  connect&unconnect
    private boolean portMirror;

    private String historyDatetime;

}
