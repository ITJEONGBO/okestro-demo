package com.itinfo.itcloud.repository.entity;

import com.itinfo.itcloud.repository.dto.HostUsageDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

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

    /**
     * 전체 사용량 - 원 그래프 %
     * 호스트 당 사용률 검색
     * @return 호스트 cpu, memory %
     */
    public HostUsageDto totalCpuMemory(){
        return HostUsageDto.builder()
                .historyDatetime(historyDatetime)
                .totalCpuUsagePercent(cpuUsagePercent)
                .totalMemoryUsagePercent(memoryUsagePercent)
                .build();
    }


    /**
     * 전체 사용량 - 선 그래프 GB
     * 호스트 사용률 전체 출력
     * 근데 10분마다 한번씩 나오는거면 최대 10개로 제한해서 한시간 내의 정보만 보여주면 되는거 아닌가?
     * @return 호스트 1분마다의 cpu,memory 값
     */
    public HostUsageDto totalUsage(){
        return HostUsageDto.builder()
                .hostId(hostId.toString())
                .historyDatetime(historyDatetime)
                .totalCpuUsagePercent(cpuUsagePercent)
                .totalMemoryUsagePercent(memoryUsagePercent)
                .build();
    }

    // 호스트 각각 현재 사용량 보여주는 거


}
