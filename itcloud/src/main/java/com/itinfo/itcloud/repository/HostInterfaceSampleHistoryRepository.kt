package com.itinfo.itcloud.repository

import com.itinfo.itcloud.repository.entity.HostInterfaceSamplesHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface HostInterfaceSampleHistoryRepository : JpaRepository<HostInterfaceSamplesHistoryEntity, Int> {
	// 호스트 - 네트워크
	fun findFirstByHostInterfaceIdOrderByHistoryDatetimeDesc(hostInterfaceId: UUID): HostInterfaceSamplesHistoryEntity
}