package com.itinfo.itcloud.VO;

import lombok.Getter;
import lombok.Setter;

// 시스템 속성 VO
@Getter @Setter
public class SystemPropertiesVO {
    private String id;
    private String password;

    private String ip;
    private int login_limit;

    private String vnc_ip;
    private String vnc_port;

    private int cpu_threshold;
    private int memory_threshold;

//    private String grafanaUri;
//    private String deepLearningUri;
//    private boolean symphonyPowerControll;
}