import React, { Suspense } from 'react';

const DiskModals = ({ 
  isModalOpen, 
  action, 
  onRequestClose, 
  selectedDisk 
}) => {
  const DiskModal = React.lazy(() => import('./DiskModal.js'));
  const DeleteModal = React.lazy(() => import('../Modal/DeleteModal'));
  const DiskActionModal = React.lazy(() => import('../Modal/DiskActionModal'));
  const DiskUploadModal = React.lazy(() => import('./DiskUploadModal'));

  if (!isModalOpen || !action) return null;

  return (
    <Suspense>
      {action === 'create' || action === 'edit' ? (
        <DiskModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          editMode={action === 'edit'}
          diskId={selectedDisk?.id || null}
          // type='vm'
        />
      ) : action === 'upload' ? (
        <DiskUploadModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
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
        // 이동, 복사
        <DiskActionModal
          isOpen={isModalOpen}
          action={action}
          onRequestClose={onRequestClose}
          contentLabel={getContentLabel(action)}
          data={selectedDisk}
        />
      )}
    </Suspense>
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
