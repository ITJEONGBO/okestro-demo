package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class HostCreateVo {
    private String id;
    private String clusterId;
    private String name;
    private String comment;
    private String description;
    private String status;
    private boolean hostEngine;
}
