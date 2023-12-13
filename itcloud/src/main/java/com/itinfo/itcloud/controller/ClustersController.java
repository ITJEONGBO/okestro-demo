package com.itinfo.itcloud.controller;

import com.itinfo.itcloud.service.ItClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ClustersController {

    @Autowired
    private ItClusterService itClusterService;

    public ClustersController(){}

    /*@GetMapping("/computing/clusters")
    public String cluster(Model model){
        List<ClusterVO> clusterVOList = this.clustersService.retrieveClusters();
        model.addAttribute("clusterList", clusterVOList);
        return "compute/clusters";
    }

    @GetMapping("/clusterStatus")
    @ResponseBody
    public List<ClusterVO> ho(){
        List<ClusterVO> list = null;
        try{
            list = clustersService.retrieveClusters();
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }*/



}
