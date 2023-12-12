package com.itinfo.itcloud.service.impl;

import com.itinfo.itcloud.ovirt.AdminConnectionService;
import com.itinfo.itcloud.dao.StorageDomainDAO;
import com.itinfo.itcloud.service.StorageDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StorageStorageDomainServiceImpl implements StorageDomainService {

    @Autowired
    private AdminConnectionService adminConnectionService;

    @Autowired
    private StorageDomainDAO storageDomainDAO;

    public StorageStorageDomainServiceImpl(){}

    /*public List<SDomainVO> retrieveStorageDomains(String status, String domainType) {
        Connection connection = this.adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        String dataCenterId = ((DataCenter)((DataCentersService.ListResponse)systemService.dataCentersService().list().send()).dataCenters().get(0)).id();
        DataCenterService dataCenterService = systemService.dataCentersService().dataCenterService(dataCenterId);
        StorageDomainsService storageDomainsService = systemService.storageDomainsService();

        List<StorageDomain> storageDomains = ((StorageDomainsService.ListResponse)systemService.storageDomainsService().list().send()).storageDomains();
        if ("all".equalsIgnoreCase(status)) {
            storageDomains = ((StorageDomainsService.ListResponse)systemService.storageDomainsService().list().send()).storageDomains();
        } else if (StorageDomainStatus.ACTIVE.value().equalsIgnoreCase(status)) {
            storageDomains = ((StorageDomainsService.ListResponse)systemService.storageDomainsService().list().search("status=active").caseSensitive(false).send()).storageDomains();
        } else {
            storageDomains = ((StorageDomainsService.ListResponse)systemService.storageDomainsService().list().search("status!=active").caseSensitive(false).send()).storageDomains();
        }

        List<DiskProfile> diskProfiles = ((DiskProfilesService.ListResponse)systemService.diskProfilesService().list().send()).profile();
        List<SDomainVO> SDomainVoList = new ArrayList<>();
        storageDomains.forEach((storageDomain) -> {
            if ("all".equals(domainType) || storageDomain.type().name().equalsIgnoreCase(domainType)) {
                SDomainVO SDomainVO = new SDomainVO();
                SDomainVO.setId(storageDomain.id());
                SDomainVO.setName(storageDomain.name());
                SDomainVO.setType(storageDomain.type().name());
                SDomainVO.setComment(storageDomain.comment());
                SDomainVO.setDescription(storageDomain.description());
                SDomainVO.setDiskFree(storageDomain.available());
                SDomainVO.setDiskUsed(storageDomain.used());
                SDomainVO.setStorageFormat(storageDomain.storageFormat().name());
                SDomainVO.setStorageAddress(storageDomain.storage().address());
                SDomainVO.setStoragePath(storageDomain.storage().path());
                SDomainVO.setStorageType(storageDomain.storage().type().name());

                System.out.println("id: "+ SDomainVO.getId());
                System.out.println("name: "+ SDomainVO.getName());
                System.out.println(SDomainVO.toString());

                if (storageDomain.status() == null) {
                    AttachedStorageDomainService asds = dataCenterService.storageDomainsService().storageDomainService(storageDomain.id());

                    try {
                        AttachedStorageDomainService.GetRequest req = asds.get();
                        AttachedStorageDomainService.GetResponse res = (AttachedStorageDomainService.GetResponse)req.send();
                        StorageDomain sd = res.storageDomain();
                        SDomainVO.setStatus(sd.status().value());
                    } catch (Exception var11) {
                        SDomainVO.setStatus((String)null);
                    }
                } else {
                    SDomainVO.setStatus(storageDomain.status().value());
                }

                *//*if (storageDomain.type().name().equals(StorageDomainType.ISO.name())) {
                    List<ImageFileVo> imageFiles = new ArrayList();
                    List<File> files = ((FilesService.ListResponse)storageDomainsService.storageDomainService(storageDomain.id()).filesService().list().send()).file();
                    files.forEach((file) -> {
                        ImageFileVo imageFile = new ImageFileVo();
                        imageFile.setId(file.id());
                        imageFile.setName(file.name());
                        imageFiles.add(imageFile);
                    });
                    storageDomainVo.setImageFileList(imageFiles);
                }

                Iterator var13 = diskProfiles.iterator();

                while(var13.hasNext()) {
                    DiskProfile dp = (DiskProfile)var13.next();
                    if (dp.storageDomain().id().equals(storageDomain.id())) {
                        domainVO.setDiskProfileId(dp.id());
                        domainVO.setDiskProfileName(dp.name());
                        break;
                    }
                }*//*

                SDomainVoList.add(SDomainVO);
            }

        });
        return SDomainVoList;
    }


    public List<SDomainVO> retrieveStorageDomainList() {
        Connection connection = this.adminConnectionService.getConnection();
        SystemService systemService = connection.systemService();

        String dataCenterId = ((DataCenter)((DataCentersService.ListResponse)systemService.dataCentersService().list().send()).dataCenters().get(0)).id();
        DataCenterService dataCenterService = systemService.dataCentersService().dataCenterService(dataCenterId);
        StorageDomainsService storageDomainsService = systemService.storageDomainsService();

        List<StorageDomain> storageDomains = ((StorageDomainsService.ListResponse)systemService.storageDomainsService().list().send()).storageDomains();
        // status가 all이라면  equalsIgnoreCase는 대소문자 구분안하고 비교
        storageDomains = ((StorageDomainsService.ListResponse)systemService.storageDomainsService().list().send()).storageDomains();
        *//*if ("all".equalsIgnoreCase(status)) {
            storageDomains = ((StorageDomainsService.ListResponse)systemService.storageDomainsService().list().send()).storageDomains();
        } else if (StorageDomainStatus.ACTIVE.value().equalsIgnoreCase(status)) {
            storageDomains = ((StorageDomainsService.ListResponse)systemService.storageDomainsService().list().search("status=active").caseSensitive(false).send()).storageDomains();
        } else {
            storageDomains = ((StorageDomainsService.ListResponse)systemService.storageDomainsService().list().search("status!=active").caseSensitive(false).send()).storageDomains();
        }*//*

        List<DiskProfile> diskProfiles = ((DiskProfilesService.ListResponse)systemService.diskProfilesService().list().send()).profile();
        List<SDomainVO> SDomainVoList = new ArrayList<>();
        storageDomains.forEach((storageDomain) -> {

//            if ("all".equals(domainType) || storageDomain.type().name().equalsIgnoreCase(domainType)) {
                SDomainVO SDomainVO = new SDomainVO();
                SDomainVO.setId(storageDomain.id());
                SDomainVO.setName(storageDomain.name());
                SDomainVO.setType(storageDomain.type().name());
                SDomainVO.setComment(storageDomain.comment());
                SDomainVO.setDescription(storageDomain.description());
                SDomainVO.setDiskFree(storageDomain.available());
                SDomainVO.setDiskUsed(storageDomain.used());
                SDomainVO.setStorageFormat(storageDomain.storageFormat().name());
                SDomainVO.setStorageAddress(storageDomain.storage().address());
                SDomainVO.setStoragePath(storageDomain.storage().path());
                SDomainVO.setStorageType(storageDomain.storage().type().name());

                if (storageDomain.status() == null) {
                    AttachedStorageDomainService asds = dataCenterService.storageDomainsService().storageDomainService(storageDomain.id());

                    try {
                        AttachedStorageDomainService.GetRequest req = asds.get();
                        AttachedStorageDomainService.GetResponse res = (AttachedStorageDomainService.GetResponse)req.send();
                        StorageDomain sd = res.storageDomain();
                        SDomainVO.setStatus(sd.status().value());
                    } catch (Exception var11) {
                        SDomainVO.setStatus((String)null);
                    }
                } else {
                    SDomainVO.setStatus(storageDomain.status().value());
                }

                *//*if (storageDomain.type().name().equals(StorageDomainType.ISO.name())) {
                    List<ImageFileVo> imageFiles = new ArrayList();
                    List<File> files = ((FilesService.ListResponse)storageDomainsService.storageDomainService(storageDomain.id()).filesService().list().send()).file();
                    files.forEach((file) -> {
                        ImageFileVo imageFile = new ImageFileVo();
                        imageFile.setId(file.id());
                        imageFile.setName(file.name());
                        imageFiles.add(imageFile);
                    });
                    storageDomainVo.setImageFileList(imageFiles);
                }

                Iterator var13 = diskProfiles.iterator();

                while(var13.hasNext()) {
                    DiskProfile dp = (DiskProfile)var13.next();
                    if (dp.storageDomain().id().equals(storageDomain.id())) {
                        domainVO.setDiskProfileId(dp.id());
                        domainVO.setDiskProfileName(dp.name());
                        break;
                    }
                }*//*

                SDomainVoList.add(SDomainVO);
//            }

        });
        return SDomainVoList;
    }*/

}
