import React  from 'react';
import '../css/Computing.css';
import TableInfo from '../../table/TableInfo';
import { useAllHosts } from '../../../api/RQHook';
import HostDupl from '../../duplication/HostDupl';

const Hosts = () => {
  const {
      data: hosts,
      status: hostsStatus,
      isRefetching: isHostsRefetching,
      refetch: refetchHosts,
      isError: isHostsError,
      error: hostsError,
      isLoading: isHostsLoading
  } = useAllHosts((e) => {
    return {
      ...e,
      status: e?.status,
      cluster: e?.clusterVo.name,
      dataCenter: e?.dataCenterVo.name,
      spmStatus: e?.spmStatus === 'SPM' ? 'SPM' : '',
      vmCnt: e?.vmSizeVo.allCnt,
      memoryUsage: e?.usageDto.memoryPercent === null ? '' : e?.usageDto.memoryPercent + '%',
      cpuUsage: e?.usageDto.cpuPercent === null ? '' : e?.usageDto.cpuPercent + '%',
      networkUsage: e?.usageDto.networkPercent === null ? '' : e?.usageDto.networkPercent + '%',
    }
  });

  return (
    <>
      <HostDupl
        hosts={hosts || []}
        columns={TableInfo.HOSTS}
      />
    </>
  );
};

export default Hosts;