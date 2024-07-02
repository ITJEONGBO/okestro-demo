package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.create.DataCenterCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.service.computing.ItDataCenterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@Api(tags = "datacenter")
@RequestMapping("/computing")
public class DataCenterController {
	private final ItDataCenterService dcService;


	@GetMapping("/datacenters")
	@ResponseBody
	@ApiOperation(value = "데이터센터 목록", notes = "@RequestParam을 활용한 GET Method")
	@ResponseStatus(HttpStatus.OK)
	public List<DataCenterVo> datacenters() {
		log.info("----- 데이터센터 목록");
		return dcService.getList();
	}


	@PostMapping("/datacenter")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public CommonVo<Boolean> addDatacenter(@RequestBody DataCenterCreateVo dcVo){
		log.info("----- 데이터센터 추가 : " + dcVo.getName());
		return dcService.addDatacenter(dcVo);
	}

	// 데이터센터 수정 창
	@GetMapping("/datacenter/{id}/settings")
	@ResponseBody()
	@ResponseStatus(HttpStatus.OK)
	public DataCenterCreateVo getDatacenter(@PathVariable String id){
		log.info("-- 데이터 센터 편집 창");
		return dcService.setDatacenter(id);
	}

	// 데이터센터 수정
	@PutMapping("/datacenter/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public CommonVo<Boolean> editDatacenter(@PathVariable String id,
											@RequestBody DataCenterCreateVo dcVo){
		log.info("----- 데이터센터 편집 : " + dcVo.getName());
		return dcService.editDatacenter(dcVo);
	}

	// 데이터센터 삭제
	@DeleteMapping("/datacenter/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonVo<Boolean> deleteDatacenter(@PathVariable String id){
		log.info("----- 데이터센터 삭제");
		return dcService.deleteDatacenter(id);
	}


	// 데이터센터 이벤트
	@GetMapping("/datacenter/{id}/events")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<EventVo> event(@PathVariable String id) {
		log.info("----- 데이터센터 이벤트 : " + id);
		return dcService.getEvent(id);
	}


}
