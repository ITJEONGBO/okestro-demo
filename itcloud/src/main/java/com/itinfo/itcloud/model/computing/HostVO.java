package com.itinfo.itcloud.model.computing;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Getter @Setter
public class HostVO {
    private String id;
    private String name;
    private String address;
    private String comment;
    private String historyDatetime;
    private String status;

    private String clusterId;
    private String clusterName;
    private String datacenterId;
    private String datacenterName;

    private String spm;
    private int spmPriority;

    private int vmCnt;
    private int vmUpCnt;
    private int vmDownCnt;

    private int cpuCoreCnt;
    private BigDecimal bootTime;

    private String hostedEngineHa;
    private String iscsi;
    private String kdumpStatus;

    private BigInteger memory;
    private BigInteger usedMemory;
    private BigInteger freeMemory;

    private BigInteger swapMemory;
    private BigInteger swapUsedMemory;
    private BigInteger swapFreeMemory;

    private BigInteger sharedMemory;
    private BigInteger newVmMemory;

    private boolean devicePassthrough;
    private boolean pageShared;
    private String pageSize;
    private String hugePagesType;
    private String hugePagesType2;
    private String seLinux;
    private String version;

    private HostHwVO hostHwVO;
    private HostSwVO hostSwVO;

    List<VmVO> vmVOList;
    List<HostNicVO> hostNicVOList;
    List<HostDeviceVO> hostDeviceVOList;
    List<AffinityLabelVO> affinityLabelVOList;

}
