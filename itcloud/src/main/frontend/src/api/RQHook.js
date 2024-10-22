import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
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


// Custom hook to fetch tree navigations
export const useAllTreeNavigations = (type = "none", mapPredicate = null) => {
  return useQuery({
    refetchOnWindowFocus: true,
    queryKey: ['allTreeNavigations', type],  // queryKey에 type을 포함시켜 type이 변경되면 데이터를 다시 가져옴
    queryFn: async () => {
      const res = await ApiManager.findAllTreeNaviations(type);  // type을 기반으로 API 호출
      if (mapPredicate)
        return res?.map((e) => mapPredicate(e)) ?? [];  // 데이터 가공 처리
      else 
        return res ?? [];  // 기본 데이터 반환
    }
  });
};
//endregion

//region: Dashboard
export const useDashboard = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['dashboard'],
  queryFn: async () => {
    const res = await ApiManager.getDashboard()
    // setShouldRefresh(prevValue => false)
    return res
    // return res?.map((e) => mapPredicate(e)) ?? []
  }
}); 

export const useDashboardCpuMemory = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['dashboardCpuMemory'],
  queryFn: async () => {
    const res = await ApiManager.getCpuMemory()
    // setShouldRefresh(prevValue => false)
    return res ?? []
    // return res?.map((e) => mapPredicate(e)) ?? []
  }
});
export const useDashboardStorage = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['dashboardStorage'],
  queryFn: async () => {
    const res = await ApiManager.getStorage()
    // setShouldRefresh(prevValue => false)
    return res ?? []
    // return res?.map((e) => mapPredicate(e)) ?? []
  }
});
export const useDashboardVmCpu = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['dashboardVmCpu'],
  queryFn: async () => {
    const res = await ApiManager.getVmCpu()
    // setShouldRefresh(prevValue => false)
    return res ?? []
    // return res?.map((e) => mapPredicate(e)) ?? []
  }
});
export const useDashboardVmMemory = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['dashboardVmMemory'],
  queryFn: async () => {
    const res = await ApiManager.getVmMemory()
    // setShouldRefresh(prevValue => false)
    return res ?? []
    // return res?.map((e) => mapPredicate(e)) ?? []
  }
});
export const useDashboardStorageMemory = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['dashboardStorageMemory'],
  queryFn: async () => {
    const res = await ApiManager.getStorageMemory()
    // setShouldRefresh(prevValue => false)
    return res ?? []
    // return res?.map((e) => mapPredicate(e)) ?? []
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
});
/**
 * @name useDataCenter
 * @description 데이터센터 정보 useQuery훅
 *
 * @param {function} mapPredicate 객체 변형 처리
 */
export const useDataCenter = (dataCenterId) => useQuery({
  refetchOnWindowFocus: true,  // 윈도우가 포커스될 때마다 데이터 리프레시
  queryKey: ['dataCenter', dataCenterId],  // queryKey에 dataCenterId를 포함시켜 dataCenterId가 변경되면 다시 요청
  queryFn: async () => {
    if (!dataCenterId) return {};  // dataCenterId가 없는 경우 빈 객체 반환
    const res = await ApiManager.findDataCenter(dataCenterId);  // dataCenterId에 따라 API 호출
    return res ?? {};  // 데이터를 반환, 없는 경우 빈 객체 반환
  },
  staleTime: 0, 
  cacheTime: 0, 
});

/**
 * @name useClustersFromDataCenter
 * @description 데이터센터 내 클러스터 목록조회 useQuery훅
 * 
 * @param {string} dataCenterId 데이터센터ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.findAllClustersFromDataCenter
 */
