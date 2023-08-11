package com.itinfo.controller;

import com.itinfo.ItInfoConstant;
import com.itinfo.model.*;
import com.itinfo.service.DomainsService;

import java.util.List;

import io.swagger.annotations.*;
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
@Api(value = "DomainsController", tags = {"domains"})
public class DomainsController extends BaseController {
	@Autowired private DomainsService domainsService;

	@ApiOperation(httpMethod = "GET", value = "domainsView", notes = "페이지 이동 > /storage/domains")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/storage/domains"})
	public String domainsView() {
		log.info("... domainsView");
		return "/castanets/storage/domains";
	}

	@ApiOperation(httpMethod = "GET", value = "createDomainView", notes = "페이지 이동 > /storage/createDomain")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/storage/createDomain"})
	public String createDomainView() {
		log.info("... createDomainView");
		return "/castanets/storage/createDomain";
	}

	@ApiOperation(httpMethod = "GET", value = "createDomainView", notes = "페이지 이동 > /storage/importDomain")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "isImport", value = "가져오기 여부")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/storage/importDomain"})
	public String importDomain(boolean isImport, 
							   Model model) {
		log.info("... importDomain('{}')", isImport);
		model.addAttribute(ItInfoConstant.RESULT_KEY, isImport);
		return "/castanets/storage/createDomain";
	}

	@ApiOperation(httpMethod = "GET", value = "updateDomainView", notes = "페이지 이동 > /storage/updateDomain")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "도메인 ID")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/storage/updateDomain"})
	public String updateDomainView(String id,
								   Model model) {
		log.info("... updateDomain('{}')", id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, id);
		return "/castanets/storage/createDomain";
	}

	@ApiOperation(httpMethod = "GET", value = "domainView", notes = "페이지 이동 > /storage/domain")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "도메인 ID")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/storage/domain"})
	public String domainView(String id, 
							 Model model) {
		log.info("... domainView('{}')", id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, id);
		return "/castanets/storage/domain";
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveDomains", notes = "도메인 목록 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "status", value = "도메인 상태"),
			@ApiImplicitParam(name = "domainType", value = "도메인 유형")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/storage/domains/retrieveDomains"})
	public String retrieveDomains(String status, 
								  String domainType, 
								  Model model) {
		log.info("... domainView('{}', '{}')", status, domainType);
		List<StorageDomainVo> storageDomains 
				= domainsService.retrieveStorageDomains(status, domainType);
		model.addAttribute(ItInfoConstant.RESULT_KEY, storageDomains);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveCreateDomainInfo", notes = "도메인 생성 정보 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "도메인 ID")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/storage/domains/retrieveCreateDomainInfo"})
	public String retrieveCreateDomainInfo(String id, 
										   Model model) {
		log.info("... retrieveCreateDomainInfo('{}')", id);
		StorageDomainCreateVo storageDomainCreateVo 
				= domainsService.retrieveCreateDomainInfo(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, storageDomainCreateVo);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveDomain", notes = "도메인 상세 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "도메인 ID")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/storage/domains/retrieveDomain"})
	public String retrieveDomain(String id, 
								 Model model) {
		log.info("... retrieveDomain('{}')", id);
		StorageDomainVo storageDomain = domainsService.retrieveStorageDomain(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, storageDomain);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveDomain", notes = "도메인 상세 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "도메인 ID")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/storage/domains/retrieveDomainUsage"})
	public String retrieveDomainUsage(String id, Model model) {
		log.info("... retrieveDomainUsage('{}')", id);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveDomainEvents", notes = "도메인 이벤트 목록 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "도메인 ID")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/storage/domains/retrieveDomainEvents"})
	public String retrieveDomainEvents(String id, 
									   Model model) {
		log.info("... retrieveDomainEvents('{}')", id);
		List<EventVo> events = domainsService.retrieveDomainEvents(id);
		model.addAttribute(ItInfoConstant.RESULT_KEY, events);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "retrieveDomainEvents", notes = "도메인 생성")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "storageDomainCreateVo", value = "생성할 도메인 정보")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/storage/domains/createDomain"})
	public String createDomain(@RequestBody StorageDomainCreateVo storageDomainCreateVo, 
							   Model model) {
		log.info("... createDomain");
		domainsService.createDomain(storageDomainCreateVo);
		doLongSleep();
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "updateDomain", notes = "도메인 수정")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "storageDomainCreateVo", value = "수정할 도메인 정보")
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/storage/domains/updateDomain"})
	public String updateDomain(@RequestBody StorageDomainCreateVo storageDomainCreateVo, 
							   Model model) {
		log.info("... updateDomain");
		domainsService.updateDomain(storageDomainCreateVo);
		doLongSleep();
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "removeDomain", notes = "도메인 제거")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "storageDomainVo", value = "제거할 도메인 정보", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/storage/domains/removeDomain"})
	public String removeDomain(@RequestBody StorageDomainVo storageDomainVo, 
							   Model model) {
		log.info("... removeDomain");
		domainsService.removeDomain(storageDomainVo);
		doLongSleep();
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "maintenanceStart", notes = "유지보수 모드 시작")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "domains", value = "도메인", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/storage/domains/maintenanceStart"})
	public String maintenanceStart(@RequestBody List<String> domains, 
								   Model model) {
		log.info("... maintenanceStart");
		domainsService.maintenanceStart(domains);
		doSleep();
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "maintenanceStop", notes = "활성화 모드 시작")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "domains", value = "도메인", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/storage/domains/maintenanceStop"})
	public String maintenanceStop(@RequestBody List<String> domains,
								  Model model) {
		log.info("... maintenanceStop");
		domainsService.maintenanceStop(domains);
		doSleep();
		model.addAttribute(ItInfoConstant.RESULT_KEY, "ok");
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "iscsiDiscover", notes = "ISCSI 찾기")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "storageDomainCreateVo", value = "생성할 도메인 정보", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/storage/domains/iscsiDiscover"})
	public String iscsiDiscover(@RequestBody StorageDomainCreateVo storageDomainCreateVo,
								Model model) {
		log.info("... iscsiDiscover");
		List<IscsiVo> iscsis
				= domainsService.iscsiDiscover(storageDomainCreateVo);
		model.addAttribute(ItInfoConstant.RESULT_KEY, iscsis);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "POST", value = "iscsiLogin", notes = "ISCSI 로그인")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "storageDomainCreateVo", value = "생성할 도메인 정보", required = true)
	})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.POST}, value = {"/storage/domains/iscsiLogin"})
	public String iscsiLogin(@RequestBody StorageDomainCreateVo storageDomainCreateVo,
							 Model model) {
		log.info("... iscsiLogin");
		boolean isSuccess
				= domainsService.iscsiLogin(storageDomainCreateVo);
		model.addAttribute(ItInfoConstant.RESULT_KEY, isSuccess);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveHosts", notes = "호스트 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/storage/domains/retrieveHosts"})
	public String retrieveHosts(Model model) {
		log.info("... retrieveHosts");
		List<HostDetailVo> hosts = domainsService.retrieveHosts();
		model.addAttribute(ItInfoConstant.RESULT_KEY, hosts);
		return ItInfoConstant.JSON_VIEW;
	}

	@ApiOperation(httpMethod = "GET", value = "retrieveDomainMeta", notes = "도메인 메타정보 조회")
	@ApiImplicitParams({})
	@ApiResponses({@ApiResponse(code = 200, message = "OK")})
	@RequestMapping(method = {RequestMethod.GET}, value = {"/storage/domains/retrieveDomainMeta"})
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
		return ItInfoConstant.JSON_VIEW;
	}
}

