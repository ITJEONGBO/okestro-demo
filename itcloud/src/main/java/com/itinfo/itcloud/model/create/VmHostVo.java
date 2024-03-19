package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @ToString
public class VmHostVo {

    private String clusterHost;     // 클러스터 내의 호스트
    private String selectHost;      // 특정 호스트
    private String migrationMode;   // 마이그레이션 모드
    private String migrationPolicy; // 마이그레이션 정책  migration_downtime
    private String migrationEncoding;   // 마이그레이션 암호화 사용
    private String parallelMigration;   // Parallel Migrations
    private String numOfVmMigration;    // Number of VM Migration Connections
}