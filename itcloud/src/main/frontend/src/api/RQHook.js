import { useQuery } from "@tanstack/react-query";
import ApiManager from "./ApiManager";

//클러스터 목록
export const useAllClusters = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['allClusters'],
  queryFn: async () => {
    const res = await ApiManager.findAllClusters()
    // setShouldRefresh(prevValue => false)
    return res?.map((e) => mapPredicate(e)) ?? []
  }
})
// 클러스터 세부정보
export const useClusterById = (clusterId) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['clusterById'],
  queryFn: async () => {
    console.log(`useClusterById ... ${clusterId}`)
    const res = await ApiManager.findAllClusterById(clusterId)
    // setShouldRefresh(prevValue => false)
    return res ?? {}
  }
})

//네트워크 목록
export const useAllNetworks = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['allNetworks'],
  queryFn: async () => {
    const res = await ApiManager.findAllNetworks()
    // setShouldRefresh(prevValue => false)
    return res?.map((e) => mapPredicate(e)) ?? []
  }
})

// 네트워크 세부정보
export const useNetworkById = (networkId) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['networkById'],
  queryFn: async () => {
    console.log(`useNetworkById ... ${networkId}`)
    const res = await ApiManager.findNetworkById(networkId)
    // setShouldRefresh(prevValue => false)
    return res ?? {}
  }
})

 

export const useAllVnicProfilesFromNetwork = (networkId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['vnicProfilesFromNetwork'],
  queryFn: async () => {
    console.log(`useAllVnicProfilesFromNetwork ... ${networkId}`)
    const res = await ApiManager.findAllVnicProfilesFromNetwork(networkId)
    // setShouldRefresh(prevValue => false)
    return res?.map((e) => mapPredicate(e)) ?? []
  }
})


