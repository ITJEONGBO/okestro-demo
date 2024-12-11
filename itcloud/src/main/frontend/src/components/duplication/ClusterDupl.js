import React, { useState } from 'react';
import ClusterActionButtons from '../button/ClusterActionButtons';
import ClusterTable from '../table/ClusterTable';
import ClusterModals from '../Modal/ClusterModals';

const ClusterDupl = ({
  clusters,
  columns,
  onFetchClusters,
  status,
  datacenterId
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태
  const [action, setAction] = useState(null); // 현재 동작
  const [selectedCluster, setSelectedCluster] = useState(null);
  
  const handleActionClick = (actionType) => {
    setAction(actionType); // 동작 설정
    setIsModalOpen(true); // 모달 열기
  };

  return (
  <>
    <ClusterActionButtons
      onCreate={() => handleActionClick('create')}
      onEdit={() => selectedCluster?.id && handleActionClick('edit')}
      onDelete={() => selectedCluster?.id && handleActionClick('delete')}
      isEditDisabled={!selectedCluster?.id}
    />
    <span>id = {selectedCluster?.id || ''}</span>

    <ClusterTable
      columns={columns}
      clusters={clusters}
      selectedCluster={selectedCluster}
      setSelectedCluster={setSelectedCluster}
    />

    <ClusterModals
      isModalOpen={isModalOpen}
      action={action}
      onRequestClose={() => setIsModalOpen(false)}
      selectedCluster={selectedCluster}
      datacenterId={datacenterId}
    />
  </>
  );
};

export default ClusterDupl;
