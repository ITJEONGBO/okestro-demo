package com.itinfo.itcloud.repository.history

import com.itinfo.itcloud.repository.history.entity.VmInterfaceSamplesHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface VmInterfaceSamplesHistoryRepository : JpaRepository<VmInterfaceSamplesHistoryEntity, Int> {
	fun findFirstByVmInterfaceIdOrderByHistoryDatetimeDesc(vnInterfaceId: UUID): VmInterfaceSamplesHistoryEntity

	@Query(
		value =
			"""
				WITH RankedNetworkUsage AS (
					SELECT 
						vic.vm_id,
						vi.*,
						SUM((vi.receive_rate_percent + vi.transmit_rate_percent) / 2) OVER (PARTITION BY vic.vm_id) AS network_usage_per,
						ROW_NUMBER() OVER (PARTITION BY vic.vm_id ORDER BY vi.history_datetime DESC) AS rn
					FROM vm_interface_samples_history vi
					JOIN vm_interface_configuration vic ON vic.vm_interface_id = vi.vm_interface_id
					WHERE vi.receive_rate_percent IS NOT NULL 
					  AND vi.transmit_rate_percent IS NOT NULL
					  AND CAST(EXTRACT(MINUTE FROM vi.history_datetime) AS INTEGER) % 10 = 0
				)
				SELECT *
				FROM RankedNetworkUsage
				WHERE rn <= 10
				ORDER BY vm_id, history_datetime DESC
			""",
		nativeQuery = true
	)
	fun findVmNetworkMetricListChart(): List<VmInterfaceSamplesHistoryEntity>

}
