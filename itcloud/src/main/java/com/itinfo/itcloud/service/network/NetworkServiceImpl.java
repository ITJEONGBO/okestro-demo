package com.itinfo.itcloud.service.network;

import com.google.gson.Gson;
import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.computing.TemplateVo;
import com.itinfo.itcloud.model.create.NetworkCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.network.*;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItNetworkService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.builders.*;
import org.ovirt.engine.sdk4.internal.containers.DnsResolverConfigurationContainer;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NetworkServiceImpl implements ItNetworkService {
    @Autowired private AdminConnectionService admin;

    // 네트워크 목록
    @Override
    public List<NetworkVo> getList() {
        SystemService system = admin.getConnection().systemService();

        List<Network> networkList = system.networksService().list().send().networks();

        return networkList.stream()
                .map(network -> {
                    List<NetworkLabel> nlList = system.networksService().networkService(network.id()).networkLabelsService().list().send().labels();

                    return NetworkVo.builder()
                            .id(network.id())
                            .name(network.name())
                            .description(network.description())
                            .comment(network.comment())
                            .mtu(network.mtuAsInteger())
                            .vdsmName(network.vdsmName())
                            .datacenterId(network.dataCenter().id())
                            .datacenterName(system.dataCentersService().dataCenterService(network.dataCenter().id()).get().send().dataCenter().name())
                            .portIsolation(network.portIsolation())
                            .vlan(network.vlanPresent() ? network.vlan().id() : null)
                            .label(
                                    nlList.stream()
                                    .map(NetworkLabel::id)
                                    .findFirst()
                                    .orElse(null)
                            )
                            .providerId(network.externalProviderPresent() ?
                                    system.openstackNetworkProvidersService().providerService(network.externalProvider().id()).get().send().provider().id()
                                    : null)
                            .providerName(network.externalProviderPresent() ?
                                    system.openstackNetworkProvidersService().providerService(network.externalProvider().id()).get().send().provider().name()
                                    : null)
                            .networkUsageVo(NetworkUsageVo.builder()
                                    .vm(network.usages().contains(NetworkUsage.VM))
                                    .display(network.usages().contains(NetworkUsage.DISPLAY))
                                    .migration(network.usages().contains(NetworkUsage.MIGRATION))
                                    .management(network.usages().contains(NetworkUsage.MANAGEMENT))
                                    .defaultRoute(network.usages().contains(NetworkUsage.DEFAULT_ROUTE))
                                    .gluster(network.usages().contains(NetworkUsage.GLUSTER))
                                    .build())
                            .build();
                })
                .collect(Collectors.toList());
    }


    // network 생성 시 필요한 dc와 클러스터 정보 가져오기
    @Override
    public List<NetworkDcClusterVo> setDcCluster() {
        SystemService system = admin.getConnection().systemService();
        List<DataCenter> dataCenterList = system.dataCentersService().list().send().dataCenters();

        return dataCenterList.stream()
                .map(dataCenter -> {
                    List<Cluster> clusterList = system.dataCentersService().dataCenterService(dataCenter.id()).clustersService().list().send().clusters();

                    return NetworkDcClusterVo.builder()
                            .dataCenterVo(
                                DataCenterVo.builder()
                                    .id(dataCenter.id())
                                    .name(dataCenter.name())
                                .build()
                            )
                            .clusterVoList(
                                clusterList.stream()
                                    .map(cluster ->
                                        ClusterVo.builder()
                                            .id(cluster.id())
                                            .name(cluster.name())
                                        .build()
                                    )
                                    .collect(Collectors.toList())
                            )
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 새 논리 네트워크 추가
    // 필요 name, datacenter_id
    @Override
    public CommonVo<Boolean> addNetwork(NetworkCreateVo ncVo) {
        SystemService system = admin.getConnection().systemService();
        NetworksService networksService = system.networksService();
        OpenStackNetworkProvider openStackNetworkProvider = system.openstackNetworkProvidersService().list().send().providers().get(0);

        Gson gson = new Gson();

        DnsResolverConfiguration dns = new DnsResolverConfigurationBuilder().nameServers().nameServers("128.0.0.2").build();

        // TODO
        //  외부 공급자 설정할 때 물리적 네트워크에 연결하는 거 구현해야함,
        //  외부 공급자 설정 시 클러스터에서 모두 필요 항목은 사라져야됨 (프론트에서 아예 설정이 안되게?)
        //  qos는 뭔지 모르겟음
        try {
            NetworkBuilder networkBuilder = new NetworkBuilder();
            networkBuilder
                    .dataCenter(new DataCenterBuilder().id(ncVo.getDatacenterId()).build())
                    .name(ncVo.getName())
                    .description(ncVo.getDescription())
                    .comment(ncVo.getComment())
                    .vlan(ncVo.getVlan() != null ? new VlanBuilder().id(ncVo.getVlan()) : null)
                    .usages(ncVo.getUsageVm() ? NetworkUsage.VM : NetworkUsage.DEFAULT_ROUTE)
                    .portIsolation(ncVo.getPortIsolation())
                    .mtu(ncVo.getMtu())
                    .stp(ncVo.getStp()) // ?
                    .externalProvider(ncVo.getExternalProvider() ?  openStackNetworkProvider : null);

            System.out.println(gson.toJson(networkBuilder));

            Network network = networksService.add().network(networkBuilder).send().network();

//            DnsResolverConfigurationContainer dnsContainer = new DnsResolverConfigurationContainer();


            // TODO: vnic 기본생성 가능. 기본생성명 수정시 기본생성과 수정명 2개가 생김
            ncVo.getVnics().forEach(vnicProfileVo -> {
                AssignedVnicProfilesService aVnicsService = system.networksService().networkService(network.id()).vnicProfilesService();
                aVnicsService.add().profile(new VnicProfileBuilder().name(vnicProfileVo.getName()).build()).send().profile();
            });

            // 클러스터 모두연결이 선택되어야지만 모두 필요가 선택됨
            ncVo.getClusterVoList().stream()
                    .filter(NetworkClusterVo::isConnected) // 연결된 경우만 필터링
                    .forEach(networkClusterVo -> {
                        ClusterNetworksService clusterNetworksService = system.clustersService().clusterService(networkClusterVo.getId()).networksService();
                        clusterNetworksService.add().network(
                                new NetworkBuilder()
                                        .id(network.id())
                                        .required(networkClusterVo.isRequired())
                        ).send().network();
                    });

            // 외부 공급자 처리시 레이블 생성 안됨
            if (ncVo.getLabel() != null && !ncVo.getLabel().isEmpty()) {
                NetworkLabelsService nlsService = system.networksService().networkService(network.id()).networkLabelsService();
                nlsService.add().label(new NetworkLabelBuilder().id(ncVo.getLabel())).send();
            }

            log.info("network {} 추가 성공", network.name());
            return CommonVo.successResponse();
        }catch (Exception e){
            log.error("error, ", e);
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }

    // 네트워크 편집 창
    // TODO
    @Override
    public NetworkCreateVo setEditNetwork(String id) {
        SystemService system = admin.getConnection().systemService();
        Network network = system.networksService().networkService(id).get().follow("networklabels").send().network();

        return NetworkCreateVo.builder()
                .datacenterId(network.dataCenter().id())
                .datacenterName(system.dataCentersService().dataCenterService(network.dataCenter().id()).get().send().dataCenter().name())
                .name(network.name())
                .description(network.description())
                .comment(network.comment())
                .label(network.networkLabels().get(0).id())
                .vlan(network.vlan().idAsInteger())
                .usageVm(network.usages().contains(NetworkUsage.VM))
                .portIsolation(network.portIsolation())
                .dnsList(network.dnsResolverConfigurationPresent() ? network.dnsResolverConfiguration().nameServers() : null)
                .mtu(network.mtu().intValue())
                .externalProvider(network.externalProviderPresent())
//                .physicalNw()     //물리적 네트워크
                .build();
    }

    // 네트워크 편집
    @Override
    public CommonVo<Boolean> editNetwork(NetworkCreateVo ncVo) {
        SystemService system = admin.getConnection().systemService();
        NetworksService networksService = system.networksService();
        DataCenter dataCenter = system.dataCentersService().dataCenterService(ncVo.getDatacenterId()).get().send().dataCenter();
//        OpenStackNetworkProvider openStackNetworkProvider = systemService.openstackNetworkProvidersService().list().send().providers().get(0);

        try {
            NetworkBuilder networkBuilder = new NetworkBuilder();
            networkBuilder
                    .id(ncVo.getId())
                    .name(ncVo.getName())
                    .description(ncVo.getDescription())
                    .comment(ncVo.getComment())
//                    .portIsolation(ncVo.getPortIsolation())       // 선택불가
                    .usages(ncVo.getUsageVm() ? NetworkUsage.VM : NetworkUsage.DEFAULT_ROUTE)
                    // TODO DNS 구현안됨
//                    .dnsResolverConfiguration()
                    .mtu(ncVo.getMtu())
                    .stp(ncVo.getStp())
                    .vlan(ncVo.getVlan() != null ? new VlanBuilder().id(ncVo.getVlan()) : null)
//                    .externalProvider(ncVo.getExternalProvider() ? openStackNetworkProvider : null);  // 수정불가
                    .dataCenter(dataCenter);

            // 외부 공급자 처리시 레이블 생성 안됨
            if (ncVo.getLabel() != null && !ncVo.getLabel().isEmpty()) {
                NetworkLabelsService nlsService = system.networksService().networkService(ncVo.getId()).networkLabelsService();

                if( nlsService.list().send().labels().get(0).idPresent() ) {
                    nlsService.labelService(nlsService.list().send().labels().get(0).id()).remove().send();
                }
                nlsService.add().label(new NetworkLabelBuilder().id(ncVo.getLabel())).send();// 그리고 다시 생성
            }

            return CommonVo.successResponse();
        }catch (Exception e){
            log.error("error, ", e);
            return CommonVo.failResponse(e.getMessage());
        }
    }

    // 네트워크 삭제
    @Override
    public CommonVo<Boolean> deleteNetwork(String id) {
        SystemService system = admin.getConnection().systemService();
        NetworkService networkService = system.networksService().networkService(id);
        List<Cluster> clusterList = system.clustersService().list().send().clusters();

        // 클러스터 목록을 스트림으로 변환하고 네트워크가 비가동 중인지 확인합니다
        boolean canDelete = clusterList.stream()
                .flatMap(cluster -> system.clustersService().clusterService(cluster.id()).networksService().list().send().networks().stream())
                .filter(network -> network.id().equals(id))
                .noneMatch(network -> network.status().equals(NetworkStatus.OPERATIONAL));

        // 삭제 가능한 경우 네트워크를 삭제하고 성공 응답을 반환합니다
        if (canDelete) {
            networkService.remove().send();
            log.info("network 삭제");
            return CommonVo.successResponse();
        } else {
            log.error("network 삭제 실패");
            return CommonVo.failResponse("오류");
        }

//        for(Cluster cluster : clusterList) {
//            List<Network> clusterNetworkList = system.clustersService().clusterService(cluster.id()).networksService().list().send().networks();
//
//            for (Network network : clusterNetworkList) {
//                System.out.println(cluster.name() + ": " + network.name() + ", " + network.status());
//
//                // 클러스터에서 돌아가는 네트워크가 비가동중인 상태여야지만 지울 수 있음
//                if (!network.status().equals(NetworkStatus.OPERATIONAL)) {
//                    networkService.remove().send();
//                    log.info("network 삭제");
//                    return CommonVo.successResponse();
//                }
//            }
//        }
//        log.error("network 삭제 실패");
//        return CommonVo.failResponse("오류");
    }


    // 가져오기 출력 창
    @Override
    public NetworkImportVo setImportNetwork() {
        SystemService system = admin.getConnection().systemService();

        // 그냥 있는거 가져오기
        OpenStackNetworkProvider osProvider = system.openstackNetworkProvidersService().list().follow("networks").send().providers().get(0);

        List<Network> ntList = system.networksService().list().send().networks();
        List<DataCenter> dcList = system.dataCentersService().list().send().dataCenters();
//        DataCenter dc = systemService.dataCentersService().dataCenterService(id).get().send().dataCenter();

        List<OpenstackVo> osVoList = osProvider.networks().stream()
                .map(os -> OpenstackVo.builder()
                                .id(os.id())
                                .name(os.name())
//                        .dcId()
                                .build()
                )
                .collect(Collectors.toList());

        List<DataCenterVo> dcVoList = dcList.stream()
                .map(dc -> DataCenterVo.builder()
                        .id(dc.id())
                        .name(dc.name())
                        .build()
                )
                .collect(Collectors.toList());

        return NetworkImportVo.builder()
                .id(osProvider.id())
                .name(osProvider.name())
                .osVoList(osVoList)
                .dcVoList(dcVoList)
                .build();
    }

    // 네트워크 - 가져오기 출력창 (openStack)
    @Override
    public CommonVo<Boolean> importNetwork() {
        SystemService system = admin.getConnection().systemService();

        // 그냥 있는거 가져오기
        OpenStackNetworkProvider osProvider = system.openstackNetworkProvidersService().list().follow("networks").send().providers().get(0);

        return null;
    }





    @Override
    public NetworkVo getNetwork(String id) {
        SystemService system = admin.getConnection().systemService();
        Network network = system.networksService().networkService(id).get().send().network();

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
        SystemService system = admin.getConnection().systemService();
        List<VnicProfile> vnicProfileList = system.networksService().networkService(id).vnicProfilesService().list().send().profiles();
        Network network = system.networksService().networkService(id).get().send().network();

        return vnicProfileList.stream()
                .filter(vnicProfile -> id.equals(vnicProfile.network().id()))
                .map(vnicProfile -> {
                    DataCenter dataCenter = system.dataCentersService().dataCenterService(network.dataCenter().id()).get().send().dataCenter();

                    return VnicProfileVo.builder()
                            .id(vnicProfile.id())
                            .name(vnicProfile.name())
                            .description(vnicProfile.description())
                            .networkName(network.name())
                            .datacenterId(network.dataCenter().id())
                            .datacenterName(dataCenter.name())
                            .version(dataCenter.version().major() + "." + dataCenter.version().minor())
                            .passThrough(vnicProfile.passThrough().mode().value())
                            .portMirroring(vnicProfile.portMirroring())
                            .networkFilterId(vnicProfile.networkFilterPresent() ? vnicProfile.networkFilter().id() : null)
                            .networkFilterName(vnicProfile.networkFilterPresent() ? system.networkFiltersService().networkFilterService(vnicProfile.networkFilter().id()).get().send().networkFilter().name() : null)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<NetworkClusterVo> getCluster(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Cluster> clusterList = system.clustersService().list().send().clusters();

        return clusterList.stream()
                .flatMap(cluster -> {
                    List<Network> networkList = system.clustersService().clusterService(cluster.id()).networksService().list().send().networks();

                    return networkList.stream()
                            .filter(network -> network.id().equals(id))
                            .map(network ->
                                    NetworkClusterVo.builder()
                                        .id(cluster.id())
                                        .name(cluster.name())
                                        .version(cluster.version().major() + "." + cluster.version().minor())
                                        .description(cluster.description())
                                        .status(network.status().value())
                                        .required(network.required())
                                        .networkUsageVo(
                                            NetworkUsageVo.builder()
                                                .vm(network.usages().contains(NetworkUsage.VM))
                                                .display(network.usages().contains(NetworkUsage.DISPLAY))
                                                .migration(network.usages().contains(NetworkUsage.MIGRATION))
                                                .management(network.usages().contains(NetworkUsage.MANAGEMENT))
                                                .defaultRoute(network.usages().contains(NetworkUsage.DEFAULT_ROUTE))
                                                .gluster(network.usages().contains(NetworkUsage.GLUSTER))
                                                .build()
                                        )
                                        .build()
                            );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<NetworkHostVo> getHost(String id) { //id가 network id
        SystemService system = admin.getConnection().systemService();
        List<Host> hostList = system.hostsService().list().send().hosts();
        DecimalFormat df = new DecimalFormat("###,###");

        return hostList.stream()
                .flatMap(host -> {
                    List<NetworkAttachment> naList = system.hostsService().hostService(host.id()).networkAttachmentsService().list().send().attachments();
                    Cluster c = system.clustersService().clusterService(host.cluster().id()).get().send().cluster();
                    List<HostNic> nicList = system.hostsService().hostService(host.id()).nicsService().list().send().nics();

                    return naList.stream()
                            .filter(na -> na.networkPresent() && na.network().id().equals(id))
                            .map(na -> {
                                NetworkHostVo.NetworkHostVoBuilder builder = NetworkHostVo.builder();
                                builder.hostId(host.id())
                                    .hostName(host.name())
                                    .hostStatus(host.status())
                                    .clusterName(c.name())
                                    .datacenterName(system.dataCentersService().dataCenterService(c.dataCenter().id()).get().send().dataCenter().name());

                                for (HostNic hostNic : nicList) {
                                    List<Statistic> statisticList = system.hostsService().hostService(host.id()).nicsService().nicService(hostNic.id()).statisticsService().list().send().statistics();
                                    builder.networkStatus(hostNic.status())
                                        .networkDevice(hostNic.name())
                                        .speed(hostNic.speed())
                                        .rxSpeed(getStatistics(statisticList, "data.current.rx.bps"))
                                        .txSpeed(getStatistics(statisticList, "data.current.tx.bps"))
                                        .rxTotalSpeed(getStatistics(statisticList, "data.total.rx"))
                                        .txTotalSpeed(getStatistics(statisticList, "data.total.tx"));
                                }
                                return builder.build();
                            });
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<NetworkVmVo> getVm(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Vm> vmList = system.vmsService().list().send().vms();
        List<NetworkVmVo> nVmVoList = new ArrayList<>();

        for(Vm vm : vmList){
            List<Nic> nicList = system.vmsService().vmService(vm.id()).nicsService().list().send().nics();
            List<ReportedDevice> rdList = system.vmsService().vmService(vm.id()).reportedDevicesService().list().send().reportedDevice();

            for(Nic nic : nicList) {
                VnicProfile vnicProfile = system.vnicProfilesService().profileService(nic.vnicProfile().id()).get().send().profile();
                List<Statistic> statisticList = system.vmsService().vmService(vm.id()).nicsService().nicService(nic.id()).statisticsService().list().send().statistics();

                if (vnicProfile.network().id().equals(id)) {
                    NetworkVmVo.NetworkVmVoBuilder builder = NetworkVmVo.builder();
                    builder
                            .vmId(vm.id())
                            .vmName(vm.name())
                            .status(vm.statusPresent() ? vm.status() : null)
                            .fqdn(vm.fqdn())
                            .clusterName(system.clustersService().clusterService(vm.cluster().id()).get().send().cluster().name())
                            .description(vm.description())
                            .vnicStatus(nic.linked())
                            .vnicName(nic.name())
                            .vnicRx(vm.status() == VmStatus.UP ? getStatistics(statisticList, "data.current.rx.bps") : null)
                            .vnicTx(vm.status() == VmStatus.UP ? getStatistics(statisticList, "data.current.tx.bps") : null)
                            .rxTotalSpeed(vm.status() == VmStatus.UP ? getStatistics(statisticList, "data.total.rx") : null)
                            .txTotalSpeed(vm.status() == VmStatus.UP ? getStatistics(statisticList, "data.total.tx") : null);

                    if (vm.status().value().equals("up")) {
                        for (ReportedDevice rd : rdList) {
                            if (vm.status() == VmStatus.UP && rd.ipsPresent()) {
                                builder
                                        .ipv4(rd.ips().get(0).address())
                                        .ipv6(rd.ips().get(1).address());
                            }
                        }
                    }
                    nVmVoList.add(builder.build());
                }
            }
        }
        return nVmVoList;
    }




    @Override
    public List<TemplateVo> getTemplate(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Template> templateList = system.templatesService().list().send().templates();


        return null;
    }

    @Override
    public List<PermissionVo> getPermission(String id) {
        SystemService system = admin.getConnection().systemService();

        List<Permission> permissionList = system.networksService().networkService(id).permissionsService().list().send().permissions();
        List<PermissionVo> pVoList = new ArrayList<>();
        PermissionVo pVo = null;

        for(Permission permission : permissionList){
            pVo = new PermissionVo();
            pVo.setPermissionId(permission.id());

            if(permission.groupPresent() && !permission.userPresent()){
                Group group = system.groupsService().groupService(permission.group().id()).get().send().get();
                pVo.setUser(group.name());
                pVo.setNameSpace(group.namespace());
                // 생성일의 경우 db에서 가져와야함?

                Role role = system.rolesService().roleService(permission.role().id()).get().send().role();
                pVo.setRole(role.name());

                pVoList.add(pVo);       // 그룹에 추가
            }

            if(permission.userPresent() && !permission.groupPresent()){
                User user = system.usersService().userService(permission.user().id()).get().send().user();
                pVo.setUser(user.name());
                pVo.setNameSpace(user.namespace());
                pVo.setProvider(user.domainPresent() ? user.domain().name() : null);

                Role role = system.rolesService().roleService(permission.role().id()).get().send().role();
                pVo.setRole(role.name());

                pVoList.add(pVo);
            }
        }
        return pVoList;
    }

    @Override
    public CommonVo<Boolean> addVnic(VnicProfileVo vpVo) {
        SystemService system = admin.getConnection().systemService();

        DataCenter dataCenter = system.dataCentersService().dataCenterService(vpVo.getDatacenterId()).get().send().dataCenter();
        NetworkService networkService = system.networksService().networkService(vpVo.getNetworkId());



        return null;
    }

    @Override
    public CommonVo<Boolean> editVnic(VnicProfileVo vpVo) {
        SystemService system = admin.getConnection().systemService();

        return null;
    }

    @Override
    public CommonVo<Boolean> deleteVnic(VnicProfileVo vpVo) {
        SystemService system = admin.getConnection().systemService();

        return null;
    }



    private BigInteger getStatistics(List<Statistic> statisticList, String q){
        return statisticList.stream()
                .filter(statistic -> statistic.name().equals(q))
                .map(statistic -> statistic.values().get(0).datum().toBigInteger())
                .findAny()
                .orElse(BigInteger.ZERO);
    }

}
