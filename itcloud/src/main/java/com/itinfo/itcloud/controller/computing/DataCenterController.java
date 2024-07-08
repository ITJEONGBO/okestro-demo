package com.itinfo.itcloud.controller.computing;

import com.itinfo.itcloud.model.computing.DataCenterVo;
import com.itinfo.itcloud.model.computing.EventVo;
import com.itinfo.itcloud.model.create.DataCenterCreateVo;
import com.itinfo.itcloud.model.error.CommonVo;
import com.itinfo.itcloud.service.computing.ItDataCenterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
@Api(tags = "Computing-Datacenter")
@RequestMapping("/computing/datacenters")
public class DataCenterController {
	private final ItDataCenterService dcService;


	@GetMapping
	@ResponseBody
	@ApiOperation(value = "데이터센터 목록", notes = "전체 데이터센터 목록을 조회한다")
	@ResponseStatus(HttpStatus.OK)
	public List<DataCenterVo> datacenters() {
		log.info("----- 데이터센터 목록");
		return dcService.getList();
	}

	@PostMapping
	@ResponseBody
	@ApiOperation(value = "데이터센터 생성", notes = "데이터센터를 생성한다")
	@ApiImplicitParam(name = "dcVo", value = "데이터센터")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonVo<Boolean> addDatacenter(@RequestBody DataCenterCreateVo dcVo){
		log.info("----- 데이터센터 추가 : " + dcVo.getName());
		return dcService.addDatacenter(dcVo);
	}

	// 데이터센터 수정 창
	@GetMapping("/{id}/edit")
	@ResponseBody()
	@ApiOperation(value = "데이터센터 수정창", notes = "수정하려는 데이터센터의 정보를 불러온다")
	@ApiImplicitParam(name = "id", value = "데이터센터 아이디")
	@ResponseStatus(HttpStatus.OK)
	public DataCenterCreateVo getDatacenter(@PathVariable String id){
		log.info("-- 데이터 센터 편집 창");
		return dcService.setDatacenter(id);
	}

	// 데이터센터 수정
	@PutMapping("/{id}")
	@ResponseBody
	@ApiOperation(value = "데이터센터 수정", notes = "데이터센터를 수정한다")
	@ApiImplicitParam(name = "id", value = "데이터센터 아이디")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonVo<Boolean> editDatacenter(@PathVariable String id,
											@RequestBody DataCenterCreateVo dcVo){
		log.info("----- 데이터센터 편집 : " + dcVo.getName());
		return dcService.editDatacenter(dcVo);
	}

	// 데이터센터 삭제
	@DeleteMapping("/{id}")
	@ResponseBody
	@ApiOperation(value = "데이터센터 삭제", notes = "선택된 데이터센터 목록을 삭제한다(중복삭제 구현해야함)")
	@ApiImplicitParam(name = "id", value = "데이터센터 아이디")
	@ResponseStatus(HttpStatus.OK)
	public CommonVo<Boolean> deleteDatacenter(@PathVariable String id){
		log.info("----- 데이터센터 삭제");
		return dcService.deleteDatacenter(id);
	}


	// 데이터센터 이벤트
	@GetMapping("/{id}/events")
	@ResponseBody
	@ApiOperation(value = "데이터센터 이벤트 목록", notes = "선택된 데이터센터 이벤트를 조회한다")
	@ApiImplicitParam(name = "id", value = "데이터센터 아이디")
	@ResponseStatus(HttpStatus.OK)
	public List<EventVo> event(@PathVariable String id) {
		log.info("----- 데이터센터 이벤트 : " + id);
		return dcService.getEvent(id);
	}


}
