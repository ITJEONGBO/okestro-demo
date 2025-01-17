package com.itinfo.itcloud.repository.history

import com.itinfo.itcloud.repository.history.entity.StorageDomainSamplesHistoryEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface StorageDomainSamplesHistoryRepository : JpaRepository<StorageDomainSamplesHistoryEntity, Int> {
	// 전체 사용량 - 스토리지
	fun findFirstByStorageDomainIdOrderByHistoryDatetimeDesc(storageDomainId: UUID): StorageDomainSamplesHistoryEntity

	// storage 사용량 3
	@Query(
		value = """
			SELECT * FROM storage_domain_samples_history s  WHERE storage_domain_status=1 AND (s.available_disk_size_gb notnull) and s.history_Datetime = 
          		(SELECT MAX(s2.history_Datetime) FROM storage_domain_samples_history s2 WHERE s2.storage_domain_id = s.storage_domain_id)
   			ORDER BY s.used_disk_size_gb desc
			""",
		nativeQuery = true
	)
	fun findStorageChart(page: Pageable?): List<StorageDomainSamplesHistoryEntity>
}
