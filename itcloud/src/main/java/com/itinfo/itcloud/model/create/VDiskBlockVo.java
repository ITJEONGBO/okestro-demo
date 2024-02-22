package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @ToString
public class VDiskBlockVo {
    // 관리되는 블록
    private String size;           // 크기(gib)
    private String alias;          // 별칭
    private String description;    // 설명
    private String interfaces;      // 인터페이스
    private String storageType;    // 스토리지 타입

    private boolean bootable;      // 부팅가능
    private boolean shareable;     // 공유가능
    private boolean readonly;      // 읽기전용
}
