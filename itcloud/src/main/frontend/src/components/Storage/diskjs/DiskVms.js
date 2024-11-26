import React, { useState } from 'react';
import { useAllVmsFromDisk } from "../../../api/RQHook";
import TableInfo from "../../table/TableInfo";
import VmTable from "../../table/VmTable";

const DiskVms = ({diskId}) => {
  const { 
      data: vms, 
      status: vmsStatus, 
      isLoading: isVmsLoading, 
      isError: isVmsError,
  } = useAllVmsFromDisk(diskId, (e) => ({ ...e,}));

  const [selectedVm, setSelectedVm] = useState(null);
  
  return (
    <VmTable
      columns={TableInfo.VMS_FROM_DISK}
      vms={vms || []}
      selectedVm={selectedVm}
      setSelectedVm={setSelectedVm}
    />
  );
};

export default DiskVms;