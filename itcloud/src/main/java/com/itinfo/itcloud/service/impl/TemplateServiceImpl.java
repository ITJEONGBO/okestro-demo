package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.ovirt.ConnectionService;
import com.itinfo.itcloud.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private ConnectionService connectionService;

    private AdminConnectionService adminConnectionService;

    public TemplateServiceImpl(){}

    /*public List<TemplateVO> retrieveTemplates() {
        Connection connection = this.connectionService.getConnection();
        SystemService systemService = connection.systemService();
        TemplatesService templatesService = systemService.templatesService();
        ClustersService clustersService = systemService.clustersService();
        List<Template> items = ((TemplatesService.ListResponse)templatesService.list().send()).templates();
        List<TemplateVO> templates = new ArrayList<>();

        try {
            items.forEach((item) -> {
                TemplateVO template = new TemplateVO();
                template.setId(item.id());
                template.setName(item.name());
                List<OperatingSystemInfo> osItemList = ((OperatingSystemsService.ListResponse)systemService.operatingSystemsService().list().send()).operatingSystem();
                Iterator var6 = osItemList.iterator();

                while(var6.hasNext()) {
                    OperatingSystemInfo osItem = (OperatingSystemInfo)var6.next();
                    if (item.os().type().equals(osItem.name())) {
                        template.setOs(osItem.name());
                    }
                }

                if (item.version().versionNumberAsInteger() > 0) {
                    template.setVersion(item.version().versionName() + " (" + item.version().versionNumber() + ")");
                }

                if (item.version().versionNumberAsInteger() > 1) {
                    template.setName(template.getName() + " / " + item.version().versionName());
                }

                template.setDescription(item.description());
                template.setCreationTime(item.creationTime().getTime());
                template.setStatus(item.status().value());

               *//* if (item.clusterPresent()) {
                    Cluster clusterItem = ((ClusterService.GetResponse)clustersService.clusterService(item.cluster().id()).get().send()).cluster();
                    ClusterVo cluster = new ClusterVo();
                    cluster.setId(clusterItem.id());
                    cluster.setName(clusterItem.name());
                    template.setCluster(cluster);
                }*//*

                templates.add(template);
            });
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return templates;
    }*/
}
