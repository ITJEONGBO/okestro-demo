package com.itinfo.itcloud.repository.entity;

import com.itinfo.itcloud.repository.dto.StorageUsageDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
/*
@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="STORAGE_DOMAIN_SAMPLES_HISTORY")
public class StorageDomainSamplesHistoryEntity {
    @Id
    @Column(unique = true, nullable = false)
    private int historyId;

    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID storageDomainId;

    private LocalDateTime historyDatetime;

    private int availableDiskSizeGb;
    private int usedDiskSizeGb;

    private int storageConfigurationVersion;
    private int storageDomainStatus;
    private int secondsInStatus;

    public StorageUsageDto totalStorage(){
        return StorageUsageDto.builder()
                .historyDatetime(historyDatetime)
                .totalGB(availableDiskSizeGb + usedDiskSizeGb)
                .usedGB(usedDiskSizeGb)
                .freeGB(availableDiskSizeGb)
                .build();
    }
}
*/