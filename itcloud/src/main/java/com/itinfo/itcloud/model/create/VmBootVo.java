package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @ToString
public class VmBootVo {
    // 부트 옵션
    private String firstDevice;
    private String secondDevice;
    private boolean cdDvdConn;
    private boolean bootingMenu;
}
