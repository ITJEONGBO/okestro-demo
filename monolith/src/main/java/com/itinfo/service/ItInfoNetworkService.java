package com.itinfo.service;

import com.itinfo.model.ItInfoNetworkClusterVo;
import com.itinfo.model.ItInfoNetworkCreateVo;
import com.itinfo.model.ItInfoNetworkGroupVo;
import com.itinfo.model.ItInfoNetworkHostVo;
import com.itinfo.model.ItInfoNetworkVmVo;
import com.itinfo.model.ItInfoNetworkVo;
import java.util.List;
public interface ItInfoNetworkService {
	List<ItInfoNetworkVo> getNetworkList();

	List<ItInfoNetworkVo> getHostNetworkList(String id);

	ItInfoNetworkGroupVo getNetworkDetail(ItInfoNetworkVo itInfoNetworkVo);

	List<ItInfoNetworkClusterVo> getNetworkCluster(String clusterId, String networkId);

	ItInfoNetworkVo getNetwork(String networkId);

	List<ItInfoNetworkHostVo> getNetworkHost(String networkId);

	List<ItInfoNetworkVmVo> getNetworkVm(String networkId);

	void addLogicalNetwork(ItInfoNetworkVo itInfoNetworkVo);

	ItInfoNetworkCreateVo getNetworkCreateResource();

	void deleteNetworks(List<ItInfoNetworkVo> itInfoNetworkVos) throws Exception;

	void updateNetwork(ItInfoNetworkVo itInfoNetworkVo);
}
