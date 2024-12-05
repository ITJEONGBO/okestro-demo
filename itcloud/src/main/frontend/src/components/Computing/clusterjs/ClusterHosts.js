import React from 'react';
import TableInfo from '../../table/TableInfo';
import { useHostFromCluster } from "../../../api/RQHook";
import HostDupl from '../../duplication/HostDupl';

const ClusterHosts = ({ clusterId }) => {
  const { 
    data: hosts, 
    status: hostsStatus, 
    isLoading: isHostsLoading, 
    isError: isHostsError 
  } = useHostFromCluster(clusterId, (e) => ({ ...e }));

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
