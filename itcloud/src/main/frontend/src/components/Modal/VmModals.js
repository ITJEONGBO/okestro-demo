import React, { Suspense } from 'react';
import VmAddTemplateModal from './VmAddTemplateModal';
import VmMigrationModal from './VmMigrationModal';
import VmDeleteModal from './VmDeleteModal';
import VmSnapshotAddModal from './VmSnapshotaddModal';
// import VmExportOVAModal from './VmExportOVAModal';

const VmModals = ({ isModalOpen, action, onRequestClose, selectedVm,selectedVms }) => {
  const VmModal = React.lazy(() => import('../Modal/VmModal'));
  const VmDeleteModal = React.lazy(() => import('./VmDeleteModal'));
  const VmActionModal = React.lazy(() => import('../Modal/VmActionModal'));
  const VmonExportModal = React.lazy(() => import('../Modal/VmonExportModal'));
  const VmExportOVAModal = React.lazy(() => import('../Modal/VmExportOVAModal'));

  if (!isModalOpen || !action) return null;

  return (
    <Suspense>
      {action === 'create' || action === 'edit' ? (
        <VmModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          editMode={action === 'edit'}
          vmId={selectedVm?.id}
          selectedVm={selectedVm}
        />
      ) : action === 'snapshots' ? ( //스냅샷(수정필요)
        <VmSnapshotAddModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          snapshotData={selectedVm.snapshotData}
          vmId={selectedVm.id}
        />
      ): action === 'delete' ? ( //삭제
        <VmDeleteModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          data={selectedVms}
        />
      ):  action === 'migration' ? ( // 마이그레이션
        <VmMigrationModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          selectedVms={selectedVms}
        />
      ) : action === 'onExport' ? ( // 가져오기
        <VmonExportModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          selectedVm={selectedVm}
        />
      ):  action === 'onCopy' ? ( // 가상머신복제(복제버전따로만들어야됨)
        <VmModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          editMode={action === 'edit'}
          hId={selectedVm?.id || null}
        />
      ) :  action === 'addTemplate' ? ( // 템플릿생성
        <VmAddTemplateModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          vmId={selectedVm?.id || null}
          selectedVm={selectedVm}
        />
      ):  action === 'exportova' ? ( // ova내보내기
        <VmExportOVAModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          selectedVms={selectedVms}
        />
      ) :(
        <VmActionModal
          isOpen={isModalOpen}
          action={action}
          onRequestClose={onRequestClose}
          contentLabel={getContentLabel(action)}
          data={selectedVms.length === 1 ? selectedVms[0] : selectedVms}
        />
      )}
    </Suspense>
  );
};

const getContentLabel = (action) => {
  switch (action) {
    case 'start': return '실행';
    case 'pause': return '일시중지';
    case 'stop': return '종료';
    case 'powerOff': return '전원끔';
    case 'reboot': return '재부팅';
    case 'reset': return '재설정';
    case 'migration': return '마이그레이션';
    case 'exportova': return 'OVA로 내보내기';
    case 'onExport': return '가져오기';
    case 'onCopy': return '가상머신 복제';
    case 'addTemplate': return '템플릿 생성';

    default: return '';
  }
};

export default VmModals;
