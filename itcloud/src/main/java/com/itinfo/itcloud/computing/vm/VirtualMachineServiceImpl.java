package com.itinfo.itcloud.computing.vm;

import com.itinfo.itcloud.VO.computing.VmNicVO;
import com.itinfo.itcloud.VO.computing.VmSystemVO;
import com.itinfo.itcloud.VO.computing.VmVO;
import com.itinfo.itcloud.ovirt.ConnectionService;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class VirtualMachineServiceImpl implements VirtualMachineSerivce {

    @Autowired
    private ConnectionService ovirtConnection;

    @Autowired
    private VirtualMachineDAO virtualMachineDAO;


    /*public List<VmVO> showList() {
        Connection connection = this.ovirtConnection.getConnection();
        SystemService systemService = connection.systemService();

        Date date = new Date(System.currentTimeMillis());

        List<Vm> vmList = ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();    // 반환되는 ovirt의 가상머신 목록을 vmList에 저장
        List<VmVO> vms = new ArrayList<>();     // 반환된 가상머신 목록

        try{
            vmList.forEach( (element) -> vms.add(showVm(element.id())) );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vms;
    }// showList*/


    @Override
    public List<VmVO> showVmList() {
        Connection connection = this.ovirtConnection.getConnection();
        SystemService systemService = connection.systemService();
        Date date = new Date(System.currentTimeMillis());

        List<Vm> vmList = ((VmsService.ListResponse)systemService.vmsService().list().send()).vms();    // 반환되는 ovirt의 가상머신 목록을 vmList에 저장
        List<VmVO> vms = new ArrayList<>();     // 반환된 가상머신 목록

        try{
            vmList.forEach((vmItem)-> {
                VmVO vmVO = new VmVO();
                vmVO.setVmId(vmItem.id());
                vmVO.setVmName(vmItem.name());
                vmVO.setStatus(vmItem.status().value());
                vmVO.setComment(vmItem.comment());
                vmVO.setDescription(vmItem.description());
                vmVO.setIpAddress(vmItem.display().address());
                vmVO.setFqdn(vmItem.fqdn());

                if(vmItem.host() != null){
                    vmVO.setHostId( vmItem.host().id());
                    vmVO.setHostName( ((HostService.GetResponse)systemService.hostsService().hostService(vmItem.host().id()).get().send()).host().name());
                }

                vmVO.setClusterId(vmItem.cluster().id());
                vmVO.setClusterName( ((ClusterService.GetResponse)systemService.clustersService().clusterService(vmItem.cluster().id()).get().send()).cluster().name());

//                vmVO.setDataCenterId( ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService().get().send()).dataCenter().id());
//                vmVO.setDataCenterName( ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(vmVO.getClusterId()).get().send()).dataCenter().name());
//                System.out.println(((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(vmVO.getClusterId()).get().send()).dataCenter().id());

                vmVO.setOrgTemplateId(vmItem.originalTemplate().id());
                vmVO.setOrgTemplateName( ((TemplateService.GetResponse)systemService.templatesService().templateService(vmItem.originalTemplate().id()).get().send()).template().name());
                vmVO.setTemplateID(vmItem.template().id());
                vmVO.setTemplateName( ((TemplateService.GetResponse)systemService.templatesService().templateService(vmItem.template().id()).get().send()).template().name());

                vmVO.setGraphicProtocol(vmItem.display().type().value());
//                vmVO.setStartTime(item.creationTime().toString());  //문제
//                vmVO.setUpTime(vm);
//                vmVO.setOs(item.os().type());
                vmVO.setOptimizeOption(vmItem.type().value());
//                vmVO.setChipsetFirmType();

                vmVO.setVmSystem(showVmSystem(vmItem.id()));
                vmVO.setVmNics(showVmNicList(vmItem.id()));
//                vmVO.setDisks();
//                vmVO.setSnapshots();

                vms.add(vmVO);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return vms;
    }// showVmList


    public VmVO showVm(String vmId){
        Connection connection = this.ovirtConnection.getConnection();
        SystemService systemService = connection.systemService();
        Date date = new Date(System.currentTimeMillis());
        Vm vm = ( (VmService.GetResponse)systemService.vmsService().vmService(vmId).get().send()).vm();

        VmVO vmVO = null;

        try{
            vmVO = new VmVO();
            vmVO.setVmId(vm.id());
            vmVO.setVmName(vm.name());
            vmVO.setStatus(vm.status().value());
            vmVO.setComment(vm.comment());
            vmVO.setDescription(vm.description());
            vmVO.setIpAddress(vm.display().address());
            vmVO.setFqdn(vm.fqdn());

            if(vm.host() != null){
                vmVO.setHostId( vm.host().id());
                vmVO.setHostName( ((HostService.GetResponse)systemService.hostsService().hostService(vm.host().id()).get().send()).host().name());
            }

            vmVO.setClusterId(vm.cluster().id());
            vmVO.setClusterName( ((ClusterService.GetResponse)systemService.clustersService().clusterService(vm.cluster().id()).get().send()).cluster().name());

//        vmVO.setDataCenterId( ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService().get().send()).dataCenter().id());
//        vmVO.setDataCenterName( ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(vmVO.getClusterId()).get().send()).dataCenter().name());
//        System.out.println(((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(vmVO.getClusterId()).get().send()).dataCenter().id());

            vmVO.setOrgTemplateId(vm.originalTemplate().id());
            vmVO.setOrgTemplateName( ((TemplateService.GetResponse)systemService.templatesService().templateService(vm.originalTemplate().id()).get().send()).template().name());
            vmVO.setTemplateID(vm.template().id());
            vmVO.setTemplateName( ((TemplateService.GetResponse)systemService.templatesService().templateService(vm.template().id()).get().send()).template().name());

            vmVO.setGraphicProtocol(vm.display().type().value());
            vmVO.setStartTime(vm.creationTime().toString());  //문제
//                vmVO.setUpTime(vm);
            vmVO.setOs(vm.os().type());
            vmVO.setOptimizeOption(vm.type().value());
            vmVO.setChipsetFirmType(vm.bios().type().value());

            vmVO.setVmSystem(showVmSystem(vm.id()));
            vmVO.setVmNics(showVmNicList(vm.id()));
//                vmVO.setDisks();
//                vmVO.setSnapshots();
        }catch (Exception e){
            e.printStackTrace();
        }

        return vmVO;
    } // showVm()


    // vmSystem
    public VmSystemVO showVmSystem(String vmId) {
        Connection connection = this.ovirtConnection.getConnection();
        SystemService systemService = connection.systemService();
        Vm vm = ((VmService.GetResponse)systemService.vmsService().vmService(vmId).get().send()).vm();

        VmSystemVO vmSystem = new VmSystemVO();
        vmSystem.setSetMemory( (vm.memoryAsLong() / 1024L / 1024L) + " MB");
        vmSystem.setGuaranteedMemory( (vm.memoryPolicy().guaranteedAsLong() / 1024L / 1024L) + " MB");
        vmSystem.setMaxMemory( (vm.memoryPolicy().maxAsLong() / 1024L / 1024L) + " MB");

        vmSystem.setSocketsCpuTopology(vm.cpu().topology().socketsAsInteger());
        vmSystem.setCoresCpuTopology(vm.cpu().topology().coresAsInteger());
        vmSystem.setThreadsCpuTopology(vm.cpu().topology().threadsAsInteger());
        vmSystem.setTotalCpuTopology(vmSystem.getSocketsCpuTopology() * vmSystem.getCoresCpuTopology() * vmSystem.getThreadsCpuTopology());

        return vmSystem;
    } // showVmSystem


    /*public VmNicVO showVmnic(String vmId){
        Connection connection = this.ovirtConnection.getConnection();
        SystemService systemService = connection.systemService();

        VmNicService vmNicService = ((VmNicService.GetResponse)systemService.vmsService().vmService(vmId).nicsService().nicService().get().send()).nic();

    }// showVmnic */


    public List<VmNicVO> showVmNicList(String vmId) {
        Connection connection = this.ovirtConnection.getConnection();
        SystemService systemService = connection.systemService();
        
        List<Nic> nicList = ((VmNicsService.ListResponse)systemService.vmsService().vmService(vmId).nicsService().list().send()).nics();
        List<VmNicVO> vmNics = new ArrayList<>();

        try{
            nicList.forEach((element) -> {
                Nic nic = ((VmNicService.GetResponse)systemService.vmsService().vmService(vmId).nicsService().nicService(element.id()).get().send()).nic();

                VmNicVO vmNic = new VmNicVO();
                vmNic.setVmsId(vmId);
//                vmNic.setVmsName();
                vmNic.setNicName(element.name());
                vmNic.setNicId(element.id());
                vmNic.setType(element.interface_().value());
                vmNic.setLinkStatus(nic.linked());
                vmNic.setPlugged(nic.plugged());

//                vmNic.setNetworkName();
//                vmNic.setProfileName();

                vmNic.setMacAddress(nic.mac().address());
//                vmNic.setIpv4(nic.reportedDevices().get(0).ips().get(0).address());
//                vmNic.setIpv6(nic.reportedDevices().get(0).ips().get(1).address());

                List<ReportedDevice> reportedDeviceList = ((VmReportedDevicesService.ListResponse)systemService.vmsService().vmService(vmId).nicsService().nicService(element.id()).reportedDevicesService().list().send()).reportedDevice();
                reportedDeviceList.forEach((dElement) ->{
                    ReportedDevice reportedDevice = ((VmReportedDeviceService.GetResponse)systemService.vmsService().vmService(vmId).nicsService().nicService(element.id()).reportedDevicesService().reportedDeviceService(dElement.id()).get().send()).reportedDevice();
                    vmNic.setIpv4(reportedDevice.ips().get(0).address());
                    vmNic.setIpv6(reportedDevice.ips().get(1).address());       // fe::80 없애야됨
                });

                vmNics.add(vmNic);
            });

        }catch (Exception e){
            e.printStackTrace();
        }

        return vmNics;
    }

   /* @Override
    public VmNicVO retrieveVmNic(String id) {
        Connection connection = this.ovirtConnection.getConnection();
        SystemService systemService = connection.systemService();

        Nic nic = ((VmNicService.GetResponse)systemService.vmsService().vmService(id).nicsService().nicService().send()).nic();
        VmNicVO vmNic = new VmNicVO();
        vmNic.setVmsId(nic.id());
        vmNic.setNicName(nic.name());
        vmNic.setNicId(nic.id());
//        vmNic.s(nic.interface_().value());

        if (nic.reportedDevicesPresent()) {
            List<Ip> ips = ((ReportedDevice)nic.reportedDevices().get(0)).ips();
            if (ips.size() > 0) {
                vmNic.setIpv4(((Ip)ips.get(0)).address());
            }

            if (ips.size() > 1) {
                vmNic.setIpv6(((Ip)ips.get(1)).address());
            }
        } else {
            vmNic.setIpv4("해당 없음");
            vmNic.setIpv6("해당 없음");
        }

        if (nic.vnicProfile() != null) {
            VnicProfile vnicProfile = ((VnicProfileService.GetResponse)systemService.vnicProfilesService().profileService(nic.vnicProfile().id()).get().send()).profile();
            Network network = ((NetworkService.GetResponse)systemService.networksService().networkService(vnicProfile.network().id()).get().send()).network();
            vmNic.setNetworkName(network.name());
            vmNic.setProfileName(vnicProfile.name());
            vmNic.setVnicProfileId(vnicProfile.id());
        }

        vmNic.setMacAddress(nic.mac().address());
        vmNic.setLinked(nic.linked());
        vmNic.setPlugged(nic.plugged());

        return vmNic;
    }*/





}
