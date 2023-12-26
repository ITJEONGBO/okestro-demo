package com.itinfo.itcloud.model.computing;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HostSwVo {
    private String osVersion;
    private String osInfo;
    private String kernalVersion;
    private String kvmVersion;
    private String libvirtVersion;
    private String vdsmVersion;
    private String spiceVersion;
    private String glustersfsVersion;
    private String cephVersion;
    private String openVswitchVersion;
    private String nmstateVersion;

    // 커널기능,vnc암호화,ovn configured
}
