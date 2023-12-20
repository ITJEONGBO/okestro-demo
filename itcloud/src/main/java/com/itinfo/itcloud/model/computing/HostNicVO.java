package com.itinfo.itcloud.model.computing;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter @Setter
public class HostNicVO {
    private String id;
    private String name;
    private String macAddress;

    private BigInteger speed;
    private String historyDatetime;

    // 논리 네트워크
    private String networkId;

    // 슬레이브
}
