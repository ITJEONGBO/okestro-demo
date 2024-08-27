package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.*
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.setting.toPermissionVos
import com.itinfo.itcloud.repository.VmInterfaceSamplesHistoryRepository
import com.itinfo.itcloud.repository.VmSamplesHistoryRepository
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.types.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.Throws

interface ItClusterService {
	/**
	 * [ItClusterService.findAll]
	 * 클러스터 목록
	 *
	 * @return List<[ClusterVo]> 클러스터 목록
	 */
	@Throws(Error::class)
	fun findAll(): List<ClusterVo>
	/**
	 * [ItClusterService.findOne]
	 * 클러스터 정보
	 *
	 * @param clusterId [String] 클러스터 아이디
	 * @return [ClusterVo]? 클러스터
	 */
	@Throws(Error::class)
	fun findOne(clusterId: String): ClusterVo?

	/**
	 * [ItClusterService.findAllDataCentersFromCluster]
	 * 클러스터 생성 위한 데이터센터 목록
	 *
	 * @return List<[DataCenterVo]> 데이터센터 목록
	 */
	@Deprecated("[ItDataCenterService.findAll] 와 같은 기능을 수행")
	@Throws(Error::class)
	fun findAllDataCentersFromCluster(): List<DataCenterVo>
	/**
	 * [ItClusterService.findAllNetworksFromDataCenter]
	 * 클러스터 생성 위한 네트워크 목록
	 *
	 * @param dataCenterId [String] 데이터센터 id
	 * @return List<[NetworkVo]> 네트워크 목록
	 */
	@Throws(Error::class)
	fun findAllNetworksFromDataCenter(dataCenterId: String): List<NetworkVo>
	/**
	 * [ItClusterService.add]
	 * 클러스터 생성
	 *
	 * @param clusterVo [ClusterVo] 클러스터
	 * @return [ClusterVo]?
	 */
	@Throws(Error::class)
	fun add(clusterVo: ClusterVo): ClusterVo?
	/**
	 * [ItClusterService.update]
	 * 클러스터 편집
	 *
	 * @param clusterVo [ClusterVo] 클러스터
	 * @return [ClusterVo]? 클러스터
	 */
	@Throws(Error::class)
	fun update(clusterVo: ClusterVo): ClusterVo?
	/**
	 * [ItClusterService.remove]
	 * 클러스터 삭제
	 *
	 * @param clusterId [String] 클러스터 아이디
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun remove(clusterId: String): Boolean

	/**
	 * [ItClusterService.findAllNetworksFromCluster]
	 * 클러스터 네트워크
	 *
	 * @param clusterId [String] 클러스터 아이디
	 * @return List<[NetworkVo]> 네트워크 목록
	 */
	@Throws(Error::class)
	fun findAllNetworksFromCluster(clusterId: String): List<NetworkVo>
	/**
	 * [ItClusterService.addNetwork]
	 * 클러스터 네트워크 추가
	 *
	 * @param clusterId [String] 클러스터 아이디
	 * @param networkVo [NetworkVo] 네트워크 생성
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun addNetwork(clusterId: String, networkVo: NetworkVo): Boolean
	/**
	 * [ItClusterService.findAllManageNetworkByCluster]
	 * 클러스터 네트워크 관리 창
	 * 할당, 필요, 관리, 네트워크 출력, 마이그레이션 네트워크, gluster 네트워크, 기본 라우팅
	 *
	 * @param clusterId [String] 클러스터 아이디
	 * @return List<[NetworkVo]>? 네트워크 관리 목록
	 */
	@Throws(Error::class)
	fun findAllManageNetworkByCluster(clusterId: String): List<NetworkVo>?
	/**
	 * [ItClusterService.manageNetworkByCluster]
	 * 클러스터 네트워크 관리
	 *
	 * @param clusterId [String] 클러스터 아이디
	 * @param networkVos List<[NetworkVo]> 네트워크 목록
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun manageNetworkByCluster(clusterId: String, networkVos: List<NetworkVo>): Boolean
	/**
	 * [ItClusterService.findAllHostsFromCluster]
	 * 클러스터 호스트
	 *
	 * @param clusterId [String] 클러스터 아이디
	 * @return List<[HostVo]> 호스트 목록
	 */
	@Throws(Error::class)
	fun findAllHostsFromCluster(clusterId: String): List<HostVo>
	/**
	 * [ItClusterService.findAllVmsFromCluster]
	 * 클러스터 가상머신
	 *
	 * @param clusterId [String] 클러스터 아이디
	 * @return List<[VmVo]> 가상머신 목록
	 */
	@Throws(Error::class)
	fun findAllVmsFromCluster(clusterId: String): List<VmVo>

