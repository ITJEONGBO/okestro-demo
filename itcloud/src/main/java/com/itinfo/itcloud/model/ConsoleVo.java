package com.itinfo.itcloud.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @Builder
public class ConsoleVo {
    private String vmId;
    private String protocol;
    private String address;
    private String port;
    private String tlsPort;
    private String passwd;
    private String hostAddress;
    private String hostPort;
}
