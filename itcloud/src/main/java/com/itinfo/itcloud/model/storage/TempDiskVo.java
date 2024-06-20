package com.itinfo.itcloud.model.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.ovirt.engine.sdk4.types.DiskInterface;
import org.ovirt.engine.sdk4.types.DiskStatus;

import java.math.BigInteger;
import java.util.List;

@Getter @Setter @Builder @ToString
public class TempDiskVo {
    private String id;
    private String name;
    private String alias;   // 별칭

    private BigInteger virtualSize; // 가상크기
    private BigInteger actualSize;  // 실제 크기

    private DiskStatus status;
    private String sparse;// 할당정책  씬true, 사전할당false
    private DiskInterface diskInterface;   // 인터페이스
    private String storageType;     // 유형
//    private String createDate;

    private List<DomainVo> domainVoList;
}
