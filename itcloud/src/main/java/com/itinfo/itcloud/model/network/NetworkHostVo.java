package com.itinfo.itcloud.model.network;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter @Setter
public class NetworkHostVo {
    // Host
    private String hostId;
    private String hostName;
    private String hostStatus;
    private String clusterName;
    private String datacenterName;


    // nic
    private String networkStatus;
    private String asynchronism;        // 비동기
    private String networkDevice;

    // nic
    private String rxSpeed;        // mbps
    private String rxTotalSpeed;   // byte
    private String txSpeed;        // mbps
    private String txTotalSpeed;   // byte
    private BigInteger speed;          // mbps


}
