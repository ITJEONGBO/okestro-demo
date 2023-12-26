package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.service.ItNetworkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class NetworkController {

	private final ItNetworkService itNetworkService;

/*
	@GetMapping("/networks")
	public String network(Model model){
		List<NetworkVO> networkVOList = this.networkService.getNetworkList();
		model.addAttribute("networkList", networkVOList);
		return "/network/networks";
	}

	@GetMapping("/networkStatus")
	@ResponseBody
	public List<NetworkVO> net(){
		List<NetworkVO> list = null;
		try{
			list = networkService.getNetworkList();
		}catch (Exception e){
			e.printStackTrace();
		}
		return list;
	}
*/
}
