import React, { useState, useEffect, Suspense } from 'react';
import Modal from 'react-modal';
import { faChevronLeft, faEllipsisV } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import TablesOuter from '../../../components/table/TablesOuter';
import { useDisksFromVM } from '../../../api/RQHook';
import DiskModal from '../../storage/disk/modal/DiskModal';
import DeleteModal from '../../../components/DeleteModal';
import TableColumnsInfo from '../../../components/table/TableColumnsInfo';
import DiskDupl from '../../storage/disk/DiskDupl';

const VmDisks = ({ vmId }) => {
  const { 
    data: disks = [], isLoading: isDisksLoading,
  } = useDisksFromVM(vmId, (e) => ({...e}));

  // const formattedDisks = disks?.map((disk) => ({
  //   alias: disk.alias,
  //   id: disk.id,
  //   icon1: disk.bootable ? '🔑' : '',
  //   icon2: disk.readOnly ? '🔒' : '',
  //   connectionTarget: disk.vmVo?.name || 'N',
  //   storageDomain: disk.diskImageVo.storageDomainVo?.name || 'N/A',
  //   virtualSize: `${(disk.diskImageVo.virtualSize / (1024 ** 3)).toFixed(0)} GB`,
  //   status: disk.diskImageVo.status,
  //   storageType: disk.diskImageVo.storageType,
  //   description: disk.diskImageVo.description || '',
  // }));

  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 상태
  const [modals, setModals] = useState({ create: false, edit: false, delete: false }); // 동작별 모달 관리
  const [action, setAction] = useState(''); // 현재 동작 (create, edit 등)
  const [activeDiskType, setActiveDiskType] = useState('all'); // 필터링된 디스크 유형
  const [selectedDisks, setSelectedDisks] = useState([]); // 다중 선택된 디스크 관리

  const handleDiskTypeClick = (type) => {
    setActiveDiskType(type); // 디스크 유형 변경
  };

  const toggleModal = (type, isOpen) => {
    setModals((prev) => ({ ...prev, [type]: isOpen })); // 모달 열기/닫기
  };

  const handleActionClick = (actionType) => {
    setAction(actionType);
    setIsModalOpen(true);
  };

  
  const selectedIds = selectedDisks.map((disk) => disk.id).join(', ');

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
        disks={disks}
        columns={
          activeDiskType === 'all' ? TableColumnsInfo.ALL_DISK
            : activeDiskType === 'image' ? TableColumnsInfo.DISKS_FROM_
            : TableColumnsInfo.LUN_DISK
        }
      />


      {/* <TablesOuter
        columns={
          activeDiskType === 'all'
            ? TableColumnsInfo.ALL_DISK
            : activeDiskType === 'image'
            ? TableColumnsInfo.DISKS_FROM_
            : TableColumnsInfo.LUN_DISK
        }
        data={disks?.map((disk) => ({
          alias: disk?.alias,
          id: disk?.id,
          icon1: disk.bootable ? '🔑' : '',
          icon2: disk.readOnly ? '🔒' : '',
          connectionTarget: disk.vmVo?.name || 'N',
          storageDomain: disk.diskImageVo.storageDomainVo?.name || 'N/A',
          virtualSize: `${(disk.diskImageVo.virtualSize / (1024 ** 3)).toFixed(0)} GB`,
          status: disk.diskImageVo.status,
          storageType: disk.diskImageVo.storageType,
          description: disk.diskImageVo.description || ''
        }))}
        onRowClick={(selectedRows) => setSelectedDisks(selectedRows)}
      />

      
      <Suspense>
        {(action === 'create' || action === 'edit') && (
          <DiskModal
            isOpen={isModalOpen}
            onRequestClose={() => setIsModalOpen(false)}
            editMode={action === 'edit'}
            diskId={selectedDisks[0]?.id || null} // 선택된 디스크 중 첫 번째
            vmId={vmId || ''}
          />
        )}
        {modals.delete && selectedDisks.length > 0 && (
          <DeleteModal
            isOpen={modals.delete}
            type="vmDisk"
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel="디스크"
            data={selectedDisks}
            vmId={vmId}
          />
        )}
      </Suspense> */}
    </div>
  );
};

export default VmDisks;