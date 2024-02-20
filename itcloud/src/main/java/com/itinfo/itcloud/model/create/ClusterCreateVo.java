package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.ovirt.engine.sdk4.types.*;

import java.util.List;

@Getter @ToString @Builder
public class ClusterCreateVo {
    // 일반

    // 전체 데이터센터중에 정하는거
    private String datacenterId;
    private String datacenterName;

    private String id;
    private String name;
    private String description;
    private String comment;

    private String networkId;           // 관리 네트워크 id(데이터센터 네트워크 리스트에서 선택)
    private String networkName;
    private Architecture cpuArc;        // CPU 아키텍처
    private String cpuType;             // CPU 유형 cpu().type()
    private BiosType biosType;          // 칩셋/펌웨어 유형

    private FipsMode fipsMode;          // FIPS 모드
    private String version;             // 호환 버전
    private SwitchType switchType;      // 스위치 유형
    private FirewallType firewallType;  // 방화벽 유형
    private Boolean networkProvider;     // 기본 네트워크 공급자 (예, 아니요?)
                                        //<link href="/ovirt-engine/api/openstacknetworkproviders/1dd7e19a-5b16-4a76-a53f-ec1f1476692f/testconnectivity" rel="testconnectivity"/>

    private Integer logMaxMemory;       // 로그의 최대 메모리 한계

//    private boolean virtService;        // virt 서비스 활성화
//    private boolean glusterService;     // Gluster 서비스 활성화
    // 추가 난수 생성기 소스


    // 마이그레이션
    private MigrationPolicy migrationPolicy;                // 마이그레이션 정책
    private MigrationBandwidthAssignmentMethod bandwidth;   // 대역폭
    private MigrateOnError recoveryPolicy;                  // 복구 정책    <error_handling> <on_error>migrate_highly_available</on_error> </error_handling>
    private InheritableBoolean encrypted;                   // 추가속성- 암호화 사용
//    private MigrationOptions parallel;                      // Parallel Migrations

}
