package com.itinfo.itcloud.model.network;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter @Setter
public class NetworkHostVo {
    // Host
    private String id;
    private String name;
    private String clusterName;
    private String datacenterName;


    // network
    private String status;
    private String asynchronism;        // 비동기
    private String networkDevice;

    // nic
    private int rxSpeed;        // mbps
    private int rxTotalSpeed;   // byte
    private int txSpeed;        // mbps
    private int txTotalSpeed;   // byte
    private BigInteger speed;          // mbps


}
