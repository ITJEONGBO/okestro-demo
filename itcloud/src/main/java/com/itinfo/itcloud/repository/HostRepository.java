package com.itinfo.itcloud.repository;

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

}
