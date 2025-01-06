import React from 'react';
import '../css/Computing.css'
import TableColumnsInfo from '../../table/TableColumnsInfo';
import { useAllVMs } from '../../../api/RQHook';
import VmDupl from '../../duplication/VmDupl';

const Vms = () => {
  const {
    data: vms = [], 
    status: vmsStatus,
    isRefetching: isVmsRefetching,
    refetch: refetchVms, 
    isError: isVmsError, 
    error: vmsError, 
    isLoading: isVmsLoading,
  } = useAllVMs((e) => {
    return {
      ...e,
    }
  });

  return (
    <>
      <VmDupl
        vms={vms || []}
        columns={TableColumnsInfo.VMS}
      />
    </>
  );
};

export default Vms;
