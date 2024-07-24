package com.itinfo.itcloud.repository.entity;

import com.itinfo.itcloud.repository.dto.UsageDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;
/*
@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="VM_SAMPLES_HISTORY")
public class VmSamplesHistoryEntity {
    @Id
    @Column(unique=true, nullable=false)
    private int historyId;
    
    private LocalDateTime historyDatetime;

    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID vmId;

    private int vmStatus;
    private Integer cpuUsagePercent;
    private Integer memoryUsagePercent;
    private String vmIp;
    private String currentUserName;

    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID currentlyRunningOnHost;

    private Integer vmConfigurationVersion;
    private Integer currentHostConfigurationVersion;
    private String vmClientIp;
    private Boolean userLoggedInToGuest;
    private Integer userCpuUsagePercent;
    private Integer systemCpuUsagePercent;

    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID currentUserId;

    private BigInteger memoryBufferedKb;
    private BigInteger memoryCachedKb;
    private Integer secondsInStatus;
}
*/
