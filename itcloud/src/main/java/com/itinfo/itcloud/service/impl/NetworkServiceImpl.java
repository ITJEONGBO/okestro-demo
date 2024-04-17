package com.itinfo.itcloud.service.impl;

import com.google.gson.Gson;
import com.itinfo.itcloud.model.TypeExtKt;
import com.itinfo.itcloud.model.computing.ClusterVo;
import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NetworkServiceImpl implements ItNetworkService {
    @Autowired private AdminConnectionService admin;
    private final CommonService commonService = new CommonService();

    // 네트워크 목록
    @Override
    public List<NetworkVo> getList() {
        SystemService system = admin.getConnection().systemService();
        List<Network> networkList = system.networksService().list().send().networks();


        return networkList.stream()
                .map(network -> {
                    List<NetworkLabel> nlList = system.networksService().networkService(network.id()).networkLabelsService().list().send().labels();
                    OpenStackNetworkProvider provider = system.openstackNetworkProvidersService().providerService(network.externalProvider().id()).get().send().provider();

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
                            .providerId(network.externalProviderPresent() ? provider.id() : null)
                            .providerName(network.externalProviderPresent() ? provider.name() : null)
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
        DataCenter dataCenter = system.dataCentersService().dataCenterService(ncVo.getDatacenterId()).get().send().dataCenter();
//        OpenStackNetworkProvider openStackNetworkProvider = systemService.openstackNetworkProvidersService().list().send().providers().get(0);

        try {
            NetworkBuilder networkBuilder = new NetworkBuilder();
            networkBuilder
                    .id(ncVo.getId())
                    .name(ncVo.getName())
                    .description(ncVo.getDescription())
                    .comment(ncVo.getComment())
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

        // 네트워크가 비가동 중인지 확인
        boolean cDelete = clusterList.stream()
                            .flatMap(cluster -> system.clustersService().clusterService(cluster.id()).networksService().list().send().networks().stream())
                            .filter(network -> network.id().equals(id))
                            .noneMatch(network -> network.status().equals(NetworkStatus.OPERATIONAL));

        // 삭제 가능한 경우 네트워크를 삭제하고 성공 응답을 반환합니다
        if (cDelete) {
            networkService.remove().send();
            log.info("network 삭제");
            return CommonVo.successResponse();
        } else {
            log.error("network 삭제 실패");
            return CommonVo.failResponse("오류");
        }
    }


    // 가져오기 출력 창
    @Override
    public NetworkImportVo setImportNetwork() {
        SystemService system = admin.getConnection().systemService();

        OpenStackNetworkProvider osProvider = system.openstackNetworkProvidersService().list().follow("networks").send().providers().get(0);
        List<DataCenter> dcList = system.dataCentersService().list().send().dataCenters();
        List<Network> nwList = system.networksService().list().send().networks();

        nwList.stream()
                .filter(network -> network.externalProviderPresent() && network.externalProvider().id().equals(osProvider.id()) && network.externalProviderPhysicalNetworkPresent())
                .map(Identified::name)
                .forEach(System.out::println);

        return NetworkImportVo.builder()
                .id(osProvider.id())
                .name(osProvider.name())
                .osVoList(
                    osProvider.networks().stream()
                            .map(os -> {


                                return OpenstackVo.builder()
                                        .id(os.id())
                                        .name(os.name())
//                                        .dcId()
//                                        .dcName()
                                        .dcVoList(
                                                dcList.stream()
                                                        .map(dc -> DataCenterVo.builder()
                                                                .id(dc.id())
                                                                .name(dc.name())
                                                                .build()
                                                        )
                                                        .collect(Collectors.toList())
                                        )
                                        .permission(true)   // 기본값을 허용으로 설정
                                        .build();
                            })
                            .collect(Collectors.toList())
                )
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





    // 일반
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
                            .dcId(network.dataCenter().id())
                            .dcName(dataCenter.name())
                            .version(dataCenter.version().major() + "." + dataCenter.version().minor())
                            .passThrough(TypeExtKt.findVnicPass(vnicProfile.passThrough().mode()))
                            .portMirroring(vnicProfile.portMirroring())
                            .networkFilterName(vnicProfile.networkFilterPresent() ? system.networkFiltersService().networkFilterService(vnicProfile.networkFilter().id()).get().send().networkFilter().name() : null)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // vnic 생성 창
    // TODO qos는 제외항목, 네트워크필터도 vdsm으로 고정
    //  통과 기능 활성화시 네트워크필터 기능 삭제, 마이그레이션 버튼 활성화, 페일오버 활성화, 포트미러링 비활성화
    //  사용자정의 속성 애매
    @Override
    public VnicCreateVo setVnic(String id) {
        SystemService system = admin.getConnection().systemService();
        Network network = system.networksService().networkService(id).get().send().network();

        return VnicCreateVo.builder()
                .dcId(network.dataCenter().id())
                .dcName(system.dataCentersService().dataCenterService(network.dataCenter().id()).get().send().dataCenter().name())
                .networkId(network.id())
                .networkName(network.name())
//                .nfVo(NetworkFilterVo.builder().build())
                // 프론트에서 네트워크 기본값 지정
                .migration(true)    // TODO 기본값?
                .build();
    }


    // vnic 생성
    @Override
    public CommonVo<Boolean> addVnic(String id, VnicCreateVo vcVo) {
        SystemService system = admin.getConnection().systemService();
        AssignedVnicProfilesService aVnicsService = system.networksService().networkService(id).vnicProfilesService();
        List<VnicProfile> vpList = system.networksService().networkService(id).vnicProfilesService().list().send().profiles();

        boolean duplicateName = vpList.stream().anyMatch(vnicProfile -> vnicProfile.name().equals(vcVo.getName()));

        try{
            // 같은 network내 vnic이름 중복 x, 다르면 중복 o
            if(!duplicateName) {
                VnicProfileBuilder vnicBuilder = new VnicProfileBuilder();
                vnicBuilder
                        .network(new NetworkBuilder().id(id).build())
                        .name(vcVo.getName())
                        .description(vcVo.getDescription())
                        // 네트워크 필터 기본생성됨
                        .passThrough(new VnicPassThroughBuilder().mode(vcVo.getPassThrough()).build())
                        .migratable(vcVo.isMigration())
                        .build();

                aVnicsService.add().profile(vnicBuilder).send().profile();

                log.info("네트워크 vnic 생성");
                return CommonVo.successResponse();
            }else {
                log.error("vnic 이름 중복");
                return CommonVo.failResponse("vnic 이름 중복");
            }
        }catch (Exception e){
            log.error("error");
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }

    // vnic 편집창
    @Override
    public VnicCreateVo setEditVnic(String id, String vcId) {
        SystemService system = admin.getConnection().systemService();
        DataCenter dataCenter = system.networksService().networkService(id).get().send().network().dataCenter();
        VnicProfile vnicProfile = system.networksService().networkService(id).vnicProfilesService().profileService(vcId).get().send().profile();

        log.info("vnic 프로파일 편집");
        return VnicCreateVo.builder()
                .dcId(dataCenter.id())
                .dcName(dataCenter.name())
                .networkId(vnicProfile.network().id())
                .networkName(system.networksService().networkService(vnicProfile.network().id()).get().send().network().name())
                .id(vcId)
                .name(vnicProfile.name())
                .description(vnicProfile.description())
                .passThrough(vnicProfile.passThrough().mode())
                .migration(!vnicProfile.migratablePresent() || vnicProfile.migratable())
                .portMirror(vnicProfile.portMirroring())
                .nfName(system.networkFiltersService().networkFilterService(vnicProfile.networkFilter().id()).get().send().networkFilter().name())
                .build();
    }

    // vnic 편집
    @Override
    public CommonVo<Boolean> editVnic(String id, String vcId, VnicCreateVo vcVo) {
        SystemService system = admin.getConnection().systemService();
        VnicProfileService vnicService = system.vnicProfilesService().profileService(vcId);

        try{
            VnicProfileBuilder vnicBuilder = new VnicProfileBuilder();
            vnicBuilder
                    .name(vcVo.getName())
                    .description(vcVo.getDescription())
                    .passThrough(new VnicPassThroughBuilder().mode(vcVo.getPassThrough()).build())
                    .migratable(vcVo.isMigration())
                    .portMirroring(vcVo.isPortMirror())
                    .build();

            vnicService.update().profile(vnicBuilder).send().profile();

            log.info("네트워크 Vnic 편집성공");
            return CommonVo.successResponse();
        }catch (Exception e){
            log.error("error");
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }
    
    // vnic 삭제
    @Override
    public CommonVo<Boolean> deleteVnic(String id, String vcId) {
        SystemService system = admin.getConnection().systemService();
        VnicProfileService vnicService = system.vnicProfilesService().profileService(vcId);

        try {
            vnicService.remove().send();
            log.info("네트워크 Vnic 삭제 성공");
            return CommonVo.successResponse();
        }catch (Exception e){
            log.error("error");
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }



    // 클러스터 목록
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

    // 클러스터 네트워크 관리 창
    @Override
    public NetworkUsageVo getUsage(String id, String cId) {
        SystemService system = admin.getConnection().systemService();
        Network network = system.clustersService().clusterService(cId).networksService().networkService(id).get().send().network();

        // 모두 할당? 모두 필요?
        return NetworkUsageVo.builder()
                .vm(network.usages().contains(NetworkUsage.VM))
                .display(network.usages().contains(NetworkUsage.DISPLAY))
                .migration(network.usages().contains(NetworkUsage.MIGRATION))
                .management(network.usages().contains(NetworkUsage.MANAGEMENT))
                .defaultRoute(network.usages().contains(NetworkUsage.DEFAULT_ROUTE))
                .gluster(network.usages().contains(NetworkUsage.GLUSTER))
                .build();
    }

    // 네트워크 관리
    // 모두 할당, 모두 필요 / 관리 기능 활성화시 추가만 가능한거 같음 / 나머지는 선택가능
    @Override
    public CommonVo<Boolean> editUsage(String id, String cId, NetworkUsageVo nuVo) {
        SystemService system = admin.getConnection().systemService();



        // 클러스터 모두연결이 선택되어야지만 모두 필요가 선택됨
//        ncVo.getClusterVoList().stream()
//                .filter(NetworkClusterVo::isConnected) // 연결된 경우만 필터링
//                .forEach(networkClusterVo -> {
//                    ClusterNetworksService clusterNetworksService = system.clustersService().clusterService(networkClusterVo.getId()).networksService();
//                    clusterNetworksService.add().network(
//                            new NetworkBuilder()
//                                    .id(network.id())
//                                    .required(networkClusterVo.isRequired())
//                    ).send().network();
//                });


        return null;
    }

    // 호스트 목록
    @Override
    public List<NetworkHostVo> getHost(String id) {
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
                                        .rxSpeed(commonService.getSpeed(statisticList, "data.current.rx.bps"))
                                        .txSpeed(commonService.getSpeed(statisticList, "data.current.tx.bps"))
                                        .rxTotalSpeed(commonService.getSpeed(statisticList, "data.total.rx"))
                                        .txTotalSpeed(commonService.getSpeed(statisticList, "data.total.tx"));
                                }
                                return builder.build();
                            });
                })
                .collect(Collectors.toList());
    }


    // 가상머신 목록
    @Override
    public List<NetworkVmVo> getVm(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Vm> vmList = system.vmsService().list().send().vms();

        return vmList.stream()
                .flatMap(vm -> {
                    List<Nic> nicList = system.vmsService().vmService(vm.id()).nicsService().list().send().nics();
                    List<ReportedDevice> rdList = system.vmsService().vmService(vm.id()).reportedDevicesService().list().send().reportedDevice();

                    return nicList.stream()
                            .filter(nic -> {
                                VnicProfile vnicProfile = system.vnicProfilesService().profileService(nic.vnicProfile().id()).get().send().profile();
                                return vnicProfile.network().id().equals(id);
                            })
                            .map(nic -> {
                                List<Statistic> statisticList = system.vmsService().vmService(vm.id()).nicsService().nicService(nic.id()).statisticsService().list().send().statistics();

                                NetworkVmVo.NetworkVmVoBuilder builder = NetworkVmVo.builder()
                                        .vmId(vm.id())
                                        .vmName(vm.name())
                                        .status(vm.statusPresent() ? vm.status() : null)
                                        .fqdn(vm.fqdn())
                                        .clusterName(system.clustersService().clusterService(vm.cluster().id()).get().send().cluster().name())
                                        .description(vm.description())
                                        .vnicStatus(nic.linked())
                                        .vnicId(nic.id())
                                        .vnicName(nic.name())
                                        .vnicRx(vm.status() == VmStatus.UP ? commonService.getSpeed(statisticList, "data.current.rx.bps") : null)
                                        .vnicTx(vm.status() == VmStatus.UP ? commonService.getSpeed(statisticList, "data.current.tx.bps") : null)
                                        .rxTotalSpeed(vm.status() == VmStatus.UP ? commonService.getSpeed(statisticList, "data.total.rx") : null)
                                        .txTotalSpeed(vm.status() == VmStatus.UP ? commonService.getSpeed(statisticList, "data.total.tx") : null);

                                if (vm.status() == VmStatus.UP) {
                                    rdList.stream()
                                        .filter(ReportedDevice::ipsPresent)
                                        .forEach(rd -> builder
                                            .ipv4(rd.ips().get(0).address())
                                            .ipv6(rd.ips().get(1).address())
                                        );
                                }
                                return builder.build();
                            });
                })
                .collect(Collectors.toList());
    }

    // 가상머신 nic 제거
    @Override
    public CommonVo<Boolean> deleteVmNic(String id, String vmId, String nicId) {
        // DELETE /ovirt-engine/api/vms/123/nics/456
        SystemService system = admin.getConnection().systemService();
        VmNicService vmNicService = system.vmsService().vmService(vmId).nicsService().nicService(nicId);

        try{
            vmNicService.remove().send();
            return CommonVo.successResponse();

        }catch (Exception e){
            log.error("error");
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }

    // 템플릿 목록
    @Override
    public List<NetworkTemplateVo> getTemplate(String id) {
        SystemService system = admin.getConnection().systemService();
        List<Template> templateList = system.templatesService().list().send().templates();

        return templateList.stream()
                .flatMap(template -> {
                    List<Nic> nicList = system.templatesService().templateService(template.id()).nicsService().list().send().nics();

                    return nicList.stream()
                            .filter(nic -> {
                                VnicProfile vp = system.vnicProfilesService().profileService(nic.vnicProfile().id()).get().send().profile();
                                return vp.networkPresent() && vp.network().id().equals(id);
                            })
                            .map(nic -> 
                                    NetworkTemplateVo.builder()
                                        .name(template.name())
                                        .status(template.status())
                                        .clusterName(system.clustersService().clusterService(template.cluster().id()).get().send().cluster().name())
                                        .nicId(nic.id())
                                        .nicName(nic.name())
                                        .build()
                            );
                })
                .collect(Collectors.toList());
    }


    // 템플릿 nic 제거
    @Override
    public CommonVo<Boolean> deleteTempNic(String id, String tempId, String nicId) {
        SystemService system = admin.getConnection().systemService();
        TemplateNicService tnService = system.templatesService().templateService(tempId).nicsService().nicService(nicId);

        try {
            tnService.remove().send();
            log.info("템플릿 nic 제거");
            return CommonVo.successResponse();
        }catch (Exception e){
            log.error("error");
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
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





}
