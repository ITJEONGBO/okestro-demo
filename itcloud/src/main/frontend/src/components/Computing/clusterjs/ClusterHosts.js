import React from 'react';
import TableInfo from '../../table/TableInfo';
import { useHostFromCluster } from "../../../api/RQHook";
import HostDupl from '../../duplication/HostDupl';

const ClusterHosts = ({ cId }) => {
  const { 
    data: hosts, 
    status: hostsStatus, 
    isLoading: isHostsLoading, 
    isError: isHostsError 
  } = useHostFromCluster(cId, (e) => ({ 
    ...e, 
    status: e?.status,
    cluster: e?.clusterVo.name,
    dataCenter: e?.dataCenterVo.name,
    spmStatus: e?.spmStatus === 'NONE' ? '보통' : e?.spmStatus,
    vmCnt: e?.vmSizeVo.allCnt,
    memoryUsage: e?.usageDto.memoryPercent === null ? '' : e?.usageDto.memoryPercent + '%',
    cpuUsage: e?.usageDto.cpuPercent === null ? '' : e?.usageDto.cpuPercent + '%',
    networkUsage: e?.usageDto.networkPercent === null ? '' : e?.usageDto.networkPercent + '%',
  }));

  return (
    <>
      <HostDupl
        hosts={hosts || []}
        columns={TableInfo.HOSTS}
      />
    </>
  );
};
  
export default ClusterHosts;
