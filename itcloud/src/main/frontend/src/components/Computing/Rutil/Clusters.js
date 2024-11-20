import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/Computing.css';
import { useAllClusters } from '../../../api/RQHook';
import TableInfo from '../../table/TableInfo';
import ClusterDupl from '../../duplication/ClusterDupl';

const Clusters = () => {
  const { 
    data: clusters, 
    status: clustersStatus,
    isRefetching: isClustersRefetching,
    refetch: refetchClusters, 
    isError: isClustersError, 
    error: clustersError, 
    isLoading: isClustersLoading,
  } = useAllClusters((e) => {
    return {
        ...e,
        hostCnt: e?.hostSize.allCnt,
        vmCnt: e?.vmSize.allCnt,
    }
  });

  const navigate = useNavigate();
  const [modals, setModals] = useState({ create: false, edit: false, delete: false });
  const [selectedCluster, setSelectedCluster] = useState(null);
  
  const toggleModal = (type, isOpen) => {
    setModals((prev) => ({ ...prev, [type]: isOpen }));
  };

  const handleNameClick = (id) => {
      navigate(`/computing/clusters/${id}`);
  };

  return (
    <>
      <ClusterDupl
        clusters={clusters}
        columns={TableInfo.CLUSTERS}
        onRowClick={setSelectedCluster}
        onCreate={() => toggleModal('create', true)}
        onEdit={() => selectedCluster?.id && toggleModal('edit', true)}
        onDelete={() => selectedCluster?.id && toggleModal('delete', true)}
        selectedCluster={selectedCluster}
        toggleModal={toggleModal}
        modals={modals}
        handleNameClick={handleNameClick}
      />
    </>
  );
};

export default Clusters;
