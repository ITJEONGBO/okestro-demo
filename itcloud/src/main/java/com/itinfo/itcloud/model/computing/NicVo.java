package com.itinfo.itcloud.model.computing;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter @Setter
public class NicVo {
    private String id;
    private String name;
    private String macAddress;

    private int rxSpeed;        // mbps
    private int rxTotalSpeed;   // byte
    private int txSpeed;        // mbps
    private int txTotalSpeed;   // byte
    private BigInteger speed;          // mbps
    private int stop;

    // 가상머신 nic
    private String networkName;
    private String ipv4;
    private String ipv6;

    // 일반
    private boolean plugged;
    private String profileName;
    private String qosName;
    private boolean linkStatus;
    private String type;
    private BigInteger speed2;
    private String portMirroring;
    private String guestInterface;

    // 통계
//    private int rxSpeed;        // mbps
//    private int rxTotalSpeed;   // byte
//    private int txSpeed;        // mbps
//    private int txTotalSpeed;   // byte
//    private int stop;


    // 논리 네트워크: 관리되지 않음, VLAN, 네트워크명, ipv4주소, ipv6주소
    // 슬레이브: 이름, MAC, 속도, rx속도, tx속도, 총rx, 총tx, 중단
}
