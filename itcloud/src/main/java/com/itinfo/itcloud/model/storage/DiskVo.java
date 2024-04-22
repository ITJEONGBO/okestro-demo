package com.itinfo.itcloud.model.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ovirt.engine.sdk4.types.DiskInterface;
import org.ovirt.engine.sdk4.types.DiskStatus;
import org.ovirt.engine.sdk4.types.DiskStorageType;

import java.math.BigInteger;

@Getter @Setter @Builder
public class DiskVo {
    private String id;
    private String name;
    private String alias;   // 별칭
    private String description;

    private DiskStatus status;
    private DiskInterface diskInterface;   // 인터페이스
    private boolean sparse;// 할당정책  씬true, 사전할당false
    private boolean format;  // 삭제 후 초기화

    private boolean shareable;
    private DiskStorageType storageType;     // 유형
    private String contentType;     // 연결대상
    private String connection;

    private BigInteger virtualSize; // 가상크기
    private BigInteger actualSize;  // 실제 크기

    private String diskProfileId;
    private String diskProfileName;

    private DomainVo domainVo;
//    private List<DomainVo> domainVoList;
}
