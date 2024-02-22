package com.itinfo.itcloud.model.create;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @Builder @ToString
public class VmHostVo {
    // 클러스터 내의 호스트 / 특정 호스트
    private boolean hostCpuPass;    // 호스트 cpu 통과
    private boolean tsc;            // tsc 주파수가 동일한 호스트에만 마이그레이션
    private String migrationMode;   // 마이그레이션 모드
    private String migrationPolicy; // 마이그레이션 정책
    private String migrationEncoding;   // 마이그레이션 암호화 사용
    private String parallelMigration;   // Parallel Migrations
    private String numOfVmMigration;    // Number of VM Migration Connections
    private int numaNode;       // NUMA 노드 수
}
