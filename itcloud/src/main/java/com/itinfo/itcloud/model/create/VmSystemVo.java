package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;

@Getter @Builder @ToString
public class VmSystemVo {
    // 시스템
    private BigInteger memorySize;         // 메모리 크기
    private BigInteger memoryMax;          // 최대 메모리
    private BigInteger memoryActual;       // 할당할 실제 메모리
    private int vCpuCnt;            // 총 가상 CPU
    private int vCpuSocket;         // 가상 소켓
    private int vCpuSocketCore;     // 가상 소켓 당 코어
    private int vCpuCoreThread;     // 코어당 스레드
    private String userEmulation;   // 사용자 정의 에뮬레이션 시스템
    private String userCpu;         // 사용자 정의 CPU
    private String userVersion;     // 사용자 정의 호환 버전

    private String instanceType;    // 인스턴스 유형
    private String timeOffset;      // 하드웨어 클럭의 시간 오프셋
}
