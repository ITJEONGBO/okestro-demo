package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.fromHostsToIdentifiedVos
import com.itinfo.itcloud.model.network.NetworkVo
import com.itinfo.itcloud.model.network.toNetworkVos
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.setting.toPermissionVos
import com.itinfo.itcloud.model.storage.StorageDomainVo
import com.itinfo.itcloud.model.storage.toStorageDomainVos
import com.itinfo.itcloud.service.BaseService
import com.itinfo.itcloud.service.network.ItNetworkService
import com.itinfo.itcloud.service.network.NetworkServiceImpl
import com.itinfo.itcloud.service.storage.ItStorageService
import com.itinfo.itcloud.service.storage.StorageServiceImpl
import com.itinfo.itcloud.service.storage.StorageServiceImpl.Companion
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
	 * 데이터센터 정보 (편집)
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
	 * @param dataCenterId [String] 데이터센터 id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun remove(dataCenterId: String): Boolean
	/**
	 * [ItDataCenterService.findAllClusterFromDataCenter]
	 * 데이터센터가 가지고있는 클러스터 목록
	 *
	 * @param dataCenterId [String]
	 * @return List<[ClusterVo]> 클러스터 목록
	 */
	@Throws(Error::class)
	fun findAllClusterFromDataCenter(dataCenterId: String): List<ClusterVo>
	/**
	 * [ItDataCenterService.findAllHostsFromDataCenter]
	 * 데이터센터가 가지고있는 호스트 목록
	 *
	 * @param dataCenterId [String]
	 * @return 호스트 목록
	 */
	@Throws(Error::class)
	fun findAllHostsFromDataCenter(dataCenterId: String): List<HostVo>
	/**
	 * [ItDataCenterService.findAllVmsFromDataCenter]
	 * 데이터센터가 가지고있는 가상머신 목록
	 *
	 * @param dataCenterId [String]
	 * @return 가상머신 목록
	 */
	@Throws(Error::class)
	fun findAllVmsFromDataCenter(dataCenterId: String): List<VmVo>
	/**
	 * [ItDataCenterService.findAllNetworkFromDataCenter]
	 * 네트워크 목록
	 *
	 * @param dataCenterId [String]
	 * @return List<[NetworkVo]> 네트워크 목록
	 */
	@Throws(Error::class)
	fun findAllNetworkFromDataCenter(dataCenterId: String): List<NetworkVo>
	/**
	 * [ItDataCenterService.findAllDomainsFromDataCenter]
	 * 데이터센터 - 스토리지 도메인 목록
	 *
	 * @param dataCenterId [String] 데이터센터 밑에 있는 도메인 목록
	 * @return List<[StorageDomainVo]> 스토리지 도메인 목록
	 */
	@Throws(Error::class)
	fun findAllDomainsFromDataCenter(dataCenterId: String): List<StorageDomainVo>
	/**
	 * [ItDataCenterService.findAllPermissionsFromDataCenter]
	 *
	 * @param dataCenterId [String] 네트워크 아이디
	 * @return List<[PermissionVo]>
	 */
	@Throws(Error::class)
	fun findAllPermissionsFromDataCenter(dataCenterId: String): List<PermissionVo>
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

	// cluster 출력
	@Throws(Error::class)
	override fun findAllClusterFromDataCenter(dataCenterId: String): List<ClusterVo> {
		log.info("findAllClusterFromDataCenter ... ")
		val res: List<Cluster> =
			conn.findAllClustersFromDataCenter(dataCenterId)
				.getOrDefault(listOf())
				.filter { it.cpuPresent() }
		return res.toClustersMenu(conn)
	}

	@Throws(Error::class)
	override fun findAllHostsFromDataCenter(dataCenterId: String): List<HostVo> {
		log.debug("findAllHostsFromDataCenter ... dataCenterId: $dataCenterId")
		val res: List<Host> =
			conn.findAllHostsFromDataCenter(dataCenterId)
				.getOrDefault(listOf())
		return res.toHostsMenu(conn)
	}

	override fun findAllVmsFromDataCenter(dataCenterId: String): List<VmVo> {
		TODO("Not yet implemented")
	}

	override fun findAllNetworkFromDataCenter(dataCenterId: String): List<NetworkVo> {
		log.info("findAllFromDataCenter ... ")
		val networks: List<Network> =
			conn.findAllNetworks()
				.getOrDefault(listOf())
				.filter { it.dataCenter().id() == dataCenterId }
		return networks.toNetworkVos(conn)
	}

	override fun findAllDomainsFromDataCenter(dataCenterId: String): List<StorageDomainVo> {
		TODO("Not yet implemented")
	}

	override fun findAllPermissionsFromDataCenter(dataCenterId: String): List<PermissionVo> {
		log.info("getPermissionsByNetwork ... ")
		val permissions: List<Permission> =
			conn.findAllPermissionsFromDisk(dataCenterId)
				.getOrDefault(listOf())
		return permissions.toPermissionVos(conn)
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