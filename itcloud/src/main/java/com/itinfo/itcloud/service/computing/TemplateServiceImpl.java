package com.itinfo.itcloud.service.computing;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.storage.DomainVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItTemplateService;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TemplateServiceImpl implements ItTemplateService {
    @Autowired
    private AdminConnectionService admin;

    @Override
    public String getName(String id){
        SystemService systemService = admin.getConnection().systemService();

        return ((TemplateService.GetResponse)systemService.templatesService().templateService(id).get().send()).template().name();
    }

    @Override
    public List<TemplateVo> getList() {
        SystemService systemService = admin.getConnection().systemService();

        List<TemplateVo> tVoList = new ArrayList<>();
        TemplateVo tVo = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        List<Template> templateList =
                ((TemplatesService.ListResponse)systemService.templatesService().list().send()).templates();

        for(Template template : templateList){
            tVo = new TemplateVo();

            tVo.setId(template.id());
            tVo.setName(template.name());
            tVo.setVersion(template.versionPresent() ? template.version().versionName() : null);
            tVo.setCreateDate(sdf.format(template.creationTime().getTime()));
            tVo.setStatus(template.status().value());
            // 보관
            // 클러스터
            // 데이터 센터
            tVo.setDescription(template.description());

            tVoList.add(tVo);
        }
        return tVoList;
    }

    @Override
    public TemplateVo getInfo(String id) {
        SystemService systemService = admin.getConnection().systemService();

        TemplateVo tVo = new TemplateVo();

        Template template =
                ((TemplateService.GetResponse)systemService.templatesService().templateService(id).get().send()).template();

        tVo.setId(template.id());
        tVo.setName(template.name());
        tVo.setDescription(template.description());
        tVo.setOsType(template.osPresent() ? template.os().boot().devices().get(0).value() : null);
        tVo.setChipsetFirmwareType(template.bios().typePresent() ? template.bios().type().value() : null);
        tVo.setOptimizeOption(template.type().value());
        tVo.setMemory(template.memoryPolicy().guaranteed());
        tVo.setCpuCoreCnt(template.cpu().topology().coresAsInteger());
        tVo.setCpuSocketCnt(template.cpu().topology().socketsAsInteger());
        tVo.setCpuThreadCnt(template.cpu().topology().threadsAsInteger());
        tVo.setCpuCnt(tVo.getCpuCoreCnt() * tVo.getCpuSocketCnt() * tVo.getCpuThreadCnt());
        tVo.setMonitor(template.display().monitorsAsInteger());
        tVo.setHa(template.highAvailability().enabled());       // 고가용성
        tVo.setPriority(template.highAvailability().priorityAsInteger()); // 우선순위
        tVo.setUsb(template.usb().enabled());
        tVo.setOrigin(template.origin());

        // 상태 비저장
        return tVo;
    }

    @Override
    public List<VmVo> getVm(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<VmVo> vmVoList = new ArrayList<>();
        VmVo vmVo = null;
        Date now = new Date(System.currentTimeMillis());

        List<Vm> vmList = systemService.vmsService().list().send().vms();

        for (Vm vm : vmList) {
            Host host = systemService.hostsService().hostService(vm.host().id()).get().send().host();

            if (vm.hostPresent() && vm.host().id().equals(id)) {
                List<Statistic> statisticList = systemService.vmsService().vmService(vm.id()).statisticsService().list().send().statistics();
                String upTime = null;
                for(Statistic statistic : statisticList) {
                    long hour = 0;
                    if (statistic.name().equals("elapsed.time")) {
                        hour = statistic.values().get(0).datum().longValue() / (60*60);      // 시간

                        if(hour > 24){
                            upTime = hour/24 + "일";
                        }else if( hour > 1 && hour < 24){
                            upTime = hour + "시간";
                        }else {
                            upTime = (statistic.values().get(0).datum().longValue() / 60) + "분";
                        }
                    }
                }

                // ipv4, ipv6
                List<Nic> nicList = systemService.vmsService().vmService(vm.id()).nicsService().list().send().nics();
                String ipv4 = null;
                String ipv6 = null;
                for (Nic nic : nicList) {
                    List<ReportedDevice> reportedDeviceList = systemService.vmsService().vmService(vm.id()).nicsService().nicService(nic.id()).reportedDevicesService().list().send().reportedDevice();
                    for (ReportedDevice r : reportedDeviceList) {
                        ipv4 = !vm.status().value().equals("down") ? r.ips().get(0).address() : null;
                        ipv6 = !vm.status().value().equals("down") ? r.ips().get(1).address() : null;
                    }
                }

                vmVo = VmVo.builder()
                        .hostName(vm.hostPresent() ? host.name() : null)
                        .id(vm.id())
                        .name(vm.name())
                        .status(vm.status().value())
                        .fqdn(vm.fqdn())
                        .upTime(upTime)
                        .ipv4(ipv4)
                        .ipv6(ipv6)
                        .build();

                vmVoList.add(vmVo);
            } else if(vm.placementPolicy().hostsPresent() && vm.placementPolicy().hosts().get(0).id().equals(id)){
                // vm이 down 상태일 경우
                vmVo = VmVo.builder()
                        .id(vm.id())
                        .name(vm.name())
                        .status(vm.status().value())
                        .clusterName(systemService.clustersService().clusterService(vm.cluster().id()).get().send().cluster().name())
                        .build();
                vmVoList.add(vmVo);
            }
        }
        return vmVoList;
    }

    @Override
    public List<NicVo> getNic(String id) {
        return null;
    }

    @Override
    public List<VmDiskVo> getDisk(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<VmDiskVo> vdVoList = new ArrayList<>();
        VmDiskVo vdVo = null;

        List<DiskAttachment> vmdiskList =
                ((DiskAttachmentsService.ListResponse)systemService.templatesService().templateService(id).diskAttachmentsService().list().send()).attachments();
        // 별칭, 가상크기, 연결대상, 인터페이스, 논리적 이름, 상태, 유형, 설명

        for(DiskAttachment diskAttachment : vmdiskList) {
            if (diskAttachment.diskPresent()) {
                vdVo = new VmDiskVo();

                vdVo.setId(diskAttachment.id());
                vdVo.setActive(diskAttachment.active());
                vdVo.setReadOnly(diskAttachment.readOnly());
                vdVo.setBootAble(diskAttachment.bootable());
                vdVo.setLogicalName(diskAttachment.logicalName());
                vdVo.setInterfaceName(diskAttachment.interface_().value());

                Disk disk =
                        ((DiskService.GetResponse) systemService.disksService().diskService(diskAttachment.disk().id()).get().send()).disk();
                vdVo.setName(disk.name());
                vdVo.setDescription(disk.description());
                vdVo.setVirtualSize(disk.provisionedSize());
                vdVo.setStatus(String.valueOf(disk.status()));  // 유형
                vdVo.setType(disk.storageType().value());
                vdVo.setConnection(disk.name());

                vdVoList.add(vdVo);
            }
        }
        return vdVoList;
    }

    @Override
    public List<DomainVo> getStorage(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<DomainVo> sdVoList = new ArrayList<>();
        DomainVo sdVo = null;

        List<StorageDomain> storageDomainList =
                ((StorageDomainsService.ListResponse)systemService.storageDomainsService().list().send()).storageDomains();

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
                sdVo.setDatacenterName(((DataCenterService.GetResponse) systemService.dataCentersService().dataCenterService(id).get().send()).dataCenter().name());

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

        List<Permission> permissionList =
                ((AssignedPermissionsService.ListResponse)systemService.templatesService().templateService(id).permissionsService().list().send()).permissions();

        for(Permission permission : permissionList){
            pVo = new PermissionVo();
            pVo.setPermissionId(permission.id());

            if(permission.groupPresent() && !permission.userPresent()){
                Group group = ((GroupService.GetResponse)systemService.groupsService().groupService(permission.group().id()).get().send()).get();
                pVo.setUser(group.name());
                pVo.setNameSpace(group.namespace());
                // 생성일의 경우 db에서 가져와야함?

                Role role = ((RoleService.GetResponse)systemService.rolesService().roleService(permission.role().id()).get().send()).role();
                pVo.setRole(role.name());

                pVoList.add(pVo);       // 그룹에 추가
            }

            if(permission.userPresent() && !permission.groupPresent()){
                User user = ((UserService.GetResponse)systemService.usersService().userService(permission.user().id()).get().send()).user();
                pVo.setUser(user.name());
                pVo.setNameSpace(user.namespace());

                Role role = ((RoleService.GetResponse)systemService.rolesService().roleService(permission.role().id()).get().send()).role();
                pVo.setRole(role.name());

                pVoList.add(pVo);
            }
        }
        return pVoList;
    }


    @Override
    public List<EventVo> getEvent(String id) {
        SystemService systemService = admin.getConnection().systemService();

        List<EventVo> eVoList = new ArrayList<>();
        EventVo eVo = null;

        // 2024. 1. 4. PM 04:01:21
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");

        List<Event> eventList = ((EventsService.ListResponse)systemService.eventsService().list().send()).events();

        Template t = ((TemplateService.GetResponse)systemService.templatesService().templateService(id).get().send()).template();

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
}
