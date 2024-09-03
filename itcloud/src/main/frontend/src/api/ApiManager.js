import ENDPOINTS from "./Endpoints"
import DEFAULT_VALUES from "./DefaultValues"
import axios from 'axios';

axios.defaults.baseURL = 'https://' + window.location.hostname + ":" + 8443
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

/**
 * @name makeAPICall
 * @description axios API 호출
 * 
 * @param {string} url 주소
 * @param {*} defaultValues 기본값
 * @returns 결과값
 */
const makeAPICall = async (url, defaultValues) => {
  try {
    const res = await axios.get(url)
    return res.data?.body
  } catch(e) {
    console.error(`Error fetching '${url}':`, e);
    return defaultValues;
  }
}

const ApiManager = {
  //region: TreeNavigation
  /**
   * @name ApiManager.findAllTreeNaviations
   * @description cpu, memory api 불러오는 값
   * 
   * @returns 
   * 
   * @see Dashboard.js (components)
   */
  findAllTreeNaviations: async (type = "none") => makeAPICall(ENDPOINTS.FIND_ALL_TREE_NAVIGATIONS(type). DEFAULT_VALUES.FIND_ALL_TREE_NAVIGATIONS),
  //endregion

  //region: Dashboard
  /**
   * @name ApiManager.getDashboard
   * @description cpu, memory api 불러오는 값
   * 
   * @returns 
   * 
   * @see Dashboard.js (components)
   */
  getDashboard: async () => makeAPICall(ENDPOINTS.GET_DASHBOARD(), DEFAULT_VALUES.GET_DASHBOARD),
  /**
   * @name ApiManager.getCpuMemory
   * @description cpu, memory api 불러오는 값
   * 
   * @returns 
   * 
   * @see Dashboard.js (components)
   */
  getCpuMemory: async () => makeAPICall(ENDPOINTS.GET_CPU_MEMERY(), DEFAULT_VALUES.GET_CPU_MEMERY),
  /**
   * @name ApiManager.getStorage
   * @description storage 불러오는 값
   * 
   * @returns 
   * 
   * @see Dashboard.js (components)
   */
  getStorage: async () => makeAPICall(ENDPOINTS.GET_STORAGE(), DEFAULT_VALUES.GET_STORAGE),
  /**
   * @name ApiManager.getVmCpu
   * @description vmCpu 불러오는 값
   * 
   * @returns 
   * 
   * @see Dashboard.js (components)
   */
  getVmCpu: async () => makeAPICall(ENDPOINTS.GET_VM_CPU()),
  /**
   * @name ApiManager.getVmMemory
   * @description vmMemory 불러오는 값
   * 
   * @returns 
   * 
   * @see Dashboard.js (components)
   */
  getVmMemory: async () => makeAPICall(ENDPOINTS.GET_VM_MEMORY()),
  /**
   * @name ApiManager.getStorageMemory
   * @description storageMemory 불러오는 값
   * 
   * @returns 
   * 
   * @see Dashboard.js (components)
   */
  getStorageMemory: async () => makeAPICall(ENDPOINTS.GET_STORAGE_MEMROY()),
  //endregion: Dashboard

  //region: DataCenter
  /**
   * @name ApiManager.findAllDataCenters
   * @description datacenter 목록 
   * @todo 백엔드 구현필요!
   * 
   * @returns 
   * 
   * @see Computing.js (components/Computing)
   */
  findAllDataCenters: async () => makeAPICall(ENDPOINTS.FIND_ALL_DATA_CENTERS(), DEFAULT_VALUES.FIND_ALL_DATA_CENTERS),
  /**
   * @name findAllClustersFromDataCenter
   * @description
   * 
   * @returns 
   */
  findAllClusters: async () => makeAPICall(ENDPOINTS.FIND_ALL_CLUSTERS(), DEFAULT_VALUES.FIND_ALL_CLUSTERS),
  //endregion: DataCenter

  //region: Network
  /**
   * 
   */
  findAllNetworks: async () => makeAPICall(ENDPOINTS.FIND_ALL_NETWORKS(), DEFAULT_VALUES.FIND_ALL_NETWORKS),
  findNetworkById: async (networkId) => makeAPICall(ENDPOINTS.FIND_NETWORK_BY_ID(networkId), DEFAULT_VALUES.FIND_NETWORK_BY_ID),
  findAllVnicProfilesFromNetwork: async (networkId) => makeAPICall(ENDPOINTS.FIND_ALL_VNIC_PROFILES_FROM_NETWORK(networkId), DEFAULT_VALUES.FIND_ALL_VNIC_PROFILES_FROM_NETWORK),
  //endregion: Network
  
  //region: StorageDomain
  findAllStorage: async () => makeAPICall(ENDPOINTS.FIND_ALL_STORAGE(), DEFAULT_VALUES.FIND_ALL_STORAGE),
  findAllStorageDomains: async () => makeAPICall(ENDPOINTS.FIND_ALL_STORAGE_DOMAINS(), DEFAULT_VALUES.FIND_ALL_STORAGE_DOMAINS),
  //endregion: StorageDomain
}

export default ApiManager