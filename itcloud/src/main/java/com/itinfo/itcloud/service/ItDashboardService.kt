package com.itinfo.itcloud.service

import com.itinfo.common.LoggerDelegate
import com.itinfo.itcloud.*
import com.itinfo.itcloud.ovirt.AdminConnectionService
import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.types.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface ItDashboardService {
	/**
	 * [ItDashboardService.getDatacenters]
	 * 대시보드 - 데이터센터 개수
	 *
	 * @param type [String] 데이터센터 상태 (up, down, all)
	 * @return [Int] 데이터센터 개수
	 */
	fun getDatacenters(type: String = ""): Int // type: up, down, ""(all)

	/**
	 * [ItDashboardService.getClusters]
	 * 대시보드 - 클러스터 개수
	 *
	 * @return [Int] 클러스터 개수
	 */
	fun getClusters(): Int

	/**
	 * [ItDashboardService.getHosts]
	 * 대시보드 - 호스트 개수
	 * 
	 * @param type [String] 호스트 상태 (up, down, all)
	 * @return [Int] 호스트 개수
	 */
	fun getHosts(type: String = ""): Int

	/**
	 * [ItDashboardService.getVms]
	 * 대시보드 - 가상머신 개수
	 *
	 * @param type [String] 데이터센터 상태 (up, down, all)
	 * @return [Int] 가상머신 개수
	 */
	fun getVms(type: String = ""): Int

	/**
	 * [ItDashboardService.getStorages]
	 * 대시보드 - 스토리지 도메인 개수
	 *
	 * @return [Int] 스토리지 도메인 개수
	 */
	fun getStorages(): Int
	fun getEvents(type: String = ""): Int
	fun getCpu(): Int
	fun getMemory(type: String = ""): Int
	fun getStorage(type: String = ""): Int
}

@Service
class DashboardServiceImpl() : ItDashboardService {
	@Autowired private lateinit var admin: AdminConnectionService
    // @Autowired private lateinit var repository: HostSamplesRepository

	/**
	 * [ItDashboardService.getDatacenters]
	 * 대시보드 - 데이터센터 개수
	 *
	 * @param type [String] 데이터센터 상태 (up, down, all)
	 * @return [Int] 데이터센터 개수
	 */
	override fun getDatacenters(type: String): Int {
		log.info("getDatacenters ... type: $type")
		val conn: Connection = admin.getConnection()
		val dataCenters: List<DataCenter> = conn.findAllDataCenters()
		log.info("getDatacenters ... ${dataCenters.size} found ... ")

		return dataCenters.count { dataCenter: DataCenter ->
			when (type) {
				"up" -> dataCenter.status() == DataCenterStatus.UP
				"down" -> dataCenter.status() != DataCenterStatus.UP
				else -> true
			}
		}
	}

	/**
	 * [ItDashboardService.getClusters]
	 * 대시보드 - 클러스터 개수
	 *
	 * @return [Int] 클러스터 개수
	 */
	override fun getClusters(): Int {
		log.info("getClusters ...")
		val conn: Connection = admin.getConnection()
		val clusters: List<Cluster> = conn.findAllClusters()
		log.info("getClusters ... ${clusters.size} found ... ")
		return clusters.size
	}

	/**
	 * [ItDashboardService.getHosts]
	 * 대시보드 - 호스트 개수
	 *
	 * @param [String] type 호스트 상태 (up, down, all)
	 * @return [Int]
	 */
	override fun getHosts(type: String): Int {
		log.info("getHosts ... type: $type")
		val conn: Connection = admin.getConnection()
		val hosts: List<Host> = conn.findAllHosts()
		log.info("getHosts ... ${hosts.size} found ... ")

		return hosts.count { host: Host ->
			when (type) {
				"up" -> host.status() == HostStatus.UP
				"down" -> host.status() != HostStatus.UP
				else -> true
			}
		}
	}

	/**
	 * [ItDashboardService.getVms]
	 * 대시보드 - 가상머신 개수
	 *
	 * @param type [String] 데이터센터 상태 (up, down, all)
	 * @return [Int] 가상머신 개수
	 */
	override fun getVms(type: String): Int {
		log.info("getVms ... type: $type")
		val conn: Connection = admin.getConnection()
		val vms: List<Vm> = conn.findAllVms()
		log.info("getVms ... ${vms.size} found ... ")
		return vms.count { vm: Vm ->
			when (type) {
				"up" -> vm.status() == VmStatus.UP
				"down" -> vm.status() != VmStatus.UP
				else -> true
			}
		}
	}

