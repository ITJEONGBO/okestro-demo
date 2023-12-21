package com.itinfo.itcloud.model.storage;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DiskVO {

    private String id;
    private String name;
    private String description;

    private String status;
    private String format;

    private String diskProfileId;
    private String contentType;     //연결대상

    private String virtualSize;
    private String actualSize;

    private boolean shareable;
    private String storageType;

    private String storageDomainId;
    private String storageDomainName;

    private String connection;


}