package com.itinfo.itcloud.service.network

import com.itinfo.common.LoggerDelegate
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.itcloud.error.ItemNotFoundException
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.fromOpenStackNetworkProviderToIdentifiedVo
import com.itinfo.itcloud.model.network.*
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.setting.toPermissionVos
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import org.ovirt.engine.sdk4.builders.*
import org.ovirt.engine.sdk4.types.*
import org.springframework.stereotype.Service
import kotlin.Error

interface ItNetworkService {
	/**
	 * [ItNetworkService.findAll]
	 * 네트워크 목록
	 *
	 * @return List<[NetworkVo]> 네트워크 목록
	 */
	@Throws(Error::class)
	fun findAll(): List<NetworkVo>
	/**
	 * [ItNetworkService.findOne]
	 * 네트워크 상세정보
	 *
	 * @param networkId [String] 네트워크 Id
	 * @return [NetworkVo] 네트워크 정보
	 */
	@Throws(ItemNotFoundException::class, Error::class)
	fun findOne(networkId: String): NetworkVo?

	// 네트워크 생성창 - 데이터센터 목록 [ItDataCenterService.findAll]
	// 				  - 클러스터 목록(연결,필수) [ItDataCenterService.findAllClusterFromDataCenter]

	/**
	 * [ItNetworkService.add]
	 * 네트워크 생성
	 * 기본 단순 생성은 클러스터가 할당되지도 필수도 선택되지 않음
	 *
	 * @param networkVo [NetworkVo]
	 * @return [NetworkVo]
	 */
	@Throws(Error::class)
	fun add(networkVo: NetworkVo): NetworkVo?
	/**
	 * [ItNetworkService.update]
	 * 네트워크 편집
	 * 편집에서는 클러스터 선택 못함
	 *
	 * @param networkVo [NetworkVo]
	 * @return [NetworkVo]
	 */
	@Throws(Error::class)
	fun update(networkVo: NetworkVo): NetworkVo?
	/**
	 * [ItNetworkService.remove]
	 * 네트워크 삭제
	 *
	 * @param networkId [String] 네트워크 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun remove(networkId: String): Boolean
	/**
	 * [ItNetworkService.findAllNetworkProviderFromNetwork]
	 * 네트워크 가져오기 - 네트워크 공급자 목록
	 *
	 * @return [IdentifiedVo]
	 */
	@Throws(Error::class)
	fun findAllNetworkProviderFromNetwork(): IdentifiedVo
	/**
	 * [ItNetworkService.findAllExternalNetworkFromNetworkProvider]
	 * 네트워크 가져오기 창 - 네트워크 공급자가 가지고있는 네트워크
	 *
	 * @param providerId [String] 공급자 Id
	 * @return List<[NetworkVo]>
	 */
	@Throws(Error::class)
	fun findAllExternalNetworkFromNetworkProvider(providerId: String): List<NetworkVo>
	/**
	 * [ItNetworkService.importNetwork]
	 * 네트워크 가져오기 - 데이터센터만 바꿔서 네트워크 복사기능
	 * // TODO 애매함 한번 가져오기 된거는 더이상 못가져오나?
	 *
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun importNetwork(): NetworkVo

	/**
	 * [ItNetworkService.findAllClustersFromNetwork]
	 * 네트워크 클러스터 목록
	 *
	 * @param networkId [String] 네트워크 아이디
	 * @return List<[ClusterVo]>
	 */
	@Throws(Error::class)
	fun findAllClustersFromNetwork(networkId: String): List<ClusterVo>
	/**
	 * [ItNetworkService.findAllHostsFromNetwork]
	 * 네트워크 호스트 목록
	 *
	 * @param networkId [String] 네트워크 아이디
	 * @return List<[HostVo]>
	 */
	@Throws(Error::class)
	fun findAllHostsFromNetwork(networkId: String): List<HostVo>
	/**
	 * [ItNetworkService.findAllVmsFromNetwork]
	 * 네트워크 가상머신 목록
	 *
	 * @param networkId [String] 네트워크 아이디
	 * @return List<[VmVo]>
	 */
	@Throws(Error::class)
	fun findAllVmsFromNetwork(networkId: String): List<VmVo>
	/**
	 * [ItNetworkService.findAllTemplatesFromNetwork]
	 *
	 * @param networkId [String] 네트워크 아이디
	 * @return List<[NetworkTemplateVo]>
	 */
	@Throws(ItemNotFoundException::class, Error::class)
	fun findAllTemplatesFromNetwork(networkId: String): List<NetworkTemplateVo>
	/**
	 * [ItNetworkService.findAllPermissionsFromNetwork]
	 *
	 * @param networkId [String] 네트워크 아이디
	 * @return List<[PermissionVo]>
	 */
	@Throws(Error::class)
	fun findAllPermissionsFromNetwork(networkId: String): List<PermissionVo>
}

