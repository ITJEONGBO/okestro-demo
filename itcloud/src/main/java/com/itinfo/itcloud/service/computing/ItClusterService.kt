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
import com.itinfo.itcloud.service.network.ItNetworkService
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

	// 클러스터 생성창 - 데이터센터 목록 [ItDataCenterService.findAll]
	// 클러스터 생성창 - 네트워크 목록 [ItDataCenterService.findAllNetworksFromDataCenter] 와 같은 기능을 수행

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
	 * @return [NetworkVo]?
	 */
	@Throws(Error::class)
	fun addNetwork(clusterId: String, networkVo: NetworkVo): NetworkVo?
	/**
	 * [ItClusterService.findAllManageNetworksFromCluster]
	 * 클러스터 네트워크 관리 창
	 * 할당, 필요, 관리, 네트워크 출력, 마이그레이션 네트워크, gluster 네트워크, 기본 라우팅
	 *
	 * @param clusterId [String] 클러스터 아이디
	 * @return List<[NetworkVo]>? 네트워크 관리 목록
	 */
	@Throws(Error::class)
	fun findAllManageNetworksFromCluster(clusterId: String): List<NetworkVo>?
	/**
	 * [ItClusterService.manageNetworksFromCluster]
	 * 클러스터 네트워크 관리
	 *
	 * @param clusterId [String] 클러스터 아이디
	 * @param networkVos List<[NetworkVo]> 네트워크 목록
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun manageNetworksFromCluster(clusterId: String, networkVos: List<NetworkVo>): Boolean
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
	@Autowired private lateinit var itNetworkService: ItNetworkService

	@Throws(Error::class)
	override fun findAll(): List<ClusterVo> {
		log.info("findAll ... ")
		// DataCenter 가 없어도 Cluster 는 출력가능
		val res: List<Cluster> =
			conn.findAllClusters()
				.getOrDefault(listOf())
				.filter { it.cpuPresent() }
		return res.toClusterMenus(conn)
	}

	@Throws(Error::class)
	override fun findOne(clusterId: String): ClusterVo? {
		log.info("findOne ... clusterId: {}", clusterId)
		val res: Cluster? =
			conn.findCluster(clusterId)
				.getOrNull()
		return res?.toClusterVo(conn)
	}

	@Throws(Error::class)
	override fun add(clusterVo: ClusterVo): ClusterVo? {
		log.info("add ... ")
		val res: Cluster? =
			conn.addCluster(clusterVo.toAddClusterBuilder(conn))
				.getOrNull()
		return res?.toClusterVo(conn)
	}

	@Throws(Error::class)
	override fun update(clusterVo: ClusterVo): ClusterVo? {
		log.info("update ... clusterName: {}", clusterVo.name)
		val res: Cluster? =
			conn.updateCluster(clusterVo.toEditClusterBuilder(conn))
				.getOrNull()
		return res?.toClusterVo(conn)
	}

	@Throws(Error::class)
	override fun remove(clusterId: String): Boolean {
		log.info("remove ... clusterId: {}", clusterId)
		val res: Result<Boolean> =
			conn.removeCluster(clusterId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun findAllNetworksFromCluster(clusterId: String): List<NetworkVo> {
		log.info("findAllNetworksFromCluster ... clusterId: {}", clusterId)
		val res: List<Network> =
			conn.findAllNetworksFromCluster(clusterId)
				.getOrDefault(listOf())
		return res.toClusterNetworkVos(conn)
	}

	@Throws(Error::class)
	override fun addNetwork(clusterId: String, networkVo: NetworkVo): NetworkVo? {
		log.info("addNetwork ... ") // // 클러스터 연결 없이 네트워크만 추가
		// TODO 클러스터 연결/할당 기능 추가
		return itNetworkService.add(networkVo)
	}

	@Throws(Error::class)
	override fun findAllManageNetworksFromCluster(clusterId: String): List<NetworkVo>? {
		log.info("findAllManageNetworksFromCluster ... clusterId: {}", clusterId)
		val res: List<Network> =
			conn.findAllNetworksFromCluster(clusterId)
				.getOrDefault(listOf())
		return res.toNetworkVos(conn) // TODO: 모두 할당? 모두 필요?
	}

	@Throws(Error::class)
	override fun manageNetworksFromCluster(clusterId: String, networkVos: List<NetworkVo>): Boolean {
		log.info("manageNetworksFromCluster ... ")
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
		conn.findCluster(clusterId)
			.getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toException()
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
		conn.findCluster(clusterId)
			.getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toException()
		val res: List<Vm> =
			conn.findAllVms()
				.getOrDefault(listOf())
				.filter { it.cluster().id() == clusterId }
		return res.toVmVos(conn)
	}

	@Throws(Error::class)
	override fun findAllPermissionsFromCluster(clusterId: String): List<PermissionVo> {
		log.info("findAllPermissionsFromCluster ... clusterId: {}", clusterId)
		val permissions: List<Permission> =
			conn.findAllPermissionsFromCluster(clusterId)
				.getOrDefault(listOf())
		return permissions.toPermissionVos(conn)
	}

	@Throws(Error::class)
	override fun findAllEventsFromCluster(clusterId: String): List<EventVo> {
		log.info("findAllEventsFromCluster ... clusterId: {}", clusterId)
		val cluster: Cluster =
			conn.findCluster(clusterId)
				.getOrNull() ?: throw ErrorPattern.CLUSTER_NOT_FOUND.toException()

		val res: List<Event> =
			conn.findAllEvents("cluster.name=${cluster.name()}")
				.getOrDefault(listOf())
				.filter {
					it.clusterPresent() &&
					it.cluster().idPresent() &&
					it.cluster().id().equals(clusterId)
				}
		return res.toEventVos()
	}

	companion object {
		private val log by LoggerDelegate()
	}
}