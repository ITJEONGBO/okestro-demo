package com.itinfo.dao

import com.itinfo.common.LoggerDelegate
import com.itinfo.dao.history.*
import com.itinfo.model.VmDeviceVo
import com.itinfo.model.VmNetworkUsageVo
import com.itinfo.model.VmUsageVo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ComputingDao {
	@Autowired private lateinit var vmSampleHistoryRepository: VmSamplesHistoryRepository
	@Autowired private lateinit var vmInterfaceSamplesHistoryRepository: VmInterfaceSamplesHistoryRepository
	@Autowired private lateinit var vmDeviceHistoryRepository: VmDeviceHistoryRepository

	fun retrieveVmUsage(vmId: String): List<VmUsageVo> {
		log.info("... retrieveVmUsage('$vmId')")
		val itemsFound: List<VmSamplesHistory> =
			vmSampleHistoryRepository.findByVmIdOrderByHistoryDatetimeDesc(vmId.toUUID())
		log.info("itemsFound: $itemsFound")
		return itemsFound.toVmUsageVos().also { log.debug("returning ... $it") }
		// return this.sqlSessionTemplate.selectList("COMPUTING.retrieveVmUsage", id);
	}

	fun retrieveVmUsageOne(vmId: String): VmUsageVo? {
		log.info("... retrieveVmUsageOne('$vmId')")
		return retrieveVmUsage(vmId).firstOrNull()?.also { log.debug("returning ... \n$it") }
		// return (VmUsageVo)this.sqlSessionTemplate.selectOne("COMPUTING.retrieveVmUsageOne", id);
	}

	fun retrieveVmNetworkUsage(vmIds: List<String>): List<VmNetworkUsageVo> {
		log.info("... retrieveVmNetworkUsage > vmIds: $vmIds")
		val itemsFound: List<VmInterfaceSamplesHistory> =
			vmInterfaceSamplesHistoryRepository.findByVmInterfaceIdIn(vmIds.toUUIDs())
		log.debug("itemsFound: $itemsFound")
		return itemsFound
			.toDashboardStatistics()
			.toVmNetworkUsageVos().also { log.debug("returning ... $it") }
		// return this.sqlSessionTemplate.selectList("COMPUTING.retrieveVmNetworkUsage", ids);
	}

	fun retrieveVmNetworkUsageOne(vmIds: List<String>): VmNetworkUsageVo? {
		log.info("... retrieveVmNetworkUsageOne > vmIds: $vmIds")
		return retrieveVmNetworkUsage(vmIds).firstOrNull()?.also { log.debug("returning ... \n$it") }
		// return (VmNetworkUsageVo)this.sqlSessionTemplate.selectOne("COMPUTING.retrieveVmNetworkUsageOne", ids);
	}

	fun retrieveVmDevices(vmId: String): List<VmDeviceVo> {
		log.info("... ")
		val itemsFound: List<VmDeviceHistory> =
			vmDeviceHistoryRepository.findByVmIdOrderByUpdateDateAsc(vmId.toUUID())
		log.info("itemsFound: $itemsFound")
		return itemsFound.toVmDeviceVos().also { log.debug("returning ... \n$it") }
		// return this.sqlSessionTemplate.selectList("COMPUTING.retrieveVmDevices", id);
	}

	companion object {
		private val log by LoggerDelegate()
	}
}