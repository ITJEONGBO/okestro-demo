import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import VmActionButtons from '../button/VmActionButtons';
import VmTable from '../table/VmTable';
import VmModals from '../Modal/VmModals';

const VmDupl = ({
  vms = [], 
  columns =[], 
  type,
  onFetchVms, 
  status
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
  const [action, setAction] = useState(null); // 현재 동작
  const [selectedVms, setSelectedVms] = useState([]); // 선택된 VM
  const navigate = useNavigate();

  const handleActionClick = (actionType) => {
    if (actionType === 'templates') {
      navigate('/computing/vms/templates');
    } else {
      setAction(actionType); // 동작 설정
      setIsModalOpen(true); // 모달 열기
    }
  };


  const hasDownStatus = selectedVms.some((vm) => vm.status === 'DOWN');  // 선택된 VM 중 DOWN 상태가 하나라도 있는지 확인
  // const hasHostVo = selectedVms.some((vm) => vm.hostVo);// 호스트 유무확인

  const selectedIds = (Array.isArray(selectedVms) ? selectedVms : []).map((vm) => vm.id).join(', ');
  const hasHostVo = Array.isArray(selectedVms) && selectedVms.every((vm) => vm.hostVo?.id);


  // 데이터 유효성 검사
  if (!Array.isArray(vms) || !Array.isArray(columns)) {
    return <p>유효하지 않은 데이터입니다.</p>;
  }

  return (
    <>
     <div onClick={(e) => e.stopPropagation()}> 
      <VmActionButtons
        onCreate={() => handleActionClick('create')} 
        onEdit={() => selectedVms.length === 1 && handleActionClick('edit')} 
        onDelete={() => selectedVms.length > 0 && handleActionClick('delete')} 
        onConsole={() => selectedVms?.id && handleActionClick('console')} 
        onStart={() => selectedVms.length > 0 && handleActionClick('start')} //디스크가없으면 실행못함
        onPause={() => selectedVms.length > 0 && handleActionClick('pause')}  // 일시정지
        onStop={() => selectedVms.length > 0 && handleActionClick('stop')} // 종료
        onPowerOff={() => selectedVms.length > 0 && handleActionClick('powerOff')} // 전원끔
        onReboot={() => selectedVms.length > 0 && handleActionClick('reboot')}  // 재부팅
        onReset={() => selectedVms.length > 0 && handleActionClick('reset')}  // 재설정
        templates={() => handleActionClick('templates')} 
        snapshots={() => selectedVms.length > 0 && handleActionClick('snapshots')} 
        migration={() => selectedVms.length > 0 && handleActionClick('migration')} 
        onExport={() => handleActionClick('onExport')} 
        onCopy={() => selectedVms.length === 1 && handleActionClick('onCopy')} 
        addTemplate={() =>  selectedVms.length === 1 && handleActionClick('addTemplate')} 
        exportOva={() => selectedVms.length > 0 && handleActionClick('exportova')} 
        isEditDisabled={!Array.isArray(selectedVms) || selectedVms.length !== 1}
        isDeleteDisabled={selectedVms.length === 0} 
        isPauseDisabled={hasDownStatus} // DOWN 상태가 하나라도 있으면 비활성화
        isMigrationDisabled={!hasHostVo} 
        setSelectedVm={selectedVms}
      />
      <span>id = {selectedIds || ''}</span>  
      
      <VmTable
        columns={columns}
        vms={vms}
        setSelectedVms={(selected) => {
          if (Array.isArray(selected)) setSelectedVms(selected); // 유효한 선택만 반영
        }}
      />
      
      <VmModals
        isModalOpen={isModalOpen}
        action={action}
        onRequestClose={() => setIsModalOpen(false)}
        selectedVm={selectedVms.length > 0 ? selectedVms[0] : null} // 선택된 첫 번째 네트워크 전달
        selectedVms={selectedVms}
      />
      </div>
    </>
  );
};

export default VmDupl;


// import React, { useState } from 'react';
// import { useNavigate } from 'react-router-dom';
// import VmActionButtons from '../button/VmActionButtons';
// import VmTable from '../table/VmTable';
// import VmModals from '../Modal/VmModals';

// const VmDupl = ({
//   vms = [], 
//   columns, 
//   type,
//   onFetchVms, 
//   status
// }) => {
//   const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
//   const [action, setAction] = useState(null); // 현재 동작
//   const [selectedVm, setSelectedVm] = useState(null);
//   const navigate = useNavigate();

//   const handleActionClick = (actionType) => {
//     if (actionType === 'templates') {
//       navigate('/computing/vms/templates');
//     } else {
//       setAction(actionType); // 동작 설정
//       setIsModalOpen(true); // 모달 열기
//     }
//   };

//   return (
//     <>
//       <VmActionButtons
//         onCreate={() => handleActionClick('create')} 
//         onEdit={() => selectedVm?.id && handleActionClick('edit')} 
//         onDelete={() => selectedVm?.id && handleActionClick('delete')} 
//         onConsole={() => selectedVm?.id && handleActionClick('console')} 
//         onStart={() => selectedVm?.id && handleActionClick('start')} 
//         onPause={() => selectedVm?.id && handleActionClick('pause')}  // 일시정지
//         onStop={() => selectedVm?.id && handleActionClick('stop')} // 종료
//         onPowerOff={() => selectedVm?.id && handleActionClick('powerOff')} // 전원끔
//         onReboot={() => selectedVm?.id && handleActionClick('reboot')}  // 재부팅
//         onReset={() => selectedVm?.id && handleActionClick('reset')}  // 재설정
//         templates={() => handleActionClick('templates')} 
//         snapshots={() => selectedVm?.id && handleActionClick('snapshots')} 
//         migration={() => selectedVm?.id && handleActionClick('migration')} 
//         onExport={() => handleActionClick('onExport')} 
//         onCopy={() => selectedVm?.id && handleActionClick('onCopy')} 
//         addTemplate={() => selectedVm?.id && handleActionClick('addTemplate')} 
//         exportOva={() => selectedVm?.id && handleActionClick('exportova')} 
//         isEditDisabled={!selectedVm?.id} 
//         status={selectedVm?.status} 
//         isMigrationDisabled={!selectedVm?.hostVo?.id} // 호스트 ID가 없으면 비활성화
//         setSelectedVm={setSelectedVm}
//       />
//       <span>id = {selectedVm?.id || ''}</span>  
      
//       <VmTable
//         columns={columns}
//         vms={vms}
//         selectedVm={selectedVm}
//         setSelectedVm={setSelectedVm}
//       />
      
//       <VmModals
//         isModalOpen={isModalOpen}
//         action={action}
//         onRequestClose={() => setIsModalOpen(false)}
//         selectedVm={selectedVm}
//       />
//     </>
//   );
// };

// export default VmDupl;