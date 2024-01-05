package com.itinfo.itcloud.model.computing;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class EventVo {
    private String id;
    private String severity;    // error 여부, Enum LogSeverity: Alert, Error, Normal, warning
    private String time;
//    private Date time;
    private String message;
    private String relationId;
    private String source;
    private String eventId; // 사용자 지정 이벤트 ID


    private String datacenterName;
    private String clusterName;
    private String hostName;
    private String vmName;
    private String templateName;
}
