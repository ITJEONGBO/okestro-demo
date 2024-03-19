package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.ovirt.engine.sdk4.types.QuotaModeType;

@Getter @Setter @ToString @Builder
public class DataCenterCreateVo {
    private String id;
    private String name;
    private String comment;
    private String description;
    private QuotaModeType quotaMode;     // QuotaMode: 비활성화됨, 감사, 강제적용
    private boolean storageType;         // local
    private String version;
}