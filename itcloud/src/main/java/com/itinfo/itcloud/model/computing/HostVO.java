package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.LunVO;
import com.itinfo.itcloud.model.NetworkAttachmentVO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class HostVO {
    private String clusterId;
    private String hostId;
    private String hostName;
    private String historyDatetime;
    private String hostStatus;

    private int memoryUsagePercent;
    private int cpuUsagePercent;
//    private int ksmCpuPercent;

    private int activeVms;
    private int totalVms;
    private int totalVmsVcpus;

    private int cpuLoad;
    private int systemCpuUsagePercent;
    private int userCpuUsagePercent;

    private int swapUsedMb;
//    private int ksmSharedMemoryMb;

    private List<LunVO> lunVOList;
    private List<NetworkAttachmentVO> networkAttachmentVOList;

}
