package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @ToString
public class VmResourceVo {
    // 리소스 할당
    private String cpuProfile;      // CPU 프로파일
    private String cpuShare;        // CPU 공유
    private int cpuShareCnt;     // 사용자 정의시 입력값
    private String cpuPinningPolicy;      // CPU Pinning Policy
    private String cpuPinningTopology;     // 피팅토폴로지
    private boolean ioThread;       // I/O 스레드 활성화
    private int ioThreadCnt;        // I/O 스레드 활성화
}
