package com.itinfo.service;

import com.itinfo.model.*;
import java.util.List;

public interface DomainsService {
	List<StorageDomainVo> retrieveStorageDomains(String status, String domainType);
	void maintenanceStart(List<String> domains);
	void maintenanceStop(List<String> domains);
	StorageDomainVo retrieveStorageDomain(String id);
	StorageDomainCreateVo retrieveCreateDomainInfo(String storageDomainId);
	List<List<String>> retrieveStorageDomainUsage(String storageDomainId);
	List<EventVo> retrieveDomainEvents(String id);
	List<HostDetailVo> retrieveHosts();
	void createDomain(StorageDomainCreateVo storageDomainCreateVo);
	void updateDomain(StorageDomainCreateVo storageDomainCreateVo);
	void removeDomain(StorageDomainVo storageDomainVo);
	List<IscsiVo> iscsiDiscover(StorageDomainCreateVo storageDomainCreateVo);
	boolean iscsiLogin(StorageDomainCreateVo storageDomainCreateVo);
}

