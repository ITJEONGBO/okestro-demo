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

	public SystemService system() {
		return conn.getConnection().systemService();
	}
	public SystemService admin() {
		return admin.getConnection().systemService();
	}


	// region: list들
	public List<DataCenter> dataCenterList(){
		return system().dataCentersService().list().send().dataCenters();
	}
	public List<Cluster> clusterList(){
		return system().clustersService().list().send().clusters();
	}
	public List<Host> hostList(){
		return system().hostsService().list().send().hosts();
	}
	public List<Vm> vmList(){
		return system().vmsService().list().send().vms();
	}
	public List<Template> templateList(){
		return system().templatesService().list().send().templates();
	}
	public List<Event> eventList(){
		return system().eventsService().list().send().events();
	}


	// network
	public List<VnicProfile> vnicProfileList(){
		return system().vnicProfilesService().list().send().profiles();
	}
	public List<Network> networkList(){
		return system().networksService().list().send().networks();
	}


	// storage
	public List<StorageDomain> domainList(){
		return system().storageDomainsService().list().send().storageDomains();
	}
	public List<Disk> diskList(){
		return system().disksService().list().send().disks();
	}

	//	public List<Volume>



	// endregion

	// region: id
	public DataCenter dataCenter(String id){
		return system().dataCentersService().dataCenterService(id).get().send().dataCenter();
	}
	public Cluster cluster(String id){
		return admin().clustersService().clusterService(id).get().send().cluster();
	}
	public Host host(String id){
		return system().hostsService().hostService(id).get().send().host();
	}
	public Vm vm(String id){
		return system().vmsService().vmService(id).get().send().vm();
	}
	public Template template(String id){
		return system().templatesService().templateService(id).get().send().template();
	}




	// network
	public VnicProfile vnic(String id){
		return system().vnicProfilesService().profileService(id).get().send().profile();
	}
	public Network network(String id){
		return system().networksService().networkService(id).get().send().network();
	}


	// endregion







	// region : datacenter

	// 데이터 센터 - 스토리지
	public List<StorageDomain> dcDomainList(String id) {
		return system().dataCentersService().dataCenterService(id).storageDomainsService().list().send().storageDomains();
	}
	// 데이터 센터 - 네트워크
	public List<Network> dcNetworkList(String id) {
		return system().dataCentersService().dataCenterService(id).networksService().list().send().networks();
	}
	// 데이터 센터 - 클러스터
	public List<Cluster> dcClusterList(String id) {
		return system().dataCentersService().dataCenterService(id).clustersService().list().send().clusters();
	}
	// 데이터 센터 - 권한
	public List<Permission> dcPermissionList(String id) {
		return system().dataCentersService().dataCenterService(id).permissionsService().list().send().permissions();
	}

	// 권한으로 뺴야함
	public Group group(String groupId){
		return ((GroupService.GetResponse) system().groupsService().groupService(groupId).get().send()).get();
	}
	public User user(String userId){
		return ((UserService.GetResponse) system().usersService().userService(userId).get().send()).user();
	}
	public Role role(String roleId){
		return ((RoleService.GetResponse) system().rolesService().roleService(roleId).get().send()).role();
	}


	// endregion


	// region : cluster
	public List<Network> cNetworkList (String id) {
		return ((ClusterNetworksService.ListResponse) system().clustersService().clusterService(id).networksService().list().send()).networks();
	}
	public List<Nic> cNicList (String vmId) {
		return ((VmNicsService.ListResponse) system().vmsService().vmService(vmId).nicsService().list().send()).nics();
	}
	public List<ReportedDevice> cReportedDeviceList (String vmId, String nicId) {
		return ((VmReportedDevicesService.ListResponse) system().vmsService().vmService(vmId).nicsService().nicService(nicId).reportedDevicesService().list().send()).reportedDevice();
	}
	public List<AffinityGroup> cAffinityGroupList (String id) {
		return ((AffinityGroupsService.ListResponse) system().clustersService().clusterService(id).affinityGroupsService().list().send()).groups();
	}
	public List<AffinityLabel> cAffinityLabelList() {
		return ((AffinityLabelsService.ListResponse) system().affinityLabelsService().list().send()).labels();
	}
	public List<Permission> cPermissionList(String id) {
		return ((AssignedPermissionsService.ListResponse) system().clustersService().clusterService(id).permissionsService().list().send()).permissions();
	}


	// endregion


	// region : Statistic

	// vm
	public List<Statistic> vmStatisticList(String vmId) {
		return ((StatisticsService.ListResponse) system().vmsService().vmService(vmId).statisticsService().list().send()).statistics();
	}



	// endregion


	public String getName(String svcName, String id) {
		String st = "";

		// computing
		if (svcName.equals("datacenter")) {
			st = system().dataCentersService().dataCenterService(id).get().send().dataCenter().name();
		}
		if (svcName.equals("cluster")) {
			st = system().clustersService().clusterService(id).get().send().cluster().name();
		}
		if (svcName.equals("host")) {
			st = system().hostsService().hostService(id).get().send().host().name();
		}
		if (svcName.equals("vm")) {
			st = system().vmsService().vmService(id).get().send().vm().name();
		}
		if (svcName.equals("template")) {
			st = system().templatesService().templateService(id).get().send().template().name();
		}


		// network
		if (svcName.equals("vnic")) {
			st = system().vnicProfilesService().profileService(id).get().send().profile().name();
		}
		if (svcName.equals("network")) {
			st = system().networksService().networkService(id).get().send().network().name();
		}


		// storage
		if (svcName.equals("domain")) {
			st = system().storageDomainsService().storageDomainService(id).get().send().storageDomain().name();
		}
		if (svcName.equals("disk")) {
			st = system().disksService().diskService(id).get().send().disk().name();
		}
//		if (svcName.equals("volume")) {
//			st = system().storageDomainsService().storageDomainService(id).get().send().storageDomain().name();
//		}





		return st;
	}


}