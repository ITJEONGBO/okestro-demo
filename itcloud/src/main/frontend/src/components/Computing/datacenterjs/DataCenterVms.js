import React from 'react';
import '../css/DataCenter.css';
import TableInfo from '../../table/TableInfo';
import { useVMsFromDataCenter } from '../../../api/RQHook';
import VmDupl from '../../duplication/VmDupl';

const DataCenterVms = ({datacenterId}) => {
  const {
    data: vms,
    status: vmsStatus,
    isLoading: isVmsLoading,
    isError: isVmsError,
  } = useVMsFromDataCenter(datacenterId, (e) => ({ 
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

export default DataCenterVms;