import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import VmActionButtons from '../button/VmActionButtons';
import VmTable from '../table/VmTable';
import VmModals from '../Modal/VmModals';

const VmDupl = ({
  vms = [], 
  columns, 
  type,
  onFetchVms, 
  status
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
  const [action, setAction] = useState(null); // 현재 동작
  const [selectedVm, setSelectedVm] = useState(null);
  const navigate = useNavigate();

  const handleActionClick = (actionType) => {
    if (actionType === 'templates') {
      navigate('/computing/vms/templates');
    } else {
      setAction(actionType); // 동작 설정
      setIsModalOpen(true); // 모달 열기
    }
  };

  return (
    <>
      <VmActionButtons
        onCreate={() => handleActionClick('create')} 
        onEdit={() => selectedVm?.id && handleActionClick('edit')} 
        onDelete={() => selectedVm?.id && handleActionClick('delete')} 
        onConsole={() => selectedVm?.id && handleActionClick('console')} 
        onStart={() => selectedVm?.id && handleActionClick('start')} 
        onPause={() => selectedVm?.id && handleActionClick('pause')} 
        onStop={() => selectedVm?.id && handleActionClick('stop')} 
        onReboot={() => selectedVm?.id && handleActionClick('reboot')} 
        templates={() => handleActionClick('templates')} 
        snapshots={() => selectedVm?.id && handleActionClick('snapshots')} 
        migration={() => selectedVm?.id && handleActionClick('migration')} 
        onExport={() => handleActionClick('onExport')} 
        onCopy={() => selectedVm?.id && handleActionClick('onCopy')} 
        addTemplate={() => selectedVm?.id && handleActionClick('addTemplate')} 
        exportOva={() => selectedVm?.id && handleActionClick('exportova')} 
        isEditDisabled={!selectedVm?.id} 
        status={selectedVm?.status} 
        isMigrationDisabled={!selectedVm?.hostVo?.id} // 호스트 ID가 없으면 비활성화
      />
      <span>id = {selectedVm?.id || ''}</span>  
      
      <VmTable
        columns={columns}
        vms={vms}
        selectedVm={selectedVm}
        setSelectedVm={setSelectedVm}
      />
      
      <VmModals
        isModalOpen={isModalOpen}
        action={action}
        onRequestClose={() => setIsModalOpen(false)}
        selectedVm={selectedVm}
      />
    </>
  );
};

export default VmDupl;