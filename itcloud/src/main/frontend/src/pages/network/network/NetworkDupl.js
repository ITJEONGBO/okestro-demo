import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
// import NetworkActionButtons from './NetworkActionButtons';
import NetworkTable from './NetworkTable';
import NetworkModals from './modal/NetworkModals';
import AllActionButton from './button/AllActionButton';

const NetworkDupl = ({ 
  networks = [], 
  columns = [], 
  onFetchNetworks, 
  status 
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [action, setAction] = useState(null);
  const [selectedNetworks, setSelectedNetworks] = useState([]); // 다중 선택된 네트워크
  const navigate = useNavigate();
  
  const handleActionClick = (actionType) => {
    if (actionType === 'vnic') {
      navigate('/vnicProfiles');
    } else {
      setAction(actionType);
      setIsModalOpen(true);
    }
  };

  const selectedIds = (Array.isArray(selectedNetworks) ? selectedNetworks : []).map((network) => network.id).join(', ');

  // 데이터 유효성 검사
  if (!Array.isArray(networks) || !Array.isArray(columns)) {
    return <p>유효하지 않은 데이터입니다.</p>;
  }
  
  const buttons = [
    { label: '새로 만들기', onClick: () => handleActionClick('create') },
    { label: '편집', onClick: () => selectedNetworks.length === 1 && handleActionClick('edit'), disabled: selectedNetworks.length !== 1 },
    { label: '삭제', onClick: () => selectedNetworks.length > 0 && handleActionClick('delete'), disabled: selectedNetworks.length === 0 },
    { label: '가져오기', onClick: () => handleActionClick('import') },
    { label: 'VNIC 프로파일', onClick: () => handleActionClick('vnic') },
  ];
  return (
    <div onClick={(e) => e.stopPropagation()}> {/* 테이블 외부 클릭 방지 */}
     <AllActionButton buttons={buttons} dropdowns={[]} />
      {/* <NetworkActionButtons
        onCreate={() => handleActionClick('create')}
        onEdit={() => selectedNetworks.length === 1 && handleActionClick('edit')}
        onDelete={() => selectedNetworks.length > 0 && handleActionClick('delete')}
        onImport={() => handleActionClick('import')}
        isEditDisabled={!Array.isArray(selectedNetworks) || selectedNetworks.length !== 1} // 방어 코드 추가
        isDeleteDisabled={selectedNetworks.length === 0} // 삭제 버튼 조건
        status={selectedNetworks[0]?.status}
      /> */}
      <span>선택된 네트워크 ID: {selectedIds || '선택된 항목이 없습니다.'}</span>

      <NetworkTable
        columns={columns}
        networks={networks}
        setSelectedNetworks={(selected) => {
          if (Array.isArray(selected)) setSelectedNetworks(selected); // 유효한 선택만 반영
        }}
      />

      <NetworkModals
        isModalOpen={isModalOpen}
        action={action}
        onRequestClose={() => setIsModalOpen(false)}
        selectedNetwork={selectedNetworks.length > 0 ? selectedNetworks[0] : null} // 선택된 첫 번째 네트워크 전달
        selectedNetworks={selectedNetworks}
      />
    </div>
  );
};

export default NetworkDupl;
