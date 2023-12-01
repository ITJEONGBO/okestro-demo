package com.itinfo.itcloud.VO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HostUsageVO {
    private String hostId;
    private String hostStatus;
    private String cpuUsagePercent;
    private String memoryUsagePercent;
    private String historyDatetime;
}
