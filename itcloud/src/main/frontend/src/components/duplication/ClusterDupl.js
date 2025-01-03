import React, { useState } from 'react';
import ClusterActionButtons from '../button/ClusterActionButtons';
import ClusterTable from '../table/ClusterTable';
import ClusterModals from '../Modal/ClusterModals';
import AllActionButton from '../button/AllActionButton';

const ClusterDupl = ({
  clusters,
  columns,
  datacenterId
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false); 
  const [action, setAction] = useState(null);
  const [selectedClusters, setSelectedClusters] = useState([]);

  const handleActionClick = (actionType) => {
    setAction(actionType); 
    setIsModalOpen(true); 
  };
  const selectedIds = (Array.isArray(selectedClusters) ? selectedClusters : []).map(cluster => cluster.id).join(', ');

  return (
  <>
    <ClusterActionButtons
      onCreate={() => handleActionClick('create')}
      onEdit={() => selectedClusters.length === 1 && handleActionClick('edit')}
      onDelete={() => selectedClusters.length === 1 && handleActionClick('delete')}
      isEditDisabled={selectedClusters.length !== 1}
    />
    <span>선택된 클러스터 ID: {selectedIds || '선택된 항목이 없습니다.'}</span>

    <ClusterTable
      columns={columns}
      clusters={clusters}
      selectedClusters={selectedClusters}
      setSelectedClusters={(selected) => {
        if (Array.isArray(selected)) setSelectedClusters(selected);
      }}
    />

    <ClusterModals
      isModalOpen={isModalOpen}
      action={action}
      onRequestClose={() => setIsModalOpen(false)}
      selectedCluster={selectedClusters.length > 0 ? selectedClusters[0] : null}
      selectedClusters={selectedClusters}
      datacenterId={datacenterId}
    />
  </>
  );
};

export default ClusterDupl;
