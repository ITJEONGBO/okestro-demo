package com.itinfo.itcloud.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NicUsageVO {
    private String hostInterfaceId;
    private String hostInterfaceName;
    private String macAddress;

    private String vmInterfaceId;
    private String vmInterfaceName;

    private String receiveRatePercent;
    private String receivedTotalByte;
    private String transmitRatePercent;
    private String transmittedTotalByte;

    private String historyDatetime;

}
