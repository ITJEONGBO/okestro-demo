package com.itinfo.itcloud.model.create;

import com.itinfo.itcloud.model.computing.AffinityGroupVo;
import com.itinfo.itcloud.model.computing.AffinityLabelVo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @Builder
public class HostCreateVo {
    // 일반
    private String id;
    private String name;
    private String clusterId;
    private String datacenterId;
    private String comment;
    private String hostIp;      // 호스트이름, IP
    private int sshPort;        // ssh 포트
    private boolean active;     // 설치 후 호스트를 활성화
    private boolean restart;    // 설치 후 호스트를 다시 시작
    private String userName;    // 사용자 이름
    private String password;    // 암호
    private boolean sshPublicKey;   // ssh 공개 키

    // 전원 관리
    private boolean powerActive;    // 전원 관리 활성
    private boolean kdump;      // kdump 통합
    private boolean powerPolicy;    // 전원 관리 정책 제어를 비활성화

    // 호스트 엔진
    private boolean hostEngine;     // 없음, 배포

    // 선호도
    private List<AffinityGroupVo> affinityGroupVoList;
    private List<AffinityLabelVo> affinityLabelVoList;


}
