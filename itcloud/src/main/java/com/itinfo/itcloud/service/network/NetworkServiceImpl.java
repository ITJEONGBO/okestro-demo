package com.itinfo.itcloud.service.network;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.create.NetworkCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.*;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItNetworkService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.builders.*;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NetworkServiceImpl implements ItNetworkService {

    @Autowired private AdminConnectionService admin;

    @Override
    public String getName(String id){
        return admin.getConnection().systemService().networksService().networkService(id).get().send().network().name();
    }

    @Override
    public List<NetworkVo> getList() {
        SystemService systemService = admin.getConnection().systemService();

        List<Network> networkList = systemService.networksService().list().send().networks();
        List<NetworkVo> nwVoList = new ArrayList<>();
        NetworkVo nwVo = null;
        NetworkUsageVo nuVo = null;

        for(Network network : networkList){
            List<NetworkLabel> nlList = systemService.networksService().networkService(network.id()).networkLabelsService().list().send().labels();

            String label = "";
            for(NetworkLabel nl : nlList) {
                label = nl.id();
            }

            String providerId = "";
            String providerName = "";
            if(network.externalProviderPresent()) {
                OpenStackNetworkProvider np = systemService.openstackNetworkProvidersService().providerService(network.externalProvider().id()).get().send().provider();
                providerId = np.id();
                providerName = np.name();
            }

            nwVo = NetworkVo.builder()
                    .id(network.id())
                    .name(network.name())
                    .description(network.description())
                    .comment(network.comment())
                    .mtu(network.mtuAsInteger())
                    .vdsmName(network.vdsmName())
                    .datacenterId(network.dataCenter().id())
                    .datacenterName(systemService.dataCentersService().dataCenterService(network.dataCenter().id()).get().send().dataCenter().name())
                    .portIsolation(network.portIsolation())     // 포트 분리, 버전문제
                    .vlan(network.vlanPresent() ? network.vlan().id() : null)
                    .label(label)
                    .providerId(providerId)
                    .providerName(providerName)
                    .networkUsageVo( NetworkUsageVo.builder()
                                        .vm(network.usages().contains(NetworkUsage.VM))
                                        .display(network.usages().contains(NetworkUsage.DISPLAY))
                                        .migration(network.usages().contains(NetworkUsage.MIGRATION))
                                        .management(network.usages().contains(NetworkUsage.MANAGEMENT))
                                        .defaultRoute(network.usages().contains(NetworkUsage.DEFAULT_ROUTE))
                                        .gluster(network.usages().contains(NetworkUsage.GLUSTER))
                                        .build() )
                    .build();

            nwVoList.add(nwVo);
        }
        return nwVoList;
    }

    @Override
    public NetworkVo getNetwork(String id) {
        SystemService systemService = admin.getConnection().systemService();
        Network network = systemService.networksService().networkService(id).get().send().network();

        return NetworkVo.builder()
                .id(network.id())
                .name(network.name())
                .description(network.description())
                .vdsmName(network.vdsmName())
                .vlan(network.vlanPresent() ? network.vlan().id() : null)
                .mtu(network.mtuAsInteger())
                .build();
    }

    @Override
    public List<VnicProfileVo> getVnic(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<VnicProfile> vnicProfileList = systemService.networksService().networkService(id).vnicProfilesService().list().send().profiles();
        List<VnicProfileVo> vpVoList = new ArrayList<>();
        VnicProfileVo vpVo = null;
        Network network = systemService.networksService().networkService(id).get().send().network();

        for(VnicProfile vnicProfile : vnicProfileList){
            if(id.equals(vnicProfile.network().id())){
                DataCenter dataCenter = systemService.dataCentersService().dataCenterService(network.dataCenter().id()).get().send().dataCenter();

                String networkFilterId = "";
                String networkFilterName = "";
                if(vnicProfile.networkFilterPresent()) {
                    networkFilterId = vnicProfile.networkFilter().id();
                    networkFilterName = systemService.networkFiltersService().networkFilterService(vnicProfile.networkFilter().id()).get().send().networkFilter().name();
                }

                vpVo = VnicProfileVo.builder()
                        .id(vnicProfile.id())
                        .name(vnicProfile.name())
                        .description(vnicProfile.description())
                        .networkName(network.name())
                        .datacenterId(network.dataCenter().id())
                        .datacenterName(dataCenter.name())
                        .version(dataCenter.version().major() + "." + dataCenter.version().minor())
                        .passThrough(vnicProfile.passThrough().mode().value())
                        .portMirroring(vnicProfile.portMirroring())
                        .networkFilterId(networkFilterId)
                        .networkFilterName(networkFilterName)
                        .build();

                vpVoList.add(vpVo);
            }
        }
        return vpVoList;
    }

    @Override
    public List<NetworkClusterVo> getCluster(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<Cluster> clusterList = systemService.clustersService().list().send().clusters();
        List<NetworkClusterVo> ncVoList = new ArrayList<>();
        NetworkClusterVo ncVo = null;

        for(Cluster cluster : clusterList){
            List<Network> networkList = systemService.clustersService().clusterService(cluster.id()).networksService().list().send().networks();

            for(Network network : networkList){
                if(network.id().equals(id)){
                    ncVo = NetworkClusterVo.builder()
                            .id(cluster.id())
                            .name(cluster.name())
                            .version(cluster.version().major() + "." + cluster.version().minor())
                            .description(cluster.description())
                            .status(network.status().value())
                            .required(network.required())
                            .networkUsageVo( NetworkUsageVo.builder()
                                                .vm(network.usages().contains(NetworkUsage.VM))
                                                .display(network.usages().contains(NetworkUsage.DISPLAY))
                                                .migration(network.usages().contains(NetworkUsage.MIGRATION))
                                                .management(network.usages().contains(NetworkUsage.MANAGEMENT))
                                                .defaultRoute(network.usages().contains(NetworkUsage.DEFAULT_ROUTE))
                                                .gluster(network.usages().contains(NetworkUsage.GLUSTER))
                                                .build()
                            )
                            .build();

                    ncVoList.add(ncVo);
                }
            }
        }
        return ncVoList;
    }

    @Override
    public List<NetworkHostVo> getHost(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<Host> hostList = systemService.hostsService().list().send().hosts();
        List<NetworkHostVo> nhVoList = new ArrayList<>();
        NetworkHostVo nhVo = null;
        DecimalFormat df = new DecimalFormat("###,###");

        for(Host host : hostList) {
            List<NetworkAttachment> naList = systemService.hostsService().hostService(host.id()).networkAttachmentsService().list().send().attachments();

            for (NetworkAttachment na : naList) {
                if (na.networkPresent() && na.network().id().equals(id)) {
                    nhVo = new NetworkHostVo();

                    nhVo.setHostId(host.id());
                    nhVo.setHostName(host.name());
                    nhVo.setHostStatus(host.status().value());

                    Cluster c = systemService.clustersService().clusterService(host.cluster().id()).get().send().cluster();
                    nhVo.setClusterName(c.name());
                    nhVo.setDatacenterName(systemService.dataCentersService().dataCenterService(c.dataCenter().id()).get().send().dataCenter().name());

                    List<HostNic> nicList = systemService.hostsService().hostService(host.id()).nicsService().list().send().nics();
                    for (HostNic hostNic : nicList) {
                        nhVo.setNetworkStatus(hostNic.status().value());
//                nhVo.setAsynchronism(hostNic.a);
                        nhVo.setNetworkDevice(hostNic.name());

                        List<Statistic> statisticList = systemService.hostsService().hostService(host.id()).nicsService().nicService(hostNic.id()).statisticsService().list().send().statistics();
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

        List<Vm> vmList = systemService.vmsService().list().send().vms();
        List<NetworkVmVo> nVmVoList = new ArrayList<>();
        NetworkVmVo nVmVo = null;

        for(Vm vm : vmList){
            List<Nic> nicList = systemService.vmsService().vmService(vm.id()).nicsService().list().send().nics();

            for(Nic nic : nicList){
                VnicProfile vnicProfile = systemService.vnicProfilesService().profileService(nic.vnicProfile().id()).get().send().profile();

                if(vnicProfile.network().id().equals(id)){
                    nVmVo = new NetworkVmVo();
                    nVmVo.setVmId(vm.id());
                    nVmVo.setStatus(vm.statusPresent() ? vm.status().value() : null);
                    System.out.println(vm.status().value());
                    nVmVo.setVmName(vm.name());
                    nVmVo.setFqdn(vm.fqdn());
                    nVmVo.setClusterName(systemService.clustersService().clusterService(vm.cluster().id()).get().send().cluster().name() );
                    nVmVo.setDescription(vm.description());

                    nVmVo.setVnicStatus(nic.linked());
                    nVmVo.setVnicName(nic.name());

                    if(vm.status().value().equals("up")) {
                        List<ReportedDevice> reportedDeviceList = systemService.vmsService().vmService(vm.id()).reportedDevicesService().list().send().reportedDevice();
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
                        List<Statistic> statisticList = systemService.vmsService().vmService(vm.id()).nicsService().nicService(nic.id()).statisticsService().list().send().statistics();

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

        List<Template> templateList = systemService.templatesService().list().send().templates();
        List<TemplateVo> tVoList = new ArrayList<>();
        TemplateVo tVo = null;

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

        List<Permission> permissionList = systemService.networksService().networkService(id).permissionsService().list().send().permissions();
        List<PermissionVo> pVoList = new ArrayList<>();
        PermissionVo pVo = null;

        for(Permission permission : permissionList){
            pVo = new PermissionVo();
            pVo.setPermissionId(permission.id());

            if(permission.groupPresent() && !permission.userPresent()){
                Group group = systemService.groupsService().groupService(permission.group().id()).get().send().get();
                pVo.setUser(group.name());
                pVo.setNameSpace(group.namespace());
                // 생성일의 경우 db에서 가져와야함?

                Role role = systemService.rolesService().roleService(permission.role().id()).get().send().role();
                pVo.setRole(role.name());

                pVoList.add(pVo);       // 그룹에 추가
            }

            if(permission.userPresent() && !permission.groupPresent()){
                User user = systemService.usersService().userService(permission.user().id()).get().send().user();
                pVo.setUser(user.name());
                pVo.setNameSpace(user.namespace());
                pVo.setProvider(user.domainPresent() ? user.domain().name() : null);

                Role role = systemService.rolesService().roleService(permission.role().id()).get().send().role();
                pVo.setRole(role.name());

                pVoList.add(pVo);
            }
        }
        return pVoList;
    }


    @Override
    public List<NetworkDcClusterVo> getDcCluster() {
        SystemService systemService = admin.getConnection().systemService();

        List<DataCenter> dataCenterList = systemService.dataCentersService().list().send().dataCenters();


        List<NetworkDcClusterVo> dcClusterVoList = new ArrayList<>();
        NetworkDcClusterVo dcvo = null;

        for(DataCenter dataCenter : dataCenterList){
            List<Cluster> clusterList = systemService.dataCentersService().dataCenterService(dataCenter.id()).clustersService().list().send().clusters();
            List<ClusterVo> clusterVoList = new ArrayList<>();

            System.out.println(clusterList.stream().filter(Identified::idPresent).map(Cluster::name).collect(Collectors.toList()));

            for (Cluster cluster : clusterList) {
                clusterVoList.add(
                        ClusterVo.builder()
                                .id(cluster.id())
                                .name(cluster.name())
                                .build()
                );
            }

            dcvo = NetworkDcClusterVo.builder()
                    .dataCenterVo(
                            DataCenterVo.builder()
                            .id(dataCenter.id())
                            .name(dataCenter.name())
                            .build()
                    )
                    .clusterVoList(clusterVoList)
                    .build();

            dcClusterVoList.add(dcvo);
        }
        return dcClusterVoList;
    }

    @Override
    public CommonVo<Boolean> addNetwork(NetworkCreateVo ncVo) {
        SystemService systemService = admin.getConnection().systemService();

        NetworksService networksService = systemService.networksService();
        DataCenter dataCenter = systemService.dataCentersService().dataCenterService(ncVo.getDatacenterId()).get().send().dataCenter();
        OpenStackNetworkProvider openStackNetworkProvider = systemService.openstackNetworkProvidersService().list().send().providers().get(0);

        try {
            NetworkBuilder networkBuilder = new NetworkBuilder();
            networkBuilder
                    .name(ncVo.getName())
                    .description(ncVo.getDescription())
                    .comment(ncVo.getComment())
                    .portIsolation(ncVo.getPortIsolation())
                    .usages(ncVo.getUsageVm() ? NetworkUsage.VM : NetworkUsage.DEFAULT_ROUTE)
                    .mtu(ncVo.getMtu())
                    .stp(ncVo.getStp())
                    .vlan(ncVo.getVlan() != null ? new VlanBuilder().id(ncVo.getVlan()) : null)
                    .externalProvider(ncVo.getExternalProvider() ? openStackNetworkProvider : null)
                    .dataCenter(dataCenter);

            Network network = networksService.add().network(networkBuilder).send().network();

            // 클러스터 모두연결이 선택되어야지만 모두 필요가 선택됨
            try {
                for (NetworkClusterVo networkClusterVo : ncVo.getClusterVoList()) {
                    ClusterNetworksService clusterNetworksService = systemService.clustersService().clusterService(networkClusterVo.getId()).networksService();

                    if (networkClusterVo.isConnected()) {
                        clusterNetworksService.add().network(new NetworkBuilder().id(network.id()).required(networkClusterVo.isRequired())).send().network();
                    }
                }
            }catch (Exception e){
                log.error("cluster network error: ", e);
            }

            try {
                // 외부 공급자 처리시 레이블 생성 안됨
                if (ncVo.getLabel() != null && !ncVo.getLabel().isEmpty()) {
                    NetworkLabelsService nlsService = systemService.networksService().networkService(network.id()).networkLabelsService();
                    nlsService.add().label(new NetworkLabelBuilder().id(ncVo.getLabel())).send();
                }
                log.info("label add");
            }catch (Exception e){
                log.error("error: label 추가 에러");
            }

            return CommonVo.successResponse();
        }catch (Exception e){
            log.error("error, ", e);
            return CommonVo.failResponse(e.getMessage());
        }

    }

    @Override
    public CommonVo<Boolean> editNetwork(NetworkCreateVo ncVo) {
        SystemService systemService = admin.getConnection().systemService();

        NetworksService networksService = systemService.networksService();
        DataCenter dataCenter = systemService.dataCentersService().dataCenterService(ncVo.getDatacenterId()).get().send().dataCenter();
        OpenStackNetworkProvider openStackNetworkProvider = systemService.openstackNetworkProvidersService().list().send().providers().get(0);

        try {
            NetworkBuilder networkBuilder = new NetworkBuilder();
            networkBuilder
                    .id(ncVo.getId())
                    .name(ncVo.getName())
                    .description(ncVo.getDescription())
                    .comment(ncVo.getComment())
//                    .portIsolation(ncVo.getPortIsolation())       // 선택불가
                    .usages(ncVo.getUsageVm() ? NetworkUsage.VM : NetworkUsage.DEFAULT_ROUTE)
                    .mtu(ncVo.getMtu())
                    .stp(ncVo.getStp())
                    .vlan(ncVo.getVlan() != null ? new VlanBuilder().id(ncVo.getVlan()) : null)
//                    .externalProvider(ncVo.getExternalProvider() ? openStackNetworkProvider : null);  // 수정불가
                    .dataCenter(dataCenter);

            Network network = networksService.networkService(ncVo.getId()).update().network(networkBuilder).send().network();

            try {
                // 외부 공급자 처리시 레이블 생성 안됨
                if (ncVo.getLabel() != null && !ncVo.getLabel().isEmpty()) {
                    NetworkLabelsService nlsService = systemService.networksService().networkService(network.id()).networkLabelsService();
                    nlsService.labelService(nlsService.list().send().labels().get(0).id()).remove().send();

                    nlsService.add().label(new NetworkLabelBuilder().id(ncVo.getLabel())).send();// 그리고 다시 생성
                }
                log.info("label edit");
            }catch (Exception e){
                log.error("error: label 수정 에러");
            }

            return CommonVo.successResponse();
        }catch (Exception e){
            log.error("error, ", e);
            return CommonVo.failResponse(e.getMessage());
        }
    }

    @Override
    public CommonVo<Boolean> deleteNetwork(String id) {
        SystemService systemService = admin.getConnection().systemService();

        NetworkService networkService = systemService.networksService().networkService(id);
        List<Cluster> clusterList = systemService.clustersService().list().send().clusters();


        for(Cluster cluster : clusterList) {
            List<Network> clusterNetworkList = systemService.clustersService().clusterService(cluster.id()).networksService().list().send().networks();
            for (Network network : clusterNetworkList) {
                System.out.println(cluster.name() + ": " + network.name() + ", " + network.status());
                // 클러스터에서 돌아가는 네트워크가 비가동중인 상태여야지만 지울 수 있음
                if (!network.status().equals(NetworkStatus.OPERATIONAL)) {
                    networkService.remove().send();
                    log.info("network 삭제");
                    return CommonVo.successResponse();
                }
            }
        }
        log.error("network 삭제 실패");
        return CommonVo.failResponse("오류");
    }

}
