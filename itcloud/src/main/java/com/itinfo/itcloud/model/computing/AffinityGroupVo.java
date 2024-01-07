package com.itinfo.itcloud.model.computing;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class AffinityGroupVo {
    private String id;
    private String name;
    private String description;
    private boolean status; // broken
    private int priority;  // 우선순위

    private boolean positive;   // 양극/음극  | 가상머신에 따라간ㅁ
    private boolean enforcing;  // 강제적용 | 가상머신에 따라감

    // host_rule, 가상머신 측 극성, 가상머신 강제적용
    private boolean vmEnabled;
    private boolean vmPositive;
    private boolean vmEnforcing;

    // vms_rule, 호스트 측 극성, 호스트 강제적용
    private boolean hostEnabled;
    private boolean hostPositive;
    private boolean hostEnforcing;


    // 가상머신 레이블, 호스트 레이블
//    private String vmLabels;
//    private String hostLabels;
    private List<AffinityLabelVo> vmLabels;
    private List<AffinityLabelVo> hostLabels;


    // 가상머신 멤버, 호스트 멤버
//    private String vmList;
//    private String hostList;
    private List<VmVo> vmList;
    private List<VmVo> hostList;

    // LINK: vms, vmlabels, hosts, hostlabels
    // cluster, hosts, vms
}
