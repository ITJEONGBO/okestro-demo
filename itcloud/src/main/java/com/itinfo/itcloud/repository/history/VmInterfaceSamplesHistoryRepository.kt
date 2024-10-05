package com.itinfo.itcloud.repository.history

import com.itinfo.itcloud.repository.history.entity.VmInterfaceSamplesHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface VmInterfaceSamplesHistoryRepository : JpaRepository<VmInterfaceSamplesHistoryEntity, Int> {
	fun findFirstByVmInterfaceIdOrderByHistoryDatetimeDesc(vnInterfaceId: UUID): VmInterfaceSamplesHistoryEntity
}
