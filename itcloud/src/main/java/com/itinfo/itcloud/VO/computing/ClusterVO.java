package com.itinfo.itcloud.VO.computing;

import com.itinfo.itcloud.VO.HostDetailVO;
import com.itinfo.itcloud.VO.network.NetworkVO;
import com.itinfo.itcloud.VO.UsageVO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
public class ClusterVO {

    private String clusterId;
    private String clusterName;
    private String description;
    private String dataCenter;

    private String cpuType;
    private String cpuImage;
    private String chipsetFirmwareType; // 추가

    private List<NetworkVO> clusterNetworks;

    private int hostCnt;
    private int hostsUp;
    private int hostsDown;

    private int vmCnt;
    private int vmsUp;
    private int vmsDown;

    private BigDecimal memoryTotal;
    private BigDecimal memoryUsed;
    private BigDecimal memoryFree;
    private BigDecimal memoryUsagePercent;
    private BigDecimal ksmCpuUsagePercent;
    private BigDecimal userCpuUsagePercent;
    private BigDecimal systemCpuUsagePercent;
    private BigDecimal idleCpuUsagePercent;
    private List<List<String>> cpuUsage;
    private List<List<String>> memoryUsage;
    private NetworkVO network;
    private List<HostDetailVO> hostDetailList;
//    private List<VmSummaryVo> vmSummaries;
    private List<UsageVO> usageVOList;

}
