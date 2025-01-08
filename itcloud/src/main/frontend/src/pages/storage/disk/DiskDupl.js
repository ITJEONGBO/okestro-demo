import React, { useState } from 'react';
import DiskActionButtons from './button/DiskActionButtons';
import DiskTable from './DiskTable';
import DiskModals from './modal/DiskModals';

const DiskDupl = ({ 
  disks = [], // 기본값 설정
  columns = [], // 기본값 설정
  onFetchDisks, 
  status
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
  const [action, setAction] = useState(null); // 현재 동작
  const [selectedDisks, setSelectedDisks] = useState([]); // 다중 선택된 디스크

  const handleActionClick = (actionType) => {
    setAction(actionType); // 동작 설정
    setIsModalOpen(true); // 모달 열기
  };

  const selectedIds = (Array.isArray(selectedDisks) ? selectedDisks : []).map((disk) => disk.id).join(', ');

  // 데이터 유효성 검사
  if (!Array.isArray(disks) || !Array.isArray(columns)) {
    return <p>유효하지 않은 데이터입니다.</p>;
  }

  return (
    <div onClick={(e) => e.stopPropagation()}> {/* 테이블 외부 클릭 방지 */}
      <DiskActionButtons
        onCreate={() => handleActionClick('create')}
        onEdit={() => selectedDisks.length === 1 && handleActionClick('edit')}
        onDelete={() => selectedDisks.length > 0 && handleActionClick('delete')}
        onMove={() => selectedDisks.length > 0 && handleActionClick('move')}
        onCopy={() => selectedDisks.length > 0 && handleActionClick('copy')}
        onUpload={() => handleActionClick('upload')}
        isEditDisabled={!Array.isArray(selectedDisks) || selectedDisks.length !== 1} // 편집은 단일 선택만 허용
        isDeleteDisabled={selectedDisks.length === 0} // 삭제 버튼 조건
        status={selectedDisks[0]?.status}
      />
      <span>선택된 디스크 ID: {selectedIds || '선택된 항목이 없습니다.'}</span>

      <DiskTable
        columns={columns}
        disks={disks}
        setSelectedDisks={(selected) => {
          if (Array.isArray(selected)) setSelectedDisks(selected); // 유효한 선택만 반영
        }}
      />

      <DiskModals
        isModalOpen={isModalOpen}
        action={action}
        onRequestClose={() => setIsModalOpen(false)}
        selectedDisk={selectedDisks.length > 0 ? selectedDisks[0] : null} // 선택된 첫 번째 디스크 전달
        selectedDisks={selectedDisks}
      />
    </div>
  );
};

export default DiskDupl;

// import React, { useState } from 'react';
// import DiskActionButtons from '../button/DiskActionButtons';
// import DiskTable from '../table/DiskTable';
// import DiskModals from '../Modal/DiskModals';

// const DiskDupl = ({ 
//   disks, 
//   columns,
//   onFetchDisks, 
//   status
// }) => {
//   const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
//   const [action, setAction] = useState(null); // 현재 동작
//   const [selectedDisk, setSelectedDisk] = useState(null);

//   const handleActionClick = (actionType) => {
//     setAction(actionType); // 동작 설정
//     setIsModalOpen(true); // 모달 열기
//   };

//   return (
//     <>
//       <DiskActionButtons
//         onCreate={() => handleActionClick('create')}
//         onEdit={() => selectedDisk?.id && handleActionClick('edit')}
//         onDelete={() => selectedDisk?.id && handleActionClick('delete')}
//         onMove={() => selectedDisk?.id && handleActionClick('move')}
//         onCopy={() => selectedDisk?.id && handleActionClick('copy')}
//         onUpload={() => handleActionClick('upload')}
//         isEditDisabled={!selectedDisk?.id}
//         status={selectedDisk?.status}
//       />
//       <span>id = {selectedDisk?.id || ''}</span>

//       <DiskTable
//         columns={columns}
//         disks={disks}
//         selectedDisk={selectedDisk}
//         setSelectedDisk={setSelectedDisk}
//       />

//       <DiskModals
//         isModalOpen={isModalOpen}
//         action={action}
//         onRequestClose={() => setIsModalOpen(false)}
//         selectedDisk={selectedDisk}
//       />
//     </>
//   );
// };

// export default DiskDupl;
