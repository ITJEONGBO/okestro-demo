package com.itinfo.itcloud.model.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name="HOST_SAMPLES_HISTORY")
@ToString
public class HostSamplesHistory {
    @Id
    @Column(unique = true, nullable = false)
    private int historyId;

    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID hostId;

    private LocalDateTime historyDatetime;

    private int memoryUsagePercent;
    private int cpuUsagePercent;

    private int ksmCpuPercent;
    private int activeVms;
    private int totalVms;
    private int totalVmsVcpus;
    private int cpuLoad;
    private int systemCpuUsagePercent;
    private int userCpuUsagePercent;
    private int hostStatus;
    private int swapUsedMb;
    private int hostConfigurationVersion;
    private int ksmSharedMemoryMb;
    private int secondsInStatus;
}