	/**
	 * [ItDashboardService.getStorages]
	 * 대시보드 - 스토리지 도메인 개수
	 * 
	 * @return [Int] 스토리지 도메인 개수
	 */
	override fun getStorages(): Int {
		log.info("getStorages ... ")
		val conn: Connection = admin.getConnection()
		val storageDomains: List<StorageDomain> = conn.findAllStorageDomains()
		log.info("getStorages ... ${storageDomains.size} found ... ")
		return storageDomains.count { storageDomain: StorageDomain ->
			!storageDomain.statusPresent()
		}
	}

	/**
	 * [ItDashboardService.getEvents]
	 * 대시보드 - 이벤트 개수
	 *
	 * @return [Int] 이벤트 개수
	 * 
	 * TODO : 기준이 애매하다
	 */
	override fun getEvents(type: String): Int {
		return 0
	}

	override fun getCpu(): Int {
		val system = admin.getConnection().systemService()
		return system.hostsService().list().send().hosts().stream()
			.mapToInt { host: Host ->
				(host.cpu().topology().cores().toInt()
						* host.cpu().topology().sockets().toInt()
						* host.cpu().topology().threads().toInt())
			}
			.sum()
	}
/*
	// 전체사용량: cpu
	public void getCpu(SystemService system) {
		int cpuTotal = 0;
		int cpuCommit = 0;
		int cpuAssigned = 0;

		List<Host> hostList = system.hostsService().list().send().hosts();
		List<Vm> vmList = system.vmsService().list().send().vms();

		// 호스트에 있는 cpu
		for (Host host : hostList) {
			cpuTotal += host.cpu().topology().cores().intValue()
					* host.cpu().topology().sockets().intValue()
					* host.cpu().topology().threads().intValue();
		}

		// 가상머신에 할당된 cpu
		for (Vm vm : vmList) {
			cpuAssigned += vm.cpu().topology().cores().intValue()
					* vm.cpu().topology().sockets().intValue()
					* vm.cpu().topology().threads().intValue();
		}

		dbVo.setCpuTotal(cpuTotal);
		dbVo.setCpuAssigned(cpuAssigned);
	}
 */

	override fun getMemory(type: String): Int {
		return 0
	}
/*
	// memory
	public void getMemory(SystemService system) {
		List<Host> hostList = system.hostsService().list().send().hosts();

		// host id
		for (Host host : hostList) {
			List<Statistic> statisticList = system.hostsService().hostService(host.id()).statisticsService().list().send().statistics();

			// memory
			for (Statistic statistic : statisticList) {
				if (statistic.name().equals("memory.total")) {
					dbVo.setMemoryTotal(dbVo.getMemoryTotal() == null ?
							statistic.values().get(0).datum() : dbVo.getMemoryTotal().add(statistic.values().get(0).datum()));
				}
				if (statistic.name().equals("memory.used")) {
					dbVo.setMemoryUsed(dbVo.getMemoryUsed() == null ?
							statistic.values().get(0).datum() : dbVo.getMemoryUsed().add(statistic.values().get(0).datum()));
				}
				if (statistic.name().equals("memory.free")) {
					dbVo.setMemoryFree(dbVo.getMemoryFree() == null ?
							statistic.values().get(0).datum() : dbVo.getMemoryFree().add(statistic.values().get(0).datum()));
				}
			}
		}
	}
*/

	override fun getStorage(type: String): Int {
		return 0
	}
/*
	// storage
	public void getStorage(SystemService system) {
		List<StorageDomain> storageDomainList = system.storageDomainsService().list().send().storageDomains();

		// storage datacenter 붙어있는지
		int storageActive = (int) storageDomainList.stream()
				.filter(StorageDomain::dataCentersPresent)
				.count();

		dbVo.setStorageDomainCnt(storageDomainList.size());
		dbVo.setStorageDomainActive(storageActive);
		dbVo.setStorageDomainInactive(storageDomainList.size() - storageActive);

		// 스토리지 값
		for (StorageDomain storageDomain : storageDomainList) {
			if (storageDomain.dataCentersPresent()) {
				dbVo.setStorageTotal( dbVo.getStorageTotal() == null ?
						new BigDecimal(storageDomain.available().add(storageDomain.used())) : dbVo.getStorageTotal().add(new BigDecimal(storageDomain.available().add(storageDomain.used()))) );

				dbVo.setStorageUsed(dbVo.getStorageUsed() == null ?
						new BigDecimal(storageDomain.used()) : dbVo.getStorageUsed().add(new BigDecimal(storageDomain.used())));

				dbVo.setStorageFree(dbVo.getStorageTotal().subtract(dbVo.getStorageUsed()));
			}
		}
	}
*/
	companion object {
		private val log by LoggerDelegate()
	}
}


