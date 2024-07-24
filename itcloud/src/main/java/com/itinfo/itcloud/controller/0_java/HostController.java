/*
@Controller
@Api(tags = "Computing-Host")
@RequestMapping("/computing/hosts")
public class HostController {
	private final ItHostService host;

	@GetMapping
	@ApiOperation(value = "호스트 목록", notes = "전체 호스트 목록을 조회한다")
	@ResponseBody
	public List<HostVo> hosts() {
		log.info("--- 호스트 목록");
		return host.findAll();
	}

//	@GetMapping("/settings")
//	@ApiOperation(value = "호스트 생성창", notes = "호스트 생성시 필요한 내용을 조회한다")
//	@ResponseBody
//	public List<ClusterVo> setClusterList(){
//		log.info("--- 호스트 생성 창");
//		return hostService.setClusterList();
//	}

	@PostMapping
	@ApiOperation(value = "호스트 생성", notes = "호스트를 생성한다")
	@ApiImplicitParam(name = "hVo", value = "호스트", dataTypeClass = HostCreateVo.class)
	@ResponseBody
	public Res<Boolean> addHost(@RequestBody HostCreateVo hVo){
		log.info("--- 호스트 생성");
		return host.add(hVo);
	}

	@GetMapping("/{id}/edit")
	@ApiOperation(value = "호스트 수정창", notes = "선택된 호스트의 정보를 조회한다")
	@ApiImplicitParam(name = "id", value = "호스트 아이디", dataTypeClass = String.class)
	@ResponseBody
	public HostCreateVo getHostCreate(@PathVariable String id){
		log.info("--- 호스트 수정창");
		return host.setHost(id);
	}

	@PutMapping("/{id}")
	@ApiOperation(value = "호스트 수정", notes = "호스트를 수정한다")
	@ApiImplicitParam(name = "id", value = "호스트 아이디", dataTypeClass = String.class)
	@ResponseBody
	public Res<Boolean> editHost(@PathVariable String id,
								 @RequestBody HostCreateVo hVo){
		log.info("--- 호스트 수정");
		return host.editHost(hVo);
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "호스트 삭제", notes = "호스트를 삭제한다")
	@ApiImplicitParam(name = "ids", value = "호스트 아이디", dataTypeClass = List.class)
	@ResponseBody
	public Res<Boolean> deleteHost(@RequestBody String id?) {
		log.info("--- 호스트 삭제");
		if (id.isNullOrEmpty()) {
			log.warn("deleteHost ... 삭제보류 아이디 없음")
			return Res.successResponse()
		}
		return host.remove(ids);
	}
	@GetMapping("/{id}")
	@ApiOperation(value = "호스트 상세정보", notes = "호스트의 상세정보를 조회한다")
	@ApiImplicitParam(name = "id", value = "호스트 아이디", dataTypeClass = String.class)
	@ResponseBody
	public HostVo getHost(@PathVariable String id) {
		log.info("--- 호스트 일반");
		return host.getHost(id);
	}



	@GetMapping("/{id}/vms")
	@ApiOperation(value = "호스트 가상머신 목록", notes = "선택된 호스트의 가상머신 목록을 조회한다")
	@ApiImplicitParam(name = "id", value = "호스트 아이디", dataTypeClass = String.class)
	@ResponseBody
	public List<VmVo> getvm(@PathVariable String id) {
		log.info("--- 호스트 vm");
		return host.getVmsByHost(id);
	}

//	@PostMapping("/{id}/vms/{vmId}/start")
//	@ResponseBody
//	public CommonVo<Boolean> startHostVm(@PathVariable String id, @PathVariable String vmId){
//		log.info("--- 호스트 가상머신 실행");
//		return vmService.startVm(vmId);
//	}
//
//	@PostMapping("/{id}/vms/{vmId}/pause")
//	@ResponseBody
//	public CommonVo<Boolean> pauseHostVm(@PathVariable String id, @PathVariable String vmId){
//		log.info("--- 호스트 가상머신 일시정지");
//		return vmService.pauseVm(vmId);
//	}

	@GetMapping("/{id}/nics")
	@ApiOperation(value = "호스트 네트워크 인터페이스 목록", notes = "선택된 호스트의 네트워크 인터페이스 목록을 조회한다")
	@ApiImplicitParam(name = "id", value = "호스트 아이디", dataTypeClass = String.class)
	@ResponseBody
	public List<NicVo> nic(@PathVariable String id) {
		log.info("--- 호스트 nic");
		return host.getNicsByHost(id);
	}


	@GetMapping("/{id}/devices")
	@ApiOperation(value = "호스트 장치 목록", notes = "선택된 호스트의 장치 목록을 조회한다")
	@ApiImplicitParam(name = "id", value = "호스트 아이디", dataTypeClass = String.class)
	@ResponseBody
	public List<HostDeviceVo> device(@PathVariable String id) {
		log.info("--- 호스트 장치");
		return host.getHostDevicesByHost(id);
	}


	@GetMapping("/{id}/permissions")
	@ApiOperation(value = "호스트 권한 목록", notes = "선택된 호스트의 권한 목록을 조회한다")
	@ApiImplicitParam(name = "id", value = "호스트 아이디", dataTypeClass = String.class)
	@ResponseBody
	public List<PermissionVo> permission(@PathVariable String id) {
		log.info("--- 호스트 권한");
		return host.getPermissionsByHost(id);
	}


//	@GetMapping("/{id}/affinitylabels")
//	@ResponseBody
//	public List<AffinityLabelVo> getAffinitylabels(@PathVariable String id) {
//		log.info("--- 호스트 선호도 레이블");
//		return hostService.getAffinitylabels(id);
//	}
//
//
//
//	@GetMapping("/{id}/affinitylabel/settings")
//	@ResponseBody
//	public AffinityHostVm setAffinitygroup(@PathVariable String id){
//		log.info("--- 호스트 선호도 레이블 생성 창");
//		return hostService.setAffinityDefaultInfo(id, "label");
//	}
//
//
//
//	@PostMapping("/{id}/affinitylabel")
//	@ResponseBody
//	public CommonVo<Boolean> addAff(@PathVariable String id,
//									@RequestBody AffinityLabelCreateVo alVo) {
//		log.info("--- 호스트 선호도 레이블 생성");
//		return hostService.addAffinitylabel(id, alVo);
//	}
//
//	@GetMapping("/{id}/affinitylabel/{alId}/settings")
//	@ResponseBody
//	public AffinityHostVm setAffinityDefaultInfo(@PathVariable String id,
//												 @PathVariable String type){
//		log.info("--- 호스트 선호도 레이블 수정 창");
//		return hostService.setAffinityDefaultInfo(id, "label");
//	}
//
//
//	@PutMapping("/{id}/affinitylabel/{alId}")
//	@ResponseBody
//	public CommonVo<Boolean> editAff(@PathVariable String id,
//									 @PathVariable String alId,
//									 @RequestBody AffinityLabelCreateVo alVo) {
//		log.info("--- 호스트 선호도 레이블 편집");
//		return hostService.editAffinitylabel(id, alId, alVo);
//	}
//
//	@DeleteMapping("/{id}/affinitylabel/{alId}")
//	@ResponseBody
//	public CommonVo<Boolean> deleteAff(@PathVariable String id,
//									   @PathVariable String alId) {
//		log.info("--- 호스트 선호도 레이블 삭제");
//		return hostService.deleteAffinitylabel(id, alId);
//	}


	@GetMapping("/{id}/events")
	@ApiOperation(value = "호스트 이벤트 목록", notes = "선택된 호스트의 이벤트 목록을 조회한다")
	@ApiImplicitParam(name = "id", value = "호스트 아이디", dataTypeClass = String.class)
	@ResponseBody
	public List<EventVo> event(@PathVariable String id) {
		log.info("--- 호스트 이벤트");
		return host.getEventsByHost(id);
	}


	private final ItHostOperationService hostOperation;

	@PostMapping("/{id}/deactivate")
	@ApiOperation(value = "호스트 유지보수", notes = "호스트를 유지보수 모드로 전환한다")
	@ApiImplicitParam(name = "id", value = "호스트 아이디", dataTypeClass = String.class)
	@ResponseBody
	public void deactiveHost(@PathVariable String id) {
		log.info("--- 호스트 유지보수");
		hostOperation.deactivate(id);
	}

	@PostMapping("/{id}/activate")
	@ApiOperation(value = "호스트 활성화", notes = "호스트를 활성화 모드로 전환한다")
	@ApiImplicitParam(name = "id", value = "호스트 아이디", dataTypeClass = String.class)
	@ResponseBody
	public void activeHost(@PathVariable String id) {
		log.info("--- 호스트 활성");
		hostOperation.activate(id);
	}

	@PostMapping("/{id}/refresh")
	@ApiOperation(value = "호스트 새로고침", notes = "호스트를 새로고침한다")
	@ApiImplicitParam(name = "id", value = "호스트 아이디", dataTypeClass = String.class)
	@ResponseBody
	public void refreshHost(@PathVariable String id) {
		log.info("--- 호스트 새로고침");
		hostOperation.refreshHost(id);
	}
	
	@PostMapping("/{id}/restart")
	@ApiOperation(value = "호스트 재시작", notes = "호스트를 재시작한다")
	@ApiImplicitParam(name = "id", value = "호스트 아이디", dataTypeClass = String.class)
	@ResponseBody
	public void reStartHost(@PathVariable String id) throws UnknownHostException {
		log.info("--- 호스트 ssh 재시작");
		hostOperation.reStartHost(id);
	}
}
*/