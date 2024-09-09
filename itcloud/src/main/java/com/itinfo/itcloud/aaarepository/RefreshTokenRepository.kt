package com.itinfo.itcloud.aaarepository

import com.itinfo.itcloud.aaarepository.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository: JpaRepository<RefreshToken, Int> {
	fun findByExternalId(externalId: String): RefreshToken?
}