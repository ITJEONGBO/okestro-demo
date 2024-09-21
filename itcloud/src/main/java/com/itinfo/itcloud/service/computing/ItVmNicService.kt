package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.network.NicVo
import com.itinfo.itcloud.model.network.toNicVoFromVm
import com.itinfo.itcloud.model.network.toNicVosFromVm
import com.itinfo.itcloud.model.network.*
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import com.itinfo.util.ovirt.error.toError
import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.builders.MacBuilder
import org.ovirt.engine.sdk4.builders.NicBuilder
import org.ovirt.engine.sdk4.builders.VnicProfileBuilder
import org.ovirt.engine.sdk4.types.NetworkFilterParameter
import org.ovirt.engine.sdk4.types.Nic
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

interface ItVmNicService {
	/**
	 * [ItVmNicService.findAllNicsFromVm]
	 * 네트워크 인터페이스
	 *
	 * @param vmId [String] 가상머신 id
	 * @return List<[NicVo]>
	 */
	@Throws(Error::class)
	fun findAllNicsFromVm(vmId: String): List<NicVo>
	/**
	 * [ItVmNicService.findNicFromVm]
	 * 네트워크 인터페이스 정보, 편집
	 *
	 * @param vmId [String] 가상머신 id
	 * @param nicId [String] nic id
	 * @return [NicVo]?
	 */
	@Throws(Error::class)
	fun findNicFromVm(vmId: String, nicId: String): NicVo?

	 // 네트워크 인터페이스 생성창 - VnicProfile 목록 [ItVmService.findAllVnicProfilesFromCluster]

	/**
	 * [ItVmNicService.addNicFromVm]
	 * 네트워크 인터페이스 생성
	 *
	 * @param vmId [String] 가상머신 id
	 * @param nicVo [NicVo]
	 * @return [NicVo]?
	 */
	@Throws(Error::class)
	fun addNicFromVm(vmId: String, nicVo: NicVo): NicVo?
	/**
	 * [ItVmNicService.updateNicFromVm]
	 * 네트워크 인터페이스 편집
	 *
	 * @param vmId [String] 가상머신 id
	 * @param nicVo [NicVo]
	 * @return [NicVo]?
	 */
	@Throws(Error::class)
	fun updateNicFromVm(vmId: String, nicVo: NicVo): NicVo?
	/**
	 * [ItVmNicService.removeNicFromVm]
	 * 네트워크 인터페이스 삭제
	 *
	 * @param vmId [String] 가상머신 id
	 * @param nicId [String] nic id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun removeNicFromVm(vmId: String, nicId: String): Boolean
}

@Service
class VmNicServiceImpl(

) : BaseService(), ItVmNicService {

	@Throws(Error::class)
	override fun findAllNicsFromVm(vmId: String): List<NicVo> {
		log.info("findAllNicsFromVm ... vmId: {}", vmId)
		val res: List<Nic> =
			conn.findAllNicsFromVm(vmId)
				.getOrDefault(listOf())
		return res.toNicVosFromVm(conn, vmId)
	}

	@Throws(Error::class)
	override fun findNicFromVm(vmId: String, nicId: String): NicVo? {
		log.info("findNicFromVm ... vmId: {}, nicId: {}", vmId, nicId)
		val res: Nic? =
			conn.findNicFromVm(vmId, nicId).getOrNull()
		return res?.toNicVoFromVm(conn, vmId)
	}

	@Throws(Error::class)
	override fun addNicFromVm(vmId: String, nicVo: NicVo): NicVo? {
		log.info("addFromVm ... ")
		val res: Nic? =
			conn.addNicFromVm(vmId, nicVo.toAddNicBuilder())
				.getOrNull()
		return res?.toNicVoFromVm(conn, vmId)

// 		네트워크 필터 매개변수 (네트워크 필터랑 다른거 같음)
//		val nfps: List<NetworkFilterParameter> =
//			nicVo.nfpVos.map { nFVo: NetworkFilterParameterVo ->
//				NetworkFilterParameterBuilder()
//					.name(nFVo.name)
//					.value(nFVo.value)
//					.nic(nic)
//					.build()
//			}
//		for (np in nfps)
//			conn.addNicNetworkFilterParameterFromVm(vmId, nic.id(), np)
//		val nfps: List<NetworkFilterParameter> = nicVo.nfpVos.ttoNetworkFilterParameters()
//		for (np in nfps)
//			conn.addNicNetworkFilterParameterFromVm(vmId, nic.id(), np)
	}

	@Throws(Error::class)
	override fun updateNicFromVm(vmId: String, nicVo: NicVo): NicVo? {
		log.info("updateFromVm ... ")
		val res: Nic? =
			conn.updateNicFromVm(vmId, nicVo.toEditNicBuilder())
				.getOrNull()
		return res?.toNicVoFromVm(conn, vmId)
	}

	@Throws(Error::class)
	override fun removeNicFromVm(vmId: String, nicId: String): Boolean {
		log.info("removeFromVm ... ")
		val res: Result<Boolean> =
			conn.removeNicFromVm(vmId, nicId)
		return res.isSuccess
	}

	companion object {
		private val log by LoggerDelegate()
	}
}