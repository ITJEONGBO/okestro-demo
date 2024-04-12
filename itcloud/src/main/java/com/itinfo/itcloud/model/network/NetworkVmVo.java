package com.itinfo.itcloud.model.network;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ovirt.engine.sdk4.types.VmStatus;

import java.math.BigInteger;

@Getter @Setter @Builder
public class NetworkVmVo {
    private String vmId;
    private String vmName;
    private String clusterId;
    private String clusterName;
    private String ipv4;
    private String ipv6;
    private String fqdn;
    private VmStatus status;

    // vnic
    private boolean vnicStatus;
    private String vnicName;
    private BigInteger vnicRx;
    private BigInteger vnicTx;
    private BigInteger rxTotalSpeed;
    private BigInteger txTotalSpeed;
    private String description;
}
