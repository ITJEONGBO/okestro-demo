package com.itinfo.itcloud.repository;

import com.itinfo.itcloud.repository.entity.HostInterfaceSamplesHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HostInterfaceSampleHistoryRepository extends JpaRepository<HostInterfaceSamplesHistoryEntity, Integer> {


    // 호스트 - 네트워크
    HostInterfaceSamplesHistoryEntity findFirstByHostInterfaceIdOrderByHistoryDatetimeDesc(UUID hostInterfaceId);
}
