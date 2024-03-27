package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.computing.PermissionVo;
import com.itinfo.itcloud.model.computing.TemplateVo;
import com.itinfo.itcloud.model.create.TemplateCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.model.storage.DomainVo;
import com.itinfo.itcloud.model.storage.TemDiskVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.services.StorageDomainsService;
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
        SystemService systemService = admin.getConnection().systemService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        List<Template> templateList = systemService.templatesService().list().send().templates();

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
                                    systemService.clustersService().clusterService(template.cluster().id()).get().send().cluster().name() : null)
                            .datacenterId(template.clusterPresent() ?
                                    systemService.clustersService().clusterService(template.cluster().id()).get().send().cluster().dataCenter().id() : null)
                            .datacenterName(template.clusterPresent() ?
                                    systemService.dataCentersService().dataCenterService(systemService.clustersService().clusterService(template.cluster().id()).get().send().cluster().dataCenter().id()).get().send().dataCenter().name() : null)
                            // 보관
                            .description(template.description())
                            .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public TemplateVo getInfo(String id) {
        SystemService systemService = admin.getConnection().systemService();

        Template template = systemService.templatesService().templateService(id).get().send().template();

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
    public List<TemDiskVo> getDisk(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<DiskAttachment> vmdiskList = systemService.templatesService().templateService(id).diskAttachmentsService().list().send().attachments();
        // 별칭, 가상크기, 연결대상, 인터페이스, 논리적 이름, 상태, 유형, 설명

        return vmdiskList.stream()
                .filter(DiskAttachment::diskPresent)
                .map(diskAttachment -> {
                    Disk disk = systemService.disksService().diskService(diskAttachment.disk().id()).get().send().disk();
//                    Disk disk = systemService.disksService().diskService(diskAttachment.disk().id()).get().follow("statistics").send().disk();

                    return TemDiskVo.builder()
                            .name(disk.name())
                            .status(disk.status())
                            .sparse(disk.sparse())
                            .diskInterface(disk.interface_())
                            .virtualSize(disk.provisionedSize())
                            .type(disk.storageType())

                        .build();
                })
                .collect(Collectors.toList());

//        for(DiskAttachment diskAttachment : vmdiskList) {
//            if (diskAttachment.diskPresent()) {
//                vdVo = new VmDiskVo();
//
//                vdVo.setId(diskAttachment.id());
//                vdVo.setActive(diskAttachment.active());
//                vdVo.setReadOnly(diskAttachment.readOnly());
//                vdVo.setBootAble(diskAttachment.bootable());
//                vdVo.setLogicalName(diskAttachment.logicalName());
//                vdVo.setInterfaceName(diskAttachment.interface_().value());
//
//                Disk disk = systemService.disksService().diskService(diskAttachment.disk().id()).get().send().disk();
//                vdVo.setName(disk.name());
//                vdVo.setDescription(disk.description());
//                vdVo.setVirtualSize(disk.provisionedSize());
//                vdVo.setStatus(String.valueOf(disk.status()));  // 유형
//                vdVo.setType(disk.storageType().value());
//                vdVo.setConnection(disk.name());
//
//                vdVoList.add(vdVo);
//            }
//        }
//        return vdVoList;
    }

    @Override
    public List<DomainVo> getStorage(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<DomainVo> sdVoList = new ArrayList<>();
        DomainVo sdVo = null;

        List<StorageDomain> storageDomainList = systemService.storageDomainsService().list().send().storageDomains();

        for(StorageDomain storageDomain : storageDomainList) {
            sdVo = new DomainVo();

            if(storageDomain.templatesPresent()) {
                List<StorageDomain> storageDomainList2 =
                    ((StorageDomainsService.ListResponse)systemService.storageDomainsService().storageDomainService(storageDomain.id()).templatesService().list().send()).storageDomains();

                sdVo.setId(storageDomain.id());
                sdVo.setName(storageDomain.name());
                sdVo.setDomainType(storageDomain.type().value() + (storageDomain.master() ? "(master)" : ""));
                sdVo.setStatus(storageDomain.status().value());     // storageDomainStatus 10개
                sdVo.setAvailableSize(storageDomain.available()); // 여유공간
                sdVo.setUsedSize(storageDomain.used()); // 사용된 공간
                sdVo.setDiskSize(storageDomain.available().add(storageDomain.used()));
                sdVo.setDescription(storageDomain.description());
                sdVo.setDatacenterName(systemService.dataCentersService().dataCenterService(id).get().send().dataCenter().name());

                sdVoList.add(sdVo);
            }else{
                System.out.println("storage 없음");
            }
        }
        return sdVoList;
    }

    @Override
    public List<PermissionVo> getPermission(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<PermissionVo> pVoList = new ArrayList<>();
        PermissionVo pVo = null;

        List<Permission> permissionList = systemService.templatesService().templateService(id).permissionsService().list().send().permissions();

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

                Role role = systemService.rolesService().roleService(permission.role().id()).get().send().role();
                pVo.setRole(role.name());

                pVoList.add(pVo);
            }
        }
        return pVoList;
    }


    @Override
    public List<EventVo> getEvent(String id) {
        SystemService systemService = admin.getConnection().systemService();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");
        List<EventVo> eVoList = new ArrayList<>();
        EventVo eVo = null;

        List<Event> eventList = systemService.eventsService().list().send().events();
        Template t = systemService.templatesService().templateService(id).get().send().template();

        for(Event event : eventList){
            if(event.templatePresent() && event.template().name().equals(t.name())){
                eVo = EventVo.builder()
                        .severity(event.severity().value())     // 상태[LogSeverity] : alert, error, normal, warning
                        .time(sdf.format(event.time()))
                        .message(event.description())
                        .relationId(event.correlationIdPresent() ? event.correlationId() : null)
                        .source(event.origin())
                        .build();

                eVoList.add(eVo);
            }
        }
        return eVoList;
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
    private String getUptime(SystemService systemService, String vmId){
        List<Statistic> statisticList = systemService.vmsService().vmService(vmId).statisticsService().list().send().statistics();

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
    private String getIp(SystemService systemService, String vmId, String version){
        List<Nic> nicList = systemService.vmsService().vmService(vmId).nicsService().list().send().nics();
        Vm vm = systemService.vmsService().vmService(vmId).get().send().vm();

        String ip = null;

        for (Nic nic : nicList){
            List<ReportedDevice> reportedDeviceList = systemService.vmsService().vmService(vmId).nicsService().nicService(nic.id()).reportedDevicesService().list().send().reportedDevice();

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
    // endregion

}
