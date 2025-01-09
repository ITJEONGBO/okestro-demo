import React, { Suspense, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ClusterActionButtons from './button/ClusterActionButtons';
import TablesOuter from '../../../components/table/TablesOuter';
import TableRowClick from '../../../components/table/TableRowClick';

const ClusterModal = React.lazy(() => import('./modal/ClusterModal'));
const ClusterDeleteModal = React.lazy(() => import('./modal/ClusterDeleteModal'));

const ClusterDupl = ({ clusters = [], columns, datacenterId }) => {
  const navigate = useNavigate();

  const [activeModal, setActiveModal] = useState(null);
  const [selectedClusters, setSelectedClusters] = useState([]);
  const selectedIds = (Array.isArray(selectedClusters) ? selectedClusters : []).map(cluster => cluster.id).join(', ');

  const handleNameClick = (id) => navigate(`/computing/clusters/${id}`);

  const openModal = (action) => setActiveModal(action);
  const closeModal = () => setActiveModal(null);

  const status = selectedClusters.length === 0 ? 'none': selectedClusters.length === 1 ? 'single': 'multiple';

  const renderModals = () => (
    <>
      <Suspense fallback={<div>Loading...</div>}>
        {activeModal === 'create' && (
          <ClusterModal            
            cId={Array.isArray(selectedClusters) && selectedClusters.length === 1 ? selectedClusters[0].id : null}
            dcId={datacenterId}
            onClose={closeModal}
          />
        )}
        {activeModal === 'edit' && (
          <ClusterModal
            editMode
            cId={Array.isArray(selectedClusters) && selectedClusters.length === 1 ? selectedClusters[0].id : null}
            dcId={datacenterId}
            onClose={closeModal}
          />
        )}
        {activeModal === 'delete' && (
          <ClusterDeleteModal
            data={selectedClusters}
            onClose={closeModal}
          />
        )}
      </Suspense>
    </>
  );
  
  return (
    <>
      <ClusterActionButtons
        openModal={openModal}
        status={status}
      />
      <span>ID: {selectedIds}</span>

      <TablesOuter
        columns={columns}
        data={clusters.map((cluster) => ({
          ...cluster,
          hostCnt: cluster?.hostSize?.allCnt,
          vmCnt: cluster?.vmSize.allCnt,
          dataCenter: (
            <TableRowClick type="datacenter" id={cluster.dataCenterVo.id}>
              {cluster.dataCenterVo.name}
            </TableRowClick>
          ),
        }))}
        shouldHighlight1stCol={true}
        onRowClick={(selectedRows) => setSelectedClusters(selectedRows)}
        clickableColumnIndex={[0]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
        multiSelect={true} // 다중 선택 활성화
      />
 
      {/* <ClusterTable
        columns={columns}
        clusters={clusters}
        setSelectedClusters={(selected) => {
          if (Array.isArray(selected)) setSelectedClusters(selected);
        }}
      /> */}

      {/* 클러스터 모달창 */}
      { renderModals() }
    </>
  );
};

export default ClusterDupl;
