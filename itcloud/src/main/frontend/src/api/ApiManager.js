import ENDPOINTS from "./Endpoints"
import axios from 'axios';

const BASE_URL = "/api/v1"
const BASE_URL_DEBUG = "https://localhost:8443/api/v1"
let baseUrl = BASE_URL_DEBUG

const ApiManager = {
  getDashboard: async () => {
    const url = `${baseUrl}${ENDPOINTS.GET_DASHBOARD()}`
    const res = await axios.get(url)
    return res.data
  },
  /**
   * @name ApiManager.getCpuMemory
   * @description cpu, memory api 불러오는 값
   * 
   * @returns 
   */
  getCpuMemory: async () => {
    const url = `${baseUrl}${ENDPOINTS.GET_CPU_MEMERY()}`
    const res = await axios.get(url)
    return res.data
    // TODO: 예외처리 기능 추가
  },
  /**
   * @name ApiManager.getStorage
   * @description storage 불러오는 값
   * 
   * @returns 
   */
  getStorage: async () => {
    const url = `${baseUrl}${ENDPOINTS.GET_STORAGE()}`
    const res = await axios.get(url)
    return res.data
    // TODO: 예외처리 기능 추가
  },
  /**
   * @name ApiManager.getVmCpu
   * @description vmCpu 불러오는 값
   * 
   * @returns 
   */
  getVmCpu: async () => {
    const url = `${baseUrl}${ENDPOINTS.GET_VM_CPU()}`
    const res = await axios.get(url)
    return res.data
    // TODO: 예외처리 기능 추가
  },
  /**
   * @name ApiManager.getVmMemory
   * @description vmMemory 불러오는 값
   * 
   * @returns 
   */
  getVmMemory: async () => {
    const url = `${baseUrl}${ENDPOINTS.GET_VM_MEMORY()}`
    const res = await axios.get(url)
    return res.data
    // TODO: 예외처리 기능 추가
  },
  getStorageMemory: async () => {
    const url = `${baseUrl}${ENDPOINTS.GET_STORAGE_MEMROY()}`
    const res = await axios.get(url)
    return res.data
    // TODO: 예외처리 기능 추가
  }
}

export default ApiManager