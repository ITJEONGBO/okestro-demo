package com.itinfo.controller;

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
		log.info("... createHostView("+id+")");
		model.addAttribute("resultKey", id);
		return "/castanets/compute/createHost";
	}

	@RequestMapping({"/compute/hosts"})
	public String hostsView() {
		log.info("... hostsView");
		return "/castanets/compute/hosts";
	}

	@RequestMapping(value = {"/compute/host"}, method = {RequestMethod.GET})
	public String hostView(String id, Model model) {
		log.info("... hostView("+id+")");
		model.addAttribute("resultKey", id);
		return "/castanets/compute/host";
	}

	@RequestMapping(value = {"/compute/hosts/retrieveCreateHostInfo"}, method = {RequestMethod.GET})
	public String retrieveCreateClusterInfo(String id, Model model) {
		log.info("... retrieveCreateClusterInfo("+id+")");
		HostCreateVo hostCreateVo
				= this.hostsService.retrieveCreateHostInfo(id);
		model.addAttribute("resultKey", hostCreateVo);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/retrieveHostsInfo"}, method = {RequestMethod.GET})
	public String retrieveHostsInfo(String status, Model model) {
		log.info("... retrieveHostsInfo("+status+")");
		List<HostDetailVo> hosts
				= this.hostsService.retrieveHostsInfo(status);
		model.addAttribute("resultKey", hosts);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/retrieveLunHostsInfo"}, method = {RequestMethod.GET})
	public String retrieveLunHostsInfo(String status, Model model) {
		log.info("... retrieveLunHostsInfo("+status+")");
		List<HostDetailVo> hosts
				= this.hostsService.retrieveLunHostsInfo(status);
		model.addAttribute("resultKey", hosts);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/retrieveHostDetail"}, method = {RequestMethod.GET})
	public String retrieveHostDetail(String id, Model model) {
		log.info("... retrieveHostDetail("+id+")");
		HostDetailVo hostDetailVo
				= this.hostsService.retrieveHostDetail(id);
		model.addAttribute("resultKey", hostDetailVo);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/retrieveHostEvents"}, method = {RequestMethod.GET})
	public String retrieveHostEvents(String id, Model model) {
		log.info("... retrieveHostEvents("+id+")");
		List<EventVo> events = this.hostsService.retrieveHostEvents(id);
		model.addAttribute("resultKey", events);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/consolidateVms"}, method = {RequestMethod.POST})
	public String consolidateVms(@RequestBody List<String> hosts, Model model) {
		log.info("... consolidateVms");
		List<ConsolidationVo> result = this.hostsService.maintenanceBeforeConsolidateVms(hosts);
		model.addAttribute("resultKey", result);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/maintenanceStart"}, method = {RequestMethod.POST})
	public String maintenanceStart(@RequestBody List<String> hosts, Model model) {
		log.info("... maintenanceStart");
		this.hostsService.maintenanceStart(hosts);
		try { Thread.sleep(500L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/maintenanceStop"}, method = {RequestMethod.POST})
	public String maintenanceStop(@RequestBody List<String> hosts, Model model) {
		log.info("... maintenanceStop");
		this.hostsService.maintenanceStop(hosts);
		try { Thread.sleep(500L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/restartHost"}, method = {RequestMethod.POST})
	public String restartHost(@RequestBody List<String> hosts, Model model) {
		log.info("... restartHost");
		this.hostsService.restartHost(hosts);
		try { Thread.sleep(500L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/startHost"}, method = {RequestMethod.POST})
	public String startHost(@RequestBody List<String> hosts, Model model) {
		log.info("... startHost");
		this.hostsService.startHost(hosts);
		try { Thread.sleep(500L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/stopHost"}, method = {RequestMethod.POST})
	public String stopHost(@RequestBody List<String> hosts, Model model) {
		log.info("... stopHost");
		this.hostsService.stopHost(hosts);
		try { Thread.sleep(500L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/createHost"}, method = {RequestMethod.POST})
	public String createHost(@RequestBody HostCreateVo hostCreateVo, Model model) throws Exception {
		log.info("... createHost");
		this.hostsService.createHost(hostCreateVo);
		try { Thread.sleep(500L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/updateHost"}, method = {RequestMethod.POST})
	public String updateHost(@RequestBody HostCreateVo hostCreateVo, Model model) throws Exception {
		log.info("... updateHost");
		this.hostsService.updateHost(hostCreateVo);
		try { Thread.sleep(500L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/removeHost"}, method = {RequestMethod.POST})
	public String removeHost(@RequestBody List<String> hosts, Model model) throws Exception {
		log.info("... removeHost");
		this.hostsService.removeHost(hosts);
		try { Thread.sleep(500L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/retrieveClusters"}, method = {RequestMethod.GET})
	public String retrieveClusters(Model model) {
		log.info("... retrieveClusters");
		List<ClusterVo> clusters = this.clustersService.retrieveClusters();
		model.addAttribute("resultKey", clusters);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/retrieveFanceAgentType"}, method = {RequestMethod.GET})
	public String retrieveFanceAgentType(Model model) {
		log.info("... retrieveClusters");
		List<String> fenceAgentTypes = this.hostsService.retrieveFanceAgentType();
		model.addAttribute("resultKey", fenceAgentTypes);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/connectTestFenceAgent"}, method = {RequestMethod.POST})
	public String connectTestFenceAgent(@RequestBody FenceAgentVo fenceAgentVo, Model model) {
		log.info("... connectTestFenceAgent");
		this.hostsService.connectTestFenceAgent(fenceAgentVo);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/shutdownHost"}, method = {RequestMethod.GET})
	public String shutdownHost(@RequestParam("id") String id, Model model) throws Exception {
		log.info("... shutdownHost");
		HostVo host = new HostVo();
		host.setId(id);
		List<HostVo> hosts = new ArrayList<>();
		hosts.add(host);
		this.hostsService.shutdownHost(hosts);
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/setupHostNetwork"}, method = {RequestMethod.POST})
	public String setupHostNetwork(@RequestBody List<NicUsageApiVo> nicUsageApiVoList, Model model) {
		log.info("... setupHostNetwork");
		this.hostsService.setupHostNetwork(nicUsageApiVoList);
		model.addAttribute("resultKey");
		return "jsonView";
	}

	@RequestMapping(value = {"/compute/hosts/modifyNicNetwork"}, method = {RequestMethod.POST})
	public String modifyNicNetwork(@RequestBody NetworkAttachmentVo networkAttachmentVo, Model model) {
		log.info("... modifyNicNetwork");
		this.hostsService.modifyNicNetwork(networkAttachmentVo);
		model.addAttribute("resultKey");
		return "jsonView";
	}
}

