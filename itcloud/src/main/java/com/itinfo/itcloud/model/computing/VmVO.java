package com.itinfo.itcloud.model.computing;

import com.itinfo.itcloud.model.SnapshotVO;
import com.itinfo.itcloud.model.storage.DiskVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.util.List;

@Getter @Setter
@ToString
public class VmVO {

    private String vmId;            // vm의 id
    private String vmName;          // vm의 이름

    private String status;          // up, down  item.status().value()
    private String comment;         // 보이지않음 <comment/>
    private String description;     // vm 설명 (다른걸로 쓴거 같은데 use를 쓰지않고 여기에 쓸예정)
    private String ipAddress;       // address + "/"+ fe:00
    private String fqdn;            // 호스트와 도메인을 함께 명시하여 전체 경로를 모두 표기하는 것

    private String hostId;          // hostId
    private String hostName;        // hostName

    private String clusterId;       // cluster Id
    private String clusterName;     // 기능구현 되어있지 않음  (vmInfoVO),

    private String dataCenterId;      //
    private String dataCenterName;    //

    private String orgTemplateId;     // origin template
    private String orgTemplateName;
    private String templateID;        // template? 근데 정보가 없음 (이건 id로 해서 링크만 하면 될거 같기도 하고)
    private String templateName;

    private String graphicProtocol;         // 그래픽 프로토콜
    private String upTime;
    private String startTime;          // startTime (start_time)

    private String os;                  //  os
    private String ChipsetFirmType;     // 칩셋/펌웨어 유형
    private String optimizeOption;      // 최적화 옵션: 서버

    private VmSystemVO vmSystem;        // 시스템 설정 cpu,memory값
    private List<VmNicVO> vmNics;       // 네트워크 인터페이스
    private List<DiskVO> disks;         // 디스크
    private List<SnapshotVO> snapshots; // 스냅샷


}