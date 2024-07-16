package com.itinfo.itcloud.repository.entity;

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

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="HOST_INTERFACE_SAMPLES_HISTORY")
public class HostInterfaceSamplesHistoryEntity {
    @Id
    @Column(unique = true, nullable = false)
    private int historyId;

    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID hostInterfaceId;

    private LocalDateTime historyDatetime;

    private BigDecimal receiveRatePercent;
    private BigDecimal transmitRatePercent;

    private int hostInterfaceConfigurationVersion;

    private BigDecimal receivedTotalByte;
    private BigDecimal transmittedTotalByte;
    private BigDecimal receivedDroppedTotalPackets;
    private BigDecimal transmittedDroppedTotalPackets;
}
