package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.computing.DashBoardVo
import com.itinfo.itcloud.model.computing.toDashboardVo
import com.itinfo.itcloud.repository.*
import com.itinfo.itcloud.repository.dto.*
import com.itinfo.itcloud.repository.entity.*
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.findAllHosts
import com.itinfo.util.ovirt.findAllStorageDomains
import org.ovirt.engine.sdk4.services.ClusterService
import org.ovirt.engine.sdk4.types.Host
import org.ovirt.engine.sdk4.types.StorageDomain
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

interface ItGraphService {
	fun getDashboard(): DashBoardVo
	/**
	 * 전체 사용량 - Host (CPU, Memory  % ) 원 그래프
	 * @return 5분마다 한번씩 불려지게 해야함
	 */
	fun totalCpuMemory(): HostUsageDto
	/**
	 * 전체 사용량 - Storage % 원 그래프
	 * @return 스토리지 사용량
	 */
	fun totalStorage(): StorageUsageDto
	/**
	 * 전체 사용량(CPU, Memory %) 선 그래프
	 * @param hostId 호스트 id
	 * @return 10분마다 그래프에 찍히게?
	 */
	fun totalCpuMemoryList(hostId: UUID, limit: Int): List<HostUsageDto>
	// 사용량 top 3
	/**
	 * 가상머신 cpu 사용량 3
	 * @return
	 */
	fun vmCpuChart(): List<UsageDto>
	/**
	 * 가상머신 memory 사용량 3
	 * @return
	 */
	fun vmMemoryChart(): List<UsageDto>
	/**
	 * % 기준? GB 기준?
	 */
	fun storageChart(): List<UsageDto>

	// 호스트 사용량 top3
	fun hostCpuChart(): List<UsageDto>
	fun hostMemoryChart(): List<UsageDto>

	// 호스트 목록 - 그래프
	fun hostPercent(hostId: String, hostNicId: String): UsageDto

	// 가상머신 목록 - 그래프
	fun vmPercent(vmId: String, vmNicId: String): UsageDto
}

