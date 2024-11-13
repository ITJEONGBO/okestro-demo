import React, { useState, useEffect, Suspense } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/DataCenter.css';
import TablesOuter from '../../table/TablesOuter';
import TableInfo from '../../table/TableInfo';
import { useClustersFromDataCenter } from '../../../api/RQHook';
import ClusterActionButtons from '../../button/ClusterActionButtons';

const ClusterModal = React.lazy(() => import('../../Modal/ClusterModal'));
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

const DataCenterClusters = ({datacenterId}) => {
  const {
    data: clusters,
    status: clustersStatus,
    isLoading: isClustersLoading,
    isError: isClustersError,
  } = useClustersFromDataCenter(datacenterId, (e) => ({ 
    ...e 
  }));
  
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
      <ClusterActionButtons
        onCreate={() => toggleModal('create', true)}
        onEdit={() => selectedCluster?.id && toggleModal('edit', true)}
        onDelete={() => selectedCluster?.id && toggleModal('delete', true)}
        isEditDisabled={!selectedCluster?.id}
      />

      <span>id = {selectedCluster?.id || ''}</span>
      <TablesOuter
        columns={TableInfo.CLUSTERS_FROM_DATACENTER}
        data={clusters}
        shouldHighlight1stCol={true}
        onRowClick={(row) => {setSelectedCluster(row)}}
        clickableColumnIndex={[0]} 
        onClickableColumnClick={(row) => {handleNameClick(row.id);}}
      />

      {/* 모달 컴포넌트를 사용할 때만 로딩 */}
      <Suspense>
        {(modals.create || (modals.edit && selectedCluster)) && (
          <ClusterModal 
            isOpen={modals.create || modals.edit}
            onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
            editMode={modals.edit}
            cId={selectedCluster?.id || null}
            datacenterId={datacenterId}
          />
        )}
        {modals.delete && selectedCluster && (
          <DeleteModal
            isOpen={modals.delete}
            type={'Cluster'}
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'클러스터'}
            data={selectedCluster}
          />
        )}
      </Suspense>
    </>
  );
};

export default DataCenterClusters;