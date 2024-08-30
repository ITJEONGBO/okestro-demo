const ENDPOINTS = {
  //region: Dashboard
  GET_DASHBOARD: () =>          `/api/v1/dashboard`,
  GET_CPU_MEMERY: () =>         `/api/v1/dashboard/cpumemory`,
  GET_STORAGE: () =>            `/api/v1/dashboard/storage`,
  GET_VM_CPU: () =>             `/api/v1/dashboard/vmCpu`,
  GET_VM_MEMORY: () =>          `/api/v1/dashboard/vmMemory`,
  GET_STORAGE_MEMROY: () =>     `/api/v1/dashboard/storageMemory`,
  //endregion: Dashboard
  
  //region: DataCenter
  FIND_ALL_DATA_CENTERS: () =>  `/api/v1/computing/datacenters`,
  FIND_ALL_CLUSTERS: () =>      `/api/v1/computing/clusters`,
  FIND_ALL_CLUSTERS_FROM_DATA_CENTER: (dataCenterId) => `/api/v1/computing/clusters`,
  //endregion: DataCenter

  //region: Network
  FIND_ALL_NETWORKS: () =>      `/api/v1/networks`, // TODO: 구현필요
  //endregion: Network

  //region: StorageDomain
  FIND_ALL_STORAGE_DOMAINS: async () => `/api/v1/storages`, // TODO: 구현필요
  //endregion: StorageDomain
}

export default ENDPOINTS