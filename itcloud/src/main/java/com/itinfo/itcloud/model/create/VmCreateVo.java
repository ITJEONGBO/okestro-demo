package com.itinfo.itcloud.model.create;

import com.itinfo.itcloud.model.computing.AffinityGroupVo;
import com.itinfo.itcloud.model.computing.AffinityLabelVo;
import com.itinfo.itcloud.model.computing.NicVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.ovirt.engine.sdk4.types.VmType;

import java.util.List;

@Getter @Builder @ToString
public class VmCreateVo {
//    https://ovirt.github.io/ovirt-engine-api-model/master/#types/vm
    // 일반
    private String clusterId;
    private String clusterName;
    private String templateId;      // 템플릿
    private String templateName;
    private String datacenterName;
    private String os;              // 운영 시스템
    private String chipsetType;     // 칩셋/펌웨어 유형
    private /*VmType*/ String option;          // 최적화 옵션

    private String id;  // 지정되는거라 설정할 수 없음
    private String name;
    private String description;     // 설명
    private String comment;

    private boolean statusSave;         // 상태 비저장
    private boolean startPaused;        // 일시정지 모드에서 시작
    private boolean deleteProtected;    // 삭제 방지

    private VDiskVo vDiskVo;        // 인스턴스 이미지
    private List<NicVo> vnicList;   // vnic 프로파일

    // 시스템
    private VmSystemVo vmSystemVo;

    // 호스트
    private VmHostVo vmHostVo;

    // 고가용성
    private VmHaVo vmHaVo;

    // 리소스 할당
    private VmResourceVo vmResourceVo;

    // 부트 옵션
    private VmBootVo vmBootVo;

    // 선호도
    private List<AffinityGroupVo> affinityGroupVoList;
    private List<AffinityLabelVo> affinityLabelVoList;

}
