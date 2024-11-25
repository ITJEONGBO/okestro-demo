import React, { Suspense } from 'react';

const DiskModals = ({ isModalOpen, action, onRequestClose, selectedDisk }) => {
  const DiskUploadModal = React.lazy(() => import('./DiskUploadModal'));
  const DiskModal = React.lazy(() => import('./DiskModal.js'));
  const DeleteModal = React.lazy(() => import('../Modal/DeleteModal'));
  const DiskActionModal = React.lazy(() => import('../Modal/DiskActionModal'));

  if (!isModalOpen || !action) return null;

  return (
    <>
    <Suspense>
      {action === 'create' || action === 'edit' ? (
        <DiskModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          editMode={action === 'edit'}
          diskId={selectedDisk?.id || null}
        />
      ) : action === 'upload' ? (
        <DiskUploadModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          editMode={action === 'edit'}
          diskId={selectedDisk?.id || null}
        />
      ) : action === 'delete' ? (
        <DeleteModal
          isOpen={isModalOpen}
          type="Disk"
          onRequestClose={onRequestClose}
          contentLabel="디스크"
          data={selectedDisk}
        />
      ) : (
        <DiskActionModal
          isOpen={isModalOpen}
          action={action}
          onRequestClose={onRequestClose}
          contentLabel={getContentLabel(action)}
          data={selectedDisk}
        />
      )}
    </Suspense>

    </>
  );
};

const getContentLabel = (action) => {
  switch (action) {
    case 'activate': return '활성';
    case 'maintenance': return '유지보수';
    
    default: return '';
  }
};

export default DiskModals;
