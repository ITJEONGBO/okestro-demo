package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.computing.TemplateVo;
import com.itinfo.itcloud.model.create.TemplateCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.storage.TempStorageVo;
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

    @Override
    public String getName(String id){
        return admin.getConnection().systemService().templatesService().templateService(id).get().send().template().name();
    }

    @Override
    public List<TemplateVo> getList() {
        SystemService system = admin.getConnection().systemService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        List<Template> templateList = system.templatesService().list().send().templates();

        return templateList.stream()
                .map(template ->
                     TemplateVo.builder()
                            .id(template.id())
                            .name(template.name())
                            .version(template.versionPresent() ? template.version().versionName() : "")
                            .createDate(sdf.format(template.creationTime().getTime()))
                            .status(template.status().value())
                            .clusterId(template.clusterPresent() ? template.cluster().id() : null)
                            .clusterName(template.clusterPresent() ?
                                    system.clustersService().clusterService(template.cluster().id()).get().send().cluster().name() : null)
                            .datacenterId(template.clusterPresent() ?
                                    system.clustersService().clusterService(template.cluster().id()).get().send().cluster().dataCenter().id() : null)
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
                .osType(template.osPresent() ? template.os().boot().devices().get(0).value() : null)
                .chipsetFirmwareType(template.bios().typePresent() ? template.bios().type().value() : null)
                .optimizeOption(template.type().value())
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
                .origin(template.origin())
            .build();
    }


    @Override
    public TempStorageVo getStorage(String id) {
        SystemService system = admin.getConnection().systemService();

        DiskAttachment diskAtt = system.templatesService().templateService(id).diskAttachmentsService().list().follow("disk").send().attachments().get(0);
        Disk disk = system.disksService().diskService(diskAtt.id()).get().send().disk();
        StorageDomain domain = system.storageDomainsService().storageDomainService(disk.storageDomains().get(0).id()).get().send().storageDomain();

        return TempStorageVo.builder()
                .domainName(domain.name())
                .domainType(domain.type())
                .domainStatus(diskAtt.active())
                .availableSize(domain.available())
                .usedSize(domain.used())
                .totalSize(domain.used().add(domain.available()))

                .diskName(disk.alias())
                .virtualSize(disk.provisionedSize())
                .diskStatus(disk.status())
                .diskSparse(disk.sparse())  // 할당정책
                .diskInterface(diskAtt.interface_())
                .diskType(disk.storageType())
                // 날짜
                .build();
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

    @Override
    public CommonVo<Boolean> addTemplate(TemplateCreateVo tVo) {
        return null;
    }

    @Override
    public CommonVo<Boolean> editTemplate(TemplateCreateVo tVo) {
        return null;
    }

    @Override
    public CommonVo<Boolean> deleteTemplate(String id) {
        return null;
    }






    // 가동시간, 업타임
    private String getUptime(SystemService system, String vmId){
        List<Statistic> statisticList = system.vmsService().vmService(vmId).statisticsService().list().send().statistics();

        long hour = statisticList.stream()
                .filter(statistic -> statistic.name().equals("elapsed.time"))
                .mapToLong(statistic -> statistic.values().get(0).datum().longValue() / (60 * 60))
                .findFirst()
                .orElse(0);

        String upTime;
        if (hour > 24) {
            upTime = hour / 24 + "일";
        } else if (hour > 1 && hour < 24) {
            upTime = hour + "시간";
        } else if (hour == 0) {
            upTime = null;
        } else {
            upTime = (hour / 60) + "분";
        }

        return upTime;
    }

    // ip 주소
    private String getIp(SystemService system, String vmId, String version){
        List<Nic> nicList = system.vmsService().vmService(vmId).nicsService().list().send().nics();
        Vm vm = system.vmsService().vmService(vmId).get().send().vm();

        String ip = null;

        for (Nic nic : nicList){
            List<ReportedDevice> reportedDeviceList = system.vmsService().vmService(vmId).nicsService().nicService(nic.id()).reportedDevicesService().list().send().reportedDevice();

            if("v4".equals(version)) {
                ip = reportedDeviceList.stream()
                        .filter(r -> !vm.status().value().equals("down"))
                        .map(r -> r.ips().get(0).address())
                        .findFirst()
                        .orElse(null);
            }else {
                ip = reportedDeviceList.stream()
                        .filter(r -> !vm.status().value().equals("down"))
                        .map(r -> r.ips().get(1).address())
                        .findFirst()
                        .orElse(null);
            }
        }
        return ip;
    }








    // region: 필요없을듯
//    @Override
//    public List<VmVo> getVm(String tId) {
//        SystemService systemService = admin.getConnection().systemService();
//
//        List<Vm> vmList = systemService.vmsService().list().send().vms();
//
//        log.info("템플릿 {} 가상머신 목록", getName(tId));
//        return vmList.stream()
//                .filter(vm -> vm.templatePresent() && vm.template().id().equals(tId))
//                .map(vm ->
//                    VmVo.builder()
//                            .status(vm.status().value())
//                            .id(vm.id())
//                            .name(vm.name())
//                            .upTime(getUptime(systemService, vm.id()))
//                            .ipv4(getIp(systemService, vm.id(), "v4"))
//                            .ipv6(getIp(systemService, vm.id(), "v6"))
//                            .build()
//                )
//                .collect(Collectors.toList());
//    }


//    @Override
//    public List<TemDiskVo> getDisk(String id) {
//        SystemService systemService = admin.getConnection().systemService();
//
//        List<DiskAttachment> vmdiskList = systemService.templatesService().templateService(id).diskAttachmentsService().list().send().attachments();
//        // 별칭, 가상크기, 연결대상, 인터페이스, 논리적 이름, 상태, 유형, 설명
//
//        return vmdiskList.stream()
//                .filter(DiskAttachment::diskPresent)
//                .map(diskAttachment -> {
//                    Disk disk = systemService.disksService().diskService(diskAttachment.disk().id()).get().send().disk();
//                    System.out.println("interface: "+disk.interface_());
//                    return TemDiskVo.builder()
//                            .name(disk.name())
//                            .status(disk.status())
//                            .sparse(disk.sparse())
////                            .diskInterface(disk.interface_()) // 인터페이스 안나옴
//                            .virtualSize(disk.provisionedSize())
//                            .actualSize(disk.actualSize())  // 1보다 작은거 처리는 front에서
//                            .type(disk.storageType())
//                            // 생성날짜
//                            .storageDomainVo(
//                                    disk.storageDomains().stream()
//                                            .map(storageDomain -> {
//                                                StorageDomain sd = systemService.storageDomainsService().storageDomainService(storageDomain.id()).get().send().storageDomain();
//
//                                                return StorageDomainVo.builder()
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
//    }
    // endregion

}
