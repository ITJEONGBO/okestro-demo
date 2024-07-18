package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.IdentifiedVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @Builder @ToString
public class AffinityGroupMember {
    private List<IdentifiedVo> vmMembers;      // 가상머신 멤버
    private List<IdentifiedVo> hostMembers;    // 호스트 멤버
}