	// 선호도 그룹
	/**
	 * [ItClusterService.getAffinityGroupsByCluster]
	 * 클러스터 선호도그룹
	 *
	 * @param clusterId [String] 클러스터 아이디
	 * @return List<[AffinityGroupVo]>? 클러스터 선호도그룹 목록
	 */
	@Throws(Error::class)
	fun getAffinityGroupsByCluster(clusterId: String): List<AffinityGroupVo>?
	/**
	 * [ItClusterService.addAffinityGroupByCluster]
	 * 클러스터 선호도그룹 생성
	 *
	 * @param clusterId [String] 클러스터 아이디
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun addAffinityGroupByCluster(clusterId: String, agVo: AffinityGroupVo): Boolean
	/**
	 * [ItClusterService.getAffinityGroupByCluster]
	 * 클러스터 선호도그룹 편집창
	 *
	 * @param clusterId [String] 클러스터 아이디
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun getAffinityGroupByCluster(clusterId: String, agId: String): AffinityGroupVo?
	/**
	 * [ItClusterService.editAffinityGroupByCluster]
	 * 클러스터 선호도그룹 편집
	 *
	 * @param agVo 선호도그룹
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun editAffinityGroupByCluster(agVo: AffinityGroupVo): Boolean
	/**
	 * [ItClusterService.deleteAffinityGroupByCluster]
	 * 클러스터 선호도그룹 삭제
	 *
	 * @param clusterId [String] 클러스터 아이디
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun deleteAffinityGroupByCluster(clusterId: String, agId: String): Boolean
	// 선호도 레이블

	/**
	 * [ItClusterService.findAllPermissionsFromCluster]
	 * 클러스터 권한
	 *
	 * @param clusterId [String] 클러스터 아이디
	 * @return List<[PermissionVo]>? 권한 목록
	 */
	@Throws(Error::class)
	fun findAllPermissionsFromCluster(clusterId: String): List<PermissionVo>
	/**
	 * [ItClusterService.findAllEventsFromCluster]
	 * 클러스터 이벤트
	 *
	 * @param clusterId [String] 클러스터 아이디
	 * @return List<[EventVo]>? 이벤트 목록
	 */
	@Throws(Error::class)
	fun findAllEventsFromCluster(clusterId: String): List<EventVo>
//    List<CpuProfileVo> getCpuProfile(String id);  // 안쓸듯
}

