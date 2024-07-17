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
	public List<DataCenterVo> dcList() {
		log.info("----- 데이터센터 목록");
		return dcService.getDataCenters();
	}

	@PostMapping
	@ResponseBody
	@ApiOperation(value = "데이터센터 생성", notes = "데이터센터를 생성한다")
	@ApiImplicitParam(name = "dcVo", value = "데이터센터", dataTypeClass = DataCenterCreateVo.class)
	public CommonVo<Boolean> addDc(@RequestBody DataCenterCreateVo dcVo){
		log.info("----- 데이터센터 추가 : " + dcVo.getName());
		return dcService.addDatacenter(dcVo);
	}

	// 데이터센터 수정 창
	@GetMapping("/{id}/edit")
	@ResponseBody
	@ApiOperation(value = "데이터센터 수정창", notes = "수정하려는 데이터센터의 정보를 불러온다")
	@ApiImplicitParam(name = "id", value = "데이터센터 아이디", dataTypeClass = String.class)
	public DataCenterCreateVo setDc(@PathVariable String id){
		log.info("-- 데이터 센터 편집 창");
		return dcService.setDatacenter(id);
	}

	// 데이터센터 수정
	@PutMapping("/{id}")
	@ResponseBody
	@ApiOperation(value = "데이터센터 수정", notes = "데이터센터를 수정한다")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "데이터센터 아이디", dataTypeClass = String.class),
			@ApiImplicitParam(name = "dcVo", value = "데이터센터", dataTypeClass = DataCenterCreateVo.class)
	})
	public CommonVo<Boolean> editDc(@PathVariable String id,
									@RequestBody DataCenterCreateVo dcVo){
		log.info("----- 데이터센터 편집 : " + dcVo.getName());
		return dcService.editDatacenter(dcVo);
	}

	// 데이터센터 삭제
	@DeleteMapping("/{id}")
	@ResponseBody
	@ApiOperation(value = "데이터센터 삭제", notes = "선택된 데이터센터 목록을 삭제한다(중복삭제 구현해야함)")
	@ApiImplicitParam(name = "ids", value = "데이터센터 아이디 목록", dataTypeClass = List.class)
	public CommonVo<Boolean> deleteDc(@RequestBody List<String> ids){
		log.info("----- 데이터센터 삭제");
		return dcService.deleteDatacenter(ids);
	}


	// 데이터센터 이벤트
	@GetMapping("/{id}/events")
	@ResponseBody
	@ApiOperation(value = "데이터센터 이벤트 목록", notes = "선택된 데이터센터 이벤트를 조회한다")
	@ApiImplicitParam(name = "id", value = "데이터센터 아이디", dataTypeClass = String.class)
	public List<EventVo> dcEventList(@PathVariable String id) {
		log.info("----- 데이터센터 이벤트 : " + id);
		return dcService.getEventsByDatacenter(id);
	}


}
