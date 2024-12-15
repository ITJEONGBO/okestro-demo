import React from 'react';
import '../css/Computing.css';
import TableInfo from '../../table/TableInfo';
import { useAllDisks } from '../../../api/RQHook';
import DiskDupl from '../../duplication/DiskDupl';

const Disks = () => {
  const { 
    data: disks,
    refetch: refetchDisks, 
    error: disksError, 
    isLoading: isDisksLoading,
  } = useAllDisks((e) => ({ ...e }));

  return (
    <>
      <DiskDupl 
        columns={TableInfo.DISKS}
        disks={disks || []}
      />
    </>
  );
};

export default Disks;
