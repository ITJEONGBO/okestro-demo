package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.model.computing.*;
import com.itinfo.itcloud.model.network.NetworkClusterVo;
import com.itinfo.itcloud.model.network.NetworkUsageVo;
import com.itinfo.itcloud.model.network.NetworkVo;
import com.itinfo.itcloud.model.network.VnicProfileVo;
import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.service.ItNetworkService;
import lombok.extern.slf4j.Slf4j;
import org.ovirt.engine.sdk4.Connection;
import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.nio.ch.Net;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class NetworkServiceImpl implements ItNetworkService {

    @Autowired
    private AdminConnectionService adminConnectionService;

    @Override
    public List<NetworkVo> getList() {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<NetworkVo> nwVoList = new ArrayList<>();
        NetworkVo nwVo = null;

        List<Network> networkList =
                ((NetworksService.ListResponse)systemService.networksService().list().send()).networks();

        for(Network network : networkList){
            nwVo = new NetworkVo();

            nwVo.setId(network.id());
            nwVo.setName(network.name());
            nwVo.setDescription(network.description());
            nwVo.setComment(network.comment());
            nwVo.setMtu(network.mtuAsInteger());
            nwVo.setVdsmName(network.vdsmName());
            nwVo.setDatacenterId(network.dataCenter().id());
            nwVo.setDatacenterName( ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(network.dataCenter().id()).get().send()).dataCenter().name() );

            // usages
            NetworkUsageVo nuVo = new NetworkUsageVo();
            nuVo.setVm(network.usages().contains(NetworkUsage.VM));
            nuVo.setDisplay(network.usages().contains(NetworkUsage.DISPLAY));
            nuVo.setMigration(network.usages().contains(NetworkUsage.MIGRATION));
            nuVo.setManagement(network.usages().contains(NetworkUsage.MANAGEMENT));
            nuVo.setDefaultRoute(network.usages().contains(NetworkUsage.DEFAULT_ROUTE));
            nuVo.setGluster(network.usages().contains(NetworkUsage.GLUSTER));

            nwVo.setNetworkUsageVo(nuVo);

            nwVoList.add(nwVo);
        }
        return nwVoList;
    }

    @Override
    public NetworkVo getNetwork(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        NetworkVo nwVo = new NetworkVo();

        Network network =
                ((NetworkService.GetResponse)systemService.networksService().networkService(id).get().send()).network();

        nwVo.setId(network.id());
        nwVo.setName(network.name());
        nwVo.setDescription(network.description());
        nwVo.setVdsmName(network.vdsmName());

        // vlan 여부 확인 후 체크
        nwVo.setVlan(network.vlanPresent() ? network.vlan().id() : null);

        nwVo.setMtu(network.mtuAsInteger());

        return nwVo;
    }

    @Override
    public List<VnicProfileVo> getVnic(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<VnicProfileVo> vpVoList = new ArrayList<>();
        VnicProfileVo vpVo = null;

        List<VnicProfile> vnicProfileList =
                ((AssignedVnicProfilesService.ListResponse)systemService.networksService().networkService(id).vnicProfilesService().list().send()).profiles();

        for(VnicProfile vnicProfile : vnicProfileList){
            if(id.equals(vnicProfile.network().id())){
                Network network =
                        ((NetworkService.GetResponse)systemService.networksService().networkService(id).get().send()).network();

                vpVo = new VnicProfileVo();

                vpVo.setId(vnicProfile.id());
                vpVo.setName(vnicProfile.name());
                vpVo.setDescription(vnicProfile.description());
                vpVo.setNetworkName( ((NetworkService.GetResponse)systemService.networksService().networkService(id).get().send()).network().name() );
                vpVo.setDatacenterId(network.dataCenter().id());
                vpVo.setDatacenterName( ((DataCenterService.GetResponse)systemService.dataCentersService().dataCenterService(network.dataCenter().id()).get().send()).dataCenter().name() );
                vpVo.setPassThrough(vnicProfile.passThrough().mode().value());
                vpVo.setPortMirroring(vnicProfile.portMirroring());
                vpVo.setNetworkFilterName( ((NetworkFilterService.GetResponse)systemService.networkFiltersService().networkFilterService(vnicProfile.networkFilter().id()).get().send()).networkFilter().name() );

                vpVoList.add(vpVo);
            }
        }
        return vpVoList;
    }

    @Override
    public List<NetworkClusterVo> getCluster(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<NetworkClusterVo> ncVoList = new ArrayList<>();
        NetworkClusterVo ncVo = null;

        // 클러스터리스트 출력
        List<Cluster> clusterList =
                ((ClustersService.ListResponse)systemService.clustersService().list().send()).clusters();

        for(Cluster cluster : clusterList){
            List<Network> networkList =
                    ((ClusterNetworksService.ListResponse)systemService.clustersService().clusterService(cluster.id()).networksService().list().send()).networks();

            for(Network network : networkList){
                if(cluster.id().equals(network.cluster().id())){
                    ncVo = new NetworkClusterVo();

                    ncVo.setId(cluster.id());
                    ncVo.setName(cluster.name());
                    ncVo.setVersion(cluster.version().major() + "." + cluster.version().minor());
                    ncVo.setDescription(cluster.description());

                    ncVo.setStatus(network.status().value());
                    ncVo.setRequired(network.required());

                    // usages
                    NetworkUsageVo nuVo = new NetworkUsageVo();
                    nuVo.setVm(network.usages().contains(NetworkUsage.VM));
                    nuVo.setDisplay(network.usages().contains(NetworkUsage.DISPLAY));
                    nuVo.setMigration(network.usages().contains(NetworkUsage.MIGRATION));
                    nuVo.setManagement(network.usages().contains(NetworkUsage.MANAGEMENT));
                    nuVo.setDefaultRoute(network.usages().contains(NetworkUsage.DEFAULT_ROUTE));
                    nuVo.setGluster(network.usages().contains(NetworkUsage.GLUSTER));

                    ncVo.setNetworkUsageVo(nuVo);

                    ncVoList.add(ncVo);
                }
            }
        }
        return ncVoList;
    }

    @Override
    public List<HostVo> getHost(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();


        return null;
    }

    @Override
    public List<VmVo> getVm(String id) {
        return null;
    }

    @Override
    public List<TemplateVo> getTemplate(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<TemplateVo> tVoList = new ArrayList<>();
        TemplateVo tVo = null;

        List<Template> templateList =
                ((TemplatesService.ListResponse)systemService.templatesService().list().send()).templates();

        for(Template template : templateList){
            if(template.nicsPresent()){
                tVo = new TemplateVo();

                tVo.setId(template.id());
                tVo.setName(template.name());

                tVoList.add(tVo);
            }
        }

        return tVoList;
    }

    @Override
    public List<PermissionVo> getPermission(String id) {
        Connection connection = adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        List<PermissionVo> pVoList = new ArrayList<>();
        PermissionVo pVo = null;

        List<Permission> permissionList =
                ((AssignedPermissionsService.ListResponse)systemService.networksService().networkService(id).permissionsService().list().send()).permissions();

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


}
