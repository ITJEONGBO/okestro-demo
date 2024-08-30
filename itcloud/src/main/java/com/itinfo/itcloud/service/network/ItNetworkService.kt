package com.itinfo.itcloud.service.network

import com.itinfo.common.LoggerDelegate
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.itcloud.error.ItemNotFoundException
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.computing.*
import com.itinfo.itcloud.model.network.*
import com.itinfo.itcloud.model.setting.PermissionVo
import com.itinfo.itcloud.model.setting.toPermissionVos
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.toError
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
	 * 네트워크 편집창
	 *
	 * @param networkId [String] 네트워크 아이디
	 * @return [NetworkVo] 네트워크 정보
	 */
	@Throws(ItemNotFoundException::class, Error::class)
	fun findOne(networkId: String): NetworkVo?

	/**
	 * [ItNetworkService.findAllDatCentersFromNetwork]
	 * 네트워크 생성- 데이터센터 목록
	 *
	 * @return List<[DataCenterVo]> 데이터센터 목록
	 */
	@Deprecated("[ItDataCenterService.findAll] 과 내용 비슷함")
	@Throws(Error::class)
	fun findAllDatCentersFromNetwork(): List<DataCenterVo>
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
	 * 네트워크 가져오기 - 네트워크 공급자가 가지고있는 네트워크
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
	fun importNetwork(): Boolean
	/**
	 * [ItNetworkService.findAllVnicProfilesFromNetwork]
	 * 네트워크 - vNIC Profile 목록
	 *
	 * @param networkId [String] 네트워크 아이디
	 * @return List<[VnicProfileVo]>
	 */
	@Throws(Error::class)
	fun findAllVnicProfilesFromNetwork(networkId: String): List<VnicProfileVo>
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
	 * @return List<[NetworkVmVo]>
	 */
	@Throws(Error::class)
	fun findAllVmsFromNetwork(networkId: String): List<NetworkVmVo>
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
		log.info("getNetworks ... ")
		val networks: List<Network> =
			conn.findAllNetworks()
				.getOrDefault(listOf())
		return networks.toNetworkVos(conn)
	}

	@Throws(Error::class)
	override fun findOne(networkId: String): NetworkVo? {
		log.info("getNetwork ... networkId: {}", networkId)
		val res: Network =
			conn.findNetwork(networkId, "networklabels")
				.getOrNull() ?: throw ErrorPattern.NETWORK_NOT_FOUND.toError()
		return res.toNetworkVo(conn)
	}

	@Deprecated("[ItDataCenterService.findAll] 과 내용 비슷함")
	@Throws(Error::class)
	override fun findAllDatCentersFromNetwork(): List<DataCenterVo> {
		log.info("findAllDatCentersFromNetwork ... ")
		val dataCenters: List<DataCenter> =
			conn.findAllDataCenters()
				.getOrDefault(listOf())
		return dataCenters.toDataCenterIdNames()
	}

	// 새 논리 네트워크 추가
	// 필요 name, datacenter_id
	@Throws(Error::class)
	override fun add(networkVo: NetworkVo): NetworkVo? {
		log.info("addNetwork ... ")
		val res: Network =
			conn.addNetwork(networkVo.toNetworkBuilder(conn).build())
				.getOrNull() ?: throw ErrorPattern.NETWORK_NOT_FOUND.toError()
		return res.toNetworkVo(conn)
		// 클러스터 연결, 필수 선택
/*
		// 기본생성되는 vnicprofile 삭제
		val vnicProfile: VnicProfile =
			conn.findAllVnicProfilesFromNetwork(network.id())
				.getOrDefault(listOf())
				.firstOrNull() ?: throw ErrorPattern.NIC_NOT_FOUND.toError()

		val resRemoveDefaultVnicProfile: Result<Boolean> =
			conn.removeVnicProfileFromNetwork(network.id(), vnicProfile.id())
		log.info("기본 vnicprofile 제거 결과: {}", resRemoveDefaultVnicProfile.isSuccess)
		// 추가해야 할 vnicprofile
		for (vo in networkVo.vnicProfileVos) {
			val vnicProfile2Build: VnicProfile = VnicProfileBuilder().name(vo.name).build()
			val resAddVnicProfile: Result<VnicProfile> =
				conn.addVnicProfileFromNetwork(network.id(), vnicProfile2Build)
			log.info("신규 vnicprofile(s) 추가 결과: {}", resAddVnicProfile.isSuccess)
		}

		// 클러스터 모두연결이 선택되어야지만 모두 필요가 선택됨
		// TODO: isConnected가 되어야 할 조건 찾아야 함
		val clusterVos: List<ClusterVo> = networkVo.clusterVos.filter { it.isConnected *//* 연결된 경우만 필터링 *//* }

		for (clusterVo in clusterVos) {
			val n: Network = NetworkBuilder()
				.id(network.id())
				.required(clusterVo.required) // TODO: 어디서 찾는 값?
				.build()
			val resNetwork: Result<Network?> =
				conn.addNetworkFromCluster(clusterVo.id, n)
			log.info("신규 network(s) 추가 결과: {}", resNetwork.isSuccess)
		}

		// 외부 공급자 처리시 레이블 생성 안됨
		if (networkVo.label.isNotEmpty()) {
			val resAddNetworkLabel: Result<NetworkLabel> =
				conn.addNetworkLabelFromNetwork(network.id(), NetworkLabelBuilder().id(networkVo.label).build())
			log.info("신규 networkLabel 추가 결과: {}", resAddNetworkLabel.isSuccess)
		}

		log.info("network {} 추가 완료", network.name())
		return network.toNetworkVo(conn)*/
	}


	@Throws(Error::class)
	override fun update(networkVo: NetworkVo): NetworkVo? {
		log.info("update ... networkName: {}", networkVo.name)
		val res: Network =
			conn.updateNetwork(networkVo.toEditNetworkBuilder(conn))
				.getOrNull()?: throw ErrorPattern.NETWORK_NOT_FOUND.toError()
		return res.toNetworkVo(conn)
		/*val dataCenter: DataCenter =
			conn.findDataCenter(networkVo.dataCenterVo.id)
				.getOrNull() ?: throw ErrorPattern.DATACENTER_NOT_FOUND.toError()
		val network: Network =
			conn.findNetwork(networkVo.id)
				.getOrNull() ?: throw ErrorPattern.NETWORK_NOT_FOUND.toError()
		val res: Result<Network?> =
			conn.updateNetwork(networkVo.id, network)

		val networkService = system.networksService().networkService(networkVo.id)
		try {
			val networkBuilder = NetworkBuilder()
				.id(networkVo.id)
				.name(networkVo.name)
				.description(networkVo.description)
				.comment(networkVo.comment)
				.usages(if (networkVo.usageVo.vm) NetworkUsage.VM else NetworkUsage.DEFAULT_ROUTE)
				// .dnsResolverConfiguration()   // TODO:HELP DNS 구현안됨
				.mtu(networkVo.mtu)
				.stp(networkVo.stp)
//				.vlan(VlanBuilder().id(ncVo.vlan))
				// .externalProvider(ncVo.getExternalProvider() ? system.openstackNetworkProvidersService().list().send().providers().get(0) : null)  // 수정불가
				.dataCenter(dataCenter)

			// 외부 공급자 처리시 레이블 생성 안됨
//			if (ncVo.label.isNotEmpty()) {
//				val nlsService = system.networksService().networkService(ncVo.id).networkLabelsService()
//				if (nlsService.list().send().labels()[0].idPresent()) {
//					nlsService.labelService(nlsService.list().send().labels()[0].id()).remove().send()
//				}
//				nlsService.add().label(NetworkLabelBuilder().id(ncVo.label)).send() // 그리고 다시 생성
//			}

			networkService.update().network(networkBuilder.build()).send()
			log.info("네트워크 편집 완료")
			return true
		} catch (e: Exception) {
			log.error("네트워크 편집에러 ... 이유: {}", e.localizedMessage)
			return false
		}*/
	}

	@Throws(Error::class)
	override fun remove(networkId: String): Boolean {
		log.info("remove ... ")
		conn.findNetwork(networkId).getOrNull()
		val res: Result<Boolean> =
			conn.removeNetwork(networkId)
		return res.isSuccess

		/*val networkService = system.networksService().networkService(networkId)
		val clusterList: List<Cluster> =
			conn.findAllClusters()
				.getOrDefault(listOf())
		// 네트워크가 비가동 중인지 확인
		val canDelete = clusterList
			.flatMap { conn.findAllNetworksFromCluster(it.id()).getOrDefault(listOf()) }
			.filter { it.id() == networkId }
			.filterNot { it.status() == NetworkStatus.OPERATIONAL }
			.isEmpty()
		// .noneMatch(network -> network.status().equals(NetworkStatus.OPERATIONAL));

		if (!canDelete) { // 삭제 가능한 경우 네트워크를 삭제하고 성공 응답을 반환합니다
			log.error("network 삭제 실패 ... ")
			// return Res.fail(404, "network 삭제 실패")
			return false
		}
		try {
			networkService.remove().send()
			log.info("network 삭제 성공")
			return true
		} catch (e: Exception) {
			log.error("network 삭제 실패 ... 이유: {}", e.localizedMessage)
			return false
		}*/
	}

	@Throws(Error::class)
	override fun findAllNetworkProviderFromNetwork(): OpenStackNetworkVo? {
		log.info("findAllNetworkProviderFromNetwork ... ")
		val osProvider: OpenStackNetworkProvider? =
			conn.findAllOpenStackNetworkProviders("networks")
				.getOrDefault(listOf())
				.firstOrNull()
		val networks: List<Network> =
			conn.findAllNetworks()
				.getOrDefault(listOf())
				.filter { it.externalProviderPresent() }
		networks.map { it.name() }.forEach { log.debug("nw's name {}", it) }
		return osProvider?.toOpenStackNetworkVoIdName()
	}

	@Throws(Error::class)
	override fun findAllExternalNetworkFromNetworkProvider(providerId: String): List<NetworkVo> {
		TODO("Not yet implemented")
	}

	@Throws(Error::class)
	override fun importNetwork(): Boolean {
		log.info("importNetwork ... ")
		// 그냥 있는거 가져오기
		val osProvider: OpenStackProvider? =
			conn.findAllOpenStackNetworkProviders("network")
				.getOrDefault(listOf())
				.firstOrNull()
		return true // TODO: 데이터 필요할 경우 return 정하기
	}

	@Throws(Error::class)
	override fun findAllVnicProfilesFromNetwork(networkId: String): List<VnicProfileVo> {
		log.info("findAllVnicProfilesFromNetwork ... networkId: {}", networkId)
		log.info("findAllVnicProfilesFromNetwork ... ")
		conn.findNetwork(networkId).getOrNull() ?: throw ErrorPattern.NETWORK_NOT_FOUND.toException()

		val res: List<VnicProfile> =
			conn.findAllVnicProfilesFromNetwork(networkId)
				.getOrDefault(listOf())
		return res.toVnicProfileVos(conn)
	}

	@Throws(Error::class)
	override fun findAllClustersFromNetwork(networkId: String): List<ClusterVo> {
		log.info("findAllClustersFromNetwork ... networkId: {}", networkId)
		conn.findNetwork(networkId).getOrNull() ?: throw ErrorPattern.NETWORK_NOT_FOUND.toException()
		val clusters: List<Cluster> =
			conn.findAllClusters(follow = "networks")
				.getOrDefault(listOf())
				.filter {
					conn.findAllNetworksFromCluster(it.id())
						.getOrDefault(listOf())
						.any { n -> n.id() == networkId }
				}
		return clusters.toClusterVos(conn)
	}

	@Throws(Error::class)
	override fun findAllHostsFromNetwork(networkId: String): List<HostVo> {
		log.info("getHost ... ")
		conn.findNetwork(networkId).getOrNull() ?: throw ErrorPattern.NETWORK_NOT_FOUND.toException()
		val hosts =
			conn.findAllHosts(follow = "nics")
				.getOrDefault(listOf())
				.filter { it.nics().first().networkPresent() && it.nics().first().network().id() == networkId }
		return hosts.toHostVos(conn) //TODO

		/*return hosts.flatMap { host ->
			val naList: List<NetworkAttachment> = conn.findAllNetworkAttachmentsFromHost(host.id())
			val cluster: Cluster? = conn.findCluster(host.cluster().id())
			val nicList: List<HostNic> = conn.findAllNicsFromHost(host.id())

			naList.filter { it.networkPresent() && it.network().id() == networkId }
				.map { n: NetworkAttachment ->
				log.debug("nicList.size: {}", nicList.size)
				val hostNic: HostNic = nicList.first()
				val statistics: List<Statistic> = conn.findAllStatisticsFromHostNic(host.id(), hostNic.id())
				NetworkHostVo.builder {
					hostId { host.id() }
					hostName { host.name() }
					hostStatus { host.status() }
					clusterName { cluster?.name() }
					datacenterName { cluster?.dataCenter()?.let { conn.findDataCenterName(it.id()) } }
					networkStatus { hostNic.status() }
					networkDevice { hostNic.name() }
					speed { hostNic.speed() }
					rxSpeed { statistics.findSpeed("data.current.rx.bps") }
					txSpeed { statistics.findSpeed( "data.current.tx.bps") }
					rxTotalSpeed { statistics.findSpeed( "data.total.rx") }
					txTotalSpeed { statistics.findSpeed( "data.total.tx") }
				}*/
				// TODO: 이게 맞는지...
	}

	@Throws(Error::class)
	override fun findAllVmsFromNetwork(networkId: String): List<NetworkVmVo> {
		log.info("getVmsByNetwork ... networkId: {}", networkId)
		conn.findNetwork(networkId).getOrNull() ?: throw ErrorPattern.NETWORK_NOT_FOUND.toError()

		val vms: List<Vm> =
			conn.findAllVms(follow = "reporteddevices,nics.vnicprofile")
				.getOrDefault(listOf())
				.filter { it.nics().any { nic -> nic.vnicProfile().network().id() == networkId } }

		// TODO?
		return vms.flatMap {
				it.nics().map { nic ->
					val statistics: List<Statistic> =
						conn.findAllStatisticsFromVmNic(it.id(), nic.id())
							.getOrDefault(listOf())
					it.toNetworkVmVo(conn, nic, statistics)
				}
			}
	}

	@Throws(Error::class)
	override fun findAllTemplatesFromNetwork(networkId: String): List<NetworkTemplateVo> {
		log.info("findTemplatesByNetwork ... network: {}", networkId)
		conn.findNetwork(networkId).getOrNull() ?: throw ErrorPattern.NETWORK_NOT_FOUND.toError()
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
