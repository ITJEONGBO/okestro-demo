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
  } = useHostFromCluster(cId, (e) => ({ ...e }));

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
