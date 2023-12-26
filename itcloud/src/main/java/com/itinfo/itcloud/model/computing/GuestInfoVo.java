package com.itinfo.itcloud.model.computing;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GuestInfoVo {
    // vm에 속해있는 속성들
    private String type;      //유형 family
    private String architecture;
    private String os;      // distribution
    private String kernalVersion;
    private String guestTime;

    // 로그인된 사용자
    // 콘솔 사용자
    // 콘솔 클라이언트 IP

}
