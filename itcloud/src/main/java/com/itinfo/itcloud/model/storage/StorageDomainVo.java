package com.itinfo.itcloud.model.storage;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
@Getter @Setter
public class StorageDomainVo {
    private String id;
    private String name;
    private String description;
    private String status;
    private BigInteger availableSize;
    private BigInteger usedSize;
    private BigInteger diskSize;
    // 오버할당비율
    private int imageCnt;   // 이미지 디스크사이즈
    // nfs 버전
    private int warning;    // 디스크공간부족 경고
    private String storagePath;     // 경로
    private String storageAddress;  // 경로
    private int blockSize;      // 디스크공간 동작차단

    private String domainType;       // 도메인 유형
    private boolean backup;


    private String datacenterName;

    // LINK: disks
    // datacenter, datacenters

}
