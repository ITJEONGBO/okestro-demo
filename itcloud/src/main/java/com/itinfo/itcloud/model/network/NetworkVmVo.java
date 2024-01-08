package com.itinfo.itcloud.model.network;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter @Setter
public class NetworkVmVo {
    private String vmId;
    private String vmName;
    private String clusterId;
    private String clusterName;
    private String ipv4;
    private String ipv6;
    private String fqdn;

    // vnic
    private boolean vnicStatus;
    private String vnicName;
    private BigInteger vNicRx;
    private BigInteger vNicTx;
    private BigInteger totalRx;
    private BigInteger totalTx;
    private String description;
}
