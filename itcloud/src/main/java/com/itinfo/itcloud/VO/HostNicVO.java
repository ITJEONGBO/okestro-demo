package com.itinfo.itcloud.VO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HostNicVO {
    private String hostNicId;
    private String hostNicName;
    private String macAddress;

    private String receiveRatePercent;
    private String receivedTotalByte;
    private String transmitRatePercent;
    private String transmittedTotalByte;

    private String historyDatetime;
}
