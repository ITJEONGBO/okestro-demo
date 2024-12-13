package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.NetworkVo
import com.itinfo.itcloud.model.network.toNetworkVos
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.setting.toPermissionVos
import com.itinfo.itcloud.model.storage.*
import com.itinfo.itcloud.repository.engine.DiskVmElementRepository
import com.itinfo.itcloud.repository.engine.entity.DiskVmElementEntity
import com.itinfo.itcloud.repository.engine.entity.toVmId
import com.itinfo.itcloud.repository.history.dto.UsageDto
import com.itinfo.itcloud.service.BaseService
import com.itinfo.itcloud.service.storage.StorageServiceImpl
import com.itinfo.itcloud.service.storage.StorageServiceImpl.Companion
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import org.ovirt.engine.sdk4.types.*
import org.ovirt.engine.sdk4.Error
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
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
	// 생성, 편집, 삭제

	/**
	 * [ItDataCenterService.findAllHostsFromDataCenter]
	 * 데이터센터가 가지고있는 호스트 목록
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return List<[HostVo]> 호스트 목록
	 */
	@Throws(Error::class)
	fun findAllHostsFromDataCenter(dataCenterId: String): List<HostVo>
	// 생성, 편집, 삭제, 유지보수, 활성, 인증서 등록, 호스트 네트워크 복사

	/**
	 * [ItDataCenterService.findAllVmsFromDataCenter]
	 * 데이터센터가 가지고있는 가상머신 목록
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return List<[VmVo]> 가상머신 목록
	 */
	@Throws(Error::class)
	fun findAllVmsFromDataCenter(dataCenterId: String): List<VmVo>
	// 생성, 편집, 삭제, 실행, 일시중지, 종료, 호스트 네트워크 복사, 재부팅, 콘솔, 템플릿 목록, 스냅샷 생성, 마이그레이션, OVA로 내보내기

	/**
	 * [ItDataCenterService.findAllStorageDomainsFromDataCenter]
	 * 데이터센터가 가지고있는 스토리지 도메인 목록
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return List<[StorageDomainVo]> 스토리지 도메인 목록
	 */
	@Throws(Error::class)
	fun findAllStorageDomainsFromDataCenter(dataCenterId: String): List<StorageDomainVo>
	/**
	 * [ItDataCenterService.findAllAcitveStorageDomainsFromDataCenter]
	 * 데이터센터가 가지고있는 활성화 된 스토리지 도메인 목록
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return List<[StorageDomainVo]> 스토리지 도메인 목록
	 */
	@Throws(Error::class)
	fun findAllAcitveStorageDomainsFromDataCenter(dataCenterId: String): List<StorageDomainVo>
	// 생성, 분리, 활성, 유지보수
	/**
	 * [ItDataCenterService.findAllDisksFromDataCenter]
	 * 스토리지 도메인 - 디스크 목록 (데이터센터가 가진)
	 * 데이터센터 - 스토리지도메인이 가진 디스크
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return List<[DiskImageVo]> 디스크 목록
	 */
	@Throws(Error::class)
	fun findAllDisksFromDataCenter(dataCenterId: String): List<DiskImageVo>

	/**
	 * [ItDataCenterService.findAllNetworksFromDataCenter]
	 * 데이터센터가 가지고있는 네트워크 목록
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return List<[NetworkVo]> 네트워크 목록
	 */
	@Throws(Error::class)
	fun findAllNetworksFromDataCenter(dataCenterId: String): List<NetworkVo>
	// 생성, 편집, 삭제

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
	@Autowired private lateinit var diskVmElementRepository: DiskVmElementRepository
	@Autowired private lateinit var itGraphService: ItGraphService

	@Throws(Error::class)
	override fun findAll(): List<DataCenterVo> {
		log.info("findAll ... ")
		val res: List<DataCenter> = conn.findAllDataCenters()
			.getOrDefault(listOf())
		return res.toDataCentersMenu(conn)
	}

	@Throws(Error::class)
	override fun findOne(dataCenterId: String): DataCenterVo? {
		log.info("findOne ... dataCenterId: {}", dataCenterId)
		val res: DataCenter? = conn.findDataCenter(dataCenterId)
			.getOrNull()
		return res?.toDataCenterVoInfo()
	}

	@Throws(Error::class)
	override fun add(dataCenterVo: DataCenterVo): DataCenterVo? {
		log.info("add ... ")
		val addBuilder = dataCenterVo.toAddDataCenterBuilder()
		val res: DataCenter? = conn.addDataCenter(addBuilder)
			.getOrNull()
		return res?.toDataCenterVoInfo()
	}

	@Throws(Error::class)
	override fun update(dataCenterVo: DataCenterVo): DataCenterVo? {
		log.info("update ... ")
		val editBuilder = dataCenterVo.toEditDataCenterBuilder()
		val res: DataCenter? = conn.updateDataCenter(editBuilder)
			.getOrNull()
		return res?.toDataCenterVoInfo()
	}

	@Throws(Error::class)
	override fun remove(dataCenterId: String): Boolean {
		log.info("remove ... dataCenterId: {}", dataCenterId)
		val res: Result<Boolean> = conn.removeDataCenter(dataCenterId)
		return res.isSuccess
	}


	@Throws(Error::class)
	override fun findAllClustersFromDataCenter(dataCenterId: String): List<ClusterVo> {
		log.info("findAllClustersFromDataCenter ... dataCenterId: {}", dataCenterId)
		val res: List<Cluster> = conn.findAllClustersFromDataCenter(dataCenterId)
			.getOrDefault(listOf())
//			.filter { it.cpuPresent() }
		return res.toClustersMenu(conn)
	}

	@Throws(Error::class)
	override fun findAllHostsFromDataCenter(dataCenterId: String): List<HostVo> {
		log.debug("findAllHostsFromDataCenter ... dataCenterId: {}", dataCenterId)
		val res: List<Host> = conn.findAllHostsFromDataCenter(dataCenterId)
			.getOrDefault(listOf())
			.filter { it.status() == HostStatus.UP }

		return res.map { host ->
			val hostNic: HostNic? = conn.findAllNicsFromHost(host.id())
				.getOrDefault(listOf()).firstOrNull()

			val usageDto: UsageDto? = if(host.status() == HostStatus.UP && hostNic != null) {
				itGraphService.hostPercent(host.id(), hostNic.id())
			} else {
				null
			}
			host.toHostMenu(conn, usageDto)
		}
	}

	@Throws(Error::class)
	override fun findAllVmsFromDataCenter(dataCenterId: String): List<VmVo> {
		log.debug("findAllVmsFromDataCenter ... dataCenterId: {}", dataCenterId)
		val res: List<Vm> = conn.findAllVmsFromDataCenter(dataCenterId)
			.getOrDefault(listOf())
		return res.toVmsMenu(conn)
	}

	@Throws(Error::class)
	override fun findAllStorageDomainsFromDataCenter(dataCenterId: String): List<StorageDomainVo> {
		log.info("findAllStorageDomainsFromDataCenter ... dataCenterId: {}", dataCenterId)
		val res: List<StorageDomain> = conn.findAllAttachedStorageDomainsFromDataCenter(dataCenterId)
			.getOrDefault(listOf())
		return res.toStorageDomainsMenu(conn)
	}

	@Throws(Error::class)
	override fun findAllAcitveStorageDomainsFromDataCenter(dataCenterId: String): List<StorageDomainVo> {
		log.info("findAllFromDataCenter ... dcId: $dataCenterId")
		val res: List<StorageDomain> =
			conn.findAllAttachedStorageDomainsFromDataCenter(dataCenterId).getOrDefault(listOf()).filter { it.status() == StorageDomainStatus.ACTIVE }
		return res.toActiveDomains(conn)
	}


	@Throws(Error::class)
	override fun findAllDisksFromDataCenter(dataCenterId: String): List<DiskImageVo> {
		val res: List<Disk> = conn.findAllAttachedStorageDomainsFromDataCenter(dataCenterId)
			.getOrDefault(listOf())
			.flatMap { conn.findAllDisksFromStorageDomain(it.id()).getOrDefault(listOf()) }

		return res.map { disk ->
			val diskVmElementEntityOpt: Optional<DiskVmElementEntity> =
				diskVmElementRepository.findByDiskId(UUID.fromString(disk.id()))

			// ID가 템플릿일 수도, VM일 수도 있으므로 먼저 템플릿과 VM을 구분하여 조회
			val id: String = diskVmElementEntityOpt.map { it.toVmId() }.orElse("")
			disk.toDiskMenu(conn, id)
		}
	}

	@Throws(Error::class)
	override fun findAllNetworksFromDataCenter(dataCenterId: String): List<NetworkVo> {
		log.info("findAllNetworksFromDataCenter ... dataCenterId: {}", dataCenterId)
		val res: List<Network> = conn.findAllNetworks()
			.getOrDefault(listOf())
			.filter { it.dataCenter().id() == dataCenterId }
		return res.toNetworkVos(conn)
	}

	@Throws(Error::class)
	override fun findAllEventsFromDataCenter(dataCenterId: String): List<EventVo> {
		log.info("findAllEventsFromDataCenter ... dataCenterId: {}", dataCenterId)
		val dataCenter: DataCenter = conn.findDataCenter(dataCenterId)
			.getOrNull() ?: throw ErrorPattern.DATACENTER_NOT_FOUND.toException()
		val res: List<Event> = conn.findAllEvents()
			.getOrDefault(listOf())
			.filter {(it.dataCenterPresent() && (
						(it.dataCenter().idPresent() && it.dataCenter().id() == dataCenterId) ||
						(it.dataCenter().namePresent() && it.dataCenter().name() == dataCenter.name())
			))}
		return res.toEventVos()
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