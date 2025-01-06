import React from 'react';
import TableColumnsInfo from '../../table/TableColumnsInfo';
import {useVMFromCluster} from "../../../api/RQHook";
import VmDupl from '../../duplication/VmDupl';

const ClusterVms = ({ clusterId }) => {
  const { 
    data: vms, 
    status: vmsStatus, 
    isLoading: isVmsLoading, 
    isError: isVmsError 
  } = useVMFromCluster(clusterId, (e) => ({ 
    ...e,
  }));

  return (
    <>
      <VmDupl
        vms={vms || []}
        columns={TableColumnsInfo.VMS}
      />
    </>
  );
};

export default ClusterVms;