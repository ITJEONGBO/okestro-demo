package com.itinfo.itcloud.network;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class NetworkController {

    @Autowired
    private NetworkService networkService;

    public NetworkController(){}

    /*@GetMapping("/networks")
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
    }*/




}
