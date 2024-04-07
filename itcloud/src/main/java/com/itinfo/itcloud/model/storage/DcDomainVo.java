package com.itinfo.itcloud.model.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @Builder @ToString
public class DcDomainVo {
    private String datacenterId;
    private String datacenterName;

    private List<DomainVo> domainList;
}