export const useClustersFromDataCenter = (dataCenterId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['clustersFromDataCenter', dataCenterId], 
  queryFn: async () => {
    console.log(`clustersFromDataCenter ... ${dataCenterId}`);
    const res = await ApiManager.findAllClustersFromDataCenter(dataCenterId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
});
/**
 * @name useHostsFromDataCenter
 * @description 데이터센터 내 호스트 목록조회 useQuery훅
 * 
 * @param {string} dataCenterId 데이터센터ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.findAllHostsFromDataCenter
 */
export const useHostsFromDataCenter = (dataCenterId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['hostsFromDataCenter', dataCenterId], 
  queryFn: async () => {
    console.log(`hostsFromDataCenter ... ${dataCenterId}`);
    const res = await ApiManager.findAllHostsFromDataCenter(dataCenterId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
});
/**
 * @name useVMsFromDataCenter
 * @description 데이터센터 내 가상머신 목록조회 useQuery훅
 * 
 * @param {string} dataCenterId 데이터센터ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.findAllVmsFromDataCenter
 */
export const useVMsFromDataCenter = (dataCenterId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['vmsFromDataCenter', dataCenterId], 
  queryFn: async () => {
    console.log(`vmsFromDataCenter ... ${dataCenterId}`);
    const res = await ApiManager.findAllVmsFromDataCenter(dataCenterId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
});
/**
 * @name useDomainsFromDataCenter
 * @description 데이터센터 내 스토리지 도메인 목록조회 useQuery훅
 * 
 * @param {string} dataCenterId 데이터센터ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.findAllDomainsFromDataCenter
 */
export const useDomainsFromDataCenter = (dataCenterId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['domainsFromDataCenter', dataCenterId], 
  queryFn: async () => {
    console.log(`domainsFromDataCenter ... ${dataCenterId}`);
    const res = await ApiManager.findAllDomainsFromDataCenter(dataCenterId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
});
/**
 * @name useNetworksFromDataCenter
 * @description 데이터센터 내 네트워크 목록조회 useQuery훅
 * 
 * @param {string} dataCenterId 데이터센터ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.findAllNetworksFromDataCenter
 */
export const useNetworksFromDataCenter = (dataCenterId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['networksFromDataCenter', dataCenterId], 
  queryFn: async () => {
    console.log(`networksFromDataCenter ... ${dataCenterId}`);
    const res = await ApiManager.findAllNetworksFromDataCenter(dataCenterId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
});
/**
 * @name useEventsFromDataCenter
 * @description 데이터센터 내 이벤트 목록조회 useQuery훅
 * 
 * @param {string} dataCenterId 데이터센터ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.findAllEventsFromDataCenter
 */
export const useEventsFromDataCenter = (dataCenterId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['eventsFromDataCenter', dataCenterId], 
  queryFn: async () => {
    console.log(`eventsFromDataCenter ... ${dataCenterId}`);
    const res = await ApiManager.findAllEventsFromDataCenter(dataCenterId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
});

/**
 * @name useAddDataCenter
 * @description 데이터센터 생성 useMutation 훅
 * 
 * @returns useMutation 훅
 */
export const useAddDataCenter = () => {
  const queryClient = useQueryClient();  // 캐싱된 데이터를 리패칭할 때 사용
  return useMutation({
    mutationFn: async (dataCenterData) => await ApiManager.addDataCenter(dataCenterData),
    onSuccess: () => {
      queryClient.invalidateQueries('allDataCenters'); // 데이터센터 추가 성공 시 'allDataCenters' 쿼리를 리패칭하여 목록을 최신화
    },
    onError: (error) => {
      console.error('Error adding data center:', error);
    },  
  });
};
/**
 * @name useEditDataCenter
 * @description 데이터센터 수정 useMutation 훅
 * 
 * @returns useMutation 훅
 */
export const useEditDataCenter = () => {
  const queryClient = useQueryClient();  
  return useMutation({
    mutationFn: async ({ dataCenterId, dataCenterData }) => await ApiManager.editDataCenter(dataCenterId, dataCenterData),
    onSuccess: () => {
      queryClient.invalidateQueries('allDataCenters');
    },
    onError: (error) => {
      console.error('Error editing data center:', error);
    },
  });
};
/**
 * @name useDeleteDataCenter
 * @description 데이터센터 삭제 useMutation 훅
 * 
 * @returns useMutation 훅
 */
export const useDeleteDataCenter = () => {
  const queryClient = useQueryClient();  // 캐싱된 데이터를 리패칭할 때 사용
  return useMutation({ 
    mutationFn: async (dataCenterId) => await ApiManager.deleteDataCenter(dataCenterId),
    onSuccess: () => {
      queryClient.invalidateQueries('allDataCenters');
    },
    onError: (error) => {
      console.error('Error deleting data center:', error);
    },
  });
};

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
    // const res = await ApiManager.findAllClustersFromDataCenter()
    // setShouldRefresh(prevValue => false)
    return res?.map((e) => mapPredicate(e)) ?? []
  }
})
/**
 * @name useCluster
 * @description 클러스터 상세조회 useQuery 훅
 * 
 * @param {string} clusterId 클러스터 ID
 * @returns useQuery 훅
 */
export const useCluster = (clusterId) => useQuery({
  refetchOnWindowFocus: true,  // 윈도우 포커스 시 데이터 리프레시
  queryKey: ['cluster', clusterId],  // queryKey에 clusterId를 포함시켜 clusterId가 변경되면 다시 요청
  queryFn: async () => {
    if (!clusterId) return {};  // clusterId가 없을 때 빈 객체 반환
    console.log(`useCluster ... ${clusterId}`);
    const res = await ApiManager.findCluster(clusterId);  // clusterId에 따라 API 호출
    return res ?? {};  // 반환값이 없으면 빈 객체 반환
  },
  staleTime: 0,  // 항상 최신 데이터를 유지
  cacheTime: 0,  // 캐시를 유지하지 않고 매번 새로운 데이터를 요청
});
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
/**
 * @name useHostById
 * @description 호스트 상세조회 useQuery훅
 * 
 * @param {string} hostId 호스트ID
 * @returns useQuery훅
 */
export const useHostById = (hostId) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['HostById',hostId],
  queryFn: async () => {
    if (!hostId) return {};
    console.log(`useHostById ... ${hostId}`)
    const res = await ApiManager.findAllHostById(hostId)
    return res ?? {}
  },
  staleTime: 0, 
  cacheTime: 0,
})


/**
 * @name useHostFromCluster
 * @description 호스트 내 가상머신 목록조회 useQuery훅
 * 
 * @param {string} clusterId 클러스터ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.useHostFromCluster
 */
export const useVmFromHost = (hostId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['VmFromHost', hostId], 
  queryFn: async () => {
    console.log(`useVmFromHost ... ${hostId}`);
    const res = await ApiManager.findVmFromHost(hostId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
})
/**
 * @name useHostFromCluster
 * @description 호스트 내 호스트장치 목록조회 useQuery훅
 * 
 * @param {string} clusterId 클러스터ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.useHostFromCluster
 */
export const useHostdeviceFromHost = (hostId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['HostdeviceFromHost', hostId], 
  queryFn: async () => {
    console.log(`useHostdeviceFromHost ... ${hostId}`);
    const res = await ApiManager.findHostdeviceFromHost(hostId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
})
/**
 * @name useHostFromCluster
 * @description 호스트 내 이벤트 목록조회 useQuery훅
 * 
 * @param {string} clusterId 클러스터ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.useHostFromCluster
 */
export const useEventFromHost = (hostId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['EventFromHost', hostId], 
  queryFn: async () => {
    console.log(`EventFromHost ... ${hostId}`);
    const res = await ApiManager.findEventsFromHost(hostId); 
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
export const usePermissionFromHost = (hostId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['PermissionFromHost', hostId], 
  queryFn: async () => {
    console.log(`usePermissionFromHost ... ${hostId}`);
    const res = await ApiManager.findPermissionsFromHost(hostId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
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
});
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
});

/**
 * @name useNetworkById
 * @description 네트워크 상세조회 useQuery 훅
 * 
 * @param {string} networkId 네트워크 ID
 * @returns useQuery 훅
 */
export const useNetworkById = (networkId) => useQuery({
  queryKey: ['networkById', networkId],
  queryFn: async () => {
    if (!networkId) return {};  // networkId가 없는 경우 빈 객체 반환
    console.log(`Fetching network with ID: ${networkId}`);
    const res = await ApiManager.findNetwork(networkId);
    return res ?? {};
  },
  staleTime: 0,
  cacheTime: 0,
});


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
    if (!networkId) {
      throw new Error('Network ID is missing'); 
    }
    console.log(`Fetching templates for Network ID: ${networkId}`);
    const res = await ApiManager.findAllTemplatesFromNetwork(networkId);
    console.log('API Response:', res); // API 응답 확인
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  },
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


/**
 * @name useAllVnicProfiles
 * @description VNIC 프로필 목록조회 useQuery훅
 * 
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.findAllVnicProfiles
 */
export const useAllVnicProfiles = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['allVnicProfiles'],
  queryFn: async () => {
    const res = await ApiManager.findAllVnicProfiles()
    return res?.map((e) => mapPredicate(e)) ?? []
  }
})

//region: storage -----------------스토리지---------------------

/**
 * @name useAllStorageDomains
 * @description 모든 스토리지 도메인 목록조회 useQuery훅
 * 
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 */
export const useAllStorageDomains = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['allStorageDomains'],
  queryFn: async () => {
    const res = await ApiManager.findAllStorageDomains()
    return res?.map((e) => mapPredicate(e)) ?? []
  }
})
/**
 * @name useDomainById
 * @description 도메인 상세조회 useQuery 훅
 * 
 * @param {string} domainId 도메인 ID
 * @returns useQuery 훅
 */
