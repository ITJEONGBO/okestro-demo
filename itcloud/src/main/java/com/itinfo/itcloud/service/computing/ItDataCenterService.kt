package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.fromDisksToIdentifiedVos
import com.itinfo.itcloud.model.network.NetworkVo
import com.itinfo.itcloud.model.network.VnicProfileVo
import com.itinfo.itcloud.model.network.toNetworkVos
import com.itinfo.itcloud.model.network.toVnicProfileToVmVos
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.setting.toPermissionVos
import com.itinfo.itcloud.model.storage.*
import com.itinfo.itcloud.repository.engine.DiskVmElementRepository
import com.itinfo.itcloud.repository.engine.entity.DiskVmElementEntity
import com.itinfo.itcloud.repository.engine.entity.toVmId
import com.itinfo.itcloud.repository.history.dto.UsageDto
import com.itinfo.itcloud.service.BaseService
import com.itinfo.itcloud.service.storage.DiskServiceImpl
import com.itinfo.itcloud.service.storage.ItDiskService
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
	/**
	 * [ItDataCenterService.findAllHostsFromDataCenter]
	 * 데이터센터가 가지고있는 호스트 목록
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return List<[HostVo]> 호스트 목록
	 */
	@Throws(Error::class)
	fun findAllHostsFromDataCenter(dataCenterId: String): List<HostVo>
	/**
	 * [ItDataCenterService.findAllVmsFromDataCenter]
	 * 데이터센터가 가지고있는 가상머신 목록
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return List<[VmVo]> 가상머신 목록
	 */
	@Throws(Error::class)
	fun findAllVmsFromDataCenter(dataCenterId: String): List<VmVo>
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
	 * [ItDataCenterService.findAllActiveStorageDomainsFromDataCenter]
	 * 데이터센터가 가지고있는 활성화 된 스토리지 도메인 목록
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return List<[StorageDomainVo]> 스토리지 도메인 목록
	 */
	@Throws(Error::class)
	fun findAllActiveStorageDomainsFromDataCenter(dataCenterId: String): List<StorageDomainVo>
	/**
	 * [ItDataCenterService.findAllDisksFromDataCenter]
	 * 데이터센터가 가지고있는 디스크 목록 (데이터센터에 속한 스토리지 도메인이 가지고 있는 디스크 목록)
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
	 * [ItDataCenterService.findAttachDiskImageFromDataCenter]
	 * 가상머신 생성 - 인스턴스 이미지 - 연결 -> 디스크 목록
	 * 기준: 아무것도 연결되어 있지 않은 디스크
	 * 인스턴스 이미지 -> 생성 시 필요한 스토리지 도메인
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return List<[DiskImageVo]> 디스크  목록
	 */
	@Throws(Error::class)
	fun findAttachDiskImageFromDataCenter(dataCenterId: String): List<DiskImageVo>
	/**
	 * [ItDataCenterService.findAllISOFromDataCenter]
	 * 가상머신 생성 - 부트 옵션 - 생성 시 필요한 CD/DVD 연결할 ISO 목록 (디스크이미지)
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return List<[IdentifiedVo]> ISO 목록
	 */
	@Throws(Error::class)
	fun findAllISOFromDataCenter(dataCenterId: String): List<IdentifiedVo>
	/**
	 * [ItDataCenterService.findAllVnicProfilesFromDataCenter]
	 * 가상머신 생성 -  vnicprofile 목록 출력 (가상머신 생성, 네트워크 인터페이스 생성)
	 *
	 * @param dataCenterId [String] 데이터센터 Id
	 * @return List<[VnicProfileVo]> VnicProfile 목록
	 */
	@Throws(Error::class)
	fun findAllVnicProfilesFromDataCenter(dataCenterId: String): List<VnicProfileVo>


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
		val res: DataCenter? = conn.addDataCenter(
			dataCenterVo.toAddDataCenterBuilder()
		).getOrNull()
		return res?.toDataCenterVoInfo()
	}

	@Throws(Error::class)
	override fun update(dataCenterVo: DataCenterVo): DataCenterVo? {
		log.info("update ... ")
		val res: DataCenter? = conn.updateDataCenter(
			dataCenterVo.toEditDataCenterBuilder()
		).getOrNull()
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
		return res.toClustersMenu(conn)
	}

	@Throws(Error::class)
	override fun findAllHostsFromDataCenter(dataCenterId: String): List<HostVo> {
		log.debug("findAllHostsFromDataCenter ... dataCenterId: {}", dataCenterId)
		val res: List<Host> = conn.findAllHostsFromDataCenter(dataCenterId)
			.getOrDefault(listOf())

		return res.map { host ->
			val hostNic: HostNic? = conn.findAllNicsFromHost(host.id())
				.getOrDefault(listOf()).firstOrNull()
			val usageDto = calculateUsage(host, hostNic)
			host.toHostMenu(conn, usageDto)
		}
	}

	private fun calculateUsage(host: Host, hostNic: HostNic?): UsageDto? {
		return if (host.status() == HostStatus.UP && hostNic != null) {
			itGraphService.hostPercent(host.id(), hostNic.id())
		} else null
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
	override fun findAllActiveStorageDomainsFromDataCenter(dataCenterId: String): List<StorageDomainVo> {
		log.info("findAllFromDataCenter ... dcId: $dataCenterId")
		val res: List<StorageDomain> = conn.findAllAttachedStorageDomainsFromDataCenter(dataCenterId)
			.getOrDefault(listOf())
			.filter { it.status() == StorageDomainStatus.ACTIVE }
		return res.toActiveDomains()
	}

	@Throws(Error::class)
	override fun findAllDisksFromDataCenter(dataCenterId: String): List<DiskImageVo> {
		val res: List<Disk> = conn.findAllAttachedStorageDomainsFromDataCenter(dataCenterId)
			.getOrDefault(listOf())
			.flatMap { conn.findAllDisksFromStorageDomain(it.id()).getOrDefault(listOf()) }
		return res.toDiskMenus(conn)
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
		val dataCenter: DataCenter? = conn.findDataCenter(dataCenterId)
			.getOrNull()
		val res: List<Event> = conn.findAllEvents()
			.getOrDefault(listOf())
			.filter {(it.dataCenterPresent() && (
					(it.dataCenter().idPresent() && it.dataCenter().id() == dataCenterId) ||
					(it.dataCenter().namePresent() && it.dataCenter().name() == dataCenter?.name())))}
		return res.toEventVos()
	}


	@Throws(Error::class)
	override fun findAttachDiskImageFromDataCenter(dataCenterId: String): List<DiskImageVo> {
		log.info("findAttachDiskImageByDataCenter ... 데이터센터 ID: $dataCenterId")
		conn.findDataCenter(dataCenterId)
			.getOrNull() ?: throw ErrorPattern.DATACENTER_ID_NOT_FOUND.toException()

		val res: List<DiskImageVo> = conn.findAllAttachedStorageDomainsFromDataCenter(dataCenterId)
			.getOrDefault(listOf())
			.flatMap {
				conn.findAllDisksFromStorageDomain(it.id())
					.getOrDefault(listOf())
					.filter { disk ->  disk.format() == DiskFormat.COW && disk.quotaPresent() }
					.map { disk ->  disk.toDiskInfo(conn) }
					.filter { disk ->  disk.connectVm.id == "" && disk.connectTemplate.id == "" }
			}
		return res
	}

	@Throws(Error::class)
	override fun findAllISOFromDataCenter(dataCenterId: String): List<IdentifiedVo> {
		log.info("findAllISOFromDataCenter ... ")
		val res: List<Disk> = conn.findDataCenter(dataCenterId, follow = "storagedomains.disks")
			.getOrNull()!!
			.storageDomains()
			.flatMap { it.disks().orEmpty() }
			.map { it }
			.filter { it.contentType() == DiskContentType.ISO && it.status() == DiskStatus.OK }
		return res.fromDisksToIdentifiedVos()
	}

	@Throws(Error::class)
	override fun findAllVnicProfilesFromDataCenter(dataCenterId: String): List<VnicProfileVo> {
		log.info("findAllVnicProfilesFromDataCenter ... dataCenterId: {}", dataCenterId)
		val res: List<VnicProfile> = conn.findAllNetworks()
			.getOrDefault(listOf())
			.filter { it.dataCenter().id() == dataCenterId }
			.flatMap { network -> conn.findAllVnicProfilesFromNetwork(network.id())
				.getOrDefault(listOf())
			}
		return res.toVnicProfileToVmVos(conn)
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
		val res: List<DataCenter> = conn.findAllDataCenters().getOrDefault(listOf())
		return res.toDataCenterVos(conn, findNetworks = false, findStorageDomains = false, findClusters = true)
	}

	@Throws(Error::class)
	override fun dashboardNetwork(): List<DataCenterVo> {
		log.info("dashboardNetwork ... ")
		val res: List<DataCenter> = conn.findAllDataCenters().getOrDefault(listOf())
		return res.toDataCenterVos(conn, findNetworks = true, findStorageDomains = false, findClusters = false)
	}

	@Throws(Error::class)
	override fun dashboardStorage(): List<DataCenterVo> {
		log.info("dashboardStorage ... ")
		val res: List<DataCenter> = conn.findAllDataCenters().getOrDefault(listOf())
		return res.toDataCenterVos(conn, findNetworks = false, findStorageDomains = true, findClusters = false)
	}


	companion object {
		private val log by LoggerDelegate()
	}
}