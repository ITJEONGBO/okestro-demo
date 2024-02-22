package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @ToString
public class VmHaVo {
    // 고가용성
    private boolean ha;     // 고가용성
    private String vmStorageDomain; // 가상 머신 임대 대상 스토리지 도메인
    private String resumeOperation; // 재개 동작
    private String priority;    // 우선순위
    private String watchDogModel;   // 워치독 모델
    private String watchDogWork;    // 워치독 작업
}
