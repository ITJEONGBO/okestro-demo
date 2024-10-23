package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.*
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.setting.toPermissionVos
import com.itinfo.itcloud.repository.history.dto.UsageDto
import com.itinfo.itcloud.service.BaseService
import com.itinfo.itcloud.service.computing.VmServiceImpl.Companion
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.builders.NetworkBuilder
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
	 * 클러스터 정보 (편집창)
	 *
	 * @param clusterId [String] 클러스터 Id
	 * @return [ClusterVo]?
	 */
	@Throws(Error::class)
	fun findOne(clusterId: String): ClusterVo?

	// 클러스터 생성창
	// 		데이터센터 목록	[ItDataCenterService.findAll]
	//		네트워크 목록	[ItDataCenterService.findAllNetworksFromDataCenter]

	/**
	 * [ItClusterService.add]
	 * 클러스터 생성
	 *
	 * @param clusterVo [ClusterVo]
	 * @return [ClusterVo]?
	 */
	@Throws(Error::class)
	fun add(clusterVo: ClusterVo): ClusterVo?
	/**
	 * [ItClusterService.update]
	 * 클러스터 편집
	 *
	 * @param clusterVo [ClusterVo] 클러스터
	 * @return [ClusterVo]?
	 */
	@Throws(Error::class)
	fun update(clusterVo: ClusterVo): ClusterVo?
	/**
	 * [ItClusterService.remove]
	 * 클러스터 삭제
	 *
	 * @param clusterId [String] 클러스터 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun remove(clusterId: String): Boolean

	/**
	 * [ItClusterService.findAllHostsFromCluster]
	 * 클러스터가 가지고있는 호스트 목록
	 *
	 * @param clusterId [String] 클러스터 Id
	 * @return List<[HostVo]> 호스트 목록
	 */
	@Throws(Error::class)
	fun findAllHostsFromCluster(clusterId: String): List<HostVo>
	// 생성, 편집, 삭제, 유지보수, 활성, 재시작, 호스트 네트워크 복사

	/**
	 * [ItClusterService.findAllVmsFromCluster]
	 * 클러스터가 가지고있는 가상머신 목록
	 *
	 * @param clusterId [String] 클러스터 Id
	 * @return List<[VmVo]> 가상머신 목록
	 */
	@Throws(Error::class)
	fun findAllVmsFromCluster(clusterId: String): List<VmVo>
	// 생성, 편집, 삭제, 실행, 일시중지, 종료, 호스트 네트워크 복사, 재부팅, 콘솔, 템플릿 목록, 스냅샷 생성, 마이그레이션

	/**
	 * [ItClusterService.findAllNetworksFromCluster]
	 * 클러스터가 가지고있는 네트워크 목록
	 *
	 * @param clusterId [String] 클러스터 Id
	 * @return List<[NetworkVo]> 네트워크 목록
	 */
	@Throws(Error::class)
	fun findAllNetworksFromCluster(clusterId: String): List<NetworkVo>
	// 생성, 편집, 삭제

	/**
	 * [ItClusterService.addNetworkFromCluster]
	 * 클러스터 네트워크 추가
	 *
	 * @param clusterId [String] 클러스터 Id
	 * @param networkVo [NetworkVo] 네트워크 생성
	 * @return [NetworkVo]?
	 */
	@Throws(Error::class)
	fun addNetworkFromCluster(clusterId: String, networkVo: NetworkVo): NetworkVo?
	/**
	 * [ItClusterService.findAllManageNetworksFromCluster]
	 * 클러스터 네트워크 관리 창
	 * 할당, 필요, 관리, 네트워크 출력, 마이그레이션 네트워크, gluster 네트워크, 기본 라우팅
	 *
	 * @param clusterId [String] 클러스터 Id
	 * @return List<[NetworkVo]>? 네트워크 관리 목록
	 */
	@Throws(Error::class)
	fun findAllManageNetworksFromCluster(clusterId: String): List<NetworkVo>
	/**
	 * [ItClusterService.manageNetworksFromCluster]
	 * 클러스터 네트워크 관리
	 *
	 * @param clusterId [String] 클러스터 Id
	 * @param networkVos List<[NetworkVo]> 네트워크 목록
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun manageNetworksFromCluster(clusterId: String, networkVos: List<NetworkVo>): Boolean

	/**
	 * [ItClusterService.findAllCpuProfilesFromCluster]
	 * 클러스터가 가지고있는 CPU 프로파일 목록
	 * vm 생성시 사용
	 *
	 * @param clusterId [String] 클러스터 Id
	 * @return List<[CpuProfileVo]> cpuProfile 목록
	 */
	@Throws(Error::class)
	fun findAllCpuProfilesFromCluster(clusterId: String): List<CpuProfileVo>
	/**
	 * [ItClusterService.findAllEventsFromCluster]
	 * 클러스터가 가지고있는 이벤트 목록
	 *
	 * @param clusterId [String] 클러스터 Id
	 * @return List<[EventVo]> 이벤트 목록
	 */
	@Throws(Error::class)
	fun findAllEventsFromCluster(clusterId: String): List<EventVo>

	/**
	 * [ItClusterService.findAllPermissionsFromCluster]
	 * 클러스터가 가지고있는 권한 목록
	 *
	 * @param clusterId [String] 클러스터 Id
	 * @return List<[PermissionVo]> 권한 목록
	 */
	@Deprecated("필요없음")
	@Throws(Error::class)
	fun findAllPermissionsFromCluster(clusterId: String): List<PermissionVo>
}

