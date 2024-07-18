package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.IdentifiedVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @Builder @ToString
public class AffinityLabelMember {
    private List<IdentifiedVo> vmLabels;     // 가상머신 레이블
    private List<IdentifiedVo> hostLabels;   // 호스트 레이블
}
