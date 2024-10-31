import React, { useEffect, useState, Suspense } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/Computing.css';
import TablesOuter from '../../table/TablesOuter';
import TableInfo from '../../table/TableInfo';
import { useAllClusters } from '../../../api/RQHook';

const ClusterModal = React.lazy(() => import('../../Modal/ClusterModal'));
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

const Clusters = () => {
  const navigate = useNavigate();
  
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
      <div className="header_right_btns">
        <button onClick={() => toggleModal('create', true)}>새로 만들기</button>
        <button onClick={() => selectedCluster?.id && toggleModal('edit', true)} disabled={!selectedCluster?.id}>편집</button>
        <button onClick={() => selectedCluster?.id && toggleModal('delete', true)} disabled={!selectedCluster?.id}>제거</button>
      </div>
      <span>id = {selectedCluster?.id || ''}</span>

      <TablesOuter
        columns={TableInfo.CLUSTERS} 
        data={clusters} 
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedCluster(row)}
        clickableColumnIndex={[0]} // "이름" 열의 인덱스 설정
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />

      {/* 모달 컴포넌트를 사용할 때만 로딩 */}
      <Suspense>
        {(modals.create || (modals.edit && selectedCluster)) && (
          <ClusterModal 
            isOpen={modals.create || modals.edit}
            onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
            editMode={modals.edit}
            cId={selectedCluster?.id || null}
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

export default Clusters;
