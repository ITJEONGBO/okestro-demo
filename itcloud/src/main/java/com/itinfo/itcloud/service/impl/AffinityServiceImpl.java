package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.computing.AffinityGroupVo;
import com.itinfo.itcloud.model.computing.AffinityLabelVo;
import com.itinfo.itcloud.model.computing.HostVo;
import com.itinfo.itcloud.model.computing.VmVo;
import com.itinfo.itcloud.model.create.AffinityGroupCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItAffinityService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.builders.*;
import org.ovirt.engine.sdk4.services.AffinityGroupsService;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AffinityServiceImpl implements ItAffinityService {
    @Autowired private AdminConnectionService admin;

    /**
     * Affinity Label 생성시
     * 해당 클러스터에 있는 모든 호스트 출력
     * @param clusterId 클러스터 아이디 비교
     * @return 호스트 리스트
     */
    @Override
    public List<IdentifiedVo> getHostList(String clusterId){
        SystemService system = admin.getConnection().systemService();
        return system.hostsService().list().send().hosts().stream()
                .filter(host -> host.cluster().id().equals(clusterId))
                .map(host ->
                        IdentifiedVo.builder()
                                .id(host.id())
                                .name(host.name())
                                .build()
                )
                .collect(Collectors.toList());
    }


    /**
     * Affinity Label 생성시
     * 해당 클러스터에 있는 모든 가상머신 출력
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
    public List<IdentifiedVo> getLabel() {
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




    /***
     *  선호도 그룹 목록 출력 [ cluster, vm ]
     * @param id    cluster/vm id가 들어간다
     * @param type  cluster/vm 인지 default는 vm
     * @return 선호도 그룹 목록
     */
    // 선호도 그룹 목록 - cluster, vm
    @Override
    public List<AffinityGroupVo> getAffinitygroup(String id, String type){
        SystemService system = admin.getConnection().systemService();
        List<AffinityGroup> agList;

        if("cluster".equals(type)) {
            agList = system.clustersService().clusterService(id).affinityGroupsService().list().send().groups();
            log.info("클러스터 선호도그룹 목록");
        }else { // vm
            Vm vm = system.vmsService().vmService(id).get().send().vm();
            agList = system.clustersService().clusterService(vm.cluster().id()).affinityGroupsService().list().send().groups();
            log.info("가상머신 선호도그룹 목록");

            agList = agList.stream()
                    .filter(ag -> ag.vmsPresent() && ag.vms().stream().anyMatch(vm1 -> vm1.id().equals(id)))
                    .collect(Collectors.toList());
        }

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

                            .hostMembers(ag.hostsPresent() ? getAgHostList(system, id, ag.id()) : null)
                            .vmMembers(ag.vmsPresent() ? getAgVmList(system, id, ag.id()) : null)
                            .hostLabels(ag.hostLabelsPresent() ? getLabelName(ag.hostLabels().get(0).id()) : null)
                            .vmLabels(ag.vmLabelsPresent() ? getLabelName(ag.vmLabels().get(0).id()) : null)
                            .build()
                )
                .collect(Collectors.toList());
    }


    // 선호도 그룹 추가 - 클러스터 아이디를 받아와서 처리


    /***
     * 선호도 그룹 생성 (cluster, vm) 상관없이
     * agVo.getClusterId()로 값받아와서 넣기
     * @param id
     * @param type
     * @param agVo
     * @return
     */
    @Override
    public CommonVo<Boolean> addAffinitygroup(String id,  String type, AffinityGroupCreateVo agVo) {
        SystemService system = admin.getConnection().systemService();
        AffinityGroupsService agsService;
        List<AffinityGroup> agList;

        if("cluster".equals(type)){
            agsService = system.clustersService().clusterService(id).affinityGroupsService();
            log.info("클러스터 선호도그룹 생성 ");
        }else { // vm
            String clusterId = system.vmsService().vmService(id).get().send().vm().cluster().id();
            agsService = system.clustersService().clusterService(clusterId).affinityGroupsService();
            log.info("가상머신 선호도그룹 생성 ");
        }

        agList = agsService.list().send().groups();

        // 선호도 그룹 이름 중복검사
        boolean duplicateName = agList.stream().anyMatch(ag -> ag.name().equals(agVo.getName()));

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
                            .enforcing(agVo.isVmEnforcing())) // 강제 적용
                    .hostsRule(new AffinityRuleBuilder()
                            .enabled(agVo.isHostEnabled())
                            .positive(agVo.isHostPositive())
                            .enforcing(agVo.isHostEnforcing()));

            setLabelsAndMembers(ag, agVo);

            agsService.add().group(ag.build()).send().group();

            log.info("선호도그룹 생성");
            return CommonVo.createResponse();
        } catch (Exception e) {
            log.error("실패: 선호도그룹 생성");
            e.printStackTrace();
            return CommonVo.failResponse(e.getMessage());
        }
    }

    @Override
    public AffinityGroupCreateVo setEditAffinitygroup(String id, String type, String agId) {
        return null;
    }

    // 선호도 그룹 생성 - 레이블과 멤버들 추가
    private void setLabelsAndMembers(AffinityGroupBuilder ag, AffinityGroupCreateVo agVo) {
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
    }


    // 선호도 그룹 편집 창
