package com.itinfo.itcloud.repository;

import com.itinfo.itcloud.model.dto.MemoryUsageDto;
import com.itinfo.itcloud.model.entity.HostSamplesHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HostRepository extends JpaRepository<HostSamplesHistory, Integer> {

    @Query(value = "SELECT * FROM HOST_SAMPLES_HISTORY h WHERE h.host_id = :hostId", nativeQuery = true)
    List<HostSamplesHistory> retrieveHosts(@Param("hostId") UUID hostId);

    @Query("SELECT new com.itinfo.itcloud.model.dto.MemoryUsageDto(h.historyDatetime, CAST(SUM(h.memoryUsagePercent) / COUNT(DISTINCT h.hostId) AS int)) " +
            "FROM HostSamplesHistory h " +
            "GROUP BY h.historyDatetime " +
            "ORDER BY h.historyDatetime DESC")
    List<MemoryUsageDto> findTotalMemoryUsagePercent();


}
