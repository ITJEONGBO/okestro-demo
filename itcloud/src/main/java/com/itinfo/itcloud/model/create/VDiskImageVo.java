package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @ToString
public class VDiskImageVo {
    // 이미지
    private String size;        // 크기(GiB)
    private String alias;       // 별칭
    private String description; // 설명
    private String interfaces;   // 인터페이스
    private String storageDomain;   // 스토리지 도메인
    private String allocationPolicy;    // 할당 정책
    private String diskProfile;     // 디스크 프로파일

    private boolean deleteInitialization;   // 삭제 후 초기화
    private boolean bootable;   // 부팅가능
    private boolean shareable;  // 공유가능
    private boolean readOnly;   // 읽기전용
    private boolean cancel;     // 취소 활성화
    private boolean backupUse;  // 증분 백업 사용
}
