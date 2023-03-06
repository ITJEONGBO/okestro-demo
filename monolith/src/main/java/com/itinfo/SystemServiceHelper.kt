package com.itinfo

import org.ovirt.engine.sdk4.Connection
import org.ovirt.engine.sdk4.services.*
import org.ovirt.engine.sdk4.types.*

class SystemServiceHelper {
	companion object {
		@Volatile private var INSTANCE: SystemServiceHelper? = null
		@JvmStatic fun getInstance(): SystemServiceHelper = INSTANCE ?: synchronized(this) {
			INSTANCE
				?: build().also { INSTANCE = it }
		}
		private fun build(): SystemServiceHelper = SystemServiceHelper()
	}

	private fun getSystemService(c: Connection): SystemService = c.systemService()

	//region: Cluster
	private fun srvClusters(c: Connection): ClustersService
		= getSystemService(c).clustersService()
	private fun srvCluster(c: Connection, clusterId: String): ClusterService
		= srvClusters(c).clusterService(clusterId)
	fun findAllClusters(c: Connection): List<Cluster>
		= srvClusters(c).list().send().clusters() ?: listOf()
	fun findCluster(c: Connection, clusterId: String): Cluster
		= srvCluster(c, clusterId).get().send().cluster()
	fun addCluster(c: Connection, cluster: Cluster): Cluster? = try {
		srvClusters(c).add().cluster(cluster).send().cluster()
	} catch (e: Exception) {
		e.printStackTrace()
		null
	}

	fun updateCluster(c: Connection, clusterId: String, cluster: Cluster): Cluster? = try {
		srvCluster(c, clusterId).update().cluster(cluster).send().cluster()
	} catch (e: Exception) {
		e.printStackTrace()
		null
	}
	fun removeCluster(c: Connection, clusterId: String): Boolean = try {
		srvCluster(c, clusterId).remove().send()
		true
	} catch (e: Exception) {
		e.printStackTrace()
		false
	}

	private fun srvClusterNetworks(c: Connection, clusterId: String): ClusterNetworksService
		= srvCluster(c, clusterId).networksService()

	fun findAllNetworksFromCluster(c: Connection, clusterId: String): List<Network>
		= srvClusterNetworks(c, clusterId).list().send().networks()
	fun addNetworkFromCluster(c: Connection, clusterId: String, network: Network): Network? = try {
		srvCluster(c, clusterId).networksService().add().network(network).send().network()
	} catch (e: Exception) {
		e.printStackTrace()
		null
	}
	private fun srvClusterNetwork(c: Connection, clusterId: String, networkId: String): ClusterNetworkService
		= srvClusterNetworks(c, clusterId).networkService(networkId)
	fun updateNetworkFromCluster(c: Connection, clusterId: String, network: Network): Network? = try {
		srvClusterNetwork(c, clusterId, network.id()).update().network(network).send().network()
	} catch (e: Exception) {
		e.printStackTrace()
		null
	}
	private fun srvExternalNetworkProviders(c: Connection, clusterId: String): ClusterExternalProvidersService
		= srvCluster(c, clusterId).externalNetworkProvidersService()

	fun findExternalNetworkProviders(c: Connection, clusterId: String): List<ExternalProvider>
		= srvExternalNetworkProviders(c, clusterId).list().send().providers()
	//endregion