//    public AffinityGroupCreateVo setEditAffinitygroup(String id, String type, String agId) {
//        SystemService system = admin.getConnection().systemService();
//
//        if("cluster".equals(type)){
//            AffinityGroupHostsService h = system.clustersService().clusterService(id).affinityGroupsService().groupService(agId).hostsService();
//            AffinityGroup affinityGroup = system.clustersService().clusterService(id).affinityGroupsService().groupService(agId).get().follow("vmlabels,hostlabels,vms,hosts").send().group();
//
//        }else {
//
//        }
//        h.list().send().hosts().stream().map(Host::name).forEach(System.out::println);
//
//        log.info("Cluster 선호도 그룹 편집창");
//        return AffinityGroupCreateVo.builder()
//                .clusterId(clusterId)
//                .id(agId)
//                .name(affinityGroup.name())
//                .description(affinityGroup.description())
//                .priority(affinityGroup.priority().intValue())
//                .vmEnabled(affinityGroup.vmsRule().enabled())// 비활성화
//                .vmPositive(affinityGroup.vmsRule().positive()) // 양극 음극
//                .vmEnforcing(affinityGroup.vmsRule().enforcing()) // 강제 적용
//                .hostEnabled(affinityGroup.hostsRule().enabled())
//                .hostPositive(affinityGroup.hostsRule().positive())
//                .hostEnforcing(affinityGroup.hostsRule().enforcing())
//                .hostLabels(
//                        affinityGroup.hostLabels().stream()
//                                .map(affinityLabel ->
//                                        IdentifiedVo.builder()
//                                                .id(affinityLabel.id())
//                                                .name(affinityLabel.name())
//                                                .build()
//                                )
//                                .collect(Collectors.toList())
//                )
//                .vmLabels(
//                        affinityGroup.vmLabels().stream()
//                                .map(affinityLabel ->
//                                        IdentifiedVo.builder()
//                                                .id(affinityLabel.id())
//                                                .name(affinityLabel.name())
//                                                .build()
//                                )
//                                .collect(Collectors.toList())
//                )
//                .hostList(
//                        affinityGroup.hosts().stream()
//                                .map(host ->
//                                        IdentifiedVo.builder()
//                                                .id(host.id())
//                                                .name(host.name())
//                                                .build()
//                                )
//                                .collect(Collectors.toList())
//                )
//                .vmList(
//                        affinityGroup.vms().stream()
//                                .map(vm ->
//                                        IdentifiedVo.builder()
//                                                .id(vm.id())
//                                                .name(vm.name())
//                                                .build()
//                                )
//                                .collect(Collectors.toList())
//                )
//                .build();
//    }
//
//
//
//    // 선호도 그룹 편집
//    @Override
//    public CommonVo<Boolean> editAffinitygroup(String id, String agId, AffinityGroupCreateVo agVo) {
//        SystemService system = admin.getConnection().systemService();
//        AffinityGroupService agService = system.clustersService().clusterService(id).affinityGroupsService().groupService(agId);
//        List<AffinityGroup> agList = system.clustersService().clusterService(id).affinityGroupsService().list().send().groups();
//
//        AffinityGroupHostsService agHosts = system.clustersService().clusterService(id).affinityGroupsService().groupService(agId).hostsService();
//        agHosts.list().send().hosts().stream().map(Host::name).forEach(System.out::println);
//
////        System.out.println(agVo.getHostList().get(0).getId());
////        AffinityGroupHostService aghost = system.clustersService().clusterService(id).affinityGroupsService().groupService(agId).hostsService().hostService(agVo.getHostList().get(0).getId());
////        aghost.remove();
//
//        boolean duplicateName = agList.stream().filter(ag -> !ag.id().equals(agId)).anyMatch(ag -> ag.name().equals(agVo.getName()));
//
//        try {
//            if(!duplicateName) {
//                AffinityGroupBuilder ag = new AffinityGroupBuilder()
//                        .id(agId)
//                        .name(agVo.getName())
//                        .description(agVo.getDescription())
//                        .cluster(new ClusterBuilder().id(id).build())
//                        .priority(agVo.getPriority())
//                        .vmsRule(
//                                new AffinityRuleBuilder()
//                                        .enabled(agVo.isVmEnabled())    // 비활성화
//                                        .positive(agVo.isVmPositive())  // 양극 음극
//                                        .enforcing(agVo.isVmEnforcing()) // 강제 적용
//                        )
//                        .hostsRule(
//                                new AffinityRuleBuilder()
//                                        .enabled(agVo.isHostEnabled())
//                                        .positive(agVo.isHostPositive())
//                                        .enforcing(agVo.isHostEnforcing())
//                        );
//
//                // 만약 받아온 hosts의 값중에 없는 id가 있다면 그 호스트를 affintiygrouphost에 id를 넣어서 삭제하는 작업을 하고 업데이트하자
//
//                if(!(agVo.getHostLabels() == null)){
//                    ag.hostLabels(
//                            agVo.getHostLabels().stream()
//                                    .map(al -> new AffinityLabelBuilder().id(al.getId()).build())
//                                    .collect(Collectors.toList())
//                    );
//                }
//                if(!(agVo.getVmLabels() == null)){
//                    ag.vmLabels(
//                            agVo.getVmLabels().stream()
//                                    .map(al -> new AffinityLabelBuilder().id(al.getId()).build())
//                                    .collect(Collectors.toList())
//                    );
//                }
//                if(!(agVo.getHostList() == null)){
//                    ag.hosts(agVo.getHostList().stream()
//                            .map(host -> new HostBuilder().id(host.getId()).build())
//                            .collect(Collectors.toList())
//                    );
//                }
//                if(!(agVo.getVmList() == null)) {
//                    ag.vms(agVo.getVmList().stream()
//                            .map(vm -> new VmBuilder().id(vm.getId()).build())
//                            .collect(Collectors.toList())
//                    );
//                }
//
//                agService.update().group(ag.build()).send().group();
////                agService.update().send().group();
//
//                log.info("Cluster 선호도그룹 편집");
//                return CommonVo.createResponse();
//            }else {
//                log.error("실패: 클러스터 선호도 그룹 이름 중복");
//                return CommonVo.duplicateResponse();
//            }
//        } catch (Exception e) {
//            log.error("실패: Cluster 선호도그룹 편집");
//            return CommonVo.failResponse(e.getMessage());
//        }
//    }
//
//
//    // 선호도 그룹 삭제 - clusterId와 agid를 가져와서 삭제
//    // 선호도 그룹 내에 항목들이 아예 없어야함
//    @Override
//    public CommonVo<Boolean> deleteAffinitygroup(String id, String agId) {
//        SystemService system = admin.getConnection().systemService();
//        AffinityGroupService agService = system.clustersService().clusterService(id).affinityGroupsService().groupService(agId);
//
//        try {
//            agService.remove().send();
//
//            log.info("Cluster 선호도그룹 삭제");
//            return CommonVo.successResponse();
//        } catch (Exception e) {
//            log.error("실패: Cluster 선호도그룹 삭제");
//            return CommonVo.failResponse(e.getMessage());
//        }
//    }








    // 클러스터에서 선호도 레이블 목록 출력  // 호스트 id
    // TODO
    @Override
    public List<AffinityLabelVo> getAffinitylabel(String id){
        SystemService system = admin.getConnection().systemService();
        List<AffinityLabel> affinityLabelList = system.affinityLabelsService().list().send().labels();

        log.info("Cluster 선호도 레이블");
        return affinityLabelList.stream()
                .map(al -> AffinityLabelVo.builder()
                        .id(al.id())
                        .name(al.name())
                        .hosts(getHostLabelMember(system, al.id()))
                        .vms(getVmLabelMember(system, al.id()))
                        .build()
                )
                .collect(Collectors.toList());
    }






    /**
     * AffinityLabel
     * 선호도  - 레이블 아이디와 이름 얻기
     * @param alId
     * @return 선호도 레이블 id, 이름
     */
    @Override
    public List<AffinityLabelVo> getLabelName(String alId){
        SystemService system = admin.getConnection().systemService();
        List<AffinityLabel> alList = system.affinityLabelsService().list().send().labels();

        return alList.stream()
                .filter(al -> al.id().equals(alId))
                .map(al ->
                        AffinityLabelVo.builder()
                                .id(al.id())
                                .name(al.name())
                                .build())
                .collect(Collectors.toList());
    }


    /**
     * 선호도 그룹이 가지고 있는 호스트 리스트 출력
     * @param system
     * @param clusterId
     * @param agId
     * @return 클러스터 밑의 호스트 리스트
     */
    @Override
    public List<HostVo> getAgHostList(SystemService system, String clusterId, String agId){
        List<Host> hostList = system.clustersService().clusterService(clusterId).affinityGroupsService().groupService(agId).hostsService().list().send().hosts();
        return hostList.stream()
                .map(host ->
                        HostVo.builder()
                                .id(host.id())
                                .name(host.name())
                                .build())
                .collect(Collectors.toList());
    }


    /**
     * 선호도 그룹이 가지고 있는 가상머신 리스트 출력
     * @param system
     * @param clusterId
     * @param agId
     * @return 클러스터 밑의 가상머신 리스트
     */
    @Override
    public List<VmVo> getAgVmList(SystemService system, String clusterId, String agId){
        List<Vm> vmList = system.clustersService().clusterService(clusterId).affinityGroupsService().groupService(agId).vmsService().list().send().vms();

        return vmList.stream()
                .map(vm ->
                        VmVo.builder()
                                .id(vm.id())
                                .name(vm.name())
                                .build())
                .collect(Collectors.toList());
    }



    /**
     * 선호도 레이블에 있는 호스트 출력
     * List를 매개변수로 주지못한 이유: 이름출력이 안됨
     * @param system
     * @param alid 선호도 레이블 아이디
     * @return 선호도 레이블이 가지고 있는 host 리스트
     */
    @Override
    public List<HostVo> getHostLabelMember(SystemService system, String alid){
        List<Host> hostList = system.affinityLabelsService().labelService(alid).hostsService().list().send().hosts();

        return hostList.stream()
                .map(host -> {
                    Host h = system.hostsService().hostService(host.id()).get().send().host();
                    return HostVo.builder()
                            .id(host.id())
                            .name(h.name())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 선호도 레이블에 있는 가상머신 출력
     * List를 매개변수로 주지못한 이유: 이름출력이 안됨
     * @param system
     * @param alid 선호도 레이블 아이디
     * @return 선호도 레이블이 가지고 있는 가상머신 리스트
     */
    @Override
    public List<VmVo> getVmLabelMember(SystemService system, String alid){
        List<Vm> vmList = system.affinityLabelsService().labelService(alid).vmsService().list().send().vms();
        return vmList.stream()
                .map(vm -> {
                    Vm v = system.vmsService().vmService(vm.id()).get().send().vm();
                    return VmVo.builder()
                            .id(vm.id())
                            .name(v.name())
                            .build();
                })
                .collect(Collectors.toList());
    }


}
