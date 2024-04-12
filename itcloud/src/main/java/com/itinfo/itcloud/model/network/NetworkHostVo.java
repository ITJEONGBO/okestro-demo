package com.itinfo.itcloud.model.network;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ovirt.engine.sdk4.types.HostStatus;
import org.ovirt.engine.sdk4.types.NicStatus;

import java.math.BigInteger;

@Getter @Setter @Builder
public class NetworkHostVo {
    // Host
    private String hostId;
    private String hostName;
    private HostStatus hostStatus;
    private String clusterName;
    private String datacenterName;


    // nic
    private NicStatus networkStatus;
    private String asynchronism;        // 비동기
    private String networkDevice;

    // nic
    private BigInteger rxSpeed;        // mbps
    private BigInteger txSpeed;        // mbps
    private BigInteger rxTotalSpeed;   // byte
    private BigInteger txTotalSpeed;   // byte
    private BigInteger speed;          // mbps


}
