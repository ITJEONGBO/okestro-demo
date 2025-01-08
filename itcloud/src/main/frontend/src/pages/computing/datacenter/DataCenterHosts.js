import React from 'react';
import '../datacenter/css/DataCenter.css';
import TableColumnsInfo from '../../../components/table/TableColumnsInfo';
import { useHostsFromDataCenter } from '../../../api/RQHook';
import HostDupl from '../../computing/host/HostDupl';

const DataCenterHosts = ({datacenterId}) => {
  const {
    data: hosts,
    status: hostsStatus,
    isLoading: isHostsLoading,
    isError: isHostsError,
  } = useHostsFromDataCenter(datacenterId, (e) => ({ ...e }));
  
  return (
    <>
      <HostDupl
        hosts={hosts || []}
        columns={TableColumnsInfo.HOSTS}
      />
    </>
  );
};

export default DataCenterHosts;