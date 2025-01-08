import React from 'react';
import TableColumnsInfo from '../../../components/table/TableColumnsInfo';
import { useHostFromCluster } from "../../../api/RQHook";
import HostDupl from '../../computing/host/HostDupl';

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
        columns={TableColumnsInfo.HOSTS}
        clusterId={clusterId}
      />
    </>
  );
};
  
export default ClusterHosts;
