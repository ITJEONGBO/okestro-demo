package com.itinfo.itcloud.service.computing.impl;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.computing.AffinityGroupMember;
import com.itinfo.itcloud.model.computing.AffinityGroupVo;
import com.itinfo.itcloud.model.computing.AffinityLabelMember;
import com.itinfo.itcloud.model.computing.AffinityLabelVo;
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
     * 클러스터 - 선호도 그룹 목록
     * @param clusterId 클러스터 id
     * @return 선호도 그룹 목록
     */
    @Override
    public List<AffinityGroupVo> getClusterAffinityGroups(String clusterId) {
        SystemService system = admin.getConnection().systemService();
        List<AffinityGroup> agList = setClusterAffinityGroups(system, clusterId);

        log.info("클러스터 선호도그룹 목록");
        return getAffinityGroupList(system, agList);
    }

    /**
     * 가상머신 - 선호도 그룹 목록
     * @param vmId 가상머신 id
     * @return 선호도 그룹 목록
     */
    @Override
    public List<AffinityGroupVo> getVmAffinityGroups(String vmId) {
        SystemService system = admin.getConnection().systemService();
        List<AffinityGroup> agList = setVmAffinityGroups(system, vmId);

        log.info("가상머신 선호도그룹 목록");
        return getAffinityGroupList(system, agList);
    }


    //---------------------생성창-------------------------------

    /**
     * Affinity Group/Label 생성시, 해당 클러스터에 있는 모든 호스트 출력
     * @param clusterId 클러스터 아이디
     * @return 호스트 목록
     */
    @Override
    public List<IdentifiedVo> setHostList(String clusterId){
        SystemService system = admin.getConnection().systemService();
        return system.hostsService().list().send().hosts().stream()
                .filter(host -> host.cluster().id().equals(clusterId))
                .map(host -> IdentifiedVo.builder().id(host.id()).name(host.name()).build())
                .collect(Collectors.toList());
    }

    /**
     * Affinity Group/Label 생성시, 해당 클러스터에 있는 모든 가상머신 출력
     * @param clusterId 클러스터 아이디
     * @return 가상머신 목록
     */
    @Override
    public List<IdentifiedVo> setVmList(String clusterId){
        SystemService system = admin.getConnection().systemService();
        return system.vmsService().list().send().vms().stream()
                .filter(vm -> vm.cluster().id().equals(clusterId))
                .map(vm -> IdentifiedVo.builder().id(vm.id()).name(vm.name()).build())
                .collect(Collectors.toList());
    }

    /***
     * Affinity Group 생성 시 사용
     * front에서 가상머신 레이블과 호스트 레이블 버튼을 새로 생성하는 방식으로 하면 될듯
     * dc나 cluster의 id와 관계없음 -> dc와는 관련있는거 같은데 api에 dc 정보가 없음
     * @return AffinityLabel 목록
     */
    @Override
    public List<IdentifiedVo> setLabelList() {
        SystemService system = admin.getConnection().systemService();
        return system.affinityLabelsService().list().send().labels().stream()
                .map(affinityLabel ->IdentifiedVo.builder().id(affinityLabel.id()).name(affinityLabel.name()).build())
                .collect(Collectors.toList());
    }


    //--------------------------------------------


    /**
     * 선호도 그룹 생성 (cluster, vm) 상관없음
     * @param id cluster id /vm id
     * @param cluster 해당 항목이 cluster 에서 생성되는건지 확인
     * @param agVo 선호도그룹
     * @return 201 / 404
     */
    @Override
    public CommonVo<Boolean> addAffinityGroup(String id,  boolean cluster, AffinityGroupVo agVo) {
        SystemService system = admin.getConnection().systemService();
        String clusterId = cluster ? id : system.vmsService().vmService(id).get().send().vm().cluster().id();
        AffinityGroupsService agsService = system.clustersService().clusterService(clusterId).affinityGroupsService();

        try {
            // 선호도 그룹 이름 중복검사
            if (nameDuplicateAg(system, clusterId, agVo.getName(), null)) {
                log.error("선호도그룹 이름 중복");
                return CommonVo.duplicateResponse();
            }

            AffinityGroupBuilder ag = getAffinityGroupBuilder(agVo);
            getLabels(ag, agVo.getLabels());
            getMembers(ag, agVo.getMembers());

            agsService.add().group(ag.build()).send().group();

            log.info(cluster ? "클러스터 선호도그룹 생성" : "가상머신 선호도그룹 생성");
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
     * @param cluster  cluster/vm 인지
     * @param agId  해당 선호도 그룹의 ID
     * @return
     */
    public AffinityGroupVo setAffinityGroup(String id, boolean cluster, String agId) {
        SystemService system = admin.getConnection().systemService();
        String clusterId = cluster ? id : system.vmsService().vmService(id).get().send().vm().cluster().id();
        AffinityGroup ag = system.clustersService().clusterService(clusterId).affinityGroupsService().groupService(agId).get().follow("vmlabels,hostlabels,vms,hosts").send().group();

        log.info(cluster ? "클러스터 선호도 그룹 편집창" : "가상머신 선호도 그룹 편집창");
        return AffinityGroupVo.builder()
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
                .labels(
                        AffinityLabelMember.builder()
                                .hostLabels(setEdit(system, ag, "hostLabels"))
                                .vmLabels(setEdit(system, ag, "vmLabels"))
                        .build()
                )
                .members(
                        AffinityGroupMember.builder()
                                .hostMembers(setEdit(system, ag, "hosts"))
                                .vmMembers(setEdit(system, ag, "vms"))
                                .build()
                )
                .build();
    }


    // 선호도 그룹 편집
    // TODO:HELP
    @Override
    public CommonVo<Boolean> editAffinityGroup(AffinityGroupVo agVo) {
        SystemService system = admin.getConnection().systemService();
        String agId = agVo.getId();
        AffinityGroupService agService = system.clustersService().clusterService(agVo.getClusterId()).affinityGroupsService().groupService(agId);

        try {
            if (nameDuplicateAg(system, agVo.getClusterId(), agVo.getName(), agId)) {
                log.error("선호도그룹 이름 중복");
                return CommonVo.duplicateResponse();
            }

            AffinityGroupBuilder ag = getAffinityGroupBuilder(agVo);
            ag.id(agId);

            // 편집시 들어갈 host/vm labels,list
            editHostLabels(system, ag, agVo);
            editVmLabels(system, ag, agVo);
            editHostMembers(system, ag, agVo);
            editVmMembers(system, ag, agVo);

            agService.update().group(ag.build()).send().group();

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
    public CommonVo<Boolean> deleteAffinityGroup(String id, boolean cluster, String agId) {
        SystemService system = admin.getConnection().systemService();
        String clusterId = cluster ? id : system.vmsService().vmService(id).get().send().vm().cluster().id();
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
    public List<AffinityLabelVo> getAffinityLabels(){
        SystemService system = admin.getConnection().systemService();
        List<AffinityLabel> affinityLabelList = system.affinityLabelsService().list().send().labels();

        log.info("선호도 레이블");
        return affinityLabelList.stream()
                .map(al ->
                    AffinityLabelVo.builder()
                        .id(al.id())
                        .name(al.name())
                        .members(
                                AffinityGroupMember.builder()
                                        .hostMembers(getAlHost(system, al.id()))
                                        .vmMembers(getAlVm(system, al.id()))
                                        .build()
                        )
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


    /**
     * 선호도 그룹 이름 중복 검사
     * @param system
     * @param clusterId
     * @param name
     * @param agId
     * @return
     */
    private boolean nameDuplicateAg(SystemService system, String clusterId, String name, String agId){
        return system.clustersService().clusterService(clusterId).affinityGroupsService().list().send().groups().stream()
                .filter(ag -> agId == null || !ag.id().equals(agId))
                .anyMatch(ag -> ag.name().equals(name));
    }



    /**
     * 클러스터 선호도 그룹 목록 불러오기
     * @param system
     * @param clusterId 클러스터 id
     * @return 선호도 그룹 목록
     */
    private List<AffinityGroup> setClusterAffinityGroups(SystemService system, String clusterId) {
        return system.clustersService().clusterService(clusterId).affinityGroupsService().list().send().groups();
    }

    /**
     * 가상머신 선호도 그룹 목록 불러오기
     * @param system
     * @param vmId 가상머신 id
     * @return 선호도 그룹 목록
     */
    private List<AffinityGroup> setVmAffinityGroups(SystemService system, String vmId) {
        String clusterId = system.vmsService().vmService(vmId).get().send().vm().cluster().id();
        return setClusterAffinityGroups(system, clusterId).stream()
                .filter(ag -> ag.vmsPresent() && ag.vms().stream().anyMatch(vm -> vm.id().equals(vmId)))
                .collect(Collectors.toList());
    }

    /**
     * 선호도 그룹 목록
     * @param system
     * @param agList 선호도 그룹 목록
     * @return 선호도 그룹 목록
     */
    private List<AffinityGroupVo> getAffinityGroupList(SystemService system, List<AffinityGroup> agList) {
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
                                .members(
                                        AffinityGroupMember.builder()
                                                .hostMembers(setEdit(system, ag, "hosts"))
                                                .vmMembers(setEdit(system, ag, "vms"))
                                                .build()
                                )
                                .labels(
                                        AffinityLabelMember.builder()
                                                .hostLabels(setEdit(system, ag, "hostLabels"))
                                                .vmLabels(setEdit(system, ag, "vmLabels"))
                                                .build()
                                )
                                .build()
                )
                .collect(Collectors.toList());
    }


    /**
     * 선호도 그룹 빌더
     * @param agVo
     * @return
     */
    private AffinityGroupBuilder getAffinityGroupBuilder(AffinityGroupVo agVo){
        AffinityGroupBuilder ag = new AffinityGroupBuilder();
        ag.name(agVo.getName())
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
        return ag;
    }


    /**
     * 선호도 그룹 레이블 생성하는
     * @param ag 선호도 그룹 빌더
     * @param affinityLabelMember 선호도 레이블 내이 레이블
     */
    private void getLabels(AffinityGroupBuilder ag, AffinityLabelMember affinityLabelMember){
        if (affinityLabelMember.getHostLabels() != null) {
            ag.hostLabels(
                    affinityLabelMember.getHostLabels().stream()
                            .map(al -> new AffinityLabelBuilder().id(al.getId()).build())
                            .collect(Collectors.toList())
            );
        }
        if (affinityLabelMember.getVmLabels() != null) {
            ag.vmLabels(
                    affinityLabelMember.getVmLabels().stream()
                            .map(al -> new AffinityLabelBuilder().id(al.getId()).build())
                            .collect(Collectors.toList())
            );
        }
    }


    private void getMembers(AffinityGroupBuilder ag, AffinityGroupMember affinityGroupMember){
        if (affinityGroupMember.getHostMembers() != null) {
            ag.hosts(
                    affinityGroupMember.getHostMembers().stream()
                            .map(host -> new HostBuilder().id(host.getId()).build())
                            .collect(Collectors.toList())
            );
        }
        if (affinityGroupMember.getVmMembers() != null) {
            ag.vms(
                    affinityGroupMember.getVmMembers().stream()
                            .map(vm -> new VmBuilder().id(vm.getId()).build())
                            .collect(Collectors.toList())
            );
        }
    }




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
    private CommonVo<Boolean> editHostLabels(SystemService system, AffinityGroupBuilder agBuilder, AffinityGroupVo agVo) {
        AffinityGroupHostLabelsService agHostLabelsService = system.clustersService().clusterService(agVo.getClusterId()).affinityGroupsService().groupService(agVo.getId()).hostLabelsService();
        if (!agVo.getLabels().getHostLabels().isEmpty()) {
            try {
                agBuilder.hostLabels(
                    agVo.getLabels().getHostLabels().stream()
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
                List<AffinityLabel> hostLabelList = agHostLabelsService.list().send().labels();

                for (AffinityLabel al : hostLabelList) {
                    System.out.println(al.id() + ":" + al.name());
                    // TODO: 지워지지 않음, issue에 올려서 로그 기록 확인 필요
                    agHostLabelsService.labelService(al.id()).remove().send();
                }
                return CommonVo.successResponse();
            }catch (Exception e){
                log.error(e.getMessage());
                return CommonVo.failResponse("fail");
            }
        }
    }



    // VM Labels
    private CommonVo<Boolean> editVmLabels(SystemService system, AffinityGroupBuilder agBuilder, AffinityGroupVo agVo) {
        AffinityGroupVmLabelsService agVmLabelsService = system.clustersService().clusterService(agVo.getClusterId()).affinityGroupsService().groupService(agVo.getId()).vmLabelsService();
        if (!agVo.getLabels().getVmLabels().isEmpty()) {
            try {
                agBuilder.vmLabels(
                    agVo.getLabels().getVmLabels().stream()
                    .map(label -> new AffinityLabelBuilder().id(label.getId()).build())
                    .collect(Collectors.toList())
                );
                return CommonVo.successResponse();
            }catch (Exception e){
                log.error("vmLabel Fail, {}", e.getMessage());
                return CommonVo.failResponse("vmLabel Fail");
            }
        }else{
            try {
                List<AffinityLabel> vmLabelList = agVmLabelsService.list().send().labels();

                for (AffinityLabel al : vmLabelList) {
                    agVmLabelsService.labelService(al.id()).remove().send();
                }
                return CommonVo.successResponse();
            }catch (Exception e){
                log.error(e.getMessage());
                return CommonVo.failResponse("fail");
            }
        }
    }


    // 선호도 그룹 수정 시, Host/Vm 멤버 추가&삭제
    private void editHostMembers(SystemService system, AffinityGroupBuilder agBuilder, AffinityGroupVo agVo) {
        AffinityGroupHostsService agHostsService = system.clustersService().clusterService(agVo.getClusterId()).affinityGroupsService().groupService(agVo.getId()).hostsService();

        if (!agVo.getMembers().getHostMembers().isEmpty()) {
            agBuilder.hosts(
                agVo.getMembers().getHostMembers().stream()
                    .map(host -> new HostBuilder().id(host.getId()).build())
                    .collect(Collectors.toList())
            );
        } else{
            List<Host> hostList = agHostsService.list().send().hosts();
            for (Host host : hostList) {
                try {
                    agHostsService.hostService(host.id()).remove().send();
                    log.debug("Removed host: " + host.name());
                } catch (Exception e) {
                    log.error("Failed to remove host: " + host.name(), e);
                }
            }
        }
    }

    // 선호도 그룹 수정 시, Host/Vm 멤버 추가&삭제
    private void editVmMembers(SystemService system, AffinityGroupBuilder agBuilder, AffinityGroupVo agVo) {
        AffinityGroupVmsService agVmsService = system.clustersService().clusterService(agVo.getClusterId()).affinityGroupsService().groupService(agVo.getId()).vmsService();

        if (!agVo.getMembers().getVmMembers().isEmpty()) {
            agBuilder.vms(agVo.getMembers().getVmMembers().stream()
                    .map(vm -> new VmBuilder().id(vm.getId()).build())
                    .collect(Collectors.toList()));
        }else {
            List<Vm> vmList = agVmsService.list().send().vms();
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
                    return IdentifiedVo.builder().id(host.id()).name(h.name()).build();
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
                    return IdentifiedVo.builder().id(vm.id()).name(v.name()).build();
                })
                .collect(Collectors.toList());
    }









}
