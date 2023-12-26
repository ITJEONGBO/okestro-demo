# Endpoint(s)

## `/admin/*`

| Method | Path |
| :---: | :--- |
| `GET` | /admin/retrieveInstanceTypeUpdateInfo
| `GET` | /admin/instanceType/updateInstanceType
| `GET` | /admin/instanceType/removeInstanceType
| `GET` | /admin/retrieveInstanceTypes
| `GET` | /admin/instanceTypes
| `GET` | /admin/retrieveInstanceTypeCreateInfo
| `POST` | /admin/instanceType/createInstanceType
| `POST` | /admin/createInstanceType
| `POST` | /admin/updateInstanceType
| `GET` | /admin/license
| `GET` | /admin/retrieveMacAddressPools
| `GET` | /admin/macAddressPools
| `GET` | /admin/systemPermissions/retrieveRoles
| `POST` | /admin/systemPermissions/addSystemPermissions
| `POST` | /admin/systemPermissions/removeSystemPermissions
| `GET` | /admin/systemPermissions/retrieveSystemPermissions
| `GET` | /admin/systemPermissions
| `GET` | /admin/systemPermissions/viewAddSystemPermissions
| `GET` | /admin/users/retrieveAllUsers
| `GET` | /admin/saveSystemProperties
| `GET` | /admin/retrieveProgramVersion
| `GET` | /admin/systemProperties
| `GET` | /admin/retrieveSystemProperties
| `GET` | /admin/retrieveProviders
| `GET` | /admin/providers
|||
| `GET` | /admin/quotas/retrieveQuotaEvents
| `POST` | /admin/quotas/createQuota
| `GET` | /admin/quotas/retrieveQuotasInfo
| `GET` | /admin/quotas/retrieveQuotaDetail
| `GET` | /admin/quotas/retrieveClusters
| `GET` | /admin/quotas/retrieveStorageDomains
| `GET` | /admin/quotas
| `POST` | /admin/createQuota
| `POST` | /admin/updateQuota
| `GET` | /admin/quota
| `GET` | /admin/quotas/retrieveCreateQuotaInfo
|||
| `GET` | /admin/users/retrieveUsers
| `GET` | /admin/users/retrieveUser
| `POST` | /admin/users/addUser
| `POST` | /admin/users/updateUser
| `POST` | /admin/users/updatePassword
| `POST` | /admin/users/removeUsers
| `GET` | /admin/users 
| `GET` | /admin/users/viewAddUser 
| `GET` | /admin/users/password

---

## `/compute/*`

| Method | Path |
| :---: | :--- |
| `GET` | /vmConsole/vncView 
| `GET` | /vmConsole/ticket 
| `GET` | /vmconsole/ticket2 
/compute/dataCenters/retrieveDataCenters
/compute/hosts/updateHost],methods=[POST
/compute/hosts/retrieveHostsInfo],methods=[GET
/compute/hosts/retrieveLunHostsInfo],methods=[GET
/compute/hosts/retrieveHostDetail],methods=[GET
/compute/hosts/retrieveHostEvents],methods=[GET
/compute/hosts/maintenanceStart],methods=[POST
/compute/hosts/maintenanceStop],methods=[POST
/compute/hosts/restartHost],methods=[POST
/compute/hosts/startHost],methods=[POST
/compute/hosts/stopHost],methods=[POST
/compute/hosts/createHost],methods=[POST
/compute/hosts/modifyNicNetwork],methods=[POST
/compute/hosts/setupHostNetwork],methods=[POST
/compute/hosts/retrieveFanceAgentType],methods=[GET
/compute/hosts/connectTestFenceAgent],methods=[POST
/compute/hosts/shutdownHost],methods=[GET
/compute/hosts/removeHost],methods=[POST
/compute/hosts/retrieveClusters],methods=[GET
/compute/hosts/retrieveCreateHostInfo],methods=[GET
/compute/host],methods=[GET
/compute/createHost
/compute/updateHost],methods=[GET
/compute/hosts
/compute/hosts/consolidateVms],methods=[POST
/compute/template/checkDuplicateName
/compute/createTemplate
/compute/removeTemplate
/compute/retrieveTemplateEditInfo
/compute/updateTemplate
/compute/updateTemplateInfo
/compute/exportTemplate
/compute/checkExportTemplate
/compute/template/cpuProfiles],methods=[GET
/compute/template/rootTemplates],methods=[GET
/compute/template/retrieveDisks],methods=[GET
/compute/template/retrieveTemplates],methods=[GET
/compute/templates
/compute/template/retrieveTemplate],methods=[GET
/compute/template
/compute/vm
/compute/vm/checkDuplicateName
/compute/createVm/disks
/compute/vm/devices],methods=[GET
/compute/startVm],methods=[POST
/compute/stopVm],methods=[POST
/compute/rebootVm],methods=[POST
/compute/suspendVm],methods=[POST
/compute/removeVm
/compute/vmList/hosts],methods=[GET
/compute/vmList/clusters],methods=[GET
/compute/vmList],methods=[GET
/compute/vmDetail],methods=[GET
/compute/vm/createVmNic],methods=[POST
/compute/vm/updateVmNic],methods=[POST
/compute/vm/events],methods=[GET
/compute/createVm/recommendHosts
/compute/createVm/checkDuplicateDiskName
/compute/createVm
/compute/createVmView
/compute/updateVmInfo
/compute/updateVm
/compute/cloneVm
/compute/cloneVmInfo
/compute/retrieveDiskProfiles
/compute/vm/snapshots],methods=[GET
/compute/vm/previewSnapshot
/compute/vm/commitSnapshot],methods=[GET
/compute/vm/undoSnapshot],methods=[GET
/compute/vm/removeSnapshot
/compute/discs],methods=[GET
/compute/changeDisc],methods=[POST
/compute/vms
/compute/vm/createSnapshot
/compute/vm/metrics/uri
/compute/vm/nic],methods=[GET
/compute/vm/removeVmNic],methods=[POST
/compute/vm/disks],methods=[GET
/compute/createVm/info],methods=[GET
/compute/updateVm/info
/compute/cloneVm/info
/compute/retrieveEngineIp],methods=[GET
/compute/vm/metrics
/dashboard
/symphony/workload
/symphony/consolidateVm
/symphony/migrateVm
/symphony/relocateVms
/symphony/retrieveDataCenterStatus
/symphony
/loginSuccess
/
/scopeTest
/loginTest
/login
/accessDenied
/userConvenienceSetting
/login/serverStatus
/login/resetLoginCount
/network/getNetworkList
/network/getHostNetworkList
/network/networks
/network/clusters
/network/network
/network/getNetworkDetail
/network/createNetwork
/network/createNetworkDeatil
/network/addNetwork
/network/deleteNetwork
/network/updateNetwork
/network/modifiedNetwork
/storage/disks
/storage/createDisk
/storage/domains/maintenanceStart],methods=[POST
/storage/domains/maintenanceStop],methods=[POST
/storage/domains/retrieveDomainEvents],methods=[GET
/storage/domains/createDomain],methods=[POST
/storage/domains/removeDomain],methods=[POST
/storage/domains/iscsiDiscover],methods=[POST
/storage/domains/iscsiLogin],methods=[POST
/storage/domains/retrieveHosts],methods=[GET
/storage/domains
/storage/createDomain
/storage/importDomain],methods=[GET
| `GET` | /storage/domains/retrieveDomainMeta |