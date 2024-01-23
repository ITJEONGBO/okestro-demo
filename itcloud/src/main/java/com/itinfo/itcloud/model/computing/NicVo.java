package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.network.VnicProfileVo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter @Setter
public class NicVo {
    private String id;
    private String name;
    private String macAddress;
    private String status;

    private String rxSpeed;        // mbps
    private String txSpeed;        // mbps
    private String rxTotalSpeed;   // byte
    private String txTotalSpeed;   // byte
    private String speed;    // mbps
    private String stop;

    // 가상머신 nic
    private String networkName;
    private String ipv4;
    private String ipv6;
    private String vLan;

    // 일반
    private boolean plugged;
    private boolean linkStatus;
    private String type;
    private BigInteger speed2;
    private String guestInterface;

    // 프로파일, pass_throught, protmirroring
    private VnicProfileVo vnicProfileVo;


    // 논리 네트워크: 관리되지 않음, VLAN, 네트워크명, ipv4주소, ipv6주소
    // 슬레이브: 이름, MAC, 속도, rx속도, tx속도, 총rx, 총tx, 중단
}
