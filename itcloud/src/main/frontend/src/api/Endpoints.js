const ENDPOINTS = {  
  //region: User
  USERS: () => `/api/v1/auth/users`,
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
  //endregion: Dashboard
  
  //region: DataCenter
  FIND_ALL_DATA_CENTERS: () =>  `/api/v1/computing/datacenters`,
  FIND_DATA_CENTER: (dataCenterId) =>  `/api/v1/computing/datacenters/${dataCenterId}`, 
  FIND_CLUSTERS_FROM_DATA_CENTER: (dataCenterId) => `/api/v1/computing/datacenters/${dataCenterId}/clusters`, 
  FIND_HOSTS_FROM_DATA_CENTER: (dataCenterId) => `/api/v1/computing/datacenters/${dataCenterId}/hosts`, 
  FIND_VMS_FROM_DATA_CENTER: (dataCenterId) => `/api/v1/computing/datacenters/${dataCenterId}/vms`, 
  FIND_STORAGE_DOMAINS_FROM_DATA_CENTER: (dataCenterId) => `/api/v1/computing/datacenters/${dataCenterId}/storageDomains`, 
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
  EDIT_DISK_FROM_VM: (vmId, diskAttachmentId) =>  `/api/v1/computing/vms/${vmId}/disks/${diskAttachmentId}`,
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
  REMOVE_NIC_FROM_VM: (vmId, nicId) =>  `/api/v1/computing/vms/${vmId}/nics/${nicId}`, 

  FIND_APPLICATIONS_FROM_VM:(vmId) =>  `/api/v1/computing/vms/${vmId}/applications`, 
  FIND_HOST_DEVICES_FROM_VM:(vmId) =>  `/api/v1/computing/vms/${vmId}/hostDevices`, 
  FIND_EVENTS_FROM_VM:(vmId) =>  `/api/v1/computing/vms/${vmId}/events`, 

  FIND_DISKS_FROM_VM:() =>  `/api/v1/computing/vms/disks`, 
  FIND_ISOS_FROM_VM:() =>  `/api/v1/computing/vms/iso`, 
  FIND_NICS_FROM_VM:(clusterId) =>  `/api/v1/computing/vms/nic/${clusterId}`, 

  ADD_VM: () => `/api/v1/computing/vms`,
  EDIT_VM: (vmId) => `/api/v1/computing/vms/${vmId}`, 
  DELETE_VM: (vmId) => `/api/v1/computing/vms/${vmId}`, 
  START_VM: (vmId) => `/api/v1/computing/vms/${vmId}/start`, 
  PAUSE_VM: (vmId) => `/api/v1/computing/vms/${vmId}/pause`, 
  ACTIVATE_VM: (vmId) => `/api/v1/computing/vms/${vmId}/activate`, 
  DEACTIVATE_VM: (vmId) => `/api/v1/computing/vms/${vmId}/deactivate`, 
  REBOOT_VM: (vmId) => `/api/v1/computing/vms/${vmId}/reboot`, 
  POWER_OFF_VM: (vmId) => `/api/v1/computing/vms/${vmId}/powerOff`, 
  SHUT_DOWN_VM: (vmId) => `/api/v1/computing/vms/${vmId}/shutdown`, 
  RESET_VM: (vmId) => `/api/v1/computing/vms/${vmId}/reset`, 
  EXPORT_VM: (vmId) => `/api/v1/computing/vms/${vmId}/export`, 
  MIGRATE_HOST_LIST_VM: (vmId, hostId) => `/api/v1/computing/vms/${vmId}/migrateHosts`, 
  MIGRATE_VM: (vmId, hostId) => `/api/v1/computing/vms/${vmId}/migrate/${hostId}`, 
  CONSOLE_VM: (vmId) => `/api/v1/computing/vms/${vmId}/console`, 
  //endregion: Vm

  //region: Template
  FIND_ALL_TEMPLATES :() =>  `/api/v1/computing/templates`,
  FIND_TEMPLATE: (templateId) => `/api/v1/computing/templates/${templateId}`, 
  FIND_VMS_FROM_TEMPLATE: (templateId) => `/api/v1/computing/templates/${templateId}/vms`, 
  FIND_NICS_FROM_TEMPLATE: (templateId) => `/api/v1/computing/templates/${templateId}/nics`, 
  ADD_NICS_FROM_TEMPLATE: (templateId) => `/api/v1/computing/templates/${templateId}/nics`, 
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

  FIND_ALL_VNIC_PROFILES: () =>  `/api/v1/networks/vnicProfiles`,
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
  //endregion: Network

  //region: StorageDomain
  FIND_ALL_STORAGE_DOMAINS: () => `/api/v1/storages/domains`,
  FIND_STORAGE_DOMAIN: (storageDomainId) => `/api/v1/storages/domains/${storageDomainId}`,
  FIND_STORAGE_DOMAINS_FROM_DATA_CENTER: (dataCenterId) => `/api/v1/storages/${dataCenterId}/domains`,

  
  FIND_EVENTS_FROM_STORAGE_DOMAINS: (storageDomainId) => `/api/v1/storages/domains/${storageDomainId}/events`,

  FIND_ALL_DISK: () =>      `/api/v1/storages/disks`,
  FIND_DISKS_FROM_STORAGE_DOMAINS: (storageDomainId) => `/api/v1/storages/domains/${storageDomainId}/disks`,
  
  //endregion: StorageDomain

  //region: Event
  FIND_ALL_EVENT: () => `/api/v1/events`,
  //endregion: Event
}

export default ENDPOINTS