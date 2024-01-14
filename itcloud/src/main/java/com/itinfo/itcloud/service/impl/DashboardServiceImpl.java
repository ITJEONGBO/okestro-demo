package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.DashboardVo;
import com.itinfo.itcloud.ovirt.ConnectionService;
import com.itinfo.itcloud.service.ItDashboardService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class DashboardServiceImpl implements ItDashboardService {
	@Autowired private ConnectionService ovirtConnection;

	private DashboardVo dbVo;


	// Dashboard 전체 불러오기
	@Override
	public DashboardVo getDashboard() {
		Connection connection = ovirtConnection.getConnection();
		SystemService systemService = connection.systemService();

		dbVo = new DashboardVo();

		getDatacenter(systemService);
		getCluster(systemService);
		getHost(systemService);
		getVm(systemService);

		getCpu(systemService);
		getMemory(systemService);
		getStorage(systemService);

		log.info("------getDashboard");
		return dbVo;
	}


	// 데이터센터 수
	private void getDatacenter(SystemService systemService) {
		List<DataCenter> dataCenterList = ((DataCentersService.ListResponse) systemService.dataCentersService().list().send()).dataCenters();

		// DataCenter status=up 개수
		int datacenterCnt = (int) dataCenterList.stream()
				.filter(dataCenter -> dataCenter.status().value().equals("up"))
				.count();

		dbVo.setDatacenterCnt(dataCenterList.size());
		dbVo.setDatacenterActive(datacenterCnt);
		dbVo.setDatacenterInactive(dataCenterList.size() - datacenterCnt);
	}

	// 클러스터 수
	private void getCluster(SystemService systemService) {
		List<Cluster> clusterList = ((ClustersService.ListResponse) systemService.clustersService().list().send()).clusters();
		dbVo.setClusterCnt(clusterList.size());
	}

	// 호스트 수
	private void getHost(SystemService systemService) {
		List<Host> hostList = ((HostsService.ListResponse) systemService.hostsService().list().send()).hosts();

		// Host status=up 개수
		int hostUpCnt = (int) hostList.stream()
				.filter(host -> host.status().value().equals("up"))
				.count();

		dbVo.setHostCnt(hostList.size());
		dbVo.setHostActive(hostUpCnt);
		dbVo.setHostInactive(dbVo.getHostCnt() - hostUpCnt);
	}

	// 가상머신 수
	private void getVm(SystemService systemService) {
		List<Vm> vmList = ((VmsService.ListResponse) systemService.vmsService().list().send()).vms();

		// Host status=up 개수
		int vmUpCnt = (int) vmList.stream()
				.filter(vm -> vm.status().value().equals("up"))
				.count();

		dbVo.setVmCnt(vmList.size());
		dbVo.setVmActive(vmUpCnt);
		dbVo.setVmInactive(vmList.size() - vmUpCnt);
	}


	// 전체사용량: cpu
	public void getCpu(SystemService systemService) {
		int cpuTotal = 0;
//		int cpuCommit = 0;
		int cpuAssigned = 0;

		List<Host> hostList = ((HostsService.ListResponse) systemService.hostsService().list().send()).hosts();
		List<Vm> vmList = ((VmsService.ListResponse) systemService.vmsService().list().send()).vms();

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


	// memory
	public void getMemory(SystemService systemService) {
		List<Host> hostList = ((HostsService.ListResponse) systemService.hostsService().list().send()).hosts();

		// host id
		for (Host host : hostList) {
			List<Statistic> statisticList =
					((StatisticsService.ListResponse) systemService.hostsService().hostService(host.id()).statisticsService().list().send()).statistics();

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


	// storage
	public void getStorage(SystemService systemService) {
		List<StorageDomain> storageDomainList = ((StorageDomainsService.ListResponse) systemService.storageDomainsService().list().send()).storageDomains();

		// storage datacenter 붙어있는지
		int storageActive = (int) storageDomainList.stream()
				.filter(storage -> !storage.dataCenters().isEmpty())
				.count();

		dbVo.setStorageDomainCnt(storageDomainList.size());
		dbVo.setStorageDomainActive(storageActive);
		dbVo.setStorageDomainInactive(storageDomainList.size() - storageActive);

		// 스토리지 값
		for (StorageDomain storageDomain : storageDomainList) {
			if (!storageDomain.dataCenters().isEmpty()) {
				dbVo.setStorageTotal(dbVo.getStorageTotal() == null ?
						new BigDecimal(storageDomain.available()) : dbVo.getStorageTotal().add(new BigDecimal(storageDomain.available())));

				dbVo.setStorageUsed(dbVo.getStorageUsed() == null ?
						new BigDecimal(storageDomain.used()) : dbVo.getStorageUsed().add(new BigDecimal(storageDomain.used())));

				dbVo.setStorageFree(dbVo.getStorageTotal().subtract(dbVo.getStorageUsed()));
			}
		}
	}

}

