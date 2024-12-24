import React, { useState } from 'react';
import DomainActionButtons from '../button/DomainActionButtons';
import DomainTable from '../table/DomainTable';
import DomainModals from '../Modal/DomainModals';

const DomainDupl = ({ 
  domains = [], // 기본값 설정
  columns = [], // 기본값 설정
  type = 'domain', // 데이터센터, 스토리지 도메인, rutil
  onFetchDomains, 
  status,
  datacenterId
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
  const [action, setAction] = useState(null); // 현재 동작
  const [selectedDomains, setSelectedDomains] = useState([]); // 다중 선택된 도메인

  const handleActionClick = (actionType) => {
    setAction(actionType); // 동작 설정
    setIsModalOpen(true); // 모달 열기
  };

  const selectedIds = selectedDomains.map((domain) => domain.id).join(', ');
  // const isDeleteDisabled = //삭제조건
  selectedDomains.length === 0 || // 선택된 항목이 없거나
  selectedDomains.some((domain) => domain.status !== 'ACTIVE'); // 'ACTIVE' 상태인 도메인이 있을 경우

  // 도메인 생성, 도메인 가져오기, 도메인 관리(편집), 삭제 등 버튼 액션 처리
  return (
    <>
      { type === 'datacenter' ? (
        <DomainActionButtons
          onActivate={() => selectedDomains.length > 0 && handleActionClick('activate')}
          onAttach={() => selectedDomains.length > 0 && handleActionClick('attach')}
          onDetach={() => selectedDomains.length > 0 && handleActionClick('detach')}
          onMaintenance={() => selectedDomains.length > 0 && handleActionClick('maintenance')}
          status={selectedDomains[0]?.status}
        />
      ) : (
        <DomainActionButtons
          onCreate={() => handleActionClick('create')}
          onImport={() => handleActionClick('imported')}
          onEdit={() => selectedDomains.length === 1 && handleActionClick('edit')}
          onDelete={() => selectedDomains.length > 0 && handleActionClick('delete')}
          disk={type === 'domain' ? true: false}
          isEditDisabled={selectedDomains.length !== 1}
          status={selectedDomains[0]?.status}
        />
      )}
      <span>선택된 도메인 ID: {selectedIds || '선택된 항목이 없습니다.'}</span>

      <DomainTable
        columns={columns}
        domains={domains}
        selectedDomains={selectedDomains}
        setSelectedDomains={(selected) => {
          if (Array.isArray(selected)) setSelectedDomains(selected); // 다중 선택 지원
        }}
      />

      <DomainModals
        isModalOpen={isModalOpen}
        action={action}
        onRequestClose={() => setIsModalOpen(false)}
        selectedDomains={selectedDomains}
        selectedDomain={selectedDomains.length > 0 ? selectedDomains[0] : null} // 선택된 첫 번째 네트워크 전달
        datacenterId={datacenterId}
      />
    </>
  );
};

export default DomainDupl;


// 옛날
// import React, { useState } from 'react';
// import DomainActionButtons from '../button/DomainActionButtons';
// import DomainTable from '../table/DomainTable';
// import DomainModals from '../Modal/DomainModals';

// const DomainDupl = ({ 
//   domains, 
//   columns,
//   type='domain',  // 데이터센터, 스토리지 도메인, rutil
//   onFetchDomains, 
//   status,
//   datacenterId
// }) => {
//   const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
//   const [action, setAction] = useState(null); // 현재 동작
//   const [selectedDomain, setSelectedDomain] = useState(null);

//   const handleActionClick = (actionType) => {
//     setAction(actionType); // 동작 설정
//     setIsModalOpen(true); // 모달 열기
//   };

//   // 도메인 생성, 도메인 가져오기, 도메인 관리(편집), 삭제, connection, lun 새로고침, 파괴, 마스터 스토리지 도메인으로 선택
//   // 데이터센터: 연결, 분리, 활성, 유지보수
//   return (
//     <>
//       { type === 'domain' &&
//         <DomainActionButtons
//           onCreate={() => handleActionClick('create')}
//           onImport={() => handleActionClick('imported')}
//           onEdit={() => selectedDomain?.id && handleActionClick('edit')}
//           onDelete={() => selectedDomain?.id && handleActionClick('delete')}
//           disk={true}
//           isEditDisabled={!selectedDomain?.id}
//           status={selectedDomain?.status}
//         />
//       }
//       { type === 'datacenter' &&
//         <DomainActionButtons
//           onActivate={() => selectedDomain?.id && handleActionClick('activate')}
//           onAttach={() => selectedDomain?.id && handleActionClick('attach')}
//           onDetach={() => selectedDomain?.id && handleActionClick('detach')}
//           onMaintenance={() => selectedDomain?.id && handleActionClick('maintenance')}
//           status={selectedDomain?.status}
//         />
//       }
//       { type === 'rutil' &&
//         <DomainActionButtons
//           onCreate={() => handleActionClick('create')}
//           onImport={() => handleActionClick('imported')}
//           onEdit={() => selectedDomain?.id && handleActionClick('edit')}
//           onDelete={() => selectedDomain?.id && handleActionClick('delete')}
//           disk={false}
//           isEditDisabled={!selectedDomain?.id}
//           status={selectedDomain?.status}
//         />
//       }
//       <span>id = {selectedDomain?.id || ''}</span>

//       <DomainTable
//         columns={columns}
//         domains={domains}
//         selectedDomain={selectedDomain}
//         setSelectedDomain={setSelectedDomain}
//       />

//       <DomainModals
//         isModalOpen={isModalOpen}
//         action={action}
//         onRequestClose={() => setIsModalOpen(false)}
//         selectedDomain={selectedDomain}
//         datacenterId={datacenterId}
//       />
//     </>
//   );
// };

// export default DomainDupl;
