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
  } = useHostsFromDataCenter(datacenterId, (e) => ({ ...e }));
  
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