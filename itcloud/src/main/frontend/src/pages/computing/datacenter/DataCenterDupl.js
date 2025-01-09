import React, { Suspense, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import TablesOuter from '../../../components/table/TablesOuter';
import DataCenterActionButtons from './button/DataCenterActionButtons';
import { renderDataCenterStatus, renderDatacenterStatusIcon } from '../../../utils/format';

const DataCenterModal = React.lazy(() => import('./modal/DataCenterModal'));
const DataCenterDeleteModal = React.lazy(() => import('./modal/DataCenterDeleteModal'));

const DataCenterDupl = ({ datacenters = [], columns = [] }) => {
  const navigate = useNavigate();
  
  const [activeModal, setActiveModal] = useState(null);
  const [selectedDataCenters, setSelectedDataCenters] = useState([]);
  const selectedIds = (Array.isArray(selectedDataCenters) ? selectedDataCenters : []).map(dc => dc.id).join(', ');

  const handleNameClick = (id) => navigate(`/computing/datacenters/${id}/clusters`);

  const openModal = (action) => setActiveModal(action);
  const closeModal = () => setActiveModal(null);

  //버튼 활성화 조건
  const status = selectedDataCenters.length === 0 ? 'none': selectedDataCenters.length === 1 ? 'single': 'multiple';

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
      <span>ID: {selectedIds}</span>

      <TablesOuter
        columns={columns}
        data={datacenters.map((dc) => ({
          ...dc,
          // name: <TableRowClick type="datacenter" id={dc?.id}>
          //   {dc?.name}
          // </TableRowClick>,
          icon: renderDatacenterStatusIcon(dc?.status),
          status: renderDataCenterStatus(dc?.status),
          storageType: dc?.storageType ? '로컬' : '공유됨'
        }))}
        onRowClick={(selectedRows) => setSelectedDataCenters(selectedRows)}
        clickableColumnIndex={[1]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
        multiSelect={true}
      />

      {/* 데이터센터 모달창 */}
      { renderModals() }
    </>
  );
};

export default DataCenterDupl;
