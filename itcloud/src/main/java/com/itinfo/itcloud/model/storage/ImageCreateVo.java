package com.itinfo.itcloud.model.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter @Setter @Builder
public class ImageCreateVo {
    private BigInteger size;   // 크기(Gib)
    private String name;
//    private String alias;
    private String description;
    private boolean sparse;     // 할당 정책 (씬, )
    private String dcId;        // 데이터센터 아이디값
    private String domain;    // 스토리지 도메인 아이디값
//    private String profileId;   // 디스크 프로파일 아이디값

    private boolean wipeAfterDelete; // 삭제 후 초기화
//    private boolean shareable;  // 공유가능
    private String backup;  // 증분 백업 사용
}
