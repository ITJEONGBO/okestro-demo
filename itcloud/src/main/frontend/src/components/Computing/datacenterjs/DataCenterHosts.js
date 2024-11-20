import React from 'react';
import '../css/DataCenter.css';
import TableInfo from '../../table/TableInfo';
import { useHostsFromDataCenter } from '../../../api/RQHook';
import HostDupl from '../../duplication/HostDupl';

const DataCenterHosts = ({datacenterId}) => {
  const {
    data: hosts,
    status: hostsStatus,
    isLoading: isHostsLoading,
    isError: isHostsError,
  } = useHostsFromDataCenter(datacenterId, (e) => ({ 
    ...e, 
    status: e?.status,
    cluster: e?.clusterVo.name,
    dataCenter: e?.dataCenterVo.name,
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

export default DataCenterHosts;