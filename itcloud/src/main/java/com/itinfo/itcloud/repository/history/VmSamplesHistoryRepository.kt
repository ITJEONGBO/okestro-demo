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
			select distinct v.vm_id, v.* 
			from vm_samples_history v 
			join vm_configuration c on v.vm_id = c.vm_id where v.vm_status = 1 
			and v.history_datetime = ( select max(v2.history_datetime) from vm_samples_history v2 where v2.vm_id = v.vm_id) 
			and not exists (select 1 from vm_configuration c2 where c2.vm_id = v.vm_id and c2.vm_name ='external-hostedenginelocal') 
			order by v.cpu_usage_percent desc
		""", nativeQuery = true
	)
	fun findVmCpuChart(page: Pageable?): List<VmSamplesHistoryEntity>

	@Query(
		value =
		"""
			select distinct v.vm_id, v.* 
			from vm_samples_history v 
			join vm_configuration c on v.vm_id = c.vm_id where v.vm_status = 1 
			and v.history_datetime = ( select max(v2.history_datetime) from vm_samples_history v2 where v2.vm_id = v.vm_id) 
			and not exists (select 1 from vm_configuration c2 where c2.vm_id = v.vm_id and c2.vm_name ='external-hostedenginelocal') 
			order by v.memory_usage_percent desc
		""", nativeQuery = true
	)
	fun findVmMemoryChart(page: Pageable?): List<VmSamplesHistoryEntity>


	// vm 사용량 순위
	fun findFirstByVmStatusOrderByCpuUsagePercentDesc(vmStatus: Int): List<VmSamplesHistoryEntity>

	@Query(
		value =
		"""
		  with rankedvms as (
			 select *, 
				   row_number() over (partition by vm_id order by history_datetime desc) as rn
			 from vm_samples_history
			 where cpu_usage_percent is not null
				  and vm_id not in (select vm_id 
									from vm_samples_history
									where history_id = 1)
				  and cast(extract(minute from history_datetime) as integer) % 10 = 0
		  ), 
		  latestvmstatus as (
			 select vm_id, 
				   vm_status
			 from vm_samples_history
			 where history_datetime = (select max(history_datetime) 
									   from vm_samples_history as sub
									   where sub.vm_id = vm_samples_history.vm_id)
				  and vm_id not in (select vm_id 
									from vm_samples_history
									where history_id = 1)
		  )
		  select *
		  from rankedvms
		  join latestvmstatus on rankedvms.vm_id = latestvmstatus.vm_id
		  where rankedvms.rn <= 10 
			and latestvmstatus.vm_status = 1
		  order by rankedvms.vm_id, rankedvms.history_datetime desc
		""", nativeQuery = true
	)
	fun findVmUsageListChart(): List<VmSamplesHistoryEntity>
	//TODO findVmUsageListChart vmCpuPerList&vmMemoryPerList 가상머신 external-HostedEngineLocal (빈값) 에 대한 에러있음
	// 에러처리로 우선 history_id 1은 예외처리해서 검색x

	@Query(
		value =
		"""
			select distinct v.vm_id, v.* from vm_samples_history v
			join vm_configuration c on v.vm_id = c.vm_id where v.vm_status = 1 
			and v.history_datetime = ( select max(v2.history_datetime) from vm_samples_history v2 where v2.vm_id = v.vm_id) 
			and not exists ( select 1 from vm_configuration c2 where c2.vm_id = v.vm_id and c2.vm_name like '%hostedenginelocal%' )
			order by history_datetime desc 
		""",
		nativeQuery = true
	)
	fun findVmMetricListChart(): List<VmSamplesHistoryEntity>



}