	//region: Host
	private fun srvHosts(c: Connection): HostsService
		= getSystemService(c).hostsService()
	fun findAllHosts(c: Connection, searchQuery: String = ""): List<Host>
		= if (searchQuery == "")	srvHosts(c).list().send().hosts()
		else 						srvHosts(c).list().search(searchQuery).caseSensitive(false).send().hosts()
	private fun srvHost(c: Connection, hostId: String): HostService
		= srvHosts(c).hostService(hostId)
	fun findHost(c: Connection, hostId: String): Host
		= srvHost(c, hostId).get().send().host()
	private fun srvNicsFromHost(c: Connection, hostId: String): HostNicsService
		= srvHost(c, hostId).nicsService()
	fun findNicsFromHost(c: Connection, hostId: String): List<HostNic>
		= srvNicsFromHost(c, hostId).list().send().nics()
	private fun srvStatisticsFromHost(c: Connection, hostId: String): StatisticsService
		= srvHost(c, hostId).statisticsService()
	fun findAllStatisticsFromHost(c: Connection, hostId: String): List<Statistic>
		= srvStatisticsFromHost(c, hostId).list().send().statistics()
	private fun srvStoragesFromHost(c: Connection, hostId: String): HostStorageService
		= srvHost(c, hostId).storageService()
	fun findAllStoragesFromHost(c: Connection, hostId: String): List<HostStorage>
		= srvStoragesFromHost(c, hostId).list().send().storages()
	private fun srvNetworkAttachmentsFromHost(c: Connection, hostId: String): NetworkAttachmentsService
		= srvHost(c, hostId).networkAttachmentsService()
	fun findAllNetworkAttachmentsFromHost(c: Connection, hostId: String): List<NetworkAttachment>
		= srvNetworkAttachmentsFromHost(c, hostId).list().send().attachments()
	//endregion


	//region: Vm
	private fun getVmsService(c: Connection): VmsService
		= getSystemService(c).vmsService()
	fun findAllVms(c: Connection, searchQuery: String = ""): List<Vm> {
		return if (searchQuery == "")	getVmsService(c).list().send().vms()
		else 							getVmsService(c).list().search(searchQuery).caseSensitive(false).send().vms()
	}
	fun srvVm(c: Connection, vmId: String): VmService
		= getVmsService(c).vmService(vmId)
	fun findVm(c: Connection, vmId: String): Vm
		= srvVm(c, vmId).get().send().vm()
	fun startVm(c: Connection, vmId: String): Boolean = try { srvVm(c, vmId).start().send();true } catch (e: Exception) { e.printStackTrace();false }
	fun stopVm(c: Connection, vmId: String): Boolean = try { srvVm(c, vmId).stop().send();true } catch (e: Exception) { e.printStackTrace();false }
	fun rebootVm(c: Connection, vmId: String): Boolean = try { srvVm(c, vmId).reboot().send();true; } catch (e: Exception) { e.printStackTrace();false }

	private fun srvVmCdromsFromVm(c: Connection, vmId: String): VmCdromsService
		= srvVm(c, vmId).cdromsService()
	fun findAllVmCdromsFromVm(c: Connection, vmId: String): List<Cdrom>
		= srvVmCdromsFromVm(c, vmId).list().send().cdroms()
	private fun srvVmCdromFromVm(c: Connection, vmId: String, cdromId: String): VmCdromService
		= srvVmCdromsFromVm(c, vmId).cdromService(cdromId)
	fun findVmCdromFromVm(c: Connection, vmId: String, cdromId: String): Cdrom
		= srvVmCdromFromVm(c, vmId, cdromId).get().send().cdrom()
	fun updateVmCdromFromVm(c: Connection, vmId: String, cdromId: String, cdrom: Cdrom): Boolean = try {
		srvVmCdromFromVm(c, vmId, cdromId).update().cdrom(cdrom).current(true).send()
		true
	} catch (e: Exception) {
		false
	}
	private fun srvNicsFromVm(c: Connection, vmId: String): VmNicsService
		= srvVm(c, vmId).nicsService()
	fun findNicsFromVm(c: Connection, vmId: String): List<Nic>
		= srvNicsFromVm(c, vmId).list().send().nics()
	private fun srvDiskAttachmentsFromVm(c: Connection, vmId: String): DiskAttachmentsService
		= srvVm(c, vmId).diskAttachmentsService()
	fun findDiskAttachmentsFromVm(c: Connection, vmId: String): List<DiskAttachment>
		= srvDiskAttachmentsFromVm(c, vmId).list().send().attachments()

