import { useQuery } from "@tanstack/react-query";
import ApiManager from "./ApiManager";
/* ----------------컴퓨팅--------------------- */


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

/* -----------------네트워크--------------------- */
//네트워크 목록 불러오기
export const useAllNetworks = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['allNetworks'],
  queryFn: async () => {
    const res = await ApiManager.findAllNetworks()
    // setShouldRefresh(prevValue => false)
    return res?.map((e) => mapPredicate(e)) ?? []
  }
})

// 네트워크 세부정보(일반)
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

// vnic
export const useAllVnicProfilesFromNetwork = (networkId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['vnicProfilesFromNetwork', networkId],
  queryFn: async () => {
    console.log(`useAllVnicProfilesFromNetwork ... ${networkId}`)
    const res = await ApiManager.findAllVnicProfilesFromNetwork(networkId)
    // setShouldRefresh(prevValue => false)
    return res?.map((e) => mapPredicate(e)) ?? []
  }
})

// 클러스터목록
export const useAllClustersFromNetwork = (networkId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['clustersFromNetwork', networkId],
  queryFn: async () => {
    console.log(`useAllClustersFromNetwork ... ${networkId}`);
    const res = await ApiManager.findAllClustersFromNetwork(networkId);  // 클러스터 목록을 가져오는 API 호출
    return res?.map((e) => mapPredicate(e)) ?? [];
  }
})
// 호스트 목록
export const useAllHostsFromNetwork = (networkId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['hostsFromNetwork', networkId], 
  queryFn: async () => {
    console.log(`useAllHostsFromNetwork ... ${networkId}`);
    const res = await ApiManager.findAllHostsFromNetwork(networkId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
})
// 가상머신 목록
export const useAllVmsFromNetwork = (networkId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['vmFromNetwork', networkId], 
  queryFn: async () => {
    console.log(`useAllVmsFromNetwork ... ${networkId}`);
    const res = await ApiManager.findAllVMFromNetwork(networkId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
})
// 템플릿 목록
export const useAllTemplateFromNetwork = (networkId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['templateFromNetwork', networkId], 
  queryFn: async () => {
    console.log(`useAllTemplateFromNetwork ... ${networkId}`);
    const res = await ApiManager.findAllTemplateFromNetwork(networkId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
})
// 권한
export const useAllPermissionFromNetwork = (networkId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['permissionFromNetwork', networkId], 
  queryFn: async () => {
    console.log(`useAllPermissionFromNetwork ... ${networkId}`);
    const res = await ApiManager.findAllPermissionFromNetwork(networkId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
})

