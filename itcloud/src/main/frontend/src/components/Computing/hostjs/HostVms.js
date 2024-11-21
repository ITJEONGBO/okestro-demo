import React from 'react';
import TableInfo from "../../table/TableInfo";
import { useVmFromHost } from "../../../api/RQHook";
import VmDupl from '../../duplication/VmDupl';

const HostVms = ({ hostId }) => {
   const { 
    data: vms, 
    status: vmsStatus, 
    isLoading: isHostsLoading, 
    isError: isHostsError 
  } = useVmFromHost(hostId, (e) => ({ 
      ...e,
      status: e?.status,
      host: e?.hostVo?.name, 
      cluster: e?.clusterVo?.name,        
      dataCenter: e?.dataCenterVo?.name,
      memoryUsage: e?.usageDto.memoryPercent === null ? '' : e?.usageDto.memoryPercent + '%',
      cpuUsage: e?.usageDto.cpuPercent === null ? '' : e?.usageDto.cpuPercent + '%',
      networkUsage: e?.usageDto.networkPercent === null ? '' : e?.usageDto.networkPercent + '%',
    }));

  return (
    <>
      <VmDupl
        vms={vms || []}
        columns={TableInfo.VMS}
      />
    </>
  );
};
  
export default HostVms;