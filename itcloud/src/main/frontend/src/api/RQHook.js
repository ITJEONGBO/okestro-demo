import { useQuery, useMutation } from "@tanstack/react-query";
import ApiManager from "./ApiManager";

//region: User
export const useAuthenticate = (username, password, _onSuccess, _onError) => useMutation({
  mutationFn: async () => {
    const res = await ApiManager.authenticate(username, password)
    return res
  },
  onSuccess: _onSuccess,
  onError: _onError,
})
//endregion: User

//region: Navigation
export const useAllTreeNavigations = (type = "none", mapPredicate = null) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['allTreeNavigations'],
  queryFn: async () => {
    const res = await ApiManager.findAllTreeNaviations(type)
    // setShouldRefresh(prevValue => false)
    if (mapPredicate)
      return res?.map((e) => mapPredicate(e)) ?? []
    else 
      return res ?? [] 
  }
});
//endregion

//region: DataCenter ----------------데이터센터----------------
/**
 * @name useAllDataCenters
 * @description 데이터센터 목록조회 useQuery훅
 *
 * @param {function} mapPredicate 객체 변형 처리
 */
export const useAllDataCenters = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['allDataCenters'],
  queryFn: async () => {
    const res = await ApiManager.findAllDataCenters()
    // setShouldRefresh(prevValue => false)
    return res?.map((e) => mapPredicate(e)) ?? []
  }
})


//endregion: DataCenter

//region: Cluster ----------------클러스터---------------------
/**
 * @name useAllClusters
 * @description 클러스터 목록조회 useQuery훅
 * 
 * @param {function} mapPredicate 객체 변형 처리
 */
export const useAllClusters = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['allClusters'],
  queryFn: async () => {
    const res = await ApiManager.findAllClusters()
    // setShouldRefresh(prevValue => false)
    return res?.map((e) => mapPredicate(e)) ?? []
  }
})

/**
 * @name useClusterById
 * @description 클러스터 상세조회 useQuery훅
 * 
 * @param {string} clusterId 클러스터ID
 * @returns useQuery훅
 */
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
/**
 * @name useLogicalFromCluster
 * @description 클러스터 내 논리네트워크 목록조회 useQuery훅
 * 
 * @param {string} clusterId 클러스터ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.useLogicalFromCluster
 */
export const useLogicalFromCluster = (clusterId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['logicalFromCluster', clusterId], 
  queryFn: async () => {
    console.log(`useLogicalFromCluster ... ${clusterId}`);
    const res = await ApiManager.findLogicalFromCluster(clusterId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
})
/**
 * @name useHostFromCluster
 * @description 클러스터 내 호스트 목록조회 useQuery훅
 * 
 * @param {string} clusterId 클러스터ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.useHostFromCluster
 */
export const useHostFromCluster = (clusterId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['hostsFromCluster', clusterId], 
  queryFn: async () => {
    console.log(`useHostFromCluster ... ${clusterId}`);
    const res = await ApiManager.findHostFromCluster(clusterId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
})
/**
 * @name useVMFromCluster
 * @description 클러스터 내 가상머신 목록조회 useQuery훅
 * 
 * @param {string} clusterId 클러스터ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.useVMFromCluster
 */
export const useVMFromCluster = (clusterId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['vmsFromCluster', clusterId], 
  queryFn: async () => {
    console.log(`useVMFromCluster ... ${clusterId}`);
    const res = await ApiManager.findVMFromCluster(clusterId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
})
/**
 * @name usePermissionFromCluster
 * @description 클러스터 내 권한 목록조회 useQuery훅
 * 
 * @param {string} clusterId 클러스터ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.usePermissionFromCluster
 */
export const usePermissionFromCluster = (clusterId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['permissionsFromCluster', clusterId], 
  queryFn: async () => {
    console.log(`usePermissionromCluster ... ${clusterId}`);
    const res = await ApiManager.findPermissionsFromCluster(clusterId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
})
/**
 * @name useEventFromCluster
 * @description 클러스터 내 이벤트 목록조회 useQuery훅
 * 
 * @param {string} clusterId 클러스터ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.useEventFromCluster
 */
export const useEventFromCluster = (clusterId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['eventsFromCluster', clusterId], 
  queryFn: async () => {
    console.log(`useEventFromCluster ... ${clusterId}`);
    const res = await ApiManager.findEventFromCluster(clusterId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
})
//endregion: Cluster

//region: Host ----------------호스트---------------------
/**
 * @name useAllHosts
 * @description 호스트 목록조회 useQuery훅
 * 
 * @param {function} mapPredicate 객체 변형 처리
 */
export const useAllHosts = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['allHosts'],
  queryFn: async () => {
    const res = await ApiManager.findAllHosts()
    // setShouldRefresh(prevValue => false)
    return res?.map((e) => mapPredicate(e)) ?? []
  }
})

//endregion: Host

//region: VM/TEMPLATE ----------------가상머신/템플릿---------------------
/**
 * @name useAllVMs
 * @description 가상머신 목록조회 useQuery훅
 * 
 * @param {function} mapPredicate 객체 변형 처리
 */
export const useAllVMs = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['allVMs'],
  queryFn: async () => {
    const res = await ApiManager.findAllVMs()
    // setShouldRefresh(prevValue => false)
    return res?.map((e) => mapPredicate(e)) ?? []
  }
})

/**
 * @name useAllTemplates
 * @description 템플릿 목록조회 useQuery훅
 * 
 * @param {function} mapPredicate 객체 변형 처리
 */
export const useAllTemplates = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['allTemplates'],
  queryFn: async () => {
    const res = await ApiManager.findAllTemplates()
    // setShouldRefresh(prevValue => false)
    return res?.map((e) => mapPredicate(e)) ?? []
  }
})
//endregion: VM/TEMPLATE


