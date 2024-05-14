package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @ToString
public class VmInitVo {
    private boolean cloudInit;
    private String hostName;
    private String timeStandard;

    // 인증
    // 네트워크
    private String script;  // 사용자 지정 스크립트

}
