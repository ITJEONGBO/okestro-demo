package com.itinfo.itcloud.model.create;

import com.itinfo.itcloud.model.IdentifiedVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.ovirt.engine.sdk4.types.InheritableBoolean;

import java.util.List;

@Getter @Builder @ToString
public class VmHostVo {
    private boolean clusterHost;     // 클러스터 내의 호스트 true-클러스터 내 호스트, false-특정
    private List<IdentifiedVo> hostId;      // 특정 호스트

    private String migrationMode;   // 마이그레이션 모드(placement_policy<affinity>) VmAffinity (MIGRATABLE, USER_MIGRATABLE, PINNED)
    private String migrationPolicy; // 마이그레이션 정책  migration_downtime
    private InheritableBoolean migrationEncrypt;   // 마이그레이션 암호화 사용
    private String parallelMigration;   // Parallel Migrations
    private String numOfVmMigration;    // Number of VM Migration Connections
}
