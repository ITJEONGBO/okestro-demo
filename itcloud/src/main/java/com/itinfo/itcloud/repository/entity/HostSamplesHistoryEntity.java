package com.itinfo.itcloud.repository.entity;

import com.itinfo.itcloud.repository.dto.HostUsageDto;
import com.itinfo.itcloud.repository.dto.UsageDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
/*
@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="HOST_SAMPLES_HISTORY")
public class HostSamplesHistoryEntity {

    @Id
    @Column(unique = true, nullable = false)
    private int historyId;

    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID hostId;

    private LocalDateTime historyDatetime;

    private Integer memoryUsagePercent;
    private Integer cpuUsagePercent;

    private Integer ksmCpuPercent;
    private Integer activeVms;
    private Integer totalVms;
    private Integer totalVmsVcpus;
    private Integer cpuLoad;
    private Integer systemCpuUsagePercent;
    private Integer userCpuUsagePercent;
    private Integer hostStatus;
    private Integer swapUsedMb;
    private Integer hostConfigurationVersion;
    private Integer ksmSharedMemoryMb;
    private Integer secondsInStatus;

    public HostUsageDto totalCpuMemory(){
        return HostUsageDto.builder()
                .historyDatetime(historyDatetime)
                .totalCpuUsagePercent(cpuUsagePercent)
                .totalMemoryUsagePercent(memoryUsagePercent)
                .build();
    }


    public HostUsageDto totalUsage(){
        return HostUsageDto.builder()
                .hostId(hostId.toString())
                .historyDatetime(historyDatetime)
                .totalCpuUsagePercent(cpuUsagePercent)
                .totalMemoryUsagePercent(memoryUsagePercent)
                .build();
    }

    public UsageDto getUsage(){
        return UsageDto.builder()
//              .id(hostId.toString())
                .cpuPercent(cpuUsagePercent)
                .memoryPercent(memoryUsagePercent)
                .build();
    }

}
*/
