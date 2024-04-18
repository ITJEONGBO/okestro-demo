package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.TypeExtKt;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.computing.TemplateVo;
import com.itinfo.itcloud.model.storage.DomainVo;
import com.itinfo.itcloud.model.storage.TempDiskVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.services.SystemService;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service @Slf4j
public class TemplateServiceImpl implements ItTemplateService {
    @Autowired private AdminConnectionService admin;
    private final CommonService commonService = new CommonService();


    @Override
    public List<TemplateVo> getList() {
        SystemService system = admin.getConnection().systemService();
        List<Template> templateList = system.templatesService().list().send().templates();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        return templateList.stream()
                .map(template ->
                    TemplateVo.builder()
                        .id(template.id())
                        .name(template.name())
                        .version(template.versionPresent() ? template.version().versionName() : "")
                        .createDate(sdf.format(template.creationTime().getTime()))
                        .status(template.status().value())
                        .clusterId(template.clusterPresent() ? template.cluster().id() : null)
                        .clusterName(template.clusterPresent() ? system.clustersService().clusterService(template.cluster().id()).get().send().cluster().name() : null)
                        .datacenterId(template.clusterPresent() ? system.clustersService().clusterService(template.cluster().id()).get().send().cluster().dataCenter().id() : null)
                        .datacenterName(template.clusterPresent() ?
                                system.dataCentersService().dataCenterService(system.clustersService().clusterService(template.cluster().id()).get().send().cluster().dataCenter().id()).get().send().dataCenter().name() : null)
                        // 보관
                        .description(template.description())
                        .build()
                )
                .collect(Collectors.toList());
    }


    @Override
    public TemplateVo getInfo(String id) {
        SystemService system = admin.getConnection().systemService();
        Template template = system.templatesService().templateService(id).get().send().template();

        // 상태 비저장
        return TemplateVo.builder()
                .id(template.id())
                .name(template.name())
                .description(template.description())
                // TODO OS 문제가 잇음
                .osType(template.osPresent() ? template.os().type() : null)
                .chipsetFirmwareType(template.bios().typePresent() ? TypeExtKt.findBios(template.bios().type()) : null)
                .optimizeOption(TypeExtKt.findVmType(template.type())) // 최적화 옵션
                .memory(template.memoryPolicy().guaranteed())
                .cpuCoreCnt(template.cpu().topology().coresAsInteger())
                .cpuSocketCnt(template.cpu().topology().socketsAsInteger())
                .cpuThreadCnt(template.cpu().topology().threadsAsInteger())
                .cpuCnt(
                        template.cpu().topology().coresAsInteger()
                        * template.cpu().topology().socketsAsInteger()
                        * template.cpu().topology().threadsAsInteger()
                )
                .monitor(template.display().monitorsAsInteger())
                .ha(template.highAvailability().enabled())
                .priority(template.highAvailability().priorityAsInteger())
                .usb(template.usb().enabled())
                .hostCluster(template.clusterPresent() ? system.clustersService().clusterService(template.cluster().id()).get().send().cluster().name() : null)
                .origin(template.origin())
            .build();
    }



    @Override
    public List<TempDiskVo> getDisk(String id) {
        SystemService system = admin.getConnection().systemService();
        List<DiskAttachment> vmdiskList = system.templatesService().templateService(id).diskAttachmentsService().list().send().attachments();
        // 별칭, 가상크기, 연결대상, 인터페이스, 논리적 이름, 상태, 유형, 설명

        return vmdiskList.stream()
                .filter(DiskAttachment::diskPresent)
                .map(diskAttachment -> {
                    Disk disk = system.disksService().diskService(diskAttachment.disk().id()).get().send().disk();
                    return TempDiskVo.builder()
                            .id(disk.id())
                            .name(disk.name())
                            .virtualSize(disk.provisionedSize())
                            .actualSize(disk.actualSize())  // 1보다 작은거 처리는 front에서
                            .status(disk.status())
                            .sparse(disk.sparse() ? "씬 프로비저닝" : "사전 할당")
                            .diskInterface(diskAttachment.interface_())
                            .storageType(TypeExtKt.findStorageType(disk.storageType()))
                            // TODO 생성날짜
                            .domainVoList(
                                disk.storageDomains().stream()
                                        .map(storageDomain -> {
                                            StorageDomain sd = system.storageDomainsService().storageDomainService(storageDomain.id()).get().send().storageDomain();
                                            return DomainVo.builder()
                                                    .name(sd.name())
                                                    .domainType(sd.type()) // 마스터
                                                    .domainTypeMaster(sd.master() /*? "마스터" : ""*/)
                                                    // 상태
                                                    .usedSize(sd.used())
                                                    .availableSize(sd.available())
                                                    .diskSize(sd.used().add(sd.available()))
                                                    .build();
                                        })
                                        .collect(Collectors.toList())
                            )
                            .build();
                })
                .collect(Collectors.toList());

//        return vmdiskList.stream()
//                .filter(DiskAttachment::diskPresent)
//                .map(diskAttachment -> {
//                    Disk disk = system.disksService().diskService(diskAttachment.disk().id()).get().send().disk();
//                    System.out.println("interface: "+disk.interface_());
//                    return TempDiskVo.builder()
//                            .name(disk.name())
//                            .status(disk.status())
//                            .sparse(disk.sparse())
////                            .diskInterface(disk.interface_()) // 인터페이스 안나옴
//                            .virtualSize(disk.provisionedSize())
//                            .actualSize(disk.actualSize())  // 1보다 작은거 처리는 front에서
//                            .storageType(TypeExtKt.findStorageType(disk.storageType()))
//                            // 생성날짜
//                            .domainVoList(
//                                    disk.storageDomains().stream()
//                                            .map(storageDomain -> {
//                                                StorageDomain sd = system.storageDomainsService().storageDomainService(storageDomain.id()).get().send().storageDomain();
//                                                return DomainVo.builder()
//                                                        .name(sd.name())
//                                                        .domainType(sd.type()) // 마스터
//                                                        // 상태
//                                                        .usedSize(sd.used())
//                                                        .availableSize(sd.available())
//                                                        .diskSize( sd.used().add(sd.available()) )
//                                                        .build();
//                                            })
//                                            .collect(Collectors.toList())
//                            )
//                            .build();
//                })
//                .collect(Collectors.toList());
    }



