package com.itinfo.itcloud.model.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ovirt.engine.sdk4.types.DiskBackup;

import java.math.BigInteger;

@Getter @Setter @Builder
public class ImageCreateVo {
    private String id;
    private BigInteger size;   // 크기(Gib)
    private BigInteger appendSize;  // 확장크기
    private String name;
    private String description;
    private boolean sparse;     // 할당 정책 (씬, )
    private String domainId;    // 스토리지 도메인 아이디값
    private String profileId;   // 디스크 프로파일 아이디값

    private boolean wipeAfterDelete; // 삭제 후 초기화
    private boolean share;  // 공유가능
    private DiskBackup backup;  // 증분 백업 사용

    private String hostId;
}