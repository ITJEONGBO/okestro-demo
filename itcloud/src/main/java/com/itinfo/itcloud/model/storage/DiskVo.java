package com.itinfo.itcloud.model.storage;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter @Setter
public class DiskVo {
    private String id;
    private String name;
    private String alias;
    private String description;

    private String status;
    private boolean format;  // 삭제 후 초기화

    private boolean shareable;
    private String storageType;     // 유형
    private String contentType;     // 연결대상
    private String connection;

    private BigInteger virtualSize; // 가상크기
    private BigInteger actualSize;  // 실제 크기

    private String diskProfileId;
    private String diskProfileName;

    private String storageDomainId;
    private String storageDomainName;
}
