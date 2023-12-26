package com.itinfo.itcloud.model.computing;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter @Setter
public class VmDiskVo {
    // vms-disk_attachments
    private String id;
    private String name;

    private boolean active;
    private boolean bootAble;
    private boolean passDiscard;
    private boolean readOnly;
    private boolean useScsi;

    private String interfaceName;
    private String logicalName;


    // DiskVo 에 있는 내용임
    private BigInteger virtualSize;
    private String connection;
    private String status;  // 상태
    private String type;
    private String description;


    // 상태, 별칭, 부팅가능, 공유가능, 읽기전용, 가상크기, 여결대상, 인터페이스, 논리저깅름, 상태, 유형, 설명

    // disk, vm
}
