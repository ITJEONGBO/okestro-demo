import React, { useState } from 'react';
import { useDisksFromVM } from '../../../api/RQHook';
import TableColumnsInfo from '../../../components/table/TableColumnsInfo';
import DiskDupl from '../../storage/disk/DiskDupl'

const VmDisks = ({ vmId }) => {
  const { 
    data: disks = [], isLoading: isDisksLoading,
  } = useDisksFromVM(vmId, (e) => ({ ...e }));

  const [activeDiskType, setActiveDiskType] = useState('all'); // 필터링된 디스크 유형

  const handleDiskTypeClick = (type) => {
    setActiveDiskType(type); // 디스크 유형 변경
  };

  // diskImageVo.storageType으로 필터링
  const filteredDisks = activeDiskType === 'all'
    ? disks
    : disks.filter((disk) => disk.diskImageVo?.storageType?.toLowerCase() === activeDiskType);

  return (
    <div>
      <div className="disk_type">
        <div className="flex">
          <span>디스크 유형: </span>
          <button
            className={activeDiskType === 'all' ? 'active' : ''}
            onClick={() => handleDiskTypeClick('all')}
          >
            모두
          </button>
          <button
            className={activeDiskType === 'image' ? 'active' : ''}
            onClick={() => handleDiskTypeClick('image')}
          >
            이미지
          </button>
          <button
            className={activeDiskType === 'lun' ? 'active' : ''}
            onClick={() => handleDiskTypeClick('lun')}
          >
            직접 LUN
          </button>
        </div>
      </div>

      <DiskDupl
        disks={filteredDisks}
        columns={
          activeDiskType === 'all' ? TableColumnsInfo.DISKS_FROM_VM
            : activeDiskType === 'image' ? TableColumnsInfo.DISK_IMAGES_FROM_VM
            : TableColumnsInfo.LUN_DISK
        }
        type='vm'
      />
    </div>
  );
};

export default VmDisks;
