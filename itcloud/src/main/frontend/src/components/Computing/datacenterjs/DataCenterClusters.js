import React, { useState } from 'react';
import { useClustersFromDataCenter } from '../../../api/RQHook';
import TableInfo from '../../table/TableInfo';
import ClusterDupl from '../../duplication/ClusterDupl';

const DataCenterClusters = ({ datacenterId }) => {
  const { data: clusters } = useClustersFromDataCenter(datacenterId, (e) => ({ ...e }));

  const [modals, setModals] = useState({ create: false, edit: false, delete: false });
  const [selectedCluster, setSelectedCluster] = useState(null);

  const toggleModal = (type, isOpen) => {
    setModals((prev) => ({ ...prev, [type]: isOpen }));
  };
    
  return (
    <>
      <ClusterDupl
        clusters={clusters || []}
        columns={TableInfo.CLUSTERS_FROM_DATACENTER}
        onRowClick={setSelectedCluster}
        onCreate={() => toggleModal('create', true)}
        onEdit={() => selectedCluster?.id && toggleModal('edit', true)}
        onDelete={() => selectedCluster?.id && toggleModal('delete', true)}
        selectedCluster={selectedCluster}
        toggleModal={toggleModal}
        modals={modals}
        datacenterId={datacenterId}
      />
    </>
  );
};

export default DataCenterClusters;
