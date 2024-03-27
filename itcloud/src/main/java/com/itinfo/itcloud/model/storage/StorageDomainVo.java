package com.itinfo.itcloud.model.storage;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.ovirt.engine.sdk4.types.StorageDomainStatus;
import org.ovirt.engine.sdk4.types.StorageDomainType;

import java.math.BigInteger;
@Getter @Setter @ToString
public class StorageDomainVo {
    private String id;
    private String name;
    private String comment;
    private String description;
    private StorageDomainStatus status;

    private BigInteger availableSize;
    private BigInteger usedSize;
    private BigInteger commitedSize;
    private BigInteger diskSize;
    private BigInteger overCommit; // 오버 활당 비율 (availableSize
    private int imageCnt;   // 이미지 디스크사이즈

    private String nfsVersion;
    private int warning;    // 디스크공간부족 경고
    private String storagePath;     // 경로
    private String storageAddress;  // 경로
    private int blockSize;      // 디스크공간 동작차단

    private StorageDomainType domainType;       // 도메인 유형
    private String domainFormat;
    private String storageType;
    private boolean backup;

    private String datacenterId;
    private String datacenterName;


    // LINK: disks
    // datacenter, datacenters

}
