package com.itinfo.itcloud.model.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ovirt.engine.sdk4.types.*;

import java.math.BigInteger;

@Getter @Setter @Builder
public class TempStorageVo {
    // 디스크 vo 와 도메인 vo를 합치는 방법도 봐야함 (개수가 여러개면)
    
    // 스토리지
    private String domainName;
//    private StorageDomainStatus domainStatus;
    private boolean domainStatus;
    private StorageDomainType domainType;

    private BigInteger availableSize; // 여유공간
    private BigInteger usedSize; // 사용된 공간
    private BigInteger totalSize; // 전체공간


    // 디스크
    private String diskName;
    private DiskStatus diskStatus;
    private DiskInterface diskInterface;   // 인터페이스
    private String diskType;    // 유형

    private boolean diskSparse;// 할당정책  씬true, 사전할당false
    private String date;
    private BigInteger virtualSize; // 가상크기

//    private StorageDomainVo sdVo;
//    private DiskVo diskVo;
}
