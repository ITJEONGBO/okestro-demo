package com.itinfo.itcloud.VO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HostInterfaceVO {
    private String historyDatetime;
    private int receiveRatePercent;
    private int transmitRatePercent;
    private int receivedTotalByte;
    private int transmittedTotalByte;
}
