package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.model.*;
import com.itinfo.service.DomainsService;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.ovirt.engine.sdk4.types.StorageDomainType;
import org.ovirt.engine.sdk4.types.StorageType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
public class DomainsController {
	@Autowired private DomainsService domainsService;

	@RequestMapping({"/storage/domains"})
	public String domainsView() {
		log.info("... domainsView");
		return "/castanets/storage/domains";
	}

	@RequestMapping({"/storage/createDomain"})
	public String createDomainView() {
		log.info("... createDomainView");
		return "/castanets/storage/createDomain";
	}

	@RequestMapping(value = {"/storage/importDomain"}, method = {RequestMethod.GET})
	public String importDomain(boolean isImport, Model model) {
		log.info("... importDomain('{}')", isImport);
		model.addAttribute(ItInfoConstant.RESULT_KEY, isImport);
		return "/castanets/storage/createDomain";
	}

	@RequestMapping(value = {"/storage/updateDomain"}, method = {RequestMethod.GET})
	public String updateDomain(String id, Model model) {
		log.info("... updateDomain('{}')", id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, id);
		return "/castanets/storage/createDomain";
	}

	@RequestMapping(value = {"/storage/domain"}, method = {RequestMethod.GET})
	public String domainView(String id, Model model) {
		log.info("... domainView('{}')", id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, id);
		return "/castanets/storage/domain";
	}

	@RequestMapping({"/storage/domains/retrieveDomains"})
	public String retrieveDomains(String status, String domainType, Model model) {
		log.info("... domainView('{}', '{}')", status, domainType);
		List<StorageDomainVo> storageDomains = domainsService.retrieveStorageDomains(status, domainType);
		model.addAttribute(ItInfoConstant.RESULT_KEY, storageDomains);
		return "jsonView";
	}

	@RequestMapping(value = {"/storage/domains/retrieveCreateDomainInfo"}, method = {RequestMethod.GET})
	public String retrieveCreateDomainInfo(String id, Model model) {
		log.info("... retrieveCreateDomainInfo('{}')", id);
		StorageDomainCreateVo storageDomainCreateVo = domainsService.retrieveCreateDomainInfo(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, storageDomainCreateVo);
		return "jsonView";
	}

	@RequestMapping(value = {"/storage/domains/retrieveDomain"}, method = {RequestMethod.GET})
	public String retrieveDomain(String id, Model model) {
		log.info("... retrieveDomain('{}')", id);
		StorageDomainVo storageDomain = domainsService.retrieveStorageDomain(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, storageDomain);
		return "jsonView";
	}

	@RequestMapping(value = {"/storage/domains/retrieveDomainUsage"}, method = {RequestMethod.GET})
	public String retrieveDomainUsage(String id, Model model) {
		log.info("... retrieveDomainUsage("+id+")");
		return "jsonView";
	}

	@RequestMapping(value = {"/storage/domains/retrieveDomainEvents"}, method = {RequestMethod.GET})
	public String retrieveDomainEvents(String id, Model model) {
		log.info("... retrieveDomainEvents("+id+")");
		List<EventVo> events = domainsService.retrieveDomainEvents(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, events);
		return "jsonView";
	}

	@RequestMapping(value = {"/storage/domains/createDomain"}, method = {RequestMethod.POST})
	public String createDomain(@RequestBody StorageDomainCreateVo storageDomainCreateVo, Model model) {
		log.info("... createDomain");
		domainsService.createDomain(storageDomainCreateVo);
		try { Thread.sleep(3000L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/storage/domains/updateDomain"}, method = {RequestMethod.POST})
	public String updateDomain(@RequestBody StorageDomainCreateVo storageDomainCreateVo, Model model) {
		log.info("... updateDomain");
		domainsService.updateDomain(storageDomainCreateVo);
		try { Thread.sleep(3000L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/storage/domains/removeDomain"}, method = {RequestMethod.POST})
	public String removeDomain(@RequestBody StorageDomainVo storageDomainVo, Model model) {
		log.info("... removeDomain");
		domainsService.removeDomain(storageDomainVo);
		try { Thread.sleep(3000L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/storage/domains/maintenanceStart"}, method = {RequestMethod.POST})
	public String maintenanceStart(@RequestBody List<String> domains, Model model) {
		log.info("... maintenanceStart");
		domainsService.maintenanceStart(domains);
		try { Thread.sleep(500L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/storage/domains/maintenanceStop"}, method = {RequestMethod.POST})
	public String maintenanceStop(@RequestBody List<String> domains, Model model) {
		log.info("... maintenanceStop");
		domainsService.maintenanceStop(domains);
		try { Thread.sleep(500L); } catch (Exception e) { log.error(e.getLocalizedMessage()); }
		return "jsonView";
	}

	@RequestMapping(value = {"/storage/domains/iscsiDiscover"}, method = {RequestMethod.POST})
	public String iscsiDiscover(@RequestBody StorageDomainCreateVo storageDomainCreateVo, Model model) {
		log.info("... iscsiDiscover");
		List<IscsiVo> iscsis = domainsService.iscsiDiscover(storageDomainCreateVo);
		model.addAttribute(ItInfoConstant.RESULT_KEY, iscsis);
		return "jsonView";
	}

	@RequestMapping(value = {"/storage/domains/iscsiLogin"}, method = {RequestMethod.POST})
	public String iscsiLogin(@RequestBody StorageDomainCreateVo storageDomainCreateVo, Model model) {
		log.info("... iscsiLogin");
		boolean isSuccess = domainsService.iscsiLogin(storageDomainCreateVo);
		model.addAttribute(ItInfoConstant.RESULT_KEY, isSuccess);
		return "jsonView";
	}

	@RequestMapping(value = {"/storage/domains/retrieveHosts"}, method = {RequestMethod.GET})
	public String retrieveHosts(Model model) {
		log.info("... retrieveHosts");
		List<HostDetailVo> hosts = domainsService.retrieveHosts();
		model.addAttribute(ItInfoConstant.RESULT_KEY, hosts);
		return "jsonView";
	}

	@RequestMapping(value = {"/storage/domains/retrieveDomainMeta"}, method = {RequestMethod.GET})
	public String retrieveDomainMeta(Model model) {
		log.info("... retrieveDomainMeta");
		boolean isExistIso = false;
		List<StorageDomainVo> domains
				= domainsService.retrieveStorageDomains("all", "all");
		for (StorageDomainVo domain : domains) {
			if (StorageDomainType.ISO.value().equalsIgnoreCase(domain.getType()))
				isExistIso = true;
		}
		model.addAttribute("domainTypeList", StorageDomainType.values());
		model.addAttribute("storageTypeList", StorageType.values());
		model.addAttribute("isExistIso", isExistIso);
		return "jsonView";
	}
}

