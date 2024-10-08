package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.NetworkVo
import com.itinfo.itcloud.model.network.toNetworkVos
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.setting.toPermissionVos
import com.itinfo.itcloud.model.storage.StorageDomainVo
import com.itinfo.itcloud.model.storage.toStorageDomainsMenu
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import org.ovirt.engine.sdk4.types.*
import org.ovirt.engine.sdk4.Error
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

interface ItDataCenterService {
	/**
	 * [ItDataCenterService.findAll]
	 * 데이터센터 전체 목록
	 *
	 * @return List<[DataCenterVo]> 데이터센터 목록
	 */
	@Throws(Error::class)
	fun findAll(): List<DataCenterVo>
	/**
	 * [ItDataCenterService.findOne]
	 * 데이터센터 정보 (편집창)
	 *
	 * @param dataCenterId [String] 데이터센터 Id
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
	 * 데이터센터 편집
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
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun remove(dataCenterId: String): Boolean

	/**
	 * [ItDataCenterService.findAllClustersFromDataCenter]
	 * 데이터센터가 가지고있는 클러스터 목록
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return List<[ClusterVo]> 클러스터 목록
	 */
	@Throws(Error::class)
	fun findAllClustersFromDataCenter(dataCenterId: String): List<ClusterVo>
	// 생성
	// 편집
	// 삭제

	/**
	 * [ItDataCenterService.findAllStorageDomainsFromDataCenter]
	 * 데이터센터가 가지고있는 스토리지 도메인 목록
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return List<[StorageDomainVo]> 스토리지 도메인 목록
	 */
	@Throws(Error::class)
	fun findAllStorageDomainsFromDataCenter(dataCenterId: String): List<StorageDomainVo>
	// 생성
	// 분리
	// 활성
	// 유지보수

	/**
	 * [ItDataCenterService.findAllNetworksFromDataCenter]
	 * 데이터센터가 가지고있는 네트워크 목록
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return List<[NetworkVo]> 네트워크 목록
	 */
	@Throws(Error::class)
	fun findAllNetworksFromDataCenter(dataCenterId: String): List<NetworkVo>
	// 생성
	// 편집
	// 삭제

	/**
	 * [ItDataCenterService.findAllEventsFromDataCenter]
	 * 데이터센터가 가지고 있는 이벤트 목록
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return List<[EventVo]> 이벤트 목록
	 */
	@Throws(Error::class)
	fun findAllEventsFromDataCenter(dataCenterId: String): List<EventVo>


	/**
	 * [ItDataCenterService.findAllHostsFromDataCenter]
	 * 데이터센터가 가지고있는 호스트 목록
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return List<[HostVo]> 호스트 목록
	 */
	@Deprecated("필요없음")
	@Throws(Error::class)
	fun findAllHostsFromDataCenter(dataCenterId: String): List<HostVo>
	/**
	 * [ItDataCenterService.findAllVmsFromDataCenter]
	 * 데이터센터가 가지고있는 가상머신 목록
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return List<[VmVo]> 가상머신 목록
	 */
	@Deprecated("필요없음")
	@Throws(Error::class)
	fun findAllVmsFromDataCenter(dataCenterId: String): List<VmVo>
	/**
	 * [ItDataCenterService.findAllPermissionsFromDataCenter]
	 * 데이터센터가 가지고 있는 권한 목록
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return List<[PermissionVo]> 권한 목록
	 */
	@Deprecated("필요없음")
	@Throws(Error::class)
	fun findAllPermissionsFromDataCenter(dataCenterId: String): List<PermissionVo>

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
	override fun findAllClustersFromDataCenter(dataCenterId: String): List<ClusterVo> {
		log.info("findAllClustersFromDataCenter ... dataCenterId: {}", dataCenterId)
		val res: List<Cluster> =
			conn.findAllClustersFromDataCenter(dataCenterId)
				.getOrDefault(listOf())
				.filter { it.cpuPresent() }
		return res.toClustersMenu(conn)
	}

	@Throws(Error::class)
	override fun findAllStorageDomainsFromDataCenter(dataCenterId: String): List<StorageDomainVo> {
		log.info("findAllStorageDomainsFromDataCenter ... dataCenterId: {}", dataCenterId)
		val res: List<StorageDomain> =
			conn.findAllAttachedStorageDomainsFromDataCenter(dataCenterId)
				.getOrDefault(listOf())
		return res.toStorageDomainsMenu(conn)
	}

	@Throws(Error::class)
	override fun findAllNetworksFromDataCenter(dataCenterId: String): List<NetworkVo> {
		log.info("findAllNetworksFromDataCenter ... dataCenterId: {}", dataCenterId)
		val res: List<Network> =
			conn.findAllNetworks()
				.getOrDefault(listOf())
				.filter { it.dataCenter().id() == dataCenterId }
		return res.toNetworkVos(conn)
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


	@Deprecated("필요없음")
	@Throws(Error::class)
	override fun findAllHostsFromDataCenter(dataCenterId: String): List<HostVo> {
		log.debug("findAllHostsFromDataCenter ... dataCenterId: {}", dataCenterId)
		val res: List<Host> =
			conn.findAllHostsFromDataCenter(dataCenterId)
				.getOrDefault(listOf())
//		return res.toHostsMenu(conn)
		TODO()
	}

	@Deprecated("필요없음")
	@Throws(Error::class)
	override fun findAllVmsFromDataCenter(dataCenterId: String): List<VmVo> {
		log.debug("findAllVmsFromDataCenter ... dataCenterId: {}", dataCenterId)
		val res: List<Vm> =
			conn.findAllVmsFromDataCenter(dataCenterId)
				.getOrDefault(listOf())
//		return res.toVmsMenu(conn)
		TODO()
	}

	@Deprecated("필요없음")
	@Throws(Error::class)
	override fun findAllPermissionsFromDataCenter(dataCenterId: String): List<PermissionVo> {
		log.info("findAllPermissionsFromDataCenter ... dataCenterId: {}", dataCenterId)
		val res: List<Permission> =
			conn.findAllPermissionsFromDataCenter(dataCenterId)
				.getOrDefault(listOf())
		return res.toPermissionVos(conn)
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