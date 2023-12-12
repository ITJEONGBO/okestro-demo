package com.itinfo.itcloud.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Getter @Setter
public class SDomainVO {
    private String id;
    private String name;
    private String description;
    private String comment;
    private String type;
    private boolean format;
    private String status;
    private String storageAddress;
    private String storagePath;
    private String storageType;
    private String storageFormat;
    private String storageDomainInfo;
    private BigInteger diskFree;
    private BigInteger diskUsed;
    private String diskProfileId;
    private String diskProfileName;
    List<List<String>> storageDomainUsages;
    /*List<DiskVo> diskVoList;
    List<DiskVo> diskSnapshotVoList;
    List<ImageFileVo> imageFileList;*/

}
