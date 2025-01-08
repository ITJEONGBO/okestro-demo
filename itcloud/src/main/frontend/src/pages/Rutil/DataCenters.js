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
  const {
    data: datacenters = [],
  } = useAllDataCenters((dc) => ({
    ...dc,
    icon: renderDatacenterStatusIcon(dc?.status),
    status: dc?.status === 'UNINITIALIZED' ? '초기화되지 않음' : 'UP',
    storageType: dc?.storageType ? '로컬' : '공유됨',
  }));
  const navigate = useNavigate();
  
  const [activeModal, setActiveModal] = useState(null);
  const [selectedDataCenter, setSelectedDataCenter] = useState(null);

  const openModal = (action) => {
    console.log('Opening modal:', action); // Debug log
    setActiveModal(action);
  };
  const closeModal = () => setActiveModal(null);

  const renderModals = () => (
    <>
      <Suspense fallback={<div>Loading...</div>}>
        {activeModal === 'create' && (
          <DataCenterModal            
            dcId={selectedDataCenter?.id}
            onClose={closeModal}
          />
        )}
        {activeModal === 'edit' && (
          <DataCenterModal
            editMode
            dcId={selectedDataCenter?.id}
            onClose={closeModal}
          />
        )}
        {activeModal === 'delete' && (
          <DataCenterDeleteModal
            data={selectedDataCenter}
            onClose={closeModal}
          />
        )}
      </Suspense>
    </>
  );
  
  const handleNameClick = (id) => {
    navigate(`/computing/datacenters/${id}/clusters`);
  };

  return (
    <>
      <DataCenterActionButtons
        openModal={openModal}
      />
      <span>ID: {selectedDataCenter?.id}</span>

      <TablesOuter
        columns={TableColumnsInfo.DATACENTERS}
        data={(datacenters || [])}
        onRowClick={(selectedRow) => setSelectedDataCenter(selectedRow)}
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
