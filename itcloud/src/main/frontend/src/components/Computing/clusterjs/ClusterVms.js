import React from 'react';
import TableInfo from '../../table/TableInfo';
import {useVMFromCluster} from "../../../api/RQHook";
import VmDupl from '../../duplication/VmDupl';

const ClusterVms = ({ cId }) => {
  const { 
    data: vms, 
    status: vmsStatus, 
    isLoading: isVmsLoading, 
    isError: isVmsError 
  } = useVMFromCluster(cId, (e) => ({ 
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

export default ClusterVms;