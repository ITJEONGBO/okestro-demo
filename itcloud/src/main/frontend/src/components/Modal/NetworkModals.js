import React, { Suspense } from 'react';

const NetworkModals = ({ 
    isModalOpen, 
    action, 
    onRequestClose, 
    selectedNetwork 
}) => {
  const NetworkModal = React.lazy(() => import('../Modal/NetworkModal'));
  const DeleteModal = React.lazy(() => import('../Modal/DeleteModal'));
  const NetworkActionModal = React.lazy(() => import('../Modal/NetworkActionModal'));

  if (!isModalOpen || !action) return null;

  return (
    <Suspense>
      {action === 'create' || action === 'edit' ? (
        <NetworkModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          editMode={action === 'edit'}
          hId={selectedNetwork?.id || null}
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