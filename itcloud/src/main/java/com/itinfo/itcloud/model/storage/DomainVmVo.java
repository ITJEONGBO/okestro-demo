package com.itinfo.itcloud.model.storage;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter @Setter
public class DomainVmVo {

    // 가상머신
    private String vmName;
    private int diskCnt;    // 디스크 개수
    private String templateName;
    private String vmDate;

    // 디스크
    private String diskName;
    private String diskDate;

    // 스냅샷
    private String snapName;
    private String snapDate;

    private BigInteger virtualSize;
    private BigInteger actualSize;
}