	private fun srvSnapshotsFromVm(c: Connection, vmId: String): SnapshotsService
		= srvVm(c, vmId).snapshotsService()
	fun findAllSnapshotsFromVm(c: Connection, vmId: String): List<Snapshot>
		= srvSnapshotsFromVm(c, vmId).list().send().snapshots()
	private fun srvSnapshotFromVm(c: Connection, vmId: String, snapshotId: String): SnapshotService
		= srvSnapshotsFromVm(c, vmId).snapshotService(snapshotId)
	fun findSnapshotFromVm(c: Connection, vmId: String, snapshotId: String): Snapshot
		= srvSnapshotFromVm(c, vmId, snapshotId).get().send().snapshot()
	fun addSnapshotFromVm(c: Connection, vmId: String, snapshot: Snapshot): Boolean = try {
		srvSnapshotsFromVm(c, vmId).add().snapshot(snapshot).send().snapshot()
		true
	} catch (e: Exception) {
		e.printStackTrace()
		false
	}

	fun removeSnapshotFromVm(c: Connection, vmId: String, snapshotId: String): Boolean = try {
		srvSnapshotFromVm(c, vmId, snapshotId).remove().send()
		true
	} catch (e: Exception) {
		e.printStackTrace()
		false
	}

	fun undoSnapshotFromVm(c: Connection, vmId: String): Boolean = try {
		srvVm(c, vmId).undoSnapshot().send()
		true
	} catch (e: Exception) {
		e.printStackTrace()
		false
	}

	fun commitSnapshotFromVm(c: Connection, vmId: String): Boolean = try {
		srvVm(c, vmId).commitSnapshot().send()
		true	} catch (e: Exception) {
		e.printStackTrace()
		false
	}

	fun previewSnapshotFromVm(c: Connection, vmId: String, snapshot: Snapshot, restoreMemory: Boolean): Boolean = try {
		srvVm(c, vmId).previewSnapshot().restoreMemory(restoreMemory).snapshot(snapshot).send()
		true
	} catch (e: Exception) {
		e.printStackTrace()
		false
	}

	private fun srvSnapshotDisksFromVm(c: Connection, vmId: String, snapshotId: String): SnapshotDisksService
		= srvSnapshotFromVm(c, vmId, snapshotId).disksService()
	fun findAllSnapshotDisksFromVm(c: Connection, vmId: String, snapshotId: String): List<Disk>
		= srvSnapshotDisksFromVm(c, vmId, snapshotId).list().send().disks()

	private fun srvSnapshotNicsFromVm(c: Connection, vmId: String, snapshotId: String): SnapshotNicsService
		= srvSnapshotFromVm(c, vmId, snapshotId).nicsService()
	fun findAllSnapshotNicsFromVm(c: Connection, vmId: String, snapshotId: String): List<Nic>
		= srvSnapshotNicsFromVm(c, vmId, snapshotId).list().send().nics()
	private fun srvVmGraphicsConsolesFromVm(c: Connection, vmId: String): VmGraphicsConsolesService
		= srvVm(c, vmId).graphicsConsolesService()
	fun findAllVmGraphicsConsolesFromVm(c: Connection, vmId: String): List<GraphicsConsole>
		= srvVmGraphicsConsolesFromVm(c, vmId).list().current(true).send().consoles()
	private fun srvVmGraphicsConsoleFromVm(c: Connection, vmId: String, graphicsConsoleId: String): VmGraphicsConsoleService
		= srvVmGraphicsConsolesFromVm(c, vmId).consoleService(graphicsConsoleId)
	fun findTicketFromVm(c: Connection, vmId: String, graphicsConsoleId: String): Ticket
		= srvVmGraphicsConsoleFromVm(c, vmId, graphicsConsoleId).ticket().send().ticket()
	private fun srvStatisticsFromVm(c: Connection, vmId: String): StatisticsService
		= srvVm(c, vmId).statisticsService()
	fun findAllStatisticsFromVm(c: Connection, vmId: String): List<Statistic>
		= srvStatisticsFromVm(c, vmId).list().send().statistics()
	//endregion

