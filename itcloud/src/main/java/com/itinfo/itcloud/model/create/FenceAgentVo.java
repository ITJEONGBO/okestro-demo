package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class FenceAgentVo {
    private String address;
    private String userName;
    private String password;
    private String type;
    private int port;
    private String option;
}
