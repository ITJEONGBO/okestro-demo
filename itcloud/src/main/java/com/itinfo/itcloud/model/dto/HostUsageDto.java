package com.itinfo.itcloud.model.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter @ToString @Builder
public class HostUsageDto {
    private String hostId;
    private int hostStatus;
    private LocalDateTime historyDatetime;
    private double totalCpuUsagePercent;
    private double totalMemoryUsagePercent;
}