	//region: VnicProfile
	private fun srvVnicProfiles(c: Connection): VnicProfilesService
		= getSystemService(c).vnicProfilesService()
	fun findAllVnicProfiles(c: Connection): List<VnicProfile>
		= srvVnicProfiles(c).list().send().profiles()
	private fun srvVnicProfile(c: Connection, vnicProfileId: String): VnicProfileService
		= srvVnicProfiles(c).profileService(vnicProfileId)
	fun findVnicProfile(c: Connection, vnicProfileId: String): VnicProfile
		= srvVnicProfile(c, vnicProfileId).get().send().profile()
	//endregion

	//region: Network
	private fun srvNetworks(c: Connection): NetworksService
		= getSystemService(c).networksService()
	fun findAllNetworks(c: Connection): List<Network>
		= srvNetworks(c).list().send().networks()
	private fun srvNetwork(c: Connection, networkId: String): NetworkService
		= srvNetworks(c).networkService(networkId)
	fun findNetwork(c: Connection, networkId: String): Network
		= srvNetwork(c, networkId).get().send().network()

	fun addNetwork(c: Connection, network: Network): Network? = try {
		srvNetworks(c).add().network(network).send().network()
	} catch (e: Exception) {
		e.printStackTrace()
		null
	}

	fun updateNetwork(c: Connection, networkId: String, network: Network): Network? = try {
		srvNetwork(c, networkId).update().network(network).send().network()
	} catch (e: Exception) {
		e.printStackTrace()
		null
	}
	private fun srvNetworkLabelsFromNetwork(c: Connection, networkId: String): NetworkLabelsService
		= srvNetwork(c, networkId).networkLabelsService()
	fun findAllNetworkLabelsFromNetwork(c: Connection, networkId: String): List<NetworkLabel>
		= srvNetworkLabelsFromNetwork(c, networkId).list().send().labels()
	fun addNetworkLabelFromNetwork(c: Connection, networkId: String, networkLabel: NetworkLabel): NetworkLabel? = try {
		srvNetworkLabelsFromNetwork(c, networkId).add().label(networkLabel).send().label()
	} catch (e: Exception) {
		e.printStackTrace()
		null
	}

	private fun srvNetworkLabelFromNetwork(c: Connection, networkId: String, networkLabelId: String): NetworkLabelService
		= srvNetworkLabelsFromNetwork(c, networkId).labelService(networkLabelId)
	fun removeNetworkLabelFromNetwork(c: Connection, networkId: String, networkLabelId: String): Boolean = try {
		srvNetworkLabelFromNetwork(c, networkId, networkLabelId).remove().send()
		true
	} catch (e: Exception) {
		e.printStackTrace()
		false
	}
	private fun srvVnicProfilesFromNetwork(c: Connection, networkId: String): AssignedVnicProfilesService
		= srvNetwork(c, networkId).vnicProfilesService()
	fun findAllVnicProfilesFromNetwork(c: Connection, networkId: String): List<VnicProfile>
		= srvVnicProfilesFromNetwork(c, networkId).list().send().profiles()


	//endregion

	//region: OpenStackNetworkProvider
	private fun srvOpenStackNetworkProviders(c: Connection): OpenstackNetworkProvidersService
		= getSystemService(c).openstackNetworkProvidersService()
	fun findAllOpenStackNetworkProviders(c: Connection): List<OpenStackNetworkProvider>
		= srvOpenStackNetworkProviders(c).list().send().providers()
	private fun srvOpenStackNetworkProvider(c: Connection, networkProviderId: String): OpenstackNetworkProviderService
		 = srvOpenStackNetworkProviders(c).providerService(networkProviderId)
	fun findOpenStackNetworkProvider(c: Connection, networkProviderId: String): OpenStackNetworkProvider
		= srvOpenStackNetworkProvider(c, networkProviderId).get().send().provider()
	//endregion

