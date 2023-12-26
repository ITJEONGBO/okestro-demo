package com.itinfo.itcloud.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class DashboardVo {
    // 사용자 정보
    private String name;
    private String id;

    // 데이터센터
    private int datacenterCnt;
    private int datacenterActive;
    private int datacenterInactive;

    // 클러스터
    private int clusterCnt;

    // 호스트
    private int hostCnt;
    private int hostActive;
    private int hostInactive;

    // 데이터 스토리지 도메인
    private int storageDomainCnt;
    private int storageDomainActive;
    private int storageDomainInactive;

    // 가상머신
    private int vmCnt;
    private int vmActive;
    private int vmInactive;


    // Total CPU
    private int cpuTotal;
    private int cpuAssigned;    // 할당된
    private int cpuCommit;      // 커밋된

    // Total Memory
    private BigDecimal memoryTotal;
    private BigDecimal memoryUsed;
    private BigDecimal memoryFree;

    // Total Disk
    private BigDecimal storageTotal;
    private BigDecimal storageUsed;
    private BigDecimal storageFree;

}