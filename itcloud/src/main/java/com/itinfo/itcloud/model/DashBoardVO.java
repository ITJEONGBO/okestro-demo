package com.itinfo.itcloud.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class DashBoardVO {

    /*private String dataCenterId;
    private String dataCenterName;
    private String description;*/

    private int datacenterCnt;
    private int datacenterActive;
    private int datacenterInactive;

    private int clusterCnt;

    private int hostCnt;
    private int hostActive;
    private int hostInactive;

    private int storageDomainCnt;
    private int storageDomainActive;
    private int storageDomainInactive;

    private int vmCnt;
    private int vmActive;
    private int vmInactive;


    private int cpuTotal;
    private int cpuAssigned;    // 할당된
    private int cpuCommit;      // 커밋된

    private BigDecimal memoryTotal;
    private BigDecimal memoryUsed;
    private BigDecimal memoryFree;

    private BigDecimal storageTotal;
    private BigDecimal storageUsed;
    private BigDecimal storageFree;

}
