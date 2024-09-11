package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.error.toError
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
	 * 데이터센터 정보 (편집을 위해 존재)
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
	 * [ItDataCenterService.findAllEventsFromDataCenter]
	 * 데이터센터 이벤트 목록
	 *
	 * @param dataCenterId [String] 데이터센터 ID
	 * @return List<[EventVo]> 이벤트 목록
	 */
	@Throws(Error::class)
	fun findAllEventsFromDataCenter(dataCenterId: String): List<EventVo>

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
		log.info("findAll ... ")
		val res: List<DataCenter> =
			conn.findAllDataCenters()
				.getOrDefault(listOf())
		return res.toDataCentersMenu(conn)
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
	override fun findAllEventsFromDataCenter(dataCenterId: String): List<EventVo> {
		log.info("findAllEventsFromDataCenter ... dataCenterId: {}", dataCenterId)
		val dataCenter: DataCenter =
			conn.findDataCenter(dataCenterId)
				.getOrNull() ?: throw ErrorPattern.DATACENTER_NOT_FOUND.toException()
		val res: List<Event> =
			conn.findAllEvents()
				.getOrDefault(listOf())
				.filter { (
						it.dataCenterPresent() && (
							(it.dataCenter().idPresent() && it.dataCenter().id() == dataCenterId) ||
							(it.dataCenter().namePresent() && it.dataCenter().name() == dataCenter.name())
						)
				)}
		return res.toEventVos()
	}

	@Throws(Error::class)
	override fun dashboardComputing(): List<DataCenterVo> {
		log.info("dashboardComputing ... ")
		val res: List<DataCenter> =
			conn.findAllDataCenters()
				.getOrDefault(listOf())
		return res.toDataCenterVos(conn, findNetworks = false, findStorageDomains = false, findClusters = true)
	}

	@Throws(Error::class)
	override fun dashboardNetwork(): List<DataCenterVo> {
		log.info("dashboardNetwork ... ")
		val res: List<DataCenter> =
			conn.findAllDataCenters()
				.getOrDefault(listOf())
		return res.toDataCenterVos(conn, findNetworks = true, findStorageDomains = false, findClusters = false)
	}

	@Throws(Error::class)
	override fun dashboardStorage(): List<DataCenterVo> {
		log.info("dashboardStorage ... ")
		val res: List<DataCenter> =
			conn.findAllDataCenters()
				.getOrDefault(listOf())
		return res.toDataCenterVos(conn, findNetworks = false, findStorageDomains = true, findClusters = false)
	}

	companion object {
		private val log by LoggerDelegate()
	}
}