import React from 'react';
import './css/AllDisk.css';
import HeaderButton from '../button/HeaderButton';
import TableInfo from '../table/TableInfo';
import { useAllDisks } from '../../api/RQHook';
import DiskDupl from '../duplication/DiskDupl';
import { faDatabase } from '@fortawesome/free-solid-svg-icons'

const AllDisk = () => {
  const { 
    data: disks,
    status: disksStatus,
    isRefetching: isDisksRefetching,
    refetch: disksRefetch, 
    isError: isDisksError, 
    error: disksError, 
    isLoading: isDiskssLoading,
  } = useAllDisks((e) => ({...e,}));


  return (
    <div id="section">
      <div>
        <HeaderButton
          titleIcon={faDatabase}
          title="디스크"
          subtitle=""
          buttons={[]} 
          popupItems={[]}
        />
        <div className="host_btn_outer">
          <DiskDupl 
            columns={TableInfo.DISKS}
            disks={disks || []}          
          />
        </div>
      </div>
    </div>
  );
};

export default AllDisk;
