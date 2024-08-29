const ENDPOINTS = {
  //region: Dashboard
  GET_DASHBOARD: () =>      `/api/v1/dashboard`,
  GET_CPU_MEMERY: () =>     `/api/v1/dashboard/cpumemory`,
  GET_STORAGE: () =>        `/api/v1/dashboard/storage`,
  GET_VM_CPU: () =>         `/api/v1/dashboard/vmCpu`,
  GET_VM_MEMORY: () =>      `/api/v1/dashboard/vmMemory`,
  GET_STORAGE_MEMROY: () => `/api/v1/dashboard/storageMemory`,
  //endregion: Dashboard
  //region: DataCenter
  FIND_ALL_DATA_CENTERS: () => `/api/v1/computing/datacenters`,
  //endregion: DataCenter
}

export default ENDPOINTS