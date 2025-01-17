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
			select * from host_samples_history h  where host_status=1
			and h.history_datetime = (select max(h2.history_datetime) from host_samples_history h2 where h2.host_id = h.host_id)
			order by h.cpu_usage_percent desc
			""" ,
		nativeQuery = true
	)
	fun findHostCpuChart(page: Pageable?): List<HostSamplesHistoryEntity>

	@Query(
		value = """
			select * from host_samples_history h  where host_status=1 
			and h.history_datetime = (select max(h2.history_datetime) from host_samples_history h2 where h2.host_id = h.host_id)
			order by h.memory_usage_percent desc
			""",
		nativeQuery = true
	)
	fun findHostMemoryChart(page: Pageable?): List<HostSamplesHistoryEntity>

	
}