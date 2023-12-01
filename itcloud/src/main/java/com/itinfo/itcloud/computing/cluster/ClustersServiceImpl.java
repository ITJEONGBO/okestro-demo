package com.itinfo.itcloud.computing.cluster;

import com.itinfo.itcloud.computing.host.HostdService;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClustersServiceImpl implements ClustersService{

    @Autowired
    private AdminConnectionService adminConnectionService;

    @Autowired
    private ClustersDAO clustersDAO;

    @Autowired
    private HostdService hostdService;

    @Autowired
    private ClustersService clustersService;

    public ClustersServiceImpl(){}

    /*public List<ClusterVO> retrieveClusters(){
        Connection connection = this.adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();
        List<Cluster> clusters = ((org.ovirt.engine.sdk4.services.ClustersService.ListResponse)systemService.clustersService().list().send()).clusters();

        System.out.println("clusters size : " + clusters.size());
        List<ClusterVO> clustersInfo = new ArrayList<>();
        Iterator var5 = clusters.iterator();

        while(var5.hasNext()) {
            Cluster cluster = (Cluster)var5.next();
            ClusterVO clusterVo = new ClusterVO();
            clusterVo.setClusterId(cluster.id());
            clusterVo.setClusterName(cluster.name());
            clusterVo.setDescription(cluster.description());
            if (cluster.cpu() != null) {
                clusterVo.setCpuType(cluster.cpu().type());
                clusterVo.setCpuImage(cluster.cpu().architecture().name());
            }

            NetworkVO networkVo = new NetworkVO();
            List<Network> networks = ((org.ovirt.engine.sdk4.services.ClusterNetworksService.ListResponse)systemService.clustersService().clusterService(cluster.id()).networksService().list().send()).networks();
            if (networks.size() != 0) {
                Network network = (Network)networks.get(0);
                networkVo.setNetworkId(network.id());
                networkVo.setNetworkName(network.name());
                networkVo.setDescription(network.description());
                networkVo.setComment(network.comment());
                clusterVo.setNetwork(networkVo);
            }

            this.setClusterHostCnt(systemService, cluster.id(), clusterVo);
            this.setClusterVmCnt(systemService, cluster.id(), clusterVo);
            clustersInfo.add(clusterVo);
        }

        return clustersInfo;
    }

    private void setClusterHostCnt(SystemService systemService, String clusterId, ClusterVO clusterVo) {
        org.ovirt.engine.sdk4.services.HostsService hostsService = systemService.hostsService();
        int cnt = 0;
        int upCnt = 0;
        int downCnt = 0;
        List<Host> hosts = ((org.ovirt.engine.sdk4.services.HostsService.ListResponse)hostsService.list().send()).hosts();
        Iterator var9 = hosts.iterator();

        while(var9.hasNext()) {
            Host host = (Host)var9.next();
            if (host.cluster().id().equals(clusterId)) {
                if ("up".equalsIgnoreCase(host.status().name())) {
                    ++upCnt;
                } else {
                    ++downCnt;
                }

                ++cnt;
            }
        }

        clusterVo.setHostCnt(cnt);
        clusterVo.setHostsUp(upCnt);
        clusterVo.setHostsDown(downCnt);
    }

    private void setClusterVmCnt(SystemService systemService, String clusterId, ClusterVO clusterVo) {
        VmsService vmsService = systemService.vmsService();
        int cnt = 0;
        int upCnt = 0;
        int downCnt = 0;
        List<Vm> vms = ((org.ovirt.engine.sdk4.services.VmsService.ListResponse)vmsService.list().send()).vms();
        Iterator var9 = vms.iterator();

        while(var9.hasNext()) {
            Vm vm = (Vm)var9.next();
            if (vm.cluster().id().equals(clusterId)) {
                if ("up".equalsIgnoreCase(vm.status().name())) {
                    ++upCnt;
                } else {
                    ++downCnt;
                }

                ++cnt;
            }
        }

        clusterVo.setVmCnt(cnt);
        clusterVo.setVmsUp(upCnt);
        clusterVo.setVmsDown(downCnt);
    }*/


}
