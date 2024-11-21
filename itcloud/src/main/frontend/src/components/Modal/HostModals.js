import React, { Suspense } from 'react';

const HostModals = ({ isModalOpen, action, onRequestClose, selectedHost }) => {
  const HostModal = React.lazy(() => import('../Modal/HostModal'));
  const DeleteModal = React.lazy(() => import('../Modal/DeleteModal'));
  const HostActionModal = React.lazy(() => import('../Modal/HostActionModal'));

  if (!isModalOpen || !action) return null;

  return (
    <Suspense>
      {action === 'create' || action === 'edit' ? (
        <HostModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          editMode={action === 'edit'}
          hId={selectedHost?.id || null}
        />
      ) : action === 'delete' ? (
        <DeleteModal
          isOpen={isModalOpen}
          type="Host"
          onRequestClose={onRequestClose}
          contentLabel="호스트"
          data={selectedHost}
        />
      ) : (
        <HostActionModal
          isOpen={isModalOpen}
          action={action}
          onRequestClose={onRequestClose}
          contentLabel={getContentLabel(action)}
          data={selectedHost}
        />
      )}
    </Suspense>
  );
};

const getContentLabel = (action) => {
  switch (action) {
    case 'deactivate': return '유지보수';
    case 'activate': return '활성';
    case 'restart': return '재시작';
    case 'stop': return '중지';
    case 'reinstall': return '다시 설치';
    case 'register': return '인증서 등록';
    case 'haon': return 'HA 활성화';
    case 'haoff': return 'HA 비활성화';
    default: return '';
  }
};

export default HostModals;