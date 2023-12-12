package com.itinfo.itcloud.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Getter @Setter
public class HostDetailVO {
    private String id;
    private String name;
    private String description;
    private String comment;
    private String address;
    private String status;
    private String clusterName;
    private String clusterId;
    private boolean powerManagementEnabled;

    private int vmsCnt;
    private int vmsUpCnt;
    private int vmsDownCnt;

    private BigDecimal memoryTotal;
    private BigDecimal memoryUsed;
    private BigDecimal memoryFree;

    private BigDecimal swapTotal;
    private BigDecimal swapUsed;
    private BigDecimal swapFree;

    private BigDecimal ksmCpuUsagePercent;
    private BigDecimal userCpuUsagePercent;
    private BigDecimal systemCpuUsagePercent;
    private BigDecimal idleCpuUsagePercent;

    private BigDecimal bootTime;

    private String hwManufacturer;
    private String hwProductName;

    private String cpuType;
    private String cpuName;
    private BigInteger cpuCores;
    private BigInteger cpuSockets;
    private BigInteger cpuThreads;
    private int cpuTotal;

    private String osVersion;
    private String osInfo;
    private String kernelVersion;
    private boolean haConfigured;
    private String haScore;

//    private HostSwVo hostSw;
    private List<List<String>> cpuUsage;
    private List<List<String>> memoryUsage;
//    private HostUsageVo hostLastUsage;
//    private List<HostUsageVo> hostUsageList;
//    private List<VmSummaryVo> vmSummaries;
    private List<NicUsageVO> nicUsageVOList;
//    private List<NicUsageApiVo> hostNicsUsageApi;
    private List<NetworkAttachmentVO> netAttachment;
//    private SshVo sshVo;
    private List<LunVO> LunVos;
    private List<UsageVO> usageVos;
}
