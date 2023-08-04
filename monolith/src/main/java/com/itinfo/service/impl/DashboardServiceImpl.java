package com.itinfo.service.impl;

import com.itinfo.service.DashboardService;
import com.itinfo.dao.DashboardDao;
import com.itinfo.service.engine.ConnectionService;
import com.itinfo.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.types.Cluster;
import org.ovirt.engine.sdk4.types.Event;
import org.ovirt.engine.sdk4.types.Host;
import org.ovirt.engine.sdk4.types.HostNic;
import org.ovirt.engine.sdk4.types.Statistic;
import org.ovirt.engine.sdk4.types.StorageDomain;
import org.ovirt.engine.sdk4.types.Vm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DashboardServiceImpl extends BaseService implements DashboardService {

    @Autowired private ConnectionService connectionService;
    @Autowired private DashboardDao dashboardDao;

    private DataCenterVo dcv;
    private List<UsageVo> usageVos;

    @Override
    public DataCenterVo retrieveDataCenterStatus() {
        Connection connection = this.connectionService.getConnection();
        this.dcv = new DataCenterVo();
        this.usageVos = new ArrayList<>();
        getClusters(connection);
        getHosts(connection);
        getVms(connection);
        return this.dcv;
    }

    private void getClusters(Connection connection) {
        List<Cluster> clusters
                = getSysSrvHelper().findAllClusters(connection, "");
        this.dcv.setClusters(clusters.size());
    }

    private void getHosts(Connection connection) {
        // VmsService vmsService = systemService.vmsService();
        // HostsService hostsService = systemService.hostsService();
        List<Host> hosts =
                getSysSrvHelper().findAllHosts(connection, "status!=up");
        this.dcv.setHostsDown(hosts.size());
        hosts = getSysSrvHelper().findAllHosts(connection, "status=up");
        this.dcv.setHostsUp(hosts.size());
        List<String> ids = new ArrayList<>();
        List<String> interfaceIds = new ArrayList<>();
        int sumTotalCpu = 0;
        for (Host host : hosts) {
            List<Statistic> stats
                    = getSysSrvHelper().findAllStatisticsFromHost(connection, host.id());
            stats.forEach(stat -> {
                if (stat.name().equals("memory.total"))
                    this.dcv.setMemoryTotal((this.dcv.getMemoryTotal() != null) ? this.dcv.getMemoryTotal().add((stat.values().get(0)).datum()) : (stat.values().get(0)).datum());
                if (stat.name().equals("memory.used"))
                    this.dcv.setMemoryUsed((this.dcv.getMemoryUsed() != null) ? this.dcv.getMemoryUsed().add((stat.values().get(0)).datum()) : (stat.values().get(0)).datum());
                if (stat.name().equals("memory.free"))
                    this.dcv.setMemoryFree((this.dcv.getMemoryFree() != null) ? this.dcv.getMemoryFree().add((stat.values().get(0)).datum()) : (stat.values().get(0)).datum());
                if (stat.name().equals("ksm.cpu.current"))
                if (stat.name().equals("cpu.current.user"))
                    this.dcv.setCpuCurrentUser(this.dcv.getCpuCurrentUser() + (stat.values().get(0)).datum().intValue());
                if (stat.name().equals("cpu.current.system"))
                    this.dcv.setCpuCurrentSystem(this.dcv.getCpuCurrentSystem() + (stat.values().get(0)).datum().intValue());
                if (stat.name().equals("cpu.current.idle"))
                    this.dcv.setCpuCurrentIdle(this.dcv.getCpuCurrentIdle() + (stat.values().get(0)).datum().intValue());
            });
            sumTotalCpu +=
                    host.cpu().topology().cores().intValue() *
                    host.cpu().topology().sockets().intValue() *
                    host.cpu().topology().threads().intValue();
            List<Vm> vms
                    = getSysSrvHelper().findAllVms(connection, "host = " + host.name());
            int sumCpu = 0;
            for (Vm vm : vms)
                sumCpu += vm.cpu().topology().cores().intValue() *
                        vm.cpu().topology().sockets().intValue() *
                        vm.cpu().topology().threads().intValue();
            this.dcv.setUsingcpu(sumCpu);
            List<HostNic> nics
                    = getSysSrvHelper().findNicsFromHost(connection, host.id());
            nics.forEach(nic -> interfaceIds.add(nic.id()));
            ids.add(host.id());
        }
        this.dcv.setTotalcpu(sumTotalCpu);
        if (ids.size() > 0) {
            List<HostVo> hostStat = this.dashboardDao.retrieveHosts(ids);
            if (hostStat.size() > 0)
                hostStat.forEach(stat -> {
                    UsageVo usageVo = new UsageVo();
                    usageVo.setCpuUsages(stat.getCpuUsagePercent());
                    usageVo.setMemoryUsages(stat.getMemoryUsagePercent());
                    usageVo.setUsageDate(stat.getHistoryDatetime());
                    this.usageVos.add(usageVo);
                });
        }
        if (interfaceIds.size() > 0) {
            List<HostInterfaceVo> hostInterfaces = this.dashboardDao.retrieveHostsInterface(interfaceIds);
            if (hostInterfaces.size() > 0)
                for (int i = 0; i < hostInterfaces.size(); i++) {
                    (this.usageVos.get(i)).setReceiveUsages((hostInterfaces.get(i)).getReceiveRatePercent());
                    (this.usageVos.get(i)).setTransitUsages((hostInterfaces.get(i)).getTransmitRatePercent());
                }
        }
        getStorageDomains(connection);
    }


    private void getStorageDomains(Connection connection) {
        List<StorageDomain> storageDomains =
                getSysSrvHelper().findAllStorageDomains(connection, "status=unattached");
        storageDomains.forEach(storageDomain -> {
            if (storageDomain.type().name().equals("DATA"))
                this.dcv.setStoragesUnattached(this.dcv.getStoragesUnattached() + 1);
        });
        storageDomains =
                getSysSrvHelper().findAllStorageDomains(connection, "status=active");
        List<String> storageIds = new ArrayList<>();
        storageDomains.forEach(storageDomain -> {
            if (storageDomain.type().name().equals("DATA")) {
                this.dcv.setStorageAvaliable((this.dcv.getStorageAvaliable() != null) ? this.dcv.getStorageAvaliable().add(storageDomain.available()) : storageDomain.available());
                this.dcv.setStorageUsed((this.dcv.getStorageUsed() != null) ? this.dcv.getStorageUsed().add(storageDomain.used()) : storageDomain.used());
                storageIds.add(storageDomain.id());
            }
        });
        this.dcv.setStoragesActive(storageIds.size());
        if (storageIds.size() > 0) {
            List<StorageVo> storages = this.dashboardDao.retireveStorages(storageIds);
            if (storages.size() > 0)
                for (int j = 0; j < storages.size(); j++) {
                    (this.usageVos.get(j)).setStorageUsages(( storages.get(j)).getUsedDiskSizeGb() * 100 / ((storages.get(j)).getAvailableDiskSizeGb() + (storages.get(j)).getUsedDiskSizeGb()));
                    (this.usageVos.get(j)).setStorageUsageDate((storages.get(j)).getHistoryDatetime());
                }
        }
        this.dcv.setUsageVos(this.usageVos);
    }


    private void getVms(Connection connection) {
        List<Vm> vms =
                getSysSrvHelper().findAllVms(connection, "status!=up");
        this.dcv.setVmsDown(vms.size());
        vms =
                getSysSrvHelper().findAllVms(connection, "status=up");
        this.dcv.setVmsUp(vms.size());
    }


    @Override
    public List<EventVo> retrieveEvents() {
        Connection connection = this.connectionService.getConnection();
        List<Event> items =
                getSysSrvHelper().findAllEvents(connection, "time>today");
        return items.stream().map(e -> {
            EventVo event = new EventVo();
            event.setId(e.id());
            event.setCorrelationId(e.correlationId());
            event.setCode(e.code());
            event.setSeverity(e.severity().value());
            event.setDescription(e.description());
            event.setOrigin(e.origin());
            event.setTime(e.time());
            return event;
        }).collect(Collectors.toList());
    }
}
