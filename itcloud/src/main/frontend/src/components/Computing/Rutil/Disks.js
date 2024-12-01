import React from 'react';
import '../css/Computing.css';
import TableInfo from '../../table/TableInfo';
import { useAllDisks } from '../../../api/RQHook';
import DiskDupl from '../../duplication/DiskDupl';

const Disks = () => {
  const { 
    data: disks, 
    status: disksStatus,
    isRefetching: isDisksRefetching,
    refetch: refetchDisks, 
    isError: isDisksError, 
    error: disksError, 
    isLoading: isDisksLoading,
  } = useAllDisks((e) => ({ ...e }));

  return (
    <>
      <DiskDupl 
        disks={disks || []}
        columns={TableInfo.DISKS}
      />
    </>
  );
};

export default Disks;
