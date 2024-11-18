import {useHostFromCluster, useVMFromCluster, useVmFromHost} from "../../../api/RQHook";
import HostDu from "../../duplication/HostDu";
import React, { useState } from 'react';
import VmDu from "../../duplication/VmDu";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUniversity } from "@fortawesome/free-solid-svg-icons";
import TableInfo from "../../table/TableInfo";


const HostVm = ({ host }) => {


   // 가상머신 
   const { 
    data: vms, 
    status: vmsStatus, 
    isLoading: isHostsLoading, 
    isError: isHostsError 
  } = useVmFromHost(host?.id, toTableItemPredicateHosts);
  function toTableItemPredicateHosts(host) {
    return {
      icon: <FontAwesomeIcon icon={faUniversity} fixedWidth />,
      status: host?.status,
      name: host?.name ?? 'Unknown', 
      cluster: host?.clusterVo?.name ?? 'Default', 
      ipv4: host?.ipv4 ?? 'Unknown',
      fqdn: host?.fqdn ?? 'Unknown', 
      memory: host?.memoryUsage ? `${host.memoryUsage}%` : 'Unknown', 
      cpu: host?.cpuUsage ? `${host.cpuUsage}%` : 'Unknown', 
      network: host?.networkUsage ? `${host.networkUsage}%` : 'Unknown', 
      statusDetail: host?.statusDetail ?? 'Unknown', 
      upTime: host?.upTime ?? 'Unknown', 
    };
  }

    return (
      <>
        <VmDu 
          data={vms} 
          columns={TableInfo.VM_CHART} 
          handleRowClick={() => console.log("Row clicked")}  

        />
      </>
    );
  };
  
  export default HostVm;