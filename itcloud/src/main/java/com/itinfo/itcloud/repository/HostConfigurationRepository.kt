package com.itinfo.itcloud.repository

import com.itinfo.itcloud.repository.entity.HostConfigurationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface HostConfigurationRepository : JpaRepository<HostConfigurationEntity, Int> {
	fun findFirstByHostIdOrderByUpdateDateDesc(hostId: UUID): HostConfigurationEntity
}
