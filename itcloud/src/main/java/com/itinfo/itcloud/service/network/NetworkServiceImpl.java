package com.itinfo.itcloud.service.network;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.network.*;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItNetworkService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.nio.ch.Net;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class NetworkServiceImpl implements ItNetworkService {

    @Autowired
    private AdminConnectionService admin;

    @Override
    public String getName(String id){
        SystemService systemService = admin.getConnection().systemService();

        return ((NetworkService.GetResponse)systemService.networksService().networkService(id).get().send()).network().name();
    }

    @Override
    public List<NetworkVo> getList() {
        SystemService systemService = admin.getConnection().systemService();

        List<NetworkVo> nwVoList = new ArrayList<>();
        NetworkVo nwVo = null;

        List<Network> networkList =
                ((NetworksService.ListResponse)systemService.networksService().list().send()).networks();

        for(Network network : networkList){
            nwVo = new NetworkVo();

            nwVo.setId(network.id());
            nwVo.setName(network.name());
            nwVo.setDescription(network.description());
            nwVo.setComment(network.comment());
            nwVo.setMtu(network.mtuAsInteger());
            nwVo.setVdsmName(network.vdsmName());
            nwVo.setDatacenterId(network.dataCenter().id());
            nwVo.setDatacenterName( ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(network.dataCenter().id()).get().send()).dataCenter().name() );
//            nwVo.setPortIsolation(network.portIsolation());        // 포트 분리, 버전문제
            nwVo.setVlan(network.vlanPresent() ? network.vlan().id() : null);


            List<NetworkLabel> nlList =
                    ((NetworkLabelsService.ListResponse) systemService.networksService().networkService(network.id()).networkLabelsService().list().send()).labels();
            for(NetworkLabel nl : nlList) {
                nwVo.setLabel(nl.id());
            }

            //
            if(network.externalProviderPresent()) {
                OpenStackNetworkProvider np =
                        ((OpenstackNetworkProviderService.GetResponse) systemService.openstackNetworkProvidersService().providerService(network.externalProvider().id()).get().send()).provider();
                nwVo.setProviderId(np.id());
                nwVo.setProviderName(np.name());
            }

            // usages
            NetworkUsageVo nuVo = new NetworkUsageVo();
            nuVo.setVm(network.usages().contains(NetworkUsage.VM));
            nuVo.setDisplay(network.usages().contains(NetworkUsage.DISPLAY));
            nuVo.setMigration(network.usages().contains(NetworkUsage.MIGRATION));
            nuVo.setManagement(network.usages().contains(NetworkUsage.MANAGEMENT));
            nuVo.setDefaultRoute(network.usages().contains(NetworkUsage.DEFAULT_ROUTE));
            nuVo.setGluster(network.usages().contains(NetworkUsage.GLUSTER));

            nwVo.setNetworkUsageVo(nuVo);

            nwVoList.add(nwVo);
        }
        return nwVoList;
    }

    @Override
    public NetworkVo getNetwork(String id) {
        SystemService systemService = admin.getConnection().systemService();

        NetworkVo nwVo = new NetworkVo();

        Network network =
                ((NetworkService.GetResponse)systemService.networksService().networkService(id).get().send()).network();

        nwVo.setId(network.id());
        nwVo.setName(network.name());
        nwVo.setDescription(network.description());
        nwVo.setVdsmName(network.vdsmName());
        nwVo.setVlan(network.vlanPresent() ? network.vlan().id() : null);   // vlan
        nwVo.setMtu(network.mtuAsInteger());

        return nwVo;
    }

    @Override
    public List<VnicProfileVo> getVnic(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<VnicProfileVo> vpVoList = new ArrayList<>();
        VnicProfileVo vpVo = null;

        List<VnicProfile> vnicProfileList =
                ((AssignedVnicProfilesService.ListResponse)systemService.networksService().networkService(id).vnicProfilesService().list().send()).profiles();

        for(VnicProfile vnicProfile : vnicProfileList){
            if(id.equals(vnicProfile.network().id())){
                Network network =
                        ((NetworkService.GetResponse)systemService.networksService().networkService(id).get().send()).network();

                vpVo = new VnicProfileVo();

                vpVo.setId(vnicProfile.id());
                vpVo.setName(vnicProfile.name());
                vpVo.setDescription(vnicProfile.description());
                vpVo.setNetworkName( ((NetworkService.GetResponse)systemService.networksService().networkService(id).get().send()).network().name() );

                DataCenter dataCenter = ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(network.dataCenter().id()).get().send()).dataCenter();
                vpVo.setDatacenterId(network.dataCenter().id());
                vpVo.setDatacenterName( dataCenter.name() );
                vpVo.setVersion(dataCenter.version().major() + "." + dataCenter.version().minor());

                vpVo.setPassThrough(vnicProfile.passThrough().mode().value());
                vpVo.setPortMirroring(vnicProfile.portMirroring());

                if(vnicProfile.networkFilterPresent()) {
                    NetworkFilter nf = ((NetworkFilterService.GetResponse) systemService.networkFiltersService().networkFilterService(vnicProfile.networkFilter().id()).get().send()).networkFilter();
                    vpVo.setNetworkFilterId(vnicProfile.networkFilter().id());
                    vpVo.setNetworkFilterName(nf.name());
                }
                vpVoList.add(vpVo);
            }
        }
        return vpVoList;
    }

    @Override
    public List<NetworkClusterVo> getCluster(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<NetworkClusterVo> ncVoList = new ArrayList<>();
        NetworkClusterVo ncVo = null;

        List<Cluster> clusterList = ((ClustersService.ListResponse)systemService.clustersService().list().send()).clusters();

        for(Cluster cluster : clusterList){
            List<Network> networkList =
                    ((ClusterNetworksService.ListResponse)systemService.clustersService().clusterService(cluster.id()).networksService().list().send()).networks();

            for(Network network : networkList){
                if(network.id().equals(id)){
                    ncVo = new NetworkClusterVo();

                    ncVo.setId(cluster.id());
                    ncVo.setName(cluster.name());
                    ncVo.setVersion(cluster.version().major() + "." + cluster.version().minor());
                    ncVo.setDescription(cluster.description());

                    ncVo.setStatus(network.status().value());
                    ncVo.setRequired(network.required());

                    // usages
                    NetworkUsageVo nuVo = new NetworkUsageVo();
                    nuVo.setVm(network.usages().contains(NetworkUsage.VM));
                    nuVo.setDisplay(network.usages().contains(NetworkUsage.DISPLAY));
                    nuVo.setMigration(network.usages().contains(NetworkUsage.MIGRATION));
                    nuVo.setManagement(network.usages().contains(NetworkUsage.MANAGEMENT));
                    nuVo.setDefaultRoute(network.usages().contains(NetworkUsage.DEFAULT_ROUTE));
                    nuVo.setGluster(network.usages().contains(NetworkUsage.GLUSTER));

                    ncVo.setNetworkUsageVo(nuVo);

                    ncVoList.add(ncVo);
                }
            }
        }
        return ncVoList;
    }

    @Override
    public List<NetworkHostVo> getHost(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<NetworkHostVo> nhVoList = new ArrayList<>();
        NetworkHostVo nhVo = null;
        DecimalFormat df = new DecimalFormat("###,###");

        List<Host> hostList = ((HostsService.ListResponse)systemService.hostsService().list().send()).hosts();

        for(Host host : hostList) {
            List<NetworkAttachment> naList = ((NetworkAttachmentsService.ListResponse) systemService.hostsService().hostService(host.id()).networkAttachmentsService().list().send()).attachments();

            for (NetworkAttachment na : naList) {
                if (na.networkPresent() && na.network().id().equals(id)) {
                    nhVo = new NetworkHostVo();

                    nhVo.setHostId(host.id());
                    nhVo.setHostName(host.name());
                    nhVo.setHostStatus(host.status().value());

                    Cluster c = ((ClusterService.GetResponse) systemService.clustersService().clusterService(host.cluster().id()).get().send()).cluster();
                    nhVo.setClusterName(c.name());
                    nhVo.setDatacenterName(((DataCenterService.GetResponse) systemService.dataCentersService().dataCenterService(c.dataCenter().id()).get().send()).dataCenter().name());


                    List<HostNic> nicList =
                            ((HostNicsService.ListResponse) systemService.hostsService().hostService(host.id()).nicsService().list().send()).nics();
                    for (HostNic hostNic : nicList) {
                        nhVo.setNetworkStatus(hostNic.status().value());
//                nhVo.setAsynchronism(hostNic.a);
                        nhVo.setNetworkDevice(hostNic.name());

                        List<Statistic> statisticList =
                                ((StatisticsService.ListResponse) systemService.hostsService().hostService(host.id()).nicsService().nicService(hostNic.id()).statisticsService().list().send()).statistics();

                        for (Statistic statistic : statisticList) {
                            String st = "";

                            if (statistic.name().equals("data.current.rx.bps")) {
                                st = df.format((statistic.values().get(0).datum()).divide(BigDecimal.valueOf(1024 * 1024)));
                                nhVo.setRxSpeed(st);
                            }
                            if (statistic.name().equals("data.current.tx.bps")) {
                                st = df.format((statistic.values().get(0).datum()).divide(BigDecimal.valueOf(1024 * 1024)));
                                nhVo.setTxSpeed(st);
                            }
                            if (statistic.name().equals("data.total.rx")) {
                                st = df.format(statistic.values().get(0).datum());
                                nhVo.setRxTotalSpeed(st);
                            }
                            if (statistic.name().equals("data.total.tx")) {
                                st = df.format(statistic.values().get(0).datum());
                                nhVo.setTxTotalSpeed(st);
                            }
                        }
                        nhVo.setSpeed(hostNic.speed());
                    }
                    nhVoList.add(nhVo);
                }
            }
        }
        return nhVoList;
    }

    @Override
    public List<NetworkVmVo> getVm(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<NetworkVmVo> nVmVoList = new ArrayList<>();
        NetworkVmVo nVmVo = null;

        List<Vm> vmList =
                ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();
        for(Vm vm : vmList){
            List<Nic> nicList =
                    ((VmNicsService.ListResponse)systemService.vmsService().vmService(vm.id()).nicsService().list().send()).nics();

            for(Nic nic : nicList){
                VnicProfile vnicProfile =
                        ((VnicProfileService.GetResponse)systemService.vnicProfilesService().profileService(nic.vnicProfile().id()).get().send()).profile();

                if(vnicProfile.network().id().equals(id)){
                    nVmVo = new NetworkVmVo();
                    nVmVo.setVmId(vm.id());
                    nVmVo.setStatus(vm.statusPresent() ? vm.status().value() : null);
                    System.out.println(vm.status().value());
                    nVmVo.setVmName(vm.name());
                    nVmVo.setFqdn(vm.fqdn());
                    nVmVo.setClusterName( ((ClusterService.GetResponse)systemService.clustersService().clusterService(vm.cluster().id()).get().send()).cluster().name() );
                    nVmVo.setDescription(vm.description());

                    nVmVo.setVnicStatus(nic.linked());
                    nVmVo.setVnicName(nic.name());

                    if(vm.status().value().equals("up")) {
                        List<ReportedDevice> reportedDeviceList =
                                ((VmReportedDevicesService.ListResponse)systemService.vmsService().vmService(vm.id()).reportedDevicesService().list().send()).reportedDevice();
                        for(ReportedDevice rd : reportedDeviceList){
                            if(rd.ipsPresent()){
                                //                        nVmVo.setVnicName(rd.name());
                                String ipv6 = "";
                                nVmVo.setIpv4(rd.ips().get(0).address());

                                for(int i=0; i < rd.ips().size(); i++){
                                    if(rd.ips().get(i).version().value().equals("v6")){
                                        ipv6 += rd.ips().get(i).address()+" ";
                                    }
                                }
                                nVmVo.setIpv6(ipv6);
                            }
                        }

                        // vm이 올라와있는 상태에서만 rx, tx 값 출력
                        DecimalFormat df = new DecimalFormat("###,###");
                        List<Statistic> statisticList =
                                ((StatisticsService.ListResponse) systemService.vmsService().vmService(vm.id()).nicsService().nicService(nic.id()).statisticsService().list().send()).statistics();

                        for (Statistic statistic : statisticList) {
                            String st = "";

                            if (statistic.name().equals("data.current.rx.bps")) {
                                st = df.format((statistic.values().get(0).datum()).divide(BigDecimal.valueOf(1024 * 1024)));
                                nVmVo.setVnicRx(st);
                            }
                            if (statistic.name().equals("data.current.tx.bps")) {
                                st = df.format((statistic.values().get(0).datum()).divide(BigDecimal.valueOf(1024 * 1024)));
                                nVmVo.setVnicTx(st);
                            }
                            if (statistic.name().equals("data.total.rx")) {
                                System.out.println("asd: " + statistic.name().equals("data.total.rx"));
                                st = df.format(statistic.valuesPresent() ? statistic.values().get(0).datum() : null);
                                nVmVo.setRxTotalSpeed(st);
                            }
                            if (statistic.name().equals("data.total.tx")) {
                                st = df.format(statistic.valuesPresent() ? statistic.values().get(0).datum() : null);
                                nVmVo.setTxTotalSpeed(st);
                            }
                        }
                    }
                }
            }

            nVmVoList.add(nVmVo);
        }
        return nVmVoList;
    }

    @Override
    public List<TemplateVo> getTemplate(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<TemplateVo> tVoList = new ArrayList<>();
        TemplateVo tVo = null;

        List<Template> templateList =
                ((TemplatesService.ListResponse)systemService.templatesService().list().send()).templates();

        for(Template template : templateList){
            if(template.nicsPresent()){
                tVo = new TemplateVo();

                tVo.setId(template.id());
                tVo.setName(template.name());

                tVoList.add(tVo);
            }
        }

        return tVoList;
    }

    @Override
    public List<PermissionVo> getPermission(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<PermissionVo> pVoList = new ArrayList<>();
        PermissionVo pVo = null;

        List<Permission> permissionList =
                ((AssignedPermissionsService.ListResponse)systemService.networksService().networkService(id).permissionsService().list().send()).permissions();

        for(Permission permission : permissionList){
            pVo = new PermissionVo();
            pVo.setPermissionId(permission.id());

            if(permission.groupPresent() && !permission.userPresent()){
                Group group = ((GroupService.GetResponse)systemService.groupsService().groupService(permission.group().id()).get().send()).get();
                pVo.setUser(group.name());
                pVo.setNameSpace(group.namespace());
                // 생성일의 경우 db에서 가져와야함?

                Role role = ((RoleService.GetResponse)systemService.rolesService().roleService(permission.role().id()).get().send()).role();
                pVo.setRole(role.name());

                pVoList.add(pVo);       // 그룹에 추가
            }

            if(permission.userPresent() && !permission.groupPresent()){
                User user = ((UserService.GetResponse)systemService.usersService().userService(permission.user().id()).get().send()).user();
                pVo.setUser(user.name());
                pVo.setNameSpace(user.namespace());
                pVo.setProvider(user.domainPresent() ? user.domain().name() : null);

                Role role = ((RoleService.GetResponse)systemService.rolesService().roleService(permission.role().id()).get().send()).role();
                pVo.setRole(role.name());

                pVoList.add(pVo);
            }
        }
        return pVoList;
    }


}
