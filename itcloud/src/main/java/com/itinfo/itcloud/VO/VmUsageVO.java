package com.itinfo.itcloud.VO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VmUsageVO {
    private String historyTime;
    private int cpuUsagePercent;
    private int memoryUsagePercent;

}