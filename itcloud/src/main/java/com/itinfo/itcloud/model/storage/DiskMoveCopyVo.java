package com.itinfo.itcloud.model.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;

@Getter @Builder @ToString
public class DiskMoveCopyVo {
    private String id;  //disk id
    private String alias;   // 별칭
    private BigInteger virtualSize; // 가상크기

    private String domainId;
    private String profileId;

//    private List<DomainVo> domainVoList; // 스토리지 도메인 리스트
//    private List<DiskProfileVo> dpVoList;   // 디스크 프로파일 리스트
}
