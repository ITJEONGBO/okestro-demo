import React from 'react';
import TableInfo from "../../table/TableInfo";
import { useVmFromHost } from "../../../api/RQHook";
import VmDupl from '../../duplication/VmDupl';

const HostVms = ({ hostId }) => {
  const { 
    data: vms, 
    status: vmsStatus, 
    isLoading: isHostsLoading, 
    isError: isHostsError 
  } = useVmFromHost(hostId, (e) => ({ ...e,}));

  return (
    <>
      <VmDupl
        vms={vms || []}
        columns={TableInfo.VMS_FROM_HOST}
      />
    </>
  );
};
  
export default HostVms;