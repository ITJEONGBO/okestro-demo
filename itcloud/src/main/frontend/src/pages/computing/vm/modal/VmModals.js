import React from 'react';
import VmModal from './VmModal';
import VmDeleteModal from './VmDeleteModal';
import VmActionModal from './VmActionModal';
import VmSnapshotModal from './VmSnapshotModal';

const VmModals = ({ activeModal, vmId, selectedVms = [], onClose }) => {
  const modals = {
    create: <VmModal isOpen={activeModal === 'create'} onClose={onClose} />,
    edit: (
      <VmModal
        editMode
        isOpen={activeModal === 'edit'}
        vmId={vmId || selectedVms[0]?.id} // vmId가 있으면 사용, 없으면 selectedVms에서 가져옴
        onClose={onClose}
      />
    ),
    delete: (
      <VmDeleteModal
        isOpen={activeModal === 'delete'}
        data={selectedVms}
        onClose={onClose}
      />
    ),
    snapshot: (
      <VmSnapshotModal
        isOpen={activeModal === 'snapshot'}
        vmId={vmId || selectedVms[0]?.id} // vmId를 우선 사용
        onClose={onClose}
      />
    ),
    action: (
      <VmActionModal
        isOpen={['start', 'pause', 'reboot', 'reset', 'stop', 'powerOff'].includes(activeModal)}
        action={activeModal}
        data={selectedVms}
        onClose={onClose}
      />
    ),
  };

  return (
    <>
      {Object.keys(modals).map((key) => (
        <React.Fragment key={key}>{modals[key]}</React.Fragment>
      ))}
    </>
  );
};

export default VmModals;

// import React, { Suspense,useState } from 'react';
// import VmAddTemplateModal from './VmAddTemplateModal';
// import VmMigrationModal from './VmMigrationModal';
// import VmSnapshotAddModal from './VmSnapshotaddModal';
// import { useDisksFromVM } from '../../../../api/RQHook';
// // import VmExportOVAModal from './VmExportOVAModal';

// const VmModals = ({ isModalOpen, action, onRequestClose, selectedVm,selectedVms = []}) => {
//   const VmModal = React.lazy(() => import('./VmModal'));
//   const VmDeleteModal = React.lazy(() => import('./VmDeleteModal'));
//   const VmActionModal = React.lazy(() => import('./VmActionModal'));
//   const VmonExportModal = React.lazy(() => import('./VmonExportModal'));
//   const VmExportOVAModal = React.lazy(() => import('./VmExportOVAModal'));

//   const [bootable, setBootable] = useState(true);
//   const { data: disks } = useDisksFromVM(selectedVm?.id, (e) => ({
//     ...e,
//     snapshot_check: (
//       <input
//         type="checkbox"
//         name="diskSelection"
//         onChange={(e) => setBootable(e.target.checked)} 
//       />
//     ),
//     alias: e?.diskImageVo?.alias,
//     description: e?.diskImageVo?.description,
//   }));


//   if (!isModalOpen || !action) return null;
//   console.log('VM 정보dddd:', selectedVm);
//   return (
//     <Suspense>
//       {action === 'create' || action === 'edit'  || action === 'copy' ? (
//         <VmModal
//           isOpen={isModalOpen}
//           onRequestClose={onRequestClose}
//           editMode={action === 'edit'}
//           vmId={selectedVm?.id}
//           selectedVm={selectedVm}
//         />
//       ) : action === 'snapshots' ? ( //스냅샷
//         <VmSnapshotAddModal
//           isOpen={isModalOpen}
//           onRequestClose={onRequestClose}
//           snapshotData={selectedVm.snapshotData}
//           vmId={selectedVm.id}
    
//         />
//       ): action === 'delete' ? ( //삭제
//         <VmDeleteModal
//           isOpen={isModalOpen}
//           onRequestClose={onRequestClose}
//           data={selectedVms}
//         />
//       ):  action === 'migration' ? ( // 마이그레이션
//         <VmMigrationModal
//           isOpen={isModalOpen}
//           onRequestClose={onRequestClose}
//           selectedVms={selectedVms}
//         />
//       ) : action === 'onExport' ? ( // 가져오기
//         <VmonExportModal
//           isOpen={isModalOpen}
//           onRequestClose={onRequestClose}
//           selectedVm={selectedVm}
//         />
//       ):  action === 'onCopy' ? ( // 가상머신복제(복제버전따로만들어야됨)
//         <VmModal
//           isOpen={isModalOpen}
//           onRequestClose={onRequestClose}
//           editMode={action === 'edit'}
//           hId={selectedVm?.id || null}
//         />
//       ) :  action === 'addTemplate' ? ( // 템플릿생성
//         <VmAddTemplateModal
//           isOpen={isModalOpen}
//           onRequestClose={onRequestClose}
//           vmId={selectedVm?.id || null}
//           selectedVm={selectedVm}
//         />
//       ):  action === 'exportova' ? ( // ova내보내기
//         <VmExportOVAModal
//           isOpen={isModalOpen}
//           onRequestClose={onRequestClose}
//           selectedVms={selectedVms}
//         />
//       ) :(
//         <VmActionModal
//           isOpen={isModalOpen}
//           action={action}
//           onRequestClose={onRequestClose}
//           contentLabel={getContentLabel(action)}
//           data={selectedVms.length === 1 ? selectedVms[0] : selectedVms}
//         />
//       )}
//     </Suspense>
//   );
// };

// const getContentLabel = (action) => {
//   switch (action) {
//     case 'start': return '실행';
//     case 'pause': return '일시중지';
//     case 'stop': return '종료';
//     case 'powerOff': return '전원끔';
//     case 'reboot': return '재부팅';
//     case 'reset': return '재설정';
//     case 'migration': return '마이그레이션';
//     case 'exportova': return 'OVA로 내보내기';
//     case 'onExport': return '가져오기';
//     case 'onCopy': return '가상머신 복제';
//     case 'addTemplate': return '템플릿 생성';

//     default: return '';
//   }
// };

// export default VmModals;