	//region: OpenStackImageProvider
	private fun srvOpenStackImageProviders(c: Connection): OpenstackImageProvidersService
		= getSystemService(c).openstackImageProvidersService()
	fun findAllOpenStackImageProviders(c: Connection): List<OpenStackImageProvider>
		= srvOpenStackImageProviders(c).list().send().providers()
	private fun srvOpenStackImageProvider(c: Connection, openStackImageProviderId: String): OpenstackImageProviderService
		= srvOpenStackImageProviders(c).providerService(openStackImageProviderId)
	fun findOpenStackImageProvider(c: Connection, openStackImageProviderId: String): OpenStackImageProvider
		= srvOpenStackImageProvider(c, openStackImageProviderId).get().send().provider()
	//endregion

	//region: OpenStackVolumeProvider
	private fun srvOpenStackVolumeProviders(c: Connection): OpenstackVolumeProvidersService
			= getSystemService(c).openstackVolumeProvidersService()
	fun findAllOpenStackVolumeProviders(c: Connection): List<OpenStackVolumeProvider>
			= srvOpenStackVolumeProviders(c).list().send().providers()
	private fun srvOpenStackVolumeProvider(c: Connection, openStackVolumeProviderId: String): OpenstackVolumeProviderService
			= srvOpenStackVolumeProviders(c).providerService(openStackVolumeProviderId)
	fun findOpenStackVolumeProvider(c: Connection, openStackVolumeProviderId: String): OpenStackVolumeProvider
			= srvOpenStackVolumeProvider(c, openStackVolumeProviderId).get().send().provider()
	//endregion

	//region: ExternalHostProvider
	private fun srvExternalHostProviders(c: Connection): ExternalHostProvidersService
		= getSystemService(c).externalHostProvidersService()
	fun findAllExternalHostProviders(c: Connection): List<ExternalHostProvider>
		= srvExternalHostProviders(c).list().send().providers()
	private fun srvExternalHostProvider(c: Connection, externalHostProviderId: String): ExternalHostProviderService
		= srvExternalHostProviders(c).providerService(externalHostProviderId)
	fun findExternalHostProvider(c: Connection, externalHostProviderId: String): ExternalHostProvider
		= srvExternalHostProvider(c, externalHostProviderId).get().send().provider()
	//endregion

	//region Template
	private fun srvTemplates(c: Connection): TemplatesService
		= getSystemService(c).templatesService()
	fun findAllTemplates(c: Connection, searchQuery: String = ""): List<Template> {
		return if (searchQuery == "")	srvTemplates(c).list().send().templates()
		else							srvTemplates(c).list().search(searchQuery).send().templates()
	}
	private fun srvTemplate(c: Connection, templateId: String): TemplateService
		= getSystemService(c).templatesService().templateService(templateId)
	fun findTemplate(c: Connection, templateId: String): Template
		= srvTemplate(c, templateId).get().send().template()
	fun removeTemplate(c: Connection, templateId: String): Boolean = try {
		srvTemplate(c, templateId).remove().send()
		true
	} catch (e: Exception) {
		e.printStackTrace()
		false
	}
	fun exportTemplate(c: Connection, templateId: String, exclusive: Boolean, toStorageDomain: StorageDomain): Boolean = try {
		srvTemplate(c, templateId).export()
			.exclusive(exclusive)
			.storageDomain(toStorageDomain)
			.send()
		true
	} catch (e: Exception) {
		e.printStackTrace()
		false
	}

	fun addTemplate(c: Connection, template: Template, clonePermissions: Boolean = false, seal: Boolean = false): Template? = try {
		srvTemplates(c).add().template(template)
			.clonePermissions(clonePermissions)
			.seal(seal)
			.send().template()
	} catch (e: Exception) {
		e.printStackTrace()
		null
	}
	private fun srvWatchdogsFromTemplate(c: Connection, templateId: String): TemplateWatchdogsService
		= srvTemplate(c, templateId).watchdogsService()
	fun findAllWatchdogsFromTemplate(c: Connection, templateId: String): List<Watchdog>
		= srvWatchdogsFromTemplate(c, templateId).list().send().watchdogs()
	private fun srvCdromsFromTemplate(c: Connection, templateId: String): TemplateCdromsService
		= srvTemplate(c, templateId).cdromsService()
	fun findAllCdromsFromTemplate(c: Connection, templateId: String): List<Cdrom>
		= srvCdromsFromTemplate(c, templateId).list().send().cdroms()
	private fun srvDiskAttachmentsFromTemplate(c: Connection, templateId: String): TemplateDiskAttachmentsService
		= srvTemplate(c, templateId).diskAttachmentsService()
	fun findAllDiskAttachmentsFromTemplate(c: Connection, templateId: String): List<DiskAttachment>
		= srvDiskAttachmentsFromTemplate(c, templateId).list().send().attachments()
	private fun srvNicsFromTemplate(c: Connection, templateId: String): TemplateNicsService
		= srvTemplate(c, templateId).nicsService()
	fun findAllNicsFromTemplate(c: Connection, templateId: String): List<Nic>
		= srvNicsFromTemplate(c, templateId).list().send().nics()

