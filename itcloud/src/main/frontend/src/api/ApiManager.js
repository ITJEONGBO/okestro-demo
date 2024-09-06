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

  //region: Dashboard--------------------------------------------
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

  //region : Cluster--------------------------------------------
  /**
   * 
   * @returns 
   **/
  findAllClusterById: async (clusterId) => makeAPICall(ENDPOINTS.FIND_CLUSTERS_BY_ID(clusterId), DEFAULT_VALUES.FIND_CLUSTERS_BY_ID),
  findLogicalFromCluster : async (clusterId) => makeAPICall(ENDPOINTS.FIND_LOGICAL_FROM_CLUSTERS(clusterId), DEFAULT_VALUES.FIND_HOST_FROM_CLUSTER),
  findHostFromCluster : async (clusterId) => makeAPICall(ENDPOINTS.FIND_HOST_FROM_CLUSTERS(clusterId), DEFAULT_VALUES.FIND_HOST_FROM_CLUSTER),
  findVMFromCluster : async (clusterId) => makeAPICall(ENDPOINTS.FIND_VM_FROM_CLUSTERS(clusterId), DEFAULT_VALUES.FIND_VM_FROM_CLUSTER),
  findPermissionsFromCluster : async (clusterId) => makeAPICall(ENDPOINTS.FIND_PERMISSIONS_FROM_CLUSTERS(clusterId), DEFAULT_VALUES.FIND_ALL_PERMISSION),
  findEventFromCluster: async (clusterId) => makeAPICall(ENDPOINTS.FIND_EVENT_FROM_CLUSTERS(clusterId), DEFAULT_VALUES.FIND_EVENT_FROM_CLUSTER),
  //endregion: Cluster


  //region : Host--------------------------------------------
  /**
   * 
   * @returns 
   **/
  findAllHosts : async () => makeAPICall(ENDPOINTS.FIND_ALL_HOSTS(), DEFAULT_VALUES.FIND_ALL_HOSTS),
  //endregion: Host


  //region : VM/Template --------------------------------------------
  findAllVMs : async () => makeAPICall(ENDPOINTS.FIND_ALL_VM_CHART(), DEFAULT_VALUES.FIND_ALL_VMS),
  findAllTemplates : async () => makeAPICall(ENDPOINTS.FIND_ALL_TEMPLATE_CHART(), DEFAULT_VALUES.FIND_ALL_TEMPLATES),
  //region: Network------------------------------------------------
  /**
   * 
   */
  findAllNetworks: async () => makeAPICall(ENDPOINTS.FIND_ALL_NETWORKS(), DEFAULT_VALUES.FIND_ALL_NETWORKS),
  findNetworkById: async (networkId) => makeAPICall(ENDPOINTS.FIND_NETWORK_BY_ID(networkId), DEFAULT_VALUES.FIND_NETWORK_BY_ID),
  findAllVnicProfilesFromNetwork: async (networkId) => makeAPICall(ENDPOINTS.FIND_ALL_VNIC_PROFILES_FROM_NETWORK(networkId), DEFAULT_VALUES.FIND_ALL_VNIC_PROFILES_FROM_NETWORK),
  findAllClustersFromNetwork : async (networkId) => makeAPICall(ENDPOINTS.FIND_ALL_CLUSTERS_FROM_NETWORK(networkId), DEFAULT_VALUES.FIND_ALL_CLUSTERS_FROM_NETWORK),
  findAllHostsFromNetwork : async (networkId) => makeAPICall(ENDPOINTS.FIND_ALL_HOST_FROM_NETWORK(networkId), DEFAULT_VALUES.FIND_ALL_HOST_FROM_NETWORK),
  findAllVmsFromNetwork : async (networkId) => makeAPICall(ENDPOINTS.FIND_ALL_VMS_FROM_NETWORK(networkId), DEFAULT_VALUES.FIND_ALL_VMS_FROM_NETWORK),
  findAllTemplatesFromNetwork : async (networkId) => makeAPICall(ENDPOINTS.FIND_ALL_TEMPLATES_NETWORK(networkId), DEFAULT_VALUES.FIND_ALL_TEMPLATES_FROM_NETWORK),
  findAllPermissionFromNetwork : async (networkId) => makeAPICall(ENDPOINTS.FIND_ALL_PERMISSION_NETWORK(networkId), DEFAULT_VALUES.FIND_ALL_PERMISSION),
  //endregion: Network
  

  
  //region: StorageDomain
  findAllStorageDomains: async () => makeAPICall(ENDPOINTS.FIND_ALL_STORAGE_DOMAINS(), DEFAULT_VALUES.FIND_ALL_STORAGE_DOMAINS),
  //endregion: StorageDomain
}

export default ApiManager