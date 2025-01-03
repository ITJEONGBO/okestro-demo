import React, { useState, Suspense } from 'react';
import '../css/Computing.css';
import TableInfo from '../../table/TableInfo';
import { useAllDataCenters } from '../../../api/RQHook';
import TablesOuter from '../../table/TablesOuter';
import { renderDatacenterStatusIcon } from '../../util/format';
import DataCenterActionButtons from '../../button/DataCenterActionButtons';

const DataCenterModal = React.lazy(() => import('../../Modal/DataCenterModal'));
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

const DataCenters = () => {
  const {
    data: datacenters,
  } = useAllDataCenters((e) => ({
    ...e,
  }));

  const [modals, setModals] = useState({ create: false, edit: false, delete: false });
  const [selectedDataCenters, setSelectedDataCenters] = useState([]);

  const toggleModal = (type, isOpen) => {
    setModals((prev) => {
      if (prev[type] === isOpen) return prev; 
      return { ...prev, [type]: isOpen };
    });
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
        columns={TableInfo.DATACENTERS}
        data={(datacenters || []).map((dc) => ({
          ...dc,
          icon: renderDatacenterStatusIcon(dc?.status),
          status: dc?.status === 'UNINITIALIZED' ? '초기화되지 않음' : 'UP',
          storageType: dc?.storageType ? '로컬' : '공유됨',
        }))}
        onRowClick={(selectedRows) => setSelectedDataCenters(selectedRows)}
        clickableColumnIndex={[1]}
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
