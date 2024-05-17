package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.network.NetworkFilterParameterVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.ovirt.engine.sdk4.types.NicStatus;

import java.math.BigInteger;
import java.util.List;

@Getter @Setter @Builder @ToString
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
    private boolean plugged;    // 연결됨 t(up)/f(down)
    private boolean linkStatus; // 링크상태
    private boolean synced; // 동기화
    private String interfaces;  // 유형  NicInterface
    private BigInteger speed2;
    private String guestInterface;

    // 프로파일, pass_throught, protmirroring
    private VnicProfileVo vnicProfileVo;

    private List<NetworkFilterParameterVo> nfVoList;
}
