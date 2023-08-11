package com.itinfo.service.impl

import com.itinfo.common.LoggerDelegate
import com.itinfo.model.*
import com.itinfo.service.ProvidersService
import com.itinfo.service.engine.ConnectionService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProvidersServiceImpl : BaseService(), ProvidersService {

	@Autowired private lateinit var connectionService: ConnectionService

	override fun retrieveProviders(): List<ProviderVo> {
		log.info("... retrieveProviders")
		val connection = connectionService.connection

		val externalHostProviders = sysSrvHelper.findAllExternalHostProviders(connection)
		val openStackImageProviders = sysSrvHelper.findAllOpenStackImageProviders(connection)
		val openStackNetworkProviders = sysSrvHelper.findAllOpenStackNetworkProviders(connection)
		val openStackVolumeProviders = sysSrvHelper.findAllOpenStackVolumeProviders(connection)

		val targets: MutableList<ProviderVo> = arrayListOf()
		targets.addAll(externalHostProviders.toProviderVosWithExternalHost())
		targets.addAll(openStackImageProviders.toProviderVosWithOpenStackImage())
		targets.addAll(openStackNetworkProviders.toProviderVosWithOpenStackNetwork())
		targets.addAll(openStackVolumeProviders.toProviderVosWithOpenStackVolume())
		return targets
	}

	companion object {
		private val log by LoggerDelegate()
	}
}