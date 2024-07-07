package com.itinfo.itcloud.model.entity;

import lombok.Getter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="HOST_SAMPLES_HISTORY")
@Getter
public class HostSamplesHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int historyId;

    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID hostId;

    private int hostStatus;

    private int memoryUsagePercent;

    private int cpuUsagePercent;
    private int ksmCpuPercent;
    private int activeVms;
    private int totalVms;
    private int totalVmsVcpus;
    private int cpuLoad;
    private int systemCpuUsagePercent;
    private int userCpuUsagePercent;
    private int swapUsedMb;
    private int hostConfigurationVersion;
    private int ksmSharedMemoryMb;
    private int secondsInStatus;

}
