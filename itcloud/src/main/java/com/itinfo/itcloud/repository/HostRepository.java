package com.itinfo.itcloud.repository;

import com.itinfo.itcloud.model.dto.HostUsageDto;
import com.itinfo.itcloud.model.entity.HostSamplesHistoryEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@Repository
public interface HostRepository extends JpaRepository<HostSamplesHistoryEntity, Integer> {

    // 해당 호스트 cpu,memory 전부 출력
    List<HostSamplesHistoryEntity> findByHostIdOrderByHistoryDatetimeDesc(UUID hostId);

    // 해당 호스트 cpu, memory 한행만 출력
    HostSamplesHistoryEntity findFirstByHostIdAndHostStatusOrderByHistoryDatetimeDesc(UUID hostId, int hostStatus);


}
