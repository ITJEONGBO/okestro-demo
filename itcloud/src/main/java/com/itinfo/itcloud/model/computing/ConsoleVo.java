package com.itinfo.itcloud.model.computing;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.ovirt.engine.sdk4.types.GraphicsType;

@Getter @Builder @ToString
public class ConsoleVo {
    private String hostAddress;
    private String hostPort;

    private String address;
    private String port;
    private String tlsPort;
    private GraphicsType type;

//    private String vmName;
    private String password;
}
