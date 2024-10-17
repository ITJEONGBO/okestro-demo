import ENDPOINTS from "./Endpoints"
import DEFAULT_VALUES from "./DefaultValues"
import axios from 'axios';
import { toast } from 'react-hot-toast';

axios.defaults.baseURL = 'https://' + window.location.hostname + ":" + 8443
axios.defaults.headers.post['Content-Type'] = 'application/json';

/**
 * @name makeAPICall
 * @description axios API 호출
 * 
 * @param {*} 파라미터
 * @returns 결과값
 */
const makeAPICall = async ({method = "GET", url, data, defaultValues}) => {
  try {
    const res = (data == null || data == undefined) ? await axios.get(url) : await axios({
      method: method,
      url: url,
      headers: { 
        // TODO: access_token으로 모든 API 처리하기
      },
      data: data
    }); 
    res.headers.get(`access_token`) && localStorage.setItem('token', res.headers.get(`access_token`)) // 로그인이 처음으로 성공했을 때 진행
    return res.data?.body
  } catch(e) {
    console.error(`Error fetching '${url}':`, e);
    toast.error(`Error fetching '${url}'\n${e.message}`)
    if (defaultValues) return defaultValues; 
  }
}

const ApiManager = {
  //region: User
  /**
   * @name ApiManager.authenticate
   * @description 로그인
   * 
   * @returns 
   * 
   * @see Login.js (page)
   */
  authenticate: async(username, password) => makeAPICall({
    method: "POST", 
    url: ENDPOINTS.FIND_USER(username), 
    data: { password: password }
  }),
  //endregion: User

  //region: TreeNavigation
  /**
   * @name ApiManager.findAllTreeNaviations
   * @description cpu, memory api 불러오는 값
   * 
   * @returns 
   * 
   * @see Dashboard.js (components)
   */
  findAllTreeNaviations: async (type = "none") => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_ALL_TREE_NAVIGATIONS(type), 
    defaultValues: DEFAULT_VALUES.FIND_ALL_TREE_NAVIGATIONS
  }),
  //endregion: TreeNavigation

  //region: Dashboard--------------------------------------------
  /**
   * @name ApiManager.getDashboard
   * @description cpu, memory api 불러오는 값
   * 
   * @returns 
   * 
   * @see Dashboard.js (components)
   */
  getDashboard: async () => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.GET_DASHBOARD(), 
    defaultValues: DEFAULT_VALUES.GET_DASHBOARD
  }),
  /**
   * @name ApiManager.getCpuMemory
   * @description cpu, memory api 불러오는 값
   * 
   * @returns 
   * 
   * @see Dashboard.js (components)
   */
  getCpuMemory: async () => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.GET_CPU_MEMORY(), 
    defaultValues: DEFAULT_VALUES.GET_CPU_MEMORY
  }),
  /**
   * @name ApiManager.getStorage
   * @description storage 불러오는 값
   * 
   * @returns 
   * 
   * @see Dashboard.js (components)
   */
  getStorage: async () => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.GET_STORAGE(), 
    defaultValues: DEFAULT_VALUES.GET_STORAGE
  }),
  /**
   * @name ApiManager.getVmCpu
   * @description vmCpu 불러오는 값
   * 
   * @returns 
   * 
   * @see Dashboard.js (components)
   */
  getVmCpu: async () => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.GET_VM_CPU(),
  }),
  /**
   * @name ApiManager.getVmMemory
   * @description vmMemory 불러오는 값
   * 
   * @returns 
   * 
   * @see Dashboard.js (components)
   */
  getVmMemory: async () => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.GET_VM_MEMORY()
  }),
  /**
   * @name ApiManager.getStorageMemory
   * @description storageMemory 불러오는 값
   * 
   * @returns 
   * 
   * @see Dashboard.js (components)
   */
  getStorageMemory: async () => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.GET_STORAGE_MEMORY()
  }),
  //endregion: Dashboard

  //region: DataCenter
  /**
   * @name ApiManager.findAllDataCenters
   * @description datacenter 목록 
   *
   * @returns 
   * 
   * @see Computing.js (components/Computing)
   */
  findAllDataCenters: async () => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_ALL_DATA_CENTERS(), 
    defaultValues: DEFAULT_VALUES.FIND_ALL_DATA_CENTERS
  }),
    /**
   * @name ApiManager.findDataCenter
   * @description datacenter
   *
   * @returns 
   * 
   * @see Computing.js (components/Computing)
   */
    findDataCenter: async (dataCenterId) => makeAPICall({ 
      method: "GET",  
      url: ENDPOINTS.FIND_DATA_CENTER(dataCenterId), 
      defaultValues: DEFAULT_VALUES.FIND_DATACENTER
    }),
  /**
   * @name findAllClustersFromDataCenter
   * @description 
   * 
   * @returns 
   */
  findAllClustersFromDataCenter: async () => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_CLUSTERS_FROM_DATA_CENTER(), 
    defaultValues: DEFAULT_VALUES.FIND_CLUSTERS_FROM_DATA_CENTER
  }),
  /**
   * @name findAllHostsFromDataCenter
   * @description 
   * 
   * @returns 
   */
  findAllHostsFromDataCenter: async () => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_HOSTS_FROM_DATA_CENTER(), 
    defaultValues: DEFAULT_VALUES.FIND_HOSTS_FROM_DATA_CENTER
  }),
  /**
   * @name findAllVmsFromDataCenter
   * @description 
   * 
   * @returns 
   */
  findAllVmsFromDataCenter: async () => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_VMS_FROM_DATA_CENTER(), 
    defaultValues: DEFAULT_VALUES.FIND_ALL_VMS
  }),
  /**
   * @name findAllDomainsFromDataCenter
   * @description 
   * 
   * @returns 
   */
  findAllDomainsFromDataCenter: async () => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_STORAGE_DOMAINS_FROM_DATA_CENTER(), 
    defaultValues: DEFAULT_VALUES.FIND_ALL_STORAGE_DOMAINS
  }),
  /**
   * @name findAllEventsFromDataCenter
   * @description 
   * 
   * @returns 
   */
  findAllEventsFromDataCenter: async () => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_EVENTS_FROM_DATA_CENTER(), 
    defaultValues: DEFAULT_VALUES.FIND_EVENT
  }),
    /**
   * @name findAllNetworksFromDataCenter
   * @description 
   * 
   * @returns 
   */
    findAllNetworksFromDataCenter: async () => makeAPICall({
      method: "GET", 
      url: ENDPOINTS.FIND_NETWORKS_FROM_DATA_CENTER(), 
      defaultValues: DEFAULT_VALUES.FIND_NETWORKS_FROM_DATA_CENTER
    }),

    /**
   * @name ApiManager.addDataCenter
   * @description 새 데이터센터 추가
   * 
   * @param {Object} dataCenterData - 추가할 데이터센터 정보
   * @returns {Promise<Object>} API 응답 결과
   */
  addDataCenter: async (dataCenterData) => {
    return makeAPICall({
      method: "POST",
      url: ENDPOINTS.ADD_DATA_CENTER(),
      data: dataCenterData, // POST 요청 시 전송할 데이터
      defaultValues: DEFAULT_VALUES.ADD_DATA_CENTER
    });
  },

  //endregion: DataCenter

  //region : Cluster--------------------------------------------
  /**
   * 
   * @returns 
   **/
  findAllClusters: async ()  => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_ALL_CLUSTERS(), 
    defaultValues: DEFAULT_VALUES.FIND_ALL_CLUSTERS
  }),
  findAllClusterById: async (clusterId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_CLUSTERS_BY_ID(clusterId), 
    defaultValues: DEFAULT_VALUES.FIND_CLUSTERS_BY_ID
  }),
  findLogicalFromCluster : async (clusterId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_LOGICAL_FROM_CLUSTERS(clusterId), 
    defaultValues: DEFAULT_VALUES.FIND_HOST_FROM_CLUSTER
  }),
  findHostFromCluster : async (clusterId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_HOST_FROM_CLUSTERS(clusterId), 
    defaultValues: DEFAULT_VALUES.FIND_HOST_FROM_CLUSTER
  }),
  findVMFromCluster : async (clusterId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_VM_FROM_CLUSTERS(clusterId), 
    defaultValues: DEFAULT_VALUES.FIND_VM_FROM_CLUSTER
  }),
  findPermissionsFromCluster : async (clusterId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_PERMISSIONS_FROM_CLUSTERS(clusterId), 
    defaultValues: DEFAULT_VALUES.FIND_ALL_PERMISSION
  }),
  findEventFromCluster: async (clusterId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_EVENT_FROM_CLUSTERS(clusterId), 
    defaultValues: DEFAULT_VALUES.FIND_EVENT_FROM_CLUSTER
  }),
  //endregion: Cluster


  //region : Host--------------------------------------------
  /**
   * 
   * @returns 
   **/
  findAllHosts : async () => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_ALL_HOSTS(), 
    defaultValues: DEFAULT_VALUES.FIND_ALL_HOSTS
  }),

  findAllHostById : async (hostId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_HOST(hostId), 
    defaultValues: DEFAULT_VALUES.FIND_HOST
  }),
  findVmFromHost : async (hostId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_VM_FROM_HOST(hostId), 
    defaultValues: DEFAULT_VALUES.FIND_HOST_FROM_CLUSTER
  }),
  findHostdeviceFromHost : async (hostId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_HOSTDEVICE_FROM_HOST(hostId), 
    defaultValues: DEFAULT_VALUES.FIND_DEVICE_FROM_HOST
  }),
  findPermissionsFromHost: async (hostId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_PERMISSIONS_FROM_HOST(hostId), 
    defaultValues: DEFAULT_VALUES.FIND_ALL_PERMISSION
  }),
  findEventsFromHost: async (hostId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_EVENT_FROM_HOST(hostId), 
    defaultValues: DEFAULT_VALUES.FIND_EVNET_FROM_HOST
  }),
  //endregion: Host


  //region : VM/Template --------------------------------------------
  /**
   * @name ApiManager.findAllVMs
   * 
   * @returns 
   **/
  findAllVMs : async () => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_ALL_VMS(), 
    defaultValues: DEFAULT_VALUES.FIND_ALL_VMS
  }),
  findAllTemplates : async () => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_ALL_TEMPLATE_CHART(), 
    defaultValues: DEFAULT_VALUES.FIND_ALL_TEMPLATES
  }),
  //region: Network------------------------------------------------
  /**
   * 
   */
  findAllNetworks: async () => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_ALL_NETWORKS(),
    defaultValues: DEFAULT_VALUES.FIND_ALL_NETWORKS
  }),
  findNetworkById: async (networkId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_NETWORK(networkId),
    defaultValues: DEFAULT_VALUES.FIND_NETWORK_BY_ID
  }),
  findAllVnicProfilesFromNetwork: async (networkId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_VNIC_PROFILES_FROM_NETWORK(networkId),
    defaultValues: DEFAULT_VALUES.FIND_ALL_VNIC_PROFILES_FROM_NETWORK
  }),
  findAllClustersFromNetwork : async (networkId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_CLUSTERS_FROM_NETWORK(networkId),
    defaultValues: DEFAULT_VALUES.FIND_ALL_CLUSTERS_FROM_NETWORK
  }),
  findAllHostsFromNetwork : async (networkId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_HOSTS_FROM_NETWORK(networkId),
    defaultValues: DEFAULT_VALUES.FIND_ALL_HOST_FROM_NETWORK
  }),
  findAllVmsFromNetwork : async (networkId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_VMS_FROM_NETWORK(networkId),
    defaultValues: DEFAULT_VALUES.FIND_ALL_VMS_FROM_NETWORK
  }),
  findAllTemplatesFromNetwork : async (networkId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_TEMPLATES_NETWORK(networkId),
    defaultValues: DEFAULT_VALUES.FIND_ALL_TEMPLATES_FROM_NETWORK
  }),
  findAllPermissionFromNetwork : async (networkId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_ALL_PERMISSION_NETWORK(networkId),
    defaultValues: DEFAULT_VALUES.FIND_ALL_PERMISSION
  }),
  //endregion: Network
  

  
  //region: Domain
  findAllStorageDomains: async () => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_ALL_STORAGE_DOMAINS(),
    defaultValues: DEFAULT_VALUES.FIND_ALL_STORAGE_DOMAINS
  }),
  findDomainById: async (storageDomainId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_STORAGE_DOMAIN(storageDomainId),
    defaultValues: DEFAULT_VALUES.FIND_DOMAIN_BY_ID
  }),
  findAllDataCenterFromDomain : async (storageDomainId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_DATA_CENTERS_FROM_STORAGE_DOMAINS(storageDomainId),
    defaultValues: DEFAULT_VALUES.FIND_DATACENTER_FROM_DOMAIN
  }),
  
  findAllDiskFromDomain: async (storageDomainId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_DISKS_FROM_STORAGE_DOMAINS(storageDomainId),
    defaultValues: DEFAULT_VALUES.FIND_DISK_FROM_DOMAIN
  }),
  findAllDiskSnapshotFromDomain: async (storageDomainId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_DISK_SNAPSHOTS_FROM_STORAGE_DOMAINS(storageDomainId),
    defaultValues: DEFAULT_VALUES.DISK_SNAPSHOT_FROM_DOMAIN
  }),
  findAllTemplateFromDomain: async (storageDomainId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_TEMPLATES_FROM_STORAGE_DOMAINS(storageDomainId),
    defaultValues: DEFAULT_VALUES.TEMPLATE_FROM_DOMAIN
  }),
  findAllEventFromDomain: async (storageDomainId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_EVENTS_FROM_STORAGE_DOMAINS(storageDomainId),
    defaultValues: DEFAULT_VALUES.FIND_EVENT
  }),
  //endregion: Domain



  //region: Disk
  findAllDisk: async () => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_ALL_DISKS(),
    defaultValues: DEFAULT_VALUES.FIND_ALL_DISK
  }),
  findDiskById: async (diskId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_DISK(diskId),
    defaultValues: DEFAULT_VALUES.FIND_DISK_BY_ID
  }),
  findAllVmsFromDisk: async (diskId) => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_VMS_DISK(diskId),
    defaultValues: DEFAULT_VALUES.VMS_FROM_DISK
  }),
  //endregion: Disk

  //region: event
  findAllEvent: async () => makeAPICall({
    method: "GET", 
    url: ENDPOINTS.FIND_ALL_EVENT(),
    defaultValues: DEFAULT_VALUES.FIND_ALL_EVENT
  }),
  //endregion: event

}





export default ApiManager