//region: Network -----------------네트워크---------------------
/**
 * @name useAllNetworks
 * @description 네트워크 목로조회 useQuery훅
 * 
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 */
export const useAllNetworks = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['allNetworks'],
  queryFn: async () => {
    const res = await ApiManager.findAllNetworks()
    // setShouldRefresh(prevValue => false)
    return res?.map((e) => mapPredicate(e)) ?? []
  }
})
/**
 * @name useNetworkById
 * @description 네트워크 상세조회 useQuery훅
 * 
 * @param {string} networkId 네트워크ID
 * @returns useQuery훅
 */
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
/**
 * @name useAllClustersFromNetwork
 * @description 네트워크 내 클러스터 목록조회 useQuery훅
 * 
 * @param {string} networkId 네트워크ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.findAllClustersFromNetwork
 */

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
/**
 * @name useAllTemplatesFromNetwork
 * @description 네트워크 내 템플릿 목록조회 useQuery훅
 * 
 * @param {string} networkId 네트워크ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.findAllTemplatesFromNetwork
 */
export const useAllTemplatesFromNetwork = (networkId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['templateFromNetwork', networkId], 
  queryFn: async () => {
    console.log(`useAllTemplatesFromNetwork ... ${networkId}`);
    const res = await ApiManager.findAllTemplatesFromNetwork(networkId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
})
/**
 * @name useAllPermissionsFromNetwork
 * @description 네트워크 내 권한 목록조회 useQuery훅
 * 
 * @param {string} networkId 네트워크ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.findAllPermissionsFromNetwork
 */
export const useAllPermissionsFromNetwork = (networkId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['permissionFromNetwork', networkId], 
  queryFn: async () => {
    console.log(`useAllPermissionFromNetwork ... ${networkId}`);
    const res = await ApiManager.findAllPermissionsFromNetwork(networkId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
})
/**
 * @name useAllVmsFromNetwork
 * @description 네트워크 내 가상머신 목록조회 useQuery훅
 * 
 * @param {string} networkId 네트워크ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.findAllVmsFromNetwork
 */
export const useAllVmsFromNetwork = (networkId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['vmFromNetwork', networkId], 
  queryFn: async () => {
    console.log(`useAllVmsFromNetwork ... ${networkId}`);
    const res = await ApiManager.findAllVmsFromNetwork(networkId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
})
/**
 * @name useAllVnicProfilesFromNetwork
 * @description 네트워크 내 VNIC 프로필 목록조회 useQuery훅
 * 
 * @param {string} networkId 네트워크ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.findAllVnicProfilesFromNetwork
 */
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

