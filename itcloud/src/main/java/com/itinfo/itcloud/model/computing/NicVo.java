package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.network.VnicProfileVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ovirt.engine.sdk4.types.NicStatus;

import java.math.BigInteger;

@Getter @Setter @Builder
public class NicVo {
    private String id;
    private String name;
    private String macAddress;
    private NicStatus status;

    private BigInteger rxSpeed;        // mbps
    private BigInteger txSpeed;        // mbps
    private BigInteger rxTotalSpeed;   // byte
    private BigInteger txTotalSpeed;   // byte
    private BigInteger speed;    // mbps
    private BigInteger stop;

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
}
