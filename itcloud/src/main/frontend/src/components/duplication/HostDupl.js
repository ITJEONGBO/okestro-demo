import React, { useState } from 'react';
import HostActionButtons from '../button/HostActionButtons';
import HostTable from '../table/HostTable';
import HostModals from '../Modal/HostModals';

const HostDupl = ({ 
  hosts, 
  columns, 
  onFetchHosts, 
  status,
  clusterId
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
  const [action, setAction] = useState(null); // 현재 동작
  const [selectedHost, setSelectedHost] = useState(null);

  const handleActionClick = (actionType) => {
    setAction(actionType); // 동작 설정
    setIsModalOpen(true); // 모달 열기
  };

  return (
    <>
      <HostActionButtons
        onCreate={() => handleActionClick('create')}
        onEdit={() => selectedHost?.id && handleActionClick('edit')}
        onDelete={() => selectedHost?.id && handleActionClick('delete')}
        onDeactivate={() => selectedHost?.id && handleActionClick('deactivate')}
        onActivate={() => selectedHost?.id && handleActionClick('activate')}
        onRestart={() => selectedHost?.id && handleActionClick('restart')}
        // onStop={() => selectedHost?.id && handleActionClick('stop')}
        onReInstall={() => selectedHost?.id && handleActionClick('reinstall')}
        onRegister={() => selectedHost?.id && handleActionClick('register')}
        onHaOn={() => selectedHost?.id && handleActionClick('haon')}
        onHaOff={() => selectedHost?.id && handleActionClick('haoff')}
        isEditDisabled={!selectedHost?.id}
        status={selectedHost?.status}
      />
      <span>id = {selectedHost?.id || ''}</span>

      <HostTable
        columns={columns}
        hosts={hosts}
        selectedHost={selectedHost}
        setSelectedHost={setSelectedHost}
      />

      <HostModals
        isModalOpen={isModalOpen}
        action={action}
        onRequestClose={() => setIsModalOpen(false)}
        selectedHost={selectedHost}
        clusterId={clusterId}
      />
    </>
  );
};

export default HostDupl;
