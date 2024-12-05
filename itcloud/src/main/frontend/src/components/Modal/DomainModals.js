import React, { Suspense } from 'react';

const DomainModals = ({ isModalOpen, action, onRequestClose, selectedDomain }) => {
  const DomainModal = React.lazy(() => import('../Modal/DomainModal'));
  const DeleteModal = React.lazy(() => import('../Modal/DeleteModal'));
  const DomainActionModal = React.lazy(() => import('../Modal/DomainActionModal'));

  if (!isModalOpen || !action) return null;

  return (
    <>
    <Suspense>
      {action === 'create' || action === 'edit' || action === 'import'? (
        <DomainModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          editMode={action === 'edit'}
          action={action}
          domainId={selectedDomain?.id || null}
        />
      ) : action === 'delete' ? (
        <DeleteModal
          isOpen={isModalOpen}
          type="Storage Domain"
          onRequestClose={onRequestClose}
          contentLabel="스토리지 도메인"
          data={selectedDomain}
        />
      ) : (
        <DomainActionModal
          isOpen={isModalOpen}
          action={action}
          onRequestClose={onRequestClose}
          contentLabel={getContentLabel(action)}
          data={selectedDomain}
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
    case 'stop': return '중지';
    default: return '';
  }
};

export default DomainModals;
