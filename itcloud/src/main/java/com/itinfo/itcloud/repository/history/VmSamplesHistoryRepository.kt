package com.itinfo.itcloud.repository.history

import com.itinfo.itcloud.repository.history.entity.VmSamplesHistoryEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface VmSamplesHistoryRepository: JpaRepository<VmSamplesHistoryEntity, Int> {
	// 가상머신 개인 cpu, memory
	fun findFirstByVmIdOrderByHistoryDatetimeDesc(vmId: UUID): VmSamplesHistoryEntity

	@Query(
		value = """SELECT * FROM Vm_Samples_History v WHERE vm_status=1 and v.history_Datetime = 
          (SELECT MAX(v2.history_Datetime) FROM Vm_Samples_History v2 WHERE v2.vm_Id = v.vm_Id)
   ORDER BY v.cpu_Usage_Percent DESC""", nativeQuery = true
	)
	fun findVmCpuChart(page: Pageable?): List<VmSamplesHistoryEntity>

	@Query(
		value = """SELECT * FROM Vm_Samples_History v WHERE vm_status=1 and v.history_Datetime = 
          (SELECT MAX(v2.history_Datetime) FROM Vm_Samples_History v2 WHERE v2.vm_Id = v.vm_Id)
   ORDER BY v.memory_Usage_Percent DESC""", nativeQuery = true
	)
	fun findVmMemoryChart(page: Pageable?): List<VmSamplesHistoryEntity>



	// vm 사용량 순위
	fun findFirstByVmStatusOrderByCpuUsagePercentDesc(vmStatus: Int): List<VmSamplesHistoryEntity>
	/*
    WITH rounded_times AS (
    SELECT
        date_trunc('hour', history_datetime) + (extract(minute FROM history_datetime)::int / 15) * interval '15 minutes' AS rounded_time,
        vm_id,
        cpu_usage_percent,
        history_datetime
    FROM vm_samples_history
    WHERE vm_status = 1
)
SELECT DISTINCT ON (rounded_time, vm_id)
    TO_CHAR(rounded_time, 'YYYY-MM-DD HH24:MI') AS time,
    vm_id,
    cpu_usage_percent
FROM rounded_times
ORDER BY rounded_time desc, vm_id, history_datetime;
     */
}