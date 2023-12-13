package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItHostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HostServiceImpl implements ItHostService {

    @Autowired
    private AdminConnectionService adminConnectionService;

    public HostServiceImpl(){}

    // 그냥 host 정보 받아오는거
    /*public List<HostDetailVO> retrieveHosts(){
        Connection connection = this.adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();
        HostsService hostsService = systemService.hostsService();

        List<Host> hosts = ((HostsService.ListResponse)systemService.hostsService().list().send()).hosts();
        List<HostDetailVO> hostDetailList = new ArrayList<>();

        hosts.forEach((host) -> {
            HostDetailVO hostDetailVO = new HostDetailVO();
            hostDetailVO.setId(host.id());

            System.out.println(hostDetailVO.getId());

            hostDetailVO.setName(host.name());
            hostDetailVO.setDescription(host.description());
            hostDetailVO.setComment(host.comment());
            hostDetailVO.setAddress(host.address());
            hostDetailVO.setStatus(host.status().value());
            hostDetailVO.setPowerManagementEnabled(host.powerManagement().enabled());
            hostDetailVO.setCpuName(host.cpu().name());
            hostDetailVO.setCpuCores(host.cpu().topology().cores());
            hostDetailVO.setCpuSockets(host.cpu().topology().sockets());
            hostDetailVO.setCpuThreads(host.cpu().topology().threads());

            if (host.cpu().topology().cores() != null && host.cpu().topology().sockets() != null && host.cpu().topology().threads() != null) {
                hostDetailVO.setCpuTotal(host.cpu().topology().cores().intValue() * host.cpu().topology().sockets().intValue() * host.cpu().topology().threads().intValue());
            } else {
                hostDetailVO.setCpuTotal(0);
            }
            hostDetailList.add(hostDetailVO);


            List<HostNic> hostNicList = ((HostNicsService.ListResponse)systemService.hostsService().hostService(host.id()).nicsService().list().send()).nics();
            List<NicUsageVO> nicUsageVOList = new ArrayList();

            Iterator var14 = hostNicList.iterator();

            while(var14.hasNext()) {
                HostNic hostNic = (HostNic)var14.next();

                NicUsageVO nicUsageVO = new NicUsageVO();
                nicUsageVO.setHostInterfaceId(hostNic.id());
                nicUsageVO.setHostInterfaceName(hostNic.name());
                nicUsageVO.setMacAddress(hostNic.mac().address());


                System.out.println(nicUsageVO.getHostInterfaceId());
                System.out.println(nicUsageVO.getHostInterfaceName());

                nicUsageVOList.add(nicUsageVO);
            }
            hostDetailVO.setNicUsageVOList(nicUsageVOList);
        });

        return hostDetailList;
    }

    @Override
    public List<HostNicVO> retrieveHostNicVO() {
        return null;
    }
*/



}
