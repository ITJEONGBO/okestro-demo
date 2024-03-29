package com.itinfo.itcloud.model.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter @Builder
public class DiskDcVo {
    private String dcId;
    private String dcName;

    private List<DomainVo> domainVoList;
    private List<DiskProfileVo> dpVoList;
}
