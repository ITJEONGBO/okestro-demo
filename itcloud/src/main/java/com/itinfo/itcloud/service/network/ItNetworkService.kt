package com.itinfo.itcloud.service.network

import com.itinfo.common.LoggerDelegate
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.itcloud.error.ItemNotFoundException
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.fromNetworkToIdentifiedVo
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
	 * [ItNetworkService.findAllFromDataCenter]
	 * 네트워크 목록
	 *
	 * @param dataCenterId [String]
	 * @return List<[NetworkVo]> 네트워크 목록
	 */
	@Throws(Error::class)
	fun findAllFromDataCenter(dataCenterId: String): List<NetworkVo>

	/**
	 * [ItNetworkService.findOne]
	 * 네트워크 편집 / 정보
	 *
	 * @param networkId [String] 네트워크 아이디
	 * @return [NetworkVo] 네트워크 정보
	 */
	@Throws(ItemNotFoundException::class, Error::class)
	fun findOne(networkId: String): NetworkVo?


	// 네트워크 생성창 - 데이터센터 목록 [ItDataCenterService.findAll]
	/**
	 * [ItNetworkService.findAllClustersFromDataCenter]
	 * 네트워크 생성- 클러스터 목록 (연결, 필수)
	 *
	 * @param dataCenterId [String]
	 * @return List<[ClusterVo]>
	 */
	fun findAllClustersFromDataCenter(dataCenterId: String): List<ClusterVo>

	/**
	 * [ItNetworkService.add]
	 * 네트워크 생성
	 *
	 * @param networkVo [NetworkVo] 네트워크
	 * @return [NetworkVo]
	 */
	@Throws(Error::class)
	fun add(networkVo: NetworkVo): NetworkVo?
	/**
	 * [ItNetworkService.update]
	 * 네트워크 편집
	 *
	 * @param networkVo [NetworkVo] 네트워크 객체
	 * @return [NetworkVo]
	 */
	@Throws(Error::class)
	fun update(networkVo: NetworkVo): NetworkVo?
	/**
	 * [ItNetworkService.remove]
	 * 네트워크 삭제
	 *
	 * @param networkId [String] 네트워크 아이디
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun remove(networkId: String): Boolean
	/**
	 * [ItNetworkService.findAllNetworkProviderFromNetwork]
	 * 네트워크 가져오기- 네트워크 공급자 목록
	 *
	 * @return [OpenStackNetworkVo]
	 */
	@Throws(Error::class)
	fun findAllNetworkProviderFromNetwork(): OpenStackNetworkVo?
	/**
	 * [ItNetworkService.findAllExternalNetworkFromNetworkProvider]
	 * 네트워크 가져오기 창
	 * 네트워크 공급자가 가지고있는 네트워크(한개)
	 *
	 * @param providerId [String]
	 * @return List<[Network]>
	 */
	@Throws(Error::class)
	fun findAllExternalNetworkFromNetworkProvider(providerId: String): List<NetworkVo>
	/**
	 * [ItNetworkService.importNetwork]
	 * 네트워크 가져오기
	 *
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun importNetwork(): NetworkVo
	/**
	 * [ItNetworkService.findAllVnicProfilesFromNetwork]
	 * 네트워크 - vNIC Profile 목록
	 *
	 * @param networkId [String] 네트워크 아이디
	 * @return List<[VnicProfileVo]>
	 */
	@Throws(Error::class)
	fun findAllVnicProfilesFromNetwork(networkId: String): List<VnicProfileVo>
	// vnicProfile 생성,편집,삭제
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
		return networks.toNetworkVos(conn)
	}

	@Throws(Error::class)
	override fun findAllFromDataCenter(dataCenterId: String): List<NetworkVo> {
		log.info("findAll ... ")
		val networks: List<Network> =
			conn.findAllNetworks()
				.getOrDefault(listOf())
				.filter { it.dataCenter().id() == dataCenterId }
		return networks.toNetworkVos(conn)
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
	override fun findAllClustersFromDataCenter(dataCenterId: String): List<ClusterVo> {
		log.info("findAllClustersFromDataCenter ... : {}", dataCenterId)
		val res: List<Cluster> =
			conn.findAllClustersFromDataCenter(dataCenterId).
				getOrDefault(listOf())
		return res.toClusterVos(conn)
	}


	// TODO dc 다르면 중복명 가능
	@Throws(Error::class)
	override fun add(networkVo: NetworkVo): NetworkVo? {
		log.info("addNetwork ... ")
		val res: Network? =
			conn.addNetwork(networkVo.toAddNetworkBuilder(conn))
				.getOrNull()
		if(res == null){
			throw ErrorPattern.NETWORK_NOT_FOUND.toException()
		}

		// 생성 후에 나온 network Id로 클러스터 네트워크 생성 및 레이블 생성 가능
		// 기본 단순 생성은 클러스터가 할당되지도 필수도 선택되지 않음
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
		// 레이블 애매
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
	override fun findAllNetworkProviderFromNetwork(): OpenStackNetworkVo? {
		log.info("findAllNetworkProviderFromNetwork ... ")
		val osProvider: OpenStackNetworkProvider? =
			conn.findAllOpenStackNetworkProviders("networks")
				.getOrDefault(listOf())
				.firstOrNull()
		return osProvider?.toOpenStackNetworkVoIdName()
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
	override fun findAllVnicProfilesFromNetwork(networkId: String): List<VnicProfileVo> {
		log.info("findAllVnicProfilesFromNetwork ... networkId: {}", networkId)
		val res: List<VnicProfile> =
			conn.findAllVnicProfilesFromNetwork(networkId)
				.getOrDefault(listOf())
		return res.toVnicProfileVos(conn)
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



/*
@Service
class ItVnicProfileService(

): BaseService(), ItNetworkService.VnicProfile {

	@Throws(Error::class)
	override fun getVnicProfileByNetwork(networkId: String): List<VnicProfileVo> {
		log.info("getVnicByNetwork ... ")
		if (conn.findNetwork(networkId).getOrNull() == null) {
			log.warn("getVnicByNetwork ... 네트워크 없음")
			return listOf()
		}

		val vnicProfiles: List<VnicProfile> =
			conn.findAllVnicProfilesFromNetwork(networkId)
				.getOrDefault(listOf())
		log.info("네트워크 vnic 목록")
		return vnicProfiles.toVnicProfileVos(conn)
	}

	@Throws(Error::class)
	override fun findOneVnicProfile(networkId: String): VnicProfileVo? {
		log.info("findOneVnic ... networkId: {}", networkId)
		val network: Network? =
			conn.findNetwork(networkId)
				.getOrNull()
		return network?.VnicProfileVo(conn)
	}


	override fun addVnicProfileByNetwork(networkId: String, vcVo: VnicProfileVo): Boolean {
		log.info("addVnicByNetwork ... networkId: {}", networkId)
		val aVnicsService = system.networksService().networkService(networkId).vnicProfilesService()
		val vpList = system.networksService().networkService(networkId).vnicProfilesService().list().send().profiles()
		val hasDuplicateName = vpList.any {
			it.name() == vcVo.name
		}
		if (hasDuplicateName) {
			log.error("vnic 이름 중복")
			return false
		}

		try {
			val vnicBuilder = VnicProfileBuilder()
				.network(NetworkBuilder().id(networkId).build())
				.name(vcVo.name)
				.description(vcVo.description) // 네트워크 필터 기본생성됨
				.passThrough(VnicPassThroughBuilder().mode(vcVo.passThrough).build())
				.migratable(vcVo.migration)
				.build()

			aVnicsService.add().profile(vnicBuilder).send().profile()
			log.info("네트워크 vnic 생성")
			return Res.successResponse()
		} catch (e: Exception) {
			log.error("네트워크 vnic 생성 실패 ... 이유: {}", e.localizedMessage)
			return Res.fail(e)
		}
	}

	override fun setEditVnicProfileByNetwork(networkId: String, vcId: String): VnicProfileVo? {
		log.info("setEditVnicByNetwork ... networkId: {}, vcId: {}", networkId, vcId)
		if(conn.findNetwork(networkId).getOrNull() == null) {
			log.warn("setEditVnicByNetwork ... 네트워크 없음")
			return null
		}

		val vnicProfile: VnicProfile = conn.findVnicProfileFromNetwork(networkId, vcId) ?: run{
			log.warn("getNetworkDetail ... vnic 없음")
			return null
		}

		log.info("vnic 프로파일 편집")
		return vnicProfile.toVnicProfileVo(conn)
	}

	override fun editVnicProfile(networkId: String, vnicProfileId: String, vcVo: VnicProfileVo): Boolean {
		log.info("editVnic ... ")
		val res: Result<VnicProfile?> = conn.updateVnicProfile(vnicProfileId, VnicProfileBuilder()
			.name(vcVo.name)
			.description(vcVo.description)
			.passThrough(VnicPassThroughBuilder().mode(vcVo.passThrough).build())
			.migratable(vcVo.migration)
			.portMirroring(vcVo.portMirroring)
			.build())
		return res.isSuccess && res.getOrNull() != null
	}

	override fun deleteVnicProfileByNetwork(networkId: String, vnicProfileId: String): Boolean {
		log.info("deleteVnic ... ")
		val res: Result<Boolean> =
			conn.removeVnicProfile(vnicProfileId)
		return res.isSuccess
	}


	companion object {
		private val log by LoggerDelegate()
	}
}*/
