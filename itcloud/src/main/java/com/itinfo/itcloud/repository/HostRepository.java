package com.itinfo.itcloud.repository;

import com.itinfo.itcloud.repository.entity.HostSamplesHistoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HostRepository extends JpaRepository<HostSamplesHistoryEntity, Integer> {

    // 해당 호스트 cpu, memory 한행만 % 출력
    HostSamplesHistoryEntity findFirstByHostIdOrderByHistoryDatetimeDesc(UUID hostId);

    // 해당 호스트 cpu,memory % List 출력
    List<HostSamplesHistoryEntity> findByHostIdOrderByHistoryDatetimeDesc(UUID hostId, Pageable page);


}
