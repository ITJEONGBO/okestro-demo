package com.itinfo.itcloud.model.storage;

import com.itinfo.itcloud.model.computing.DataCenterVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class BlockVo {
    private int size;   // 크기(Gib)
    private String alias;
    private String description;
    private DataCenterVo dcVo;
    private DomainVo domainVo;
}
