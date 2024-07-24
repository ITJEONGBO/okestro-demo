package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import org.ovirt.engine.sdk4.types.*
import org.ovirt.engine.sdk4.Error
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

interface ItDataCenterService {
	/**
	 * [ItDataCenterService.findAll]
	 * 데이터센터 목록
	 *
	 * @return List<[DataCenterVo]> 데이터센터 목록
	 */
	@Throws(Error::class)
	fun findAll(): List<DataCenterVo>
	/**
	 * [ItDataCenterService.findOne]
	 * 데이터센터 정보
	 *
	 * @param dataCenterId [String] 데이터센터 id
	 * @return [DataCenterVo]?
	 */
	@Throws(Error::class)
	fun findOne(dataCenterId: String): DataCenterVo?
	/**
	 * [ItDataCenterService.add]
	 * 데이터센터 생성
	 *
	 * @param dataCenterVo [DataCenterVo]
	 * @return [DataCenterVo]?
	 */
	@Throws(Error::class)
	fun add(dataCenterVo: DataCenterVo): DataCenterVo?
	/**
	 * [ItDataCenterService.update]
	 * 데이터센터 수정
	 *
	 * @param dataCenterVo [DataCenterVo]
	 * @return [DataCenterVo]?
	 */
	@Throws(Error::class)
	fun update(dataCenterVo: DataCenterVo): DataCenterVo?
	/**
	 * [ItDataCenterService.remove]
	 * 데이터센터 삭제
	 *
	 * @param dataCenterId [String] 데이터센터 id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun remove(dataCenterId: String): Boolean
	/**
	 * [ItDataCenterService.findAllEventsBy]
	 * 데이터센터 이벤트 목록
	 *
	 * @param dataCenterId [String] 데이터센터 ID
	 * @return List<[EventVo]> 이벤트 목록
	 */
	@Throws(Error::class)
	fun findAllEventsBy(dataCenterId: String): List<EventVo>

	/**
	 * [ItDataCenterService.dashboardComputing]
	 * 대시보드 컴퓨팅 목록
	 */
	@Throws(Error::class)
	fun dashboardComputing(): List<DataCenterVo>
	/**
	 * [ItDataCenterService.dashboardNetwork]
	 * 대시보드 네트워크
	 */
	@Throws(Error::class)
	fun dashboardNetwork(): List<DataCenterVo>
	/**
	 * [ItDataCenterService.dashboardStorage]
	 * 대시보드 - 스토리지
	 */
	@Throws(Error::class)
	fun dashboardStorage(): List<DataCenterVo>
}

@Service
class DataCenterServiceImpl(

): BaseService(), ItDataCenterService {
	@Throws(Error::class)
	override fun findAll(): List<DataCenterVo> {
		log.info("findAll ... 데이터센터 목록")
		val dataCenters: List<DataCenter> =
			conn.findAllDataCenters()
				.getOrDefault(listOf())
//		return dataCenters.toDataCenterVos(conn) // java.lang.StackOverflowError
		return dataCenters.toDataCenterVoInfos()
	}

	@Throws(Error::class)
	override fun findOne(dataCenterId: String): DataCenterVo? {
		log.info("findOne ... dataCenterId: {}", dataCenterId)
		val res: DataCenter? =
			conn.findDataCenter(dataCenterId)
				.getOrNull()
		return res?.toDataCenterVoInfo()
	}

	@Throws(Error::class)
	override fun add(dataCenterVo: DataCenterVo): DataCenterVo? {
		log.info("add ... ")
		val res: DataCenter? =
			conn.addDataCenter(dataCenterVo.toAddDataCenterBuilder())
				.getOrNull()
		return res?.toDataCenterVoInfo()
	}

	@Throws(Error::class)
	override fun update(dataCenterVo: DataCenterVo): DataCenterVo? {
		log.info("update ... ")
		val res: DataCenter? =
			conn.updateDataCenter(dataCenterVo.toEditDataCenterBuilder())
				.getOrNull()
		return res?.toDataCenterVoInfo()
	}

	@Throws(Error::class)
	override fun remove(dataCenterId: String): Boolean {
		log.info("remove ... dataCenterId: {}", dataCenterId)
		val res: Result<Boolean> =
			conn.removeDataCenter(dataCenterId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun findAllEventsBy(dataCenterId: String): List<EventVo> {
		log.info("findAllEventsBy ... dataCenterId: {}", dataCenterId)
		val dataCenter: DataCenter? =
			conn.findDataCenter(dataCenterId)
				.getOrNull()
		val events: List<Event> =
			conn.findAllEvents()
				.getOrDefault(listOf())
				.filter { (
						it.dataCenterPresent() && (
							(it.dataCenter().idPresent() && it.dataCenter().id() == dataCenterId) ||
							(it.dataCenter().namePresent() && it.dataCenter().name() == dataCenter?.name())
						)
				)}
		return events.toEventVos()
	}

	@Throws(Error::class)
	override fun dashboardComputing(): List<DataCenterVo> {
		log.info("dashboardComputing ... ")
		val dataCenters: List<DataCenter> =
			conn.findAllDataCenters()
				.getOrDefault(listOf())
		return dataCenters.toDataCenterVos(conn, findNetworks = false, findStorageDomains = false, findClusters = true)
	}

	@Throws(Error::class)
	override fun dashboardNetwork(): List<DataCenterVo> {
		log.info("dashboardNetwork ... ")
		val dataCenters: List<DataCenter> =
			conn.findAllDataCenters()
				.getOrDefault(listOf())
		return dataCenters.toDataCenterVos(conn, findNetworks = true, findStorageDomains = false, findClusters = false)
	}

	@Throws(Error::class)
	override fun dashboardStorage(): List<DataCenterVo> {
		log.info("dashboardStorage ... ")
		val dataCenters: List<DataCenter> =
			conn.findAllDataCenters()
				.getOrDefault(listOf())
		return dataCenters.toDataCenterVos(conn, findNetworks = false, findStorageDomains = true, findClusters = false)
	}

	companion object {
		private val log by LoggerDelegate()
	}
}