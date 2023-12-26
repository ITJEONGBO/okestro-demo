package com.itinfo.itcloud.model.computing;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class SnapshotVo {
    private String description;
    private Date date;
    private boolean persistMemory;
    private String status;
    private String type;


    // 일반-> 날짜, 상태, 메모리, 설명, 설정된 메모리, 할당할 실제메모리, cpu코어수
    // 디스크-> 상태, 별칭, 가상크기, 실제크기, 할당정책, 인터페이스, 생성일자, 디스크스냅샷id, 유형, 설명
    // nic-> 이름, 네트워크이름, 프로파일이름, 유형, mac, rx, tx, 중단
    // 설치된 애플리케이션

}