@Service
class ClusterServiceImpl(

) : BaseService(), ItClusterService {
	@Autowired private lateinit var itGraphService: ItGraphService

	@Throws(Error::class)
	override fun findAll(): List<ClusterVo> {
		log.info("findAll ... ")
		val res: List<Cluster> =
			conn.findAllClusters().getOrDefault(listOf())
		return res.toClustersMenu(conn)
	}

	@Throws(Error::class)
	override fun findOne(clusterId: String): ClusterVo? {
		log.info("findOne ... clusterId: {}", clusterId)
		val res: Cluster? =
			conn.findCluster(clusterId).getOrNull()
		return res?.toClusterInfo(conn)
	}

	@Throws(Error::class)
	override fun add(clusterVo: ClusterVo): ClusterVo? {
		log.info("add ... ")
//		log.info("{}", MigrateOnError.fromValue(clusterVo.errorHandling))
		val res: Cluster? =
			conn.addCluster(
				clusterVo.toAddClusterBuilder(conn)
			).getOrNull()
		return res?.toClusterVo(conn)
	}

	@Throws(Error::class)
	override fun update(clusterVo: ClusterVo): ClusterVo? {
		log.info("update ... clusterName: {}", clusterVo.name)
		val res: Cluster? =
			conn.updateCluster(
				clusterVo.toEditClusterBuilder(conn)
			).getOrNull()
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
	override fun findAllHostsFromCluster(clusterId: String): List<HostVo> {
		log.info("findAllHostsFromCluster ... clusterId: {}", clusterId)
		val res: List<Host> =
			conn.findAllHostsFromCluster(clusterId).getOrDefault(listOf())

		return res.map { host ->
			val hostNic: HostNic? =
				conn.findAllNicsFromHost(host.id()).getOrDefault(listOf()).firstOrNull()

			val usageDto: UsageDto? =
				if(host.status() == HostStatus.UP) hostNic?.id()?.let { itGraphService.hostPercent(host.id(), it) }
				else null
			host.toHostMenu(conn, usageDto)
		}
	}

	@Throws(Error::class)
	override fun findAllVmsFromCluster(clusterId: String): List<VmVo> {
		log.info("findAllVmsFromCluster ... clusterId: {}", clusterId)
		val res: List<Vm> =
			conn.findAllVmsFromCluster(clusterId).getOrDefault(listOf())
		return res.toVmsMenu(conn)
	}


	@Throws(Error::class)
	override fun findAllNetworksFromCluster(clusterId: String): List<NetworkVo> {
		log.info("findAllNetworksFromCluster ... clusterId: {}", clusterId)
		val res: List<Network> =
			conn.findAllNetworksFromCluster(clusterId).getOrDefault(listOf())
		return res.toClusterNetworkVos(conn)
	}

	@Throws(Error::class)
	override fun addNetworkFromCluster(clusterId: String, networkVo: NetworkVo): NetworkVo? {
		log.info("addNetworkFromCluster ... ")
		val network: Network =
			conn.addNetwork(
				networkVo.toAddNetworkBuilder()
			).getOrNull() ?: throw ErrorPattern.NETWORK_NOT_FOUND.toException()

		// 네트워크를 생성하고 cluster 에 붙이는 방식
		val res: Network? =
			conn.addNetworkFromCluster(
				clusterId,
				NetworkBuilder().id(network.id()).build()
			).getOrNull()
		return res?.toNetworkVo(conn)
	}

	// 클러스터 네트워크 - 네트워크 관리창
	@Throws(Error::class)
	override fun findAllManageNetworksFromCluster(clusterId: String): List<NetworkVo> {
		log.info("findAllManageNetworksFromCluster ... clusterId: {}", clusterId)
		val cluster: Cluster =
			conn.findCluster(clusterId).getOrNull()
				?: throw ErrorPattern.CLUSTER_ID_NOT_FOUND.toException()

		// 클러스터가 가지고 있는 네트워크
		val clusterNetworks: List<Network> =
			conn.findAllNetworksFromCluster(clusterId).getOrDefault(listOf())

		val networks: List<Network> =
			conn.findAllNetworksFromDataCenter(cluster.dataCenter().id()).getOrDefault(listOf())
				.filter {
					clusterNetworks.none { clusterNetwork -> clusterNetwork.id() == it.id() }
				}

		val mergedNetworks = clusterNetworks + networks
		return mergedNetworks.toClusterNetworkVos(conn)
	}

	@Throws(Error::class)
	override fun manageNetworksFromCluster(clusterId: String, networkVos: List<NetworkVo>): Boolean {
		log.info("manageNetworksFromCluster ... ")
		if(networkVos.isNotEmpty()){
			networkVos.forEach { networkVo ->
				networkVo.toAddClusterAttach(conn, networkVo.id)	// 클러스터 연결, 필수 선택
			}
		}

		//
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
//		return false
		TODO("네트워크 관리 실행")
	}

	@Throws(Error::class)
	override fun findAllCpuProfilesFromCluster(clusterId: String): List<CpuProfileVo> {
		log.info("findAllCpuProfilesFromCluster ... clusterId: {}", clusterId)
		val res: List<CpuProfile> =
			conn.findAllCpuProfilesFromCluster(clusterId).getOrDefault(listOf())
		return res.toCpuProfileVos()
	}

	@Throws(Error::class)
	override fun findAllEventsFromCluster(clusterId: String): List<EventVo> {
		log.info("findAllEventsFromCluster ... clusterId: {}", clusterId)
		val cluster: Cluster =
			conn.findCluster(clusterId).getOrNull()
				?: throw ErrorPattern.CLUSTER_NOT_FOUND.toException()

		val res: List<Event> =
			conn.findAllEvents("cluster.name=${cluster.name()}").getOrDefault(listOf())
				.filter { it.clusterPresent() && it.cluster().idPresent() && it.cluster().id().equals(clusterId) }
		return res.toEventVos()
	}

	@Deprecated("필요없음")
	@Throws(Error::class)
	override fun findAllPermissionsFromCluster(clusterId: String): List<PermissionVo> {
		log.info("findAllPermissionsFromCluster ... clusterId: {}", clusterId)
		val res: List<Permission> =
			conn.findAllPermissionsFromCluster(clusterId).getOrDefault(listOf())
		return res.toPermissionVos(conn)
	}


	companion object {
		private val log by LoggerDelegate()
	}
}