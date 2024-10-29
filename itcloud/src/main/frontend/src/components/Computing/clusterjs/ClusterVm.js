import {useHostFromCluster, useVMFromCluster} from "../../../api/RQHook";
import HostDu from "../../duplication/HostDu";
import React, { useState } from 'react';
import TableColumnsInfo from "../../table/TableColumnsInfo";
import TableOuter from "../../table/TableOuter";
import { useNavigate } from 'react-router-dom';
import VmDu from "../../duplication/VmDu";


const ClusterVm = ({ cluster }) => {


    const { 
        data: vms, 
        status: vmsStatus, 
        isLoading: isVmsLoading, 
        isError: isVmsError 
      } = useVMFromCluster(cluster?.id, toTableItemPredicateVms);
      function toTableItemPredicateVms(vm) {
        const statusIcon = vm?.status === 'DOWN' 
            ? <i class="fa-solid fa-chevron-down text-red-500" fixedWidth/>
            : vm?.status === 'UP' || vm?.status === '실행 중'
            ? <i class="fa-solid fa-chevron-up text-green-500" fixedWidth/>
            : ''; // 기본값
        return {
          icon: statusIcon,      
          name: vm?.name ?? 'Unknown',               
          status: vm?.status ?? 'Unknown',           
          upTime: vm?.upTime ?? '',             
          cpu: vm?.cpu ?? '',                    
          memory: vm?.memory ?? '',              
          network: vm?.network ?? '',             
          ipv4: vm?.ipv4 ?? '',         
        };
      }

    return (
        <VmDu 
        data={vms} 
        columns={TableColumnsInfo.VM_CHART} 

        />
    );
  };
  
  export default ClusterVm;