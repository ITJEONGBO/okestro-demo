import React, { useState } from 'react';
import VnicProfileActionButtons from '../button/VnicProfileActionButtons';
import VnicProfileTable from '../table/VnicProfileTable.js';
import VnicProfileModals from '../Modal/VnicProfileModals';

const VnicProfileDupl = ({ 
  vnicProfiles, 
  columns,
  onFetchVnicProfiles, 
  status
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
  const [action, setAction] = useState(null); // 현재 동작
  const [selectedVnicProfile, setSelectedVnicProfile] = useState(null);

  const handleActionClick = (actionType) => {
    setAction(actionType); // 동작 설정
    setIsModalOpen(true); // 모달 열기
  };

  return (
    <>
      <VnicProfileActionButtons
        onCreate={() => handleActionClick('create')}
        onEdit={() => selectedVnicProfile?.id && handleActionClick('edit')}
        onDelete={() => selectedVnicProfile?.id && handleActionClick('delete')}
        isEditDisabled={!selectedVnicProfile?.id}
      />
      <span>id = {selectedVnicProfile?.id || ''}</span>

      <VnicProfileTable
        columns={columns}
        vnicProfiles={vnicProfiles}
        selectedVnicProfile={selectedVnicProfile}
        setSelectedVnicProfile={setSelectedVnicProfile}
      />

      <VnicProfileModals
        isModalOpen={isModalOpen}
        action={action}
        onRequestClose={() => setIsModalOpen(false)}
        selectedVnicProfile={selectedVnicProfile}
      />
    </>
  );
};

export default VnicProfileDupl;
