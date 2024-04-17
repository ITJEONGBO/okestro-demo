package com.itinfo.itcloud.model.network;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ovirt.engine.sdk4.types.TemplateStatus;

@Getter @Setter @Builder
public class NetworkTemplateVo {
    private String name;
//    private int version;
    private TemplateStatus status;
    private String clusterName;
    private String nicId;
    private String nicName;
}
