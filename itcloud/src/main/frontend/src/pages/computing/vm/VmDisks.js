import React, { useState } from 'react';
import { useDisksFromVM } from '../../../api/RQHook';
import TableColumnsInfo from '../../../components/table/TableColumnsInfo';
import DiskDupl from '../../storage/disk/DiskDupl';

const VmDisks = ({ vmId }) => {
  const { 
    data: disks = [], isLoading: isDisksLoading 
  } = useDisksFromVM(vmId);
  const [activeDiskType, setActiveDiskType] = useState('all'); // 필터링된 디스크 유형

  const diskTypes = [
    { type: 'all', label: '모두' },
    { type: 'image', label: '이미지' },
    { type: 'lun', label: '직접 LUN' },
  ];
   
  return (
    <div>
      <div className="disk_type">
        <div className="flex">
          <span>디스크 유형: </span>
          {diskTypes.map(({ type, label }) => (
            <button
              key={type} onClick={() => setActiveDiskType(type)}
              className={activeDiskType === type ? 'active' : ''}
            >
              {label}
            </button>
          ))}
        </div>
      </div>

      <DiskDupl 
        disks={activeDiskType === 'all' ? disks
          : disks.filter((disk) => disk.diskImageVo?.storageType?.toLowerCase() === activeDiskType)
        } 
        columns={activeDiskType === 'all' ? TableColumnsInfo.DISKS_FROM_VM
          : activeDiskType === 'image' ? TableColumnsInfo.DISK_IMAGES_FROM_VM
          : TableColumnsInfo.DISK_LUN_FROM_VM
        } 
        type="vm" 
      />
    </div>
  );
};

export default VmDisks;
