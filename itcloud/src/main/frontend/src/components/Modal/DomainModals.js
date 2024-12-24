import React, { Suspense } from 'react';

const DomainModals = ({ 
  isModalOpen, 
  action, 
  onRequestClose, 
  selectedDomain,
  datacenterId,
  selectedDomains
}) => {
  const DomainModal = React.lazy(() => import('../Modal/DomainModal'));
  const DomainDeleteModal = React.lazy(() => import('../Modal/DomainDeleteModal'));
  const DomainActionModal = React.lazy(() => import('../Modal/DomainActionModal'));

  if (!isModalOpen || !action) return null;

  return (
    <>
    <Suspense>
      {/* 편집일때는 설명, 그 한도제한? 그거만 할수 있음 */}
      { action === 'create' || action === 'imported'? (
        <DomainModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          action={action}
          // domainId={selectedDomain?.id || null}
        />
      ): action === 'edit' ? (
        <DomainModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          action={'edit'}
          domainId={selectedDomain?.id || null}
        />      
      ) : action === 'delete' ? (
        <DomainDeleteModal
          isOpen={isModalOpen}
          onRequestClose={onRequestClose}
          data={selectedDomains}
        />
      ) : (
        <DomainActionModal // 여러개도 되는지 확인
          isOpen={isModalOpen}
          action={action}
          onRequestClose={onRequestClose}
          contentLabel={getContentLabel(action)}
          data={selectedDomain}
          datacenterId={datacenterId}
        />
      )}
    </Suspense>

    </>
  );
};

const getContentLabel = (action) => {
  switch (action) {
    case 'activate': return '활성';
    case 'attach': return '연결';
    case 'detach': return '분리';
    case 'maintenance': return '유지보수';
    default: return '';
  }
};

export default DomainModals;
