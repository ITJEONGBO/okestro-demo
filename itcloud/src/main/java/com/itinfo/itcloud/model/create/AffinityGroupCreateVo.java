package com.itinfo.itcloud.model.create;

import com.itinfo.itcloud.model.IdentifiedVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @Builder @ToString
public class AffinityGroupCreateVo {
    private String id;  // ag id
    private String name;
    private String description;
    private int priority;       // 우선순위

    private String clusterId;

    // vms_rule
    private boolean vmEnabled;      // 가상머신 측 극성 (비활성화)
    private boolean vmPositive;     // 가상머신 측 극성 (양극, 음극)
    private boolean vmEnforcing;    // 가상머신 강제적용

    // host_rule
    private boolean hostEnabled;    // 호스트 측 극성 (비활성화)
    private boolean hostPositive;   // 호스트 측 극성 (양극, 음극)
    private boolean hostEnforcing;  // 호스트 강제적용

    private List<IdentifiedVo> vmLabels;     // 가상머신 레이블
    private List<IdentifiedVo> hostLabels;   // 호스트 레이블

    private List<IdentifiedVo> vmList;      // 가상머신 멤버
    private List<IdentifiedVo> hostList;    // 호스트 멤버
}
