import React, { useState } from 'react';
import { useAllVmsFromDisk } from "../../../api/RQHook";
import TableColumnsInfo from "../../table/TableColumnsInfo";
import VmTable from "../../table/VmTable";

const DiskVms = ({diskId}) => {
  const { 
      data: vms, 
  } = useAllVmsFromDisk(diskId, (e) => ({ ...e,}));

  const [selectedVms, setSelectedVms] = useState([]); // 선택된 VM
  return (
    <VmTable
      columns={TableColumnsInfo.VMS_FROM_DISK}
      vms={vms || []}
      setSelectedVms={(selected) => {
        if (Array.isArray(selected)) setSelectedVms(selected); // 유효한 선택만 반영
      }}
    />
  );
};

export default DiskVms;