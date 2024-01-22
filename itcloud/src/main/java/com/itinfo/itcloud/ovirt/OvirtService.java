package com.itinfo.itcloud.ovirt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.ovirt.engine.sdk4.services.*;
import org.ovirt.engine.sdk4.types.*;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OvirtService {
	private final ConnectionService conn;
	private final AdminConnectionService admin;

	public SystemService getSystemService() {
		return conn.getConnection().systemService();
	}
	public SystemService getAdminSystemService() {
		return admin.getConnection().systemService();
	}


	// region: list들
	public List<DataCenter> dataCenterList(){
		return ((DataCentersService.ListResponse)getSystemService().dataCentersService().list().send()).dataCenters();
	}
	public List<Cluster> clusterList(){
		return ((ClustersService.ListResponse)getSystemService().clustersService().list().send()).clusters();
	}
	public List<Host> hostList(){
		return ((HostsService.ListResponse)getSystemService().hostsService().list().send()).hosts();
	}
	public List<Vm> vmList(){
		return ((VmsService.ListResponse)getSystemService().vmsService().list().send()).vms();
	}
	public List<Template> templateList(){
		return ((TemplatesService.ListResponse)getSystemService().templatesService().list().send()).templates();
	}
	public List<Event> eventList(){
		return ((EventsService.ListResponse)getSystemService().eventsService().list().send()).events();
	}


	// network
	public List<VnicProfile> vnicProfileList(){
		return ((VnicProfilesService.ListResponse)getSystemService().vnicProfilesService().list().send()).profiles();
	}
	public List<Network> networkList(){
		return ((NetworksService.ListResponse)getSystemService().networksService().list().send()).networks();
	}


	// storage
	public List<StorageDomain> domainList(){
		return ((StorageDomainsService.ListResponse)getSystemService().storageDomainsService().list().send()).storageDomains();
	}
	public List<Disk> diskList(){
		return ((DisksService.ListResponse)getSystemService().disksService().list().send()).disks();
	}

	//	public List<Volume>



	// endregion

	// region: id
	public DataCenter dataCenter(String id){
		return ((DataCenterService.GetResponse) getSystemService().dataCentersService().dataCenterService(id).get().send()).dataCenter();
	}
	public Cluster cluster(String id){
		return ((ClusterService.GetResponse) getAdminSystemService().clustersService().clusterService(id).get().send()).cluster();
	}
	public Host host(String id){
		return ((HostService.GetResponse) getSystemService().hostsService().hostService(id).get().send()).host();
	}
	public Vm vm(String id){
		return ((VmService.GetResponse) getSystemService().vmsService().vmService(id).get().send()).vm();
	}
	public Template template(String id){
		return ((TemplateService.GetResponse) getSystemService().templatesService().templateService(id).get().send()).template();
	}




	// network
	public VnicProfile vnic(String id){
		return ((VnicProfileService.GetResponse)getSystemService().vnicProfilesService().profileService(id).get().send()).profile();
	}
	public Network network(String id){
		return ((NetworkService.GetResponse) getSystemService().networksService().networkService(id).get().send()).network();
	}


	// endregion







	// region : datacenter

	// 데이터 센터 - 스토리지
	public List<StorageDomain> dcDomainList(String id) {
		return ((AttachedStorageDomainsService.ListResponse) getSystemService().dataCentersService().dataCenterService(id).storageDomainsService().list().send()).storageDomains();
	}
	// 데이터 센터 - 네트워크
	public List<Network> dcNetworkList(String id) {
		return ((DataCenterNetworksService.ListResponse) getSystemService().dataCentersService().dataCenterService(id).networksService().list().send()).networks();
	}
	// 데이터 센터 - 클러스터
	public List<Cluster> dcClusterList(String id) {
		return ((ClustersService.ListResponse) getSystemService().dataCentersService().dataCenterService(id).clustersService().list().send()).clusters();
	}
	// 데이터 센터 - 권한
	public List<Permission> dcPermissionList(String id) {
		return ((AssignedPermissionsService.ListResponse) getSystemService().dataCentersService().dataCenterService(id).permissionsService().list().send()).permissions();
	}

	// 권한으로 뺴야함
	public Group group(String groupId){
		return ((GroupService.GetResponse) getSystemService().groupsService().groupService(groupId).get().send()).get();
	}
	public User user(String userId){
		return ((UserService.GetResponse) getSystemService().usersService().userService(userId).get().send()).user();
	}
	public Role role(String roleId){
		return ((RoleService.GetResponse) getSystemService().rolesService().roleService(roleId).get().send()).role();
	}


	// endregion


	// region : cluster
	public List<Network> cNetworkList (String id) {
		return ((ClusterNetworksService.ListResponse) getSystemService().clustersService().clusterService(id).networksService().list().send()).networks();
	}
	public List<Nic> cNicList (String vmId) {
		return ((VmNicsService.ListResponse) getSystemService().vmsService().vmService(vmId).nicsService().list().send()).nics();
	}
	public List<ReportedDevice> cReportedDeviceList (String vmId, String nicId) {
		return ((VmReportedDevicesService.ListResponse) getSystemService().vmsService().vmService(vmId).nicsService().nicService(nicId).reportedDevicesService().list().send()).reportedDevice();
	}
	public List<AffinityGroup> cAffinityGroupList (String id) {
		return ((AffinityGroupsService.ListResponse) getSystemService().clustersService().clusterService(id).affinityGroupsService().list().send()).groups();
	}
	public List<AffinityLabel> cAffinityLabelList() {
		return ((AffinityLabelsService.ListResponse) getSystemService().affinityLabelsService().list().send()).labels();
	}
	public List<Permission> cPermissionList(String id) {
		return ((AssignedPermissionsService.ListResponse) getSystemService().clustersService().clusterService(id).permissionsService().list().send()).permissions();
	}


	// endregion


	// region : Statistic

	// vm
	public List<Statistic> vmStatisticList(String vmId) {
		return ((StatisticsService.ListResponse) getSystemService().vmsService().vmService(vmId).statisticsService().list().send()).statistics();
	}



	// endregion


	public String getName(String svcName, String id) {
		String st = "";

		// computing
		if (svcName.equals("datacenter")) {
			st = getSystemService().dataCentersService().dataCenterService(id).get().send().dataCenter().name();
		}
		if (svcName.equals("cluster")) {
			st = getSystemService().clustersService().clusterService(id).get().send().cluster().name();
		}
		if (svcName.equals("host")) {
			st = getSystemService().hostsService().hostService(id).get().send().host().name();
		}
		if (svcName.equals("vm")) {
			st = getSystemService().vmsService().vmService(id).get().send().vm().name();
		}
		if (svcName.equals("template")) {
			st = getSystemService().templatesService().templateService(id).get().send().template().name();
		}


		// network
		if (svcName.equals("vnic")) {
			st = getSystemService().vnicProfilesService().profileService(id).get().send().profile().name();
		}
		if (svcName.equals("network")) {
			st = getSystemService().networksService().networkService(id).get().send().network().name();
		}


		// storage
		if (svcName.equals("domain")) {
			st = getSystemService().storageDomainsService().storageDomainService(id).get().send().storageDomain().name();
		}
		if (svcName.equals("disk")) {
			st = getSystemService().disksService().diskService(id).get().send().disk().name();
		}
//		if (svcName.equals("volume")) {
//			st = getSystemService().storageDomainsService().storageDomainService(id).get().send().storageDomain().name();
//		}





		return st;
	}


}