@Service
class GraphServiceImpl(

): BaseService(), ItGraphService {
	@Autowired private lateinit var hostSamplesHistoryRepository: HostSamplesHistoryRepository
	@Autowired private lateinit var hostInterfaceSampleHistoryRepository: HostInterfaceSampleHistoryRepository
	@Autowired private lateinit var vmSamplesHistoryRepository: VmSamplesHistoryRepository
	@Autowired private lateinit var vmInterfaceSamplesHistoryRepository: VmInterfaceSamplesHistoryRepository
	@Autowired private lateinit var storageDomainSamplesHistoryRepository: StorageDomainSamplesHistoryRepository

	override fun getDashboard(): DashBoardVo {
		log.info("getDashboard ... ")
		return conn.toDashboardVo()
	}

	override fun totalCpuMemory(): HostUsageDto {
		log.info("totalCpuMemory ... ")
		val hosts: List<Host> =
			conn.findAllHosts()
				.getOrDefault(listOf())
		val hostSamplesHistoryEntities: List<HostSamplesHistoryEntity> = hosts.map {
			hostSamplesHistoryRepository.findFirstByHostIdOrderByHistoryDatetimeDesc(UUID.fromString(it.id()))
		}
		return hosts.toHostUsageDto(conn, hostSamplesHistoryEntities)
	}

	override fun totalStorage(): StorageUsageDto {
		log.info("totalStorage ... ")
		val storageDomains: List<StorageDomain> =
			conn.findAllStorageDomains()
				.getOrDefault(listOf())
		return storageDomains.toStorageUsageDto(conn)
	}

	override fun totalCpuMemoryList(hostId: UUID, limit: Int): List<HostUsageDto> {
		log.info("totalCpuMemoryList ... ")
		val page: Pageable = PageRequest.of(0, limit)
		val hostSamplesHistoryEntities: List<HostSamplesHistoryEntity> =
			hostSamplesHistoryRepository.findByHostIdOrderByHistoryDatetimeDesc(hostId, page)
		val host: Host =
			conn.findAllHosts("id=$hostId")
				.getOrDefault(listOf())
				.firstOrNull() ?: run {
			log.warn("totalCpuMemoryList ... no host FOUND!")
			return listOf()
		}
		return hostSamplesHistoryEntities.toTotalCpuMemoryUsages(conn, host)
	}

	override fun vmCpuChart(): List<UsageDto> {
		log.info("vmCpuChart ... ")
		val page: Pageable = PageRequest.of(0, 3)
		val vmSampleHistoryEntities: List<VmSamplesHistoryEntity> = vmSamplesHistoryRepository.findVmCpuChart(page)
		return vmSampleHistoryEntities.toVmCpuUsageDtos(conn)
	}

	override fun vmMemoryChart(): List<UsageDto> {
		log.info("vmMemoryChart ... ")
		val page: Pageable = PageRequest.of(0, 3)
		val vmSampleHistoryEntities: List<VmSamplesHistoryEntity> = vmSamplesHistoryRepository.findVmMemoryChart(page)
		return vmSampleHistoryEntities.toVmMemUsageDtos(conn)
			.filterNotNull()
	}

	override fun storageChart(): List<UsageDto> {
		log.info("storageChart ... ")
		val page: Pageable = PageRequest.of(0, 3)
		val storageDomainSampleHistoryEntities: List<StorageDomainSamplesHistoryEntity> = storageDomainSamplesHistoryRepository.findStorageChart(page)
		return storageDomainSampleHistoryEntities.toStorageCharts(conn)
	}

	override fun hostCpuChart(): List<UsageDto> {
		log.info("hostCpuChart ... ")
		val page: Pageable = PageRequest.of(0, 3)
		val hostSampleHistoryEntities: List<HostSamplesHistoryEntity> = hostSamplesHistoryRepository.findHostCpuChart(page)
		return hostSampleHistoryEntities.toHostCpuCharts(conn)
	}

	override fun hostMemoryChart(): List<UsageDto> {
		log.info("hostMemoryChart ... ")
		val page: Pageable = PageRequest.of(0, 3)
		val hostSampleHistoryEntities: List<HostSamplesHistoryEntity> = hostSamplesHistoryRepository.findHostMemoryChart(page)
		return hostSampleHistoryEntities.toHostMemCharts(conn)
	}

	override fun hostPercent(hostId: String, hostNicId: String): UsageDto {
		log.info("hostPercent ... ")
		val hostSampleHistoryEntity: HostSamplesHistoryEntity =
			hostSamplesHistoryRepository.findFirstByHostIdOrderByHistoryDatetimeDesc(UUID.fromString(hostId))
		val usageDto = hostSampleHistoryEntity.getUsage()
		val hostInterfaceSamplesHistoryEntity: HostInterfaceSamplesHistoryEntity =
			hostInterfaceSampleHistoryRepository.findFirstByHostInterfaceIdOrderByHistoryDatetimeDesc(UUID.fromString(hostNicId))
		val networkRate = hostInterfaceSamplesHistoryEntity.receiveRatePercent.toInt()
		usageDto.networkPercent = networkRate
		return usageDto
	}

	override fun vmPercent(vmId: String, vmNicId: String): UsageDto {
		log.info("vmPercent ... ")
		val vmSamplesHistoryEntity =
			vmSamplesHistoryRepository.findFirstByVmIdOrderByHistoryDatetimeDesc(UUID.fromString(vmId))
		val usageDto = vmSamplesHistoryEntity.getUsage()
		val vmInterfaceSamplesHistoryEntity: VmInterfaceSamplesHistoryEntity =
			vmInterfaceSamplesHistoryRepository.findFirstByVmInterfaceIdOrderByHistoryDatetimeDesc(UUID.fromString(vmNicId))
		val networkRate = vmInterfaceSamplesHistoryEntity.receiveRatePercent.toInt()
		usageDto.networkPercent = networkRate
		return usageDto
	}

	companion object {
		private val log by LoggerDelegate()
	}
}