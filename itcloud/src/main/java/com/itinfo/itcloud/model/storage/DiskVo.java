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
    private String storageType;
    private String contentType;     //연결대상
    private String connection;

    private BigInteger virtualSize;
    private BigInteger actualSize;

    private String diskProfileId;
    private String diskProfileName;

    private String storageDomainId;
    private String storageDomainName;

}
