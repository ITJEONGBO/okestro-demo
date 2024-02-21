package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @ToString
public class InstanceImageVo {
    // 인스턴스 이미지 클릭했을 때 나오는 페이지
    // 새 가상 디스크

    // 이미지
    private String size;        // 크기(GiB)
    private String alias;       // 별칭
    private String description; // 설명
    private String diskInterface;   // 인터페이스
    private String storageDomain;   // 스토리지 도메인
    private String allocationPolicy;    // 할당 정책
    private String diskProfile;     // 디스크 프로파일

    private boolean deleteInitialization;   // 삭제 후 초기화
    private boolean bootable;   // 부팅가능
    private boolean shareable;  // 공유가능
    private boolean readOnly;   // 읽기전용
    private boolean cancel;     // 취소 활성화
    private boolean backupUse;  // 증분 백업 사용


    // 직접 LUN
    private String lunAlias;            // 별칭
    private String lunDescription;      // 설명
    private String lunInterface;        // 인터페이스
    private String lunHost;             // 호스트
    private String lunStorageType;      // 스토리지 타입

    private boolean lunBootable;        // 부팅가능
    private boolean lunShareable;       // 공유가능
    private boolean lunReadonly;        // 읽기전용
    private boolean lunCancel;          // 취소 활성화
    private boolean lunScsiPass;        // scsi 통과 활성화
    private boolean lunScsiPermission;  // 권한 부여된 scsi i/o 허용
    private boolean lunScsiReservation; // scsi 예약 사용



    // 관리되는 블록
    private String blockSize;           // 크기(gib)
    private String blockAlias;          // 별칭
    private String blockDescription;    // 설명
    private String blockInterface;      // 인터페이스
    private String blockStorageType;    // 스토리지 타입

    private boolean blockBootable;      // 부팅가능
    private boolean blockShareable;     // 공유가능
    private boolean blockReadonly;      // 읽기전용


}
