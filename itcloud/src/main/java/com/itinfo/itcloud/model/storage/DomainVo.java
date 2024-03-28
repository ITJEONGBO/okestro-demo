package com.itinfo.itcloud.model.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.ovirt.engine.sdk4.types.StorageDomainStatus;
import org.ovirt.engine.sdk4.types.StorageDomainType;
import org.ovirt.engine.sdk4.types.StorageFormat;
import org.ovirt.engine.sdk4.types.StorageType;

import java.math.BigInteger;
@Getter @Setter @ToString @Builder
public class DomainVo {
    private String id;
    private String name;
    private String comment;
    private String description;

    private StorageDomainStatus status;
    private StorageDomainType domainType;       // 도메인 유형
    private Boolean domainTypeMaster;

    private BigInteger availableSize; // 여유공간
    private BigInteger usedSize; // 사용된 공간
    private BigInteger commitedSize;
    private BigInteger diskSize; // 전체공간
    private BigInteger overCommit; // 오버 활당 비율 (availableSize
    private int imageCnt;   // 이미지 디스크사이즈

    private String nfsVersion;
    private int warning;    // 디스크공간부족 경고
    private String storagePath;     // 경로
    private String storageAddress;  // 경로
    private int blockSize;      // 디스크공간 동작차단

    private StorageFormat format;
    private StorageType storageType;
    private boolean backup;

    private String datacenterId;
    private String datacenterName;


    // LINK: disks
    // datacenter, datacenters

}
