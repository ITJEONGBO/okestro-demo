package com.itinfo.itcloud.service.computing

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.model.IdentifiedVo
import com.itinfo.itcloud.model.computing.VmExportVo
import com.itinfo.itcloud.model.fromHostsToIdentifiedVos
import com.itinfo.itcloud.model.response.Res
import com.itinfo.itcloud.service.BaseService
import com.itinfo.util.ovirt.*
import org.ovirt.engine.sdk4.Error
import org.ovirt.engine.sdk4.types.Host
import org.ovirt.engine.sdk4.types.Vm

import org.springframework.stereotype.Service

interface ItVmOperationService {
	/**
	 * [ItVmOperationService.start]
	 * 가상머신 실행
	 *
	 * @param vmId [String] 가상머신 id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun start(vmId: String): Boolean
	/**
	 * [ItVmOperationService.pause]
	 * 가상머신 일시정지
	 *
	 * @param vmId [String] 가상머신 id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun pause(vmId: String): Boolean
	/**
	 * [ItVmOperationService.powerOff]
	 * 가상머신 전원끔
	 *
	 * @param vmId [String] 가상머신 id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun powerOff(vmId: String): Boolean
	/**
	 * [ItVmOperationService.shutdown]
	 * 가상머신 종료
	 *
	 * @param vmId [String] 가상머신 id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun shutdown(vmId: String): Boolean
	/**
	 * [ItVmOperationService.reboot]
	 * 가상머신 재부팅
	 *
	 * @param vmId [String] 가상머신 id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun reboot(vmId: String): Boolean
	/**
	 * [ItVmOperationService.reset]
	 * 가상머신 재설정
	 * 
	 * @param vmId [String] 가상머신 id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun reset(vmId: String): Boolean

	/**
	 * [ItVmOperationService.migrateHostList]
	 * 마이그레이션 할 수 있는 호스트 목록
	 *
	 * @param vmId [String] 가상머신 id
	 * @return List<[IdentifiedVo]>
	 */
	@Throws(Error::class)
	fun migrateHostList(vmId: String): List<IdentifiedVo>
	/**
	 * [ItVmOperationService.migrate]
	 * 가상머신 마이그레이션
	 *
	 * @param vmId [String] 가상머신 id
	 * @param hostId [String] 마이그레이션할 호스트 id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun migrate(vmId: String, hostId: String): Boolean
	/**
	 * [ItVmOperationService.cancelMigration]
	 * 가상머신 마이그레이션 취소
	 *
	 * @param vmId [String] 가상머신 id
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun cancelMigration(vmId: String): Boolean
	/**
	 * TODO:HELP
	 * [ItVmOperationService.exportOvaVm]
	 * ova 창 = setHostList(String clusterId)
	 *
	 * @param vmExportVo [VmExportVo]
	 * @return [Boolean]
	 */
	@Throws(Error::class)
	fun exportOvaVm(vmExportVo: VmExportVo): Boolean // ova로 내보내기
}

@Service
class VmOperationServiceImpl: BaseService(), ItVmOperationService {

	@Throws(Error::class)
	override fun start(vmId: String): Boolean {
		log.info("start ... vmId: {}", vmId)
		val res: Result<Boolean> =
			conn.startVm(vmId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun pause(vmId: String): Boolean {
		log.info("pauseVm ... vmId: {}", vmId)
		val res: Result<Boolean> =
			conn.suspendVm(vmId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun powerOff(vmId: String): Boolean {
		log.info("powerOffVm ... vmId: {}", vmId)
		val res: Result<Boolean> =
			conn.stopVm(vmId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun shutdown(vmId: String): Boolean {
		log.info("shutDownVm ... vmId: {}", vmId)
		val res: Result<Boolean> =
			conn.shutdownVm(vmId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun reboot(vmId: String): Boolean {
		log.info("rebootVm ... vmId: {}", vmId)
		val res: Result<Boolean> =
			conn.rebootVm(vmId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun reset(vmId: String): Boolean {
		log.info("resetVm ... vmId: {}", vmId)
		val res: Result<Boolean> =
			conn.resetVm(vmId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun migrateHostList(vmId: String): List<IdentifiedVo> {
		log.info("migrateHostList ... vmId: {}", vmId)
		val vm: Vm = conn.findVm(vmId).getOrNull() ?: run {
			log.warn("migrateHostList ... 가상머신 없음")
			return listOf()
		}

		val hosts: List<Host> =
			conn.findAllHosts()
				.getOrDefault(listOf())
				.filter { it.cluster().id() == vm.cluster().id() && it.id() != vm.host().id() }
//		if (vm.placementPolicy().hostsPresent()){
//			log.info("가상머신 특정 호스트 마이그레이션 목록");
//			return vm.placementPolicy().hosts().stream() // 특정호스트
//				.filter(host -> !host.id().equals(vm.host().id()))
//				.map {
//						Host host1 = system.hostsService().hostService(it.id()).get().send().host();
//						return IdentifiedVo.builder().id(it.id()).name(host1.name()).build();
//				}
//		}
		// 이건 클러스터 내 호스트 이야기
		return hosts.fromHostsToIdentifiedVos()
	}

	@Throws(Error::class)
	override fun migrate(vmId: String, hostId: String): Boolean {
		log.info("migrateVm ... ")
		val res: Result<Boolean> =
			conn.migrationVm(vmId, hostId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun cancelMigration(vmId: String): Boolean {
		log.info("migrateCancelVm ... ")
		val res: Result<Boolean> =
			conn.cancelMigrationVm(vmId)
		return res.isSuccess
	}

	@Throws(Error::class)
	override fun exportOvaVm(vmExportVo: VmExportVo): Boolean {
		log.info("exportOvaVm ... ")
		val res: Result<Boolean> =
			conn.exportVm(vmExportVo.vmVo.id, vmExportVo.hostVo.name, vmExportVo.directory, vmExportVo.fileName)
		return res.isSuccess
	}


	companion object {
		private val log by LoggerDelegate()
	}
}