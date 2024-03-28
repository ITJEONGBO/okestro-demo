package com.itinfo.itcloud.model.storage;

import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.HostVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ovirt.engine.sdk4.types.StorageType;

@Getter @Setter @Builder
public class LunVo {
    private int size;   // 크기(Gib)
    private String alias;
    private String description;
    private DataCenterVo dcVo;
    private HostVo hostVo;
    private StorageType storageType;

    //
}
