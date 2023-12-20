package com.itinfo.itcloud.model.storage;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter @Setter
public class DomainVO {

    private String id;
    private String name;
    private String comment;
    private String description;
    private String status;

    private String domainType;
    private String storageType;
    private String format;
    private String datacenterStatus;

    // 데이터유형 고민
    private BigInteger diskSize;           // 크기
    private BigInteger availableDiskSize;  // 사용 가능
    private BigInteger usedDiskSize;       // 사용됨
    private BigInteger assignedDiskSize;   // 할당됨
//    private int percent;          // 오버할당 비율

    private int imageCnt;       // 이미지
    private String path;        // 경로
    private String nfsVer;      // nfs 버전

}
