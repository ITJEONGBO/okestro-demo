package com.itinfo.itcloud.model.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ovirt.engine.sdk4.types.StorageType;

@Getter @Setter @Builder
public class LunCreateVo {
    private String alias;    // 별칭
    private String description;

    private String hostId;
    private StorageType storageType;

    private String address;
    private int port;
    private String target;

    private boolean share;  // 공유가능
}
