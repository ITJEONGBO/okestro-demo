import React, { useState, Suspense } from 'react';
import { useNavigate } from 'react-router-dom';
import TableColumnsInfo from '../../components/table/TableColumnsInfo';
import { useAllDataCenters } from '../../api/RQHook';
import TablesOuter from '../../components/table/TablesOuter';
import { renderDatacenterStatusIcon } from '../../utils/format';
import DataCenterActionButtons from '../../pages/computing/datacenter/button/DataCenterActionButtons';

const DataCenterModal = React.lazy(() => import('../../pages/computing/datacenter/modal/DataCenterModal'));
const DataCenterDeleteModal = React.lazy(() => import('../computing/datacenter/modal/DataCenterDeleteModal'));

const DataCenters = () => {
  const navigate = useNavigate();
  const {
    data: datacenters = [],
  } = useAllDataCenters((dc) => ({
    ...dc,
    icon: renderDatacenterStatusIcon(dc?.status),
    status: dc?.status === 'UNINITIALIZED' ? '초기화되지 않음' : 'UP',
    storageType: dc?.storageType ? '로컬' : '공유됨',
  }));
  
  const [activeModal, setActiveModal] = useState(null);
  const [selectedDataCenters, setSelectedDataCenters] = useState([]);
  const selectedIds = (Array.isArray(selectedDataCenters) ? selectedDataCenters : [])
  .map(dc => dc.id)
  .join(', ');


  const handleNameClick = (id) => navigate(`/computing/datacenters/${id}/clusters`);

  const openModal = (action) => setActiveModal(action);
  const closeModal = () => setActiveModal(null);

  //버튼 활성화 조건
  const status = selectedDataCenters.length === 0 ? 'none'
  : selectedDataCenters.length === 1 ? 'single'
  : 'multiple';

  const renderModals = () => (
    <>
      <Suspense fallback={<div>Loading...</div>}>
        {activeModal === 'create' && (
          <DataCenterModal            
            dcId={Array.isArray(selectedDataCenters) && selectedDataCenters.length === 1 ? selectedDataCenters[0].id : null}
            onClose={closeModal}
          />
        )}
        {activeModal === 'edit' && (
          <DataCenterModal
            editMode
            dcId={Array.isArray(selectedDataCenters) && selectedDataCenters.length === 1 ? selectedDataCenters[0].id : null}
            onClose={closeModal}
          />
        )}
        {activeModal === 'delete' && (
          <DataCenterDeleteModal
            data={selectedDataCenters}
            onClose={closeModal}
          />
        )}
      </Suspense>
    </>
  );
  

  return (
    <>
      <DataCenterActionButtons
        openModal={openModal}
        status={status}
      />
      <span>선택된 데이터센터 ID: {selectedIds || '선택된 항목이 없습니다.'}</span>


      <TablesOuter
        columns={TableColumnsInfo.DATACENTERS}
        data={datacenters}
        onRowClick={(selectedRows) => setSelectedDataCenters(selectedRows)}
        clickableColumnIndex={[1]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
        multiSelect={true}
      />

      {/* 데이터센터 모달창 */}
      {renderModals()}
    </>
  );
};

export default DataCenters;
