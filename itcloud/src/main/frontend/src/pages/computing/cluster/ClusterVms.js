import React from 'react';
import TableColumnsInfo from '../../../components/table/TableColumnsInfo';
import {useVMFromCluster} from "../../../api/RQHook";
import VmDupl from '../../computing/vm/VmDupl';

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