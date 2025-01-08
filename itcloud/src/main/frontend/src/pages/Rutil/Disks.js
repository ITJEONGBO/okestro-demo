import React from 'react';
import TableColumnsInfo from '../../components/table/TableColumnsInfo';
import { useAllDisks } from '../../api/RQHook';
import DiskDupl from '../../pages/storage/disk/DiskDupl';

const Disks = () => {
  const { 
    data: disks = [],
    refetch: refetchDisks, 
    error: disksError, 
    isLoading: isDisksLoading,
  } = useAllDisks((e) => ({ 
    ...e,
    connected : e?.connectVm?.name || e?.connectTemplate?.name
   }));

  return (
    <>
      <DiskDupl 
        columns={TableColumnsInfo.DISKS}
        disks={disks || []}
      />
    </>
  );
};

export default Disks;
