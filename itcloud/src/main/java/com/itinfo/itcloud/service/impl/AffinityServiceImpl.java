package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.IdentifiedVo;
import com.itinfo.itcloud.model.computing.AffinityLabelVo;
import com.itinfo.itcloud.model.computing.HostVo;
import com.itinfo.itcloud.model.computing.VmVo;
import com.itinfo.itcloud.service.AffinityService;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.types.AffinityLabel;
import org.ovirt.engine.sdk4.types.Host;
import org.ovirt.engine.sdk4.types.Vm;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AffinityServiceImpl implements AffinityService {

    /**
     * Affinity
     * 해당 클러스터에 있는 모든 호스트 출력
     * setAffinityDefaultInfo
     * @param hostList
     * @param clusterId 클러스터 아이디 비교
     * @return 호스트 리스트
     */

    public List<IdentifiedVo> setHostList(List<Host> hostList, String clusterId){
        return hostList.stream()
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
     * Affinity
     * 해당 클러스터에 있는 모든 가상머신 출력
     * @param vmList
     * @param clusterId
     * @return 가상머신 리스트
     */

    public List<IdentifiedVo> setVmList(List<Vm> vmList, String clusterId){
        return vmList.stream()
                .filter(vm -> vm.cluster().id().equals(clusterId))
                .map(vm ->
                        IdentifiedVo.builder()
                                .id(vm.id())
                                .name(vm.name())
                                .build()
                )
                .collect(Collectors.toList());
    }

    public List<IdentifiedVo> setLabel(SystemService system) {
        List<AffinityLabel> alList = system.affinityLabelsService().list().send().labels();

        return alList.stream()
                .map(affinityLabel ->
                        IdentifiedVo.builder()
                                .id(affinityLabel.id())
                                .name(affinityLabel.name())
                                .build()
                )
                .collect(Collectors.toList());
    }





    /**
     * AffinityLabel
     * 선호도  - 레이블 아이디와 이름 얻기
     * @param system
     * @param alId
     * @return 선호도 레이블 id, 이름
     */
    public List<AffinityLabelVo> getLabelName(SystemService system, String alId){
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
