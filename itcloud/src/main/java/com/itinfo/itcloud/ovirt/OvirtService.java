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
public class OvirtService {

	private final ConnectionService ovirtConnection;

	public SystemService getSystemService() {
		return ovirtConnection.getConnection().systemService();
	}


	// 컴퓨팅 주요 서비스
	public List computeListService(String svcName) {
		List list = new ArrayList();

		if (svcName.equals("datacenterList")) {
//			((DataCentersService.ListResponse)getSystemService().dataCentersService().list().send()).dataCenters()
			list = getSystemService().dataCentersService().list().send().dataCenters();
		}
		if (svcName.equals("clusterList")) {
			list = getSystemService().clustersService().list().send().clusters();
		}
		if (svcName.equals("hostList")) {
			list = getSystemService().hostsService().list().send().hosts();
		}
		if (svcName.equals("vmList")) {
			list = getSystemService().vmsService().list().send().vms();
		}
		if (svcName.equals("templateList")) {
			list = getSystemService().templatesService().list().send().templates();
		}



		if (svcName.equals("eventList")) {
			list = getSystemService().eventsService().list().send().events();
		}



		log.info("--- {}", svcName);
		return list;
	}

	public Object computeService(String svcName, String id) {
		Object object = new Object();

		if (svcName.equals("datacenter")) {
//			((DataCenterService.GetResponse) getSystemService().dataCentersService().dataCenterService(id).get().send()).dataCenter();
			object = getSystemService().dataCentersService().dataCenterService(id).get().send().dataCenter();
		}
		if (svcName.equals("cluster")) {
			object = getSystemService().clustersService().clusterService(id).get().send().cluster();
		}
		if (svcName.equals("host")) {
			object = getSystemService().hostsService().hostService(id).get().send().host();
		}
		if (svcName.equals("vm")) {
			object = getSystemService().vmsService().vmService(id).get().send().vm();
		}
		if (svcName.equals("template")) {
			object = getSystemService().templatesService().templateService(id).get().send().template();
		}

		// permission group
		if (svcName.equals("group")) {
			object = getSystemService().groupsService().groupService(id).get().send().get();
		}
		if (svcName.equals("user")) {
			object = getSystemService().usersService().userService(id).get().send().user();
		}
		if (svcName.equals("role")) {
			object =  getSystemService().rolesService().roleService(id).get().send().role();
		}


		return object;
	}


	// 컴퓨팅 내부 서비스
	public List subListService(String svcName, String id) {
		List list = new ArrayList();

		// datacenter 에서 사용
		if (svcName.equals("domainList")) {
			list = getSystemService().dataCentersService().dataCenterService(id).storageDomainsService().list().send().storageDomains();
		}
		if (svcName.equals("networkList")) {
			list = getSystemService().dataCentersService().dataCenterService(id).networksService().list().send().networks();
		}
		if (svcName.equals("clusterList")) {
			list = getSystemService().dataCentersService().dataCenterService(id).clustersService().list().send().clusters();
		}

		if (svcName.equals("permissionList")) {
			list = ovirtConnection.getConnection().systemService().dataCentersService().dataCenterService(id).permissionsService().list().send().permissions();
		}





		return list;
	}


	public List networkListService(String svcName) {
		List list = new ArrayList();

		if (svcName.equals("vnicList")) {
			list = getSystemService().vnicProfilesService().list().send().profiles();
		}
		if (svcName.equals("networkList")) {
			list = getSystemService().networksService().list().send().networks();
		}

		log.info("--- {}", svcName);
		return list;
	}

	public Object networkService(String svcName, String id) {
		Object object = new Object();

		if (svcName.equals("network")) {
			object = getSystemService().networksService().networkService(id).get().send().network();;
		}

		log.info("--- {}: {}", svcName, id);
		return object;
	}


	public List storageListService(String svcName) {
		List list = new ArrayList();

		if (svcName.equals("domainList")) {
			list = getSystemService().storageDomainsService().list().send().storageDomains();
		}
//		if (svcName.equals("volumeList")) {
//			List<Network> networkList = ((NetworksService.ListResponse)getSystemService().networksService().list().send()).networks();
//			list = volumeList;
//		}
		if (svcName.equals("diskList")) {
			list = getSystemService().disksService().list().send().disks();
		}

		log.info("--- {}", svcName);
		return list;
	}

	public Object storageService(String svcName, String id) {
		Object object = new Object();

		if (svcName.equals("domain")) {
			object = getSystemService().storageDomainsService().storageDomainService(id).get().send().storageDomain();
		}
//		if (svcName.equals("volume")) {
//			StorageDomain domain = ((StorageDomainService.GetResponse)getSystemService().storageDomainsService().storageDomainService(id).get().send()).storageDomain();
//			object = domain;
//		}
		if (svcName.equals("disk")) {
			object = getSystemService().disksService().diskService(id).get().send().disk();
		}

		log.info("--- {}: {}", svcName, id);
		return object;
	}


	public String getName(String svcName, String id) {
		String st = "";

		if (svcName.equals("datacenter")) {
//			((DataCenterService.GetResponse) getSystemService().dataCentersService().dataCenterService(id).get().send()).dataCenter();
			st = getSystemService().dataCentersService().dataCenterService(id).get().send().dataCenter().name();
		}
		if (svcName.equals("cluster")) {
			st = getSystemService().clustersService().clusterService(id).get().send().cluster().name();
		}
		if (svcName.equals("host")) {
			st = getSystemService().hostsService().hostService(id).get().send().host().name();
		}
		if (svcName.equals("vm")) {
			st = getSystemService().vmsService().vmService(id).get().send().vm().name();
		}
		if (svcName.equals("template")) {
			st = getSystemService().templatesService().templateService(id).get().send().template().name();
		}

		return st;
	}

	// permission event는 중복이 너무 많아요



}
