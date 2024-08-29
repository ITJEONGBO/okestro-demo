import ENDPOINTS from "./Endpoints"
import axios from 'axios';

axios.defaults.baseURL = 'https://' + window.location.hostname + ":" + 8443
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

const makeAPICall = async (url) => {
  try {
    const res = await axios.get(url)
    return res.data?.body
  } catch(e) {
    console.error(`Error fetching '${url}':`, e);
    return {};
  }
}

const ApiManager = {
  /**
   * @name ApiManager.getDashboard
   * @description cpu, memory api 불러오는 값
   * 
   * @returns 
   */
  getDashboard: async () => makeAPICall(ENDPOINTS.GET_DASHBOARD()),
  /**
   * @name ApiManager.getCpuMemory
   * @description cpu, memory api 불러오는 값
   * 
   * @returns 
   */
  getCpuMemory: async () => makeAPICall(ENDPOINTS.GET_CPU_MEMERY()),
  /**
   * @name ApiManager.getStorage
   * @description storage 불러오는 값
   * 
   * @returns 
   */
  getStorage: async () => makeAPICall(ENDPOINTS.GET_STORAGE()),
  /**
   * @name ApiManager.getVmCpu
   * @description vmCpu 불러오는 값
   * 
   * @returns 
   */
  getVmCpu: async () => makeAPICall(ENDPOINTS.GET_VM_CPU()),
  /**
   * @name ApiManager.getVmMemory
   * @description vmMemory 불러오는 값
   * 
   * @returns 
   */
  getVmMemory: async () => makeAPICall(ENDPOINTS.GET_VM_MEMORY()),
  /**
   * @name ApiManager.getStorageMemory
   * @description storageMemory 불러오는 값
   * 
   * @returns 
   */
  getStorageMemory: async () => makeAPICall(ENDPOINTS.GET_STORAGE_MEMROY()),
}

export default ApiManager