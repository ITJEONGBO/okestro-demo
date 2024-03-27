package com.itinfo.itcloud.model.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ovirt.engine.sdk4.types.DiskInterface;
import org.ovirt.engine.sdk4.types.DiskStatus;
import org.ovirt.engine.sdk4.types.DiskStorageType;

import java.math.BigInteger;

@Getter @Setter @Builder
public class TemDiskVo {
    // 디스크 vo 와 도메인 vo를 합치는 방법도 봐야함

    private String name;
    private DiskStatus status;
    private boolean sparse;// 할당정책  씬true, 사전할당false
    private DiskInterface diskInterface;   // 인터페이스
    private DiskStorageType type;    // 유형
    private String date;

    private BigInteger virtualSize; // 가상크기
    private BigInteger actualSize;  // 실제 크기

    // domain
    private String domainName;
    private String domainType;
    private String domainStatus;

    private BigInteger availableSize;   // 여유공간
    private BigInteger usedSize;    // 사용된 공간
    private BigInteger diskSize;    // 전체공간
}
