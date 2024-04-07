package com.itinfo.itcloud.model.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.ovirt.engine.sdk4.types.StorageDomainType;
import org.ovirt.engine.sdk4.types.StorageType;

@Getter @Setter @Builder @ToString
public class DomainCreateVo {
    private String name;
    private String description;
    private String comment;

    private String datacenterId;
    private String datacenterName;
    private StorageDomainType domainType;       // 도메인 기능
    private StorageType storageType;    // 스토리지 유형
    private String path; // 내보내기 경로

    private String logicalUnitId;

    private String hostId;
    private String hostName;

    // TODO
    // 사용자 정의 연결 매개변수 해야하는건가

}
