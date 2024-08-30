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
	 * [ItVmNicService.findOneNicFromVm]
	 * nic 수정창
	 *
	 * @param vmId [String] 가상머신 id
	 * @param nicId [String] nic id
	 * @return [NicVo]
	 */
	@Throws(Error::class)
	fun findOneNicFromVm(vmId: String, nicId: String): NicVo?
	/**
	 * [ItVmNicService.addFromVm]
	 * 가상머신 - 새 네트워크 인터페이스
	 * 생성창은 필요없음, 왜냐면 프로파일 리스트만 가지고 오면됨
	 * public List<VnicProfileVo> setVnic(String clusterId) {} 사용하면 됨
	 *
	 * @param vmId [String] 가상머신 id
	 * @param nicVo [NicVo]
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun addFromVm(vmId: String, nicVo: NicVo): NicVo? // nic 추가
	/**
	 * [ItVmNicService.updateFromVm]
	 *
	 * @param vmId [String] 가상머신 id
	 * @param nicVo [NicVo]
	 * @return CommonVo<[Boolean]> 201(create) 404(fail)
	 */
	@Throws(Error::class)
	fun updateFromVm(vmId: String, nicVo: NicVo): NicVo? // nic 수정
	/**
	 * [ItVmNicService.removeFromVm]
	 *
	 * @param vmId [String] 가상머신 id
	 * @param nicId [String] nic id
	 * @return [Boolean] 200(success) 404(fail)
	 */
	@Throws(Error::class)
	fun removeFromVm(vmId: String, nicId: String): Boolean // nic 삭제
}

@Service
class VmNicServiceImpl(

) : BaseService(), ItVmNicService {

	@Throws(Error::class)
	override fun findAllNicsFromVm(vmId: String): List<NicVo> {
		log.info("findAllFromVm ... vmId: {}", vmId)
		val res: List<Nic> =
			conn.findAllNicsFromVm(vmId)
				.getOrDefault(listOf())
		return res.toNicVosFromVm(conn, vmId)
	}

	@Throws(Error::class)
	override fun findOneNicFromVm(vmId: String, nicId: String): NicVo? {
		log.info("findOneNicFromVm ... vmId: {}, nicId: {}", vmId, nicId)
		conn.findVm(vmId).getOrNull()?:throw ErrorPattern.VM_NOT_FOUND.toError()
		val res: Nic =
			conn.findNicFromVm(vmId, nicId).getOrNull() ?:throw ErrorPattern.NIC_NOT_FOUND.toError()
		return res.toNicVoFromVm(conn, vmId)
	}

	@Throws(Error::class)
	override fun addFromVm(vmId: String, nicVo: NicVo): NicVo? {
		log.info("addFromVm ... ")
		conn.findVm(vmId).getOrNull()?:throw ErrorPattern.VM_NOT_FOUND.toError()
		val nic: Nic =
			conn.addNicFromVm(vmId, nicVo.toAddNicBuilder())
				.getOrNull() ?: throw ErrorPattern.NIC_NOT_FOUND.toError()
		return nic.toNicVoFromVm(conn, vmId)

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
	override fun updateFromVm(vmId: String, nicVo: NicVo): NicVo? {
		log.info("updateFromVm ... ")
		conn.findVm(vmId).getOrNull()?:throw ErrorPattern.VM_NOT_FOUND.toError()
		val nic: Nic =
			conn.updateNicFromVm(vmId, nicVo.toEditNicBuilder())
				.getOrNull() ?: throw ErrorPattern.NIC_NOT_FOUND.toError()
		return nic.toNicVoFromVm(conn, vmId)
	}

	@Throws(Error::class)
	override fun removeFromVm(vmId: String, nicId: String): Boolean {
		log.info("removeNicFromVm ... ")
		conn.findVm(vmId).getOrNull() ?: throw ErrorPattern.VM_NOT_FOUND.toError()
		conn.findNicFromVm(vmId, nicId).getOrNull() ?: throw ErrorPattern.NIC_NOT_FOUND.toError()
		val res: Result<Boolean> =
			conn.removeNicFromVm(vmId, nicId)
		return res.isSuccess
	}

	companion object {
		private val log by LoggerDelegate()
	}
}