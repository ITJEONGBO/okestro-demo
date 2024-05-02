package com.itinfo.itcloud.model.create;

import com.itinfo.itcloud.model.computing.AffinityGroupVo;
import com.itinfo.itcloud.model.computing.AffinityLabelVo;
import com.itinfo.itcloud.model.computing.NicVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @Builder @ToString
public class VmCreateVo {
    // 일반
    private String id;
    private String name;

//    private String dcName;    // 따지고보면 생성창에서 보여주는 역할만 하는거 같음
    private String clusterId;
    private String clusterName;
    private String templateId;      // 템플릿
    private String templateName;
    private String os;              // 운영 시스템
    private String chipsetType;     // 칩셋/펌웨어 유형
    private String option;          // 최적화 옵션

    private String description;     // 설명
    private String comment;

    private boolean stateless;         // 상태 비저장
    private boolean startPaused;        // 일시정지 모드에서 시작
    private boolean deleteProtected;    // 삭제 방지
    // 보관

    private List<VDiskVo> vDiskList;        // 인스턴스 이미지
    private List<NicVo> vnicList;   // vnic 프로파일

    // 시스템
    private VmSystemVo vmSystemVo;

    // 콘솔제외

    // 호스트
    private VmHostVo vmHostVo;

    // 고가용성
    private VmHaVo vmHaVo;

    // 리소스 할당
    private VmResourceVo vmResourceVo;

    // 부트 옵션
    private VmBootVo vmBootVo;

    // 선호도
    private List<AffinityGroupVo> agVoList;
    private List<AffinityLabelVo> alVoList;

}
