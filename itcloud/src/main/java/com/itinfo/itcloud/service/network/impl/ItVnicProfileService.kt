/*
package com.itinfo.itcloud.service.network

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.ItemNotFoundException
import com.itinfo.itcloud.model.network.VnicProfileVo
import com.itinfo.itcloud.model.network.toVnicProfileVo
import com.itinfo.itcloud.model.network.toVnicProfileVos
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.error.toError
import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.builders.NetworkBuilder
import org.ovirt.engine.sdk4.builders.VnicPassThroughBuilder
import org.ovirt.engine.sdk4.builders.VnicProfileBuilder
import org.ovirt.engine.sdk4.types.Network
import org.ovirt.engine.sdk4.types.VnicProfile
import org.springframework.stereotype.Service

// 네트워크 생성시 자동으로 생성되는 vnicProfile 정도로만 사용해도 된다고 하심
@Deprecated("기능구현 필요 없을듯 함")
interface ItVnicProfileService {
	*/
/**
	 * [ItNetworkService.findAllFromNetwork]
	 *
	 * @param networkId [String] 네트워크 아이디
	 * @return List<[VnicProfileVo]>
	 *//*

	@Throws(Error::class)
	fun findAllFromNetwork(networkId: String): List<VnicProfileVo>
	*/
/**
	 * [ItNetworkService.findOneFromNetwork]
	 *
	 * @param networkId [String] 네트워크 아이디
	 * @return [VnicProfileVo]
	 *//*

	@Throws(ItemNotFoundException::class, Error::class)
	fun findOneFromNetwork(networkId: String): VnicProfileVo?
	*/
/**
	 * [ItNetworkService.addFromNetwork]
	 *
	 * @param networkId [String] 네트워크 아이디
	 * @param vcVo [VnicProfileVo]
	 * @return CommonVo<[Boolean]>
	 *//*

	fun addFromNetwork(networkId: String, vcVo: VnicProfileVo): VnicProfileVo?
	*/
/**
	 * [ItNetworkService.findOneFromNetwork]
	 *
	 * @param vcId [String] vnic 아이디
	 * @return [VnicCreateVo]
	 *//*

	fun findOneFromNetwork(networkId: String, vcId: String): VnicProfileVo?
	*/
/**
	 * [ItNetworkService.updateFromNetwork]
	 *
	 * @param networkId [String] 네트워크 아이디
	 * @param vnicProfileId [String]
	 * @param vcVo [VnicCreateVo]
	 * @return [Boolean]
	 *//*

	fun update(networkId: String, vnicProfileId: String, vcVo: VnicProfileVo): VnicProfileVo?
	*/
/**
	 * [ItNetworkService.deleteVnicProfileByNetwork]
	 *
	 * @param networkId [String] 네트워크 아이디
	 * @param vnicProfileId [String] 네트워크 아이디
	 * @return [Boolean]
	 *//*

	fun remove(networkId: String, vnicProfileId: String): Boolean
}


@Service
class VnicProfileServiceImpl(

): BaseService(), ItVnicProfileService {

	@Throws(Error::class)
	override fun findAllFromNetwork(networkId: String): List<VnicProfileVo> {
		log.info("findAllFromNetwork ... ")
		conn.findNetwork(networkId).getOrNull() ?: throw ErrorPattern.NETWORK_NOT_FOUND.toError()
		val res: List<VnicProfile> =
			conn.findAllVnicProfilesFromNetwork(networkId)
				.getOrDefault(listOf())
		return res.toVnicProfileVos(conn)
	}

	@Throws(Error::class)
	override fun findOneVnicProfile(networkId: String): VnicProfileVo? {
		log.info("findOneVnic ... networkId: {}", networkId)
		val network: Network =
			conn.findNetwork(networkId).getOrNull() ?: throw ErrorPattern.NETWORK_NOT_FOUND.toError()
		val vnicProfileId: String = network.vnicProfiles().firstOrNull()?.id() ?: throw ErrorPattern.VNIC_PROFILE_ID_NOT_FOUND.toError()
		// TODO: 이렇게 찾아야 하는게 맞는지 모르겠음 ...
		val vnicProfile: VnicProfile =
			conn.findVnicProfileFromNetwork(networkId, vnicProfileId)
				.getOrNull() ?: throw ErrorPattern.VNIC_PROFILE_NOT_FOUND.toError()

		return vnicProfile.toVnicProfileVo(conn)
	}

	override fun addVnicProfileByNetwork(networkId: String, vcVo: VnicProfileVo): VnicProfileVo? {
		log.info("addVnicProfileFromNetwork ... networkId: {}", networkId)
		val vnicProfile2Build = VnicProfileBuilder()
			.network(NetworkBuilder().id(networkId).build())
			.name(vcVo.name)
			.description(vcVo.description) // 네트워크 필터 기본생성됨
			.passThrough(VnicPassThroughBuilder().mode(vcVo.passThrough).build())
			.migratable(vcVo.migration)
			.build()

		val vnicProfileBuilt: VnicProfile? =
			conn.addVnicProfileFromNetwork(networkId, vnicProfile2Build)
				.getOrNull()
		return vnicProfileBuilt?.toVnicProfileVo(conn)
	}

	override fun setEditVnicProfileByNetwork(networkId: String, vcId: String): VnicProfileVo? {
		log.info("setEditVnicByNetwork ... networkId: {}, vcId: {}", networkId, vcId)
		conn.findNetwork(networkId).getOrNull() ?: throw ErrorPattern.NETWORK_NOT_FOUND.toError()
		val vnicProfile: VnicProfile =
			conn.findVnicProfileFromNetwork(networkId, vcId).getOrNull() ?: throw ErrorPattern.VNIC_PROFILE_NOT_FOUND.toError()
		return vnicProfile.toVnicProfileVo(conn)
	}

	override fun updateVnicProfileFromNetwork(networkId: String, vnicProfileId: String, vcVo: VnicProfileVo): VnicProfileVo? {
		log.info("editVnicProfile ... ")
		val vnicProfile: VnicProfile = VnicProfileBuilder()
			.name(vcVo.name)
			.description(vcVo.description)
			.passThrough(VnicPassThroughBuilder().mode(vcVo.passThrough).build())
			.migratable(vcVo.migration)
			.portMirroring(vcVo.portMirroring)
			.build()

		val res: VnicProfile? =
			conn.updateVnicProfile(vnicProfileId, vnicProfile)
				.getOrNull()
		return res?.toVnicProfileVo(conn)
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
