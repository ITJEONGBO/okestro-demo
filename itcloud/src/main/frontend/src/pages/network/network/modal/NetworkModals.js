import React, { Suspense } from 'react';
import '../css/MNetwork.css';

const NetworkModals = ({ 
    isModalOpen, 
    action, 
    onRequestClose, 
    selectedNetwork,
    selectedNetworks
}) => {
  const NetworkModal = React.lazy(() => import('./NetworkModal'));
  const NetworkActionModal = React.lazy(() => import('./NetworkActionModal'));
  const DeleteModal = React.lazy(() => import('../../../../components/DeleteModal'));

  if (!isModalOpen || !action) return null;

  return (
    <Suspense>
      {action === 'create' || action === 'edit' ? (
        <NetworkModal
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
          data={selectedNetworks}
        />
      ) : (
         //가져오기
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
