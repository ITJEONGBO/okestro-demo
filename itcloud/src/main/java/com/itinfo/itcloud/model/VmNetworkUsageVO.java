package com.itinfo.itcloud.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VmNetworkUsageVO {
    private String historyTime;
    private int receiveRatePercent;
    private int transmitRatePercent;
}
