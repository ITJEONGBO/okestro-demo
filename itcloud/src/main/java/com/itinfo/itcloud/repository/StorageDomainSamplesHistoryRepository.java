package com.itinfo.itcloud.repository;

import com.itinfo.itcloud.repository.entity.StorageDomainSamplesHistoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StorageDomainSamplesHistoryRepository extends JpaRepository<StorageDomainSamplesHistoryEntity, Integer> {

    // 전체 사용량 - 스토리지
    StorageDomainSamplesHistoryEntity findFirstByStorageDomainIdOrderByHistoryDatetimeDesc(UUID storageDomainId);

    // storage 사용량 3
    @Query(value = "SELECT * FROM storage_domain_samples_history s  WHERE storage_domain_status=1 and s.history_Datetime = \n" +
            "          (SELECT MAX(s2.history_Datetime) FROM storage_domain_samples_history s2 WHERE s2.storage_domain_id = s.storage_domain_id)\n" +
            "   ORDER BY s.used_disk_size_gb desc", nativeQuery = true)
    List<StorageDomainSamplesHistoryEntity> findStorageChart(Pageable page);

}
