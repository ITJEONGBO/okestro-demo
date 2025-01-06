import React from 'react';
import HeaderButton from '../button/HeaderButton';
import TableColumnsInfo from '../table/TableColumnsInfo';
import { useAllDisks } from '../../api/RQHook';
import DiskDupl from '../duplication/DiskDupl';
import { faDatabase } from '@fortawesome/free-solid-svg-icons'

const AllDisk = () => {
  const { 
    data: disks,
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
            columns={TableColumnsInfo.DISKS}
            disks={disks || []}          
          />
        </div>
      </div>
    </div>
  );
};

export default AllDisk;
