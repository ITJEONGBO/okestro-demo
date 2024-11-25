import React, { useState } from 'react';
import DiskActionButtons from '../button/DiskActionButtons';
import DiskTable from '../table/DiskTable';
import DiskModals from '../Modal/DiskModals';

const DiskDupl = ({ 
  disks, 
  columns,
  onFetchDisks, 
  status
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
  const [action, setAction] = useState(null); // 현재 동작
  const [selectedDisk, setSelectedDisk] = useState(null);

  const handleActionClick = (actionType) => {
    setAction(actionType); // 동작 설정
    setIsModalOpen(true); // 모달 열기
  };

  return (
    <>
      <DiskActionButtons
        onCreate={() => handleActionClick('create')}
        onEdit={() => selectedDisk?.id && handleActionClick('edit')}
        onDelete={() => selectedDisk?.id && handleActionClick('delete')}
        onMove={() => selectedDisk?.id && handleActionClick('move')}
        onCopy={() => selectedDisk?.id && handleActionClick('copy')}
        onUpload={() => handleActionClick('upload')}
        isEditDisabled={!selectedDisk?.id}
        status={selectedDisk?.status}
      />
      <span>id = {selectedDisk?.id || ''}</span>

      <DiskTable
        columns={columns}
        disks={disks}
        selectedDisk={selectedDisk}
        setSelectedDisk={setSelectedDisk}
      />

      <DiskModals
        isModalOpen={isModalOpen}
        action={action}
        onRequestClose={() => setIsModalOpen(false)}
        selectedDisk={selectedDisk}
      />
    </>
  );
};

export default DiskDupl;
