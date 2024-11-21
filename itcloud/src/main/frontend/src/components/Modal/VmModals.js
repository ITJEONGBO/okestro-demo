import React, { Suspense } from 'react';
// import VmExportOVAModal from './VmExportOVAModal';

const VmModals = ({ isModalOpen, action, onRequestClose, selectedVm }) => {
  const VmModal = React.lazy(() => import('../Modal/VmModal'));
  const DeleteModal = React.lazy(() => import('../Modal/DeleteModal'));
  const VmActionModal = React.lazy(() => import('../Modal/VmActionModal'));

  if (!isModalOpen || !action) return null;

  return (
    <Suspense>
      {action === 'create' || action === 'edit' ? (
        <VmModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          editMode={action === 'edit'}
          hId={selectedVm?.id || null}
        />
      ) : action === 'delete' ? (
        <DeleteModal
          isOpen={isModalOpen}
          type="VM"
          onRequestClose={onRequestClose}
          contentLabel="가상머신"
          data={selectedVm}
        />
      // ) : action === 'exportova' ? (
      //   <VmExportOVAModal
      //     isOpen={isModalOpen}
      //     onRequestClose={onRequestClose}
      //     selectedVm={selectedVm}
      //   />
      ) : (
        <VmActionModal
          isOpen={isModalOpen}
          action={action}
          onRequestClose={onRequestClose}
          contentLabel={getContentLabel(action)}
          data={selectedVm}
        />
      )}
    </Suspense>
  );
};

const getContentLabel = (action) => {
  switch (action) {
    case 'start': return '실행';
    case 'pause': return '일시중지';
    case 'powerOff': return '전원끔';
    case 'shutdown': return '종료';
    case 'reboot': return '재시작';
    case 'reset': return '재설정';
    case 'exportova': return 'OVA로 내보내기';

    default: return '';
  }
};

export default VmModals;
