//package com.itinfo.service.impl;
//
//import com.itinfo.model.ModelsKt;
//import com.itinfo.service.ProvidersService;
//import com.itinfo.service.engine.ConnectionService;
//import com.itinfo.model.ProviderVo;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.ovirt.engine.sdk4.Connection;
//import org.ovirt.engine.sdk4.types.ExternalHostProvider;
//import org.ovirt.engine.sdk4.types.OpenStackImageProvider;
//import org.ovirt.engine.sdk4.types.OpenStackNetworkProvider;
//import org.ovirt.engine.sdk4.types.OpenStackVolumeProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class ProvidersServiceImpl extends BaseService implements ProvidersService {
//	@Autowired private ConnectionService connectionService;
//
//	@Override
//	public List<ProviderVo> retrieveProviders() {
//		log.info("... retrieveProviders");
//		Connection connection = connectionService.getConnection();
//
//		List<ExternalHostProvider> externalHostProviders
//				= getSysSrvHelper().findAllExternalHostProviders(connection);
//		List<OpenStackImageProvider> openStackImageProviders
//				= getSysSrvHelper().findAllOpenStackImageProviders(connection);
//		List<OpenStackNetworkProvider> openStackNetworkProviders
//				= getSysSrvHelper().findAllOpenStackNetworkProviders(connection);
//		List<OpenStackVolumeProvider> openStackVolumeProviders
//				= getSysSrvHelper().findAllOpenStackVolumeProviders(connection);
//
//		List<ProviderVo> targets = new ArrayList<>();
//		targets.addAll(ModelsKt.toProviderVosWithExternalHost(externalHostProviders));
//		targets.addAll(ModelsKt.toProviderVosWithOpenStackImage(openStackImageProviders));
//		targets.addAll(ModelsKt.toProviderVosWithOpenStackNetwork(openStackNetworkProviders));
//		targets.addAll(ModelsKt.toProviderVosWithOpenStackVolume(openStackVolumeProviders));
//		return targets;
//	}
//}