	//endregion

	//region: OperatingSystemInfo
	private fun srvOperatingSystems(c: Connection): OperatingSystemsService
		= getSystemService(c).operatingSystemsService()
	fun findAllOperatingSystems(c: Connection): List<OperatingSystemInfo>
		= srvOperatingSystems(c).list().send().operatingSystem()
	//endregion

	//region: StorageDomain
	private fun srvStorageDomains(c: Connection): StorageDomainsService
		= getSystemService(c).storageDomainsService()
	fun findAllStorageDomains(c: Connection, searchQuery: String = ""): List<StorageDomain>
		= if (searchQuery == "")	srvStorageDomains(c).list().search(searchQuery).send().storageDomains()
		else 						srvStorageDomains(c).list().send().storageDomains()
	private fun srvStorageDomain(c: Connection, storageId: String): StorageDomainService
		= srvStorageDomains(c).storageDomainService(storageId)
	fun findStorageDomain(c: Connection, storageId: String): StorageDomain
		= srvStorageDomain(c, storageId).get().send().storageDomain()
	private fun srvFileFromStorageDomain(c: Connection, storageId: String): FilesService
		= srvStorageDomain(c, storageId).filesService()
	fun findAllFilesFromStorageDomain(c: Connection, storageId: String): List<File>
		= srvFileFromStorageDomain(c, storageId).list().send().file()
	private fun srvDisksFromStorageDomain(c: Connection, storageId: String): StorageDomainDisksService
		= srvStorageDomain(c, storageId).disksService()
	fun findAllDisksFromStorageDomain(c: Connection, storageId: String): List<Disk>
		= srvDisksFromStorageDomain(c, storageId).list().send().disks()
	private fun srvTemplatesFromStorageDomain(c: Connection, storageId: String): StorageDomainTemplatesService
		= srvStorageDomain(c, storageId ).templatesService()
	fun findAllTemplatesFromStorageDomain(c: Connection, storageId: String): List<Template>
		= srvTemplatesFromStorageDomain(c, storageId).list().send().templates()
	//endregion

	//region: CPUProfile
	private fun srvCpuProfiles(c: Connection): CpuProfilesService
		= getSystemService(c).cpuProfilesService()
	fun findAllCpuProfiles(c: Connection): List<CpuProfile>
		= srvCpuProfiles(c).list().send().profile()
	//endregion

	//region: DiskProfile
	private fun srvDiskProfiles(c: Connection): DiskProfilesService
		= getSystemService(c).diskProfilesService()
	fun findAllDiskProfiles(c: Connection): List<DiskProfile>
		= srvDiskProfiles(c).list().send().profile()
	private fun srvDiskProfile(c: Connection, diskProfileId: String): DiskProfileService
		= srvDiskProfiles(c).diskProfileService(diskProfileId)
	fun findDiskProfile(c: Connection, diskProfileId: String): DiskProfile
		= srvDiskProfile(c, diskProfileId).get().send().profile()
	//endregion

	//region: Disk
	private fun srvDisks(c: Connection): DisksService
		= getSystemService(c).disksService()
	fun findAllDisks(c: Connection, searchQuery: String = ""): List<Disk> {
		return if (searchQuery == "")	srvDisks(c).list().send().disks()
		else							srvDisks(c).list().search(searchQuery).caseSensitive(false).send().disks()
	}
	//endregion

