package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.DefaultSetVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @Builder @ToString
public class VmSetVo {
    private String clusterId;
    private String clusterName;

    private String dcName;

    // 인스턴스 이미지 연결, 생성

    private List<VmVnicVo> vnicList;
    private List<DefaultSetVo> hostVoList;
    private List<DefaultSetVo> profileVoList;

    private List<DefaultSetVo> agVoList;
    private List<DefaultSetVo> alVoList;


//    private List<NicVo> nicVoList;
//    private List<HostVo> hostVoList;
//    private List<CpuProfileVo> profileVoList;
//    private List<DiskVo> diskVoList;
//
//    private List<AffinityGroupVo> agVoList;
//    private List<AffinityLabelVo> alVoList;
}
