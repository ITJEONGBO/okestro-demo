package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.error.toException
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.ConsoleVo
import com.itinfo.itcloud.model.computing.VmExportVo
import com.itinfo.itcloud.model.computing.toConsoleVo
import com.itinfo.itcloud.model.fromHostsToIdentifiedVos
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import com.itinfo.util.ovirt.error.ErrorPattern
import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.types.Host
import org.ovirt.engine.sdk4.types.Vm

import org.springframework.stereotype.Service

interface ItVmOperationService {
	/**
	 * [ItVmOperationService.start]
	 * 가상머신 - 실행
	 *
	 * @param vmId [String] 가상머신 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun start(vmId: String): Boolean
	/**
	 * [ItVmOperationService.pause]
	 * 가상머신 - 일시정지
	 *
	 * @param vmId [String] 가상머신 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun pause(vmId: String): Boolean
	/**
	 * [ItVmOperationService.powerOff]
	 * 가상머신 - 전원끔
	 *
	 * @param vmId [String] 가상머신 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun powerOff(vmId: String): Boolean
	/**
	 * [ItVmOperationService.shutdown]
	 * 가상머신 - 종료
	 *
	 * @param vmId [String] 가상머신 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun shutdown(vmId: String): Boolean
	/**
	 * [ItVmOperationService.reboot]
	 * 가상머신 - 재부팅
	 *
	 * @param vmId [String] 가상머신 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun reboot(vmId: String): Boolean
	/**
	 * [ItVmOperationService.reset]
	 * 가상머신 - 재설정
	 * 
	 * @param vmId [String] 가상머신 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun reset(vmId: String): Boolean

	/**
	 * [ItVmOperationService.migrateHostList]
	 * 마이그레이션 할 수 있는 호스트 목록
	 *
	 * @param vmId [String] 가상머신 Id
	 * @return List<[IdentifiedVo]>
	 */
	@Throws(Error::class)
	fun migrateHostList(vmId: String): List<IdentifiedVo>
	/**
	 * [ItVmOperationService.migrate]
	 * 가상머신 - 마이그레이션
	 *
	 * @param vmId [String] 가상머신 Id
	 * @param hostId [String] 마이그레이션할 호스트 Id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun migrate(vmId: String, hostId: String): Boolean

	// 가상머신 내보내기 창
	// 		호스트 목록 [ItClusterService.findAllHostsFromCluster] (가상 어플라이언스로 가상머신 내보내기)

	/**
	 * [ItVmOperationService.exportOva]
	 * 가상머신 ova로 내보내기 (실행시, 해당 host?vm? 내부에 파일이 생성됨)
	 *
	 * @param vmId [String]
	 * @param vmExportVo [VmExportVo]
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun exportOva(vmId: String, vmExportVo: VmExportVo): Boolean

	/**
	 * [ItVmOperationService.console]
	 * 가상머신 콘솔
	 *
	 * @param vmId [String] 가상머신 Id
	 */
	@Throws(Error::class)
	fun console(vmId: String): ConsoleVo?
}

@Service
class VmOperationServiceImpl: BaseService(), ItVmOperationService {

	@Throws(Error::class)
	override fun start(vmId: String): Boolean {
		log.info("start ... vmId: {}", vmId)
		val res: Result<Boolean> = conn.startVm(vmId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun pause(vmId: String): Boolean {
		log.info("pause ... vmId: {}", vmId)
		val res: Result<Boolean> = conn.suspendVm(vmId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun powerOff(vmId: String): Boolean {
		log.info("powerOff ... vmId: {}", vmId)
		val res: Result<Boolean> = conn.stopVm(vmId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun shutdown(vmId: String): Boolean {
		log.info("shutdown ... vmId: {}", vmId)
		val res: Result<Boolean> = conn.shutdownVm(vmId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun reboot(vmId: String): Boolean {
		log.info("reboot ... vmId: {}", vmId)
		val res: Result<Boolean> = conn.rebootVm(vmId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun reset(vmId: String): Boolean {
		log.info("reset ... vmId: {}", vmId)
		val res: Result<Boolean> = conn.resetVm(vmId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun migrateHostList(vmId: String): List<IdentifiedVo> {
		log.info("migrateHostList ... vmId: {}", vmId)
		val vm: Vm =
			conn.findVm(vmId).getOrNull()
				?: throw ErrorPattern.VM_NOT_FOUND.toException()

		val res: List<Host> =
			conn.findAllHosts().getOrDefault(listOf())
				.filter { it.cluster().id() == vm.cluster().id() && it.id() != vm.host().id() }
		return res.fromHostsToIdentifiedVos()
	}

	@Throws(Error::class)
	override fun migrate(vmId: String, hostId: String): Boolean {
		log.info("migrate ... ")
		val res: Result<Boolean> = conn.migrationVm(vmId, hostId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun exportOva(vmId: String, vmExportVo: VmExportVo): Boolean {
		log.info("exportOva ... ")
		val res: Result<Boolean> =
			conn.exportVm(
				vmId,
				vmExportVo.hostVo.name,
				vmExportVo.directory,
				vmExportVo.fileName
			)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun console(vmId: String): ConsoleVo? {
		log.info("console ... vmId: {}", vmId)
		val res: Vm =
			conn.findVm(vmId).getOrNull()
				?: throw ErrorPattern.VM_NOT_FOUND.toException()
		return res.toConsoleVo(conn, systemPropertiesVo)
	}


	companion object {
		private val log by LoggerDelegate()
	}
}