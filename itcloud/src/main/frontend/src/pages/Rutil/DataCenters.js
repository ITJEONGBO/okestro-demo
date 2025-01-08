import React, { useState, Suspense } from 'react';
import { useNavigate } from 'react-router-dom';
import TableColumnsInfo from '../../components/table/TableColumnsInfo';
import { useAllDataCenters } from '../../api/RQHook';
import TablesOuter from '../../components/table/TablesOuter';
import { renderDatacenterStatusIcon } from '../../utils/format';
import DataCenterActionButtons from '../../pages/computing/datacenter/button/DataCenterActionButtons';

const DataCenterModal = React.lazy(() => import('../../pages/computing/datacenter/modal/DataCenterModal'));
const DeleteModal = React.lazy(() => import('../../components/DeleteModal'));

const DataCenters = () => {
  const {
    data: datacenters = [],
  } = useAllDataCenters((e) => ({
    ...e,
  }));
  const navigate = useNavigate();

  const [modals, setModals] = useState({ create: false, edit: false, delete: false });
  const [selectedDataCenters, setSelectedDataCenters] = useState([]);

  const toggleModal = (type, isOpen) => {
    setModals((prev) => {
      if (prev[type] === isOpen) return prev; 
      return { ...prev, [type]: isOpen };
    });
  };

  const handleNameClick = (id) => {
    navigate(`/computing/datacenters/${id}/clusters`);
  };

  const selectedIds = (Array.isArray(selectedDataCenters) ? selectedDataCenters : []).map(dc => dc.id).join(', ');

  return (
    <>
      <DataCenterActionButtons
        onCreate={() => toggleModal('create', true)}
        onEdit={() => selectedDataCenters.length === 1 && toggleModal('edit', true)}
        onDelete={() => selectedDataCenters.length === 1 && toggleModal('delete', true)}
        isEditDisabled={!Array.isArray(selectedDataCenters) || selectedDataCenters.length !== 1}
      />
      <span>선택된 데이터센터 ID: {selectedIds || '선택된 항목이 없습니다.'}</span>
      <TablesOuter
        columns={TableColumnsInfo.DATACENTERS}
        data={(datacenters || []).map((dc) => ({
          ...dc,
          icon: renderDatacenterStatusIcon(dc?.status),
          
          status: dc?.status === 'UNINITIALIZED' ? '초기화되지 않음' : 'UP',
          storageType: dc?.storageType ? '로컬' : '공유됨',
        }))}
        onRowClick={(selectedRows) => setSelectedDataCenters(selectedRows)}
        clickableColumnIndex={[1]}
        onClickableColumnClick={(row) => handleNameClick(row.id)}
        multiSelect={true}
      />

      <Suspense>
        {(modals.create || (modals.edit && Array.isArray(selectedDataCenters) && selectedDataCenters.length === 1)) && (
          <DataCenterModal
            isOpen={modals.create || modals.edit}
            onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
            editMode={modals.edit}
            dcId={Array.isArray(selectedDataCenters) && selectedDataCenters.length === 1 ? selectedDataCenters[0].id : null}
          />
        )}
        {modals.delete && selectedDataCenters.length > 0 && (
          <DeleteModal
            isOpen={modals.delete}
            type='DataCenter'
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'데이터센터'}
            data={selectedDataCenters}
          />
        )}
      </Suspense>
    </>
  );
};

export default DataCenters;
