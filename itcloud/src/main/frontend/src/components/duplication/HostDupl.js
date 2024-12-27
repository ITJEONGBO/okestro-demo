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
  const [selectedHosts, setSelectedHosts] = useState([]); // 다중 선택된 호스트

  const handleActionClick = (actionType) => {
    setAction(actionType); // 동작 설정
    setIsModalOpen(true); // 모달 열기
  };

  const selectedIds = (Array.isArray(selectedHosts) ? selectedHosts : []).map((host) => host.id).join(', ');

  return (
    <>
      <HostActionButtons
        onCreate={() => handleActionClick('create')}
        onEdit={() => selectedHosts.length === 1 && handleActionClick('edit')}
        onDelete={() => selectedHosts.length > 0 && handleActionClick('delete')}
        onDeactivate={() => selectedHosts.length > 0 && handleActionClick('deactivate')}
        onActivate={() => selectedHosts.length > 0 && handleActionClick('activate')}
        onRestart={() => selectedHosts.length > 0 && handleActionClick('restart')}
        onReInstall={() => selectedHosts.length > 0 && handleActionClick('reinstall')}
        onRegister={() => selectedHosts.length > 0 && handleActionClick('register')}
        onHaOn={() => selectedHosts.length > 0 && handleActionClick('haon')}
        onHaOff={() => selectedHosts.length > 0 && handleActionClick('haoff')}
        isEditDisabled={selectedHosts.length !== 1}
        isDeleteDisabled={selectedHosts.length === 0}
        status={selectedHosts[0]?.status} // 첫 번째 선택된 호스트의 상태
      />
      <span>선택된 호스트 ID: {selectedIds || '선택된 항목이 없습니다.'}</span>

      <HostTable
        columns={columns}
        hosts={hosts}
        selectedHosts={selectedHosts} // 다중 선택 상태 전달
        setSelectedHosts={(selected) => {
          if (Array.isArray(selected)) setSelectedHosts(selected); // 유효한 선택만 반영
        }}
      />

      <HostModals
        isModalOpen={isModalOpen}
        action={action}
        onRequestClose={() => setIsModalOpen(false)}
        selectedHost={selectedHosts.length > 0 ? selectedHosts[0] : null} // 다중 선택된 호스트 전달
        selectedHosts={selectedHosts}
        clusterId={clusterId}
      />
    </>
  );
};

export default HostDupl;

// import React, { useState } from 'react';
// import HostActionButtons from '../button/HostActionButtons';
// import HostTable from '../table/HostTable';
// import HostModals from '../Modal/HostModals';

// const HostDupl = ({ 
//   hosts, 
//   columns, 
//   onFetchHosts, 
//   status,
//   clusterId
// }) => {
//   const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
//   const [action, setAction] = useState(null); // 현재 동작
//   const [selectedHost, setSelectedHost] = useState(null);

//   const handleActionClick = (actionType) => {
//     setAction(actionType); // 동작 설정
//     setIsModalOpen(true); // 모달 열기
//   };

//   return (
//     <>
//       <HostActionButtons
//         onCreate={() => handleActionClick('create')}
//         onEdit={() => selectedHost?.id && handleActionClick('edit')}
//         onDelete={() => selectedHost?.id && handleActionClick('delete')}
//         onDeactivate={() => selectedHost?.id && handleActionClick('deactivate')}
//         onActivate={() => selectedHost?.id && handleActionClick('activate')}
//         onRestart={() => selectedHost?.id && handleActionClick('restart')}
//         onReInstall={() => selectedHost?.id && handleActionClick('reinstall')}
//         onRegister={() => selectedHost?.id && handleActionClick('register')}
//         onHaOn={() => selectedHost?.id && handleActionClick('haon')}
//         onHaOff={() => selectedHost?.id && handleActionClick('haoff')}
//         isEditDisabled={!selectedHost?.id}
//         status={selectedHost?.status}
//       />
//       <span>id = {selectedHost?.id || ''}</span>

//       <HostTable
//         columns={columns}
//         hosts={hosts}
//         selectedHost={selectedHost}
//         setSelectedHost={setSelectedHost}
//       />

//       <HostModals
//         isModalOpen={isModalOpen}
//         action={action}
//         onRequestClose={() => setIsModalOpen(false)}
//         selectedHost={selectedHost}
//         clusterId={clusterId}
//       />
//     </>
//   );
// };

// export default HostDupl;
