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
    private int rxSpeed;        // mbps
    private int rxTotalSpeed;   // byte
    private int txSpeed;        // mbps
    private int txTotalSpeed;   // byte
    private BigInteger speed;          // mbps


}
