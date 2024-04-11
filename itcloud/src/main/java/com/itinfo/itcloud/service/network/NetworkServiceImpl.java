package com.itinfo.itcloud.service.network;

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

    // 네트워크 목록
    @Override
    public List<NetworkVo> getList() {
        SystemService system = admin.getConnection().systemService();

        List<Network> networkList = system.networksService().list().send().networks();

        return networkList.stream()
                .map(network -> {
                    List<NetworkLabel> nlList = system.networksService().networkService(network.id()).networkLabelsService().list().send().labels();
                    String label = nlList.stream()
                            .map(NetworkLabel::id)
                            .findFirst()
                            .orElse(null);

                    String providerId = "";
                    String providerName = "";
                    if (network.externalProviderPresent()) {
                        OpenStackNetworkProvider np = system.openstackNetworkProvidersService().providerService(network.externalProvider().id()).get().send().provider();
                        providerId = np.id();
                        providerName = np.name();
                    }

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
                            .label(label)
                            .providerId(providerId)
                            .providerName(providerName)
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
    @Override
    public CommonVo<Boolean> addNetwork(NetworkCreateVo ncVo) {
        SystemService system = admin.getConnection().systemService();

        NetworksService networksService = system.networksService();
        DataCenter dataCenter = system.dataCentersService().dataCenterService(ncVo.getDatacenterId()).get().send().dataCenter();
        OpenStackNetworkProvider openStackNetworkProvider = system.openstackNetworkProvidersService().list().send().providers().get(0);
//        List<Cluster> clusterList = system.dataCentersService().dataCenterService(ncVo.getDatacenterId()).clustersService().list().send().clusters();

        List<String> dnsList = ncVo.getDns().stream()
                .distinct()
                .map(NetworkDnsVo::getDnsIp)
                .collect(Collectors.toList());

        try {
            NetworkBuilder networkBuilder = new NetworkBuilder();
            networkBuilder
                    .name(ncVo.getName())
                    .description(ncVo.getDescription())
                    .comment(ncVo.getComment())
                    .portIsolation(ncVo.getPortIsolation())
                    .usages(ncVo.getUsageVm() ? NetworkUsage.VM : NetworkUsage.DEFAULT_ROUTE)
                    .mtu(ncVo.getMtu())
                    .dnsResolverConfiguration(      // TODO DNS 생성 오류, 이슈남기기
                            ncVo.getDns() != null ? new DnsResolverConfigurationBuilder().nameServers(dnsList) : null
                    )
                    .stp(ncVo.getStp())
                    .vlan(ncVo.getVlan() != null ? new VlanBuilder().id(ncVo.getVlan()) : null)
                    // TODO 외부 공급자 설정할 때 물리적 네트워크에 연결하는 거 필요, 외부 공급자 설정 시 클러스터에서 모두 필요 항목은 사라져야됨 (프론트에서 아예 설정이 안되게?)
                    .externalProvider(ncVo.getExternalProvider() ? openStackNetworkProvider : null)
                    .dataCenter(dataCenter);
//                    .build();

            Network network = networksService.add().network(networkBuilder).send().network();


            // TODO: vnicprofile도 문제 있음, 되기는 한데 기본생성이 사라지지 않음
            ncVo.getVnics().forEach(vnicProfileVo -> {
                AssignedVnicProfilesService aVnicsService = system.networksService().networkService(network.id()).vnicProfilesService();
                aVnicsService.add().profile(new VnicProfileBuilder().name(vnicProfileVo.getName()).build()).send().profile();
            });

            // 클러스터 모두연결이 선택되어야지만 모두 필요가 선택됨
            ncVo.getClusterVoList().stream()
                    .filter(NetworkClusterVo::isConnected) // 연결된 경우만 필터링
                    .forEach(networkClusterVo -> {
                        ClusterNetworksService clusterNetworksService = system.clustersService().clusterService(networkClusterVo.getId()).networksService();
                        clusterNetworksService.add()
                                .network(new NetworkBuilder().id(network.id()).required(networkClusterVo.isRequired()))
                                .send().network();
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
            return CommonVo.failResponse(e.getMessage());
        }
    }

    // 네트워크 편집 창
    // TODO
    @Override
    public NetworkCreateVo setEditNetwork(String id) {
        SystemService system = admin.getConnection().systemService();
        Network network = system.networksService().networkService(id).get().send().network();

        return NetworkCreateVo.builder()
                .name(network.name())
                .description(network.description())
                .comment(network.comment())
//                .label()
                .vlan(network.vlan().idAsInteger())
                .usageVm(network.usages().contains(NetworkUsage.VM))
                .mtu(network.mtu().intValue())

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
        List<NetworkClusterVo> ncVoList = new ArrayList<>();
        NetworkClusterVo ncVo = null;

//        return clusterList.stream()
//                .map(cluster -> {
//                    List<Network> networkList = system.clustersService().clusterService(cluster.id()).networksService().list().send().networks();
//
//                    return networkList.stream()
//                            .filter(network -> network.id().equals(id))
//                            .map(network ->
//                                NetworkClusterVo.builder()
//                                    .id(cluster.id())
//                                    .name(cluster.name())
//                                    .version(cluster.version().major() + "." + cluster.version().minor())
//                                    .description(cluster.description())
//                                    .status(network.status().value())
//                                    .required(network.required())
//                                    .networkUsageVo(
//                                        NetworkUsageVo.builder()
//                                            .vm(network.usages().contains(NetworkUsage.VM))
//                                            .display(network.usages().contains(NetworkUsage.DISPLAY))
//                                            .migration(network.usages().contains(NetworkUsage.MIGRATION))
//                                            .management(network.usages().contains(NetworkUsage.MANAGEMENT))
//                                            .defaultRoute(network.usages().contains(NetworkUsage.DEFAULT_ROUTE))
//                                            .gluster(network.usages().contains(NetworkUsage.GLUSTER))
//                                            .build()
//                                    )
//                                .build()
//                            )
//                            .collect(Collectors.toList());
//                })
//                .collect(Collectors.toList());

//        for(Cluster cluster : clusterList){
//            List<Network> networkList = system.clustersService().clusterService(cluster.id()).networksService().list().send().networks();
//
//            for(Network network : networkList){
//                if(network.id().equals(id)){
//                    ncVo = NetworkClusterVo.builder()
//                            .id(cluster.id())
//                            .name(cluster.name())
//                            .version(cluster.version().major() + "." + cluster.version().minor())
//                            .description(cluster.description())
//                            .status(network.status().value())
//                            .required(network.required())
//                            .networkUsageVo( NetworkUsageVo.builder()
//                                                .vm(network.usages().contains(NetworkUsage.VM))
//                                                .display(network.usages().contains(NetworkUsage.DISPLAY))
//                                                .migration(network.usages().contains(NetworkUsage.MIGRATION))
//                                                .management(network.usages().contains(NetworkUsage.MANAGEMENT))
//                                                .defaultRoute(network.usages().contains(NetworkUsage.DEFAULT_ROUTE))
//                                                .gluster(network.usages().contains(NetworkUsage.GLUSTER))
//                                                .build()
//                            )
//                            .build();
//
//                    ncVoList.add(ncVo);
//                }
//            }
//        }
//        return ncVoList;
        return null;
    }

    @Override
    public List<NetworkHostVo> getHost(String id) {
        SystemService system = admin.getConnection().systemService();

        List<Host> hostList = system.hostsService().list().send().hosts();
        List<NetworkHostVo> nhVoList = new ArrayList<>();
        NetworkHostVo nhVo = null;
        DecimalFormat df = new DecimalFormat("###,###");

        for(Host host : hostList) {
            List<NetworkAttachment> naList = system.hostsService().hostService(host.id()).networkAttachmentsService().list().send().attachments();

            for (NetworkAttachment na : naList) {
                if (na.networkPresent() && na.network().id().equals(id)) {
                    nhVo = new NetworkHostVo();

                    nhVo.setHostId(host.id());
                    nhVo.setHostName(host.name());
                    nhVo.setHostStatus(host.status().value());

                    Cluster c = system.clustersService().clusterService(host.cluster().id()).get().send().cluster();
                    nhVo.setClusterName(c.name());
                    nhVo.setDatacenterName(system.dataCentersService().dataCenterService(c.dataCenter().id()).get().send().dataCenter().name());

                    List<HostNic> nicList = system.hostsService().hostService(host.id()).nicsService().list().send().nics();
                    for (HostNic hostNic : nicList) {
                        nhVo.setNetworkStatus(hostNic.status().value());
//                nhVo.setAsynchronism(hostNic.a);
                        nhVo.setNetworkDevice(hostNic.name());

                        List<Statistic> statisticList = system.hostsService().hostService(host.id()).nicsService().nicService(hostNic.id()).statisticsService().list().send().statistics();
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
        SystemService system = admin.getConnection().systemService();

        List<Vm> vmList = system.vmsService().list().send().vms();
        List<NetworkVmVo> nVmVoList = new ArrayList<>();
        NetworkVmVo nVmVo = null;

        for(Vm vm : vmList){
            List<Nic> nicList = system.vmsService().vmService(vm.id()).nicsService().list().send().nics();

            for(Nic nic : nicList){
                VnicProfile vnicProfile = system.vnicProfilesService().profileService(nic.vnicProfile().id()).get().send().profile();

                if(vnicProfile.network().id().equals(id)){
                    nVmVo = new NetworkVmVo();
                    nVmVo.setVmId(vm.id());
                    nVmVo.setStatus(vm.statusPresent() ? vm.status().value() : null);
                    System.out.println(vm.status().value());
                    nVmVo.setVmName(vm.name());
                    nVmVo.setFqdn(vm.fqdn());
                    nVmVo.setClusterName(system.clustersService().clusterService(vm.cluster().id()).get().send().cluster().name() );
                    nVmVo.setDescription(vm.description());

                    nVmVo.setVnicStatus(nic.linked());
                    nVmVo.setVnicName(nic.name());

                    if(vm.status().value().equals("up")) {
                        List<ReportedDevice> reportedDeviceList = system.vmsService().vmService(vm.id()).reportedDevicesService().list().send().reportedDevice();
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
                        List<Statistic> statisticList = system.vmsService().vmService(vm.id()).nicsService().nicService(nic.id()).statisticsService().list().send().statistics();

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
//        SystemService systemService = admin.getConnection().systemService();
//
//        List<Template> templateList = systemService.templatesService().list().send().templates();
        List<TemplateVo> tVoList = new ArrayList<>();
//        TemplateVo tVo = null;
//
//        for(Template template : templateList){
//            if(template.nicsPresent()){
//                tVo = new TemplateVo();
//
//                tVo.setId(template.id());
//                tVo.setName(template.name());
//
//                tVoList.add(tVo);
//            }
//        }

        return tVoList;
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
}
