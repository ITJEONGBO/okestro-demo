const ENDPOINTS = {  
  //region: User
  FIND_ALL_USERS: () => `/api/v1/auth/users`,
  FIND_USER: (username) => `/api/v1/auth/users/${username}`,
  //endregion: User

  //region: TreeNavigation
  FIND_ALL_TREE_NAVIGATIONS: (type) => `/api/v1/navigation/${type}`,
  //endregion: TreeNavigation

  //region: Dashboard
  GET_DASHBOARD: () =>          `/api/v1/dashboard`,
  GET_CPU_MEMORY: () =>         `/api/v1/dashboard/cpumemory`,
  GET_STORAGE: () =>            `/api/v1/dashboard/storage`,
  GET_VM_CPU: () =>             `/api/v1/dashboard/vmCpu`,
  GET_VM_MEMORY: () =>          `/api/v1/dashboard/vmMemory`,
  GET_STORAGE_MEMORY: () =>     `/api/v1/dashboard/storageMemory`,

  GET_PER_VM_CPU: () =>         `/api/v1/dashboard/vmCpuPerList`,
  GET_PER_VM_MEMORY: () =>      `/api/v1/dashboard/vmMemoryPerList`,
  GET_PER_VM_NETWORK: () =>      `/api/v1/dashboard/vmNetworkPerList`,

  GET_METRIC_VM: () =>          `/api/v1/dashboard/vmMetricList`,
  GET_METRIC_STORAGE: () =>      `/api/v1/dashboard/storageMetricList`,
  //endregion: Dashboard
  
  //region: DataCenter
  FIND_ALL_DATA_CENTERS: () =>  `/api/v1/computing/datacenters`,
  FIND_DATA_CENTER: (dataCenterId) =>  `/api/v1/computing/datacenters/${dataCenterId}`, 
  FIND_CLUSTERS_FROM_DATA_CENTER: (dataCenterId) => `/api/v1/computing/datacenters/${dataCenterId}/clusters`, 
  FIND_HOSTS_FROM_DATA_CENTER: (dataCenterId) => `/api/v1/computing/datacenters/${dataCenterId}/hosts`, 
  FIND_VMS_FROM_DATA_CENTER: (dataCenterId) => `/api/v1/computing/datacenters/${dataCenterId}/vms`, 
  FIND_STORAGE_DOMAINS_FROM_DATA_CENTER: (dataCenterId) => `/api/v1/computing/datacenters/${dataCenterId}/storageDomains`, 
  FIND_ACTIVE_STORAGE_DOMAINS_FROM_DATA_CENTER: (dataCenterId) => `/api/v1/computing/datacenters/${dataCenterId}/activeDomains`,
  FIND_NETWORKS_FROM_DATA_CENTER: (dataCenterId) => `/api/v1/computing/datacenters/${dataCenterId}/networks`, 
  FIND_EVENTS_FROM_DATA_CENTER: (dataCenterId) => `/api/v1/computing/datacenters/${dataCenterId}/events`,

  ADD_DATA_CENTER: () => `/api/v1/computing/datacenters`,
  EDIT_DATA_CENTER: (dataCenterId) => `/api/v1/computing/datacenters/${dataCenterId}`, 
  DELETE_DATA_CENTER: (dataCenterId) => `/api/v1/computing/datacenters/${dataCenterId}`, 
  //endregion: DataCenter

  // region: Cluster
  FIND_ALL_CLUSTERS: () =>      `/api/v1/computing/clusters`,
  FIND_CLUSTER: (clusterId) =>  `/api/v1/computing/clusters/${clusterId}`, 
  FIND_HOSTS_FROM_CLUSTER:(clusterId) =>  `/api/v1/computing/clusters/${clusterId}/hosts`, 
  FIND_VMS_FROM_CLUSTER:(clusterId) =>  `/api/v1/computing/clusters/${clusterId}/vms`, 

  FIND_NETWORKS_FROM_CLUSTER:(clusterId) =>  `/api/v1/computing/clusters/${clusterId}/networks`, 
  ADD_NETWORK_FROM_CLUSTER:(clusterId) =>  `/api/v1/computing/clusters/${clusterId}/networks`,
  MANAGE_NETWORKS_FROM_CLUSTER:(clusterId) =>  `/api/v1/computing/clusters/${clusterId}/networks/manageNetworks`, 

  FIND_EVENTS_FROM_CLUSTER: (clusterId) => `/api/v1/computing/clusters/${clusterId}/events`,
  FIND_CPU_PROFILES_FROM_CLUSTER:(clusterId) =>  `/api/v1/computing/clusters/${clusterId}/cpuProfiles`, 
    
  ADD_CLUSTER: () => `/api/v1/computing/clusters`,
  EDIT_CLUSTER: (clusterId) => `/api/v1/computing/clusters/${clusterId}`, 
  DELETE_CLUSTER: (clusterId) => `/api/v1/computing/clusters/${clusterId}`, 
  // endregion: Cluster

  //region: Host
  FIND_ALL_HOSTS: () =>  `/api/v1/computing/hosts`,
  FIND_HOST: (hostId) =>  `/api/v1/computing/hosts/${hostId}`, 
  FIND_VMS_FROM_HOST:(hostId) =>  `/api/v1/computing/hosts/${hostId}/vms`, 

  FIND_HOST_NICS_FROM_HOST: (hostId) =>  `/api/v1/computing/hosts/${hostId}/nics`, 
  FIND_NETWORKS_FROM_HOST: (hostId) =>  `/api/v1/computing/hosts/${hostId}/network`, 
  SETUP_HOST_NICS_FROM_HOST: (hostId) =>  `/api/v1/computing/hosts/${hostId}/nics/setup`, 

  FIND_HOSTDEVICES_FROM_HOST:(hostId) =>  `/api/v1/computing/hosts/${hostId}/devices`, 
  FIND_EVENTS_FROM_HOST:(hostId) =>  `/api/v1/computing/hosts/${hostId}/events`, 

  ADD_HOST: () => `/api/v1/computing/hosts`,
  EDIT_HOST: (hostId) => `/api/v1/computing/hosts/${hostId}`, 
  DELETE_HOST: (hostId) => `/api/v1/computing/hosts/${hostId}`, 
  ACTIVATE_HOST: (hostId) => `/api/v1/computing/hosts/${hostId}/activate`, 
  DEACTIVATE_HOST: (hostId) => `/api/v1/computing/hosts/${hostId}/deactivate`, 
  RESTART_HOST: (hostId) => `/api/v1/computing/hosts/${hostId}/restart`, 
  //endregion: Host

  //region: Vm
  FIND_ALL_VMS: () =>  `/api/v1/computing/vms`,
  FIND_VM: (vmId) =>  `/api/v1/computing/vms/${vmId}`, 
  
  FIND_DISKS_FROM_VM: (vmId) =>  `/api/v1/computing/vms/${vmId}/disks`, 
  FIND_DISK_FROM_VM: (vmId, diskAttachmentId) =>  `/api/v1/computing/vms/${vmId}/disks/${diskAttachmentId}`,
  ADD_DISK_FROM_VM: (vmId) =>  `/api/v1/computing/vms/${vmId}/disks`, 
  EDIT_DISK_FROM_VM: (vmId) =>  `/api/v1/computing/vms/${vmId}/disks`,
  DELETE_DISKS_FROM_VM: (vmId) =>  `/api/v1/computing/vms/${vmId}/disks`, 
  ATTACH_DISKS_FROM_VM: (vmId) =>  `/api/v1/computing/vms/${vmId}/disks/attach`, 
  FIND_STORAGE_DOMAINS_FROM_VM: (vmId, diskAttachmentId) =>  `/api/v1/computing/vms/${vmId}/disks/${diskAttachmentId}/storageDomains`,
  ACTIVATE_DISKS_FROM_VM: (vmId) =>  `/api/v1/computing/vms/${vmId}/disks/activate`, 
  DEACTIVATE_DISKS_FROM_VM: (vmId) =>  `/api/v1/computing/vms/${vmId}/disks/deactivate`, 
  MOVE_DISK_FROM_VM: (vmId) =>  `/api/v1/computing/vms/${vmId}/disks/move`, 

  FIND_SNAPSHOTS_FROM_VM: (vmId) =>  `/api/v1/computing/vms/${vmId}/snapshots`, 
  FIND_SNAPSHOT_FROM_VM: (vmId, snapshotId) =>  `/api/v1/computing/vms/${vmId}/snapshots/${snapshotId}`, 
  ADD_SNAPSHOT_FROM_VM: (vmId) =>  `/api/v1/computing/vms/${vmId}/snapshots`, 
  DELETE_SNAPSHOTS_FROM_VM: (vmId) =>  `/api/v1/computing/vms/${vmId}/snapshots`, 
  PREVIEW_SNAPSHOT_FROM_VM: (vmId, snapshotId) =>  `/api/v1/computing/vms/${vmId}/snapshots/${snapshotId}/preview`,
  CLONE_SNAPSHOTS_FROM_VM: (vmId) =>  `/api/v1/computing/vms/${vmId}/snapshots/clone`, 
  COMMIT_SNAPSHOTS_FROM_VM: (vmId) =>  `/api/v1/computing/vms/${vmId}/snapshots/commit`, 
  UNDO_SNAPSHOTS_FROM_VM: (vmId) =>  `/api/v1/computing/vms/${vmId}/snapshots/undo`, 

  FIND_NICS_FROM_VM: (vmId) =>  `/api/v1/computing/vms/${vmId}/nics`, 
  FIND_NIC_FROM_VM: (vmId, nicId) =>  `/api/v1/computing/vms/${vmId}/nics/${nicId}`, 
  ADD_NICS_FROM_VM: (vmId) =>  `/api/v1/computing/vms/${vmId}/nics`, 
  EDIT_NIC_FROM_VM: (vmId, nicId) =>  `/api/v1/computing/vms/${vmId}/nics/${nicId}`, 
  DELETE_NIC_FROM_VM: (vmId, nicId) =>  `/api/v1/computing/vms/${vmId}/nics/${nicId}`, 

  FIND_APPLICATIONS_FROM_VM:(vmId) =>  `/api/v1/computing/vms/${vmId}/applications`, 
  FIND_HOST_DEVICES_FROM_VM:(vmId) =>  `/api/v1/computing/vms/${vmId}/hostDevices`, 
  FIND_EVENTS_FROM_VM:(vmId) =>  `/api/v1/computing/vms/${vmId}/events`, 

  FIND_NICS_FROM_CLUSTER:(clusterId) =>  `/api/v1/computing/vms/nic/${clusterId}`, 

  ADD_VM: () => `/api/v1/computing/vms`,
  EDIT_VM: (vmId) => `/api/v1/computing/vms/${vmId}`, 
  DELETE_VM: (vmId, detachOnly) => `/api/v1/computing/vms/${vmId}?detachOnly=${detachOnly}`,
  
  START_VM: (vmId) => `/api/v1/computing/vms/${vmId}/start`, 
  PAUSE_VM: (vmId) => `/api/v1/computing/vms/${vmId}/pause`, 
  POWER_OFF_VM: (vmId) => `/api/v1/computing/vms/${vmId}/powerOff`, 
  SHUT_DOWN_VM: (vmId) => `/api/v1/computing/vms/${vmId}/shutdown`, 
  REBOOT_VM: (vmId) => `/api/v1/computing/vms/${vmId}/reboot`, 
  RESET_VM: (vmId) => `/api/v1/computing/vms/${vmId}/reset`, 
  EXPORT_VM: (vmId) => `/api/v1/computing/vms/${vmId}/export`, 
  CONSOLE_VM: (vmId) => `/api/v1/computing/vms/${vmId}/console`, 
  MIGRATE_HOST_LIST_VM: (vmId) => `/api/v1/computing/vms/${vmId}/migrateHosts`, 
  MIGRATE_VM: (vmId, hostId) => `/api/v1/computing/vms/${vmId}/migrate/${hostId}`, 
  
  //endregion: Vm

  //region: Template
  FIND_ALL_TEMPLATES :() =>  `/api/v1/computing/templates`,
  FIND_TEMPLATE: (templateId) => `/api/v1/computing/templates/${templateId}`, 
  FIND_VMS_FROM_TEMPLATE: (templateId) => `/api/v1/computing/templates/${templateId}/vms`, 
  FIND_NICS_FROM_TEMPLATE: (templateId) => `/api/v1/computing/templates/${templateId}/nics`, 
  ADD_NICS_FROM_TEMPLATE: (templateId) => `/api/v1/computing/templates/${templateId}/nics`, 
  EDIT_NICS_FROM_TEMPLATE: (templateId,nicId) =>  `/api/v1/computing/templates/${templateId}/nics/${nicId}`,
  DELETE_NICS_FROM_TEMPLATE: (templateId,nicId) =>  `/api​/v1​/computing​/templates​/${templateId}​/nics​/${nicId}`, 
  FIND_DISKS_FROM_TEMPLATE: (templateId) => `/api/v1/computing/templates/${templateId}/disks`,  
  FIND_STORAGE_DOMAINS_FROM_TEMPLATE: (templateId) => `/api/v1/computing/templates/${templateId}/storageDomains`, 
  FIND_EVENTS_FROM_TEMPLATE: (templateId) => `/api/v1/computing/templates/${templateId}/events`, 

  ADD_TEMPLATE: () => `/api/v1/computing/templates`, 
  EDIT_TEMPLATE: (templateId) => `/api/v1/computing/templates/${templateId}`, 
  DELETE_TEMPLATE: (templateId) => `/api/v1/computing/templates/${templateId}`, 
  //endregion: Template

  //region: Network
  FIND_ALL_NETWORKS: () => `/api/v1/networks`,
  FIND_NETWORK: (networkId) => `/api/v1/networks/${networkId}`,

  FIND_VNIC_PROFILES_FROM_NETWORK: (networkId) =>  `/api/v1/networks/${networkId}/vnicProfiles`,
  FIND_VNIC_PROFILE_FROM_NETWORK: (networkId, vnicProfileId) =>  `/api/v1/networks/${networkId}/vnicProfiles/${vnicProfileId}`,
  ADD_VNIC_PROFILE_FROM_NETWORK: (networkId) =>  `/api/v1/networks/${networkId}/vnicProfiles`,
  EDIT_VNIC_PROFILE_FROM_NETWORK: (networkId, vnicProfileId) =>  `/api/v1/networks/${networkId}/vnicProfiles/${vnicProfileId}`,
  DELETE_VNIC_PROFILE_FROM_NETWORK: (networkId, vnicProfileId) =>  `/api/v1/networks/${networkId}/vnicProfiles/${vnicProfileId}`,

  FIND_CLUSTERS_FROM_NETWORK:(networkId) => `/api/v1/networks/${networkId}/clusters`,
  FIND_HOSTS_FROM_NETWORK:(networkId) => `/api/v1/networks/${networkId}/hosts`,
  FIND_VMS_FROM_NETWORK:(networkId) => `/api/v1/networks/${networkId}/vms`,
  FIND_TEMPLATES_NETWORK:(networkId) => `/api/v1/networks/${networkId}/templates`,

  ADD_NETWORK: () => `/api/v1/networks`,
  EDIT_NETWORK: (networkId) => `/api/v1/networks/${networkId}`,
  DELETE_NETWORK: (networkId) => `/api/v1/networks/${networkId}`,
  FIND_NETWORK_PROVIDERS: () => `/api/v1/networks/import/settings`,
  FIND_NETWORKS_FROM_PROVIDERS: (providerId) => `/api/v1/networks/import/settings/${providerId}`,
  FIND_DATA_CENTERS_FROM_NETWORK: (openstackNetworkId) => `/api/v1/networks/import/datacenters/${openstackNetworkId}`,
  IMPORT_NETWORK: () => `/api/v1/networks/import`,
  
  FIND_ALL_VNIC_PROFILES: () =>  `/api/v1/networks/vnicProfiles`,
  //endregion: Network

  //region: StorageDomain
  FIND_ALL_STORAGE_DOMAINS: () => `/api/v1/storages/domains`,
  FIND_STORAGE_DOMAIN: (storageDomainId) => `/api/v1/storages/domains/${storageDomainId}`,
  
  FIND_DATA_CENTERS_FROM_STORAGE_DOMAINS: (storageDomainId) => `/api/v1/storages/domains/${storageDomainId}/dataCenters`,    
  ACTIVATE_FROM_DATACENTER: (storageDomainId, dataCenterId) => `/api/v1/storages/domains/${storageDomainId}/dataCenters/${dataCenterId}/activate`,
  ATTACH_FROM_DATACENTER: (storageDomainId, dataCenterId) => `/api/v1/storages/domains/${storageDomainId}/dataCenters/${dataCenterId}/attach`,
  DETACH_FROM_DATACENTER: (storageDomainId, dataCenterId) => `/api/v1/storages/domains/${storageDomainId}/dataCenters/${dataCenterId}/detach`,
  MAINTENANCE_FROM_DATACENTER: (storageDomainId, dataCenterId) => `/api/v1/storages/domains/${storageDomainId}/dataCenters/${dataCenterId}/maintenance`,

  FIND_VMS_FROM_STORAGE_DOMAINS: (storageDomainId) => `/api/v1/storages/domains/${storageDomainId}/vms`,
  FIND_DISKS_FROM_STORAGE_DOMAINS: (storageDomainId) => `/api/v1/storages/domains/${storageDomainId}/disks`,
  FIND_DISK_PROFILES_FROM_STORAGE_DOMAINS: (storageDomainId) => `/api/v1/storages/domains/${storageDomainId}/diskProfiles`,
  FIND_DISK_SNAPSHOTS_FROM_STORAGE_DOMAINS: (storageDomainId) => `/api/v1/storages/domains/${storageDomainId}/diskSnapshots`,
  FIND_TEMPLATES_FROM_STORAGE_DOMAINS: (storageDomainId) => `/api/v1/storages/domains/${storageDomainId}/templates`,
  FIND_EVENTS_FROM_STORAGE_DOMAINS: (storageDomainId) => `/api/v1/storages/domains/${storageDomainId}/events`,
  FIND_ACTIVE_DATA_CENTERS: () => `/api/v1/storages/domains/dataCenters`,
  
  FIND_FIBRES_FROM_HOST: (hostId) => `/api/v1/computing/hosts/${hostId}/fibres`,
  FIND_ISCSIS_FROM_HOST: (hostId) => `/api/v1/computing/hosts/${hostId}/iscsis`,
  
  ADD_STORAGE_DOMAIN: () => `/api/v1/storages/domains`,
  EDIT_STORAGE_DOMAIN: (domainId) => `/api/v1/storages/domains/${domainId}`,
  DELETE_STORAGE_DOMAIN: (storageDomainId) => `/api/v1/storages/domains/${storageDomainId}`,

  DESTORY_STORAGE_DOMAIN: (storageDomainId) => `/api/v1/storages/domains/${storageDomainId}/destory`, 
  IMPORT_STORAGE_DOMAIN: () => `/api/v1/storages/domains/import`,

  //endregion: StorageDomain

  //region: Disk
  FIND_ALL_DISKS: () =>      `/api/v1/storages/disks`,
  FIND_DISK: (diskId) =>      `/api/v1/storages/disks/${diskId}`,
  FIND_VMS_FROM_DISK: (diskId) =>      `/api/v1/storages/disks/${diskId}/vms`,
  FIND_STORAGE_DOMAINS_FROM_DISK: (diskId) =>      `/api/v1/storages/disks/${diskId}/storageDomains`,
  FIND_DISK_LIST_FROM_VM:() =>  `/api/v1/storages/disks/attachImage`, 
  FIND_ISOS_FROM_VM:() =>  `/api/v1/storages/disks/iso`, 

  ADD_DISK: () =>      `/api/v1/storages/disks`,
  EDIT_DISK: (diskId) =>      `/api/v1/storages/disks/${diskId}`,
  DELETE_DISK: (diskId) =>      `/api/v1/storages/disks/${diskId}`,
  COPY_DISK: (diskId) =>      `/api/v1/storages/disks/${diskId}/copy`,
  FIND_STORAGE_DOMAINS_TO_MOVE_DISK: (diskId) =>      `/api/v1/storages/disks/${diskId}/move`,
  MOVE_DISK: (diskId) =>      `/api/v1/storages/disks/${diskId}/move`,
  REFRESH_LUN_DISK: (diskId) =>      `/api/v1/storages/disks/${diskId}/refreshLun`,
  UPLOAD_DISK: (diskId) =>      `/api/v1/storages/disks/upload`,
  //endregion: Disk
  

  //region: Event
  FIND_ALL_EVENTS: () => `/api/v1/events`,
  //endregion: Event
}

export default ENDPOINTS