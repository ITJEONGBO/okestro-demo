package com.itinfo.itcloud.model.computing;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@Getter @Setter @Builder @ToString
public class TemplateVo {
    private String id;
    private String name;
    private String comment; // 템플릿 생성과 목록에는 있지만 일반 출력에는 없음
    private String description;
    private String version;
    private String createDate;
    private String status;
    // 보관

    private String osType;
    private String chipsetFirmwareType; // bios.type
    private String optimizeOption;      // 최적화 옵션

    private BigInteger memory;  // 설정된 메모리
    private int cpuCnt;     // cpu 코어 수
    private int cpuCoreCnt;
    private int cpuSocketCnt;
    private int cpuThreadCnt;

    private String hostCluster;
    private int monitor;    // 모니터 수
    private boolean ha; // 고가용성
    private int priority;   // 우선순위
    private boolean usb;    // usb
    private boolean noneStocked;   // 상태 비저장
    private String origin;  // 소스

    private String clusterId;
    private String clusterName;
    private String datacenterId;
    private String datacenterName;
}
