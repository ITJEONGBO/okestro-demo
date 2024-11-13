import React, { useEffect, useState, Suspense } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/Computing.css';
import TablesOuter from '../../table/TablesOuter';
import TableInfo from '../../table/TableInfo';
import { useAllDataCenters } from '../../../api/RQHook';
import DataCenterActionButtons from '../../button/DataCenterActionButtons';

const DataCenterModal = React.lazy(() => import('../../Modal/DataCenterModal'));
const DeleteModal = React.lazy(() => import('../../Modal/DeleteModal'));

const DataCenters = () => {
  const {
    data: datacenters,
    status: datacentersStatus,
    isRefetching: isDatacentersRefetching,
    refetch: refetchDatacenters,
    isError: isDatacentersError,
    error: datacentersError,
    isLoading: isDatacentersLoading
  } = useAllDataCenters((e) => ({
    ...e,
    storageType: e?.storageType ? '로컬' : '공유됨',
    status: e?.status === 'UNINITIALIZED' ? '초기화되지 않음' : 'UP'
  }));

  const navigate = useNavigate();
  const [modals, setModals] = useState({ create: false, edit: false, delete: false });
  const [selectedDataCenter, setSelectedDataCenter] = useState(null);
  
  const toggleModal = (type, isOpen) => {
    setModals((prev) => {
      if (prev[type] === isOpen) return prev; // 이미 열려 있으면 무시
      return { ...prev, [type]: isOpen };
    });
  };

  const handleNameClick = (id) => {
    navigate(`/computing/datacenters/${id}/clusters`);
  };

  return (
    <>
       <DataCenterActionButtons
        onCreate={() => toggleModal('create', true)}
        onEdit={() => selectedDataCenter?.id && toggleModal('edit', true)}
        onDelete={() => selectedDataCenter?.id && toggleModal('delete', true)}
        isEditDisabled={!selectedDataCenter?.id}
      />
      <span>id = {selectedDataCenter?.id || ''}</span>

      <TablesOuter
        columns={TableInfo.DATACENTERS}
        data={datacenters}
        shouldHighlight1stCol={true}
        onRowClick={(row) => setSelectedDataCenter(row)}
        clickableColumnIndex={[0]} // "이름" 열의 인덱스 설정
        onClickableColumnClick={(row) => handleNameClick(row.id)}
      />

      {/* 모달 컴포넌트를 사용할 때만 로딩 */}
      <Suspense>
        {(modals.create || (modals.edit && selectedDataCenter)) && (
          <DataCenterModal
            isOpen={modals.create || modals.edit}
            onRequestClose={() => toggleModal(modals.create ? 'create' : 'edit', false)}
            editMode={modals.edit}
            dcId={selectedDataCenter?.id || null}
          />
        )}
        {modals.delete && selectedDataCenter && (
          <DeleteModal
            isOpen={modals.delete}
            type='Datacenter'
            onRequestClose={() => toggleModal('delete', false)}
            contentLabel={'데이터센터'}
            data={selectedDataCenter}
          />
        )}
       </Suspense>
    </>
  );
};

export default DataCenters;