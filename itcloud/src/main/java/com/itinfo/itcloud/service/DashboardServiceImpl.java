package com.itinfo.itcloud.service;

import com.itinfo.itcloud.dao.enitiy.HostSamplesHistory;
import com.itinfo.itcloud.dao.repository.HostSamplesRepository;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.builders.Builders;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DashboardServiceImpl implements ItDashboardService {
	@Autowired private AdminConnectionService admin;
//	@Autowired private HostSamplesRepository repository;

	/**
	 * 대시보드 데이터센터 숫자표시
	 * @param type 데이터센터 상태 (up, down, all)
	 * @return 데이터센터 개수
	 */
	@Override
	public int getDatacenters(String type) {
		SystemService system = admin.getConnection().systemService();
		return (int) system.dataCentersService().list().send().dataCenters().stream()
				.filter(dataCenter ->
						"up".equals(type) ? dataCenter.status() == DataCenterStatus.UP
						: "down".equals(type) ? dataCenter.status() != DataCenterStatus.UP
						: true)
				.count();
	}

	/**
	 * 대시보드 - 클러스터 개수
	 * @return 클러스터 개수
	 */
	@Override
	public int getClusters() {
		return admin.getConnection().systemService().clustersService().list().send().clusters().size();
	}

	/**
	 * 대시보드 호스트 숫자표시
	 * @param type 호스트 상태 (up, down, all)
	 * @return
	 */
	@Override
	public int gethosts(String type) {
		SystemService system = admin.getConnection().systemService();
		return (int) system.hostsService().list().send().hosts().stream()
				.filter(host ->
						type.equals("up") ? host.status() == HostStatus.UP
						: type.equals("down") ? host.status() != HostStatus.UP
						:true)
				.count();
	}

	/**
	 * 대시보드 가상머신 숫자표시
	 * @param type 가상머신 상태 (up, down, all)
	 * @return
	 */
	@Override
	public int getvms(String type) {
		SystemService system = admin.getConnection().systemService();
		return (int) system.vmsService().list().send().vms().stream()
				.filter(vm ->
						type.equals("up") ? vm.status() == VmStatus.UP
						: type.equals("down") ? vm.status() != VmStatus.UP
						:true)
				.count();
	}

	/**
	 * 대시보드 스토리지 도메인 숫자표시
	 * @return
	 */
	@Override
	public int getStorages() {
		SystemService system = admin.getConnection().systemService();
		return (int) system.storageDomainsService().list().send().storageDomains().stream()
				.filter(storageDomain -> !storageDomain.statusPresent())
				.count();
	}

	// TODO : 기준이 애매하다
	@Override
	public int getEvents(String type) {
		return 0;
	}


	@Override
	public int getCpu() {
		SystemService system = admin.getConnection().systemService();
		return system.hostsService().list().send().hosts().stream()
						.mapToInt(host -> host.cpu().topology().cores().intValue()
								* host.cpu().topology().sockets().intValue()
								* host.cpu().topology().threads().intValue())
						.sum();
	}

	@Override
	public int getMemory(String type) {
		return 0;
	}

	@Override
	public int getStorage(String type) {
		return 0;
	}


//
//	// 전체사용량: cpu
//	public void getCpu(SystemService system) {
//		int cpuTotal = 0;
////		int cpuCommit = 0;
//		int cpuAssigned = 0;
//
//		List<Host> hostList = system.hostsService().list().send().hosts();
//		List<Vm> vmList = system.vmsService().list().send().vms();
//
//		// 호스트에 있는 cpu
//		for (Host host : hostList) {
//			cpuTotal += host.cpu().topology().cores().intValue()
//					* host.cpu().topology().sockets().intValue()
//					* host.cpu().topology().threads().intValue();
//		}
//
//		// 가상머신에 할당된 cpu
//		for (Vm vm : vmList) {
//			cpuAssigned += vm.cpu().topology().cores().intValue()
//					* vm.cpu().topology().sockets().intValue()
//					* vm.cpu().topology().threads().intValue();
//		}
//
//		dbVo.setCpuTotal(cpuTotal);
//		dbVo.setCpuAssigned(cpuAssigned);
//	}
//
//
//	// memory
//	public void getMemory(SystemService system) {
//		List<Host> hostList = system.hostsService().list().send().hosts();
//
//		// host id
//		for (Host host : hostList) {
//			List<Statistic> statisticList = system.hostsService().hostService(host.id()).statisticsService().list().send().statistics();
//
//			// memory
//			for (Statistic statistic : statisticList) {
//				if (statistic.name().equals("memory.total")) {
//					dbVo.setMemoryTotal(dbVo.getMemoryTotal() == null ?
//							statistic.values().get(0).datum() : dbVo.getMemoryTotal().add(statistic.values().get(0).datum()));
//				}
//				if (statistic.name().equals("memory.used")) {
//					dbVo.setMemoryUsed(dbVo.getMemoryUsed() == null ?
//							statistic.values().get(0).datum() : dbVo.getMemoryUsed().add(statistic.values().get(0).datum()));
//				}
//				if (statistic.name().equals("memory.free")) {
//					dbVo.setMemoryFree(dbVo.getMemoryFree() == null ?
//							statistic.values().get(0).datum() : dbVo.getMemoryFree().add(statistic.values().get(0).datum()));
//				}
//			}
//		}
//
//	}
//
//
//	// storage
//	public void getStorage(SystemService system) {
//		List<StorageDomain> storageDomainList = system.storageDomainsService().list().send().storageDomains();
//
//		// storage datacenter 붙어있는지
//		int storageActive = (int) storageDomainList.stream()
//				.filter(StorageDomain::dataCentersPresent)
//				.count();
//
//		dbVo.setStorageDomainCnt(storageDomainList.size());
//		dbVo.setStorageDomainActive(storageActive);
//		dbVo.setStorageDomainInactive(storageDomainList.size() - storageActive);
//
//		// 스토리지 값
//		for (StorageDomain storageDomain : storageDomainList) {
//			if (storageDomain.dataCentersPresent()) {
//				dbVo.setStorageTotal( dbVo.getStorageTotal() == null ?
//						new BigDecimal(storageDomain.available().add(storageDomain.used())) : dbVo.getStorageTotal().add(new BigDecimal(storageDomain.available().add(storageDomain.used()))) );
//
//				dbVo.setStorageUsed(dbVo.getStorageUsed() == null ?
//						new BigDecimal(storageDomain.used()) : dbVo.getStorageUsed().add(new BigDecimal(storageDomain.used())));
//
//				dbVo.setStorageFree(dbVo.getStorageTotal().subtract(dbVo.getStorageUsed()));
//			}
//		}
//	}


}

