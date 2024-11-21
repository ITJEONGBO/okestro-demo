import React from 'react';
import '../css/Computing.css'
import TableInfo from '../../table/TableInfo';
import { useAllVMs } from '../../../api/RQHook';
import VmDupl from '../../duplication/VmDupl';

const Vms = () => {
  const {
    data: vms, 
    status: vmsStatus,
    isRefetching: isVmsRefetching,
    refetch: refetchVms, 
    isError: isVmsError, 
    error: vmsError, 
    isLoading: isVmsLoading,
  } = useAllVMs((e) => {
    return {
      ...e,
      status: e?.status,
      host: e?.hostVo?.name, 
      cluster: e?.clusterVo?.name,        
      dataCenter: e?.dataCenterVo?.name,
      memoryUsage: e?.usageDto.memoryPercent === null ? '' : e?.usageDto.memoryPercent + '%',
      cpuUsage: e?.usageDto.cpuPercent === null ? '' : e?.usageDto.cpuPercent + '%',
      networkUsage: e?.usageDto.networkPercent === null ? '' : e?.usageDto.networkPercent + '%',
    }
  });

  return (
    <>
      <VmDupl
        vms={vms || []}
        columns={TableInfo.VMS}
      />
    </>
  );
};

export default Vms;
