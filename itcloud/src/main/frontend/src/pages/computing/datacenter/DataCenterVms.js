import React from 'react';
import '../datacenter/css/DataCenter.css';
import TableColumnsInfo from '../../../components/table/TableColumnsInfo';
import { useVMsFromDataCenter } from '../../../api/RQHook';
import VmDupl from '../../computing/vm/VmDupl';

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
        columns={TableColumnsInfo.VMS}
      />
    </>
  );
};

export default DataCenterVms;