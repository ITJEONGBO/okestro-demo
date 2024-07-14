package com.itinfo.itcloud.service.computing.impl;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.computing.AffinityGroupVo;
import com.itinfo.itcloud.model.computing.AffinityLabelVo;
import com.itinfo.itcloud.model.create.AffinityGroupCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.computing.ItAffinityService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.builders.*;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.AffinityGroup;
import org.ovirt.engine.sdk4.types.AffinityLabel;
import org.ovirt.engine.sdk4.types.Host;
import org.ovirt.engine.sdk4.types.Vm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AffinityServiceImpl implements ItAffinityService {
    @Autowired private AdminConnectionService admin;

    /**
     * Affinity 생성시, 해당 클러스터에 있는 모든 호스트 출력
     * @param clusterId 클러스터 아이디 비교
     * @return 호스트 리스트
     */
    @Override
    public List<IdentifiedVo> getHostList(String clusterId){
        SystemService system = admin.getConnection().systemService();
        return system.hostsService().list().send().hosts().stream()
                .filter(host -> host.cluster().id().equals(clusterId))
                .map(host -> IdentifiedVo.builder().id(host.id()).name(host.name()).build())
                .collect(Collectors.toList());
    }

    /**
     * Affinity 생성시, 해당 클러스터에 있는 모든 가상머신 출력
     * @param clusterId 클러스터 아이디 비교
     * @return 가상머신 리스트
     */
    @Override
    public List<IdentifiedVo> getVmList(String clusterId){
        SystemService system = admin.getConnection().systemService();
        return system.vmsService().list().send().vms().stream()
                .filter(vm -> vm.cluster().id().equals(clusterId))
                .map(vm ->
                        IdentifiedVo.builder()
                                .id(vm.id())
                                .name(vm.name())
                                .build()
                )
                .collect(Collectors.toList());
    }

    /***
     * Affinity Group 생성 시 사용
     * front에서 가상머신 레이블과 호스트 레이블 버튼을 새로 생성하는 방식으로 하면 될듯
     * dc나 cluster의 id와 관계없음
     * @return AffinityLabel 전체 목록
     */
    @Override
    public List<IdentifiedVo> getLabelList() {
        SystemService system = admin.getConnection().systemService();
        return system.affinityLabelsService().list().send().labels().stream()
                .map(affinityLabel ->
                        IdentifiedVo.builder()
                                .id(affinityLabel.id())
                                .name(affinityLabel.name())
                                .build()
                )
                .collect(Collectors.toList());
    }


    //--------------------------------------------

    /**
     *  선호도 그룹 목록 출력 [ cluster, vm ]
     * @param id    cluster/vm id가 들어간다
     * @param type  cluster/vm 인지 default는 vm
     * @return 선호도 그룹 목록
     */
    // 선호도 그룹 목록 - cluster, vm

    // TODO:HELP clusterId를 쿠키에?
    @Override
    public List<AffinityGroupVo> getAffinitygroup(String id, String type){
        SystemService system = admin.getConnection().systemService();
        List<AffinityGroup> agList;

        if("cluster".equals(type)) {
            agList = system.clustersService().clusterService(id).affinityGroupsService().list().send().groups();
        }else { // vm
            String clusterId = system.vmsService().vmService(id).get().send().vm().cluster().id();
            agList = system.clustersService().clusterService(clusterId).affinityGroupsService().list().send().groups().stream()
                    .filter(ag -> ag.vmsPresent() && ag.vms().stream().anyMatch(vm1 -> vm1.id().equals(id)))
                    .collect(Collectors.toList());
        }
        
        log.info("cluster".equals(type) ? "클러스터 선호도그룹 목록" : "가상머신 선호도그룹 목록");
        return agList.stream()
                .map(ag ->
                        AffinityGroupVo.builder()
                            .id(ag.id())
                            .name(ag.name())
                            .description(ag.description())
                            .status(ag.broken())
                            .priority(ag.priority().intValue())  // 우선순위
                            .positive(ag.positivePresent() && ag.positive())
                            .vmEnabled(ag.vmsRule().enabled())
                            .vmPositive(ag.vmsRule().positive())
                            .vmEnforcing(ag.vmsRule().enforcing())
                            .hostEnabled(ag.hostsRule().enabled())
                            .hostPositive(ag.hostsRule().positive())
                            .hostEnforcing(ag.hostsRule().enforcing())
                            .hostLabels(setEdit(system, ag, "hostLabels"))
                            .vmLabels(setEdit(system, ag, "vmLabels"))
                            .hostMembers(setEdit(system, ag, "hosts"))
                            .vmMembers(setEdit(system, ag, "vms"))
                            .build()
                )
                .collect(Collectors.toList());
    }




    /**
     * 선호도 그룹 생성 (cluster, vm) 상관없이
     * @param id
     * @param type
     * @param agVo
     * @return
     */
    @Override
    public CommonVo<Boolean> addAffinitygroup(String id,  String type, AffinityGroupCreateVo agVo) {
        SystemService system = admin.getConnection().systemService();
        String clusterId = "cluster".equals(type) ? id : system.vmsService().vmService(id).get().send().vm().cluster().id();
        AffinityGroupsService agsService = system.clustersService().clusterService(clusterId).affinityGroupsService();

        // 선호도 그룹 이름 중복검사
        boolean duplicateName = agsService.list().send().groups().stream().anyMatch(ag -> ag.name().equals(agVo.getName()));

        try {
            if(duplicateName) {
                log.error("실패: 선호도그룹 이름 중복");
                return CommonVo.duplicateResponse();
            }

            AffinityGroupBuilder ag = new AffinityGroupBuilder()
                    .name(agVo.getName())
                    .description(agVo.getDescription())
                    .cluster(new ClusterBuilder().id(agVo.getClusterId()).build())
                    .priority(agVo.getPriority())
                    .vmsRule(new AffinityRuleBuilder()
                            .enabled(agVo.isVmEnabled())    // 비활성화
                            .positive(agVo.isVmPositive())  // 양극 음극
                            .enforcing(agVo.isVmEnforcing()) // 강제 적용
                    )
                    .hostsRule(new AffinityRuleBuilder()
                            .enabled(agVo.isHostEnabled())
                            .positive(agVo.isHostPositive())
                            .enforcing(agVo.isHostEnforcing())
                    );

            if (agVo.getHostLabels() != null) {
                ag.hostLabels(
                        agVo.getHostLabels().stream()
                                .map(al -> new AffinityLabelBuilder().id(al.getId()).build())
                                .collect(Collectors.toList())
                );
            }
            if (agVo.getVmLabels() != null) {
                ag.vmLabels(
                        agVo.getVmLabels().stream()
                                .map(al -> new AffinityLabelBuilder().id(al.getId()).build())
                                .collect(Collectors.toList())
                );
            }
            if (agVo.getHostList() != null) {
                ag.hosts(agVo.getHostList().stream()
                        .map(host -> new HostBuilder().id(host.getId()).build())
                        .collect(Collectors.toList())
                );
            }
            if (agVo.getVmList() != null) {
                ag.vms(agVo.getVmList().stream()
                        .map(vm -> new VmBuilder().id(vm.getId()).build())
                        .collect(Collectors.toList())
                );
            }

            AffinityGroup affinityGroup = agsService.add().group(ag.build()).send().group();

            log.info("cluster".equals(type) ? "클러스터 선호도그룹 생성" : "가상머신 선호도그룹 생성");
            return CommonVo.createResponse();
        } catch (Exception e) {
            log.error("실패: 선호도그룹 생성");
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }


    /**
     * 선호도 그룹 편집 창
     * @param id    cluster/vm ID
     * @param type  cluster/vm 인지
     * @param agId  해당 선호도 그룹의 ID
     * @return
     */
    public AffinityGroupCreateVo setEditAffinitygroup(String id, String type, String agId) {
        SystemService system = admin.getConnection().systemService();
        String clusterId = "cluster".equals(type) ? id : system.vmsService().vmService(id).get().send().vm().cluster().id();
        AffinityGroup ag = system.clustersService().clusterService(clusterId).affinityGroupsService().groupService(agId).get().follow("vmlabels,hostlabels,vms,hosts").send().group();

        log.info("cluster".equals(type) ? "클러스터 선호도 그룹 편집창" : "가상머신 선호도 그룹 편집창");
        return AffinityGroupCreateVo.builder()
                .clusterId(clusterId)
                .id(agId)
                .name(ag.name())
                .description(ag.description())
                .priority(ag.priority().intValue())
                .vmEnabled(ag.vmsRule().enabled())// 비활성화
                .vmPositive(ag.vmsRule().positive()) // 양극 음극
                .vmEnforcing(ag.vmsRule().enforcing()) // 강제 적용
                .hostEnabled(ag.hostsRule().enabled())
                .hostPositive(ag.hostsRule().positive())
                .hostEnforcing(ag.hostsRule().enforcing())
                .hostLabels(setEdit(system, ag, "hostLabels"))
                .vmLabels(setEdit(system, ag, "vmLabels"))
                .hostList(setEdit(system, ag, "hosts"))
                .vmList(setEdit(system, ag, "vms"))
                .build();
    }


    // 선호도 그룹 편집
    // TODO:HELP
    @Override
    public CommonVo<Boolean> editAffinitygroup(String id, AffinityGroupCreateVo agVo) {
        SystemService system = admin.getConnection().systemService();
        String agId = agVo.getId();
        AffinityGroupService agService = system.clustersService().clusterService(agVo.getClusterId()).affinityGroupsService().groupService(agId);

        boolean nameDuplicate = system.clustersService().clusterService(id).affinityGroupsService().list().send().groups().stream()
                .filter(ag -> !ag.id().equals(agId))
                .anyMatch(ag -> ag.name().equals(agVo.getName()));

        try {
            if(nameDuplicate) {
                log.error("선호도 그룹 이름 중복");
                return CommonVo.duplicateResponse();
            }

            AffinityGroupBuilder agBuilder = new AffinityGroupBuilder()
                    .id(agId)
                    .name(agVo.getName())
                    .description(agVo.getDescription())
                    .cluster(new ClusterBuilder().id(agVo.getClusterId()).build())
                    .priority(agVo.getPriority())
                    .vmsRule(
                            new AffinityRuleBuilder()
                                    .enabled(agVo.isVmEnabled())    // 비활성화
                                    .positive(agVo.isVmPositive())  // 양극 음극
                                    .enforcing(agVo.isVmEnforcing()) // 강제 적용
                    )
                    .hostsRule(
                            new AffinityRuleBuilder()
                                    .enabled(agVo.isHostEnabled())
                                    .positive(agVo.isHostPositive())
                                    .enforcing(agVo.isHostEnforcing())
                    );

            // 편집시 들어갈 host/vm labels,list
            setHostLabels(system, agBuilder, agVo);
            setVmLabels(system, agBuilder, agVo);
            setMembers(system, agBuilder, agVo);

            AffinityGroup affinityGroup1 = agService.update().group(agBuilder.build()).send().group();
            // TODO 상태

            log.info("Cluster 선호도그룹 편집");
            return CommonVo.createResponse();
            
        } catch (Exception e) {
            log.error("실패: Cluster 선호도그룹 편집");
            e.getMessage();
            return CommonVo.failResponse(e.getMessage());
        }
    }



    // 선호도 그룹 삭제 - clusterId와 agId를 가져와서 삭제
    // 선호도 그룹 내에 항목들이 아예 없어야함
    @Override
    public CommonVo<Boolean> deleteAffinitygroup(String id, String type, String agId) {
        SystemService system = admin.getConnection().systemService();
        String clusterId = "cluster".equals(type) ? id : system.vmsService().vmService(id).get().send().vm().cluster().id();
        AffinityGroupService agService = system.clustersService().clusterService(clusterId).affinityGroupsService().groupService(agId);

        try {
            agService.remove().send();

            log.info("선호도그룹 삭제 ");
            return CommonVo.successResponse();
        } catch (Exception e) {
            log.error("실패: Cluster 선호도그룹 삭제");
            return CommonVo.failResponse(e.getMessage());
        }
    }





    // 선호도 레이블

    // 클러스터에서 선호도 레이블 목록 출력  // 호스트 id
    // 호스트 본인의 id가 있어야 출력됨
    // TODO
    @Override
    public List<AffinityLabelVo> getAffinitylabel(/*String id, String type*/){
        SystemService system = admin.getConnection().systemService();
        List<AffinityLabel> affinityLabelList = system.affinityLabelsService().list().send().labels();

        log.info("선호도 레이블");
        return affinityLabelList.stream()
                .map(al ->
                    AffinityLabelVo.builder()
                        .id(al.id())
                        .name(al.name())
                        .hosts(getAlHost(system, al.id()))
                        .vms(getAlVm(system, al.id()))
                        .build()
                )
                .collect(Collectors.toList());
    }






    // 클러스터에서 가지고 왓ㅇ므요
    // 선호도 레이블 생성
//    @Override
//    public CommonVo<Boolean> addAffinitylabel(String id, AffinityLabelCreateVo alVo) {
//        SystemService system = admin.getConnection().systemService();
//        AffinityLabelsService alServices = system.affinityLabelsService();
//        List<AffinityLabel> alList = system.affinityLabelsService().list().send().labels();
//
//        // 중복이름
//        boolean duplicateName = alList.stream().noneMatch(al -> al.name().equals(alVo.getName()));
//
//        try {
//            if(duplicateName) {
//                log.error("실패: Cluster 선호도레이블 이름 중복");
//                return CommonVo.failResponse("이름 중복");
//            }
//            AffinityLabelBuilder alBuilder = new AffinityLabelBuilder();
//            alBuilder
//                    .name(alVo.getName())
//                    .hosts(
//                            alVo.getHostList().stream()
//                                    .map(host -> new HostBuilder().id(host.getId()).build())
//                                    .collect(Collectors.toList())
//                    )
//                    .vms(
//                            alVo.getVmList().stream()
//                                    .map(vm -> new VmBuilder().id(vm.getId()).build())
//                                    .collect(Collectors.toList())
//                    )
//                    .build();
//
//            alServices.add().label(alBuilder).send().label();
//
//            log.info("Cluster 선호도레이블 생성");
//            return CommonVo.createResponse();
//        } catch (Exception e) {
//            log.error("실패: Cluster 선호도 레이블");
//            e.printStackTrace();
//            return CommonVo.failResponse(e.getMessage());
//        }
//    }
//
//
//        // 선호도 레이블 편집 시 출력창
//        @Override
//        public AffinityLabelCreateVo getAffinityLabel(String id, String alId){   // id는 alid
//            SystemService system = admin.getConnection().systemService();
//            AffinityLabel al = system.affinityLabelsService().labelService(alId).get().follow("hosts,vms").send().label();
//
//            log.info("Cluster 선호도 레이블 편집창");
//            return AffinityLabelCreateVo.builder()
//                    .id(id)
//                    .name(al.name())
//    //                .hostList(al.hostsPresent() ? affinityService.getHostLabelMember(system, alId) : null )
//    //                .vmList(al.vmsPresent() ? affinityService.getVmLabelMember(system, alId) : null)
//                    .build();
//        }
//
//        // 선호도 레이블 - 편집
//        // 이름만 바뀌는거 같음, 호스트하고 vm은 걍 삭제하는 방식으로
//        @Override
//        public CommonVo<Boolean> editAffinitylabel(String id, String alId, AffinityLabelCreateVo alVo) {
//            SystemService system = admin.getConnection().systemService();
//            AffinityLabelService alService = system.affinityLabelsService().labelService(alVo.getId());
//            List<AffinityLabel> alList = system.affinityLabelsService().list().send().labels();
//
//            // 중복이름
//            boolean duplicateName = alList.stream().noneMatch(al -> al.name().equals(alVo.getName()));
//
//            try {
//                AffinityLabelBuilder alBuilder = new AffinityLabelBuilder();
//                alBuilder
//                        .id(alVo.getId())
//                        .name(alVo.getName())
//                        .hosts(
//                                alVo.getHostList().stream()
//                                        .map(host ->
//                                                new HostBuilder()
//                                                        .id(host.getId())
//                                                        .build()
//                                        )
//                                        .collect(Collectors.toList())
//                        )
//                        .vms(
//                                alVo.getVmList().stream()
//                                        .map(vm ->
//                                                new VmBuilder()
//                                                        .id(vm.getId())
//                                                        .build()
//                                        )
//                                        .collect(Collectors.toList())
//                        )
//                        .build();
//
//    //            alVo.getVmList().stream().distinct().forEach(System.out::println);
//
//                alService.update().label(alBuilder).send().label();
//                log.info("Cluster 선호도레이블 편집");
//                return CommonVo.createResponse();
//            } catch (Exception e) {
//                log.error("실패: Cluster 선호도레이블 편집");
//                e.printStackTrace();
//                return CommonVo.failResponse(e.getMessage());
//            }
//        }
//
//
//        // 선호도 레이블 - 삭제하려면 해당 레이블에 있는 가상머신&호스트 멤버 전부 내리고 해야함
//        @Override
//        public CommonVo<Boolean> deleteAffinitylabel(String id, String alId) {
//            SystemService system = admin.getConnection().systemService();
//            AffinityLabelService alService = system.affinityLabelsService().labelService(alId);
//            AffinityLabel affinityLabel = system.affinityLabelsService().labelService(alId).get().follow("hosts,vms").send().label();
//
//            try {
//                if(!affinityLabel.hostsPresent() && !affinityLabel.vmsPresent()) {
//                    alService.remove().send();
//
//                    log.info("Cluster 선호도레이블 삭제");
//                    return CommonVo.successResponse();
//                } else {
//                    log.error("가상머신 혹은 호스트를 삭제하세요");
//                    return CommonVo.failResponse("error");
//                }
//            } catch (Exception e) {
//                log.error("실패: Cluster 선호도레이블 삭제");
//                return CommonVo.failResponse(e.getMessage());
//            }
//        }





    // -------------------------------------------------------------

    // 선호도 그룹 편집창 - host/vm 레이블, host/vm 아이디,이름 출력만
    private List<IdentifiedVo> setEdit(SystemService system, AffinityGroup ag, String type){
        switch (type) {
            case "hostLabels":
                return system.clustersService().clusterService(ag.cluster().id()).affinityGroupsService().groupService(ag.id()).hostLabelsService().list().send().labels().stream()
                        .map(label -> IdentifiedVo.builder()
                                .id(label.id())
                                .name(label.name())
                                .build())
                        .collect(Collectors.toList());
            case "vmLabels":
                return system.clustersService().clusterService(ag.cluster().id()).affinityGroupsService().groupService(ag.id()).vmLabelsService().list().send().labels().stream()
                        .map(label -> IdentifiedVo.builder()
                                .id(label.id())
                                .name(label.name())
                                .build())
                        .collect(Collectors.toList());
            case "hosts":
                return system.clustersService().clusterService(ag.cluster().id()).affinityGroupsService().groupService(ag.id()).hostsService().list().send().hosts().stream()
                        .map(host -> IdentifiedVo.builder()
                                .id(host.id())
                                .name(host.name())
                                .build())
                        .collect(Collectors.toList());
            case "vms":
                return system.clustersService().clusterService(ag.cluster().id()).affinityGroupsService().groupService(ag.id()).vmsService().list().send().vms().stream()
                        .map(vm -> IdentifiedVo.builder()
                                .id(vm.id())
                                .name(vm.name())
                                .build())
                        .collect(Collectors.toList());
            default:
                return Collections.emptyList();
        }
    }



    // Host Labels
    private CommonVo<Boolean> setHostLabels(SystemService system, AffinityGroupBuilder agBuilder, AffinityGroupCreateVo agVo) {
        System.out.println(agVo.getHostLabels().isEmpty()); // true
        if (!agVo.getHostLabels().isEmpty()) {
            try {
                agBuilder.hostLabels(
                    agVo.getHostLabels().stream()
                    .map(label -> new AffinityLabelBuilder().id(label.getId()).build())
                    .collect(Collectors.toList())
                );
                return CommonVo.successResponse();
            }catch (Exception e){
                e.getMessage();
                return CommonVo.failResponse("hostfail");
            }
        } else {
            try {
                List<AffinityLabel> hostLabelList = system.clustersService().clusterService(agVo.getClusterId()).affinityGroupsService().groupService(agVo.getId()).hostLabelsService().list().send().labels();
                AffinityGroupHostLabelsService agHostLabelsService = system.clustersService().clusterService(agVo.getClusterId()).affinityGroupsService().groupService(agVo.getId()).hostLabelsService();
                System.out.println(hostLabelList.size());

                for (AffinityLabel al : hostLabelList) {
                    System.out.println(al.id() + ":" + al.name());
                    // TODO: 지워지지 않음, issue에 올려서 로그 기록 확인 필요
                    agHostLabelsService.labelService(al.id()).remove().send();
                }
//                List<AffinityLabel> _hostLabelList =
                System.out.println(hostLabelList.size());

                return CommonVo.successResponse();
            }catch (Exception e){
                e.getMessage();
                return CommonVo.failResponse("faile");
            }
        }
    }



    // VM Labels
    private CommonVo<Boolean> setVmLabels(SystemService system, AffinityGroupBuilder agBuilder, AffinityGroupCreateVo agVo) {
        if (!agVo.getVmLabels().isEmpty()) {
            try {
                agBuilder.vmLabels(
                    agVo.getVmLabels().stream()
                    .map(label -> new AffinityLabelBuilder().id(label.getId()).build())
                    .collect(Collectors.toList())
                );
                return CommonVo.successResponse();
            }catch (Exception e){
                log.error("vmLabel Fail");
                e.getMessage();
                return CommonVo.failResponse("vmLabel Fail");
            }
        }else{
            try {
                List<AffinityLabel> vmLabelList = system.clustersService().clusterService(agVo.getClusterId()).affinityGroupsService().groupService(agVo.getId()).vmLabelsService().list().send().labels();
                AffinityGroupVmLabelsService affinityGroupVmLabelsService = system.clustersService().clusterService(agVo.getClusterId()).affinityGroupsService().groupService(agVo.getId()).vmLabelsService();
                System.out.println(vmLabelList.size());

                for (AffinityLabel al : vmLabelList) {
                    System.out.println(al.id() + ":" + al.name());
                    affinityGroupVmLabelsService.labelService(al.id()).remove().send();
                }
                System.out.println(vmLabelList.size());
                return CommonVo.successResponse();
            }catch (Exception e){
                e.getMessage();
                return CommonVo.failResponse("fail");
            }
        }
    }


    // 선호도 그룹 수정 시, Host/Vm 멤버 추가&삭제
    private void setMembers(SystemService system, AffinityGroupBuilder agBuilder, AffinityGroupCreateVo agVo) {
        // Host
        if (!agVo.getHostList().isEmpty()) {
            agBuilder.hosts(
                agVo.getHostList().stream()
                    .map(host -> new HostBuilder().id(host.getId()).build())
                    .collect(Collectors.toList())
            );
        } else{
            List<Host> hostList = system.clustersService().clusterService(agVo.getClusterId()).affinityGroupsService().groupService(agVo.getId()).hostsService().list().send().hosts();
            AffinityGroupHostsService agHostsService = system.clustersService().clusterService(agVo.getClusterId()).affinityGroupsService().groupService(agVo.getId()).hostsService();
            for (Host host : hostList) {
                try {
                    agHostsService.hostService(host.id()).remove().send();
                    log.debug("Removed host: " + host.name());
                } catch (Exception e) {
                    log.error("Failed to remove host: " + host.name(), e);
                }
            }
        }

        // VM
        if (!agVo.getVmList().isEmpty()) {
            agBuilder.vms(agVo.getVmList().stream()
                    .map(vm -> new VmBuilder().id(vm.getId()).build())
                    .collect(Collectors.toList()));
        }else {
            List<Vm> vmList = system.clustersService().clusterService(agVo.getClusterId()).affinityGroupsService().groupService(agVo.getId()).vmsService().list().send().vms();
            AffinityGroupVmsService agVmsService = system.clustersService().clusterService(agVo.getClusterId()).affinityGroupsService().groupService(agVo.getId()).vmsService();
            for (Vm vm : vmList) {
                try {
                    agVmsService.vmService(vm.id()).remove().send();
                    log.debug("Removed vm: " + vm.name());
                } catch (Exception e) {
                    log.error("Failed to remove vm: " + vm.name(), e);
                }
            }
        }
    }
















    /**
     * 선호도 레이블에 있는 호스트 출력
     * @param alId 선호도 레이블 아이디
     * @return 선호도 레이블이 가지고 있는 host 리스트
     */
    private List<IdentifiedVo> getAlHost(SystemService system, String alId){
        return system.affinityLabelsService().labelService(alId).hostsService().list().send().hosts().stream()
                .map(host -> {
                    Host h = system.hostsService().hostService(host.id()).get().send().host();
                    return IdentifiedVo.builder()
                            .id(host.id())
                            .name(h.name())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 선호도 레이블에 있는 가상머신 출력
     * @param alId 선호도 레이블 아이디
     * @return 선호도 레이블이 가지고 있는 가상머신 리스트
     */
    private List<IdentifiedVo> getAlVm(SystemService system, String alId){
        return system.affinityLabelsService().labelService(alId).vmsService().list().send().vms().stream()
                .map(vm -> {
                    Vm v = system.vmsService().vmService(vm.id()).get().send().vm();
                    return IdentifiedVo.builder()
                            .id(vm.id())
                            .name(v.name())
                            .build();
                })
                .collect(Collectors.toList());
    }









}
