package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.repository.dto.HostUsageDto;
import com.itinfo.itcloud.repository.dto.StorageUsageDto;
import com.itinfo.itcloud.repository.dto.UsageDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ItDashService {
    // 원 그래프
    HostUsageDto totalCpuMemory();
    StorageUsageDto totalStorage();

    // 선 그래프
    List<HostUsageDto> totalCpuMemoryList(UUID hostId, int limit);

    // 사용량 top 3
    List<UsageDto> vmCpuChart();
    List<UsageDto> vmMemoryChart();
    List<UsageDto> storageChart();

    // 호스트 사용량 top3
    List<UsageDto> hostCpuChart();
    List<UsageDto> hostMemoryChart();

    List<UsageDto> hostNetwork();

}
