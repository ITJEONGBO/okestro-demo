import React from 'react';
import '../css/Computing.css'
import TableInfo from '../../table/TableInfo';
import { useAllVMs } from '../../../api/RQHook';
import VmDupl from '../../duplication/VmDupl';

const Vms = () => {
  const {
    data: vms, 
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
        columns={TableInfo.VMS}
      />
    </>
  );
};

export default Vms;
