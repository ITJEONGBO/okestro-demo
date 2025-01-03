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
		value =
		"""
			SELECT DISTINCT v.vm_id, v.* 
			FROM Vm_Samples_History v 
			JOIN vm_configuration c ON v.vm_id = c.vm_id WHERE v.vm_status = 1 
			AND v.history_Datetime = ( SELECT MAX(v2.history_Datetime) FROM Vm_Samples_History v2 WHERE v2.vm_Id = v.vm_Id) 
			AND NOT EXISTS (SELECT 1 FROM vm_configuration c2 WHERE c2.vm_id = v.vm_id AND c2.vm_name ='external-HostedEngineLocal') 
			ORDER BY v.cpu_Usage_Percent DESC
		""", nativeQuery = true
	)
	fun findVmCpuChart(page: Pageable?): List<VmSamplesHistoryEntity>

	@Query(
		value =
		"""
			SELECT DISTINCT v.vm_id, v.* 
			FROM Vm_Samples_History v 
			JOIN vm_configuration c ON v.vm_id = c.vm_id WHERE v.vm_status = 1 
			AND v.history_Datetime = ( SELECT MAX(v2.history_Datetime) FROM Vm_Samples_History v2 WHERE v2.vm_Id = v.vm_Id) 
			AND NOT EXISTS (SELECT 1 FROM vm_configuration c2 WHERE c2.vm_id = v.vm_id AND c2.vm_name ='external-HostedEngineLocal') 
			ORDER BY v.memory_Usage_Percent DESC
		""", nativeQuery = true
	)
	fun findVmMemoryChart(page: Pageable?): List<VmSamplesHistoryEntity>


	// vm 사용량 순위
	fun findFirstByVmStatusOrderByCpuUsagePercentDesc(vmStatus: Int): List<VmSamplesHistoryEntity>

	@Query(
		value =
		"""
		  WITH RankedVMs AS (
			 SELECT *, 
				   ROW_NUMBER() OVER (PARTITION BY vm_id ORDER BY history_datetime DESC) AS rn
			 FROM vm_samples_history
			 WHERE cpu_usage_percent IS NOT NULL
				  AND vm_id NOT IN (SELECT vm_id 
									FROM vm_samples_history
									WHERE history_id = 1)
				  AND CAST(EXTRACT(MINUTE FROM history_datetime) AS INTEGER) % 10 = 0
		  ), 
		  LatestVMStatus AS (
			 SELECT vm_id, 
				   vm_status
			 FROM vm_samples_history
			 WHERE history_datetime = (SELECT MAX(history_datetime) 
									   FROM vm_samples_history AS sub
									   WHERE sub.vm_id = vm_samples_history.vm_id)
				  AND vm_id NOT IN (SELECT vm_id 
									FROM vm_samples_history
									WHERE history_id = 1)
		  )
		  SELECT *
		  FROM RankedVMs
		  JOIN LatestVMStatus ON RankedVMs.vm_id = LatestVMStatus.vm_id
		  WHERE RankedVMs.rn <= 10 
			AND LatestVMStatus.vm_status = 1
		  ORDER BY RankedVMs.vm_id, RankedVMs.history_datetime DESC
		""", nativeQuery = true
	)
	fun findVmUsageListChart(): List<VmSamplesHistoryEntity>
	//TODO findVmUsageListChart vmCpuPerList&vmMemoryPerList 가상머신 external-HostedEngineLocal (빈값) 에 대한 에러있음
	// 에러처리로 우선 history_id 1은 예외처리해서 검색x

	@Query(
		value =
		"SELECT DISTINCT v.vm_id, v.* FROM Vm_Samples_History v " +
				"JOIN vm_configuration c ON v.vm_id = c.vm_id WHERE v.vm_status = 1 " +
				"AND v.history_Datetime = ( SELECT MAX(v2.history_Datetime) FROM Vm_Samples_History v2 WHERE v2.vm_Id = v.vm_Id) " +
				"AND NOT EXISTS ( SELECT 1 FROM vm_configuration c2 WHERE c2.vm_id = v.vm_id AND c2.vm_name LIKE '%HostedEngineLocal%' ) " +
				"ORDER BY history_Datetime desc ",
		nativeQuery = true
	)
	fun findVmMetricListChart(): List<VmSamplesHistoryEntity>



}