export const useDomainById = (storageDomainId) => useQuery({
  queryKey: ['DomainById', storageDomainId],
  queryFn: async () => {
    if (!storageDomainId) return {};  
    console.log(`Fetching network with ID: ${storageDomainId}`);
    const res = await ApiManager.findDomain(storageDomainId);
    return res ?? {};
  },
  staleTime: 0,
  cacheTime: 0,
});
/**
 * @name useAllDataCenterFromDomain
 * @description 도메인 내 데이터센터 목록조회 useQuery훅
 * 
 * @param {string} storageDomainId 도메인ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.findAllDataCenterFromDomain
 */
export const useAllDataCenterFromDomain = (storageDomainId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['AllDataCenterFromDomain', storageDomainId], 
  queryFn: async () => {
    console.log(`useAllDataCenterFromDomain ... ${storageDomainId}`);
    const res = await ApiManager.findAllDataCentersFromDomain(storageDomainId); 
    return res?.map((e) => mapPredicate(e)) ?? []; // 데이터 가공
  }
})

/**
 * @name useAllDiskFromDomain
 * @description 도메인 내 디스크 목록조회 useQuery훅
 * 
 * @param {string} storageDomainId 도메인ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.findAllDataCenterFromDomain
 */
export const useAllDiskFromDomain = (storageDomainId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['AllDiskFromDomain', storageDomainId], 
  queryFn: async () => {
    console.log(`useAllDiskFromDomain ... ${storageDomainId}`);
    const res = await ApiManager.findAllDisksFromDomain(storageDomainId); 
    return res?.map((e) => mapPredicate(e)) ?? []; 
  }
})
/**
 * @name useAllDiskSnapshotFromDomain
 * @description 도메인 내 디스크스냅샷 목록조회 useQuery훅
 * 
 * @param {string} storageDomainId 도메인ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.findAllDataCenterFromDomain
 */
