package com.itinfo.itcloud.repository;

import com.itinfo.itcloud.repository.entity.VmInterfaceSamplesHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VmInterfaceSamplesHistoryRepository extends JpaRepository<VmInterfaceSamplesHistoryEntity, Integer> {

    VmInterfaceSamplesHistoryEntity findFirstByVmInterfaceIdOrderByHistoryDatetimeDesc(UUID vnInterfaceId);
}
