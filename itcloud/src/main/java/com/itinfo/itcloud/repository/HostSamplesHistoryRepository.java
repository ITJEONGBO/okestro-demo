package com.itinfo.itcloud.repository;

import com.itinfo.itcloud.repository.entity.HostSamplesHistoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HostSamplesHistoryRepository extends JpaRepository<HostSamplesHistoryEntity, Integer> {

    // 해당 호스트 cpu, memory 한행만 % 출력
    HostSamplesHistoryEntity findFirstByHostIdOrderByHistoryDatetimeDesc(UUID hostId);

    // 해당 호스트 cpu,memory % List 출력
    List<HostSamplesHistoryEntity> findByHostIdOrderByHistoryDatetimeDesc(UUID hostId, Pageable page);


    @Query(value = "SELECT * FROM host_Samples_History h  WHERE host_status=1 \n" +
            "\tand h.history_Datetime = (SELECT MAX(h2.history_Datetime) FROM host_Samples_History h2 WHERE h2.host_Id = h.host_Id)\n" +
            "ORDER BY h.cpu_Usage_Percent DESC", nativeQuery = true)
    List<HostSamplesHistoryEntity> findHostCpuChart(Pageable page);

    @Query(value = "SELECT * FROM host_Samples_History h  WHERE host_status=1 \n" +
            "\tand h.history_Datetime = (SELECT MAX(h2.history_Datetime) FROM host_Samples_History h2 WHERE h2.host_Id = h.host_Id)\n" +
            "ORDER BY h.memory_Usage_Percent DESC", nativeQuery = true)
    List<HostSamplesHistoryEntity> findHostMemoryChart(Pageable page);


}
