package com.itinfo.itcloud.model.dashboard;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter @Setter
public class DashBoardVOd {

    // 사용자 정보
    private String name;
    private String id;

    // Total CPU
    private int totalCPU;
    private int usingCPU;

    // Total Memory
    private BigDecimal memoryTotal;
    private BigDecimal memoryUsed;
    private BigDecimal memoryFree;

    // Total Disk
    private BigInteger storageTotal;
    private BigInteger storageUsed;


    private String hostName;
    private String upTime;
    private String ip;


    // 호스트 별 사용량
    private int hostsUp;
    private int hostsDown;


    // 가상머신 수량
    private int vmsUp;
    private int vmsDown;


    // 클러스터
//    private int clusters;



}
