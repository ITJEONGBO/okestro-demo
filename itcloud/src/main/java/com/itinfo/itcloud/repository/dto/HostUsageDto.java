package com.itinfo.itcloud.repository.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @ToString @Builder
public class HostUsageDto {
    // 전체 사용량 용도
    private String hostId;
    private String hostName;
    private LocalDateTime historyDatetime;

    private double totalCpuUsagePercent;
    private double totalMemoryUsagePercent;

    private double totalMemoryGB;
    private double usedMemoryGB;
    private double freeMemoryGB;
}