	//region: DataCenter
	private fun srvDataCenters(c: Connection): DataCentersService
		= getSystemService(c).dataCentersService()
	fun findAllDataCenters(c: Connection): List<DataCenter>
		= srvDataCenters(c).list().send().dataCenters()
	private fun srvDataCenter(c: Connection, dataCenterId: String): DataCenterService
		= srvDataCenters(c).dataCenterService(dataCenterId)
	fun findDataCenter(c: Connection, dataCenterId: String): DataCenter
		= srvDataCenter(c, dataCenterId).get().send().dataCenter()
	private fun srvQossFromDataCenter(c: Connection, dataCenterId: String): QossService
		= srvDataCenter(c, dataCenterId).qossService()
	fun findAllQossFromDataCenter(c: Connection, dataCenterId: String): List<Qos>
		= srvQossFromDataCenter(c, dataCenterId).list().send().qoss()
	private fun srvQuotasFromDataCenter(c: Connection, dataCenterId: String): QuotasService
		= srvDataCenter(c, dataCenterId).quotasService()
	fun findAllQuotasFromDataCenter(c: Connection, dataCenterId: String): List<Quota>
		= srvQuotasFromDataCenter(c, dataCenterId).list().send().quotas()
	fun addQuotaFromDataCenter(c: Connection, dataCenterId: String, quota: Quota): Quota? = try {
		srvQuotasFromDataCenter(c, dataCenterId).add().quota(quota).send().quota()
	} catch (e: Exception) {
		e.printStackTrace()
		null
	}
	private fun srvQuotaFromDataCenter(c: Connection, dataCenterId: String, quotaId: String): QuotaService
		= srvQuotasFromDataCenter(c, dataCenterId).quotaService(quotaId)
	fun findQuotaFromDataCenter(c: Connection, dataCenterId: String, quotaId: String): Quota
		= srvQuotaFromDataCenter(c, dataCenterId, quotaId).get().send().quota()
	private fun srvQuotaClusterLimitsFromDataCenter(c: Connection, dataCenterId: String, quotaId: String): QuotaClusterLimitsService
		= srvQuotaFromDataCenter(c, dataCenterId, quotaId).quotaClusterLimitsService()
	fun findAllQuotaClusterLimitsFromDataCenter(c: Connection, dataCenterId: String, quotaId: String): List<QuotaClusterLimit>
		= srvQuotaClusterLimitsFromDataCenter(c, dataCenterId, quotaId).list().send().limits()
	private fun srvQuotaStorageLimitsFromDataCenter(c: Connection, dataCenterId: String, quotaId: String): QuotaStorageLimitsService
		= srvQuotaFromDataCenter(c, dataCenterId, quotaId).quotaStorageLimitsService()
	fun findAllQuotaStorageLimitsFromDataCenter(c: Connection, dataCenterId: String, quotaId: String): List<QuotaStorageLimit>
		= srvQuotaStorageLimitsFromDataCenter(c, dataCenterId, quotaId).list().send().limits()
	//endregion

	//region: InstanceType
	private fun srvInstanceTypes(c: Connection): InstanceTypesService
		= getSystemService(c).instanceTypesService()
	fun findAllInstanceTypes(c: Connection): List<InstanceType>
		= srvInstanceTypes(c).list().send().instanceType()
	private fun srvInstanceType(c: Connection, instanceTypeId: String): InstanceTypeService
		= srvInstanceTypes(c).instanceTypeService(instanceTypeId)
	fun findInstanceType(c: Connection, instanceTypeId: String): InstanceType
		= srvInstanceType(c, instanceTypeId).get().send().instanceType()
	private fun srvNicsFromInstanceType(c: Connection, instanceTypeId: String): InstanceTypeNicsService
		= srvInstanceType(c, instanceTypeId).nicsService()
	fun addNicForInstanceType(c: Connection, instanceTypeId: String, nic: Nic): Boolean = try {
		srvNicsFromInstanceType(c, instanceTypeId).add().nic(nic).send()
		true
	} catch (e: Exception) {
		e.printStackTrace()
		false
	}