    @Override
    public List<PermissionVo> getPermission(String id) {
        SystemService system = admin.getConnection().systemService();

        List<PermissionVo> pVoList = new ArrayList<>();
        PermissionVo pVo = null;

        List<Permission> permissionList = system.templatesService().templateService(id).permissionsService().list().send().permissions();

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

                Role role = system.rolesService().roleService(permission.role().id()).get().send().role();
                pVo.setRole(role.name());

                pVoList.add(pVo);
            }
        }
        return pVoList;
    }


    @Override
    public List<EventVo> getEvent(String id) {
        SystemService system = admin.getConnection().systemService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");
        List<Event> eventList = system.eventsService().list().send().events();
        Template t = system.templatesService().templateService(id).get().send().template();

        return eventList.stream()
                .filter(event -> event.templatePresent() && event.template().name().equals(t.name()))
                .map(event ->
                        EventVo.builder()
                            .severity(event.severity().value())     // 상태[LogSeverity] : alert, error, normal, warning
                            .time(sdf.format(event.time()))
                            .message(event.description())
                            .relationId(event.correlationIdPresent() ? event.correlationId() : null)
                            .source(event.origin())
                        .build()
                )
                .collect(Collectors.toList());
    }








//    @Override
//    public List<VmVo> getVm(String tId) {
//        SystemService system = admin.getConnection().systemService();
//        List<Vm> vmList = system.vmsService().list().send().vms();
//
//        log.info("템플릿 가상머신 목록");
//        return vmList.stream()
//                .filter(vm -> vm.templatePresent() && vm.template().id().equals(tId))
//                .map(vm ->
//                        VmVo.builder()
//                                .id(vm.id())
//                                .name(vm.name())
//                                .hostName(vm.hostPresent() ? system.hostsService().hostService(vm.host().id()).get().send().host().name() : null)
//                                .ipv4(commonService.getVmIp(system, vm.id(), "v4"))
//                                .ipv6(commonService.getVmIp(system, vm.id(), "v6"))
//                                .fqdn(vm.fqdnPresent() ? vm.fqdn() : null)
//                                .status(vm.status().value())
//                                .upTime(commonService.getVmUptime(system, vm.id()))
//                                .build()
//                )
//                .collect(Collectors.toList());
//    }
//
//    // TODO 링크상태
//    @Override
//    public List<NicVo> getNic(String id) {
//        SystemService system = admin.getConnection().systemService();
//        List<Nic> nicList = system.templatesService().templateService(id).nicsService().list().send().nics();
//
//        return nicList.stream()
//                .map(nic -> {
//                    VnicProfile vnicProfile = system.vnicProfilesService().profileService(nic.vnicProfile().id()).get().send().profile();
//                    return NicVo.builder()
//                            .id(nic.id())
//                            .name(nic.name())
//                            .plugged(nic.plugged())   // 연결됨
//                            .networkName(system.networksService().networkService(vnicProfile.network().id()).get().send().network().name())
//                            .vnicProfileVo(
//                                    VnicProfileVo.builder()
//                                            .id(vnicProfile.id())
//                                            .name(vnicProfile.name())
//                                            .build()
//                            )
//                            // 링크상태
//                            .type(nic.interface_().value())
//                            .build();
//                })
//                .collect(Collectors.toList());
//    }
//
//
//    @Override
//    public TempStorageVo getStorage(String id) {
//        SystemService system = admin.getConnection().systemService();
//        DiskAttachment diskAtt = system.templatesService().templateService(id).diskAttachmentsService().list().follow("disk").send().attachments().get(0);
//        Disk disk = system.disksService().diskService(diskAtt.id()).get().send().disk();
//        StorageDomain domain = system.storageDomainsService().storageDomainService(disk.storageDomains().get(0).id()).get().send().storageDomain();
//
//        return TempStorageVo.builder()
//                .domainName(domain.name())
//                .domainType(domain.type())
//                .domainStatus(diskAtt.active())
//                .availableSize(domain.available())
//                .usedSize(domain.used())
//                .totalSize(domain.used().add(domain.available()))
//
//                .diskName(disk.alias())
//                .virtualSize(disk.provisionedSize())
//                .diskStatus(disk.status())
//                .diskSparse(disk.sparse())  // 할당정책
//                .diskInterface(diskAtt.interface_())
//                .diskType(disk.storageType())
//                // 날짜
//                .build();
//    }
//
//





}
