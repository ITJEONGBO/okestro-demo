package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.ClustersService;
import com.itinfo.service.HostsService;
import com.itinfo.model.karajan.ConsolidationVo;
import com.itinfo.model.karajan.HostVo;
import com.itinfo.model.*;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class HostsController {
	@Autowired private HostsService hostsService;
	@Autowired private ClustersService clustersService;

	@RequestMapping({"/compute/createHost"})
	public String createHostView() {
		log.info("... createHostView");
		return "/castanets/compute/createHost";
	}

	@RequestMapping(value = {"/compute/updateHost"}, method = {RequestMethod.GET})
	public String createHostView(String id, Model model) {
		log.info("... createHostView('{}')", id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, id);
		return "/castanets/compute/createHost";
	}

	@RequestMapping({"/compute/hosts"})
	public String hostsView() {
		log.info("... hostsView");
		return "/castanets/compute/hosts";
	}

	@RequestMapping(value = {"/compute/host"}, method = {RequestMethod.GET})
	public String hostView(String id, Model model) {
		log.info("... hostView('{}')", id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, id);
		return "/castanets/compute/host";
	}

	@RequestMapping(value = {"/compute/hosts/retrieveCreateHostInfo"}, method = {RequestMethod.GET})
	public String retrieveCreateClusterInfo(String id, Model model) {
		log.info("... retrieveCreateClusterInfo('{}')", id);
		HostCreateVo hostCreateVo
				= hostsService.retrieveCreateHostInfo(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, hostCreateVo);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/retrieveHostsInfo"}, method = {RequestMethod.GET})
	public String retrieveHostsInfo(String status, Model model) {
		log.info("... retrieveHostsInfo('{}')", status);
		List<HostDetailVo> hosts
				= hostsService.retrieveHostsInfo(status);
		model.addAttribute(ItInfoConstant.RESULT_KEY, hosts);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/retrieveLunHostsInfo"}, method = {RequestMethod.GET})
	public String retrieveLunHostsInfo(String status, Model model) {
		log.info("... retrieveLunHostsInfo('{}')", status);
		List<HostDetailVo> hosts
				= hostsService.retrieveLunHostsInfo(status);
		model.addAttribute(ItInfoConstant.RESULT_KEY, hosts);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/retrieveHostDetail"}, method = {RequestMethod.GET})
	public String retrieveHostDetail(String id, Model model) {
		log.info("... retrieveHostDetail('{}')", id);
		HostDetailVo hostDetailVo
				= hostsService.retrieveHostDetail(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, hostDetailVo);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/retrieveHostEvents"}, method = {RequestMethod.GET})
	public String retrieveHostEvents(String id, Model model) {
		log.info("... retrieveHostEvents('{}')", id);
		List<EventVo> events = hostsService.retrieveHostEvents(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, events);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/consolidateVms"}, method = {RequestMethod.POST})
	public String consolidateVms(@RequestBody List<String> hosts, Model model) {
		log.info("... consolidateVms[{}]", hosts.size());
		List<ConsolidationVo> result = hostsService.maintenanceBeforeConsolidateVms(hosts);
		model.addAttribute(ItInfoConstant.RESULT_KEY, result);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/maintenanceStart"}, method = {RequestMethod.POST})
	public String maintenanceStart(@RequestBody List<String> hosts, Model model) {
		log.info("... maintenanceStart[{}]", hosts.size());
		hostsService.maintenanceStart(hosts);
		try { Thread.sleep(500L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/maintenanceStop"}, method = {RequestMethod.POST})
	public String maintenanceStop(@RequestBody List<String> hosts, Model model) {
		log.info("... maintenanceStop[{}]", hosts.size());
		hostsService.maintenanceStop(hosts);
		try { Thread.sleep(500L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/restartHost"}, method = {RequestMethod.POST})
	public String restartHost(@RequestBody List<String> hosts, Model model) {
		log.info("... restartHost[{}]", hosts.size());
		hostsService.restartHost(hosts);
		try { Thread.sleep(500L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/startHost"}, method = {RequestMethod.POST})
	public String startHost(@RequestBody List<String> hosts, Model model) {
		log.info("... startHost[{}]", hosts.size());
		hostsService.startHost(hosts);
		try { Thread.sleep(500L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/stopHost"}, method = {RequestMethod.POST})
	public String stopHost(@RequestBody List<String> hosts, Model model) {
		log.info("... stopHost[{}]", hosts.size());
		hostsService.stopHost(hosts);
		try { Thread.sleep(500L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/createHost"}, method = {RequestMethod.POST})
	public String createHost(@RequestBody HostCreateVo hostCreateVo, Model model) throws Exception {
		log.info("... createHost");
		hostsService.createHost(hostCreateVo);
		try { Thread.sleep(500L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/updateHost"}, method = {RequestMethod.POST})
	public String updateHost(@RequestBody HostCreateVo hostCreateVo, Model model) throws Exception {
		log.info("... updateHost");
		hostsService.updateHost(hostCreateVo);
		try { Thread.sleep(500L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/removeHost"}, method = {RequestMethod.POST})
	public String removeHost(@RequestBody List<String> hosts, Model model) throws Exception {
		log.info("... removeHost[{}]", hosts.size());
		hostsService.removeHost(hosts);
		try { Thread.sleep(500L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/retrieveClusters"}, method = {RequestMethod.GET})
	public String retrieveClusters(Model model) {
		log.info("... retrieveClusters");
		List<ClusterVo> clusters = this.clustersService.retrieveClusters();
		model.addAttribute(ItInfoConstant.RESULT_KEY, clusters);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/retrieveFanceAgentType"}, method = {RequestMethod.GET})
	public String retrieveFanceAgentType(Model model) {
		log.info("... retrieveClusters");
		List<String> fenceAgentTypes = hostsService.retrieveFanceAgentType();
		model.addAttribute(ItInfoConstant.RESULT_KEY, fenceAgentTypes);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/connectTestFenceAgent"}, method = {RequestMethod.POST})
	public String connectTestFenceAgent(@RequestBody FenceAgentVo fenceAgentVo, Model model) {
		log.info("... connectTestFenceAgent");
		hostsService.connectTestFenceAgent(fenceAgentVo);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/shutdownHost"}, method = {RequestMethod.GET})
	public String shutdownHost(@RequestParam("id") String id, Model model) throws Exception {
		log.info("... shutdownHost('{}')", id);
		HostVo host = new HostVo();
		host.setId(id);
		List<HostVo> hosts = new ArrayList<>();
		hosts.add(host);
		hostsService.shutdownHost(hosts);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/setupHostNetwork"}, method = {RequestMethod.POST})
	public String setupHostNetwork(@RequestBody List<NicUsageApiVo> nicUsageApiVoList, Model model) {
		log.info("... setupHostNetwork");
		hostsService.setupHostNetwork(nicUsageApiVoList);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/modifyNicNetwork"}, method = {RequestMethod.POST})
	public String modifyNicNetwork(@RequestBody NetworkAttachmentVo networkAttachmentVo, Model model) {
		log.info("... modifyNicNetwork");
		hostsService.modifyNicNetwork(networkAttachmentVo);
		model.addAttribute(ItInfoConstant.RESULT_KEY);
		return "jsonView";
	}
}