@Service
class ClusterServiceImpl(

) : BaseService(), ItClusterService {
	@Autowired private lateinit var vmSamplesHistoryRepository: VmSamplesHistoryRepository
	@Autowired private lateinit var vmInterfaceSamplesHistoryRepository: VmInterfaceSamplesHistoryRepository

	@Throws(Error::class)
	override fun findAll(): List<ClusterVo> {
		log.info("findAll ... ")
		// DataCenter 가 없어도 Cluster 는 출력가능
		val res: List<Cluster> =
			conn.findAllClusters()
				.getOrDefault(listOf())
				.filter { /*it.dataCenterPresent() && */ it.cpuPresent() }
		return res.toClusterVos(conn)
//		return res.toClusterVoInfos()
	}

	@Throws(Error::class)
	override fun findOne(clusterId: String): ClusterVo? {
		log.info("findOne ... clusterId: {}", clusterId)
		val res: Cluster =
			conn.findCluster(clusterId)
				.getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toException()
		return res.toClusterVo(conn)
	}

	@Deprecated("[ItDataCenterService.findAll] 와 같은 기능을 수행")
	@Throws(Error::class)
	override fun findAllDataCentersFromCluster(): List<DataCenterVo> {
		log.info("findAllDataCentersFromCluster ... ")
		val res: List<DataCenter> =
			conn.findAllDataCenters()
				.getOrDefault(listOf())
		return res.toDataCenterVos(conn, findNetworks = true, findClusters = true, findStorageDomains = true)
	}

	@Throws(Error::class)
	override fun findAllNetworksFromDataCenter(dataCenterId: String): List<NetworkVo> {
		log.info("findAllNetworksFromDataCenter ... dataCenterId: {}", dataCenterId)
		conn.findDataCenter(dataCenterId).getOrNull()
			?: throw ErrorPattern.DATACENTER_NOT_FOUND.toException()
		val res: List<Network> =
			conn.findAllNetworksFromFromDataCenter(dataCenterId)
				.getOrDefault(listOf())
		return res.toNetworkVos(conn)
	}

	@Throws(Error::class)
	override fun add(clusterVo: ClusterVo): ClusterVo? {
		log.info("add ... ")
		val res: Cluster =
			conn.addCluster(clusterVo.toAddClusterBuilder(conn))
				.getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toException()
		return res.toClusterVo(conn)
	}

	@Throws(Error::class)
	override fun update(clusterVo: ClusterVo): ClusterVo? {
		log.info("update ... clusterName: {}", clusterVo.name)
		val res: Cluster =
			conn.updateCluster(clusterVo.toEditClusterBuilder(conn))
				.getOrNull()?: throw ErrorPattern.CLUSTER_NOT_FOUND.toException()
		return res.toClusterVo(conn)
	}

	@Throws(Error::class)
	override fun remove(clusterId: String): Boolean {
		log.info("remove ... clusterName: {}", conn.findClusterName(clusterId))
		conn.findCluster(clusterId).getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toException()
		val res: Result<Boolean> =
			conn.removeCluster(clusterId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun findAllNetworksFromCluster(clusterId: String): List<NetworkVo> {
		log.info("findAllNetworksFromCluster ... clusterId: {}", clusterId)
		conn.findCluster(clusterId).getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toException()
		val res: List<Network> =
			conn.findAllNetworksFromCluster(clusterId)
				.getOrDefault(listOf())
		return res.toClusterNetworkVos(conn)
	}

	@Throws(Error::class)
	override fun addNetwork(clusterId: String, networkVo: NetworkVo): Boolean {
		TODO("Network 부분에서 기능구현해서 합치기")
	}

	@Throws(Error::class)
	override fun findAllManageNetworkByCluster(clusterId: String): List<NetworkVo>? {
		log.info("getUsagesByNetwork ... networkId: {}", clusterId)
		conn.findCluster(clusterId).getOrNull() ?: ErrorPattern.CLUSTER_NOT_FOUND.toException()
		val res: List<Network> =
			conn.findAllNetworksFromCluster(clusterId)
				.getOrDefault(listOf())
		return res.toNetworkVos(conn) // TODO: 모두 할당? 모두 필요?
	}

	@Throws(Error::class)
	override fun manageNetworkByCluster(clusterId: String, networkVos: List<NetworkVo>): Boolean {
		log.info("editUsage ... ")
		// 클러스터 모두연결이 선택되어야지만 모두 필요가 선택됨
		/*
        nuVo.getClusterVoList().stream()
                .filter(NetworkClusterVo::isConnected) // 연결된 경우만 필터링
                .forEach(networkClusterVo -> {
                    ClusterNetworksService clusterNetworksService = system.clustersService().clusterService(networkClusterVo.getId()).networksService();
                    clusterNetworksService.add().network(
                            new NetworkBuilder()
                                    .id(network.id())
                                    .required(networkClusterVo.isRequired())
                    ).send().network();
                })
		 */
		return false
	}

	@Throws(Error::class)
	override fun findAllHostsFromCluster(clusterId: String): List<HostVo> {
		log.info("findAllHostsFromCluster ... clusterId: {}", clusterId)
		conn.findCluster(clusterId).getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toException()
		val res: List<Host> =
			conn.findAllHosts()
				.getOrDefault(listOf())
				.filter { it.cluster().id() == clusterId }
		return res.toHostVos(conn)
		// TODO usageDto 기능 추가예정
	}

	@Throws(Error::class)
	override fun findAllVmsFromCluster(clusterId: String): List<VmVo> {
		log.info("findAllVmsFromCluster ... clusterId: {}", clusterId)
		conn.findCluster(clusterId).getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toException()
		val res: List<Vm> =
			conn.findAllVms()
				.getOrDefault(listOf())
				.filter { it.cluster().id() == clusterId }
		return res.toVmVos(conn)
	}

	@Throws(Error::class)
	override fun getAffinityGroupsByCluster(clusterId: String): List<AffinityGroupVo>? {
		log.info("getAffinityGroupsByCluster ... clusterId: {}", clusterId)
		conn.findCluster(clusterId).getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toException()
		val res: List<AffinityGroup> =
			conn.findAllAffinityGroupsFromCluster(clusterId)
				.getOrDefault(listOf())
		return res.toAffinityGroupVos(conn, clusterId)
	}

	@Throws(Error::class)
	override fun addAffinityGroupByCluster(clusterId: String, agVo: AffinityGroupVo): Boolean {
		log.info("addAffinityGroupByCluster ... ")
		TODO("Not yet implemented")
	}

	@Throws(Error::class)
	override fun getAffinityGroupByCluster(clusterId: String, agId: String): AffinityGroupVo? {
		log.info("getAffinityGroupByCluster ... ")
		TODO("Not yet implemented")
	}

	@Throws(Error::class)
	override fun editAffinityGroupByCluster(agVo: AffinityGroupVo): Boolean {
		log.info("editAffinityGroupByCluster ... ")
		TODO("Not yet implemented")
	}

	@Throws(Error::class)
	override fun deleteAffinityGroupByCluster(clusterId: String, agId: String): Boolean {
		log.info("deleteAffinityGroupByCluster ... ")
		TODO("Not yet implemented")
	}

	@Throws(Error::class)
	override fun findAllPermissionsFromCluster(clusterId: String): List<PermissionVo> {
		log.info("findAllPermissionsByCluster ... clusterId: {}", clusterId)
		conn.findCluster(clusterId).getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toException()
		val permissions: List<Permission> =
			conn.findAllPermissionsFromCluster(clusterId)
				.getOrDefault(listOf())
		return permissions.toPermissionVos(conn)
	}

	@Throws(Error::class)
	override fun findAllEventsFromCluster(clusterId: String): List<EventVo> {
		log.info("findAllEventsByCluster ... clusterId: {}", clusterId)
		val cluster: Cluster =
			conn.findCluster(clusterId)
				.getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toException()

		val events: List<Event> =
			conn.findAllEvents("cluster.name=${cluster.name()}")
				.getOrDefault(listOf())
				.filter {
					it.clusterPresent() &&
					it.cluster().idPresent() &&
					it.cluster().id().equals(clusterId)
				}
		return events.toEventVos()
	}

	companion object {
		private val log by LoggerDelegate()
	}
}