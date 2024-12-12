import React, { Suspense } from 'react';
import NetworkNewModal from './NetworkNewModal';

const NetworkModals = ({ 
    isModalOpen, 
    action, 
    onRequestClose, 
    selectedNetwork 
}) => {
  const NetworkNewModal = React.lazy(() => import('../Modal/NetworkNewModal'));
  const DeleteModal = React.lazy(() => import('../Modal/DeleteModal'));
  const NetworkActionModal = React.lazy(() => import('../Modal/NetworkActionModal'));

  if (!isModalOpen || !action) return null;

  return (
    <Suspense>
      {action === 'create' || action === 'edit' ? (
        <NetworkNewModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          editMode={action === 'edit'}
          networkId={selectedNetwork?.id || null}
        />
      ) : action === 'delete' ? (
        <DeleteModal
          isOpen={isModalOpen}
          type="Network"
          onRequestClose={onRequestClose}
          contentLabel="네트워크"
          data={selectedNetwork}
        />
      ) : (
        <NetworkActionModal
          isOpen={isModalOpen}
          action={action}
          onRequestClose={onRequestClose}
          contentLabel={getContentLabel(action)}
          data={selectedNetwork}
        />
      )}
    </Suspense>
  );
};

const getContentLabel = (action) => {
  switch (action) {
    case 'import': return '가져오기';
    default: return '';
  }
};

export default NetworkModals;
