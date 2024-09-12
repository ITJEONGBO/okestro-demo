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
  FIND_ALL_CLUSTERS_FROM_DATA_CENTER: (dataCenterId) => `/api/v1/computing/clusters`,
  //endregion: DataCenter

  //region: Host
  FIND_ALL_HOSTS: () =>  `/api/v1/computing/hosts`,
  //endregion: Host

  //region: Vm/Template
  FIND_ALL_VM_CHART: () =>  `/api/v1/computing/vms`,
  FIND_ALL_TEMPLATE_CHART :() =>  `/api/v1/computing/templates`,
  //endregion: Vm/Template


  // region: Cluster
  FIND_ALL_CLUSTERS: () =>      `/api/v1/computing/clusters`,
  FIND_CLUSTERS_BY_ID: (clusterId) =>  `/api/v1/computing/clusters/${clusterId}`, 
  FIND_LOGICAL_FROM_CLUSTERS:(clusterId) =>  `/api/v1/computing/clusters/${clusterId}/networks`, 
  FIND_HOST_FROM_CLUSTERS:(clusterId) =>  `/api/v1/computing/clusters/${clusterId}/hosts`, 
  FIND_VM_FROM_CLUSTERS:(clusterId) =>  `/api/v1/computing/clusters/${clusterId}/vms`, 
  FIND_PERMISSIONS_FROM_CLUSTERS:(clusterId) =>  `/api/v1/computing/clusters/${clusterId}/permissions`, 
  FIND_EVENT_FROM_CLUSTERS: (clusterId) => `/api/v1/computing/clusters/${clusterId}/events`,
  // endregion: Cluster

  //region: Network
  FIND_ALL_NETWORKS: () =>      `/api/v1/networks`,
  FIND_NETWORK_BY_ID: (networkId) =>  `/api/v1/networks/${networkId}`,
  FIND_ALL_VNIC_PROFILES_FROM_NETWORK: (networkId) =>  `/api/v1/networks/${networkId}/vnics`, // vnics
  FIND_ALL_CLUSTERS_FROM_NETWORK:(networkId) => `/api/v1/networks/${networkId}/clusters`,
  FIND_ALL_HOST_FROM_NETWORK:(networkId) => `​/api​/v1​/networks​/${networkId}​/hosts`,
  FIND_ALL_VMS_FROM_NETWORK:(networkId) => `/api/v1/networks/${networkId}/vms`,
  FIND_ALL_TEMPLATES_NETWORK:(networkId) => `/api/v1/networks/${networkId}/templates`,
  FIND_ALL_PERMISSION_NETWORK:(networkId) => `/api/v1/networks/${networkId}/permissions`,
  //endregion: Network

  //region: StorageDomain
  FIND_ALL_STORAGE_DOMAINS: async () => `/api/v1/storages`, // TODO: 구현필요
  //endregion: StorageDomain
}

export default ENDPOINTS