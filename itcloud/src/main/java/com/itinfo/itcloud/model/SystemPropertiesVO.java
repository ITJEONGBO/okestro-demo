package com.itinfo.itcloud.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.ConnectionBuilder;

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

    @NonNull
    public Connection getConnection(){
        return ConnectionBuilder.connection()
                .url("https://" + getIp() + "/ovirt-engine/api")
                .user(getId()+ "@internal")
                .password(getPassword())
                .insecure(true)
                .timeout(20000).build();
    }

}