import React, { Suspense } from 'react';

const VnicProfileModals = ({ 
  isModalOpen, 
  action, 
  onRequestClose, 
  selectedVnicProfile,
  selectedVnicProfiles,
  networkId
}) => {
  const VnicProfileModal = React.lazy(() => import('./VnicProfileModal.js'));
  const DeleteModal = React.lazy(() => import('../Modal/DeleteModal'));

  if (!isModalOpen || !action) return null;

  return (
    <>
      <Suspense>
        {action === 'create' || action === 'edit' ? (
          <VnicProfileModal
            isOpen={isModalOpen}
            onRequestClose={onRequestClose}
            editMode={action === 'edit'}
            vnicProfileId={selectedVnicProfile?.id || null}
            networkId={networkId}
          />
        ) : (
          <DeleteModal
            isOpen={isModalOpen}
            type="vnicProfile"
            onRequestClose={onRequestClose}
            contentLabel="vNIC 프로파일"
            data={selectedVnicProfiles}
            networkId={networkId}
          />
        )}
      </Suspense>
    </>
  );
};

export default VnicProfileModals;
