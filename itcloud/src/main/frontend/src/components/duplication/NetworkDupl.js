import React, { useState } from 'react';
import NetworkActionButtons from '../button/NetworkActionButtons';
import NetworkTable from '../table/NetworkTable';
import NetworkModals from '../Modal/NetworkModals';

const NetworkDupl = ({ 
  networks, 
  columns, 
  onFetchNetworks, 
  status 
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
  const [action, setAction] = useState(null); // 현재 동작
  const [selectedNetwork, setSelectedNetwork] = useState(null);

  const handleActionClick = (actionType) => {
    setAction(actionType); // 동작 설정
    setIsModalOpen(true); // 모달 열기
  };

  return (
    <>
      <NetworkActionButtons
        onCreate={() => handleActionClick('create')}
        onEdit={() => selectedNetwork?.id && handleActionClick('edit')}
        onDelete={() => selectedNetwork?.id && handleActionClick('delete')}
        onImport={() => handleActionClick('import')}
        isEditDisabled={!selectedNetwork?.id}
        status={selectedNetwork?.status}
      />
      <span>id = {selectedNetwork?.id || ''}</span>

      <NetworkTable
        columns={columns}
        networks={networks}
        selectedNetwork={selectedNetwork}
        setSelectedNetwork={setSelectedNetwork}
      />

      <NetworkModals
        isModalOpen={isModalOpen}
        action={action}
        onRequestClose={() => setIsModalOpen(false)}
        selectedNetwork={selectedNetwork}
      />
    </>
  );
};

export default NetworkDupl;
