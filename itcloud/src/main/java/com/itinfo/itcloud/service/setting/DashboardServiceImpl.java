package com.itinfo.itcloud.service.setting;

import com.itinfo.itcloud.ovirt.AdminConnectionService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.types.DataCenter;
import org.ovirt.engine.sdk4.types.DataCenterStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DashboardServiceImpl implements ItDashboardService {
	@Autowired private AdminConnectionService admin;

	@Override
	public int getDatacenters(String type) {
		SystemService system = admin.getConnection().systemService();
		List<DataCenter> dataCenterList = system.dataCentersService().list().send().dataCenters();

		if ("up".equals(type)) {
			return (int) dataCenterList.stream().filter(dataCenter -> dataCenter.status() == DataCenterStatus.UP).count();
		} else if ("down".equals(type)) {
			return (int) dataCenterList.stream().filter(dataCenter -> dataCenter.status() != DataCenterStatus.UP).count();
		} else{
			return dataCenterList.size();
		}
	}

	@Override
	public int getClusters() {
		return 0;
	}

	@Override
	public int gethosts(String type) {
		return 0;
	}

	@Override
	public int getvms(String type) {
		return 0;
	}

	@Override
	public int getStorages(String type) {
		return 0;
	}

	@Override
	public int getEvents(String type) {
		return 0;
	}

	@Override
	public int getCpu(String type) {
		return 0;
	}

	@Override
	public int getMemory(String type) {
		return 0;
	}

	@Override
	public int getStorage(String type) {
		return 0;
	}


	//	private DashboardVo dbVo;
	// Dashboard 전체 불러오기
//	@Override
//	public DashboardVo getDashboard() {
//		SystemService system = admin.getConnection().systemService();
//
//		dbVo = new DashboardVo();
//
//		getDatacenter(system);
//		getCluster(system);
//		getHost(system);
//		getVm(system);
//
//		getCpu(system);
//		getMemory(system);
//		getStorage(system);
//
//		log.info("------getDashboard");
//		return dbVo;
//	}
//
//	// 데이터센터 수
//	private void getDatacenter(SystemService system) {
//		List<DataCenter> dataCenterList = system.dataCentersService().list().send().dataCenters();
//
//		// DataCenter status=up 개수
//		int datacenterCnt = (int) dataCenterList.stream()
//				.filter(dataCenter -> dataCenter.status().value().equals("up"))
//				.count();
//
//		dbVo.setDatacenterCnt(dataCenterList.size());
//		dbVo.setDatacenterActive(datacenterCnt);
//		dbVo.setDatacenterInactive(dataCenterList.size() - datacenterCnt);
//	}
//
//	// 클러스터 수
//	private void getCluster(SystemService system) {
//		dbVo.setClusterCnt(system.clustersService().list().send().clusters().size() );
//	}
//
//	// 호스트 수
//	private void getHost(SystemService system) {
//		List<Host> hostList = system.hostsService().list().send().hosts();
//
//		// Host status=up 개수
//		int hostUpCnt = (int) hostList.stream()
//				.filter(host -> host.status().value().equals("up"))
//				.count();
//
//		dbVo.setHostCnt(hostList.size());
//		dbVo.setHostActive(hostUpCnt);
//		dbVo.setHostInactive(dbVo.getHostCnt() - hostUpCnt);
//	}
//
//	// 가상머신 수
//	private void getVm(SystemService system) {
//		List<Vm> vmList = system.vmsService().list().send().vms();
//
//		// Host status=up 개수
//		int vmUpCnt = (int) vmList.stream()
//				.filter(vm -> vm.status().value().equals("up"))
//				.count();
//
//		dbVo.setVmCnt(vmList.size());
//		dbVo.setVmActive(vmUpCnt);
//		dbVo.setVmInactive(vmList.size() - vmUpCnt);
//	}
//
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