export const useAllDiskSnapshotFromDomain = (storageDomainId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['AllDiskSnapshotFromDomain', storageDomainId], 
  queryFn: async () => {
    console.log(`useAllDiskSnapshotFromDomain ... ${storageDomainId}`);
    const res = await ApiManager.findAllDiskSnapshotsFromDomain(storageDomainId); 
    return res?.map((e) => mapPredicate(e)) ?? []; 
  }
})
/**
 * @name useAllTemplateFromDomain
 * @description 도메인 내 템플릿 목록조회 useQuery훅
 * 
 * @param {string} storageDomainId 도메인ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.findAllDataCenterFromDomain
 */
export const useAllTemplateFromDomain = (storageDomainId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['AllTemplateFromDomain', storageDomainId], 
  queryFn: async () => {
    console.log(`useAllTemplateFromDomain ... ${storageDomainId}`);
    const res = await ApiManager.findAllTemplatesFromDomain(storageDomainId); 
    return res?.map((e) => mapPredicate(e)) ?? []; 
  }
})
/**
 * @name useAllEventFromDomain
 * @description 도메인 내 이벤트 목록조회 useQuery훅
 * 
 * @param {string} storageDomainId 도메인ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.findAllDataCenterFromDomain
 */
export const useAllEventFromDomain = (storageDomainId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['AllEventFromDomain', storageDomainId], 
  queryFn: async () => {
    console.log(`useAllEventFromDomain ... ${storageDomainId}`);
    const res = await ApiManager.findAllEventsFromDomain(storageDomainId); 
    return res?.map((e) => mapPredicate(e)) ?? []; 
  }
})
//region: storage -----------------디스크---------------------
/**
 * @name useAllDisks
 * @description 모든 디스크목록조회 useQuery훅
 * 
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 */
export const useAllDisks = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['allDisks'],
  queryFn: async () => {
    const res = await ApiManager.findAllDisks()
    return res?.map((e) => mapPredicate(e)) ?? [];
  }
})
/**
 * @name useDiskById
 * @description 디스크 상세조회 useQuery 훅
 * 
 * @param {string} diskId 디스크ID
 * @returns useQuery 훅
 */
