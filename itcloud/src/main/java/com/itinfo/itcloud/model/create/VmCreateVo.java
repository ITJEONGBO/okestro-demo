package com.itinfo.itcloud.model.create;

import com.itinfo.itcloud.model.computing.AffinityGroupVo;
import com.itinfo.itcloud.model.computing.AffinityLabelVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @Builder @ToString
public class VmCreateVo {
    // 일반
    private String clusterId;
    private String templateId;
    private String os;              // 운영 시스템
    private String chipsetType;     // 칩셋/펌웨어 유형
    private String option;          // 최적화 옵션

    private String id;
    private String name;
    private String description;     // 설명
    private String comment;
    private String vmId;

    private boolean statusSave;     // 상태 비저장
    private boolean pauseStart;     // 일시정지 모드에서 시작
    private boolean deletePrevention;   // 삭제 방지
    private boolean keeping;        // 보관

    private InstanceImageVo iVo;    // 인스턴스 이미지

    private List<String> vnic;  // vnic 프로파일


    // 시스템
    private int memorySize;         // 메모리 크기
    private int memoryMax;          // 최대 메모리
    private int memoryActual;       // 할당할 실제 메모리
    private int vCpuCnt;            // 총 가상 CPU
    private int vCpuSocket;         // 가상 소켓
    private int vCpuSocketCore;     // 가상 소켓 당 코어
    private int vCpuCoreThread;     // 코어당 스레드
    private String userEmulation;   // 사용자 정의 에뮬레이션 시스템
    private String userCpu;         // 사용자 정의 CPU
    private String userVersion;     // 사용자 정의 호환 버전

    private String instanceType;    // 인스턴스 유형
    private String timeOffset;      // 하드웨어 클럭의 시간 오프셋
    private String serialNumPolicy; // 일련번호 정책
    private String userSerialNum;   // 사용자 정의 일련 번호


    // 호스트
    // 클러스터 내의 호스트 / 특정 호스트
    private boolean hostCpuPass;    // 호스트 cpu 통과
    private boolean tsc;            // tsc 주파수가 동일한 호스트에만 마이그레이션
    private String migrationMode;   // 마이그레이션 모드
    private String migrationPolicy; // 마이그레이션 정책
    private String migrationEncoding;   // 마이그레이션 암호화 사용
    private String parallelMigration;   // Parallel Migrations
    private String numOfVmMigration;    // Number of VM Migration Connections
    private int numaNode;       // NUMA 노드 수


    // 고가용성
    private boolean ha;     // 고가용성
    private String vmStorageDomain; // 가상 머신 임대 대상 스토리지 도메인
    private String resumeOperation; // 재개 동작
    private String priority;    // 우선순위
    private String watchDogModel;   // 워치독 모델
    private String watchDogWork;    // 워치독 작업

    // 리소스 할당
    private String cpuProfile;      // CPU 프로파일
    private String cpuShare;        // CPU 공유
    private String cpuTopology;     // CPU Pinning Policy
    private boolean memoryBalloon;  // 메모리 Balloon 활성화
    private boolean tpm;            // TPM 장치 활성화
    private boolean ioThread;       // I/O 스레드 활성화
    private int ioThreadCnt;        // I/O 스레드 활성화
    private boolean multiQue;       // 멀티 큐 사용
    private boolean virtioScsi;     // VirtIO-SCSI 활성화
    private String virtioScsiQueues;    // VirtIO-SCSI Multi Queues


    // 부트 옵션
    private String firstDevice;
    private String secondDevice;
    private boolean cdDvdConn;
    private boolean bootingMenu;


    // 선호도
    private List<AffinityGroupVo> affinityGroupVoList;
    private List<AffinityLabelVo> affinityLabelVoList;





    // 가상머신에 있는거 위에는 피그마에만 해당되는거

    // 초기실행
    private String beginClusterId;
    private String beginTemplateId;
    private String beginOs;
    private String beginChipsetType;
    private String beginOption;

    private boolean cloudInit;
    private String vmHostName;
    private boolean timeSetting;
    private String timeZone;
    // 인증, 네트워크, 사용자 지정 스크립트

    // 콘솔
    // 리소스 할당
    // 난수 생성기
    // 사용자 정의 속성
    // 아이콘
    // foreman/satellite
    
}