@Service
class NetworkServiceImpl(

): BaseService(), ItNetworkService {

	@Throws(Error::class)
	override fun findAll(): List<NetworkVo> {
		log.info("findAll ... ")
		val networks: List<Network> =
			conn.findAllNetworks()
				.getOrDefault(listOf())
		return networks.toNetworksMenu(conn)
	}

	@Throws(Error::class)
	override fun findOne(networkId: String): NetworkVo? {
		log.info("findOne ... networkId: {}", networkId)
		val res: Network? =
			conn.findNetwork(networkId, "networklabels")
				.getOrNull()
		return res?.toNetworkVo(conn)
	}

	@Throws(Error::class)
	override fun add(networkVo: NetworkVo): NetworkVo? {
		// dc 다르면 중복명 가능
		log.info("addNetwork ... ")
		val res: Network? =
			conn.addNetwork(networkVo.toAddNetworkBuilder(conn))
				.getOrNull()

		if(res == null){
			throw ErrorPattern.NETWORK_NOT_FOUND.toException()
		}

		// 생성 후에 나온 network Id로 클러스터 네트워크 생성 및 레이블 생성 가능
		networkVo.toAddClusterAttach(conn, res.id())	// 클러스터 연결, 필수 선택
		networkVo.toAddNetworkLabel(conn, res.id())
		return res.toNetworkVo(conn)
	}

	@Throws(Error::class)
	override fun update(networkVo: NetworkVo): NetworkVo? {
		log.info("update ... networkName: {}", networkVo.name)
		val res: Network? =
			conn.updateNetwork(networkVo.toEditNetworkBuilder(conn))
				.getOrNull()
		return res?.toNetworkVo(conn)
	}

	@Throws(Error::class)
	override fun remove(networkId: String): Boolean {
		log.info("remove ... ")
		val res: Result<Boolean> =
			conn.removeNetwork(networkId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun findAllNetworkProviderFromNetwork(): IdentifiedVo {
		log.info("findAllNetworkProviderFromNetwork ... ")
		val osProvider: OpenStackNetworkProvider =
			conn.findAllOpenStackNetworkProviders("networks")
				.getOrDefault(listOf())
				.first()
		return osProvider.fromOpenStackNetworkProviderToIdentifiedVo()
	}

	@Throws(Error::class)
	override fun findAllExternalNetworkFromNetworkProvider(providerId: String): List<NetworkVo> {
		log.info("findAllExternalNetworkFromNetworkProvider ... ")
		val res: List<Network> =
			conn.findAllNetworks()
				.getOrDefault(listOf())
				.filter { it.externalProviderPresent() }
		return res.toNetworkVos(conn)
	}

	@Throws(Error::class)
	override fun importNetwork(): NetworkVo {
		log.info("importNetwork ... ")

		TODO("Not yet implemented")
	}


	@Throws(Error::class)
	override fun findAllClustersFromNetwork(networkId: String): List<ClusterVo> {
		log.info("findAllClustersFromNetwork ... networkId: {}", networkId)
		conn.findNetwork(networkId)
			.getOrNull() ?: throw ErrorPattern.NETWORK_NOT_FOUND.toException()
		val res: List<Cluster> =
			conn.findAllClusters(follow = "networks")
				.getOrDefault(listOf())
				.filter {
					conn.findAllNetworksFromCluster(it.id())
						.getOrDefault(listOf())
						.any { n -> n.id() == networkId }
				}
		return res.toClusterVos(conn)
	}

	@Throws(Error::class)
	override fun findAllHostsFromNetwork(networkId: String): List<HostVo> {
		log.info("findAllHostsFromNetwork ... ")
		// TODO Attached / Unattahed 구분해야하는건지 보기
		conn.findNetwork(networkId)
			.getOrNull() ?: throw ErrorPattern.NETWORK_NOT_FOUND.toException()
		val res: List<Host> =
			conn.findAllHosts(follow = "nics")
				.getOrDefault(listOf())
				.filter { it.nics().first().networkPresent() && it.nics().first().network().id() == networkId }
		return res.toHostVos(conn) //TODO
	}

	@Throws(Error::class)
	override fun findAllVmsFromNetwork(networkId: String): List<VmVo> {
		// TODO VmVo가 가지고 있는 nicVo가 나옴 (vm -nics)
		//  근데 ovirt에서는 Nic가 우선  (nic - vm)
		log.info("getVmsByNetwork ... networkId: {}", networkId)
		conn.findNetwork(networkId)
			.getOrNull()?: throw ErrorPattern.NETWORK_NOT_FOUND.toException()

		val vms: List<Vm> =
			conn.findAllVms(follow = "reporteddevices,nics.vnicprofile")
				.getOrDefault(listOf())
				.filter { it.nics().any { nic -> nic.vnicProfile().network().id() == networkId } }
		return vms.toVmVoFromNetworks(conn)
	}

	@Throws(Error::class)
	override fun findAllTemplatesFromNetwork(networkId: String): List<NetworkTemplateVo> {
		log.info("findTemplatesByNetwork ... network: {}", networkId)
		// TODO?
		conn.findNetwork(networkId)
			.getOrNull() ?: throw ErrorPattern.NETWORK_NOT_FOUND.toException()
		val templates: List<Template> =
			conn.findAllTemplates(follow = "nics.vnicprofile")
				.getOrDefault(listOf())
				.filter { it.nics().any { nic -> nic.vnicProfile().network().id() == networkId }
		}

		return templates.flatMap { it ->
			it.nics().map { nic -> it.toNetworkTemplateVo(conn, nic) }
		}
	}

	@Throws(Error::class)
	override fun findAllPermissionsFromNetwork(networkId: String): List<PermissionVo> {
		log.info("getPermissionsByNetwork ... ")
		val permissions: List<Permission> =
			conn.findAllPermissionsFromNetwork(networkId)
				.getOrDefault(listOf())
		return permissions.toPermissionVos(conn)
	}


	companion object {
		private val log by LoggerDelegate()
	}
}
