import React, { Suspense } from 'react';

const ClusterModals = ({ 
  isModalOpen, 
  action, 
  onRequestClose, 
  selectedCluster,
  datacenterId
}) => {
  const ClusterModal = React.lazy(() => import('../Modal/ClusterModal'));
  const DeleteModal = React.lazy(() => import('../Modal/DeleteModal'));

  if (!isModalOpen || !action) return null;

  return (
    <Suspense>
      {(action === 'create' || action === 'edit') && (
        <ClusterModal
            isOpen={isModalOpen}
            onRequestClose={onRequestClose}
            editMode={action === 'edit'}
            cId={selectedCluster?.id || null}
            datacenterId={datacenterId}
        />
      )}
      {action === 'delete' && (
        <DeleteModal
            isOpen={isModalOpen}
            type="Cluster"
            onRequestClose={onRequestClose}
            contentLabel="클러스터"
            data={selectedCluster}
        />
      )}
    </Suspense>
  );
};


export default ClusterModals;