	fun addInstanceType(c: Connection, instanceType: InstanceType): InstanceType? = try {
		srvInstanceTypes(c).add().instanceType(instanceType).send().instanceType()
	} catch (e: Exception) {
		e.printStackTrace()
		null
	}

	fun updateInstanceType(c: Connection, instanceType: InstanceType): InstanceType? = try {
		srvInstanceType(c, instanceType.id()).update().send().instanceType()
	} catch (e: Exception) {
		e.printStackTrace()
		null
	}
	fun removeInstanceType(c: Connection, instanceTypeId: String): Boolean = try {
		srvInstanceType(c, instanceTypeId).remove().send()
		true
	} catch (e: Exception) {
		e.printStackTrace()
		false
	}
	//endregion

	//region: MacPool
	private fun srvMacPools(c: Connection): MacPoolsService
		= getSystemService(c).macPoolsService()
	fun findAllMacPools(c: Connection): List<MacPool>
		= srvMacPools(c).list().send().pools()
	//endregion

	//region: Event
	private fun srvEvents(c: Connection): EventsService
		= getSystemService(c).eventsService()
	fun findAllEvents(c: Connection, searchQuery: String = ""): List<Event> =
		if (searchQuery == "")	srvEvents(c).list().send().events()
		else 							srvEvents(c).list().search(searchQuery).caseSensitive(false).send().events()
	private fun srvEvent(c: Connection, eventId: String): EventService
		= srvEvents(c).eventService(eventId)
	fun findEvent(c: Connection, eventId: String): Event
		= srvEvent(c, eventId).get().send().event()
	//endregion

	//region: SystemPermissions
	private fun srvSystemPermissions(c: Connection): SystemPermissionsService
		= getSystemService(c).permissionsService()
	fun findAllPermissions(c: Connection): List<Permission>
		= srvSystemPermissions(c).list().send().permissions()
	fun addPermission(c: Connection, permission: Permission): Permission? = try {
		srvSystemPermissions(c).add().permission(permission).send().permission()
	} catch (e: Exception) {
		e.printStackTrace()
		null
	}
	fun srvSystemPermission(c: Connection, permissionId: String): PermissionService
		= srvSystemPermissions(c).permissionService(permissionId)
	fun removePermission(c: Connection, permissionId: String): Boolean = try {
		srvSystemPermission(c, permissionId).remove().send()
		true
	} catch (e: Exception) {
		e.printStackTrace()
		false
	}
	//endregion

	//region: User
	private fun srvUsers(c: Connection): UsersService
		= getSystemService(c).usersService()
	fun findAllUsers(c: Connection): List<User>
		= srvUsers(c).list().send().users()
	fun addUser(c: Connection, user: User): User? = try {
		srvUsers(c).add().user(user).send().user()
	} catch (e: Exception) {
		e.printStackTrace()
		null
	}
	private fun srvUser(c: Connection, userId: String): UserService
		= srvUsers(c).userService(userId)
	fun findUser(c: Connection, userId: String): User
		= srvUser(c, userId).get().send().user()

	//endregion

	//region: Group
	private fun srvGroups(c: Connection): GroupsService
		= getSystemService(c).groupsService()
	fun findAllGroups(c: Connection): List<Group>
		= srvGroups(c).list().send().groups()
	private fun srvGroup(c: Connection, groupId: String): GroupService
		= srvGroups(c).groupService(groupId)
	fun findGroup(c: Connection, groupId: String): Group
		= srvGroup(c, groupId).get().send().get()
	//endregion

	//region: Role
	private fun srvRoles(c: Connection): RolesService
		= getSystemService(c).rolesService()
	fun findAllRoles(c: Connection): List<Role>
		= srvRoles(c).list().send().roles()
	private fun srvRole(c: Connection, roleId: String): RoleService
		= srvRoles(c).roleService(roleId)
	fun findRole(c: Connection, roleId: String): Role
		= srvRole(c, roleId).get().send().role()
	//endregion
}