package com.itinfo.itcloud.aaarepository

import com.itinfo.itcloud.aaarepository.entity.Settings
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util.UUID

@Repository
interface SettingsRepository: JpaRepository<Settings, Int> {
}