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
    private String status;

    // vnic
    private boolean vnicStatus;
    private String vnicName;
    private String vnicRx;
    private String vnicTx;
    private String rxTotalSpeed;
    private String txTotalSpeed;
    private String description;
}
