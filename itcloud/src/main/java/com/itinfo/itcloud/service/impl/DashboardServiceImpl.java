package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.DashBoardVO;
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

	private DashBoardVO dashBoardVO;


	// Dashbord 값
	@Override
	public DashBoardVO showDashboard() {
		Connection connection = this.ovirtConnection.getConnection();
		SystemService systemService = connection.systemService();

		dashBoardVO = new DashBoardVO();
		getDatacenterCnt(systemService);
		getClusterCnt(systemService);
		getHostCnt(systemService);
		getVmCnt(systemService);
		getTotalCpu(systemService);
		getTotalMemory(systemService);
		getTotalStorage(systemService);

		log.info("------showDashboard");
		return dashBoardVO;
	}


	// 데이터센터 수
	private void getDatacenterCnt(SystemService systemService) {
		List<DataCenter> dataCenterList =
				((DataCentersService.ListResponse) systemService.dataCentersService().list().send()).dataCenters();
		dashBoardVO.setDatacenterCnt(dataCenterList.size());

		// DataCenter status=up 개수
		int datacenterCnt = (int) dataCenterList.stream()
				.filter(dataCenter -> dataCenter.status().value().equals("up"))
				.count();

		dashBoardVO.setDatacenterActive(datacenterCnt);
		dashBoardVO.setDatacenterInactive(dataCenterList.size() - datacenterCnt);
	}

	// 클러스터 수
	private void getClusterCnt(SystemService systemService) {
		List<Cluster> clusterList =
				((ClustersService.ListResponse) systemService.clustersService().list().send()).clusters();
		dashBoardVO.setClusterCnt(clusterList.size());
	}


	// 호스트 수
	private void getHostCnt(SystemService systemService) {
		List<Host> hostList =
				((HostsService.ListResponse) systemService.hostsService().list().send()).hosts();
		dashBoardVO.setHostCnt(hostList.size());

		// Host status=up 개수
		int hostUpCnt = (int) hostList.stream()
				.filter(host -> host.status().value().equals("up"))
				.count();

		dashBoardVO.setHostActive(hostUpCnt);
		dashBoardVO.setHostInactive(dashBoardVO.getHostCnt() - hostUpCnt);
	}


	// 가상머신 수
	private void getVmCnt(SystemService systemService) {
		List<Vm> vmList = ((VmsService.ListResponse) systemService.vmsService().list().send()).vms();
		dashBoardVO.setVmCnt(vmList.size());

		// Host status=up 개수
		int vmUpCnt = (int) vmList.stream()
				.filter(vm -> vm.status().value().equals("up"))
				.count();

		dashBoardVO.setVmActive(vmUpCnt);
		dashBoardVO.setVmInactive(vmList.size() - vmUpCnt);
	}


	// 전체사용량
	// cpu
	public void getTotalCpu(SystemService systemService) {
		int cpuTotal = 0;
		int cpuCommit = 0;
		int cpuAssigned = 0;

		List<Host> hostList =
				((HostsService.ListResponse) systemService.hostsService().list().send()).hosts();

		List<Vm> vmList =
				((VmsService.ListResponse) systemService.vmsService().list().send()).vms();

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

		dashBoardVO.setCpuTotal(cpuTotal);
		dashBoardVO.setCpuAssigned(cpuAssigned);
	}


	// memory
	public void getTotalMemory(SystemService systemService) {
		List<Host> hostList =
				((HostsService.ListResponse) systemService.hostsService().list().send()).hosts();

		// host id
		for (Host host : hostList) {
			List<Statistic> statisticList =
					((StatisticsService.ListResponse) systemService.hostsService().hostService(host.id()).statisticsService().list().send()).statistics();

			// memory
			for (Statistic statistic : statisticList) {
				if (statistic.name().equals("memory.total")) {
					dashBoardVO.setMemoryTotal(dashBoardVO.getMemoryTotal() == null ?
							statistic.values().get(0).datum() : dashBoardVO.getMemoryTotal().add(statistic.values().get(0).datum()));
				}
				if (statistic.name().equals("memory.used")) {
					dashBoardVO.setMemoryUsed(dashBoardVO.getMemoryUsed() == null ?
							statistic.values().get(0).datum() : dashBoardVO.getMemoryUsed().add(statistic.values().get(0).datum()));
				}
				if (statistic.name().equals("memory.free")) {
					dashBoardVO.setMemoryFree(dashBoardVO.getMemoryFree() == null ?
							statistic.values().get(0).datum() : dashBoardVO.getMemoryFree().add(statistic.values().get(0).datum()));
				}
			}
		}
	}


	// storage
	public void getTotalStorage(SystemService systemService) {
		List<StorageDomain> storageDomainList =
				((StorageDomainsService.ListResponse) systemService.storageDomainsService().list().send()).storageDomains();

		// storage datacenter 붙어있는지
		int storageActive = (int) storageDomainList.stream()
				.filter(storage -> !storage.dataCenters().isEmpty())
				.count();

		dashBoardVO.setStorageDomainCnt(storageDomainList.size());
		dashBoardVO.setStorageDomainActive(storageActive);
		dashBoardVO.setStorageDomainInactive(storageDomainList.size() - storageActive);

		// 스토리지 값
		for (StorageDomain storageDomain : storageDomainList) {
			if (!storageDomain.dataCenters().isEmpty()) {
				dashBoardVO.setStorageTotal(dashBoardVO.getStorageTotal() == null ?
						new BigDecimal(storageDomain.available()) : dashBoardVO.getStorageTotal().add(new BigDecimal(storageDomain.available())));

				dashBoardVO.setStorageUsed(dashBoardVO.getStorageUsed() == null ?
						new BigDecimal(storageDomain.used()) : dashBoardVO.getStorageUsed().add(new BigDecimal(storageDomain.used())));

				dashBoardVO.setStorageFree(dashBoardVO.getStorageTotal().subtract(dashBoardVO.getStorageUsed()));
			}
		}
	}

}