export const useDiskById = (diskId) => useQuery({
  queryKey: ['DiskById', diskId],
  queryFn: async () => {
    if (!diskId) return {};  
    console.log(`useDiskById: ${diskId}`);
    const res = await ApiManager.findDisk(diskId);
    return res ?? {};
  },
  staleTime: 0,
  cacheTime: 0,
});
/**
 * @name useAllVmsFromDisk
 * @description 디스크 내 가상머신 목록조회 useQuery훅
 * 
 * @param {string} diskId 디스크ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.findAllVmsFromDisk
 */
export const useAllVmsFromDisk = (diskId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['AllVmsFromDisk', diskId], 
  queryFn: async () => {
    console.log(`useAllVmsFromDisk ... ${diskId}`);
    const res = await ApiManager.findAllVmsFromDisk(diskId); 
    return res?.map((e) => mapPredicate(e)) ?? []; 
  }
})

/**
 * @name useAllStorageDomainFromDisk
 * @description 디스크 내 스토리지 목록조회 useQuery훅
 * 
 * @param {string} diskId 디스크ID
 * @param {function} mapPredicate 목록객체 변형 처리
 * @returns useQuery훅
 * 
 * @see ApiManager.findAllStorageDomainsFromDisk
 */
export const useAllStorageDomainFromDisk = (diskId, mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['AllStorageDomainFromDisk', diskId], 
  queryFn: async () => {
    console.log(`useAllStorageDomainFromDisk ... ${diskId}`);
    const res = await ApiManager.findAllStorageDomainsFromDisk(diskId); 
    return res?.map((e) => mapPredicate(e)) ?? []; 
  }
})
//region: event -----------------이벤트---------------------
/**
 * @name useAllEvents
 * @description 모든 이벤트 목록조회 useQuery훅
 * 
 * @param {function} mapPredicate 
 * @returns useQuery훅
 */
export const useAllEvents = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['allEvents'],
  queryFn: async () => {
    const res = await ApiManager.findAllEvent()
    return res?.map((e) => mapPredicate(e)) ?? []
  }
})
