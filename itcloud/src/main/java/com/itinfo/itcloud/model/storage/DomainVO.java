package com.itinfo.itcloud.model.storage;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DomainVO {

    private String storageDomainId;
    private String storageDomainName;
    private String comment;
    private String description;
    private String status;

    private String domainType;
    private String storageType;
    private String format;
    private String datacenterStatus;

    // 데이터유형 고민
    private int diskSize;           // 크기
    private int availableDiskSize;  // 사용 가능
    private int usedDiskSize;       // 사용됨
    private int assignedDiskSize;   // 할당됨
//    private int percent;          // 오버할당 비율
    
    private int imageCnt;       // 이미지
    private String path;        // 경로
    private String nfsVer;      // nfs 버전

}
