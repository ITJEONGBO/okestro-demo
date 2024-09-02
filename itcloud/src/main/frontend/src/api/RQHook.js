import { useQuery } from "@tanstack/react-query";
import ApiManager from "./ApiManager";

export const useAllClusters = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['allClusters'],
  queryFn: async () => {
    const res = await ApiManager.findAllClusters()
    // setShouldRefresh(prevValue => false)
    return res?.map((e) => mapPredicate(e)) ?? []
  }
})

export const useAllNetworks = (mapPredicate) => useQuery({
  refetchOnWindowFocus: true,
  queryKey: ['allNetworks'],
  queryFn: async () => {
    const res = await ApiManager.findAllNetworks()
    // setShouldRefresh(prevValue => false)
    return res?.map((e) => mapPredicate(e)) ?? []
  }
})

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