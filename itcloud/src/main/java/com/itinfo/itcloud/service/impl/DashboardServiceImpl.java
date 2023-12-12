package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.dao.DashboardDAO;
import com.itinfo.itcloud.model.DashBoardVO;
import com.itinfo.itcloud.ovirt.ConnectionService;
import com.itinfo.itcloud.service.ClustersService;
import com.itinfo.itcloud.service.DashboardService;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ConnectionService ovirtConnection;

    @Autowired
    private DashboardDAO dashboardDAO;

    private DashBoardVO dashBoardVO;

//    private List<UsageVO> usageVOList;

    public DashboardServiceImpl() {
    }


    @Override
    public DashBoardVO showDashboard(){
        Connection connection = this.ovirtConnection.getConnection();
        SystemService systemService = connection.systemService();

        // 선언 후 사용
        /*DataCentersService dataCentersService = systemService.dataCentersService();
        ClustersService clustersService = systemService.clustersService();
        HostsService hostsService = systemService.hostsService();
        VmsService vmsService = systemService.vmsService();

        List<DataCenter> dataCenterList = ((DataCentersService.ListResponse)dataCentersService.list().send()).dataCenters();
        List<Cluster> clusterList = ((ClustersService.ListResponse)clustersService.list().send()).clusters();
        List<Host> hostList = ((HostsService.ListResponse)hostsService.list().send()).hosts();
        List<Vm> vmList = ((VmsService.ListResponse)vmsService.list().send()).vms();*/

        // 선언하지 않고 사용, 시간차이 없음
        /*List<DataCenter> dataCenterList = ((DataCentersService.ListResponse)systemService.dataCentersService().list().send()).dataCenters();
        List<Cluster> clusterList = ((ClustersService.ListResponse)systemService.clustersService().list().send()).clusters();
        List<Host> hostList = ((HostsService.ListResponse)systemService.hostsService().list().send()).hosts();
        List<Vm> vmList = ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();

        this.dashBoardVO = new DashBoardVO();
        dashBoardVO.setDatacenterCnt(dataCenterList.size());
        dashBoardVO.setClusterCnt(clusterList.size());
        dashBoardVO.setHostCnt(hostList.size());
        dashBoardVO.setVmCnt(vmList.size());*/

        this.dashBoardVO = new DashBoardVO();
        getDatacenterCnt(systemService);
        getClusterCnt(systemService);
        getHostCnt(systemService);
        getVmCnt(systemService);
//        getHosts(systemService);

        return dashBoardVO;
    } // showDashboard


    private void getDatacenterCnt(SystemService systemService){
        List<DataCenter> dataCenterList = ((DataCentersService.ListResponse)systemService.dataCentersService().list().send()).dataCenters();
        this.dashBoardVO.setDatacenterCnt(dataCenterList.size());
    }

    private void getClusterCnt(SystemService systemService) {
        List<Cluster> clusterList = ((ClustersService.ListResponse)systemService.clustersService().list().send()).clusters();
        this.dashBoardVO.setClusterCnt(clusterList.size());
    }

    // 오케스트로
   /* private void getHosts(SystemService systemService) {
        List<Host> hosts = ((HostsService.ListResponse)systemService.hostsService().list().search("status!=up").caseSensitive(false).send()).hosts();
        this.dashBoardVO.setVmInactive(hosts.size());
        hosts = ((HostsService.ListResponse)systemService.hostsService().list().search("status=up").caseSensitive(false).send()).hosts();
        this.dashBoardVO.setHostActive(hosts.size());

        int sumTotalCpu = 0;
        Iterator var8 = hosts.iterator();

        while(var8.hasNext()) {
            Host host = (Host)var8.next();
            StatisticsService statisticsService = systemService.hostsService().hostService(host.id()).statisticsService();

            sumTotalCpu += host.cpu().topology().cores().intValue() * host.cpu().topology().sockets().intValue() * host.cpu().topology().threads().intValue();

            List<Vm> vms = ((VmsService.ListResponse)systemService.vmsService().list().search("host = " + host.name()).caseSensitive(false).send()).vms();
            int sumCpu = 0;

            System.out.println("host.name(): " + host.name());

            Vm vm;
            for(Iterator var14 = vms.iterator();
                    var14.hasNext();
                    sumCpu += vm.cpu().topology().cores().intValue() * vm.cpu().topology().sockets().intValue() * vm.cpu().topology().threads().intValue()) {
                System.out.println("시작 sumCpu: " + sumCpu);
                vm = (Vm)var14.next();
                System.out.println("넘어간 vm.name(): " + vm.name() + ", sumCpu: " + sumCpu);
            }

            this.dashBoardVO.setCpuUsed(sumCpu);
            System.out.println("총 sumCpu: " + sumCpu);
        }

        System.out.println("dashBoardVO.getCpuUsed(): "+dashBoardVO.getCpuUsed());

        this.dashBoardVO.setCpuTotal(sumTotalCpu);
    }*/



    private void getHostCnt(SystemService systemService){
        List<Host> hostList = ((HostsService.ListResponse)systemService.hostsService().list().send()).hosts();
        this.dashBoardVO.setHostCnt(hostList.size());

        hostList = ((HostsService.ListResponse)systemService.hostsService().list().search("status=up").send()).hosts();
        this.dashBoardVO.setHostActive(hostList.size());
        this.dashBoardVO.setHostInactive(dashBoardVO.getHostCnt() - dashBoardVO.getHostActive());

        int cpuTotal = 0;
        int cpuCommit = 0;
        int cpuAssigned = 0;

        Iterator<Host> hostIterator = hostList.iterator();

        while (hostIterator.hasNext()) {
            Host host = (Host) hostIterator.next();
            List<Statistic> statisticList = ((StatisticsService.ListResponse) systemService.hostsService().hostService(host.id()).statisticsService().list().send()).statistics();

            // memory
            Iterator<Statistic> statisticIterator = statisticList.iterator();
            while (statisticIterator.hasNext()) {
                Statistic statistic = (Statistic) statisticIterator.next();

                // memory
                if (statistic.name().equals("memory.total")) {
                    this.dashBoardVO.setMemoryTotal(dashBoardVO.getMemoryTotal() == null ? statistic.values().get(0).datum() : dashBoardVO.getMemoryTotal().add(statistic.values().get(0).datum()));
                }
                if (statistic.name().equals("memory.used")) {
                    this.dashBoardVO.setMemoryUsed(dashBoardVO.getMemoryUsed() == null ? statistic.values().get(0).datum() : dashBoardVO.getMemoryUsed().add(statistic.values().get(0).datum()));
                }
                if (statistic.name().equals("memory.free")) {
                    this.dashBoardVO.setMemoryFree(dashBoardVO.getMemoryFree() == null ? statistic.values().get(0).datum() : dashBoardVO.getMemoryFree().add(statistic.values().get(0).datum()));
                }
            }

            cpuTotal += host.cpu().topology().cores().intValue() * host.cpu().topology().sockets().intValue() * host.cpu().topology().threads().intValue();
        }

        // 할당받은 cpu
        List<Vm> vmList = ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();
        for(Vm vm : vmList){
            if(vm.status().value().equals("up")){
                cpuAssigned += (vm.cpu().topology().cores().intValue() * vm.cpu().topology().sockets().intValue() * vm.cpu().topology().threads().intValue());
            }
            cpuCommit += (vm.cpu().topology().cores().intValue() * vm.cpu().topology().sockets().intValue() * vm.cpu().topology().threads().intValue());
        }

        /*
        vmList = ((VmsService.ListResponse)systemService.vmsService().list().search("status=up").send()).vms();
        for(Vm vml : vmList){
            cpuAssigned += (vml.cpu().topology().cores().intValue() * vml.cpu().topology().sockets().intValue() * vml.cpu().topology().threads().intValue());
        }*/

        this.dashBoardVO.setCpuCommit(cpuCommit);
        this.dashBoardVO.setCpuAssigned(cpuAssigned);
        this.dashBoardVO.setCpuTotal(cpuTotal);
    }

    private void getVmCnt(SystemService systemService){
        List<Vm> vmList = ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();
        this.dashBoardVO.setVmCnt(vmList.size());

        vmList = ((VmsService.ListResponse)systemService.vmsService().list().search("status=up").send()).vms();
        this.dashBoardVO.setVmActive(vmList.size());
        vmList = ((VmsService.ListResponse)systemService.vmsService().list().search("status=down").send()).vms();
        this.dashBoardVO.setVmInactive(vmList.size());
    }



}
