package com.itinfo.itcloud.repository;

import com.itinfo.itcloud.repository.entity.HostInterfaceSamplesHistoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HostInterfaceRepository extends JpaRepository<HostInterfaceSamplesHistoryEntity, Integer> {

    // 해당 호스트의 네트워크
    List<HostInterfaceSamplesHistoryEntity> findByHostInterfaceIdOrderByHistoryDatetimeDesc(UUID hostInterfaceId, Pageable page);
}
