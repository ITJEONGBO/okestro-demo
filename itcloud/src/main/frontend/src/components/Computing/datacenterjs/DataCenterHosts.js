import React from 'react';
import '../css/DataCenter.css';
import TableColumnsInfo from '../../table/TableColumnsInfo';
import { useHostsFromDataCenter } from '../../../api/RQHook';
import HostDupl from '../../duplication/HostDupl';

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