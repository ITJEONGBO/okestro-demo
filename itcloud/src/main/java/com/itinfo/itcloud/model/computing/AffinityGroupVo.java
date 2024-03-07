package com.itinfo.itcloud.model.computing;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Builder
public class AffinityGroupVo {
    // 선호도 그룹
    private String id;
    private String name;
    private String description;
    private boolean status;     // broken
    private int priority;       // 우선순위

    private boolean positive;   // 양극/음극    | 가상머신 따라감
    private boolean enforcing;  // 강제적용     | 가상머신 따라감

    // vms_rule
    private boolean vmEnabled;      // 가상머신 측 극성 (비활성화)
    private boolean vmPositive;     // 가상머신 측 극성 (양극, 음극)
    private boolean vmEnforcing;    // 가상머신 강제적용

    // host_rule
    private boolean hostEnabled;    // 호스트 측 극성 (비활성화)
    private boolean hostPositive;   // 호스트 측 극성 (양극, 음극)
    private boolean hostEnforcing;  // 호스트 강제적용


    private List<String> vmLabels;        // 가상머신 레이블
    private List<String> hostLabels;      // 호스트 레이블
//    private List<AffinityLabelVo> vmLabels;
//    private List<AffinityLabelVo> hostLabels;


    private List<String> vmList;
    private List<String> hostList;
//    private List<VmVo> vmList;      // 가상머신 멤버
//    private List<VmVo> hostList;    // 호스트 멤버


    // LINK: vms, vmlabels, hosts, hostlabels
    // cluster, hosts, vms
}
