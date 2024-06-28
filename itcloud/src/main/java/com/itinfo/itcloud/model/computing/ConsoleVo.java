package com.itinfo.itcloud.model.computing;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @ToString
public class ConsoleVo {
    private String hostAddress;
    private String hostPort;

    private String address;
    private String port;
    private String tlsPort;
    private String type;

    private String vmName;
    private String password;
}
