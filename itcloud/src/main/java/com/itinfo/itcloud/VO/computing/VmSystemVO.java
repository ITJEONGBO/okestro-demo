package com.itinfo.itcloud.VO.computing;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VmSystemVO {
    // 가상머신 메모리, cpu 등 정보
    private String setMemory;           // definedMemory 시스템 메모리 크기
    private String guaranteedMemory;    // 시스템 메모리 크기
    private String maxMemory;           // 최대 메모리

    private int socketsCpuTopology;     // vms-topology-sockets
    private int coresCpuTopology;       // vms-topology-cores
    private int threadsCpuTopology;     // vms-topology-threads
    private int totalCpuTopology;       // vms-topology-(socket*core*threads)
}
