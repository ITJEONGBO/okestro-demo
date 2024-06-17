package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @ToString
public class VmBootVo {
    // 부트 옵션
    private String firstDevice;
    private String secDevice;
//    private List<String> deviceList;

//    private boolean cdDvdConn;
    private String connId;  // cd/dvd 연결되면 뜰 iso id (사실 디스크id)
    private String connName;
    private boolean bootingMenu;

//    CDROM("cdrom"), HD("hd"), NETWORK("network");
}
