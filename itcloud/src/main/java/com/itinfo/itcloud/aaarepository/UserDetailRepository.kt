package com.itinfo.itcloud.aaarepository

import com.itinfo.itcloud.aaarepository.entity.UserDetail
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserDetailRepository: JpaRepository<UserDetail, UUID> {
	fun findByExternalId(externalId: String): UserDetail?
}