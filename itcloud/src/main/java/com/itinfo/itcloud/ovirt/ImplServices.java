package com.itinfo.itcloud.ovirt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImplServices {

	private final ConnectionService ovirtConnection;

	public SystemService getSystemService() { return ovirtConnection.getConnection().systemService(); }


	public List getSdkServices(String svcName) {
		int cntList = 0;
		List list = new ArrayList();

		if (svcName.equals("datacenters")) {
			List<DataCenter> dataCenterList =
					((DataCentersService.ListResponse) getSystemService().dataCentersService().list().send()).dataCenters();
			list = dataCenterList;
		}
		if (svcName.equals("clusters")) {
			List<Cluster> clusterList =
					((ClustersService.ListResponse) getSystemService().clustersService().list().send()).clusters();
			log.info("------" + String.valueOf(clusterList.size()));
			list = clusterList;
		}
		if (svcName.equals("hosts")) {
			List<Host> hostList =
					((HostsService.ListResponse) getSystemService().hostsService().list().send()).hosts();
			list = hostList;
		}
		if (svcName.equals("vms")) {
			List<Vm> vmList =
					((VmsService.ListResponse) getSystemService().vmsService().list().send()).vms();
			list = vmList;
		}

		log.info("--------" + svcName + " " + list.size());
		return list;
	}

/*
	private void getStatisticsService(Connection connection, String svcName, String id) {
		if (svcName.equals("hosts")) {
			List<Host> hostList =
					((HostsService.ListResponse) getSystemService(connection).hostsService().list().send()).hosts();
			log.info("---------hosts");
			log.info(String.valueOf(hostList.size()));
			return ((StatisticsService.ListResponse) getSystemService(connection).hostsService().hostService(id)).statistics();
		}

		return null;
	}
*/
}
