package com.itinfo.itcloud.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor @ToString
public class MemoryUsageDto {
    private LocalDateTime historyDatetime;
    private int totalMemoryUsagePercent;
}
