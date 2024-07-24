package com.itinfo.itcloud.repository.entity;

import com.itinfo.itcloud.model.computing.HostSwVo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
/*
@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="HOST_CONFIGURATION")
public class HostConfigurationEntity {
    @Id
    @Column(unique = true, nullable = false)
    private int historyId;

    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID hostId;

    private String hostUniqueId;
    private String hostName;

    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID clusterId;

    private int hostType;
    private String fqdnOrIp;
    private int memorySizeMb;
    private int swapSizeMb;
    private String cpuModel;
    private int numberOfCores;
    private String hostOs;
    private String kernelVersion;
    private String kvmVersion;
    private String vdsmVersion;
    private int vdsmPort;
    private int clusterConfigurationVersion;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private LocalDateTime deleteDate;
    private int numberOfSockets;
    private BigDecimal cpuSpeedMh;
    private int threadsPerCore;
    private String hardwareManufacturer;
    private String hardwareProductName;
    private String hardwareVersion;
    private String hardwareSerialNumber;
    private int numberOfThreads;

    public HostSwVo getSoftware(){
        return HostSwVo.builder()
                .osVersion(hostOs)
                .osInfo("oVirt Node 4.5.5 (임의로 넣은값)")
                .kernalVersion(kernelVersion)
                .kvmVersion(kvmVersion)
                .build();
    }

}
*/