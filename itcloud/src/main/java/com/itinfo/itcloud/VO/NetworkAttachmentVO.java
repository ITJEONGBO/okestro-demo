package com.itinfo.itcloud.VO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class NetworkAttachmentVO {
    private List<String> dnsServer;
    private String bootProtocol;

    private String nicAddress;
    private String nicGateway;
    private String nicNetmask;
    private String nicNetworkName;
    private String nicNetworkId;

    private String hostNicName;
    private String hostNicId;
    private String netHostId;
    private String netHostName;
    private String netAttachmentId;

}
