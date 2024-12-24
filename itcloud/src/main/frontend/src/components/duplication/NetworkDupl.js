import React, { useState } from 'react';
import NetworkActionButtons from '../button/NetworkActionButtons';
import NetworkTable from '../table/NetworkTable';
import NetworkModals from '../Modal/NetworkModals';

const NetworkDupl = ({ 
  networks = [], 
  columns = [], 
  onFetchNetworks, 
  status 
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [action, setAction] = useState(null);
  const [selectedNetworks, setSelectedNetworks] = useState([]); // 다중 선택된 네트워크

  const handleActionClick = (actionType) => {
    setAction(actionType);
    setIsModalOpen(true);
  };

  const selectedIds = (Array.isArray(selectedNetworks) ? selectedNetworks : []).map((network) => network.id).join(', ');

  // 데이터 유효성 검사
  if (!Array.isArray(networks) || !Array.isArray(columns)) {
    return <p>유효하지 않은 데이터입니다.</p>;
  }

  return (
    <div onClick={(e) => e.stopPropagation()}> {/* 테이블 외부 클릭 방지 */}
      <NetworkActionButtons
        onCreate={() => handleActionClick('create')}
        onEdit={() => selectedNetworks.length === 1 && handleActionClick('edit')}
        onDelete={() => selectedNetworks.length > 0 && handleActionClick('delete')}
        onImport={() => handleActionClick('import')}
        isEditDisabled={!Array.isArray(selectedNetworks) || selectedNetworks.length !== 1} // 방어 코드 추가
        isDeleteDisabled={selectedNetworks.length === 0} // 삭제 버튼 조건
        status={selectedNetworks[0]?.status}
      />
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


// import React, { useState } from 'react';
// import NetworkActionButtons from '../button/NetworkActionButtons';
// import NetworkTable from '../table/NetworkTable';
// import NetworkModals from '../Modal/NetworkModals';

// const NetworkDupl = ({ 
//   networks, 
//   columns, 
//   onFetchNetworks, 
//   status 
// }) => {
//   const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
//   const [action, setAction] = useState(null); // 현재 동작
//   const [selectedNetwork, setSelectedNetwork] = useState(null);

//   const handleActionClick = (actionType) => {
//     setAction(actionType); // 동작 설정
//     setIsModalOpen(true); // 모달 열기
//   };

//   return (
//     <>
//       <NetworkActionButtons
//         onCreate={() => handleActionClick('create')}
//         onEdit={() => selectedNetwork?.id && handleActionClick('edit')}
//         onDelete={() => selectedNetwork?.id && handleActionClick('delete')}
//         onImport={() => handleActionClick('import')}
//         isEditDisabled={!selectedNetwork?.id}
//         status={selectedNetwork?.status}
//       />
//       <span>id = {selectedNetwork?.id || ''}</span>

//       <NetworkTable
//         columns={columns}
//         networks={networks}
//         selectedNetwork={selectedNetwork}
//         setSelectedNetwork={setSelectedNetwork}
//       />

//       <NetworkModals
//         isModalOpen={isModalOpen}
//         action={action}
//         onRequestClose={() => setIsModalOpen(false)}
//         selectedNetwork={selectedNetwork}
//       />
//     </>
//   );
// };

// export default NetworkDupl;
