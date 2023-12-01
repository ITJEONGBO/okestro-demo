package com.itinfo.itcloud.VO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VmNetworkUsageVO {
    private String historyTime;
    private int receiveRatePercent;
    private int transmitRatePercent;
}
