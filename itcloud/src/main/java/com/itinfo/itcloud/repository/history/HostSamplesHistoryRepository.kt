package com.itinfo.itcloud.repository.history

import com.itinfo.itcloud.repository.history.entity.HostSamplesHistoryEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface HostSamplesHistoryRepository : JpaRepository<HostSamplesHistoryEntity, Int> {
	// 해당 호스트 cpu, memory 한행만 % 출력
	fun findFirstByHostIdOrderByHistoryDatetimeDesc(hostId: UUID?): HostSamplesHistoryEntity

	// 해당 호스트 cpu,memory % List 출력
	fun findByHostIdOrderByHistoryDatetimeDesc(hostId: UUID?, page: Pageable?): List<HostSamplesHistoryEntity>


	@Query(
		value = """
			SELECT * FROM host_Samples_History h  WHERE host_status=1
			and h.history_Datetime = (SELECT MAX(h2.history_Datetime) FROM host_Samples_History h2 WHERE h2.host_Id = h.host_Id)
			ORDER BY h.cpu_Usage_Percent DESC
			""" ,
		nativeQuery = true
	)
	fun findHostCpuChart(page: Pageable?): List<HostSamplesHistoryEntity>

	@Query(
		value = """
			SELECT * FROM host_Samples_History h  WHERE host_status=1 
			and h.history_Datetime = (SELECT MAX(h2.history_Datetime) FROM host_Samples_History h2 WHERE h2.host_Id = h.host_Id)
			ORDER BY h.memory_Usage_Percent DESC
			""",
		nativeQuery = true
	)
	fun findHostMemoryChart(page: Pageable?): List<HostSamplesHistoryEntity>

	
}