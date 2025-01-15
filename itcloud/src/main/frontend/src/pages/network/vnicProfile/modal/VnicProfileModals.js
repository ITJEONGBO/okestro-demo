import React, { Suspense } from 'react';

const VnicProfileModals = ({ 
  isModalOpen, 
  action, 
  onRequestClose, 
  selectedVnicProfile,
  selectedVnicProfiles,
}) => {
  const VnicProfileModal = React.lazy(() => import('./VnicProfileModal.js'));
  const DeleteModal = React.lazy(() => import('../../../../components/DeleteModal.js'));

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
          
          />
        ) : (
          <DeleteModal
            isOpen={isModalOpen}
            type="vnicProfile"
            onRequestClose={onRequestClose}
            contentLabel="vNIC 프로파일"
            data={selectedVnicProfiles}
          />
        )}
      </Suspense>
    </>
  );
};

export default VnicProfileModals;