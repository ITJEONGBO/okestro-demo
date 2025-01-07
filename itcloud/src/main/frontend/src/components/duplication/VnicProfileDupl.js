import React, { useState } from 'react';
import VnicProfileActionButtons from '../button/VnicProfileActionButtons';
import VnicProfileTable from '../table/VnicProfileTable.js';
import VnicProfileModals from '../Modal/VnicProfileModals';

const VnicProfileDupl = ({ 
  vnicProfiles = [], 
  columns = [], 
  networkId,
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
  const [action, setAction] = useState(null); // 현재 동작
  const [selectedVnicProfiles, setSelectedVnicProfiles] = useState([]); // 다중 선택된 vNIC 프로파일

  const handleActionClick = (actionType) => {
    setAction(actionType); // 동작 설정
    setIsModalOpen(true); // 모달 열기
  };


const selectedIds = Array.isArray(selectedVnicProfiles)
  ? selectedVnicProfiles.map((nic) => nic.id || "알 수 없음").join(", ") // ID를 문자열로 변환
  : "선택된 항목이 없습니다.";



  return (
    <div onClick={(e) => e.stopPropagation()}> {/* 테이블 외부 클릭 방지 */}
      <VnicProfileActionButtons
        onCreate={() => handleActionClick('create')}
        onEdit={() => selectedVnicProfiles.length === 1 && handleActionClick('edit')}
        onDelete={() => selectedVnicProfiles.length > 0 && handleActionClick('delete')}
        isEditDisabled={!Array.isArray(selectedVnicProfiles) || selectedVnicProfiles.length !== 1} // 방어 코드 추가
        isDeleteDisabled={selectedVnicProfiles.length === 0} // 삭제 버튼 조건
        status={selectedVnicProfiles[0]?.status}
      />
      <span>선택된 vNIC 프로파일 ID: {selectedIds || '선택된 항목이 없습니다.'}</span>

      <VnicProfileTable
        columns={columns}
        vnicProfiles={vnicProfiles}
        setSelectedVnicProfiles={(selected) => {
          if (Array.isArray(selected)) setSelectedVnicProfiles(selected);
        }}
      />

      <VnicProfileModals
        isModalOpen={isModalOpen}
        action={action}
        onRequestClose={() => setIsModalOpen(false)}
        selectedVnicProfile={selectedVnicProfiles.length > 0 ? selectedVnicProfiles[0] : null} // 선택된 첫 번째 vNIC 프로파일 전달
        selectedVnicProfiles={selectedVnicProfiles}
        networkId={networkId}
        // nicId={selectedIds}
      />
    </div>
  );
};

export default VnicProfileDupl;

// import React, { useState } from 'react';
// import VnicProfileActionButtons from '../button/VnicProfileActionButtons';
// import VnicProfileTable from '../table/VnicProfileTable.js';
// import VnicProfileModals from '../Modal/VnicProfileModals';

// const VnicProfileDupl = ({ 
//   vnicProfiles, 
//   columns,
//   onFetchVnicProfiles, 
//   status,
//   networkId,
// }) => {
//   const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
//   const [action, setAction] = useState(null); // 현재 동작
//   const [selectedVnicProfile, setSelectedVnicProfile] = useState(null);

//   const handleActionClick = (actionType) => {
//     setAction(actionType); // 동작 설정
//     setIsModalOpen(true); // 모달 열기
//   };

//   return (
//     <>
//       <VnicProfileActionButtons
//         onCreate={() => handleActionClick('create')}
//         onEdit={() => selectedVnicProfile?.id && handleActionClick('edit')}
//         onDelete={() => selectedVnicProfile?.id && handleActionClick('delete')}
//         isEditDisabled={!selectedVnicProfile?.id}
//       />
//       <span>id = {selectedVnicProfile?.id || ''}</span>

//       <VnicProfileTable
//         columns={columns}
//         vnicProfiles={vnicProfiles}
//         selectedVnicProfile={selectedVnicProfile}
//         setSelectedVnicProfile={setSelectedVnicProfile}
//       />

//       <VnicProfileModals
//         isModalOpen={isModalOpen}
//         action={action}
//         onRequestClose={() => setIsModalOpen(false)}
//         selectedVnicProfile={selectedVnicProfile}
//         networkId={networkId}
//       />
//     </>
//   );
// };

// export default VnicProfileDupl;
